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
package org.bitsofinfo.util.address.usps.ais.zipplus4;

import java.util.ArrayList;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.bitsofinfo.util.address.usps.ais.ActionCode;
import org.bitsofinfo.util.address.usps.ais.CopyrightDetailCode;
import org.bitsofinfo.util.address.usps.ais.CopyrightedUSPSRecord;
import org.bitsofinfo.util.address.usps.ais.LACSStatusIndicator;
import org.bitsofinfo.util.address.usps.ais.OddEvenCode;
import org.bitsofinfo.util.address.usps.ais.USPSProductType;
import org.bitsofinfo.util.address.usps.ais.annotations.USPSDataFileField;
import org.bitsofinfo.util.address.usps.ais.annotations.USPSIdentifierField;
import org.bitsofinfo.util.address.usps.ais.annotations.USPSKeyField;
import org.bitsofinfo.util.address.usps.ais.annotations.USPSRecordContext;

/**
 * USPSZipPlus4Detail represents an USPS Zip+4 record as
 * defined in the AIS Zip Plus 4 product.
 * 
 * @author bitsofinfo.g [at] gmail [dot] com
 * @see USPS Address Products PDF (Zip+4 Records) (page 33-35)
 */ 
@Entity
@USPSRecordContext(productTypes={USPSProductType.ZIP_PLUS_4}, 
	 		copyrightDetailCode=CopyrightDetailCode.D)
public class ZipPlus4Detail extends CopyrightedUSPSRecord {

	@USPSKeyField
	@USPSDataFileField(start=2,length=5)
	@Column(length=5)
	private String zipCode; 

	@USPSIdentifierField
	@USPSKeyField
	@USPSDataFileField(start=7,length=10)
	@Column(length=10)
	private String updateKeyNumber;
	
	@USPSDataFileField(start=17,length=1)
	@Enumerated(EnumType.STRING)
	@Column(length=1)
	private ActionCode actionCode;
	
	@USPSDataFileField(start=18,length=1)
	@Column(length=1)
	@Enumerated(EnumType.STRING)
	private RecordType recordType;
	
	@USPSKeyField
	@USPSDataFileField(start=19,length=4)
	@Column(length=4)
	private String carrierRouteId;
	
	@USPSDataFileField(start=23,length=2)
	@Column(length=2)
	private String streetPreDirectionalAbbr;
	
	@USPSKeyField
	@USPSDataFileField(start=25,length=28)
	@Column(length=28)
	private String streetName;
	
	@USPSDataFileField(start=53,length=4)
	@Column(length=4)
	private String streetSuffixAbbr;
	
	@USPSDataFileField(start=57,length=2)
	@Column(length=2)
	private String streetPostDirectionalAbbr;
	
	@USPSDataFileField(start=59,length=10)
	@Column(length=10)
	private String addressPrimaryLowNum;
	
	@USPSDataFileField(start=69,length=10)
	@Column(length=10)
	private String addressPrimaryHighNum;
	
	@USPSDataFileField(start=79,length=1)
	@Column(length=1)
	private OddEvenCode addrPrimaryOddEvenCode;
	
	@USPSKeyField
	@USPSDataFileField(start=80,length=40)
	@Column(length=40)
	private String buildingOrFirmName;
	
	@USPSDataFileField(start=120,length=4)
	@Column(length=4)
	private String addressSecondaryAbbr;
	
	@USPSDataFileField(start=124,length=8)
	@Column(length=8)
	private String addressSecondaryLowNum;
	
	@USPSDataFileField(start=132,length=8)
	@Column(length=8)
	private String addressSecondaryHighNum;
	
	@USPSDataFileField(start=140,length=1)
	@Enumerated(EnumType.STRING)
	@Column(length=1)
	private OddEvenCode addrSecondaryOddEvenCode;
	
	@USPSDataFileField(start=141,length=2)
	@Column(length=2)
	private String plus4LowZipSectorNumber;

	@USPSDataFileField(start=143,length=2)
	@Column(length=2)
	private String plus4LowZipSegmentNumber;
	
	@USPSDataFileField(start=145,length=2)
	@Column(length=2)
	private String plus4HighZipSectorNumber;
	
	@USPSDataFileField(start=147,length=2)
	@Column(length=2)
	private String plus4HighZipSegmentNumber;
	
