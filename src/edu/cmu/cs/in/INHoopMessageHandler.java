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

package edu.cmu.cs.in;

//import java.util.ArrayList;

import javax.swing.DefaultListModel;
//import javax.swing.JList;
//import javax.swing.JTextArea;
//import javax.swing.ListModel;

import org.w3c.dom.Element;

import edu.cmu.cs.in.base.INBase;
import edu.cmu.cs.in.base.INHoopLink;
//import edu.cmu.cs.in.controls.INGridNodeVisualizer;
//import edu.cmu.cs.in.controls.INJFeatureList;
//import edu.cmu.cs.in.controls.INScatterPlot;
import edu.cmu.cs.in.controls.INVisualFeature;
import edu.cmu.cs.in.controls.base.INEmbeddedJPanel;
import edu.cmu.cs.in.hoop.INHoopStatistics;
import edu.cmu.cs.in.stats.INPerformanceMetrics;
//import edu.cmu.cs.in.stats.INStatistics;
import edu.cmu.cs.in.network.INMessageHandler;

/**
*
*/
public class INHoopMessageHandler extends INBase implements INMessageHandler
{    	
	//private INStatistics stats=null;
	//private INScatterPlot plotter=null;
	//private JTextArea rawStats=null;
	//private INGridNodeVisualizer clusterGrid=null;
	//private INJFeatureList jobList=null;
	private DefaultListModel listModel=null;
	//private ArrayList <String>jobs=null;
	
	/**
	*
	*/	
	public INHoopMessageHandler ()
	{  
    	setClassName ("INHoopMessageHandler");
    	debug ("INHoopMessageHandler ()");
    	
    	//stats=aStats;
    	//plotter=aPlotter;
    	//rawStats=aStatsPanel;
    	//clusterGrid=aGrid;
    	//jobList=aJobList;
    	listModel = new DefaultListModel();
    	//aJobList.setModel(listModel);
    	//jobs=new ArrayList ();
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
    	for (int i=0;i<INHoopLink.metrics.size();i++)
    	{
    		INPerformanceMetrics check=INHoopLink.metrics.get(i);
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
    	    	
    	for (int i=0;i<INHoopLink.jobs.size();i++)
    	{
    		String job=INHoopLink.jobs.get(i);
    		if (job.equals(aJob)==true)
    			return; // Already know about this one
    	}
    	
    	INHoopLink.jobs.add(aJob);
    	    	
    	INVisualFeature feature=new INVisualFeature ();
    	feature.setInstanceName(aJob);
    	feature.setText(aJob);
    	listModel.addElement (feature);
    	
		INEmbeddedJPanel win=INHoopLink.getWindow ("Hadoop Jobs");
		if (win!=null)
		{
			win.updateContents();
	    	//jobList.invalidate();
		}    	
    }
	/**
	 *
	 */
    public void handleIncomingData (int ID,String data)
    {
    	debug ("Received: ["+ID+"] " + data);
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
    	Long time=(long) 0;
    	
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
									
			INHoopLink.metrics.add(measure);
			
			INEmbeddedJPanel win=INHoopLink.getWindow ("Cluster");
			if (win!=null)
			{
				win.updateContents();
			}
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
				INHoopLink.stats.calcStatistics();
				String results=INHoopLink.stats.printStatistics();
				debug (results);
				
				INHoopStatistics statsPanel=(INHoopStatistics) INHoopLink.getWindow ("Statistics");
				
				if (statsPanel!=null)
				{
					statsPanel.appendString ("Status: Done\n");
					statsPanel.appendString(results);
				}
				
				/*
				if (rawStats!=null)
				{
					rawStats.setText("Status: Done\n");
					rawStats.append(results);
				}
				*/
			}
			
			INEmbeddedJPanel win=INHoopLink.getWindow ("Cluster");
			if (win!=null)
			{
				win.updateContents();
			}			
		}
		
		/*
		if (plotter!=null)
			plotter.setData(INHoopLink.metrics);
		*/

		INEmbeddedJPanel plotwin=INHoopLink.getWindow ("Plotter");
		if (plotwin!=null)
		{
			plotwin.updateContents();
		}			
		
		if (hadoopID.toLowerCase().equals("main")==false)
		{

			INHoopStatistics statsPanel=(INHoopStatistics) INHoopLink.getWindow ("Statistics");
			
			if (statsPanel!=null)
			{
				StringBuffer formatter=new StringBuffer ();
				formatter.append("Status: Running\n");
				formatter.append("Nr. Measures: " + INHoopLink.metrics.size()+"\n");
				
				statsPanel.appendString (formatter.toString());
			}
			
			/*
			if (rawStats!=null)
			{
				StringBuffer formatter=new StringBuffer ();
				formatter.append("Status: Running\n");
				formatter.append("Nr. Measures: " + INHoopLink.metrics.size()+"\n");
				rawStats.setText (formatter.toString());
			}
			*/
		}	
    }
}
