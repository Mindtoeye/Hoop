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

//import edu.cmu.cs.in.base.INBase;

/**
*
*/
public class INKV //extends INBase
{    			
	private int key=-1;
	private String keyString="";
	private String value="";
			
	/**
	 *
	 */
    public INKV () 
    {
		//setClassName ("INKV");
		//debug ("INKV ()");						
    }
	/**
	 *
	 */
	public int getKey() 
	{
		return key;
	}
	/**
	 *
	 */
	public void setKey(int key) 
	{
		this.key = key;
	}
	/**
	 *
	 */
	public String getValue() 
	{
		return value;
	}
	/**
	 *
	 */
	public void setValue(String value) 
	{
		this.value = value;
	}
	/**
	 *
	 */	
	public String getKeyString() 
	{
		return keyString;
	}
	/**
	 *
	 */	
	public void setKeyString(String keyString) 
	{
		this.keyString = keyString;
	}  
}
