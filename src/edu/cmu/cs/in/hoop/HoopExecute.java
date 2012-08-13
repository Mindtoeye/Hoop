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
//import edu.cmu.cs.in.base.HoopLink;
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
	private void prepareHoops (HoopBase aRoot)
	{
		debug ("prepareHoops ("+aRoot.getClassName()+")");
		
		aRoot.reset();
		aRoot.setInEditor(inEditor);
		
		ArrayList<HoopBase> outHoops=aRoot.getOutHoops();
			
		for (int i=0;i<outHoops.size();i++)
		{
			HoopBase current=outHoops.get(i);
		
			prepareHoops (current);
		}		
	}
	/**
	 * 
	 */
	private Boolean execute (HoopBase aParent,HoopBase aRoot)
	{
		debug ("execute () push");
							
		// Execution phase of the current Hoop ...
		
		if (aRoot.getActive()==false)
		{
			debug ("This hoop is not active, skipping execution of connected hoops as well");
			return (true);
		}
		
		while (aRoot.getDone()==false)
		{							
			debug ("Hoop "+aRoot.getInstanceName()+" with class " + aRoot.getClassName()+ " is not done yet");
			
			aRoot.setDone(true);
			
			if (aRoot.runHoopInternal(aParent)==false)
			{
				debug ("Unable to run hoop: " + aRoot.getErrorString());
				
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
		
			if (outHoops.size()>0)
			{
				debug ("Executing " + outHoops.size() + " sub hoops ...");
		
				for (int i=0;i<outHoops.size();i++)
				{
					debug ("Running sub hoop: " + i);
				
					HoopBase current=outHoops.get(i);
							
					if (execute (aRoot,current)==false)
						return (false);
				}
				
				debug ("Done executing sub hoops");
			}							
		}
		
		// All done, really done
		
		debug ("execute () pop");
		
		return (true);
	}	
	/**
	 * 
	 */
	@Override
	public void run() 
	{	
		debug ("run ()");
		
		if (root==null)
		{
			debug ("Error: no graph root available");
			return;
		}
				
		prepareHoops (root);
		
		loopExecuting=true;
		
		if (loopCount==-1)
		{
			while (loopExecuting==true)
			{
				execute (null,root);
			}	
		}	
		else
		{
			if (loopCount>0)
			{
				for (int i=0;i<loopCount;i++)
				{
					execute (null,root);
				}
			}
			else
				debug ("Error: loop count can't be 0");
		}
		
		loopExecuting=false;
	}
}

