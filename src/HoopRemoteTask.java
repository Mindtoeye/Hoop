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
 * By default uses the following:
 * 
 * 	stopwords: a,an,and,are,as,at,for,i,if,in,is,it,of,on,so,that,the,to
 *  garbage:   -$%^&*()!@# (when found as a complete term)
 * 	casefolding: yes (can be turned off but is not recommended)
 * 	stemming: yes
 * 
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.*;

import edu.cmu.cs.in.search.HoopDataSet;
import edu.cmu.cs.in.base.HoopRoot;
import edu.cmu.cs.in.base.HoopStringTools;
import edu.cmu.cs.in.base.io.HoopFileManager;
import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.stats.HoopPerformanceMeasure;
import edu.cmu.cs.in.stats.HoopStatistics;
import edu.cmu.cs.in.hadoop.HoopHadoopReporter;
import edu.cmu.cs.in.hadoop.HoopInvertedListMapper;
import edu.cmu.cs.in.hadoop.HoopInvertedListReducer;
import edu.cmu.cs.in.hadoop.HoopShardedOutputFormat;
import edu.cmu.cs.in.hadoop.HoopWordCountMapper;
import edu.cmu.cs.in.hadoop.HoopWordCountReducer;
import edu.cmu.cs.in.hadoop.HoopWholeFileInputFormat;

public class HoopRemoteTask extends HoopHadoopReporter
{	
	public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";
	private HoopDataSet data=null;
	public static FileSystem hdfs=null;
       	
