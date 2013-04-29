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
import java.util.Iterator;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;

/** 
 * @author vvelsen
 *
 */

public class HoopWordCountReducer extends HoopHadoopReporter implements Reducer<Text, IntWritable, Text, IntWritable> 
{ 
	/**
	 * 
	 */
	public HoopWordCountReducer ()
	{
		setClassName ("HoopWordCountReducer");
		debug ("HoopWordCountReducer ()");	
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
		//HoopBase.debug ("HoopReducer","reduce ()");		
		
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



