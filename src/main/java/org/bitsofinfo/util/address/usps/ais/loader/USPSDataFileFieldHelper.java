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

import java.lang.reflect.Field;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang.StringUtils;

import org.bitsofinfo.util.address.usps.ais.annotations.USPSDataFileField;

public class USPSDataFileFieldHelper {

	public static Object[] extractValue(String rawRecord, Field f) throws RecordParseException {
		USPSDataFileField ann =f.getAnnotation(USPSDataFileField.class);
		
		// DataFileField.starts are all 1 based, so adjust back 1
		int start = ann.start()-1;
		
		// get the target field name 
		String fieldName = f.getName();
		
		String rawValue = null;
		try {
			// extract the value
			rawValue = rawRecord.substring(start, start+ann.length());
			
		} catch (IndexOutOfBoundsException e) {
			throw new RecordParseException("Could not extract data for field: " +
						fieldName + ". USPSDataFileField annotation specifys combination" +
						" of start="+start +" length="+ann.length() + " which is" +
					    " out of bounds for the raw data line being parsed..",
					    rawRecord,e);
		}
		
		// convert blanks into nulls
		if (StringUtils.isBlank(rawValue)) {
			rawValue = null;
		}
		
		// the target member's data type 
		Class fieldClass = f.getType();

		// use bean utils to do any necessary conversion on the raw value
		Object trueValue = ConvertUtils.convert(rawValue, fieldClass);
		
		// trim it
		if (trueValue instanceof String) {
			trueValue = ((String)trueValue).trim();
		}
		
		Object[] toReturn = new Object[2];
		toReturn[0] = rawValue;
		toReturn[1] = trueValue;
		return toReturn;
		
	}
	
}
