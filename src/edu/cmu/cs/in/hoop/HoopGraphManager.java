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

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.hoop.hoops.base.HoopConnection;
import edu.cmu.cs.in.hoop.project.HoopGraphFile;
import edu.cmu.cs.in.hoop.properties.HoopPropertyPanel;

/** 
 * @author Martin van Velsen
 */
public class HoopGraphManager extends HoopBase
{		
	private static final long serialVersionUID = -5244155123177538405L;
	
	/**
	 * 
	 */
	public HoopGraphManager()
	{		
		setClassName ("HoopGraphManager");
		debug ("HoopGraphManager ()");		
	}
	/**
	 * 
	 */
	public HoopBase findHoopByID (String aRef)
	{
		debug ("findHoopByID (String)");
		
		if (aRef==null)
			return (null);
				
		HoopGraphFile grFile=(HoopGraphFile) HoopLink.project.getFileByClass (new HoopGraphFile ().getClassName());
		
		if (grFile!=null)
		{
			ArrayList <HoopBase> hoops=grFile.getHoops();
			
			for (int i=0;i<hoops.size();i++)
			{
				HoopBase testHoop=hoops.get(i);
				
				if (testHoop.getHoopID().equalsIgnoreCase(aRef)==true)
					return (testHoop);
			}
		}	
		else
			debug ("Error: unable to find graph file in project");		
		
		return (null);
	}	
	/**
	 * 
	 */
	public HoopBase findHoopByReference (Object aRef)
	{
		debug ("findHoopByReference (Object)");
		
		if (aRef==null)
			return (null);
		
		HoopBase aRoot=HoopLink.getGraphRoot ();
		
		if (aRoot==null)
			return (null);
		
		if (aRoot.getGraphCellReference()==aRef)
			return (aRoot);
		
		ArrayList <HoopBase> hoopList=aRoot.getOutHoops();
		
		for (int i=0;i<hoopList.size();i++)
		{
			HoopBase checker=hoopList.get(i);
			
			if (checker.getGraphCellReference()==aRef)
				return (checker);
			else
			{
				HoopBase deferred=findHoopByReference (aRef,checker);
				if (deferred!=null)
				{
					return (deferred);
				}
			}	
		}
		
		return (null);
	}
	/**
	 * 
	 */
	public HoopBase findHoopByReference (Object aRef,HoopBase aRoot)
	{
		debug ("findHoopByReference (Object,HoopBase)");
		
		if (aRef==null)
			return (null);
				
		if (aRoot==null)
			return (null);
				
		ArrayList <HoopBase> hoopList=aRoot.getOutHoops();
		
		for (int i=0;i<hoopList.size();i++)
		{
			HoopBase checker=hoopList.get(i);
			
			if (checker.getGraphCellReference()==aRef)
				return (checker);
			else
			{
				HoopBase deferred=findHoopByReference (aRef,checker);
				if (deferred!=null)
				{
					return (deferred);
				}
			}	
		}
		
		return (null);		
	}
	/**
	 * 
	 */
	public void reset ()
	{
		debug ("reset ()");
				
		HoopGraphFile grFile=(HoopGraphFile) HoopLink.project.getFileByClass (new HoopGraphFile ().getClassName());
		
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
	public void addHoop(HoopBase aHoop) 
	{
		debug ("addHoop ()");

		if (HoopLink.project==null)
		{
			debug ("Error: no project available yet");
			return;
		}		
		
		if (HoopLink.getGraphRoot ()==null)
		{
			debug ("Error: unable to get graph root, creating one ...");
			HoopLink.project.setGraphRoot (aHoop);
		}
						
		HoopGraphFile grFile=(HoopGraphFile) HoopLink.project.getFileByClass (new HoopGraphFile ().getClassName());
		
		if (grFile!=null)
		{
			ArrayList <HoopBase> hoops=grFile.getHoops();
			
			if (hoops!=null)
				hoops.add(aHoop);
			else
				debug ("Error: graph file does not contain hoops list");
		}	
		else
			debug ("Error: unable to find graph file in project");
	}	
	/** 
	 * @param aHoop
	 */
	public void removeHoop (HoopBase aHoop)
	{
		debug ("removeHoop ()");
	
		if (HoopLink.project==null)
		{
			debug ("Error: no project available yet");
			return;
		}		
		
		if (HoopLink.getGraphRoot ()==null)
		{
			debug ("Error: unable to get graph root, creating one ...");
			return;
		}
		
		// First we remove the properties panel from the properties window ...
		
		HoopPropertyPanel propPanel=(HoopPropertyPanel) HoopLink.getWindow("Properties");

		propPanel.removePropertyPanel(aHoop);
				
		// Next we completely remove the hoop from our main list of hoops ...
						
		HoopGraphFile grFile=(HoopGraphFile) HoopLink.project.getFileByClass (new HoopGraphFile ().getClassName());
		
		if (grFile!=null)
		{
			ArrayList <HoopBase> hoops=grFile.getHoops();
			
			if (hoops!=null)
			{
				hoops.remove(aHoop);
			}
			else
				debug ("Error: graph file does not contain hoops list");
		}	
		else
			debug ("Error: unable to find graph file in project");		
	}
	/** 
	 * @param root
	 */
	public void setRoot(HoopBase aRoot) 
	{
		debug ("setRoot ()");
		
		if (HoopLink.project==null)
		{
			debug ("Error: no project available yet");
			return;
		}
		
		HoopLink.project.setGraphRoot (aRoot);
	}
	/** 
	 * @return HoopBase
	 */
	public HoopBase getRoot() 
	{
		debug ("setRoot ()");
		
		if (HoopLink.project==null)
		{
			debug ("Error: no project available yet");
			return (null);
		}
		
		return (HoopLink.project.getGraphRoot());
	}
	/**
	 * 
	 */
	public HoopConnection getConnection (HoopBase aSource,HoopBase aTarget)
	{		
		debug ("getConnection ()");
		
		ArrayList <HoopConnection> conns=HoopLink.project.getGraphConnections();
		
		for (int i=0;i<conns.size();i++)
		{
			HoopConnection aConn=conns.get(i);
			
			if ((aConn.getFromHoop()==aSource) && (aConn.getToHoop()==aTarget))
			{
				debug ("Found connection in connection list, removing ...");
				
				return (aConn);
			}
		}
		
		return (null);
	}
	/** 
	 * @param aSource
	 * @param aTarget
	 */
	public HoopConnection connectHoops(HoopBase aSource,HoopBase aTarget) 
	{
		debug ("connectHoops ()");
		
		if (aSource==null)
		{
			debug ("Error: hoop source is null");
			return (null);
		}
		
		if (aTarget==null)
		{
			debug ("Error: hoop target is null");
			return (null);
		}		
		
		if (aSource==aTarget)
		{
			setErrorString ("Can't connect a hoop to itself");
			return (null);
		}
		
		HoopConnection aConnection=getConnection (aSource,aTarget);
		if (aConnection!=null)
		{
			debug ("Connection already exists, returning found connection");
			return (aConnection);
		}
				
		aSource.addOutHoop(aTarget);
		
		aConnection=new HoopConnection ();
		
		aConnection.setFromHoop(aSource);
		aConnection.setToHoop(aTarget);
		
		ArrayList <HoopConnection> conns=HoopLink.project.getGraphConnections();
		
		conns.add(aConnection);
		
		return (aConnection);
	}	
	/** 
	 * @param aSource
	 * @param aTarget
	 */
	public Boolean disconnectHoops(HoopBase aSource,HoopBase aTarget) 
	{
		debug ("disconnectHoops ()");
				
		ArrayList<HoopBase> list=aSource.getOutHoops();
		
		list.remove(aTarget); // We remove it but don't delete it
		
		ArrayList <HoopConnection> conns=HoopLink.project.getGraphConnections();
		
		for (int i=0;i<conns.size();i++)
		{
			HoopConnection aConn=conns.get(i);
			
			if ((aConn.getFromHoop()==aSource) && (aConn.getToHoop()==aTarget))
			{
				debug ("Found connection in connection list, removing ...");
				
				conns.remove(aConn);
				return (true);
			}
		}
				
		return (true);
	}
}
