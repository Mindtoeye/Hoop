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

import edu.cmu.cs.in.base.HoopRoot;
import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.controls.base.HoopEmbeddedJPanel;
import edu.cmu.cs.in.hoop.HoopStatisticsPanel;
import edu.cmu.cs.in.hoop.visualizers.HoopCluster;
import edu.cmu.cs.in.hoop.visualizers.HoopScatterPlot;
import edu.cmu.cs.in.network.HoopMessageHandlerInterface;
import edu.cmu.cs.in.stats.HoopPerformanceMeasure;

/**
*
*/
public class HoopMessageHandler extends HoopRoot implements HoopMessageHandlerInterface
{    	
	/**
	*
	*/	
	public HoopMessageHandler ()
	{  
    	setClassName ("HoopMessageHandler");
    	debug ("HoopMessageHandler ()");    	
	}	     
	/**
	 * Here is another case where it would be much better to use a
	 * hash table, but for clarity purposes we use an arraylist for
	 * now. Keep in mind that we typically use lists where we either
	 * know we have small amounts of data or where the calculation
	 * time doesn't matter.
	 */
	/*
    private HoopPerformanceMeasure findPerformanceMetric (String guid)
    {
    	for (int i=0;i<HoopLink.metrics.size();i++)
    	{
    		HoopPerformanceMeasure check=(HoopPerformanceMeasure) HoopLink.metrics.get(i);
    		if (check.getGuid().equals(guid)==true)
    		{
    			return (check);
    		}
    	}
    	
    	return (null);
    }
    */
	/**
	 *
	 */
    private void addJob (String aJob)
    {
    	if (aJob.isEmpty()==true)
    		return;
    	    	
    	Boolean found=false;
    	
    	for (int i=0;i<HoopLink.jobs.size();i++)
    	{
    		String job=HoopLink.jobs.get(i);
    		if (job.equals(aJob)==true)
    		{
    			found=true;
    		}
    	}
    	
    	if (found==false)
    		HoopLink.jobs.add(aJob);
    	    	    	
		HoopEmbeddedJPanel win=HoopLink.getWindow ("Hadoop Jobs");
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
    	    	
    	if (root.getName().equals("register")==true)
    	{
    		debug ("Processing register messsage ...");
    		
    		addJob (root.getAttributeValue("job"));
    		
			hadoopID=root.getAttributeValue("class");
			hadoopGUID=root.getAttributeValue("guid");
			hadoopNode=root.getAttributeValue("node");			
			time=Long.parseLong(root.getAttributeValue("time"));    	
			
			HoopPerformanceMeasure measure=new HoopPerformanceMeasure ();
			measure.setLabel(hadoopID);
			measure.setGuid(hadoopGUID);
			measure.setInPoint(time);
			
			debug ("Adding: " + measure.getLabel()+" at: " + time);			
    		
			HoopLink.metrics.getDataSet().add(measure);
			
			HoopEmbeddedJPanel win=HoopLink.getWindow ("Cluster");
			if (win!=null)
			{
				HoopCluster cluster=(HoopCluster) win;
				
				if (hadoopID.equals("Mapper")==true)
					cluster.getDriver().incNodeMapper (hadoopNode);
				
				if (hadoopID.equals("Reducer")==true)
					cluster.getDriver().incNodeReducer (hadoopNode);
				
				//win.updateContents();
			}			
    	}
    	    	
    	if (root.getName().equals("unregister")==true)
    	{
			debug ("Processing unregister messsage ...");
			
			hadoopID=root.getAttributeValue("class");
			hadoopGUID=root.getAttributeValue("guid");
			hadoopNode=root.getAttributeValue("node");
			time=Long.parseLong(root.getAttributeValue("time"));
			
			/*
			HoopPerformanceMeasure update=findPerformanceMetric (hadoopGUID);
			
			if (update!=null)
			{    		
				debug ("Updating: " + update.getLabel()+" with outpoint: " + time + "for inpoint:" + update.getInPoint());
				
				update.setOutPoint(time);
				
				HoopEmbeddedJPanel win=HoopLink.getWindow ("Cluster");
				if (win!=null)
				{
					HoopCluster cluster=(HoopCluster) win;
					
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
				HoopLink.stats.calcStatistics(HoopLink.metrics);
				String results=HoopLink.stats.printStatistics();
				debug (results);
				
				HoopStatisticsPanel statsPanel=(HoopStatisticsPanel) HoopLink.getWindow ("Statistics");
				
				if (statsPanel!=null)
				{
					statsPanel.appendString ("Status: Done\n");
					statsPanel.appendString(results);
				}			
			}
			*/			
    	}
		    	
    	HoopStatisticsPanel stats=(HoopStatisticsPanel) HoopLink.getWindow("Statistics");
    	
    	if (stats!=null)
    	{
    		stats.setData(HoopLink.metrics);
    		stats.updateContents();
    	}
		
		if (hadoopID.toLowerCase().equals("main")==false)
		{

			HoopStatisticsPanel statsPanel=(HoopStatisticsPanel) HoopLink.getWindow ("Statistics");
			
			if (statsPanel!=null)
			{
				StringBuffer formatter=new StringBuffer ();
				formatter.append("Status: Running\n");
				formatter.append("Nr. Measures: " + HoopLink.metrics.getDataSet().size()+"\n");
				
				statsPanel.appendString (formatter.toString());
			}		
		}	
    }
}
