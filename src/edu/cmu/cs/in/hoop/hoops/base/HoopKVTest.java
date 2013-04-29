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

import java.util.UUID;

import edu.cmu.cs.in.base.HoopDataType;
import edu.cmu.cs.in.base.kv.HoopKVString;
import edu.cmu.cs.in.hoop.properties.types.HoopEnumSerializable;
import edu.cmu.cs.in.hoop.properties.types.HoopIntegerSerializable;

/**
* 
*/
public class HoopKVTest extends HoopBase implements HoopInterface
{    				
	private static final long serialVersionUID = 7935432284064785337L;
	
	protected HoopIntegerSerializable dataSize=null;	
	protected HoopEnumSerializable KVType=null;
	
	/**
	 *
	 */
    public HoopKVTest () 
    {
		setClassName ("HoopKVTest");
		debug ("HoopKVTest ()");
		
		setHoopCategory ("Test");		
		setHoopDescription ("Generate any number of KVs");
		
		removeInPort ("KV");
		
		dataSize=new HoopIntegerSerializable (this,"dataSize",100);
		KVType=new HoopEnumSerializable (this,"KVType",HoopDataType.typesToString ());
    }   
	/**
	 *
	 */
	public Boolean runHoop (HoopBase inHoop)
	{		
		debug ("runHoop ()");

		int totalSize=dataSize.getPropValue();
		
		if (totalSize<=0)
		{
			this.setErrorString("Error: please enter a data size larger than 0");
			return (false);
		}
		
		for (int i=0;i<totalSize;i++)
		{
			HoopKVString aKV=new HoopKVString ();
			
			aKV.setKey(UUID.randomUUID().toString());
			aKV.setValue(UUID.randomUUID().toString());
			
			updateProgressStatus (i,totalSize);
			
			addKV (aKV);
		}
		
		updateProgressStatus (this.getData().size(),this.getData().size());

		return (true);
	}
	/**
	 * 
	 */
	public HoopBase copy ()
	{
		return (new HoopKVTest ());
	}	
}
