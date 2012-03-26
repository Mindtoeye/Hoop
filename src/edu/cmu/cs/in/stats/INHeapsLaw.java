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
