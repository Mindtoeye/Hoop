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

package edu.cmu.cs.in.hoop.hoops.save;

import java.util.ArrayList;

import edu.cmu.cs.in.base.io.HoopFileManager;
import edu.cmu.cs.in.base.kv.HoopKV;
import edu.cmu.cs.in.base.kv.HoopKVInteger;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.hoop.hoops.base.HoopInterface;
import edu.cmu.cs.in.hoop.hoops.base.HoopSaveBase;
import edu.cmu.cs.in.hoop.properties.types.HoopStringSerializable;
import edu.cmu.cs.in.hoop.properties.types.HoopURISerializable;

/**
* 
*/
public class HoopCSVWriter extends HoopSaveBase implements HoopInterface
{    			
	private String mode="TAB"; // TAB,COMMA,DASH

	private HoopFileManager fManager=null;
	
	private HoopURISerializable URI=null;
	private HoopStringSerializable writeMode=null; // APPEND, OVERWRITE
	
	/**
	 *
	 */
    public HoopCSVWriter () 
    {
		setClassName ("HoopCSVWriter");
		debug ("HoopCSVWriter ()");
				
		setHoopDescription ("Save KVs in CSV format");
		
		fManager=new HoopFileManager ();
		
		URI=new HoopURISerializable (this,"URI","<PROJECTPATH>/HoopOut.csv");
		writeMode=new HoopStringSerializable (this,"writeMode","APPEND");
    }  
	/**
	 *
	 */
	public Boolean runHoop (HoopBase inHoop)
	{		
		debug ("runHoop ()");
		
		ArrayList <HoopKV> inData=inHoop.getData();
					
		if (inData!=null)
		{
			StringBuffer formatted=new StringBuffer ();
			
			for (int t=0;t<inData.size();t++)
			{
				HoopKV aKV=inData.get(t);
								
				formatted.append(aKV.getKeyString ());
				
				ArrayList<Object> vals=aKV.getValuesRaw();
				
				for (int i=0;i<vals.size();i++)
				{
					formatted.append(",");
					formatted.append(vals.get(i));
				}
				
				formatted.append("\n");
			}
			
			if (writeMode.getValue().equals("OVERWRITE")==true)
				fManager.saveContents (this.projectToFullPath(URI.getValue()),formatted.toString());
			else
				fManager.appendContents (this.projectToFullPath(URI.getValue()),formatted.toString());
		}	
						
		return (true);
	}	
	/**
	 * 
	 */
	public HoopBase copy ()
	{
		return (new HoopCSVWriter ());
	}	
}
