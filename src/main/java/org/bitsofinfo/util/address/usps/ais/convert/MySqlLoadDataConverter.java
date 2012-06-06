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
package org.bitsofinfo.util.address.usps.ais.convert;

import java.lang.reflect.Field;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.bitsofinfo.util.address.usps.ais.USPSRecord;
import org.bitsofinfo.util.address.usps.ais.USPSUtils;
import org.bitsofinfo.util.address.usps.ais.annotations.USPSDataFileField;
import org.bitsofinfo.util.address.usps.ais.loader.USPSDataFileFieldHelper;

public class MySqlLoadDataConverter implements USPSRawRecordConverter {


	@Autowired
	private USPSUtils uspsUtils;
	
	public void setUspsUtils(USPSUtils uspsUtils) {
		this.uspsUtils = uspsUtils;
	}

	
	@Override
	public String convert(String rawRecord) throws Exception {
		Class<? extends USPSRecord> clazz = uspsUtils.getClassForRawRecord(rawRecord);
		
		// get the list of all Fields known in this class annotated by USPSDataFileField
		List<Field> dataFileFields = uspsUtils.getFieldsByAnnotation(clazz, USPSDataFileField.class);
		
		StringBuffer sb = new StringBuffer("@S@");
		
		for (Field f : dataFileFields) {
			Object[] parts = USPSDataFileFieldHelper.extractValue(rawRecord,f);
			String rawValue = parts[0].toString();
			sb.append(rawValue.trim()+"|");
		}
		
		return sb.toString().substring(0,sb.length()-1);
		
	}

}
