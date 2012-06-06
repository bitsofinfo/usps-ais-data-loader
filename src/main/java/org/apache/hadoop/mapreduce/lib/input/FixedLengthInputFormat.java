package org.apache.hadoop.mapreduce.lib.input;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionCodecFactory;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;

/**
 * FixedLengthInputFormat is an input format which can be used
 * for input files which contain fixed length records with NO
 * delimiters and NO carriage returns (CR, LF, CRLF) etc. Such
 * files typically only have one gigantic line and each "record"
 * is of a fixed length, and padded with spaces if the record's actual
 * value is shorter than the fixed length.<BR><BR>
 * 
 * Users must configure the record length property before submitting
 * any jobs which use FixedLengthInputFormat.<BR><BR>
 * 
 * myJobConf.setInt("mapreduce.input.fixedlengthinputformat.record.length",[myFixedRecordLength]);<BR><BR>
 * 
 * This input format overrides <code>computeSplitSize()</code> in order to ensure
 * that InputSplits do not contain any partial records since with fixed records
 * there is no way to determine where a record begins if that were to occur.
 * Each InputSplit passed to the FixedLengthRecordReader will start at the beginning
 * of a record, and the last byte in the InputSplit will be the last byte of a record.
 * The override of <code>computeSplitSize()</code> delegates to FileInputFormat's
 * compute method, and then adjusts the returned split size by doing the following:
 * <code>(Math.floor(fileInputFormatsComputedSplitSize / fixedRecordLength) * fixedRecordLength)</code>
 *
 * <BR><BR>
 * This InputFormat returns a FixedLengthRecordReader. <BR><BR>
 * 
 * Compressed files currently are not supported.
 *
 * @see	FixedLengthRecordReader
 * 
 * @author bitsofinfo.g [at] gmail [dot] com.g (AT) gmail.com
 *
 */
public class FixedLengthInputFormat extends FileInputFormat<LongWritable, Text> {
	
	
	/**
	 * When using FixedLengthInputFormat you MUST set this
	 * property in your job configuration to specify the fixed
	 * record length.
	 * <BR><BR>
	 * 
	 * i.e. myJobConf.setInt("mapreduce.input.fixedlengthinputformat.record.length",[myFixedRecordLength]);
	 */
	public static final String FIXED_RECORD_LENGTH = "mapreduce.input.fixedlengthinputformat.record.length"; 
	
	// our logger reference
	private static final Log LOG = LogFactory.getLog(FixedLengthInputFormat.class);
	
	// the default fixed record length (-1), error if this does not change
	private int recordLength = -1;

	/**
	 * Return the int value from the given Configuration found
	 * by the FIXED_RECORD_LENGTH property.
	 * 
	 * @param config
	 * @return	int record length value
	 * @throws IOException if the record length found is 0 (non-existant, not set etc)
	 */
	public static int getRecordLength(Configuration config) throws IOException {
		int recordLength = config.getInt(FixedLengthInputFormat.FIXED_RECORD_LENGTH, 0); 
		
		// this would be an error
		if (recordLength == 0) {
			throw new IOException("FixedLengthInputFormat requires the Configuration property:" + FIXED_RECORD_LENGTH + " to" +
					" be set to something > 0. Currently the value is 0 (zero)");
		}

		return recordLength;
	}
	
	/**
	 * This input format overrides <code>computeSplitSize()</code> in order to ensure
	 * that InputSplits do not contain any partial records since with fixed records
	 * there is no way to determine where a record begins if that were to occur.
	 * Each InputSplit passed to the FixedLengthRecordReader will start at the beginning
	 * of a record, and the last byte in the InputSplit will be the last byte of a record.
	 * The override of <code>computeSplitSize()</code> delegates to FileInputFormat's
	 * compute method, and then adjusts the returned split size by doing the following:
	 * <code>(Math.floor(fileInputFormatsComputedSplitSize / fixedRecordLength) * fixedRecordLength)</code>
	 * 
	 * @inheritDoc
	 */
	@Override
	protected long computeSplitSize(long blockSize, long minSize, long maxSize) {
		long defaultSize = super.computeSplitSize(blockSize, minSize, maxSize);

		// 1st, if the default size is less than the length of a
		// raw record, lets bump it up to a minimum of at least ONE record length
		if (defaultSize <= recordLength) {
			return recordLength;
		}
		
		// determine the split size, it should be as close as possible to the 
		// default size, but should NOT split within a record... each split
		// should contain a complete set of records with the first record
		// starting at the first byte in the split and the last record ending
		// with the last byte in the split.
		
		long splitSize = ((long)(Math.floor((double)defaultSize / (double)recordLength))) * recordLength;
		LOG.info("FixedLengthInputFormat: calculated split size: " + splitSize);

		return splitSize;
		
	}
	
	/**
	 * Returns a FixedLengthRecordReader instance
	 * 
	 * @inheritDoc
	 */
	@Override
	public RecordReader<LongWritable, Text> createRecordReader(InputSplit split,
			TaskAttemptContext context) throws IOException, InterruptedException {
		return new FixedLengthRecordReader();
	}

	/**
	 * @inheritDoc
	 */
 	@Override
 	protected boolean isSplitable(JobContext context, Path file) {
 		
 		try {
			if (this.recordLength == -1) {
				this.recordLength = getRecordLength(context.getConfiguration());
			}
			LOG.info("FixedLengthInputFormat: my fixed record length is: " + recordLength);

 		} catch(Exception e) {
 			LOG.error("Error in FixedLengthInputFormat.isSplitable() when trying to determine the fixed record length, returning false, input files will NOT be split!",e);
 			return false;
 		}
		
 		CompressionCodec codec = new CompressionCodecFactory(context.getConfiguration()).getCodec(file);
	 	if (codec != null) {
	 		return false; 
	 	}
	 	
	 	return true; 
	 } 
	
}
