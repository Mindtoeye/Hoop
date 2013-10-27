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
public class HoopMatrix2X2 extends HoopMatrixBase
{	
	/**
	 * 
	 */
	public HoopMatrix2X2 ()
	{
		setMatrixType (HoopMatrixBase.MATRIX2X2);
		
		entries=new double[2*2];
		
		entries [0]=1;
		entries [1]=0;
		entries [2]=1;
		entries [3]=0;	
	}
	/**
	 * 
	 */
	public HoopMatrix2X2 (double e1,double e2,
					   	  double e3,double e4)
	{
		setMatrixType (HoopMatrixBase.MATRIX2X2);
		
		entries=new double[2*2];
		
		entries [0]=e1;
		entries [1]=e2;
		entries [2]=e3;
		entries [3]=e4;		
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
		return (new HoopMatrix2X2 ());
	}	
}
