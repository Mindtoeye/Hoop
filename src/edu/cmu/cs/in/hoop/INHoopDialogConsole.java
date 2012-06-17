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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import edu.cmu.cs.in.base.INHoopLink;
import edu.cmu.cs.in.controls.base.INEmbeddedJPanel;
import edu.cmu.cs.in.hoop.hoops.INHoopStdin;
import edu.cmu.cs.in.hoop.hoops.INHoopStdout;

/** 
 * @author vvelsen
 *
 */
public class INHoopDialogConsole extends INEmbeddedJPanel implements KeyListener, ActionListener
{	
	private static final long serialVersionUID = 1L;
	
    private JTextArea entry=null;
    private JTextArea textArea=null;
    
    private INHoopStdin inHoop=null;
    private INHoopStdout outHoop=null;
    
	private int consoleSize=200; // Only show 200 lines
	
	private JButton clearButton=null;
	private JButton saveButton=null;	
	private JTextField maxLines=null;
	private JButton setButton=null;
	private JButton inButton=null;
	private JButton outButton=null;	    
	private JCheckBox submitEnter=null;
		
	/**
	 * 
	 */	
	public INHoopDialogConsole()
	{
		setClassName ("INHoopDialogConsole");
		debug ("INHoopDialogConsole ()");
		
		Box mainBox = new Box (BoxLayout.Y_AXIS);
		
		Border blackborder=BorderFactory.createLineBorder(Color.black);
		
		entry=new JTextArea ();
		entry.setFont(new Font("Dialog", 1, 10));
		entry.setBorder(blackborder);
		entry.setMinimumSize(new Dimension (50,25));
		entry.setPreferredSize(new Dimension (100,25));		
		entry.addKeyListener(this);		
						
		clearButton=new JButton ();
		clearButton.setIcon(INHoopLink.imageIcons [8]);
		clearButton.setMargin(new Insets(1, 1, 1, 1));
		//clearButton.setText("Clear");
		clearButton.setFont(new Font("Courier",1,8));
		clearButton.setPreferredSize(new Dimension (20,16));
		clearButton.addActionListener(this);
		
		saveButton=new JButton ();	
		saveButton.setIcon(INHoopLink.imageIcons [19]);
		saveButton.setMargin(new Insets(1, 1, 1, 1));
		saveButton.setFont(new Font("Courier",1,8));
		saveButton.setPreferredSize(new Dimension (20,16));
		saveButton.addActionListener(this);
		
		maxLines=new JTextField ();
		maxLines.setText(String.format("%d",consoleSize));
		//maxLines.setFont(new Font("Courier",1,fontSize));
		maxLines.setPreferredSize(new Dimension (40,25));
		maxLines.setMaximumSize(new Dimension (40,25));
		
		setButton=new JButton ();
		//setButton.setText("Set");
		setButton.setIcon(INHoopLink.imageIcons [22]);
		setButton.setMargin(new Insets(1, 1, 1, 1));
		setButton.setFont(new Font("Courier",1,8));
		setButton.setMinimumSize(new Dimension (20,16));
		setButton.setPreferredSize(new Dimension (20,16));
		setButton.addActionListener(this);
		
		inButton=new JButton ();
		//setButton.setText("Set");
		inButton.setIcon(INHoopLink.imageIcons [72]);
		inButton.setMargin(new Insets(1, 1, 1, 1));
		inButton.setFont(new Font("Courier",1,8));
		inButton.setPreferredSize(new Dimension (20,16));
		inButton.addActionListener(this);
		
		outButton=new JButton ();
		//setButton.setText("Set");
		outButton.setIcon(INHoopLink.imageIcons [73]);
		outButton.setMargin(new Insets(1, 1, 1, 1));
		outButton.setFont(new Font("Courier",1,8));
		outButton.setPreferredSize(new Dimension (20,16));
		outButton.addActionListener(this);
		
		submitEnter=new JCheckBox ();
		submitEnter.setText("Submit on Enter");
		submitEnter.setSelected(true);
		submitEnter.setFont(new Font("Courier",1,8));
		submitEnter.setPreferredSize(new Dimension (100,16));		
		
		Box controlBox = new Box (BoxLayout.X_AXIS);
		controlBox.add(submitEnter);
		
        JSeparator sep=new JSeparator(SwingConstants.VERTICAL);
        sep.setMinimumSize(new Dimension (10,5));
        sep.setMaximumSize(new Dimension (10,50));
        
		controlBox.add(setButton);
		
        controlBox.add(sep);
        
		controlBox.add(clearButton);
		controlBox.add(saveButton);
		controlBox.add(maxLines);
		
		controlBox.add(inButton);
		controlBox.add(outButton);
		controlBox.add(Box.createHorizontalGlue());
		
		controlBox.setMinimumSize(new Dimension (100,24));
		controlBox.setPreferredSize(new Dimension (100,24));
												
		textArea=new JTextArea ();
		textArea.setEditable(false);
		textArea.setBorder(blackborder);
		textArea.setFont(new Font("Dialog", 1, 10));
		textArea.setMinimumSize(new Dimension (50,50));
		//textArea.setPreferredSize(new Dimension (100,50));
		
		mainBox.add(entry);
		mainBox.add(controlBox);		
		mainBox.add(textArea);		
										
		// We should be ready for action now
		
		this.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
		
		setContentPane (mainBox);					
	}
	/**
	 * 
	 */	
	public INHoopStdin getInHoop() 
	{
		return inHoop;
	}
	/**
	 * 
	 */	
	public void setInHoop(INHoopStdin inHoop) 
	{
		this.inHoop = inHoop;
	}
	/**
	 * 
	 */	
	public INHoopStdout getOutHoop() 
	{
		return outHoop;
	}
	/**
	 * 
	 */	
	public void setOutHoop(INHoopStdout outHoop) 
	{
		this.outHoop = outHoop;
	}	
	/**
	 * 
	 */
	@Override
	public void keyPressed(KeyEvent event) 
	{
	    int key = event.getKeyCode();
	    
        if (key == KeyEvent.VK_ENTER)
        {
        	processInput ();
        }  		
	}
	/**
	 * 
	 */
	@Override
	public void keyReleased(KeyEvent arg0) 
	{
		// TODO Auto-generated method stub
		
	}
	/**
	 * 
	 */
	@Override
	public void keyTyped(KeyEvent arg0) 
	{
		// TODO Auto-generated method stub
		
	}	
	/**
	 *
	 */		
	public void processOutput (String aString)
	{
		textArea.append(aString+"\n");
	}
	/**
	 *
	 */		
	private void processInput ()
	{
		debug ("processInput ()");
				
		if (entry.getText().isEmpty()==true)
		{
			debug ("Please enter a query");
			JOptionPane.showMessageDialog(null, "Please enter a query");
			return;
		}
		
		if (inHoop!=null)
		{
			List<String> holder=inHoop.getHolder();
			
			synchronized (holder) 
			{
				holder.add(entry.getText());
				holder.notify();
			}							
		}
		else
			JOptionPane.showMessageDialog(null, "Error: no input hoop available process request");
		
		entry.setText("");
	}
	/**
	 * 
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) 
	{
		// TODO Auto-generated method stub
		
	}	
}