	@USPSDataFileField(start=149,length=1)
	@Column(length=1)
	@Enumerated(EnumType.STRING)
	private BaseAlternateCode baseAlternateCode;
	
	@USPSDataFileField(start=150,length=1)
	@Column(length=1)
	@Enumerated(EnumType.STRING)
	private LACSStatusIndicator lacsStatusIndicator;
	
	@USPSDataFileField(start=151,length=1)
	@Column(length=1)
	@Enumerated(EnumType.STRING)
	private GovtBuildingIndicator govtBuildingIndicator;
	
	@USPSDataFileField(start=152,length=6)
	@Column(length=6)
	private String financeNumber;
	
	@USPSKeyField
	@USPSDataFileField(start=158,length=2)
	@Column(length=2)
	private String stateAbbr;
	
	@USPSKeyField
	@USPSDataFileField(start=160,length=3)
	@Column(length=3)
	private String countyNumber;
	
	@USPSDataFileField(start=163,length=2)
	@Column(length=2)
	private String congressionalDistrictNumber;
	
	@USPSDataFileField(start=165,length=6)
	@Column(length=6)
	private String muniCityStateKey;
	
	@USPSDataFileField(start=171,length=6)
	@Column(length=6)
	private String urbanCityStateKey;
	
	@USPSKeyField
	@USPSDataFileField(start=177,length=6)
	@Column(length=6)
	private String preferredLastLineCityStateKey;
	
	private String formatZipPlus4(int plus4) {
		return getZipCode() +"-"+plus4;
	}
	
	
	public ArrayList<String> getFormattedZipPlus4Codes() {
		ArrayList<String> codes = new ArrayList<String>();
		
		// do we need to give a range?
		if (!this.getPlus4HighNumber().equals(this.getPlus4LowNumber())) {

			for (int i=this.getPlus4LowNumberAsInt(); i <= this.getPlus4HighNumberAsInt(); i++) {
				codes.add(formatZipPlus4(i));
			}
			
		// nope, return one
		} else {
			codes.add(formatZipPlus4(getPlus4HighNumberAsInt()));
		}
		
		return codes;
	}

	public Integer getPlus4LowNumberAsInt() {
		return Integer.parseInt(this.getPlus4LowNumber());
	}
	
	public Integer getPlus4HighNumberAsInt() {
		return Integer.parseInt(this.getPlus4HighNumber());
	}
	
	public String getPlus4HighNumber() {
		return this.plus4HighZipSectorNumber + this.plus4HighZipSegmentNumber;
	}
	
