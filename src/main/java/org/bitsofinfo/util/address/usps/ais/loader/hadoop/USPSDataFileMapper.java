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
package org.bitsofinfo.util.address.usps.ais.loader.hadoop;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Counter;
import org.apache.hadoop.mapreduce.MapContext;
import org.apache.hadoop.mapreduce.Mapper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.bitsofinfo.util.address.usps.ais.Copyright;
import org.bitsofinfo.util.address.usps.ais.USPSIdGenerator;
import org.bitsofinfo.util.address.usps.ais.USPSRecord;
import org.bitsofinfo.util.address.usps.ais.USPSUtils;
import org.bitsofinfo.util.address.usps.ais.loader.USPSRecordParser;
import org.bitsofinfo.util.address.usps.ais.processor.ProcessException;
import org.bitsofinfo.util.address.usps.ais.processor.USPSDataProcessor;
import org.bitsofinfo.util.reflection.ClassFinder;

/**
 * USPSDataFileMapper is a Hadoop MapReduce Map task
 * which processes records from raw USPS data files,
 * translates them into USPSRecord objects and then
 * saves them off in a USPSDataProcessor.
 * 
 * @author bitsofinfo.g [at] gmail [dot] com
 *
 */
public class USPSDataFileMapper extends Mapper<LongWritable,Text,Text,WritableUSPSRecord> {
	
	// Static members which store the common resources
	// that are used by all USPSDataFileMapper instances
	// within a JVM. (This is the Spring container, shared)
	private static Object synchLock = new Object();
	private static boolean initialized = false;
	private static USPSRecordParser lineParser;
	private static USPSUtils uspsUtils;
	private static ClassFinder classFinder;
	private static USPSIdGenerator idGenerator;
	private static USPSDataProcessor dataProcessor;
	
	// our logger reference
	private static final Log LOG = LogFactory.getLog(USPSDataFileMapper.class);
	
	
	// config property that specifies the spring app context file to load
	public static final String MAPPER_APP_CONTEXT_XML = "uspsdatafilemapper.appcontextxml.filename";
	
	// config property for the length of records being processed by this mapper.
	public static final String MAPPER_RECORD_LENGTH = "uspsdatafilemapper.recordlength";

	// config property for record commit buffer
	public static final String MAPPER_DATA_PROCESSOR_BATCH_SIZE = "uspsdatafilemapper.processor.batchsize";
	
	// config property for the counter group name to store this tasks processed counter data
	public static final String USPS_COUNTERS_GROUP_NAME ="uspsdatafilemapper.counters";
	
	// counter name for the overall records process counter
	public static final String OVERALL_RECORDS_PROCESSED_COUNTER ="overall_total";
	
	// counter name for the individual records process counter
	public static final String MAPPER_RECORDS_PROCESSED_COUNTER ="per_mapper_total_";
	
	// my record length
	private int myRecordLength = -1;
	
	// if we skip copyright records
	private boolean skipCopyrights = false;
	
	// the copyright to apply to the records. This
	// is updated as we encounter a new.
	private Copyright copyrightToApply = null;
	
	// batch of records converted so far, to be flushed to DB
	private ArrayList<USPSRecord> batch = new ArrayList<USPSRecord>();
	
	// batch size
	private int processorBatchSize = 10000;
	
	// the processed Counter
	private Counter myTotalProcessedCounter = null;
	
	// the overall counter
	private Counter overallProcessedCounter = null;

	public USPSDataFileMapper() {}
	
	@Override
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		
		String className = key.toString();		
		String rawRecord = value.toString();

		// get the class
		Class<? extends USPSRecord> clazz = null;
		try {
		    clazz = uspsUtils.getClassForRawRecord(rawRecord.substring(0,1),myRecordLength);
		    if (clazz == null) {
		    	throw new IOException("NO CLASS 4 RAW RECORD:|" + rawRecord+ "|");
		    }
		} catch(Exception e) {
			throw new IOException("NO CLASS 4 RAW RECORD:|" + rawRecord+ "|");
		//	throw new IOException("Could not determine target USPSRecord class for record of type: " 
			//+ rawRecord.substring(0,1) + " len: " + myRecordLength);
		}
		
