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

package edu.cmu.cs.in.hoop;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
//import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.mxgraph.layout.mxCompactTreeLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import edu.cmu.cs.in.base.INLink;
import edu.cmu.cs.in.search.INQueryOperator;
import edu.cmu.cs.in.controls.base.INEmbeddedJPanel;
import edu.cmu.cs.in.controls.base.INJInternalFrame;
//import edu.cmu.cs.in.controls.INVisualFeature;
import edu.cmu.cs.in.search.INTextSearch;
import edu.cmu.cs.in.stats.INPerformanceMetrics;
//import edu.cmu.cs.in.stats.INTrecEval;

/**
 * 
 */
public class INHoopExperimenter extends INEmbeddedJPanel implements ActionListener, ListSelectionListener 
{  
	private static final long serialVersionUID = 8387762921834350566L;
	
    private JFileChooser fc=null;	
	private JLabel filePath=null;
	private JLabel queryStats=null;	
	private mxGraph graph=null;	
	private JList searchList=null;
	
	public INHoopExperimenter ()  
    {
    	//super("Experimenter", true, true, true, true);

    	Box holder = new Box (BoxLayout.Y_AXIS);
    	
		//Border padding = BorderFactory.createEmptyBorder(2,0,0,0);
		//Border redborder=BorderFactory.createLineBorder(Color.red);    	
		Border blackborder=BorderFactory.createLineBorder(Color.black);
	    			
		Box inputBox = new Box (BoxLayout.X_AXIS);
		inputBox.setMinimumSize(new Dimension (50,25));
		inputBox.setPreferredSize(new Dimension (5000,25));
		inputBox.setMaximumSize(new Dimension (5000,25));		
		
		filePath=new JLabel ();
		filePath.setFont(new Font("Dialog", 1, 10));
		filePath.setMinimumSize(new Dimension (50,25));
		filePath.setPreferredSize(new Dimension (5000,25));
		filePath.setMaximumSize(new Dimension (5000,25));		
		
		JButton search=new JButton ();
		search.setDefaultCapable(true);
		search.setText("Load Queries");
		search.setFont(new Font("Dialog", 1, 10));
		search.setMinimumSize(new Dimension (75,25));
		search.setPreferredSize(new Dimension (75,25));
		search.setMaximumSize(new Dimension (75,25));
		search.addActionListener(this);
		
		JButton run=new JButton ();
		run.setDefaultCapable(true);
		run.setText("Run");
		run.setFont(new Font("Dialog", 1, 10));
		run.setMinimumSize(new Dimension (75,25));
		run.setPreferredSize(new Dimension (75,25));
		run.setMaximumSize(new Dimension (75,25));
		run.addActionListener(this);		
								
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
		mxGraphComponent graphComponent=new mxGraphComponent(graph);
		graphComponent.setEnabled(false);
		graphComponent.setMinimumSize(new Dimension (50,25));
		graphComponent.setPreferredSize(new Dimension (5000,5000));
		graphComponent.setMaximumSize(new Dimension (5000,5000));	
		
		searchList=new JList ();
		searchList.setFont(new Font("Dialog", 1, 10));
		searchList.setMinimumSize(new Dimension (50,25));
		searchList.setPreferredSize(new Dimension (150,5000));
		searchList.setMaximumSize(new Dimension (150,5000));
		searchList.addListSelectionListener(this);
		
		JSplitPane centerPane=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,graphComponent,searchList);
		centerPane.setResizeWeight(0.5);
		//centerPane.setDividerLocation(150);
		centerPane.setOneTouchExpandable(true);
		centerPane.setContinuousLayout(true);
		
		Box centerBox = new Box (BoxLayout.X_AXIS);
		centerBox.setMinimumSize(new Dimension (50,25));
		centerBox.setPreferredSize(new Dimension (5000,5000));
		centerBox.setMaximumSize(new Dimension (5000,5000));			
		
		centerBox.add(centerPane);
		
		inputBox.add (filePath);
		inputBox.add (search);
		inputBox.add (run);
		
		holder.add (inputBox);
		holder.add (topStatsBox);
		holder.add (centerBox);
    	
