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
 * CityStateZoneSplit represents a USPS zone split record
 * as defined in the USPS AIS City/State (zone split)
 * product
 *  
 * @author bitsofinfo.g [at] gmail [dot] com
 * @see USPS Address Products PDF (Zone Split Record) (page 20)
 *
 */
@USPSRecordContext(productTypes={USPSProductType.CITY_STATE},
	 		copyrightDetailCode=CopyrightDetailCode.Z) 
@Entity
public class CityStateZoneSplit extends CopyrightedUSPSRecord {

	@USPSIdentifierField
	@USPSDataFileField(start=2,length=5)
	@Column(length=5)
	private String oldZipCode;
	
	@USPSIdentifierField
	@USPSDataFileField(start=7,length=4)
	@Column(length=4)
	private String oldCarrierRouteId;
	
	@USPSIdentifierField
	@USPSDataFileField(start=11,length=5)
	@Column(length=5)
	private String newZipCode;
	
	@USPSIdentifierField
	@USPSDataFileField(start=16,length=4)
	@Column(length=4)
	private String newCarrierRouteId;
	
	@USPSIdentifierField
	@USPSDataFileField(start=20,length=2)
	@Column(length=2)
	private String transactionCentury;
	
	@USPSIdentifierField
	@USPSDataFileField(start=22,length=2)
	@Column(length=2)
	private String transactionYear;
	
	@USPSIdentifierField
	@USPSDataFileField(start=24,length=2)
	@Column(length=2)
	private String transactionMonth;
	
	@USPSIdentifierField
	@USPSDataFileField(start=26,length=2)
	@Column(length=2)
	private String transactionDay;
	
	public String getTransactionDate() {
		return transactionCentury +
			transactionYear +
			transactionMonth +
			transactionDay;
	}
	
	public String getOldCarrierRoute() {
		return oldZipCode + oldCarrierRouteId;
	}
	
	public String getNewCarrierRoute() {
		return newZipCode + newCarrierRouteId;
	}

	/**
	 * @return the oldZipCode
	 */
	public String getOldZipCode() {
		return oldZipCode;
	}

	/**
	 * @param oldZipCode the oldZipCode to set
	 */
	public void setOldZipCode(String oldZipCode) {
		this.oldZipCode = oldZipCode;
	}

	/**
	 * @return the oldCarrierRouteId
	 */
	public String getOldCarrierRouteId() {
		return oldCarrierRouteId;
	}

	/**
	 * @param oldCarrierRouteId the oldCarrierRouteId to set
	 */
	public void setOldCarrierRouteId(String oldCarrierRouteId) {
		this.oldCarrierRouteId = oldCarrierRouteId;
	}

	/**
	 * @return the newZipCode
	 */
	public String getNewZipCode() {
		return newZipCode;
	}

	/**
	 * @param newZipCode the newZipCode to set
	 */
	public void setNewZipCode(String newZipCode) {
		this.newZipCode = newZipCode;
	}

	/**
	 * @return the newCarrierRouteId
	 */
	public String getNewCarrierRouteId() {
		return newCarrierRouteId;
	}

	/**
	 * @param newCarrierRouteId the newCarrierRouteId to set
	 */
	public void setNewCarrierRouteId(String newCarrierRouteId) {
		this.newCarrierRouteId = newCarrierRouteId;
	}

	/**
	 * @return the transactionCentury
	 */
	public String getTransactionCentury() {
		return transactionCentury;
	}

	/**
	 * @param transactionCentury the transactionCentury to set
	 */
	public void setTransactionCentury(String transactionCentury) {
		this.transactionCentury = transactionCentury;
	}

	/**
	 * @return the transactionYear
	 */
	public String getTransactionYear() {
		return transactionYear;
	}

	/**
	 * @param transactionYear the transactionYear to set
	 */
	public void setTransactionYear(String transactionYear) {
		this.transactionYear = transactionYear;
	}

	/**
	 * @return the transactionMonth
	 */
	public String getTransactionMonth() {
		return transactionMonth;
	}

	/**
	 * @param transactionMonth the transactionMonth to set
	 */
	public void setTransactionMonth(String transactionMonth) {
		this.transactionMonth = transactionMonth;
	}

	/**
	 * @return the transactionDay
	 */
	public String getTransactionDay() {
		return transactionDay;
	}

	/**
	 * @param transactionDay the transactionDay to set
	 */
	public void setTransactionDay(String transactionDay) {
		this.transactionDay = transactionDay;
	}
	
}
