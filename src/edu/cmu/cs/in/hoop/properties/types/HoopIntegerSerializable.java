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

package edu.cmu.cs.in.hoop.properties.types;

import edu.cmu.cs.in.base.HoopDataType;
import edu.cmu.cs.in.hoop.hoops.base.HoopPropertyContainer;

/**
*
*/
public class HoopIntegerSerializable extends HoopSerializable
{		
	private Integer propValue=0;
	
	/**
	 *
	 */
	public HoopIntegerSerializable (HoopPropertyContainer aParent,String aName,Integer aPropValue) 
	{
		super (aParent,aName);
		
		setDataType (HoopDataType.INT);
		setClassName ("HoopIntegerSerializable");
		
		debug ("HoopIntegerSerializable (HoopPropertyContainer aParent,String aName,Integer aPropValue)");
   	
		this.setPropValue(aPropValue);
	}
	/**
	 *
	 */
	public HoopIntegerSerializable (HoopPropertyContainer aParent,String aName) 
	{
		super (aParent,aName);
		
		setDataType (HoopDataType.INT);
		setClassName ("HoopIntegerSerializable");
		
		debug ("HoopIntegerSerializable (HoopPropertyContainer aParent,String aNam)");
	}	
	/**
	 * 
	 */
	public void setValue (String aValue)
	{
		super.setValue(aValue);
		
		this.propValue=Integer.parseInt(aValue);
	}
	/**
	 *
	 */
	public String getValue ()
	{
		return (propValue.toString());
	} 	
    /** 
     * @param propValue
     */
	public void setPropValue(Integer aPropValue) 
	{		
		debug ("setPropValue ("+propValue+")");

		super.setValue (propValue.toString ());
		
		this.propValue=aPropValue;
		
		debug ("Check: " + this.getValue());
	}
	/** 
	 * @return
	 */
	public Integer getPropValue() 
	{
		return propValue;
	}
}
