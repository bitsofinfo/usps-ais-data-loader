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

import org.bitsofinfo.util.address.usps.ais.OddEvenCode;

/**
 * AddressRange is a utility class when working with USPS AIS data,
 * specifically primary/secondary address number low/high fields.
 * 
 * You take an primary/secondary address number low/high combination
 * and feed those into AddressNumbers, then pass the matching set
 * into AddressRangew which can then be used as an Iterator to
 * crawl through all the possible address numbers represented by
 * the low/high pair.
 * 
 * i.e.
 * 
 * primary   address number low = 00-0700A
 * primary address number high = 00-0800E
 * 
 * x = new AddressRange(new AddressNumber("00-0700A"),new AddressNumber("00-0800B"));
 * Iterator i =  x.iterator();
 * while(i.hasNext) {
 *   
 *   00700A
 *   00700B
 *   00701A
 *   00701B
 *   00702A
 *   ...
 *   ...
 * }
 *
 * 
 * @author bitsofinfo.g [at] gmail [dot] com
 *
 */
public class AddressRange implements Iterator<String> {
	
	private List<String> allAddresses = new ArrayList<String>();
	private Iterator<String> addressIterator;
	
	public AddressRange(AddressNumber low, AddressNumber high, OddEvenCode oec) {
		
		List<AddressPartRange> partRanges = new ArrayList<AddressPartRange>();
		for (int i=0; i<low.parts.size(); i++) {
			AddressPart plow = low.parts.get(i);
			AddressPart phigh = high.parts.get(i);
			AddressPartRange apr = new AddressPartRange(plow,phigh,oec);
			partRanges.add(apr);
		}
		

		this.createAddresses(new ArrayList<String>(), partRanges, 0, allAddresses);
		
		reset();
	}
	
	public boolean isInRange(String addressNumber) {
		return allAddresses.contains(addressNumber);
	}
	
	private void createAddresses(List<String> currParts, List<AddressPartRange> ranges, int rangeIndex, List<String> allAddys) {
		
		AddressPartRange currRange = ranges.get(rangeIndex);
		int nextRangeIndex = rangeIndex+1;
		Iterator<String> rangeI = currRange.iterator();
	
		while(rangeI.hasNext()) {
			String p = rangeI.next();

			if (nextRangeIndex == ranges.size()) {
				StringBuffer sb = new StringBuffer();
				for(String part : currParts) {
					sb.append(part);
				}
				allAddys.add(sb.toString() + p);
				
			} else {
				currParts.add(p);
				createAddresses(currParts,ranges,nextRangeIndex,allAddys);
				currParts.clear();
				//nextRangeIndex--;
				//nextRangeIndex = rangeIndex+1;
			}
		}
	}
	
	public void reset() {
		this.addressIterator = this.allAddresses.iterator();
	}
	
	@Override
	public boolean hasNext() {
		return this.addressIterator.hasNext();
	}

	@Override
	public String next() {
		return this.addressIterator.next();
	}

	@Override
	public void remove() {
		// do nothing
	}
	
}