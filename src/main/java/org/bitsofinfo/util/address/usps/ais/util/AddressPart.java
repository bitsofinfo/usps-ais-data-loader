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

/**
 * AddressPart is used by AddressNumber. 
 * 
 * An AddressPart can represent a set of 
 * numeric, alpha or other characters. Multiple
 * AddressPart's can be put together to reformulate
 * the original USPS AIS primary/secondary address number
 * low/high field. 
 * 
 * AddressPart is also used by AddressPartRange to
 * create a valid ranges of a given part for a pair
 * of low-high AddressParts from the same AddressNumber set
 * 
 * @author bitsofinfo.g [at] gmail [dot] com
 *
 */
public class AddressPart {
	
	public static final String TYPE_DIGIT = "digit";
	public static final String TYPE_ALPHA = "alpha";
	public static final String TYPE_OTHER = "other";
	
	
	
	private boolean currentlyLeadingZeros = false;
	private StringBuffer rawData = new StringBuffer();
	private StringBuffer leadingZeros = new StringBuffer();
	public String data = "";
	public String type = "";
	public String zeros = "";
	
	private StringBuffer relevantDigits = new StringBuffer();
	public int number;
	
	private StringBuffer relevantAlphas = new StringBuffer();
	public String alpha;
	
	public boolean isUnknownType() {
		return this.type.equals("");
	}
	
	public boolean isOther() {
		return this.type.equals(TYPE_OTHER);
	}
	
	public boolean isDigit() {
		return this.type.equals(TYPE_DIGIT);
	}
	
	public boolean isAlpha() {
		return this.type.equals(TYPE_ALPHA);
	}
	
	public void addLeadingZero() {
		currentlyLeadingZeros = true;
		addZero();
	}
	
	public void addZero() {
		if (!currentlyLeadingZeros) {
			if (type.equals(TYPE_DIGIT)) {
				relevantDigits.append('0');
			} else if (type.equals(TYPE_ALPHA)) {
				relevantAlphas.append('0');
			}
			
			// in both cases retain in raw data
			if (!isUnknownType()) {
				rawData.append('0');
			} else {
				// do nothing, not leading zeros and we have NO type yet
			}
			
		} else {
			rawData.append('0');
			leadingZeros.append('0');
		}
	}
	
	// Neither a digit nor alpha
	public void addOther(char c) throws Exception {
		type = TYPE_OTHER;
		
		if (Character.isDigit(c) || Character.isLetter(c)) {
			throw new Exception("Call addDigit/Alpha() when adding letters/numbers");
		}
		
		currentlyLeadingZeros = false;
		rawData.append(c);
	}
	
	public void addDigit(char digit) throws Exception {
		type = TYPE_DIGIT;
		
		if (digit == '0') {
			throw new Exception("Call addZero() when adding zeros");
		}
		
		currentlyLeadingZeros = false;
		relevantDigits.append(digit);
		rawData.append(digit);
	}
	
	public void addAlpha(char alpha) throws Exception {
		type = TYPE_ALPHA;

		
		if (alpha == '0') {
			throw new Exception("Call addZero() when adding zeros");
		}
		
		
		currentlyLeadingZeros = false;
		relevantAlphas.append(alpha);
		rawData.append(alpha);

	}
	
	public AddressPart finish() {
		
		if (type.equals(TYPE_DIGIT)) {
			number = Integer.valueOf(relevantDigits.toString()).intValue();

		} else {
			alpha = relevantAlphas.toString();
		}
		
		zeros = leadingZeros.toString();
		data = rawData.toString();
		
		return this;
	}
}
