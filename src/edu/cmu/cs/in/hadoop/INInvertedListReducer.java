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
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;

import edu.cmu.cs.in.base.INDf;
import edu.cmu.cs.in.base.INLink;
import edu.cmu.cs.in.stats.INPerformanceMetrics;
import edu.cmu.cs.in.base.INToken;

/** 
 * @author vvelsen
 *
 */
public class INInvertedListReducer extends INHadoopReporter implements Reducer<Text, Text, Text, Text> 
{ 
	private ArrayList <INDf>documents=null;
	private INPerformanceMetrics reducerMarker=null;
	
	/**
	 * 
	 */
	public INInvertedListReducer ()
	{
		setClassName ("INInvertedListReducer");
		debug ("INInvertedListReducer ()");
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
	@Override
	public void close() throws IOException 
	{
		unregister ();				
	}	
	/**
	 *
	 */
	private INDf findDocument (String docID)
	{
		for (int i=0;i<documents.size();i++)
		{
			INDf test=documents.get(i);
			if (test.getDocID().equals(docID)==true)
			{
				return (test);
			}
		}
		
		return (null);
	}
	/**
	 *
	 */
	private void incDf (String docIDWithPos)
	{
		INToken token=new INToken (docIDWithPos);
		
		INDf test=findDocument (token.getValue());
		
		if (test==null)
		{
			test=new INDf ();
			test.setDocID(token.getValue());
			documents.add(test);
		}
		
		test.addPosition(token.getPosition());
		test.incDf();
	}	
	/**
	 * I've taken out the following code since it does not run on version 0.20
	 * of Hadoop. The code was developed under Hadoop 1.0.0
	 */
	/*
	public void configure(JobConf conf) 
	{
		 mos = new MultipleOutputs(conf);
	}
	*/
	/**
	 * I've taken out the following code since it does not run on version 0.20
	 * of Hadoop. The code was developed under Hadoop 1.0.0
	 */	
	/*
	public void cleanup(Context aContext) throws IOException 
	{
		if (mos!=null)
			mos.close();	
	}
	*/	 
	/**
	 * Text key: In this case every key is a unique term from our documents 
	 * Iterable<IntWritable> values: Is a list of values, in this case 1,1,1,1,1,1
	 */
	public void reduce (Text key, 
						Iterator<Text> values,	
						OutputCollector<Text, Text>	output, 
						Reporter reporter) throws IOException
	{
		if (key.toString().length()==0)
		{
			debug ("Error: key of length 0 found!");
			return;
		}
		
		if (INLink.metrics!=null)
		{
			reducerMarker=new INPerformanceMetrics ();
			reducerMarker.setMarker("Reducer");
			INLink.metrics.add(reducerMarker);
		}
		
		/**
		 * We can not write to globally static variable since each node will have its
		 * own VM and therefore the resulting numbers will be bogus. Instead total
		 * term count should be done in a different way for the stats to work
		 */
		
		//INLink.termCount++;
		
		try 
		{
			reduceFull (key,values,output);
		} 
		catch (InterruptedException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (reducerMarker!=null)
		{
			reducerMarker.getMarkerRaw ();
		}		
	}
	/**
	 * Text key: In this case every key is a unique term from our documents 
	 * Iterable<IntWritable> values: Is a list of values, in this case 1,1,1,1,1,1
	 */
	public void reduceDummy (Text key, 
							 Iterator<Text> values,
							 OutputCollector<Text, Text> output) throws IOException, InterruptedException 
	{
		//INFeatureMatrixBase.debug ("INInvertedListReducer","reduce ()");		
				
		StringBuffer tester=new StringBuffer ();		
		Long count = (long) 0;
										
		//for (Text valB : values)
		while (values.hasNext())
		{				
			if (count>0)
				tester.append(",");
			
			//tester.append(valB);
			tester.append(values.next().toString());
				
			count++;				
		}			
														
		output.collect (new Text (key.toString()+":"+count),new Text (tester.toString()));				
	}
	/**
	 * Text key: In this case every key is a unique term from our documents 
	 * Iterable<IntWritable> values: Is a list of values, in this case 1,1,1,1,1,1
	 */
	public void reduceFull (Text key, 
							Iterator<Text> values,
							OutputCollector<Text, Text>	output) throws IOException, InterruptedException 
	{
		//INFeatureMatrixBase.debug ("INInvertedListReducer","reduce ()");		
		
		StringBuffer tester=new StringBuffer ();		
		Long count = (long) 0;
		documents=new ArrayList<INDf> ();
								
		// First create a list of hash tables so that we can get term counts per term
		// this table should also store the positions within the document
		
		while (values.hasNext())
		{	
			StringBuffer whoknows=new StringBuffer ();
			whoknows.append(values.next().toString());
			
			incDf (whoknows.toString());
				
			count++;
		}			
				
		// We now have the tf for all the terms, but we need to count the terms for
		// each document
		
		for (int i=0;i<documents.size();i++)
		{
			INDf doc=documents.get(i);
			
			if (i>0)
				tester.append(",");
			
			tester.append(doc.getDocID()+","+doc.getDf());
			
			ArrayList<Integer> positions=doc.getPositions();
			
			tester.append(",");
			
			for (int t=0;t<positions.size();t++)
			{
				Integer pos=positions.get(t);
				
				if (t>0)
					tester.append(" ");
				
				tester.append(pos.toString());
			}			
		}
										
		output.collect (new Text (key.toString()+","+count),new Text (tester.toString()));
	}
}
