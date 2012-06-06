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
package org.bitsofinfo.util.address.usps.ais.processor;

import java.util.List;

import org.bitsofinfo.util.address.usps.ais.Copyright;
import org.bitsofinfo.util.address.usps.ais.USPSRecord;

/**
 * USPSDataProcessor is a generic interface which defines a contract
 * for the generic processing of USPSRecord objects. 
 * 
 * Implementations can do whatever they want with the records.
 * 
 * @author bitsofinfo.g [at] gmail [dot] com
 *
 */
public interface USPSDataProcessor {

	/** 
	 * Process a List of USPSRecords.
	 *
	 * @param records		The set of USPS record to process
	 * @param copyright		The USPS Copyright which these records are bound to
	 * @throws ProcessException
	 */
	public void processRecords(List<USPSRecord> records, Copyright copyright) 
		throws ProcessException; 

	/**
	 * Return the filename of the application context XML
	 * required for Hadoop mappers to configure and send
	 * data to this processor
	 * 
	 * @return String filename with class package path pre-pended
	 */
	public String getHadoopMapperAppContextXMLFilename();
	
	/**
	 * Control if hadoop mapper jobs should use speculative 
	 * execution when this processor is the target of their output.
	 * 
	 * Typically FALSE for RDBMS, vs big-tables which are TRUE
	 * 
	 * @return true, false if hadoop should use speculative exec
	 */
	public boolean permitHadoopSpeculativeExecution();
		
}
