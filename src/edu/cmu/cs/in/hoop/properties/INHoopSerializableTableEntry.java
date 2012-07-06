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

import edu.cmu.cs.in.hoop.base.INHoopBase;
import edu.cmu.cs.in.hoop.properties.types.INHoopSerializable;

public class INHoopSerializableTableEntry extends INHoopBase
{	
	private INHoopSerializable entry=null;	
	private INHoopBase component=null;
	private String value="";
	
	/**
	 *
	 */
    public INHoopSerializableTableEntry (String aString) 
    {
    	setClassName ("INHoopSerializableTableEntry");
    	debug ("INHoopSerializableTableEntry ()");
    	
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
	public void setEntry(INHoopSerializable anEntry) 
	{
		entry=anEntry;
	}
	/**
	 *
	 */
	public INHoopSerializable getEntry() 
	{
		return entry;
	}	
	/**
	 *
	 */
	public void setComponent(INHoopBase component) 
	{
		this.component=component;
	}
	/**
	 *
	 */
	public INHoopBase getComponent() 
	{
		return component;
	}
}
