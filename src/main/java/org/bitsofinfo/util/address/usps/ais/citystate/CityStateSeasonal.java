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
import org.bitsofinfo.util.address.usps.ais.annotations.USPSRecordContext;


/**
 * CityStateSeasonal represents a USPS city state seasonal record
 * as defined in the USPS AIS City/State (seasonal)
 * product
 * 
 * @author bitsofinfo.g [at] gmail [dot] com
 * @see USPS Address Products PDF (seasonal records) (page 19)
 *  
 */ 
@USPSRecordContext(productTypes={USPSProductType.CITY_STATE},
	 		copyrightDetailCode=CopyrightDetailCode.N)
@Entity
public class CityStateSeasonal extends CopyrightedUSPSRecord {

	@USPSIdentifierField
	@USPSDataFileField(start=2,length=5)
	@Column(length=5)
	private String zipCode;
	
	@USPSDataFileField(start=7,length=1)
	@Column(length=1)
	private SeasonalDeliveryIndicator january;
	
	@USPSDataFileField(start=8,length=1)
	@Column(length=1)
	private SeasonalDeliveryIndicator february;
	
	@USPSDataFileField(start=9,length=1)
	@Column(length=1)
	private SeasonalDeliveryIndicator march;
	
	@USPSDataFileField(start=10,length=1)
	@Column(length=1)
	private SeasonalDeliveryIndicator april;
	
	@USPSDataFileField(start=11,length=1)
	@Column(length=1)
	private SeasonalDeliveryIndicator may;
	
	@USPSDataFileField(start=12,length=1)
	@Column(length=1)
	private SeasonalDeliveryIndicator june;
	
	@USPSDataFileField(start=13,length=1)
	@Column(length=1)
	private SeasonalDeliveryIndicator july;
	
	@USPSDataFileField(start=14,length=1)
	@Column(length=1)
	private SeasonalDeliveryIndicator august;
	
	@USPSDataFileField(start=15,length=1)
	@Column(length=1)
	private SeasonalDeliveryIndicator september;
	
	@USPSDataFileField(start=16,length=1)
	@Column(length=1)
	private SeasonalDeliveryIndicator october;
	
	@USPSDataFileField(start=17,length=1)
	@Column(length=1)
	private SeasonalDeliveryIndicator november;
	
	@USPSDataFileField(start=18,length=1)
	@Column(length=1)
	private SeasonalDeliveryIndicator december;

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
	 * @return the january
	 */
	public SeasonalDeliveryIndicator getJanuary() {
		return january;
	}

	/**
	 * @param january the january to set
	 */
	public void setJanuary(SeasonalDeliveryIndicator january) {
		this.january = january;
	}

	/**
	 * @return the february
	 */
	public SeasonalDeliveryIndicator getFebruary() {
		return february;
	}

	/**
	 * @param february the february to set
	 */
	public void setFebruary(SeasonalDeliveryIndicator february) {
		this.february = february;
	}

	/**
	 * @return the march
	 */
	public SeasonalDeliveryIndicator getMarch() {
		return march;
	}

	/**
	 * @param march the march to set
	 */
	public void setMarch(SeasonalDeliveryIndicator march) {
		this.march = march;
	}

	/**
	 * @return the april
	 */
	public SeasonalDeliveryIndicator getApril() {
		return april;
	}

	/**
	 * @param april the april to set
	 */
	public void setApril(SeasonalDeliveryIndicator april) {
		this.april = april;
	}

	/**
	 * @return the may
	 */
	public SeasonalDeliveryIndicator getMay() {
		return may;
	}

	/**
	 * @param may the may to set
	 */
	public void setMay(SeasonalDeliveryIndicator may) {
		this.may = may;
	}

	/**
	 * @return the june
	 */
	public SeasonalDeliveryIndicator getJune() {
		return june;
	}

	/**
	 * @param june the june to set
	 */
	public void setJune(SeasonalDeliveryIndicator june) {
		this.june = june;
	}

	/**
	 * @return the july
	 */
	public SeasonalDeliveryIndicator getJuly() {
		return july;
	}

	/**
	 * @param july the july to set
	 */
	public void setJuly(SeasonalDeliveryIndicator july) {
		this.july = july;
	}

	/**
	 * @return the august
	 */
	public SeasonalDeliveryIndicator getAugust() {
		return august;
	}

	/**
	 * @param august the august to set
	 */
	public void setAugust(SeasonalDeliveryIndicator august) {
		this.august = august;
	}

	/**
	 * @return the september
	 */
	public SeasonalDeliveryIndicator getSeptember() {
		return september;
	}

	/**
	 * @param september the september to set
	 */
	public void setSeptember(SeasonalDeliveryIndicator september) {
		this.september = september;
	}

	/**
	 * @return the october
	 */
	public SeasonalDeliveryIndicator getOctober() {
		return october;
	}

	/**
	 * @param october the october to set
	 */
	public void setOctober(SeasonalDeliveryIndicator october) {
		this.october = october;
	}

	/**
	 * @return the november
	 */
	public SeasonalDeliveryIndicator getNovember() {
		return november;
	}

	/**
	 * @param november the november to set
	 */
	public void setNovember(SeasonalDeliveryIndicator november) {
		this.november = november;
	}

	/**
	 * @return the december
	 */
	public SeasonalDeliveryIndicator getDecember() {
		return december;
	}

	/**
	 * @param december the december to set
	 */
	public void setDecember(SeasonalDeliveryIndicator december) {
		this.december = december;
	}

}