		// create a new instance to populate
		USPSRecord record = null;
		try { 
			record = clazz.newInstance();
		} catch(Exception e) {
			throw new IOException("Could not create target USPSRecord instance for record of type: " 
					+ clazz.getName()  + " " + e.getMessage());
		}
		
		// apply the line to the record
		try {
			lineParser.applyRawRecord(rawRecord, record);
			record.setIdentifier(idGenerator.generateId(record));

		} catch(Exception e) {
			throw new IOException("Could not apply record data to target USPSRecord instance for record of type: " 
					+ rawRecord + " " + myRecordLength +" "+ clazz.getName()  + " " + e.getMessage());
		}
		
		// get the record ID
		String id = record.getIdentifier();
		
		// if a copyright, update this as our latest one...
		// if the copyright has changed.., we need to flush all
		// previous records that were loaded after the copyrightToApply
		// before updating our copyrightToApply which will be applied
		// to all subsequent records
		if (record instanceof Copyright) {
			Copyright tmpC = (Copyright)record;
			if (copyrightToApply == null || !copyrightToApply.getIdentifier().equals(tmpC.getIdentifier())) {
				flushBatch();
				copyrightToApply = (Copyright)record;
			}
		}

		// add to batch
		batch.add(record);

		// commit?
		if (batch.size() >= processorBatchSize) {
			flushBatch();
		}

	
		// or the old version was Text,Text
		//context.write(new Text(id), new WritableUSPSRecord(record)); // json output
		
		
	}
	
	private void flushBatch() throws IOException {
		if (batch.size() > 0) {
			try {
				dataProcessor.processRecords(batch,copyrightToApply);
				
				// increment our counters and clear
				int batchSize = batch.size();
				this.myTotalProcessedCounter.increment(batchSize);
				this.overallProcessedCounter.increment(batchSize);
				
				// clear
				batch.clear();
				
			} catch(ProcessException e) {
				e.printStackTrace();
				throw new IOException("Error sending USPSRecord to USPSDataProcessor: "+  e.getMessage() +" " 
						+ e.toString() + " source=" +e.baseException.getMessage());
			}
		}
	}
	
	@Override
	protected void cleanup(Context context) throws IOException, InterruptedException {
		flushBatch();
	}
	
	@Override
	protected void setup(Context mapperContext) throws IOException, InterruptedException {
		
		String mapperAppContextXML = mapperContext.getConfiguration().get(MAPPER_APP_CONTEXT_XML);

		// processor batch size
		this.processorBatchSize = mapperContext.getConfiguration().getInt(MAPPER_DATA_PROCESSOR_BATCH_SIZE,10000);
		
		// our record length?
		this.myRecordLength = mapperContext.getConfiguration().getInt(MAPPER_RECORD_LENGTH,-1);
		if (myRecordLength == -1) {
			throw new IOException("USPSDataFileMapper must have the config property " + MAPPER_RECORD_LENGTH + " set > 0");
		}
		
		// my records stored counter
		this.myTotalProcessedCounter = ((MapContext)mapperContext).getCounter(USPS_COUNTERS_GROUP_NAME, MAPPER_RECORDS_PROCESSED_COUNTER + mapperContext.getTaskAttemptID().toString());
		
		// overall job processed counter
		this.overallProcessedCounter = ((MapContext)mapperContext).getCounter(USPS_COUNTERS_GROUP_NAME, OVERALL_RECORDS_PROCESSED_COUNTER);
		
		LOG.info("USPSDataFileMapper configured: skipCopyrights="+this.skipCopyrights + " processorBatchSize="+processorBatchSize +
				" myRecordLength="+this.myRecordLength + " mapperAppContextXML=" + mapperAppContextXML);

		// init static stuff
		if (!initialized) {
			synchronized(synchLock) {
				if (!initialized) {
					ApplicationContext context = 
						new ClassPathXmlApplicationContext(mapperAppContextXML);
					classFinder = (ClassFinder)context.getBean("classFinder");
					uspsUtils = (USPSUtils)context.getBean("uspsUtils");
					idGenerator = (USPSIdGenerator)context.getBean("uspsIdGenerator");
					lineParser = (USPSRecordParser)context.getBean("uspsLineParser");
					dataProcessor = (USPSDataProcessor)context.getBean("uspsDataProcessor");
					
					initialized = true;
				}
			}
		}
		
		
		
	}
	
}
