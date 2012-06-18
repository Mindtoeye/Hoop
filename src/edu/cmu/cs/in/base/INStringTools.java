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

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class INStringTools extends INBase
{    				
	public static LinkedList<String> numbers=null;
	
	/**
	 *
	 */
    public INStringTools () 
    {
		setClassName ("INStringTools");
		debug ("INStringTools ()");						
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
    	//INBase.debug ("INStringTools","Splitting: " + input);
    	
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
    	//INBase.debug ("INStringTools","Splitting: " + input);
    	
    	String [] splitter=input.split("\\.");
    	
    	if (splitter.length==0)
    		return (input);    	
	
    	return (splitter [splitter.length-1]);
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
}
