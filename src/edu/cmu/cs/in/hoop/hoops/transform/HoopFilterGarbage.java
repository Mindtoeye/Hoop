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
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.hoop.hoops.base.HoopInterface;
import edu.cmu.cs.in.hoop.hoops.base.HoopTransformBase;
import edu.cmu.cs.in.hoop.properties.types.HoopBooleanSerializable;
import edu.cmu.cs.in.hoop.properties.types.HoopStringSerializable;

/**
* 
*/
public class HoopFilterGarbage extends HoopTransformBase implements HoopInterface
{    				
	private static final long serialVersionUID = -3087945592273966950L;
	
	public HoopStringSerializable contains = null;
	public HoopBooleanSerializable removeNumeric=null;
	public HoopBooleanSerializable removeBlank=null;
	
	/**
	 *
	 */
    public HoopFilterGarbage () 
    {
		setClassName ("HoopFilterGarbage");
		debug ("HoopFilterGarbage ()");
				
		setHoopDescription ("Remove any garbage terms from input KVs");		
		
		contains=new HoopStringSerializable (this,"contains","http://,file://");
		removeNumeric=new HoopBooleanSerializable (this,"removeNumeric",true);
		removeBlank=new HoopBooleanSerializable (this,"removeBlank",true);
    }
	/**
	 *
	 */
	public Boolean runHoop (HoopBase inHoop)
	{		
		debug ("runHoop ()");
		
		String compiled=contains.getPropValue();
		
		ArrayList <String> garbage=HoopStringTools.splitComma (compiled);
				
		ArrayList <HoopKV> inData=inHoop.getData();
		if (inData!=null)
		{					
			for (int i=0;i<inData.size();i++)
			{
				HoopKVInteger aKV=(HoopKVInteger) inData.get(i);
								
				Boolean isGarbage=false;
				
				for (int t=0;t<garbage.size();t++)
				{
					String test=garbage.get(t);
					
					if (aKV.getValue().indexOf(test)!=-1)
						isGarbage=true;
				}	
				
				if (
					(HoopStringTools.isInteger(aKV.getValue())==true) ||
					(HoopStringTools.isLong(aKV.getValue())==true)
					)
				{
					isGarbage=true;
				}
				
				if (aKV.getValue().isEmpty()==true)
					isGarbage=true;
								
				if (isGarbage==false)
				{
					addKV (new HoopKVInteger (i,aKV.getValue()));
				}	
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
		return (new HoopFilterGarbage ());
	}	
}
