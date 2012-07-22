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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;

import edu.cmu.cs.in.base.HoopDf;
import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.stats.HoopPerformanceMetrics;
import edu.cmu.cs.in.base.HoopToken;

/** 
 * @author vvelsen
 *
 */
public class HoopInvertedListReducer extends HoopHadoopReporter implements Reducer<Text, Text, Text, Text> 
{ 
	private ArrayList <HoopDf>documents=null;
	private HoopPerformanceMetrics reducerMarker=null;
	
	/**
	 * 
	 */
	public HoopInvertedListReducer ()
	{
		setClassName ("HoopInvertedListReducer");
		debug ("HoopInvertedListReducer ()");
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
	private HoopDf findDocument (String docID)
	{
		for (int i=0;i<documents.size();i++)
		{
			HoopDf test=documents.get(i);
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
		HoopToken token=new HoopToken (docIDWithPos);
		
		HoopDf test=findDocument (token.getValue());
		
		if (test==null)
		{
			test=new HoopDf ();
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
		
		if (HoopLink.metrics!=null)
		{
			reducerMarker=new HoopPerformanceMetrics ();
			reducerMarker.setMarker("Reducer");
			HoopLink.metrics.add(reducerMarker);
		}
		
		/**
		 * We can not write to globally static variable since each node will have its
		 * own VM and therefore the resulting numbers will be bogus. Instead total
		 * term count should be done in a different way for the stats to work
		 */
		
		//HoopLink.termCount++;
		
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
		//HoopBase.debug ("HoopInvertedListReducer","reduce ()");		
				
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
		//HoopBase.debug ("HoopInvertedListReducer","reduce ()");		
		
		StringBuffer tester=new StringBuffer ();		
		Long count = (long) 0;
		documents=new ArrayList<HoopDf> ();
								
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
			HoopDf doc=documents.get(i);
			
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
