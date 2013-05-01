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

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.lib.MultipleTextOutputFormat;

import edu.cmu.cs.in.base.HoopRoot;

public class HoopShardedOutputFormat extends MultipleTextOutputFormat<Text, Text> 
{
	/**
	 * We will get keys in the form of: 
	 * apple:2,4
	 * Where the item after the colon is the partition ID and the
	 * item after the comma is the term count
	 */
    @Override
    protected String generateFileNameForKeyValue(Text key, Text value,String name) 
    {        
    	//HoopBase.debug ("HoopShardedOutputFormat","generateFileNameForKeyValue ()");
    	
        HoopPartitioner partitioner=new HoopPartitioner ();
        return ("shard-"+partitioner.getPartitionFromKey(key.toString()));
    	
    	//HoopBase.debug ("HoopShardedOutputFormat","Shard name: " + key.toString());    	
        //return (key.toString());
    }
}