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
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
//import java.util.Set;

import edu.cmu.cs.in.base.HoopRoot;
import edu.cmu.cs.in.base.io.HoopVFSL;
import edu.cmu.cs.in.base.kv.HoopKVInteger;

public class HoopPositionsBase extends HoopRoot
{    			
	private HoopVFSL fManager=null;
    private Hashtable<String, Integer> entries=null;
    //private ArrayList<HoopKVInteger> entries =null;
    private ArrayList<HoopKVInteger> sorted =null;
    private ArrayList<HoopKVInteger> results=null;
	
	static class MyComparator implements Comparator<Object>
	{
		public int compare(Object obj1, Object obj2)
		{
			int result=0;
			Map.Entry e1 = (Map.Entry)obj1 ;
			Map.Entry e2 = (Map.Entry)obj2 ;//Sort based on values.

			Integer value1 = (Integer)e1.getValue();
			Integer value2 = (Integer)e2.getValue();

			if	(value1.compareTo(value2)==0)
			{
				String word1=(String)e1.getKey();
				String word2=(String)e2.getKey();

				//Sort String in an alphabetical order
				result=word1.compareToIgnoreCase(word2);
			} 
			else
			{
				//Sort values in a descending order
				result=value2.compareTo( value1 );
			}

			return result;
		}
	}		
	/**
	 *
	 */
    public HoopPositionsBase () 
    {
		setClassName ("HoopPositionsBase");
		debug ("HoopPositionsBase ()");			
		
		fManager=new HoopVFSL ();
    }  
	/**
	 *
	 */    
    public boolean isInteger (String input)  
    {      	
    	try  
    	{  
    		Integer.parseInt (input);  
    		return true;  
    	}  
    	catch(NumberFormatException e)  
    	{  
    		return false;  
    	}  
    }      
    /**
     * 
     */    
    public ArrayList<HoopKVInteger> getResults ()
    {
    	return (results);
    }
    /**
     * 
     */    
    public ArrayList<HoopKVInteger> getSorted ()
    {
    	return (sorted);
    }    
    /**
     * 
     */
    public ArrayList<HoopKVInteger> getTop (int topNr)
    {
    	debug ("getTop ()");
    	
    	results=new ArrayList<HoopKVInteger> ();
    	
    	//String str;    	
        //int index=0;
        
        if (topNr>sorted.size())
        	topNr=sorted.size();
        
        for (int i=0;i<topNr;i++) 
        {          
          HoopKVInteger keyValue=sorted.get(i);
          
          results.add(keyValue);
          
          //index++;
        }
        
        return (results);
    }
    /**
     * 
     */
    public Boolean fromString (String aText)
    {
    	debug ("fromString ()");
    
    	String content=HoopLink.fManager.loadContents(HoopLink.datapath);
    	
        ArrayList<String> lines=HoopStringTools.dataToLines(content);
    	
    	return (fromLines (lines));    	   
    }
    /**
     * 
     */
    public void sortEntries ()
    {
    	debug ("sortEntries ()");
    	
        //Put keys and values in to an arraylist using entryset
        ArrayList<Object> myArrayList=new ArrayList<Object>(entries.entrySet());

        //Sort the values based on values first and then keys.
        Collections.sort (myArrayList, new MyComparator());

        //Show sorted results
        Iterator itr=myArrayList.iterator();
        String key="";
        int value=0;
        
        //debug ("Pass 2 ...");
                        
        while (itr.hasNext())
        {
        	System.out.print(".");
        	
        	//cnt++;
        	Map.Entry e=(Map.Entry)itr.next();

        	key = (String)e.getKey();
        	value = ((Integer)e.getValue()).intValue();
        	
        	HoopKVInteger entry=new HoopKVInteger ();
        	entry.setKeyString(key);
        	entry.setValue(e.toString());
        	
        	sorted.add(entry);

        	//debug ("Sorted: "+key+", "+value);
        }            	
    }
    /**
     * 
     */    
    public ArrayList<HoopKVInteger> getValues (String val)
    {
    	debug ("getValues ()");
    	
    	ArrayList<Object> myArrayList=new ArrayList<Object>(entries.entrySet());
    	ArrayList<HoopKVInteger> subset=new ArrayList<HoopKVInteger> ();
    	
        Iterator itr=myArrayList.iterator();
        
        String key="";
        int value=0;        
                                
        while (itr.hasNext())
        {
        	//cnt++;
        	Map.Entry e=(Map.Entry)itr.next();

        	key = (String)e.getKey();
        	value = ((Integer)e.getValue()).intValue();
        	
        	HoopKVInteger tag=new HoopKVInteger ();

        	tag.setKeyString(key);
        	tag.setValue(e.toString ());
        	        	
        	subset.add(tag);
        }            	    	
    	
    	return (subset);
    }
    /**
     * 
     */    
    public String getValuesFormatted (String val)
    {
    	debug ("getValuesFormatted ()");
    	
    	StringBuffer buffer=new StringBuffer ();
    	
    	for (int i=0;i<results.size();i++)
    	{
    		HoopKVInteger res=results.get(i);
    		if (res.getValue().equals(val)==true)
    		{
    			//if ((isInteger (res.getKeyString())==false) && (res.getKeyString().length()<10) && (res.getKeyString().indexOf('$')!=-1) && (res.getKeyString().indexOf('/')!=-1) && (res.getKeyString().indexOf('\\')!=-1))
    			if ((isInteger (res.getKeyString())==false) && (res.getKeyString().length()<10))
    			{
    				buffer.append(res.getKeyString()+"\n");
    			}	
    		}
    	}
    	    	
    	return (buffer.toString());
    }    
    /**
     * 
     */    
    public Boolean fromLines (ArrayList<String> lines)
    {
    	debug ("fromLines ()");
    	
        entries=new Hashtable<String, Integer>();
        sorted=new ArrayList <HoopKVInteger>();
        results=new ArrayList<HoopKVInteger> ();
        
        //debug ("Pass 1 ...");
        
        for (int i=0;i<lines.size();i++)
        {        	
        	String line=lines.get(i);
        	String [] kv=line.split("\\t");
        	
        	//debug ("Key: "+kv [0] +", value: " + kv [1]);
        	
        	HoopKVInteger newer=new HoopKVInteger ();
        	newer.setKeyString(kv [0]);
        	newer.setValue(kv [1]);
        	
        	entries.put(kv [0],Integer.parseInt(kv [1]));
        	results.add(newer);
        }
                 
        return (true);
	}    	    
}
