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


/**
 * RecordParseExceptions are thrown by the USPSRecordParser suite
 * of methods for parsing raw line data from USPS raw data files
 * 
 * @author bitsofinfo.g [at] gmail [dot] com
 *
 */
public class RecordParseException extends Exception {
	
	private String message;
	
	private Exception cause;
	
	private String sourceData;
	
	public RecordParseException(String message, String sourceData, Exception cause) {
		this.message = message;
		this.cause = cause;
		this.sourceData = sourceData;
	}

	/**
	 * The informational exception message
	 * 
	 * @return the message
	 */
	@Override
	public String getMessage() {
		return message;
	}

	/**
	 * The base exception which triggered this error
	 * 
	 * @return the cause
	 */
	@Override
	public Exception getCause() {
		return cause;
	}

	/**
	 * The raw line of source data which was being parsed
	 * 
	 * @return the sourceData
	 */
	public String getSourceData() {
		return sourceData;
	}


}
