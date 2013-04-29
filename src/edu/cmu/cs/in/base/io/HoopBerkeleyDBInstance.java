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
//import com.sleepycat.bind.tuple.StringBinding;
//import com.sleepycat.bind.tuple.StringBinding;
//import com.sleepycat.collections.StoredMap;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.Environment;

import edu.cmu.cs.in.base.HoopRoot;

/**
*
*/
public class HoopBerkeleyDBInstance extends HoopBerkeleyDBBase
{
    private StoredMap<String, String> map=null;
 	
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
	public StoredMap<String, String> getData ()
	{
		return map;
	}
	/**
	 * 
	 */
	public void assignMap (StoredMap<String, String> aMap)
	{
		map=aMap;
	}	
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
     * @param aValue String
     */
    public boolean writeKV (String aKey,String aValue)
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
}
