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

import edu.cmu.cs.in.base.HoopDataType;
import edu.cmu.cs.in.controls.base.HoopJTable;
import edu.cmu.cs.in.hoop.properties.types.HoopSerializable;

public class HoopPropertyTable extends HoopJTable
{
	private static final long serialVersionUID = -1127341907493007641L;
	private HoopSheetCellRenderer defaultRenderer=null;
	private HoopSheetCellColor colorRenderer=null;
		
	/**
	 *
	 */
    public HoopPropertyTable () 
    {
    	debug ("HoopPropertyTable ()");
    	
    	defaultRenderer=new HoopSheetCellRenderer ();
    	colorRenderer=new HoopSheetCellColor (true);
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
    	//debug ("getCellRenderer ("+row+","+column+")");
    	
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
        			
        		if (tester instanceof HoopSerializableTableEntry)
        		{       		
        			//debug ("Table value is HoopSerializable");
        			
        			HoopSerializableTableEntry entry=(HoopSerializableTableEntry) this.getValueAt (row,column);
        			
        			HoopSerializable object=entry.getEntry();
        	
        			if (object!=null)
        			{
        				//debug ("Cell type: " + object.getType ());
        			
        				//if (object.getType().equals("Color")==true)
        				if (object.getType()==HoopDataType.COLOR)
        				{
        					debug ("Detected a color entry ...");
        					return (colorRenderer);
        				}
        				
        				return (defaultRenderer);
        			}
        			
        			/*
        			HoopSAI sai=entry.getSAI();
        			
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
        			HoopArgument arg=entry.getArgument();
        			
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
