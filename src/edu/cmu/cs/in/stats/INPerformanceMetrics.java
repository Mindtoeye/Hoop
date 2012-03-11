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

package edu.cmu.cs.in.stats;

import java.util.Date;

//import edu.cmu.cs.in.base.INBase;

public class INPerformanceMetrics extends INXYMeasure
{    						
	private Date inPoint=null;
	private Date outPoint=null;
	private String label="";
	//private Long measure=0L;
	private String guid="";
	private Boolean open=true;
	
	/**
	 *
	 */
    public INPerformanceMetrics () 
    {
		setClassName ("INPerformanceMetrics");
		//debug ("INPerformanceMetrics ()");						
    }  
	/**
	 *
	 */
    public Boolean isOpen ()
    {
    	return (open);
    }    
	/**
	 *
	 */
    public void reset ()
    {
    	setMarker ("");
    }
	/**
	 *
	 */
    public void setMarker (String aLabel)
    {
    	setLabel(aLabel);
    	inPoint=new Date ();
    }
	/**
	 *
	 */
    public void setInPoint (long aPoint)
    {
    	inPoint=new Date (aPoint);
    }
	/**
	 *
	 */
    public long getOutPoint ()
    {
    	debug ("getOutPoint ()");
   	
      	outPoint=new Date ();
      	
      	return (outPoint.getTime());
    }    
	/**
	 *
	 */
    public void setOutPoint (long aPoint)
    {
    	debug ("setOutPoint ("+aPoint+")");
    	
       	outPoint=new Date (aPoint);
    	setYValue (outPoint.getTime()-inPoint.getTime());
    	open=false;
    }        
	/**
	 *
	 */
    public long getInPoint ()
    {
    	return (inPoint.getTime());
    }
	/**
	 *
	 */
    public void setMarkerRaw (long aValue)
    {
    	setYValue (aValue);
    	open=false;
    }
	/**
	 *
	 */
    public long getMarkerRaw ()
    {
    	if (open==false) // We could have run a simulation
    		return (getYValue ());
    	
    	if (inPoint==null)
    		return (0L);
    	    	    	
    	outPoint=new Date ();
    	
    	setYValue (outPoint.getTime()-inPoint.getTime());
    	
    	open=false;
    	
    	return (getYValue ());
    }    
	/**
	 *
	 */
    public String getMarker ()
    {
    	return ("undefined");
    }  
	/**
	 *
	 */
    public String getMetrics (long timeDiv)
    {    	
    	return ("Time taken for "+label+" is: ("+timeDiv+") ~ " +(timeDiv/1000)+" seconds, " +timeDiv+" milliseconds");
    }    
	/**
	 *
	 */
    public void printMetrics (long timeDiv)
    {    	
    	debug ("Time taken for "+label+" is: ("+timeDiv+") ~ " +(timeDiv/1000)+" seconds, " +timeDiv+" milliseconds");
    }
	/**
	 *
	 */    
	public String getLabel() 
	{
		return label;
	}
	/**
	 *
	 */	
	public void setLabel(String label) 
	{
		this.label = label;
	}
	/**
	 *
	 */	
	public Long getMeasure() 
	{
		return getYValue ();
	}
	/**
	 *
	 */	
	public void setMeasure(Long measure) 
	{
		setYValue (measure);
	}
	/**
	 *
	 */	
	public String getGuid() 
	{
		return guid;
	}
	/**
	 *
	 */	
	public void setGuid(String guid) 
	{
		this.guid = guid;
	}
}
