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
//import java.net.URL;

//import javax.swing.JFrame;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
//import javax.swing.JFrame;
import javax.swing.JPanel;

import edu.cmu.cs.in.base.INBase;

/** 
 * @author vvelsen
 *
 */
public class INJPanel extends JPanel 
{
	private static final long serialVersionUID = -6522707440476438254L;
	private String className="INBase";	
	private String instanceName="Undefined";
	
    protected ImageIcon defaultIcon=null;	
	
	/**
	 * Creates a new JPanel with a double buffer and a flow layout.
	 */	
	public INJPanel()
	{
		setClassName ("INJPanel");
		debug ("INJPanel ()");
	}
	/**
	 * Creates a new JPanel with FlowLayout and the specified buffering strategy.
	 */	
	public INJPanel(boolean isDoubleBuffered)
	{
		super (isDoubleBuffered);
		setClassName ("INJPanel");
		debug ("INJPanel ()");		
	}    
	/**
	 * Create a new buffered JPanel with the specified layout manager
	 */	
	public INJPanel(LayoutManager layout)
	{
		super (layout);
		setClassName ("INJPanel");
		debug ("INJPanel ()");		
	}
	/**
	 * Creates a new JPanel with the specified layout manager and buffering strategy.
	 */	
	public INJPanel(LayoutManager layout, boolean isDoubleBuffered)
	{
		super (layout,isDoubleBuffered);
		setClassName ("INJPanel");
		debug ("INJPanel ()");		
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
		INBase.debug(getClassName(),aMessage);
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
		INBase.alert(aMessage);
	}
}
