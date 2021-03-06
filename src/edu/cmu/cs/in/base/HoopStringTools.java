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
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

public class HoopStringTools extends HoopRoot
{    				
	public static LinkedList<String> numbers=null;
	
	/**
	 *
	 */
    public HoopStringTools () 
    {
		setClassName ("HoopStringTools");
		debug ("HoopStringTools ()");						
    }
	/**
	 *
	 */    
    public static LinkedList<String> StringToNumbers (String aString)
    {
    	LinkedList<String> numbers = new LinkedList<String>();

    	Pattern p = Pattern.compile("\\d+");
    	Matcher m = p.matcher(aString); 
    	while (m.find()) 
    	{
    		numbers.add (m.group());
    	}
    	
    	return (numbers);
    }
	/**
	 *
	 */
    public static String getNamespaceFirst (String input)
    {
    	//HoopBase.debug ("HoopStringTools","Splitting: " + input);
    	
    	String [] splitter=input.split("\\.");
    	
    	if (splitter.length==0)
    		return (input);
    	
    	return (splitter [0]);
    }
	/**
	 *
	 */
    public static String getNamespaceLast (String input)
    {
    	//HoopBase.debug ("HoopStringTools","Splitting: " + input);
    	
    	String [] splitter=input.split("\\.");
    	
    	if (splitter.length==0)
    		return (input);    	
	
    	return (splitter [splitter.length-1]);
    }    
	/**
	 *
	 */
    public static ArrayList <String> splitComma (String input)
    {   	
    	ArrayList <String> newList=new ArrayList<String> ();
    	
    	String [] splitter=input.split("\\,");
    	
    	for (int i=0;i<splitter.length;i++)
    	{
    		String anEntry=splitter [i];
    		
    		newList.add(anEntry.trim());
    	}
   	
    	return (newList);
    }        
	/**
	 *
	 */    
    public static boolean isInteger (String input)  
    {  
       try  
       {  
          Integer.parseInt (input);  
          return true;  
       }  
       catch(Exception e)  
       {  
          return false;  
       }  
    }      
	/**
	 *
	 */    
   public static boolean isLong (String input)  
   {  
      try  
      {  
         Long.parseLong(input);
         return true;  
      }  
      catch(Exception e)  
      {  
         return false;  
      }  
   } 
   /**
    * 
    */
	public static ArrayList<String> dataToLines (String aData)
	{    
		//debug ("dataToLines ()");
		
		ArrayList<String> lines=new ArrayList<String> ();		
	
		String [] lineList=aData.split(System.getProperty("line.separator"));
		
		for (int i=0;i<lineList.length;i++)
		{
			lines.add(new String (lineList [i]));
		}
		
		return (lines);
	}
	/**
	 * 
	 */
	public static void listLines (ArrayList<String> aLines)
	{
		//debug ("listLines ()");
		
		for (int i=0;i<aLines.size();i++)
		{
			//debug ("* "+aLines.get(i)+" *");
		}
	}	
	/**
	 * 
	 */
	public static long calculateChecksum (String aString)
	{
		if (aString==null)
			return (0);
		
        //Convert string to bytes
        byte bytes[] = aString.getBytes();
       
        Checksum checksum = new CRC32();
       
        /*
         * To compute the CRC32 checksum for byte array, use
         *
         * void update(bytes[] b, int start, int length)
         * method of CRC32 class.
         */
         
        checksum.update(bytes,0,bytes.length);
       
        /*
         * Get the generated checksum using
         * getValue method of CRC32 class.
         */
                
    	long currentChecksum = checksum.getValue();
    	
    	return (currentChecksum);
	}
}
