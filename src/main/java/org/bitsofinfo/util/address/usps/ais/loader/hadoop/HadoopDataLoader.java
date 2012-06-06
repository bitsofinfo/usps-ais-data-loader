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
package org.bitsofinfo.util.address.usps.ais.loader.hadoop;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.CounterGroup;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FixedLengthInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

import org.bitsofinfo.util.address.usps.ais.USPSProductType;
import org.bitsofinfo.util.address.usps.ais.loader.LoaderJob;
import org.bitsofinfo.util.address.usps.ais.loader.USPSDataLoader;
import org.bitsofinfo.util.address.usps.ais.processor.USPSDataProcessor;
import org.bitsofinfo.util.io.FileHandle;
import org.bitsofinfo.util.io.URIScheme;


/**
 * HadoopDataLoader processes a LoaderJob against using Hadoop MapReduce
 * over an X node cluster. All processed data is saved into the 
 * USPSDataProcessor configured in the LoaderJob.
 * 
 * @author bitsofinfo.g [at] gmail [dot] com
 *
 */
public class HadoopDataLoader implements USPSDataLoader, ApplicationContextAware, Runnable {
	
	private static final String MAPPER_COUNTER_GROUP_NAME_PREFIX = "ProcessedRecords-";

	private ApplicationContext appContext;

	private String hadoopConfFilename = "hadoop-localhost.xml";
	
	private String hdfsSourceDataRootDir = "/uspsDataLoader/jobSourceData";
	
	private String hdfsJobOutputRootDir = "/uspsDataLoader/jobOutputData";
	
	private String hdfsMapperJarPath = null;
	
	private String hdfsMapperJarFilename = null;
	
	private Resource hadoopConfResource = null;
	
	private Hashtable<String,LoaderJobTracker> runningJobs = new Hashtable<String, LoaderJobTracker>();
	
	private Thread monitorThread = null;

	public void init() throws Exception {
		// no conf?
		if (this.hadoopConfFilename == null) {
			throw new Exception("Cannot init(), HadoopDataLoader requires the hadoopConfFilename property to be set!");
		}
		
		// no jar?
		if (this.hdfsMapperJarPath == null) {
			throw new Exception("Cannot init(), HadoopDataLoader requires the HDFS location of the mapper support JAR be set [hdfsMapperJarPath]!");
		}
		
		// no hdfs source data root dir?
		if (this.hdfsSourceDataRootDir == null) {
			throw new Exception("Cannot init(), HadoopDataLoader requires the HDFS root folder that will contain source files we copy in from jobs [hdfsSourceDataRootDir]!");
		}
		
		// no hdfs otput data root dir?
		if (this.hdfsJobOutputRootDir == null) {
			throw new Exception("Cannot init(), HadoopDataLoader requires the HDFS root folder that will contain output data from our jobs [hdfsJobOutputRootDir]!");
		}
		
		// no hdfs otput data root dir?
		if (this.hdfsMapperJarFilename == null) {
			throw new Exception("Cannot init(), HadoopDataLoader requires the HDFS hdfsMapperJarFilename to be set [hdfsMapperJarFilename]!");
		}
		
		
		// get the resource for our hadoop conf
		hadoopConfResource = appContext.getResource("classpath:"+hadoopConfFilename);

		// setup our monitor thread
		this.monitorThread = new Thread(this,"HadoopDataLoader job tracker monitor");
		this.monitorThread.start();
		
		// ensure hdfs dirs exists
		Configuration conf = new Configuration();
		conf.addResource(hadoopConfResource.getURL());
		FileSystem hdfs = FileSystem.get(conf);
		hdfs.mkdirs(new Path(this.hdfsJobOutputRootDir));
		hdfs.mkdirs(new Path(this.hdfsMapperJarPath));
		hdfs.mkdirs(new Path(this.hdfsSourceDataRootDir));
		
		if (!hdfs.exists(new Path(this.hdfsMapperJarPath + "/" + hdfsMapperJarFilename))) {
			throw new Exception("hdfsMapperJarFilename does not exists @ " + this.hdfsMapperJarPath + "/" + hdfsMapperJarFilename);
		}
		 
	}



	@Override
	public LoaderJob createJob(String identifier, List<FileHandle> sourceData, USPSDataProcessor targetProcessor) {
		HadoopLoaderJob job = 
			new HadoopLoaderJob(identifier,sourceData,targetProcessor,this);
		return job;
	}
	
