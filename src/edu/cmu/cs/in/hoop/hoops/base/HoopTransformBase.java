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

import edu.cmu.cs.in.base.kv.HoopKV;
import edu.cmu.cs.in.base.kv.HoopKVTools;
import edu.cmu.cs.in.hoop.properties.types.HoopBooleanSerializable;
import edu.cmu.cs.in.hoop.properties.types.HoopEnumSerializable;

/**
* 
*/
public class HoopTransformBase extends HoopBase implements HoopInterface
{    				
	private static final long serialVersionUID = -2512183248281416755L;
	public HoopBooleanSerializable reKey=null;
	public HoopEnumSerializable changeKeyType=null;
	
	/**
	 *
	 */
    public HoopTransformBase () 
    {
		setClassName ("HoopTransformBase");
		debug ("HoopTransformBase ()");
		
		setHoopCategory ("Transform");		
		setHoopDescription ("Abstract Hoop Transformer");
		
		reKey=new HoopBooleanSerializable (this,"reKey",false);
		changeKeyType=new HoopEnumSerializable (this,"changeKeyType","keep the same,CLASS,INT,LONG,STRING,FLOAT,BOOLEAN,ENUM,TABLE,DOCUMENT");
    }
	/**
	 *
	 */
	public Boolean runHoop (HoopBase inHoop)
	{		
		debug ("runHoop ()");
		
				
		return (true);
	}
	/**
	 * 
	 */
	protected HoopKV createKV (HoopKV aKV)
	{
		//debug ("createKV ()");
		
		HoopKV newKV=null;
		
		if ((changeKeyType.getPropValue().equals("keep the same")==true) ||
			(changeKeyType.getPropValue().equals("---")==true)
			)
		{
			//debug ("Creating like HoopKV with type: " + aKV.typeToString ());
			
			newKV=HoopKVTools.getLikeKVType(aKV);
		}
		else
		{
			//debug ("Mapping from: " + aKV.typeToString () + " to: " + changeKeyType.getPropValue());
			
			newKV=HoopKVTools.createFromType (changeKeyType.getPropValue());
			newKV.setValuesRaw(HoopKVTools.copyValues(aKV));
		}
						
		return (newKV);
	}
	/**
	 * 
	 */
	public HoopBase copy ()
	{
		return (new HoopTransformBase ());
	}	
}
