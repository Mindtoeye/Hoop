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

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.base.kv.HoopKV;
import edu.cmu.cs.in.base.kv.HoopKVDocument;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.hoop.hoops.base.HoopSaveBase;
import edu.cmu.cs.in.hoop.properties.types.HoopSerializable;
import edu.cmu.cs.in.hoop.properties.types.HoopStringSerializable;
import edu.cmu.cs.in.search.HoopDataSet;

/**
* 
*/
public class HoopDocumentWriter extends HoopSaveBase
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
	private HoopStringSerializable modifiedDate=null;	
	@SuppressWarnings("unused")
	private HoopStringSerializable description=null;
	@SuppressWarnings("unused")
	private HoopStringSerializable text=null;
	@SuppressWarnings("unused")
	private HoopStringSerializable keywords=null;
	@SuppressWarnings("unused")
	private HoopStringSerializable url=null;		
		
	/**
	 *
	 */
    public HoopDocumentWriter () 
    {
		setClassName ("HoopDocumentWriter");
		debug ("HoopDocumentWriter ()");
												
		setHoopDescription ("Write KVs to Document DB");
				
		author=new HoopStringSerializable (this,"author","author");
		title=new HoopStringSerializable (this,"title","title");
		abstr=new HoopStringSerializable (this,"abstr","abstr");
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
	public Boolean runHoop (HoopBase inHoop)
	{		
		debug ("runHoop ()");
		
		ArrayList <HoopKV> inData=inHoop.getData();
		
		if (inData==null)
		{
			this.setErrorString ("Error: no input data to work with");
			return (false);
		}		
		
		debug ("Mapping project path ("+getProjectPath ()+") to db dir ...");
				
		if (HoopLink.dataSet==null)
		{
			HoopLink.dataSet=new HoopDataSet ();
			HoopLink.dataSet.checkDB ();
		}
		else
			HoopLink.dataSet.checkDB ();
		
		for (int t=0;t<inData.size();t++)
		{
			HoopKV aKV=inData.get(t);
			
			HoopKVDocument newDocument=new HoopKVDocument ();
			
			Integer indexTransformer=t;
			
			newDocument.setKey(indexTransformer.toString());
			
			ArrayList <Object>docElements=aKV.getValuesRaw();
			
			for (int i=0;i<docElements.size();i++)
			{
				String aTypeName=inHoop.getKVTypeName (i);
				
				String remapped=mapType (aTypeName);
				
				if (remapped.equalsIgnoreCase("author")==true)
					newDocument.author.setValue((String) docElements.get(i));
				
				if (remapped.equalsIgnoreCase("title")==true)
					newDocument.title.setValue((String) docElements.get(i));
				
				if (remapped.equalsIgnoreCase("abstr")==true)
					newDocument.abstr.setValue((String) docElements.get(i));
				
				if (remapped.equalsIgnoreCase("createDate")==true)
					newDocument.createDate.setValue((String) docElements.get(i));
				
				if (remapped.equalsIgnoreCase("modifiedDate")==true)
					newDocument.modifiedDate.setValue((String) docElements.get(i));

				if (remapped.equalsIgnoreCase("description")==true)
					newDocument.description.setValue((String) docElements.get(i));				

				if (remapped.equalsIgnoreCase("keywords")==true)
					newDocument.keywords.setValue((String) docElements.get(i));

				if (remapped.equalsIgnoreCase("url")==true)
					newDocument.url.setValue((String) docElements.get(i));
				
				if (remapped.equalsIgnoreCase("text")==true)
					newDocument.setValue((String) docElements.get(i));				
			}
			
			newDocument.postProcess();
			
			HoopLink.dataSet.getData().put(indexTransformer.toString(),newDocument);
		}			
				
		return (true);
	}		
	/**
	 * 
	 */
	public HoopBase copy ()
	{
		return (new HoopDocumentWriter ());
	}		
}
