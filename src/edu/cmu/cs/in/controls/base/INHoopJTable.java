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

import javax.swing.JTable;

import edu.cmu.cs.in.base.INBase;
//import javax.swing.table.TableCellRenderer;

public class INHoopJTable extends JTable
{
	private static final long serialVersionUID = -1127341907493007641L;
	
	//BR_Controller controller=null;	
	
	/**
	 *
	 */
    public INHoopJTable () 
    {
    	debug ("INHoopJTable ()");
    	    	
    	this.setFont(new Font("Dialog", 1, 10));
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
    /*
	public void setController (BR_Controller aController)
	{
		debug ("setController ()");
		
		controller=aController;
	} 
	*/   
	/**
	 *
	 */    
    public boolean isCellEditable (int rowIndex, int vColIndex) 
    {    	
        return true;
    }        
}
