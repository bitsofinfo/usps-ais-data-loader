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

import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

/**
 * CopyrightedUSPSRecord is an extension of USPSRecord
 * for all record types other than Copyright itself.
 * 
 * Here we implement the get/set Copyright methods
 * to store a reference to the Copright record for
 * this source data record
 * 
 * @see 	USPS AIS documentatin
 * @author bitsofinfo.g [at] gmail [dot] com
 *
 */
@MappedSuperclass
public abstract class CopyrightedUSPSRecord extends USPSRecord {

	@OneToOne
	@JoinColumn(name="copyright_id")
	private Copyright copyright;

	/**
	 * @return the copyright
	 */
	@Override
	public Copyright getCopyright() {
		return copyright;
	}


	/**
	 * @param copyright the copyright to set
	 */
	@Override
	public void setCopyright(Copyright copyright) {
		this.copyright = copyright;
	}
	

}
