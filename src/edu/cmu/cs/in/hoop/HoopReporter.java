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

/*
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.tree.*;
*/

package edu.cmu.cs.in.hoop;

//import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
//import java.util.ArrayList;

//import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
//import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
//import javax.swing.border.Border;

//import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.controls.base.HoopEmbeddedJPanel;
//import edu.cmu.cs.in.controls.base.HoopJInternalFrame;
import edu.cmu.cs.in.search.HoopTrecEval;

/**
 * 
 */
public class HoopReporter extends HoopEmbeddedJPanel implements ActionListener 
{  
	private static final long serialVersionUID = -1;
	private JTextField inputPath=null;
	private JTextArea console=null;
	private JFileChooser fc=null;
	private JCheckBox collate=null;
	
	public HoopReporter() 
    {
    	//super("Report Generator", true, true, true, true);
    	
    	fc = new JFileChooser();

    	Box holder = new Box (BoxLayout.Y_AXIS);
    	
		//Border padding = BorderFactory.createEmptyBorder(2,0,0,0);
		//Border blackborder=BorderFactory.createLineBorder(Color.black);
		//Border redborder=BorderFactory.createLineBorder(Color.red);	    	
		
		Box inputBox = new Box (BoxLayout.X_AXIS);
		inputBox.setMinimumSize(new Dimension (50,25));
		inputBox.setPreferredSize(new Dimension (5000,25));
		inputBox.setMaximumSize(new Dimension (5000,25));		
		
		inputPath=new JTextField ();
		inputPath.setEditable(false);
		inputPath.setFont(new Font("Dialog", 1, 10));
		inputPath.setMinimumSize(new Dimension (50,25));
		inputPath.setPreferredSize(new Dimension (5000,25));
		inputPath.setMaximumSize(new Dimension (5000,25));		
		//inputPath.addKeyListener(this);
		
		collate=new JCheckBox ();
		collate.setText("Collate");
		collate.setSelected(true);
		collate.setFont(new Font("Dialog", 1, 10));
		collate.setMinimumSize(new Dimension (75,25));
		collate.setPreferredSize(new Dimension (75,25));
		collate.setMaximumSize(new Dimension (75,25));
		
		JButton search=new JButton ();
		//search.setDefaultCapable(true);
		search.setText("Output ...");
		search.setFont(new Font("Dialog", 1, 10));
		search.setMinimumSize(new Dimension (75,25));
		search.setPreferredSize(new Dimension (75,25));
		search.setMaximumSize(new Dimension (75,25));
		search.addActionListener(this);
		
		JButton report=new JButton ();
		report.setDefaultCapable(true);
		report.setText("Report");
		report.setFont(new Font("Dialog", 1, 10));
		report.setMinimumSize(new Dimension (75,25));
		report.setPreferredSize(new Dimension (75,25));
		report.setMaximumSize(new Dimension (75,25));
		report.addActionListener(this);		
    	  				
		inputBox.add (inputPath);
		inputBox.add (collate);
		inputBox.add (search);
		inputBox.add (report);
		
		holder.add (inputBox);
		
		console=new JTextArea ();
		console.setEditable (false);
	    console.setFont(new Font("Courier",1,8));
		console.setMinimumSize(new Dimension (50,150));
		
		holder.add(console);
		
		setContentPane (holder);
		//setSize (325,200);
		//setLocation (75,75);
    }
	/**
	 * 
	 */
	@Override
	public void actionPerformed(ActionEvent event) 
	{
		debug ("actionPerformed ("+event.getActionCommand()+")");
		
		//String act=event.getActionCommand();
		JButton button = (JButton)event.getSource();
		
		//>---------------------------------------------------------
		
		if (button.getText().equals("Report"))
		{
			HoopTrecEval evaluator=new HoopTrecEval (collate.isSelected());
			evaluator.setOutputPath(inputPath.getText());
			console.setText(evaluator.flushData());
		}
		
		//>---------------------------------------------------------
		
		if (button.getText().equals("Output ..."))
		{
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	        int returnVal=fc.showOpenDialog (this);

	        if (returnVal==JFileChooser.APPROVE_OPTION) 
	        {
	        	File file = fc.getSelectedFile();
	        		
	        	inputPath.setText(file.getPath());
	        	//HoopLink.vocabularyPath=file.getPath ();	        	
	        }			
		}
	}
}
