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
package org.bitsofinfo.util.reflection;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;

/**
 * SpringClassFinder is a ClassFinder implementation which leverages the Spring Framework's
 * suite of reflection and resource loading utilities.
 * 
 * @author bitsofinfo.g [at] gmail [dot] com
 *
 */
public class SpringClassFinder implements ClassFinder {
	
	private static String convertResourceToClassName(Resource resource,String basePackage) throws IOException {
		String path = resource.getURI().toString();
		String pathWithoutSuffix = path.substring(0, path.length()- ClassUtils.CLASS_FILE_SUFFIX.length());
		String relativePathWithoutSuffix = pathWithoutSuffix.substring(pathWithoutSuffix.indexOf(basePackage));
		String className = relativePathWithoutSuffix.replace(File.separator, ".");
		return className;
	}
	

	@Autowired
	private ResourcePatternResolver resourcePatternResolver;
	
	@Autowired
	private MetadataReaderFactory metadataReaderFactory;
	

	public Set<Class> findByTypeAnnotation(Class<? extends Annotation> annotation, String packageRoot) throws Exception {

		Set<Class> foundClasses = new HashSet<Class>();
		
		packageRoot = StringUtils.replace(packageRoot, ".", File.separator);
		 
		AnnotationTypeFilter typeFilter = new AnnotationTypeFilter(annotation);
		String pattern = "classpath*:"+packageRoot+File.separator+"**"+File.separator+"*.class";
		
		Resource[] resources = resourcePatternResolver.getResources(pattern);
		 
		for (Resource resource : resources) {
			
			MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
			if (typeFilter.match(metadataReader, metadataReaderFactory)) {
				String className = convertResourceToClassName(resource,packageRoot);
				foundClasses.add(Class.forName(className));
			}
		}
		
		return foundClasses;

				
	}

}
