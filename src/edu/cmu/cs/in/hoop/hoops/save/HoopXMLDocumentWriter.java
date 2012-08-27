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
//import edu.cmu.cs.in.base.HoopHTML2Text;
import edu.cmu.cs.in.base.kv.HoopKV;
import edu.cmu.cs.in.base.kv.HoopKVDocument;
import edu.cmu.cs.in.base.kv.HoopKVString;
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
	private HoopStringSerializable dateFormat=null;	
	@SuppressWarnings("unused")
	private HoopStringSerializable createDate=null;
	@SuppressWarnings("unused")
	private HoopStringSerializable modifiedDate=null;	
	@SuppressWarnings("unused")
	private HoopStringSerializable description=null;
	@SuppressWarnings("unused")
	private HoopStringSerializable text=null;
	@SuppressWarnings("unused")
	private HoopStringSerializable keywords=null;
	@SuppressWarnings("unused")
	private HoopStringSerializable url=null;	
	
	private int abstrSize=50; // 50 characters for now, CHANGE THIS TO WHOLE TERMS!
	
	/**
	 *
	 */
    public HoopXMLDocumentWriter () 
    {
		setClassName ("HoopXMLDocumentWriter");
		debug ("HoopXMLDocumentWriter ()");
												
		setHoopDescription ("Write a XML Document(s)");
		
		author=new HoopStringSerializable (this,"author","author");
		title=new HoopStringSerializable (this,"title","title");
		abstr=new HoopStringSerializable (this,"abstr","abstr");
		dateFormat=new HoopStringSerializable (this,"dateFormat","yyyy-MM-dd HH:mm:ss.S");
		createDate=new HoopStringSerializable (this,"createDate","date created");
		modifiedDate=new HoopStringSerializable (this,"modifiedDate","date modified");
		description=new HoopStringSerializable (this,"description","description");
		text=new HoopStringSerializable (this,"text","text");
		keywords=new HoopStringSerializable (this,"keywords","keywords");
		url=new HoopStringSerializable (this,"url","url");
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
		
			if (getIncludeIndex ()==true)
				fileElement.addContent(typesToXML (inHoop));
										
			for (int t=0;t<inData.size();t++)
			{
				HoopKV aKV=inData.get(t);
												
				HoopKVString newKV=storeDocument (inHoop,aKV);
				
				HoopKVDocument newDocument=KVToDocument (newKV);
				
				//Integer indexTransformer=t;				
				//newDocument.setKey(indexTransformer.toString());
				
				newDocument.setKey((long) t);
				
				newDocument.postProcess();
				
				fileElement.addContent(newDocument.toXML());
			}
			
			return (saveXML (fileElement));	
		}	
		else
		{							
			for (int t=0;t<inData.size();t++)
			{
				HoopKV aKV=inData.get(t);
					
				Element fileElement=new Element ("hoopoutput");
					
				if (getIncludeIndex ()==true)
					fileElement.addContent(typesToXML (inHoop));
				
				HoopKVString newKV=storeDocument (inHoop,aKV);
				
				HoopKVDocument newDocument=KVToDocument (newKV);
				
				//Integer indexTransformer=t;
				//newDocument.setKey(indexTransformer.toString());
				
				newDocument.setKey((long) t);
				newDocument.setRank(t);
				
				newDocument.postProcess();
				
				fileElement.addContent(newDocument.toXML());
								
				Boolean result=saveXML (fileElement,t);
					
				if (result==false)
					return (false);
			}
		}
		
		return (true);	
	}	
	/**
	 * Map the incoming KV types to document types and return
	 * the new KV object
	 */
	private HoopKVString storeDocument (HoopBase inHoop,HoopKV aKV)
	{
		debug ("storeDocument ()");
		
		HoopKVString newKV=new HoopKVString ();
				
		ArrayList <Object> vals=aKV.getValuesRaw ();
		
		for (int j=0;j<vals.size();j++)
		{
			String aTypeName=inHoop.getKVTypeName (j);
						
			String remapped=mapType (aTypeName);
			
			this.setKVType(j,HoopDataType.STRING,remapped);
			
			newKV.setValue(aKV.getValueAsString(j),j);			
		}				
				
		return (newKV);
	}
	/**
	 * 
	 */
	private HoopKVDocument KVToDocument (HoopKV aKV)
	{
		debug ("KVToDocument ()");
		
		HoopKVDocument newDocument=new HoopKVDocument ();
		
		ArrayList <Object> vals=aKV.getValuesRaw ();
		
		for (int j=0;j<vals.size();j++)
		{
			String aTypeName=this.getKVTypeName (j);
						
			if (aTypeName.equalsIgnoreCase("author")==true)
				newDocument.author.setValue((String) vals.get(j));
			
			if (aTypeName.equalsIgnoreCase("title")==true)
				newDocument.title.setValue((String) vals.get(j));			
			
			if (aTypeName.equalsIgnoreCase("abstract")==true)
				newDocument.abstr.setValue((String) vals.get(j));
			
			newDocument.dateFormat.setValue(this.dateFormat.getPropValue());
			
			if (aTypeName.equalsIgnoreCase("createDate")==true)
				newDocument.createDate.setValue((String) vals.get(j));
			
			if (aTypeName.equalsIgnoreCase("modifiedDate")==true)
				newDocument.modifiedDate.setValue((String) vals.get(j));
			
			if (aTypeName.equalsIgnoreCase("description")==true)
				newDocument.description.setValue((String) vals.get(j));
						
			if (aTypeName.equalsIgnoreCase("keywords")==true)
				newDocument.keywords.setValue((String) vals.get(j));
			
			if (aTypeName.equalsIgnoreCase("url")==true)
				newDocument.url.setValue((String) vals.get(j));
			
			if (aTypeName.equalsIgnoreCase("text")==true)
				newDocument.setValue((String) vals.get(j));			
		}						
		
		return (newDocument);
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
