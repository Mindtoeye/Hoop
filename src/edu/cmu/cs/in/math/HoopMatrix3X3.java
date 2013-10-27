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
public class HoopMatrix3X3 extends HoopMatrixBase
{	
	/**
	 * 
	 */
	public HoopMatrix3X3 ()
	{
		setMatrixType (HoopMatrixBase.MATRIX3X3);
		
		entries=new double[3*3];
		
		entries [0]=1;
		entries [1]=0;
		entries [2]=0;
		entries [3]=0;
		entries [4]=1;
		entries [5]=0;
		entries [6]=0;
		entries [7]=0;
		entries [8]=1;	
	}
	/**
	 * 
	 */
	public HoopMatrix3X3 (double e1,double e2,double e3,
					   	  double e4,double e5,double e6,
					      double e7,double e8,double e9)
	{
		setMatrixType (HoopMatrixBase.MATRIX3X3);
		
		entries=new double[3*3];
		
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
	/**
	 * http://en.wikipedia.org/wiki/Identity_matrix
	 */
	public HoopMatrixBase identity ()
	{
		return (new HoopMatrix3X3 ());
	}
	/**
	 * http://en.wikipedia.org/wiki/Transpose
	 * 
	 * [0][1][2]
	 * [3][4][5]
	 * [6][7][8]
	 * 
	 * =
	 * 
	 * [0][3][6]
	 * [1][4][7]
	 * [2][5][8] 
	 */
	public HoopMatrixBase transpose ()
	{
		HoopMatrix3X3 transposed=new HoopMatrix3X3 ();
		
		transposed.entries [3]=this.entries[1];
		transposed.entries [6]=this.entries[2];
		transposed.entries [7]=this.entries[5];
		
		transposed.entries [0]=this.entries[0];
		transposed.entries [4]=this.entries[4];
		transposed.entries [8]=this.entries[8];		
		
		transposed.entries [1]=this.entries[3];
		transposed.entries [2]=this.entries[6];
		transposed.entries [5]=this.entries[7];		
		
		return (transposed);
	}	
}
