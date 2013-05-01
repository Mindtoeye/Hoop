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
//import edu.cmu.cs.in.base.HoopBase;

/** 
 * @author vvelsen
 *
 */
public class HoopWordCountMapper extends HoopHadoopReporter implements Mapper <IntWritable, Text, Text, IntWritable> 
{
	private final static IntWritable one=new IntWritable (1);
	private Text word=new Text();
   	
	/**
	 * 
	 */
	public HoopWordCountMapper ()
	{
		setClassName ("HoopWordCountMapper");
		debug ("HoopWordCountMapper ()");		
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
				
		HoopDocumentParser parser=new HoopDocumentParser ();
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