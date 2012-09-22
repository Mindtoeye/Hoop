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

package edu.cmu.cs.in.base.io;

import java.util.ArrayList;

/**
 * 
 */
public interface HoopVFSLInterface
{	
	/**
	 *
	 */	
	public StringBuilder getContents(); 
	
	/**
	 *
	 */	
	public String getURI();
	
	/**
	 *
	 */	
	public void setURI(String uRI);	
	/**
	 *
	 */  
	public boolean isStreamOpen ();

	/**
	 *
	 */  
	public boolean openStream (String aFileURI); 

	/**
	 *
	 */  
	public void closeStream ();

	/**
	 *
	 */ 
	public void writeToStream (String aContents);

	/**
	 *
	 */	
	public String loadContents (String aFileURI);

	/**
	 *
	 */  
	public boolean saveContents (String aFileURI,String aContents); 

	/**
	 *
	 */  
	public boolean appendContents (String aFileURI,String aContents);
	
	/**
	 * 
	 */
	public boolean createDirectory (String aDirURI);
	
	/**
	 * 
	 */
	public boolean removeDirectory (String aDirURI);
	
	/**
	 * 
	 */
	public boolean doesFileExist (String aFileURI);
	
	/**
	 * 
	 */
	public ArrayList<String> listDirectoryEntries (String aDirURI);
	
	/**
	 * 
	 */
	public ArrayList<String> listFiles (String aDirURI);
	
	/**
	 * 
	 */
	public Long getFileTime (String aPath);
}
