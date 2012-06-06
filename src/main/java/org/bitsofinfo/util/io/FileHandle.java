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
package org.bitsofinfo.util.io;

import java.net.URI;
import java.util.HashMap;

/**
 * A FileHandle acts as a pointer to a file
 * somwhere on local disk or anywhere as
 * defined by it's URI property. 
 * 
 * Lastly, a FileHandle can also carry an arbitrary
 * set of variable properties.
 * 
 * @author bitsofinfo.g [at] gmail [dot] com
 *
 */
public class FileHandle {

	private URI uri;
	private HashMap<String,Object> properties = new HashMap<String, Object>();

	public FileHandle(URI uri) {
		this.uri = uri;
	}

	public void setProperty(String name, Object value) {
		properties.put(name, value);
	}
	
	public Object getProperty(String name) {
		return properties.get(name);
	}

	
	/**
	 * @return the uri
	 */
	public URI getUri() {
		return uri;
	}

	/**
	 * @param uri the uri to set
	 */
	public void setUri(URI uri) {
		this.uri = uri;
	}
	
	
		
}
