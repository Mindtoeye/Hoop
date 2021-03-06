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
import java.io.IOException;
import java.util.ArrayList;

import com.sleepycat.bind.serial.StoredClassCatalog;
import com.sleepycat.collections.TransactionRunner;
import com.sleepycat.collections.TransactionWorker;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.ExceptionEvent;
import com.sleepycat.je.ExceptionListener;

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.base.HoopRoot;

/**
*
*/
public class HoopBerkeleyDB extends HoopRoot implements TransactionWorker, ExceptionListener
{
    private EnvironmentConfig envConfig=null;	
    private Environment       env=null;

	private FileHandler fh=null; // for logging purposes
    
    private boolean dbDisabled=false;
    private boolean create = true;
    public boolean dumpDatabase=false;
    
    private String dbDir   = "./db";
    private TransactionRunner runner=null;
    private HoopBerkeleyDBBase mainDB=null;
    private HoopBerkeleyDBInstance catalogDb=null;    
    private ArrayList <HoopBerkeleyDBBase> databases=null;
    private StoredClassCatalog javaCatalog=null;
    
    private Boolean dbStarted=false;
	    
	/**
	*
	*/	
	public HoopBerkeleyDB ()
	{  
    	setClassName ("HoopBerkeleyDB");
    	debug ("HoopBerkeleyDB ()");
    	
    	databases=new ArrayList<HoopBerkeleyDBBase> ();
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
	public Boolean getDbStarted() 
	{
		return dbStarted;
	}
    /**
     * 
     */	
	public void setDbStarted(Boolean dbStarted) 
	{
		this.dbStarted = dbStarted;
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
	private void configureLogging ()
	{
		debug ("configureLogging ()");
		
		envConfig.setConfigParam(EnvironmentConfig.FILE_LOGGING_LEVEL, "ALL");
		//envConfig.setConfigParam(EnvironmentConfig.CONSOLE_LOGGING_LEVEL, "ALL");
		
		LogManager lm = LogManager.getLogManager();
		
		String logPath=HoopLink.project.getBasePath()+"//logs";
		
		File logDir=new File (logPath);
		
		if (logDir.exists()==false)
		{
			debug ("Directory doesn't exist yet, creating ...");
			logDir.mkdirs();
		}
		
		debug ("Configuring logging to go to: " + logPath);
		
		try 
		{
			fh = new FileHandler(logPath+"//je.log.txt");
		} 
		catch (SecurityException e1) 
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		catch (IOException e1) 
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Logger logger = Logger.getLogger ("com.sleepycat.je");

		//logger.setLevel(Level.INFO);		
		logger.setLevel(Level.FINE);
		
		lm.addLogger(logger);
		
		logger.addHandler(fh);
				
		logger.log(Level.INFO, "BerkeleyDB logging enabled");		
	}
	/**
	 * 
	 */
	public Boolean startDBService (String MainDB)
	{
		debug ("startDBService ()");
		
		if (getDbStarted()==true)
			return (true);
		
        // environment is transactional
        envConfig=new EnvironmentConfig();
        envConfig.setTransactional(true);
        envConfig.setExceptionListener(this);
        
		configureLogging ();
                
        if (create==true) 
        {
        	debug ("EnvironmentConfig.setAllowCreate(true);");
            envConfig.setAllowCreate(true);
        }
        
        env=new Environment(new File(dbDir), envConfig);
                       
        try 
        {
        	mainDB=findDB (MainDB);
        	
        	if (mainDB==null)
        	{
        		mainDB=new HoopBerkeleyDBInstance ();
        		mainDB.setInstanceName(MainDB);
        		mainDB.setEnvironment(env);
        		mainDB.openDB(MainDB);
            
        		databases.add(mainDB);
        	}	
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
        	catalogDb=new HoopBerkeleyDBInstance ();
        	catalogDb.setInstanceName("java_class_catalog");
			this.accessDB(catalogDb);
		} 
        catch (Exception e) 
        {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return (false);
		}        
        
        javaCatalog=new StoredClassCatalog(catalogDb.getDB());
        
        mainDB.setJavaCatalog(javaCatalog);
        
        return (true);
	}
	/**
	 * 
	 */
	public Boolean startDBService (HoopBerkeleyDBBase aDB)
	{
		debug ("startDBService ()");
		
		if (getDbStarted()==true)
			return (true);
				
        // environment is transactional
        envConfig=new EnvironmentConfig();
        envConfig.setTransactional(true);
        envConfig.setExceptionListener(this);
        
		configureLogging ();
        
        if (create==true) 
        {
        	debug ("EnvironmentConfig.setAllowCreate(true);");
            envConfig.setAllowCreate(true);
        }
        
        env=new Environment(new File(dbDir), envConfig);
                
        try 
        {
        	aDB.setEnvironment(env);
        	aDB.openDB();
            
        	databases.add(aDB);	
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
        	catalogDb=new HoopBerkeleyDBInstance ();
        	catalogDb.setInstanceName("java_class_catalog");
			this.accessDB(catalogDb);
		} 
        catch (Exception e) 
        {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return (false);
		}        
        
        javaCatalog=new StoredClassCatalog(catalogDb.getDB());        
        
        aDB.setJavaCatalog(javaCatalog);
        
        return (true);
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
    public HoopBerkeleyDBBase findDB (String aDB)
    {
    	debug ("findDB ("+aDB+")");
    	
    	for (int i=0;i<databases.size();i++)
    	{
    		HoopBerkeleyDBBase db=databases.get(i);
    		if (db.getInstanceName().toLowerCase().equals(aDB))
    			return (db);
    	}
    	
    	return (null);
    }
    /**
     * 
     */
    public HoopBerkeleyDBBase getDB (Integer aDB)
    {
    	debug ("getDB ("+aDB+")");
    	
    	return (databases.get(aDB));
    }    
    /** 
     * Find a database by name and if one does not exist, create it
     */
    public HoopBerkeleyDBBase accessDB(String aDB) //throws Exception 
    {
    	debug ("openDB ("+aDB+")");
    	        
    	HoopBerkeleyDBBase db=findDB (aDB);
    	
    	if (db==null)
    	{
    		debug ("Database does not exist yet, creating ...");
    		
    		db=new HoopBerkeleyDBBase ();
    		db.setInstanceName(aDB);
    		db.setEnvironment(env);
    		db.setJavaCatalog(javaCatalog);
    		
    		try 
    		{
				db.openDB(aDB);
			} 
    		catch (Exception e) 
    		{			
				e.printStackTrace();
				return (null);
			}
        
    		databases.add(db);
                
    		debug ("Database should be open and available");
    	}	
        
        return (db);
    }        
    /** 
     * Find a database by name and if one does not exist, create it
     */
    public Boolean accessDB(HoopBerkeleyDBBase aDB) //throws Exception 
    {
    	debug ("openDB ("+aDB+")");
    	            	    		
    	aDB.setEnvironment(env);
    	aDB.setJavaCatalog(javaCatalog);
    		
    	try 
    	{
			aDB.openDB();
		} 
    	catch (Exception e) 
    	{			
			e.printStackTrace();
			return (false);
		}
        
    	databases.add(aDB);
                
    	debug ("Database should be open and available");
    	        
        return (true);
    }            
    /** 
     * Closes the database. 
     */
    public void close() throws Exception 
    {
    	debug ("close ()");
    	
    	dbDisabled=true;
    	
    	debug ("Closing log ...");
    	
    	fh.close();
    	        
        debug("Closing databases ...");
                        
        for (int i=0;i<databases.size();i++)
        {
        	HoopBerkeleyDBBase db=databases.get(i);
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
    /**
     * 
     */
	@Override
	public void exceptionThrown(ExceptionEvent arg0) 
	{
		debug ("exceptionThrown ()");		
	}
}
