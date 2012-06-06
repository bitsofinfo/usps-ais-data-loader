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
package org.bitsofinfo.util.address.usps.ais.loader;

import org.apache.commons.beanutils.Converter;

/**
 * GenericEnumConverter is a commons BeanUtils converter
 * which takes and Enum class and a String value. The 
 * converter will return the real Enum value for the given
 * string value for the given Enum class. If no match
 * is found null returned
 * 
 * 
 * @author bitsofinfo.g [at] gmail [dot] com
 *
 */
public class GenericEnumConverter implements Converter {

	@Override
	public Object convert(Class clazz, Object value) {
		
		if (clazz.isEnum() && value instanceof String) {
			String toParse = (String)value;
			if (toParse != null && toParse.trim().length() >0) {
				try {
					return Enum.valueOf(clazz, toParse);
				} catch(Exception e) {
					System.out.println("error parsing..." +toParse);
					return null;
					
				}
			}
		}
		
		return null;
	}

}
