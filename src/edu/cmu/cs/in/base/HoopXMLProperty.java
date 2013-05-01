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

import java.util.ArrayList;
import org.w3c.dom.*;

/** 
 * @author Martin van Velsen
 */
public class HoopXMLProperty extends HoopXMLBase
{
	public ArrayList <HoopXMLPropertyAttr> attr;
 
	HoopXMLPropertyAttr coreAttribute=null;
 
	/**
	 *  
	 */
	public HoopXMLProperty () 
	{  
		setClassName ("HoopXMLProperty");
		//debug ("HoopXMLProperty ()");
  
		attr=new ArrayList<HoopXMLPropertyAttr> ();
  
		coreAttribute=new HoopXMLPropertyAttr ();
		coreAttribute.name="undefined";
		coreAttribute.value="undefined";
		attr.add (coreAttribute);
	}
	/**
	 *  
	 */
	public void setName (String a_name)
	{
		coreAttribute.name=a_name;
	}
	/**
	 *  
	 */
	public String getName ()
	{
		return (coreAttribute.name);
	}
	/**
	 *  
	 */
	public void setValue (String a_value)
	{
		coreAttribute.value=a_value;
	}
	/**
	 *  
	 */
	public String getValue ()
	{
		return (coreAttribute.value);
	}
	/**
	 *  
	 */
	public HoopXMLPropertyAttr findAttribute (String a_name)
	{
		for (int i=0;i<attr.size();i++)
		{
			HoopXMLPropertyAttr temper=attr.get(i);
			if (temper.name==a_name)
				return (temper);
		}
	 
		return (null);
	}
	/**
	 *  
	 */
	public void setAttribute (String an_attribute,String a_value)
	{
		HoopXMLPropertyAttr temp=findAttribute (an_attribute);
		if (temp==null)
		{
			temp=new HoopXMLPropertyAttr ();
			temp.setName(an_attribute);
		}

		temp.setValue(a_value);
	}
	/**
	 *  
	 */
	public String getAttribute (String an_attribute)
	{
		HoopXMLPropertyAttr temp=findAttribute (an_attribute);
		if (temp!=null)
			return (temp.getValue());
		 
		return (null);	 
	} 
	/**
	 *  
	 */
	public void fromXML (Node a_root)
	{
		//debug ("fromXML ()");
  
		Element elm=(Element) a_root;
  
		coreAttribute.setName(elm.getAttribute ("name"));
		coreAttribute.setValue(elm.getAttribute ("value"));
  
		//debug ("Property: (" +name+ ","+value+")");
	}
	/**
	 *  
	 */
	public void fromString (String a_string)
	{
		//debug ("fromString ()");
  
	}
	/**
	 *  
	 */
	public String toString ()
	{
		//debug ("toString ()");
  
		StringBuffer buff=new StringBuffer ();
		buff.append("<node ");
	 
		for (int i=0;i<attr.size();i++)
		{
			HoopXMLPropertyAttr temper=attr.get(i);
	
			if (i==0)
			{
				buff.append("name=\""+temper.getName()+"\" value=\""+temper.getValue()+"\" ");
			}
			else
			{
				buff.append(temper.getName());
				buff.append("=");
				buff.append("\"");
				buff.append(temper.getValue ());
				buff.append("\" ");
			}
		}
	 
		buff.append(" />");
	 
		return (buff.toString());
	 
		//return ("<node name=\""+name+"\" value=\""+value+"\" label=\""+value+"\"  class=\""+getClassName ()+"\" />");
	} 
}
