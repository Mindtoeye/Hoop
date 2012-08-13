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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
//import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;

import edu.cmu.cs.in.base.HoopDataType;
import edu.cmu.cs.in.base.HoopRoot;
import edu.cmu.cs.in.hoop.properties.types.HoopEnumSerializable;
import edu.cmu.cs.in.hoop.properties.types.HoopSerializable;
import edu.cmu.cs.in.hoop.properties.types.HoopURISerializable;

public class HoopSheetCellEditor extends AbstractCellEditor implements TableCellEditor, ActionListener, ItemListener 
{
	private static final long serialVersionUID = 1L;
	
	private HoopSerializableTableEntry entry=null;
	private HoopSerializable obj=null;
	
	private JTextField textComponent =null;
	private JComboBox booleanComponent=null;
	private JComboBox enumComponent=null;
	private JComboBox fontComponent=null;
	private HoopSheetCellNumber numberComponent=null;
	private HoopSheetPathEditor pathComponent=null;
	
	private JButton colorDelegate = new JButton();	
	private JButton arrayDelegate = new JButton();
	
	String[] booleanStrings = {"TRUE","FALSE"};
	String[] fontStrings = {"Arial","Helvetica","Verdana","Times","Times New Roman"};
	
    /**
	 * 
	 */	
	public HoopSheetCellEditor ()
	{
		debug ("HoopSheetCellEditor ()");
						
		textComponent=new JTextField();
		textComponent.setMinimumSize(new Dimension (10,10));
		textComponent.setMaximumSize(new Dimension (5000,5000));
		textComponent.setFont(new Font("Dialog", 1, 10));
		
		booleanComponent=new JComboBox (booleanStrings);
		booleanComponent.setMinimumSize(new Dimension (10,10));
		booleanComponent.setMaximumSize(new Dimension (5000,5000));
		booleanComponent.setFont(new Font("Dialog", 1, 10));
		booleanComponent.addItemListener (this);
		
		fontComponent=new JComboBox (fontStrings);
		fontComponent.setMinimumSize(new Dimension (10,10));
		fontComponent.setMaximumSize(new Dimension (5000,5000));
		fontComponent.setFont(new Font("Dialog", 1, 10));		
		
		numberComponent=new HoopSheetCellNumber ();
		numberComponent.setMinimumSize(new Dimension (10,10));
		numberComponent.setMaximumSize(new Dimension (5000,5000));
		numberComponent.setFont(new Font("Dialog", 1, 10));		
		
		pathComponent=new HoopSheetPathEditor ();
		
		colorDelegate.addActionListener (this);
		arrayDelegate.addActionListener (this);
	}	
    /**
	 * 
	 */
    private void debug (String aMessage)
    {
    	HoopRoot.debug ("HoopSheetCellEditor",aMessage);
    }  
    /**
	 * 
	 */    
	public void actionPerformed (ActionEvent actionEvent)
	{
		debug ("actionPerformed ()");
		
		if (actionEvent.getSource ()==colorDelegate) 
		{	
			debug ("Showing color chooser ...");
			
			Color newColor=(Color) HoopColorUtil.parse (obj.getValue());		
			Color color = JColorChooser.showDialog (colorDelegate,"Color Chooser",newColor);
			
			if (obj!=null)
				obj.setValue(HoopColorUtil.toHex(color));			
		}
					
		fireEditingStopped();
	}    
    /**
	 * This method is called when a cell value is edited by the user.
	 */	     
    public Component getTableCellEditorComponent(JTable table, 
    											 Object anObject,
    											 boolean isSelected, 
    											 int rowIndex, 
    											 int vColIndex) 
    {
    	debug ("getTableCellEditorComponent ()");
    	
    	if (anObject==null)
    	{
    		debug ("Error: table entry obj is null");
    		return (null);
    	}
    	
    	entry=(HoopSerializableTableEntry) anObject;
    	
    	if (entry.getEntry()!=null)
    		obj=(HoopSerializable) entry.getEntry();
    	
        // 'value' is value contained in the cell located at (rowIndex, vColIndex)

        if (isSelected) 
        {
            // cell (and perhaps other cells) are selected
        }

        if (obj!=null)
        {
        	if (obj.getType()==HoopDataType.STRING)
        	{
        		textComponent.setText (obj.getValue());
        	}	

        	if (obj.getType()==HoopDataType.BOOLEAN)
        	{
        		return (booleanComponent);
        	}
        
        	if (obj.getType()==HoopDataType.FONT)
        	{
        		return (fontComponent);
        	}
        
        	if (obj.getType()==HoopDataType.COLOR)
        	{
        		return (colorDelegate);
        	}
        	
        	if (obj.getType()==HoopDataType.ENUM)
        	{
        		HoopEnumSerializable transformer=(HoopEnumSerializable) obj;
        		
        		ArrayList <String> enumList=transformer.getOptions();
        		
        		String[] array = enumList.toArray(new String[enumList.size()]);
        		
        		enumComponent=new JComboBox (array);
        		enumComponent.setMinimumSize(new Dimension (10,10));
        		enumComponent.setMaximumSize(new Dimension (5000,5000));
        		enumComponent.setFont(new Font("Dialog", 1, 10));     
        		enumComponent.addItemListener (this);
        		
        		return (enumComponent);
        	}
        	
        	if (obj.getType()==HoopDataType.URI)
        	{
        		pathComponent.setPathObject((HoopURISerializable) obj);
        		return (pathComponent);
        	}
        	
        	textComponent.setText (obj.getValue());
        }
                
        return (textComponent);
    }
    /**
	 * Implement the one CellEditor method that AbstractCellEditor doesn't. This 
	 * method is called when editing is completed. It must return the new value 
	 * to be stored in the cell.
	 */        
    public Object getCellEditorValue() 
    {
    	debug ("getCellEditorValue ()");
    	
    	if (this.obj!=null)
    	{
    		debug ("Setting direct value ...");
    	
    		if (obj.getType()==HoopDataType.BOOLEAN)
    		{
    			debug ("Returning ComboBox selection ...");
    			obj.setValue((String) booleanComponent.getSelectedItem());
    			textComponent.setText(obj.getValue());
    		}
    	
    		if (obj.getType()==HoopDataType.FONT)
    		{
    			debug ("Returning Font selection ...");
    			obj.setValue((String) fontComponent.getSelectedItem());
    			textComponent.setText(obj.getValue());
    		}
    	
    		if (obj.getType()==HoopDataType.COLOR)
    		{
    			debug ("Returning Color string ["+obj.getValue()+"]->["+textComponent.getText()+"]...");
    			textComponent.setText(obj.getValue());
    		}
    	
    		if ((obj.getType()==HoopDataType.INT) || (obj.getType()==HoopDataType.FLOAT))
    		{
    			debug ("Returning Number ...");
    			obj.setValue(numberComponent.getText());
    			textComponent.setText(obj.getValue());
    			//return (numberComponent.getText());
    		}    	
    	
    		if (obj.getType()==HoopDataType.ENUM)
    		{
    			debug ("Returning Enumeration ...");
    			obj.setValue((String) enumComponent.getSelectedItem());
    			textComponent.setText(obj.getValue());
    		} 
    		
    		if (obj.getType()==HoopDataType.STRING)
    		{
    			debug ("Returning String ...");
    			obj.setValue(textComponent.getText ());
    			textComponent.setText(obj.getValue());
    		}
    		
    		if (obj.getType()==HoopDataType.URI)
    		{
    			debug ("Returning Path ...");
    			obj.setValue(pathComponent.getPath ());
    			textComponent.setText(obj.getValue());
    		}    		
    	}
    	    	
        return (entry);
    }
    /**
	 * 1 For single-click activation     
	 * 2 For double-click activation      
	 * 3 For triple-click activation      
	 */    
    public boolean isCellEditable(EventObject evt) 
    {
        if (evt instanceof MouseEvent) 
        {
            return ((MouseEvent)evt).getClickCount()>=2;
        }
        
        return true;
    }
    /**
     * 
     */
	@Override
	public void itemStateChanged(ItemEvent event) 
	{
		debug ("itemStateChanged ()");
		
		 JComboBox cb=(JComboBox) event.getSource();
		 
		 if (cb==enumComponent)
		 {
			 debug ("Assigning selected value ("+(String) enumComponent.getSelectedItem()+") to enum object ...");
			 obj.setValue((String) enumComponent.getSelectedItem());
		 }
		 
		 if (cb==booleanComponent)
		 {
			 debug ("Assigning selected value ("+(String) booleanComponent.getSelectedItem()+") to boolean object ...");
			 obj.setValue((String) booleanComponent.getSelectedItem());			 
		 }
	}    
}
