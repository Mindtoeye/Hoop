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

package edu.cmu.cs.in.hoop.base;

/**
* 
*/
public class INHoopLoadBase extends INHoopBase
{    				
	private String content=null;
	
	/**
	 *
	 */
    public INHoopLoadBase () 
    {
		setClassName ("INHoopLoadBase");
		debug ("INHoopLoadBase ()");
		hoopCategory="load";
    }
	/**
	 *
	 */
	public void setContent(String content) 
	{
		this.content = content;
	}
	/**
	 *
	 */
	public String getContent() 
	{
		return content;
	}
}
