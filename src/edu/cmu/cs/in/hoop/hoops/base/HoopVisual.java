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
import edu.cmu.cs.in.hoop.properties.HoopVisualProperties;

/**
* To separate any non-hoop related objects from the core hoop classes we
* created a new class that stores any visual properties and attributes of
* a hoop. This way when we run a hoop graph on a cluster we don't create
* massive amounts of objects that are not relevant. Please see the
* class HoopVisualProperties class for the place where we actually store
* all visual properties.
*/
public class HoopVisual extends HoopRoot
{	
	private HoopVisualProperties vizProps=null;
	
	/**
	 *
	 */
    public HoopVisual () 
    {
		setClassName ("HoopVisual");
		debug ("HoopVisual ()");
						
		vizProps=new HoopVisualProperties ();
    }    
    /**
     * 
     */
    public HoopVisualProperties getVisualProperties ()
    {
    	return (vizProps);
    }
}
