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

import edu.cmu.cs.in.base.HoopRoot;
import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.hoop.editor.HoopVisualRepresentation;

/** 
 * @author Martin van Velsen
 */
public class HoopExecute extends HoopRoot implements Runnable 
{
	private HoopBase root=null;
	/// -1 = forever, 1 = once, >1 = run N times
	private int loopCount=-1; 
	private Boolean loopExecuting=false;
	private Boolean inEditor=false;
		
	/**
	 *
	 */
	public HoopExecute () 
	{
		setClassName ("HoopExecute");
		debug ("HoopExecute ()");					
	}	
	/**
	 * 
	 */	
	public void setRoot (HoopBase aRoot)
	{
		root=aRoot;
	}
	/**
	 * 
	 */
	public HoopBase getRoot ()
	{
		return (root);
	}
	/**
	 * 
	 */
	public void stopExecution ()
	{
		loopExecuting=false;
	}
	/**
	 * 
	 */    
	public void setInEditor(Boolean inEditor) 
	{
		this.inEditor = inEditor;
	}
	/**
	 * 
	 */	
	public Boolean getInEditor() 
	{
		return inEditor;
	} 	
	/**
	 * 
	 */	
	public void setLoopCount(int loopCount) 
	{
		this.loopCount = loopCount;
	}
	/**
	 * 
	 */	
	public int getLoopCount() 
	{
		return loopCount;
	}	
	/**
	 * 
	 */
	protected void showError (String aClass,String anError)
	{
		debug ("ShowError ()");
		
		debug ("Hoop: " + aClass+", Error: " + anError);
	}	
	/**
	 * 
	 */
	private Boolean execute (HoopBase aRoot)
	{
		debug ("execute (HoopBase)");
						
		aRoot.reset();
		aRoot.setInEditor(inEditor);
		
		// Execution phase of the current Hoop ...
		
		while (aRoot.getDone()==false)
		{		
			if (aRoot.runHoopInternal(aRoot)==false)
			{
				debug ("Error executing hoop: " + aRoot.getErrorString());
				HoopVisualRepresentation panel=aRoot.getVisualizer();
			
				if (panel!=null)
					panel.setState("ERROR");
				else
					debug ("No visual representation present to show error result!");
			
				showError (aRoot.getClassName(),aRoot.getErrorString());
											
				return (false);
			}
		
			/// One of: STOPPED, WAITING, RUNNING, PAUSED, ERROR
			aRoot.setExecutionState("STOPPED");		
		
			// Iterate through all the connected Hoops and call execute ...
		
			ArrayList<HoopBase> outHoops=aRoot.getOutHoops();
		
			debug ("Executing " + outHoops.size() + " hoops ...");
		
			for (int i=0;i<outHoops.size();i++)
			{
				HoopBase current=outHoops.get(i);
			
				if (execute (current)==false)
					return (false);
			}
		}
		
		// All done
		
		return (true);
	}	
	@Override
	public void run() 
	{	
		debug ("run ()");
		
		if (root==null)
		{
			debug ("Error: no graph root available");
			return;
		}
		
		if (root.getActive()==false)
		{
			debug ("Error: graph root is not active");
			return;
		}
		
		loopExecuting=true;
		
		if (loopCount==-1)
		{
			while (loopExecuting==true)
			{
				execute (root);
			}	
		}	
		else
		{
			if (loopCount>0)
			{
				for (int i=0;i<loopCount;i++)
				{
					execute (root);
				}
			}
			else
				debug ("Error: loop count can't be 0");
		}
		
		loopExecuting=false;
	}
}

