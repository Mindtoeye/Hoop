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

package edu.cmu.cs.in.controls.base;

//import java.awt.GraphicsConfiguration;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
//import java.net.URL;

//import javax.swing.JFrame;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
//import javax.swing.JFrame;
import javax.swing.JPanel;

import edu.cmu.cs.in.base.HoopRoot;

/** 
 * @author vvelsen
 *
 */
public class HoopJPanel extends JPanel implements ComponentListener 
{
	private static final long serialVersionUID = -6522707440476438254L;
	private String className="HoopBase";	
	private String instanceName="Undefined";
	
    protected ImageIcon defaultIcon=null;	
	
	/**
	 * Creates a new JPanel with a double buffer and a flow layout.
	 */	
	public HoopJPanel()
	{
		setClassName ("HoopJPanel");
		debug ("HoopJPanel ()");
		
		this.addComponentListener(this);
	}
	/**
	 * Creates a new JPanel with FlowLayout and the specified buffering strategy.
	 */	
	public HoopJPanel(boolean isDoubleBuffered)
	{
		super (isDoubleBuffered);
		setClassName ("HoopJPanel");
		debug ("HoopJPanel ()");		
		
		this.addComponentListener(this);
	}    
	/**
	 * Create a new buffered JPanel with the specified layout manager
	 */	
	public HoopJPanel(LayoutManager layout)
	{
		super (layout);
		setClassName ("HoopJPanel");
		debug ("HoopJPanel ()");		
		
		this.addComponentListener(this);
	}
	/**
	 * Creates a new JPanel with the specified layout manager and buffering strategy.
	 */	
	public HoopJPanel(LayoutManager layout, boolean isDoubleBuffered)
	{
		super (layout,isDoubleBuffered);
		setClassName ("HoopJPanel");
		debug ("HoopJPanel ()");		
		
		this.addComponentListener(this);
	}
	/**
	 *
	 */
	public void setClassName (String aName)
	{
		className=aName;
	}
	/**
	 *
	 */
	public String getClassName ()
	{
		return (className);
	}	
	/**
	 *
	 */	
	public String getInstanceName() 
	{
		return instanceName;
	}
	/**
	 *
	 */	
	public void setInstanceName(String instanceName) 
	{
		this.instanceName = instanceName;
	}		
	/**
	 * 
	 */
	protected void debug (String aMessage)
	{
		HoopRoot.debug(getClassName(),aMessage);
	}
	/**
	 * 
	 */	
	public Box addInHorizontalLayout (Component comp,int maxX,int maxY) 
	{
		Box dynamicBox=new Box (BoxLayout.X_AXIS);
		dynamicBox.setMinimumSize(new Dimension (20,20));
		dynamicBox.setMaximumSize(new Dimension (maxX,maxY));
		dynamicBox.add(comp);
		return (dynamicBox);
	}
	/**
	 * 
	 */	
	public Box addInVerticalLayout (Component comp) 
	{
		Box dynamicBox=new Box (BoxLayout.Y_AXIS);
		dynamicBox.add(comp);
		return (dynamicBox);
	}		
	/**
	 * 
	 */
	public void centerWindow (JPanel aWindow)
	{
		debug ("centerWindow ()");
		
		// Get the size of the screen
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		 
		// Determine the new location of the window
		int w = aWindow.getSize().width;
		int h = aWindow.getSize().height;
		int x = (dim.width-w)/2;
		int y = (dim.height-h)/2;
		 
		// Move the window
		aWindow.setLocation(x, y);		
	}
	/**
	 * 
	 */
	protected void alert (String aMessage)
	{
		HoopRoot.alert(aMessage);
	}
	/**
	 * 
	 */
	@Override
	public void componentHidden (ComponentEvent arg0) 
	{
		debug ("componentHidden ()");
	}
	/**
	 * 
	 */	
	@Override
	public void componentMoved(ComponentEvent arg0) 
	{
		debug ("componentMoved ()");		
	}
	/**
	 * 
	 */	
	@Override
	public void componentResized(ComponentEvent arg0) 
	{
		debug ("componentHidden ()");
		
		updateSize();	
	}
	/**
	 * 
	 */	
	@Override
	public void componentShown(ComponentEvent arg0) 
	{
		debug ("componentHidden ()");
		
		updateSize();	
	}
	/**
	 *
	 */	
	public void updateSize() 
	{
		debug ("updateSize ()");
		
		// Implement in child class !!
	}			
}
