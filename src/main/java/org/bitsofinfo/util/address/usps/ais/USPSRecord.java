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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import org.bitsofinfo.util.address.usps.ais.annotations.USPSDataFileField;

/**
 * USPSRecord is the base class for all USPS object types
 * that are defined in the AIS product family. All USPSRecords
 * have a Copyright property (noting the copyright of the source
 * data set it originated from) as
 * well as a CopyrightDetailCode which is an classifier for
 * a given record.<BR><BR>
 * 
 * All USPSRecord properties which correspond to a given field
 * in a USPS raw data row, should be annotated with @DataFileField
 * notations to map those members to locations within a raw USPS
 * data file row.<BR><BR>
 * 
 * All USPSRecords have a unique "identifier" which is a String
 * identifier unique to the USPSRecord. This identifier is universally
 * unique.
 * 
 * @see 	USPS AIS documentatin
 * @author bitsofinfo.g [at] gmail [dot] com
 *
 */
@MappedSuperclass
public abstract class USPSRecord {
	
	@Id
	@Column(length=37)
	private String identifier;
	
	@USPSDataFileField(start=1,length=1)
	@Column(length=1)
	@Enumerated(EnumType.STRING)
	private CopyrightDetailCode copyrightDetailCode;
	
	/**
	 * Return the unique identifier for this record
	 * 
	 * 
	 * @return
	 */
	public String getIdentifier() {
		return this.identifier;
	}
	
	/**
	 * Set the identifier
	 *  
	 */
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	/**
	 * @return the copyrightDetailCode
	 */
	public CopyrightDetailCode getCopyrightDetailCode() {
		return copyrightDetailCode;
	}


	/**
	 * @param copyrightDetailCode the copyrightDetailCode to set
	 */
	public void setCopyrightDetailCode(CopyrightDetailCode copyrightDetailCode) {
		this.copyrightDetailCode = copyrightDetailCode;
	}


	/**
	 * @return the copyright
	 */
	public abstract Copyright getCopyright();


	/**
	 * @param copyright the copyright to set
	 */
	public abstract void setCopyright(Copyright copyright);
	
	/**
	 * Return the Action Code for this record.
	 * Note that only some USPSRecord data files
	 * define an action code, however we promote
	 * the getter here which by default returns
	 * 
	 * ActionCode.A (add)
	 * 
	 * @return ActionCode.A (add)
	 */
	public ActionCode getActionCode() {
		return ActionCode.A;
	}
}
