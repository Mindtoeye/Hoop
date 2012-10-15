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

*/
public class HoopBooleanSerializable extends HoopSerializable
{				
	private Boolean propValue=true;
	
	/**
	 *
	 */
	public HoopBooleanSerializable (HoopPropertyContainer aParent,String aName,Boolean aPropValue) 
	{
		super (aParent,aName);
		
		setType (HoopDataType.BOOLEAN);
		setClassName ("HoopBooleanSerializable");
		debug ("HoopBooleanSerializable ()");
   	
		this.setPropValue(aPropValue);
	}	
	/**
	 *
	 */
	public HoopBooleanSerializable (HoopPropertyContainer aParent,String aName) 
	{
		super (aParent,aName);
		
		setType (HoopDataType.BOOLEAN);
		setClassName ("HoopBooleanSerializable");
		debug ("HoopBooleanSerializable ()");  	
	}	
    /** 
     * @param propValue
     */
	public void setPropValue(Boolean propValue) 
	{
		this.propValue = propValue;
		
		if (propValue==true)
			value="TRUE";
		else
			value="FALSE";
		
		//this.setValue(propValue.toString());
	}
	/**
	 *
	 */
	public void setValue (String aValue)
	{
		//value=aValue;
		
		super.setValue(aValue);
		
		if (aValue.equalsIgnoreCase("true")==true)
			this.propValue = true;
		else
			this.propValue = false;
	}	
	/**
	 * 
	 * @return
	 */
	public Boolean getPropValue() 
	{
		return propValue;
	}
}
