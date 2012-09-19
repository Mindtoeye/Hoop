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

package edu.cmu.cs.in.hoop.editor;

import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.handler.mxConnectionHandler;

import edu.cmu.cs.in.base.HoopRoot;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;

/**
 * Notice: we don't create new connections between Hoops in this class
 * anymore. Please see the method createComponents in the
 * HoopVisualGraphComponent class.
 */
public class HoopVisualGraphConnectionHandler extends mxConnectionHandler
{
	private Object hoopSource=null;
	private	Object hoopTarget=null;
	
	/** 
	 * @param arg0
	 */
	public HoopVisualGraphConnectionHandler(mxGraphComponent arg0) 
	{
		super(arg0);
	
		debug ("HoopVisualGraphConnectionHandler ()");
	}	
	/**
	 * 
	 */
	private void debug (String aMessage)
	{
		HoopRoot.debug("HoopVisualGraphConnectionHandler",aMessage);
	}
	/**
	 * 
	 */
	public void setHoopSource(Object hoopSource) 
	{
		this.hoopSource = hoopSource;
	}	
	/**
	 * 
	 */
	public Object getHoopSource() 
	{
		return hoopSource;
	}
	/**
	 * 
	 */
	public void setHoopTarget(Object hoopTarget) 
	{
		this.hoopTarget = hoopTarget;
	}	
	/**
	 * 
	 */
	public Object getHoopTarget() 
	{
		return hoopTarget;
	}		
	/**
	 * 
	 */
	public boolean isValidSource(Object cell)
	{
		//debug ("isValidSource ()");
	
		if (cell instanceof mxCell)
		{
			//debug ("We've got a source cell here");
	
			mxCell sourceCell=(mxCell) cell;
			
			Object userSourceObject=sourceCell.getValue();
			
			if (userSourceObject!=null)
			{
				if (userSourceObject instanceof HoopBase)
				{
					//debug ("Source object is a valid hoopbase object");
					
					setHoopSource(userSourceObject);
				}	
				//else
				//	debug ("Source is not HoopBase class, instead: " + userSourceObject.getClass());				
			}
			else
				debug ("Source is null");
		}			
		
		return (true);
	}
	/**
	 * 
	 */
	public boolean isValidTarget(Object cell)
	{
		//debug ("isValidTarget ()");
		
		if (cell instanceof mxCell)
		{
			//debug ("We've got a target cell here");
	
			mxCell targetCell=(mxCell) cell;
			
			Object userTargetObject=targetCell.getValue();
			
			if (userTargetObject!=null)
			{
				if (userTargetObject instanceof HoopBase)
				{
					//debug ("Source object is a valid hoopbase object");
					
					setHoopTarget(userTargetObject);
				}	
				//else
				//	debug ("Target is not HoopBase class, instead: " + userTargetObject.getClass());
			}
			else
				debug ("Target is null");
		}					
		
		return (true);
	}
}
