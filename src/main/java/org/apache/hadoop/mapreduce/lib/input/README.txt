This project had a need for a Hadoop InputFormat/RecordReader that could read from fixed length 
data files (none existed), so I created it for this project (FixedLengthInputFormat). This was also 
contributed as a patch to the Hadoop project. This source is included in here and updated for 
Hadoop 0.23.1 (not yet tested), however the patch that was submitted to the Hadoop project is still 
pending and was compiled under 0.20.x. The 0.20.x version in the patch files was tested and functionally 
running on a 4 node Hadoop and Hbase cluster.

You can read more about the fixed length record reader patch @ 

http://bitsofinfo.wordpress.com/2009/11/01/reading-fixed-length-width-input-record-reader-with-hadoop-mapreduce/

https://issues.apache.org/jira/browse/MAPREDUCE-1176 
