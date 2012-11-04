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

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.base.kv.HoopKV;
import edu.cmu.cs.in.base.kv.HoopKVInteger;
import edu.cmu.cs.in.hoop.HoopDialogConsole;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.hoop.hoops.base.HoopInterface;
import edu.cmu.cs.in.hoop.hoops.base.HoopSaveBase;

/**
* 
*/
public class HoopStdout extends HoopSaveBase implements HoopInterface
{    	
	private static final long serialVersionUID = 1119349970126294724L;
	private HoopDialogConsole userIO=null;
	
	/**
	 *
	 */
    public HoopStdout () 
    {
		setClassName ("HoopStdout");
		debug ("HoopStdout ()");
														
		setHoopDescription ("Write to Standard Output");
    }
	/**
	 *
	 */
	public Boolean runHoop (HoopBase inHoop)
	{		
		debug ("runHoop ()");
				
		ArrayList <HoopKV> inData=inHoop.getData();
		
		if (getInEditor()==true)
		{
			userIO=(HoopDialogConsole) HoopLink.getWindow("User Dialog");
			if (userIO==null)
			{
				HoopLink.addView ("User Dialog",new HoopDialogConsole (),HoopLink.bottom);
				userIO=(HoopDialogConsole) HoopLink.getWindow("User Dialog");
			}
			
			userIO.setOutHoop(this);
			
			StringBuffer formatted=new StringBuffer ();
			
			for (int i=0;i<inData.size();i++)
			{
				HoopKVInteger aKV=(HoopKVInteger) inData.get(i);
				
				formatted.append (aKV.getKeyString()+" : " + aKV.getValue()+"\n");					
			}			
			
			userIO.processOutput (formatted.toString());
		}
		else
		{
			for (int i=0;i<inData.size();i++)
			{
				HoopKVInteger aKV=(HoopKVInteger) inData.get(i);
				
				System.out.println(aKV.getKeyString()+" : " + aKV.getValue());
			}
		}		
				
		return (true);
	}	
	/**
	 * 
	 */
	public HoopBase copy ()
	{
		return (new HoopStdout ());
	}		
}
