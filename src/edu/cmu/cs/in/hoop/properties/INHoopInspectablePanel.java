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
//import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
//import java.io.File;
//import java.net.URL;
import java.util.ArrayList;
//import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

//import edu.cmu.old_pact.dormin.trace;
//import edu.cmu.pact.BehaviorRecorder.Controller.BR_Controller;
import edu.cmu.cs.in.base.INLink;
import edu.cmu.cs.in.controls.base.INJPanel;
//import edu.cmu.cs.in.controls.base.INJPanel;
import edu.cmu.cs.in.hoop.properties.INHoopPropertyTable;

/**
 * 
 */
public class INHoopInspectablePanel extends INJPanel implements ActionListener, ItemListener
{	
	private static final long serialVersionUID = 1L;
	
	private INHoopInspectable component=null;
	
	private JButton foldButton=null;
	private JLabel componentType=null;
	
	private JPanel preview=null;
	
	Icon close=null;
	Icon open=null;
	
	private Boolean folded=false;
	
    private int fixedWidth=200;
    private int fixedHeight=300;	
	    
	JPanel parameterPanel=null;
	JPanel stylePanel=null;
	JPanel SAIPanel=null;
	
	JCheckBox componentShow=null;
	JLabel componentIcon=null;
	
	INHoopPropertyTable parameterTable=null;
	INHoopPropertyTable styleTable=null;
		
	String[] columnNames = {"Name","Value"};	
	
	DefaultTableModel parameterModel=null;
	DefaultTableModel styleModel=null;
	
	//BR_Controller controller=null;
	
