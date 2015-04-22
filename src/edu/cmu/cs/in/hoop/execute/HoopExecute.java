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

import javax.swing.JButton;

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.base.HoopRoot;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.hoop.hoops.base.HoopThreadBase;
import edu.cmu.cs.in.hoop.editor.HoopVisualRepresentation;

/** 
 *
 */
public class HoopExecute extends HoopRoot
{
	public static int EXEC_STOPPED=0;
	public static int EXEC_RUNNING=1;
	public static int EXEC_SUSPENDED=2;	
	public static int EXEC_KILLED=3;
	
	private int executionState=EXEC_RUNNING;
	
	protected HoopBase root=null;
	 
	//private Boolean loopExecuting=false;
	private Boolean inEditor=false;
	
	static public int LOCAL=0;
	static public int CLUSTER=1;
		
	private HoopBase currentRunner=null;
	
	private int location=LOCAL;
			
    private String experimentID=UUID.randomUUID().toString();
    
    // Pointers to GUI controls we need to keep informed of state
    
    public JButton runButton = null;    
    public JButton runClusterButton = null;    
    public JButton debugButton = null;
    public JButton stopButton = null;
	
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
			
			current.prep ();
		
			prepareHoops (current);
		}
	}
	/**
	 * 
	 * @param aSource
	 */
	public Boolean dataReady (HoopThreadBase aSource)
	{
		debug ("dataReady ("+aSource.getClassName()+")");
		
		HoopBase upCast=(HoopBase) aSource;
		
		// Iterate through all the connected Hoops and call execute ...
		
		ArrayList<HoopBase> outHoops=upCast.getOutHoops();
	
		if (outHoops.size()>0)
		{
			debug ("Executing " + outHoops.size() + " sub hoops for " + upCast.getClassName() + " ...");
	
			for (int i=0;i<outHoops.size();i++)
			{
				debug ("Running sub hoop: " + i);
			
				HoopBase current=outHoops.get(i);
											
				if (execute (upCast,current)==false)
				{
					return (false);
				}
			}
			
			debug ("Done executing sub hoops");
		}
		else
			debug ("No sub hoops, all done");
		
		debug ("dataReady ("+aSource.getClassName()+") Done");
		
		return (true);
	}
	/**
	 * 
	 */
	protected Boolean execute (HoopBase aParent,HoopBase aCurrent)
	{
		debug ("execute ()");

		startHoopExecution (aCurrent);
		
		if (aCurrent.getBreakBefore()==true)
		{
			debug ("Stopping execution!");
			return (true);
		}
		
		aCurrent.resetData();

		currentRunner=aCurrent;
		
		if (aCurrent.runHoopInternal(aParent)==false)
		{
			currentRunner=null;
			
			debug ("Unable to run hoop: " + aCurrent.getErrorString());
			
			HoopVisualRepresentation panel=aCurrent.getVisualizer();
		
			if (panel!=null)
				panel.setState("ERROR");
			else
				debug ("No visual representation present to show error result!");
		
			showError (aCurrent.getClassName(),aCurrent.getErrorString());
			
			endHoopExecution (aCurrent);
														
			return (false);
		}
				
		currentRunner=null;
	
		/// One of: STOPPED, WAITING, RUNNING, PAUSED, ERROR
		aCurrent.setExecutionState("STOPPED");		
	
		// Iterate through all the connected Hoops and call execute ...
	
		ArrayList<HoopBase> outHoops=aCurrent.getOutHoops();
	
		if (outHoops.size()>0)
		{
			debug ("Executing " + outHoops.size() + " sub hoops for " + aCurrent.getClassName() + " ...");
	
			for (int i=0;i<outHoops.size();i++)
			{
				debug ("Running sub hoop: " + i);
			
				HoopBase current=outHoops.get(i);
											
				if (execute (aCurrent,current)==false)
				{
					endHoopExecution (aCurrent);
					return (false);
				}
			}
			
			debug ("Done executing sub hoops");
		}
		else
			debug ("No sub hoops, all done");
		
		// All done, really done
		
		debug ("execute () pop");
		
		if (aCurrent.getBreakAfter()==true)
		{
			debug ("Stopping execution!");
			return (true);
		}
		
		endHoopExecution (aCurrent);
				
		return (true);
	}	
	/**
	 * 
	 */
	public void stop ()
	{
		debug ("stop ()");
		
		stopExecution (root);
	}
	/**
	 * 
	 */
	public void stopExecution (HoopBase aRoot)
	{
		debug ("stopExecution ()");
		
		//loopExecuting=false;
						
		// Then call stop on all Hoops ...
		
		if (aRoot==null)
		{
			debug ("Error: no graph root available");
			return;
		}
		
		aRoot.stop();
		
		ArrayList<HoopBase> outHoops=aRoot.getOutHoops();		
		
		for (int i=0;i<outHoops.size();i++)
		{
			HoopBase current=outHoops.get(i);

			stopExecution (current);
		}			
	}
	/**
	 * 
	 */
	public void stopError ()
	{
		stopExecution (root);
		
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
	public void run() 
	{	
		debug ("run ()");
						
		experimentID=UUID.randomUUID().toString();
		
		currentRunner=null;
				
		if (root==null)
		{
			debug ("Error: no graph root available");
			return;
		}
		
		executionState=HoopExecute.EXEC_RUNNING;
		
		startExecution ();
				
		//showHoopTree (null,root);
		
		prepareHoops (root);
		
		//showHoopTree (null,root);
				
		if (execute (null,root)==true)
		{
			updateDependencies ();
		}
		
		endExecution ();
		
		executionState=HoopExecute.EXEC_STOPPED;		
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
	/**
	 * 
	 */	
	public int getExecutionState() 
	{
		return executionState;
	}
	/**
	 * 
	 */	
	public void setExecutionState(int executionState) 
	{
		this.executionState = executionState;
	}
}
