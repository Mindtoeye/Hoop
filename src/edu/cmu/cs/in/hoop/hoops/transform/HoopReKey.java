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

import edu.cmu.cs.in.base.HoopStringTools;
import edu.cmu.cs.in.base.kv.HoopKV;
import edu.cmu.cs.in.base.kv.HoopKVInteger;
import edu.cmu.cs.in.base.kv.HoopKVString;
import edu.cmu.cs.in.base.kv.HoopKVTools;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.hoop.hoops.base.HoopInterface;
import edu.cmu.cs.in.hoop.hoops.base.HoopTransformBase;
import edu.cmu.cs.in.hoop.properties.types.HoopIntegerSerializable;

/**
* 
*/
public class HoopReKey extends HoopTransformBase implements HoopInterface
{    		
	private static final long serialVersionUID = -2696793271992691781L;
	
	protected HoopIntegerSerializable keyIndex=null;
	
	/**
	 *
	 */
    public HoopReKey () 
    {
		setClassName ("HoopReKey");
		debug ("HoopReKey ()");
				
		setHoopDescription ("Pick a value to be the key");
		
		keyIndex=new HoopIntegerSerializable (this,"keyIndex",1);
		
		removeProperty ("reKey");
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
			this.setErrorString("No data found in previous hoop");
			return (false);
		}
		
		Integer targetIndex=keyIndex.getPropValue();
		
		if (targetIndex<0)
		{
			this.setErrorString("Please enter an index larger than 0");
			return (false);
		}
		
		debug ("Using index " + targetIndex + " as new index");
							
		for (int i=0;i<inData.size();i++)
		{				
			HoopKV aKV=inData.get(i);

			String newKey=aKV.getValueAsString(targetIndex);
				
			debug ("New Key: " + newKey);
			
			HoopKV newKV=createKV (aKV);
			newKV.setKeyString (newKey);
			newKV.toss(targetIndex);
			//newKV.setValue(aKV.getValue(),0);
			
			addKV (newKV);								
		}
		
		return (true);
	}	 
	/**
	 * 
	 */
	public HoopBase copy ()
	{
		return (new HoopReKey ());
	}	
}
