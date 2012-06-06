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
import java.util.List;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.bitsofinfo.util.address.usps.ais.CopyrightDetailCode;
import org.bitsofinfo.util.address.usps.ais.USPSRecord;
import org.bitsofinfo.util.address.usps.ais.USPSUtils;
import org.bitsofinfo.util.address.usps.ais.annotations.USPSDataFileField;
import org.bitsofinfo.util.address.usps.ais.annotations.USPSRecordContext;

/**
 * USPSRecordParser can take a line of String text (representing a record )
 * from any USPS raw data file and will parse that line into a USPSRecord
 * object with all fields populated according to the binding defined by
 * USPSDataFileField annotations within the given target USPSRecord class.
 * 
 * @author bitsofinfo.g [at] gmail [dot] com
 *
 */
public class USPSRecordParser {

	@Autowired
	private USPSUtils uspsUtils;
	
	
	public void setUspsUtils(USPSUtils uspsUtils) {
		this.uspsUtils = uspsUtils;
	}


	/**
	 * Takes a line of raw USPS data file record data and applies it
	 * to a USPSRecord instance (overwriting any previous data) according
	 * to the USPSDataFileField annotations defined within the USPSRecord
	 * class.
	 * 
	 * @param rawRecord		Raw string of text representing a line from a USPS 
	 * 					raw database record file
	 * 
	 * @param target	The target instance (derivative of USPSRecord) which will
	 * 					be updated with the values parsed from the line of data.
	 * 					This class MUST have its members annotated with USPSDataFileField
	 * 					
	 * @throws RecordParseException on any error
	 */
	public void applyRawRecord(String rawRecord, USPSRecord targetRecord) throws RecordParseException {
		
		Class<? extends USPSRecord> targetClass = targetRecord.getClass();
		
		// 1st lets do a simple check to make sure this record is compatible...
		CopyrightDetailCode lineType = CopyrightDetailCode.valueOf(rawRecord.substring(0, 1));
		USPSRecordContext productInfo = targetClass.getAnnotation(USPSRecordContext.class);
		if (productInfo.copyrightDetailCode() != lineType) {
			throw new RecordParseException("According to @USPSRecordContext annotation, we cannot apply line data " +
					" of type " +lineType.name() + " against " +
					" target USPSRecord of type " + targetClass.getName(),rawRecord,null);
		}
		
		
		// get the list of all Fields known in this class annotated by USPSDataFileField
		List<Field> dataFileFields = uspsUtils.getFieldsByAnnotation(targetClass, USPSDataFileField.class);
		
		
		/* for every member of the target class, located those
		 * annotated with DataFileField and located the data within
		 * the line which matches the markers described in the annotation.
		 * For each value parsed, convert it, then set the value against
		 * the USPSRecord instance.
		 */
		for (Field f : dataFileFields) {

			Object[] parts = USPSDataFileFieldHelper.extractValue(rawRecord,f);
			Object trueValue = parts[1];
			String rawValue = parts[0].toString();

			// attempt to set the property against our target instance
			try {
				PropertyUtils.setSimpleProperty(targetRecord, f.getName(), trueValue);
				
			} catch(Exception e) {
				throw new RecordParseException("Failed when attempting to set value (" +
						"rawValue="+rawValue + " objValue="+(trueValue != null ? (trueValue.toString() + "("+trueValue.getClass().getName()+")") : null) +
						") against " + targetClass.getName() + " field=" + f.getName(),rawRecord,e);
			}

		}
		
	}
	

}
