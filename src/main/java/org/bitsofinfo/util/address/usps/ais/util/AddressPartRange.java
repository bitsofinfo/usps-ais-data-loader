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
 * AddressPartRange takes a low/high pair
 * of AddressParts from a low/high pair of
 * AddressNumbers and exposes an Iterator
 * to walk through the low to high possibilities
 * for that AddressPart.
 * 
 * @author bitsofinfo.g [at] gmail [dot] com
 *
 */
public class AddressPartRange {
	
	private OddEvenCode oec = null;
	private AddressPart low = null;
	private AddressPart high = null;
	private List<String> items = new ArrayList<String>();
	private char[] alphaset = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
	
	public AddressPartRange(AddressPart low, AddressPart high, OddEvenCode oec) {
		this.low = low;
		this.high = high;
		this.oec = oec;
		
		init();
	}
	
	public Iterator<String> iterator() {
		return items.iterator();
	}
	
	private void init() {

		
		// for digits go from low to high numbers
		if (low.isDigit()) {
			for (int i=low.number; i<=high.number; i++) {
				
				boolean relevant = false;
				
				// odds?
				if (oec == OddEvenCode.O) {
					if (i % 2 != 0) {
						relevant = true;
					}
					
				// even
				} else if (oec == OddEvenCode.E) {
					if (i % 2 == 0) {
						relevant = true;
					}
				
				// both
			    } else if (oec == OddEvenCode.B) {
			    	relevant = true;
			    }
					
				if (relevant) {
					StringBuffer item = new StringBuffer(low.zeros);
					item.append(i);
					items.add(item.toString());
				}
			}
		}
		
		// for alphas
		if (low.isAlpha()) {
			boolean add = false;
			char lowChar = low.alpha.charAt(low.alpha.length()-1);
			char highChar = high.alpha.charAt(high.alpha.length()-1);
			String prefix = low.alpha.substring(0,high.alpha.length()-1);
			
			for (int i=0; i<alphaset.length; i++) {
				char c = alphaset[i];
				
				if (c == lowChar) {
					add=true;
				}

				if (add) {
					items.add(new String(prefix + c));
					if (c == highChar) {
						add=false;
					}
				}
				
			}
		}
		
		if (low.isOther()) {
			items.add(low.data);
		}
	}
	
	private void processPartSet(AddressPart partLow, AddressPart partHigh, List<StringBuffer> workspace) {
		
		if (partLow.isDigit()) {
			for (int j=partLow.number; j<partHigh.number; j++) {
				StringBuffer addr = new StringBuffer(partLow.zeros);
				workspace.add(addr);
				addr.append(j);	
			}
		}
		
		if (partLow.isOther()) {
			for (int j=partLow.number; j<partHigh.number; j++) {
				StringBuffer addr = new StringBuffer(partLow.zeros);
				workspace.add(addr);
				addr.append(j);	
			}
		}
		
	}


	
}