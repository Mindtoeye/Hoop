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

import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import org.jdom.Document;
import org.jdom.output.Format;

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.base.HoopStringTools;

/** 
 * 
 */
public class HoopProjectFile extends HoopFile
{
	private Boolean virginFile=true;
	
	private long lastChecksum=0;
	
	/**
	 * 
	 */
	public HoopProjectFile ()
	{
		setClassName ("HoopProjectFile");
		setXMLID("hoopfile");		
		//debug ("HoopProjectFile ()");				
	}
	/**
	 * 
	 */
	public void reset ()
	{
		debug ("reset ()");
		
		// Called when this file already exists and is used for loading new data
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
	public Boolean save ()
	{
		debug ("save ()");
		
		this.virginFile=false;
		
		Document document = new Document();
		
		Element root=this.toXML();
		
		document.setContent(root);
		
		XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
        String xmlString = outputter.outputString(document);
		
        prepContents (xmlString);
        
		return (HoopLink.fManager.saveContents(getFileURI(),xmlString));
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
		
		String aContent=HoopLink.fManager.loadContents (aURI);
		
		if (aContent==null) // file not available
			return (false);
		
		prepContents (aContent);
		
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
	/**
	 * 
	 */
	protected void prepContents (String aContents)
	{
		debug ("prepContents ()");
		
        lastChecksum = HoopStringTools.calculateChecksum(aContents);
	}
	/**
	 * 
	 */
	public void resetChanged ()
	{
		debug ("resetChanged ()");
		
		Document document = new Document();
		
		Element root=this.toXML();
		
		document.setContent(root);
		
		XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
        String xmlString = outputter.outputString(document);		
		
        lastChecksum=HoopStringTools.calculateChecksum(xmlString);        
	}	
	/**
	 * 
	 */
	public boolean hasChanged ()
	{
		debug ("hasChanged ()");
		
		Document document = new Document();
		
		Element root=this.toXML();
		
		document.setContent(root);
		
		XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
        String xmlString = outputter.outputString(document);		
		
        long currentChecksum=HoopStringTools.calculateChecksum(xmlString);
    	
        debug ("Comparing last: " + lastChecksum + " to: " + currentChecksum);
        
    	if (lastChecksum!=currentChecksum)
    		return (true);
        
		return (false);
	}
}
