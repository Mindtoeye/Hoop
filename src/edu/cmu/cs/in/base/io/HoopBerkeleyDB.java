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

import java.io.File;
import java.util.ArrayList;

import com.sleepycat.bind.serial.StoredClassCatalog;
import com.sleepycat.collections.TransactionRunner;
import com.sleepycat.collections.TransactionWorker;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;

import edu.cmu.cs.in.base.HoopRoot;

/**
*
*/
public class HoopBerkeleyDB extends HoopRoot implements TransactionWorker
{
    private EnvironmentConfig envConfig=null;	
    private Environment       env=null;

    private boolean dbDisabled=false;
    private boolean create = true;
    private String dbDir   = "./db";
    public boolean dumpDatabase=false;
    private TransactionRunner runner=null;
    private HoopBerkeleyDBInstance mainDB=null;
    private HoopBerkeleyDBInstance catalogDb=null;
    private StoredClassCatalog javaCatalog=null;
    private ArrayList <HoopBerkeleyDBInstance> databases=null;
	    
	/**
	*
	*/	
	public HoopBerkeleyDB ()
	{  
    	setClassName ("HoopBerkeleyDB");
    	debug ("HoopBerkeleyDB ()");
    	
    	databases=new ArrayList<HoopBerkeleyDBInstance> ();
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
	public long getCacheMisses ()
	{
		
		long cacheMisses = env.getStats(null).getNCacheMiss();
		
		return (cacheMisses);
	}	
	/**
	 * 
	 */
	public Boolean startDBService ()
	{
		debug ("startDBService ()");
		
        // environment is transactional
        envConfig=new EnvironmentConfig();
        envConfig.setTransactional(true);
        
        if (create==true) 
        {
        	debug ("EnvironmentConfig.setAllowCreate(true);");
            envConfig.setAllowCreate(true);
        }
        
        env=new Environment(new File(dbDir), envConfig);
                
        try 
        {
			open();
		} 
        catch (Exception e) 
        {
			e.printStackTrace();
			debug ("Caught a database exception whilst opening the db, aborting main server ...");
			env.toString();
			return (false);
		}	        
        
        if (dbDisabled==true)
        {
        	debug ("Error: database is not open or disabled, aborting");
        	return (false);
        }
                        	  
        try 
        {
			catalogDb=this.accessDB("java_class_catalog");
		} 
        catch (Exception e) 
        {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return (false);
		}        
        
        javaCatalog=new StoredClassCatalog(catalogDb.getDB());        
        
        return (true);
	}
    /**
     * Return the class catalog.
     */
    public final StoredClassCatalog getClassCatalog() 
    {    	
        return javaCatalog;
    }	
	/**
	 * 
	 */
	public boolean startDBThread ()
	{
		debug ("startDBThread ()");
		
		if (runner!=null)
		{
			try 
			{
				runner.run(this);
			} 
			catch (DatabaseException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				return (false);
			} 
			catch (Exception e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				return (false);
			}
		}	
		
		return (false);
	}
    /** 
     * Performs work within a transaction. 
     */
    public void doWork() 
    {
    	debug ("doWork ()");
        	
    	if (dumpDatabase==true)
    	{
    		dumpDB ();
    	}
    }
    /**
     * 
     */
    public HoopBerkeleyDBInstance findDB (String aDB)
    {
    	debug ("findDB ("+aDB+")");
    	
    	for (int i=0;i<databases.size();i++)
    	{
    		HoopBerkeleyDBInstance db=databases.get(i);
    		if (db.getInstanceName().toLowerCase().equals(aDB))
    			return (db);
    	}
    	
    	return (null);
    }
    /** 
     * Opens the main database and creates the Map. 
     */
    public HoopBerkeleyDBInstance open() throws Exception 
    {
    	debug ("open ()");
    	
    	mainDB=findDB ("TSMonitor");
    	
    	if (mainDB==null)
    	{
    		mainDB=new HoopBerkeleyDBInstance ();
    		mainDB.setInstanceName("TSMonitor");
    		mainDB.setEnvironment(env);
    		mainDB.openDB("TSMonitor");
        
    		databases.add(mainDB);
    	}	
                
        debug ("Database should be open and available");      

        return (mainDB);
    }  
    /** 
     * Find a database by name and if one does not exist, create it
     */
    public HoopBerkeleyDBInstance accessDB(String aDB) throws Exception 
    {
    	debug ("openDB ("+aDB+")");
    	        
    	HoopBerkeleyDBInstance db=findDB (aDB);
    	
    	if (db==null)
    	{
    		debug ("Database does not exist yet, creating ...");
    		
    		db=new HoopBerkeleyDBInstance ();
    		db.setInstanceName(aDB);
    		db.setEnvironment(env);
    		db.openDB(aDB);
        
    		databases.add(db);
                
    		debug ("Database should be open and available");
    	}	
        
        return (db);
    }        
    /** 
     * Closes the database. 
     */
    public void close() throws Exception 
    {
    	debug ("close ()");
    	
    	dbDisabled=true;
    	        
        debug("Closing databases ...");
                        
        for (int i=0;i<databases.size();i++)
        {
        	HoopBerkeleyDBInstance db=databases.get(i);
        	if (db!=null)
        	{
        		debug ("Closing: " + db.getInstanceName());
        		db.close();
        	}
        }
        
        debug("Closing class catalog ...");
        
        if (javaCatalog!=null)
        {
        	javaCatalog.close();
        	javaCatalog=null;
        }        
        
        debug("Closing database environment ...");
        
        if (env != null) 
        {
            env.close();
            env = null;
        }
                        
        debug("Resetting map ...");
                
        debug("Database successfully shutdown");
    }
    /**
     * 
     */
    public String getStatus ()
    {
    	if (env==null)
    	{
    		return ("No database environment available");
    	}
    	
    	return (env.getStats(null).toString());
    }
    /**
     * 
     */    
    public ArrayList<String> getDatabases ()
    {
    	if (env==null)
    		return (null);
    	    	
    	return (ArrayList<String>) (env.getDatabaseNames());
    }
    /**
     * 
     */
    public void dumpDB ()
    {
    	debug ("dumpDB ()");
    	
        System.out.print(env.getStats(null).toString());    	    	
    }
}
