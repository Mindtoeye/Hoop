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

package edu.cmu.cs.in.base.kv;

import java.util.ArrayList;

/**
 * 
 */
public class HoopKVTools
{    			
    /**
     * 
     */
    public static HoopKV getLikeKVType (HoopKV aKV)
    {
    	HoopKV newKV=new HoopKVString ();
    	
    	if (aKV instanceof HoopKVInteger)
    		newKV=new HoopKVInteger ();
    	
    	if (aKV instanceof HoopKVString)
    	{
    		newKV=new HoopKVString ();
    	}
    	
    	newKV.setValuesRaw (HoopKVTools.copyValues (aKV));
    	
    	return (newKV);
    }
    /**
     * 
     */
    public static ArrayList <Object> copyValues (HoopKV aKV)
    {
    	ArrayList <Object> newValues=new ArrayList<Object> ();
    	
    	ArrayList <Object> oldValues=aKV.getValuesRaw();
    	
    	for (int i=0;i<oldValues.size();i++)
    	{
    		Object oldValue=oldValues.get(i);
    		
    		// Cheat ...
    		
    		newValues.add(oldValue);
    	}
    	
    	return (newValues);
    }
    /**
     * 
     */
    public static HoopKV createFromType (String aType)
    {
    	if (aType.equals("CLASS")==true)
    	{
    		return (new HoopKVClass ());
    	}
    	
    	if (aType.equals("INT")==true)
    	{
    		return (new HoopKVInteger ());
    	}
    	
    	if (aType.equals("LONG")==true)
    	{
    		return (new HoopKVLong ());
    	}
    	
    	if (aType.equals("STRING")==true)
    	{
    		return (new HoopKVString ());
    	}
    	
    	if (aType.equals("FLOAT")==true)
    	{
    		return (new HoopKVFloat ());
    	}
    	
    	if (aType.equals("BOOLEAN")==true)
    	{
    		return (new HoopKVBoolean ());
    	}
    	
    	if (aType.equals("ENUM")==true)
    	{
    			
    	}
    	
    	if (aType.equals("TABLE")==true)
    	{
    		
    	}
    	
    	if (aType.equals("DOCUMENT")==true)
    	{
    		return (new HoopKVDocument ());
    	}
    	
    	if (aType.equals("DATE")==true)
    	{
    		
    	}
    	
    	if (aType.equals("COLOR")==true)
    	{
    		
    	}
    	
    	if (aType.equals("FONT")==true)
    	{
    		
    	}
    	
    	if (aType.equals("URI")==true)
    	{
    		
    	}
    	
    	if (aType.equals("URL")==true)
    	{
    		
    	}  	
    	    	
    	return (new HoopKVString ());
    }
}
