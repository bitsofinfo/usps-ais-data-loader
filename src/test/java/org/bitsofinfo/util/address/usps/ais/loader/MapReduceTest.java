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

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@ContextConfiguration
public class MapReduceTest extends AbstractJUnit4SpringContextTests {

	
	@Test
	public void testMapReduceJob() throws Exception{ /*
		Resource file = applicationContext.getResource("classpath:hadoop-localhost.xml");
		Configuration conf = new Configuration();
		conf.addResource(file.getURL());
		
		
		FileSystem hdfs = FileSystem.get(conf);
		//Resource sampleFile = 
		//	applicationContext.getResource("classpath:zipinfo.com.usps.zipPlus4.sample.txt");
		//applicationContext.getResource("classpath:usps.ais.zipPlus4.sample.txt");
		//String targetFileName = "/uspsData/tests/MapReduceTest-"+sampleFile.getFilename();
		//Path targetFilePath = new Path(targetFileName);
		
		
		Path targetFilePath = new Path("/uspsData/tests/bigzip.txt");
		
		boolean sampleFileExists = hdfs.exists(targetFilePath);
		if (!sampleFileExists) {
		//	hdfs.copyFromLocalFile(new Path(sampleFile.getURI().getPath()), targetFilePath);
			hdfs.copyFromLocalFile(new Path("/Users/bitsofinfo/Desktop/bigzip.txt"), targetFilePath);
		}
		
		
		
		// map = take lines and convert to (id,USPSRecord object)
		// reduce = take USPSRecord object and convert to HBase PUT
		
		conf.setInt(FixedLengthInputFormat.FIXED_RECORD_LENGTH, USPSProductType.ZIP_PLUS_4.getRecordLength());
		//conf.setLong("mapred.max.split.size", 100000); // force max split size of 100K
		DistributedCache.addFileToClassPath(new Path("/mapper-jars/mcr.jar"), conf);
		
		Job job = new Job(conf);
		job.setJarByClass(USPSDataFileMapper.class);
		
		//File localJarPath = new File(job.getJar());
		//Path hdfsJarPath = new Path("/mapper-jars/" + localJarPath.getName());
		//boolean jarExists = hdfs.exists(hdfsJarPath);
		//if (!jarExists) {
		//	hdfs.copyFromLocalFile(new Path(localJarPath.getAbsolutePath()), hdfsJarPath);
		//}
		

		
		job.setMapperClass(USPSDataFileMapper.class);	

		job.setInputFormatClass(FixedLengthInputFormat.class);
		
		//FileInputFormat.addInputPath(job, targetFilePath);
		FileInputFormat.addInputPath(job, new Path("/uspsData/tests/bigzip.txt"));
		//FileInputFormat.addInputPath(job, new Path("/uspsData/tests/citystate.txt"));
		//FileInputFormat.addInputPath(job, new Path("/uspsData/tests/zipinfo.txt"));
		//FileInputFormat.addInputPath(job, new Path("/uspsData/tests/803.txt"));
		
		FileOutputFormat.setOutputPath(job, new Path("/uspsData/test-output/MapReduceTest-results-"+System.currentTimeMillis()));
		
		//job.setMapOutputKeyClass(Text.class);
		//job.setMapOutputValueClass(WritableUSPSRecord.class);
		
		//job.setOutputKeyClass(Text.class);
		//job.setOutputValueClass(WritableUSPSRecord.class);
		
		job.submit();
		
		while(!job.isComplete()) {
			Thread.currentThread().sleep(1000);
		}
		
		assert(job.isSuccessful() == true);*/
	}
	
}
