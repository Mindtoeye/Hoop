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

package edu.cmu.cs.in.hoop.hoops.transform;

import java.util.ArrayList;
//import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import edu.cmu.cs.in.base.kv.HoopKV;
import edu.cmu.cs.in.base.kv.HoopKVInteger;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.hoop.hoops.base.HoopInterface;
import edu.cmu.cs.in.hoop.hoops.base.HoopTransformBase;

/**
* 
*/
public class HoopKeySort extends HoopTransformBase implements HoopInterface
{    				
	/**
	 *
	 */
    public HoopKeySort () 
    {
		setClassName ("HoopKeySort");
		debug ("HoopKeySort ()");
				
		setHoopDescription ("Sort the keys into a new order");		
    }
	/**
	 * http://mrtextminer.wordpress.com/2007/09/14/java-hashtable-sorted-by-values/
	 */
	public Boolean runHoop (HoopBase inHoop)
	{		
		debug ("runHoop ()");
								
		ArrayList <HoopKV> inData=inHoop.getData();
		if (inData!=null)
		{
			Hashtable<String,Integer> uniqueHash = new Hashtable<String,Integer>();
					
			for (int i=0;i<inData.size();i++)
			{
				HoopKVInteger aKV=(HoopKVInteger) inData.get(i);
				
				uniqueHash.put(aKV.getValue(),i);					
			}						
			
			Iterator<Map.Entry<String, Integer>> it = uniqueHash.entrySet().iterator();

			while (it.hasNext()) 
			{
			  Map.Entry<String, Integer> entry = it.next();

			  addKV (new HoopKVInteger (entry.getValue (),entry.getKey()));			  
			} 			
		}
		else
			return (false);		
				
		return (true);
	}	 
	/**
	 * 
	 */
	public HoopBase copy ()
	{
		return (new HoopKeySort ());
	}	
}
