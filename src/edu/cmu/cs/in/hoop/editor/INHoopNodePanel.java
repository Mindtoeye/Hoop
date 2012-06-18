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

import com.mxgraph.swing.mxGraphComponent;

import edu.cmu.cs.in.base.INHoopLink;
import edu.cmu.cs.in.hoop.INHoopTablePanel;
import edu.cmu.cs.in.hoop.base.INHoopBase;
import edu.cmu.cs.in.hoop.base.INHoopFileLoadBase;
import edu.cmu.cs.in.hoop.properties.INHoopInspectablePanel;
import edu.cmu.cs.in.hoop.properties.INHoopPropertyPanel;

/**   
 * An easy access class to the panel that represents a node in the Hoop graph.
 * You should be able to manage all graphics controls and hoop operations from
 * here without having to worry about how Swing does or does not play well 
 * with UI components. See the INHoopNodeRenderer class if you feel inclined
 * to subject yourself to Swing GUI management.
 */
public class INHoopNodePanel extends INHoopNodeRenderer
{
	private static final long serialVersionUID = -1L;
	private INHoopPropertyPanel propPanel=null;
	private INHoopTablePanel tablePanel=null;
	
	/**
	 * 
	 */
	public INHoopNodePanel (INHoopBase aHoop,Object cell,mxGraphComponent graphContainer)
	{
		super (aHoop,cell,graphContainer);
		
		setClassName ("INHoopNodePanel");
		debug ("INHoopNodePanel ()");
		
		propPanel=(INHoopPropertyPanel) INHoopLink.getWindow("Properties");
					
		tablePanel=(INHoopTablePanel) INHoopLink.getWindow("Data View");
		if (tablePanel==null)
		{
			INHoopLink.addView("Data View",new INHoopTablePanel (),"bottom");
			tablePanel=(INHoopTablePanel) INHoopLink.getWindow("Data View");
		}
		
		setHoop (new INHoopFileLoadBase ()); // Just for testing
		
		if (hoop!=null)
			this.setTitle (hoop.getClassName());
		
		debug ("INHoopNodePanel () done");
	}
	/**
	 * 
	 */	
	public INHoopBase getHoop() 
	{
		return hoop;
	}
	/**
	 * 
	 */
	public void reset ()
	{
		icon.setIcon(INHoopLink.getImageByName("led-yellow.png"));
		
		if (hoop!=null)
			hoop.reset ();
	}
	/**
	 * 
	 */	
	public void setHoop(INHoopBase aHoop) 
	{
		debug ("setHoop ()");
		
		this.hoop=aHoop;
		
		this.setTitle (hoop.getHoopDescription());
		
		if (propPanel!=null)
		{
			propPanel.addPropertyPanel (new INHoopInspectablePanel (hoop.getHoopDescription()));
		}
		
		debug ("setHoop () done");
	}	
	/**
	 * 
	 */
	protected void examineData ()
	{
		debug ("examineData ()");
				
		INHoopTablePanel panel=(INHoopTablePanel) INHoopLink.getWindow("Data View");
		
		if (panel!=null)
		{
			panel.showHoop(hoop);
		}		
	}	
}
