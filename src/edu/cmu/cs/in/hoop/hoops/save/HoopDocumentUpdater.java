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

import com.sleepycat.collections.StoredMap;

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.base.kv.HoopKV;
import edu.cmu.cs.in.base.kv.HoopKVDocument;
import edu.cmu.cs.in.base.kv.HoopKVInteger;
import edu.cmu.cs.in.base.kv.HoopKVLong;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.hoop.hoops.base.HoopSaveBase;
import edu.cmu.cs.in.hoop.properties.types.HoopEnumSerializable;
import edu.cmu.cs.in.search.HoopDataSet;

/**
* 
*/
public class HoopDocumentUpdater extends HoopSaveBase
{    						
	private HoopEnumSerializable selectedField=null;
	
	/**
	 *
	 */
    public HoopDocumentUpdater () 
    {
		setClassName ("HoopDocumentUpdater");
		debug ("HoopDocumentUpdater ()");
												
		setHoopDescription ("Update Document Attributes from KVs");
				
		selectedField=new HoopEnumSerializable (this,"selectedField","title,author,abstr,text,createDate,modifiedDate,keywords,url,description,tokens");		
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
		
		StoredMap<Long, HoopKVDocument> inp=HoopLink.dataSet.getData();
		
		for (int t=0;t<inData.size();t++)
		{
			HoopKV testKV=inData.get(t);
			
			if ((testKV instanceof HoopKVLong) || (testKV instanceof HoopKVInteger))
			{
				HoopKVLong aKV=(HoopKVLong) inData.get(t);
			
				//String aDocumentKey=aKV.getKeyString();
			
				HoopKVDocument aDocument=inp.get(aKV.getKey ());
			
				if (selectedField.getValue().equalsIgnoreCase("title")==true)
				{
					aDocument.title.setValue((String) aKV.getValue());
				}
			
				if (selectedField.getValue().equalsIgnoreCase("author")==true)
				{
					aDocument.author.setValue((String) aKV.getValue());
				}
			
				if (selectedField.getValue().equalsIgnoreCase("description")==true)
				{
					aDocument.description.setValue((String) aKV.getValue());
				}
			
				if (selectedField.getValue().equalsIgnoreCase("createDate")==true)
				{
					aDocument.createDate.setValue((String) aKV.getValue());
				}
			
				if (selectedField.getValue().equalsIgnoreCase("modifiedDate")==true)
				{
					aDocument.modifiedDate.setValue((String) aKV.getValue());
				}
			
				if (selectedField.getValue().equalsIgnoreCase("text")==true)
				{
					aDocument.bump((String) aKV.getValue(),"transformed");
				}
			
				if (selectedField.getValue().equalsIgnoreCase("abstr")==true)
				{
					aDocument.abstr.setValue((String) aKV.getValue());
				}	
			
				if (selectedField.getValue().equalsIgnoreCase("keywords")==true)
				{
					aDocument.keywords.setValue((String) aKV.getValue());
				}
			
				if (selectedField.getValue().equalsIgnoreCase("url")==true)
				{
					aDocument.url.setValue((String) aKV.getValue());
				}
			
				if (selectedField.getValue().equalsIgnoreCase("tokens")==true)
				{
					// Copy all tokens from the incoming KV to the token field
					// in the document
				
					for (int i=0;i<aKV.getValuesRaw().size();i++)
					{
						//aDocument.tokens.setValue(aKV.getValue(i),i);
					}
				}
			}	
		}			
				
		return (true);
	}		
	/**
	 * 
	 */
	public HoopBase copy ()
	{
		return (new HoopDocumentUpdater ());
	}		
}
