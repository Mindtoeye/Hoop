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
import edu.cmu.cs.in.base.INHoopLink;
import edu.cmu.cs.in.controls.base.INEmbeddedJPanel;
//import edu.cmu.cs.in.controls.base.INJInternalFrame;

/** 
 * @author vvelsen
 *
 */
public class INHoopConsole extends INEmbeddedJPanel implements INHoopConsoleInterface, ActionListener
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
		//super("Console", true, true, true, true);
		
		setClassName ("INHoopConsole");
		debug ("INHoopConsole ()");
		
		consoleData=new INFixedSizeQueue<String>(consoleSize);
		
		setLayout(new BoxLayout (this,BoxLayout.Y_AXIS));
				
		clearButton=new JButton ();
		clearButton.setIcon(INHoopLink.imageIcons [8]);
		clearButton.setMargin(new Insets(1, 1, 1, 1));
		//clearButton.setText("Clear");
		clearButton.setFont(new Font("Courier",1,8));
		clearButton.setPreferredSize(new Dimension (16,16));
		clearButton.addActionListener(this);
		
		saveButton=new JButton ();	
		saveButton.setIcon(INHoopLink.imageIcons [19]);
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
		setButton.setIcon(INHoopLink.imageIcons [22]);
		setButton.setMargin(new Insets(1, 1, 1, 1));
		setButton.setFont(new Font("Courier",1,8));
		setButton.setPreferredSize(new Dimension (16,16));
		setButton.addActionListener(this);
		
		inButton=new JButton ();
		//setButton.setText("Set");
		inButton.setIcon(INHoopLink.imageIcons [72]);
		inButton.setMargin(new Insets(1, 1, 1, 1));
		inButton.setFont(new Font("Courier",1,8));
		inButton.setPreferredSize(new Dimension (16,16));
		inButton.addActionListener(this);
		
		outButton=new JButton ();
		//setButton.setText("Set");
		outButton.setIcon(INHoopLink.imageIcons [73]);
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
		
		//this.add(mainBox);
		
		// We should be ready for action now
		
		INHoopLink.console=this;
		
		setContentPane (mainBox);
		//setSize (425,300);
		//setLocation (75,75);
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
	/**
	 * 
	 */
	protected void processClose ()
	{
		debug ("processClose ()");
		
		INHoopLink.console=this;
	}	
}
