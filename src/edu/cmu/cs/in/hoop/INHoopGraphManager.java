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

import java.util.ArrayList;

import edu.cmu.cs.in.base.INHoopLink;
import edu.cmu.cs.in.hoop.base.INHoopBase;
import edu.cmu.cs.in.hoop.project.INHoopGraphFile;
//import edu.cmu.cs.in.hoop.project.INHoopProjectFile;

/** 
 * @author Martin van Velsen
 */
public class INHoopGraphManager extends INHoopBase
{		
	/**
	 * 
	 */
	public INHoopGraphManager()
	{		
		setClassName ("INHoopGraphManager");
		debug ("INHoopGraphManager ()");		
	}
	/**
	 * 
	 */
	public void reset ()
	{
		debug ("reset ()");
				
		INHoopGraphFile grFile=(INHoopGraphFile) INHoopLink.project.getFileByClass (new INHoopGraphFile ().getClassName());
		
		if (grFile!=null)
		{
			grFile.reset();
		}	
		else
			debug ("Error: unable to find graph file in project");	
	}
	/** 
	 * @param aHoop
	 */
	public void addHoop(INHoopBase aHoop) 
	{
		debug ("addHoop ()");

		if (INHoopLink.project==null)
		{
			debug ("Error: no project available yet");
			return;
		}		
		
		if (INHoopLink.getGraphRoot ()==null)
		{
			debug ("Error: unable to get graph root, creating one ...");
			INHoopLink.project.setGraphRoot (aHoop);
		}
						
		INHoopGraphFile grFile=(INHoopGraphFile) INHoopLink.project.getFileByClass (new INHoopGraphFile ().getClassName());
		
		if (grFile!=null)
		{
			ArrayList <INHoopBase> hoops=grFile.getHoops();
			
			if (hoops!=null)
				hoops.add(aHoop);
			else
				debug ("Error: graph file does not contain hoops list");
		}	
		else
			debug ("Error: unable to find graph file in project");
	}	
	/** 
	 * @param root
	 */
	public void setRoot(INHoopBase aRoot) 
	{
		debug ("setRoot ()");
		
		if (INHoopLink.project==null)
		{
			debug ("Error: no project available yet");
			return;
		}
		
		INHoopLink.project.setGraphRoot (aRoot);
	}
	/** 
	 * @return INHoopBase
	 */
	public INHoopBase getRoot() 
	{
		debug ("setRoot ()");
		
		if (INHoopLink.project==null)
		{
			debug ("Error: no project available yet");
			return (null);
		}
		
		return (INHoopLink.project.getGraphRoot());
	}
	/** 
	 * @param aSource
	 * @param aTarget
	 */
	public Boolean connectHoops(INHoopBase aSource,INHoopBase aTarget) 
	{
		debug ("connectHoops ()");
		
		if (aSource==null)
		{
			debug ("Error: hoop source is null");
			return (false);
		}
		
		if (aTarget==null)
		{
			debug ("Error: hoop target is null");
			return (false);
		}		
		
		if (aSource==aTarget)
		{
			setErrorString ("Can't connect a hoop to itself");
			return (false);
		}
		
		aSource.addOutHoop(aTarget);
		
		return (true);
	}	
}
