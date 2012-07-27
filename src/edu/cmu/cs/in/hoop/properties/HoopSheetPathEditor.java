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
//import java.awt.Component;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellRenderer;

import edu.cmu.cs.in.controls.base.HoopJPanel;
import edu.cmu.cs.in.hoop.properties.types.HoopSerializable;
import edu.cmu.cs.in.hoop.properties.types.HoopURISerializable;

/*
 * 
 */
class HoopSheetPathEditor extends HoopJPanel implements ActionListener, TableCellRenderer 
{
	private static final long serialVersionUID = 1L;

	private JButton chooser = null;
	private Color savedColor;
	private JTextField chosenPath;
		
	private HoopURISerializable pathObject=null;
	
	/**
	 *
	 */  
	public HoopSheetPathEditor() 
	{		
		setClassName ("HoopSheetPathEditor");
		debug ("HoopSheetPathEditor ()");
		
		this.setLayout(new BoxLayout (this,BoxLayout.X_AXIS));
		
		chosenPath=new JTextField ();
		
		chooser = new JButton();
		chooser.setText("...");
		chooser.setFont(new Font("Dialog", 1, 10));
		chooser.addActionListener(this);
		chooser.setPreferredSize(new Dimension (35,25));
		chooser.setMaximumSize(new Dimension (35,25));

		this.add(chosenPath);
		this.add(chooser);				
	}
	/**
	 * 
	 */
	public HoopURISerializable getPathObject() 
	{
		return pathObject;
	}
	/**
	 * 
	 */
	public void setPathObject(HoopURISerializable pathObject) 
	{
		this.pathObject = pathObject;
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
	public String getPath ()
	{
		return (pathObject.getValue());
	}
	/**
	 * 
	 */
	private void changePath (String aPath)
	{
		debug ("changePath ("+aPath+")");
		
		pathObject.setValue(aPath);
		chosenPath.setText(aPath);
	}
	/**
	 * 
	 */
	public void actionPerformed(ActionEvent actionEvent) 
	{
		debug ("actionPerformed ()");
		
	    JFileChooser fc=new JFileChooser ();
	    
	    /*
		FileNameExtensionFilter filter=new FileNameExtensionFilter (".xml rule files", "xml");
		fc.setFileFilter(filter);
		*/	    
					
		int returnVal=fc.showSaveDialog (this);

		if (returnVal==JFileChooser.APPROVE_OPTION) 
		{
	       	File file = fc.getSelectedFile();
	       	
	       	changePath (file.getAbsolutePath());
		} 	
	}
	/**
	 * 
	 */
	@Override
	public Component getTableCellRendererComponent (JTable table, 
													Object aValue,
													boolean isSelected, 
													boolean hasFocus,
													int row, 
													int column) 
	{
		debug ("getTableCellRendererComponent ("+aValue.getClass().getName()+")");
		
    	if (aValue instanceof String)
    	{
    		String safety=(String) aValue;
    		debug ("For some reason we're now getting a string back, going into safety mode ...");
        	debug ("Parsing: " + safety);
        	
        	changePath (safety);    		
    	}
    	else
    	{
    		
    		HoopSerializableTableEntry value=(HoopSerializableTableEntry) aValue;
    	
    		HoopSerializable object=(HoopSerializable) value.getEntry();
    		
    		changePath (object.getValue());
    	}	
		
		return this;
	}	
}
