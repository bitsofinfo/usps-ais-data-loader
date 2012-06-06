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
package org.bitsofinfo.util.io;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import com.sun.xml.internal.rngom.parse.compact.EOFException;

/**
 * RecordLoader defines the interface for
 * a utility service which loads "records" 
 * out of raw text files. A record is defined
 * as a String of text of a fixed length.
 * 
 * RecordLoader can load a target set of records
 * and return them as a List of Strings OR pass
 * each record to a RecordHandler as each record is
 * read from the file (to reduce memory footprints)
 * 
 * RecordLoader implementations must be thread-safe
 * 
 * @author bitsofinfo.g [at] gmail [dot] com
 *
 */
public interface RecordLoader {

	/**
	 * Loads records/lines from a file located by the given FileHandle
	 * 
	 * @param handle					FileHandle of the file to be read
	 * @param startAtRecord				The line/record number to start at (1 based) Pass 15 to start at the 15th line)
	 * @param totalRecordsToRead			The max number of records/lines to read, if available
	 * @param sizeOfEachRecordInBytes		The size of each record/line in the file in bytes (including the line terminating character(s))
	 * 
	 * @return	List of String lines
	 * 
	 * @throws EOFException				if the start at record is beyond the length of the file
	 * @throws SecurityException		if the file cannot be read
	 * @throws FileNotFoundException	if the file does not exist
	 * @throws IOException				if an IOError occurs
	 * @throws Exception				if an other unexpected error occurs
	 */
	public List<String> loadRecords(FileHandle handle,
								  long startAtRecord,
								  long totalRecordsToRead,
								  int sizeOfEachRecordInBytes) 
				throws EOFException, SecurityException, FileNotFoundException, IOException, Exception;
	
	
	
	/**
	 * Loads records/lines from a file located by the given FileHandle directly
	 * into the given RecordHandler as each record is read from the file.
	 * 
	 * @param handle						FileHandle of the file to be read
	 * @param recordHandler					RecordHandler who's handleRecord(recordNumber, record) method will
	 * 										be called as each record is loaded from the File
	 * 
	 * @param startAtRecord					The line/record number to start at (1 based) Pass 15 to start at the 15th line)
	 * @param totalRecordsToRead			The max number of records/lines to read, if available
	 * @param sizeOfEachRecordInBytes		The size of each record/line in the file in bytes (including the line terminating character(s))
	 * 
	 * @throws EOFException
	 * @throws SecurityException
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws Exception
	 */
	public void loadRecords(FileHandle handle,
							RecordHandler recordHandler,
							long startAtRecord,
							long totalRecordsToRead,
							int sizeOfEachRecordInBytes) 
			  	throws EOFException, SecurityException, FileNotFoundException, IOException, Exception;
	
}
