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
import edu.cmu.cs.in.base.kv.HoopKVTools;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.hoop.hoops.base.HoopInterface;
import edu.cmu.cs.in.hoop.hoops.base.HoopTransformBase;
import edu.cmu.cs.in.hoop.properties.types.HoopEnumSerializable;

/**
* 
*/
public class HoopTokenCaseChange extends HoopTransformBase implements HoopInterface
{    					
	private static final long serialVersionUID = -8331508015259336062L;
	private HoopEnumSerializable changeMode=null; // NONE,TOLOWER,TOUPPER
	
	/**
	 *
	 */
    public HoopTokenCaseChange () 
    {
		setClassName ("HoopTokenCaseChange");
		debug ("HoopTokenCaseChange ()");
										
		setHoopDescription ("Change tokens to upper or lower case");
		
		changeMode=new HoopEnumSerializable (this,"changeMode","NONE,TOLOWER,TOUPPER");
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
			for (int i=0;i<inData.size();i++)
			{
				HoopKV aKV=inData.get(i);
				
				Integer keyString=i;
				
				HoopKV newKV=HoopKVTools.getLikeKVType(aKV);				
				newKV.setKeyString (keyString.toString());
												
				if (changeMode.getValue().equals("TOLOWER")==true)
				{					
					ArrayList <Object> tokenChanger=aKV.getValuesRaw();
					
					for (int j=0;j<tokenChanger.size();j++)
					{
						String changer=(String) tokenChanger.get(j);
						
						newKV.setValue(changer.toLowerCase(),j);
					}
				}
				
				if (changeMode.getValue().equals("TOUPPER")==true)
				{					
					ArrayList <Object> tokenChanger=aKV.getValuesRaw();
					
					for (int j=0;j<tokenChanger.size();j++)
					{
						String changer=(String) tokenChanger.get(j);
						
						newKV.setValue(changer.toUpperCase(),j);
					}					
				}
				
				addKV (newKV);
				
				updateProgressStatus (i,inData.size());
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
		return (new HoopTokenCaseChange ());
	}	
}
