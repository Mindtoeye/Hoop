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
 * Uses the following:
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

import edu.cmu.cs.in.INDataSet;
import edu.cmu.cs.in.base.INBase;
import edu.cmu.cs.in.base.INFileManager;
import edu.cmu.cs.in.base.INLink;
import edu.cmu.cs.in.network.INSocketServerBase;
import edu.cmu.cs.in.stats.INPerformanceMetrics;
import edu.cmu.cs.in.stats.INStatistics;
import edu.cmu.cs.in.hadoop.INHadoopReporter;
import edu.cmu.cs.in.hadoop.INInvertedListMapper;
import edu.cmu.cs.in.hadoop.INInvertedListReducer;
import edu.cmu.cs.in.hadoop.INShardedOutputFormat;
import edu.cmu.cs.in.hadoop.INWordCountMapper;
import edu.cmu.cs.in.hadoop.INWordCountReducer;
import edu.cmu.cs.in.hadoop.INWholeFileInputFormat;

import edu.cmu.cs.in.hadoop.INPartitioner;

public class INMain extends INSocketServerBase
{	
	public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";
	private INDataSet data=null;
	public static FileSystem hdfs=null;
       	
	/**
	 *
	 */
    public INMain () 
    {
		setClassName ("INMain");
		debug ("INMain ()");
		INBase.debug ("INHoop","Running on: "+INHadoopReporter.getMachineName ());
    }
    /**
	 *
	 */
    private Boolean indexDocuments ()
    {
    	debug ("indexDocuments ()");
    	
    	data=new INDataSet ();
    	data.loadDocuments(INLink.datapath);
    	
        data.printStats ();
    	
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
    	System.out.println ("Usage 1): -classpath INMain.jar INMain <options>");
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
    	System.out.println (" java -classpath INMain.jar INMain -cleanoutput yes -datapath /Root/DataSet/Wiki-10 -outputpath Root/Results/");    	
    	System.out.println (" ");    	
    	System.out.println ("Usage 2): -classpath INMain.jar INMain -hadoop <options>");
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
    	System.out.println (" java -classpath INMain.jar INMain -datapath /user/hduser/Wiki-10 -outputpath /user/hduser/output");    	
    	System.out.println (" ");
    	System.out.println ("Usage 3): -classpath INMain.jar INSorterStats <options>");
    	System.out.println (" <options>:");
    	System.out.println ("   -inputfile <path-to-file");
    	System.out.println ("	-gettop yes/no (Default: no) Shows the top 25 frequencies for the selected input");    	
    	System.out.println ("	-getselected yes/no (Default: no) Uses an internal table of selected words to show frequences for (see homework)");
    	System.out.println ("	-getshowrare yes/no (Default: no) For the frequencies 1,2,3,4,5 create output files using the output path and list the terms for each");    	
    	System.out.println (" ");    	
    	System.out.println ("Example:");    	
    	System.out.println (" java -classpath INMain.jar INSorterStats -getshowrare yes -inputfile Root/Results/MapReduced-60k.txt -outputpath Root/Results/");
    }
    /**
	 *
	 */    
    private static void dbg (String aMessage)
    {
    	INBase.debug ("INMain",aMessage);
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
        			INLink.casefold=true;
        		else
        			INLink.casefold=false;
        	}
        	
        	if (args [i].compareTo ("-dbglocal")==0)
        	{
        		if (args [i+1].equals("yes")==true)
        			INLink.dbglocal=true;
        		else
        			INLink.dbglocal=false;
        	}        	
        	        	
        	if (args [i].compareTo ("-stopwords")==0)
        	{
        		if (args [i+1].equals("yes")==true)
        			INLink.stopwords=true;
        		else
        			INLink.stopwords=false;
        	}
        	
        	if (args [i].compareTo ("-stemming")==0)
        	{
        		if (args [i+1].equals("yes")==true)
        			INLink.stemming=true;
        		else
        			INLink.stemming=false;
        	}
        	
        	if (args [i].compareTo ("-minstemsize")==0)
        	{
        		String stemsizepre=args [i+1];
        		INLink.minstemsize=Integer.parseInt(stemsizepre);
        	}           	
        	
        	if (args [i].compareTo ("-cleanoutput")==0)
        	{
        		if (args [i+1].equals("yes")==true)
        			INLink.cleanoutput=true;
        		else
        			INLink.stemming=false;        		
        	}        	
        	
        	if (args [i].compareTo ("-datapath")==0)
        	{
        		INLink.datapath=args [i+1];
        	}
        	
        	if (args [i].compareTo ("-outputpath")==0)
        	{
        		INLink.outputpath=args [i+1];
        	}        	
        	
        	if (args [i].compareTo ("-hadoop")==0)
        	{
        		INLink.useHadoop=true;
        	}
        	
        	if (args [i].compareTo ("-task")==0)
        	{
        		String taskpre=args [i+1];
        		INLink.task=taskpre.toLowerCase();
        	}
        	
