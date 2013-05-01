/** 
 * Author: Martin van Velsen <vvelsen@cs.cmu.edu>
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as 
 *  published by the Free Software Foundation, either version 3 of the 
 *  License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package edu.cmu.cs.in.hoop.project.export;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import edu.cmu.cs.in.hoop.project.HoopProject;
 
/**
 * 
 */
public class HoopProjectZipExport extends HoopProjectExportInterface
{
    List<String> fileList;
    private static final String OUTPUT_ZIP_FILE = "C:\\MyFile.zip";
    private static final String SOURCE_FOLDER = "C:\\testzip";
 
    /**
     * 
     */
    public HoopProjectZipExport()
    {
		setClassName ("HoopProjectZipExport");
		debug ("HoopProjectZipExport ()");
		
    	fileList = new ArrayList<String>();
    	
    	setDescription ("Export as Zip file");
    }
 
    /*
    public static void main( String[] args )
    {
    	AppZip appZip = new AppZip();
    	appZip.generateFileList(new File(SOURCE_FOLDER));
    	appZip.zipIt(OUTPUT_ZIP_FILE);
    }
    */
 
	/**
	 * 
	 */
	public Boolean exportProject (HoopProject aProject)
	{
		debug ("exportProject ()");
		
		return (true);
	}    
    
    /**
     * 
     */
    public void zipIt(String zipFile)
    {
    	debug ("zipIt ("+zipFile+")");
    	
    	byte[] buffer = new byte[1024];
 
    	try
    	{
    		FileOutputStream fos = new FileOutputStream(zipFile);
    		ZipOutputStream zos = new ZipOutputStream(fos);
 
    		System.out.println("Output to Zip : " + zipFile);
 
    		for(String file : this.fileList)
    		{ 
    			System.out.println("File Added : " + file);
    			ZipEntry ze= new ZipEntry(file);
    			zos.putNextEntry(ze);
 
    			FileInputStream in=new FileInputStream(SOURCE_FOLDER + File.separator + file);
 
    			int len;
    			
    			while ((len = in.read(buffer)) > 0) 
    			{
    				zos.write(buffer, 0, len);
    			}
 
    			in.close();
    		}
 
    		zos.closeEntry();
    		//remember close it
    		zos.close();
 
    		System.out.println("Done");
    	}
    	catch(IOException ex)
    	{
    		ex.printStackTrace();   
    	}
    }
    /**
     * Traverse a directory and get all files,
     * and add the file into fileList  
     * @param node file or directory
     */
    public void generateFileList(File node)
    {
    	debug ("generateFileList ()");
    	
    	//add file only
    	if(node.isFile())
    	{
    		fileList.add(generateZipEntry(node.getAbsoluteFile().toString()));
    	}
 
    	if(node.isDirectory())
    	{
    		String[] subNote = node.list();
    		
    		for(String filename : subNote)
    		{
    			generateFileList(new File(node, filename));
    		}
    	}
    }
    /**
     * Format the file path for zip
     * @param file file path
     * @return Formatted file path
     */
    private String generateZipEntry(String file)
    {
    	debug ("generateZipEntry ("+file+")");
    	
    	return file.substring(SOURCE_FOLDER.length()+1, file.length());
    }
}
