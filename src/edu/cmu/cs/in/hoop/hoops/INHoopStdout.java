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

import edu.cmu.cs.in.base.INHoopLink;
import edu.cmu.cs.in.base.INKV;
import edu.cmu.cs.in.hoop.INHoopDialogConsole;
import edu.cmu.cs.in.hoop.base.INHoopBase;
import edu.cmu.cs.in.hoop.base.INHoopInterface;

/**
* 
*/
public class INHoopStdout extends INHoopBase implements INHoopInterface
{    	
	private INHoopDialogConsole userIO=null;
	
	/**
	 *
	 */
    public INHoopStdout () 
    {
		setClassName ("INHoopStdout");
		debug ("INHoopStdout ()");
										
		setHoopCategory ("save");
		
		setHoopDescription ("Write to Standard Output");
    }
	/**
	 *
	 */
	public Boolean runHoop (INHoopBase inHoop)
	{		
		debug ("runHoop ()");
				
		ArrayList <INKV> inData=inHoop.getData();
		
		if (getInEditor()==true)
		{
			userIO=(INHoopDialogConsole) INHoopLink.getWindow("User Dialog");
			if (userIO==null)
			{
				INHoopLink.addView ("User Dialog",new INHoopDialogConsole (),INHoopLink.bottom);
				userIO=(INHoopDialogConsole) INHoopLink.getWindow("User Dialog");
			}			
			
			userIO.setOutHoop(this);
			
			for (int i=0;i<inData.size();i++)
			{
				INKV aKV=inData.get(i);
				
				userIO.processOutput (aKV.getKeyString()+" : " + aKV.getValue());					
			}			
		}
		else
		{
			for (int i=0;i<inData.size();i++)
			{
				INKV aKV=inData.get(i);
				
				System.out.println(aKV.getKeyString()+" : " + aKV.getValue());
			}
		}		
				
		return (true);
	}	
	/**
	 * 
	 */
	public INHoopBase copy ()
	{
		return (new INHoopStdout ());
	}		
}
