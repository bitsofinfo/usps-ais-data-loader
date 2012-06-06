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

import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * ClassFinder defines a generic interface
 * for reflection activities within a 
 * specific package namespace supported by
 * implementing classes.
 * 
 * @author bitsofinfo.g [at] gmail [dot] com
 *
 */
public interface ClassFinder {
	
	/**
	 * Locate all Classes which contain
	 * a given TYPE annotation within the 
	 * given package root (descending)
	 * 
	 * @param annotation
	 * @param packageRoot
	 * @return
	 * @throws Exception
	 */
	public Set<Class> findByTypeAnnotation(Class<? extends Annotation> annotation, String packageRoot) throws Exception;

}
