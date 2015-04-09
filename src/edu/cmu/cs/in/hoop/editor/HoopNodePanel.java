
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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.Timer;

import com.mxgraph.swing.mxGraphComponent;

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.base.HoopProperties;
import edu.cmu.cs.in.controls.HoopProgressPainter;
import edu.cmu.cs.in.controls.HoopShadowBorder;
import edu.cmu.cs.in.hoop.HoopTablePanel;
import edu.cmu.cs.in.hoop.execute.HoopExecuteInEditor;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.hoop.properties.HoopInspectablePanel;
import edu.cmu.cs.in.hoop.properties.HoopPropertyPanel;
import edu.cmu.cs.in.hoop.properties.HoopVisualProperties;

/**   
 * An easy access class to the panel that represents a node in the Hoop graph.
 * You should be able to manage all graphics controls and hoop operations from
 * here without having to worry about how Swing does or does not play well 
 * with UI components. See the HoopNodeRenderer class if you feel inclined
 * to subject yourself to Swing GUI management.
 */
public class HoopNodePanel extends HoopNodeRenderer implements HoopVisualRepresentation, MouseListener, ActionListener
{
	private static final long serialVersionUID = -1L;
	private HoopPropertyPanel propPanel=null;
	private HoopInspectablePanel propertiesPanel=null;
	private double scale=1.0;
	
	private Timer blinker=null;	
	private boolean blinking=false;

	/**
	 * 
	 */
	public HoopNodePanel (HoopBase aHoop,Object cell,mxGraphComponent graphContainer)
	{
		super (cell,graphContainer);
		
		setClassName ("HoopNodePanel");
		debug ("HoopNodePanel ()");
		
		propPanel=(HoopPropertyPanel) HoopLink.getWindow("Properties");
							
		setHoop (aHoop);
				
		//this.addMouseListener(this);
		
		debug ("HoopNodePanel () done");
	}
	/**
	 * 
	 */
	public double getScale() 
	{
		return scale;
	}
	/**
	 * 
	 */	
	public void setScale(double scale) 
	{
		this.scale = scale;
	}	
	/**
	 * When a graph is saved it will go through each hoop and call this
	 * method. This will in turn in the panel belonging to that hoop
	 * call a method that sets all the visual properties back into the
	 * hoop.
	 */	
	@Override
	public void propagateVisualProperties() 
	{
		debug ("propagateVisualProperties ()");
		
		HoopExecuteInEditor execution=(HoopExecuteInEditor) HoopLink.runner;
		
		if (execution.getExecuteSpeed ()==HoopExecuteInEditor.SPEED_FAST)
			return;
		
		if (hoop!=null)
		{
			HoopVisualProperties vizProps=hoop.getVisualProperties();
			
			vizProps.setX(this.getX());
			vizProps.setY(this.getY());
			vizProps.setWidth(this.getWidth());
			vizProps.setHeight(this.getHeight());
			
			setStatus ("Ex: " + hoop.getExecutionCount());
		}
	}	
	/**
	 * 
	 */
	public void fixDimensions (double aScale)
	{
		debug ("fixDimensions ()");
		
		setScale(aScale);
		
		if (hoop!=null)
		{
			HoopVisualProperties vizProps=hoop.getVisualProperties();
			
			vizProps.setOriginalWidth (this.getWidth());
			vizProps.setOriginalHeight (this.getHeight());
		}
		else
			debug ("Internal error: no hoop object available to fix dimensions");
	}
	/**
	 * 
	 */	
	public HoopBase getHoop() 
	{
		return hoop;
	}
	/**
	 * 
	 */
	public void reset ()
	{
		icon.setIcon(HoopLink.getImageByName("led-yellow.png"));
		
		if (hoop!=null)
			hoop.reset ();
	}
	/**
	 * 
	 */	
	public void setHoop(HoopBase aHoop) 
	{
		debug ("setHoop ()");
		
		this.hoop=aHoop;
				
		if (hoop!=null)					
		{
			this.setTitle (hoop.getClassName());
			//this.setDescription (hoop.getHoopDescription());
			
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
			
			colorHoop ();
		}		

		popupPropertyPanel ();

		debug ("setHoop () done");
	}
	/**
	 * 
	 */
	private void colorHoop ()
	{
		debug ("colorHoop ()");
		
		backgrColor=HoopProperties.getHoopColor(this.hoop);
								
		setBackground(backgrColor);
		titleBar.setBackground(backgrColor);
		toolBar.setBackground(backgrColor);
		kvExamineButton.setBackground(backgrColor);
		hoopOptionButton.setBackground(backgrColor);
		showHelpButton.setBackground(backgrColor);
		leftPortBox.setBackground(backgrColor);
		rightPortBox.setBackground(backgrColor);
		bottomPanel.setBackground(backgrColor);
	}
	/**
	 * 
	 */
	public void popupPropertyPanel ()
	{
		debug ("popupPropertyPanel ()");
		
		if (this.hoop==null)
		{
			debug ("Error no hoop object available in node panel");
			return;
		}
		
		if ((this.hoop.getPropertiesPanel()!=null) || (this.hoop.getProperties().size()>0))
		{	
			if ((propPanel!=null) && (this.hoop.getClassName().equals("HoopStart")==false))
			{
				propertiesPanel=new HoopInspectablePanel (this.hoop);

				propPanel.addPropertyPanel (propertiesPanel);

				JPanel hoopPanel=this.hoop.getPropertiesPanel();

				if (hoopPanel!=null)
				{
					propertiesPanel.setPanelContent (hoopPanel,this.hoop);
					//hoopPanel.setPreferredSize(propertiesPanel.getCurrentDimensions ());
					hoopPanel.setPreferredSize(propertiesPanel.getCurrentDimensions ());
				}
				else
				{
					debug ("No custom config panel provided, switching to properties panel instead");
					propertiesPanel.setHoop(this.hoop);
				}
			}
		}	
	}
	/**
	 * 
	 */
	protected void examineData ()
	{
		debug ("examineData ()");
				
		HoopTablePanel panel=(HoopTablePanel) HoopLink.getWindow("Data View");
		
		if (panel==null)
		{
			panel=new HoopTablePanel();
			HoopLink.addView ("Data View",panel,"bottom");
		}
		 
		HoopLink.popWindow("Data View");
		panel.showHoop(hoop);					
	}
	/**
	 * 
	 */
	public void blink ()
	{
		debug ("blink ()");
		
		if (blinking==true)
		{
			return;
		}
		
		icon.setIcon(HoopLink.getImageByName("led-green.png"));
		
		blinker=new Timer(200,this);

		blinker.start();
		
		blinking=true;
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
			icon.setIcon(HoopLink.getImageByName("led-red.png"));
			setBorder(BorderFactory.createCompoundBorder(HoopShadowBorder.getSharedInstance(), BorderFactory.createBevelBorder(BevelBorder.RAISED)));
			setWaiting (false);
			return;
		}
		
