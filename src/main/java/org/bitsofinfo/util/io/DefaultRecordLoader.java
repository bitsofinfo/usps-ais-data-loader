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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import com.sun.xml.internal.rngom.parse.compact.EOFException;

/**
 * DefaultRecordLoader is an RecordLoader implementation
 * which supports reading files from a local "file" URI
 * via an internal BufferedReader
 * 
 * @author bitsofinfo.g [at] gmail [dot] com
 *
 */
public class DefaultRecordLoader implements RecordLoader {

	

	@Override
	public List<String> loadRecords(FileHandle handle, long startAtRecord,
			long totalRecordsToRead, int sizeOfEachRecordInBytes) throws EOFException, SecurityException, FileNotFoundException, IOException, Exception {
		
		
		
		ListBackedRecordHandler handler = new ListBackedRecordHandler();
		this.loadRecords(handle, handler, startAtRecord, totalRecordsToRead, sizeOfEachRecordInBytes);
		return handler.getAllRecords();
		
	}
	
	private void loadRecordsFromFile(FileHandle handle, 
									RecordHandler recordHandler, 
									long startAtRecord, 
									long totalRecordsToRead, 
									int sizeOfEachRecordInBytes) throws EOFException, IOException, Exception {

		File file = new File(handle.getUri());
		if (!file.exists()) {
			throw new FileNotFoundException("File not found @"+handle.getUri().toString());
			
		} else if (file.isDirectory()) {
			throw new FileNotFoundException("File @"+handle.getUri().toString() + " is a directory, not a file");
			
		} else if (!file.canRead()) {
			throw new SecurityException("Cannot read from file @" + handle.getUri().toString());
		}
		
		long bytePos = (startAtRecord == 1 ? 0 :((startAtRecord-1) * sizeOfEachRecordInBytes));
		if (bytePos > file.length()) {
			throw new EOFException();
		}
		
		// seek to that position
		long startAtByte;
		try {
			RandomAccessFile rand = new RandomAccessFile(file,"r");
			rand.seek(bytePos);
			startAtByte = rand.getFilePointer();
			rand.close();
			
		} catch(IOException e) {
			throw new IOException("There was an error seeking to the specified record/line in the file: " + startAtRecord + " @position: " +bytePos);
		}
		
		BufferedReader reader = null;
		try {
			// lets fire up a buffered reader and skip right to that spot.
			reader = new BufferedReader(new FileReader(file));
			reader.skip(startAtByte);
			
			String line;
			long totalRead = 0;
			long recordNumber = startAtRecord;
			char[] buffer = new char[sizeOfEachRecordInBytes];
			while(totalRead < totalRecordsToRead && (-1 != reader.read(buffer, 0, sizeOfEachRecordInBytes))) {
				recordHandler.handleRecord(recordNumber,new String(buffer));
				totalRead++;
				recordNumber++;
			}
		} catch(Exception e) {
			throw new Exception("An unexpected error occurred while reading records/lines from the file @" + handle.getUri().toString() + " error=" + e.getMessage());
			
		} finally {
			if (reader != null) {
				// might not be good
				try {reader.close();} catch(Exception ignore) {}
			}
		}
	
		// call finish;
		recordHandler.finish();
	}
	


	@Override
	public void loadRecords(FileHandle handle, RecordHandler recordHandler,
			long startAtRecord, long totalRecordsToRead,
			int sizeOfEachRecordInBytes) throws EOFException,
			SecurityException, FileNotFoundException, IOException, Exception {
		
		if (handle.getUri().getScheme().toLowerCase().equals(URIScheme.FILE.getName())) {

			loadRecordsFromFile(handle, recordHandler, startAtRecord, totalRecordsToRead, sizeOfEachRecordInBytes);
			
		} else {
			throw new Exception("DefaultRecordLoader.loadLines() only supports loading" +
					" lines from URIs which schemes of type URIScheme.FILE");
		}
		
		
	}
	
	/**
	 * Internal RecordHandler implementation which simply stores
	 * each processed record in an internal List
	 * 
	 * @author bitsofinfo.g [at] gmail [dot] com
	 *
	 */
	private class ListBackedRecordHandler implements RecordHandler {
		
		private ArrayList<String> records = new ArrayList<String>();
		
		public void handleRecord(long recordNumber, String record) {
			records.add(record);
		}
		
		public List<String> getAllRecords() {
			return records;
		}
		
		public long getTotalRecordsHandled() {
			return records.size();
		}
		 
		public void finish() throws Exception {
			// nothing
		}
	}
	

}
