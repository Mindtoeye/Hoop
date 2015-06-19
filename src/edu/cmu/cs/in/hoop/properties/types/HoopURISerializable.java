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

package edu.cmu.cs.in.hoop.properties.types;

import java.util.List;

import org.jdom.Element;

import edu.cmu.cs.in.base.HoopDataType;
import edu.cmu.cs.in.hoop.hoops.base.HoopPropertyContainer;

/**

*/
public class HoopURISerializable extends HoopSerializable
{
	private String fileExtension=".txt";
	private Boolean dirsOnly=false;
	
	/**
	 *
	 */
	public HoopURISerializable (HoopPropertyContainer aParent,String aName,String aPropValue) 
	{
		super (aParent,aName);
		
		setDataType (HoopDataType.URI);
		setClassName ("HoopURISerializable");
		debug ("HoopURISerializable ()");
  	
		this.setValue(aPropValue);
	}		
	/**
	 *
	 */
    public HoopURISerializable (HoopPropertyContainer aParent,String aName) 
    {
    	super (aParent,aName);
    	
    	setDataType (HoopDataType.URI);
    	setClassName ("HoopURISerializable");
    	debug ("HoopURISerializable ()");
    	
    }
	/**
	 * 
	 */
	public String getFileExtension() 
	{
		return fileExtension;
	}
	/**
	 * 
	 */	
	public void setFileExtension(String fileExtension) 
	{
		this.fileExtension = fileExtension;
	}    
    /** 
     * @param propValue
     */
	public void setPropValue(String propValue) 
	{
		this.setValue(propValue);
	}
	/** 
	 * @return
	 */
	public String getPropValue() 
	{
		return (getValue ());
	}
	/**
	 * 
	 */
	public Boolean getDirsOnly() 
	{
		return dirsOnly;
	}
	/**
	 * 
	 */
	public void setDirsOnly(Boolean dirsOnly) 
	{
		this.dirsOnly = dirsOnly;
	}      
	/**
	 * @return 
	 *
	 */
    public Boolean fromXML (Element anElement)
    {
    	debug ("fromXML ("+anElement.getName()+")");
    	
    	/*
    	if (super.fromXML(anElement)==false)
    	{
    		return (false);
    	}
    	*/
    	
		if (anElement.getName().equals(this.getXMLID())==false)
		{
			debug ("Error: element name is not " + this.getXMLID());
			return (false);			
		}
		
		this.setInstanceName (anElement.getAttributeValue("instance"));    	
    	    	    	  
    	List <?> children=anElement.getChildren ("value");
    	      
    	if (children==null)
    	{
    		debug ("Internal error: children list is null");
    		return (false);
    	}
		    	
    	for (int i=0;i<children.size();i++) 
    	{    		
    		Element node = (Element) children.get(i);
            
   			debug ("Parsing text node ("+node.getName()+")...");
   			
   			if (node.getAttribute("format")!=null)
   			{
   				debug ("format: " + node.getAttributeValue("format"));
   		
   				setFormat (node.getAttributeValue("format"));
   			}
   			else
   				debug ("Attribute format not found in serialible property");
   	    	
   	    	if (node.getAttribute("fileExtension")!=null)
   	    	{
   	    		debug ("fileExtension: " + node.getAttributeValue("fileExtension"));
   	    		
   	    		setFileExtension (node.getAttributeValue("fileExtension"));
   	    	}	
   	    	else
   				debug ("Attribute fileExtension not found in serialible property");
   				
   			if (node.getAttribute ("dirsOnly")!=null)
   			{   			
   				debug ("dirsOnly: " + node.getAttributeValue("dirsOnly"));
   				   			   				
   				if (node.getAttributeValue("dirsOnly").equalsIgnoreCase("true")==true)
   					dirsOnly=true;
   				else
   					dirsOnly=false;
   			}	
   			else
   				debug ("Attribute dirsOnly not found in serialible property");
   			
   			debug ("Setting value to: " + node.getText());
   			
   			this.setValue(node.getText());
   		}
		
		return (true);
    }
	/**
	 * 
	 */
	public Element toXML ()
	{
		debug ("toXML ()");
				
		Element classElement=toXMLID();
						
		Element valueElement=new Element ("value");
		valueElement.setAttribute("format",this.getFormat ());
		valueElement.setAttribute("type",this.typeToString ());
		valueElement.setAttribute ("fileExtension",fileExtension);
		valueElement.setAttribute ("dirsOnly",dirsOnly.toString());
		valueElement.setText(this.getValue());
		
		classElement.addContent(valueElement);
		
		return (classElement);		
	}	
}
