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

import edu.cmu.cs.in.base.INFeatureMatrixBase;

public class INStatisticsMeasure extends INFeatureMatrixBase
{    			
	private float N=0;
	private float value=0;
	private float median=0;
	private float avg=0;
	private float min=0;
	
	private String label="Unlabeled";
	private String statsError="noerror";
		
	/**
	 *
	 */
    public INStatisticsMeasure () 
    {
		setClassName ("INStatisticsMeasure");
		debug ("INStatisticsMeasure ()");						
    }
	/**
	 *
	 */
    public Boolean calcStats ()
    {
    	debug ("calcStats ()");
    	
    	if (N==0)
    	{
    		statsError="N=0, can't calculate";
    		return (false);
    	}    	
    	
    	if (N==1)
    	{
    		statsError="N=1, can't calculate";
    		return (false);
    	}
    	
    	if (value==0)
    	{
    		statsError="Value=0, can't calculate";
    		return (false);
    	}
    	
    	avg=value/N;
    	
    	return (true);
    }
	/**
	 *
	 */
	public String getLastError() 
	{
		return statsError;
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
	public String getLabel() 
	{
		return label;
	}	
	/**
	 *
	 */
	public float getN() 
	{
		return N;
	}
	/**
	 *
	 */
	public void setN(float n) 
	{
		N = n;
	}
	/**
	 *
	 */
	public void increment(float aValue) 
	{
		N++;
		value+=aValue;
		avg=value/N;
	}	
	/**
	 *
	 */
	public float getMedian() 
	{
		return median;
	}
	/**
	 *
	 */
	public float getAvg() 
	{
		return avg;
	}
	/**
	 *
	 */
	public float getMin() 
	{
		return min;
	}
	/**
	 *
	 */	
	public float getValue() 
	{
		return value;
	}
	/**
	 *
	 */	
	public void setValue(float value) 
	{
		this.value = value;
	}  
}
