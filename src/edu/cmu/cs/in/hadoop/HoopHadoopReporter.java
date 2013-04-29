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

package edu.cmu.cs.in.hadoop;

import java.net.InetAddress;
import java.util.UUID;

import org.apache.hadoop.mapred.JobConf;

import edu.cmu.cs.in.base.HoopRoot;
import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.base.io.HoopMessageReceiver;
import edu.cmu.cs.in.base.io.HoopStreamedSocket;
import edu.cmu.cs.in.stats.HoopPerformanceMeasure;

/**
*
*/
public class HoopHadoopReporter extends HoopRoot implements HoopMessageReceiver
{    						
	private HoopStreamedSocket socket=null;	
	private String guid="";
	private String hadoopClass="Undefined";
	private HoopPerformanceMeasure marker=null;
	public static String cachedMachineName="undefined";	
	private JobConf job=null;
	
	/**
	 *
	 */
    public HoopHadoopReporter () 
    {
		setClassName ("HoopHadoopReporter");
		//debug ("HoopHadoopReporter ()");
		
		setGuid(UUID.randomUUID().toString());
		marker=new HoopPerformanceMeasure ();
    }
	/**
	 *
	 */
	public JobConf getJob() 
	{
		return job;
	}
	/**
	 *
	 */
	public void setJob(JobConf job) 
	{
		this.job = job;
	}    
	/**
	 *
	 */    
	public String getGuid() 
	{
		return guid;
	}
	/**
	 *
	 */	
	public void setGuid(String guid) 
	{
		this.guid = guid;
	}    
	/**
	 *
	 */    
	public String getHadoopClass() 
	{
		return hadoopClass;
	}
	/**
	 *
	 */	
	public void setHadoopClass(String hadoopClass) 
	{
		this.hadoopClass = hadoopClass;
	} 
	/**
	 * 
	 */
	public static String getMachineName ()
	{
		/*
		if (cachedMachineName.equals("undefined")==true)
		{
			Random randomizer=new Random ();
			cachedMachineName=("Node-"+randomizer.nextInt(10));
		}
		
		return (cachedMachineName);
		*/
		
		if (HoopHadoopReporter.cachedMachineName.equals("undefined")==false)
			return (HoopHadoopReporter.cachedMachineName);
		
    	try
    	{
    		String computername=InetAddress.getLocalHost().getHostName();
    		return (computername);
    	}
    	catch (Exception e)
    	{
    		HoopRoot.debug ("Hoop","Exception caught ="+e.getMessage());
    	}    	
    	
    	return ("undefined");
	}
	/**
	 *
	 */
	public void register ()
	{
		register (this.getHadoopClass());
	}
	/**
	 *
	 */
    public void register (String aClass)
    {
    	debug ("register ("+aClass+")");
    	
    	setHadoopClass (aClass);
    	
    	if (socket==null)
    		socket=new HoopStreamedSocket ();
    	
    	marker.setMarker(hadoopClass);

    	if (job!=null)
    		socket.sendAndKeepOpen(HoopLink.monitorHost,
    							   HoopLink.monitorPort,
    							   "<?xml version=\"1.0\" encoding=\"utf-8\"?><register class=\""+hadoopClass+"\" job=\""+job.getJobName()+"\" node=\""+HoopHadoopReporter.getMachineName()+"\" guid=\""+guid+"\" time=\""+marker.getInPoint ()+"\" />",
    							   this);
    	else
    		socket.sendAndKeepOpen(HoopLink.monitorHost,
    							   HoopLink.monitorPort,
    							   "<?xml version=\"1.0\" encoding=\"utf-8\"?><register class=\""+hadoopClass+"\" job=\"UndefinedJob\" node=\""+HoopHadoopReporter.getMachineName()+"\" guid=\""+guid+"\" time=\""+marker.getInPoint ()+"\" />",
    							   this);
    }    
	/**
	 *
	 */
    public void unregister ()
    {
    	if (socket==null)
    		return;
   	
    	if (job!=null)
    		socket.sendAndKeepOpen(HoopLink.monitorHost,
    							   HoopLink.monitorPort,
    							   "<?xml version=\"1.0\" encoding=\"utf-8\"?><unregister class=\""+hadoopClass+"\" job=\""+job.getJobName()+"\" node=\""+HoopHadoopReporter.getMachineName()+"\" guid=\""+guid+"\" time=\""+marker.getOutPoint()+"\" />",
    							   this);
    	else
    		socket.sendAndKeepOpen(HoopLink.monitorHost,
    							   HoopLink.monitorPort,
    							   "<?xml version=\"1.0\" encoding=\"utf-8\"?><unregister class=\""+hadoopClass+"\" job=\"UndefinedJob\" node=\""+HoopHadoopReporter.getMachineName()+"\" guid=\""+guid+"\" time=\""+marker.getOutPoint()+"\" />",
    							   this);
    	
    	cachedMachineName="undefined"; // Horrible horrible hack
    	
		socket.close();
    }  
	/**
	 *
	 */    
	@Override
	public void handleIncomingData(String data) 
	{
		debug ("handleIncomingData ()");
		
	}
	/**
	 *
	 */	
	@Override
	public void handleConnectionClosed() 
	{
		debug ("handleConnectionClosed ()");
		
	}    
}
