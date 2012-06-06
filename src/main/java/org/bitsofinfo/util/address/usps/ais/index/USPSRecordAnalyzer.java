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
package org.bitsofinfo.util.address.usps.ais.index;

import java.io.Reader;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.PorterStemFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.WhitespaceTokenizer;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;

/**
 * The Lucene analyzer we use for analyzing USPSRecord
 * property values.
 * 
 * This analyzer tokenizes on whitespace, lowercases 
 * all text and then stems all the input tokens via a
 * PorterStemFilter.
 * 
 * We do not do any STOP word analysis as all parts of
 * USPSRecords are important (particurally state identifiers
 * and street/address line components)
 * 
 * @author bitsofinfo.g [at] gmail [dot] com
 *
 */
public class USPSRecordAnalyzer extends Analyzer {

	@Override
	public TokenStream tokenStream(String arg0, Reader reader) {
		TokenStream result = new WhitespaceTokenizer(reader);
		result = new LowerCaseFilter(result);
		result = new PorterStemFilter(result);
		return result;
	}
	
	/**
	* Filters a string or word
	* through same filters as when doc is indexed
	*
	* @param      words   String word
	* @return     words	that are analyzed
	*/
	public String filter(String words) {
		StringReader reader = new StringReader(words);
		TokenStream stream = tokenStream(null, reader);
		StringBuffer sb = new StringBuffer();
	
		try {
			while(stream.incrementToken()) {
				sb.append(stream.getAttribute(TermAttribute.class).term());
				sb.append(" ");
			}
		} catch(Exception e) {
			System.out.println("Error in MrmAnalyzer filter(): " + e);
		}
	
		return sb.toString().trim();
	}

}
