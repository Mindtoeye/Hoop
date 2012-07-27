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

package edu.cmu.cs.in.hoop.hoops.load;

import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.hoop.hoops.base.HoopFileLoadBase;

public class HoopBerkeleyDBReader extends HoopFileLoadBase
{
	/**
	 *
	 */ 
	public HoopBerkeleyDBReader () 
	{		
		setClassName ("HoopBerkeleyDBReader");
		debug ("HoopBerkeleyDBReader ()");
		
		setHoopDescription ("Load KVs from a Berkeley DB");

		removeInPort ("KV");
	}
	/**
	 *
	 */
	public Boolean runHoop (HoopBase inHoop)
	{		
		debug ("runHoop ()");
					
		/*
		textRepresentation=new StringBuffer ();
		
		ArrayList <HoopKV> inData=inHoop.getData();
		
		for (int i=0;i<inData.size();i++)
		{
			HoopKVInteger aKV=(HoopKVInteger) inData.get(i);
			
			textRepresentation.append(aKV.getKeyString()+" : " + aKV.getValue() + "\n");
		}
		*/		
				
		return (true);
	}	
	/**
	 * 
	 */
	public HoopBase copy ()
	{
		return (new HoopBerkeleyDBReader ());
	}	
}
