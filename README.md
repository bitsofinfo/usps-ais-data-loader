# USPS AIS Data Loading Utility

### Overview 
This project was originally started to create a framework for loading data files from the USPS AIS suite of data products (zipPlus4, cityState). The project has not been worked on in a while but I figured I'd open-source it and maybe some folks would like to team up to work on it further. Throwing it out there under the Apache 2.0 license. Some of the libs need updating etc as well, for instance it was originally developed w/ Spring 2.5.

USPS AIS data files are fixed length format records. This framework was created to handle bulk loading/updating this data into a structured/semi-structured data store of address data (i.e. MySql or HBase). It is wired together using Spring and built w/ Maven. A key package is the "org.bitsofinfo.util.address.usps.ais" package which defines the pojos for the records, and leverages a custom annotation which binds record properties to locations within the fixed length records which contain the data being loaded.

Initial loader implementations include both a single JVM multi-threaded version as well as a second one that leverages Hadoop Mapreduce to split the AIS files up across HDFS and process them in parallel using Hadoop mapreduce nodes to ingest the data much faster then just on one box. Both of these obviously operate asynchronously given a load job submission. Ingestion times are significantly faster using Hadoop.

This project also had a need for a Hadoop InputFormat/RecordReader that could read from fixed length data files (none existed), so I created it for this project (FixedLengthInputFormat). This was also contributed as a patch to the Hadoop project. This source is included in here and updated for Hadoop 0.23.1 (not yet tested), however the patch that was submitted to the Hadoop project is still pending and was compiled under 0.20.x. The 0.20.x version in the patch files was tested and functionally running on a 4 node Hadoop and Hbase cluster.


### Additional Info 

You can read more about the fixed length record reader patch @ 

http://bitsofinfo.wordpress.com/2009/11/01/reading-fixed-length-width-input-record-reader-with-hadoop-mapreduce/

https://issues.apache.org/jira/browse/MAPREDUCE-1176 

The USPS AIS products have some sample data-sets available online at the USPS website, however for the full product of data-files you need to pay for the data and/or subscription for delta updates. Some of the unit-tests reference files from the real data-sets, they have been omitted, you will have to replace them with the real ones. Other unit tests reference the sample files freely available via USPS or other providers.

Links where USPS data files can be purchased:

http://www.zipinfo.com/products/natzip4/natzip4.htm

https://www.usps.com/business/address-information-systems.htm
