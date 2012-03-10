/** 
 * Author: Martin van Velsen <vvelsen@cs.cmu.edu>
 * 
 * This source set uses code written for other various graduate 
 * courses and is part of a larger research effort in the field of
 * interactive narrative (IN). Courses that have contribute
 * to this source base are:
 * 
 * 	05-834 Applied Machine Learning
 *  11-719 Computational Models of Discourse Analysis
 *  11-741 Information Retrieval
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import com.mxgraph.layout.mxCompactTreeLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import edu.cmu.cs.in.search.INQueryOperator;
import edu.cmu.cs.in.base.INLink;
import edu.cmu.cs.in.controls.base.INJInternalFrame;
import edu.cmu.cs.in.search.INTextSearch;

/**
 * 
 */
public class INHoopSearch extends INJInternalFrame implements ActionListener
{  
	private static final long serialVersionUID = 8387762921834350566L;

	private JTextField inputField=null;
	private JLabel queryStats=null;	
	private mxGraph graph=null;
    //private int queryNr=1;    
    //private INTextSearch aSearch=null;
    
	/**
	 *
	 */		
	public INHoopSearch() 
    {
    	super("Search", true, true, true, true);
    	
		setClassName ("INHoopSearch");
		debug ("INHoopSearch ()");    	

    	Box holder = new Box (BoxLayout.Y_AXIS);
    	    	
		//Border padding = BorderFactory.createEmptyBorder(2,0,0,0);
		Border blackborder=BorderFactory.createLineBorder(Color.black);
		//Border redborder=BorderFactory.createLineBorder(Color.red);	    	
		
		Box inputBox = new Box (BoxLayout.X_AXIS);
		inputBox.setMinimumSize(new Dimension (50,25));
		inputBox.setPreferredSize(new Dimension (5000,25));
		inputBox.setMaximumSize(new Dimension (5000,25));		
		
		inputField=new JTextField ();
		inputField.setFont(new Font("Dialog", 1, 10));
		inputField.setMinimumSize(new Dimension (50,25));
		inputField.setPreferredSize(new Dimension (5000,25));
		inputField.setMaximumSize(new Dimension (5000,25));		
		
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
		queryStats.setMinimumSize(new Dimension (5000,25));
		queryStats.setPreferredSize(new Dimension (5000,25));
		queryStats.setMaximumSize(new Dimension (5000,25));
		queryStats.setOpaque(true);
		queryStats.setForeground(Color.black);
		queryStats.setBackground(new Color (252,246,194));
		
		Box topStatsBox = new Box (BoxLayout.X_AXIS);
		topStatsBox.setMinimumSize(new Dimension (50,25));
		topStatsBox.setPreferredSize(new Dimension (5000,25));
		topStatsBox.setMaximumSize(new Dimension (5000,25));
		
		topStatsBox.add (queryStats);

		graph=new mxGraph();		
		mxGraphComponent graphComponent = new mxGraphComponent(graph);
		graphComponent.setEnabled(false);
		
		inputBox.add (inputField);
		inputBox.add (search);
		
		holder.add (inputBox);
		holder.add (topStatsBox);
		holder.add (graphComponent);
    	
		setContentPane (holder);
		setSize (425,300);
		setLocation (75,75);
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
		
		if (button.getText().equals("Search"))
		{
			debug ("Command " + act + " on allButton");
			if (inputField.getText().isEmpty()==true)
			{
				debug ("Please enter a query");
				return;
			}
			
			INTextSearch aSearch=new INTextSearch ();
			aSearch.search (inputField.getText().toLowerCase());
			
			StringBuffer formatter=new StringBuffer ();
			
			INQueryOperator query=aSearch.getRootQueryOperator ();
									
			formatter.append("Time taken: "+ query.getTimeTaken()+", Memory used: " + query.getMemUsed()+" for: " +"\""+inputField.getText().toLowerCase()+"\"");
			
			queryStats.setText(formatter.toString());
			
			// Show document list ...
			
			INLink.updateAllWindows ();
			
			// Then show some visuals ...
			
			visualize (null,aSearch.getRootQueryOperator());
			
			// Once we have all the information we need, we should toss the intermediate
			// data otherwise we can't hold that many query results.
			query.reset(); 
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
	private void visualizeQuery (Object aParent,INQueryOperator aQuery)
	{
		debug ("visualizeQuery ("+aQuery.getInstanceName()+")");
												
		Object parent=graph.getDefaultParent();
		
		Object root=aParent;
		
		if (aParent==null)
			root=graph.insertVertex (parent, null,aQuery.getInstanceName(),20,20,80,30);
				
		ArrayList<INQueryOperator> operators=aQuery.getOperators();
		
		debug ("Visualizing " + operators.size () + " sub queries ...");
		
		for (int i=0;i<operators.size();i++)
		{
			INQueryOperator op=operators.get(i);
			
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
	private void visualize (Object aParent,INQueryOperator aQuery)
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
}
