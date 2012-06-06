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
package org.bitsofinfo.util.address.usps.ais.store.hbase;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.TableExistsException;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import org.bitsofinfo.util.address.usps.ais.ActionCode;
import org.bitsofinfo.util.address.usps.ais.Copyright;
import org.bitsofinfo.util.address.usps.ais.USPSIdGenerator;
import org.bitsofinfo.util.address.usps.ais.USPSRecord;
import org.bitsofinfo.util.address.usps.ais.USPSUtils;
import org.bitsofinfo.util.address.usps.ais.loader.hadoop.USPSDataFileMapper;
import org.bitsofinfo.util.address.usps.ais.processor.ProcessException;
import org.bitsofinfo.util.address.usps.ais.store.StoreException;
import org.bitsofinfo.util.address.usps.ais.store.USPSDataStore;

/**
 * HBaseStore is a USPSDataStore which stores
 * all USPSRecord objects in separate tables
 * (one table per-distinct USPSRecord type)
 * 
 * The database structure looks as follows
 * 
 * [USPSRecord type] ->[ records keyed by identifier] -> RECORD [ "datacols" (family), qualifier per distinct property]
 * 
 * @author bitsofinfo.g [at] gmail [dot] com
 *
 */
public class HBaseStore implements USPSDataStore {
	
	private static final String DATA_COL_FAMILY = "datacols";
	
	@Autowired
	private USPSIdGenerator uspsIdGenerator;
	
	@Autowired
	private USPSUtils uspsUtils;
	
	private HBaseAdmin admin;
	
	private Map<Class<? extends USPSRecord>,String> tableMap = new Hashtable<Class<? extends USPSRecord>,String>();
	private Map<String,Copyright> copyrightCache = new HashMap<String,Copyright>();
	
	private HBaseConfiguration config;

	
	public void initialize() throws Exception {
		
		this.config = new HBaseConfiguration(); 
		try {
			admin = new HBaseAdmin(config);
		} catch(MasterNotRunningException e) {
			throw new Exception("Could not setup HBaseAdmin as no master is running...");
		}
		
		
		initializeStore();

	}

	private List<Class<? extends USPSRecord>> getClasses() {
		
		List<Class<? extends USPSRecord>> clazzes = new ArrayList<Class<? extends USPSRecord>>();
		try {
			for (Class clazz : uspsUtils.getUSPSRecordClasses()) {
				clazzes.add(clazz);
			}
			
		} catch(Exception e) {
			throw new RuntimeException(e.getMessage());
		}
 
		return clazzes;
	}

	private void initializeStore() throws Exception{
		List<Class<? extends USPSRecord>> clazzes = getClasses();
		
		for (Class<? extends USPSRecord> clazz : clazzes) {
			String tableName = clazz.getSimpleName();
			
			if (!admin.tableExists(tableName)) {
				HTableDescriptor tdesc = new HTableDescriptor(tableName);
				String[] columns = this.getColumns(clazz);
				
				//for (String column : columns) {
					HColumnDescriptor colDesc = new HColumnDescriptor(DATA_COL_FAMILY);
					tdesc.addFamily(colDesc);
				//}
				
				// create the table which will be ENABLED automatically
				try {
					if (!admin.tableExists(tableName)) {
						admin.createTable(tdesc);
					}
				} catch(TableExistsException e) {
					// FOR NOW IGNORE this, in a clustered setup, another box
					// may have beat us to it....
				}
			}

			// this should not occur, but we check anyways
			if (!admin.isTableEnabled(tableName)) {
				admin.enableTable(tableName);
			}
			
			tableMap.put(clazz, tableName);
		}
	}
	
	@Override
	public void deleteRecord(String identifier) throws StoreException {
		Class<? extends USPSRecord> clazz = null;
		try {
			clazz = uspsIdGenerator.getRecordType(identifier);
		} catch(Exception e) {
			throw new StoreException("Error translating identifier into a target table type: " + identifier,e);
		}

		
		HTable table = getTable(clazz);
		if (table == null) {
			throw new StoreException("No target table exists for searching by identifier of " + identifier,null);
		}
		
		Delete d = new Delete(Bytes.toBytes(identifier));
		
		try {
			table.delete(d);
		} catch(Exception e) {
			throw new StoreException("Error deleting by identifier from target table: " + identifier,e);
		}
		
		try {
			table.close();
		} catch(Exception e) {
			throw new StoreException("Error closing table, when deleting by identifier from target table: " + identifier,e);
		}
		

	}

	private HTable getTable(Class<? extends USPSRecord> clazz) throws StoreException{
		String tableName =  tableMap.get(clazz);
		try {
			return new HTable(config,tableName);
		} catch(Exception e) {
			throw new StoreException("Unexpected error retrieving table named " + tableName,e);
		}
	}
	
	@Override
	public void deleteRecord(USPSRecord record) throws StoreException {
		deleteRecord(record.getIdentifier());
	}