	/**
	 *
	 */
    public HoopRemoteTask () 
    {
		setClassName ("HoopRemoteTask");
		debug ("HoopRemoteTask ()");
		HoopRoot.debug ("Hoop","Running on: "+HoopHadoopReporter.getMachineName ());
    }
    /**
	 *
	 */
    private Boolean indexDocuments ()
    {
    	debug ("indexDocuments ()");
    	
    	/*
    	data=new HoopDataSet ();
    	data.loadDocuments(HoopLink.datapath);    	
        data.printStats ();
        */
    	
    	return (true);
    }
    /**
	 *
	 */
    private static void usage ()
    {
    	System.out.println ("Usage: choose either one of:");
    	System.out.println (" 1) Locally analyzing wiki files");
    	System.out.println (" 2) Running the MapReduce task on hadoop");
    	System.out.println (" 3) Post processing/analyzing hadoop output");
    	System.out.println (" ");    	
    	System.out.println ("Usage 1): -classpath HoopRemoteTask.jar HoopRemoteTask <options>");
    	System.out.println (" <options>:");
    	System.out.println ("	-task wordcount|invert");
    	System.out.println ("	-splitsize <nr> (Default: 10)"); // 10, 10K or 60K    	
    	System.out.println ("	-shards <nr> (Default: 1)");
    	System.out.println ("	-shardcreate hdfs|partitions|mos|none (Default: hdfs)");    	
    	System.out.println ("	-stopwords yes/no (Default: yes)");
    	System.out.println ("	-stemming yes/no (Default: yes)");    	
    	System.out.println ("	-minstemsize <Number> (Default: 4)");
    	System.out.println ("	-cleanoutput yes/no (Default: no");    	
    	System.out.println ("	-datapath <path>");
    	System.out.println ("	-outputpath <path>");
    	System.out.println ("	-monitorhost <ip|hostname>");    	
    	System.out.println ("Example:");    	
    	System.out.println (" java -classpath HoopRemoteTask.jar HoopRemoteTask -cleanoutput yes -datapath /Root/DataSet/Wiki-10 -outputpath Root/Results/");    	
    	System.out.println (" ");    	
    	System.out.println ("Usage 2): -classpath HoopRemoteTask.jar HoopRemoteTask -hadoop <options>");
    	System.out.println (" <options>:");
    	System.out.println ("	-splitsize <nr> (Default: 10)"); // 10, 10K or 60K    	
    	System.out.println ("	-shards <nr> (Default: 1)");    	
    	System.out.println ("	-shardcreate hdfs|partitions|mos|none (Default: hdfs)");    	
    	System.out.println ("	-dbglocal yes/no (Default: no)");    	
    	System.out.println ("	-stopwords yes/no (Default: yes)");
    	System.out.println ("	-stemming yes/no (Default: yes)");   
    	System.out.println ("	-minstemsize <Number> (Default: 4)");    	
    	System.out.println ("	-datapath <path>");
    	System.out.println ("	-outputpath <path>");    	
    	System.out.println ("	-monitorhost <ip|hostname>");    	
    	System.out.println ("Example:");    	
    	System.out.println (" java -classpath HoopRemoteTask.jar HoopRemoteTask -datapath /user/hduser/Wiki-10 -outputpath /user/hduser/output");    	
    	System.out.println (" ");
    	System.out.println ("Usage 3): -classpath HoopRemoteTask.jar HoopSorterStats <options>");
    	System.out.println (" <options>:");
    	System.out.println ("   -inputfile <path-to-file");
    	System.out.println ("	-gettop yes/no (Default: no) Shows the top 25 frequencies for the selected input");    	
    	System.out.println ("	-getselected yes/no (Default: no) Uses an internal table of selected words to show frequences for (see homework)");
    	System.out.println ("	-getshowrare yes/no (Default: no) For the frequencies 1,2,3,4,5 create output files using the output path and list the terms for each");    	
    	System.out.println (" ");    	
    	System.out.println ("Example:");    	
    	System.out.println (" java -classpath HoopRemoteTask.jar HoopSorterStats -getshowrare yes -inputfile Root/Results/MapReduced-60k.txt -outputpath Root/Results/");
    }
    /**
	 *
	 */    
    private static void dbg (String aMessage)
    {
    	HoopRoot.debug ("HoopRemoteTask",aMessage);
    }
    /**
	 *
	 */
    private static Boolean parseArgs (String args [])
    {
    	dbg ("parseArgs ()");
    
    	if (args.length<3)
    	{    		
    		return (false);
    	}
    	
        for (int i = 0; i < args.length; i++)
        {        	
        	if (args [i].compareTo ("-casefold")==0)
        	{
        		if (args [i+1].equals("yes")==true)
        			HoopLink.casefold=true;
        		else
        			HoopLink.casefold=false;
        	}
        	
        	if (args [i].compareTo ("-dbglocal")==0)
        	{
        		if (args [i+1].equals("yes")==true)
        			HoopLink.dbglocal=true;
        		else
        			HoopLink.dbglocal=false;
        	}        	
        	        	
        	if (args [i].compareTo ("-stopwords")==0)
        	{
        		if (args [i+1].equals("yes")==true)
        			HoopLink.stopwords=true;
        		else
        			HoopLink.stopwords=false;
        	}
        	
        	if (args [i].compareTo ("-stemming")==0)
        	{
        		if (args [i+1].equals("yes")==true)
        			HoopLink.stemming=true;
        		else
        			HoopLink.stemming=false;
        	}
        	
        	if (args [i].compareTo ("-minstemsize")==0)
        	{
        		String stemsizepre=args [i+1];
        		HoopLink.minstemsize=Integer.parseInt(stemsizepre);
        	}           	
        	
        	if (args [i].compareTo ("-cleanoutput")==0)
        	{
        		if (args [i+1].equals("yes")==true)
        			HoopLink.cleanoutput=true;
        		else
        			HoopLink.stemming=false;        		
        	}        	
        	
        	if (args [i].compareTo ("-datapath")==0)
        	{
        		HoopLink.datapath=args [i+1];
        	}
        	
        	if (args [i].compareTo ("-outputpath")==0)
        	{
        		HoopLink.outputpath=args [i+1];
        	}        	
        	
        	if (args [i].compareTo ("-hadoop")==0)
        	{
        		HoopLink.useHadoop=true;
        	}
        	
        	if (args [i].compareTo ("-task")==0)
        	{
        		String taskpre=args [i+1];
        		HoopLink.task=taskpre.toLowerCase();
        	}
        	
        	if (args [i].compareTo ("-splitsize")==0)
        	{
        		String sizepre=args [i+1];
        		HoopLink.splitsize=Long.parseLong(sizepre);
        	}        	
        	
        	if (args [i].compareTo ("-shards")==0)
        	{
        		String shardpre=args [i+1];
        		HoopLink.nrshards=Integer.parseInt(shardpre);
        	}
        	
        	if (args [i].compareTo ("-shardtype")==0)
        	{
        		HoopLink.shardtype=args [i+1];
        	}        	

        	if (args [i].compareTo ("-shardcreate")==0)
        	{
        		String createpre=args [i+1];
        		HoopLink.shardcreate=createpre.toLowerCase();
        	}
        	
        	if (args [i].compareTo ("-postonly")==0)
        	{
        		if (args [i+1].equals("yes")==true)
        			HoopLink.postonly=true;
        		else
        			HoopLink.postonly=false;        		
        	}        
        	
        	if (args [i].compareTo ("-monitorhost")==0)
        	{
        		String createhost=args [i+1];
        		HoopLink.monitorHost=createhost.toLowerCase();
        	}        	
        }    	
        
        return (true);
    }
    /**
	 *
	 */    
    public static void postOnly ()
    {
    	dbg ("postOnly ()");
    	
    	// First get the amount of items in the file ...
    	    	    	
		BufferedReader reader=null;
		
		//HoopFileManager fManager=new HoopFileManager ();
		
		String content=HoopLink.fManager.loadContents(HoopLink.datapath);
		
		long termCount=HoopStringTools.dataToLines (content).size();
    	    	
		dbg ("Sharding " + termCount + " terms ...");
		
    	// Then shard as usual
    			
		try 
		{
			reader=new BufferedReader(new FileReader (HoopLink.datapath));
		} 
		catch (FileNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String thisLine="";
		int count=0;
		int split=Math.round(termCount/HoopLink.nrshards);
		int partition=0;		    				
		Writer out=null;
		
		try 
		{
			out=new BufferedWriter (new FileWriter(HoopLink.outputpath+"/partition-"+partition+"-00000.txt"));
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		dbg ("Sharding to: " + HoopLink.outputpath+"/partition-"+partition+"-00000.txt");
						
		if (out!=null)
		{
			try 
			{
				while ((thisLine=reader.readLine())!=null) 
				{					
					count++;
				
					if (count>split)
					{									
						partition++;
					    			
						out=new BufferedWriter (new FileWriter(HoopLink.outputpath+"/partition-"+partition+"-00000.txt"));
					
						dbg ("Sharding to: " + HoopLink.outputpath+"/partition-"+partition+"-00000.txt");
						
						split++;
						count=0;
					}
				    										
					out.write(thisLine);
					out.write("\n");
				}
			} 
			catch (UnsupportedEncodingException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}								
		}	
		else
			dbg ("Error: unable to open output file");
		
		dbg ("All done!");
    }    
    /**
	 *
	 */    
    public static int countTerms (Configuration conf)
    {
    	dbg ("postProcess ()");
    	
    	int count=0;
    	
		String output=conf.get("mapred.output.dir");    
    	
		if (output!=null)
		{
			if (output.isEmpty()==true)
				output=HoopLink.outputpath;
		}
		else
			output=HoopLink.outputpath;    	
    	
		Path inFile=new Path (output+"/part-r-00000");
		FSDataInputStream in=null;
				
		@SuppressWarnings("unused")
		String thisLine=null;
		
		try 
		{
			in=HoopRemoteTask.hdfs.open (inFile);
			
			BufferedReader reader=new BufferedReader(new InputStreamReader(in));
			
			while ((thisLine=reader.readLine())!=null) 
			{				
				count++;		 
			}			
			
			in.close();
		} 
		catch (IOException e) 
		{			
			e.printStackTrace();
			dbg ("Error opening file in HDFS");
		}			
    	
    	return (count);
    }
    /**
	 *
	 */    
    public static void postProcess (Configuration conf)
    {
    	dbg ("postProcess ()");
    		
    	if (HoopLink.nrshards==1)
    	{
    		dbg ("Only 1 shard needed, skipping post processing");
    		return;
    	}
    	
    	if (HoopLink.shardcreate.equals("mos")==true)
    	{
    		dbg ("We shouldn't be pos-processing since the HoopShardedOutputFormat class already did this");
    		return;
    	}
    	
    	if (HoopLink.shardcreate.equals("hdfs")==true)
    	{    	
    		dbg ("Starting shard post-process task ...");
    		    	
    		int termCount=countTerms (conf);
    		
    		String output=conf.get("mapred.output.dir");    
    	    	    	
    		if (output!=null)
    		{
    			if (output.isEmpty()==true)
    				output=HoopLink.outputpath;
    		}
    		else
    			output=HoopLink.outputpath;
    	    	
    		dbg ("Post processing "+termCount+" items in: " + output);
    	
    		Path inFile=new Path (output+"/part-r-00000");
    		Path outFile=null;
    		FSDataInputStream in =null;
    		FSDataOutputStream out =null;
    	
    		try 
    		{
    			in=HoopRemoteTask.hdfs.open (inFile);
    			
    			BufferedReader reader=new BufferedReader(new InputStreamReader(in));
    			
    			String thisLine;
    			
    			int count=0;
    			int split=Math.round(termCount/HoopLink.nrshards);
    			int partition=0;
    			
    			outFile=new Path (output+"/partition-"+partition+"-00000.txt");    					
				out=HoopRemoteTask.hdfs.create (outFile);
    			
				if (out!=null)
				{
					while ((thisLine=reader.readLine())!=null) 
					{
						StringBuffer formatted=new StringBuffer ();
						formatted.append (thisLine);
						formatted.append ("\n");
						
						count++;
					
						if (count>split)
						{
							out.close();
    					
							partition++;
    					
							outFile=new Path (output+"/partition-"+partition+"-00000.txt");    					
							out=HoopRemoteTask.hdfs.create (outFile);
    					
							split++;
							count=0;
						}
    				    										
						byte[] utf8Bytes = formatted.toString().getBytes("UTF8");
						// We get an additional 0 because of Java string encoding. leave it out!
						out.write(utf8Bytes); 
					}
					
					if (in!=null)
						in.close();
					
	    			if (out!=null)
	    				out.close();					
				}	
				else
					dbg ("Error: unable to open output file");
    			
    		} 
    		catch (IOException e) 
    		{
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    	

    		dbg ("Starting rudimentary sharding into " + HoopLink.nrshards);
    	
        	if (in!=null)
        	{
        		        		
        		try 
        		{
					in.close();
				} 
        		catch (IOException e) 
        		{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}    		
        	
    	}	
    	
    	HoopStatistics stats=new HoopStatistics ();
    	String results=stats.printStatistics(null);
    	dbg (results);
    }
    /**
	 *
	 */
    public static void showTimeStamp ()
    {
    	Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
    	dbg ("Time: " + sdf.format(cal.getTime()));	
    }
    /**
     * 
     */
    public static void transferConf (Configuration conf)
    {
    	conf.set ("useHadoop",new Boolean (HoopLink.useHadoop).toString());
    	conf.set ("casefold",new Boolean (HoopLink.casefold).toString());
    	conf.set ("stopwords",new Boolean (HoopLink.stopwords).toString());
    	conf.set ("stemming",new Boolean (HoopLink.stemming).toString());
    	conf.set ("cleanoutput",new Boolean (HoopLink.cleanoutput).toString());
    	conf.set ("dbglocal",new Boolean (HoopLink.dbglocal).toString());
    	conf.set ("minstemsize",new Integer (HoopLink.minstemsize).toString());    	    	
    	conf.set ("splitsize",new Long (HoopLink.splitsize).toString());
    	conf.set ("nrshards",new Integer (HoopLink.nrshards).toString());
    	conf.set ("shardtype",HoopLink.shardtype);    	
    	conf.set ("shardcount",new Integer (HoopLink.shardcount).toString());
    	conf.set ("shardcreate",new Boolean (HoopLink.shardcreate).toString());   	
    	conf.set ("task",HoopLink.task);
    	conf.set ("monitorHost",HoopLink.monitorHost);    	
    }
    /**
	 *
	 */
    public static void main(String args[]) throws Exception
	{
    	// run the HoopLink constructor; We need this to have a global settings registry    	
    	@SuppressWarnings("unused")
    	HoopLink link = new HoopLink(); 
    	
    	dbg ("main ()");
    	
    	showTimeStamp ();
    	
    	/**
    	 * I've taken out the statistics portion since it relies on code that isn't distributed
    	 * The next version will have this solved. I might try the solution in:
    	 * http://stackoverflow.com/questions/7443074/initialize-public-static-variable-in-hadoop-through-arguments
    	 * Although chances are I will switch to using Hoop to collect much better performance and distribution 
    	 * statistics. See Hoop.java for more information
    	 */ 
    	    	
    	HoopPerformanceMeasure metrics=new HoopPerformanceMeasure ();
    	metrics.setMarker ("main");
    	HoopLink.metrics.getDataSet().add(metrics);
    	    	
    	if (parseArgs (args)==false)
    	{
    		usage ();
    		return;
    	}
    	
    	if (HoopLink.postonly==true)
    	{
    		postOnly ();
    		return;
    	}
    	
    	if (HoopLink.task.equals ("none")==true)
    	{
    		dbg ("No task defined, please use the commandline option -task <task>");
    		return;
    	}
        
        dbg ("Starting system ...");
        
        HoopRemoteTask driver=new HoopRemoteTask ();
        
        if (HoopLink.useHadoop==false)
        {
        	dbg ("Starting built-in mapper ...");
        	
        	driver.indexDocuments ();
        }
        else
        {
        	dbg ("Starting hadoop job ...");
        	
        	Configuration conf=new Configuration();
        	
        	// TRANSFER SETTHoopGS FROM HoopLink to Configuration!!!
        	
        	transferConf (conf);
        	
        	// Now we're feeling much better
        	
        	HoopRemoteTask.hdfs=FileSystem.get (conf);
        	
        	if (HoopLink.dbglocal==true)
        	{
        		dbg ("Enabling local debugging ...");
        		conf.set("mapred.job.tracker", "local");
        	}
        	else
        		dbg ("Disabling local debugging");
                 
        	JobConf job=new JobConf (conf,HoopRemoteTask.class);
        	
        	job.setJobName(driver.getClassName());
        	
        	driver.setJob(job);
        	
        	@SuppressWarnings("unused")
        	String[] otherArgs=new GenericOptionsParser(conf, args).getRemainingArgs();
        	        	
        	job.setJarByClass (HoopRemoteTask.class);
        	                 	        	                     	
        	if (HoopLink.task.equals("invert")==true)
        	{
        		dbg ("Configuring job for invert task ...");
        		
            	job.setReducerClass (HoopInvertedListReducer.class);
            	job.setMapperClass (HoopInvertedListMapper.class);
        		job.setMapOutputKeyClass(Text.class);		
        		job.setMapOutputValueClass(Text.class);
        	}
        	
        	if (HoopLink.task.equals("wordcount")==true)
        	{
        		dbg ("Configuring job for wordcount task ...");
        		
            	job.setReducerClass (HoopWordCountReducer.class);
            	job.setMapperClass (HoopWordCountMapper.class);
        		job.setMapOutputKeyClass (Text.class);
        		job.setMapOutputValueClass (IntWritable.class);
        	}	
                        	        	
        	dbg ("Using input path: " + HoopLink.datapath);
        	dbg ("Using output path: " + HoopLink.outputpath);
        	                	
        	FileInputFormat.addInputPath (job, new Path (HoopLink.datapath));
        	FileOutputFormat.setOutputPath (job,new Path (HoopLink.outputpath));
        	    
            job.setInputFormat (HoopWholeFileInputFormat.class);
        	
        	if ((HoopLink.shardcreate.equals("mos")==true) && (HoopLink.nrshards>1))
        	{        		
        		dbg ("Setting output to sharded output streams class ...");
        		
        		job.setOutputFormat (HoopShardedOutputFormat.class);
        	}
        	else
            	job.setOutputFormat (TextOutputFormat.class);        		
         
        	/**
        	 * Temporarily commented out for testing purposes
        	 */
        	
        	//job.setPartitionerClass (HoopPartitioner.class);        	        	
        	
        	driver.register("Main");
        	
            JobClient.runJob(job); 
        	
        	postProcess (conf);
        }
        
        showTimeStamp ();
        
        metrics.closeMarker();
        long timeTaken=metrics.getYValue();
        //long timeTaken=metrics.getMarkerRaw ();
        metrics.printMetrics(timeTaken);
        
        driver.unregister ();
        
    	/**
    	 * I've taken out the statistics portion since it relies on code that isn't distributed
    	 * The next version will have this solved. I might try the solution in:
    	 * http://stackoverflow.com/questions/7443074/initialize-public-static-variable-in-hadoop-through-arguments
    	 * Although chances are I will switch to using Hoop to collect much better performance and distribution 
    	 * statistics. See Hoop.java for more information
    	 */                
        //stats.calcStatistics();
        //dbg (stats.printStatistics());
	}
}
