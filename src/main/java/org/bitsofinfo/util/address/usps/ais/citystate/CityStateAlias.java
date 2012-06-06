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
package org.bitsofinfo.util.address.usps.ais.citystate;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.bitsofinfo.util.address.usps.ais.CopyrightDetailCode;
import org.bitsofinfo.util.address.usps.ais.CopyrightedUSPSRecord;
import org.bitsofinfo.util.address.usps.ais.OddEvenCode;
import org.bitsofinfo.util.address.usps.ais.USPSProductType;
import org.bitsofinfo.util.address.usps.ais.annotations.USPSDataFileField;
import org.bitsofinfo.util.address.usps.ais.annotations.USPSIdentifierField;
import org.bitsofinfo.util.address.usps.ais.annotations.USPSKeyField;
import org.bitsofinfo.util.address.usps.ais.annotations.USPSRecordContext;


/**
 * CityStateAlias represents a USPS Alias record as defined
 * in the USPS AIS city/state (alias section) product
 *  
 * @author bitsofinfo.g [at] gmail [dot] com
 * @see USPS Address Products PDF  (City State Alias Record) (page 17)
 *  
 */
@USPSRecordContext(productTypes={USPSProductType.CITY_STATE},
			 copyrightDetailCode=CopyrightDetailCode.A)
@Entity
public class CityStateAlias extends CopyrightedUSPSRecord {

	@USPSIdentifierField
	@USPSKeyField
	@USPSDataFileField(start=2,length=5)
	@Column(length=5)
	private String zipCode;
	
	@USPSDataFileField(start=7,length=2)
	@Column(length=2)
	@USPSIdentifierField
	private String aliasStreetPreDirectionalAbbr;
	
	@USPSKeyField
	@USPSDataFileField(start=9,length=28)
	@Column(length=28)
	@USPSIdentifierField
	private String aliasStreetName;
	
	@USPSDataFileField(start=37,length=4)
	@Column(length=4)
	@USPSIdentifierField
	private String aliasStreetSuffixAbbr;
	
	@USPSDataFileField(start=41,length=2)
	@Column(length=2)
	@USPSIdentifierField
	private String aliasStreetPostDirectionalAbbr;
	
	@USPSDataFileField(start=43,length=2)
	@Column(length=2)
	@USPSIdentifierField
	private String streetPreDirectionalAbbr;
	
	@USPSKeyField
	@USPSDataFileField(start=45,length=28)
	@Column(length=28)
	@USPSIdentifierField
	private String streetName;
	
	@USPSDataFileField(start=73,length=4)
	@Column(length=4)
	@USPSIdentifierField
	private String streetSuffixAbbr;
	
	@USPSDataFileField(start=77,length=2)
	@Column(length=2)
	@USPSIdentifierField
	private String streetPostDirectionalAbbr;
	
	@USPSDataFileField(start=79,length=1)
	@Column(length=1)
	@USPSIdentifierField
	private AliasTypeCode aliasTypeCode;
	
	@USPSIdentifierField
	@USPSDataFileField(start=80,length=2)
	@Column(length=2)
	private String aliasCentury;
	
	@USPSIdentifierField
	@USPSDataFileField(start=82,length=2)
	@Column(length=2)
	private String aliasYear;
	
	@USPSIdentifierField
	@USPSDataFileField(start=84,length=2)
	@Column(length=2)
	private String aliasMonth;
	
	@USPSIdentifierField
	@USPSDataFileField(start=86,length=2)
	@Column(length=2)
	private String aliasDay;
	
	@USPSDataFileField(start=88,length=10)
	@Column(length=10)
	@USPSIdentifierField
	private String aliasDeliveryAddrLowNumber;
	
	@USPSDataFileField(start=98,length=10)
	@Column(length=10)
	@USPSIdentifierField
	private String aliasDeliveryAddrHighNumber;
	
	@USPSDataFileField(start=108,length=1)
	@Column(length=1)
	@USPSIdentifierField
	private OddEvenCode aliasRangeOddEvenCode;
	
	
	
	
	public String getAliasDate() {
		return aliasCentury + aliasYear + aliasMonth + aliasDay;
	}
	
	
	public String getStreetInformation() {
		return streetPreDirectionalAbbr + 
			   streetName +
			   streetSuffixAbbr +
			   streetPostDirectionalAbbr;
	}
	
	public String getAliasStreetInformation() {
		return aliasStreetPreDirectionalAbbr + 
			   aliasStreetName +
			   aliasStreetSuffixAbbr +
			   aliasStreetPostDirectionalAbbr;
	}
	
	/**
	 * @return the zipCode
	 */
	public String getZipCode() {
		return zipCode;
	}


