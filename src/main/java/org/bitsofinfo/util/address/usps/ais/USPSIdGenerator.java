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
 * USPSIdGenerator defines the interface
 * for USPSRecord UUID generation. 
 * The values returned by this generator
 * are to be applied to the USPSRecord identifier property
 * 
 * @author bitsofinfo.g [at] gmail [dot] com
 *
 */
public interface USPSIdGenerator {
	/**
	 * Generates and returns new ID for the given USPSRecord
	 * 
	 * THIS DOES NOT APPLY/SET THE ID ON THE RECORD!
	 * 
	 * @param record
	 * @return String id that you should set on the record
	 * @throws Exception
	 */
	public String generateId(USPSRecord record) throws Exception;
	
	/**
	 * Given an Identifier, this will return the USPSRecord class
	 * that the identifier is valid for 
	 * 
	 * @param identifier
	 * @return USPSRecord class that the identifier is valid for
	 * @throws Exception
	 */
	public Class<? extends USPSRecord> getRecordType(String identifier) throws Exception;
}
