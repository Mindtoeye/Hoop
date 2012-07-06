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
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
//import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;

import edu.cmu.cs.in.base.INBase;
import edu.cmu.cs.in.hoop.properties.types.INHoopSerializable;

public class INHoopSheetCellEditor extends AbstractCellEditor implements TableCellEditor, ActionListener 
{
	private static final long serialVersionUID = 1L;
	
	private INHoopSerializableTableEntry entry=null;

	private INHoopSerializable obj=null;
	//private INHoopSAI          sai=null;
	//private INHoopArgument     arg=null;
	
	private JTextField textComponent =null;
	private JComboBox booleanComponent=null;
	private JComboBox fontComponent=null;
	private INHoopSheetCellNumber numberComponent=null;
	
	private JButton colorDelegate = new JButton();	
	private JButton arrayDelegate = new JButton();
	
	String[] booleanStrings = {"TRUE","FALSE"};
	String[] fontStrings = {"Arial","Helvetica","Verdana","Times","Times New Roman"};
	
    /**
	 * 
	 */	
	public INHoopSheetCellEditor ()
	{
		debug ("INHoopSheetCellEditor ()");
						
		textComponent=new JTextField();
		textComponent.setMinimumSize(new Dimension (10,10));
		textComponent.setMaximumSize(new Dimension (5000,5000));
		textComponent.setFont(new Font("Dialog", 1, 10));
		
		booleanComponent=new JComboBox (booleanStrings);
		booleanComponent.setMinimumSize(new Dimension (10,10));
		booleanComponent.setMaximumSize(new Dimension (5000,5000));
		booleanComponent.setFont(new Font("Dialog", 1, 10));
		
		fontComponent=new JComboBox (fontStrings);
		fontComponent.setMinimumSize(new Dimension (10,10));
		fontComponent.setMaximumSize(new Dimension (5000,5000));
		fontComponent.setFont(new Font("Dialog", 1, 10));		
		
		numberComponent=new INHoopSheetCellNumber ();
		numberComponent.setMinimumSize(new Dimension (10,10));
		numberComponent.setMaximumSize(new Dimension (5000,5000));
		numberComponent.setFont(new Font("Dialog", 1, 10));		
		
		colorDelegate.addActionListener (this);
		arrayDelegate.addActionListener (this);
	}	
    /**
	 * 
	 */
    private void debug (String aMessage)
    {
    	INBase.debug ("INHoopSheetCellEditor",aMessage);
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
			
			Color newColor=(Color) INHoopColorUtil.parse (obj.getValue());		
			Color color = JColorChooser.showDialog (colorDelegate,"Color Chooser",newColor);
			
			if (obj!=null)
				obj.setValue(INHoopColorUtil.toHex(color));
			
			/*
			if (arg!=null)
				arg.setValue(INHoopColorUtil.toHex(color));
			*/				
		}
		
		/*
		if (actionEvent.getSource ()==arrayDelegate) 
		{	
			debug ("Showing argument editor ...");
			
			INHoopArgumentEditor d = new INHoopArgumentEditor(null,"Argument Editor");				
			d.setSize(200,400);
			//d.setLocationRelativeTo(null);
			d.setSAI(sai);
			d.setVisible(true);
		}
		*/		
		
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
    	
    	entry=(INHoopSerializableTableEntry) anObject;
    	
    	if (entry.getEntry()!=null)
    		obj=(INHoopSerializable) entry.getEntry();
    	
    	/*
    	if (entry.getArgument()!=null)
    		arg=entry.getArgument();
    	*/	
    	
    	/*
    	if (entry.getSAI ()!=null)
    		sai=entry.getSAI ();
    	*/	
    	
        // 'value' is value contained in the cell located at (rowIndex, vColIndex)

        if (isSelected) 
        {
            // cell (and perhaps other cells) are selected
        }

        if (obj!=null)
        {
        	if (obj.getType().equals("String")==true)
        	{
        		textComponent.setText (obj.getValue());
        	}	

        	if (obj.getType().equals("Boolean")==true)
        	{
        		return (booleanComponent);
        	}
        
        	if (obj.getType().equals("Font")==true)
        	{
        		return (fontComponent);
        	}
        
        	if (obj.getType().equals("Color")==true)
        	{
        		return (colorDelegate);
        	}
        	
        	textComponent.setText (obj.getValue());
        }
        
        /*
        if (arg!=null)
        {
        	if (arg.getType().equals("String")==true)
        	{
        		textComponent.setText (arg.getValue());
        	}	

        	if (arg.getType().equals("Boolean")==true)
        	{
        		return (booleanComponent);
        	}
        
        	if (arg.getType().equals("Font")==true)
        	{
        		return (fontComponent);
        	}
        
        	if (arg.getType().equals("Color")==true)
        	{
        		return (colorDelegate);
        	}
        	
        	textComponent.setText (arg.getValue());
        } 
        */       
        	
