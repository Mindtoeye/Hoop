/** 
 * Author: Martin van Velsen <vvelsen@cs.cmu.edu>
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as 
 *  published by the Free Software Foundation, either version 3 of the 
 *  License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package edu.cmu.cs.in.stats;

import edu.cmu.cs.in.stats.HoopStatsBase;

public class HoopZipfsLaw extends HoopStatsBase
{
	private double N=1;
	private double Ctf=1;
	private double R=1; // Rank 1
	private double A=0.1; // For English
	
	/**
	 *
	 */
    public HoopZipfsLaw () 
    {
		setClassName ("HoopZipfsLaw");
		debug ("HoopZipfsLaw ()");						
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
    	HoopZipfsLaw zl=new HoopZipfsLaw ();
    	
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
