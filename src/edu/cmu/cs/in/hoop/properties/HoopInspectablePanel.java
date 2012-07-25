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

package edu.cmu.cs.in.hoop.properties;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.controls.base.HoopJPanel;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.hoop.properties.HoopPropertyTable;
import edu.cmu.cs.in.hoop.properties.types.HoopSerializable;

/**
 * 
 */
public class HoopInspectablePanel extends HoopJPanel implements ActionListener, ItemListener, TableModelListener
{	
	private static final long serialVersionUID = 1L;
	
	private HoopBase component=null;
	
	private FlatButton foldButton=null;
	private JScrollPane parameterScrollList=null;
	
	private JPanel preview=null;
	
	Icon close=null;
	Icon open=null;
	
	private Boolean folded=false;
	
    private int fixedWidth=200;
    private int fixedHeight=300;	
	    
	JPanel parameterPanel=null;
		
	HoopPropertyTable parameterTable=null;
		
	String[] columnNames = {"Name","Value"};	
	
	DefaultTableModel parameterModel=null;
	
	String panelTitle="No Title";
	
	private class FlatButton extends JButton 
	{
	    private static final long serialVersionUID = 1L;

	    public FlatButton(String label) 
	    {
	        super(label);
	        this.setContentAreaFilled(false);
	    }

	    @Override
	    protected void paintComponent(Graphics g) 
	    {
	        super.paintComponent(g);

	        //g.setColor(Color.WHITE);
	        //g.fillRect(0,0,this.getWidth(),this.getHeight());
	    }
	}
	
	/**
	 * 
	 */		
	public HoopInspectablePanel () 
    {
    	setClassName ("HoopInspectablePanel");
    	debug ("HoopInspectablePanel ()");   
    	
    	create ();
    }
	/**
	 * 
	 */		
	public HoopInspectablePanel (String aTitle)
	{		
    	setClassName ("HoopInspectablePanel");
    	debug ("HoopInspectablePanel ()");   

		panelTitle=aTitle;
		
    	create ();		
	}
	/**
	 * 
	 */
	private void create ()
	{
		debug ("create ()");
		
    	close=HoopLink.imageIcons [82];
    	open=HoopLink.imageIcons [83];
		
		this.setLayout (new BoxLayout (this,BoxLayout.X_AXIS));		
		this.setBorder (BorderFactory.createRaisedBevelBorder());

        Box panelBox=new Box (BoxLayout.Y_AXIS);
        this.add (panelBox);
        
        Box controlBox=Box.createHorizontalBox();
        panelBox.add (controlBox);
        
        foldButton=new FlatButton (panelTitle);
        foldButton.setIcon(close);
        foldButton.setBorder(null);
        //foldButton.setText(panelTitle);
        foldButton.setMargin(new Insets (0,0,0,0));
        foldButton.setFont(new Font("Dialog", 1, 10));
        foldButton.setMinimumSize(new Dimension (20,20));
        foldButton.setPreferredSize(new Dimension (200,20));
        foldButton.setMaximumSize(new Dimension (200,20));
        foldButton.setHorizontalAlignment(SwingConstants.LEFT);
        foldButton.addActionListener (this);

        controlBox.add (foldButton);
        controlBox.add(Box.createHorizontalGlue());
                                                	    
        parameterPanel=new JPanel ();
        //parameterPanel.setBorder(BorderFactory.createLineBorder(Color.white));
        
        parameterModel=new DefaultTableModel (null,columnNames);
        parameterModel.addTableModelListener (this);
        
        parameterTable=new HoopPropertyTable ();
        //parameterTable.setBorder(BorderFactory.createLineBorder(Color.black));
        parameterTable.setModel(parameterModel);
                                
        parameterScrollList=new JScrollPane (parameterTable);        
        //parameterScrollList=new JScrollPane ();
        parameterScrollList.setMinimumSize(new Dimension (10,10));
        parameterScrollList.setPreferredSize(new Dimension (100,100));   
        parameterScrollList.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);        
        //parameterScrollList.setBorder(BorderFactory.createLineBorder(Color.red));
        parameterScrollList.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        parameterScrollList.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        
        BorderLayout configBox=new BorderLayout();
        parameterPanel.setLayout(configBox);
                                       
        parameterPanel.add(parameterScrollList,BorderLayout.CENTER);        
                
        panelBox.add (parameterPanel);	
        
