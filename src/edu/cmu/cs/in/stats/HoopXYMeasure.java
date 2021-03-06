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

import edu.cmu.cs.in.base.HoopRoot;

//import edu.cmu.cs.in.base.HoopRoot;

/**
 * Careful here, we don't want to inherit from HoopRoot since this object
 * will be instantiated lots and lots of times.
 */
public class HoopXYMeasure //extends HoopRoot
{    						
	protected long xValue=0;
	protected long yValue=0;
	
	/**
	 *
	 */
    public HoopXYMeasure () 
    {
		//setClassName ("HoopXYMeasure");
		//debug ("HoopXYMeasure ()");						
    }  
    /**
     * 
     */
    protected void debug (String aMessage)
    {
    	HoopRoot.debug("HoopXYMeasure",aMessage);
    }
	/**
	 *
	 */	
	public void setXValue(long xValue) 
	{
		this.xValue = xValue;
	}
	/**
	 *
	 */	
	public long getXValue() 
	{
		return xValue;
	}
	/**
	 *
	 */	
	public void setYValue(long aValue) 
	{
		//debug ("setYValue ("+aValue+")");
				
		if (aValue!=0)
			yValue=aValue;
	}
	/**
	 *
	 */	
	public long getYValue() 
	{
		return yValue;
	}	
}
