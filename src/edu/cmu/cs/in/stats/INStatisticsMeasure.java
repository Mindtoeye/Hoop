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

import edu.cmu.cs.in.base.INBase;

public class INStatisticsMeasure extends INBase
{    			
	private float N=0;
	private float value=0;
	private float median=0;
	private float avg=0;
	private float min=0;
	
	private String label="Unlabeled";
	private String statsError="noerror";
		
	/**
	 *
	 */
    public INStatisticsMeasure () 
    {
		setClassName ("INStatisticsMeasure");
		debug ("INStatisticsMeasure ()");						
    }
	/**
	 *
	 */
    public Boolean calcStats ()
    {
    	debug ("calcStats ()");
    	
    	if (N==0)
    	{
    		statsError="N=0, can't calculate";
    		return (false);
    	}    	
    	
    	if (N==1)
    	{
    		statsError="N=1, can't calculate";
    		return (false);
    	}
    	
    	if (value==0)
    	{
    		statsError="Value=0, can't calculate";
    		return (false);
    	}
    	
    	avg=value/N;
    	
    	return (true);
    }
	/**
	 *
	 */
	public String getLastError() 
	{
		return statsError;
	}    
	/**
	 *
	 */
	public void setLabel(String label) 
	{
		this.label = label;
	}
	/**
	 *
	 */
	public String getLabel() 
	{
		return label;
	}	
	/**
	 *
	 */
	public float getN() 
	{
		return N;
	}
	/**
	 *
	 */
	public void setN(float n) 
	{
		N = n;
	}
	/**
	 *
	 */
	public void increment(float aValue) 
	{
		N++;
		value+=aValue;
		avg=value/N;
	}	
	/**
	 *
	 */
	public float getMedian() 
	{
		return median;
	}
	/**
	 *
	 */
	public float getAvg() 
	{
		return avg;
	}
	/**
	 *
	 */
	public float getMin() 
	{
		return min;
	}
	/**
	 *
	 */	
	public float getValue() 
	{
		return value;
	}
	/**
	 *
	 */	
	public void setValue(float value) 
	{
		this.value = value;
	}  
}
