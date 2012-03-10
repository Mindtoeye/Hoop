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
import java.util.Iterator;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;

/** 
 * @author vvelsen
 *
 */

public class INWordCountReducer extends INHadoopReporter implements Reducer<Text, IntWritable, Text, IntWritable> 
{ 
	/**
	 * 
	 */
	public INWordCountReducer ()
	{
		setClassName ("INWordCountReducer");
		debug ("INWordCountReducer ()");	
	}	
	/**
	 * 
	 */
	@Override
	public void configure(JobConf aJob) 
	{
		debug ("configure ()");
		
		this.setJob(aJob);		
		
		register ("Reducer");
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
	 * Text key: In this case every key is a unique term from our documents 
	 * Iterable<IntWritable> values: Is a list of values, in this case 1,1,1,1,1,1
	 */
	public void reduce (Text key, 
			Iterator<IntWritable> values,
			OutputCollector<Text, IntWritable> 
			output, Reporter reporter) throws IOException 
	{
		//INFeatureMatrixBase.debug ("INReducer","reduce ()");		
		
		int sum = 0;
			
		// We're not actually getting a list, instead we get an iterator, so we can't
		// just take the size of a list, we have to go through the whole thing
				
		//for (IntWritable val : values) 
		while (values.hasNext())
		{
			//sum += val.get();
			sum += values.next().get();
		}
			
		//context.write (key,new IntWritable (sum));
		output.collect (key,new IntWritable (sum));
	}
}	



