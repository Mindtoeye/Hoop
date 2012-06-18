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
import java.util.Date;

//import edu.cmu.cs.in.base.INBase;

public class INPerformanceMetrics extends INXYMeasure implements Serializable
{    						
	private static final long serialVersionUID = 1L;
	
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
