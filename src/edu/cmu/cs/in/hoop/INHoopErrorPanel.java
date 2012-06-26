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

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

import edu.cmu.cs.in.controls.base.INEmbeddedJPanel;
import edu.cmu.cs.in.controls.base.INHoopJTable;

/** 
 * @author vvelsen
 *
 */
public class INHoopErrorPanel extends INEmbeddedJPanel implements ActionListener
{	
	private static final long serialVersionUID = 1L;			
	private INHoopJTable table=null;		
	//private String[] columnNames = {"Hoop","Error"};	
	private DefaultTableModel errorData=null;
	
	/**
	 * http://stackoverflow.com/questions/965023/how-to-wrap-lines-in-a-jtable-cell
	 */	
	public INHoopErrorPanel ()
	{
		setClassName ("INHoopTablePanel");
		debug ("INHoopTablePanel ()");
		
    	Box holder = new Box (BoxLayout.Y_AXIS);    	

    	errorData = new DefaultTableModel();    
    	errorData.addColumn("Hoop");  
    	errorData.addColumn("Error/Warning");      	
    			
		table=new INHoopJTable (errorData);
		
		JScrollPane scrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);		
		//table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		//holder.add(controlBox);
		holder.add (scrollPane);
		
		this.add(holder);		
	}
	/**
	 * 
	 */
	public void addError (String aHoop,String anError)
	{
		debug ("addError ()");
					
		errorData.addRow(new Object[]{aHoop, anError});
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
	@Override
	public void actionPerformed(ActionEvent event) 
	{
		debug ("actionPerformed ()");
		
	}	
}
