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

package edu.cmu.cs.in.hoop;

import java.util.ArrayList;

import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

import edu.cmu.cs.in.base.INKV;
import edu.cmu.cs.in.controls.base.INEmbeddedJPanel;
import edu.cmu.cs.in.controls.base.INHoopJTable;
import edu.cmu.cs.in.hoop.base.INHoopBase;

/** 
 * @author vvelsen
 *
 */
public class INHoopTablePanel extends INEmbeddedJPanel
{	
	private static final long serialVersionUID = 1L;
			
	private INHoopJTable table=null;
	
	private String[] columnNames = {"Key","Value"};	
	
	/**
	 * Creates a new JPanel with a double buffer and a flow layout.
	 */	
	public INHoopTablePanel()
	{
		setClassName ("INHoopTablePanel");
		debug ("INHoopTablePanel ()");

		Object[][] data ={}; 
		
		table=new INHoopJTable (data,columnNames);
		
		JScrollPane scrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);		
		
		this.add(scrollPane);		
	}
	/**
	 * 
	 */	
	public INHoopJTable getTable() 
	{
		return table;
	}
	/**
	 * 
	 */
	public void showHoop (INHoopBase aHoop)
	{
		debug ("showHoop ()");
		
		ArrayList <INKV> content=aHoop.getData();
		
		// Convert KV model to table model and show
		
		DefaultTableModel model=new DefaultTableModel (null,columnNames);
				
		// For large data sets we will have to use ranges on the index!
		
		if (content!=null)
		{
			for (INKV p : content) 
			{
				model.addRow(new String[] {p.getKeyString(), p.getValue()});
			}
		
			table.setModel(model);
		}
		
		debug ("showHoop () done");
	}
	/**
	 * 
	 */	
	public void setTable(INHoopJTable table) 
	{
		this.table = table;
	}	
	/**
	 * 
	 */
	public void handleCloseEvent ()
	{
		debug ("handleCloseEvent ()");
		
		// Process this in child class!!
	}
	/**
	 *
	 */	
	public void updateContents() 
	{
		debug ("updateContents ()");
		
		// Implement in child class!!
	}	
}
