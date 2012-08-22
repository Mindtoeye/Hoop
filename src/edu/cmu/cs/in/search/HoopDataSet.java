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

import com.sleepycat.bind.serial.SerialBinding;
import com.sleepycat.bind.tuple.StringBinding;
import com.sleepycat.collections.StoredMap;

import edu.cmu.cs.in.base.HoopRoot;
import edu.cmu.cs.in.base.io.HoopBerkeleyDB;
import edu.cmu.cs.in.base.io.HoopBerkeleyDocumentDB;
import edu.cmu.cs.in.base.io.HoopFileManager;
import edu.cmu.cs.in.base.kv.HoopKVDocument;
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
    private StoredMap<String,HoopKVDocument> map=null;
	
	/**
	 *
	 */
    public HoopDataSet () 
    {
		setClassName ("HoopDataSet");
		debug ("HoopDataSet ()");		
				
		// For fast and easy access we re-use the file manager from
		// the registry. Makes a huge difference when you're doing
		// loops since it's a costly object to create
		
		if (HoopLink.fManager==null)
			HoopLink.fManager=new HoopFileManager ();
		
		//setDocuments(new ArrayList<HoopKVDocument> ());		
		//checkDB ();
    } 
    /**
     * 
     */
    public StoredMap<String,HoopKVDocument> getData ()
    {
    	return (map);
    }
	/**
	 *
	 */
	public void addDocument (HoopKVDocument anInstance)
	{
		//documents.add(anInstance);
	}
	/**
	 * 
	 */
	public void checkDB ()
	{
		debug ("checkDB ()");
		
		if (driver==null)
		{				
			File checker=new File (HoopLink.project.getBasePath()+"/system/documents");
			
			if (checker.exists()==false)
			{
				if (HoopLink.fManager.createDirectory (HoopLink.project.getBasePath()+"/system/documents")==false)
				{
					debug ("Error creating database directory: "+HoopLink.project.getBasePath()+"/system/documents");
					return;
				}
			}
			
			driver=new HoopBerkeleyDB ();
			driver.setDbDir (HoopLink.project.getBasePath()+"/system/documents");			
			
			documentDriver=new HoopBerkeleyDocumentDB ();
			documentDriver.setInstanceName("documents");
			
			if (driver.startDBService (documentDriver)==false)
			{
				this.setErrorString("Error: unable to start database");
				driver=null; // reset
				return;
			}
			
			StringBinding keyBinding = new StringBinding();
			SerialBinding <HoopKVDocument> dataBinding=new SerialBinding<HoopKVDocument> (documentDriver.getJavaCatalog(),HoopKVDocument.class);
			
	        // create a map view of the database
	        this.map=new StoredMap<String, HoopKVDocument> (documentDriver.getDB(),keyBinding,dataBinding,true);
		        
	        if (this.map==null)
	        {
	        	debug ("Error creating StoredMap from database");
	        	return;
	        }        			 			
		}		
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
