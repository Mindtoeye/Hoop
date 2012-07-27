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
public class HoopEnumSerializable extends HoopSerializable
{		
	/**
	 *
	 */
	public HoopEnumSerializable (HoopPropertyContainer aParent,String aName,String aPropValue) 
	{
		super (aParent,aName);
		
		setType (HoopDataType.ENUM);
		setClassName ("HoopEnumSerializable");
		debug ("HoopEnumSerializable ()");
  	
		setValue (aPropValue);
	}	
	/**
	 *
	 */
    public HoopEnumSerializable (HoopPropertyContainer aParent,String aName) 
    {
    	super (aParent,aName);
    	
    	setType (HoopDataType.ENUM);
    	setClassName ("HoopStringSerializable");
    	debug ("HoopStringSerializable ()");
    	
    }
    /** 
     * @param propValue
     */
	public void setPropValue(String [] propValue) 
	{
		StringBuffer formatter=new StringBuffer ();
		
		for (int i=0;i<propValue.length;i++)
		{
			if (i==0)
				formatter.append(" , ");
			
			formatter.append(propValue [i]);
		}
		
		this.setValue(formatter.toString());
	}    
    /** 
     * @param propValue
     */
	public void setPropValue(String propValue) 
	{
		this.setValue(propValue);
	}
	/** 
	 * @return
	 */
	public String getPropValue() 
	{
		return (getValue ());
	}    
}
