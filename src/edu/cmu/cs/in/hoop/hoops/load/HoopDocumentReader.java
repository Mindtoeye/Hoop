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

package edu.cmu.cs.in.hoop.hoops.load;

import com.sleepycat.collections.StoredMap;

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.base.kv.HoopKVDocument;
import edu.cmu.cs.in.base.kv.HoopKVString;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.hoop.hoops.base.HoopFileLoadBase;
import edu.cmu.cs.in.hoop.properties.types.HoopEnumSerializable;
//import edu.cmu.cs.in.hoop.properties.types.HoopIntegerSerializable;
import edu.cmu.cs.in.search.HoopDataSet;

public class HoopDocumentReader extends HoopFileLoadBase
{
	private static final long serialVersionUID = 3069547626921137451L;
	
	private HoopEnumSerializable selectedField=null;
		
	/**
	 *
	 */ 
	public HoopDocumentReader () 
	{		
		setClassName ("HoopDocumentReader");
		debug ("HoopDocumentReader ()");
		
		setHoopDescription ("Load KVs from Document DB");
				
		removeInPort ("KV");
		enableProperty ("URI",false);
		
		selectedField=new HoopEnumSerializable (this,"selectedField","title,author,abstr,text,createDate,modifiedDate,keywords,url,description,tokens");
	}
	/**
	 *
	 */
	public Boolean runHoop (HoopBase inHoop)
	{		
		debug ("runHoop ()");
					
		debug ("Mapping project path ("+getProjectPath ()+") to db dir ...");
		
		if (HoopLink.dataSet==null)
		{
			HoopLink.dataSet=new HoopDataSet ();
			HoopLink.dataSet.checkDB ();
		}
		else
			HoopLink.dataSet.checkDB ();
		
		StoredMap<Long, HoopKVDocument> inp=HoopLink.dataSet.getData();
		
		for (int i=0;i<inp.size();i++)
		{
			Integer anInt=i;
			
			String transformer=anInt.toString();
			
			HoopKVDocument aDocument=inp.get(transformer);
			
			if (selectedField.getValue().equalsIgnoreCase("title")==true)
			{
				this.addKV(new HoopKVString (aDocument.getKeyString(),aDocument.title.getValue()));
			}
			
			if (selectedField.getValue().equalsIgnoreCase("author")==true)
			{
				this.addKV(new HoopKVString (aDocument.getKeyString(),aDocument.author.getValue()));
			}
			
			if (selectedField.getValue().equalsIgnoreCase("description")==true)
			{
				this.addKV(new HoopKVString (aDocument.getKeyString(),aDocument.description.getValue()));
			}
			
			if (selectedField.getValue().equalsIgnoreCase("createDate")==true)
			{
				this.addKV(new HoopKVString (aDocument.getKeyString(),aDocument.createDate.getValue()));
			}
			
			if (selectedField.getValue().equalsIgnoreCase("modifiedDate")==true)
			{
				this.addKV(new HoopKVString (aDocument.getKeyString(),aDocument.modifiedDate.getValue()));
			}
			
			if (selectedField.getValue().equalsIgnoreCase("text")==true)
			{
				HoopKVString textContent=new HoopKVString (aDocument.getKeyString(),aDocument.getValue());
				
				// Copy all other versions of the text into the new document (if available)
				
				for (int j=0;j<aDocument.getValuesRaw().size();j++)
				{
					if (j>0)
					{
						textContent.setValue(aDocument.getValue(j),j);
					}
				}
				
				this.addKV(textContent);
			}
			
			if (selectedField.getValue().equalsIgnoreCase("abstr")==true)
			{
				HoopKVString textContent=new HoopKVString (aDocument.abstr.getKeyString(),aDocument.getValue());
				
				// Copy all other versions of the abstract into the new document (if available)
				
				for (int j=0;j<aDocument.abstr.getValuesRaw().size();j++)
				{
					if (j>0)
					{
						textContent.setValue(aDocument.abstr.getValue(j),j);
					}
				}
				
				this.addKV(textContent);
			}	
			
			if (selectedField.getValue().equalsIgnoreCase("keywords")==true)
			{
				this.addKV(new HoopKVString (aDocument.getKeyString(),aDocument.keywords.getValue()));
			}
			
			if (selectedField.getValue().equalsIgnoreCase("url")==true)
			{
				this.addKV(new HoopKVString (aDocument.getKeyString(),aDocument.url.getValue()));
			}			
		}
						
		return (true);
	}	
	/**
	 * 
	 */
	public HoopBase copy ()
	{
		return (new HoopDocumentReader ());
	}	
}
