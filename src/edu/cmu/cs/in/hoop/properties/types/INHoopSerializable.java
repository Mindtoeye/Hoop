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

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
//import org.xml.sax.InputSource;
//import org.xml.sax.SAXException;
//import org.xml.sax.SAXParseException;

import edu.cmu.cs.in.base.INXMLBase;
import edu.cmu.cs.in.hoop.base.INHoopPropertyContainer;

/**

*/
public class INHoopSerializable extends INXMLBase
{			
	protected String value="";
	protected String type="";
	
	/// Either one of Boolean, Float, Integer, String where higher level types such as Color and Font will have t be mapped to base types
	protected String format=""; 
	
	private Boolean touched=false;
	
	private INHoopPropertyContainer propParent=null;
	
	/**
	 *
	 */
    public INHoopSerializable (INHoopPropertyContainer aParent) 
    {
    	setClassName ("INHoopSerializable");
    	debug ("INHoopSerializable ()");
    	
    	propParent=aParent;
    	
    	if (propParent!=null)
    	{
    		ArrayList <INHoopSerializable> props=propParent.getProperties ();
    		if (props!=null)
    		{
    			debug ("Adding this to parent properties list");
    			props.add(this);
    		}
    	}
    }
	/**
	 *
	 */    
	public INHoopPropertyContainer getPropParent() 
	{
		return propParent;
	}
	/**
	 *
	 */	
	public void setPropParent(INHoopPropertyContainer propParent) 
	{
		this.propParent = propParent;
	}    
	/**
	 *
	 */    
	public void setTouched(Boolean touched) 
	{
		this.touched = touched;
	}
	/**
	 *
	 */	
	public Boolean getTouched() 
	{
		return touched;
	}    
	/**
	 *
	 */
    public void setValue (String aValue)
    {
    	value=aValue;
    }
	/**
	 *
	 */
    public String getValue ()
    {
    	return (value);
    }  
	/**
	 *
	 */	
	public void setType(String aType) 
	{
		this.type = aType;
	}
	/**
	 *
	 */	
	public String getType() 
	{
		return type;
	}  
	/**
	 *
	 */	
	public void setFormat(String aFormat) 
	{
		this.format = aFormat;
	}
	/**
	 *
	 */	
	public String getFormat() 
	{
		return format;
	} 	
	/**
	 * @return 
	 *
	 */
    public Boolean fromXML (Element anElement)
    {
    	debug ("fromXML ()");
    	
    	if (anElement.getNodeType()!=Node.ELEMENT_NODE)
    		return (false);
    	
    	Node node=anElement;
  
    	NodeList children=node.getChildNodes ();
    	      
    	if (children==null)
    	{
    		debug ("Internal error: children list is null");
    		return (false);
    	}
		
    	if (children.getLength()>0)
    	{    
    		for (int i=0;i<children.getLength();i++) 
    		{
    			Node childNode=children.item (i);
            
    			debug ("Parsing text node ("+childNode.getNodeName()+")...");
            
    			if (childNode.getNodeName().equals ("name")==true)
    			{
    				debug ("Parsing selection: " + childNode.getChildNodes ().item (0).getNodeValue());
    				setInstanceName (childNode.getChildNodes ().item (0).getNodeValue());
    			}   
			
    			if (childNode.getNodeName().equals ("value")==true)
    			{
    				setValue (childNode.getChildNodes ().item (0).getNodeValue());				
    				//setType (childNode.getAttributeValue("type")));
    				setType (getAttributeValue(childNode,"type"));
    			}
    		}
    	}	
		
		return (true);
    }
	/**
	*	
	*/		
	public String toString()
	{
		StringBuffer buffer=new StringBuffer();
		buffer.append(getClassOpen ()+"<name>"+getInstanceName ()+"</name><value fmt=\"text\" type=\"String\">"+value+"</value>"+getClassClose ());
		return (buffer.toString ());
	}
}
