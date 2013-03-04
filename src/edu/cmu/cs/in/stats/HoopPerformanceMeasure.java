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

package edu.cmu.cs.in.stats;

import java.io.Serializable;
import java.util.ArrayList;
//import java.io.Serializable;
import java.util.Date;

//import edu.cmu.cs.in.base.HoopBase;

/**
 * Careful here, we don't want to inherit from HoopRoot since this object
 * will be instantiated lots and lots of times.
 */
public class HoopPerformanceMeasure extends HoopSampleMeasure implements Serializable
{    							
	private static final long serialVersionUID = 4186308870928663985L;
	
	private Date inPoint=null;
	private Date outPoint=null;
	private String label="";
	private String guid="";
	
	/**
	 *
	 */
    public HoopPerformanceMeasure () 
    {

    }  
	/**
	 *
	 */
    public void reset ()
    {
    	super.reset();
    	
    	xValue=0;
    	yValue=0;    	
    	
    	inPoint=null;
    	outPoint=null;
    	label="";
    }
	/**
	 *
	 */
    public void setMarker (String aLabel)
    {
    	setLabel(aLabel);
    	inPoint=new Date ();
    	
    	debug ("Setting (in) marker at: " + inPoint.getTime());
    }
    /**
     * 
     */
    public void closeMarker ()
    {
    	debug ("closeMarker ()");
    	
    	outPoint=new Date ();
    	
    	debug ("Setting (out) marker at: " + outPoint.getTime());
    	
    	setYValue (outPoint.getTime()-inPoint.getTime());
    	
    	HoopXYMeasure newValue=new HoopXYMeasure ();
    	newValue.setYValue(this.getYValue());    	
    	
    	addValue (newValue);    	
    }
    /**
     * 
     */
    public void closeMarker (String aMarker)
    {
    	debug ("closeMarker ("+aMarker+")");
    	
    	label=aMarker;
    			
    	outPoint=new Date ();
    	
    	debug ("Setting (out) marker at: " + outPoint.getTime());
    	
    	setYValue (outPoint.getTime()-inPoint.getTime());
    	
    	HoopXYMeasure newValue=new HoopXYMeasure ();
    	newValue.setYValue(this.getYValue());
    	
    	addValue (newValue);
    }    
    /**
     * 
     */
    public float getAverage ()
    {
    	if (getValuesSize ()==0)
    		return (float) (0.0);
    	
    	long total=0;
    	
    	for (int i=0;i<getValuesSize ();i++)
    	{
    		HoopXYMeasure measure=getValue (i);
    		    		
    		total+=measure.getYValue();
    	}
    	
    	return (total/getValuesSize ());
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
    public long getInPoint ()
    {
    	return (inPoint.getTime());
    }    
	/**
	 *
	 */
    public void setOutPoint (long aPoint)
    {
    	debug ("setOutPoint ("+aPoint+")");
   	
      	outPoint=new Date (aPoint);
      	setYValue (outPoint.getTime()-inPoint.getTime());
      	setOpen (false);
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
    public void printMetrics ()
   	{    	
    	debug ("Time taken for "+label+" is: ("+getYValue ()+") ~ " +(getYValue ()/1000)+" seconds, " +getYValue ()+" milliseconds");
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
