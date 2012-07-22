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
	public HoopIntegerSerializable (HoopPropertyContainer aParent,String aName,Boolean aPropValue) 
	{
		super (aParent,aName);
		setClassName ("HoopIntegerSerializable");
		debug ("HoopIntegerSerializable ()");
   	
		this.setPropValue(propValue);
	}
	/**
	 *
	 */
	public HoopIntegerSerializable (HoopPropertyContainer aParent,String aName) 
	{
		super (aParent,aName);
		setClassName ("HoopIntegerSerializable");
		debug ("HoopIntegerSerializable ()");
	}	
    /** 
     * @param propValue
     */
	public void setPropValue(Integer propValue) 
	{
		this.propValue = propValue;
		this.setValue(propValue.toString ());
	}
	/** 
	 * @return
	 */
	public Integer getPropValue() 
	{
		return propValue;
	}
}
