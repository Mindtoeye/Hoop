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

import java.util.ArrayList;

import edu.cmu.cs.in.base.INBase;

/**
* Here we have the basis for all the hoops. It manages incoming and
* outgoing links to other hoops. Please keep in mind that even
* though the API allows more than one incoming hoops, we currently
* restrict the functionality to only one.
*/
public class INHoopBase extends INBase
{    			
	private ArrayList <INHoopBase> inHoops=null;
	private ArrayList <INHoopBase> outHoops=null;

	/// Either one of display,load,save,transform 
	protected String hoopCategory="none"; 
	
	/**
	 *
	 */
    public INHoopBase () 
    {
		setClassName ("INHoopBase");
		debug ("INHoopBase ()");						
    }
	/**
	 *
	 */    
	public ArrayList <INHoopBase> getInHoops ()
	{
		return (inHoops);
	}
	/**
	 *
	 */	
	public ArrayList <INHoopBase> getOutHoops ()
	{
		return (outHoops);
	}
	/**
	 *
	 */	
	public void setHoopCategory(String hoopCategory) 
	{
		this.hoopCategory = hoopCategory;
	}
	/**
	 *
	 */	
	public String getHoopCategory() 
	{
		return hoopCategory;
	}
}
