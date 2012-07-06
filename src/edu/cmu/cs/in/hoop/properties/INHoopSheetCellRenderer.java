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

import edu.cmu.cs.in.base.INBase;
import edu.cmu.cs.in.hoop.properties.types.INHoopSerializable;

/**
 * 
 */	
class INHoopSheetCellRenderer extends DefaultTableCellRenderer 
{
	private static final long serialVersionUID = 8503493022905127918L;
	private INHoopSerializable object=null;
	//private INHoopSAI sai=null;
	//private INHoopArgument arg=null;
	
    /**
	 * 
	 */	
    public INHoopSheetCellRenderer() 
    { 
    	//super(); 
    	debug ("INHoopSheetCellRenderer ()");
    	this.setFont(new Font("Dialog", 1, 10));
    }
    /**
	 * 
	 */
    private void debug (String aMessage)
    {
    	INBase.debug ("INHoopSheetCellRenderer",aMessage);
    }
    /**
	 * 
	 */
    public void setValue(Object value) 
    {
    	debug ("setValue ()");
    	
    	if (value==null)
    	{
    		setText("Undefined");
    		return;
    	}
        	    	
		if (value instanceof String)
		{    	    	
			debug ("INSTANCE IS STRING NOT SERIALIZABLE OBJECT!");
			
			String object=(String) value;
			setText(object);
		}	
		
		if (value instanceof INHoopSerializableTableEntry)
		{
			debug ("Instance is a INHoopSerializableTableEntry");
			
	    	INHoopSerializableTableEntry entry=(INHoopSerializableTableEntry) value;
	    	
	    	object=(INHoopSerializable) entry.getEntry();
    	    if (object!=null)    	    	   
    	    	setText (object.getValue());
    	    
    	    /*
	    	sai=(INHoopSAI) entry.getSAI();
    	    if (sai!=null)    	    	   
    	    {
    	    	if (sai.getArgumentSize()>1)
    	    		setText (sai.toArgumentString());
    	    	else
    	    		setText (sai.getValue ());
    	    }
    	    */	
    	    
    	    /*
	    	arg=(INHoopArgument) entry.getArgument();
    	    if (arg!=null)    	    	   
    	    	setText (arg.getValue());
    	    */	    	    
		}	
    }
}
