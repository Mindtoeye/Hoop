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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
//import javax.swing.BoxLayout;
import javax.swing.border.Border;

import edu.cmu.cs.in.base.INLink;

/** 
 * @author vvelsen
 *
 */
public class INEmbeddedJPanel extends INJPanel
{	
	private static final long serialVersionUID = 1L;
		
	/**
	 * Creates a new JPanel with a double buffer and a flow layout.
	 */	
	public INEmbeddedJPanel()
	{
		setClassName ("INEmbeddedJPanel");
		debug ("INEmbeddedJPanel ()");
		
		this.setLayout(new BorderLayout(2,2));
		Border border=BorderFactory.createLineBorder(Color.black);
		this.setBorder(border);					
	}
	/**
	 * 
	 */
	protected void setContentPane (Component aChild)
	{
		debug ("setContentPane ()");
		
		this.add(aChild);
	}
	/**
	 * 
	 */
	public void close ()
	{
		handleCloseEvent ();
		INLink.removeWindow(this);
	}
	/**
	 * 
	 */
	public void handleCloseEvent ()
	{
		debug ("handleCloseEvent ()");
		
		// Process this in child class!!
	}
	/**
	 *
	 */	
	public void updateContents() 
	{
		debug ("updateContents ()");
		// Implement in child class!!
	}	
}
