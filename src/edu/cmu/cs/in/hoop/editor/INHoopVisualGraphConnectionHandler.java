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

import java.awt.event.MouseEvent;

import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.handler.mxConnectionHandler;

import edu.cmu.cs.in.base.INBase;
import edu.cmu.cs.in.base.INHoopLink;
import edu.cmu.cs.in.hoop.base.INHoopBase;

/**
 * 
 */
public class INHoopVisualGraphConnectionHandler extends mxConnectionHandler
{
	private Object source=null;
	private	Object target=null;
	
	/** 
	 * @param arg0
	 */
	public INHoopVisualGraphConnectionHandler(mxGraphComponent arg0) 
	{
		super(arg0);
	
		debug ("INHoopVisualGraphConnectionHandler ()");
	}
	/**
	 * 
	 */
	private void debug (String aMessage)
	{
		INBase.debug("INHoopVisualGraphConnectionHandler",aMessage);
	}
	/**
	 * Edge delete happens here!
	 */
	public void reset ()
	{
		debug ("reset ()");
		super.reset();
		
	}
	/**
	 * 
	 */
	public void mouseReleased(MouseEvent e)
	{
		debug ("mouseReleased ()");
		
		super.mouseReleased(e);
		
		if ((source!=null) && (target!=null))
		{
			INHoopBase sourceHoop=(INHoopBase) source;
			INHoopBase targetHoop=(INHoopBase) target;
			
			if ((sourceHoop!=null) && (targetHoop!=null))
			{
				debug ("We have a valid source and target hoop, linking them together ...");
				
				if (INHoopLink.hoopGraphManager.connectHoops(sourceHoop, targetHoop)==false)
				{				
					debug ("Error creating edge");
				}
				else
					debug ("Edge created");
			}			
			else
				debug ("Info: no complete edge yet, waiting ...");					
		}
		else
			debug ("Either source or target isn't valid");
		
		source=null;
		target=null;
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
				if (userSourceObject instanceof INHoopBase)
				{
					source=userSourceObject;
				}	
				//else
				//	debug ("Source is not INHoopBase class, instead: " + userSourceObject.getClass());				
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
				if (userTargetObject instanceof INHoopBase)
				{
					target=userTargetObject;
				}	
				//else
				//	debug ("Target is not INHoopBase class, instead: " + userTargetObject.getClass());
			}
			else
				debug ("Target is null");
		}					
		
		return (true);
	}	
}
