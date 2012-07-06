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

import javax.swing.table.TableCellRenderer;

import edu.cmu.cs.in.controls.base.INHoopJTable;
import edu.cmu.cs.in.hoop.properties.types.INHoopSerializable;

public class INHoopPropertyTable extends INHoopJTable
{
	private static final long serialVersionUID = -1127341907493007641L;
	private INHoopSheetCellRenderer defaultRenderer=null;
	private INHoopSheetCellColor colorRenderer=null;
		
	/**
	 *
	 */
    public INHoopPropertyTable () 
    {
    	debug ("INHoopPropertyTable ()");
    	
    	defaultRenderer=new INHoopSheetCellRenderer ();
    	colorRenderer=new INHoopSheetCellColor (true);
    }
	/**
	 *
	 */    
    public boolean isCellEditable(int rowIndex, int vColIndex) 
    {
    	if (vColIndex==0)
    		return (false);
    	
        return true;
    }    
	/**
	 *
	 */    
    public TableCellRenderer getCellRenderer(int row, int column) 
    {
    	debug ("getCellRenderer ("+row+","+column+")");
    	
        if (column==1)
        {
        	if (this.getValueAt (row,column)!=null)
        	{
        		Object tester=this.getValueAt (row,column);
        		
        		if (tester instanceof String)
        		{
        			// We get into this situation when we're visualizing a straight string
        			return (defaultRenderer);			
        		}
        			
        		if (tester instanceof INHoopSerializableTableEntry)
        		{       		
        			debug ("Table value is INHoopSerializable");
        			
        			INHoopSerializableTableEntry entry=(INHoopSerializableTableEntry) this.getValueAt (row,column);
        			
        			INHoopSerializable object=entry.getEntry();
        	
        			if (object!=null)
        			{
        				debug ("Cell type: " + object.getType ());
        			
        				if (object.getType().equals("Color")==true)
        				{
        					debug ("Detected a color entry ...");
        					return (colorRenderer);
        				}
        				
        				return (defaultRenderer);
        			}
        			
        			/*
        			INHoopSAI sai=entry.getSAI();
        			
        			if (sai!=null)
        			{
        				if (sai.getArgumentSize()==1)
        				{
        					if (object.getType().equals("Color")==true)
        					{
        						debug ("Detected a color entry ...");
        						return (colorRenderer);
        					}
        				}	
        				
        				return (defaultRenderer);
        			}
        			*/
        			
        			/*
        			INHoopArgument arg=entry.getArgument();
        			
        			if (arg!=null)
        			{
       					if (arg.getType().equals("Color")==true)
       					{
       						debug ("Detected a color entry ...");
       						return (colorRenderer);
       					}
        				
        				return (defaultRenderer);
        			} 
        			*/       			
      			        			
       				return (defaultRenderer);
        		}	
        	}	
        }

        return super.getCellRenderer (row,column);
    }    
}
