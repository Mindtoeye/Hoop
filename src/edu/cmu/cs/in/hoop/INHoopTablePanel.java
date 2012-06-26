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

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import edu.cmu.cs.in.base.INHoopLink;
import edu.cmu.cs.in.base.INKV;
import edu.cmu.cs.in.controls.base.INEmbeddedJPanel;
import edu.cmu.cs.in.controls.base.INHoopJTable;
import edu.cmu.cs.in.hoop.base.INHoopBase;

/** 
 * @author vvelsen
 *
 */
public class INHoopTablePanel extends INEmbeddedJPanel implements ActionListener
{	
	private static final long serialVersionUID = 1L;
			
	private INHoopJTable table=null;
	
	private String[] columnNames = {"Key","Value"};	
	
	/**
	 * http://stackoverflow.com/questions/965023/how-to-wrap-lines-in-a-jtable-cell
	 */	
	public INHoopTablePanel()
	{
		setClassName ("INHoopTablePanel");
		debug ("INHoopTablePanel ()");
		
    	Box holder = new Box (BoxLayout.Y_AXIS);
    	
		Box controlBox = new Box (BoxLayout.X_AXIS);
		
		JButton clearButton=new JButton ();
		clearButton.setIcon(INHoopLink.imageIcons [8]);
		clearButton.setMargin(new Insets(1, 1, 1, 1));
		//clearButton.setText("Clear");
		clearButton.setFont(new Font("Courier",1,8));
		clearButton.setPreferredSize(new Dimension (20,16));
		clearButton.addActionListener(this);
		
		JButton saveButton=new JButton ();	
		saveButton.setIcon(INHoopLink.imageIcons [19]);
		saveButton.setMargin(new Insets(1, 1, 1, 1));
		saveButton.setFont(new Font("Courier",1,8));
		saveButton.setPreferredSize(new Dimension (20,16));
		saveButton.addActionListener(this);
		
		controlBox.add (clearButton);
		controlBox.add (saveButton);
		controlBox.add(Box.createHorizontalGlue());
		
		controlBox.setMinimumSize(new Dimension (100,24));
		controlBox.setPreferredSize(new Dimension (100,24));		

		// We need some empty data because Java crashes when you provide a null parameter in the constructor
		Object[][] data ={}; 
		
		table=new INHoopJTable (data,columnNames);
		
		JScrollPane scrollPane = new JScrollPane(table);
		//table.setFillsViewportHeight(true);		
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		holder.add(controlBox);
		holder.add (scrollPane);
		
		this.add(holder);		
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
		
		TableColumnModel columnModel=table.getColumnModel();
		
		ArrayList <INKV> content=aHoop.getData();
					
		// Convert KV model to table model and show
		
		String[] cNames = new String [aHoop.getMaxValues()+1];
		
		cNames [0]="Key";
		cNames [1]="Value";
		
		for (int w=0;w<(aHoop.getMaxValues()-2);w++)
		{
			cNames [w+2]="V"+w;
		}
		
		DefaultTableModel model=new DefaultTableModel (null,cNames);
				
		// For large data sets we will have to use ranges on the index!
		
		if (content!=null)
		{
			for (INKV p : content) 
			{				
				String [] rowData=new String [p.getValueSize()+1];
				
				rowData [0]=p.getKeyString();
				
				//debug ("Adding " + p.getValueSize() + " values");
				
				for (int i=0;i<p.getValueSize();i++)
				{
					rowData [i+1]=p.getValue(i);
				}
				
				model.addRow(rowData);
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
	/**
	 * 
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) 
	{
		debug ("actionPerformed ()");
		
	}	
}
