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

//import edu.cmu.cs.in.base.INLink;
import edu.cmu.cs.in.controls.base.INEmbeddedJPanel;
import edu.cmu.cs.in.controls.base.INJInternalFrame;
import edu.cmu.cs.in.search.INTrecEval;

/**
 * 
 */
public class INHoopReporter extends INEmbeddedJPanel implements ActionListener 
{  
	private static final long serialVersionUID = -1;
	private JTextField inputPath=null;
	private JTextArea console=null;
	private JFileChooser fc=null;
	private JCheckBox collate=null;
	
	public INHoopReporter() 
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
			INTrecEval evaluator=new INTrecEval (collate.isSelected());
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
	        	//INLink.vocabularyPath=file.getPath ();	        	
	        }			
		}
	}
}
