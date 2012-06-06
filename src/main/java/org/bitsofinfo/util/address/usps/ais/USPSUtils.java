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

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.bitsofinfo.util.address.usps.ais.annotations.USPSIdentifierField;
import org.bitsofinfo.util.address.usps.ais.annotations.USPSKeyField;
import org.bitsofinfo.util.address.usps.ais.annotations.USPSRecordContext;
import org.bitsofinfo.util.address.usps.ais.loader.GenericEnumConverter;
import org.bitsofinfo.util.reflection.ClassFinder;

/**
 * USPSUtils provides utility methods
 * for working with USPSRecord annotations and other
 * utility operations in the USPS AIS package
 * suite of classes.
 * 
 * This is primarily used by the usps.ais.loader package
 * 
 * @author bitsofinfo.g [at] gmail [dot] com
 *
 */
public class USPSUtils {
	
	// cache of USPSRecord classes to all Fields annotated by a specific Annotation type within it
	// the key is ClassName+AnnotationClassName
	private  Map<String,List<Field>> class2AnnotatedFieldCache = new Hashtable<String,List<Field>>();

	// cache of USPSRecord classes to field names annotated with USPSKeyField in that class
	private  Map<Class<? extends USPSRecord>,String[]> keyFieldCache = new Hashtable<Class<? extends USPSRecord>,String[]>();
	
	// cache of USPSRecord classes to field names annotated with USPSIdentifierField in that class
	private  Map<Class<? extends USPSRecord>,String[]> idFieldCache = new Hashtable<Class<? extends USPSRecord>,String[]>();
	
	// cache of USPSRecord classes to declared field names 
	private  Map<Class<? extends USPSRecord>,String[]> declaredFieldNamesCache = new Hashtable<Class<? extends USPSRecord>,String[]>();
	
	// cache of USPSRecord classes to declared field names (stored as Set>
	private  Map<Class<? extends USPSRecord>,Set<String>> declaredFieldNamesAsSetCache = new Hashtable<Class<? extends USPSRecord>,Set<String>>();
	
	// cache of USPSRecord classes to declared fields 
	private  Map<Class<? extends USPSRecord>,Field[]> declaredFieldsCache = new Hashtable<Class<? extends USPSRecord>,Field[]>();
	
	// cache of record (copyrightDetailCodes+recordLengths) -> target USPSRecord classes 
	private  Map<String,Class<? extends USPSRecord>> recordLengthMap = new Hashtable<String,Class<? extends USPSRecord>>();
	
	/**
	 * This is a cache which maps:
	 * 
	 * USPSProductType -> Supported CopyrightDetailCodes -> USPSRecord class to handle that code
	 * 
	 */
	private Map<USPSProductType,Map<CopyrightDetailCode,Class<? extends USPSRecord>>> productTypeMapCache
                  = new Hashtable<USPSProductType,Map<CopyrightDetailCode,Class<? extends USPSRecord>>>();
	

	@Autowired
	private ClassFinder classFinder;
	private String uspsAisPackage;


	public void setClassFinder(ClassFinder classFinder) {
		this.classFinder = classFinder;
	}

	public void setUspsAisPackage(String uspsAisPackage) {
		this.uspsAisPackage = uspsAisPackage;
	}
	
	/**
	 * For a given raw USPS data file record. This will return the target USPSRecord
	 * class that it should be applied against
	 * 
	 * @param record
	 * @return USPSRecord class 
	 * @throws Exception
	 */
	public Class<? extends USPSRecord> getClassForRawRecord(String record) throws Exception {
		return getClassForRawRecord(record.substring(0, 1),record.length());
	}
	
