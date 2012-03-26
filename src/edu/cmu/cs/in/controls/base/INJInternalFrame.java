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

import javax.swing.JInternalFrame;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import edu.cmu.cs.in.base.INBase;
import edu.cmu.cs.in.base.INLink;

/** 
 * @author vvelsen
 *
 */
public class INJInternalFrame extends JInternalFrame implements InternalFrameListener
{
	private static final long serialVersionUID = -2840625729169060134L;
	private String className="INBase";
	private String instanceName="Undefined";
	
	/**
	 * Constructs a new frame that is initially invisible.
	 */	
	public INJInternalFrame()
	{
		setClassName ("INJInternalFrame");
		debug ("INJInternalFrame ()");
		
		//INLink.addWindow(this);
		this.addInternalFrameListener(this);
	}
	/**
	 * Creates a non-resizable, non-closable, non-maximizable, non-iconifiable JInternalFrame with the specified title.
	 */
	public INJInternalFrame(String title)
	{
		super (title);
		
		setClassName ("INJInternalFrame");
		debug ("INJInternalFrame ()");
		
		//INLink.addWindow(this);
		this.addInternalFrameListener(this);		
	}
	/**
	 * Creates a non-closable, non-maximizable, non-iconifiable JInternalFrame 
	 * with the specified title and resizability.
	 */
	public INJInternalFrame(String title, boolean resizable)
	{
		super (title,resizable);
		
		setClassName ("INJInternalFrame");
		debug ("INJInternalFrame ()");

		//INLink.addWindow(this);
		this.addInternalFrameListener(this);		
	}	
	/**
	 * Creates a non-maximizable, non-iconifiable JInternalFrame with the specified title, 
	 * resizability, and closability.
	 */
	public INJInternalFrame(String title, boolean resizable, boolean closable)
	{
		super (title,resizable,closable);
		
		setClassName ("INJInternalFrame");
		debug ("INJInternalFrame ()");
		
		//INLink.addWindow(this);
		this.addInternalFrameListener(this);		
	}
	/**
	 * Creates a non-iconifiable JInternalFrame with the specified title, resizability, 
	 * closability, and maximizability.
	 */
	public INJInternalFrame(String title, boolean resizable, boolean closable, boolean maximizable)
	{
		super (title,resizable,closable,maximizable);
		
		setClassName ("INJInternalFrame");
		debug ("INJInternalFrame ()");
		
		//INLink.addWindow(this);
		this.addInternalFrameListener(this);		
	}	
	/**
	 * Creates a JInternalFrame with the specified title, resizability, closability, maximizability, 
	 * and iconifiability.
	 */
	public INJInternalFrame(String title, boolean resizable, boolean closable, boolean maximizable, boolean iconifiable)
	{
		super (title,resizable,closable,maximizable,iconifiable);
		
		setClassName ("INJInternalFrame");
		debug ("INJInternalFrame ()");
		
		//INLink.addWindow(this);
		this.addInternalFrameListener(this);		
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
	public void updateContents() 
	{
		debug ("updateContents ()");
		// Implement in child class!!
	}
	/**
	 *
	 */	
	@Override
	public void internalFrameActivated(InternalFrameEvent arg0) 
	{
		debug ("internalFrameActivated ()");
	}
	/**
	 *
	 */	
	@Override
	public void internalFrameClosed(InternalFrameEvent arg0) 
	{
		debug ("internalFrameClosed ()");
	}
	/**
	 *
	 */	
	@Override
	public void internalFrameClosing(InternalFrameEvent arg0) 
	{
		debug ("internalFrameClosing ()");
		processClose ();
		//INLink.removeWindow(this);
	}
	/**
	 *
	 */	
	@Override
	public void internalFrameDeactivated(InternalFrameEvent arg0) 
	{
		debug ("internalFrameDeactivated ()");		
	}
	/**
	 *
	 */	
	@Override
	public void internalFrameDeiconified(InternalFrameEvent arg0) 
	{
		debug ("internalFrameDeiconified ()");		
	}
	/**
	 *
	 */	
	@Override
	public void internalFrameIconified(InternalFrameEvent arg0) 
	{
		debug ("internalFrameIconified ()");		
	}
	/**
	 *
	 */	
	@Override
	public void internalFrameOpened(InternalFrameEvent arg0) 
	{
		debug ("internalFrameOpened ()");		
	}
	/**
	 * 
	 */
	protected void processClose ()
	{
		debug ("processClose ()");
		// handle in child window!
	}
}
