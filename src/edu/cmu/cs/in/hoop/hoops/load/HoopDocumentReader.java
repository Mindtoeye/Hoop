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
import edu.cmu.cs.in.hoop.hoops.base.HoopLoadBase;
import edu.cmu.cs.in.hoop.properties.types.HoopEnumSerializable;
//import edu.cmu.cs.in.hoop.properties.types.HoopIntegerSerializable;
import edu.cmu.cs.in.search.HoopDataSet;

public class HoopDocumentReader extends HoopLoadBase
{
	private static final long serialVersionUID = 3069547626921137451L;
	
	private HoopEnumSerializable selectedField=null;
	
    private Integer bSize=100;
    private Integer bCount=0;
    private Integer loadMax=100;
    private Integer loadIndex=0;	
		
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
	public void reset ()
	{
		debug ("reset ()");
		
		super.reset ();
		
		bSize=-1;
		bCount=0;		
	    loadMax=100;
	    loadIndex=0;			
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
		
		int actualSize=inp.size();
		
		calculateIndexingSizes (actualSize);
				
		if (mode.getValue().equals("LINEAR")==true)
			processLinear ();
		else
			processSample ();
						
		return (true);
	}
	/**
	 * 
	 */
	private void processLinear ()
	{
		debug ("processLinear ()");
		
		StoredMap<Long, HoopKVDocument> inp=HoopLink.dataSet.getData();
		
		bCount=0;
		
		while (checkLoopDone ()==false)		
		//for (int i=0;i<inp.size();i++)
		{
			Integer anInt=loadIndex;
			
			String transformer=anInt.toString();
			
			HoopKVDocument aDocument=inp.get(transformer);
			
			processDocument (aDocument);
			
			loadIndex++; // Update total index
			bCount++; // Update batch count
		}		
		
		if (checkDone ()==false)
			this.setDone(false);
	}
	/**
	 * 
	 */
	private void processSample ()
	{
		debug ("processSample ()");
		
		StoredMap<Long, HoopKVDocument> inp=HoopLink.dataSet.getData();

	}	
	/**
	 * 
	 */
	private void processDocument (HoopKVDocument aDocument)
	{
		debug ("processDocument ()");
		
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
	/**
	 * 
	 */
	private void calculateIndexingSizes (int actualSize)
	{
		debug ("calculateIndexingSizes ("+actualSize+")");
		
		if (bSize==-1)
		{
			debug ("Prepping indexing variables ...");
			
			bSize=Integer.parseInt(batchSize.getPropValue());
			loadMax=Integer.parseInt(queryMax.getPropValue());
				
			if (actualSize<loadMax)
				loadMax=actualSize;
			
			if (actualSize<bSize)
				bSize=actualSize;
					
			if (bSize>loadMax)
				loadMax=bSize;
		}	
		else
			debug ("We're already in a run, no need to prep indexing variables");		
	}
	/**
	 * 
	 */
	private boolean checkLoopDone ()
	{
		if (bCount<bSize)
			return (false);
			
		return (true);
	}
	/**
	 * 
	 */
	private boolean checkDone ()
	{
		if (loadIndex<loadMax)
			return (false);
		
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
