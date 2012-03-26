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

public class INToken //extends INBase
{    
	private String value;
	private Integer position;
	
	/**
	 *
	 */
    public INToken () 
    {
		//setClassName ("INToken");
		//debug ("INToken ()");						
    }
	/**
	 *
	 */
   public INToken (String aValue) 
   {
		//setClassName ("INToken");
		//debug ("INToken ()");	
	   
	   setValue (aValue);
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
		
		if (value.indexOf(':')!=-1)
		{		
			String [] temp=value.split(":");
		
			if (temp.length>1)
			{
				this.value=temp [0];
				position=Integer.parseInt(temp [1]);
			}
		}	
	}
	/**
	 *
	 */
	public Integer getPosition() 
	{
		return position;
	}	
	/**
	 *
	 */
	public void setPosition(Integer position) 
	{
		this.position = position;
	}  
}
