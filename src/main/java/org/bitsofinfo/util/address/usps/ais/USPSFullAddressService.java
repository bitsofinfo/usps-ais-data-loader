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

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.bitsofinfo.util.address.usps.ais.citystate.CityStateAlias;
import org.bitsofinfo.util.address.usps.ais.citystate.CityStateDetail;
import org.bitsofinfo.util.address.usps.ais.citystate.CityStateScheme;
import org.bitsofinfo.util.address.usps.ais.citystate.CityStateSeasonal;
import org.bitsofinfo.util.address.usps.ais.citystate.CityStateZoneSplit;
import org.bitsofinfo.util.address.usps.ais.search.USPSSearchService;
import org.bitsofinfo.util.address.usps.ais.util.AddressNumber;
import org.bitsofinfo.util.address.usps.ais.util.AddressRange;
import org.bitsofinfo.util.address.usps.ais.util.ZipPlus4Range;
import org.bitsofinfo.util.address.usps.ais.zipplus4.RecordType;
import org.bitsofinfo.util.address.usps.ais.zipplus4.ZipPlus4Detail;

public class USPSFullAddressService {
	
	@Autowired
	USPSSearchService searchService; 

	public USPSFullAddressService() {
		// ensure prop descriptors are cached
		PropertyDescriptor[] zip4detailsProps = PropertyUtils.getPropertyDescriptors(ZipPlus4Detail.class);
		PropertyDescriptor[] fullAddressProps = PropertyUtils.getPropertyDescriptors(USPSFullAddress.class);
	}
	

	public List<USPSFullAddress> explode(ZipPlus4Detail z4record) throws Exception {
		List<USPSFullAddress> addys = new ArrayList<USPSFullAddress>();

		// primary address
		OddEvenCode pOE = z4record.getAddrPrimaryOddEvenCode();
		AddressNumber pLow = new AddressNumber(z4record.getAddressPrimaryLowNum());
		AddressNumber pHigh = new AddressNumber(z4record.getAddressPrimaryHighNum());
		AddressRange primaryRange = new AddressRange(pLow,pHigh,pOE);
		
		// z+4 sector/segment
		ZipPlus4Range z4range = new ZipPlus4Range(z4record.getPlus4LowZipSectorNumber(),
										  		  z4record.getPlus4HighZipSectorNumber(),
										  	      z4record.getPlus4LowZipSegmentNumber(),
										  		  z4record.getPlus4HighZipSegmentNumber());
		
		// optional secondary address
		AddressRange secondaryRange = null;
		if (z4record.getAddressSecondaryLowNum() != null) {
			OddEvenCode sOE = z4record.getAddrSecondaryOddEvenCode();
			AddressNumber sLow = new AddressNumber(z4record.getAddressSecondaryLowNum());
			AddressNumber sHigh = new AddressNumber(z4record.getAddressSecondaryHighNum());
			secondaryRange = new AddressRange(sLow,sHigh,sOE);
		}

		
		// fetch the aliases for this z4record
		List<USPSRecord> aliases = getAliases(z4record);
		
		// fetch zone split records where this zip code is the NEW zip code
		// ie this will return older zip codes that got changed to this zip
		List<USPSRecord> zoneSplits = getZoneSplits(z4record.getZipCode());
		
		// get CityStateSchem
		CityStateScheme scheme = getCityStateScheme(z4record.getZipCode());
		
		// fetch the CityStateDetail
		CityStateDetail detail = this.getCityStateDetail(z4record.getPreferredLastLineCityStateKey());
		
		// fetch the city state seasonal
		CityStateSeasonal seasonal = this.getCityStateSeasonal(z4record.getZipCode());
		
		// street 
		if (z4record.getRecordType() == RecordType.S) {
			processStreetRecord(z4record,addys,primaryRange,z4range,detail,seasonal);	
		
		// PO BOX 
	    } else if (z4record.getRecordType() == RecordType.P) {
			
		}

		
		
		
		return addys;
		
	}


	private void processStreetRecord(ZipPlus4Detail z4record,
			List<USPSFullAddress> addys, AddressRange primaryRange,
			ZipPlus4Range z4range, CityStateDetail detail,
			CityStateSeasonal seasonal) throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		
		// generate a full record for each address
		while(primaryRange.hasNext()) {
			
			USPSFullAddress full = new USPSFullAddress();
			
			// clone over all source z4 record props
			PropertyUtils.copyProperties(full, z4record);
			
			// clone over city state detail source record props
			PropertyUtils.copyProperties(full, detail);
			
			// clone over city state seasonal source record props
			if (seasonal != null) {
				PropertyUtils.copyProperties(full, seasonal);
			}
			
			// set the actual address
			full.setPrimaryAddress(primaryRange.next());
			
			// set the full ZIP+4 zip code
			full.setFullZipPlus4Code(full.getZipCode() +"-" + z4range.getSingleCode());

			// deliverable?
			if (z4range.isNonDeliverySegment()) {
				full.setNonDeliverable(true);
			}
			
			// add the address to the return set
			addys.add(full);
		}
	}


	private List<USPSRecord> getAliases(ZipPlus4Detail z4record) throws Exception {
		Map<String,Object> query = new HashMap<String,Object>();
		query.put("zipCode", z4record.getZipCode());
		query.put("streetName", z4record.getZipCode());
		query.put("streetSuffixAbbr", z4record.getStreetSuffixAbbr());
		query.put("streetPostDirectionalAbbr", z4record.getStreetPostDirectionalAbbr());
		query.put("streetPreDirectionalAbbr", z4record.getStreetPreDirectionalAbbr());
		List<USPSRecord> aliases = searchService.getBy(CityStateAlias.class, query);
		return aliases;
	}
	

	private List<USPSRecord> getZoneSplits(String newZipCode) throws Exception {
		Map<String,Object> query = new HashMap<String,Object>();
		query.put("newZipCode", newZipCode);
		List<USPSRecord> splits = searchService.getBy(CityStateZoneSplit.class, query);
		return splits;
	}
	
	private CityStateDetail getCityStateDetail(String cityStateKey) throws Exception {
		Map<String,Object> query = new HashMap<String,Object>();
		query.put("cityStateKey", cityStateKey);
		List<USPSRecord> details = searchService.getBy(CityStateDetail.class, query);
		if (details.size() == 1) {
			return (CityStateDetail)details.get(0);
		}
		return null;
	}
	
	private CityStateSeasonal getCityStateSeasonal(String zipCode) throws Exception {
		Map<String,Object> query = new HashMap<String,Object>();
		query.put("zipCode", zipCode);
		List<USPSRecord> x = searchService.getBy(CityStateSeasonal.class, query);
		if (x.size() == 1) {
			return (CityStateSeasonal)x.get(0);
		}
		return null;
	}
	
	
	private CityStateScheme getCityStateScheme(String zipCode) throws Exception {
		Map<String,Object> query = new HashMap<String,Object>();
		query.put("combinedZipCode", zipCode);
		List<USPSRecord> x = searchService.getBy(CityStateScheme.class, query);
		if (x.size() == 1) {
			return (CityStateScheme)x.get(0);
		}
		return null;
	}

	

	
	

	

	
	
}
