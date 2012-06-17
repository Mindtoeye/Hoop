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
* We should deliberately not derive from INBase since that comes with a
* large memory footprint.
*/
public class INKV
{    			
	private int key=-1;
	private String keyString="";
	private String value="";
			
	/**
	 *
	 */
    public INKV () 
    {
				
    }
	/**
	 *
	 */
    public INKV (int aKey,String aValue) 
    {	   
    	setKey (aKey);
    	setValue (aValue);
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
		if (keyString.isEmpty()==true)
		{
			Integer transformer=key;
			return (transformer.toString());
		}
		
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
