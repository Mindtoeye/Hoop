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

import edu.cmu.cs.in.base.INKV;
import edu.cmu.cs.in.base.INKVInteger;
import edu.cmu.cs.in.hoop.base.INHoopBase;
import edu.cmu.cs.in.hoop.base.INHoopTransformBase;

/**
* 
*/
public class INHoopKV2TXT extends INHoopTransformBase
{    	
	private StringBuffer textRepresentation=null;
	
	/**
	 *
	 */
    public INHoopKV2TXT () 
    {
		setClassName ("INHoopKV2TXT");
		debug ("INHoopKV2TXT ()");
											
		setHoopDescription ("Generate text from KV values");
		
		addOutPort ("TXT");		
    }
	/**
	 *
	 */
	public Boolean runHoop (INHoopBase inHoop)
	{		
		debug ("runHoop ()");
				
		textRepresentation=new StringBuffer ();
		
		ArrayList <INKV> inData=inHoop.getData();
		
		for (int i=0;i<inData.size();i++)
		{
			INKVInteger aKV=(INKVInteger) inData.get(i);
			
			textRepresentation.append(aKV.getKeyString()+" : " + aKV.getValue() + "\n");
		}		
				
		return (true);
	}	
	/**
	 * 
	 */
	public INHoopBase copy ()
	{
		return (new INHoopKV2TXT ());
	}		
}
