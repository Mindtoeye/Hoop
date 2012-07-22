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

package edu.cmu.cs.in.hoop.properties;

import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.hoop.properties.types.HoopSerializable;

public class HoopSerializableTableEntry extends HoopBase
{	
	private HoopSerializable entry=null;	
	private HoopBase component=null;
	private String value="";
	
	/**
	 *
	 */
    public HoopSerializableTableEntry (String aString) 
    {
    	setClassName ("HoopSerializableTableEntry");
    	debug ("HoopSerializableTableEntry ()");
    	
    	value=aString;
    }
	/**
	 *
	 */    
    public String toString ()
    {
    	if (entry!=null)
    		return (entry.toString());
    	
    	return (value);
    }
	/**
	 *
	 */    
    public void fromString (String aString)
    {
    	value=aString;
    }
	/**
	 *
	 */
	public void setEntry(HoopSerializable anEntry) 
	{
		entry=anEntry;
	}
	/**
	 *
	 */
	public HoopSerializable getEntry() 
	{
		return entry;
	}	
	/**
	 *
	 */
	public void setComponent(HoopBase component) 
	{
		this.component=component;
	}
	/**
	 *
	 */
	public HoopBase getComponent() 
	{
		return component;
	}
}
