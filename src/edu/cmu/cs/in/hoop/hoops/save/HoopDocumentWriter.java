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
import java.util.Iterator;

import com.sleepycat.collections.StoredMap;

import edu.cmu.cs.in.base.HoopDataType;
import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.base.HoopStringTools;
import edu.cmu.cs.in.base.kv.HoopKV;
import edu.cmu.cs.in.base.kv.HoopKVDocument;
import edu.cmu.cs.in.base.kv.HoopKVLong;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.hoop.properties.types.HoopBooleanSerializable;
import edu.cmu.cs.in.search.HoopDataSet;

/**
* 
*/
public class HoopDocumentWriter extends HoopDocumentIndexer
{    				
	private static final long serialVersionUID = -1691608095189030052L;
	
	private int checkCounter=0;
	private int errorCounter=0;
	
	private StoredMap<Long,HoopKVLong> threadData=null;
		
	private HoopBooleanSerializable indexDocuments=null;
	
	/**
	 *
	 */
    public HoopDocumentWriter ()
    {
		setClassName ("HoopDocumentWriter");
		debug ("HoopDocumentWriter ()");
												
		setHoopDescription ("Write Documents to Document DB");
		
		indexDocuments=new HoopBooleanSerializable (this,"indexDocuments",false);
    }
	/**
	 * 
	 */
    public void reset ()
    {
    	debug ("reset ()");
    	
    	super.reset();
    	
    	checkCounter=0;
    	errorCounter=0;    	
    	
    	threadData=null;
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
			
			//initSearch ();
		}
		else
			HoopLink.dataSet.checkDB ();
		
		HoopKVLong threadCache=null;
		
		checkCounter=0;
		
		for (int t=0;t<inData.size();t++)
		{
			HoopKV aKV=inData.get(t);
									
			if (aKV instanceof HoopKVDocument)
			{			
				HoopKVDocument newDocument=(HoopKVDocument) aKV;
				
				// This call will associate a timestamp with a document, but
				// through an alternative table also use a unique ID to link
				// to a document. But only if the documentID field is not blank
				// and contains a long value.
			
				HoopLink.dataSet.writeKV (newDocument.getKey(),newDocument);
				
				if (indexDocuments.getPropValue()==true)
					addToSearchIndex (newDocument);
				
				threadCache=processThreadData (newDocument,threadCache);
				
				checkCounter++;
			}			
			
			updateProgressStatus (t,inData.size());
		}			
		
		debug ("Check: processed " + checkCounter + " documents, with " + errorCounter + " errors");
		
		if (getVisualizer ()!=null)
		{
			getVisualizer ().setExecutionInfo (" R: " + checkCounter + ", Err: " + errorCounter);
		}
				
