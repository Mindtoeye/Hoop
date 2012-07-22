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

import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import org.jdom.Document;
import org.jdom.output.Format;

import edu.cmu.cs.in.base.HoopXMLBase;
import edu.cmu.cs.in.base.io.HoopFileManager;

/** 
 * @author Martin van Velsen
 */
public class HoopProjectFile extends HoopXMLBase
{
	private String fileURI="";
	protected HoopFileManager fManager=null;
	private Boolean virginFile=true;
	
	/**
	 * 
	 */
	public HoopProjectFile ()
	{
		setClassName ("HoopProjectFile");
		setXMLID("hoopfile");		
		debug ("HoopProjectFile ()");		
		
		fManager=new HoopFileManager ();
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
		
		Document document = new Document();
		
		Element root=this.toXML();
		
		document.setContent(root);
		
		XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
        String xmlString = outputter.outputString(document);
		
		return (fManager.saveContents(fileURI,xmlString));
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
	public Element toXML() 
	{
		debug ("toXML ()");
	
		return (super.toXML());
	}	
}
