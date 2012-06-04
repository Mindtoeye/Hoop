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
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import edu.cmu.cs.in.base.INBase;
//import javax.swing.table.TableCellRenderer;

public class INHoopJTable extends JTable
{
	private static final long serialVersionUID = -1127341907493007641L;
		
	/**
    *
    */
	public INHoopJTable () 
	{
		debug ("INHoopJTable ()");
    	    	
		this.setFont(new Font("Dialog", 1, 10));
	}	
	/**
	 * Constructs a JTable with numRows and numColumns of empty cells using DefaultTableModel.
	 */
	public INHoopJTable(int numRows, int numColumns)
	{
		super (numRows,numColumns);
		
		debug ("INHoopJTable ()");
	}    
	/**
	 * Constructs a JTable to display the values in the two dimensional array, rowData, with column names, columnNames.
	 */
	public INHoopJTable(Object[][] rowData, Object[] columnNames)
	{
	   super (rowData,columnNames);
	   
	   debug ("INHoopJTable ()");
	   
	}    
	/**
    * Constructs a JTable that is initialized with dm as the data model, a default column model, and a default selection model.
    */
	public INHoopJTable(TableModel dm)
	{
	   super (dm);
	   
	   debug ("INHoopJTable ()");
	   	   
	}    
	/**
	 * Constructs a JTable that is initialized with dm as the data model, cm as the column model, and a default selection model.
	 */
	public INHoopJTable(TableModel dm, TableColumnModel cm)
	{
	   super (dm,cm);
	   
	   debug ("INHoopJTable ()");
	   
	}    
	/**
    *	Constructs a JTable that is initialized with dm as the data model, cm as the column model, and sm as the selection model.
    */
	public INHoopJTable(TableModel dm, TableColumnModel cm, ListSelectionModel sm)
	{
	   super (dm,cm,sm);
	   
	   debug ("INHoopJTable ()");
	   
	}    
	/**
    * Constructs a JTable to display the values in the Vector of Vectors, rowData, with column names, columnNames.
    */
	public INHoopJTable(Vector rowData, Vector columnNames)
	{
		super (rowData,columnNames);
		
		debug ("INHoopJTable ()");
	}    	
	/**
    *
    */
    protected void debug (String aMessage)
    {
    	INBase.debug ("INHoopJTable",aMessage);
    }  
	/**
	 *
	 */    
    public boolean isCellEditable (int rowIndex, int vColIndex) 
    {    	
        return true;
    }        
}
