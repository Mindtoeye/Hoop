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

import edu.cmu.cs.in.base.kv.HoopKV;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.hoop.hoops.base.HoopInterface;
import edu.cmu.cs.in.hoop.hoops.base.HoopTransformBase;
import edu.cmu.cs.in.hoop.properties.types.HoopStringSerializable;

/**
* 
*/
public class HoopLabelKV extends HoopTransformBase implements HoopInterface
{    	
	//@SuppressWarnings("unused")
	private HoopStringSerializable keyLabel=null;
	//@SuppressWarnings("unused")
	private HoopStringSerializable valueLabel=null;
	
	/**
	 *
	 */
    public HoopLabelKV () 
    {
		setClassName ("HoopLabelKV");
		debug ("HoopLabelKV ()");
				
		setHoopDescription ("Assign a Label to Key and Value");		
		
		this.reKey.setEnabled(false);
		
		keyLabel=new HoopStringSerializable (this,"keyLabel","keyLabel");
		valueLabel=new HoopStringSerializable (this,"valueLabel","valueLabel");
    }
	/**
	 *
	 */
	public Boolean runHoop (HoopBase inHoop)
	{		
		debug ("runHoop ()");
		
		inHoop.copyTypes (this);
		
		setKVLabel (0,keyLabel.getPropValue());
		setKVLabel (1,valueLabel.getPropValue());
								
		ArrayList <HoopKV> inData=inHoop.getData();
		
		this.setData (inData);
		
		if (inData!=null)
		{
			/*
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
			*/			
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
		return (new HoopLabelKV ());
	}	
}
