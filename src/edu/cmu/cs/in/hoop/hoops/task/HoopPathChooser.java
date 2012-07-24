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
import edu.cmu.cs.in.hoop.properties.types.HoopStringSerializable;

/**
* 
*/
public class HoopPathChooser extends HoopControlBase implements HoopInterface
{    	
	private HoopStringSerializable activePath=null;
	
	/**
	 *
	 */
    public HoopPathChooser () 
    {
		setClassName ("HoopPathChooser");
		debug ("HoopPathChooser ()");
										
		setHoopDescription ("Setup and Control Paths");
		
		activePath=new HoopStringSerializable (this,"activePath","");
    }
	/**
	 *
	 */
	public Boolean runHoop (HoopBase inHoop)
	{		
		debug ("runHoop ()");
		
		if (activePath.getValue().isEmpty()==true)
		{
			debug ("No path chosen");
			return (true);
		}
		
		for (int i=0;i<outHoops.size();i++)
		{
			HoopBase aHoop=outHoops.get(i);
			
			aHoop.setActive(false);
		}
		
		for (int j=0;j<outHoops.size();j++)
		{
			HoopBase aHoop=outHoops.get(j);
			
			if (aHoop.getHoopID().equals(activePath.getValue())==true)
			{
				aHoop.setActive(true);
			}
			
			aHoop.setActive(true);
		}		
						
		return (true);
	}	
	/**
	 * 
	 */
	public HoopBase copy ()
	{
		return (new HoopPathChooser ());
	}	
}
