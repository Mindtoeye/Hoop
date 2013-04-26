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

package edu.cmu.cs.in.hoop.execute;

import java.util.ArrayList;
import java.util.UUID;

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.base.HoopRoot;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.hoop.editor.HoopVisualRepresentation;

/** 
 *
 */
public class HoopExecute extends HoopRoot implements Runnable 
{
	private HoopBase root=null;
	/// -1 = forever, 1 = once, >1 = run N times
	private int loopCount=-1; 
	private Boolean loopExecuting=false;
	private Boolean inEditor=false;
	
	static public int LOCAL=0;
	static public int CLUSTER=1;
		
	private HoopBase currentRunner=null;
	
	private int location=LOCAL;
			
    private String experimentID=UUID.randomUUID().toString();   
	
	/**
	 *
	 */
	public HoopExecute () 
	{
		setClassName ("HoopExecute");
		debug ("HoopExecute ()");	
		
		HoopLink.runner=this;
	}
	/**
	 * 
	 */
	public String getExperimentID ()
	{
		return (experimentID);
	}
	/**
	 * 
	 */	
	public int getLocation() 
	{
		return location;
	}
	/**
	 * 
	 */	
	public void setLocation(int location) 
	{
		this.location = location;
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
		debug ("showError ()");
		
		debug ("Hoop: " + aClass + ", Error: " + anError);
	}	
	/**
	 * 
	 */
	private void prepareHoops (HoopBase aRoot)
	{
		debug ("prepareHoops ("+aRoot.getClassName()+")");
		
		aRoot.reset(); // This is a hard reset
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
	protected Boolean execute (HoopBase aParent,HoopBase aRoot)
	{
		debug ("execute () push");
							
		if (loopExecuting==false)
			return (true);
					
		// Execution phase of the current Hoop ...
		
		if (aRoot.getActive()==false)
		{
			debug ("This hoop is not active, skipping execution of connected hoops as well");
			return (true);
		}
		
		experimentID=UUID.randomUUID().toString();
		
		startHoopExecution (aRoot);
		
		if (aRoot.getBreakBefore()==true)
		{
			debug ("Stopping execution!");
			return (true);
		}
		
		aRoot.resetData();
		
		while ((aRoot.getDone()==false) && (loopExecuting==true))
		{							
			debug ("Hoop "+aRoot.getInstanceName()+" with class " + aRoot.getClassName()+ " is not done yet");
			
			aRoot.setDone(true);
			
			setSubTreeDone (aRoot,false);
			
			currentRunner=aRoot;
			
			if (aRoot.runHoopInternal(aParent)==false)
			{
				currentRunner=null;
				
				debug ("Unable to run hoop: " + aRoot.getErrorString());
				
				HoopVisualRepresentation panel=aRoot.getVisualizer();
			
				if (panel!=null)
					panel.setState("ERROR");
				else
					debug ("No visual representation present to show error result!");
			
				showError (aRoot.getClassName(),aRoot.getErrorString());
				
				endHoopExecution (aRoot);
															
				return (false);
			}
			else
			{
				if (loopExecuting==false)
				{
					endHoopExecution (aRoot);
					return (true);
				}
			}
			
			currentRunner=null;
		
			/// One of: STOPPED, WAITING, RUNNING, PAUSED, ERROR
			aRoot.setExecutionState("STOPPED");		
		
			// Iterate through all the connected Hoops and call execute ...
		
			ArrayList<HoopBase> outHoops=aRoot.getOutHoops();
		
			if (outHoops.size()>0)
			{
				debug ("Executing " + outHoops.size() + " sub hoops for " + aRoot.getClassName() + " ...");
		
				for (int i=0;i<outHoops.size();i++)
				{
					debug ("Running sub hoop: " + i);
				
					HoopBase current=outHoops.get(i);
												
					if (execute (aRoot,current)==false)
					{
						endHoopExecution (aRoot);
						return (false);
					}
				}
				
				debug ("Done executing sub hoops");
			}
			else
				debug ("No sub hoops, all done");
		}
		
		// All done, really done
		
		debug ("execute () pop");
		
		if (aRoot.getBreakAfter()==true)
		{
			debug ("Stopping execution!");
			return (true);
		}
		
		endHoopExecution (aRoot);
				
		return (true);
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
	public void stopError ()
	{
		stopExecution ();
		
		if (currentRunner!=null)
		{
			HoopVisualRepresentation panel=currentRunner.getVisualizer();
		
			if (panel!=null)
				panel.setState("ERROR");
			else
				debug ("No visual representation present to show error result!");
		}	
	}
	/**
	 * 
	 */
	protected void startHoopExecution (HoopBase aHoop)
	{
		debug ("startHoopExecution ()");
		
		// Implement in child class!
	}	
	/**
	 * 
	 */
	protected void endHoopExecution (HoopBase aHoop)
	{
		debug ("endHoopExecution ()");
		
		// Implement in child class!
	}	
	/**
	 * 
	 */
	protected void startExecution ()
	{
		debug ("startExecution ()");
		
		// Implement in child class
	}
	/**
	 * 
	 */
	protected void endExecution ()
	{
		debug ("endExecution ()");
		
		// Implement in child class
	}	
	/**
	 * 
	 */
	@Override
	public void run() 
	{	
		debug ("run ()");
		
		loopExecuting=true;
		currentRunner=null;
				
		if (root==null)
		{
			debug ("Error: no graph root available");
			return;
		}
		
		startExecution ();
				
		showHoopTree (null,root);
		
		prepareHoops (root);
		
		showHoopTree (null,root);
		
		loopExecuting=true;
		
		if (loopCount==-1)
		{
			while (loopExecuting==true)
			{
				if (execute (null,root)==true)
				{
					updateDependencies ();
				}
			}	
		}	
		else
		{
			if (loopCount>0)
			{
				for (int i=0;i<loopCount;i++)
				{
					if (execute (null,root)==true)
					{
						updateDependencies ();
					}	
				}
			}
			else
				debug ("Error: loop count can't be 0");
		}
		
		loopExecuting=false;
		
		endExecution ();
	}
	/**
	 * 
	 */
	protected void updateDependencies ()
	{
		debug ("updateDependencies ()");
		
		// Implement in child class if any visual dependencies need to
		// be updated or informed
	}
	/**
	 * 
	 */
	private void showHoopTree (HoopBase aParent,HoopBase aRoot)
	{									
		if (aParent!=null)
			debug ("+ "+aParent.getInstanceName()+" with class " + aParent.getClassName() + " -> " + aRoot.getInstanceName() + " with class " + aRoot.getClassName());
		else
			debug ("+ "+aRoot.getInstanceName()+" with class " + aRoot.getClassName());
																
		ArrayList<HoopBase> outHoops=aRoot.getOutHoops();
		
		if (outHoops.size()>0)
		{		
			for (int i=0;i<outHoops.size();i++)
			{						
				HoopBase current=outHoops.get(i);
				
				showHoopTree (aRoot,current);
			}				
		}								
	}
	/**
	 * 
	 */
	private void setSubTreeDone (HoopBase aParent,Boolean aValue)
	{																									
		ArrayList<HoopBase> outHoops=aParent.getOutHoops();
		
		if (outHoops.size()>0)
		{		
			for (int i=0;i<outHoops.size();i++)
			{						
				HoopBase current=outHoops.get(i);
				
				current.setDone(aValue);

				setSubTreeDone (current,aValue);
			}				
		}								
	}	
}
