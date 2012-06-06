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

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import org.bitsofinfo.util.address.usps.ais.USPSProductType;
import org.bitsofinfo.util.address.usps.ais.processor.DataProcessorRecordHandler;
import org.bitsofinfo.util.address.usps.ais.source.Zip4SourceDataExtractor;
import org.bitsofinfo.util.io.FileHandle;
import org.bitsofinfo.util.io.RecordLoader;

@ContextConfiguration
public class Zip4NationalSourceReaderTest extends AbstractJUnit4SpringContextTests {

	@Autowired
	private Zip4SourceDataExtractor reader;
	
	@Autowired
	private RecordLoader recordLoader;
	
	
	@Autowired
	private DataProcessorRecordHandler recordHandler;
	
	@Test
	public void testReader() throws Exception {
		ArrayList<File> sourceDirs = new ArrayList<File>();
		sourceDirs.add(new File("/Volumes/ZIP4NATL__11/ctystate"));
		sourceDirs.add(new File("/Volumes/ZIP4NATL__11/zip4"));
		sourceDirs.add(new File("/Volumes/ZIP4NATL__11/z4trans"));
		List<FileHandle> handles = reader.extractSourceData(sourceDirs, new File("/tmp"));
		
		
		for (FileHandle handle : handles) {
			
			if (handle.getUri().toString().indexOf("ctystate") != -1) {
			//	recordLoader.loadRecords(handle, recordHandler, 2, Integer.MAX_VALUE, 
			//			USPSProductType.CITY_STATE.getRecordLength());
			} else {
				recordLoader.loadRecords(handle, recordHandler, 2, Integer.MAX_VALUE, 
					USPSProductType.ZIP_PLUS_4.getRecordLength());
			}
		}
		
		
	}
	
}
