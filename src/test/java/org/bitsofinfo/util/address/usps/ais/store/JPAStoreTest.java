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
package org.bitsofinfo.util.address.usps.ais.store;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import org.bitsofinfo.util.address.usps.ais.USPSIdGenerator;
import org.bitsofinfo.util.address.usps.ais.loader.USPSParser;
import org.bitsofinfo.util.address.usps.ais.store.USPSDataStore;
import org.bitsofinfo.util.io.RecordLoader;

@Transactional
@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration
public class JPAStoreTest extends AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private PlatformTransactionManager transactionManager;
	
	@Autowired
	private USPSIdGenerator uspsIdGenerator;

	@Autowired
	private RecordLoader recordLoader;
	
	@Autowired 
	private USPSParser parser;
	
	@Autowired
	private USPSDataStore dataStore;

	@Test
	public void testJPAStore() throws Exception {
		/*
		try {
			
 		Resource file = applicationContext.getResource("classpath:805.txt");
			FileHandle handle = new FileHandle(file.getURI());
			// no CR/LF present so straight 182 chars
			List<String> copyrightLine = recordLoader.loadRecords(handle, 1, 1, USPSProductType.ZIP_PLUS_4.getRecordLength());
			List<String> lines = recordLoader.loadRecords(handle, 2, 500000, USPSProductType.ZIP_PLUS_4.getRecordLength());// get max of 10K records
			Copyright copyright = parser.parseCopyright(copyrightLine.get(0));
			copyright.setIdentifier(uspsIdGenerator.generateId(copyright));
			
			
			TransactionStatus trans = transactionManager.getTransaction(null);
			
			List<USPSRecord> records = parser.parseData(USPSProductType.ZIP_PLUS_4, copyright, lines);
			
			long start = System.currentTimeMillis();
			
			// for each record save it in the USPSDataStore
			long count = 0;
			for (USPSRecord r : records) {
				r.setIdentifier(uspsIdGenerator.generateId(r));
				dataStore.saveRecord(r);
				count++;
				
				if (count == 1000) {
					transactionManager.commit(trans);
					trans = transactionManager.getTransaction(null);
					count = 0;
				}
			}
			
			System.out.println("SAVE TIME: "+ (System.currentTimeMillis() - start));
			
			dataStore.purgeEntireStore();
			
		} catch (Exception e) {
			e.printStackTrace();
			int v= 1;
		}*/
		
	}
}