	/**
	 * @param zipCode the zipCode to set
	 */
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}


	/**
	 * @return the aliasStreetPreDirectionalAbbr
	 */
	public String getAliasStreetPreDirectionalAbbr() {
		return aliasStreetPreDirectionalAbbr;
	}


	/**
	 * @param aliasStreetPreDirectionalAbbr the aliasStreetPreDirectionalAbbr to set
	 */
	public void setAliasStreetPreDirectionalAbbr(
			String aliasStreetPreDirectionalAbbr) {
		this.aliasStreetPreDirectionalAbbr = aliasStreetPreDirectionalAbbr;
	}


	/**
	 * @return the aliasStreetName
	 */
	public String getAliasStreetName() {
		return aliasStreetName;
	}


	/**
	 * @param aliasStreetName the aliasStreetName to set
	 */
	public void setAliasStreetName(String aliasStreetName) {
		this.aliasStreetName = aliasStreetName;
	}


	/**
	 * @return the aliasStreetSuffixAbbr
	 */
	public String getAliasStreetSuffixAbbr() {
		return aliasStreetSuffixAbbr;
	}


	/**
	 * @param aliasStreetSuffixAbbr the aliasStreetSuffixAbbr to set
	 */
	public void setAliasStreetSuffixAbbr(String aliasStreetSuffixAbbr) {
		this.aliasStreetSuffixAbbr = aliasStreetSuffixAbbr;
	}


	/**
	 * @return the aliasStreetPostDirectionalAbbr
	 */
	public String getAliasStreetPostDirectionalAbbr() {
		return aliasStreetPostDirectionalAbbr;
	}


	/**
	 * @param aliasStreetPostDirectionalAbbr the aliasStreetPostDirectionalAbbr to set
	 */
	public void setAliasStreetPostDirectionalAbbr(
			String aliasStreetPostDirectionalAbbr) {
		this.aliasStreetPostDirectionalAbbr = aliasStreetPostDirectionalAbbr;
	}


	/**
	 * @return the streetPreDirectionalAbbr
	 */
	public String getStreetPreDirectionalAbbr() {
		return streetPreDirectionalAbbr;
	}


	/**
	 * @param streetPreDirectionalAbbr the streetPreDirectionalAbbr to set
	 */
	public void setStreetPreDirectionalAbbr(String streetPreDirectionalAbbr) {
		this.streetPreDirectionalAbbr = streetPreDirectionalAbbr;
	}


	/**
	 * @return the streetName
	 */
	public String getStreetName() {
		return streetName;
	}


	/**
	 * @param streetName the streetName to set
	 */
	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}


	/**
	 * @return the streetSuffixAbbr
	 */
	public String getStreetSuffixAbbr() {
		return streetSuffixAbbr;
	}


	/**
	 * @param streetSuffixAbbr the streetSuffixAbbr to set
	 */
	public void setStreetSuffixAbbr(String streetSuffixAbbr) {
		this.streetSuffixAbbr = streetSuffixAbbr;
	}


	/**
	 * @return the streetPostDirectionalAbbr
	 */
	public String getStreetPostDirectionalAbbr() {
		return streetPostDirectionalAbbr;
	}


	/**
	 * @param streetPostDirectionalAbbr the streetPostDirectionalAbbr to set
	 */
	public void setStreetPostDirectionalAbbr(String streetPostDirectionalAbbr) {
		this.streetPostDirectionalAbbr = streetPostDirectionalAbbr;
	}


	/**
	 * @return the aliasTypeCode
	 */
	public AliasTypeCode getAliasTypeCode() {
		return aliasTypeCode;
	}


	/**
	 * @param aliasTypeCode the aliasTypeCode to set
	 */
	public void setAliasTypeCode(AliasTypeCode aliasTypeCode) {
		this.aliasTypeCode = aliasTypeCode;
	}


	/**
	 * @return the aliasCentury
	 */
	public String getAliasCentury() {
		return aliasCentury;
	}


	/**
	 * @param aliasCentury the aliasCentury to set
	 */
	public void setAliasCentury(String aliasCentury) {
		this.aliasCentury = aliasCentury;
	}


	/**
	 * @return the aliasYear
	 */
	public String getAliasYear() {
		return aliasYear;
	}


	/**
	 * @param aliasYear the aliasYear to set
	 */
	public void setAliasYear(String aliasYear) {
		this.aliasYear = aliasYear;
	}


	/**
	 * @return the aliasMonth
	 */
	public String getAliasMonth() {
		return aliasMonth;
	}


	/**
	 * @param aliasMonth the aliasMonth to set
	 */
	public void setAliasMonth(String aliasMonth) {
		this.aliasMonth = aliasMonth;
	}


	/**
	 * @return the aliasDay
	 */
	public String getAliasDay() {
		return aliasDay;
	}


	/**
	 * @param aliasDay the aliasDay to set
	 */
	public void setAliasDay(String aliasDay) {
		this.aliasDay = aliasDay;
	}


	/**
	 * @return the aliasRangeOddEvenCode
	 */
	public OddEvenCode getAliasRangeOddEvenCode() {
		return aliasRangeOddEvenCode;
	}


	/**
	 * @param aliasRangeOddEvenCode the aliasRangeOddEvenCode to set
	 */
	public void setAliasRangeOddEvenCode(OddEvenCode aliasRangeOddEvenCode) {
		this.aliasRangeOddEvenCode = aliasRangeOddEvenCode;
	}


	/**
	 * @return the aliasDeliveryAddrLowNumber
	 */
	public String getAliasDeliveryAddrLowNumber() {
		return aliasDeliveryAddrLowNumber;
	}


	/**
	 * @param aliasDeliveryAddrLowNumber the aliasDeliveryAddrLowNumber to set
	 */
	public void setAliasDeliveryAddrLowNumber(String aliasDeliveryAddrLowNumber) {
		this.aliasDeliveryAddrLowNumber = aliasDeliveryAddrLowNumber;
	}


	/**
	 * @return the aliasDeliveryAddrHighNumber
	 */
	public String getAliasDeliveryAddrHighNumber() {
		return aliasDeliveryAddrHighNumber;
	}


	/**
	 * @param aliasDeliveryAddrHighNumber the aliasDeliveryAddrHighNumber to set
	 */
	public void setAliasDeliveryAddrHighNumber(String aliasDeliveryAddrHighNumber) {
		this.aliasDeliveryAddrHighNumber = aliasDeliveryAddrHighNumber;
	}





}
