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

import edu.cmu.cs.in.base.INBase;

public class INXYMeasure extends INBase
{    						
	private long xValue=0;
	private long yValue=0;
	
	/**
	 *
	 */
    public INXYMeasure () 
    {
		setClassName ("INXYMeasure");
		//debug ("INXYMeasure ()");						
    }  
	/**
	 *
	 */	
	public void setXValue(long xValue) 
	{
		this.xValue = xValue;
	}
	/**
	 *
	 */	
	public long getXValue() 
	{
		return xValue;
	}
	/**
	 *
	 */	
	public void setYValue(long yValue) 
	{
		this.yValue = yValue;
	}
	/**
	 *
	 */	
	public long getYValue() 
	{
		return yValue;
	}	
}
