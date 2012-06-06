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
package org.bitsofinfo.util.address.usps.ais.annotations;


import java.lang.annotation.*;


/**
 * DataFileField is an annotation for USPSRecord
 * classes, which binds a particular member property
 * against it's source position within a USPS raw
 * data file row.
 * 
 * Usage:
 * USPSDataFileField(start=<characterStartPositionInRow>,length=<lengthOfFieldvalueInRow>)
 * private String somePropertyInAUSPSRecord
 * 
 * @author bitsofinfo.g [at] gmail [dot] com
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface USPSDataFileField {
	 
	/**
	 * The starting character position of the field
	 * 
	 * @return
	 */
	int start();
	
	/**
	 * The length of the field
	 * 
	 * @return
	 */
	int length();
	
}