		return (true);
	}	
	/**
	 * 
	 */
	private HoopKVLong processThreadData (HoopKVDocument aDocument,HoopKVLong threadCache)
	{
		if (threadCache!=null)
			debug ("processThreadData ("+aDocument.documentID.getValue()+","+aDocument.getKeyString()+","+threadCache.getKey()+")");
		else
			debug ("processThreadData ("+aDocument.documentID.getValue()+","+aDocument.getKeyString()+")");
		
		if (HoopLink.dataSet==null)
		{
			debug ("No dataset, can't process thread data");
			return (null);
		}
		
		if (threadData==null)
		{
			threadData=HoopLink.dataSet.getThreads();
		}
		
		if (threadData==null)
		{
			debug ("Error: no thread database available, can't process thread data");
			return (null);
		}
				
		HoopKVLong testThread=null;
		
		Long newThreadID=(long) 0;
		
		if (aDocument.threadID.getValue().isEmpty()==false)
		{						
			debug ("Procedding thread ID: " + aDocument.threadID.getValue() + " for document: " + aDocument.documentID.getValue());
			
			if (HoopStringTools.isLong (aDocument.threadID.getValue())==true)
			{
				debug ("Thread ID is of type Long");

				newThreadID=Long.parseLong(aDocument.threadID.getValue());

				if (threadCache!=null)
				{
					debug ("We've got a potential thread cache hit, checking ("+threadCache.getKey()+","+newThreadID+")...");
															
					if (threadCache.getKey().equals(newThreadID)==true)
					{
						debug ("Thread cache hit: " + newThreadID);
																		
						threadCache.add(aDocument.getKeyString());
						
						showThread (threadCache);
												
						return (threadCache);
					}
					
					// We're switching over so we have to make sure that we write out all the
					// updates we've put in the cached object so far
					
					threadData.put(threadCache.getKey(),threadCache);					
				}

				testThread=threadData.get(newThreadID);

				//debug ("No cache hit, proceed as normal with thread id: " + testThread.getValue());

				if (testThread==null)
				{
					debug ("No thread entry found for ID:" + newThreadID + " for document "+aDocument.documentID.getValue()+", creating ...");
										
					testThread=new HoopKVLong ();
					testThread.setKey(newThreadID);
					testThread.setValue(aDocument.getKeyString());
					//threadData.put(newThreadID,testThread);
					
					//showThread (testThread);															
				}
				else
				{
					debug ("Thread ID is already in our database, updating ...");
					
					showThread (testThread);
					
					if (aDocument.threadStarter.getValue().isEmpty()==false)
					{
						if (HoopDataType.booleanStringValue(aDocument.threadStarter.getValue())==true)
						{
							debug ("Potential conflict, thread ID already exists but document indicates it's a thread starter");

							errorCounter++;
						}
						else
						{
							debug ("Adding thread ID ("+newThreadID+") with document: "+aDocument.createDate.getValue()+" ...");

							testThread.add(aDocument.getKeyString());

							//threadData.put(newThreadID,testThread);													
						}
					}
					else
					{
						debug ("Adding thread ID ("+newThreadID+") with document: "+aDocument.createDate.getValue()+" ...");

						testThread.add(aDocument.getKeyString());

						//threadData.put(newThreadID,testThread);						
					}
				}								
			}
		}		
		else
			debug ("This document does not contain thread data");

		threadData.put(newThreadID,testThread);		
		
		showThread (testThread);
		
		showThreadDB ();
		
		return (testThread);
	}
	/**
	 * 
	 */
	/*
	private HoopKVLong processThreadData (HoopKVDocument aDocument,HoopKVLong threadCache)
	{
		if (threadCache!=null)
			debug ("processThreadData ("+aDocument.documentID.getValue()+","+aDocument.getKeyString()+","+threadCache.getKey()+")");
		else
			debug ("processThreadData ("+aDocument.documentID.getValue()+","+aDocument.getKeyString()+")");
		
		if (HoopLink.dataSet==null)
		{
			debug ("No dataset, can't process thread data");
			return (null);
		}
		
		if (threadData==null)
		{
			threadData=HoopLink.dataSet.getThreads();
		}
		
		if (threadData==null)
		{
			debug ("Error: no thread database available, can't process thread data");
			return (null);
		}
				
		if (aDocument.threadID.getValue().isEmpty()==false)
		{						
			debug ("Procedding thread ID: " + aDocument.threadID.getValue() + " for document: " + aDocument.documentID.getValue());
			
			if (HoopStringTools.isLong (aDocument.threadID.getValue())==true)
			{
				debug ("Thread ID is of type Long");
				
				Long newThreadID=Long.parseLong(aDocument.threadID.getValue());
								
				HoopKVLong testThread=null;
				
				if (threadCache!=null)
				{
					debug ("We've got a potential thread cache hit, checking ("+threadCache.getKey()+","+newThreadID+")...");
															
					if (threadCache.getKey().equals(newThreadID)==true)
					{
						debug ("Thread cache hit: " + newThreadID);
																		
						threadCache.add(aDocument.getKeyString());
												
						return (threadCache);
					}
					
					// We're switching over so we have to make sure that we write out all the
					// updates we've put in the cached object
					
					threadData.put(threadCache.getKey(),threadCache);					
				}

				testThread=threadData.get(newThreadID);

				//debug ("No cache hit, proceed as normal with thread id: " + testThread.getValue());

				if (testThread==null)
				{
					debug ("No thread entry found for ID:" + newThreadID + " for document "+aDocument.documentID.getValue()+", creating ...");
										
					testThread=new HoopKVLong ();
					testThread.setKey(newThreadID);
					testThread.setValue(aDocument.getKeyString());
					threadData.put(newThreadID,testThread);
															
					return (testThread);
					
					//showThreadDB ();
				}
				else
				{
					debug ("Thread ID is already in our database, updating ...");
					
					debug ("Check >");
					showThread (testThread);
					debug ("Check <");
					
					if (aDocument.threadStarter.getValue().isEmpty()==false)
					{
						if (HoopDataType.booleanStringValue(aDocument.threadStarter.getValue())==true)
						{
							debug ("Potential conflict, thread ID already exists but document indicates it's a thread starter");
							
							errorCounter++;
						}
						else
						{
							debug ("Bumping thread ID ("+newThreadID+") with document: "+aDocument.createDate.getValue()+" ...");
														
							testThread.add(aDocument.getKeyString());
							
							threadData.put(newThreadID,testThread);
							
							//showThreadDB ();
						}
					}
					else
					{
						debug ("Bumping thread ID ("+newThreadID+") with document: "+aDocument.createDate.getValue()+" ...");
						
						testThread.add(aDocument.getKeyString());
						
						threadData.put(newThreadID,testThread);
						
						return (testThread);
					}
				}								
			}
		}		
		else
			debug ("This document does not contain thread data");
		
		return (null);
	}
	*/	
	/**
	 * 
	 */
	//@SuppressWarnings("unused")
	private void showThreadDB ()
	{
		debug ("showThreadDB ()");

		StoredMap<Long,HoopKVLong> threadData=HoopLink.dataSet.getThreads();
		
		if (threadData==null)
		{
			debug ("Error: no thread data available");
			return;
		}		
		
		StoredMap<Long,HoopKVDocument> map=HoopLink.dataSet.getData();
		
		if (map==null)
		{
			debug ("Error: no document data available");
			return;
		}		
		   	
    	Integer totalShown=100;
    	
    	int dSize=threadData.size();
    	
    	if (dSize<totalShown)
    		totalShown=dSize;
    	
    	//debug ("Thread data size: " + dSize + " adjusted: " + totalShown);
    	    	
		Iterator<HoopKVLong> iterator = threadData.values().iterator();

		int index=0;
		int count=100;
		
		if (threadData.size()<100)
			count=threadData.size ();
		
		//debug ("Thread data size: " + dSize + " adjusted: " + count);
												
		while ((iterator.hasNext()) && (index<count)) 
		{
			HoopKVLong aThread=(HoopKVLong) iterator.next();
								
			showThread (aThread);
    			
			index++;
		}	    			
	}
	/**
	 * 
	 */
	private void showThread (HoopKVLong aThread)
	{
		debug ("showThread ("+aThread.getKeyString()+","+aThread.getValueSize()+")");
		
		ArrayList <Object> docIDs=aThread.getValuesRaw();
		
		StringBuffer docList=new StringBuffer ();
		
		for (int i=0;i<docIDs.size();i++)
		{
			if (i>0)
				docList.append (", ");
			
			docList.append(docIDs.get (i));
		}
		
		debug ("Doc list: " + docList.toString());
	}
	/**
	 * 
	 */
	public HoopBase copy ()
	{
		return (new HoopDocumentWriter ());
	}		
}
