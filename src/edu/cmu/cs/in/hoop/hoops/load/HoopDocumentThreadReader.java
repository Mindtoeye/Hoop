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

import java.util.ArrayList;

import com.sleepycat.collections.StoredMap;

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.base.kv.HoopKV;
import edu.cmu.cs.in.base.kv.HoopKVDocument;
import edu.cmu.cs.in.base.kv.HoopKVLong;
import edu.cmu.cs.in.base.kv.HoopKVString;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.hoop.hoops.base.HoopLoadBase;
import edu.cmu.cs.in.hoop.properties.types.HoopEnumSerializable;
import edu.cmu.cs.in.hoop.properties.types.HoopStringSerializable;

public class HoopDocumentThreadReader extends HoopLoadBase
{
	private static final long serialVersionUID = 3069547626921137451L;
		
	private HoopEnumSerializable incomingType=null;
	
	private StoredMap<Long, HoopKVDocument> inp=null;
	
	public HoopStringSerializable minThreadSize=null;    
	
	/**
	 *
	 */ 
	public HoopDocumentThreadReader () 
	{		
		setClassName ("HoopDocumentThreadReader");
		debug ("HoopDocumentThreadReader ()");
		
		setHoopDescription ("Load Documents for a specific thread ID");
				
		//removeInPort ("KV");
		enableProperty ("URI",false);
		
		minThreadSize=new HoopStringSerializable (this,"minThreadSize","10");
		incomingType=new HoopEnumSerializable (this,"incomingType","Documents,Threads");
	}
	/**
	 *
	 */
	public Boolean runHoop (HoopBase inHoop)
	{		
		debug ("runHoop ()");
			
		StoredMap<Long, HoopKVLong> threadMap=HoopLink.dataSet.getThreads();
		
		if (threadMap==null)
		{
			this.setErrorString("Error: no thread dataset available for documents");
			return (false);
		}

		inp=HoopLink.dataSet.getData();
		
		if (inp==null)
		{
			this.setErrorString("Unable to obtain handle to document database");
			return (false);
		}
								
		Integer minSize=Integer.parseInt(minThreadSize.getPropValue());
		
		ArrayList<HoopKV> data=inHoop.getData();
		
		//>--------------------------------------------------------------------------------
		
		if (incomingType.getValue().equalsIgnoreCase("Documents")==true)
		{
			for (int i=0;i<data.size();i++)
			{
				HoopKVDocument aDocument=(HoopKVDocument) data.get(i);
			
				debug ("Locating thread: " + aDocument.threadID.getValue());
						
				Long targetThread=Long.parseLong(aDocument.threadID.getValue());
			
				HoopKVLong aThread=threadMap.get(targetThread);
			
				if (aThread!=null)
				{
					debug ("Retrieving document IDs ...");
			
					ArrayList <Object> aDocIDList=aThread.getValuesRaw ();
			
					if (aDocIDList.size()>minSize)
					{				
						debug ("Check, thread size of " + aDocIDList.size() + " is larger that minimum requested size of: " + minSize);
					
						this.addKV(aDocument); // Make sure we add the original document (the thread starter)
					
						for (int j=0;j<aDocIDList.size();j++)
						{					
							long targetID=Long.parseLong((String) aDocIDList.get(j));
					
							//debug ("Document ID: " + targetID);
						
							HoopKVDocument retrievedDocument=inp.get(targetID);
						
							if (retrievedDocument!=null)
							{
								//debug ("Adding document: " + retrievedDocument.getKeyString());
							
								this.addKV(retrievedDocument);
							}
							else
								debug ("Document " + targetID + " not found in data set");
						}
					}	
				}
			}
		}
		
		//>--------------------------------------------------------------------------------
		
		if (incomingType.getValue().equalsIgnoreCase("Threads")==true)
		{
			for (int i=0;i<data.size();i++)
			{
				HoopKVLong aThread=(HoopKVLong) data.get(i);
							
				debug ("Processing thread: " + aThread.getKeyString());
				
				if (aThread.getValueSize()>minSize)
				{
					ArrayList <Object> aDocIDList=aThread.getValuesRaw ();
					
					if (aDocIDList.size()>0)
					{					
						if (aDocIDList.size()>minSize)
						{
							HoopKVString tester=new HoopKVString (aThread.getKeyString(),"*");
						
							debug ("Check, thread size of " + aDocIDList.size() + " is larger that minimum requested size of: " + minSize);
										
							for (int j=0;j<aDocIDList.size();j++)
							{					
								long targetID=Long.parseLong((String) aDocIDList.get(j));
					
								debug ("Document ID: " + targetID);
						
								HoopKVDocument retrievedDocument=inp.get(targetID);
						
								if (retrievedDocument!=null)
								{
									debug ("Adding document: " + retrievedDocument.getKeyString() + " with thread id: " + retrievedDocument.threadID.getValue());
							
									//this.addKV(retrievedDocument);
								
									StringBuffer formatter=new StringBuffer ();
								
									formatter.append(retrievedDocument.getKeyString());
									formatter.append (":");
									formatter.append (retrievedDocument.threadID.getValue());
								
									tester.add(formatter.toString());
								}
								else
									debug ("Document " + targetID + " not found in data set");
							}
						
							this.addKV(tester);
						}
					}
					else
					{
						this.setErrorString("Error: thread + " + aThread.getKeyString() + " has no entries!");
						return (false);
					}
				}				
			}
		}	
		
		//>--------------------------------------------------------------------------------
					
		return (true);
	}
	/**
	 * 
	 */
	public HoopBase copy ()
	{
		return (new HoopDocumentThreadReader ());
	}
}