		if (aState.equals("STOPPED")==true)
		{
			icon.setIcon(HoopLink.getImageByName("led-yellow.png"));
			setBorder(BorderFactory.createCompoundBorder(HoopShadowBorder.getSharedInstance(), BorderFactory.createBevelBorder(BevelBorder.RAISED)));
			setWaiting (false);
			return;			
		}
		
		if (aState.equals("WAITING")==true)
		{
			icon.setIcon(HoopLink.getImageByName("led-yellow.png"));
			setBorder(BorderFactory.createCompoundBorder(HoopShadowBorder.getSharedInstance(), BorderFactory.createBevelBorder(BevelBorder.RAISED)));
			setWaiting (false);			
			return;			
		}
		
		if (aState.equals("RUNNING")==true)
		{
			icon.setIcon(HoopLink.getImageByName("led-green.png"));
			setBorder(BorderFactory.createCompoundBorder(HoopShadowBorder.getSharedInstance(), raisedRed));
			setWaiting (true);
			return;			
		}		
		
		// If we get an undefined state, fall back to STOPPED ...
		
		icon.setIcon(HoopLink.getImageByName("led-yellow.png"));
		setBorder(BorderFactory.createCompoundBorder(HoopShadowBorder.getSharedInstance(), BorderFactory.createBevelBorder(BevelBorder.RAISED)));
		setWaiting (false);		
	}
	/**
	 * 
	 */
	@Override
	public void mouseClicked(MouseEvent arg0) 
	{
		// Not implemented, see baseclass
	}
	/**
	 * 
	 */	
	@Override
	public void mouseEntered(MouseEvent arg0) 
	{
		// Not implemented, see baseclass
	}
	/**
	 * 
	 */	
	@Override
	public void mouseExited(MouseEvent arg0) 
	{
		// Not implemented, see baseclass
	}
	/**
	 * 
	 */	
	@Override
	public void mousePressed(MouseEvent arg0) 
	{
		debug ("mousePressed ()");
		
		super.mousePressed(arg0);
		
		propPanel.highlightHoop(hoop);
	}
	/**
	 * 
	 */	
	@Override
	public void mouseReleased(MouseEvent arg0) 
	{
		// Not implemented, see baseclass
	}	
	/**
	 * 
	 */
	public HoopProgressPainter getProgressPainter ()
	{
		return (progressPanel);
	}
	/**
	 * 
	 */
	@Override
	public void actionPerformed(ActionEvent event) 
	{
		debug ("actionPerformed ("+event.getSource().getClass().getSimpleName()+")");
		
		if (event.getSource().equals(blinker)==false)
		{
			super.actionPerformed(event);
			return;
		}	
		
		icon.setIcon(HoopLink.getImageByName("led-yellow.png"));
		
		blinker.stop();
		blinker=null;
		
		setState ("STOPPED");
		
		blinking=false;
	}	
}

