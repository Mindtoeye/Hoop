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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.JTextComponent;

import edu.cmu.cs.in.controls.base.HoopJDialog;
import edu.cmu.cs.in.hoop.properties.HoopPropertyTable;
import edu.cmu.cs.in.hoop.properties.HoopSerializableTableEntry;
import edu.cmu.cs.in.hoop.properties.types.HoopSerializable;


/**
 * 
 */
public class HoopPreferencesDialog extends HoopJDialog implements ActionListener, TableModelListener 
{	
	private static final long serialVersionUID = -1222994810612036014L;

	private GridBagLayout gbl = null;
	
	private JTextField htdocs=null;
	
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
		
		/*
		JPanel configPanel=createConfigPanel ();
		
		JScrollPane aConfigScroller=new JScrollPane (configPanel);
		*/
		
        parameterModel=new DefaultTableModel (null,columnNames);
        parameterModel.addTableModelListener (this);
		
		HoopPropertyTable parameterTable=new HoopPropertyTable ();
        parameterTable.setModel(parameterModel);
        parameterTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
                                
        JScrollPane parameterScrollList=new JScrollPane (parameterTable);
        parameterScrollList.setMinimumSize(new Dimension (10,10));
        parameterScrollList.setPreferredSize(new Dimension (100,100));   
        parameterScrollList.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);        
        parameterScrollList.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);		
		
		contentFrame.add(parameterScrollList);
		
		this.setSize(new Dimension (300,250));
    }
	/**
	 * 
	 */
	private JPanel createConfigPanel ()
	{
		JPanel tfp = new JPanel();
		gbl = new GridBagLayout();
		tfp.setLayout(gbl);
		
		addLabeledComponent (tfp, "Enable disk logging ", htdocs = new JTextField("true", 15));
		addLabeledComponent (tfp, "Preference B: ", htdocs = new JTextField("Default", 15));
		addLabeledComponent (tfp, "Preference C: ", htdocs = new JTextField("Default", 15));
		addLabeledComponent (tfp, "Preference D: ", htdocs = new JTextField("Default", 15));
		addLabeledComponent (tfp, "Preference E: ", htdocs = new JTextField("Default", 15));
		addLabeledComponent (tfp, "Preference F: ", htdocs = new JTextField("Default", 15));
		
		return (tfp);
	}
	/** 
	 * @param tfp
	 * @param labelText
	 * @param comp
	 */
	private void addLabeledComponent (JPanel tfp, String labelText,JComponent comp) 
	{
		debug ("addLabeledComponent ()");
		
		GridBagConstraints c = new GridBagConstraints();
		
		if (comp instanceof JTextComponent)
		{
			c.insets = new Insets(2, 2, 2, 2);
		}
		
	    c.anchor = GridBagConstraints.EAST;
	    JLabel label = new JLabel(labelText);
	    c.gridwidth = GridBagConstraints.RELATIVE; //next-to-last
	    c.fill = GridBagConstraints.NONE;      //reset to default
	    c.weightx = 0.0;                       //reset to default
	    tfp.add(label, c);

	    c.gridwidth = GridBagConstraints.REMAINDER;  //end of row
	    c.fill = GridBagConstraints.HORIZONTAL;
	    c.weightx = 1.0;
	    
	    tfp.add(comp, c);
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
				
				/*
				HoopBase target=value.getComponent();
			
				entry.setTouched(true);
									
				entry.setTouched(false);
				*/												
			}	
		}		
	}
}