	@Override
	public void load(LoaderJob theJob) throws Exception {
		
		// big error
		if (!(theJob instanceof HadoopLoaderJob)) {
			throw new Exception("Cannot pass non-HadoopLoaderJob instance to HadoopDataLoader! Get your job" +
					" via the createJob() factory method on a HadoopDataLoader instance");
		}
		
		HadoopLoaderJob loaderJob = (HadoopLoaderJob)theJob;
		
		// create our internal job tracker which will actually run the job in hadoop
		LoaderJobTracker jobTracker = new LoaderJobTracker(loaderJob, hadoopConfResource, hdfsJobOutputRootDir);

		// get the file system, so we can push the source files into HDFS
		Configuration conf = new Configuration();
		conf.addResource(hadoopConfResource.getURL());
		FileSystem hdfs = FileSystem.get(conf);

		// for each source data file, lets get it into HDFS		
		for (FileHandle sourceData : loaderJob.getSourceData()) {
			String sourceURI = sourceData.getUri().toString();
			
			// we currently only support local files
			if (sourceURI.indexOf(URIScheme.FILE.name()) != -1) {
				throw new Exception("HadoopDataLoader currently only supports source data URIs that are from the local file system (file:.//)");
			}
			
			// get the product type of this source file, we need this to determine the record length
			USPSProductType uspsProdType = (USPSProductType)sourceData.getProperty(FILE_HANDLE_PROP_USPS_PRODUCT_TYPE);
			if (uspsProdType == null) {
				throw new Exception("Source data FileHandles passed to HadoopDataLoader must have the FILE_HANDLE_PROP_USPS_PRODUCT_TYPE property set.");
			}
			
			// ok, lets make sure to copy the local file into the HDFS filesystem
			// the target path in HDFS is /[hdfsSourceDataRootDir]/loader_job-[id]/[local source URI with file:// stripped]
			Path sourceFileLocalPath = new Path(new File(sourceData.getUri()).getAbsolutePath());
			String localPathStripped = sourceData.getUri().toString();
			localPathStripped = StringUtils.replace(localPathStripped, URIScheme.FILE.getName()+":/", "");
			Path sourceFileTargetHDFSPath = new Path(this.hdfsSourceDataRootDir +"/loader_job-"+loaderJob.getIdentifier()+"/"+localPathStripped);
			
			// copy..if not already there
			if (!hdfs.exists(sourceFileTargetHDFSPath)) {
				hdfs.copyFromLocalFile(false, true, sourceFileLocalPath, sourceFileTargetHDFSPath);
			}
			
			// add the source file in HDFS to the job tracker, keyed by the USPSProductType
			jobTracker.addSourceDataFile(uspsProdType,sourceFileTargetHDFSPath);


		}
		
		
		// have the jobTracker fire off the job
		jobTracker.startHadoopJob();
		
		// add to running jobs
		this.runningJobs.put(loaderJob.getIdentifier(), jobTracker);
		
		// make sure to wake it
		synchronized(this) {
			notify();
		}

	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.appContext = applicationContext;
		
	}
	
	/**
	 * This class wraps a Hadoop Job that proceses
	 * our LoaderJob and tracks the job on the Hadoop cluster
	 * 
	 * @author bitsofinfo.g [at] gmail [dot] com
	 *
	 */
	private class LoaderJobTracker {
		
		// the source job...
		protected HadoopLoaderJob loaderJob = null;
		
		// reference to the hadoop conf
		protected Resource hadoopConfFile = null;
		
		// where job output is written 
		protected String outputRootDir = null;
		
		// Map that contains all source files for this job, organized by the USPSProductType
		protected HashMap<USPSProductType,List<Path>> sourceDataMap = new HashMap<USPSProductType,List<Path>>();
		
		// the actual hadoop MR jobs we monitor
		private List<Job> trackedJobs = new ArrayList<Job>();
		
		protected LoaderJobTracker(HadoopLoaderJob loaderJob, Resource hadoopConfFile, String outputRootDir) {
			this.loaderJob = loaderJob;
			this.hadoopConfFile = hadoopConfFile;
			this.outputRootDir = outputRootDir;
		}
		
		protected void addSourceDataFile(USPSProductType uspsProdType, Path hdfsSourceDataFile) {
			List<Path> pathsForType = sourceDataMap.get(uspsProdType);
			if (pathsForType == null) {
				pathsForType = new ArrayList<Path>();
				sourceDataMap.put(uspsProdType, pathsForType);
			}
			
			pathsForType.add(hdfsSourceDataFile);
		}
		
