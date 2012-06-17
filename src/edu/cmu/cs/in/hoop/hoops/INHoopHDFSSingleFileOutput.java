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

package edu.cmu.cs.in.hoop.hoops;

import edu.cmu.cs.in.hoop.base.INHoopBase;
import edu.cmu.cs.in.hoop.base.INHoopFileLoadBase;
import edu.cmu.cs.in.hoop.base.INHoopInterface;

/**
* 
*/
public class INHoopHDFSSingleFileOutput extends INHoopFileLoadBase implements INHoopInterface
{    					
	/**
	 *
	 */
    public INHoopHDFSSingleFileOutput () 
    {
		setClassName ("INHoopHDFSSingleFileOutput");
		debug ("INHoopHDFSSingleFileOutput ()");
										
		setHoopDescription ("HDFS Single File Write");
    }
	/**
	 *
	 */
	public Boolean runHoop (INHoopBase inHoop)
	{		
		debug ("runHoop ()");
		
		super.runHoop(inHoop);		
				
		return (true);
	}	
	/**
	 * 
	 */
	public INHoopBase copy ()
	{
		return (new INHoopHDFSSingleFileOutput ());
	}	
}
