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

package edu.cmu.cs.in.hoop;

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.base.HoopRoot;

public class HoopMainCommandProcessor extends HoopRoot implements HoopCommandProcessor
{		
	/**
	 * 
	 */
	public HoopMainCommandProcessor ()
	{
		setClassName ("HoopMainCommandProcessor");
		debug ("HoopMainCommandProcessor ()");		
	}
	/**
	 *
	 */
	public void processCommand (String aCommand)
	{
		debug ("processCommand ("+aCommand+")");
		
		if (aCommand.equalsIgnoreCase("startEditing")==true)
		{
			HoopLink.preferences.putBoolean("welcome",false);
			
			HoopMainFrame mFrame=(HoopMainFrame) HoopLink.mainFrame;
			
			HoopLink.removeWindow("Welcome");
			
			if (mFrame!=null)				
				mFrame.startEditor ();			
		}
	}
}