	public String getPlus4LowNumber() {
		return this.plus4LowZipSectorNumber + this.plus4LowZipSegmentNumber;
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
	 * @return the updateKeyNumber
	 */
	public String getUpdateKeyNumber() {
		return updateKeyNumber;
	}

	/**
	 * @param updateKeyNumber the updateKeyNumber to set
	 */
	public void setUpdateKeyNumber(String updateKeyNumber) {
		this.updateKeyNumber = updateKeyNumber;
	}


	/**
	 * @return the recordType
	 */
	public RecordType getRecordType() {
		return recordType;
	}

	/**
	 * @param recordType the recordType to set
	 */
	public void setRecordType(RecordType recordType) {
		this.recordType = recordType;
	}

	/**
	 * @return the carrierRouteId
	 */
	public String getCarrierRouteId() {
		return carrierRouteId;
	}

	/**
	 * @param carrierRouteId the carrierRouteId to set
	 */
	public void setCarrierRouteId(String carrierRouteId) {
		this.carrierRouteId = carrierRouteId;
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
	 * @param streetSuffix the streetSuffixAbbr to set
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
	 * @return the addressPrimaryLowNum
	 */
	public String getAddressPrimaryLowNum() {
		return addressPrimaryLowNum;
	}

	/**
	 * @param addressPrimaryLowNum the addressPrimaryLowNum to set
	 */
	public void setAddressPrimaryLowNum(String addressPrimaryLowNum) {
		this.addressPrimaryLowNum = addressPrimaryLowNum;
	}

	/**
	 * @return the addressPrimaryHighNum
	 */
	public String getAddressPrimaryHighNum() {
		return addressPrimaryHighNum;
	}

	/**
	 * @param addressPrimaryHighNum the addressPrimaryHighNum to set
	 */
	public void setAddressPrimaryHighNum(String addressPrimaryHighNum) {
		this.addressPrimaryHighNum = addressPrimaryHighNum;
	}

	/**
	 * @return the addrPrimaryOddEvenCode
	 */
	public OddEvenCode getAddrPrimaryOddEvenCode() {
		return addrPrimaryOddEvenCode;
	}

	/**
	 * @param addrPrimaryOddEvenCode the addrPrimaryOddEvenCode to set
	 */
	public void setAddrPrimaryOddEvenCode(OddEvenCode addrPrimaryOddEvenCode) {
		this.addrPrimaryOddEvenCode = addrPrimaryOddEvenCode;
	}

	/**
	 * @return the buildingOrFirmName
	 */
	public String getBuildingOrFirmName() {
		return buildingOrFirmName;
	}

	/**
	 * @param buildingOrFirmName the buildingOrFirmName to set
	 */
	public void setBuildingOrFirmName(String buildingOrFirmName) {
		this.buildingOrFirmName = buildingOrFirmName;
	}

	/**
	 * @return the addressSecondaryAbbr
	 */
	public String getAddressSecondaryAbbr() {
		return addressSecondaryAbbr;
	}

	/**
	 * @param addressSecondaryAbbr the addressSecondaryAbbr to set
	 */
	public void setAddressSecondaryAbbr(String addressSecondaryAbbr) {
		this.addressSecondaryAbbr = addressSecondaryAbbr;
	}

	/**
	 * @return the addressSecondaryLowNum
	 */
	public String getAddressSecondaryLowNum() {
		return addressSecondaryLowNum;
	}

	/**
	 * @param addressSecondaryLowNum the addressSecondaryLowNum to set
	 */
	public void setAddressSecondaryLowNum(String addressSecondaryLowNum) {
		this.addressSecondaryLowNum = addressSecondaryLowNum;
	}

	/**
	 * @return the addressSecondaryHighNum
	 */
	public String getAddressSecondaryHighNum() {
		return addressSecondaryHighNum;
	}

	/**
	 * @param addressSecondaryHighNum the addressSecondaryHighNum to set
	 */
	public void setAddressSecondaryHighNum(String addressSecondaryHighNum) {
		this.addressSecondaryHighNum = addressSecondaryHighNum;
	}

	/**
	 * @return the addrSecondaryOddEvenCode
	 */
	public OddEvenCode getAddrSecondaryOddEvenCode() {
		return addrSecondaryOddEvenCode;
	}

	/**
	 * @param addrSecondaryOddEvenCode the addrSecondaryOddEvenCode to set
	 */
	public void setAddrSecondaryOddEvenCode(
			OddEvenCode addrSecondaryOddEvenCode) {
		this.addrSecondaryOddEvenCode = addrSecondaryOddEvenCode;
	}

	/**
	 * @return the plus4LowZipSectorNumber
	 */
	public String getPlus4LowZipSectorNumber() {
		return plus4LowZipSectorNumber;
	}

	/**
	 * @param plus4LowZipSectorNumber the plus4LowZipSectorNumber to set
	 */
	public void setPlus4LowZipSectorNumber(String plus4LowZipSectorNumber) {
		this.plus4LowZipSectorNumber = plus4LowZipSectorNumber;
	}

	/**
	 * @return the plus4LowZipSegmentNumber
	 */
	public String getPlus4LowZipSegmentNumber() {
		return plus4LowZipSegmentNumber;
	}

	/**
	 * @param plus4LowZipSegmentNumber the plus4LowZipSegmentNumber to set
	 */
	public void setPlus4LowZipSegmentNumber(String plus4LowZipSegmentNumber) {
		this.plus4LowZipSegmentNumber = plus4LowZipSegmentNumber;
	}

	/**
	 * @return the plus4HighZipSectorNumber
	 */
	public String getPlus4HighZipSectorNumber() {
		return plus4HighZipSectorNumber;
	}

	/**
	 * @param plus4HighZipSectorNumber the plus4HighZipSectorNumber to set
	 */
	public void setPlus4HighZipSectorNumber(String plus4HighZipSectorNumber) {
		this.plus4HighZipSectorNumber = plus4HighZipSectorNumber;
	}

	/**
	 * @return the plus4HighZipSegmentNumber
	 */
	public String getPlus4HighZipSegmentNumber() {
		return plus4HighZipSegmentNumber;
	}

	/**
	 * @param plus4HighZipSegmentNumber the plus4HighZipSegmentNumber to set
	 */
	public void setPlus4HighZipSegmentNumber(String plus4HighZipSegmentNumber) {
		this.plus4HighZipSegmentNumber = plus4HighZipSegmentNumber;
	}

	/**
	 * @return the baseAlternateCode
	 */
	public BaseAlternateCode getBaseAlternateCode() {
		return baseAlternateCode;
	}

	/**
	 * @param baseAlternateCode the baseAlternateCode to set
	 */
	public void setBaseAlternateCode(BaseAlternateCode baseAlternateCode) {
		this.baseAlternateCode = baseAlternateCode;
	}


	/**
	 * @return the govtBuildingIndicator
	 */
	public GovtBuildingIndicator getGovtBuildingIndicator() {
		return govtBuildingIndicator;
	}

	/**
	 * @param govtBuildingIndicator the govtBuildingIndicator to set
	 */
	public void setGovtBuildingIndicator(GovtBuildingIndicator govtBuildingIndicator) {
		this.govtBuildingIndicator = govtBuildingIndicator;
	}

	/**
	 * @return the financeNumber
	 */
	public String getFinanceNumber() {
		return financeNumber;
	}

	/**
	 * @param financeNumber the financeNumber to set
	 */
	public void setFinanceNumber(String financeNumber) {
		this.financeNumber = financeNumber;
	}

	/**
	 * @return the stateAbbr
	 */
	public String getStateAbbr() {
		return stateAbbr;
	}

	/**
	 * @param stateAbbr the stateAbbr to set
	 */
	public void setStateAbbr(String stateAbbr) {
		this.stateAbbr = stateAbbr;
	}

	/**
	 * @return the countyNumber
	 */
	public String getCountyNumber() {
		return countyNumber;
	}

	/**
	 * @param countyNumber the countyNumber to set
	 */
	public void setCountyNumber(String countyNumber) {
		this.countyNumber = countyNumber;
	}

	/**
	 * @return the congressionalDistrictNumber
	 */
	public String getCongressionalDistrictNumber() {
		return congressionalDistrictNumber;
	}

	/**
	 * @param congressionalDistrictNumber the congressionalDistrictNumber to set
	 */
	public void setCongressionalDistrictNumber(String congressionalDistrictNumber) {
		this.congressionalDistrictNumber = congressionalDistrictNumber;
	}

	/**
	 * @return the muniCityStateKey
	 */
	public String getMuniCityStateKey() {
		return muniCityStateKey;
	}

	/**
	 * @param muniCityStateKey the muniCityStateKey to set
	 */
	public void setMuniCityStateKey(String muniCityStateKey) {
		this.muniCityStateKey = muniCityStateKey;
	}

	/**
	 * @return the urbanCityStateKey
	 */
	public String getUrbanCityStateKey() {
		return urbanCityStateKey;
	}

	/**
	 * @param urbanCityStateKey the urbanCityStateKey to set
	 */
	public void setUrbanCityStateKey(String urbanCityStateKey) {
		this.urbanCityStateKey = urbanCityStateKey;
	}

	/**
	 * @return the preferredLastLineCityStateKey
	 */
	public String getPreferredLastLineCityStateKey() {
		return preferredLastLineCityStateKey;
	}

	/**
	 * @param preferredLastLineCityStateKey the preferredLastLineCityStateKey to set
	 */
	public void setPreferredLastLineCityStateKey(
			String preferredLastLineCityStateKey) {
		this.preferredLastLineCityStateKey = preferredLastLineCityStateKey;
	}

	/**
	 * @return the actionCode
	 */
	@Override
	public ActionCode getActionCode() {
		return actionCode;
	}
 
	/**
	 * @param actionCode the actionCode to set
	 */
	public void setActionCode(ActionCode actionCode) {
		this.actionCode = actionCode;
	}


	/**
	 * @return the lacsStatusIndicator
	 */
	public LACSStatusIndicator getLacsStatusIndicator() {
		return lacsStatusIndicator;
	}


	/**
	 * @param lacsStatusIndicator the lacsStatusIndicator to set
	 */
	public void setLacsStatusIndicator(LACSStatusIndicator lacsStatusIndicator) {
		this.lacsStatusIndicator = lacsStatusIndicator;
	}
	

}
