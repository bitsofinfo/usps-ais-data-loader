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
package org.bitsofinfo.util.address.usps.ais.store;

import java.util.List;

import org.bitsofinfo.util.address.usps.ais.Copyright;
import org.bitsofinfo.util.address.usps.ais.USPSRecord;
import org.bitsofinfo.util.address.usps.ais.processor.USPSDataProcessor;


/**
 * USPSDataStore defines the interface for storing
 * USPSRecord objects. A USPSDataStore supports
 * saving (add/upd), deleting and retrieving USPSRecord
 * objects which are identified by a universally
 * unique record identifier. The general concept 
 * is that a USPSDataStore behaves like a giant
 * Hashtable, where records are added/updated/deleted/retrieved
 * by a unique, univiersally unique identifier. 
 * 
 * <BR><BR>
 * A USPSDataStore is a USPSDataProcessor so it can be the 
 * target of data bulk data loads.
 * <BR><BR>
 * 
 * Implementations of USPSDataStore must NOT permit
 * implementation specific to leak out of this interface
 * (i.e. JPA/hibernate or other storage concepts such 
 * as sessions, detached/attached objects, lazy loading, 
 * record previously exists etc). 
 * 
 * @author bitsofinfo.g [at] gmail [dot] com
 *
 */
public interface USPSDataStore extends USPSDataProcessor {
	
	/** 
	 * Saves batch of USPSRecords into the data store. This method
	 * will internally read the value of USPSRecord.getActionCode() to determine if
	 * an individual record should be added/updated or deleted in the store.
	 * <BR><BR>
	 * 
	 * 
	 * @param record		The USPS record to process
	 * @param copyright		The USPS Copyright which these records are bound to
	 */
	public void processBatchUpdate(List<USPSRecord> records, Copyright copyright) throws StoreException;
	
	/**
	 * Saves a USPSRecord into the data store. This will update
	 * or add the record into the store depending on if it previously
	 * exists
	 * 
	 * @param record
	 */
	public void saveRecord(USPSRecord record) throws StoreException;
	
	/**
	 * Retrieves a USPSRecord by its unique identifier
	 * 
	 * @param identifier
	 * @return
	 */
	public USPSRecord getByIdentifier(String identifier) throws StoreException;
	
	/**
	 * Determine if a USPSRecord exists by the given identifier
	 * 
	 * @param identifier
	 * @return true/false if the record exists
	 * @throws StoreException
	 */
	public boolean exists(String identifier) throws StoreException;
	
	/**
	 * Deletes a USPSRecord by the unique identifier
	 *
	 * @param identifier
	 */
	public void deleteRecord(String identifier) throws StoreException;
	
	/**
	 * Deletes the given USPSRecord from the store
	 * 
	 * @param record
	 */
	public void deleteRecord(USPSRecord record) throws StoreException;
	
	/**
	 * Purges all data from the store
	 * 
	 * @param record
	 */
	public void purgeEntireStore() throws StoreException;
	



}
