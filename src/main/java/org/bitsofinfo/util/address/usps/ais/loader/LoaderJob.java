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
package org.bitsofinfo.util.address.usps.ais.loader;

import java.util.List;
import org.bitsofinfo.util.address.usps.ais.processor.USPSDataProcessor;
import org.bitsofinfo.util.io.FileHandle;

/**
 * LoaderJob is to be used for submitting data load
 * jobs to a USPSDataLoader. Callers should call
 * USPSDataLoader.createJob() to obtain an instance of
 * a LoaderJob and then pass it back to the USPSDataLoader
 * when ready to begin processing. 
 * 
 * Callers will want to retain an instance to the LoaderJob
 * instance to be able to monitor its status
 * 
 * @author bitsofinfo.g [at] gmail [dot] com
 *
 */
public interface LoaderJob {

	/**
	 * Return the USPSDataProcessor that the loaded data
	 * was processed by
	 * 
	 * @return
	 */
	public USPSDataProcessor getDataProcessor();
	
	/**
	 * Return the list of the original source data
	 * files that this job was setup to process
	 * @return
	 */
	public List<FileHandle> getSourceData();
	
	/**
	 * The job identifier
	 * 
	 * @return
	 */
	public String getIdentifier();
	
	/**
	 * If the job is complete, regardless of 
	 * fail/success
	 * 
	 * @return
	 */
	public boolean isComplete();
	
	/**
	 * If the job was successful
	 * 
	 * @return
	 */
	public boolean isSuccessful();
	
	/**
	 * Get the job's progress as a float
	 * between 0.0 and 1.0
	 * 
	 * @return
	 */
	public float getProgress();
	
	/**
	 * Return the total records loaded so far
	 * 
	 * @return
	 */
	public long getTotalRecordsLoaded();
	
	/**
	 * Return the USPSDataLoader this job was submitted to
	 * 
	 * @return
	 */
	public USPSDataLoader getDataLoader();
	
	
}
