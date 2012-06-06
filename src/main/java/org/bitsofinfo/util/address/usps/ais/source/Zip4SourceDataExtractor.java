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
package org.bitsofinfo.util.address.usps.ais.source;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.bitsofinfo.util.address.usps.ais.USPSProductType;
import org.bitsofinfo.util.address.usps.ais.loader.USPSDataLoader;
import org.bitsofinfo.util.io.FileHandle;
import org.bitsofinfo.util.io.ZipExtractor;

/**
 * Zip4SourceDataExtractor will extract raw USPS
 * one or more source data files within a given
 * USPS source directory. 
 * 
 * Source data usually is delivered on a CDROM laid
 * out in the following format:
 * 
 * /ctystate/ctystate.zip
 * /zip4|z4trans/[001-999]/[num].zip
 * 
 * The caller must provide an List of File references to 
 * the source directories that contain the source data. These
 * directories are "ctystate", "zip4" and "z4trans". The extractor
 * will extract the files to a target directory locally on disk.
 * 
 * References to the extracted files are returned as a List of FileHandle
 * objects
 * 
 * @author bitsofinfo.g [at] gmail [dot] com
 *
 */
public class Zip4SourceDataExtractor {
	
	@Autowired
	private ZipExtractor zipExtractor;
	
	public List<FileHandle> createHandles(List<File> extractedDirs) throws Exception {
		ArrayList<FileHandle> handles = new ArrayList<FileHandle>();
		
		for (File dir : extractedDirs) {
			if (!dir.isDirectory()) {
				throw new Exception("Create handles must be passed a list of directories, this list contains a File");
			}
			collectHandles(handles,dir);
		}
		
		return handles;
	}
	
	private void collectHandles(List<FileHandle> handles, File dir) {
		File[] contents = dir.listFiles();
		for (File f : contents) {
			if (f.isFile()) {
				FileHandle handle = new FileHandle(f.toURI());
				handle.setProperty("identifier", dir.getName());
				if (f.getAbsolutePath().indexOf("ctystate") != -1) {
					handle.setProperty(USPSDataLoader.FILE_HANDLE_PROP_USPS_PRODUCT_TYPE, USPSProductType.CITY_STATE);
				} else {
					handle.setProperty(USPSDataLoader.FILE_HANDLE_PROP_USPS_PRODUCT_TYPE, USPSProductType.ZIP_PLUS_4);
				}
				handles.add(handle);
			} else {
				collectHandles(handles,f);
			}
		}
	}
	
	public List<FileHandle> extractSourceData(List<File> sourceDirs, File extractToDir) throws Exception {
		ArrayList<FileHandle> files = new ArrayList<FileHandle>();

		for (File sourceDir : sourceDirs) {
			if (!sourceDir.exists() || !sourceDir.isDirectory()) {
				throw new Exception("Target directory to locate ZIP files in does not exist or is NOT a directory: " + sourceDir.getAbsolutePath());
			}
			
			// for each file extract it
			for (File zip : sourceDir.listFiles()) {
				// unzip to the [extractToDir]/[sourceDirName]/[zipFilenameWithoutExtension/
				File unzipToDir = new File(extractToDir.getAbsolutePath() + "/" +  
										sourceDir.getName() + "/" + 
										zip.getName().substring(0,zip.getName().indexOf('.')));
				
				if (!unzipToDir.exists()) {
					unzipToDir.mkdirs();
				}
				
				// extract it
				zipExtractor.extractZipToDirectory(zip,unzipToDir);
				
				// each source zip only contains ONE file
				File[] contents = unzipToDir.listFiles();
				if (contents.length == 0) {
					// error ?
					continue;
				}
				
				//for (File unzipped : contents) {
				//	FileHandle fh = new FileHandle(unzipped.toURI());
				//	fh.setProperty("dir",unzipToDir.getName());
				//	files.add(fh);			
				//}
				
				this.collectHandles(files, unzipToDir);

			}
			
		}
		
		
		
		return files;
		
	}

}
