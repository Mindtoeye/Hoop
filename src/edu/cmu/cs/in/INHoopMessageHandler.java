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

import org.jdom.Element;

import edu.cmu.cs.in.base.INBase;
import edu.cmu.cs.in.base.INHoopLink;
import edu.cmu.cs.in.controls.base.INEmbeddedJPanel;
import edu.cmu.cs.in.hoop.INHoopStatistics;
import edu.cmu.cs.in.hoop.visualizers.INHoopCluster;
import edu.cmu.cs.in.hoop.visualizers.INScatterPlot;
import edu.cmu.cs.in.stats.INPerformanceMetrics;
import edu.cmu.cs.in.network.INMessageHandler;

/**
*
*/
public class INHoopMessageHandler extends INBase implements INMessageHandler
{    	
	/**
	*
	*/	
	public INHoopMessageHandler ()
	{  
    	setClassName ("INHoopMessageHandler");
    	debug ("INHoopMessageHandler ()");    	
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
    	    	
    	Boolean found=false;
    	
    	for (int i=0;i<INHoopLink.jobs.size();i++)
    	{
    		String job=INHoopLink.jobs.get(i);
    		if (job.equals(aJob)==true)
    		{
    			found=true;
    		}
    	}
    	
    	if (found==false)
    		INHoopLink.jobs.add(aJob);
    	    	    	
		INEmbeddedJPanel win=INHoopLink.getWindow ("Hadoop Jobs");
		if (win!=null)
			win.updateContents();    	
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
    	
    	if (root.getName().equals("ack")==true)
    	{
    		debug ("Processing ack message ...");
    	}
    	
    	/*
		if (root.getNodeName().equals("ack")==true)
		{	
			debug ("Processing acknowledge messsage ...");
			
		}
		*/
    	
    	if (root.getName().equals("register")==true)
    	{
    		debug ("Processing register messsage ...");
    		
    		addJob (root.getAttributeValue("job"));
    		
			hadoopID=root.getAttributeValue("class");
			hadoopGUID=root.getAttributeValue("guid");
			hadoopNode=root.getAttributeValue("node");			
			time=Long.parseLong(root.getAttributeValue("time"));    	
			
			INPerformanceMetrics measure=new INPerformanceMetrics ();
			measure.setLabel(hadoopID);
			measure.setGuid(hadoopGUID);
			measure.setInPoint(time);
			
			debug ("Adding: " + measure.getLabel()+" at: " + time);			
    		
			INHoopLink.metrics.add(measure);
			
			INEmbeddedJPanel win=INHoopLink.getWindow ("Cluster");
			if (win!=null)
			{
				INHoopCluster cluster=(INHoopCluster) win;
				
				if (hadoopID.equals("Mapper")==true)
					cluster.getDriver().incNodeMapper (hadoopNode);
				
				if (hadoopID.equals("Reducer")==true)
					cluster.getDriver().incNodeReducer (hadoopNode);
				
				//win.updateContents();
			}			
    	}
    	
    	/*
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
											
			INHoopLink.metrics.add(measure);
			
			INEmbeddedJPanel win=INHoopLink.getWindow ("Cluster");
			if (win!=null)
			{
				INHoopCluster cluster=(INHoopCluster) win;
				
				if (hadoopID.equals("Mapper")==true)
					cluster.getDriver().incNodeMapper (hadoopNode);
				
				if (hadoopID.equals("Reducer")==true)
					cluster.getDriver().incNodeReducer (hadoopNode);
				
				//win.updateContents();
			}
		}
		*/
    	
    	if (root.getName().equals("register")==true)
    	{
			debug ("Processing unregister messsage ...");
			
			hadoopID=root.getAttributeValue("class");
			hadoopGUID=root.getAttributeValue("guid");
			hadoopNode=root.getAttributeValue("node");
			time=Long.parseLong(root.getAttributeValue("time"));
			
			INPerformanceMetrics update=findPerformanceMetric (hadoopGUID);
			
			if (update!=null)
			{    		
				debug ("Updating: " + update.getLabel()+" with outpoint: " + time + "for inpoint:" + update.getInPoint());
				
				update.setOutPoint(time);
				
				INEmbeddedJPanel win=INHoopLink.getWindow ("Cluster");
				if (win!=null)
				{
					INHoopCluster cluster=(INHoopCluster) win;
					
					if (hadoopID.equals("Mapper")==true)
						cluster.getDriver().decNodeMapper (hadoopNode);
					
					if (hadoopID.equals("Reducer")==true)
						cluster.getDriver().decNodeReducer (hadoopNode);
					
					//win.updateContents();
				}								
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
			}			
    	}
		
    	/*
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
				
				INEmbeddedJPanel win=INHoopLink.getWindow ("Cluster");
				if (win!=null)
				{
					INHoopCluster cluster=(INHoopCluster) win;
					
					if (hadoopID.equals("Mapper")==true)
						cluster.getDriver().decNodeMapper (hadoopNode);
					
					if (hadoopID.equals("Reducer")==true)
						cluster.getDriver().decNodeReducer (hadoopNode);
					
					//win.updateContents();
				}				
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
			}
			
			//INEmbeddedJPanel win=INHoopLink.getWindow ("Cluster");
			//if (win!=null)
			//{
			//	win.updateContents();
			//}			
		}
    	*/
		
		INScatterPlot plotter=(INScatterPlot) INHoopLink.getWindow ("Main Data Plotter");
		
		if (plotter!=null)
			plotter.setData(INHoopLink.metrics);

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
		}	
    }
}
