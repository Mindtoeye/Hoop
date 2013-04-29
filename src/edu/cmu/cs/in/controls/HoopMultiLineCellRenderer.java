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

import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
//import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import edu.cmu.cs.in.base.HoopRoot;
 
/**
 * 
 */ 
public class HoopMultiLineCellRenderer extends JTextArea implements TableCellRenderer 
{
	private static final long serialVersionUID = -1L;
	int lineCount=-1;
	
	/*
	 * 
	 */
	public HoopMultiLineCellRenderer() 
	{
		debug ("HoopMultiLineCellRenderer ()");
				
		setLineWrap(true);
		setWrapStyleWord(true);
		setOpaque(true);		
		setFont(new Font("Courier",1,9));
	}
	/**
	 * 
	 */
	private void debug (String aMessage)	
	{
		HoopRoot.debug("HoopMultiLineCellRenderer",aMessage);
	}
	/**
	 * 
	 */
	public Component getTableCellRendererComponent (JTable table, 
												 	Object value,
												 	boolean isSelected, 
												 	boolean hasFocus, 
												 	int row, 
												 	int column) 
	{
		//debug ("getTableCellRendererComponent ()");
		
		if (isSelected) 
		{
			setForeground(table.getSelectionForeground());
			setBackground(table.getSelectionBackground());
		} 
		else 
		{
			setForeground(table.getForeground());
			setBackground(table.getBackground());
		}
				
		if (hasFocus) 
		{
			setBorder( UIManager.getBorder("Table.focusCellHighlightBorder") );
			
			if (table.isCellEditable(row, column)) 
			{
				setForeground( UIManager.getColor("Table.focusCellForeground") );
				setBackground( UIManager.getColor("Table.focusCellBackground") );
			}
		} 
		else 
		{
			setBorder(new EmptyBorder(1, 2, 1, 2));
		}
				
		if (value instanceof String)		
		{
			String formatted=(String) value;
																	
			setText (formatted);			
		}
		else
			setText ("Error formatting text");
		
		return (this);
	}
}
