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

package edu.cmu.cs.in.base;

/** 
 * @author Martin van Velsen
 */
public class INHoopXMLPropertyAttr extends INBase
{
	public String name ="undefined";
	public String value="undefined";
  
	/**
	 *  
	 */
	public INHoopXMLPropertyAttr () 
	{ 
		setClassName ("INHoopXMLPropertyAttr");
		//debug ("INHoopXMLPropertyAttr ()");
	}
	/**
	 *  
	 */
	public void setName (String a_name)
	{
		name=a_name;
	}
	/**
	 *  
	 */
	public String getName ()
	{
		return (name);
	}
	/**
	 *  
	 */
	public void setValue (String a_value)
	{
		value=a_value;
	}
	/**
	 *  
	 */
	public String getValue ()
	{
		return (value);
	}
	/**
	 *  
	 */
	public void fromString (String a_string)
	{
		//debug ("fromString ()");
  
	}
	/**
	 *  
	 */
	public String toString ()
	{
		//debug ("toString ()");
  
		return (name+"=\""+value+"\"");
	} 
}