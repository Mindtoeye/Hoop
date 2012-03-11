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
//import edu.cmu.cs.in.base.INBase;

/** 
 * @author vvelsen
 *
 */
public class INWordCountMapper extends INHadoopReporter implements Mapper <IntWritable, Text, Text, IntWritable> 
{
	private final static IntWritable one=new IntWritable (1);
	private Text word=new Text();
   	
	/**
	 * 
	 */
	public INWordCountMapper ()
	{
		setClassName ("INWordCountMapper");
		debug ("INWordCountMapper ()");		
	}	
	/**
	 * 
	 */	
	@Override
	public void configure(JobConf aJob) 
	{
		debug ("configure ()");
		
		this.setJob(aJob);
		
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
	public void map (IntWritable key, 
					 Text value,
					 OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException 
	{
		debug ("map ()");
						
		String line = value.toString(); // We assume here we're getting one file at a time
		
		//debug (line);
				
		INDocumentParser parser=new INDocumentParser ();
		parser.loadDocumentFromData(line);
		
		List<String> tokens=parser.getTokens();
		
		for (int i=0;i<tokens.size();i++)
		{
			String token=tokens.get(i);
						
			word.set(token);
			//context.write (word,one);
			output.collect(word,one);
		}    			
		
		debug ("map ("+tokens.size()+" tokens) done");
	}
} 