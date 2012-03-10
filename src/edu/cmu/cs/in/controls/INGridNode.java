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

package edu.cmu.cs.in.controls;

import edu.cmu.cs.in.base.INFeatureMatrixBase;

public class INGridNode extends INFeatureMatrixBase
{
	private int mapperCount=0;
	private int reducerCount=0;
	private int mapperCountTotal=0;
	private int reducerCountTotal=0;	
	private String nodeName="";
	private Boolean active=false;
	
	/**
	 *
	 */
	public INGridNode () 
	{
		setClassName("INGridNode");
		debug ("INGridNode ()");
	}
	/**
	 *
	 */
	public void incMapperCount ()
	{
		this.mapperCount++;
		this.mapperCountTotal++;
	}
	/**
	 *
	 */
	public void decMapperCount ()
	{
		this.mapperCount--;
	}	
	/**
	 *
	 */
	public void incReducerCount ()
	{
		this.reducerCount++;
		this.reducerCountTotal++;
	}
	/**
	 *
	 */
	public void decReducerCount ()
	{
		this.reducerCount--;
	}	
	/**
	 *
	 */
	public void setMapperCount(int mapperCount) 
	{
		this.mapperCount = mapperCount;
	}
	/**
	 *
	 */
	public int getMapperCount() 
	{
		return mapperCount;
	}
	/**
	 *
	 */
	public void setReducerCount(int reducerCount) 
	{
		this.reducerCount = reducerCount;
	}
	/**
	 *
	 */
	public int getReducerCount() 
	{
		return reducerCount;
	}
	/**
	 *
	 */
	public void setNodeName(String nodeName) 
	{
		this.nodeName = nodeName;
	}
	/**
	 *
	 */
	public String getNodeName() 
	{
		return nodeName;
	}
	/**
	 *
	 */	
	public void setActive(Boolean active) 
	{
		this.active = active;
	}
	/**
	 *
	 */	
	public Boolean getActive() 
	{
		return active;
	}
	/**
	 *
	 */	
	public int getMapperCountTotal() 
	{
		return mapperCountTotal;
	}
	/**
	 *
	 */
	public int getReducerCountTotal() 
	{
		return reducerCountTotal;
	}
}

