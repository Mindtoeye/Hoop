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

/*
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.tree.*;
*/

package edu.cmu.cs.in.hoop;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.mxgraph.layout.mxCompactTreeLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import edu.cmu.cs.in.search.HoopQueryOperator;
import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.controls.base.HoopEmbeddedJPanel;
//import edu.cmu.cs.in.controls.base.HoopJInternalFrame;
import edu.cmu.cs.in.search.HoopTextSearch;

/**
 * 
 */
public class HoopSearch extends HoopEmbeddedJPanel implements ActionListener, KeyListener, ItemListener
{  
	private static final long serialVersionUID = 8387762921834350566L;

	private JTextField inputField=null;
	private JLabel queryStats=null;	
	private mxGraph graph=null;
	private JComboBox<String> rankType=null;
	private JComboBox<String> queryLanguage=null;
	private JTextField topDocsInput=null;
    
	private JLabel l2=null;
	private JLabel k1Label=null;	
	private JTextField k1Input=null;	
	private JLabel bLabel=null;	
	private JTextField bInput=null;	
	private JLabel k3Label=null;	
	private JTextField k3Input=null;
	
	/**
	 *
	 */		
	public HoopSearch() 
    {
    	//super("Search", true, true, true, true);
    	
		setClassName ("HoopSearch");
		debug ("HoopSearch ()");    	

    	Box holder = new Box (BoxLayout.Y_AXIS);
    	    	
		Border blackborder=BorderFactory.createLineBorder(Color.black);
			
		Box inputBox = new Box (BoxLayout.X_AXIS);
		inputBox.setMinimumSize(new Dimension (50,25));
		//inputBox.setPreferredSize(new Dimension (5000,25));
		inputBox.setMaximumSize(new Dimension (5000,25));		
		
		inputField=new JTextField ();
		inputField.setFont(new Font("Dialog", 1, 10));
		inputField.setMinimumSize(new Dimension (50,25));
		//inputField.setPreferredSize(new Dimension (5000,25));
		inputField.setMaximumSize(new Dimension (5000,25));		
		inputField.addKeyListener(this);
		
		JButton search=new JButton ();
		search.setDefaultCapable(true);
		search.setText("Search");
		search.setFont(new Font("Dialog", 1, 10));
		search.setMinimumSize(new Dimension (75,25));
		search.setPreferredSize(new Dimension (75,25));
		search.setMaximumSize(new Dimension (75,25));
		search.addActionListener(this);
								
		queryStats=new JLabel ();
		queryStats.setBorder(blackborder);
		queryStats.setFont(new Font("Dialog", 1, 10));
		queryStats.setMinimumSize(new Dimension (100,25));
		//queryStats.setPreferredSize(new Dimension (5000,25));
		queryStats.setMaximumSize(new Dimension (5000,25));
		queryStats.setOpaque(true);
		queryStats.setForeground(Color.black);
		queryStats.setBackground(new Color (252,246,194));
		
		Box topStatsBox = new Box (BoxLayout.X_AXIS);
		//topStatsBox.setMinimumSize(new Dimension (50,25));
		topStatsBox.setPreferredSize(new Dimension (200,25));
		//topStatsBox.setMaximumSize(new Dimension (200,25));
		
		topStatsBox.add (queryStats);
		
		// New configuration box to set search parameters
		
		JPanel confPanel=new JPanel ();
		confPanel.setLayout(null);
		confPanel.setFont(new Font("Dialog", 1, 10));
		confPanel.setBorder(BorderFactory.createTitledBorder("Search Module"));
		confPanel.setMinimumSize(new Dimension (100,100));
		confPanel.setPreferredSize(new Dimension (100,100));
		confPanel.setMaximumSize(new Dimension (5000,100));		
		
		Insets insets = confPanel.getInsets();
		
		//>-------------------------------------------------
		
		String[] typeStrings = {"None", "Lucene","Vector", "Binary Independence Model", "Okapi (BM25F)"};
						
		rankType=new JComboBox (typeStrings);
		rankType.setFont(new Font("Dialog", 1, 10));
		rankType.addItemListener(this);
		
		JLabel l1=new JLabel ();
		l1.setFont(new Font("Dialog", 1, 10));
		l1.setText("Show only top ");
		
		topDocsInput=new JTextField ();
		topDocsInput.setText("20");
		topDocsInput.setFont(new Font("Dialog", 1, 10));
		
		l2=new JLabel ();
		l2.setFont(new Font("Dialog", 1, 10));
		l2.setText(" documents");
		
		JLabel l3=new JLabel ();
		l3.setFont(new Font("Dialog", 1, 10));
		l3.setText("Query Language");
		
		String[] qTypeStrings = {"Basic", "Indri"};
		
		queryLanguage=new JComboBox (qTypeStrings);
		queryLanguage.setFont(new Font("Dialog", 1, 10));
		queryLanguage.addItemListener(this);
		
		//>-------------------------------------------------
		
		JSeparator verLine=new JSeparator ();
		verLine.setOrientation(SwingConstants.VERTICAL);		

		k1Label=new JLabel ();
		k1Label.setText("k1: ");
		k1Label.setFont(new Font("Dialog", 1, 10));
		
		k1Input=new JTextField ();
		k1Input.setText("1.2");
		k1Input.setFont(new Font("Dialog", 1, 10));
		
		bLabel=new JLabel ();
		bLabel.setText("b: ");
		bLabel.setFont(new Font("Dialog", 1, 10));
		
		bInput=new JTextField ();
		bInput.setText("0.75");
		bInput.setFont(new Font("Dialog", 1, 10));		
		
		k3Label=new JLabel ();
		k3Label.setText("k3: ");
		k3Label.setFont(new Font("Dialog", 1, 10));
		
		k3Input=new JTextField ();
		k3Input.setText("500");
		k3Input.setFont(new Font("Dialog", 1, 10));
		
		//>--------------------------------------------------
		
		confPanel.add(rankType);
		
		confPanel.add(l1);
		confPanel.add(topDocsInput);
		confPanel.add(l2);
		
		confPanel.add(l3);
		confPanel.add(queryLanguage);
		
		confPanel.add(l1);
		confPanel.add(topDocsInput);
		confPanel.add(l2);
		
		confPanel.add(verLine);
		
		confPanel.add(k1Label);
		confPanel.add(bLabel);
		confPanel.add(k3Label);
		
		confPanel.add(k1Input);
		confPanel.add(bInput);
		confPanel.add(k3Input);		
		
		rankType.setBounds (2+insets.left,2+insets.top,200,20);
		
		l1.setBounds (2+insets.left,2+insets.top+30,75,20);
		topDocsInput.setBounds (2+insets.left+80,2+insets.top+30,30,20);
		l2.setBounds (2+insets.left+110,2+insets.top+30,200,20);
		
		l3.setBounds (2+insets.left,2+insets.top+30+25,100,20);
		queryLanguage.setBounds (2+insets.left+100,2+insets.top+30+25,100,20);
		
		verLine.setBounds (208+insets.left,insets.top,10,70);
		
		k1Label.setBounds (208+insets.left+6,insets.top,30,20);		
		bLabel.setBounds (208+insets.left+6,insets.top+25,30,20);
		k3Label.setBounds (208+insets.left+6,insets.top+50,30,20);
		
		k1Input.setBounds (208+insets.left+6+35,insets.top,50,20);
		bInput.setBounds (208+insets.left+6+35,insets.top+25,50,20);
		k3Input.setBounds (208+insets.left+6+35,insets.top+50,50,20);
				
		// Business as usual ...		

		graph=new mxGraph();		
		mxGraphComponent graphComponent = new mxGraphComponent(graph);
		graphComponent.setEnabled(false);
		
		inputBox.add (inputField);
		inputBox.add (search);
		
		holder.add (inputBox);
		holder.add (topStatsBox);
		holder.add (confPanel);
		holder.add (graphComponent);
    	
		setContentPane (holder);
		//setSize (425,300);
		//setLocation (75,75);
		
		l2.setEnabled(false);
		k1Label.setEnabled(false);
		k1Input.setEnabled(false);
		bLabel.setEnabled(false);
		bInput.setEnabled(false);
		k3Label.setEnabled(false);
		k3Input.setEnabled(false);		
    }
	/**
	 *
	 */	
	@Override
	public void actionPerformed (ActionEvent event) 
	{
		debug ("actionPerformed ("+event.getActionCommand()+")");
		
		//String act=event.getActionCommand();
		JButton button = (JButton) event.getSource();
		
		if (button.getText().equals("Search"))
		{
			search ();
		}			
	}  
	/**
	 * Sets a new root using createRoot.
	 */
	public void clear()
	{	
		debug ("clear ()");
		graph.removeCells(graph.getChildVertices(graph.getDefaultParent()));
	}	
	/**
	 *
	 */
	private void visualizeQuery (Object aParent,HoopQueryOperator aQuery)
	{
		debug ("visualizeQuery ()");
												
		Object parent=graph.getDefaultParent();
		
		Object root=aParent;
		
		if (aParent==null)
			root=graph.insertVertex (parent, null,aQuery.getInstanceName(),20,20,80,30);
				
		ArrayList<HoopQueryOperator> operators=aQuery.getOperators();
		
		for (int i=0;i<operators.size();i++)
		{
			HoopQueryOperator op=operators.get(i);
			
			Object childOperation = graph.insertVertex (parent, null,op.getInstanceName(),240, 150,80,30);
			graph.insertEdge (parent, null,"",root,childOperation);
			
			if (op.getOperators().size()>0)
			{
				visualizeQuery (childOperation,op);
			}
		}						
	}
	/**
	 *
	 */
	private void visualize (Object aParent,HoopQueryOperator aQuery)
	{
		debug ("visualize ()");
		
		clear();
								
		graph.getModel().beginUpdate();
				
		visualizeQuery (aParent,aQuery);
		
		graph.getModel().endUpdate();
		
		mxCompactTreeLayout vertTree=new mxCompactTreeLayout(graph, false);
		vertTree.setLevelDistance(10);
		vertTree.setNodeDistance(20);
		vertTree.execute(graph.getDefaultParent());
	}
	/**
	 *
	 */	
	@Override
	public void keyPressed(KeyEvent event) 
	{
	    int key = event.getKeyCode();  
        if (key == KeyEvent.VK_ENTER)
        {
        	search ();
        }  		
	}
	/**
	 *
	 */	
	@Override
	public void keyReleased(KeyEvent arg0) 
	{
		// TODO Auto-generated method stub
	}
	/**
	 *
	 */	
	@Override
	public void keyTyped(KeyEvent arg0) 
	{
		// TODO Auto-generated method stub	
	}	
	/**
	 *
	 */		
	private void search ()
	{
		debug ("search ()");
				
		if (inputField.getText().isEmpty()==true)
		{
			debug ("Please enter a query");
			JOptionPane.showMessageDialog(null, "Please enter a query");
			return;
		}
		
		HoopTextSearch aSearch=new HoopTextSearch ();
		aSearch.search (inputField.getText().toLowerCase(),Integer.parseInt(topDocsInput.getText()));
		
		HoopQueryOperator query=aSearch.getRootQueryOperator ();
		
		if (rankType.getSelectedItem().equals("Lucene")==true)
		{
			
		}
		else
		{
			if (HoopLink.posFiles==null)
			{
				JOptionPane.showMessageDialog(null, "Warning, no vocabulary loaded!");
				return;
			}
		
			if (HoopLink.posFiles.size()==0)
			{
				JOptionPane.showMessageDialog(null, "Warning, no vocabulary loaded!");
				return;
			}
				
			if (topDocsInput.getText().isEmpty()==true)
			{
				debug ("Please enter how many documents should be retrieved");
				JOptionPane.showMessageDialog (null,"Please enter how many documents should be retrieved");
				return;			
			}
				
			StringBuffer formatter=new StringBuffer ();
				
			formatter.append("Time taken: "+ String.format(".2f seconds",(float) (query.getTimeTaken()/1000))+", for: " +"\""+inputField.getText().toLowerCase()+"\"");
		
			queryStats.setText(formatter.toString());
		}	
		
		// Show document list ...
		
		HoopLink.updateAllWindows ();
		
		// Then show some visuals ...
		
		visualize (null,aSearch.getRootQueryOperator());		
		
		// Once we have all the information we need, we should toss the intermediate
		// data otherwise we can't hold that many query results.
		query.reset(); 
	}
	/**
	 *
	 */	
	@Override
	public void itemStateChanged(ItemEvent e) 
	{
		if(e.getSource()==rankType) 
		{			
			if(rankType.getSelectedItem().equals("None"))
			{
				l2.setEnabled(false);
				k1Label.setEnabled(false);
				k1Input.setEnabled(false);
				bLabel.setEnabled(false);
				bInput.setEnabled(false);
				k3Label.setEnabled(false);
				k3Input.setEnabled(false);
			}
			
			if(rankType.getSelectedItem().equals("Lucene"))
			{
				l2.setEnabled(false);
				k1Label.setEnabled(false);
				k1Input.setEnabled(false);
				bLabel.setEnabled(false);
				bInput.setEnabled(false);
				k3Label.setEnabled(false);
				k3Input.setEnabled(false);
			}			
			
			if(rankType.getSelectedItem().equals("Vector"))
			{
				l2.setEnabled(false);
				k1Label.setEnabled(false);
				k1Input.setEnabled(false);
				bLabel.setEnabled(false);
				bInput.setEnabled(false);
				k3Label.setEnabled(false);
				k3Input.setEnabled(false);
			}
			
			if(rankType.getSelectedItem().equals("Binary Independence Model"))
			{
				l2.setEnabled(false);
				k1Label.setEnabled(false);
				k1Input.setEnabled(false);
				bLabel.setEnabled(false);
				bInput.setEnabled(false);
				k3Label.setEnabled(false);
				k3Input.setEnabled(false);
			}
			
			if(rankType.getSelectedItem().equals("Okapi (BM25F)"))
			{
				l2.setEnabled(true);
				k1Label.setEnabled(true);
				k1Input.setEnabled(true);
				bLabel.setEnabled(true);
				bInput.setEnabled(true);
				k3Label.setEnabled(true);
				k3Input.setEnabled(true);
			}		
		}	
	}	   		
}
