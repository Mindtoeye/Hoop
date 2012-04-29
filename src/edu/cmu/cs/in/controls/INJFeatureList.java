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

package edu.cmu.cs.in.controls;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.Border;

import edu.cmu.cs.in.base.INBase;
import edu.cmu.cs.in.controls.INVisualFeatureVisualizer;

public class INJFeatureList extends JPanel implements ActionListener, MouseListener
{
	private static final long serialVersionUID = 1L;
	
	private JList coreList;
	private JLabel coreLabel;
	private JTextField coreFilter;
	
	private DefaultListModel originalModel;
	private DefaultListModel filteredModel;
	
    private JButton allButton=null;
    private JButton noneButton=null;
    private JButton inverseButton=null;
    private JButton selectedButton=null;	
    
    private JPanel colorPicker=null;
    
    private Color color = Color.BLACK;
    
    private ArrayList <INVisualFeatureVisualizer> dependends=null;
    private INVisualClass assignedClass=null;
    
	/**
	 *
	 */
	private void debug (String aMessage)
	{
		INBase.debug ("INJFeatureList",aMessage);	
	}	
	/**
	 *
	 */	
	public INJFeatureList ()
	{
		super();
		
		debug ("INJFeatureList ()");		
				
		init (true);
	}
	/**
	 *
	 */	
	public INJFeatureList (Boolean showSearch)
	{
		super();
		
		debug ("INJFeatureList ()");		
				
		init (showSearch);
	}	
	/**
	 *
	 */	
	private void init (Boolean showSearch)
	{
		dependends=new ArrayList<INVisualFeatureVisualizer> ();
		
		Border border=BorderFactory.createLineBorder(Color.black);
		Border bevel=BorderFactory.createLoweredBevelBorder();
		
		//setBackground(INJColorTools.parse("#ffffff"));
		
		this.setLayout(new BoxLayout (this,BoxLayout.Y_AXIS));
				
		coreList = new JList ();
		coreList.setCellRenderer (new INJCheckListItem ());
	    JScrollPane posScrollList = new JScrollPane (coreList);
	    
	    coreFilter=new JTextField ();
	    coreFilter.setMaximumSize(new Dimension (2000,20));
	    
	    coreLabel=new JLabel ("");
	    coreLabel.setBorder(bevel);
	    coreLabel.setHorizontalAlignment (JLabel.LEFT);
	    coreLabel.setFont(new Font("Dialog", 1, 10));
	    coreLabel.setBackground(INJColorTools.parse("#ffffff"));
	    coreLabel.setMinimumSize(new Dimension (50,20));
	    coreLabel.setMaximumSize(new Dimension (2000,20));
	        
	    colorPicker=new JPanel ();
	    colorPicker.setBackground(color);
	    colorPicker.setBorder(border);
	    colorPicker.setMinimumSize(new Dimension (20,20));
	    colorPicker.setPreferredSize(new Dimension (20,20));
	    colorPicker.setMaximumSize(new Dimension (20,20));
	    colorPicker.addMouseListener(this);
	    	    
	    Box labelBox = new Box (BoxLayout.X_AXIS);
	    labelBox.add(coreLabel);
	    labelBox.add(colorPicker);
	    
	    Box buttonBox = new Box (BoxLayout.X_AXIS);
	    allButton=new JButton ();
	    allButton.setFont(new Font("Dialog", 1, 8));
	    allButton.setPreferredSize(new Dimension (30,20));
	    allButton.setMaximumSize(new Dimension (2000,20));
	    allButton.setText("All");	    	    
	    allButton.addActionListener(this);
	    buttonBox.add (allButton);
	    noneButton=new JButton ();
	    noneButton.setFont(new Font("Dialog", 1, 8));
	    noneButton.setPreferredSize(new Dimension (30,20));
	    noneButton.setMaximumSize(new Dimension (2000,20));
	    noneButton.setText("None");
	    noneButton.addActionListener(this);
	    buttonBox.add (noneButton);
	    inverseButton=new JButton ();
	    inverseButton.setFont(new Font("Dialog", 1, 8));
	    inverseButton.setPreferredSize(new Dimension (30,20));
	    inverseButton.setMaximumSize(new Dimension (2000,20));
	    inverseButton.setText("Inverse");
	    inverseButton.addActionListener(this);
	    buttonBox.add (inverseButton);
	    selectedButton=new JButton ();
	    selectedButton.setFont(new Font("Dialog", 1, 8));
	    selectedButton.setPreferredSize(new Dimension (30,20));
	    selectedButton.setMaximumSize(new Dimension (2000,20));
	    selectedButton.setText("Selected");
	    selectedButton.addActionListener(this);
	    selectedButton.setEnabled(false);
	    buttonBox.add (selectedButton);
	    
	    if (showSearch==true)
	    {
	    	this.add (coreFilter);
	    }
	    
	    this.add (labelBox);	    	    
	    this.add (posScrollList);	    
	    this.add (buttonBox);
	    	    
	    //>---------------------------------------------------
	    
	    coreFilter.addKeyListener (new KeyListener() 
	    {	    	
	        public void keyPressed(KeyEvent keyEvent) { }
	        	        
	        public void keyReleased(KeyEvent keyEvent) 
	        {	        	
	        	//debug ("Filtering on: " + coreFilter.getText());
	        	filterModel ();
	        }
	        
	        public void keyTyped(KeyEvent keyEvent) { }	        
	    });
	    
	    //>---------------------------------------------------	    
	    
	    coreList.addMouseListener (new MouseAdapter()
	    {
	    	public void mouseClicked (MouseEvent event)
	    	{
	    		JList list=(JList) event.getSource();
 
	    		// Get index of item clicked
	    
	    		int index=list.locationToIndex(event.getPoint());
	    		INVisualFeature item=(INVisualFeature) coreList.getModel().getElementAt(index);
	     
	    		// Toggle selected state
	    
	    		item.setSelected (!item.isSelected());
	    
	    		// Repaint cell
	    			    		
	    		list.repaint (list.getCellBounds(index, index));
	    		updateDependends ();
	    	}
	    });  	    	   
	    	    	    	   	    
	    //>---------------------------------------------------	    		
	}
	/**
	 *
	 */
	public void addDependend (INVisualFeatureVisualizer aDep)
	{
		debug ("addDependend ()");
		dependends.add (aDep);
	}
	/**
	 *
	 */
	public void updateDependends ()
	{
		debug ("updateDependends ()");
					
		for (int i=0;i<dependends.size();i++)
		{
			INVisualFeatureVisualizer dep=dependends.get(i);
			dep.updateVisualFeatures ();
		}
	}
	/**
	 *
	 */
	public void setLabel (String aLabel)
	{
		debug ("setLabel ()");
		coreLabel.setText(aLabel);
	}
	/**
	 *
	 */	
	public void modelFromArray (String [] list)
	{
		debug ("modelFromArray ()");
		
		DefaultListModel mdl=new DefaultListModel ();
		
		for (int i=0;i<list.length;i++)
		{
			INVisualFeature item=new INVisualFeature ();
			item.setInstanceName(list [i]);
			item.setText(list [i]);
			mdl.addElement(item);
		}
		
		setModel (mdl);
	}		
	/**
	 *
	 */	
	public void modelFromArrayList (ArrayList <String> list)
	{
		debug ("modelFromArrayList ()");
		
		DefaultListModel mdl=new DefaultListModel ();
		
		for (int i=0;i<list.size();i++)
		{
			String entry=list.get(i);
			INVisualFeature item=new INVisualFeature ();
			item.setInstanceName(entry);
			item.setText(entry);
			mdl.addElement(item);
		}
		
		setModel (mdl);
	}	
	/**
	 *
	 */	
	public void setModel(DefaultListModel model)
	{
		debug ("setModel ()");
		
		originalModel=model;
		filteredModel=model;
		
		setFilteredModel (filteredModel);
	}
	/**
	 *
	 */	
	public void assignClass (INVisualClass aClass)
	{
		debug ("assignClass ()");
		assignedClass=aClass;
	}
	/**
	 *
	 */	
	private void setFilteredModel (DefaultListModel model)
	{
		debug ("setFilteredModel ()");
				
		coreList.setModel (filteredModel);
	}	
	/**
	 *
	 */
	private void filterModel ()
	{
		debug ("filterModel ()");
		
		if (coreFilter.getText().isEmpty()==true)
			filteredModel=originalModel;
		else
		{
			filteredModel=new DefaultListModel();
			
        	for (int i=0;i<originalModel.getSize();i++) 
        	{
        	    INVisualFeature item=(INVisualFeature) originalModel.getElementAt (i);
        	    if (item.text.contains(coreFilter.getText ())==true)
        	    {
        	    	// We should include this
        	    	filteredModel.addElement (item);
        	    }
        	}			
		}
		
		setFilteredModel (filteredModel);
	}
	/**
	 *
	 */	
	@Override
	public void actionPerformed (ActionEvent event) 
	{
		debug ("actionPerformed ("+event.getActionCommand()+")");
		
		String act=event.getActionCommand();
		JButton button = (JButton)event.getSource();
		
		if (button.getText()=="All")
		{
			debug ("Command " + act + " on allButton");
			selectAll ();
		}
		
		if (button.getText().equals("None")==true)
		{
			debug ("Command " + act + " on noneButton");
			selectNone ();
		}
		
		if (button.getText().equals ("Inverse")==true)
		{
			debug ("Command " + act + " on inverseButton");
			selectInverse ();
		}
		
		if (button.getText().equals("Selected")==true)
		{
			debug ("Command " + act + " on selectedButton");
			selectSelected ();
		}		
	}
	/**
	 *
	 */	
	public void selectAll ()
	{
		debug ("selectAll ()");
		
    	for (int i=0;i<filteredModel.getSize();i++) 
    	{
    	    INVisualFeature item=(INVisualFeature) filteredModel.getElementAt (i);
    	    item.setSelected(true);
    	}					
    	    	
    	updateDependends ();
    	coreList.repaint();
	}
	/**
	 *
	 */	
	public void selectNone ()
	{
		debug ("selectNone ()");
		
    	for (int i=0;i<filteredModel.getSize();i++) 
    	{
    	    INVisualFeature item=(INVisualFeature) filteredModel.getElementAt (i);
    	    item.setSelected(false);
    	}		
    	
    	updateDependends ();
    	coreList.repaint();
	}
	/**
	 *
	 */	
	public void selectInverse ()
	{
		debug ("selectInverse ()");
	
    	for (int i=0;i<filteredModel.getSize();i++) 
    	{
    	    INVisualFeature item=(INVisualFeature) filteredModel.getElementAt (i);
    	    if (item.isSelected()==true)
    	    	item.setSelected(false);
    	    else
    	    	item.setSelected(true);
    	}		
    	
    	updateDependends ();
    	coreList.repaint();
	}
	/**
	 *
	 */	
	public void selectSelected ()
	{
		debug ("selectSelected ()");
		
		updateDependends ();
		coreList.repaint();
	}	
	/**
	 *
	 */	
	public int getSelectedIndex()
	{
		return (coreList.getSelectedIndex());
	}
	/**
	 *
	 */		
	@Override
	public void mouseClicked(MouseEvent e) 
	{
		debug ("mouseReleased ("+e.getX()+","+e.getY()+")");		
	}
	/**
	 *
	 */	
	@Override
	public void mousePressed(MouseEvent e) 
	{
		debug ("mouseReleased ("+e.getX()+","+e.getY()+")");
	}
	/**
	 *
	 */	
	@Override
	public void mouseReleased(MouseEvent e) 
	{
		debug ("mouseReleased ("+e.getX()+","+e.getY()+")");
				
        color=JColorChooser.showDialog (this,"Choose a color",color);

        if (color==null)
            color=Color.lightGray;
        
        colorPicker.setBackground(color);

        if (assignedClass!=null)
        	assignedClass.setColor(color);
        
        updateDependends ();
	}
	/**
	 *
	 */	
	@Override
	public void mouseEntered(MouseEvent e) 
	{
		// Move along, nothing to see here		
	}
	/**
	 *
	 */	
	@Override
	public void mouseExited(MouseEvent e) 
	{
		// Move along, nothing to see here
	}	
}
