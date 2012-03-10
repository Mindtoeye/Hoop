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

package edu.cmu.cs.in.hoop;

import java.awt.Dimension;
import java.awt.Font;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BoxLayout;
import javax.swing.JTextField;

import edu.cmu.cs.in.controls.base.INJPanel;

/** 
 * @author vvelsen
 *
 */
public class INHoopStatusBar extends INJPanel
{	
	
	class UpdateTimeTask extends TimerTask 
	{
		public void run() 
		{
			//System.out.println ("tick");
			update ();
		}
	}	
	
	private static final long serialVersionUID = -1L;
	
	private JTextField memory=null;
	private JTextField keys=null;
	private JTextField padding=null;
	
	/**
	 * 
	 */	
	public INHoopStatusBar()
	{
		setClassName ("INHoopStatusBar");
		debug ("INHoopStatusBar ()");
	
		setLayout(new BoxLayout (this,BoxLayout.X_AXIS));
		
		memory=new JTextField ();
		memory.setEditable(false);
		memory.setFont(new Font("Dialog", 1, 10));
		memory.setMinimumSize(new Dimension (250,23));
		memory.setPreferredSize(new Dimension (250,23));
		memory.setMaximumSize(new Dimension (250,23));
		
		keys=new JTextField ();
		keys.setEditable(false);
		keys.setFont(new Font("Dialog", 1, 10));
		keys.setMinimumSize(new Dimension (100,23));
		keys.setPreferredSize(new Dimension (100,23));
		keys.setMaximumSize(new Dimension (100,23));
		
		padding=new JTextField ();
		padding.setEditable(false);
		padding.setFont(new Font("Dialog", 1, 10));
		padding.setMinimumSize(new Dimension (50,23));
		padding.setPreferredSize(new Dimension (5000,23));
		padding.setMaximumSize(new Dimension (5000,23));
		
		this.add(memory);
		this.add(keys);
		this.add(padding);
		
		update ();
		
		Timer timer=new Timer();
		timer.schedule(new UpdateTimeTask (),0,1000L);
	}
	/**
	 * 
	 */	
	public void update ()
	{
		StringBuffer formatted=new StringBuffer ();
		formatted.append ("Total: ");
		formatted.append (Long.toString(Runtime.getRuntime().totalMemory()/1024));
		formatted.append ("K Max: ");
		formatted.append (Long.toString(Runtime.getRuntime().maxMemory()/1024));
		formatted.append ("K Free: ");
		formatted.append (Long.toString(Runtime.getRuntime().freeMemory()/1024));
		formatted.append ("K");
		
		memory.setText(formatted.toString());
		
		keys.setText("N | C | S - I/O");
		
		padding.setText(" ");
	}
}
