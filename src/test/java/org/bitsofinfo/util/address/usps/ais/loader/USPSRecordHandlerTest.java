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

import java.util.Map;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import org.bitsofinfo.util.address.usps.ais.USPSIdGenerator;
import org.bitsofinfo.util.address.usps.ais.USPSProductType;
import org.bitsofinfo.util.address.usps.ais.USPSRecord;
import org.bitsofinfo.util.address.usps.ais.USPSUtils;
import org.bitsofinfo.util.address.usps.ais.zipplus4.ZipPlus4Detail;
import org.bitsofinfo.util.io.FileHandle;
import org.bitsofinfo.util.io.RecordLoader;

@ContextConfiguration
public class USPSRecordHandlerTest extends AbstractJUnit4SpringContextTests {


	@Autowired
	private RecordLoader recordLoader;
	
	@Autowired
	private USPSUtils uspsUtils;
	
	@Autowired
	private USPSIdGenerator idGenerator;

	@Autowired
	private USPSRecordParser parser;
	
	@Test
	public void testLoad805WithUSPSRecordHandler() {
		try {
			Resource file = applicationContext.getResource("classpath:805.txt");
			FileHandle handle = new FileHandle(file.getURI());
			
			MapBackedRecordHandler handler = new MapBackedRecordHandler(uspsUtils,parser,idGenerator);

			recordLoader.loadRecords(handle, handler, 1, 500000, 
						USPSProductType.ZIP_PLUS_4.getRecordLength());
			
			Map<Long,USPSRecord> results = handler.getResults();
			for (Long recordNum : results.keySet()) {
				ZipPlus4Detail rec = (ZipPlus4Detail)results.get(recordNum);
				if (rec.getStreetName().indexOf("BOG") != -1) {
					int v =1;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			int v= 1;
		}
	}	
}
