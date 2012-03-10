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

//import edu.cmu.cs.in.base.INFeatureMatrixBase;

public class INToken //extends INFeatureMatrixBase
{    
	private String value;
	private Integer position;
	
	/**
	 *
	 */
    public INToken () 
    {
		//setClassName ("INToken");
		//debug ("INToken ()");						
    }
	/**
	 *
	 */
   public INToken (String aValue) 
   {
		//setClassName ("INToken");
		//debug ("INToken ()");	
	   
	   setValue (aValue);
   }    
	/**
	 *
	 */
	public String getValue() 
	{
		return value;
	}
	/**
	 *
	 */
	public void setValue(String value) 
	{
		this.value = value;
		
		if (value.indexOf(':')!=-1)
		{		
			String [] temp=value.split(":");
		
			if (temp.length>1)
			{
				this.value=temp [0];
				position=Integer.parseInt(temp [1]);
			}
		}	
	}
	/**
	 *
	 */
	public Integer getPosition() 
	{
		return position;
	}	
	/**
	 *
	 */
	public void setPosition(Integer position) 
	{
		this.position = position;
	}  
}
