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

//import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.border.Border;

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.controls.HoopJCheckListItem;
import edu.cmu.cs.in.controls.HoopVisualFeature;
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
    
		//Border border=BorderFactory.createLineBorder(Color.black);
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
		stopList.setCellRenderer (new HoopJCheckListItem ());
		stopList.setBorder(bevel);
		stopList.setMinimumSize(new Dimension (50,5000));
		stopList.setMaximumSize(new Dimension (5000,5000));
							
	    stopList.addMouseListener (new MouseAdapter()
	    {
	    	public void mouseClicked (MouseEvent event)
	    	{
	    		JList list=(JList) event.getSource();
 
	    		// Get index of item clicked
	    
	    		int index=list.locationToIndex(event.getPoint());
	    		
	    		Object rep=stopList.getModel().getElementAt(index);
	    		
	    		if (event.getClickCount()==1)
	    		{
	    			unhighlight ();
	    			
	    			if (rep instanceof HoopBase)
	    			{	    			    					    				
	    				HoopBase item=(HoopBase) rep;
	     
	    				item.setHighlighted(true);
	    				
	    				list.repaint (list.getCellBounds(index, index));
	    				//	updateDependends ();
	    			}	    			
	    		}
	    		
	    		if (event.getClickCount()==2)
	    		{	    		
	    			debug ("Toggling path ...");
	    			
	    			if (rep instanceof HoopVisualFeature)
	    			{	    		
	    				debug ("Toggling HoopVisualFeature ...");
	    				
	    				HoopVisualFeature item=(HoopVisualFeature) rep;
	     
	    				// Toggle selected state
	    
	    				item.setSelected (!item.isSelected());
	    
	    				// Repaint cell
	    			    		
	    				list.repaint (list.getCellBounds(index, index));
	    				//	updateDependends ();
	    			}
	    		
	    			if (rep instanceof HoopBase)
	    			{	    		
	    				debug ("Toggling HoopBase ...");
	    				
	    				HoopBase item=(HoopBase) rep;
	     
	    				// Toggle selected state
	    
	    				item.setActive(!item.getActive());
	    
	    				// Repaint cell
	    			    		
	    				list.repaint (list.getCellBounds(index, index));
	    				//	updateDependends ();
	    			}
	    		}	
	    	}
	    });  	    	   		
		
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
	private void unhighlight ()
	{
		debug ("unhighlight");
		
		for (int i=0;i<stopList.getModel().getSize();i++)
		{
			Object rep=stopList.getModel().getElementAt(i);
    		
    		if (rep instanceof HoopBase)
    		{	    			    					    				
    			HoopBase item=(HoopBase) rep;
     
    			item.setHighlighted(false);
    		}	
		}
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
        		
        		/*
        		StringBuffer formatter=new StringBuffer ();
        		formatter.append(i+":"+aHoop.getClassName()+":"+aHoop.getInstanceName());
        		
        		filteredModel.addElement (formatter.toString());
        		*/
        		
        		filteredModel.addElement (aHoop);
        	}
        	
        	stopList.setModel (filteredModel);
		}
	}
	/**
	 * 
	 */
	public HoopBase getSelectedHoop ()
	{
		debug ("getSelectedHoop ()");
		
		if (stopList.getSelectedIndex ()==-1)
		{
			return (null);
		}
		
		HoopBase item=(HoopBase) stopList.getModel().getElementAt(stopList.getSelectedIndex ());
		
		return (item);
	}
	/**
	 * 
	 */
	private HoopBase getHighlightedHoop ()
	{
		debug ("getSelectedHoop ()");
		
		for (int i=0;i<stopList.getModel().getSize();i++)
		{
			Object rep=stopList.getModel().getElementAt(i);
    		
    		if (rep instanceof HoopBase)
    		{	    			    					    				
    			HoopBase item=(HoopBase) rep;
     
    			if (item.getHighlighted()==true)
    				return (item);
    		}	
		}
		
		return (null);
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
			
			HoopBase selection=getHighlightedHoop ();
			
			if (selection==null)
			{
				alert ("Please select a hoop first");
				return;
			}
			
			ArrayList <HoopBase> switcher=controller.getOutHoops();
			
			Collections.swap(switcher,switcher.indexOf(selection), switcher.indexOf(selection)-1);

		}
		
		if (button==moveDown)
		{
			debug ("Move path down ...");
			
			HoopBase selection=getHighlightedHoop ();
			
			if (selection==null)
			{
				alert ("Please select a hoop first");
				return;
			}
			
			ArrayList <HoopBase> switcher=controller.getOutHoops();
			
			Collections.swap(switcher,switcher.indexOf(selection), switcher.indexOf(selection)+1);
		}		
	}
}
