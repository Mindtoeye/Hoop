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

import java.util.Iterator;
import java.util.Map;
import com.sleepycat.collections.StoredMap;

//import com.sleepycat.bind.serial.StoredClassCatalog;
//import com.sleepycat.bind.tuple.LongBinding;
//import com.sleepycat.bind.tuple.StringBinding;
//import com.sleepycat.collections.StoredMap;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.Environment;

import edu.cmu.cs.in.base.HoopRoot;

/**
*
*/
public class HoopBerkeleyDBInstance extends HoopRoot
{
    private Database                db=null;
    private DatabaseConfig          dbConfig=null;
    private StoredMap<Long, String> map=null;
    private Environment             env=null;
    private boolean                 dbDisabled=false;
    private boolean                 create = true;
    private String                  dbDir  = "./db";
	
	/**
	*
	*/	
	public HoopBerkeleyDBInstance ()
	{  
    	setClassName ("HoopBerkeleyDBInstance");
    	debug ("HoopBerkeleyDBInstance ()");    	    	    	
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
	public String getDbDir() 
	{
		return dbDir;
	}
	/**
	 * 
	 */	
	public void setDbDir(String dbDir) 
	{
		this.dbDir = dbDir;
	}		
	/**
	 * 
	 */
	public StoredMap<Long, String> getData ()
	{
		return map;
	}
	/**
	 * 
	 */
	public void assignMap (StoredMap<Long, String> aMap)
	{
		map=aMap;
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
     * 
     */
    /*
    public void assignMap (Map aMap)
    {
    	debug ("assignMap ()");
    	
        LongBinding keyBinding = new LongBinding();
        StringBinding dataBinding = new StringBinding();        
        
        // create a map view of the database
        this.map=new StoredMap<Long, String> (db, keyBinding, dataBinding, true);
        
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
                        
        debug("Resetting map ...");
        
        this.map=null;
        
        debug("Database successfully shutdown");
        
        return (true);
    }
    /** 
     * @param aKey Long
     * @param aValue String
     */
    public boolean writeKV (Long aKey,String aValue)
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
    /**
     * 
     */
    public void dumpDB ()
    {
    	debug ("dumpDB ()");
    	
    	if (map==null)
    	{
    		debug ("No map available to read from, aborting ..");
    		return;
    	}    	
    	
        debug ("Map size: " + map.size() + " entries");
            
        Iterator<Map.Entry<Long, String>> iter=null;
        
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
            Map.Entry<Long, String> entry = iter.next();
            debug (entry.getKey().toString() + ' ' +  entry.getValue());
        }    	
    }
    /**
     * 
     */
    public Boolean checkDB ()
    {
    	debug ("checkDB ()");
    	
    	if (map==null)
    	{
    		debug ("No map available to read from, aborting ..");
    		return (false);
    	}    	
    	
        debug ("Checking: " + map.size() + " entries");
            
        Iterator<Map.Entry<Long, String>> iter=null;
        
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
			Map.Entry<Long, String> entry = iter.next();
            //debug (entry.getKey().toString() + ' ' +  entry.getValue());
            System.out.print(".");
        }
        
        System.out.println (" done");
        
        return (true);
    }            
}
