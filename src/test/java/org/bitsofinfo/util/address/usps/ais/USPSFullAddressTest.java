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
package org.bitsofinfo.util.address.usps.ais;

import java.util.List;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import org.bitsofinfo.util.address.usps.ais.Copyright;
import org.bitsofinfo.util.address.usps.ais.USPSFullAddressService;
import org.bitsofinfo.util.address.usps.ais.USPSProductType;
import org.bitsofinfo.util.address.usps.ais.USPSRecord;
import org.bitsofinfo.util.address.usps.ais.loader.USPSParser;
import org.bitsofinfo.util.address.usps.ais.zipplus4.ZipPlus4Detail;
import org.bitsofinfo.util.reflection.ClassFinder;

@ContextConfiguration
public class USPSFullAddressTest extends AbstractJUnit4SpringContextTests {

	@Autowired
	private ClassFinder classFinder;
	
	@Autowired
	private USPSParser uspsParser;
	
	@Autowired
	private USPSFullAddressService service;

	
	private List<String> loadLinesFromFile(String filename) throws Exception {
		Resource file = applicationContext.getResource("classpath:" + filename);
		List<String> lines = FileUtils.readLines(file.getFile());
		return lines;
	}
	
	
	@Test
	public void testZipPlus4DetailConvert() throws Exception {
		
		// locate the raw source data 
		List<String> lines = loadLinesFromFile("usps.ais.zipPlus4.sample.txt");
		
		assert(lines.size() == 40);
		
		// get the copyright
		Copyright copyright = uspsParser.parseCopyright(lines.remove(0));
		
		// load up all the data
		List<USPSRecord> records = uspsParser.parseData(USPSProductType.ZIP_PLUS_4, copyright, lines);
		
		assert(records.size() == 39);
		
		
		for (USPSRecord record : records) {
			if (record instanceof ZipPlus4Detail) {
				service.explode((ZipPlus4Detail)record);
			}
		}
		

	}
}