	/**
	 * Determine if the given fieldName exists within the target USPSRecord class
	 * @param clazz
	 * @param fieldName
	 * @return true/false
	 */
	public boolean fieldExists(Class<? extends USPSRecord> clazz, String fieldName) {
		if (this.declaredFieldNamesAsSetCache.get(clazz) == null) {
			synchronized(this.declaredFieldNamesAsSetCache) {
				if (this.declaredFieldNamesAsSetCache.get(clazz) == null) {
					this.getAllDeclaredFieldNames(clazz);
					for (Class<? extends USPSRecord> c : this.declaredFieldNamesCache.keySet()) {
						Set<String> x = new HashSet<String>();
						for (String fname : this.declaredFieldNamesCache.get(c)) {
							x.add(fname);
						}
						
						this.declaredFieldNamesAsSetCache.put(c,x);
					}
				}
			}
		}
		
		return this.declaredFieldNamesAsSetCache.get(clazz).contains(fieldName);
	}
	
	/**
	 * For a given raw USPS data file record. This will return the target USPSRecord
	 * class that it should be applied against
	 * 
	 * @param record
	 * @return USPSRecord class 
	 * @throws Exception
	 */
	public Class<? extends USPSRecord> getClassForRawRecord(String copyrightDetailCode, int recordLength) throws Exception {
		String key = copyrightDetailCode+recordLength;
		Class<? extends USPSRecord> targetClass = recordLengthMap.get(key);
		
		if (targetClass == null) {
			synchronized(recordLengthMap) {
				targetClass = recordLengthMap.get(key);
				if (targetClass == null) {
					USPSProductType type = USPSProductType.getTypeForRawRecord(recordLength);
					Map<CopyrightDetailCode,Class<? extends USPSRecord>> map = this.getTargetUSPSRecordClasses(type);
					for (CopyrightDetailCode cdc : map.keySet()) {
						Class cz = map.get(cdc);
						recordLengthMap.put(cdc.toString()+recordLength,cz);
					}
					
					targetClass = recordLengthMap.get(key);
				}
				
				
			}
		}
		
		return targetClass;	
	}
	
	/**
	 * Get all Classes that can handle USPSRecord data
	 * 
	 * @return
	 * @throws Exception
	 */
	public Set<Class> getUSPSRecordClasses() throws Exception {
		return this.classFinder.findByTypeAnnotation(USPSRecordContext.class, this.uspsAisPackage);
	}
	
	
	/**
	 * Given a Class and an List, this method will
	 * populate the given List with all Field's for that Class and
	 * all of its parent Classes in the inheritance tree
	 * 
	 * @param fields
	 * @param type the Class to check
	 * @param stopAtClass, if the current class is equal to this, we go no further up the parent chains
	 * @param	annotationClass the class of the annotation which must be present in 
	 */
	private  void populateAllDeclaredFields(List<Field> fields, Class type, Class stopAtClass, Class<? extends Annotation> annotationClass) {
	    for (Field field: type.getDeclaredFields()) {

	    	if (annotationClass != null) {
	    		if (field.isAnnotationPresent(annotationClass)) {
	    			fields.add(field);
	    		}
	    	} else {
	    		fields.add(field);
	    	}
	    	
	    	if (field.getType().isEnum()) {
	    		ConvertUtils.register(new GenericEnumConverter(), field.getType());
	    	}

	    }

	    // this is as far as we go...
	    if (stopAtClass != null && stopAtClass == type) {
	    	return;
	    }
	    
	    if (type.getSuperclass() != null) {
	    	populateAllDeclaredFields(fields, type.getSuperclass(), stopAtClass, annotationClass);
	    }
	}

	
	/**
	 * Return all Fields in the given class [type] that have the given
	 * annotation present for the field
	 * 
	 * @param type
	 * @param annotationClass
	 * @return List of Fields annotated by annotationClass within in the passed clazz
	 */
	public  List<Field> getFieldsByAnnotation(Class clazz, Class<? extends Annotation> annotationClass) {
		
		// get the list of all Fields known in this class (and parents)
		String lookupKey = clazz.getName()+annotationClass.getName();
		List<Field> allFields = class2AnnotatedFieldCache.get(lookupKey);
		
		if (allFields == null) {
			synchronized(class2AnnotatedFieldCache) {
				allFields = class2AnnotatedFieldCache.get(lookupKey);
				if (allFields == null) {
					allFields = new ArrayList<Field>();
					populateAllDeclaredFields(allFields,clazz,null,annotationClass);
					class2AnnotatedFieldCache.put(lookupKey, allFields);
				}
			}
		}
		
		return allFields;
	}
	
	
	
	
	/**
	 * For the given USPSRecord class return an Array 
	 * of all field/property names declared within the USPSRecord class,
	 * going no farther than the parent USPSRecord.class
	 * 
	 * @param clazz
	 * @return String array of key fields for the class
	 */
	public  Field[] getAllDeclaredFields(Class<? extends USPSRecord> clazz) {
		Field[] allFields = declaredFieldsCache.get(clazz);
		
		if (allFields == null) {
			synchronized(declaredFieldsCache) {
				allFields = declaredFieldsCache.get(clazz);
				
				if (allFields == null) {
					ArrayList<Field> tmpFields = new ArrayList<Field>();
					populateAllDeclaredFields(tmpFields,clazz,USPSRecord.class,null);
					allFields = tmpFields.toArray(new Field[1]);
					declaredFieldsCache.put(clazz,allFields);
					
				}
			}
		}
		
		return allFields;
	}
	
