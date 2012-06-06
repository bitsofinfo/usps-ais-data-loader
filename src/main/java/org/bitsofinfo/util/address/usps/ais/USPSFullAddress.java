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
package org.bitsofinfo.util.address.usps.ais;

import org.bitsofinfo.util.address.usps.ais.citystate.CarrierRouteRateSortationMerged5DigitIndicator;
import org.bitsofinfo.util.address.usps.ais.citystate.CityDeliveryIndicator;
import org.bitsofinfo.util.address.usps.ais.citystate.FacilityCode;
import org.bitsofinfo.util.address.usps.ais.citystate.MailingNameIndicator;
import org.bitsofinfo.util.address.usps.ais.citystate.SeasonalDeliveryIndicator;
import org.bitsofinfo.util.address.usps.ais.citystate.UniqueZipNameIndicator;
import org.bitsofinfo.util.address.usps.ais.citystate.ZipClassificationCode;
import org.bitsofinfo.util.address.usps.ais.zipplus4.BaseAlternateCode;
import org.bitsofinfo.util.address.usps.ais.zipplus4.GovtBuildingIndicator;
import org.bitsofinfo.util.address.usps.ais.zipplus4.RecordType;

public class USPSFullAddress {
	
	
	// custom exploded props
	private String primaryAddress;   // the actual unique addy
	private String secondaryAddress; // the actual unique secondary addy
	private String fullZipPlus4Code = null;
	private boolean nonDeliverable = false;
	
	//alias
	/*
	private String zipCode;
	private String aliasStreetPreDirectionalAbbr;
	private String aliasStreetName;
	private String aliasStreetSuffixAbbr;
	private String aliasStreetPostDirectionalAbbr;
	private String streetPreDirectionalAbbr;
	private String streetName;
	private String streetSuffixAbbr;
	private String streetPostDirectionalAbbr;
	private AliasTypeCode aliasTypeCode;
	private String aliasCentury;
	private String aliasYear;
	private String aliasMonth;
	private String aliasDay;
	private String aliasDeliveryAddrLowNumber;
	private String aliasDeliveryAddrHighNumber;
	private OddEvenCode aliasRangeOddEvenCode;
	*/
	
	
	

	// cs scheme
	//private String labelZipCode; // group identifier
/*	private String combinedZipCode; // actual zip code in the group
*/

	// cs detail
	private String labelZipCode;
	private String cityStateKey;
	private ZipClassificationCode zipClassificationCode;
	private String name;
	private String nameAbbr;
	private FacilityCode facilityCode;
	private MailingNameIndicator mailingNameIndicator;
	private String preferredLastLineKey;
	private String preferredLastLineName;
	private CityDeliveryIndicator cityDeliveryIndicator;
	private CarrierRouteRateSortationMerged5DigitIndicator carrierRouteRateSortationMerged5DigitIndicator;
	private UniqueZipNameIndicator uniqueZipNameIndicator;
	private String countyName;

	

	//z4detail
	private String zipCode; 
	private String updateKeyNumber;
	private ActionCode actionCode;
	private RecordType recordType;
	private String carrierRouteId;
	private String streetPreDirectionalAbbr;
	private String streetName;
	private String streetSuffixAbbr;
	private String streetPostDirectionalAbbr;
	private String addressPrimaryLowNum;
	private String addressPrimaryHighNum;
	private OddEvenCode addrPrimaryOddEvenCode;
	private String buildingOrFirmName;
	private String addressSecondaryAbbr;
	private String addressSecondaryLowNum;
	private String addressSecondaryHighNum;
	private OddEvenCode addrSecondaryOddEvenCode;
	private String plus4LowZipSectorNumber;
	private String plus4LowZipSegmentNumber;
	private String plus4HighZipSectorNumber;
	private String plus4HighZipSegmentNumber;
	private BaseAlternateCode baseAlternateCode;
	private LACSStatusIndicator lacsStatusIndicator;
	private GovtBuildingIndicator govtBuildingIndicator;
	private String congressionalDistrictNumber;
	private String muniCityStateKey; // key to city state
	private String urbanCityStateKey; // key to city state
	private String preferredLastLineCityStateKey; // key to city state
	private String financeNumber;
	private String stateAbbr;
	private String countyNumber;

