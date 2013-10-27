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
	public static HoopMatrixBase createInstance (HoopMatrixBase anInstance)
	{		
		if (anInstance.getMatrixType()==HoopMatrixBase.MATRIX1X2)
		{
			return (new HoopMatrix1X2 ());
		}
		
		if (anInstance.getMatrixType()==HoopMatrixBase.MATRIX2X2)
		{
			return (new HoopMatrix2X2 ());
		}
		
		if (anInstance.getMatrixType()==HoopMatrixBase.MATRIX1X3)
		{
			return (new HoopMatrix1X3 ());			
		}
		
		if (anInstance.getMatrixType()==HoopMatrixBase.MATRIX3X3)
		{
			return (new HoopMatrix3X3 ());			
		}
		
		if (anInstance.getMatrixType()==HoopMatrixBase.MATRIX4X4)
		{
			return (new HoopMatrix4X4 ());			
		}
		
		return (null);
	}
	/**
	 * 
	 */
	public HoopMatrixBase mult (HoopMatrixBase A,double multiplier)
	{		
		HoopMatrixBase newInstance=HoopMatrixOperations.createInstance (A);
		
		for (int i=0;i<A.entries.length;i++)
		{
			newInstance.entries [i]=A.entries [i]*multiplier;
		}
		
		return (newInstance);
	}
	/**
	 * 
	 */
	public HoopMatrixBase add (HoopMatrixBase A,double adder)
	{
		HoopMatrixBase result=HoopMatrixOperations.createInstance(A);
		
		for (int i=0;i<A.entries.length;i++)
		{
			result.entries [i]=A.entries [i]+adder;
		}
		
		return (result);
	}	
	/**
	 * 
	 */
	public HoopMatrixBase inverse (HoopMatrixBase A)
	{
		HoopMatrixBase result=HoopMatrixOperations.createInstance(A);
		
		// TBD
		
		return (result);
	}	
	/**
	 * http://en.wikipedia.org/wiki/Outer_product
	 */
	public HoopMatrixBase outterProduct (HoopMatrixBase A,HoopMatrixBase B)
	{
		if (A.getMatrixType()!=B.getMatrixType())
		{
			debug ("Error: currently no code to do matrix reductions or transformations");
			
			return (null);
		}
		
		HoopMatrixBase result=HoopMatrixOperations.createInstance(A);
		
		
		
		return (result);
	}
	/**
	 * http://en.wikipedia.org/wiki/Dot_product#Inner_product
	 */
	public double innerProduct (HoopMatrixBase A,HoopMatrixBase B)
	{
		if (A.getMatrixType()!=B.getMatrixType())
		{
			debug ("Error: currently no code to do matrix reductions or transformations");
			
			return (0);
		}
				
		double inner=0;
		
		for (int i=0;i<A.entries.length;i++)
		{
			inner+=(A.entries [i]*B.entries [i]);
		}
		
		return (inner);
	}	
	/**
	 * 
	 */
	public HoopMatrixBase identity (HoopMatrixBase A)
	{
		if (A.getMatrixType()==HoopMatrixBase.MATRIX1X2)
		{
			return (new HoopMatrix1X2 ().identity());
		}
		
		if (A.getMatrixType()==HoopMatrixBase.MATRIX2X2)
		{
			return (new HoopMatrix2X2 ().identity());
		}
		
		if (A.getMatrixType()==HoopMatrixBase.MATRIX1X3)
		{
			return (new HoopMatrix1X3 ().identity());			
		}
		
		if (A.getMatrixType()==HoopMatrixBase.MATRIX3X3)
		{
			return (new HoopMatrix3X3 ().identity());
		}
		
		if (A.getMatrixType()==HoopMatrixBase.MATRIX4X4)
		{
			return (new HoopMatrix4X4 ().identity());
		}
		
		return (A);
	}
	/**
	 * http://en.wikipedia.org/wiki/Eigenvalues_and_eigenvectors
	 */
	public double eigenValue (HoopMatrix3X3 A)
	{
		
		return (0);
	}
	/**
	 * http://en.wikipedia.org/wiki/Eigenvalues_and_eigenvectors
	 */
	public double[] eigenVector (HoopMatrix3X3 A)
	{
		double [] eigen={0,0,0};
		
		return (eigen);
	}	
	/**
	 * 
	 */
	public void displayMatrix (HoopMatrixBase target)
	{
		if (target.getMatrixType()==HoopMatrixBase.MATRIX1X2)
		{
			System.out.println ("Matrix: \n" + target.entries [0] +" , " + target.entries [1] +"\n");
		}		
		
		if (target.getMatrixType()==HoopMatrixBase.MATRIX2X2)
		{
			System.out.println ("Matrix: \n" + target.entries [0] +" , " + target.entries [1] +"\n");
			System.out.println ("Matrix: \n" + target.entries [2] +" , " + target.entries [3] +"\n");
		}
		
		if (target.getMatrixType()==HoopMatrixBase.MATRIX1X3)
		{
			System.out.println ("Matrix: \n" + target.entries [0] +" , " + target.entries [1] + " , " + target.entries [2] +"\n");
		}		
		
		if (target.getMatrixType()==HoopMatrixBase.MATRIX3X3)
		{
			System.out.println ("Matrix: \n" + target.entries [0] +" , " + target.entries [1] + " , " + target.entries [2] +"\n"
											 + target.entries [3] +" , " + target.entries [4] + " , " + target.entries [5] +"\n"
											 + target.entries [6] +" , " + target.entries [7] + " , " + target.entries [8] +"\n");
		}
		
		if (target.getMatrixType()==HoopMatrixBase.MATRIX4X4)
		{
			System.out.println ("Matrix: \n" + target.entries [0] +" , " + target.entries [1] + " , " + target.entries [2] + " , " + target.entries [3] +"\n"
											 + target.entries [4] +" , " + target.entries [5] + " , " + target.entries [6] + " , " + target.entries [7] +"\n"
											 + target.entries [8] +" , " + target.entries [9] + " , " + target.entries [10] + " , " + target.entries [11] +"\n"
			 								 + target.entries [12] +" , " + target.entries [13] + " , " + target.entries [14] + " , " + target.entries [15] +"\n");
		}		
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
  		
  		System.out.println ("Random (3X3)");
  		
  		operations.displayMatrix(A);
  		
  		System.out.println ("Mult (2.5)");
  		
  		operations.displayMatrix(operations.mult(A,2.5));
  		
  		System.out.println ("Add (2.5)");  		
  		
  		operations.displayMatrix(operations.add(A,2.5));
  		
  		System.out.println ("Identity");
  		
  		operations.displayMatrix(A.identity());
  		
  		System.out.println ("Transpose");  		
  		
  		operations.displayMatrix(A.transpose());
  	}	
}
