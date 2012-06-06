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
package org.bitsofinfo.util.address.usps.ais.citystate;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.bitsofinfo.util.address.usps.ais.CopyrightDetailCode;
import org.bitsofinfo.util.address.usps.ais.CopyrightedUSPSRecord;
import org.bitsofinfo.util.address.usps.ais.USPSProductType;
import org.bitsofinfo.util.address.usps.ais.annotations.USPSDataFileField;
import org.bitsofinfo.util.address.usps.ais.annotations.USPSIdentifierField;
import org.bitsofinfo.util.address.usps.ais.annotations.USPSKeyField;
import org.bitsofinfo.util.address.usps.ais.annotations.USPSRecordContext;


/**
 * CityStateScheme represents a USPS 5 digit zip scheme record
 * as defined in the USPS AIS City/State (scheme)
 * product
 * 
 * @author bitsofinfo.g [at] gmail [dot] com
 * @see USPS Address Products PDF (Scheme records) (page 18)
 *  
 */
@USPSRecordContext(productTypes={USPSProductType.CITY_STATE},
	 	copyrightDetailCode=CopyrightDetailCode.S)
@Entity
public class CityStateScheme extends CopyrightedUSPSRecord {

	@USPSIdentifierField
	@USPSKeyField
	@USPSDataFileField(start=2,length=5)
	@Column(length=5)
	private String labelZipCode;
	
	@USPSIdentifierField
	@USPSKeyField
	@USPSDataFileField(start=7,length=5)
	@Column(length=5)
	private String combinedZipCode;

	/**
	 * @return the labelZipCode
	 */
	public String getLabelZipCode() {
		return labelZipCode;
	}

	/**
	 * @param labelZipCode the labelZipCode to set
	 */
	public void setLabelZipCode(String labelZipCode) {
		this.labelZipCode = labelZipCode;
	}

	/**
	 * @return the combinedZipCode
	 */
	public String getCombinedZipCode() {
		return combinedZipCode;
	}

	/**
	 * @param combinedZipCode the combinedZipCode to set
	 */
	public void setCombinedZipCode(String combinedZipCode) {
		this.combinedZipCode = combinedZipCode;
	}
	

}
