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
package org.bitsofinfo.util.address.usps.ais.processor;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.bitsofinfo.util.address.usps.ais.Copyright;
import org.bitsofinfo.util.address.usps.ais.USPSIdGenerator;
import org.bitsofinfo.util.address.usps.ais.USPSRecord;
import org.bitsofinfo.util.address.usps.ais.USPSUtils;
import org.bitsofinfo.util.address.usps.ais.loader.USPSRecordHandler;
import org.bitsofinfo.util.address.usps.ais.loader.USPSRecordParser;

/**
 * DataProcessorRecordHandler is a USPSRecordHandler which
 * queues up each raw record into a queue of records
 * which once it reaches the "batchSize" they will be 
 * saved against the configured USPSDataProcessor.
 * 
 * @author bitsofinfo.g [at] gmail [dot] com
 *
 */
public class DataProcessorRecordHandler extends USPSRecordHandler {

	@Autowired
	private USPSDataProcessor dataProcessor;
	
	private long batchSize;
	private List<USPSRecord> batch = new ArrayList<USPSRecord>();
	private long totalRecordsHandled = 0;
	private Copyright currentCopyright = null;
	
	public DataProcessorRecordHandler() {
		super();
	}

	/**
	 * Constructor
	 * 
	 * @param batchSize	max number of USPSRecords to queue up before saving
	 * @param uspsUtils	reference to the USPSUtils
	 * @param parser	reference to the USPSRecordParser
	 * @param dataStore	the target USPSDataProcessor that converted records will be saved in
	 * @param idGenerator	the IDGenerator
	 */
	public DataProcessorRecordHandler(long batchSize, 
								  USPSUtils uspsUtils, 
								  USPSRecordParser parser,
								  USPSDataProcessor dataProcessor,
								  USPSIdGenerator idGenerator) {

		super(uspsUtils, parser, idGenerator);
		
		
		this.batchSize = batchSize;
		this.dataProcessor =dataProcessor;
		
	}

	public void setBatchSize(long batchSize) {
		this.batchSize = batchSize;
	}
	
	@Override
	public long getTotalRecordsHandled() {
		return this.totalRecordsHandled;
	}
	
	@Override
	protected void processRecord(Long recordNumber, USPSRecord record, Copyright copyright) throws Exception {

		try {		
			
			// if the copyright has changed.., we need to flush all
			// previous records that were loaded after the currentCopyright
			// before updating our currentCopyright which will be applied
			// to all subsequent records
			if (this.currentCopyright != copyright) {
				saveBatch();
				this.currentCopyright = copyright;
			}
			
			batch.add(record);
			
			if (batch.size() == batchSize) {
				saveBatch();
			}
			
		} catch(Exception e) {
			throw new Exception("Error in saving record: " +e.getMessage());
		}
		
	}
	
	private void saveBatch() throws Exception {
		if (batch.size() > 0) {
			this.dataProcessor.processRecords(batch,currentCopyright);
			batch.clear();
			this.totalRecordsHandled += batchSize;
		}
	}
	
	@Override
	protected void handleFinished() throws Exception {
		if (batch.size() > 0) {
			saveBatch();
		}
	}

}
