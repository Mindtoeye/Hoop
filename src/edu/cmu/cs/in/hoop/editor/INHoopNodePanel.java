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

import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.JPanel;

import com.mxgraph.swing.mxGraphComponent;

import edu.cmu.cs.in.base.INHoopLink;
import edu.cmu.cs.in.hoop.INHoopTablePanel;
import edu.cmu.cs.in.hoop.base.INHoopBase;
import edu.cmu.cs.in.hoop.properties.INHoopInspectablePanel;
import edu.cmu.cs.in.hoop.properties.INHoopPropertyPanel;

/**   
 * An easy access class to the panel that represents a node in the Hoop graph.
 * You should be able to manage all graphics controls and hoop operations from
 * here without having to worry about how Swing does or does not play well 
 * with UI components. See the INHoopNodeRenderer class if you feel inclined
 * to subject yourself to Swing GUI management.
 */
public class INHoopNodePanel extends INHoopNodeRenderer implements INHoopVisualRepresentation
{
	private static final long serialVersionUID = -1L;
	private INHoopPropertyPanel propPanel=null;
	private INHoopTablePanel tablePanel=null;
	private INHoopInspectablePanel propertiesPanel=null;
	
	/**
	 * 
	 */
	public INHoopNodePanel (INHoopBase aHoop,Object cell,mxGraphComponent graphContainer)
	{
		super (cell,graphContainer);
		
		setClassName ("INHoopNodePanel");
		debug ("INHoopNodePanel ()");
		
		propPanel=(INHoopPropertyPanel) INHoopLink.getWindow("Properties");
					
		tablePanel=(INHoopTablePanel) INHoopLink.getWindow("Data View");
		if (tablePanel==null)
		{
			INHoopLink.addView("Data View",new INHoopTablePanel (),"bottom");
			tablePanel=(INHoopTablePanel) INHoopLink.getWindow("Data View");			
		}
		
		setHoop (aHoop);
				
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
				
		if (hoop!=null)					
		{
			this.setTitle (hoop.getClassName());
			this.setDescription (hoop.getHoopDescription());
			
			leftPortBox.removeAll();
			rightPortBox.removeAll();
			
			ArrayList <String> inPorts=hoop.getInPorts();
			
			for (int i=0;i<inPorts.size();i++)
			{
				addInPort (inPorts.get(i));
			}
			
			ArrayList <String> outPorts=hoop.getOutPorts();
			
			for (int j=0;j<outPorts.size();j++)
			{
				addOutPort (outPorts.get(j));
			}			
			
			leftPortBox.add(Box.createVerticalGlue());
			rightPortBox.add(Box.createVerticalGlue());
		}		
		
		if (propPanel!=null)
		{
			propertiesPanel=new INHoopInspectablePanel (hoop.getHoopDescription());
						
			JPanel hoopPanel=aHoop.getPropertiesPanel();
						
			propPanel.addPropertyPanel (propertiesPanel);
			
			if (hoopPanel!=null)
			{
				propertiesPanel.setPanelContent (hoopPanel);
				hoopPanel.setPreferredSize(propertiesPanel.getCurrentDimensions ());
			}
			else
			{
				debug ("No custom config panel provided, switching to properties panel instead");
				propertiesPanel.setComponent(aHoop);
			}						
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
	/**
	 * One of: STOPPED, WAITING, RUNNING, PAUSED, ERROR
	 */
	@Override
	public void setState(String aState) 
	{
		debug ("setState ("+aState+")");
		
		if (aState.equals("ERROR")==true)
		{
			icon.setIcon(INHoopLink.getImageByName("led-red.png"));	
		}
		
		if (aState.equals("STOPPED")==true)
		{
			icon.setIcon(INHoopLink.getImageByName("led-yellow.png"));
		}
		
		if (aState.equals("WAITING")==true)
		{
			icon.setIcon(INHoopLink.getImageByName("led-yellow.png"));
		}
		
		if (aState.equals("RUNNING")==true)
		{
			icon.setIcon(INHoopLink.getImageByName("led-green.png"));
		}		
	}	
}
