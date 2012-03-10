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

import edu.cmu.cs.in.base.INFilterBase;

public class INWikipediaFilter extends INFilterBase
{    	
	private String manipulator="";
	
	/**
	 *
	 */
    public INWikipediaFilter () 
    {
		setClassName ("INWikipediaFilter");
		debug ("INWikipediaFilter ()");						
    }  
	/**
	 *
	 */
    public Boolean evaluate (String aText)   
    {
    	//debug ("evaluate ("+aText+")");
    	
    	if (getNoMore()==true)
    		return (false);
    	
    	if ((aText.equals("References")==true) || (aText.equals("references")==true))
    	{
    		//debug ("FOUND the References tag, skipping everything else ...");
    		setNoMore (true);
    		return (false);
    	}
    	
    	return (true);
    }
	/**
	 *
	 */
   public Boolean cleanBrackets (String aText)   
   {
   		//debug ("cleanBrackets ("+aText+")");
   	   		
   		int firstVal=-1;
   		int lastVal=-1;
   	
   		firstVal=aText.indexOf('[');
   	
   		if (firstVal==-1)
   			return (false);
   		else
   		{
   			lastVal=aText.indexOf (']',firstVal+1);
   			if (lastVal==-1)
   				return (false);
   			else
   			{
   				StringBuffer s = new StringBuffer(aText);
   				StringBuffer removed=s.delete(firstVal,lastVal+1);
   				manipulator=removed.toString();
   			}
   		}
   	
   		//debug ("Clean: " + manipulator);
   		
   		return (true);
   	}                                
	/**
	 *
	 */
   	public Boolean cleanCharacter (String aText,String aChar)   
   	{
 		//debug ("cleanBrackets ("+aText+")");
 	   		
 		int pos=-1;
 	
 		pos=aText.indexOf(aChar);
 	
 		if (pos==-1)
 			return (false);
 		else
 		{
			StringBuffer s = new StringBuffer(aText);
			StringBuffer removed=s.delete(pos,pos+1);
			manipulator=removed.toString();
 		}
 	
 		//debug ("Clean: " + manipulator);
 		
 		return (true);
 	}                  
	/**
	 *
	 */
    public String clean (String aText)   
    {
    	//debug ("clean ("+aText+")");
    	  	   	
    	manipulator=aText;
    	
    	Boolean again=true;
    	
    	while (again==true)
    	{
    		again=cleanBrackets (manipulator);
    	}
    	
    	again=true;    	
    		    	
    	for (int i=0;i<INLink.garbage.length;i++)
    	{
    		again=true;
    	
    		while (again==true)
    		{
    			again=cleanCharacter (manipulator,INLink.garbage [i]);
    		}
    	}	
    	    	
    	return (manipulator);
    }  
	/**
	 *
	 */
    public Boolean check (String aText)   
    {
    	//debug ("check ("+aText+")");
    	
    	if (INStringTools.isInteger(manipulator)==true)
    	{
    		//debug ("Token is number");
    		return (false);
    	}
    	
    	//debug ("Testing: " + aText);
    	
   		if (aText.matches("([a-zA-Z]*)")==true)
   		{
   			//debug ("Token has alphanumeric");
   			return (true);
   		}

   		return (false);
    }
    /**
	 *
	 */
    /*
    public static void main(String args[]) throws Exception
	{
    	INWikipediaFilter filter=new INWikipediaFilter ();
    
    	filter.clean ("[tough] little [tricky] sentence [period]");
	}
	*/
}
