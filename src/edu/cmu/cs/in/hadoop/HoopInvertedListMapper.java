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

import edu.cmu.cs.in.HoopDocumentParser;
import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.stats.HoopPerformanceMetrics;
import edu.cmu.cs.in.base.HoopToken;

/** 
 * @author vvelsen
 *
 */
public class HoopInvertedListMapper extends HoopHadoopReporter implements Mapper <LongWritable, Text, Text, Text> 
{
	//private final static LongWritable one=new LongWritable (1);
	private Text word=new Text();
	private HoopPerformanceMetrics mapperMarker=null;
	private HoopPartitioner partitioner=null;
		
	/**
	 * 
	 */
	public HoopInvertedListMapper ()
	{
		setClassName ("HoopInvertedListMapper");
		debug ("HoopInvertedListMapper ()");			

		partitioner=new HoopPartitioner ();
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
				
		if (HoopLink.metrics!=null)
		{
			mapperMarker=new HoopPerformanceMetrics ();
			mapperMarker.setMarker("Mapper");
			HoopLink.metrics.add(mapperMarker);
		}
		
		if (value==null)
		{
			debug ("Internal error: value is null");
			return;
		}
		
		String line = value.toString(); // We assume here we're getting one file at a time
								
		HoopDocumentParser parser=new HoopDocumentParser ();
		//parser.setDocID(key.toString());
		parser.setKey(key.get());
		parser.setIncludePositions(true);
		parser.loadDocumentFromData(line); // Tokenization happens here
		
		List<String> tokens=parser.getTokens();
		
		for (int i=0;i<tokens.size();i++)
		{
			HoopToken token=new HoopToken (tokens.get(i));
			
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
			//mapperMarker.getMarkerRaw ();
			mapperMarker.closeMarker();
		}		
	}
} 