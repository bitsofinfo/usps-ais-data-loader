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
 * 
 * CopyrightDetailCode represents the possible copyright codes
 * for a USPS records
 * 
 * @author bitsofinfo.g [at] gmail [dot] com
 *
 */
public enum CopyrightDetailCode  {
	A("Alias"),				// City/state alias
	C("Copyright"),			// general copyright header
	D("Detail"),			// for various products
	N("Seasonal"),			// city/state seasonal
	S("Scheme"),			// city/state scheme
	Z("Zone Split");		// city/state zone split
	
	
	private String name;
	
	private CopyrightDetailCode(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

}
