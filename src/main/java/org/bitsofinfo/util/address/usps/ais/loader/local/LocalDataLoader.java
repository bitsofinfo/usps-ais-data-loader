/*****************************************************************************
* Copyright 2012  bitsofinfo.g [at] gmail [dot] com 
* 
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
* 
* http://www.apache.org/licenses/LICENSE-2.0 
* 
* Unless required by applicable law or agreed to in writing, software 
* distributed under the License is distributed on an "AS IS" BASIS, 
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and 
* limitations under the License
* 
* Author: bitsofinfo.g [at] gmail [dot] com 
* @see bitsofinfo.wordpress.com
*****************************************************************************/
package org.bitsofinfo.util.address.usps.ais.loader.local;

import java.util.Hashtable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.bitsofinfo.util.address.usps.ais.USPSIdGenerator;
import org.bitsofinfo.util.address.usps.ais.USPSProductType;
import org.bitsofinfo.util.address.usps.ais.USPSUtils;
import org.bitsofinfo.util.address.usps.ais.loader.LoaderJob;
import org.bitsofinfo.util.address.usps.ais.loader.USPSDataLoader;
import org.bitsofinfo.util.address.usps.ais.loader.USPSRecordParser;
import org.bitsofinfo.util.address.usps.ais.processor.DataProcessorRecordHandler;
import org.bitsofinfo.util.address.usps.ais.processor.USPSDataProcessor;
import org.bitsofinfo.util.io.FileHandle;
import org.bitsofinfo.util.io.RecordHandler;
import org.bitsofinfo.util.io.RecordLoader;

/**
 * LocalDataLoader is a USPSDataLoader which runs
 * in a single JVM, local to one machine. 
 * 
 * @author bitsofinfo.g [at] gmail [dot] com
 *
 */
public class LocalDataLoader implements USPSDataLoader, Runnable {
	
	// total threads per job
	private int threadsPerJob = 2;
	
	// map of running processors
	private Hashtable<String,JobProcessor> runningJobs = new Hashtable<String, JobProcessor>();
	
	// the batch size for saves to the processor
	private int processorBatchSize = 10000;
	
	// the IO record loader that reads data from raw files
	@Autowired
	private RecordLoader recordLoader;
	
	// our utils class
	@Autowired
	private USPSUtils uspsUtils;
	
	// the id generator
	@Autowired
	private USPSIdGenerator idGenerator;
	
	// the end parser that turns records into objects
	@Autowired
	private USPSRecordParser recordParser;

	// my monitor thread
	private Thread monitorThread = new Thread(this,"LocalDataLoader job monitor");
	
	/**
	 * The number of threads allocated for each job
	 * submitted
	 * 
	 * @return int
	 */
	public int getThreadsPerJob() {
		return threadsPerJob;
	}

	/**
	 * Set the max number of threads allocated for each
	 * submitted job
	 * @param threadsPerJob
	 */
	public void setThreadsPerJob(int threadsPerJob) {
		this.threadsPerJob = threadsPerJob;
	}
	
	/**
	 * Total number of records to commit to the 
	 * USPSDataProcessor at a time
	 * 
	 * @return int
	 */
	public int getProcessorBatchSize() {
		return this.processorBatchSize;
	}

	/**
	 * Set the total number of records to send to the 
	 * USPSDataProcessor at a time
	 * 
	 * @param int total
	 */
	public void setProcessorPatchSize(int processorBatchSize) {
		this.processorBatchSize = processorBatchSize;
	}

	@Override
	public LoaderJob createJob(String identifier, List<FileHandle> sourceData,
			USPSDataProcessor targetProcessor) {
		LocalLoaderJob job = new LocalLoaderJob(identifier,sourceData,targetProcessor,this);
		return job;
	}

	@Override
	public void load(LoaderJob job) throws Exception {
		
		
		// big error
		if (!(job instanceof LocalLoaderJob)) {
			throw new Exception("Cannot pass non-HadoopLoaderJob instance to LocalDataLoader! Get your job" +
					" via the createJob() factory method on a LocalLoaderJob instance");
		}
		
		// verify the source data FileHandles are configured properly
		for (FileHandle sourceData : job.getSourceData()) {
			USPSProductType uspsProdType = (USPSProductType)sourceData.getProperty(FILE_HANDLE_PROP_USPS_PRODUCT_TYPE);
			if (uspsProdType == null) {
				throw new Exception("Source data FileHandles passed to LocalDataLoader must have the FILE_HANDLE_PROP_USPS_PRODUCT_TYPE property set.");
			}
		}
		
		// create a new job processor
		JobProcessor jp = new JobProcessor(threadsPerJob,(LocalLoaderJob)job,this);
		jp.start();
		
		// add it to our tracking
		runningJobs.put(job.getIdentifier(),jp);
		
		// wake our monitor thread up
		synchronized(this) {
			notify();
		}

	}
	
	/**
	 * Return the record loader to be used by JobProcessors
	 * 
	 * @return RecordLoader
	 */
	protected RecordLoader getRecordLoader() {
		return recordLoader;
	}
	
	/**
	 * Return the RecordHandler to be used by JobProcessors 
	 * 
	 * @param targetProcessor
	 * @return RecordHandler
	 */
	protected RecordHandler getRecordHandler(USPSDataProcessor targetProcessor) {
		return new DataProcessorRecordHandler(processorBatchSize,uspsUtils,recordParser,targetProcessor,idGenerator);
	}

	// here we monitor all active JobProcessors
	@Override
	public void run() {
		while(true) {
			try {
				for (JobProcessor job : runningJobs.values()) {
					LocalLoaderJob endJob = job.getLoaderJob();
					
					if (job.isComplete()) {
						// remove from list of running jobs
						this.runningJobs.remove(endJob.getIdentifier());
						
						endJob.setCompleted(true);
						if (job.hasErrors()) {
							endJob.setSuccessful(false);
						}
					} else {
						endJob.setCompleted(false);
					}
				}
				
				if (runningJobs.size() > 0) {
					Thread.currentThread();
					// sleep for 30 seconds
					Thread.sleep(30000);
				} else {
					synchronized(this) {
						this.wait();
					}
				}
				
			} catch(Exception e) {
				//??
			}
		}
		
	}


}
