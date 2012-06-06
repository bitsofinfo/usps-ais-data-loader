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


/**
 * Defines the possible USPS AIS products
 * 
 * @author bitsofinfo.g [at] gmail [dot] com
 *
 */
public enum USPSProductType {
	CITY_STATE("City State Product","C",129),
	ZIP_PLUS_4("Zip+4 Product","Z",182);
	
	
	private String id;
	private String name; 
	private int recordLength;
	
	public static USPSProductType getTypeForRawRecord(int recordLength) {
		int len = recordLength;
		if (len == CITY_STATE.recordLength || (len+1) == CITY_STATE.recordLength) {
			return CITY_STATE;
		} else if (len == ZIP_PLUS_4.recordLength || (len+1) == ZIP_PLUS_4.recordLength) {
			return ZIP_PLUS_4;
		}
		return null;
	}
	
	public static USPSProductType getTypeForRawRecord(String record) {
		return getTypeForRawRecord(record.length());
	}

	private USPSProductType(String name,String id,int recordLength) {
		this.name = name;
		this.id = id;
		this.recordLength = recordLength;
	}
	
	public String getName() {
		return name;
	}

	
	public String getId() {
		return id; 
	}

	public int getRecordLength() {
		return recordLength;
	}
	
	
	
}
