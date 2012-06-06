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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.bitsofinfo.util.address.usps.ais.USPSProductType;
import org.bitsofinfo.util.address.usps.ais.loader.USPSDataLoader;
import org.bitsofinfo.util.io.FileHandle;
import org.bitsofinfo.util.io.RecordHandler;
import org.bitsofinfo.util.io.RecordLoader;

/**
 * JobProcessor is created/managed by LocalDataLoader. JobProcessor
 * processes the load/storage of one or more source USPS source
 * data files. JobProcessor spawns X threads (configured in LocalDataLoader)
 * and divides the workload among them. JobProcessor spawns off multiple
 * SourceDataProcessors (inner class), each one is run under one thread.
 * 
 * @author bitsofinfo.g [at] gmail [dot] com
 *
 */
public class JobProcessor {

	// reference to the LoaderJob itself
	private LocalLoaderJob loaderJob = null;
	
	// the max threads, default 2
	private int maxThreads = 2;
	
	// the total number of SourceDataProcessors completed
	private int completeCount = 0;
	
	// the entire set of sourceData we need to process
	private List<FileHandle> sourceData = null;
	
	// list of SourceDataProcessor that are doing the actual work
	private List<SourceDataProcessor> processors = new ArrayList<SourceDataProcessor>();
	
	// reference back to the LocalDataLoader that created us
	private LocalDataLoader dataLoader = null;
	
	/**
	 * Constructor
	 * 
	 * @param maxThreads
	 * @param job
	 * @param dataLoader
	 */
	public JobProcessor(int maxThreads, LocalLoaderJob job, LocalDataLoader dataLoader) {
		this.maxThreads = maxThreads;
		this.loaderJob = job;
		this.sourceData = job.getSourceData();
		this.dataLoader = dataLoader;
		
		// figure out how many source data files we give each processor
		int sourcesPerProcessor = (int)Math.ceil((double)sourceData.size() / (double)maxThreads);
		
		// obtain iterator 
		Iterator<FileHandle> iterator = sourceData.iterator();
		
		// while within our maxThreads property
		// add FileHandles of source data to individual
		// SourceDataProcessor instances and fire them off
		for (int i=0; i<maxThreads;i++) {
			List<FileHandle> sources = new ArrayList<FileHandle>();
			for (int j=0;j<sourcesPerProcessor;j++) {
				FileHandle tmp = null;
				try {
					tmp = iterator.next();
				} catch(NoSuchElementException e) {
					// ignore
					break;
				}
				
				sources.add(tmp);
			}
			
			// start a processor and track it
			SourceDataProcessor sdp = new SourceDataProcessor(this,sources);
			processors.add(sdp);
		}
	}
	
	protected LocalLoaderJob getLoaderJob() {
		return this.loaderJob;
	}

	// record a RecordLoader that the SourceDataProcessor should use
	protected RecordLoader getRecordLoader() {
		return dataLoader.getRecordLoader();
	}
	
	// record a RecordHandler that the SourceDataProcessor should use
	protected RecordHandler getRecordHandler() {
		return dataLoader.getRecordHandler(loaderJob.getDataProcessor());
	}
	
	// start all processors
	protected void start() {
		for (SourceDataProcessor sdp : processors) {
			sdp.start();
		}
	}
	
	// we are complete when our complete count = total processors
	protected boolean isComplete() {
		return this.completeCount == processors.size();
	}
	
	// mark a SDP as complete
	protected void setCompleted(SourceDataProcessor sdp) {
		this.completeCount++;
	}
	
	// if we encountered any errors during processing
	protected boolean hasErrors() {
		for (SourceDataProcessor sdp : processors) {
			if (sdp.hasErrors) {
				return true;
			}
		}
		return false;
	}
	
	// return aggregated results from all processors 
	protected Map<FileHandle,String> getResults() {
		HashMap<FileHandle,String> tmp = new HashMap<FileHandle,String>();
		for (SourceDataProcessor sdp : processors) {
			Map<FileHandle,String> rslts = sdp.getResults();
			for (FileHandle key : rslts.keySet()) {
				tmp.put(key, rslts.get(key));
			}
		}
		
		return tmp;
	}
	
	
	/**
	 * SourceDataProcessor is a runnable that processes a 
	 * unique set of USPS source data FileHandles
	 * 
	 * @author bitsofinfo.g [at] gmail [dot] com
	 *
	 */
	private class SourceDataProcessor implements Runnable {

		// my thread
		private Thread myThread = null;
		
		// my set of source data
		private List<FileHandle> sourceData = null;
		
		// reference back to the job processor
		private JobProcessor parent = null;
		
		// if I am finished
		private boolean finished = false;
		
		// per FileHandle results
		private HashMap<FileHandle,String> results = new HashMap<FileHandle,String>();
		
		// if we had errors during the load
		private boolean hasErrors = false;
		
		/**
		 * Constructor
		 * 
		 * @param parent
		 * @param sourceData
		 */
		protected SourceDataProcessor(JobProcessor parent, List<FileHandle> sourceData) {
			this.sourceData = sourceData;
			this.parent = parent;
			this.myThread = new Thread(this,"SourceDataProcessor(total:"+sourceData.size()+")");
		}
		
		// get results
		protected Map<FileHandle,String> getResults() {
			return results;
		}
		
		protected boolean hasErrors() {
			return hasErrors;
		}
		
		// start me up
		protected void start() {
			this.myThread.start();
		}
		
		// here is where the work occurs
		@Override
		public void run() {
			while(!finished) {
				
				// for each of our file handles....
				for (FileHandle handle : sourceData) {
					try {
						// get the IO record loader and the record handler we should be using...
						RecordLoader recordLoader = parent.getRecordLoader();
						RecordHandler recordHandler = parent.getRecordHandler();
						
						// get the USPSProductType bound to the FileHandle
						USPSProductType type = (USPSProductType)handle.getProperty(USPSDataLoader.FILE_HANDLE_PROP_USPS_PRODUCT_TYPE);
						
						if (type == null) {
							results.put(handle, "No USPSProductType custom property set for FileHandle, cannot process it. " +
									"You must set the USPSDataLoader.FILE_HANDLE_PROP_USPS_PRODUCT_TYPE property for the FileHandle via FileHandle.setProperty()");
							hasErrors = true;
							
						} else {
							try {
								recordLoader.loadRecords(handle, recordHandler, 1, Integer.MAX_VALUE, type.getRecordLength());
								results.put(handle, "Successfully loaded");
								
							} catch(Exception e) {
								results.put(handle, "Exception caught while RecordLoader was processing the file: " + e.getMessage());
								hasErrors = true;
							}
						}

					} catch(Exception e) {
						results.put(handle, "Unexpected Exception caught while preparing to load data: " + e.getMessage());
						hasErrors = true;
					}
				}
				
				finished = true;
				parent.setCompleted(this);
			}
		}
		
	}
	
}