	/**
	 * For the given USPSRecord class return an Array 
	 * of all field/property names declared within the USPSRecord class,
	 * going no farther than the parent USPSRecord.class
	 * 
	 * @param clazz
	 * @return String array of key fields for the class
	 */
	public  String[] getAllDeclaredFieldNames(Class<? extends USPSRecord> clazz) {
		String[] fieldNames = declaredFieldNamesCache.get(clazz);
		
		if (fieldNames == null) {
			synchronized(declaredFieldNamesCache) {
				fieldNames = declaredFieldNamesCache.get(clazz);
				
				if (fieldNames == null) {
					Field[] allFields = getAllDeclaredFields(clazz);
					
					fieldNames = new String[allFields.length];
					
					for (int i=0; i<allFields.length; i++) {
						fieldNames[i] = allFields[i].getName();
					}
					
					declaredFieldNamesCache.put(clazz, fieldNames);
				}
			}
		}
		
		return fieldNames;
	}
	
	
	/**
	 * For the given USPSRecord class return an Array 
	 * of all field/property names that have are annotated
	 * by the USPSIdentifierField annotation
	 * 
	 * @param clazz
	 * @return String array of key fields for the class
	 */
	public  String[] getIdentifierFieldNames(Class<? extends USPSRecord> clazz) {
		String[] fieldNames = idFieldCache.get(clazz);
		
		if (fieldNames == null) {
			synchronized(clazz) {
				if (fieldNames == null) {
					List<Field> idFields = getFieldsByAnnotation(clazz, USPSIdentifierField.class);
					fieldNames = new String[idFields.size()];
					
					for (int i=0; i<idFields.size(); i++) {
						fieldNames[i] = idFields.get(i).getName();
					}
					
					idFieldCache.put(clazz, fieldNames);
				}
			}
		}
		
		return fieldNames;		
	}

	/**
	 * For the given USPSRecord class return an Array 
	 * of all field/property names that have are annotated
	 * by the USPSIdentifierField annotation
	 * 
	 * @param record
	 * @return String array of key fields for the class
	 */
	public  String[] getIdentifierFieldNames(USPSRecord record) {
		Class<? extends USPSRecord> clazz = record.getClass();
		return getIdentifierFieldNames(clazz);
	}

