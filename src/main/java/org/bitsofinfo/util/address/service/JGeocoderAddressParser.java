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

package org.bitsofinfo.util.address.service;

import java.util.Map;

import net.sourceforge.jgeocoder.us.AddressParser;
import net.sourceforge.jgeocoder.AddressComponent;

public class JGeocoderAddressParser implements PostalAddressParser {

	public ParseResult parse(ParseRequest request) {
		Map<AddressComponent, String> parsedAddr = AddressParser.parseAddress(request.getRawAddress());
		return new ParseResult(request,parsedAddr.toString());
	}

	public ParseResult parse(String address) {
		return parse(new ParseRequest(address));
	}

}
