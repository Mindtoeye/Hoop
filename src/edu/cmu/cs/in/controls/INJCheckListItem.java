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

import java.awt.Component;
import java.awt.Font;

import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;

import edu.cmu.cs.in.base.INBase;
  
class INJCheckListItem extends JCheckBox implements ListCellRenderer 
{
	private static final long serialVersionUID = 1L;
	
	/**
	 *
	 */
	private void debug (String aMessage)
	{
		INBase.debug ("INJCheckListItem",aMessage);	
	}		
	/**
	 * 
	 */	
	public INJCheckListItem () 
	{
		setBackground (UIManager.getColor("List.textBackground"));
		setForeground (UIManager.getColor("List.textForeground"));
		setFont(new Font("Dialog", 1, 10)); 
		
		debug ("INJCheckList ()");
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
		
		setEnabled(listBox.isEnabled());
		setSelected(((INVisualFeature)obj).isSelected());
		setFont(listBox.getFont());
		setBackground(listBox.getBackground());
		setForeground(listBox.getForeground());
		setText (obj.toString());
		return this;
	}	
}
