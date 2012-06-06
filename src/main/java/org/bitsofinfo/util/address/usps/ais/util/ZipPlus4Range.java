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
package org.bitsofinfo.util.address.usps.ais.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * ZipPlus4Range can be used to iterate through all
 * the available ZIP+4 numbers for a given range
 * as specified by the Zip+4 LOW/HIGH sector/segment
 * numbers as defined in the AIS Z+4 detail records.
 * 
 * Callers should call "isSingleCode()" first to 
 * determine if the range is only ONE in length,
 * if so callers can optionally call getSingleCode()
 * instead of using the Iterator functionality.
 * 
 * i.e.
 * 
 * z = new ZipPlus4Range("22","33","44","55");
 * z.next() = 2244;
 * z.next() = 2245;
 * ...
 * z.next() = 3355;
 * 
 * OR for "non-delivery" segments...
 * 
 * z = new ZipPlus4Range("22","33","ND","ND");
 * z.next() = 22ND;
 * z.next() = 23ND;
 * ...
 * z.next() = 33ND;
 * 
 * OR for 
 * 
 * z = new ZipPlus4Range("00","33","00","88");
 * z.next() = 0033;
 * z.next() = 0034;
 * ...
 * z.next() = 0088;
 * 
 * @author bitsofinfo.g [at] gmail [dot] com
 *
 */
public class ZipPlus4Range implements Iterator<String> {

	private static String NON_DELIVERY_SEGMENT_ID = "ND";
	
	private boolean isNonDeliverySegment = false;
	private List<String> plus4s = new ArrayList<String>();
	private Iterator<String> plus4sIterator = null;
	
	public ZipPlus4Range(String lowSectorStr, String highSectorStr,
						 String lowSegmentStr, String highSegmentStr) {
		
		int lowSector = Integer.parseInt(lowSectorStr);
		int highSector = Integer.parseInt(highSectorStr);
		
		boolean nonDeliverySegment = false;
		
		if (lowSegmentStr.trim().equalsIgnoreCase(NON_DELIVERY_SEGMENT_ID) ||
			highSegmentStr.trim().equalsIgnoreCase(NON_DELIVERY_SEGMENT_ID)) {
			
			for (int i=lowSector; i<=highSector; i++) {
				plus4s.add((i < 10 ? "0" : "") + i + NON_DELIVERY_SEGMENT_ID);
			}

			nonDeliverySegment = true;
			
		} else {
			int lowPlus4 = Integer.parseInt(lowSectorStr + lowSegmentStr);
			int highPlus4 = Integer.parseInt(highSectorStr +highSegmentStr);
			
			for (int i=lowPlus4; i<=highPlus4; i++) {
				String padding = "";
				if (i < 10) {
					padding = "000";
				} else if (i < 100) {
					padding = "00";
				} else if (i < 1000) {
					padding = "0";
				}
				plus4s.add(padding + String.valueOf(i));
			}
		}
		
		reset();

	}
	
	public boolean isInRange(String zipPlus4) {
		return plus4s.contains(zipPlus4);
	}
	
	
	public boolean isNonDeliverySegment() {
		return isNonDeliverySegment;
	}
	
	public boolean isSingleCode() {
		if (plus4s.size() == 1) {
			return true;
		}
		
		return false;
	}
	
	public String getSingleCode() {
		return plus4s.get(0);
	}
	
	public void reset() {
		plus4sIterator = plus4s.iterator();
	}

	@Override
	public boolean hasNext() {
		return plus4sIterator.hasNext();
	}

	@Override
	public String next() {
		return plus4sIterator.next();
	}

	@Override
	public void remove() {
		// do nothing
	}
	

}