	/**
	 * For the given USPSRecord class return an Array 
	 * of all field/property names that have are annotated
	 * by the USPSKeyField annotation
	 * 
	 * @param clazz
	 * @return String array of key fields for the class
	 */
	public  String[] getKeyFieldNames(Class<? extends USPSRecord> clazz) {
		String[] fieldNames = keyFieldCache.get(clazz);
		
		if (fieldNames == null) {
			synchronized(clazz) {
				if (fieldNames == null) {
					List<Field> keyFields = getFieldsByAnnotation(clazz, USPSKeyField.class);
					fieldNames = new String[keyFields.size()];
					
					for (int i=0; i<keyFields.size(); i++) {
						fieldNames[i] = keyFields.get(i).getName();
					}
					
					keyFieldCache.put(clazz, fieldNames);
				}
			}
		}
		
		return fieldNames;
	}
	
	/**
	 * For the given USPSRecord class return an Array 
	 * of all field/property names that have are annotated
	 * by the USPSKeyField annotation
	 * 
	 * @param record
	 * @return String array of key fields for the class
	 */
	public  String[] getKeyFieldNames(USPSRecord record) {
		Class<? extends USPSRecord> clazz = record.getClass();
		return getKeyFieldNames(clazz);
	}

	
	
	/**
	 * Retrieve a Set of CopyrightDetailCodes -> USPSRecord classes for the given USPSProductType.
	 * 
	 * @param productType
	 * @return a map that contains the mapping between CopyrightDetailCodes and their target USPSRecord class for the passed USPSProductType
	 * 
	 * @throws Exception
	 */
	public Map<CopyrightDetailCode,Class<? extends USPSRecord>>	
			getTargetUSPSRecordClasses(USPSProductType productType) throws Exception {
	
		// see if the map already exists for this product type
		Map<CopyrightDetailCode,Class<? extends USPSRecord>> classesForProduct = productTypeMapCache.get(productType);
		
		// if it does not exist
		if (classesForProduct == null) {
			
			synchronized(productTypeMapCache) {
				try {
					// create container for the product type
					classesForProduct = new Hashtable<CopyrightDetailCode,Class<? extends USPSRecord>>();
					
					// located all classes which are annotated by @USPSProduct
					Set<Class> productClasses = classFinder.findByTypeAnnotation(USPSRecordContext.class, this.uspsAisPackage);
					
					// for each class, determine if the requested USPSProductType is supported by the given class
					for (Class clazz : productClasses) {
						
						// huh? misconfig, USPSRecordContext annotations should only be used on derivatives of USPSRecord targets
						if (!clazz.getSuperclass().equals(USPSRecord.class) && !clazz.getSuperclass().equals(CopyrightedUSPSRecord.class)) {
							throw new Exception("Class: " +clazz.getName() +" is annotated by @USPSRecordContext. Only derivatives of USPSRecord " +
									" can be annotated with @USPSRecordContext ");
						}
						
						// get the annotation
						USPSRecordContext prodInfo = (USPSRecordContext)clazz.getAnnotation(USPSRecordContext.class);
						
						// if this class is applicable for the passed USPSProductType, create a map entry for it by 
						// the annotations target copyright detail code
						for (USPSProductType permissableType : prodInfo.productTypes()) {
							CopyrightDetailCode cdc = prodInfo.copyrightDetailCode();
							if (permissableType == productType) {
								
								// already exists? this is a mis-config
								if (classesForProduct.get(cdc) != null) {
									throw new Exception("Mis-configuration: There are one or more classes that are annotated with @USPSRecordContext for the combination of USPSProductType: " + 
											productType.name() + " and CopyrightDetailCode of: " +cdc.name());
								} else {
									// add to the map, keyed by the CopyrightDetailCode
									classesForProduct.put(cdc, clazz);
								}
							}
						}
					}
					
				// some other error
				} catch(Exception e) {
					throw new Exception("There was a critical error locating USPSRecord classes annotated with USPSRecordContext...error:" + e.getMessage());
				}
			}
		}
		
		// return the map of detail codes -> classes which handle it
		return classesForProduct;
		
		
	}
	
	
	
}
