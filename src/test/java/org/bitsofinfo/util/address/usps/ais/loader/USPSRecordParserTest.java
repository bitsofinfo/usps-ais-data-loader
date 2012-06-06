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

import java.util.List;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import org.bitsofinfo.util.address.usps.ais.ActionCode;
import org.bitsofinfo.util.address.usps.ais.Copyright;
import org.bitsofinfo.util.address.usps.ais.CopyrightDetailCode;
import org.bitsofinfo.util.address.usps.ais.OddEvenCode;
import org.bitsofinfo.util.address.usps.ais.USPSIdGenerator;
import org.bitsofinfo.util.address.usps.ais.USPSProductType;
import org.bitsofinfo.util.address.usps.ais.USPSRecord;
import org.bitsofinfo.util.address.usps.ais.USPSUtils;
import org.bitsofinfo.util.address.usps.ais.citystate.CityStateAlias;
import org.bitsofinfo.util.address.usps.ais.citystate.CityStateDetail;
import org.bitsofinfo.util.address.usps.ais.citystate.CityStateScheme;
import org.bitsofinfo.util.address.usps.ais.citystate.CityStateSeasonal;
import org.bitsofinfo.util.address.usps.ais.citystate.CityStateZoneSplit;
import org.bitsofinfo.util.address.usps.ais.zipplus4.BaseAlternateCode;
import org.bitsofinfo.util.address.usps.ais.zipplus4.RecordType;
import org.bitsofinfo.util.address.usps.ais.zipplus4.ZipPlus4Detail;
import org.bitsofinfo.util.reflection.ClassFinder;

@ContextConfiguration
public class USPSRecordParserTest extends AbstractJUnit4SpringContextTests {

	@Autowired
	private ClassFinder classFinder;
	
	@Autowired
	private USPSUtils annotationUtils;
	
	@Autowired
	private USPSIdGenerator uspsIdGenerator;
	
	@Autowired
	private USPSRecordParser lineParser;

	
	private List<String> loadLinesFromFile(String filename) throws Exception {
		Resource file = applicationContext.getResource("classpath:" + filename);
		List<String> lines = FileUtils.readLines(file.getFile());
		return lines;
	}
	
	@Test
	public void parseCityStateSample() throws Exception {
		
		// locate the raw source data 
		List<String> lines = loadLinesFromFile("usps.ais.cityState.sample.txt");
		
		assert(lines.size() == 59);
		
		Map<CopyrightDetailCode,Class<? extends USPSRecord>> typeMap = annotationUtils.getTargetUSPSRecordClasses(USPSProductType.CITY_STATE);
		
		// for every line parse a record
		int lineNumber = 1;
		for (String line : lines) {
			
			// get the detail code for the line
			CopyrightDetailCode type = CopyrightDetailCode.valueOf(line.substring(0,1));
			
			// if it is a copyright record, return that class, otherwise a Zip+4 record
			Class clazz = typeMap.get(type);
			
			USPSRecord record = null;
			try {
				// parse it!
				record = (USPSRecord)clazz.newInstance();
				lineParser.applyRawRecord(line, record);
				record.setIdentifier(uspsIdGenerator.generateId(record));
				
				
				
			} catch(Exception e) {
				assert(false);
				System.out.println("EXCEPTION: " + e.getMessage());
			}


			// some random tests
			if (lineNumber == 1) {
				assert(record instanceof Copyright);
				Copyright c = (Copyright)record;
				assert(c.getCopyrightDetailCode() == CopyrightDetailCode.C);
				assert(c.getFileVersionMonth().equals("02"));
				assert(c.getFileVersionYear().equals("05"));
				assert(c.getVolumeSequenceNumber().equals("001"));
				
				
			} else if (lineNumber == 11) {
				assert(record instanceof CityStateAlias);
				
			} else if (lineNumber == 14) {
				assert(record instanceof CityStateDetail);
				
			} else if (lineNumber == 27) {
				assert(record instanceof CityStateSeasonal);
				
			} else if (lineNumber == 38) {
				assert(record instanceof CityStateScheme);
				
			} else if (lineNumber == 59) {
				assert(record instanceof CityStateZoneSplit);
			}
			
			
			lineNumber++;
			
		}
	}
	
