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
package org.bitsofinfo.util.address.usps.ais.store.jpa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.StringUtils;

import org.bitsofinfo.util.address.usps.ais.ActionCode;
import org.bitsofinfo.util.address.usps.ais.Copyright;
import org.bitsofinfo.util.address.usps.ais.USPSIdGenerator;
import org.bitsofinfo.util.address.usps.ais.USPSRecord;
import org.bitsofinfo.util.address.usps.ais.USPSUtils;
import org.bitsofinfo.util.address.usps.ais.index.USPSIndexService;
import org.bitsofinfo.util.address.usps.ais.loader.hadoop.USPSDataFileMapper;
import org.bitsofinfo.util.address.usps.ais.processor.ProcessException;
import org.bitsofinfo.util.address.usps.ais.search.USPSSearchService;
import org.bitsofinfo.util.address.usps.ais.store.StoreException;
import org.bitsofinfo.util.address.usps.ais.store.USPSDataStore;

/**
 * JPAStore is a JPA backed implementation of USPSDataStore and USPSSearchService
 * which searches against the JPA store. 
 * 
 * @author bitsofinfo.g [at] gmail [dot] com
 *
 */
public class JPAStore implements USPSDataStore, USPSSearchService {

	private EntityManager entityMgr;
	
	@Autowired
	private USPSIdGenerator uspsIdGenerator;
	
	@Autowired
	private USPSUtils uspsUtils;
	
