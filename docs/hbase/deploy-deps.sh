#! /bin/sh
#
# Deploy all HBase dependencies which are not available via the official
#	 maven repository at http://repo1.maven.org.
#
#
# This is for HBase 0.20.1
#
# Modified for HBase 0.20.1 from the original located at
# http://www.fiveclouds.com/2009/04/13/deploying-hbase-to-your-local-maven-repo/
#
# The maven repository to deploy to.
#

REPOSITORY_URL=file:///$HOME/.m2/repository

if [ -z $HBASE_HOME ]; then
	echo "Error: HBASE_HOME is not set." 2>&1
	exit 1
fi

HBASE_LIBDIR=$HBASE_HOME/lib


# HBase
#
mvn deploy:deploy-file -Dfile=$HBASE_HOME/hbase-0.20.1.jar \
	-DpomFile=hbase.pom -Durl=$REPOSITORY_URL

#Hadoop
mvn deploy:deploy-file -DgroupId=org.apache -DartifactId=hadoop \
	-Dversion=0.20.1-hdfs127 -Dpackaging=jar -Durl=$REPOSITORY_URL \
	-Dfile=$HBASE_LIBDIR/hadoop-0.20.1-hdfs127-core.jar

#thrift
mvn deploy:deploy-file -DgroupId=com.facebook -DartifactId=thrift \
	-Dversion=r771587 -Dpackaging=jar -Durl=$REPOSITORY_URL \
	-Dfile=$HBASE_LIBDIR/libthrift-r771587.jar

#apache commons cli
mvn deploy:deploy-file -DgroupId=commons-cli -DartifactId=commons-cli \
	-Dversion=2.0-SNAPSHOT -Dpackaging=jar -Durl=$REPOSITORY_URL \
	-Dfile=$HBASE_LIBDIR/commons-cli-2.0-SNAPSHOT.jar
	
#zookeeper
mvn deploy:deploy-file -DgroupId=org.apache.hadoop -DartifactId=zookeeper \
	-Dversion=3.2.1 -Dpackaging=jar -Durl=$REPOSITORY_URL \
	-Dfile=$HBASE_LIBDIR/zookeeper-3.2.1.jar


# EOF
