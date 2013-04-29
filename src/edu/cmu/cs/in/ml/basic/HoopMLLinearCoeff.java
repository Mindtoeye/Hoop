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

package edu.cmu.cs.in.ml.basic;

import edu.cmu.cs.in.base.HoopRoot;

/**
 * A very basic implementation of the coefficient used in a linear
 * equation. We use the generalized linear model:
 * 
 * 	V=w0+(w1*x1)+(w2*x2)+(w3*x3)+(w4*x4)+(w4*x4)
 * 
 * 	Where V is the resulting evaluation
 *  w0 is a constant
 *  w1...4 are weights
 *  x1...4 are variables
 */
public class HoopMLLinearCoeff extends HoopRoot
{    	
	private double weight=1.0;
	private double value=0.0;
	
	/**
	 *
	 */
    public HoopMLLinearCoeff () 
    {
		setClassName ("HoopMLLinearCoeff");
		debug ("HoopMLLinearCoeff ()");						
    }
	/**
	 *
	 */
    public HoopMLLinearCoeff (double aValue) 
    {
		setClassName ("HoopMLLinearCoeff");
		debug ("HoopMLLinearCoeff ()");		
		
		value=aValue;
    }
	/**
	 *
	 */
   public HoopMLLinearCoeff (String aLabel,double aValue) 
   {
		setClassName ("HoopMLLinearCoeff");
		debug ("HoopMLLinearCoeff ()");		
		
		this.setInstanceName(aLabel);
		
		value=aValue;
   }    
	/**
	 *
	 */
	public double getWeight() 
	{
		return weight;
	}
	/**
	 *
	 */
	public void setWeight(double weight) 
	{
		this.weight = weight;
	}
	/**
	 *
	 */
	public double getValue() 
	{
		return value;
	}
	/**
	 *
	 */
	public void setValue(double value) 
	{
		this.value = value;
	}
}
