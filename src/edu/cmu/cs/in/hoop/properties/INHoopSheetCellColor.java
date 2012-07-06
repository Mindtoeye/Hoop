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

//import javax.swing.BorderFactory;
//import javax.swing.Box;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
//import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;

import edu.cmu.cs.in.controls.base.INJPanel;
import edu.cmu.cs.in.hoop.properties.types.INHoopSerializable;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

/**
 * 
 */	
public class INHoopSheetCellColor extends INJPanel implements TableCellRenderer 
{
	private static final long serialVersionUID = -1568793295263302888L;
	
	//Border unselectedBorder = null;
    //Border selectedBorder = null;
    boolean isBordered = true;
    
	private INHoopSerializable object=null;    

    private JLabel colorLabel=new JLabel ("");
    private JPanel colorPicker=new JPanel ();	
	
    /**
	 * 
	 */	    
    public INHoopSheetCellColor (boolean isBordered) 
    {
    	setClassName ("INHoopSheetCellColor");
        debug ("INHoopSheetCellColor ()");
        
        this.setLayout(new BoxLayout (this,BoxLayout.X_AXIS));
        this.setMinimumSize(new Dimension (10,10));
        this.setMaximumSize(new Dimension (5000,5000));        
        
        this.isBordered = isBordered;
        setOpaque (true); //MUST do this for background to show up.
        
	    colorLabel=new JLabel ("");
	    colorLabel.setHorizontalAlignment (JLabel.LEFT);
	    colorLabel.setFont(new Font("Dialog", 1, 10));
	    colorLabel.setOpaque(true);
	    colorLabel.setBackground(new Color (255,255,255));
	    colorLabel.setMinimumSize(new Dimension (50,20));
	    colorLabel.setMaximumSize(new Dimension (2000,20));
	        
	    colorPicker=new JPanel ();
	    //colorPicker.setBackground(color);
	    colorPicker.setBorder(BorderFactory.createLineBorder(Color.black));
	    colorPicker.setMinimumSize(new Dimension (30,20));
	    //colorPicker.setPreferredSize(new Dimension (20,20));
	    colorPicker.setMaximumSize(new Dimension (30,30));
	    //colorPicker.addMouseListener(this);
	    
	    /*
	    Box labelBox = new Box (BoxLayout.X_AXIS);
	    labelBox.add(colorLabel);
	    labelBox.add(colorPicker);
	    
	    this.add(labelBox);
	    */

	    this.add(colorLabel);
	    this.add(colorPicker);
    } 
    /**
	 * 
	 */	
    public Component getTableCellRendererComponent (JTable table, 
    												Object aValue,
                            						boolean isSelected, 
                            						boolean hasFocus,
                            						int row, 
                            						int column) 
    {
    	debug ("getTableCellRendererComponent ("+aValue.getClass().getName()+")");
    	
    	/*
		if (aValue==null)
		{
			debug ("Internal error: value object is null!");
			return (this);
		}
		*/	
		
		Color newColor=null;
    	    	
    	if (aValue instanceof String)
    	{
    		String safety=(String) aValue;
    		debug ("For some reason we're now getting a string back, going into safety mode ...");
        	debug ("Parsing: " + safety);
        	
        	colorLabel.setText(safety);
        	
            newColor=(Color) INHoopColorUtil.parse(safety);
            colorPicker.setBackground(newColor);    		
    	}
    	else
    	{
    		
    		INHoopSerializableTableEntry value=(INHoopSerializableTableEntry) aValue;
    	
    		object=(INHoopSerializable) value.getEntry();
    	    	
    		debug ("Parsing: " + object.getValue());
    	
    		colorLabel.setText(object.getValue());
    	
    		newColor=(Color) INHoopColorUtil.parse(object.getValue());
    		colorPicker.setBackground(newColor);
    		colorPicker.repaint();
        
    		/*
        	if (isBordered) 
        	{
            	if (isSelected) 
            	{
                	if (selectedBorder == null) 
                	{
                    	selectedBorder = BorderFactory.createMatteBorder(2,5,2,5,table.getSelectionBackground());
                	}
                
                	setBorder(selectedBorder);
            	}
            	else 
            	{
                	if (unselectedBorder == null) 
                	{
                    	unselectedBorder = BorderFactory.createMatteBorder(2,5,2,5,table.getBackground());
                	}
                
                	setBorder(unselectedBorder);
            	}
        	}
    		 */
    	}
    		
        setToolTipText("RGB value: " + newColor.getRed() + ", "
                                     + newColor.getGreen() + ", "
                                     + newColor.getBlue());
        return this;
    }
}
