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

import edu.cmu.cs.in.base.INBase;

public class INPositionList extends INBase
{    		    					
	private String stemmTerm=""; 
	private String collTerm="";
	private Long freq=(long) 1; 
	private Long N=(long) 1;
	private ArrayList<INPositionEntry> posEntries=null;
	
	/**
	 *
	 */
    public INPositionList () 
    {
		setClassName ("INPositionList");
		//debug ("INPositionList ()");		
		posEntries=new ArrayList<INPositionEntry> ();
    }
	/**
	 *
	 */
    public void addDocument (INPositionEntry aDoc)
    {
    	posEntries.add(aDoc);
    }    
	/**
	 *
	 */
    public void addDocument (String aDocID)
    {
    	INPositionEntry newEntry=new INPositionEntry ();
    	newEntry.setDocID(Long.parseLong(aDocID));
    	posEntries.add(newEntry);
    }
	/**
	 *
	 */
    public ArrayList<INPositionEntry> getPosEntries ()
    {
    	return (posEntries);
    }
	/**
	 *
	 */
	public void setStemmTerm(String stemmTerm) 
	{
		this.stemmTerm = stemmTerm;
	}
	/**
	 *
	 */
	public String getStemmTerm() 
	{
		return stemmTerm;
	}
	/**
	 *
	 */
	public void setCollTerm(String collTerm) 
	{
		this.collTerm = collTerm;
	}
	/**
	 *
	 */
	public String getCollTerm() 
	{
		return collTerm;
	}
	/**
	 *
	 */
	public void setFreq(Long freq) 
	{
		this.freq = freq;
	}
	/**
	 *
	 */
	public Long getFreq() 
	{
		return freq;
	}
	/**
	 *
	 */
	public void setN(Long n) 
	{
		N = n;
	}
	/**
	 *
	 */
	public Long getN() 
	{
		return N;
	}    
}
