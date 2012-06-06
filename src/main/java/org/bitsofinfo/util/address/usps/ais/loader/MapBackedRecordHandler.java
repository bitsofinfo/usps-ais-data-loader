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

import java.util.Hashtable;
import java.util.Map;

import org.bitsofinfo.util.address.usps.ais.Copyright;
import org.bitsofinfo.util.address.usps.ais.USPSIdGenerator;
import org.bitsofinfo.util.address.usps.ais.USPSRecord;
import org.bitsofinfo.util.address.usps.ais.USPSUtils;

/**
 * A USPSRecordHandler which simply stores the records in an internal Map
 * 
 * @author bitsofinfo.g [at] gmail [dot] com
 *
 */
public class MapBackedRecordHandler extends USPSRecordHandler {

	public MapBackedRecordHandler(USPSUtils uspsUtils, USPSRecordParser parser, USPSIdGenerator idGenerator) {
		super(uspsUtils, parser, idGenerator);
	}

	private Map<Long,USPSRecord> recordMap = new Hashtable<Long,USPSRecord>();
	private long totalRecordsHandled = 0;

	@Override
	protected void processRecord(Long recordNum, USPSRecord record, Copyright copyright) throws Exception {
		record.setCopyright(copyright);
		recordMap.put(recordNum, record);
		totalRecordsHandled++;
	}
	
	@Override
	public long getTotalRecordsHandled() {
		return this.totalRecordsHandled;
	}
 

	public Map<Long,USPSRecord> getResults() {
		return recordMap;
	}
	
	@Override
	protected void handleFinished() throws Exception {
		// nothing
	}



}
