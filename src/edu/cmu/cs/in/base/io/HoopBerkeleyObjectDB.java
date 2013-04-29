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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import com.sleepycat.bind.tuple.StringBinding;
import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.collections.StoredMap;

/**
*
*/
public class HoopBerkeleyObjectDB extends HoopBerkeleyDBBase
{
    private StoredMap<String, ArrayList<Object>> map=null;
 	
	/**
	*
	*/	
	public HoopBerkeleyObjectDB ()
	{  
    	setClassName ("HoopBerkeleyObjectDB");
    	debug ("HoopBerkeleyObjectDB ()");    	    	    	
	}
	/**
	 * 
	 */
	public StoredMap<String, ArrayList<Object>> getData ()
	{
		return map;
	}
	/**
	 * 
	 */
	public void assignMap (StoredMap<String, ArrayList<Object>> aMap)
	{
		debug ("assignMap ()");
		
		map=aMap;
	}	
	/**
	 * 
	 */
	public void bind ()
	{

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
    	
    	if (super.close()==false)
    		return (false);
    	                         
        debug("Resetting map ...");
        
        this.map=null;
        
        debug("Database successfully shutdown");
        
        return (true);
    }
    /** 
     * @param aKey String
     * @param aValue ArrayList<Object>
     */
    public boolean writeKV (String aKey,ArrayList<Object> aValue)
    {
    	debug ("writeKV (key:"+aKey+", value:"+aValue+")");
    	
    	if (this.isDbDisabled()==true)
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
    	
    	if (this.isDbDisabled()==true)
    	{
    		debug ("Error: database is not open or disabled, aborting");
    		return;
    	}
    	
    	if (map==null)
    	{
    		debug ("No map available to read from, aborting ..");
    		return;
    	}    	
    	
        debug ("Map size: " + map.size() + " entries");
            
        Iterator<Map.Entry<String, ArrayList<Object>>> iter=null;
        
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
            Map.Entry<String, ArrayList<Object>> entry = iter.next();
            debug (entry.getKey().toString() + ' ' +  entry.getValue());
        }    	
    }
    /**
     * 
     */
    public Boolean checkDB ()
    {
    	debug ("checkDB ()");
    	
    	if (this.isDbDisabled()==true)
    	{
    		debug ("Error: database is not open or disabled, aborting");
    		return (false);
    	}
    	
    	if (map==null)
    	{
    		debug ("No map available to read from, aborting ..");
    		return (false);
    	}    	
    	
        debug ("Checking: " + map.size() + " entries");
            
        Iterator<Map.Entry<String, ArrayList<Object>>> iter=null;
        
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
			Map.Entry<String, ArrayList<Object>> entry = iter.next();
            //debug (entry.getKey().toString() + ' ' +  entry.getValue());
            System.out.print(".");
        }
        
        debug (" done");
        
        return (true);
    }            
}
