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

import edu.cmu.cs.in.hoop.base.INHoopPropertyContainer;

/**

*/
public class INHoopURISerializable extends INHoopSerializable
{
	/**
	 *
	 */
	public INHoopURISerializable (INHoopPropertyContainer aParent,String aName,String aPropValue) 
	{
		super (aParent,aName);
		setClassName ("INHoopURISerializable");
		debug ("INHoopURISerializable ()");
  	
		this.setValue(aPropValue);
	}	
	/**
	 *
	 */
	public INHoopURISerializable (INHoopPropertyContainer aParent,String aName) 
	{
		super (aParent,aName);
		setClassName ("INHoopURISerializable");
		debug ("INHoopURISerializable ()");
   	
	}	
	/**
	 *
	 */
    public INHoopURISerializable (INHoopPropertyContainer aParent) 
    {
    	super (aParent);
    	setClassName ("INHoopURISerializable");
    	debug ("INHoopURISerializable ()");
    	
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
