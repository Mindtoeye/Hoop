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

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.base.HoopStringTools;
import edu.cmu.cs.in.search.HoopQueryOperator;
import edu.cmu.cs.in.controls.base.HoopEmbeddedJPanel;
//import edu.cmu.cs.in.controls.base.HoopJInternalFrame;
//import edu.cmu.cs.in.controls.HoopVisualFeature;
import edu.cmu.cs.in.search.HoopTextSearch;
import edu.cmu.cs.in.stats.HoopPerformanceMeasure;
//import edu.cmu.cs.in.stats.HoopTrecEval;

/**
 * 
 */
public class HoopExperimenter extends HoopEmbeddedJPanel implements ActionListener, ListSelectionListener 
{  
	private static final long serialVersionUID = 8387762921834350566L;
	
    private JFileChooser fc=null;	
	private JLabel filePath=null;
	private JLabel queryStats=null;	
	private mxGraph graph=null;	
	private JList<String> searchList=null;
	
	public HoopExperimenter ()  
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
		
		searchList=new JList<String> ();
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
	        		
	        	//HoopLink.queries=HoopLink.fManager.loadLines(file.getPath());
	        	
	        	String content=HoopLink.fManager.loadContents(HoopLink.datapath);
	        	
	            HoopLink.queries=HoopStringTools.dataToLines(content);
	        		
	        	queryStats.setText("Loaded " + HoopLink.queries.size() + " runnable queries");
	        		
	        	filePath.setText(file.getPath());
	        }
		}
		
		//>---------------------------------------------------------
		
		if (button.getText().equals("Run"))
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
			
			if (HoopLink.queries==null)
			{
				queryStats.setText ("Please load an experiment file first");
				JOptionPane.showMessageDialog(null, "Please load an experiment file first");
				return;
			}
			
			if (HoopLink.queries.size()==0)
			{
				queryStats.setText ("Experiment file has 0 entries");
				JOptionPane.showMessageDialog(null, "Experiment file has 0 entries");
				return;
			}			
			
			debug ("Running experiment ...");
			
			HoopPerformanceMeasure metrics=new HoopPerformanceMeasure ();
			metrics.setMarker ("Query Experiment");
			HoopLink.metrics.getDataSet().add(metrics);
			
			DefaultListModel<String> mdl=new DefaultListModel<String> ();
			
			searchList.setModel (mdl);
									
			for (int i=0;i<HoopLink.queries.size();i++)
			{
				String aQuery=HoopLink.queries.get(i);
				
				String [] preProcess=aQuery.split("\\:");
				
				String theQuery=aQuery;
				
				if (preProcess.length>1)
					theQuery=preProcess [1];
					
				debug ("Running query: " + theQuery);
				
				HoopTextSearch aSearch=new HoopTextSearch ();
				aSearch.setInstanceName(preProcess [0]);
				
				aSearch.search(theQuery,20);
				
				if (i==0)
				{
					visualize (null,aSearch.getRootQueryOperator());
				}
				
				HoopLink.searchHistory.add (aSearch);
				
				StringBuffer formatter=new StringBuffer ();
				
				HoopQueryOperator query=aSearch.getRootQueryOperator ();
				
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
								
			//long timeTaken=metrics.getMarkerRaw ();
			metrics.closeMarker();
			long timeTaken=metrics.getYValue();
			
			queryStats.setText(metrics.getMetrics(timeTaken));
			
			HoopLink.experimentNr++;			
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
	public void valueChanged(ListSelectionEvent event) 
	{
		debug ("valueChanged ()");
		
		int selectedRow=event.getFirstIndex();
		
		if (selectedRow!=-1)
		{
			HoopTextSearch aSearch=HoopLink.searchHistory.get(selectedRow);
						
			if (aSearch!=null)
			{
				visualize (null,aSearch.getRootQueryOperator());
				
				HoopLink.dataSet=aSearch.getDataSet();
				
				HoopLink.updateAllWindows ();
			}			
		}
	}	
}
