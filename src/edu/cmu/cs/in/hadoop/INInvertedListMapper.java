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

package edu.cmu.cs.in.hadoop;

import java.io.IOException;
import java.util.List;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;

import edu.cmu.cs.in.INDocumentParser;
import edu.cmu.cs.in.base.INLink;
import edu.cmu.cs.in.stats.INPerformanceMetrics;
import edu.cmu.cs.in.base.INToken;

/** 
 * @author vvelsen
 *
 */
public class INInvertedListMapper extends INHadoopReporter implements Mapper <LongWritable, Text, Text, Text> 
{
	//private final static LongWritable one=new LongWritable (1);
	private Text word=new Text();
	private INPerformanceMetrics mapperMarker=null;
	private INPartitioner partitioner=null;
		
	/**
	 * 
	 */
	public INInvertedListMapper ()
	{
		setClassName ("INInvertedListMapper");
		debug ("INInvertedListMapper ()");			

		partitioner=new INPartitioner ();
	}
	/**
	 * 
	 */	
	@Override
	public void configure(JobConf aJob) 
	{
		debug ("configure ()");
		
		this.setJob(aJob);
		
		if (partitioner!=null)
			partitioner.configure(aJob); // Just do it manually
		
		register ("Mapper");
	}
	/**
	 * 
	 */	
	@Override
	public void close() throws IOException 
	{
		unregister ();		
	}	
	/**
	 * 
	 */
	public void map (LongWritable key, 
					 Text value, 
					 OutputCollector<Text, Text> output, 
					 Reporter reporter)	throws IOException 
	{
		debug ("map ()");
				
		if (INLink.metrics!=null)
		{
			mapperMarker=new INPerformanceMetrics ();
			mapperMarker.setMarker("Mapper");
			INLink.metrics.add(mapperMarker);
		}
		
		if (value==null)
		{
			debug ("Internal error: value is null");
			return;
		}
		
		String line = value.toString(); // We assume here we're getting one file at a time
								
		INDocumentParser parser=new INDocumentParser ();
		parser.setDocID(key.toString());
		parser.setIncludePositions(true);
		parser.loadDocumentFromData(line); // Tokenization happens here
		
		List<String> tokens=parser.getTokens();
		
		for (int i=0;i<tokens.size();i++)
		{
			INToken token=new INToken (tokens.get(i));
			
			StringBuffer formatted=new StringBuffer ();
			formatted.append(key.get ());
			formatted.append (":");
			formatted.append (token.getPosition().toString());
												
			//word.set(token.getValue()+":"+key.toString()); // We need this for the partitioner and reducers
			word.set(token.getValue()+":"+partitioner.getPartition(new Text ("key:"+key.toString()),new Text ("undef"),partitioner.getNrPartitions())); // We need this for the partitioner and reducers

			output.collect (word,new Text (formatted.toString()));
		}    			
		
		debug ("map ("+tokens.size()+" tokens) done for key: " + key.toString());
		
		if (mapperMarker!=null)
		{
			mapperMarker.getMarkerRaw ();
		}		
	}
} 