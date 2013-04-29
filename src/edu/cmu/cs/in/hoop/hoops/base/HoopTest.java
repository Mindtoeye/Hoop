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

import java.util.ArrayList;

import edu.cmu.cs.in.base.kv.HoopKV;

/**
* 
*/
public class HoopTest extends HoopBase implements HoopInterface
{    						
	private static final long serialVersionUID = -487594649843050665L;
	
	private int waitTime=2000; // 2 seconds
	
	/**
	 *
	 */
    public HoopTest () 
    {
		setClassName ("HoopTest");
		debug ("HoopTest ()");
		
		setHoopCategory ("Test");		
		setHoopDescription ("Sleep for N milliseconds");		
    }  
	/**
	 *
	 */
	public Boolean runHoop (HoopBase inHoop)
	{		
		debug ("runHoop ()");

		ArrayList <HoopKV> inData=inHoop.getData();
		
		if (inData==null)
		{
			this.setErrorString("Error: no data available");
			return (false);
		}
		
		// All we need here is a dumb passthrough
		
		this.setData(inData);
							
		try 
		{
			debug ("Sleeping for " + waitTime + " milliseconds ...");
			Thread.sleep(waitTime);
		} 
		catch (InterruptedException e) 
		{		
			e.printStackTrace();
		}
		
		return (true);
	}
	/**
	 * 
	 */
	public HoopBase copy ()
	{
		return (new HoopTest ());
	}	
}
