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
 * CarrierRouteRateSortationMerged and 5DigitIndicator 
 * field of a USPS City/state detail record
 * 
 * @author bitsofinfo.g [at] gmail [dot] com
 *
 */
public enum CarrierRouteRateSortationMerged5DigitIndicator {
	A("Carrier Route Sortation Rates Apply - Merging Permitted"),
	B("Carrier Route Sortation Rates Apply - Merging Not Permitted"),
	C("Carrier Route Sortation Rates Do Not Apply - Merging Permitted"),
	D("Carrier Route Sortation Rates Do Not Apply - Merging Not Permitted");
	
	
	private String name;
	
	private CarrierRouteRateSortationMerged5DigitIndicator(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
