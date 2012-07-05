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

package edu.cmu.cs.in.base;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

/** 
 * @author Martin van Velsen
 */
public class INHoopEnvironment extends INBase 
{
	/**
	 * 
	 */
	public INHoopEnvironment ()
	{
		setClassName ("INHoopEnvironment");
		debug ("INHoopEnvironment ()");			
	}
	/**
	*
	 */
	public ArrayList <String> getProfileLocation ()
    {
    	debug ("getProfileLocation ()");
    	
    	ArrayList <String> writableDirs=new ArrayList<String> ();
    	
		Properties systemProps = System.getProperties();
		Set<Entry<Object, Object>> sets = systemProps.entrySet();
		Map<String,String> env=System.getenv();
		
		//debug ("Java properties: ");
		
		for (Entry<Object,Object> entry : sets) 
		{
			//debug ("Inspecting Java property: " + entry.getKey().toString() +"="+entry.getValue().toString());								
		}    	
				
		//debug ("Environment properties: ");
		
        for (String envName : env.keySet()) 
        {
        	//debug ("Inspecting Environment property: " + envName +"="+env.get(envName));        	
        }
		
		String option="";
    	    	
    	if (isWindows()==true)
    	{
    		//String result=Environment.GetFolderPath(Environment.SpecialFolder.CommonApplicationData);
    		//option=System.getenv("CSIDL_COMMON_APPDATA");
    		
    		option=env.get("APPDATA"); // Something like: "C:\Users\vvelsen\AppData\Roaming"
    		
    		writableDirs.add(option);
    		
    		option=System.getProperty("user.home"); // Something like : "C:\Users\vvelsen"
    		
    		writableDirs.add(option);
    		
    		option=env.get("ALLUSERSPROFILE"); // Something like: "C:\ProgramData"
    		
    		writableDirs.add(option);
    		    		
    		option=env.get("HOMEPATH"); // Something like: "\Users\vvelsen"
    		
    		writableDirs.add(option);
    		
    		option=env.get("ProgramFiles");
    		if(option != null) writableDirs.add(option);
    		    		    		
    		//option=env.get("PUBLIC"); // Something like: "C:\Users\Public" DOES NOT WORK ON XP
    		
    		//writableDirs.add(option);    		
    	}	
    	
    	if (isMac()==true)
    	{
    		/*
    		 * mkdir ~/Public
    		 * chmod 755 ~/Public
    		 */
    		
    		option=System.getenv("user.home");
    		
    		writableDirs.add(option);
    	}	
    	    	
    	if (isUnix()==true)
    	{
    		option=System.getenv("user.home");
    		
    		writableDirs.add(option);
    	}	
    	
    	if (isSolaris()==true)
    	{
    		option=System.getenv("user.home");
    		
    		writableDirs.add(option);
    	}
    	
    	// remove writableDirs that are not actually writable
    	int i = 0;
    	while(i < writableDirs.size())
    	{
    		String s = writableDirs.get(i);
    		java.io.File f = new java.io.File(s);
    		if(!f.canWrite())
    		{
    			writableDirs.remove(i);
    			continue;
    		}
    		++i;
    	}
    	    	
    	return (writableDirs);
    }	
	/**
	*
	*/		
	public static boolean isWindows() 
	{		
		String os = System.getProperty("os.name").toLowerCase();
		// windows
		return (os.indexOf("win") >= 0);
	}
	/**
	*
	*/	
	public static boolean isMac() 
	{
		String os = System.getProperty("os.name").toLowerCase();
		// Mac
		return (os.indexOf("mac") >= 0);
	}
	/**
	*
	*/	 
	public static boolean isUnix() 
	{
		String os = System.getProperty("os.name").toLowerCase();
		// linux or unix
		return (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0);
	}
	/**
	*
	*/	 
	public static boolean isSolaris() 
	{
		String os = System.getProperty("os.name").toLowerCase();
		// Solaris
		return (os.indexOf("sunos") >= 0);
	}				
}	