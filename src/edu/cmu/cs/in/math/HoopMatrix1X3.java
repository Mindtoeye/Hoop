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

/**
 * 
 */
public class HoopMatrix1X3 extends HoopMatrixBase
{
	/**
	 * 
	 */
	public HoopMatrix1X3 ()
	{		
		setMatrixType (HoopMatrixBase.MATRIX1X3);
		
		entries=new double[3];
		
		entries [0]=1;
		entries [1]=1;
		entries [2]=1;
	}	
	/**
	 * 
	 */
	public HoopMatrix1X3 (double e1,double e2,double e3)
	{
		setMatrixType (HoopMatrixBase.MATRIX1X3);
		
		entries=new double[3];
		
		entries [0]=e1;
		entries [1]=e2;
		entries [2]=e3;					
	}		
	/**
	 * 
	 */
	public void setEntry (int col,double value)
	{		
		int colTrans=col-1;
		
		if (colTrans<0)
		{
			debug ("Index out of range: (col) " + colTrans);
			return;
		}
				
		entries[colTrans]=value;
	}
	/**
	 * 
	 */
	public double getEntry (int col)
	{				
		int colTrans=col-1;
				
		if (colTrans<0)
		{
			debug ("Index out of range: (col) " + colTrans);
			return (0);
		}
						
		return (entries[colTrans]);
	}
	/**
	 * http://en.wikipedia.org/wiki/Identity_matrix
	 */
	public HoopMatrixBase identity ()
	{
		return (new HoopMatrix1X3 ());
	}		
}
