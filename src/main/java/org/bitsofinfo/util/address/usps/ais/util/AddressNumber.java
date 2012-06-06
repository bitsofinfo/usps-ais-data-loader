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
import java.util.List;

import org.bitsofinfo.util.address.usps.ais.util.AddressPart;

/**
 * AddressNumber is a utility class to be used with the
 * USPS AIS primary/secondary address number low/high fields.
 * 
 * This class will parse the given address field according to the
 * rules defined in the AIS data guide where "-" mean the start 
 * of significant zeros in the address. The address field is stored
 * as separate AddressParts. One part for each alpha, numeric 
 * and other (non digit/alpha) portions.
 * 
 * i.e. given "0000-0831A-FF02 E"
 *  
 * Will net an AddressNumber with 7 parts
 * 
 * 1 = digit part of "00831"
 * 2 = alpha part of "A"
 * 3 = other part of "-"
 * 4 = alpha part of "FF"
 * 5 = digit part of "02"
 * 6 = other part of " "
 * 7 = alpha part of "E"
 * 
 * @author bitsofinfo.g [at] gmail [dot] com
 *
 */
public class AddressNumber {
	
	public String source = null;
	public List<AddressPart> parts = new ArrayList<AddressPart>();
	
	public AddressNumber(String number) throws Exception {
		
		if (number == null) {
			return;
		}
		
		this.source = number.trim();
		
		AddressPart currentPart = new AddressPart();
		
		for (char c : source.toCharArray()) {

			// significant leading 0
			if (c == '-' && currentPart.isUnknownType()) {
				currentPart = new AddressPart();
				currentPart.addLeadingZero();

				
			} else if (c == '0') {
				currentPart.addZero();
				
			
			} else {

				// digits
				if (Character.isDigit(c)) {
					
					if (!currentPart.isDigit() && !currentPart.isUnknownType()) {
						parts.add(currentPart.finish());
						currentPart = new AddressPart();
					}
					
					currentPart.addDigit(c);
					
					
				// letter
				} else if (Character.isLetter(c)) {
					
					if (!currentPart.isAlpha() && !currentPart.isUnknownType()) {
						parts.add(currentPart.finish());
						currentPart = new AddressPart();
					}
					
					currentPart.addAlpha(c);
					
					
				} else {
					
					if (!currentPart.isOther() && !currentPart.isUnknownType()) {
						parts.add(currentPart.finish());
						currentPart = new AddressPart();
					}
					
					currentPart.addOther(c);
				}
				
			}
		}
		
		parts.add(currentPart.finish());
	}
	

	
	
	
	public String getNumber() {
		StringBuffer tmp = new StringBuffer();
		for (AddressPart p : this.parts) {
			tmp.append(p.data);
		}
		return tmp.toString();
	}


	
}