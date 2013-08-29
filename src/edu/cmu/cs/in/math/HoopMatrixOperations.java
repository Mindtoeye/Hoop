/** 
 * 	Author: Martin van Velsen <vvelsen@cs.cmu.edu>
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

package edu.cmu.cs.in.math;

import java.util.Random;

import edu.cmu.cs.in.hoop.hoops.base.HoopBase;

/**
 * 
 */
public class HoopMatrixOperations
{	
	/**
	 * 
	 */
	protected void debug (String aMessage)
	{
		HoopBase.debug("HoopMatrix3X3Operations",aMessage);
	}
	/**
	 * 
	 */
	public HoopMatrix3X3 mult (HoopMatrix3X3 A,double multiplier)
	{
		HoopMatrix3X3 result=new HoopMatrix3X3 ();
		
		for (int i=0;i<9;i++)
		{
			result.entries [i]=A.entries [i]*multiplier;
		}
		
		return (result);
	}
	/**
	 * 
	 */
	public HoopMatrix3X3 add (HoopMatrix3X3 A,double adder)
	{
		HoopMatrix3X3 result=new HoopMatrix3X3 ();
		
		for (int i=0;i<9;i++)
		{
			result.entries [i]=A.entries [i]+adder;
		}
		
		return (result);
	}	
	/**
	 * 
	 */
	public HoopMatrix3X3 outterProduct (HoopMatrix3X3 A,HoopMatrix3X3 B)
	{
		return (A);
	}
	/**
	 * 
	 */
	public HoopMatrix3X3 innerProduct (HoopMatrix3X3 A,HoopMatrix3X3 B)
	{
		return (A);
	}	
	/**
	 * 
	 */
	public HoopMatrix3X3 identity (HoopMatrix3X3 A)
	{
		return (A);
	}
	/**
	 * 
	 */
	public double[] eigenValue (HoopMatrix3X3 A)
	{
		double [] eigen={0,0,0};
		
		return (eigen);
	}
	/**
	 * 
	 */
	/*
	public void displayMatrix (HoopMatrix3X3 target)
	{
		System.out.println ("Matrix: \n");
		System.out.println (target.getEntry(1, 1) + " , " + target.getEntry(1, 2) + " , " + target.getEntry(3, 3));
		System.out.println (target.getEntry(2, 1) + " , " + target.getEntry(2, 2) + " , " + target.getEntry(2, 3));
		System.out.println (target.getEntry(3, 1) + " , " + target.getEntry(3, 2) + " , " + target.getEntry(3, 3));
	}
	*/	
	/**
	 * 
	 */
	public void displayMatrix (HoopMatrix3X3 target)
	{
		System.out.println ("Matrix: \n" + target.entries [0] +" , " + target.entries [1] +" , " + target.entries [2] +"\n"
				 					     + target.entries [3] +" , " + target.entries [4] +" , " + target.entries [5] +"\n"
				 					     + target.entries [6] +" , " + target.entries [7] +" , " + target.entries [8] +"\n");
	}		
	/**
	 * 
	 */
  	public static void main(String[] args) 
  	{
  		Random generator = new Random();
  		
  		HoopMatrix3X3 A=new HoopMatrix3X3 (generator.nextInt(10),generator.nextInt(10),generator.nextInt(10),
  									 	   generator.nextInt(10),generator.nextInt(10),generator.nextInt(10),
  									 	   generator.nextInt(10),generator.nextInt(10),generator.nextInt(10));
  		
  		HoopMatrixOperations operations=new HoopMatrixOperations ();
  		
  		operations.displayMatrix(A);  		
  	}	
}
