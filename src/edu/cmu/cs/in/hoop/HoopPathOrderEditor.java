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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.border.Border;

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.controls.base.HoopEmbeddedJPanel;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.hoop.hoops.task.HoopPathChooser;

/**
 * 
 */
public class HoopPathOrderEditor extends HoopEmbeddedJPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;
	    
	private JList stopList=null;
	
	private JButton moveUp=null;
	private JButton moveDown=null;
	
	private HoopPathChooser controller=null;
	
	/**
	 * 
	 */
	public HoopPathOrderEditor ()  
    {    	
		setClassName ("HoopPathOrderEditor");
		debug ("HoopPathOrderEditor ()");    	
    
		Border border=BorderFactory.createLineBorder(Color.black);
		Border bevel=BorderFactory.createLoweredBevelBorder();		
		
	    Box buttonBox = new Box (BoxLayout.X_AXIS);
	    buttonBox.setBorder(BorderFactory.createEmptyBorder(1,1,1,2));
	    	   	    
	    moveUp=new JButton ();
	    moveUp.setFont(new Font("Dialog", 1, 8));
	    moveUp.setPreferredSize(new Dimension (20,20));
	    moveUp.setMaximumSize(new Dimension (20,20));
	    moveUp.setIcon(HoopLink.getImageByName("gtk-go-up.png"));
	    moveUp.addActionListener(this);
	    buttonBox.add (moveUp);
	    
	    moveDown=new JButton ();
	    moveDown.setFont(new Font("Dialog", 1, 8));
	    moveDown.setPreferredSize(new Dimension (20,20));
	    moveDown.setMaximumSize(new Dimension (20,20));
	    moveDown.setIcon(HoopLink.getImageByName("gtk-go-down.png"));
	    moveDown.addActionListener(this);
	    buttonBox.add (moveDown);
	    	    
	    buttonBox.add(Box.createHorizontalGlue());		
		
		stopList=new JList ();
		stopList.setBorder(bevel);
		stopList.setMinimumSize(new Dimension (50,5000));
		stopList.setMaximumSize(new Dimension (5000,5000));
							
		Box stopBox = new Box (BoxLayout.Y_AXIS);
		stopBox.setMinimumSize(new Dimension (50,50));
		stopBox.setPreferredSize(new Dimension (150,5000));
		stopBox.setMaximumSize(new Dimension (150,5000));
		
		stopBox.add(buttonBox);
		stopBox.add(stopList);		
		
		setContentPane (stopBox);		
    }
	/**
	 * 
	 */
	public HoopPathChooser getController() 
	{
		return controller;
	}
	/**
	 * 
	 */
	public void setController(HoopPathChooser controller) 
	{
		this.controller = controller;
		
		updatePaths ();
	}	
	/**
	 * 
	 */	
	public void updatePaths ()
	{
		debug ("updatePaths ()");
		
		if (controller!=null)
		{
			ArrayList <HoopBase> hoopList=controller.getOutHoops();
			
			DefaultListModel filteredModel=new DefaultListModel();
			
        	for (int i=0;i<hoopList.size();i++) 
        	{
        		HoopBase aHoop=hoopList.get(i);
        		
        		StringBuffer formatter=new StringBuffer ();
        		formatter.append(aHoop.getClassName()+":"+aHoop.getInstanceName());
        		
        		filteredModel.addElement (formatter.toString());
        	}
        	
        	stopList.setModel (filteredModel);
		}
	}
	/**
	 * 
	 */	
	@Override
	public void actionPerformed(ActionEvent event) 
	{
		debug ("actionPerformed ()");
		
		JButton button = (JButton) event.getSource();
		
		if (button==moveUp)
		{
			debug ("Move path up ...");
		}
		
		if (button==moveDown)
		{
			debug ("Move path down ...");
		}		
	}
}
