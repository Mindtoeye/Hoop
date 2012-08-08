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

/**
* 
*/
public class HoopXMLDocumentWriter extends HoopXMLWriter
{    			
	/**
	 *
	 */
    public HoopXMLDocumentWriter () 
    {
		setClassName ("HoopXMLDocumentWriter");
		debug ("HoopXMLDocumentWriter ()");
												
		setHoopDescription ("Write a Hoop Document as an XML file");
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

		int authorIndex=getPropertyIndex ("Author");
		int abstractIndex=getPropertyIndex ("Abstract");
		int descIndex=getPropertyIndex ("Description");
		if (descIndex==-1)
			descIndex=getPropertyIndex ("Desc");
		int contentIndex=getPropertyIndex ("Content");
		if (contentIndex==-1)			
			contentIndex=getPropertyIndex ("Text");
						
		if (inData!=null)
		{			
			for (int t=0;t<inData.size();t++)
			{
				HoopKV aKV=inData.get(t);
				
				if (authorIndex!=-1)
				{
					String author=aKV.getValueAsString(authorIndex);
					
					Element authorElement=new Element ("author");
					authorElement.setText(author);
					dataElement.addContent(authorElement);
				}
				
				if (abstractIndex!=-1)
				{
					String abstr=aKV.getValueAsString(abstractIndex); 
					
					Element abstractElement=new Element ("author");
					abstractElement.setText(abstr);
					dataElement.addContent(abstractElement);					
				}
				
				if (descIndex!=-1)
				{
					String desc=aKV.getValueAsString(descIndex); 
					
					Element descElement=new Element ("description");
					descElement.setText(desc);
					dataElement.addContent(descElement);					
				}				
				
				if (contentIndex!=-1)
				{
					String cont=aKV.getValueAsString(contentIndex);
					
					Element textElement=new Element ("text");
					textElement.setText(cont);
					dataElement.addContent(textElement);					
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
