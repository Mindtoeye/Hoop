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

/**
*
*/
public class INKV //extends INFeatureMatrixBase
{    			
	private int key=-1;
	private String keyString="";
	private String value="";
			
	/**
	 *
	 */
    public INKV () 
    {
		//setClassName ("INKV");
		//debug ("INKV ()");						
    }
	/**
	 *
	 */
	public int getKey() 
	{
		return key;
	}
	/**
	 *
	 */
	public void setKey(int key) 
	{
		this.key = key;
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
	}
	/**
	 *
	 */	
	public String getKeyString() 
	{
		return keyString;
	}
	/**
	 *
	 */	
	public void setKeyString(String keyString) 
	{
		this.keyString = keyString;
	}  
}