		protected void startHadoopJob() throws Exception {
			
			// for each USPSProductType we have files for, we will configure 
			// a separate Hadoop MR job for it
			for (USPSProductType uspsType : this.sourceDataMap.keySet()) {
				
				// hdfs paths of input files (source data)
				List<Path> hdfsSourcePaths = this.sourceDataMap.get(uspsType);
				
				// get our conf and fire up a new mapreduce/hadoop configuration
				Configuration conf = new Configuration();
				conf.addResource(hadoopConfFile.getURL());
				
				// get the data store target
				USPSDataProcessor targetProcessor = this.loaderJob.getDataProcessor();
				
				// set MAPPER config properties...
				conf.set(USPSDataFileMapper.MAPPER_APP_CONTEXT_XML, targetProcessor.getHadoopMapperAppContextXMLFilename());
				conf.setInt(USPSDataFileMapper.MAPPER_RECORD_LENGTH, uspsType.getRecordLength());
				conf.setInt(USPSDataFileMapper.MAPPER_DATA_PROCESSOR_BATCH_SIZE, 20000);
				conf.setBoolean("mapred.map.tasks.speculative.execution", targetProcessor.permitHadoopSpeculativeExecution());
				
				// ensure the our hadoop JAR which contains all classes required for our 
				// this data loader is in the distributed cache.
				org.apache.hadoop.mapreduce.filecache.DistributedCache.addFileToClassPath(new Path(hdfsMapperJarPath + "/" + hdfsMapperJarFilename), conf);	
				
				// configure, the correct record length for the files for this type...
				conf.setInt(FixedLengthInputFormat.FIXED_RECORD_LENGTH, uspsType.getRecordLength());
				
				// setup the hadoop MR job
				Job job = new Job(conf);
				job.setJarByClass(USPSDataFileMapper.class);
				job.setMapperClass(USPSDataFileMapper.class);	
				job.setInputFormatClass(FixedLengthInputFormat.class);
				
				// for each input file, add the path to the job
				for (Path inputFile : hdfsSourcePaths) {
					FileInputFormat.addInputPath(job, inputFile);
				}

				// where output is written
				FileOutputFormat.setOutputPath(job, new Path(outputRootDir + "/loader_job-" + loaderJob.getIdentifier() 
						+ "-" + uspsType.getId()+ System.currentTimeMillis()+ "/"));
				
				// submit the job to hadoop!
				try {
					job.submit();
				} catch(Exception e) {
					throw new Exception("Error submitting Job to Hadoop... " +e.getMessage());
				}
				
				// add it to our tracking queue 
				this.trackedJobs.add(job);
			}
		
		}
		
		// For progress, we return the LEAST progress
		// of all Job's map phase...
		public void updateProgress() throws Exception {
			float progress = 0;
			for (Job j : trackedJobs) {
				float mapProgress = j.mapProgress();
				
				if (progress == 0.0) {
					progress = mapProgress;
					
				} else if (progress > mapProgress) {
					progress = mapProgress;
				}
			}
			
			this.loaderJob.setProgress(progress);
			
			// update the total records processed so far
			for (Job j :trackedJobs) {
				CounterGroup cgroup = j.getCounters().getGroup(USPSDataFileMapper.USPS_COUNTERS_GROUP_NAME);
				long total = cgroup.findCounter(USPSDataFileMapper.OVERALL_RECORDS_PROCESSED_COUNTER).getValue();
				this.loaderJob.setTotalRecordsLoaded(total);
			}
			
		}
		
		public boolean isComplete() throws Exception {
			// iterate over to see if complete
			for (Job j : trackedJobs) {
				if (!j.isComplete()) {
					return false;
				}
			}
			return true;
		}
		
		// CANNOT CALL THIS UNTIL isComplete() == true
		public boolean wasSuccessful() throws Exception {
			// iterate over to see if complete
			for (Job j : trackedJobs) {
				if (!j.isSuccessful()) {
					return false;
				}
			}
			return true;
		}
	}

	/**
	 * Here we monitor job status every 30 seconds
	 */
	@Override
	public void run() {
		while(true) {
			try {
				for (String id : this.runningJobs.keySet()) {
					LoaderJobTracker tracker = this.runningJobs.get(id);
					
					// if the tracker is complete
					if (tracker.isComplete()) {
						
						// construct the loader results
						tracker.updateProgress();
						
						// set completed and optionally success 
						tracker.loaderJob.setCompleted(true);
						if (tracker.wasSuccessful()) {
							tracker.loaderJob.setSuccessful(true);
						}
						
						// remove it
						this.runningJobs.remove(tracker);
						
					} else {
						// update progress
						tracker.updateProgress();
						
					}
				}
				
				if (this.runningJobs.size() == 0) {
					synchronized(this) {
						wait();
					}
				} else {
					try {
						Thread.currentThread();
						Thread.sleep(30000);
					} catch(Exception e) {
						throw new RuntimeException("Error in HadoopDataLoader, monitoring thread!");
					}
				}
			} catch(Exception e) {
				throw new RuntimeException("Error in HadoopDataLoader monitor thread! " +e.getMessage());
			}
		}
		
	}

	public String getHadoopConfFilename() {
		return hadoopConfFilename;
	}



	public void setHadoopConfFilename(String hadoopConfFilename) {
		this.hadoopConfFilename = hadoopConfFilename;
	}



	public String getHdfsSourceDataRootDir() {
		return hdfsSourceDataRootDir;
	}



	public void setHdfsSourceDataRootDir(String hdfsSourceDataRootDir) {
		this.hdfsSourceDataRootDir = hdfsSourceDataRootDir;
	}



	public String getHdfsJobOutputRootDir() {
		return hdfsJobOutputRootDir;
	}



	public void setHdfsJobOutputRootDir(String hdfsJobOutputRootDir) {
		this.hdfsJobOutputRootDir = hdfsJobOutputRootDir;
	}



	public String getHdfsMapperJarPath() {
		return hdfsMapperJarPath;
	}



	public void setHdfsMapperJarPath(String hdfsMapperJarPath) {
		this.hdfsMapperJarPath = hdfsMapperJarPath;
	}



	public String getHdfsMapperJarFilename() {
		return hdfsMapperJarFilename;
	}



	public void setHdfsMapperJarFilename(String hdfsMapperJarFilename) {
		this.hdfsMapperJarFilename = hdfsMapperJarFilename;
	}

}
