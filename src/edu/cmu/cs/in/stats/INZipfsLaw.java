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

public class INZipfsLaw extends INStatsBase
{
	private double N=1;
	private double Ctf=1;
	private double R=1; // Rank 1
	private double A=0.1; // For English
	
	/**
	 *
	 */
    public INZipfsLaw () 
    {
		setClassName ("INZipfsLaw");
		debug ("INZipfsLaw ()");						
    }
	/**
	 *
	 */
	public void setN(double n) 
	{
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
	public void setCtf(double ctf) 
	{
		Ctf = ctf;
	}
	/**
	 *
	 */
	public double getCtf() 
	{
		return Ctf;
	}
	/**
	 *
	 */
	public void setR(double r) 
	{
		R = r;
	}
	/**
	 *
	 */
	public double getR() 
	{
		return R;
	}
	/**
	 *
	 */
	public void setA(double a) 
	{
		A = a;
	}
	/**
	 *
	 */
	public double getA() 
	{
		return A;
	}
	/**
	 *
	 */
	public double calculateProbability ()
	{		
		return ((A*N)/R);
	}
	/**
	 *
	 */
	public double calculateOccurrences ()
	{		
		return ((A/R)*N);
	}	
	/**
	 * 
	 */
    public static void main(String args[]) throws Exception
	{
    	INZipfsLaw zl=new INZipfsLaw ();
    	
    	//double N=1000;
    	int i=0;
    	
    	for (int j=1;j<10;j++)
    	{    	
    		zl.setN(j*10000);
    		
    		System.out.println ("For N is " + zl.getN());

    		
    		for (i=1;i<11;i++) // The 10 highest ranking terms
    		{
    			zl.setR(i);
    			System.out.println ("For R(ank): " + i + " in N: "+ zl.getN()+" has " + zl.calculateOccurrences () + " occurrances");
    		}
    	}	
	}	
}
