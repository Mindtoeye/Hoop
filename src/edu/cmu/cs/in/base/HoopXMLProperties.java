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

package edu.cmu.cs.in.base;

import org.w3c.dom.*;

import java.util.ArrayList;

/** 
 * @author Martin van Velsen
 */
public class HoopXMLProperties extends HoopXMLProperty
{
	private ArrayList <HoopXMLProperty> properties;
  
	/**
	 *  
	 */
	public HoopXMLProperties () 
	{
		setClassName ("HoopXMLProperties");
		//debug ("HoopXMLProperties ()");
  
		properties=new ArrayList <HoopXMLProperty> ();
	}
	/**
	 *  
	 */
	public void addProperty (String a_property,String a_value)
	{
		HoopXMLProperty prop=new HoopXMLProperty ();
		prop.setName(a_property);
		prop.setValue(a_value);
		properties.add(prop);
	}  
	/**
	 *  
	 */
	public void setProperty (String a_property,String a_value)
	{
		for (int i = 0, n = properties.size(); i < n; i++) 
		{
			HoopXMLProperty prop=(HoopXMLProperty) properties.get (i);
			if (prop.getName ().equals (a_property)==true)
				prop.setValue (a_value);
		}   
	} 
	/**
	 *  
	 */
	public String getProperty (String a_property)
	{
		for (int i = 0, n = properties.size(); i < n; i++) 
		{
			HoopXMLProperty prop=(HoopXMLProperty) properties.get (i);
			if (prop.getName ().equals (a_property)==true)
				return (prop.getValue ());
		}
  
		return (null);
	}  
	/**
	 *  
	 */
	public Boolean fromXML (Element root)
	{
		debug ("fromXML ()");
     
		if ((root.getNodeName ().equals ("properties")) || (root.getNodeName().equals ("node")))
		{
			coreAttribute.setName(root.getAttribute ("name"));
			coreAttribute.setValue(root.getAttribute ("value"));
      
			NodeList children=root.getChildNodes ();
      
			if (children==null)
			{
				debug ("Internal error: children list is null");
				return (false);
			}
      
			if (children.getLength()>0)
			{    
				for (int i=0;i<children.getLength();i++) 
				{
					Node node=children.item (i);
					//showNodeType (node);
					if (node.getNodeType ()==Node.ELEMENT_NODE)
					{
						HoopXMLProperty prop=new HoopXMLProperty ();
						prop.fromXML (node);
						properties.add (prop);
					} 
				}                        
			}
			//else
			// debug ("No sub nodes found");
		}  
  
		return (true);
	}
	/**
	 *  
	 */
	public void fromString (String a_string)
	{
		debug ("fromFile ()");
	}
	/**
	 *  
	 */
	public String toString ()
	{
		//debug ("toString ()");
  
		StringBuffer result=new StringBuffer ();
  
		result.append ("<node name=\""+getName ()+"\" value=\""+getValue ()+"\" label=\""+getValue ()+"\"  class=\""+getClassName ()+"\" >\n");
  
		//debug ("Doing a toString () on "+properties.size ()+" elements");
  
		for (int i=0,n=properties.size ();i<n;i++) 
		{   
			HoopXMLProperty prop=(HoopXMLProperty) properties.get (i);
			result.append ("\t");
			result.append (prop.toString ());
			result.append ("\n");
		}  
  
		result.append ("</node>");
  
		return (result.toString ());
	}
}
