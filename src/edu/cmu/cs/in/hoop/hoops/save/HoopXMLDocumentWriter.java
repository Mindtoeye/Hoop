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

import edu.cmu.cs.in.base.HoopDataType;
import edu.cmu.cs.in.base.kv.HoopKV;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.hoop.properties.types.HoopStringSerializable;

/**
* 
*/
public class HoopXMLDocumentWriter extends HoopXMLWriter
{    			
	private HoopStringSerializable authorTag=null;
	private HoopStringSerializable titleTag=null;
	private HoopStringSerializable abstractTag=null;
	private HoopStringSerializable createDateTag=null;
	private HoopStringSerializable descriptionTag=null;
	private HoopStringSerializable textTag=null;
	private HoopStringSerializable keywordsTag=null;
	
	/**
	 *
	 */
    public HoopXMLDocumentWriter () 
    {
		setClassName ("HoopXMLDocumentWriter");
		debug ("HoopXMLDocumentWriter ()");
												
		setHoopDescription ("Write a Hoop Document as an XML file");
		
		authorTag=new HoopStringSerializable (this,"authorTag","author");
		titleTag=new HoopStringSerializable (this,"titleTag","title");
		abstractTag=new HoopStringSerializable (this,"abstractTag","abstract");
		createDateTag=new HoopStringSerializable (this,"createTag","date created");
		descriptionTag=new HoopStringSerializable (this,"descriptionTag","description");
		textTag=new HoopStringSerializable (this,"textTag","text");
		keywordsTag=new HoopStringSerializable (this,"keywordsTag","keywords");		
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
		
		Element fileElement=new Element ("hoopoutput");
		
		fileElement.addContent(typesToXML (inHoop));
		
		Element dataElement=new Element ("content");
		
		fileElement.addContent(dataElement);
						
		if (inData!=null)
		{			
			for (int t=0;t<inData.size();t++)
			{
				HoopKV aKV=inData.get(t);
												
				Element keyElement=new Element ("key");
				keyElement.setText(aKV.getKeyString());
				dataElement.addContent(keyElement);
				
				ArrayList <Object> vals=aKV.getValuesRaw ();
				
				for (int j=0;j<vals.size();j++)
				{
					String aTypeName=this.getKVTypeName (j);
					
					HoopStringSerializable mapper=(HoopStringSerializable) this.getProperty(aTypeName);
					
					if (mapper!=null)
					{
						Element documentElement=new Element (mapper.getInstanceName());
						documentElement.setText(aKV.getValueAsString(j));
						dataElement.addContent(documentElement);
					}
					else
						debug ("Document property " + aTypeName + " could not be mapped");
				}				
			}
		}	
		
		return (saveXML (fileElement));	
	}	
	/**
	 * 
	 */
	public HoopBase copy ()
	{
		return (new HoopXMLDocumentWriter ());
	}		
}
