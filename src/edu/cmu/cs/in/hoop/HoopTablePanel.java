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

//import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

//import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
//import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import edu.cmu.cs.in.base.HoopDataType;
import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.base.kv.HoopKV;
import edu.cmu.cs.in.controls.base.HoopEmbeddedJPanel;
import edu.cmu.cs.in.controls.base.HoopJTable;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;

/** 
 * @author vvelsen
 *
 */
public class HoopTablePanel extends HoopEmbeddedJPanel implements ActionListener, ItemListener
{	
	private static final long serialVersionUID = 1L;
			
	private HoopJTable table=null;
	
	private JTextField status=null;
    private JTextField minRange=null;
    private JTextField maxRange=null;    
    private JButton setRange=null;    
    private JButton previousSet=null;
    private JButton nextSet=null;	
	private JCheckBox autoUpdate=null;
	
    private JRadioButton showData=null;    
    private JRadioButton showTrash=null;
    
	private int primColWidth=100;
	
	private String[] columnNames = {"Key","Value"};	
	
	private Boolean displayData=true;
	
	private HoopBase hoop=null;
	
	/**
	 * http://stackoverflow.com/questions/965023/how-to-wrap-lines-in-a-jtable-cell
	 */	
	public HoopTablePanel()
	{
		setClassName ("HoopTablePanel");
		debug ("HoopTablePanel ()");
		
    	Box holder = new Box (BoxLayout.Y_AXIS);    	
		Box controlBox = new Box (BoxLayout.X_AXIS);
		
		//Border border=BorderFactory.createLineBorder(Color.black);
		
		autoUpdate=new JCheckBox ();
		autoUpdate.setText("Auto Update");
		autoUpdate.setFont(new Font("Dialog",1,10));
		autoUpdate.setPreferredSize(new Dimension (100,20));
		autoUpdate.addItemListener(this);	
		controlBox.add(autoUpdate);
		
	    minRange=new JTextField ();
	    minRange.setText("0");
	    minRange.setFont(new Font("Dialog", 1, 10));
	    minRange.setMinimumSize(new Dimension (30,18));
	    minRange.setPreferredSize(new Dimension (30,18));
	    minRange.setMaximumSize(new Dimension (30,18));
	    controlBox.add(minRange);
	    	    	    	    
	    maxRange=new JTextField ();
	    maxRange.setText("100");
	    maxRange.setFont(new Font("Dialog", 1, 10));
	    maxRange.setMinimumSize(new Dimension (50,20));
	    maxRange.setPreferredSize(new Dimension (50,20));
	    maxRange.setMaximumSize(new Dimension (30,18));
	    controlBox.add(maxRange);
	    	    
	    setRange=new JButton ();
	    setRange.setMargin(new Insets(1,1,1,1));
	    setRange.setText("Set");
	    setRange.setFont(new Font("Dialog", 1, 10));
	    setRange.setMinimumSize(new Dimension (60,20));
	    setRange.setPreferredSize(new Dimension (60,20));
	    setRange.addActionListener(this);
	    controlBox.add(setRange);	    
	    
	    previousSet=new JButton ();
	    previousSet.setMargin(new Insets(1,1,1,1));
	    previousSet.setText("Previous");
	    previousSet.setFont(new Font("Dialog", 1, 10));
	    previousSet.setMinimumSize(new Dimension (60,20));
	    previousSet.setPreferredSize(new Dimension (60,20));
	    previousSet.addActionListener(this);
	    controlBox.add(previousSet);
	    
	    nextSet=new JButton ();
	    nextSet.setMargin(new Insets(1,1,1,1));  
	    nextSet.setText("Next");
	    nextSet.setFont(new Font("Dialog", 1, 10));
	    nextSet.setMinimumSize(new Dimension (60,20));
	    nextSet.setPreferredSize(new Dimension (60,20));
	    nextSet.addActionListener(this);
	    controlBox.add(nextSet);	    
	    
	    controlBox.add(new JSeparator(SwingConstants.VERTICAL));
		
	    showData = new JRadioButton();
	    showData.setText("Show Data");
	    showData.setIcon(HoopLink.getImageByName("data.gif"));
	    showData.setSelected(true);
	    showData.setFont(new Font("Dialog", 1, 10));
	    showData.addActionListener(this);
	    
	    showTrash = new JRadioButton();
	    showTrash.setText("Show Discarded");
	    showTrash.setIcon(HoopLink.getImageByName("delete.png"));
	    showTrash.setFont(new Font("Dialog", 1, 10));
	    showTrash.addActionListener(this);
	    
	    //Group the radio buttons.
	    ButtonGroup group=new ButtonGroup();
	    group.add (showData);
	    group.add (showTrash);
	    
	    controlBox.add(showData);
	    controlBox.add(showTrash);
	    
		controlBox.setMinimumSize(new Dimension (100,24));
		controlBox.setPreferredSize(new Dimension (100,24));		

	    controlBox.add(new JSeparator(SwingConstants.VERTICAL));

	    status=new JTextField ();
	    status.setText("Status: OK");
	    status.setEditable(false);
	    //status.setBorder(border);
	    status.setFont(new Font("Dialog", 1, 10));
	    status.setMinimumSize(new Dimension (20,20));
	    status.setPreferredSize(new Dimension (100,20));
	    controlBox.add(status);	    
		
		// We need some empty data because Java crashes when you provide a null parameter in the constructor
		Object[][] data ={}; 
		
		table=new HoopJTable (data,columnNames);
		
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
	public HoopJTable getTable() 
	{
		return table;
	}
	/**
	 * 
	 */
	public void showHoop (HoopBase aHoop)
	{
		debug ("showHoop ()");
		
		hoop=aHoop;
		
		updateContents ();
	}
	/**
	 * 
	 */	
	public void setTable(HoopJTable table) 
	{
		this.table = table;
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
		
		if (hoop==null)
		{
			debug ("No data provided yet");
			return;
		}
		
		//TableColumnModel columnModel=table.getColumnModel();
		
		ArrayList <HoopKV> content=null;
		
		if (displayData==true)
			content=hoop.getData();
		else
			content=hoop.getTrash();
					
		// Convert KV model to table model and show
					
		ArrayList <HoopDataType> types=hoop.getTypes();
		
		String[] cNames = new String [types.size()];
				
		for (int n=0;n<types.size();n++)
		{
			HoopDataType aType=types.get(n);
			
			//debug ("Adding column: " + n + " " + aType.getTypeValue() + " ("+aType.typeToString()+")");
			
			if (n==0)
				cNames [0]="Key: " + aType.getTypeValue()+" ("+aType.typeToString()+")";
			else
				cNames [n]=aType.getTypeValue()+" ("+aType.typeToString()+")";
		}
				
		DefaultTableModel model=new DefaultTableModel (null,cNames);
				
		// For large data sets we will have to use ranges on the index!
		
		if (content!=null)
		{
			for (HoopKV p : content) 
			{				
				String [] rowData=new String [p.getValueSize()+1];
				
				rowData [0]=p.getKeyString();
				
				//debug ("Adding " + p.getValueSize() + " values");
				
				for (int i=0;i<p.getValueSize();i++)
				{
					String aValue=p.getValueAsString(i);
					
					if (aValue.length()>100)
					{
						rowData [i+1]="*Data too Large*";
					}
					else
						rowData [i+1]=aValue;
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
	@Override
	public void actionPerformed(ActionEvent event) 
	{
		debug ("actionPerformed ()");
		
		if (event.getSource()==setRange)
		{
			/*
			sortby=BYRANGE;
			
			createRange ();
			
			refreshTable ();
			*/
			
			return;
		}
		
		if (event.getSource()==showData)
		{
			displayData=true;
			updateContents ();
		}
		
		if (event.getSource()==showTrash)
		{
			displayData=false;
			updateContents ();
		}
	}	
	/**
	 *
	 */	
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
	/**
	 * 
	 */
	@Override
	public void itemStateChanged(ItemEvent arg0) 
	{
		
		
	}	
}
