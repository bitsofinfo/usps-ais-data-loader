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

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import org.bitsofinfo.util.address.usps.ais.Copyright;
import org.bitsofinfo.util.address.usps.ais.USPSRecord;
import org.bitsofinfo.util.address.usps.ais.USPSUtils;
import org.bitsofinfo.util.address.usps.ais.zipplus4.ZipPlus4Detail;

public class WritableUSPSRecord implements Writable {
	
	private USPSRecord record;
	private Class<? extends USPSRecord> clazz;
	private USPSUtils utils;
	private String json;
	
	public WritableUSPSRecord() {
		// nada, this is done on the deserialize side
	}
	
	public WritableUSPSRecord(USPSRecord target) {
		this.record = target;
		this.clazz = target.getClass();
		this.utils = utils;
	}
	
	@Override
	public String toString() {
		return json;
	}
	
	@Override
	public void readFields(DataInput in) throws IOException {
		
		try {
			int len = in.readInt();
			byte[] data = new byte[len];
			in.readFully(data);
			json = new String(data);
			
			JsonParser parser = new JsonParser();
			String identifier = parser.parse(json).getAsJsonObject().get("identifier").getAsString();
			
			Class clazz;
			if (identifier.indexOf("Z4CS") != -1) {
				clazz = Copyright.class;
			} else {
				clazz = ZipPlus4Detail.class;
			}
			
			
			Gson g = new Gson();
			record = (USPSRecord)g.fromJson(json, clazz);
			
		} catch(Exception e) {
			throw new IOException("Error converting DataInput into JSON -> USPSRecord: " + e.getMessage());
		}
		

	}

	@Override
	public void write(DataOutput out) throws IOException {
		Gson g = new Gson();
		String json = g.toJson(record);
		int len = json.length();
		out.writeInt(len);
		out.writeBytes(json);
	}

}
