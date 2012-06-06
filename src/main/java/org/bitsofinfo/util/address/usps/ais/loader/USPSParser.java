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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;

import org.bitsofinfo.util.address.usps.ais.Copyright;
import org.bitsofinfo.util.address.usps.ais.CopyrightDetailCode;
import org.bitsofinfo.util.address.usps.ais.USPSProductType;
import org.bitsofinfo.util.address.usps.ais.USPSRecord;
import org.bitsofinfo.util.address.usps.ais.USPSUtils;
import org.bitsofinfo.util.reflection.ClassFinder;

/**
 * USPSParser is a thread-safe utility class which loads
 * raw records from USPS product data files, into USPSRecord
 * objects.
 * 
 * @author bitsofinfo.g [at] gmail [dot] com
 *
 */
public class USPSParser {
	
	@Autowired
	private ClassFinder classFinder;
	
	@Autowired
	private USPSUtils uspsUtils;
	
	@Autowired
	private USPSRecordParser lineParser;
	
	private USPSRecord getNewUSPSRecord(USPSProductType type, String dataLine) throws Exception {
		Map<CopyrightDetailCode,Class<? extends USPSRecord>> map = 
			uspsUtils.getTargetUSPSRecordClasses(type);
		
		CopyrightDetailCode cdc = CopyrightDetailCode.valueOf(dataLine.substring(0,1));
		Class<? extends USPSRecord> targetClass = map.get(cdc);
		USPSRecord record = targetClass.newInstance();
		return record;		
	}
	
	/**
	 * Given a String raw copyright record line from a USPS data file, this
	 * will return a new Copyright object containing that copyright line's 
	 * details
	 * 
	 * @param copyrightLine
	 * @return Copyright object that contains the data
	 * @throws Exception should a parse error occur
	 */
	public Copyright parseCopyright(String copyrightLine) throws Exception {
		Copyright copyright = new Copyright();
		lineParser.applyRawRecord(copyrightLine, copyright);
		return copyright;
		
	}
	
	/**
	 * Parses a list of raw String lines from a USPS data file
	 * for the given USPSProductType and Copyright header
	 * into the appropriate USPSRecord objects.
	 *
	 * 
	 * @param productType	The USPSProductType that the raw string lines are from
	 * @param copyright		The Copyright of the parent data-file which the passed lines came from
	 * @param lines			List of raw String lines from a USPS data file
	 * @return	List of USPSRecord objects, one per line that was parsed
	 * 
	 * @throws Exception if any error occurs during the parsing of this set of lines.
	 */
	public List<USPSRecord> parseData(USPSProductType productType, Copyright copyright, List<String> lines) throws Exception {
		
		ArrayList<USPSRecord> records = new ArrayList<USPSRecord>();

		for (String line : lines) {
			USPSRecord record = getNewUSPSRecord(productType,line);
			try {
				lineParser.applyRawRecord(line, record);
				record.setCopyright(copyright);
				records.add(record);
			} catch(RecordParseException e) {
				throw e;
			}
		}

		return records;
		
		
	}

}
