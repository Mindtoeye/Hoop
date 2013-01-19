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

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;

import edu.cmu.cs.in.base.HoopRoot;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.hoop.properties.HoopVisualProperties;
  
public class HoopJCheckListItem extends JCheckBox implements ListCellRenderer, ItemListener 
{
	private static final long serialVersionUID = 1L;	
	private Object representative=null;
	
	/**
	 *
	 */
	private void debug (String aMessage)
	{
		HoopRoot.debug ("HoopJCheckListItem",aMessage);	
	}		
	/**
	 * 
	 */	
	public HoopJCheckListItem () 
	{
		setBackground (UIManager.getColor("List.textBackground"));
		setForeground (UIManager.getColor("List.textForeground"));
		setFont(new Font("Dialog", 1, 10)); 
		
		debug ("HoopJCheckList ()");
		
		//this.addItemListener(this);
	}
	/**
	 * 
	 */
	public Component getListCellRendererComponent  (JList listBox, 
													Object obj, 
													int currentindex, 
													boolean isChecked, 
													boolean hasFocus) 
	{
		if (obj==null)
		{
			debug ("Internal error: object is null");
			return this;
		}	
		
		representative=obj;
		
		setEnabled(listBox.isEnabled());
		setFont(listBox.getFont());		
		setForeground(listBox.getForeground());
		
		if (obj instanceof HoopVisualFeature)
		{
			setBackground(listBox.getBackground());
			setSelected(((HoopVisualFeature)obj).isSelected());

			setText (obj.toString());
		}
		
		if (obj instanceof HoopBase)
		{		
			HoopBase aHoop=(HoopBase) obj;
			
			HoopVisualProperties vizProps=aHoop.getVisualProperties();
			
			if (vizProps.getHighlighted()==true)
				setBackground(new Color (220,220,220));				
			else
				setBackground(listBox.getBackground());
			
			setSelected(((HoopBase)obj).getActive());

			setText (aHoop.getClassName()+":"+aHoop.getInstanceName());
		}		
		
		return this;
	}
	/**
	 * 
	 */
	@Override
	public void itemStateChanged(ItemEvent event) 
	{
		debug ("itemStateChanged ()");
		
		if (representative==null)
		{
			debug ("Error: this checkbox does not represent or contain a user defined object");
			return;
		}
				 
		if (event.getStateChange()==ItemEvent.DESELECTED)
		{
			debug ("Deselect item ..."); 
			
			if (representative instanceof HoopBase)
			{
				HoopBase aHoop=(HoopBase) representative;
				
				aHoop.setActive(false);
			}
		}
		 
		if (event.getStateChange()==ItemEvent.SELECTED)
		{
			debug ("Select item ...");
			 
			if (representative instanceof HoopBase)
			{
				HoopBase aHoop=(HoopBase) representative;
				
				aHoop.setActive(true);
			}	
		}		 
	}	
}
