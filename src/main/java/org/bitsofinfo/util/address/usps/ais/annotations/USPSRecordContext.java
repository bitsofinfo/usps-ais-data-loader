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

import org.bitsofinfo.util.address.usps.ais.CopyrightDetailCode;
import org.bitsofinfo.util.address.usps.ais.USPSProductType;


/**
 * USPSRecordContext is an annotation for USPSRecord
 * classes, which marks a particular class
 * as the target record holder for raw USPS
 * data file records (lines) of for specific USPSProductTypes
 * and only for lines that begin with a particular CopyrightDetailCode  
 * 
 * 
 * Usage:
 * @USPSProduct(productTypes={<USPSProductType.X>,...},
 * 				copyrightDetailCode=<CopyrightDetailCode.X>)
 * public class <className> extends USPSRecord
 * 
 * @author bitsofinfo.g [at] gmail [dot] com
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface USPSRecordContext {
	
	/**
	 * The CopyrightDetailCode which identifies the record
	 * 
	 * @return
	 */
	CopyrightDetailCode copyrightDetailCode();
	
	/**
	 * The USPSProductType this record belongs to
	 * 
	 * @return
	 */
	USPSProductType[] productTypes();
	

	
}
