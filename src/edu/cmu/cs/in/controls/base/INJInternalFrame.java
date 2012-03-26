/** 
 * Author: Martin van Velsen <vvelsen@cs.cmu.edu>
 * 
 * This source set uses code written for other various graduate 
 * courses and is part of a larger research effort in the field of
 * interactive narrative (IN). Courses that have contribute
 * to this source base are:
 * 
 * 	05-834 Applied Machine Learning
 *  11-719 Computational Models of Discourse Analysis
 *  11-741 Information Retrieval
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
