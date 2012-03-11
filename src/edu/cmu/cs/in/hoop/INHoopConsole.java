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

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import edu.cmu.cs.in.base.INFixedSizeQueue;
import edu.cmu.cs.in.base.INLink;
import edu.cmu.cs.in.controls.base.INJPanel;

/** 
 * @author vvelsen
 *
 */
public class INHoopConsole extends INJPanel implements INHoopConsoleInterface
{	
	private static final long serialVersionUID = 7326548847413008855L;

	private JTextArea console=null;
	private int consoleSize=200; // Only show 200 lines
	private INFixedSizeQueue <String>consoleData=null;
	
	/**
	 * 
	 */	
	public INHoopConsole()
	{
		setClassName ("INHoopConsole");
		debug ("INHoopConsole ()");
		
		consoleData=new INFixedSizeQueue<String>(consoleSize);
		
		setLayout(new BoxLayout (this,BoxLayout.Y_AXIS));
				
		JButton clearButton=new JButton ();
		clearButton.setText("Clear");
		clearButton.setFont(new Font("Courier",1,8));
		clearButton.setPreferredSize(new Dimension (100,25));
		
		JButton saveButton=new JButton ();	
		saveButton.setText("Save ...");
		saveButton.setFont(new Font("Courier",1,8));
		saveButton.setPreferredSize(new Dimension (100,25));
		
		JTextField maxLines=new JTextField ();
		maxLines.setText(String.format("%d",consoleSize));
		maxLines.setFont(new Font("Courier",1,8));
		maxLines.setPreferredSize(new Dimension (100,25));
		maxLines.setMaximumSize(new Dimension (100,25));		
		
		JButton setButton=new JButton ();
		setButton.setText("Set");
		setButton.setFont(new Font("Courier",1,8));
		setButton.setPreferredSize(new Dimension (70,25));		
		
		Box controlBox = new Box (BoxLayout.Y_AXIS);
		controlBox.add(clearButton);
		controlBox.add(saveButton);
		controlBox.add(maxLines);
		controlBox.add(setButton);
		controlBox.setMinimumSize(new Dimension (100,150));
		controlBox.setPreferredSize(new Dimension (100,150));
		
		console=new JTextArea ();
		console.setEditable (false);
	    console.setFont(new Font("Courier",1,8));
		console.setMinimumSize(new Dimension (50,150));
						
		JScrollPane consoleContainer = new JScrollPane (console);
		consoleContainer.setMinimumSize(new Dimension (50,150));
		consoleContainer.setPreferredSize(new Dimension (500,150));
			
		Box mainBox = new Box (BoxLayout.X_AXIS);
		
		mainBox.add(controlBox);
		mainBox.add(consoleContainer);
		
		this.add(mainBox);
		
		// We should be ready for action now
		
		INLink.console=this;
	}
	/**
	 * 
	 */	
	public int getConsoleSize() 
	{
		return consoleSize;
	}
	/**
	 * 
	 */	
	public void setConsoleSize(int consoleSize) 
	{
		this.consoleSize = consoleSize;
	}	
	/**
	 * 
	 */		
	public void appendString (String aMessage)
	{		
		consoleData.add (aMessage);
		
		if (console!=null)
		{
			console.setText(""); // Reset
	
			//System.out.println ("Console size: " + consoleData.size ());
			
			for (int i=0;i<consoleData.size();i++)
			{			
				String aString=consoleData.get(i);
				console.append(aString);
			}
			
			// Scroll to bottom
			console.setCaretPosition(console.getDocument().getLength());
		}	
	}
}
