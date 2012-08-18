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

package edu.cmu.cs.in.controls.base;

import java.awt.Font;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import edu.cmu.cs.in.base.HoopRoot;
//import javax.swing.table.TableCellRenderer;

public class HoopJTable extends JTable
{
	private static final long serialVersionUID = -1127341907493007641L;
		
	private TableCellRenderer globalRenderer=null;
	
	/**
    *
    */
	public HoopJTable () 
	{
		debug ("HoopJTable ()");
		
		autoCreateColumnsFromModel=true;
    	    			
		this.setFont(new Font("Dialog", 1, 10));
	}	
	/**
	 * Constructs a JTable with numRows and numColumns of empty cells using DefaultTableModel.
	 */
	public HoopJTable(int numRows, int numColumns)
	{
		super (numRows,numColumns);
		
		autoCreateColumnsFromModel=true;
		
		debug ("HoopJTable ()");
	}    
	/**
	 * Constructs a JTable to display the values in the two dimensional array, rowData, with column names, columnNames.
	 */
	public HoopJTable(Object[][] rowData, Object[] columnNames)
	{
	   super (rowData,columnNames);
	   
	   autoCreateColumnsFromModel=true;
	   
	   debug ("HoopJTable ()");	   
	}    
	/**
    * Constructs a JTable that is initialized with dm as the data model, a default column model, and a default selection model.
    */
	public HoopJTable(TableModel dm)
	{
	   super (dm);
	   
	   autoCreateColumnsFromModel=true;
	   
	   debug ("HoopJTable ()");	   	   
	}    
	/**
	 * Constructs a JTable that is initialized with dm as the data model, cm as the column model, and a default selection model.
	 */
	public HoopJTable(TableModel dm, TableColumnModel cm)
	{
	   super (dm,cm);

	   autoCreateColumnsFromModel=true;
	   
	   debug ("HoopJTable ()");	   	   	   
	}    
	/**
    *	Constructs a JTable that is initialized with dm as the data model, cm as the column model, and sm as the selection model.
    */
	public HoopJTable(TableModel dm, TableColumnModel cm, ListSelectionModel sm)
	{
	   super (dm,cm,sm);
	   
	   autoCreateColumnsFromModel=true;	   
	   
	   debug ("HoopJTable ()");	   
	}    
	/**
    * Constructs a JTable to display the values in the Vector of Vectors, rowData, with column names, columnNames.
    */
	public HoopJTable(Vector rowData, Vector columnNames)
	{
		super (rowData,columnNames);
		
		autoCreateColumnsFromModel=true;		
		
		debug ("HoopJTable ()");
	}    	
	/**
    *
    */
    protected void debug (String aMessage)
    {
    	HoopRoot.debug ("HoopJTable",aMessage);
    }  
	/**
	 *
	 */    
    public boolean isCellEditable (int rowIndex, int vColIndex) 
    {    	
        return true;
    }
	/**
	 * 
	 */
	protected void alert (String aMessage)
	{
		HoopRoot.alert(aMessage);
	}    
	/**
	 *
	 */    
	public TableCellRenderer getCellRenderer (int row, int column) 
	{
		if (globalRenderer!=null)
			return (globalRenderer);
		
		return (getDefaultRenderer (getColumnClass (column)));
	}	
	/**
	 * 
	 * @param globalRenderer
	 */
	public void setGlobalRenderer(TableCellRenderer globalRenderer) 
	{
		this.globalRenderer = globalRenderer;
	}
	/**
	 * 
	 */
	public TableCellRenderer getGlobalRenderer() 
	{
		return globalRenderer;
	}
}
