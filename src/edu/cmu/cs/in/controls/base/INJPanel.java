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
import java.awt.LayoutManager;

import javax.swing.JFrame;
import javax.swing.JPanel;

import edu.cmu.cs.in.base.INFeatureMatrixBase;

/** 
 * @author vvelsen
 *
 */
public class INJPanel extends JPanel 
{
	private static final long serialVersionUID = -6522707440476438254L;
	private String className="INFeatureMatrixBase";	
	
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
	protected void debug (String aMessage)
	{
		INFeatureMatrixBase.debug(getClassName(),aMessage);
	}
}
