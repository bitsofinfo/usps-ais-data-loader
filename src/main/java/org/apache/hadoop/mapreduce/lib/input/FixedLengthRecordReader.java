package org.apache.hadoop.mapreduce.lib.input;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionCodecFactory;
import org.apache.hadoop.mapreduce.Counter;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.MapContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

/**
 * 
 * FixedLengthRecordReader is returned by FixedLengthInputFormat. This reader
 * uses the record length property set within the FixedLengthInputFormat to 
 * read one record at a time from the given InputSplit. This record reader
 * does not support compressed files.<BR><BR>
 * 
 * Each call to nextKeyValue() updates the LongWritable KEY and Text VALUE.<BR><BR>
 * 
 * KEY = byte position in the file the record started at<BR>
 * VALUE = the record itself (Text)
 * 
 * 
 * @author bitsofinfo.g [at] gmail [dot] com.g (AT) gmail.com
 *
 */
public class FixedLengthRecordReader extends RecordReader<LongWritable, Text> {
	
	// reference to the logger
	private static final Log LOG = LogFactory.getLog(FixedLengthRecordReader.class);
	
	// the start point of our split
	private long splitStart;
	
	// the end point in our split
	private long splitEnd; 
	
	// our current position in the split
	private long currentPosition;
	
	// the length of a record
	private int recordLength; 
	
	// reference to the input stream
	private FSDataInputStream fileInputStream;
	
	// the input byte counter
	private Counter inputByteCounter; 
	
	// reference to our FileSplit
	private FileSplit fileSplit;
	
	// our record key (byte position)
	private LongWritable recordKey = null;
	
	// the record value
	private Text recordValue = null; 
	
	@Override
	public void close() throws IOException {
		if (fileInputStream != null) {
			fileInputStream.close();
		}
	}

	@Override
	public LongWritable getCurrentKey() throws IOException,
			InterruptedException {

		return recordKey;
	}

	@Override
	public Text getCurrentValue() throws IOException, InterruptedException {
		return recordValue;
	}

	@Override
	public float getProgress() throws IOException, InterruptedException {
		if (splitStart == splitEnd) {
			return 0;
		} else {
			return Math.min((float)1.0, (currentPosition - splitStart) / (float)(splitEnd - splitStart));
		} 
	}

	@Override
	public void initialize(InputSplit inputSplit, TaskAttemptContext context)
			throws IOException, InterruptedException {
		
		// the file input fileSplit
		this.fileSplit = (FileSplit)inputSplit;
		
		// the byte position this fileSplit starts at within the splitEnd file
		splitStart = fileSplit.getStart();
		
		// splitEnd byte marker that the fileSplit ends at within the splitEnd file
		splitEnd = splitStart + fileSplit.getLength();
		
		// log some debug info
		LOG.info("FixedLengthRecordReader: SPLIT START="+splitStart + " SPLIT END=" +splitEnd + " SPLIT LENGTH="+fileSplit.getLength() );
		
		// the actual file we will be reading from
		Path file = fileSplit.getPath(); 
		
		// job configuration
		Configuration job = context.getConfiguration(); 
		
		// check to see if compressed....
		CompressionCodec codec = new CompressionCodecFactory(job).getCodec(file);
	 	if (codec != null) {
	 		throw new IOException("FixedLengthRecordReader does not support reading compressed files");
	 	}
		
		// for updating the total bytes read in 
	 	inputByteCounter = ((MapContext)context).getCounter("FileInputFormatCounters", "BYTES_READ"); 
	 	
	 	// THE JAR COMPILED AGAINST 0.20.1 does not contain a version of FileInputFormat with these constants (but they exist in trunk)
	 	// uncomment the below, then comment or discard the line above
	 	//inputByteCounter = ((MapContext)context).getCounter(FileInputFormat.COUNTER_GROUP, FileInputFormat.BYTES_READ); 
		
		// the size of each fixed length record
		this.recordLength = FixedLengthInputFormat.getRecordLength(job);
		
		// get the filesystem
		final FileSystem fs = file.getFileSystem(job); 
		
		// open the File
		fileInputStream = fs.open(file,(64 * 1024)); 
		
		// seek to the splitStart position
		fileInputStream.seek(splitStart);
	
		// set our current position
	 	this.currentPosition = splitStart; 
	}

	@Override
	public boolean nextKeyValue() throws IOException, InterruptedException {
		if (recordKey == null) {
		 	recordKey = new LongWritable();
	 	}
		
		// the Key is always the position the record starts at
	 	recordKey.set(currentPosition);
	 	
	 	// the recordValue to place the record text in
	 	if (recordValue == null) {
	 		recordValue = new Text();
	 	} else {
	 		recordValue.clear();
	 	}

	 	// if the currentPosition is less than the split end..
	 	if (currentPosition < splitEnd) {
	 		
	 		// setup a buffer to store the record
	 		byte[] buffer = new byte[this.recordLength];
	 		int totalRead = 0; // total bytes read
	 		int totalToRead = recordLength; // total bytes we need to read
	 		
	 		// while we still have record bytes to read
	 		while(totalRead != recordLength) {
	 			// read in what we need
	 			int read = this.fileInputStream.read(buffer, 0, totalToRead);
	 			
	 			// append to the buffer
	 			recordValue.append(buffer,0,read);
	 			
	 			// update our markers
	 			totalRead += read;
	 			totalToRead -= read;
	 			//LOG.info("READ: just read=" + read +" totalRead=" + totalRead + " totalToRead="+totalToRead);
	 		}
	 		
	 		// update our current position and log the input bytes
	 		currentPosition = currentPosition +recordLength;
	 		inputByteCounter.increment(recordLength);
	 		
	 		//LOG.info("VALUE=|"+fileInputStream.getPos()+"|"+currentPosition+"|"+splitEnd+"|" + recordLength + "|"+recordValue.toString());
	 		
	 		// return true
	 		return true;		 	
	 	}

	 	// nothing more to read....
		return false;
	}


}
