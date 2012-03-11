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

import edu.cmu.cs.in.base.INBase;

public class INFilterBase extends INBase
{    		    		
	private Boolean noMore=false;
	
	/**
	 *
	 */
    public INFilterBase () 
    {
		setClassName ("INFilterBase");
		debug ("INFilterBase ()");						
    }  
	/**
	 *
	 */
    public Boolean evaluate (String aText)   
    {
    	debug ("evaluate ("+aText+")");
    	
    	if (noMore==true)
    		return (false);
    	
    	return (true);
    }
	/**
	 *
	 */
    public String clean (String aText)   
    {
    	debug ("clean ("+aText+")");
   	   	
    	return (aText);
    }
	/**
	 *
	 */
    public Boolean check (String aText)   
    {
    	debug ("check ("+aText+")");
  	   	
    	return (true);
    }    
	/**
	 *
	 */
    public void reset ()
    {
    	noMore=false;
    }
	/**
	 *
	 */    
	public Boolean getNoMore() 
	{
		return noMore;
	}
	/**
	 *
	 */	
	public void setNoMore(Boolean noMore) 
	{
		this.noMore = noMore;
	}  
}
