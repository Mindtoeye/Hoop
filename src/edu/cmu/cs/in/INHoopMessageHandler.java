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

package edu.cmu.cs.in;

import java.util.ArrayList;

import javax.swing.DefaultListModel;
//import javax.swing.JList;
import javax.swing.JTextArea;
//import javax.swing.ListModel;

import org.w3c.dom.Element;

import edu.cmu.cs.in.base.INBase;
import edu.cmu.cs.in.base.INLink;
import edu.cmu.cs.in.controls.INGridNodeVisualizer;
import edu.cmu.cs.in.controls.INJFeatureList;
import edu.cmu.cs.in.controls.INScatterPlot;
import edu.cmu.cs.in.controls.INVisualFeature;
import edu.cmu.cs.in.stats.INPerformanceMetrics;
import edu.cmu.cs.in.stats.INStatistics;
import edu.cmu.cs.in.network.INMessageHandler;

/**
*
*/
public class INHoopMessageHandler extends INBase implements INMessageHandler
{    	
	private INStatistics stats=null;
	private INScatterPlot plotter=null;
	private JTextArea rawStats=null;
	private INGridNodeVisualizer clusterGrid=null;
	private INJFeatureList jobList=null;
	private DefaultListModel listModel=null;
	private ArrayList <String>jobs=null;
	
	/**
	*
	*/	
	public INHoopMessageHandler (INStatistics aStats,
								 INScatterPlot aPlotter,
								 JTextArea aStatsPanel,
								 INGridNodeVisualizer aGrid)
	{  
    	setClassName ("INHoopMessageHandler");
    	debug ("INHoopMessageHandler ()");
    	
    	stats=aStats;
    	plotter=aPlotter;
    	rawStats=aStatsPanel;
    	clusterGrid=aGrid;
    	//jobList=aJobList;
    	listModel = new DefaultListModel();
    	//aJobList.setModel(listModel);
    	jobs=new ArrayList ();
	}	
	/**
	 *
	 */
    public void handleIncomingData (int ID,String data)
    {
    	debug ("Received: ["+ID+"] " + data);
    }      
	/**
	 * Here is another case where it would be much better to use a
	 * hash table, but for clarity purposes we use an arraylist for
	 * now. Keep in mind that we typically use lists where we either
	 * know we have small amounts of data or where the calculation
	 * time doesn't matter.
	 */
    private INPerformanceMetrics findPerformanceMetric (String guid)
    {
    	for (int i=0;i<INLink.metrics.size();i++)
    	{
    		INPerformanceMetrics check=INLink.metrics.get(i);
    		if (check.getGuid().equals(guid)==true)
    		{
    			return (check);
    		}
    	}
    	
    	return (null);
    }
	/**
	 *
	 */
    private void addJob (String aJob)
    {
    	if (aJob.isEmpty()==true)
    		return;
    	
    	if (jobList==null)
    		return;
    	
    	for (int i=0;i<jobs.size();i++)
    	{
    		String job=jobs.get(i);
    		if (job.equals(aJob)==true)
    			return; // Already know about this one
    	}
    	
    	jobs.add(aJob);
    	    	
    	INVisualFeature feature=new INVisualFeature ();
    	feature.setInstanceName(aJob);
    	feature.setText(aJob);
    	listModel.addElement (feature);
    	jobList.invalidate();
    }
	/**
	 *
	 */
    public void handleIncomingXML (int ID,Element root)
    {
    	debug ("handleIncomingXML ()");
    	
    	String hadoopID="";
    	String hadoopGUID="";
    	String hadoopNode="";
    	Long   time=(long) 0;
    	
		if (root.getNodeName().equals("register")==true)
		{			
			debug ("Processing register messsage ...");
			
			addJob (root.getAttribute("job"));
			
			hadoopID=root.getAttribute("class");
			hadoopGUID=root.getAttribute("guid");
			hadoopNode=root.getAttribute("node");			
			time=Long.parseLong(root.getAttribute("time"));
			
			INPerformanceMetrics measure=new INPerformanceMetrics ();
			measure.setLabel(hadoopID);
			measure.setGuid(hadoopGUID);
			measure.setInPoint(time);
			
			debug ("Adding: " + measure.getLabel()+" at: " + time);
			
			/*
			if (clusterGrid!=null)
			{
				if (hadoopID.equals("Mapper")==true)
					clusterGrid.incNodeMapper (hadoopNode);
				
				if (hadoopID.equals("Reducer")==true)
					clusterGrid.incNodeReducer (hadoopNode);
			}
			*/
			
			INLink.metrics.add(measure);
		}
		
		if (root.getNodeName().equals("unregister")==true)
		{			
			debug ("Processing unregister messsage ...");
			
			hadoopID=root.getAttribute("class");
			hadoopGUID=root.getAttribute("guid");
			hadoopNode=root.getAttribute("node");
			time=Long.parseLong(root.getAttribute("time"));
			
			INPerformanceMetrics update=findPerformanceMetric (hadoopGUID);
			if (update!=null)
			{
				debug ("Updating: " + update.getLabel()+" with outpoint: " + time + "for inpoint:" + update.getInPoint());
				
				update.setOutPoint(time);
				
				/*
				if (clusterGrid!=null)
				{
					if (hadoopID.equals("Mapper")==true)
						clusterGrid.decNodeMapper (hadoopNode);
					
					if (hadoopID.equals("Reducer")==true)
						clusterGrid.decNodeReducer (hadoopNode);
				}
				*/				
			}
			
			if (hadoopID.toLowerCase().equals("main")==true)
			{
				debug ("Received unregister of Main task, calculating statistics ...");
				stats.calcStatistics();
				String results=stats.printStatistics();
				debug (results);
				
				if (rawStats!=null)
				{
					rawStats.setText("Status: Done\n");
					rawStats.append(results);
				}
			}
		}
		
		if (plotter!=null)
			plotter.setData(INLink.metrics);
		
		if (hadoopID.toLowerCase().equals("main")==false)
		{
			if (rawStats!=null)
			{
				StringBuffer formatter=new StringBuffer ();
				formatter.append("Status: Running\n");
				formatter.append("Nr. Measures: " + INLink.metrics.size()+"\n");
				rawStats.setText (formatter.toString());
			}
		}	
    }
}
