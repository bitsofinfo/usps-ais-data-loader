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
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import org.bitsofinfo.util.address.usps.ais.USPSIdGenerator;
import org.bitsofinfo.util.address.usps.ais.loader.USPSParser;
import org.bitsofinfo.util.address.usps.ais.store.USPSDataStore;
import org.bitsofinfo.util.io.RecordLoader;

@ContextConfiguration
public class HBaseStoreTest extends AbstractJUnit4SpringContextTests {

	@Autowired
	private USPSDataStore uspsDataStore;
	

	@Autowired
	private USPSIdGenerator uspsIdGenerator;

	@Autowired
	private RecordLoader recordLoader;
	
	@Autowired 
	private USPSParser parser;

	@Test
	public void testHBaseStore() throws Exception {
		/*
		try {
			
			Resource file = applicationContext.getResource("classpath:zipinfo.com.usps.zipPlus4.sample.txt");
			FileHandle handle = new FileHandle(file.getURI());
			// no CR/LF present so straight 182 chars
			List<String> copyrightLine = recordLoader.loadRecords(handle, 1, 1, USPSProductType.ZIP_PLUS_4.getRecordLength());
			List<String> lines = recordLoader.loadRecords(handle, 2, 10000, USPSProductType.ZIP_PLUS_4.getRecordLength());// get max of 10K records
			Copyright copyright = parser.parseCopyright(copyrightLine.get(0));
			copyright.setIdentifier(uspsIdGenerator.generateId(copyright));
			
			
			List<USPSRecord> records = parser.parseData(USPSProductType.ZIP_PLUS_4, copyright, lines);
			
			long start = System.currentTimeMillis();
			
			// for each record save it in the USPSDataStore
			for (USPSRecord r : records) {
				r.setIdentifier(uspsIdGenerator.generateId(r));
				uspsDataStore.saveRecord(r);
			}
			
			System.out.println("SAVE TIME: "+ (System.currentTimeMillis() - start));
			
			
			start = System.currentTimeMillis();
			
			// now lets get em
			for (USPSRecord r : records) {
				ZipPlus4Detail orig = (ZipPlus4Detail)r;
				ZipPlus4Detail fetched = (ZipPlus4Detail)uspsDataStore.getByIdentifier(r.getIdentifier());
				assert(fetched != null);
				assert(fetched.getCopyrightDetailCode().equals(r.getCopyrightDetailCode()));
				assert(fetched.getActionCode() == orig.getActionCode());
			}
			
			System.out.println("READ TIME: "+ (System.currentTimeMillis() - start));
			
			
			start = System.currentTimeMillis();
			
			// lets test deleting
			for (USPSRecord r : records) {
				ZipPlus4Detail orig = (ZipPlus4Detail)r;
				uspsDataStore.deleteRecord(orig.getIdentifier());
				//assert(null == uspsDataStore.getByIdentifier(orig.getIdentifier()));
			}
			
			System.out.println("DELETE TIME: "+ (System.currentTimeMillis() - start));

			uspsDataStore.purgeEntireStore();
			
			
		} catch (Exception e) {
			e.printStackTrace();
			int v= 1;
		}
		 */
	}
}
