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

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import edu.cmu.cs.in.base.INHoopLink;
import edu.cmu.cs.in.controls.base.INJPanel;
import edu.cmu.cs.in.hoop.properties.INHoopPropertyTable;

/**
 * 
 */
public class INHoopInspectablePanel extends INJPanel implements ActionListener, ItemListener
{	
	private static final long serialVersionUID = 1L;
	
	private INHoopInspectable component=null;
	
	private FlatButton foldButton=null;
	//private JLabel componentType=null;
	
	private JPanel preview=null;
	
	Icon close=null;
	Icon open=null;
	
	private Boolean folded=false;
	
    private int fixedWidth=200;
    private int fixedHeight=300;	
	    
	JPanel parameterPanel=null;
		
	INHoopPropertyTable parameterTable=null;
		
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
	public INHoopInspectablePanel () 
    {
    	setClassName ("INHoopInspectablePanel");
    	debug ("INHoopInspectablePanel ()");   
    	
    	create ();
    }
	/**
	 * 
	 */		
	public INHoopInspectablePanel (String aTitle)
	{		
    	setClassName ("INHoopInspectablePanel");
    	debug ("INHoopInspectablePanel ()");   

		panelTitle=aTitle;
		
    	create ();		
	}
	/**
	 * 
	 */
	private void create ()
	{
		debug ("create ()");
		
    	close=INHoopLink.imageIcons [82];
    	open=INHoopLink.imageIcons [83];
		
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
                                               	    
        parameterTable=new INHoopPropertyTable ();
        parameterTable.setBorder(BorderFactory.createLineBorder(Color.black));
        //parameterTable.setMaximumSize(new Dimension (5000,5000));
                
        JScrollPane parameterScrollList=new JScrollPane (parameterTable);
        parameterScrollList.setMinimumSize(new Dimension (10,10));
        parameterScrollList.setPreferredSize(new Dimension (100,100));
        //parameterScrollList.setMaximumSize(new Dimension (5000,5000));	    
        parameterScrollList.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        BorderLayout configBox=new BorderLayout();
        parameterPanel.setLayout(configBox);
                                       
        parameterPanel.add(parameterScrollList,BorderLayout.CENTER);        
                
        panelBox.add (parameterPanel);	
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
	public void setComponent(INHoopInspectable component) 
	{
		this.component=component;
		
		//this.component.setChecker (componentShow);
		
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
