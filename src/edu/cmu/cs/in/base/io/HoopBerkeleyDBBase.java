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

package edu.cmu.cs.in.base.io;

import com.sleepycat.bind.serial.StoredClassCatalog;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.Environment;

import edu.cmu.cs.in.base.HoopRoot;

/**
*
*/
public class HoopBerkeleyDBBase extends HoopRoot
{
    private Database                db=null;
    private DatabaseConfig          dbConfig=null;
    private boolean                 dbDisabled=false;
    private StoredClassCatalog		javaCatalog=null;
    private Environment             env=null;
    private boolean                 create = true;
    private boolean					allowDuplicates=false;
	
	/**
	*
	*/	
	public HoopBerkeleyDBBase ()
	{  
    	setClassName ("HoopBerkeleyDBBase");
    	debug ("HoopBerkeleyDBBase ()");    	    	    	
	}
    /**
     * 
     */
	public Database getDB ()
	{
		return (db);
	}
    /**
     * 
     */
	public void setJavaCatalog(StoredClassCatalog javaCatalog) 
	{
		this.javaCatalog = javaCatalog;
	}
    /**
     * 
     */	
	public StoredClassCatalog getJavaCatalog() 
	{
		return javaCatalog;
	}	
    /**
     * 
     */	
	public void setEnvironment (Environment anEnv)
	{
		env=anEnv;
	}
    /**
     * 
     */
	public Environment getEnvironment ()
	{
		return (env);
	}
    /**
     * 
     */    
	public boolean isDbDisabled() 
	{
		return dbDisabled;
	}
    /**
     * 
     */
	public void setDbDisabled(boolean dbDisabled) 
	{
		this.dbDisabled = dbDisabled;
	}
	/**
	 * 
	 */
	public boolean isAllowDuplicates() 
	{
		return allowDuplicates;
	}
	/**
	 * 
	 */
	public void setAllowDuplicates(boolean allowDuplicates) 
	{
		this.allowDuplicates = allowDuplicates;
	}	
    /** 
     * Opens the database but does not create or assign the map
     */
    public void openDB (String DBName) throws Exception 
    {
    	debug ("openDB ()");
    	
    	if (env==null)
    	{
    		debug ("Error, no Environment available");
    		return;
    	}
    	
        dbConfig=new DatabaseConfig();
        dbConfig.setTransactional(true);
        dbConfig.setSortedDuplicates(allowDuplicates);
        
        if (create) 
        {
            dbConfig.setAllowCreate(true);
        }

        this.db=env.openDatabase (null, DBName, dbConfig);

        if (this.db==null)
        {
        	setDbDisabled(true);
        	return;
        }
                                               
        debug ("Database should be open and available for binding");
    }
    /** 
     * Opens the database but does not create or assign the map
     */
    public void openDB () throws Exception 
    {
    	debug ("openDB ()");
    	
    	if (env==null)
    	{
    		debug ("Error, no Environment available");
    		return;
    	}
    	
        dbConfig=new DatabaseConfig();
        dbConfig.setTransactional(true);
        dbConfig.setSortedDuplicates(allowDuplicates);
        
        if (create) 
        {
            dbConfig.setAllowCreate(true);
        }

        this.db=env.openDatabase (null,this.getInstanceName(), dbConfig);

        if (this.db==null)
        {
        	setDbDisabled(true);
        	return;
        }
                                               
        debug ("Database should be open and available for binding");
    }    
    /**
     * 
     */
    /*
    public void assignMap (Map aMap)
    {
    	debug ("assignMap ()");
    	
        StringBinding keyBinding = new StringBinding();
        StringBinding dataBinding = new StringBinding();        
        
        // create a map view of the database
        this.map=new StoredMap<String, String> (db, keyBinding, dataBinding, true);
        
        if (this.map==null)
        {
        	debug ("Error creating StoredMap from database");
        	setDbDisabled (true);
        }        	    	
    }
    */
    /** 
     * Closes the database. 
     */
    public Boolean close()
    {
    	debug ("close ()");
    	
    	dbDisabled=true;
    	       
        debug("Closing database ...");
        
        if (db != null) 
        {
            db.close();
            db = null;
        }

        return (true);
    }
    /** 
     * @param aKey String
     * @param aValue String
     */
    /*
    public boolean writeKV (String aKey,String aValue)
    {
    	debug ("writeKV (key:"+aKey+", value:"+aValue+")");
    	
    	if (dbDisabled==true)
    	{
    		debug ("Error: database is not open or disabled, aborting");
    		return (false);
    	}	
    	
    	if (map==null)
    	{
    		debug ("No map available to write to, aborting ..");
    		return (false);
    	}
    	
    	map.put (aKey,aValue);
    	    	
    	return (true);
    }
    */
    /*
     * 
     */
    /*
    public void dumpDB ()
    {
    	debug ("dumpDB ()");
    	
    	if (map==null)
    	{
    		debug ("No map available to read from, aborting ..");
    		return;
    	}    	
    	
        debug ("Map size: " + map.size() + " entries");
            
        Iterator<Map.Entry<String, String>> iter=null;
        
		try
		{
			iter=map.entrySet().iterator();
		}
		catch (IndexOutOfBoundsException e)
		{
			debug ("Integrity check failed, IndexOutOfBoundsException");
		}		
                
        debug ("Reading data ...");
                
        while (iter.hasNext()) 
        {
            Map.Entry<String, String> entry = iter.next();
            debug (entry.getKey().toString() + ' ' +  entry.getValue());
        }    	
    }
    */
    /**
     * 
     */
    /*
    public Boolean checkDB ()
    {
    	debug ("checkDB ()");
    	
    	if (map==null)
    	{
    		debug ("No map available to read from, aborting ..");
    		return (false);
    	}    	
    	
        debug ("Checking: " + map.size() + " entries");
            
        Iterator<Map.Entry<String, String>> iter=null;
        
		try
		{
			iter=map.entrySet().iterator();
		}
		catch (IndexOutOfBoundsException e)
		{
			debug ("Integrity check failed, IndexOutOfBoundsException");
			return (false);
		}		
                                
        while (iter.hasNext()) 
        {
            @SuppressWarnings("unused")
			Map.Entry<String, String> entry = iter.next();
            //debug (entry.getKey().toString() + ' ' +  entry.getValue());
            System.out.print(".");
        }
        
        debug (" done");
        
        return (true);
    }            
    */
}
