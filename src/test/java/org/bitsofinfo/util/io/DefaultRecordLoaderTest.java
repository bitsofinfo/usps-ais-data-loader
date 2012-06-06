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
package org.bitsofinfo.util.io;

import java.util.List;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import org.bitsofinfo.util.address.usps.ais.Copyright;
import org.bitsofinfo.util.address.usps.ais.USPSProductType;
import org.bitsofinfo.util.address.usps.ais.USPSRecord;
import org.bitsofinfo.util.address.usps.ais.loader.USPSParser;

@ContextConfiguration
public class DefaultRecordLoaderTest extends AbstractJUnit4SpringContextTests {


	@Autowired
	private RecordLoader recordLoader;
	
	@Autowired USPSParser parser;

	
	@Test
	public void testRecordLoaderWithUSPSSample() {
		try {
			Resource file = applicationContext.getResource("classpath:usps.ais.zipPlus4.sample.txt");
			FileHandle handle = new FileHandle(file.getURI());
			// has CR/LF, so 182+1 chars
			List<String> line = recordLoader.loadRecords(handle, 7, 10, 182+1);
			int v = 1;
		} catch (Exception e) {
			e.printStackTrace();
			int v= 1;
		}
	}	
	
	@Test
	public void testRecordLoaderWithZipInfoSample() {
		try {
			Resource file = applicationContext.getResource("classpath:zipinfo.com.usps.zipPlus4.sample.txt");
			FileHandle handle = new FileHandle(file.getURI());
			// no CR/LF present so straight 182 chars
			List<String> copyrightLine = recordLoader.loadRecords(handle, 1, 1, 182);
			List<String> lines = recordLoader.loadRecords(handle, 2, 1002, 182);
			Copyright copyright = parser.parseCopyright(copyrightLine.get(0));
			List<USPSRecord> records = parser.parseData(USPSProductType.ZIP_PLUS_4, copyright, lines);
			int v = 1;
		} catch (Exception e) {
			e.printStackTrace();
			int v= 1;
		}
	}	
	
	@Test
	public void testRecordLoaderWithZipInfoSampleGetAll() {
		try {
			Resource file = applicationContext.getResource("classpath:zipinfo.com.usps.zipPlus4.sample.txt");
			FileHandle handle = new FileHandle(file.getURI());
			// no CR/LF present so straight 182 chars
			List<String> copyrightLine = recordLoader.loadRecords(handle, 1, 1, 182);
			List<String> lines = recordLoader.loadRecords(handle, 2, 10000, 182); // get all
			Copyright copyright = parser.parseCopyright(copyrightLine.get(0));
			List<USPSRecord> records = parser.parseData(USPSProductType.ZIP_PLUS_4, copyright, lines);
			assert(records.size() == 5074);
		} catch (Exception e) {
			e.printStackTrace();
			int v= 1;
		}
	}

}
