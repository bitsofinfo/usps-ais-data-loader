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
package org.bitsofinfo.util.address.usps.ais.search;

import java.util.List;
import java.util.Map;

import org.bitsofinfo.util.address.usps.ais.USPSRecord;

public interface USPSSearchService {

	/**
	 * Retrieves a USPSRecord by its unique identifier
	 * 
	 * @param identifier
	 * @return
	 */
	public USPSRecord getByIdentifier(String identifier) throws Exception;
	
	
	/**
	 * Retrieves a List of USPSRecord by a simple property = value
	 * comparison
	 * 
	 * @param type			The USPSRecord class you are looking for
	 * @param propertyName  The USPSRecord POJO member/property name 
	 * @param value			The value to find against
	 * @return	List of USPSRecord objects which match the criteria
	 * @throws Exception
	 */
	public List<USPSRecord> getBy(Class<? extends USPSRecord> type, 
										String propertyName, 
										Object value) throws Exception;
	
	/**
	 * Retrieves a List of USPSRecord by a set of property/value
	 * critiera
	 * 
	 * @param type			The USPSRecord class you are looking for
	 * @param propValueMap	Map of USPSRecord property names ->values which will
	 * 					    be ANDed together to locate matches
	 * @return	List of USPSRecord objects which match the criteria
	 * @throws Exception
	 */
	public List<USPSRecord> getBy(Class<? extends USPSRecord> type, 
							     Map<String,Object> propValueMap) throws Exception;
	
}
