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