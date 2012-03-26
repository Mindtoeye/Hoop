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
