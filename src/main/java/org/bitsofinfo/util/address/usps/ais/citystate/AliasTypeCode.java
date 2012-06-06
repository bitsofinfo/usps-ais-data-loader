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


/**
 * Represents the set of possible values for the 
 * aliasTypeCode field of a USPS Alias record
 * 
 * @author bitsofinfo.g [at] gmail [dot] com
 *
 */
public enum AliasTypeCode {
	A("Abbreviated"),
	C("Street Name Changed"),
	O("Nickname/Other"),
	P("Preferred Street Name");
	
	
	private String name;
	
	private AliasTypeCode(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
