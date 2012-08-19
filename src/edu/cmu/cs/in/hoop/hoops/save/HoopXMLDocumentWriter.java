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

package edu.cmu.cs.in.hoop.hoops.save;

import java.util.ArrayList;

import org.jdom.Element;

//import edu.cmu.cs.in.base.HoopDataType;
import edu.cmu.cs.in.base.kv.HoopKV;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.hoop.properties.types.HoopSerializable;
import edu.cmu.cs.in.hoop.properties.types.HoopStringSerializable;

/**
* 
*/
public class HoopXMLDocumentWriter extends HoopXMLWriter
{    			
	@SuppressWarnings("unused")
	private HoopStringSerializable author=null;
	@SuppressWarnings("unused")
	private HoopStringSerializable title=null;
	@SuppressWarnings("unused")
	private HoopStringSerializable abstr=null;
	@SuppressWarnings("unused")
	private HoopStringSerializable createDate=null;
	@SuppressWarnings("unused")
	private HoopStringSerializable description=null;
	@SuppressWarnings("unused")
	private HoopStringSerializable text=null;
	@SuppressWarnings("unused")
	private HoopStringSerializable keywords=null;
	
	/**
	 *
	 */
    public HoopXMLDocumentWriter () 
    {
		setClassName ("HoopXMLDocumentWriter");
		debug ("HoopXMLDocumentWriter ()");
												
		setHoopDescription ("Write a Hoop Document as an XML file");
		
		author=new HoopStringSerializable (this,"author","author");
		title=new HoopStringSerializable (this,"title","title");
		abstr=new HoopStringSerializable (this,"abstr","abstr");
		createDate=new HoopStringSerializable (this,"create","date created");
		description=new HoopStringSerializable (this,"description","description");
		text=new HoopStringSerializable (this,"text","text");
		keywords=new HoopStringSerializable (this,"keywords","keywords");		
    }
	/**
	 *
	 */
	public Boolean runHoop (HoopBase inHoop)
	{		
		debug ("runHoop ()");
		
		ArrayList <HoopKV> inData=inHoop.getData();
		
		if (inData==null)
		{
			this.setErrorString("Error: no input data to work with");
			return (false);
		}		
		
		if (URI.getDirsOnly()==false)
		{
			Element fileElement=new Element ("hoopoutput");
		
			fileElement.addContent(typesToXML (inHoop));
		
			Element dataElement=new Element ("content");
		
			fileElement.addContent(dataElement);
								
			for (int t=0;t<inData.size();t++)
			{
				HoopKV aKV=inData.get(t);
												
				dataElement.addContent(kvToElement (inHoop,aKV));
			}
			
			return (saveXML (fileElement));	
		}	
		else
		{							
			for (int t=0;t<inData.size();t++)
			{
				HoopKV aKV=inData.get(t);
					
				Element fileElement=new Element ("hoopoutput");
					
				fileElement.addContent (typesToXML (inHoop));
				
				Element dataElement=new Element ("content");
				
				fileElement.addContent(dataElement);					
												
				dataElement.addContent(kvToElement (inHoop,aKV));
								
				Boolean result=saveXML (fileElement,t);
					
				if (result==false)
					return (false);
			}
		}
		
		return (true);	
	}	
	/**
	 * 
	 */
	private Element kvToElement (HoopBase inHoop,HoopKV aKV)
	{		
		debug ("kvToElement ()");
		
		Element documentElement=new Element ("document");
		
		Element keyElement=new Element ("key");
		keyElement.setText(aKV.getKeyString());
		documentElement.addContent(keyElement);
		
		ArrayList <Object> vals=aKV.getValuesRaw ();
						
		for (int j=0;j<vals.size();j++)
		{
			String aTypeName=inHoop.getKVTypeName (j);
			
			//debug ("Mapping: " + aTypeName);
			
			String remapped=mapType (aTypeName);
								
			Element subElement=new Element (remapped);
			subElement.setText(aKV.getValueAsString(j));
			documentElement.addContent(subElement);
		}				
		
		return (documentElement);
	}
	/**
	 * 
	 */
	private String mapType (String aTypeName)
	{
		ArrayList <HoopSerializable> props=getProperties ();
		
		for (int i=0;i<props.size();i++)
		{
			HoopSerializable aProp=props.get(i);
			
			if (aProp.getValue().equalsIgnoreCase(aTypeName)==true)
			{
				//debug ("Mapped " + aTypeName + " to: " + aProp.getInstanceName());
				
				return (aProp.getInstanceName());
			}
		}
						
		return (aTypeName);
	}
	/**
	 * 
	 */
	public HoopBase copy ()
	{
		return (new HoopXMLDocumentWriter ());
	}		
}
