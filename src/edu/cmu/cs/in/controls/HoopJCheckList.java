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

import javax.swing.UIManager;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * 
 */
public class HoopJCheckList extends JFrame 
{  
	/**
	 * 
	 */
	private static final long serialVersionUID = -7535841999671818686L;

	public HoopJCheckList() 
	{
		super ("HoopJCheckList");

		String[] listData = {"Apple", "Orange", "Cherry", "Blue Berry", "Banana", "Red Plum", "Watermelon"};

		try 
		{
   			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e) 
		{
			System.out.println("Unable to find System Look and Feel");
		}                                            

    		//This listbox holds only the checkboxes
		final JList listCheckBox = new JList(buildCheckBoxItems(listData.length));

    		//This listbox holds the actual descriptions of list items.
		final JList listDescription = new JList(listData);

    		listDescription.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		listDescription.addMouseListener(new MouseAdapter() 
		{
      		public void mouseClicked(MouseEvent me) 
      		{
				if (me.getClickCount() != 2)
					return;
	  			int selectedIndex = listDescription.locationToIndex(me.getPoint());
	  			if (selectedIndex < 0)
					return;
        			CheckBoxItem item = (CheckBoxItem)listCheckBox.getModel().getElementAt(selectedIndex);
        			item.setChecked(!item.isChecked());
	  			listCheckBox.repaint();

      		}
    		});


    		listCheckBox.setCellRenderer(new CheckBoxRenderer());

    		listCheckBox.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

     		listCheckBox.addMouseListener(new MouseAdapter() {
      		public void mouseClicked(MouseEvent me) {
	  			int selectedIndex = listCheckBox.locationToIndex(me.getPoint());
				if (selectedIndex < 0)
					return;
        			CheckBoxItem item = (CheckBoxItem)listCheckBox.getModel().getElementAt(selectedIndex);
        			item.setChecked(!item.isChecked());
	  			listDescription.setSelectedIndex(selectedIndex);
        			listCheckBox.repaint();
      		}
    		});
 
	
		// Now create a scrollpane;
		
		JScrollPane scrollPane = new JScrollPane();
		
		//Make the listBox with Checkboxes look like a rowheader. 
		//This will place the component on the left corner of the scrollpane
		scrollPane.setRowHeaderView(listCheckBox);

		//Now, make the listbox with actual descriptions as the main view
		scrollPane.setViewportView(listDescription);
		
		// Align both the checkbox height and widths
		listDescription.setFixedCellHeight(20);
		listCheckBox.setFixedCellHeight(listDescription.getFixedCellHeight());
		listCheckBox.setFixedCellWidth(20);
    	
		getContentPane().add(scrollPane); //, BorderLayout.CENTER);

    		setSize(350, 200);
    		setVisible(true);
    
 	}
  
	private CheckBoxItem[] buildCheckBoxItems(int totalItems) {
		CheckBoxItem[] checkboxItems = new CheckBoxItem[totalItems];
    		for (int counter=0;counter<totalItems;counter++) {
      		checkboxItems[counter] = new CheckBoxItem();
    		}
    		return checkboxItems;
  	}
  
	public static void main(String args[]) 
	{
		HoopJCheckList checkList = new HoopJCheckList();
		checkList.addWindowListener(new WindowAdapter() 
		{
      		public void windowClosing(WindowEvent we) 
      		{
				System.exit(0);
			}
    		});
  	}

/* Inner class to hold data for JList with checkboxes */
	class CheckBoxItem {
		private boolean isChecked;

	public CheckBoxItem() {
      	isChecked = false;
    	}
    	public boolean isChecked() {
      	return isChecked;
    	}
	public void setChecked(boolean value) {
      	isChecked = value;
    }
  }
  
/* Inner class that renders JCheckBox to JList*/

	class CheckBoxRenderer extends JCheckBox implements ListCellRenderer {
    
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public CheckBoxRenderer() {
      		setBackground(UIManager.getColor("List.textBackground"));
      		setForeground(UIManager.getColor("List.textForeground"));
    		}

    		public Component getListCellRendererComponent(JList listBox, Object obj, int currentindex, 
					boolean isChecked, boolean hasFocus) {
      		setSelected(((CheckBoxItem)obj).isChecked());
      		return this;
    		}

  	}
}
