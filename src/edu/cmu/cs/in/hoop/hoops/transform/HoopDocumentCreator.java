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

package edu.cmu.cs.in.hoop.hoops.transform;

import java.util.ArrayList;

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.base.kv.HoopKV;
import edu.cmu.cs.in.base.kv.HoopKVDocument;
import edu.cmu.cs.in.base.kv.HoopKVString;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.hoop.hoops.base.HoopTransformBase;
import edu.cmu.cs.in.hoop.properties.types.HoopSerializable;
import edu.cmu.cs.in.hoop.properties.types.HoopStringSerializable;
import edu.cmu.cs.in.search.HoopDataSet;

/**
* 
*/
public class HoopDocumentCreator extends HoopTransformBase
{    					
	private static final long serialVersionUID = -8088465589033868527L;
	
	@SuppressWarnings("unused")
	private HoopStringSerializable documentID=null;	
	@SuppressWarnings("unused")
	private HoopStringSerializable author=null;
	@SuppressWarnings("unused")
	private HoopStringSerializable authorID=null;	
	@SuppressWarnings("unused")
	private HoopStringSerializable title=null;
	@SuppressWarnings("unused")
	private HoopStringSerializable abstr=null;
	//@SuppressWarnings("unused")
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
	private HoopStringSerializable threadID=null;
	@SuppressWarnings("unused")
	private HoopStringSerializable threadStarter=null;	
	@SuppressWarnings("unused")
	private HoopStringSerializable keywords=null;
	@SuppressWarnings("unused")
	private HoopStringSerializable url=null;		
		
	/**
	 *
	 */
    public HoopDocumentCreator () 
    {
		setClassName ("HoopDocumentCreator");
		debug ("HoopDocumentCreator ()");
												
		setHoopDescription ("Create documents from KVs");
				
		documentID=new HoopStringSerializable (this,"documentID","documentID");		
		author=new HoopStringSerializable (this,"author","author");
		authorID=new HoopStringSerializable (this,"authorID","authorID");
		title=new HoopStringSerializable (this,"title","title");
		abstr=new HoopStringSerializable (this,"abstr","abstr");
		dateFormat=new HoopStringSerializable (this,"dateFormat",HoopLink.dateFormat);
		createDate=new HoopStringSerializable (this,"createDate","date created");
		modifiedDate=new HoopStringSerializable (this,"modifiedDate","date modified");
		description=new HoopStringSerializable (this,"description","description");
		threadID=new HoopStringSerializable (this,"threadID","threadID");
		threadStarter=new HoopStringSerializable (this,"threadStarter","threadStarter");
		text=new HoopStringSerializable (this,"text","text");
		keywords=new HoopStringSerializable (this,"keywords","keywords");
		url=new HoopStringSerializable (this,"url","url");		
    }
	/**
	 * Currently this method can't map one type to more than one value because
	 * it returns the mapped type as soon as it finds it. We'll have to change
	 * this 
	 */
	private String mapType (String aTypeName)
	{
		debug ("mapType ("+aTypeName+")");
		
		ArrayList <HoopSerializable> props=getProperties ();
		
		for (int i=0;i<props.size();i++)
		{
			HoopSerializable aProp=props.get(i);
			
			if (aProp.getValue().equalsIgnoreCase(aTypeName)==true)
			{				
				debug ("Found " + aTypeName + " as: " + aProp.getValue() + " mapped to: " + aProp.getInstanceName());
				
				return (aProp.getInstanceName());
			}
			//else
			//	debug ("Original " + aTypeName + " does not map to: " + aProp.getValue());
		}
		
		debug ("Error: " + aTypeName + " not found");
						
		return (null);
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
			debug (">>>>>>>>>>>>>>>>>>>>>>>>>>>");
			
			HoopKV aKV=inData.get(t);
			
			HoopKVDocument newDocument=new HoopKVDocument ();
			
			// make it the index for now, might be replaced by create date
			// when we run the post process routine
			newDocument.setKey((long) t);
			newDocument.setRank(t);
			newDocument.dateFormat.setValue(this.dateFormat.getPropValue());
			
			ArrayList <Object>docElements=aKV.getValuesRaw();
			
			for (int i=0;i<docElements.size();i++)
			{
				debug ("Getting type of index: " + i);
				
				String aTypeName=inHoop.getKVTypeName (i+1); // Skip the key type
				
				String remapped=mapType (aTypeName);
				
				if (remapped!=null)
				{				
					debug ("Assigning " + remapped);
					
					// We can cast this because we know it's a variable of a HoopKVDocument
					HoopKVString var=(HoopKVString) newDocument.getVariable(remapped);
					
					if (var!=null)
						var.setValue((String) docElements.get(i));
					else
						debug ("Variable " + remapped + " not found in document object");					
				}	
			}
			
			newDocument.postProcess();
						
			// This call will associate a timestamp with a document, but
			// through an alternative table also use a unique ID to link
			// to a document. But only if the documentID field is not blank
			// and contains a long value.
			
			//HoopLink.dataSet.writeKV(newDocument.getKey(),newDocument);
			
			//processThreadData (newDocument);
			
			this.addKV(newDocument);
						
			updateProgressStatus (t,inData.size());
			
			debug ("<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		}			
		
		getVisualizer ().setExecutionInfo (" R: " + inData.size() + " out of " + inData.size());
				
		return (true);
	}			
	/**
	 * 
	 */
	public HoopBase copy ()
	{
		return (new HoopDocumentCreator ());
	}		
}
