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

package edu.cmu.cs.in.controls.dialogs;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.ArrayList;
//import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.controls.base.HoopJDialog;
import edu.cmu.cs.in.hoop.properties.HoopPropertyTable;
import edu.cmu.cs.in.hoop.properties.HoopSerializableTableEntry;
import edu.cmu.cs.in.hoop.properties.HoopSheetCellEditor;
import edu.cmu.cs.in.hoop.properties.types.HoopSerializable;

/**
 * 
 */
public class HoopPreferencesDialog extends HoopJDialog implements ActionListener, TableModelListener 
{	
	private static final long serialVersionUID = -1222994810612036014L;
	
	private String[] columnNames = {"Name","Value"};	
	
	private DefaultTableModel parameterModel=null;	
	
	private HoopPropertyTable parameterTable=null;
	
	/**
     * 
     */
    public HoopPreferencesDialog (JFrame frame, boolean modal) 
	{
		super (HoopJDialog.OK,frame, modal,"Preferences");
		
		setClassName ("HoopPreferencesDialog");
		debug ("HoopPreferencesDialog ()");				

		JPanel contentFrame=getFrame ();
				
        parameterModel=new DefaultTableModel (null,columnNames);
        parameterModel.addTableModelListener (this);
		
		parameterTable=new HoopPropertyTable ();
        parameterTable.setModel(parameterModel);
        parameterTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
                                
        JScrollPane parameterScrollList=new JScrollPane (parameterTable);
        parameterScrollList.setMinimumSize(new Dimension (10,10));
        parameterScrollList.setPreferredSize(new Dimension (100,100));   
        parameterScrollList.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);        
        parameterScrollList.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);		
		
		contentFrame.add(parameterScrollList);
		
		configPanel ();
		
		this.setSize(new Dimension (300,250));
    }
	/**
	 * 
	 */
	@Override
	public void tableChanged(TableModelEvent tEvent) 
	{
		debug ("tableChanged ()");
		
		debug ("Table changed: " + tEvent.getFirstRow() + "," + tEvent.getType());
		
		if (tEvent.getType()==TableModelEvent.UPDATE)
		{
			debug ("Propagating parameter value back into Hoop object ...");
			
			Object tester=parameterTable.getValueAt(tEvent.getFirstRow(),1);
			debug ("Style object: " + tester.getClass().getName() + " with value: " + tester);						
			
			HoopSerializableTableEntry value=(HoopSerializableTableEntry) parameterModel.getValueAt(tEvent.getFirstRow(),1);
			HoopSerializable entry=(HoopSerializable) value.getEntry();
			
			if (entry!=null)
			{							
				debug ("Entry: " + entry.toString());																
			}	
		}		
	}
	/**
	 *
	 */
	private void configPanel ()
	{				
		debug ("configPanel ()");
		
		if (parameterTable!=null)
		{						
			parameterModel=new DefaultTableModel (null,columnNames);
			
			ArrayList <HoopSerializable> props=HoopLink.props.getProperties();
												
			for (int i=0;i<props.size();i++)
			{
				HoopSerializable prop=props.get(i);
				
				if (prop.getEnabled()==true)
				{							
					HoopSerializableTableEntry entry1=new HoopSerializableTableEntry (prop.getName());				
				
					HoopSerializableTableEntry entry2=new HoopSerializableTableEntry (prop.getValue());
					entry2.setEntry(prop);
					//entry2.setComponent(getHoop());
				
					HoopSerializableTableEntry[] parameterData = {entry1,entry2};
				
					parameterModel.addRow (parameterData);
				}	
			}
			
			parameterTable.setModel(parameterModel);
			parameterTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
			
	        TableColumn colP = parameterTable.getColumnModel().getColumn(1);
	        colP.setCellEditor(new HoopSheetCellEditor());	        					
		}
		else
			debug ("Error: no parameter table available for property sheet");		
	}	
}
