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

import edu.cmu.cs.in.base.INBase;
import edu.cmu.cs.in.base.INHoopLink;
import edu.cmu.cs.in.hoop.base.INHoopBase;
import edu.cmu.cs.in.hoop.editor.INHoopVisualRepresentation;

/** 
 * @author Martin van Velsen
 */
public class INHoopExecute extends INBase implements Runnable 
{
	private INHoopBase root=null;
	/// -1 = forever, 1 = once, >1 = run N times
	private int loopCount=-1; 
	private Boolean loopExecuting=false;
	private Boolean inEditor=false;
		
	/**
	 *
	 */
	public INHoopExecute () 
	{
		setClassName ("INHoopExecute");
		debug ("INHoopExecute ()");					
	}	
	/**
	 * 
	 */	
	public void setRoot (INHoopBase aRoot)
	{
		root=aRoot;
	}
	/**
	 * 
	 */
	public INHoopBase getRoot ()
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
	public Boolean execute ()
	{
		debug ("execute ()");
		
		if (root==null)
		{
			debug ("Nothing to execute");
			return (false);
		}
		
		root.setInEditor(inEditor);
		
		if (root.runHoopInternal(null)==false)
		{
			debug ("Error executing hoop: " + root.getErrorString());
			INHoopVisualRepresentation panel=root.getVisualizer();
			
			if (panel!=null)
				panel.setState("ERROR");
			else
				debug ("No visual representation present to show error result!");
			
			INHoopErrorPanel errorPanel=(INHoopErrorPanel) INHoopLink.getWindow("Errors");
			if (errorPanel!=null)
			{
				errorPanel.addError (root.getClassType(),root.getErrorString());
			}			
		}
		
		return (execute (root));	
	}
	/** 
	 * @param aRoot
	 * @return
	 */
	private Boolean execute (INHoopBase aRoot)
	{
		debug ("execute (INHoopBase)");
						
		ArrayList<INHoopBase> outHoops=aRoot.getOutHoops();
		
		debug ("Executing " + outHoops.size() + " hoops ...");
		
		for (int i=0;i<outHoops.size();i++)
		{
			INHoopBase current=outHoops.get(i);
			
			current.reset();
			current.setInEditor(inEditor);
			
			if (current.runHoopInternal(aRoot)==false)
			{
				debug ("Error executing hoop: " + current.getErrorString());
				INHoopVisualRepresentation panel=current.getVisualizer();
				
				if (panel!=null)
					panel.setState("ERROR");
				else
					debug ("No visual representation present to show error result!");
				
				INHoopErrorPanel errorPanel=(INHoopErrorPanel) INHoopLink.getWindow("Errors");
				if (errorPanel!=null)
				{
					errorPanel.addError (current.getClassType(),current.getErrorString());
				}
				
				return (false);
			}
			
			/// One of: STOPPED, WAITING, RUNNING, PAUSED, ERROR
			current.setExecutionState("STOPPED");
			
			if (current.getOutHoops().size()>0) // quick test before we execute
			{
				return (execute (current));
			}	
		}
		
		return (true);
	}
	@Override
	public void run() 
	{	
		debug ("run ()");
		
		loopExecuting=true;
		
		if (loopCount==-1)
		{
			while (loopExecuting==true)
			{
				execute ();
			}	
		}	
		else
		{
			if (loopCount>0)
			{
				for (int i=0;i<loopCount;i++)
				{
					execute ();
				}
			}
			else
				debug ("Error: loop count can't be 0");
		}
		
		loopExecuting=false;
	}
}

