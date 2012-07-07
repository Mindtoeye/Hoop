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

import org.w3c.dom.Element;

import edu.cmu.cs.in.base.INXMLBase;
import edu.cmu.cs.in.base.io.INFileManager;

/** 
 * @author Martin van Velsen
 */
public class INHoopProjectFile extends INXMLBase
{
	private String fileURI="";
	protected INFileManager fManager=null;
	private Boolean virginFile=true;
	
	/**
	 * 
	 */
	public INHoopProjectFile ()
	{
		setClassName ("INHoopProjectFile");
		debug ("INHoopProjectFile ()");		
		
		fManager=new INFileManager ();
	}
	/**
	 * 
	 */
	public void setVirginFile(Boolean virginFile) 
	{
		this.virginFile = virginFile;
	}
	/** 
	 * @return Boolean
	 */
	public Boolean getVirginFile() 
	{
		return virginFile;
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
	public Boolean save ()
	{
		debug ("save ()");
		
		this.virginFile=false;
		
		return (fManager.saveContents(fileURI,this.toXML()));
	}	
	/**
	 * 
	 */
	public Boolean saveAs (String aURI)
	{
		debug ("saveAs ("+aURI+")");
		
		setFileURI (aURI);
		
		return (save ());
	}		
	/**
	 * 
	 */
	public Boolean load (String aURI)
	{
		debug ("load ("+aURI+")");
		
		setFileURI (aURI);
		
		String aContent=fManager.loadContents (aURI);
		
		Element root=this.fromXMLString (aContent);
		
		if (root==null)
			return (false);
		
		fromXML (root);
		
		this.virginFile=false;
		
		return (true);
	}	
	/**
	*
	*/	
	public Boolean fromXML(Element root) 
	{
		debug ("fromXML ()");
				
		return (true);
	}	
	/**
	*
	*/	
	public String toXML() 
	{
		debug ("toXML ()");
	
		StringBuffer formatted=new StringBuffer ();
		formatted.append (super.toXML());
		
		
		return (formatted.toString());
	}	
}
