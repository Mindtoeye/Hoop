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
import edu.cmu.cs.in.base.kv.HoopKVInteger;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.hoop.hoops.base.HoopTransformBase;

/**
* 
*/
public class HoopKV2TXT extends HoopTransformBase
{    	
	private StringBuffer textRepresentation=null;
	
	/**
	 *
	 */
    public HoopKV2TXT () 
    {
		setClassName ("HoopKV2TXT");
		debug ("HoopKV2TXT ()");
											
		setHoopDescription ("Generate text from KV values");
		
		addOutPort ("TXT");		
    }
	/**
	 *
	 */
	public Boolean runHoop (HoopBase inHoop)
	{		
		debug ("runHoop ()");
				
		textRepresentation=new StringBuffer ();
		
		ArrayList <HoopKV> inData=inHoop.getData();
		
		for (int i=0;i<inData.size();i++)
		{
			HoopKVInteger aKV=(HoopKVInteger) inData.get(i);
			
			textRepresentation.append(aKV.getKeyString()+" : " + aKV.getValue() + "\n");
			
			updateProgressStatus (i,inData.size());
		}		
				
		return (true);
	}	
	/**
	 * 
	 */
	public HoopBase copy ()
	{
		return (new HoopKV2TXT ());
	}		
}
