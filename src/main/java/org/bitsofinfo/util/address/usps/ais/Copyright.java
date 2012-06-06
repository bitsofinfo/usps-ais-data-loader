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

import javax.persistence.Column;
import javax.persistence.Entity;

import org.bitsofinfo.util.address.usps.ais.annotations.USPSDataFileField;
import org.bitsofinfo.util.address.usps.ais.annotations.USPSIdentifierField;
import org.bitsofinfo.util.address.usps.ais.annotations.USPSKeyField;
import org.bitsofinfo.util.address.usps.ais.annotations.USPSRecordContext;


/**
 * Copyright is a USPSRecord which contains the copyright detail
 * information for a source USPS raw data file. This type of 
 * record contains meta-data about the data file, such as its date
 * and sequence number information. Each USPSRecord loaded from
 * that same file will have a pointer back to it's containing file's
 * Copyright record.
 *  
 * @author bitsofinfo.g [at] gmail [dot] com
 * @see USPS Address Products PDF (general Copyright database header)
 *
 */
@USPSRecordContext(productTypes={USPSProductType.ZIP_PLUS_4, USPSProductType.CITY_STATE},
 			copyrightDetailCode=CopyrightDetailCode.C)
@Entity
public class Copyright extends USPSRecord {

	@USPSIdentifierField
	@USPSKeyField
	@USPSDataFileField(start=20,length=2)
	@Column(length=2)
	private String fileVersionMonth;
	
	@USPSIdentifierField
	@USPSKeyField
	@USPSDataFileField(start=23,length=2)
	@Column(length=2)
	private String fileVersionYear;
	
	@USPSIdentifierField
	@USPSKeyField
	@USPSDataFileField(start=31,length=3)
	@Column(length=3)
	private String volumeSequenceNumber;
	

	/**
	 * @return the fileVersionMonth
	 */
	public String getFileVersionMonth() {
		return fileVersionMonth;
	}
	/**
	 * @param fileVersionMonth the fileVersionMonth to set
	 */
	public void setFileVersionMonth(String fileVersionMonth) {
		this.fileVersionMonth = fileVersionMonth;
	}
	/**
	 * @return the fileVersionYear
	 */
	public String getFileVersionYear() {
		return fileVersionYear;
	}
	/**
	 * @param fileVersionYear the fileVersionYear to set
	 */
	public void setFileVersionYear(String fileVersionYear) {
		this.fileVersionYear = fileVersionYear;
	}
	/**
	 * @return the volumeSequenceNumber
	 */
	public String getVolumeSequenceNumber() {
		return volumeSequenceNumber;
	}
	/**
	 * @param volumeSequenceNumber the volumeSequenceNumber to set
	 */
	public void setVolumeSequenceNumber(String volumeSequenceNumber) {
		this.volumeSequenceNumber = volumeSequenceNumber;
	}
	
	/**
	 * @return the copyright
	 */
	@Override
	public Copyright getCopyright() {
		return this;
	}


	/**
	 * @param copyright the copyright to set
	 */
	@Override
	public void setCopyright(Copyright copyright) {
		// I do nothing
	}
	

}
