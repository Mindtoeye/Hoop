/** 
 * Author: Martin van Velsen <vvelsen@cs.cmu.edu>
 * 
 * This source set uses code written for other various graduate 
 * courses and is part of a larger research effort in the field of
 * interactive narrative (IN). Courses that have contribute
 * to this source base are:
 * 
 * 	05-834 Applied Machine Learning
 *  11-719 Computational Models of Discourse Analysis
 *  11-741 Information Retrieval
 * 
 */

package edu.cmu.cs.in.base;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class INStringTools extends INFeatureMatrixBase
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
