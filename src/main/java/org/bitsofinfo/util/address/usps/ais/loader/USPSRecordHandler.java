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

import org.springframework.beans.factory.annotation.Autowired;

import org.bitsofinfo.util.address.usps.ais.Copyright;
import org.bitsofinfo.util.address.usps.ais.USPSIdGenerator;
import org.bitsofinfo.util.address.usps.ais.USPSRecord;
import org.bitsofinfo.util.address.usps.ais.USPSUtils;
import org.bitsofinfo.util.io.RecordHandler;

/**
 * USPSRecordHandler is a RecordHandler implementation
 * which will apply a given record string against an appropriate
 * USPSRecord instance for the record and then call an internal
 * template method to process the record. USPSRecordHandler
 * will assign the Copyright reference as well as an identifier to
 * each parsed record.
 * 
 * Caller may specify the Copyright to apply to each record by calling
 * setCopyright() OR the handler will determine it automatically using
 * the first record (which is always the copyright header). 
 * 
 * @author bitsofinfo.g [at] gmail [dot] com
 *
 */
public abstract class USPSRecordHandler implements RecordHandler {

	@Autowired
	private USPSUtils uspsUtils;
	
	@Autowired
	private USPSRecordParser parser;
	
	// finished
	private boolean finished = false;
	
	// the copyright to apply to the records. This
	// is updated as we encounter a new.
	private Copyright copyrightToApply = null;
	
	@Autowired
	private USPSIdGenerator uspsIdGenerator;


	public USPSRecordHandler() {};
	
	public USPSRecordHandler(USPSUtils uspsUtils, USPSRecordParser parser, USPSIdGenerator idGenerator) {
		this.uspsUtils = uspsUtils;
		this.parser = parser;
		this.uspsIdGenerator = idGenerator;
	}
	
	@Override
	public void handleRecord(long recordNumber, String record) throws Exception {
		
		if (finished) {
			throw new Exception("Cannot call handleRecord() on finished RecordHandler, please aquire a new instance!");
		}
		
		
		USPSRecord instance = null;
		try {
			Class<? extends USPSRecord> clazz = uspsUtils.getClassForRawRecord(record);
			instance = clazz.newInstance();
		} catch(Exception e) {
			throw new Exception("Error handling raw record ("+record+"), could not locate appropriate class: " +e.getMessage());
		}
		
		try {			
			
			// parse the contents into the instance
			parser.applyRawRecord(record, instance);
			
			// set the id
			instance.setIdentifier(uspsIdGenerator.generateId(instance));
			
		} catch(Exception e) {
			throw new Exception("Error translating record to USPSRecord instance:" + e.getMessage());
		}
		
		
		// if a copyright...
		if (instance instanceof Copyright) {
			this.copyrightToApply = (Copyright)instance;
		}

		// process it
		processRecord(recordNumber,instance,copyrightToApply);
	}
	
	public void finish() throws Exception{
		finished = true;
		this.handleFinished();
	}
	
	public abstract long getTotalRecordsHandled();
	
	protected abstract void handleFinished() throws Exception;
	
	protected abstract void processRecord(Long recordNumber, USPSRecord record, Copyright copyright) throws Exception;

}