	// seasonal
	private SeasonalDeliveryIndicator january;
	private SeasonalDeliveryIndicator february;
	private SeasonalDeliveryIndicator march;
	private SeasonalDeliveryIndicator april;
	private SeasonalDeliveryIndicator may;
	private SeasonalDeliveryIndicator june;
	private SeasonalDeliveryIndicator july;
	private SeasonalDeliveryIndicator august;
	private SeasonalDeliveryIndicator september;
	private SeasonalDeliveryIndicator october;
	private SeasonalDeliveryIndicator november;
	private SeasonalDeliveryIndicator december;
	
	// zone split, sort of an alias for zips
	// if you cannot find a zip in z+4 check the
	// "old" fields here to find the new ones
	/*private String oldZipCode;
	private String oldCarrierRouteId;
	private String newZipCode;
	private String newCarrierRouteId;
	private String transactionCentury;
	private String transactionYear;
	private String transactionMonth;
	private String transactionDay;*/
	


	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.getPrimaryAddress() +" ");
		sb.append((this.streetPreDirectionalAbbr != null ? streetPreDirectionalAbbr.trim()  + " ": ""));
		sb.append(this.getStreetName().trim() + " ");
		sb.append((this.streetPostDirectionalAbbr != null ? streetPostDirectionalAbbr.trim()  + " ": ""));
		sb.append((this.streetPostDirectionalAbbr != null ? streetPostDirectionalAbbr.trim()  + " ": ""));
		sb.append((this.streetSuffixAbbr != null ? streetSuffixAbbr.trim()  + " ": ""));
		sb.append(", ");
		if (this.secondaryAddress != null) {
			sb.append((this.addressSecondaryAbbr != null ? addressSecondaryAbbr.trim()  + " ": ""));
			sb.append(this.getSecondaryAddress() +" ");
			sb.append(", ");
		}
		// fill in city stuff..
		sb.append(this.getCityName().trim() + ", ");	
		sb.append(this.stateAbbr + " ");
		sb.append(this.fullZipPlus4Code +" | ");
		sb.append(this.getCountyName().trim() +" (county)");
		return sb.toString();
		
	}
	
	public String getPrimaryAddress() {
		return primaryAddress;
	}
	public void setPrimaryAddress(String primaryAddress) {
		this.primaryAddress = primaryAddress;
	}
	public String getSecondaryAddress() {
		return secondaryAddress;
	}
	public void setSecondaryAddress(String secondaryAddress) {
		this.secondaryAddress = secondaryAddress;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getUpdateKeyNumber() {
		return updateKeyNumber;
	}
	public void setUpdateKeyNumber(String updateKeyNumber) {
		this.updateKeyNumber = updateKeyNumber;
	}
	public ActionCode getActionCode() {
		return actionCode;
	}
	public void setActionCode(ActionCode actionCode) {
		this.actionCode = actionCode;
	}
	public RecordType getRecordType() {
		return recordType;
	}
	public void setRecordType(RecordType recordType) {
		this.recordType = recordType;
	}
	public String getCarrierRouteId() {
		return carrierRouteId;
	}
	public void setCarrierRouteId(String carrierRouteId) {
		this.carrierRouteId = carrierRouteId;
	}
	public String getStreetPreDirectionalAbbr() {
		return streetPreDirectionalAbbr;
	}
	public void setStreetPreDirectionalAbbr(String streetPreDirectionalAbbr) {
		this.streetPreDirectionalAbbr = streetPreDirectionalAbbr;
	}
	public String getStreetName() {
		return streetName;
	}
	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}
	public String getStreetSuffixAbbr() {
		return streetSuffixAbbr;
	}
	public void setStreetSuffixAbbr(String streetSuffixAbbr) {
		this.streetSuffixAbbr = streetSuffixAbbr;
	}
	public String getStreetPostDirectionalAbbr() {
		return streetPostDirectionalAbbr;
	}
	public void setStreetPostDirectionalAbbr(String streetPostDirectionalAbbr) {
		this.streetPostDirectionalAbbr = streetPostDirectionalAbbr;
	}
	public String getAddressPrimaryLowNum() {
		return addressPrimaryLowNum;
	}
	public void setAddressPrimaryLowNum(String addressPrimaryLowNum) {
		this.addressPrimaryLowNum = addressPrimaryLowNum;
	}
	public String getAddressPrimaryHighNum() {
		return addressPrimaryHighNum;
	}
	public void setAddressPrimaryHighNum(String addressPrimaryHighNum) {
		this.addressPrimaryHighNum = addressPrimaryHighNum;
	}
	public OddEvenCode getAddrPrimaryOddEvenCode() {
		return addrPrimaryOddEvenCode;
	}
	public void setAddrPrimaryOddEvenCode(OddEvenCode addrPrimaryOddEvenCode) {
		this.addrPrimaryOddEvenCode = addrPrimaryOddEvenCode;
	}
	public String getBuildingOrFirmName() {
		return buildingOrFirmName;
	}
	public void setBuildingOrFirmName(String buildingOrFirmName) {
		this.buildingOrFirmName = buildingOrFirmName;
	}
	public String getAddressSecondaryAbbr() {
		return addressSecondaryAbbr;
	}
	public void setAddressSecondaryAbbr(String addressSecondaryAbbr) {
		this.addressSecondaryAbbr = addressSecondaryAbbr;
	}
	public String getAddressSecondaryLowNum() {
		return addressSecondaryLowNum;
	}
	public void setAddressSecondaryLowNum(String addressSecondaryLowNum) {
		this.addressSecondaryLowNum = addressSecondaryLowNum;
	}
	public String getAddressSecondaryHighNum() {
		return addressSecondaryHighNum;
	}
	public void setAddressSecondaryHighNum(String addressSecondaryHighNum) {
		this.addressSecondaryHighNum = addressSecondaryHighNum;
	}
	public OddEvenCode getAddrSecondaryOddEvenCode() {
		return addrSecondaryOddEvenCode;
	}
	public void setAddrSecondaryOddEvenCode(OddEvenCode addrSecondaryOddEvenCode) {
		this.addrSecondaryOddEvenCode = addrSecondaryOddEvenCode;
	}
	public String getPlus4LowZipSectorNumber() {
		return plus4LowZipSectorNumber;
	}
	public void setPlus4LowZipSectorNumber(String plus4LowZipSectorNumber) {
		this.plus4LowZipSectorNumber = plus4LowZipSectorNumber;
	}
	public String getPlus4LowZipSegmentNumber() {
		return plus4LowZipSegmentNumber;
	}
	public void setPlus4LowZipSegmentNumber(String plus4LowZipSegmentNumber) {
		this.plus4LowZipSegmentNumber = plus4LowZipSegmentNumber;
	}
	public String getPlus4HighZipSectorNumber() {
		return plus4HighZipSectorNumber;
	}
	public void setPlus4HighZipSectorNumber(String plus4HighZipSectorNumber) {
		this.plus4HighZipSectorNumber = plus4HighZipSectorNumber;
	}
	public String getPlus4HighZipSegmentNumber() {
		return plus4HighZipSegmentNumber;
	}
	public void setPlus4HighZipSegmentNumber(String plus4HighZipSegmentNumber) {
		this.plus4HighZipSegmentNumber = plus4HighZipSegmentNumber;
	}
	public BaseAlternateCode getBaseAlternateCode() {
		return baseAlternateCode;
	}
	public void setBaseAlternateCode(BaseAlternateCode baseAlternateCode) {
		this.baseAlternateCode = baseAlternateCode;
	}
	public LACSStatusIndicator getLacsStatusIndicator() {
		return lacsStatusIndicator;
	}
	public void setLacsStatusIndicator(LACSStatusIndicator lacsStatusIndicator) {
		this.lacsStatusIndicator = lacsStatusIndicator;
	}
	public GovtBuildingIndicator getGovtBuildingIndicator() {
		return govtBuildingIndicator;
	}
	public void setGovtBuildingIndicator(GovtBuildingIndicator govtBuildingIndicator) {
		this.govtBuildingIndicator = govtBuildingIndicator;
	}
	public String getCongressionalDistrictNumber() {
		return congressionalDistrictNumber;
	}
	public void setCongressionalDistrictNumber(String congressionalDistrictNumber) {
		this.congressionalDistrictNumber = congressionalDistrictNumber;
	}
	public String getMuniCityStateKey() {
		return muniCityStateKey;
	}
	public void setMuniCityStateKey(String muniCityStateKey) {
		this.muniCityStateKey = muniCityStateKey;
	}
	public String getUrbanCityStateKey() {
		return urbanCityStateKey;
	}
	public void setUrbanCityStateKey(String urbanCityStateKey) {
		this.urbanCityStateKey = urbanCityStateKey;
	}
	public String getPreferredLastLineCityStateKey() {
		return preferredLastLineCityStateKey;
	}
	public void setPreferredLastLineCityStateKey(
			String preferredLastLineCityStateKey) {
		this.preferredLastLineCityStateKey = preferredLastLineCityStateKey;
	}
	public String getFullZipPlus4Code() {
		return fullZipPlus4Code;
	}
	public void setFullZipPlus4Code(String fullZipPlus4Code) {
		this.fullZipPlus4Code = fullZipPlus4Code;
	}
	public boolean isNonDeliverable() {
		return nonDeliverable;
	}
	public void setNonDeliverable(boolean nonDeliverable) {
		this.nonDeliverable = nonDeliverable;
	}

	public String getFinanceNumber() {
		return financeNumber;
	}

	public void setFinanceNumber(String financeNumber) {
		this.financeNumber = financeNumber;
	}

	public String getStateAbbr() {
		return stateAbbr;
	}

	public void setStateAbbr(String stateAbbr) {
		this.stateAbbr = stateAbbr;
	}

	public String getCountyNumber() {
		return countyNumber;
	}

	public void setCountyNumber(String countyNumber) {
		this.countyNumber = countyNumber;
	}

	public String getLabelZipCode() {
		return labelZipCode;
	}

	public void setLabelZipCode(String labelZipCode) {
		this.labelZipCode = labelZipCode;
	}

	public String getCityStateKey() {
		return cityStateKey;
	}

	public void setCityStateKey(String cityStateKey) {
		this.cityStateKey = cityStateKey;
	}

	public ZipClassificationCode getZipClassificationCode() {
		return zipClassificationCode;
	}

	public void setZipClassificationCode(ZipClassificationCode zipClassificationCode) {
		this.zipClassificationCode = zipClassificationCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	public String getCityName() {
		return name;
	}

	public void setCityName(String name) {
		this.name = name;
	}

	public String getCityNameAbbr() {
		return nameAbbr;
	}

	public void setCityNameAbbr(String nameAbbr) {
		this.nameAbbr = nameAbbr;
	}

	public FacilityCode getFacilityCode() {
		return facilityCode;
	}

	public void setFacilityCode(FacilityCode facilityCode) {
		this.facilityCode = facilityCode;
	}

	public MailingNameIndicator getMailingNameIndicator() {
		return mailingNameIndicator;
	}

	public void setMailingNameIndicator(MailingNameIndicator mailingNameIndicator) {
		this.mailingNameIndicator = mailingNameIndicator;
	}

	public String getPreferredLastLineKey() {
		return preferredLastLineKey;
	}

	public void setPreferredLastLineKey(String preferredLastLineKey) {
		this.preferredLastLineKey = preferredLastLineKey;
	}

	public String getPreferredLastLineName() {
		return preferredLastLineName;
	}

	public void setPreferredLastLineName(String preferredLastLineName) {
		this.preferredLastLineName = preferredLastLineName;
	}

	public CityDeliveryIndicator getCityDeliveryIndicator() {
		return cityDeliveryIndicator;
	}

	public void setCityDeliveryIndicator(CityDeliveryIndicator cityDeliveryIndicator) {
		this.cityDeliveryIndicator = cityDeliveryIndicator;
	}

	public CarrierRouteRateSortationMerged5DigitIndicator getCarrierRouteRateSortationMerged5DigitIndicator() {
		return carrierRouteRateSortationMerged5DigitIndicator;
	}

	public void setCarrierRouteRateSortationMerged5DigitIndicator(
			CarrierRouteRateSortationMerged5DigitIndicator carrierRouteRateSortationMerged5DigitIndicator) {
		this.carrierRouteRateSortationMerged5DigitIndicator = carrierRouteRateSortationMerged5DigitIndicator;
	}

	public UniqueZipNameIndicator getUniqueZipNameIndicator() {
		return uniqueZipNameIndicator;
	}

	public void setUniqueZipNameIndicator(
			UniqueZipNameIndicator uniqueZipNameIndicator) {
		this.uniqueZipNameIndicator = uniqueZipNameIndicator;
	}

	public String getCountyName() {
		return countyName;
	}

	public void setCountyName(String countyName) {
		this.countyName = countyName;
	}

	public String getNameAbbr() {
		return nameAbbr;
	}

	public void setNameAbbr(String nameAbbr) {
		this.nameAbbr = nameAbbr;
	}

	public SeasonalDeliveryIndicator getJanuary() {
		return january;
	}

	public void setJanuary(SeasonalDeliveryIndicator january) {
		this.january = january;
	}

	public SeasonalDeliveryIndicator getFebruary() {
		return february;
	}

	public void setFebruary(SeasonalDeliveryIndicator february) {
		this.february = february;
	}

	public SeasonalDeliveryIndicator getMarch() {
		return march;
	}

	public void setMarch(SeasonalDeliveryIndicator march) {
		this.march = march;
	}

	public SeasonalDeliveryIndicator getApril() {
		return april;
	}

	public void setApril(SeasonalDeliveryIndicator april) {
		this.april = april;
	}

	public SeasonalDeliveryIndicator getMay() {
		return may;
	}

	public void setMay(SeasonalDeliveryIndicator may) {
		this.may = may;
	}

	public SeasonalDeliveryIndicator getJune() {
		return june;
	}

	public void setJune(SeasonalDeliveryIndicator june) {
		this.june = june;
	}

	public SeasonalDeliveryIndicator getJuly() {
		return july;
	}

	public void setJuly(SeasonalDeliveryIndicator july) {
		this.july = july;
	}

	public SeasonalDeliveryIndicator getAugust() {
		return august;
	}

	public void setAugust(SeasonalDeliveryIndicator august) {
		this.august = august;
	}

	public SeasonalDeliveryIndicator getSeptember() {
		return september;
	}

	public void setSeptember(SeasonalDeliveryIndicator september) {
		this.september = september;
	}

	public SeasonalDeliveryIndicator getOctober() {
		return october;
	}

	public void setOctober(SeasonalDeliveryIndicator october) {
		this.october = october;
	}

	public SeasonalDeliveryIndicator getNovember() {
		return november;
	}

	public void setNovember(SeasonalDeliveryIndicator november) {
		this.november = november;
	}

	public SeasonalDeliveryIndicator getDecember() {
		return december;
	}

	public void setDecember(SeasonalDeliveryIndicator december) {
		this.december = december;
	}
	
	


	
}
