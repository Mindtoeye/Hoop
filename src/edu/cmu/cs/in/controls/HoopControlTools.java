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

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import edu.cmu.cs.in.base.HoopRoot;

/**
 * 
 */
public class HoopControlTools extends HoopRoot
{	

	/**
	 *
	 */    
    public static JButton makeNavigationButton (String actionCommand,
    											String toolTipText,    									   
    											ImageIcon icon) 
    {
    	//Create and initialize the button.
    	JButton button = new JButton();
    	//button.setBorder (null);
    	button.setMinimumSize(new Dimension (22,22));
    	button.setPreferredSize(new Dimension (22,22));
    	button.setActionCommand(actionCommand);
    	button.setToolTipText (toolTipText);
    	//button.addActionListener(this);
    	button.setIcon(icon);
    	button.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));

    	return button;
    }         
}