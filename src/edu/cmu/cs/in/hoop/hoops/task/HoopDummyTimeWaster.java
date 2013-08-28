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

package edu.cmu.cs.in.hoop.hoops.task;

import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.hoop.hoops.base.HoopControlBase;
import edu.cmu.cs.in.hoop.hoops.base.HoopInterface;
import edu.cmu.cs.in.hoop.properties.types.HoopIntegerSerializable;

/**
* 
*/
public class HoopDummyTimeWaster extends HoopControlBase implements HoopInterface
{    						
	private static final long serialVersionUID = -47342639836020148L;
	
	public HoopIntegerSerializable waste=null;
	
	/**
	 *
	 */
    public HoopDummyTimeWaster () 
    {
		setClassName ("HoopDummyTimeWaster");
		debug ("HoopDummyTimeWaster ()");
										
		setHoopDescription ("Gremlin hoop that wastes time");	
		
		waste=new HoopIntegerSerializable (this,"waste",5); // 5 seconds default
    }
	/**
	 *
	 */
	public Boolean runHoop (HoopBase inHoop)
	{		
		debug ("runHoop ()");
		
		super.runHoop(inHoop); // This will do the right thing with the data
				
		try 
		{
			Thread.sleep(waste.getPropValue());
		} 
		catch (InterruptedException e) 
		{		
			e.printStackTrace();
			return (false);
		}
		
		return (true);
	}	
	/**
	 * 
	 */
	public HoopBase copy ()
	{
		return (new HoopDummyTimeWaster ());
	}
}
