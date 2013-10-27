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

import edu.cmu.cs.in.hoop.hoops.base.HoopBase;

/**
 * 
 */
public class HoopMatrixBase
{	
	public static int MATRIX1X2=0;
	public static int MATRIX2X2=1;
	public static int MATRIX1X3=2;
	public static int MATRIX3X3=3;
	public static int MATRIX4X4=4;
	
	private int matrixType=MATRIX1X3;
	
	public double [] entries={}; //  = new double[10];
	
	/**
	 * 
	 */
	protected void debug (String aMessage)
	{
		HoopBase.debug("HoopMatrix",aMessage);
	}
	/**
	 * 
	 */
	public int getMatrixType() 
	{
		return matrixType;
	}
	/**
	 * 
	 */
	public void setMatrixType(int aType) 
	{
		matrixType = aType;
	}
	/**
	 * 
	 */
	public int getMatrixSize ()
	{
		return (entries.length);
	}
	/**
	 * http://en.wikipedia.org/wiki/Identity_matrix
	 */
	public HoopMatrixBase identity ()
	{
		return (this);
	}
	/**
	 * http://en.wikipedia.org/wiki/Transpose
	 */
	public HoopMatrixBase transpose ()
	{
		return (this);
	}	
}
