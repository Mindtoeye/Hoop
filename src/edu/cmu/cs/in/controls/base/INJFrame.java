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

import java.awt.GraphicsConfiguration;
import javax.swing.JFrame;

import edu.cmu.cs.in.base.INBase;

/** 
 * @author vvelsen
 *
 */
public class INJFrame extends JFrame 
{

	private static final long serialVersionUID = 7391090164244209176L;
	private String className="INBase";	
	
	/**
	 * Constructs a new frame that is initially invisible.
	 */	
	public INJFrame()
	{
		setClassName ("INJFrame");
		debug ("INJFrame ()");
	}
	/**
	 * Creates a Frame in the specified GraphicsConfiguration of a screen device and a blank title.
	 */	
	public INJFrame(GraphicsConfiguration gc)
	{
		super (gc);
		setClassName ("INJFrame");
		debug ("INJFrame ()");
	}
	/**
	 * Creates a new, initially invisible Frame with the specified title.
	 */	
	public INJFrame(String title)
	{
		super (title);
		setClassName ("INJFrame");
		debug ("INJFrame ()");
	}
	/**
	 * Creates a JFrame with the specified title and the specified GraphicsConfiguration of a screen device.
	 */	
	public INJFrame(String title, GraphicsConfiguration gc)
	{
		super (title,gc);
		setClassName ("INJFrame");
		debug ("INJFrame ()");
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
	protected void debug (String aMessage)
	{
		INBase.debug(getClassName(),aMessage);
	}
}