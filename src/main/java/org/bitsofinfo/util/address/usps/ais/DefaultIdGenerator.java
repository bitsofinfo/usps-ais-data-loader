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

import java.security.MessageDigest;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import org.bitsofinfo.util.address.usps.ais.annotations.USPSRecordContext;

/**
 * DefaultIDGenerator is a USPSIdGenerator.
 * 
 * For USPSRecord types a unique String ID will be created consisting of:
 * 
 * [USPSProductType.id]-[Values from properties on the record annotated with USPSKeyField]
 * 
 * @author bitsofinfo.g [at] gmail [dot] com
 *
 */
public class DefaultIdGenerator implements USPSIdGenerator {

	private Map<Class<? extends USPSRecord>,String> typeCache = new Hashtable<Class<? extends USPSRecord>,String>();
	private Map<String,Class<? extends USPSRecord>> prefixCache = new Hashtable<String,Class<? extends USPSRecord>>();
	private MessageDigest digest = null;

	
	@Autowired
	private USPSUtils uspsUtils;
	 
	public void setUspsUtils(USPSUtils uspsUtils) {
		this.uspsUtils = uspsUtils;
	}

	public void initialize() throws Exception {
		this.buildCaches();
		
		digest = MessageDigest.getInstance("MD5");
	}
	
	@Override
	public String generateId(USPSRecord record) throws Exception {
		
		String idPrefix = getPrefix(record);
		String[] keyFieldNames = uspsUtils.getIdentifierFieldNames(record);
		
		StringBuffer tmp = new StringBuffer();
		for (String fieldName : keyFieldNames) {
			try {
				Object value = PropertyUtils.getProperty(record, fieldName);
				if (value != null) {
					tmp.append(value);
				}
				tmp.append("|");
				
			} catch (NoSuchMethodException e) {
				throw new Exception("Error in generating id for USPSRecord... no getter defined for USPSKeyField " + 
							fieldName + " within class " +record.getClass().getName());
			}
		}
		
		String toReturn = tmp.toString();
		
		synchronized(digest) {
			digest.reset();
			byte[] digested = digest.digest(toReturn.getBytes());
            toReturn = new String(Hex.encodeHex(digested));
		}
		
		return idPrefix+"-"+toReturn;
	}
	
	
	

	
	private String getPrefix(USPSRecord record) {
		Class<? extends USPSRecord> clazz = record.getClass();
		String prefix = typeCache.get(clazz);		
		return prefix;
	}
	
	private synchronized void buildCaches() throws Exception {
		if (typeCache.size() == 0) {
			Set<Class> clazzes = uspsUtils.getUSPSRecordClasses();
			for (Class clazz : clazzes) {
				USPSRecordContext context = (USPSRecordContext)clazz.getAnnotation(USPSRecordContext.class);
				StringBuffer tmp = new StringBuffer();
				for (USPSProductType pt : context.productTypes()) {
					tmp.append(pt.getId());
				}
				String prefix = tmp.toString() + context.copyrightDetailCode();
				typeCache.put(clazz, prefix);
				prefixCache.put(prefix,clazz);
			}
		}
	}


	@Override
	public Class<? extends USPSRecord> getRecordType(String identifier) {
		String prefix = StringUtils.split(identifier, "-")[0];
		Class clazz = prefixCache.get(prefix);
		return clazz;
	}

}
