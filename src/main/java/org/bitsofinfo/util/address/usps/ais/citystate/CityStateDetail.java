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
import org.bitsofinfo.util.address.usps.ais.USPSProductType;
import org.bitsofinfo.util.address.usps.ais.annotations.USPSDataFileField;
import org.bitsofinfo.util.address.usps.ais.annotations.USPSIdentifierField;
import org.bitsofinfo.util.address.usps.ais.annotations.USPSKeyField;
import org.bitsofinfo.util.address.usps.ais.annotations.USPSRecordContext;


/**
 * CityStateDetail represents a USPS city state detail record
 * as defined in the USPS AIS City/State (detail)
 * product
 * 
 * @author bitsofinfo.g [at] gmail [dot] com 
 * @see USPS Address Products PDF (Detail records) (page 21)
 *
 */ 
@USPSRecordContext(productTypes={USPSProductType.CITY_STATE},
		 	copyrightDetailCode=CopyrightDetailCode.D)  
@Entity
public class CityStateDetail extends CopyrightedUSPSRecord {

	@USPSKeyField
	@USPSDataFileField(start=2,length=5)
	@Column(length=5)
	@USPSIdentifierField
	private String labelZipCode;
	
	@USPSIdentifierField
	@USPSKeyField
	@USPSDataFileField(start=7,length=6)
	@Column(length=6)
	private String cityStateKey;

	@USPSDataFileField(start=13,length=1)
	@Column(length=1)
	@USPSIdentifierField
	private ZipClassificationCode zipClassificationCode;
	
	@USPSKeyField
	@USPSDataFileField(start=14,length=28)
	@Column(length=28)
	@USPSIdentifierField
	private String name;
	
	@USPSDataFileField(start=42,length=13)
	@Column(length=13)
	@USPSIdentifierField
	private String nameAbbr;
	
	@USPSDataFileField(start=55,length=1)
	@Column(length=1)
	@USPSIdentifierField
	private FacilityCode facilityCode;
	
	@USPSDataFileField(start=56,length=1)
	@Column(length=1)
	@USPSIdentifierField
	private MailingNameIndicator mailingNameIndicator;
	
	@USPSDataFileField(start=57,length=6)
	@Column(length=6)
	@USPSIdentifierField
	private String preferredLastLineKey;

	@USPSDataFileField(start=63,length=28)
	@Column(length=28)
	@USPSIdentifierField
	private String preferredLastLineName;
	
	@USPSDataFileField(start=91,length=1)
	@Column(length=1)
	@USPSIdentifierField
	private CityDeliveryIndicator cityDeliveryIndicator;

	@USPSDataFileField(start=92,length=1)
	@Column(length=1)
	@USPSIdentifierField
	private CarrierRouteRateSortationMerged5DigitIndicator carrierRouteRateSortationMerged5DigitIndicator;
	
	@USPSDataFileField(start=93,length=1)
	@Column(length=1)
	@USPSIdentifierField
	private UniqueZipNameIndicator uniqueZipNameIndicator;
	
	@USPSDataFileField(start=94,length=6)
	@Column(length=6)
	@USPSIdentifierField
	private String financeNumber;
	
	@USPSKeyField
	@USPSDataFileField(start=100,length=2)
	@Column(length=2)
	@USPSIdentifierField
	private String stateAbbr;
	
	@USPSKeyField
	@USPSDataFileField(start=102,length=3)
	@Column(length=3)
	@USPSIdentifierField
	private String countyNumber;
	
	@USPSKeyField
	@USPSDataFileField(start=105,length=25)
	@Column(length=25)
	@USPSIdentifierField
	private String countyName;

	/**
	 * @return the labelZipCode
	 */
	public String getLabelZipCode() {
		return labelZipCode;
	}

	/**
	 * @param labelZipCode the labelZipCode to set
	 */
	public void setLabelZipCode(String labelZipCode) {
		this.labelZipCode = labelZipCode;
	}

	/**
	 * @return the cityStateKey
	 */
	public String getCityStateKey() {
		return cityStateKey;
	}

