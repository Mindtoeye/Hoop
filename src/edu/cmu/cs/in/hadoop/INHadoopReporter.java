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

package edu.cmu.cs.in.hadoop;

import java.net.InetAddress;
import java.util.Random;
import java.util.UUID;

import org.apache.hadoop.mapred.JobConf;

import edu.cmu.cs.in.base.INFeatureMatrixBase;
import edu.cmu.cs.in.base.INLink;
import edu.cmu.cs.in.stats.INPerformanceMetrics;
import edu.cmu.cs.in.network.INStreamedSocket;

/**
*
*/
public class INHadoopReporter extends INFeatureMatrixBase
{    						
	private INStreamedSocket socket=null;	
	private String guid="";
	private String hadoopClass="Undefined";
	private INPerformanceMetrics marker=null;
	public static String cachedMachineName="undefined";	
	private JobConf job=null;
	
	/**
	 *
	 */
    public INHadoopReporter () 
    {
		setClassName ("INHadoopReporter");
		//debug ("INHadoopReporter ()");
		
		setGuid(UUID.randomUUID().toString());
		marker=new INPerformanceMetrics ();
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
		
		if (INHadoopReporter.cachedMachineName.equals("undefined")==false)
			return (INHadoopReporter.cachedMachineName);
		
    	try
    	{
    		String computername=InetAddress.getLocalHost().getHostName();
    		return (computername);
    	}
    	catch (Exception e)
    	{
    		INFeatureMatrixBase.debug ("INHoop","Exception caught ="+e.getMessage());
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
    		socket=new INStreamedSocket ();
    	
    	marker.setMarker(hadoopClass);

    	if (job!=null)
    		socket.sendAndKeepOpen(INLink.monitorHost,INLink.monitorPort,"<?xml version=\"1.0\" encoding=\"utf-8\"?><register class=\""+hadoopClass+"\" job=\""+job.getJobName()+"\" node=\""+INHadoopReporter.getMachineName()+"\" guid=\""+guid+"\" time=\""+marker.getInPoint ()+"\" />");
    	else
    		socket.sendAndKeepOpen(INLink.monitorHost,INLink.monitorPort,"<?xml version=\"1.0\" encoding=\"utf-8\"?><register class=\""+hadoopClass+"\" job=\"UndefinedJob\" node=\""+INHadoopReporter.getMachineName()+"\" guid=\""+guid+"\" time=\""+marker.getInPoint ()+"\" />");
    }    
	/**
	 *
	 */
    public void unregister ()
    {
    	if (socket==null)
    		return;
   	
    	if (job!=null)
    		socket.sendAndKeepOpen(INLink.monitorHost,INLink.monitorPort,"<?xml version=\"1.0\" encoding=\"utf-8\"?><unregister class=\""+hadoopClass+"\" job=\""+job.getJobName()+"\" node=\""+INHadoopReporter.getMachineName()+"\" guid=\""+guid+"\" time=\""+marker.getOutPoint()+"\" />");
    	else
    		socket.sendAndKeepOpen(INLink.monitorHost,INLink.monitorPort,"<?xml version=\"1.0\" encoding=\"utf-8\"?><unregister class=\""+hadoopClass+"\" job=\"UndefinedJob\" node=\""+INHadoopReporter.getMachineName()+"\" guid=\""+guid+"\" time=\""+marker.getOutPoint()+"\" />");
    	
    	cachedMachineName="undefined"; // Horrible horrible hack
    	
		socket.close();
    }    
}
