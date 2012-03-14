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

package edu.cmu.cs.in.search;

import java.util.ArrayList;
import java.util.Random;

//import edu.cmu.cs.in.base.INBase;

public class INPositionEntry /*extends INBase*/ implements Comparable
{
	private long docID=-1; 
	private long tf=1; 
	private long docLen=1; 
	private float evaluation=(float) 0.0;
	private ArrayList<Long> posList=null;
	
	/**
	 *
	 */
    public INPositionEntry () 
    {
		//setClassName ("INPositionEntry");
		//debug ("INPositionEntry ()");		
		
		posList=new ArrayList<Long> ();
		
		Random testScoreGenerator = new Random();	
		setEvaluation((float) (testScoreGenerator.nextFloat()-0.010441));		
    }
	/**
	 *
	 */
	public void setDocID(long docID) 
	{
		this.docID = docID;
	}
	/**
	 *
	 */
	public long getDocID() 
	{
		return docID;
	}
	/**
	 *
	 */
	public void setTf(long tf) 
	{
		this.tf = tf;
	}
	/**
	 *
	 */
	public long getTf() 
	{
		return tf;
	}
	/**
	 *
	 */
	public void setDocLen(long docLen) 
	{
		this.docLen = docLen;
	}
	/**
	 *
	 */
	public long getDocLen() 
	{
		return docLen;
	}
	/**
	 *
	 */
	public void setPosList(ArrayList<Long> posList) 
	{
		this.posList = posList;
	}
	/**
	 *
	 */
	public ArrayList<Long> getPosList() 
	{
		return posList;
	}
	/**
	 *
	 */	
	public float getEvaluation() 
	{
		return evaluation;
	}
	/**
	 *
	 */	
	public void setEvaluation(float evaluation) 
	{
		this.evaluation = evaluation;
	}
	/**
	 *
	 */	
	@Override
	public int compareTo(Object obj) 
	{
		//System.out.println ("compareTo ()");
		
		INPositionEntry target=(INPositionEntry) obj;
		if (target.evaluation>this.evaluation)
			return (1);
		
		return 0;
	}    
}