        /*
        if (sai!=null)
        {
        	if (sai.getArgumentSize()>1)
        	{
        		arrayDelegate.setText (sai.toArgumentString());
        		return (arrayDelegate);
        	}
        	else
        	{
            	if (sai.getType().equals("String")==true)
            	{
            		textComponent.setText (sai.getValue());
            	}	

            	if (sai.getType().equals("Boolean")==true)
            	{
            		return (booleanComponent);
            	}
            
            	if (sai.getType().equals("Font")==true)
            	{
            		return (fontComponent);
            	}
            
            	if (sai.getType().equals("Color")==true)
            	{
            		return (colorDelegate);
            	}
            	
            	textComponent.setText (sai.getValue());        		
        	}
        }
        */
        
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
    	
    		if (obj.getType().equals("Boolean")==true)
    		{
    			debug ("Returning ComboBox selection ...");
    			obj.setValue((String) booleanComponent.getSelectedItem());
    			textComponent.setText(obj.getValue());
    			//return (booleanComponent.getSelectedItem());
    		}
    	
    		if (obj.getType().equals("Font")==true)
    		{
    			debug ("Returning Font selection ...");
    			obj.setValue((String) fontComponent.getSelectedItem());
    			textComponent.setText(obj.getValue());
    			//return (fontComponent.getSelectedItem());
    		}
    	
    		if (obj.getType().equals("Color")==true)
    		{
    			debug ("Returning Color string ["+obj.getValue()+"]->["+textComponent.getText()+"]...");
    			//obj.setValue(textComponent.getText());
    			//return (textComponent.getText()); // REPLACE!
    			textComponent.setText(obj.getValue());
    		}
    	
    		if (obj.getType().equals("Number")==true)
    		{
    			debug ("Returning Number ...");
    			obj.setValue(numberComponent.getText());
    			textComponent.setText(obj.getValue());
    			//return (numberComponent.getText());
    		}    	
    	
    		if (obj.getType().equals("Enum")==true)
    		{
    			debug ("Returning Enumeration ...");
    			obj.setValue(textComponent.getText());    			
    			//return (textComponent.getText()); // REPLACE!
    		} 
    		
    		if (obj.getType().equals ("String")==true)
    		{
    			debug ("Returning String ...");
    			obj.setValue(textComponent.getText ());
    		}
    	}
    	
    	/*
    	if (arg!=null)
    	{
    		debug ("Setting direct value ...");    	
    		
    		if (arg.getType().equals("Boolean")==true)
    		{
    			debug ("Returning ComboBox selection ...");
    			arg.setValue((String) booleanComponent.getSelectedItem());
    			//return (booleanComponent.getSelectedItem());
    		}
    	
    		if (arg.getType().equals("Font")==true)
    		{
    			debug ("Returning Font selection ...");
    			arg.setValue((String) fontComponent.getSelectedItem());
    			//return (fontComponent.getSelectedItem());
    		}
    	
    		if (arg.getType().equals("Color")==true)
    		{
    			debug ("Returning Color string ["+arg.getValue()+"]->["+textComponent.getText()+"]...");
    			arg.setValue(textComponent.getText());
    			//return (textComponent.getText()); // REPLACE!
    		}
    	
    		if (arg.getType().equals("Number")==true)
    		{
    			debug ("Returning Number ...");
    			arg.setValue(numberComponent.getText());
    			//return (numberComponent.getText());
    		}    	
    	
    		if (arg.getType().equals("Enum")==true)
    		{
    			debug ("Returning Enumeration ...");
    			arg.setValue(textComponent.getText());
    			//return (textComponent.getText()); // REPLACE!
    		} 
    		
    		if (arg.getType().equals ("String")==true)
    		{
    			debug ("Returning String ...");
    			arg.setValue(textComponent.getText ());
    		}    		
    	} 
    	*/   	
    	
    	/*
    	if (sai!=null)
    	{    	
    		debug ("Setting sai value(s) ...");
    		
    		if (sai.getArgumentSize()>1)
    		{
    			debug ("BUILD THIS!!!");
    		}
    		else
    		{
    			if (sai.getType().equals("Boolean")==true)
    			{
    				debug ("Returning ComboBox selection ...");
    				sai.setValue((String) booleanComponent.getSelectedItem());
    				//return (booleanComponent.getSelectedItem());
    			}
    	
    			if (sai.getType().equals("Font")==true)
    			{
    				debug ("Returning Font selection ...");
    				sai.setValue((String) fontComponent.getSelectedItem());
    				//return (fontComponent.getSelectedItem());
    			}
    	
    			if (sai.getType().equals("Color")==true)
    			{
    				debug ("Returning Color string ["+sai.getValue()+"]->["+textComponent.getText()+"]...");
    				sai.setValue(textComponent.getText());
    				//return (textComponent.getText()); // REPLACE!
    			}
    	
    			if (sai.getType().equals("Number")==true)
    			{
    				debug ("Returning Number ...");
    				sai.setValue(numberComponent.getText());
    				//return (numberComponent.getText());
    			}    	
    	
    			if (sai.getType().equals("Enum")==true)
    			{
    				debug ("Returning Enumeration ...");
    				sai.setValue(textComponent.getText());
    				//return (textComponent.getText()); // REPLACE!
    			}
    			
        		if (sai.getType().equals ("String")==true)
        		{
        			debug ("Returning String ...");
        			sai.setValue(textComponent.getText ());
        		}    			
    		}	
    	} 
    	*/   	
    	
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
}
