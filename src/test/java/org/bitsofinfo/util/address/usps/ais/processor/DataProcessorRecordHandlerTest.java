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

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.transaction.PlatformTransactionManager;
import org.bitsofinfo.util.address.usps.ais.USPSProductType;
import org.bitsofinfo.util.address.usps.ais.USPSUtils;
import org.bitsofinfo.util.address.usps.ais.loader.USPSRecordParser;
import org.bitsofinfo.util.address.usps.ais.processor.DataProcessorRecordHandler;
import org.bitsofinfo.util.address.usps.ais.store.USPSDataStore;
import org.bitsofinfo.util.io.FileHandle;
import org.bitsofinfo.util.io.RecordLoader;

@ContextConfiguration
public class DataProcessorRecordHandlerTest extends AbstractJUnit4SpringContextTests {


	@Autowired
	private RecordLoader recordLoader;
	
	@Autowired
	private USPSUtils uspsUtils;
	
	@Autowired
	private USPSRecordParser parser;

	@Autowired
	private USPSDataStore dataStore;
	
	@Autowired
	private DataProcessorRecordHandler recordHandler;
	
	@Autowired
	private PlatformTransactionManager transactionManager;
	
	
	@Test
	public void testLoad805WithDataProcessorRecordHandler() {
		try {
			
			Resource file = applicationContext.getResource("classpath:805.txt");
			FileHandle handle = new FileHandle(file.getURI());

			recordLoader.loadRecords(handle, recordHandler, 2, 500000, 
						USPSProductType.ZIP_PLUS_4.getRecordLength());
			
// should be 60971
			
		//	dataStore.purgeEntireStore();
			
		} catch (Exception e) {
			e.printStackTrace();
			int v= 1;
		}
	}	
}
