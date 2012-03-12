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
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
public class INHoopConsole extends INJPanel implements INHoopConsoleInterface, ActionListener
{	
	private static final long serialVersionUID = 7326548847413008855L;

	private JTextArea console=null;
	private int consoleSize=200; // Only show 200 lines
	private INFixedSizeQueue <String>consoleData=null;
	
	private JButton clearButton=null;
	private JButton saveButton=null;	
	private JTextField maxLines=null;
	private JButton setButton=null;
	private JButton inButton=null;
	private JButton outButton=null;	
	
	private int fontSize=8;
	
	/**
	 * 
	 */	
	public INHoopConsole()
	{
		setClassName ("INHoopConsole");
		debug ("INHoopConsole ()");
		
		consoleData=new INFixedSizeQueue<String>(consoleSize);
		
		setLayout(new BoxLayout (this,BoxLayout.Y_AXIS));
				
		clearButton=new JButton ();
		clearButton.setIcon(INLink.imageIcons [8]);
		clearButton.setMargin(new Insets(1, 1, 1, 1));
		//clearButton.setText("Clear");
		clearButton.setFont(new Font("Courier",1,8));
		clearButton.setPreferredSize(new Dimension (16,16));
		clearButton.addActionListener(this);
		
		saveButton=new JButton ();	
		saveButton.setIcon(INLink.imageIcons [19]);
		saveButton.setMargin(new Insets(1, 1, 1, 1));
		//saveButton.setText("Save ...");
		saveButton.setFont(new Font("Courier",1,8));
		saveButton.setPreferredSize(new Dimension (16,16));
		saveButton.addActionListener(this);
		
		maxLines=new JTextField ();
		maxLines.setText(String.format("%d",consoleSize));
		maxLines.setFont(new Font("Courier",1,fontSize));
		maxLines.setPreferredSize(new Dimension (40,25));
		maxLines.setMaximumSize(new Dimension (40,25));
		
		setButton=new JButton ();
		//setButton.setText("Set");
		setButton.setIcon(INLink.imageIcons [22]);
		setButton.setMargin(new Insets(1, 1, 1, 1));
		setButton.setFont(new Font("Courier",1,8));
		setButton.setPreferredSize(new Dimension (16,16));
		setButton.addActionListener(this);
		
		inButton=new JButton ();
		//setButton.setText("Set");
		inButton.setIcon(INLink.imageIcons [72]);
		inButton.setMargin(new Insets(1, 1, 1, 1));
		inButton.setFont(new Font("Courier",1,8));
		inButton.setPreferredSize(new Dimension (16,16));
		inButton.addActionListener(this);
		
		outButton=new JButton ();
		//setButton.setText("Set");
		outButton.setIcon(INLink.imageIcons [73]);
		outButton.setMargin(new Insets(1, 1, 1, 1));
		outButton.setFont(new Font("Courier",1,8));
		outButton.setPreferredSize(new Dimension (16,16));
		outButton.addActionListener(this);
		
		Box controlBox = new Box (BoxLayout.Y_AXIS);
		controlBox.add(clearButton);
		controlBox.add(saveButton);
		controlBox.add(maxLines);
		controlBox.add(setButton);
		controlBox.add(inButton);
		controlBox.add(outButton);
		controlBox.setMinimumSize(new Dimension (24,150));
		controlBox.setPreferredSize(new Dimension (24,150));
		
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
	/**
	 * 
	 */	
	@Override
	public void actionPerformed(ActionEvent event) 
	{
		debug ("actionPerformed ()");
		
		String act=event.getActionCommand();
		JButton button = (JButton)event.getSource();		

		if (button==clearButton)
		{
			console.setText("");
		}
		
		if (button==saveButton)
		{
			
		}		
		
		if (button==setButton)
		{
			
		}
		
		if (button==inButton)
		{
			fontSize++;
			
			if (fontSize>23)
				fontSize=23;
			
			console.setFont(new Font("Courier",1,fontSize));
		}
		
		if (button==outButton)
		{
			fontSize--;
			
			if (fontSize<1)
				fontSize=1;
			
			console.setFont(new Font("Courier",1,fontSize));			
		}							
	}
}
