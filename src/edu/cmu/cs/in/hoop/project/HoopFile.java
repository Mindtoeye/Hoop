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

package edu.cmu.cs.in.hoop.project;

import java.io.File;
import java.util.ArrayList;

import edu.cmu.cs.in.base.HoopXMLBase;

/** 
 * 
 */
public class HoopFile extends HoopXMLBase
{
	private String fileURI="";
	private Boolean isDir=false;
	
	private ArrayList <HoopFile> subEntries=null;
	
	/**
	 * 
	 */
	public HoopFile ()
	{
		setClassName ("HoopProjectFile");		
		debug ("HoopProjectFile ()");
		
		subEntries=new ArrayList<HoopFile> ();
	}
	/**
	 * 
	 */
	public ArrayList <HoopFile> getSubEntries ()
	{
		return (subEntries);
	}
	/**
	 * 
	 */
	public void addSubEntry (HoopFile aFile)
	{
		subEntries.add(aFile);
	}
	/**
	 * 
	 */	
	public void setFileURI(String aURI) 
	{
		debug ("setFileURI ("+aURI+")");
		
		fileURI = aURI;
		
		File transf=new File (fileURI);
		
		debug ("Setting instance name to: " + transf.getName());
		
		this.setInstanceName(transf.getName());
	}
	/**
	 * 
	 */	
	public String getFileURI() 
	{
		return fileURI;
	}
	/**
	 * 
	 */
	public String getBasePath ()
	{
		File transf=new File (fileURI);
		
		//int index=fileURI.indexOf(transf.getP)
		
		return (transf.getParent());
	}
	/**
	 * 
	 */
	public Boolean getIsDir() 
	{
		return isDir;
	}
	/**
	 * 
	 */
	public void setIsDir(Boolean isDir) 
	{
		this.isDir = isDir;
	} 	
}
