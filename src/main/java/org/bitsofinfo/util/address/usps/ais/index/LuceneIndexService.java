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
package org.bitsofinfo.util.address.usps.ais.index;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.springframework.beans.factory.annotation.Autowired;

import org.bitsofinfo.util.address.usps.ais.USPSRecord;
import org.bitsofinfo.util.address.usps.ais.USPSUtils;

public class LuceneIndexService implements USPSIndexService {

	@Autowired
	private USPSUtils uspsUtils;

	
	// the root dir for all of our indexes
	private File indexRootDir = null;
	
	// if we are read only or not
	private boolean readOnly = false;
	
	private USPSRecordAnalyzer uspsRecordAnalyzer = new USPSRecordAnalyzer();
	
	private HashMap<Class,LuceneIndex> indexMap = new HashMap<Class,LuceneIndex>();

	private int indexCount = 0;
	
	public void setIndexRootDir(String path) {
		this.indexRootDir = new File(path);
	}
	
	private IndexWriter getIndexWriter(File targetDirPath,boolean optimizeForHeavyWriteOp, boolean create) 
								throws CorruptIndexException, LockObtainFailedException,IOException {

		boolean autoCommit = true;
		long ramBufferSize = 64;//mb
		boolean compoundFile = true;
		int mergeFactor = 20;
		
		if (optimizeForHeavyWriteOp) {
			autoCommit = false;
			ramBufferSize = 100;//mb
			compoundFile = false;
			mergeFactor = mergeFactor * 2;
		}
		
		IndexWriter indexWriter = new IndexWriter(FSDirectory.open(targetDirPath),uspsRecordAnalyzer,create,MaxFieldLength.UNLIMITED);
		indexWriter.setRAMBufferSizeMB(ramBufferSize);
		indexWriter.setUseCompoundFile(compoundFile);
		indexWriter.setMergeFactor(mergeFactor);
		return indexWriter;
	}
	
	protected void indexRecords(List<USPSRecord> records) throws Exception {
		//IndexWriter idxWriter = new IndexWriter();
	}
	
	protected Document uspsRecordToDocument(USPSRecord record) throws Exception {
		// create a new document
		Document doc = new Document();
		
		// add the identifier field (stored, indexed but not analyzed)
		Field idField = new Field("identifier",record.getIdentifier(),Field.Store.YES,Field.Index.NOT_ANALYZED);
		
		// get all field names, and index these values
		String[] fields2index = uspsUtils.getKeyFieldNames(record);
		for (String fieldName : fields2index) {
			Object rawVal = null;
			try {
				rawVal = PropertyUtils.getProperty(record, fieldName);
				
			} catch(Exception e) {
				// HANDLE THIS..(bad prop name etc).
				rawVal = null;
			}
			
			// index/analyze the field value, do NOT store it
			if (rawVal != null) {
				Field f = new Field(fieldName,rawVal.toString(),Field.Store.NO,Field.Index.ANALYZED);
				doc.add(f);
			}
		}
		
		return doc;
		
	}
	
	public void initialize() {
		try {
			// ensure the indexer root dir exists
			if (!indexRootDir.exists()) {
				indexRootDir.mkdirs();
			}
			
			// create the storage structure for all our indexes
			Set<Class> clazzes = uspsUtils.getUSPSRecordClasses();
			for (Class clazz : clazzes) {
				File baseDir = new File(indexRootDir.getAbsolutePath() + "/" + clazz.getSimpleName());
				File repopDir = new File(baseDir.getAbsolutePath() + "/repopulate");
				File activeDir = new File(baseDir.getAbsolutePath() + "/active");
				
				if (!baseDir.exists()) {
					baseDir.mkdirs();
				}
				if (!repopDir.exists()) {
					repopDir.mkdirs();
					this.getIndexWriter(repopDir, false, true); // create it
				}
				if (!activeDir.exists()) {
					activeDir.mkdirs();
					this.getIndexWriter(activeDir, false, true); // create it
				}
				
				indexMap.put(clazz,new LuceneIndex(activeDir,repopDir));
				
			}
			
		} catch(Exception e) {
			
		}
	}
	
	
	@Override
	public void index(List<USPSRecord> records) {
		
		// sort em all out
		HashMap<Class,List<USPSRecord>> clazzMap = new HashMap<Class,List<USPSRecord>>();
		for (USPSRecord record : records) {
			Class clazz = record.getClass();
			List<USPSRecord> tmp = clazzMap.get(clazz);
			if (tmp == null) {
				tmp = new ArrayList<USPSRecord>();
				clazzMap.put(clazz, tmp);
			}
			
			tmp.add(record);
		}
			
			
		for (Class clazz : clazzMap.keySet()) {
		
			List<USPSRecord> items = clazzMap.get(clazz);
			LuceneIndex index = indexMap.get(clazz);
			IndexWriter writer = null;
			try {
				 writer = this.getIndexWriter(index.activeDir, true, false);
				 
				 for (USPSRecord r : items) {
					 Document doc = this.uspsRecordToDocument(r);
					 Term id = new Term("identifier",r.getIdentifier());
					 writer.updateDocument(id, doc, this.uspsRecordAnalyzer);
				 }
				 
				 if (indexCount == 10) {
					 writer.optimize();
					 indexCount=0;
				 }
				
			} catch(Exception e) {
				
			} finally {
				if (writer != null) {
					try {writer.close();} catch(Exception ignoreForNow){}
				}
			}
		}
		
		indexCount++;

	}

}