	@Test
	public void parseZipPlus4Sample() throws Exception {
		
		// locate the raw source data 
		List<String> lines = loadLinesFromFile("usps.ais.zipPlus4.sample.txt");
		
		assert(lines.size() == 40);
		
		// for every line parse a record
		int lineNumber = 1;
		for (String line : lines) {
			
			// get the detail code for the line
			CopyrightDetailCode type = CopyrightDetailCode.valueOf(line.substring(0,1));
			
			Map<CopyrightDetailCode,Class<? extends USPSRecord>> typeMap = annotationUtils.getTargetUSPSRecordClasses(USPSProductType.ZIP_PLUS_4);
			
			// if it is a copyright record, return that class, otherwise a Zip+4 record
			Class clazz = typeMap.get(type);
			
			USPSRecord record = null;
			try {
				// parse it!
				record = (USPSRecord)clazz.newInstance();
				lineParser.applyRawRecord(line, record);
				record.setIdentifier(uspsIdGenerator.generateId(record));
				
			} catch(Exception e) {
				assert(false);
				System.out.println("EXCEPTION: " + e.getMessage());
			}

			 
			// some random tests
			if (lineNumber == 1) {
				assert(record instanceof Copyright);
				Copyright c = (Copyright)record;
				assert(c.getCopyrightDetailCode() == CopyrightDetailCode.C);
				assert(c.getFileVersionMonth().equals("06"));
				assert(c.getFileVersionYear().equals("00"));
				assert(c.getVolumeSequenceNumber().equals("001"));
				
				
			} else if (lineNumber == 15) {
				assert(record instanceof ZipPlus4Detail);
				ZipPlus4Detail a = (ZipPlus4Detail)record;
				assert(a.getIdentifier().indexOf(a.getUpdateKeyNumber()) != -1);
				assert(a.getZipCode().equals("02601"));
				assert(a.getUpdateKeyNumber().equals("V203814311"));
				assert(a.getActionCode().equals(ActionCode.A));
				assert(a.getRecordType().equals(RecordType.F));
				assert(a.getCarrierRouteId().equals("C011"));
				assert(a.getStreetPreDirectionalAbbr()==null);
				assert(a.getStreetName().equals("BREEDS HILL"));
				assert(a.getStreetSuffixAbbr().equals("RD"));
				assert(a.getStreetPostDirectionalAbbr()==null);
				assert(a.getAddressPrimaryLowNum().equals("0000000110"));
				assert(a.getAddressPrimaryHighNum().equals("0000000110"));
				assert(a.getAddrPrimaryOddEvenCode().equals(OddEvenCode.E));
				assert(a.getBuildingOrFirmName().equals("CELLCOM COMPUTER"));
				assert(a.getAddressSecondaryAbbr().equals("STE"));
				assert(a.getAddressSecondaryLowNum().equals("00000010"));
				assert(a.getAddressSecondaryHighNum().equals("00000010"));
				assert(a.getBaseAlternateCode().equals(BaseAlternateCode.B));
				assert(a.getLacsStatusIndicator() == null);
				assert(a.getGovtBuildingIndicator() == null);
				assert(a.getFinanceNumber().equals("243723"));
				assert(a.getStateAbbr().equals("MA"));
				assert(a.getCountyNumber().equals("001"));
				assert(a.getCongressionalDistrictNumber().equals("10"));
				assert(a.getMuniCityStateKey() == null);
				assert(a.getUrbanCityStateKey() == null);
				assert(a.getPreferredLastLineCityStateKey().equals("V21919"));

				for (String code : a.getFormattedZipPlus4Codes()) {
					assert(code.equals("02601-1885"));
				}
				
			}
			
			
			
			lineNumber++;
			
		}
	}
}