	@Override
	public USPSRecord getByIdentifier(String identifier) throws StoreException {
		
		Class<? extends USPSRecord> clazz = null;
		try {
			clazz = uspsIdGenerator.getRecordType(identifier);
		} catch(Exception e) {
			throw new StoreException("Error translating identifier into a target table type: " + identifier,e);
		}
		
		HTable table = getTable(clazz);
		if (table == null) {
			throw new StoreException("No target table exists for searching by identifier of " + identifier,null);
		}
		
		// lets create a get for the KV lookup
		Get g = new Get(Bytes.toBytes(identifier));
		
		Result result = null;
		try {
			// exec the get
			 result = table.get(g);
		} catch(Exception e) {
			throw new StoreException("There was an unexpected error fetching by identifier: " + identifier,e);
		}
		
		// not found??
		if (result == null) {
			return null;
		}
		
		// create a dummy record
		USPSRecord record = null;
		try {
			record = clazz.newInstance();
		} catch(Exception e) {
			throw new StoreException("Error constructing target object for class: " + clazz.getName(),e);
		}
		
		// get all columns
		Field[] columns = getFields(clazz);
		
		// family as bytes
		byte[] datacols = Bytes.toBytes(DATA_COL_FAMILY);
		
		// map to the properties
		for (Field column : columns) {
			byte[] bc = Bytes.toBytes(column.getName());
			byte[] colVal = result.getValue(datacols,bc);
			if (colVal != null) {
				try {
					Object trueValue;
					
					// convert the copyright into the object
					if (column.getType() == Copyright.class) {
						record.setCopyright(getCopyright(new String(colVal)));
						
					} else {
						trueValue = ConvertUtils.convert(new String(colVal), column.getType());
						PropertyUtils.setSimpleProperty(record, column.getName(), trueValue);
					}
					
				} catch(Exception e) {
					throw new StoreException("Error setting property for column: " + column.getName() + " with value " + new String(colVal),e);
				}
			}
		}
		
		try {
			table.close();
		} catch(Exception e) {
			throw new StoreException("Error closing table, when reading by identifier from target table: " + identifier,e);
		}
		
		return record;
	}
	
	// TODO this should be replaced by a cache based system
	private Copyright getCopyright(String id) throws Exception {
		return (Copyright)getByIdentifier(id);
	}

	@Override
	public void saveRecord(USPSRecord record) throws StoreException {
		
		HTable table = getTable(record.getClass());
		if (table == null) {
			throw new StoreException("No target table exists for " + record.getClass().getSimpleName(),null);
		}
		
		if (record.getIdentifier() == null) {
			throw new StoreException("Cannot save record, no identifier value exists!",null);
		}
		
		
		// get copyright
		Copyright copyright = record.getCopyright();
		
		// get all columns
		String[] columns = getColumns(record.getClass());
		
		// lets put a new object with identifier of the record
		Put p = new Put(Bytes.toBytes(record.getIdentifier()));
		
		// manually set the copyright to store the identifier of the Copyright object
		if (copyright != null) {
			p.add(Bytes.toBytes(DATA_COL_FAMILY),Bytes.toBytes("copyright"),Bytes.toBytes(record.getCopyright().getIdentifier()));
		}
		
		// for every other property lets set it 
		for (String column : columns) {
			Object value = null;
			try {
				value = PropertyUtils.getProperty(record, column);
				if (value != null) {
					p.add(Bytes.toBytes(DATA_COL_FAMILY),Bytes.toBytes(column),Bytes.toBytes(value.toString()));
				}
			} catch(Exception e) {
				throw new StoreException("Error in saving record, could apply value:\"" + value + "\" to column " + column,e);
			}
		}
		
		// save the row
		try {
			table.put(p);
		} catch(Exception e) {
			throw new StoreException("Error in saving record, could not save new row",e);
		}
		
		try {
			table.close();
		} catch(Exception e) {
			throw new StoreException("Error closing table, when putting by identifier from target table: " + record.getIdentifier(),e);
		}

	}
	
	private Field[] getFields(Class<? extends USPSRecord> clazz) throws StoreException {
		Field[] columns = null; 
		try {
			columns = uspsUtils.getAllDeclaredFields(clazz);
		
		} catch(Exception e) {
			throw new StoreException("Error in saving record, could not retrieve list of column names for the target record: error:" + e.getMessage(),e);
		}
		
		return columns;
	}
	
	private String[] getColumns(Class<? extends USPSRecord> clazz) throws StoreException {
		String[] columns = null;
		try {
			columns = uspsUtils.getAllDeclaredFieldNames(clazz);
		
		} catch(Exception e) {
			throw new StoreException("Error in saving record, could not retrieve list of column names for the target record: error:" + e.getMessage(),e);
		}
		
		return columns;
	}



	@Override
	public void purgeEntireStore() throws StoreException {
		List<Class<? extends USPSRecord>> clazzes = getClasses();
		for (Class<? extends USPSRecord> clazz : clazzes) {
			try {
				
				String tb = clazz.getSimpleName();
				
				if (admin.tableExists(tb)) {
					admin.disableTable(tb);
					admin.deleteTable(tb);
				}
				
				//HConnectionManager.deleteConnectionInfo(config, true);
				
			} catch(Exception e) {
				e.printStackTrace();
				throw new StoreException("Error in disabling and dropping all tables",e);
			}
		}

	}
	
	@Override
	public void processBatchUpdate(List<USPSRecord> records, Copyright copyright) throws StoreException {
		for (USPSRecord r : records) {
			
			// ensure it is set to this copyright
			r.setCopyright(copyright);
			
			// if we use the action code, use that to
			// determine to save or delete.
			if (r.getActionCode() == ActionCode.D) {
				deleteRecord(r);
			} else {
				saveRecord(r);
			}
		}
		
	}
	
	@Override
	public String getHadoopMapperAppContextXMLFilename() {
		String pack = StringUtils.replace(USPSDataFileMapper.class.getPackage().getName(),".","/");
		return pack + "/USPSDataFileMapper2HBase-context.xml";
	}

	
	@Override 	
	public boolean permitHadoopSpeculativeExecution() {
		return true;
	}

	@Override
	public boolean exists(String identifier) throws StoreException {
		return this.getByIdentifier(identifier) != null;
	}

	@Override
	public void processRecords(List<USPSRecord> records, Copyright copyright)
			throws ProcessException {
		try { 
			this.processBatchUpdate(records, copyright);
			
		} catch(StoreException e) {
			throw new ProcessException(e.getMessage(),e.baseException);
		}
	}

}
