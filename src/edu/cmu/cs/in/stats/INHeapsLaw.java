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

import edu.cmu.cs.in.stats.INStatsBase;

public class INHeapsLaw extends INStatsBase
{    		    			
	private double Beta=(double) 0.5;
	private double K=25;
	private double N=1;// Most likely something like 6000000 terms
		
	/**
	 *
	 */
    public INHeapsLaw () 
    {
		setClassName ("INHeapsLaw");
		debug ("INHeapsLaw ()");						
    }
	/**
	 *
	 */
    public double calculateHeapsLaw ()
    {
    	return (K*Math.pow (N,Beta));
    }
	/**
	 *
	 */
    public double calculateHeapsLaw (double anN)
    {
    	setN (anN);
    	return (calculateHeapsLaw ());
    }    
	/**
	 *
	 */
	public void setBeta(double beta) 
	{
		Beta = beta;
	}
	/**
	 *
	 */
	public double getBeta() 
	{
		return Beta;
	}
	/**
	 *
	 */
	public void setK(double k) 
	{
		K = k;
	}
	/**
	 *
	 */
	public double getK() 
	{
		return K;
	}
	/**
	 *
	 */
	public void setN(double n) 
	{
		if (n<1)
			return; // No, I say no!
		
		N = n;
	}
	/**
	 *
	 */
	public double getN() 
	{
		return N;
	}    
	/**
	 * 
	 */
    public static void main(String args[]) throws Exception
	{
    	INHeapsLaw hl=new INHeapsLaw ();
    	
    	for (double i=0;i<70;i++)
    	{
    		hl.setN(i*1000000);
    		System.out.println ("For N: " + i + "*(100K) Heaps Law: ~" + Math.round(hl.calculateHeapsLaw()/1000) + "K");
    	}
	}
}
