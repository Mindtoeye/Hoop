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

package edu.cmu.cs.in.hoop.hoops.base;

import edu.cmu.cs.in.base.HoopRoot;

/**
* We use the instance name to identify the edge
*/
public class HoopConnection extends HoopRoot
{
	private String fromHoopID=""; 
	private String toHoopID="";
	
	private HoopBase fromHoop=null;
	private HoopBase toHoop=null;
	
	/**
	 *
	 */
    public HoopConnection () 
    {
		setClassName ("HoopConnection");
		debug ("HoopConnection ()");
						
    }
	/**
	 *
	 */
	public String getFromHoopID() 
	{
		return fromHoopID;
	}
	/**
	 *
	 */
	public void setFromHoopID(String fromHoopID) 
	{
		this.fromHoopID = fromHoopID;
	}
	/**
	 *
	 */
	public String getToHoopID() 
	{
		return toHoopID;
	}
	/**
	 *
	 */
	public void setToHoopID(String toHoopID) 
	{
		this.toHoopID = toHoopID;
	}
	/**
	 *
	 */	
	public HoopBase getFromHoop() 
	{
		return fromHoop;
	}
	/**
	 *
	 */	
	public void setFromHoop(HoopBase aFromHoop) 
	{
		this.fromHoop = aFromHoop;
		this.fromHoopID=fromHoop.getHoopID();
	}
	/**
	 *
	 */	
	public HoopBase getToHoop() 
	{
		return toHoop;
	}
	/**
	 *
	 */	
	public void setToHoop(HoopBase toHoop) 
	{
		this.toHoop = toHoop;
		this.toHoopID=toHoop.getHoopID();
	}    
}
