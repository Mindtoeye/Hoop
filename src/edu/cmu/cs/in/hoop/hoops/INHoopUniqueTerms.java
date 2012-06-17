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

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import edu.cmu.cs.in.base.INKV;
import edu.cmu.cs.in.hoop.base.INHoopBase;
import edu.cmu.cs.in.hoop.base.INHoopInterface;
import edu.cmu.cs.in.hoop.base.INHoopTransformBase;

/**
* 
*/
public class INHoopUniqueTerms extends INHoopTransformBase implements INHoopInterface
{    				
	/**
	 *
	 */
    public INHoopUniqueTerms () 
    {
		setClassName ("INHoopUniqueTerms");
		debug ("INHoopUniqueTerms ()");
				
		setHoopDescription ("Transform into unique list of KVs");		
    }
	/**
	 *
	 */
	public Boolean runHoop (INHoopBase inHoop)
	{		
		debug ("runHoop ()");
		
		super.runHoop(inHoop);		
						
		ArrayList <INKV> inData=inHoop.getData();
		if (inData!=null)
		{
			Hashtable<String,Integer> uniqueHash = new Hashtable<String,Integer>();
					
			for (int i=0;i<inData.size();i++)
			{
				INKV aKV=inData.get(i);
				
				uniqueHash.put(aKV.getValue(),i);					
			}						
			
			Iterator<Map.Entry<String, Integer>> it = uniqueHash.entrySet().iterator();

			while (it.hasNext()) 
			{
			  Map.Entry<String, Integer> entry = it.next();

			  addKV (new INKV (entry.getValue (),entry.getKey()));			  
			} 			
		}
		else
			return (false);		
				
		return (true);
	}	 
	/**
	 * 
	 */
	public INHoopBase copy ()
	{
		return (new INHoopUniqueTerms ());
	}	
}
