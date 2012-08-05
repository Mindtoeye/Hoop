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
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import edu.cmu.cs.in.base.HoopFixedSizeQueue;
import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.controls.base.HoopEmbeddedJPanel;
//import edu.cmu.cs.in.controls.base.HoopJInternalFrame;

/** 
 * @author vvelsen
 *
 */
public class HoopConsole extends HoopEmbeddedJPanel implements HoopConsoleInterface, ActionListener
{	
	private static final long serialVersionUID = 7326548847413008855L;

	private JTextArea console=null;
	private int consoleSize=200; // Only show 200 lines
	private HoopFixedSizeQueue <String>consoleData=null;
	
	private JButton clearButton=null;
	private JButton saveButton=null;	
	private JTextField maxLines=null;
	private JButton setButton=null;
	private JButton inButton=null;
	private JButton outButton=null;	
	
	private JButton pauseButton=null;
	
	private JCheckBox filterClassCheck=null;
	private JComboBox filterClass=null;
	private JButton updateClasses=null;
	
	private int fontSize=8;
	
	private Boolean logPaused=false;
	
	private Hashtable<String,String> classTable=null; 
	
	/**
	 * 
	 */	
	public HoopConsole()
	{
		//super("Console", true, true, true, true);
		
		setClassName ("HoopConsole");
		debug ("HoopConsole ()");
		
		classTable = new Hashtable<String, String>();
		
		consoleData=new HoopFixedSizeQueue<String>(consoleSize);
		
		setLayout(new BoxLayout (this,BoxLayout.Y_AXIS));
				
		clearButton=new JButton ();
		clearButton.setIcon(HoopLink.imageIcons [8]);
		clearButton.setMargin(new Insets(1, 1, 1, 1));
		//clearButton.setText("Clear");
		clearButton.setFont(new Font("Courier",1,8));
		clearButton.setPreferredSize(new Dimension (20,20));
		clearButton.addActionListener(this);
		
		saveButton=new JButton ();	
		saveButton.setIcon(HoopLink.imageIcons [19]);
		saveButton.setMargin(new Insets(1, 1, 1, 1));
		//saveButton.setText("Save ...");
		saveButton.setFont(new Font("Courier",1,8));
		saveButton.setPreferredSize(new Dimension (20,20));
		saveButton.addActionListener(this);
		
		maxLines=new JTextField ();
		maxLines.setText(String.format("%d",consoleSize));
		maxLines.setFont(new Font("Courier",1,fontSize));
		maxLines.setPreferredSize(new Dimension (40,25));
		maxLines.setMaximumSize(new Dimension (40,25));
		
		setButton=new JButton ();
		//setButton.setText("Set");
		setButton.setIcon(HoopLink.imageIcons [22]);
		setButton.setMargin(new Insets(1, 1, 1, 1));
		setButton.setFont(new Font("Courier",1,8));
		setButton.setPreferredSize(new Dimension (20,20));
		setButton.addActionListener(this);
		
		inButton=new JButton ();
		//setButton.setText("Set");
		inButton.setIcon(HoopLink.imageIcons [72]);
		inButton.setMargin(new Insets(1, 1, 1, 1));
		inButton.setFont(new Font("Courier",1,8));
		inButton.setPreferredSize(new Dimension (20,20));
		inButton.addActionListener(this);
		
		outButton=new JButton ();
		//setButton.setText("Set");
		outButton.setIcon(HoopLink.imageIcons [73]);
		outButton.setMargin(new Insets(1, 1, 1, 1));
		outButton.setFont(new Font("Courier",1,8));
		outButton.setPreferredSize(new Dimension (20,20));
		outButton.addActionListener(this);
		
		pauseButton=new JButton ();
		//setButton.setText("Set");
		pauseButton.setIcon(HoopLink.getImageByName("player-pause.png"));
		pauseButton.setMargin(new Insets(1, 1, 1, 1));
		pauseButton.setFont(new Font("Courier",1,8));
		pauseButton.setPreferredSize(new Dimension (20,20));
		pauseButton.addActionListener(this);		
		
		filterClassCheck=new JCheckBox ();
		filterClassCheck.setText("Filter on class name:");
		filterClassCheck.setFont(new Font("Courier",1,9));
		filterClassCheck.setPreferredSize(new Dimension (150,20));
		filterClassCheck.setMaximumSize(new Dimension (150,20));		
		
		filterClass = new JComboBox();
		filterClass.setFont(new Font("Courier",1,9));
		filterClass.setPreferredSize(new Dimension (150,20));
		filterClass.setMaximumSize(new Dimension (150,20));
		
		updateClasses=new JButton ();
		//setButton.setText("Set");
		updateClasses.setIcon(HoopLink.getImageByName("redo.gif"));
		updateClasses.setMargin(new Insets(1, 1, 1, 1));
		updateClasses.setFont(new Font("Courier",1,8));
		updateClasses.setPreferredSize(new Dimension (20,20));
		updateClasses.addActionListener(this);				
		
		Box controlBox = new Box (BoxLayout.X_AXIS);
		controlBox.add(clearButton);
		controlBox.add (Box.createRigidArea(new Dimension(2,0)));
		controlBox.add(saveButton);
		controlBox.add (Box.createRigidArea(new Dimension(2,0)));
		controlBox.add(maxLines);
		controlBox.add (Box.createRigidArea(new Dimension(2,0)));
		controlBox.add(setButton);
		controlBox.add (Box.createRigidArea(new Dimension(2,0)));
		controlBox.add(inButton);
		controlBox.add (Box.createRigidArea(new Dimension(2,0)));
		controlBox.add(outButton);
		controlBox.add (Box.createRigidArea(new Dimension(2,0)));
		
		controlBox.add(pauseButton);
		controlBox.add (Box.createRigidArea(new Dimension(2,0)));
		
		controlBox.add(filterClassCheck);
		controlBox.add (Box.createRigidArea(new Dimension(2,0)));
		controlBox.add(filterClass);
		controlBox.add (Box.createRigidArea(new Dimension(2,0)));
		controlBox.add(updateClasses);		
		controlBox.add (Box.createRigidArea(new Dimension(2,0)));
		
		controlBox.add(Box.createHorizontalGlue());
		
		controlBox.setMinimumSize(new Dimension (150,24));
		controlBox.setPreferredSize(new Dimension (150,24));		
		
		console=new JTextArea ();
		console.setEditable (false);
	    console.setFont(new Font("Courier",1,8));
		console.setMinimumSize(new Dimension (50,150));
						
		JScrollPane consoleContainer = new JScrollPane (console);
		consoleContainer.setMinimumSize(new Dimension (50,100));
		//consoleContainer.setPreferredSize(new Dimension (500,100));

		Box mainBox = new Box (BoxLayout.Y_AXIS);
		
		mainBox.add(controlBox);
		mainBox.add (Box.createRigidArea(new Dimension(0,2)));
		mainBox.add(consoleContainer);
								
		HoopLink.console=this;
		
		// We should be ready for action now
		
		setContentPane (mainBox);
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
		
		if (button==updateClasses)
		{
			fillClassCombo ();
		}
		
		if (button==pauseButton)
		{
			if (logPaused==true)
			{
				logPaused=false;
				pauseButton.setIcon(HoopLink.getImageByName("player-pause.png"));
			}
			else
			{
				logPaused=true;
				pauseButton.setIcon(HoopLink.getImageByName("player-play.png"));
			}	
		}
	}
	/**
	 * 
	 */
	protected void processClose ()
	{
		debug ("processClose ()");
		
		HoopLink.console=this;
	}
	/**
	 * DO NOT CALL DEBUG Hoop THIS METHOD!!
	 */
	public void appendString (String aMessage)
	{		
		if (logPaused==true)
			return;
		
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
		
		processString (aMessage);
	}	
	/**
	 * DO NOT CALL DEBUG Hoop THIS METHOD!!
	 */
	private void processString (String aString)
	{
		int startIndex=aString.indexOf("<");
		int endIndex=aString.indexOf(">");
		
		if ((startIndex!=-1) && (endIndex!=-1))
		{
			if (endIndex>startIndex)
			{
				String classIdent=aString.substring(startIndex+1,endIndex);
				
				classTable.put(classIdent, classIdent);
			}
		}
	}
	/**
	 * DO NOT CALL DEBUG Hoop THIS METHOD!!
	 */	
	private void fillClassCombo ()
	{
		filterClass.removeAllItems();
		filterClass.addItem ("ALL");
		
		Enumeration<String> names; 
		names = classTable.keys();
		while(names.hasMoreElements()) 
		{
			String str = (String) names.nextElement();
			//System.out.println(str + ": " +	names.get(str));
			filterClass.addItem (str);
		}
	}
}
