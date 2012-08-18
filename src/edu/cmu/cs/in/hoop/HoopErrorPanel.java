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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

import edu.cmu.cs.in.controls.HoopMultiLineCellRenderer;
import edu.cmu.cs.in.controls.base.HoopEmbeddedJPanel;
import edu.cmu.cs.in.controls.base.HoopJTable;

/** 
 * @author vvelsen
 *
 */
public class HoopErrorPanel extends HoopEmbeddedJPanel implements ActionListener
{	
	private class HoopErrorObject
	{
		public String aSource="";
		public String anError="";
	}
	
	private static final long serialVersionUID = 1L;
	
	private HoopJTable table=null;
	private HoopMultiLineCellRenderer textCellRenderer=null;
	private int lines=3;
	
	private String[] columnNames = {"Key","Value"};
	
	private ArrayList <HoopErrorObject> errors=null;
	
	/**
	 * http://stackoverflow.com/questions/965023/how-to-wrap-lines-in-a-jtable-cell
	 */	
	public HoopErrorPanel ()
	{
		setClassName ("HoopErrorPanel");
		debug ("HoopErrorPanel ()");
		
    	Box holder = new Box (BoxLayout.Y_AXIS);    	

    	errors=new ArrayList<HoopErrorObject> ();
    	    			
    	textCellRenderer=new HoopMultiLineCellRenderer ();
    	
		// We need some empty data because Java crashes when you provide a null parameter in the constructor
		Object[][] data ={}; 
    	
		table=new HoopJTable (data,columnNames);
		table.setGlobalRenderer(textCellRenderer);
		
		JScrollPane scrollPane = new JScrollPane(table);
		//table.setFillsViewportHeight(true);		
		//table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		holder.add (scrollPane);
		
		this.add(holder);		
	}
	/**
	 * 
	 */
	public void addError (String aHoop,String anError)
	{
		debug ("addError ()");
		
		debug ("Hoop: " + aHoop + ", " + anError);
		
		HoopErrorObject anErrorObject=new HoopErrorObject ();
		anErrorObject.aSource=aHoop;
		anErrorObject.anError=anError;
		
		errors.add(anErrorObject);
		
		showErrors ();
		
		if (errors.size()>0)
		{
			int rowHeight = table.getRowHeight();
			
			int lineCount=countLines (anError);
			
			debug ("Setting new row " + (errors.size()-1) + " to height: " + (lineCount*rowHeight));
			
			table.setRowHeight(errors.size()-1,(lineCount*rowHeight));
		}			
	}
	/**
	 * 
	 */
	private void showErrors ()
	{
		debug ("showErrors ()");
		
		DefaultTableModel model=new DefaultTableModel (null,columnNames);
		
		for (int i=0;i<errors.size();i++)
		{		
			HoopErrorObject anError=errors.get(i);
			
			model.addRow (new Object[]{anError.aSource,anError.anError});
		}	
		
		table.setModel(model);		
	}
	/**
	 * 
	 */	
	public HoopJTable getTable() 
	{
		return table;
	}
	/**
	 * 
	 */
	/*
	public JTable getTable() 
	{
		return table;
	}
	*/	
	/**
	 * 
	 */
	public void handleCloseEvent ()
	{
		debug ("handleCloseEvent ()");
		
	}
	/**
	 *
	 */	
	public void updateContents() 
	{
		debug ("updateContents ()");
		
	}
	/**
	 *
	 */	
	/*
	public void updateSize() 
	{
		debug ("updateSize ()");
		
		TableColumn col1 = table.getColumnModel().getColumn(0);
		col1.setPreferredWidth(primColWidth);		
	 
		this.setVisible(true);
		
		debug ("Total width: " + this.getWidth());
		
		TableColumn col2 = table.getColumnModel().getColumn(1);
		col2.setPreferredWidth(this.getWidth()-primColWidth);		
	}
	*/	
	/**
	 * 
	 */
	@Override
	public void actionPerformed(ActionEvent event) 
	{
		debug ("actionPerformed ()");
		
	}	
	/** 
	 * @param str
	 * @return
	 */
	private int countLines (String str)
	{
		String[] lines = str.split("\r\n|\r|\n");
		
		return (lines.length);
	}	
}
