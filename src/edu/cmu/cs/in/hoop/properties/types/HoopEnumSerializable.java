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
import edu.cmu.cs.in.base.HoopStringTools;
import edu.cmu.cs.in.hoop.hoops.base.HoopPropertyContainer;

/**

*/
public class HoopEnumSerializable extends HoopSerializable
{		
	private String options="";
	
	/**
	 *
	 */
	public HoopEnumSerializable (HoopPropertyContainer aParent,String aName,String aPropValue) 
	{
		super (aParent,aName);
		
		setDataType (HoopDataType.ENUM);
		setClassName ("HoopEnumSerializable");
		debug ("HoopEnumSerializable ()");
  	
		options=aPropValue;
		
		ArrayList <String> test=getOptions();
		
		if (test.size()>0)
			setValue (test.get(0));
	}	
	/**
	 *
	 */
    public HoopEnumSerializable (HoopPropertyContainer aParent,String aName) 
    {
    	super (aParent,aName);
    	
    	setDataType (HoopDataType.ENUM);
    	setClassName ("HoopStringSerializable");
    	debug ("HoopStringSerializable ()");
    	
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
     * @param propValue
     */
	public void setOptions(String propValue) 
	{
		options=propValue;
	}
    /**
     * @param propValue
     */
	public void setOptions(String [] propValue) 
	{
		StringBuffer formatter=new StringBuffer ();
		
		for (int i=0;i<propValue.length;i++)
		{
			if (i==0)
				formatter.append(" , ");
			
			formatter.append(propValue [i]);
		}
		
		this.setValue(formatter.toString());
	}
	/**
	 * 
	 */
	public String getOptionString ()
	{
		return (options);
	}
	/** 
	 * @return
	 */
	public ArrayList <String> getOptions() 
	{		
		return (HoopStringTools.splitComma(options));
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
   			
   			if (node.getName().equals ("options")==true)
   			{
   				debug ("Parsing options: " + node.getName() + ":"+node.getText());
   				setOptions (node.getText());
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
		
		Element classElement=toXMLID();
				
		Element valueElement=new Element ("value");
		valueElement.setAttribute("format",this.getFormat ());
		valueElement.setAttribute("type",this.typeToString ());
		valueElement.setAttribute("options",this.getOptionString ());
		valueElement.setText(this.getValue());
		
		classElement.addContent(valueElement);
		
		return (classElement);		
	}	
}