        	if (args [i].compareTo ("-splitsize")==0)
        	{
        		String sizepre=args [i+1];
        		INLink.splitsize=Long.parseLong(sizepre);
        	}        	
        	
        	if (args [i].compareTo ("-shards")==0)
        	{
        		String shardpre=args [i+1];
        		INLink.nrshards=Integer.parseInt(shardpre);
        	}
        	
        	if (args [i].compareTo ("-shardtype")==0)
        	{
        		INLink.shardtype=args [i+1];
        	}        	

        	if (args [i].compareTo ("-shardcreate")==0)
        	{
        		String createpre=args [i+1];
        		INLink.shardcreate=createpre.toLowerCase();
        	}
        	
        	if (args [i].compareTo ("-postonly")==0)
        	{
        		if (args [i+1].equals("yes")==true)
        			INLink.postonly=true;
        		else
        			INLink.postonly=false;        		
        	}        
        	
        	if (args [i].compareTo ("-monitorhost")==0)
        	{
        		String createhost=args [i+1];
        		INLink.monitorHost=createhost.toLowerCase();
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
		
		INFileManager fManager=new INFileManager ();
		long termCount=fManager.countLines (INLink.datapath);
    	    	
		dbg ("Sharding " + termCount + " terms ...");
		
    	// Then shard as usual
    			
		try 
		{
			reader=new BufferedReader(new FileReader (INLink.datapath));
		} 
		catch (FileNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String thisLine="";
		int count=0;
		int split=Math.round(termCount/INLink.nrshards);
		int partition=0;		    				
		Writer out=null;
		
		try 
		{
			out=new BufferedWriter (new FileWriter(INLink.outputpath+"/partition-"+partition+"-00000.txt"));
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		dbg ("Sharding to: " + INLink.outputpath+"/partition-"+partition+"-00000.txt");
						
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
					    			
						out=new BufferedWriter (new FileWriter(INLink.outputpath+"/partition-"+partition+"-00000.txt"));
					
						dbg ("Sharding to: " + INLink.outputpath+"/partition-"+partition+"-00000.txt");
						
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
				output=INLink.outputpath;
		}
		else
			output=INLink.outputpath;    	
    	
		Path inFile=new Path (output+"/part-r-00000");
		FSDataInputStream in=null;
				
		@SuppressWarnings("unused")
		String thisLine=null;
		
		try 
		{
			in=INMain.hdfs.open (inFile);
			
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
    		
    	if (INLink.nrshards==1)
    	{
    		dbg ("Only 1 shard needed, skipping post processing");
    		return;
    	}
    	
    	if (INLink.shardcreate.equals("mos")==true)
    	{
    		dbg ("We shouldn't be pos-processing since the INShardedOutputFormat class already did this");
    		return;
    	}
    	
    	if (INLink.shardcreate.equals("hdfs")==true)
    	{    	
    		dbg ("Starting shard post-process task ...");
    		    	
    		int termCount=countTerms (conf);
    		
    		String output=conf.get("mapred.output.dir");    
    	    	    	
    		if (output!=null)
    		{
    			if (output.isEmpty()==true)
    				output=INLink.outputpath;
    		}
    		else
    			output=INLink.outputpath;
    	    	
    		dbg ("Post processing "+termCount+" items in: " + output);
    	
    		Path inFile=new Path (output+"/part-r-00000");
    		Path outFile=null;
    		FSDataInputStream in =null;
    		FSDataOutputStream out =null;
    	
    		try 
    		{
    			in=INMain.hdfs.open (inFile);
    			
    			BufferedReader reader=new BufferedReader(new InputStreamReader(in));
    			
    			String thisLine;
    			
    			int count=0;
    			int split=Math.round(termCount/INLink.nrshards);
    			int partition=0;
    			
    			outFile=new Path (output+"/partition-"+partition+"-00000.txt");    					
				out=INMain.hdfs.create (outFile);
    			
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
							out=INMain.hdfs.create (outFile);
    					
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
    	

    		dbg ("Starting rudimentary sharding into " + INLink.nrshards);
    	
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
    	
    	INStatistics stats=new INStatistics ();
    	String results=stats.printStatistics();
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
    	conf.set ("useHadoop",new Boolean (INLink.useHadoop).toString());
    	conf.set ("casefold",new Boolean (INLink.casefold).toString());
    	conf.set ("stopwords",new Boolean (INLink.stopwords).toString());
    	conf.set ("stemming",new Boolean (INLink.stemming).toString());
    	conf.set ("cleanoutput",new Boolean (INLink.cleanoutput).toString());
    	conf.set ("dbglocal",new Boolean (INLink.dbglocal).toString());
    	conf.set ("minstemsize",new Integer (INLink.minstemsize).toString());    	    	
    	conf.set ("splitsize",new Long (INLink.splitsize).toString());
    	conf.set ("nrshards",new Integer (INLink.nrshards).toString());
    	conf.set ("shardtype",INLink.shardtype);    	
    	conf.set ("shardcount",new Integer (INLink.shardcount).toString());
    	conf.set ("shardcreate",new Boolean (INLink.shardcreate).toString());   	
    	conf.set ("task",INLink.task);
    	conf.set ("monitorHost",INLink.monitorHost);    	
    }
    /**
	 *
	 */
    public static void main(String args[]) throws Exception
	{
    	// run the INLink constructor; We need this to have a global settings registry    	
    	@SuppressWarnings("unused")
		INLink link = new INLink(); 
    	
    	dbg ("main ()");
    	
    	showTimeStamp ();
    	
    	/**
    	 * I've taken out the statistics portion since it relies on code that isn't distributed
    	 * The next version will have this solved. I might try the solution in:
    	 * http://stackoverflow.com/questions/7443074/initialize-public-static-variable-in-hadoop-through-arguments
    	 * Although chances are I will switch to using Hoop to collect much better performance and distribution 
    	 * statistics. See INHoop.java for more information
    	 */ 
    	
    	//INStatistics stats=new INStatistics ();
    	
    	INPerformanceMetrics metrics=new INPerformanceMetrics ();
    	metrics.setMarker ("main");
    	INLink.metrics.add(metrics);
    	    	
    	if (parseArgs (args)==false)
    	{
    		usage ();
    		return;
    	}
    	
    	if (INLink.postonly==true)
    	{
    		postOnly ();
    		return;
    	}
    	
    	if (INLink.task.equals ("none")==true)
    	{
    		dbg ("No task defined, please use the commandline option -task <task>");
    		return;
    	}
        
        dbg ("Starting system ...");
        
        INMain driver=new INMain ();
        
        if (INLink.useHadoop==false)
        {
        	dbg ("Starting built-in mapper ...");
        	
        	driver.indexDocuments ();
        }
        else
        {
        	dbg ("Starting hadoop job ...");
        	
        	Configuration conf=new Configuration();
        	
        	// TRANSFER SETTINGS FROM INLink to Configuration!!!
        	
        	transferConf (conf);
        	
        	// Now we're feeling much better
        	
        	INMain.hdfs=FileSystem.get (conf);
        	
        	if (INLink.dbglocal==true)
        	{
        		dbg ("Enabling local debugging ...");
        		conf.set("mapred.job.tracker", "local");
        	}
        	else
        		dbg ("Disabling local debugging");
                 
        	JobConf job=new JobConf (conf,INMain.class);
        	
        	job.setJobName(driver.getClassName());
        	
        	driver.setJob(job);
        	
        	@SuppressWarnings("unused")
        	String[] otherArgs=new GenericOptionsParser(conf, args).getRemainingArgs();
        	        	
        	job.setJarByClass (INMain.class);
        	                 	        	                     	
        	if (INLink.task.equals("invert")==true)
        	{
        		dbg ("Configuring job for invert task ...");
        		
            	job.setReducerClass (INInvertedListReducer.class);
            	job.setMapperClass (INInvertedListMapper.class);
        		job.setMapOutputKeyClass(Text.class);		
        		job.setMapOutputValueClass(Text.class);
        	}
        	
        	if (INLink.task.equals("wordcount")==true)
        	{
        		dbg ("Configuring job for wordcount task ...");
        		
            	job.setReducerClass (INWordCountReducer.class);
            	job.setMapperClass (INWordCountMapper.class);
        		job.setMapOutputKeyClass (Text.class);
        		job.setMapOutputValueClass (IntWritable.class);
        	}	
                        	        	
        	dbg ("Using input path: " + INLink.datapath);
        	dbg ("Using output path: " + INLink.outputpath);
        	                	
        	FileInputFormat.addInputPath (job, new Path (INLink.datapath));
        	FileOutputFormat.setOutputPath (job,new Path (INLink.outputpath));
        	    
            job.setInputFormat (INWholeFileInputFormat.class);
        	
        	if ((INLink.shardcreate.equals("mos")==true) && (INLink.nrshards>1))
        	{        		
        		dbg ("Setting output to sharded output streams class ...");
        		
        		job.setOutputFormat (INShardedOutputFormat.class);
        	}
        	else
            	job.setOutputFormat (TextOutputFormat.class);        		
         
        	/**
        	 * Temporarily commented out for testing purposes
        	 */
        	
        	//job.setPartitionerClass (INPartitioner.class);        	        	
        	
        	driver.register("Main");
        	
            JobClient.runJob(job); 
        	
        	postProcess (conf);
        }
        
        showTimeStamp ();
        
        long timeTaken=metrics.getMarkerRaw ();
        metrics.printMetrics(timeTaken);
        
        driver.unregister ();
        
    	/**
    	 * I've taken out the statistics portion since it relies on code that isn't distributed
    	 * The next version will have this solved. I might try the solution in:
    	 * http://stackoverflow.com/questions/7443074/initialize-public-static-variable-in-hadoop-through-arguments
    	 * Although chances are I will switch to using Hoop to collect much better performance and distribution 
    	 * statistics. See INHoop.java for more information
    	 */                
        //stats.calcStatistics();
        //dbg (stats.printStatistics());
	}
}
