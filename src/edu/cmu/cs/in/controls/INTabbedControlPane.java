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