		setContentPane (holder);
		//setSize (425,300);
		//setLocation (75,75);
    }
	/**
	 *
	 */	
	@Override
	public void actionPerformed (ActionEvent event) 
	{
		debug ("actionPerformed ("+event.getActionCommand()+")");
		
		//String act=event.getActionCommand();
		JButton button = (JButton)event.getSource();
		
		//>---------------------------------------------------------
		
		if (button.getText().equals("Load Queries"))
		{
			fc = new JFileChooser();
			//fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	        int returnVal=fc.showOpenDialog (this);

	        if (returnVal==JFileChooser.APPROVE_OPTION) 
	        {
	        	File file = fc.getSelectedFile();
	        		
	        	debug ("Loading: " + file.getPath() + " ...");
	        		
	        	INLink.queries=INLink.fManager.loadLines(file.getPath());
	        		
	        	queryStats.setText("Loaded " + INLink.queries.size() + " runnable queries");
	        		
	        	filePath.setText(file.getPath());
	        }
		}
		
		//>---------------------------------------------------------
		
		if (button.getText().equals("Run"))
		{
			if (INLink.posFiles==null)
			{
				JOptionPane.showMessageDialog(null, "Warning, no vocabulary loaded!");
				return;
			}
			
			if (INLink.posFiles.size()==0)
			{
				JOptionPane.showMessageDialog(null, "Warning, no vocabulary loaded!");
				return;
			}
			
			if (INLink.queries==null)
			{
				queryStats.setText ("Please load an experiment file first");
				JOptionPane.showMessageDialog(null, "Please load an experiment file first");
				return;
			}
			
			if (INLink.queries.size()==0)
			{
				queryStats.setText ("Experiment file has 0 entries");
				JOptionPane.showMessageDialog(null, "Experiment file has 0 entries");
				return;
			}			
			
			debug ("Running experiment ...");
			
			INPerformanceMetrics metrics=new INPerformanceMetrics ();
			metrics.setMarker ("Query Experiment");
			INLink.metrics.add(metrics);
			
			DefaultListModel mdl=new DefaultListModel ();
			
			searchList.setModel (mdl);
									
			for (int i=0;i<INLink.queries.size();i++)
			{
				String aQuery=INLink.queries.get(i);
				
				String [] preProcess=aQuery.split("\\:");
				
				String theQuery=aQuery;
				
				if (preProcess.length>1)
					theQuery=preProcess [1];
					
				debug ("Running query: " + theQuery);
				
				INTextSearch aSearch=new INTextSearch ();
				aSearch.setInstanceName(preProcess [0]);
				
				aSearch.search(theQuery,20);
				
				if (i==0)
				{
					visualize (null,aSearch.getRootQueryOperator());
				}
				
				INLink.searchHistory.add (aSearch);
				
				StringBuffer formatter=new StringBuffer ();
				
				INQueryOperator query=aSearch.getRootQueryOperator ();
				
				float tt=(float) (query.getTimeTaken()/1000.0);
				//formatter.append("["+i+"] = "+ String.format("%.2f",tt)+" seconds, Mem: " + query.getMemUsed()/1024+"K for: " +"\""+theQuery+"\"");
				formatter.append("["+String.format ("%02d",i)+"] = "+ String.format("%.2f",tt)+"s,: " +"\""+theQuery+"\"");
				
				mdl.add (i,formatter.toString());
				searchList.invalidate();
				searchList.repaint();
				
				// Clean it all up!
				
				query.reset();
				
				Runtime r = Runtime.getRuntime();
				r.gc();
			}
								
			long timeTaken=metrics.getMarkerRaw ();
			queryStats.setText(metrics.getMetrics(timeTaken));
			
			INLink.experimentNr++;			
		}
		
		//>---------------------------------------------------------
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
		debug ("visualizeQuery ()");
												
		Object parent=graph.getDefaultParent();
		
		Object root=aParent;
		
		if (aParent==null)
			root=graph.insertVertex (parent, null,aQuery.getInstanceName(),20,20,80,30);
				
		ArrayList<INQueryOperator> operators=aQuery.getOperators();
		
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
	/**
	 *
	 */	
	@Override
	public void valueChanged(ListSelectionEvent event) 
	{
		debug ("valueChanged ()");
		
		int selectedRow=event.getFirstIndex();
		
		if (selectedRow!=-1)
		{
			INTextSearch aSearch=INLink.searchHistory.get(selectedRow);
						
			if (aSearch!=null)
			{
				visualize (null,aSearch.getRootQueryOperator());
				
				INLink.dataSet=aSearch.getDataSet();
				
				INLink.updateAllWindows ();
			}			
		}
	}	
}
