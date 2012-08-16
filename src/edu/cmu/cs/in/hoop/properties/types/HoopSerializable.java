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
import java.util.List;

import org.jdom.Element;

import edu.cmu.cs.in.base.HoopDataType;
import edu.cmu.cs.in.base.HoopXMLBase;
import edu.cmu.cs.in.hoop.hoops.base.HoopPropertyContainer;

/**
* 
*/
public class HoopSerializable extends HoopXMLBase
{			
	/// Either one of Boolean, Float, Integer, String where higher level types such as Color and Font will have t be mapped to base types
	//protected String type="";	
	protected String format="Text";
	protected String value="";
	
	private Boolean touched=false;
	
	protected HoopPropertyContainer propParent=null;
	
	/**
	 *
	 */
	public HoopSerializable (HoopPropertyContainer aParent) 
	{
		setType (HoopDataType.CLASS); // Default anyway but this makes it formal
		setClassName ("HoopSerializable");
		setXMLID("serializable");
		debug ("HoopSerializable ()");
						
		propParent=aParent;		
		
		assignName (aParent);
					   	
		if (propParent!=null)
		{
			ArrayList <HoopSerializable> props=propParent.getProperties ();
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
	public HoopSerializable (HoopPropertyContainer aParent,String aName) 
	{
		setType (HoopDataType.CLASS); // Default anyway but this makes it formal
		setClassName ("HoopSerializable");
		setXMLID("serializable");
		debug ("HoopSerializable ()");
		
		propParent=aParent;		
		
		if (aName==null)
		{
			assignName (aParent);
		}
		else
			this.setInstanceName (aName);
			
		   	
		if (propParent!=null)
		{
			ArrayList <HoopSerializable> props=propParent.getProperties ();
			if (props!=null)
			{
				debug ("Adding this to parent properties list");
				props.add(this);
			}
		}
	}	
	/**
	 * Field.equals (): Compares this Field against the specified object. Returns true 
	 * if the objects are the same. Two Field objects are the same if they were declared 
	 * by the same class and have the same name and type. 
	 */
	private void assignName (HoopPropertyContainer aParent)
	{
		debug ("assignName ()");
		
		String aName=aParent.getFieldName (this);
		
		if (aName!=null)
		{
			debug ("My name is: " + aName);
			this.setInstanceName(aName);
		}
		else
		{
			debug ("Unable to find my instance name");
			this.setInstanceName("undefined");
		}
	}
	/**
	 *
	 */    
	public HoopPropertyContainer getPropParent() 
	{
		return propParent;
	}
	/**
	 *
	 */	
	public void setPropParent(HoopPropertyContainer propParent) 
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
    /*
	public void setType(String aType) 
	{
		this.type = aType;
	}
	*/
	/**
	 *
	 */
    /*
	public String getType() 
	{
		return type;
	} 
	*/ 
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
    	
    	if (super.fromXML(anElement)==false)
    	{
    		return (false);
    	}
    	    	    	  
    	List <?> children =anElement.getChildren ("value");
    	      
    	if (children==null)
    	{
    		debug ("Internal error: children list is null");
    		return (false);
    	}
		
    	for (int i=0;i<children.size();i++) 
    	{    		
    		Element node = (Element) children.get(i);
            
   			debug ("Parsing text node ("+node.getName()+")...");
            
   			if (node.getName().equals ("format")==true)
   			{
   				debug ("Parsing selection: " + node.getName() + ":"+node.getText());
   				setFormat (node.getText());
   			}      			
   			   			
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
		
		Element classElement=super.toXML();
				
		Element valueElement=new Element ("value");
		valueElement.setAttribute("format",this.getFormat ());
		valueElement.setAttribute("type",this.typeToString ());
		valueElement.setText(this.getValue());
		
		classElement.addContent(valueElement);
		
		return (classElement);		
	}
}
