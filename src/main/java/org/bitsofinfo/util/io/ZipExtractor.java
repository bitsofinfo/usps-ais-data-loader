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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Extracts a ZIP file to a given target directory.
 * 
 * @author bitsofinfo.g [at] gmail [dot] com
 *
 */
public class ZipExtractor {
	
	public void extractZipToDirectory(File zipfile, File extractToDir) throws Exception {
		FileInputStream fis = null;
		ZipInputStream zis = null;
		try {
			fis = new FileInputStream(zipfile);
			zis = new ZipInputStream(fis);
		} catch(Exception e) {
			throw new Exception("Error attempting to create ZipInputStream against " + zipfile.getAbsolutePath() + " " + e.getMessage());
		}
			
		try {
			// if extract to dir does not exist create it
			if (!extractToDir.exists()) {
				extractToDir.mkdir();
			}
			
			ZipEntry entry = null;
			while((entry = zis.getNextEntry()) != null) {
				String name = entry.getName();
				File zipFile = new File(extractToDir.getAbsolutePath() + "/" + name);

				if (entry.isDirectory()) {
					zipFile.mkdir();
	
				// not a dir but file, write it out
				} else {
					
					// could be passed as path spec
					if (name.indexOf("/") != -1 || name.indexOf("\\") != -1) {
						String dirToken = "/";
						if (name.indexOf("\\") != -1) {
							dirToken = "\\";
						}
						String rawPath = name.substring(0,name.lastIndexOf(dirToken));
						StringTokenizer st = new StringTokenizer(rawPath,dirToken);
						StringBuffer path = new StringBuffer(extractToDir.getAbsolutePath());
						while(st.hasMoreTokens()) {
							path.append(dirToken + st.nextToken());
							File dir = new File(path.toString());
							try {
								if (!dir.exists() || !dir.isDirectory()) {
									dir.mkdir();
								}
							} catch (SecurityException e) {
								//e.printStackTrace();
								throw new Exception("Could not create new directory due to lack of persmission? " + dir.getAbsoluteFile() +" " + e.getMessage());
							}
						}
					}
					
					zipFile.createNewFile();
					FileOutputStream fos = new FileOutputStream(zipFile);
					int buffSize = 32 * 1024;
					int read = 0;
					byte[] input_bytes = new byte[buffSize];
					while((read = zis.read(input_bytes,0,buffSize)) != -1) {
						fos.write(input_bytes,0,read);
					}
					fos.close();
				}
				
				zis.closeEntry();
			}
			
			fis.close();
			zis.close();
		
		} catch(Exception e) {
			throw new Exception("Error extracting zip file located at " + zipfile.getAbsolutePath() + " " + e.getMessage());
		}
	}
}
