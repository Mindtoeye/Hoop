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
 */

package edu.cmu.cs.in.hadoop;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.lib.MultipleTextOutputFormat;

//import edu.cmu.cs.in.base.INBase;

public class INShardedOutputFormat extends MultipleTextOutputFormat<Text, Text> 
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
    	//INBase.debug ("INShardedOutputFormat","generateFileNameForKeyValue ()");
    	
        INPartitioner partitioner=new INPartitioner ();
        return ("shard-"+partitioner.getPartitionFromKey(key.toString()));
    }
}