        this.setPreferredSize(new Dimension (fixedWidth,fixedHeight));
	}
	/**
	 * 
	 */	
	public void setTitle (String aTitle)
	{
		if (foldButton!=null)
			foldButton.setText(aTitle);
	}
	/**
	 * 
	 */	
	public void setPreview(JPanel preview) 
	{
		this.preview = preview;
	}
	/**
	 * 
	 */		
	public JPanel getPreview() 
	{
		return preview;
	}	
	/**
	 * 
	 */	
	public void setFixedWidth(int fixedWidth) 
	{
		this.fixedWidth = fixedWidth;
	}
	/**
	 * 
	 */	
	public int getFixedWidth() 
	{
		return fixedWidth;
	}
	/**
	 * 
	 */	
	public Dimension getCurrentDimensions() 
	{
		return (new Dimension (parameterScrollList.getWidth(),parameterScrollList.getHeight()));
	}	
	/**
	 * 
	 */	
	public void setFixedHeight(int fixedHeight) 
	{
		this.fixedHeight = fixedHeight;
	}
	/**
	 * 
	 */	
	public int getFixedHeight() 
	{
		return fixedHeight;
	}	
	/**
	 * 
	 */	
	public void itemStateChanged(ItemEvent e) 
	{
		debug ("itenStateChanged ()");
		
	    Object source=e.getItemSelectable();

	    /*
	    if (source==componentShow) 
	    	checkComponent ();
	    */	
	}	
	/**
	 * 
	 */	
	public void actionPerformed(ActionEvent e) 
	{
		debug ("actionPerformed ()");
		
		if (e.getSource ()==foldButton) 
		{
			debug ("Folding ...");
			
			if (folded==true)
			{
				foldOut ();				
			}
			else
			{			
				foldIn ();				
			}
		}
	}
	/**
	 * 
	 */	
	public void foldOut ()
	{
		if (foldButton!=null)
			foldButton.setIcon(close);
		
		if (parameterPanel!=null)
			parameterPanel.setVisible(true);
						
		this.setMinimumSize(new Dimension (50,26));
		this.setPreferredSize(new Dimension (this.getWidth (),getFixedHeight()));
		//this.setMaximumSize(new Dimension (5000,getFixedHeight()));
		
		folded=false;		
	}
	/**
	 * 
	 */	
	public void foldIn ()
	{
		if (foldButton!=null)
			foldButton.setIcon(open);
		
		this.setMinimumSize(new Dimension (50,26));
		this.setPreferredSize(new Dimension (this.getWidth(),26));				
		//this.setMaximumSize(new Dimension (5000,26));
				
		if (parameterPanel!=null)
			parameterPanel.setVisible(false);
				
		folded=true;		
	}	
	/**
	 * 
	 */	
	public void setComponent(HoopBase component) 
	{
		debug ("setComponent ()");
		
		this.component=component;
				
		configComponentPanel ();
	}
	/**
	 * 
	 */	
	public HoopBase getComponent() 
	{
		return component;
	}
	/**
	 *
	 */
	private void configComponentPanel ()
	{				
		debug ("configComponentPanel ()");
		
		if (parameterTable!=null)
		{						
			parameterModel=new DefaultTableModel (null,columnNames);
									
			ArrayList<HoopSerializable> props=component.getProperties();
			
			for (int i=0;i<props.size();i++)
			{
				HoopSerializable prop=props.get(i);
								
				HoopSerializableTableEntry entry1=new HoopSerializableTableEntry (prop.getName());				
				
				HoopSerializableTableEntry entry2=new HoopSerializableTableEntry (prop.getValue());
				entry2.setEntry(prop);
				entry2.setComponent(getComponent());
				
				HoopSerializableTableEntry[] parameterData = {entry1,entry2};
				
				parameterModel.addRow (parameterData);				
			}
			
			parameterTable.setModel(parameterModel);
			
	        TableColumn colP = parameterTable.getColumnModel().getColumn(1);
	        colP.setCellEditor(new HoopSheetCellEditor());	        					
		}
		else
			debug ("Error: no parameter table available for property sheet");
	}
	/**
	 * 
	 */
	private void untouch ()
	{
		debug ("untouch ()");
			
		for (int j=0;j<parameterModel.getRowCount();j++)
		{
			Object tester=parameterModel.getValueAt(j,1);

			if (tester instanceof HoopSerializableTableEntry)
			{
				HoopSerializableTableEntry value=(HoopSerializableTableEntry) parameterModel.getValueAt(j,1);
				HoopSerializable entry=value.getEntry();
				entry.setTouched(false);
			}
			else
				debug ("Internal error: item in parameter table is not a Hoop entry");
		}	
	}
	/**
	 * 
	 */	
	public void checkComponent ()
	{
		debug ("checkComponent ()");
			
	}
	/**
	 * 
	 */
	public void setPanelContent (JPanel aContent)
	{
		debug ("setPanelContent ()");
		
		if (aContent==null)
		{
			debug ("Internal error: can't insert a null object!");
			return;
		}
		
		//parameterScrollList.removeAll();
		parameterScrollList.setViewportView (aContent);
		parameterScrollList.invalidate();
		
		debug ("setPanelContent () done");
	}
	/*
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
				
				HoopBase target=value.getComponent();
			
				entry.setTouched(true);
									
				entry.setTouched(false);												
			}	
		}		
	}
}
