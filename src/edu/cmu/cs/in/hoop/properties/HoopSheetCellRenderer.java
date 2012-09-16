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

import java.awt.Font;

import javax.swing.table.DefaultTableCellRenderer;

import edu.cmu.cs.in.base.HoopRoot;
import edu.cmu.cs.in.hoop.properties.types.HoopSerializable;

/**
 * 
 */	
public class HoopSheetCellRenderer extends DefaultTableCellRenderer 
{
	private static final long serialVersionUID = -1L;
	private HoopSerializable object=null;
	
    /**
	 * 
	 */	
    public HoopSheetCellRenderer() 
    {  
    	debug ("HoopSheetCellRenderer ()");
    	this.setFont(new Font("Dialog", 1, 10));
    }
    /**
	 * 
	 */
    private void debug (String aMessage)
    {
    	HoopRoot.debug ("HoopSheetCellRenderer",aMessage);
    }
    /**
	 * 
	 */
    public void setValue(Object value) 
    {
    	//debug ("setValue ()");
    	
    	/*
    	if (value==null)
    	{
    		setText("Undefined");
    		return;
    	}
    	*/
        	    	
		if (value instanceof String)
		{    	    	
			//debug ("INSTANCE IS STRING, NOT SERIALIZABLE OBJECT!");
			
			String object=(String) value;
			setText(object);
		}	
		
		if (value instanceof HoopSerializableTableEntry)
		{			
	    	HoopSerializableTableEntry entry=(HoopSerializableTableEntry) value;
	    	
	    	object=(HoopSerializable) entry.getEntry();
	    	
    	    if (object!=null)
    	    {
    	    	//debug ("Cell renders value " + object.getValue() + " from class: " + object.getClassName());
    	    	setText (object.getValue());
    	    }
		}	
    }
    /**
     * 
     */
    public Object getValue ()
    {
    	return (object);
    }
}
