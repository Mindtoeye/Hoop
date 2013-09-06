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
public class HoopMatrix4X4 extends HoopMatrixBase
{
	public double [] entries={0,0,0,0,0,0,0,0,0};
	
	/**
	 * 
	 */
	public HoopMatrix4X4 ()
	{
		setMatrixType (HoopMatrixBase.MATRIX4X4);
		
		entries=new double[4*4];
	}
	/**
	 * 
	 */
	public HoopMatrix4X4 (double e1,double e2,double e3,
					   	  double e4,double e5,double e6,
					      double e7,double e8,double e9)
	{
		setMatrixType (HoopMatrixBase.MATRIX4X4);
		
		entries=new double[4*4];
		
		entries [0]=e1;
		entries [1]=e2;
		entries [2]=e3;
		entries [3]=e4;
		entries [4]=e5;
		entries [5]=e6;
		entries [6]=e7;
		entries [7]=e8;
		entries [8]=e9;
	}	
	/**
	 * 
	 */
	public int getMatrixSize ()
	{
		return (entries.length);
	}	
	/**
	 * Ex:
	 * 
	 * 1,2,3
	 * 4,5,6
	 * 7,8,9
	 * 
	 * ent(col,row)
	 * ent(2,3) -> 6 
	 */
	public void setEntry (int row,int col,double value)
	{		
		int colTrans=col-1;
		
		if (colTrans<0)
		{
			debug ("Index out of range: (col) " + colTrans);
			return;
		}
		
		int rowTrans=row-1;
		
		if (rowTrans<0)
		{
			debug ("Index out of range: (row) " + rowTrans);
			return;
		}			
		
		entries[(3*rowTrans)+colTrans]=value;
	}
	/**
	 * 
	 */
	public double getEntry (int row,int col)
	{				
		int colTrans=col-1;
				
		if (colTrans<0)
		{
			debug ("Index out of range: (col) " + colTrans);
			return (0);
		}
		
		int rowTrans=row-1;
		
		if (rowTrans<0)
		{
			debug ("Index out of range: (row) " + rowTrans);
			return (0);
		}			
						
		return (entries[(3*rowTrans)+colTrans]);
	}
}