	@Autowired
	private USPSIndexService indexService;
	
	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.entityMgr = em;
	}
	
	@Autowired
	private PlatformTransactionManager transactionManager;

	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void deleteRecord(String identifier) throws StoreException {
		Class<? extends USPSRecord> clazz = getClassForIdentifier(identifier);
		
		try {
			Query q = this.entityMgr.createQuery("delete from " + clazz.getSimpleName() + " o where o.identifier = :idval");
			q.setParameter("idval", identifier);
			int count = q.executeUpdate();
		
		} catch(Exception e) {
			throw new StoreException("Unexpected error in deleting the given record", e);
		}
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void deleteRecord(USPSRecord record) throws StoreException {
		try {
			this.entityMgr.remove(record);
			
		} catch(Exception e) {
			throw new StoreException("Unexpected error in deleting the given record", e);
		}
	}
	

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public boolean exists(String identifier) throws StoreException {
		return getByIdentifier(identifier) != null;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public USPSRecord getByIdentifier(String identifier) throws StoreException {
		Class<? extends USPSRecord> clazz = getClassForIdentifier(identifier);
		
		USPSRecord record = null;
		try {
			record = this.entityMgr.find(clazz, identifier);
			
		} catch(Exception e) {
			throw new StoreException("Unexpected error in locating a record by the given identifier", e);
		}
		
		return record;
	}

	private Class<? extends USPSRecord> getClassForIdentifier(String identifier) throws StoreException {
		Class<? extends USPSRecord> clazz = null;
		try {
			clazz = uspsIdGenerator.getRecordType(identifier);
		} catch(Exception e) {
			throw new StoreException("Error translating identifier into a target class: " + identifier,e);
		}
		
		if (clazz == null) {
			throw new StoreException("No USPSRecord Class located for identifier: " + identifier,null);
		}
		
		return clazz;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void purgeEntireStore() throws StoreException {
		
		List<Class<? extends USPSRecord>> clazzes = getClasses();
		for (Class<? extends USPSRecord> clazz : clazzes) {
			try {
				TransactionStatus ts = this.transactionManager.getTransaction(null); // FIXME
				Query q = this.entityMgr.createQuery("delete from " + clazz.getSimpleName());
				int totalRemoved = q.executeUpdate();
				transactionManager.commit(ts);
				
			} catch(Exception e) {
				//throw new StoreException("Error in disabling and dropping all tables",e);
			}
		}

	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveRecord(USPSRecord record) throws StoreException {
		try {
			// we merge the record into the context which will save/update it
			// and ensure that subsequent changes to the passed "record" will NOT
			// be saved permanently. Only what is in the "record" at this point in time
			this.entityMgr.merge(record);
			
		} catch(Exception e) {
 			throw new StoreException("Unexpected error in saving the given record", e);
		}
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

	@Override
	public void processBatchUpdate(List<USPSRecord> records, Copyright copyright) throws StoreException {
		
		/* This method is implemented in separate transactions
		 * 
		 * a) processCopyright() - we process the Copyright record separately as for batch loads
		 * the copyright record is duplicated across many record sets. Also since this may
		 * be called by multiple threads, we don't want a duplicate entry exception to stop
		 * the insertion/update of the other records in the set which are unique.
		 * 
		 * b) procesNonCopyrightRecords() - this transaction processes all non-Copyright records
		 * contained in the passed set. All records are associated with the passed copyright
		 */
		
		
		// if the copyright was provided, ensure that it exists and
		// we are using the correct instance
		if (copyright != null) {
			copyright = processCopyright(copyright);
		}
		
		// process the remaining records
		processNonCopyrightRecords(records, copyright);
		
		// dump em off to the indexer
		//this.indexService.index(records);
	}
	

	private Copyright processCopyright(Copyright copyright) throws StoreException {
		Copyright c = this.entityMgr.find(Copyright.class, copyright.getIdentifier());
		
		if (c == null) {
			DefaultTransactionDefinition td = new DefaultTransactionDefinition();
			td.setName("Process copyright");
			td.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
			
			TransactionStatus txs = this.transactionManager.getTransaction(td);
			
			try {
				this.saveRecord(copyright);
				this.transactionManager.commit(txs);
				
			} catch(DataIntegrityViolationException e) {
				if (!txs.isCompleted()) {
					this.transactionManager.rollback(txs);
				}
				
				// ignore this, it is fine, some other load beat us to it
				c = this.entityMgr.find(Copyright.class, copyright.getIdentifier());
			}
			
			c = copyright;
		}
		
		return c;
	}
	

	private void processNonCopyrightRecords(List<USPSRecord> records, Copyright copyright) throws StoreException {
		
		
		DefaultTransactionDefinition td = new DefaultTransactionDefinition();
		td.setName("Process copyright");
		td.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		
		TransactionStatus txs = this.transactionManager.getTransaction(td);
		try {
		
			//copyright = this.entityMgr.merge(copyright);
			
			for(USPSRecord r : records) {
	
				// skip copyrights if it is the same one passed in above
				if (r instanceof Copyright) {
					if (r.getIdentifier().equals(copyright.getIdentifier())) {
						// chuck it as it has already been processed
						continue;
					}
				}
				
				// ensure the copyright is linked
				r.setCopyright(copyright);
				
				// if we use the action code, use that to
				// determine to save or delete.
				if (r.getActionCode() == ActionCode.D) {
					this.deleteRecord(r);
				} else {
					this.saveRecord(r);
				}
			}
			
			this.transactionManager.commit(txs);
			
		} catch(Exception e) {
			this.transactionManager.rollback(txs);
		}
		
	}

	@Override
	public String getHadoopMapperAppContextXMLFilename() {
		String pack = StringUtils.replace(USPSDataFileMapper.class.getPackage().getName(),".","/");
		return pack + "/USPSDataFileMapper2JPA-context.xml";
	}

	@Override 	
	public boolean permitHadoopSpeculativeExecution() {
		return false;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public List<USPSRecord> getBy(Class<? extends USPSRecord> type, String propertyName,
			Object value) throws Exception {
		HashMap<String,Object> query = new HashMap<String,Object>();
		query.put(propertyName, value);
		return getBy(type,query);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public List<USPSRecord> getBy(Class<? extends USPSRecord> type,
			Map<String, Object> propValueMap) throws Exception {
	
		try {
			// validate props in criteria
			for (String pname : propValueMap.keySet()) {
				if (!uspsUtils.fieldExists(type,pname.trim())) {
					throw new Exception("Cannot search against given criteria, passed" +
							" property " + pname + " is invalid");
				}
			}
			
			
			// build query
			StringBuffer query = new StringBuffer("where ");
			for (String pname : propValueMap.keySet()) {
				query.append("o." + pname.trim() + "=:"+pname + " AND ");
			}
			
			String where = query.toString().substring(0, query.length()-4);
			
			Query q = this.entityMgr.createQuery("select o from " + type.getSimpleName() + " o " + where);
			
			for (String pname : propValueMap.keySet()) {
				q.setParameter(pname.trim(), propValueMap.get(pname));
			}
			
			// execute the search
			return q.getResultList();
		
		} catch(Exception e) {
			throw new StoreException("Unexpected error in searching", e);
		}
		
		
		
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
