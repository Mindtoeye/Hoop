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

package edu.cmu.cs.in.search;

import java.io.File;

//import com.sleepycat.bind.serial.SerialBinding;
//import com.sleepycat.bind.tuple.LongBinding;
//import com.sleepycat.bind.tuple.StringBinding;
import com.sleepycat.collections.StoredMap;

import edu.cmu.cs.in.base.HoopRoot;
//import edu.cmu.cs.in.base.HoopStringTools;
//import edu.cmu.cs.in.base.io.HoopBerkeleyAltDocumentDB;
import edu.cmu.cs.in.base.io.HoopBerkeleyDB;
import edu.cmu.cs.in.base.io.HoopBerkeleyDocumentDB;
import edu.cmu.cs.in.base.io.HoopBerkeleyThreadDB;
import edu.cmu.cs.in.base.kv.HoopKVDocument;
import edu.cmu.cs.in.base.kv.HoopKVLong;
import edu.cmu.cs.in.base.HoopLink;

/** 
 * There's not much to this class, it holds a list of documents
 * (HoopKVDocument) so that we can easily retrieve them
 *
 */
public class HoopDataSet extends HoopRoot
{	
	private HoopBerkeleyDB driver=null;
	private HoopBerkeleyDocumentDB documentDriver=null;
	private HoopBerkeleyThreadDB threadDriver=null;
	//private HoopBerkeleyAltDocumentDB altDocumentDriver=null;
		
    //private StoredMap<Long,HoopKVDocument> map=null;
	
	/**
	 *
	 */
    public HoopDataSet () 
    {
		setClassName ("HoopDataSet");
		debug ("HoopDataSet ()");		
    } 
    /**
     * 
     */
    public HoopKVDocument getDocumentFromDate (Long dateID)
    {
    	return (documentDriver.getData().get(dateID));    	
    }
    /**
     * 
     */
    public StoredMap<Long,HoopKVDocument> getData ()
    {
    	if (documentDriver!=null)
    		return (documentDriver.getData());
    	
    	return (null);
    }
    /**
     * 
     */
    public StoredMap<Long,HoopKVLong> getThreads ()
    {
    	return (threadDriver.getData());
    }  
    /**
    * @param aKey String
    * @param aValue ArrayList<Object>
    */
    public boolean writeKV (Long aKey,HoopKVDocument aValue)
    {
    	if (documentDriver==null)
    		return (false);
    	
    	Boolean result=documentDriver.writeKV(aKey, aValue);
    	
    	if (result==false)
    		return (false);
    	
    	/*
    	if (aValue.documentID.getValue().isEmpty()==false)
    	{    	
    		debug ("Processing document id: " + aValue.documentID.getValue());
    		
    		if (HoopStringTools.isLong(aValue.documentID.getValue())==true)
    		{
    			Long testID=Long.parseLong(aValue.documentID.getValue());
    				
    			result=altDocumentDriver.writeKV(testID, aValue);
    		}	
    	}
    	else
    		debug ("Info: document has no explicit document ID");
    	*/	
    	
    	return (true);
    }
	/**
	 *
	 */
    /*
	public void addDocument (HoopKVDocument anInstance)
	{
		if (map==null)
		{
			return;
		}

		Long aSize=(long) map.size();
		
		map.put(aSize,anInstance);
	}
	*/
    /**
     * 
     */
    public void unmount ()
    {
    	debug ("unmount ()");
    
    	if (driver!=null)
    	{
			try 
			{
				driver.close();
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
    	}	
    }
	/**
	 * 
	 */
	public void checkDB ()
	{
		debug ("checkDB ()");
		
		if (HoopLink.project==null)
		{
			debug ("No project yet, aborting db boot");
			return;
		}
		
		if (HoopLink.project.getVirginFile()==true)
		{
			debug ("Project hasn't been saved yet, aborting db boot");
			return;
		}		
		
		if (driver==null)
		{				
			debug ("Driver is null, starting db boot process ...");
			
			String dbPath=HoopLink.project.getBasePath()+"/system/documents";
			
			File checker=new File (dbPath);
			
			if (checker.exists()==false)
			{
				if (HoopLink.fManager.createDirectory (dbPath)==false)
				{
					debug ("Error creating database directory: " + dbPath);
					return;
				}
			}
			else
				debug ("Document database directory exists, excellent");
			
			driver=new HoopBerkeleyDB ();
			driver.setDbDir (dbPath);			
			
			documentDriver=new HoopBerkeleyDocumentDB ();
			documentDriver.setInstanceName("documents");
			
			if (driver.startDBService (documentDriver)==false)
			{
				this.setErrorString("Error: unable to start database");
				driver=null; // reset
				return;
			}
			
			//>---------------------------------------------------------
			
			/*
			LongBinding keyBinding = new LongBinding();
			SerialBinding <HoopKVDocument> dataBinding=new SerialBinding<HoopKVDocument> (documentDriver.getJavaCatalog(),HoopKVDocument.class);
			
	        this.map=new StoredMap<Long, HoopKVDocument> (documentDriver.getDB(),keyBinding,dataBinding,true);
		        
	        if (this.map==null)
	        {
	        	debug ("Error creating StoredMap from database");
	        	return;
	        }
	        */
			
			documentDriver.bind();
	        
			//>---------------------------------------------------------
	        
	        threadDriver=new HoopBerkeleyThreadDB ();
	        threadDriver.setInstanceName("documenthreads");
	        driver.accessDB(threadDriver);
	        threadDriver.bind();
	        
	        /*
	        altDocumentDriver=new HoopBerkeleyAltDocumentDB ();
	        altDocumentDriver.setInstanceName("altdocumenttable");
	        driver.accessDB(altDocumentDriver);
	        altDocumentDriver.bind();
	        */	        
		}
		else
			debug ("Driver exists, assuming that the database has been initialized");
	}
	/**
	 *
	 */
	/*
    public Boolean loadDocumentsAsFiles (String aPath)
    {
    	debug ("loadDocumentsAsFiles ("+aPath+")");
    	
    	ArrayList<String> files=HoopLink.fManager.listFiles (aPath);
    	
    	if (files.size()==0)
    	{
    		debug ("Error: no files found");
    		return (false);
    	}
    	
    	for (int i=0;i<files.size();i++)
    	{
    		String entry=(String) files.get(i);
    		
    		if ((entry.equals(".")==true) || (entry.equals(".")==true))
    		{
    			// Skipping the usual suspects
    		}
    		else
    		{
    			HoopKVDocument loader=new HoopKVDocument ();
    			//loader.setInstanceName(entry);
    			loader.setKey(entry);
    			
    			// Keep in mind here that we load an entire document all at
    			// once. This is a remnant from how the hadoop code worked,
    			// however the position lists are retrieved much faster
    			
    			loader.loadDocument (aPath+"/"+entry);
    			documents.add(loader);
    		}
    	}
    	
    	return (true);
    }
    */
	/**
	 * Nice to have debug method to see what was loaded
	 */
    /*
    public void printStats ()
    {
    	debug ("printStats ()");
    	
    	for (int i=0;i<documents.size();i++)
    	{
    		HoopKVDocument test=documents.get(i);
    		debug (test.getKey()+" : " + test.getTokens().size());
    	}
    }
    */
}
