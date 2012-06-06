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

import java.util.List;

import org.bitsofinfo.util.address.usps.ais.processor.USPSDataProcessor;
import org.bitsofinfo.util.io.FileHandle;


/**
 * USPSDataLoader will load USPS raw data into a target
 * USPSDataProcessor according to the details specified in a 
 * passed LoaderJob
 * 
 * @author bitsofinfo.g [at] gmail [dot] com
 *
 */
public interface USPSDataLoader {
	
	/**
	 * For all FileHandles passed to a USPSDataLoader, the following property
	 * must exist on each FileHandle. The value of which is a USPSProductType that
	 * the source data FileHandle is for.
	 */
	public static String FILE_HANDLE_PROP_USPS_PRODUCT_TYPE = "uspsProductType";

	public LoaderJob createJob(String identifier, List<FileHandle> sourceData, 
			USPSDataProcessor targetProcessor);
	 
	public void load(LoaderJob job) throws Exception;
	
}
