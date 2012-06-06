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
package org.bitsofinfo.util.address.usps.ais.loader;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import org.bitsofinfo.util.address.usps.ais.Copyright;
import org.bitsofinfo.util.address.usps.ais.CopyrightDetailCode;
import org.bitsofinfo.util.address.usps.ais.USPSProductType;
import org.bitsofinfo.util.address.usps.ais.USPSRecord;
import org.bitsofinfo.util.address.usps.ais.USPSUtils;
import org.bitsofinfo.util.address.usps.ais.annotations.USPSIdentifierField;
import org.bitsofinfo.util.address.usps.ais.annotations.USPSKeyField;
import org.bitsofinfo.util.address.usps.ais.citystate.CityStateAlias;
import org.bitsofinfo.util.address.usps.ais.citystate.CityStateDetail;
import org.bitsofinfo.util.address.usps.ais.citystate.CityStateScheme;
import org.bitsofinfo.util.address.usps.ais.citystate.CityStateSeasonal;
import org.bitsofinfo.util.address.usps.ais.citystate.CityStateZoneSplit;
import org.bitsofinfo.util.address.usps.ais.zipplus4.ZipPlus4Detail;

@ContextConfiguration
public class USPSUtilsTest extends AbstractJUnit4SpringContextTests {

	@Autowired
	private USPSUtils uspsUtils;
	 
	@Test 
	public void verifyRawRecord2Classes() {
		try {
			String zplus4copyright = "C     COPYRIGHT(C) 06-00 USPS 001                                                                                                                                                     ";
			String zplus4detail = "D02601V203814348AFC015  CATS SCHOOL ROAD            EXT   00000000350000000035OCATKELL DENTISTS                                            B38793899B  243723MA00110            V11919";
			String csCopyright = "C     COPYRIGHT(C) 02-05 USPS 001                                                                                                ";
			String csAlias = "A01002  MOUNT PLEASANT              ST      CATTT PLEASANT                    O19990629                                          ";
			String csDetail = "D55418Y26237 DOGFO ANTHONY                            NNY25845MINNEAPCATS                 YD 266360MN053HENNEPIN                 ";
			String csScheme = "S0101301022                                                                                                                      ";
			String csSeasonal = "N01011NNNNNYYYNNNN                                                                                                               ";
			String csZone = "Z01002R00301054R00320030804                                                                                                      ";
			  
			boolean check = ZipPlus4Detail.class == this.uspsUtils.getClassForRawRecord(zplus4detail);
			assert(check == true); 
			check = Copyright.class == this.uspsUtils.getClassForRawRecord(zplus4copyright);
			assert(check == true); 
			check = Copyright.class == this.uspsUtils.getClassForRawRecord(csCopyright);
			assert(check == true); 
			check = CityStateAlias.class == this.uspsUtils.getClassForRawRecord(csAlias);
			assert(check == true); 
			check = CityStateDetail.class == this.uspsUtils.getClassForRawRecord(csDetail);
			assert(check == true); 
			check = CityStateScheme.class == this.uspsUtils.getClassForRawRecord(csScheme);
			assert(check == true); 
			check = CityStateSeasonal.class == this.uspsUtils.getClassForRawRecord(csSeasonal);
			assert(check == true); 
			check = CityStateZoneSplit.class == this.uspsUtils.getClassForRawRecord(csZone);
			assert(check == true); 
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test 
	public void verifyIdentifierFields() {
		try {
			List<Field> fields = uspsUtils.getFieldsByAnnotation(Copyright.class, USPSIdentifierField.class);
			assert(fields.size() == 3);
			
			String[] names = uspsUtils.getIdentifierFieldNames(new Copyright());
			assert(names[0].equals("fileVersionMonth"));
			assert(names[1].equals("fileVersionYear"));
			assert(names[2].equals("volumeSequenceNumber"));
			
			names = uspsUtils.getIdentifierFieldNames(new ZipPlus4Detail());
			assert(names[0].equals("updateKeyNumber"));
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	 
	@Test 
	public void verifyKeyFields() {
		try {
			List<Field> fields = uspsUtils.getFieldsByAnnotation(Copyright.class, USPSKeyField.class);
			assert(fields.size() == 3);
			
			String[] names = uspsUtils.getKeyFieldNames(new Copyright());
			assert(names[0].equals("fileVersionMonth"));
			assert(names[1].equals("fileVersionYear"));
			assert(names[2].equals("volumeSequenceNumber"));
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	  
	@Test 
	public void verifyProductTypeTargetClasses() {
		try {
			Map<CopyrightDetailCode,Class<? extends USPSRecord>> map = uspsUtils.getTargetUSPSRecordClasses(USPSProductType.CITY_STATE);
			assert(map.size() == 6);
			assert(map.get(CopyrightDetailCode.A) == CityStateAlias.class);
			assert(map.get(CopyrightDetailCode.C) == Copyright.class);
			assert(map.get(CopyrightDetailCode.N) == CityStateSeasonal.class);
			assert(map.get(CopyrightDetailCode.S) == CityStateScheme.class);
			assert(map.get(CopyrightDetailCode.Z) == CityStateZoneSplit.class);
			assert(map.get(CopyrightDetailCode.D) == CityStateDetail.class);
			
			map = uspsUtils.getTargetUSPSRecordClasses(USPSProductType.ZIP_PLUS_4);
			assert(map.size() == 2);
			assert(map.get(CopyrightDetailCode.D) == ZipPlus4Detail.class);
			assert(map.get(CopyrightDetailCode.C) == Copyright.class);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
