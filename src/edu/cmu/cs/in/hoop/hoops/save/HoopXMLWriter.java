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

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import edu.cmu.cs.in.base.HoopDataType;
//import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.base.kv.HoopKV;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.hoop.properties.types.HoopBooleanSerializable;
import edu.cmu.cs.in.hoop.properties.types.HoopEnumSerializable;

/**
* 
*/
public class HoopXMLWriter extends HoopFileSaveBase
{    		
	private HoopEnumSerializable writeMode=null; // APPEND, OVERWRITE
	private HoopBooleanSerializable includeIndex=null;
	
	private Boolean repeatTypes=false;
	
	/**
	 *
	 */
    public HoopXMLWriter () 
    {
		setClassName ("HoopXMLWriter");
		debug ("HoopXMLWriter ()");
												
		setHoopDescription ("Write to an XML file");
		setFileExtension ("xml");
		
		writeMode=new HoopEnumSerializable (this,"writeMode","OVERWRITE,APPEND");
		includeIndex=new HoopBooleanSerializable (this,"includeIndex",false);
    }
    /**
     * 
     */
    public void setIncludeIndex (Boolean aVal)
    {
    	includeIndex.setPropValue(aVal);
    }
    /**
     * 
     */
    public Boolean getIncludeIndex ()
    {
    	return (includeIndex.getPropValue());
    }
    /**
     * 
     */
    protected Element typesToXML (HoopBase inHoop)
    {
    	debug ("typesToXML ()");
    	
		Element typeElement=new Element ("datatypes");
								
		ArrayList <HoopDataType> types=inHoop.getTypes();
						
		for (int n=0;n<types.size();n++)
		{			
			HoopDataType aType=types.get(n);
			
			Element aTypeElement=new Element ("type");
			
			aTypeElement.setAttribute("type",aType.getTypeValue ());
			aTypeElement.setAttribute("value",aType.typeToString ());
						
			typeElement.addContent(aTypeElement);
		}		
		
		return (typeElement);
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
		
		if (writeMode.getValue().equalsIgnoreCase("OVERWRITE")==true)
			this.setAlwaysOverwrite(true);
		
		ArrayList <HoopDataType> types=inHoop.getTypes();
				
		Element fileElement=new Element ("hoopoutput");
				
		if (getIncludeIndex ()==true)
			fileElement.addContent(typesToXML (inHoop));
										
		Element dataElement=new Element ("data");
			
		for (int t=0;t<inData.size();t++)
		{
			HoopKV aKV=inData.get(t);
								
			Element keyElement=new Element ("entry");
			keyElement.setAttribute ("key",aKV.getKeyString ());
							
			ArrayList<Object> vals=aKV.getValuesRaw();
				
			for (int i=0;i<vals.size();i++)
			{												
				Element valueElement=new Element ("value");
				
				if (repeatTypes==true)
				{
					HoopDataType aType=types.get(i);				
					valueElement.setAttribute("name",aType.getTypeValue());
					valueElement.setAttribute("type",aType.typeToString());
				}
				
				valueElement.setText((String) vals.get(i));
				keyElement.addContent(valueElement);
			}
			
			dataElement.addContent(keyElement);
		}	
					
		fileElement.addContent (dataElement);
		
		return (saveXML (fileElement));							
	}	
	/**
	 * 
	 */
	protected Boolean saveXML (Element aRoot)
	{
		debug ("saveXML ()");
		
		Document document = new Document();
		
		document.setContent(aRoot);
		
		XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
        String xmlString = outputter.outputString(document);		
		        
        return (saveContents (xmlString));
	}
	/**
	 * 
	 */
	protected Boolean saveXML (Element aRoot,int aSequence)
	{
		debug ("saveXML ()");
		
		Document document = new Document();
		
		document.setContent(aRoot);
		
		XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
        String xmlString = outputter.outputString(document);		
		        
        return (saveContents (xmlString,aSequence));
	}	
	/**
	 * 
	 */
	public HoopBase copy ()
	{
		return (new HoopXMLWriter ());
	}		
}
