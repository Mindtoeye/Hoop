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

package edu.cmu.cs.in.hoop.properties;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;

import edu.cmu.cs.in.base.HoopXMLBase;
import edu.cmu.cs.in.hoop.hoops.base.HoopBaseTyped;
import edu.cmu.cs.in.hoop.hoops.base.HoopPropertyContainer;
import edu.cmu.cs.in.hoop.properties.types.HoopSerializable;

/**
*
*/
public class HoopStoredProperties extends HoopXMLBase implements HoopPropertyContainer
{    			
	private ArrayList <HoopSerializable> properties=null;
	
	/**
	 *
	 */
    public HoopStoredProperties () 
    {
		setClassName ("HoopStoredProperties");
		debug ("HoopStoredProperties ()");
						
		properties=new ArrayList<HoopSerializable> ();		
    }
    /**
     * 
     */
    public ArrayList <HoopSerializable> getProperties ()
    {
    	return (properties);
    }
    /**
     * 
     */
    public HoopSerializable getProperty (String anInstance)
    {
    	debug ("getProperty ("+anInstance+")");
    	
    	for (int i=0;i<properties.size();i++)
    	{
    		HoopSerializable prop=properties.get(i);
    	
    		debug ("Comparing " + prop.getInstanceName() + " to: " + anInstance);
    		
    		if (prop.getInstanceName().equalsIgnoreCase(anInstance)==true)
    			return (prop);
    	}
    	
    	return (null);
    }
    /**
     * 
     */
    public int getPropertyIndex (String anInstance)
    {
    	debug ("getProperty ("+anInstance+")");
    	
    	for (int i=0;i<properties.size();i++)
    	{
    		HoopSerializable prop=properties.get(i);
    		
    		if (prop.getInstanceName().equals(anInstance.toLowerCase())==true)
    			return (i);
    	}
    	
    	return (-1);
    }
    /**
     * 
     */
    public void removeProperty (String aProperty)
    {
    	HoopSerializable checker=getProperty (aProperty);
    	
    	if (checker!=null)
    	{
    		properties.remove(checker);
    	}
    }    
    /**
     * 
     */
    public void enableProperty (String aProperty,Boolean aVal)
    {
    	HoopSerializable checker=getProperty (aProperty);
    	
    	if (checker!=null)    
    		checker.setEnabled (aVal);
    }    
    /**
     * 
     */
    public void enableProperty (HoopSerializable aProperty,Boolean aVal)
    {
    	aProperty.setEnabled (aVal);
    }    
	/**
	 * 
	 */
	public Boolean fromXML(Element anElement) 
	{
		debug ("fromXML ()");
					
		if (anElement.getName().equals("properties")==true)
		{
			debug ("Assigning visual information to hoop ...");
				
			List <?> propList=anElement.getChildren ();
				
			for (int j=0; j<propList.size(); j++) 
			{
				Element propNode = (Element) propList.get(j);
					
				if (propNode.getName().equals ("serializable")==true)
				{
					String targetInstance=propNode.getAttributeValue ("instance");
					
					if (targetInstance!=null)
					{
						debug ("Configuring serialized property " + targetInstance + " from xml ...");
							
						HoopSerializable prop=this.getProperty(targetInstance);
						
						if (prop!=null)
						{
							prop.fromXML(propNode);
						}
						else
						{
							debug ("Error: unable to find property instance '" + targetInstance + "' in hoop");
							//listProperties ();
							return (false);
						}	
					}
				}
			}
		}
			
		return (true);
	}    
	/**
	 * 
	 */
	public Element toXML ()
	{
		debug ("toXML ()");
				
		Element propertiesElement=new Element ("properties");							
				
		for (int i=0;i<properties.size();i++)
		{
			HoopSerializable prop=properties.get(i);
			
			if (prop.getEnabled()==true)
			{
				propertiesElement.addContent(prop.toXML());
			}	
		}		
				
		return (propertiesElement);
	}
	/**
	 * 
	 */
	/*
	@Override
	public HoopBaseTyped getPropParent() 
	{
	
		return this;
	}
	*/
	/**
	 * 
	 */
	@Override
	public void propertyChanged() 
	{
		// event listener
	}		    
}