	/**
	 * 
	 */		
	public INHoopInspectablePanel () 
    {
    	setClassName ("INHoopInspectablePanel");
    	debug ("INHoopInspectablePanel ()");   
    	
		//close=new ImageIcon(getClass().getClassLoader().getResource("pact/CommWidgets/close.png"));
		//open=new ImageIcon(getClass().getClassLoader().getResource("pact/CommWidgets/open.png"));
    	close=INLink.imageIcons [82];
    	open=INLink.imageIcons [83];
		
		this.setLayout (new BoxLayout (this,BoxLayout.X_AXIS));		
		this.setBorder(BorderFactory.createRaisedBevelBorder());

        Box panelBox=new Box (BoxLayout.Y_AXIS);
        this.add (panelBox);
        
        //>-------------------------------------------------

        Box controlBox=new Box (BoxLayout.X_AXIS);
        panelBox.add (controlBox);
        
        foldButton=new JButton ();
        foldButton.setIcon(close);
        foldButton.setText("");
        foldButton.setFont(new Font("Dialog", 1, 10));
        foldButton.setMinimumSize(new Dimension (20,20));
        foldButton.setMaximumSize(new Dimension (5000,20));
        foldButton.setHorizontalAlignment(SwingConstants.LEFT);
        foldButton.addActionListener (this);
        controlBox.add (addInHorizontalLayout (foldButton,5000,20));
        
        componentShow=new JCheckBox ("Show");                
        componentShow.setSelected(false);                
        componentShow.addItemListener(this);
        
        controlBox.add(componentShow);
        
        defaultIcon = createImageIcon("INHoopButton.png",null);        
        componentIcon=new JLabel ();                
        componentIcon.setMinimumSize(new Dimension (20,20));
        componentIcon.setPreferredSize(new Dimension (20,20));
        componentIcon.setMaximumSize(new Dimension (20,20));
        componentIcon.setIcon(defaultIcon);
        
        controlBox.add(componentIcon);        
                
        //>-------------------------------------------------
               
        componentType=new JLabel();
        componentType.setFont(new Font("Dialog", 1, 10));   
        componentType.setMinimumSize(new Dimension (10,20));
        componentType.setMaximumSize(new Dimension (5000,20));        
        componentType.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
                      
        panelBox.add (addInHorizontalLayout (componentType,5000,20));
        
        //>-------------------------------------------------        
        
	    JTabbedPane tabbedPane=new JTabbedPane();
	    tabbedPane.setMinimumSize(new Dimension (10,20));
	    tabbedPane.setMaximumSize(new Dimension (5000,5000));	    
	    tabbedPane.setFont(new Font("Dialog",1,10));	
	    	    	    
	    panelBox.add(addInHorizontalLayout (tabbedPane,5000,5000));
        
        //>-------------------------------------------------
        
        stylePanel=new JPanel ();
        tabbedPane.addTab ("Styles",null,stylePanel,"tbd");
                                               
        BorderLayout styleBox=new BorderLayout();
        stylePanel.setLayout(styleBox);        
                		        
        styleTable=new INHoopPropertyTable ();
        styleTable.setBorder(BorderFactory.createLineBorder(Color.black));
        styleTable.setMinimumSize(new Dimension (20,20));
        styleTable.setMaximumSize(new Dimension (5000,5000));
                
        JScrollPane styleScrollList=new JScrollPane (styleTable);
        styleScrollList.setMinimumSize(new Dimension (10,10));
        styleScrollList.setMaximumSize(new Dimension (5000,5000));
        
        stylePanel.add (styleScrollList,BorderLayout.CENTER);	    
	    
        //>-------------------------------------------------
        	    
        parameterPanel=new JPanel ();
        tabbedPane.addTab ("Parameters",null,parameterPanel,"tbd");
                                               
        BorderLayout configBox=new BorderLayout();
        parameterPanel.setLayout(configBox);
                                       
        parameterTable=new INHoopPropertyTable ();
        parameterTable.setBorder(BorderFactory.createLineBorder(Color.black));
        parameterTable.setMaximumSize(new Dimension (5000,5000));
                
        JScrollPane parameterScrollList=new JScrollPane (parameterTable);
        parameterScrollList.setMinimumSize(new Dimension (10,10));
        parameterScrollList.setMaximumSize(new Dimension (5000,5000));
                
        parameterPanel.add(parameterScrollList,BorderLayout.CENTER);
                
        //>-------------------------------------------------
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

	    if (source==componentShow) 
	    	checkComponent ();
	}	
	/**
	 * 
	 */	
	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource ()==foldButton) 
		{
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
		
		if (stylePanel!=null)
			stylePanel.setVisible(true);
		
		if (componentType!=null)
			componentType.setVisible (true);
		
		this.setMinimumSize(new Dimension (50,26));
		this.setPreferredSize(new Dimension (this.getWidth (),getFixedHeight()));
		this.setMaximumSize(new Dimension (5000,getFixedHeight()));
		
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
		this.setMaximumSize(new Dimension (5000,26));
		
		if (componentType!=null)
			componentType.setVisible(false);
		
		if (parameterPanel!=null)
			parameterPanel.setVisible(false);
		
		if (stylePanel!=null)
			stylePanel.setVisible(false);
		
		folded=true;		
	}	
	/**
	 * 
	 */	
	public void setComponent(INHoopInspectable component) 
	{
		this.component=component;
		this.component.setChecker (componentShow);
		
		configComponentPanel ();
	}
	/**
	 * 
	 */	
	public INHoopInspectable getComponent() 
	{
		return component;
	}
	/**
	 *
	 */
	private void configComponentPanel ()
	{
		if (foldButton!=null)
			foldButton.setText(component.getInstanceName ());
		
		if (componentType!=null)
		{
			componentType.setText(component.getClassType());
			
	        ImageIcon icon = createImageIcon(component.getClassType()+".png",null);			
	        componentIcon.setIcon(icon);
		}
				
		if (parameterTable!=null)
		{						
			parameterModel=new DefaultTableModel (null,columnNames);
		
			parameterModel.addTableModelListener(new TableModelListener() 
			{
				@Override
				public void tableChanged(TableModelEvent arg0) 
				{
					debug ("Table changed: " + arg0.getFirstRow() + "," + arg0.getType());
					
					if (arg0.getType()==TableModelEvent.UPDATE)
					{
						debug ("Propagating parameter value back into INHoop object ...");
						
						Object tester=parameterTable.getValueAt(arg0.getFirstRow(),1);
						debug ("Style object: " + tester.getClass().getName() + " with value: " + tester);						
						
						INHoopSerializableTableEntry value=(INHoopSerializableTableEntry) parameterModel.getValueAt(arg0.getFirstRow(),1);
						INHoopStyleProperty entry=(INHoopStyleProperty) value.getEntry();
						if (entry!=null)
						{							
							debug ("Entry: " + entry.toString());
							
							INHoopInspectable target=value.getComponent();
						
							entry.setTouched(true);
						
							/*
							if (controller!=null)
							{
								//debug ("Sending: " +target.toStringElementTouched());
								
								controller.sendInterfaceDescription (target.getName(),target.toStringElementTouched());
							}	
							else
								debug ("Error: controller object is null!");
							*/	
						
							entry.setTouched(false);
						
							//untouch ();
						}	
					}
				}
			});						
			
			/*
			ArrayList<INHoopParameter> params=component.getParameters();
						
			for (int i=0;i<params.size();i++)
			{
				INHoopParameter param=params.get(i);
								
				INHoopSerializableTableEntry entry1=new INHoopSerializableTableEntry (param.getInstanceName());				
				
				INHoopSerializableTableEntry entry2=new INHoopSerializableTableEntry (param.getValue());
				entry2.setEntry(param);
				entry2.setComponent(getComponent());
				
				INHoopSerializableTableEntry[] parameterData = {entry1,entry2};
				
				parameterModel.addRow (parameterData);				
			}
			
			parameterTable.setModel(parameterModel);
			
	        TableColumn colP = parameterTable.getColumnModel().getColumn(1);
	        colP.setCellEditor(new INHoopSheetCellEditor());
	        */			
		}
		
		if (styleTable!=null)
		{
			styleModel=new DefaultTableModel (null,columnNames);			
			
			styleModel.addTableModelListener(new TableModelListener()
			{
				@Override
				public void tableChanged(TableModelEvent arg0) 
				{
					debug ("Table changed: " + arg0.getFirstRow() + "," + arg0.getType());
					
					if (arg0.getType()==TableModelEvent.UPDATE)
					{
						debug ("Propagating style value back into INHoop object ...");
							
						Object tester=styleTable.getValueAt(arg0.getFirstRow(),1);
						//debug ("Style object: " + tester.getClass().getName() + " with value: " + tester);
						
						INHoopSerializableTableEntry value=(INHoopSerializableTableEntry) styleModel.getValueAt(arg0.getFirstRow(),1);
						INHoopStyleProperty entry=(INHoopStyleProperty) value.getEntry();
						
						if (entry!=null)
						{
							debug ("Entry: " + entry.toString());
							
							INHoopInspectable target=value.getComponent();
						
							entry.setTouched(true);
						
							/*
							if (controller!=null)
							{
								//debug ("Sending: " +target.toStringElementTouched());
								controller.sendInterfaceDescription (target.getName(),target.toStringElementTouched());
							}
							else
								debug ("Error: controller object is null!");
							*/	
						
							entry.setTouched(false);
						
							//untouch ();
						}
						else
							debug ("Error: style entity is not the right entry type");
					}
				}
			});			
						
			ArrayList<INHoopStyleProperty> styles=component.getStyleProperties();
			
			for (int j=0;j<styles.size();j++)
			{
				INHoopStyleProperty style=styles.get(j);
				
				debug ("Adding: " + style.getInstanceName() + " with value: " + style.getValue());
								
				INHoopSerializableTableEntry entry1=new INHoopSerializableTableEntry (style.getInstanceName());				
				INHoopSerializableTableEntry entry2=new INHoopSerializableTableEntry (style.getValue());
				entry2.setEntry(style);
				entry2.setComponent(getComponent());
				
				INHoopSerializableTableEntry[] styleData = {entry1,entry2};
				
				styleModel.addRow (styleData);
				
				//trace.printStack();				
			}
						
			styleTable.setModel(styleModel);
			
	        TableColumn colS = styleTable.getColumnModel().getColumn(1);
	        colS.setCellEditor(new INHoopSheetCellEditor());			
		}	
	}
	/**
	 * 
	 */
	private void untouch ()
	{
		debug ("untouch ()");
	
		for (int i=0;i<styleModel.getRowCount();i++)
		{
			Object tester=styleModel.getValueAt(i,1);

			if (tester instanceof INHoopSerializableTableEntry)
			{
				INHoopSerializableTableEntry value=(INHoopSerializableTableEntry) styleModel.getValueAt(i,1);
				INHoopSerializable entry=value.getEntry();
				entry.setTouched(false);
			}
			else
				debug ("Internal error: item in style table is not a INHoop entry");
		}
		
		for (int j=0;j<parameterModel.getRowCount();j++)
		{
			Object tester=parameterModel.getValueAt(j,1);

			if (tester instanceof INHoopSerializableTableEntry)
			{
				INHoopSerializableTableEntry value=(INHoopSerializableTableEntry) parameterModel.getValueAt(j,1);
				INHoopSerializable entry=value.getEntry();
				entry.setTouched(false);
			}
			else
				debug ("Internal error: item in parameter table is not a INHoop entry");
		}
		
		/*
		for (int i=0;i<styleModel.getRowCount();i++)
		{
			Object tester=styleModel.getValueAt(i,1);

			if (tester instanceof INHoopSerializableTableEntry)
			{
				INHoopSerializableTableEntry value=(INHoopSerializableTableEntry) styleModel.getValueAt(i,1);
				INHoopSerializable entry=value.getEntry();
				entry.setTouched(false);
			}
			else
				debug ("Internal error: item in style table is not a INHoop entry");
		}
		*/		
	}
	/**
	 * 
	 */	
	public void checkComponent ()
	{
		debug ("checkComponent ()");
		
		/*
		if (controller!=null)
		{
			if (componentShow.isSelected()==true)
			{
				controller.sendHighlightMsg("",s2v(component.getInstanceName()),s2v ("dummy"));
				component.setSelected(true);
			}	
			else
			{	
				controller.sendUnHighlightMsg("",s2v(component.getInstanceName()),s2v ("dummy"));
				component.setSelected(false);
			}	
			
			if (preview!=null)
				preview.repaint();
		}
		else
			debug ("Error: controller object is null");
		*/	
	}
}