	/**
	 * @param cityStateKey the cityStateKey to set
	 */
	public void setCityStateKey(String cityStateKey) {
		this.cityStateKey = cityStateKey;
	}

	/**
	 * @return the zipClassificationCode
	 */
	public ZipClassificationCode getZipClassificationCode() {
		return zipClassificationCode;
	}

	/**
	 * @param zipClassificationCode the zipClassificationCode to set
	 */
	public void setZipClassificationCode(ZipClassificationCode zipClassificationCode) {
		this.zipClassificationCode = zipClassificationCode;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the nameAbbr
	 */
	public String getNameAbbr() {
		return nameAbbr;
	}

	/**
	 * @param nameAbbr the nameAbbr to set
	 */
	public void setNameAbbr(String nameAbbr) {
		this.nameAbbr = nameAbbr;
	}

	/**
	 * @return the facilityCode
	 */
	public FacilityCode getFacilityCode() {
		return facilityCode;
	}

	/**
	 * @param facilityCode the facilityCode to set
	 */
	public void setFacilityCode(FacilityCode facilityCode) {
		this.facilityCode = facilityCode;
	}

	/**
	 * @return the mailingNameIndicator
	 */
	public MailingNameIndicator getMailingNameIndicator() {
		return mailingNameIndicator;
	}

	/**
	 * @param mailingNameIndicator the mailingNameIndicator to set
	 */
	public void setMailingNameIndicator(MailingNameIndicator mailingNameIndicator) {
		this.mailingNameIndicator = mailingNameIndicator;
	}

	/**
	 * @return the preferredLastLineKey
	 */
	public String getPreferredLastLineKey() {
		return preferredLastLineKey;
	}

	/**
	 * @param preferredLastLineKey the preferredLastLineKey to set
	 */
	public void setPreferredLastLineKey(String preferredLastLineKey) {
		this.preferredLastLineKey = preferredLastLineKey;
	}

	/**
	 * @return the preferredLastLineName
	 */
	public String getPreferredLastLineName() {
		return preferredLastLineName;
	}

	/**
	 * @param preferredLastLineName the preferredLastLineName to set
	 */
	public void setPreferredLastLineName(String preferredLastLineName) {
		this.preferredLastLineName = preferredLastLineName;
	}

	/**
	 * @return the cityDeliveryIndicator
	 */
	public CityDeliveryIndicator getCityDeliveryIndicator() {
		return cityDeliveryIndicator;
	}

	/**
	 * @param cityDeliveryIndicator the cityDeliveryIndicator to set
	 */
	public void setCityDeliveryIndicator(CityDeliveryIndicator cityDeliveryIndicator) {
		this.cityDeliveryIndicator = cityDeliveryIndicator;
	}

	/**
	 * @return the carrierRouteRateSortationMerged5DigitIndicator
	 */
	public CarrierRouteRateSortationMerged5DigitIndicator getCarrierRouteRateSortationMerged5DigitIndicator() {
		return carrierRouteRateSortationMerged5DigitIndicator;
	}

	/**
	 * @param carrierRouteRateSortationMerged5DigitIndicator the carrierRouteRateSortationMerged5DigitIndicator to set
	 */
	public void setCarrierRouteRateSortationMerged5DigitIndicator(
			CarrierRouteRateSortationMerged5DigitIndicator carrierRouteRateSortationMerged5DigitIndicator) {
		this.carrierRouteRateSortationMerged5DigitIndicator = carrierRouteRateSortationMerged5DigitIndicator;
	}

	/**
	 * @return the uniqueZipNameIndicator
	 */
	public UniqueZipNameIndicator getUniqueZipNameIndicator() {
		return uniqueZipNameIndicator;
	}

	/**
	 * @param uniqueZipNameIndicator the uniqueZipNameIndicator to set
	 */
	public void setUniqueZipNameIndicator(
			UniqueZipNameIndicator uniqueZipNameIndicator) {
		this.uniqueZipNameIndicator = uniqueZipNameIndicator;
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
	 * @return the countyName
	 */
	public String getCountyName() {
		return countyName;
	}

	/**
	 * @param countyName the countyName to set
	 */
	public void setCountyName(String countyName) {
		this.countyName = countyName;
	}

}
