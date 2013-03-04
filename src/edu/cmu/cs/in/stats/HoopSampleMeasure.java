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

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Careful here, we don't want to inherit from HoopRoot since this object
 * will be instantiated lots and lots of times.
 */
public class HoopSampleMeasure extends HoopXYMeasure implements Serializable
{    							
	private static final long serialVersionUID = 4186308870928663985L;
	
	private Boolean open=true;
	
	private ArrayList<HoopXYMeasure> values=null;
	
	/**
	 *
	 */
    public HoopSampleMeasure () 
    {		
    	values=new ArrayList<HoopXYMeasure> ();
    }  
	/**
	 *
	 */
    public Boolean isOpen ()
    {
    	return (open);
    }  
    /**
     * 
     */
    public void setOpen (Boolean aValue)
    {
    	open=aValue;
    }
    /**
     * 
     */
    public Boolean getOpen ()
    {
    	return (open);
    }
	/**
	 *
	 */
    public void reset ()
    {    	
    	values=new ArrayList<HoopXYMeasure> ();

    	open=true;    
    }
    /**
     * 
     */
    public int getValuesSize ()
    {
    	return (values.size());
    }
    /**
     * 
     */
	public ArrayList<HoopXYMeasure> getValues() 
	{
		return values;
	}
	/**
	 * 
	 */
	public void addValue (HoopXYMeasure aValue)
	{
		values.add(aValue);
	}
	/**
	 * 
	 */
	public HoopXYMeasure getValue (int anIndex)
	{
		return (values.get (anIndex));		
	}
}
