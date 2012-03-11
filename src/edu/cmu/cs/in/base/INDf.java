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

import java.util.ArrayList;

//import edu.cmu.cs.in.base.INBase;

/**
*
*/
public class INDf //extends INBase
{    			
	private String docID="-1";
	private int Df=0;
	private ArrayList<Integer> positions=null;
	
	/**
	 *
	 */
    public INDf () 
    {
		//setClassName ("INDf");
		//debug ("INDf ()");		
    	positions=new ArrayList<Integer> ();
    }    
	/**
	 *
	 */
	public void setDocID(String docID) 
	{
		this.docID = docID;
	}
	/**
	 *
	 */
	public String getDocID() 
	{
		return docID;
	}	
	/**
	 *
	 */
	public void setDf(int df) 
	{
		Df = df;
	}  
	/**
	 *
	 */
	public void incDf ()
	{
		Df++;
	}
	/**
	 *
	 */
	public int getDf() 
	{
		return Df;
	}	
	/**
	 *
	 */
	public void addPosition (Integer aPos)
	{
		positions.add(aPos);
	}
	/**
	 *
	 */
	public ArrayList<Integer> getPositions ()
	{
		return (positions);
	}
}
