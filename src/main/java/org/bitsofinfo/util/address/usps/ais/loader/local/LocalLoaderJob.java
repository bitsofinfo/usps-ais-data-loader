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

import java.util.List;

import org.bitsofinfo.util.address.usps.ais.loader.LoaderJob;
import org.bitsofinfo.util.address.usps.ais.loader.USPSDataLoader;
import org.bitsofinfo.util.address.usps.ais.processor.USPSDataProcessor;
import org.bitsofinfo.util.io.FileHandle;


/**
 * Implementation of LoaderJob which is used for 
 * jobs submitted to the LocalDataLoader
 * 
 * @author bitsofinfo.g [at] gmail [dot] com
 *
 */
public class LocalLoaderJob implements LoaderJob {

	private USPSDataProcessor targetProcessor = null;
	private String identifier;
	private List<FileHandle> sourceData = null;
	private boolean completed = false;
	private boolean successful = false;
	private float progress = 0;
	private long totalRecordsLoaded;

	private USPSDataLoader dataLoader;

	
	protected LocalLoaderJob(String identifier, List<FileHandle> sourceData, USPSDataProcessor processor, LocalDataLoader dataLoader) {
		this.targetProcessor = processor;
		this.identifier = identifier;
		this.sourceData = sourceData;
		this.dataLoader = dataLoader;
	}
	
	@Override
	public USPSDataProcessor getDataProcessor() {
		return targetProcessor;
	}
	
	@Override
	public String getIdentifier() {
		return identifier;
	}


	@Override
	public List<FileHandle> getSourceData() {
		return sourceData;
	}

	@Override
	public boolean isComplete() {
		return completed;
	}

	@Override
	public boolean isSuccessful() {
		return successful;
	}
	
	@Override
	public float getProgress() {
		return progress;
	}
	
	
	protected void setDataProcessor(USPSDataProcessor processor) {
		this.targetProcessor = processor;
	}

	protected void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	protected void setSourceData(List<FileHandle> sourceData) {
		this.sourceData = sourceData;
	}

	protected void setCompleted(boolean completed) {
		this.completed = completed;
	}

	protected void setSuccessful(boolean successful) {
		this.successful = successful;
	}

	protected void setProgress(float progress) {
		this.progress = progress;
	}
	
	
	protected void setTotalRecordsLoaded(long totalRecordsLoaded) {
		this.totalRecordsLoaded = totalRecordsLoaded;
	}

	@Override
	public USPSDataLoader getDataLoader() {
		return dataLoader;
	}

	@Override
	public long getTotalRecordsLoaded() {
		return totalRecordsLoaded;
	}


}
