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

package edu.cmu.cs.in.hoop.properties;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/*
 * 
 */
class INHoopSheetColorEditor extends AbstractCellEditor implements TableCellEditor 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JButton delegate = new JButton();

	Color savedColor;

	/**
	 *
	 */  
	public INHoopSheetColorEditor() 
	{
		ActionListener actionListener = new ActionListener() 
		{
			public void actionPerformed(ActionEvent actionEvent) 
			{
				Color color = JColorChooser.showDialog(delegate, "Color Chooser", savedColor);
				changeColor(color);
			}
		};
		
		delegate.addActionListener(actionListener);
	}
	/**
	 * Implement the one CellEditor method that AbstractCellEditor doesn't.
	 */
	public Object getCellEditorValue() 
	{
		return savedColor;
	}
	/**
	 *
	 */
	private void changeColor(Color color) 
	{
		if (color != null) 
		{
			savedColor = color;
			delegate.setBackground(color);
		}
	}
	/**
	 *
	 */
	public Component getTableCellEditorComponent (JTable table, Object value, boolean isSelected,int row, int column) 
	{
		changeColor((Color) value);
		return delegate;
	}
}
