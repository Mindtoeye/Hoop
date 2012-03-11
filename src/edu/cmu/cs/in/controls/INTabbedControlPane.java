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

package edu.cmu.cs.in.controls;

//import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
//import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.KeyEvent;
//import java.awt.event.KeyListener;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
//import java.awt.event.MouseListener;
import java.awt.Insets;
//import java.util.ArrayList;

import javax.swing.*;

/**
*
*/
public class INTabbedControlPane 
{ 
	/**
	 *
	 */	
	public void makeUI() 
	{
		JTabbedPane tabbedPane = new JTabbedPane();
      
		for (int i = 0; i < 3; i++) 
		{
			JPanel panel = new JPanel();
			panel.setName("tab" + (i + 1));
			panel.setPreferredSize(new Dimension(400, 400));
			tabbedPane.add(panel);
		}
 
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(tabbedPane);
		frame.pack();
 
		Rectangle tabBounds = tabbedPane.getBoundsAt(0);
 
		Container glassPane = (Container) frame.getRootPane().getGlassPane();
		glassPane.setVisible(true);
		glassPane.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(tabBounds.y, 0, 0, 15);
		gbc.anchor = GridBagConstraints.NORTHEAST;
 
		JButton button = new JButton("My Button Position");
		button.setPreferredSize(new Dimension(button.getPreferredSize().width,(int) tabBounds.getHeight() - 2));
		glassPane.add(button, gbc);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	/**
	 *
	 */   
	public static void main(String[] args) 
	{
		SwingUtilities.invokeLater(new Runnable() 
		{
			@Override
			public void run() 
			{
				new INTabbedControlPane().makeUI();
			}
		});
	}   
}