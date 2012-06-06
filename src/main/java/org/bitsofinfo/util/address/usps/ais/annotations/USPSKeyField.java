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
 * USPSKeyField is an annotation for USPSRecord
 * properties, which marks a particular property/field
 * as being an important key field, who's value may 
 * be used as a part of an identifier, or used in an index
 * for the record etc
 * 
 * Usage:
 * USPSKeyField
 * private String somePropertyInAUSPSRecord
 * 
 * @author bitsofinfo.g [at] gmail [dot] com
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface USPSKeyField {
	 
}
