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

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;

/**
 * Here we have in a single class both the Hadoop parition driver as well as
 * the class that generates the partition ID based on some kind of splitting
 * preference. Currently you can have partitions assigned based on the first
 * character of the key (alphabetical), the range of the document id (docid) 
 * or the size of the document (docsize). Please see the comments in each of 
 * the specific splitting methods to find out how partitions are assigned 
 * based on the preferences.
 * 
 * Please keep in mind that currently more classes are in development to use 
 * the partitioning better and to make the Reducers more aware of which 
 * partition they're part of. For more information see this example:
 * 
 * http://www.riccomini.name/Topics/DistributedComputing/Hadoop/SortByValue/
 */
public class HoopPartitioner extends HoopHadoopReporter implements Partitioner <Text, Text> 
{    
	/// One of: docid|alphabetical|docsize
	private String partitionType="alhpabetical";
	/// Number of documents in dataset, configurable from command line options
	private long docMax=60000;
	private int nrPartitions=5;
	private JobConf conf=null;
	
	/**
	 * 
	 */
	public HoopPartitioner ()
	{
		setClassName ("HoopPartitioner");
		//debug ("HoopPartitioner ()");
	}
	/**
	 *
	 */    
	public int getNrPartitions() 
	{
		return nrPartitions;
	}	
	/**
	 *
	 */    
	public long getDocMax() 
	{
		return docMax;
	}
	/**
	 *
	 */	
	public void setDocMax(long docMax) 
	{
		this.docMax = docMax;
	}    
	/**
	 *
	 */	
	public String getPartitionType() 
	{
		return partitionType;
	}
	/**
	 * Use this method if you want to retrieve the original key. This method
	 * works regardless if the key is encoded or not. Keys can come in the
	 * form of:
	 *  
	 *   apple:2,4
	 *   
	 * Where the item after the colon is the partition ID and the
	 * item after the comma is the term count
	 */
	public String getOriginalKey (String formattedKey)
	{
		if (formattedKey.indexOf(':')!=-1)
		{		
			String [] temp=formattedKey.split(":");
					
			if (temp.length>1)
			{
				return (temp [0]);
			}
		}
		return (formattedKey);
	}
	/**
	 * We will get keys in the form of: 
	 * apple:2,4
	 * Where the item after the colon is the partition ID and the
	 * item after the comma is the term count
	 */
	public String getPartitionFromKey (String formattedKey)
	{
		//debug ("getPartitionFromKey ("+formattedKey+")");
		
		if (formattedKey.indexOf(':')!=-1)
		{		
			//debug ("The key does have a colon!");
			
			String [] temp=formattedKey.split(":");
					
			if (temp.length>1)
			{								
				String partial=temp [1];
				
				//debug ("Examining: " +partial);
				
				if (partial.indexOf(',')!=-1)
				{
					String [] almost=partial.split(",");
					
					//debug ("We now have: " + almost [0]);
					
					return (almost [0]);
				}
				
				return (temp [1]);
			}
		}
		
		return (formattedKey);
	}		
	/**
	 *
	 */	
	public void setPartitionType(String partitionType) 
	{
		this.partitionType = partitionType;
	}       
	/**
	 *
	 */    
	public void configure(JobConf aConf) 
	{ 
		debug ("configure ()");
		
		conf=aConf;
		
		partitionType=conf.getRaw("shardtype");
		docMax=Long.parseLong(conf.getRaw ("splitsize"));
		nrPartitions=Integer.parseInt(conf.getRaw ("nrshards"));
		
		debug ("Partitioning using "+partitionType+" with " + docMax + " items over " + nrPartitions);
	}	
	/**
	 * Return a partition based on the amount of documents. For example
	 * the 60K document set with 5 partitions would give you:
	 * 00,000-12,000 - Partition 1
	 * 12,001-24,000 - Partition 2
	 * 24,001-36,000 - Partition 3
	 * 36,001-48,000 - Partition 4
	 * 48,001-60,000 - Partition 5
	 */
	private int getPartitionDocID(Text key, Text value, int numPartitions) 
	{	
		//debug ("getPartitionDocID ()");
				
		long splitSize=Math.round(docMax/nrPartitions);
		
		//debug ("Split size: " + splitSize);
		
		String raw=key.toString();
		
		if (raw.indexOf(':')!=-1)
		{		
			String [] temp=raw.split(":");
					
			if (temp.length>1)
			{
				long docId=Integer.parseInt(temp [1]);
				
				long count=0;
				
				for (int i=0;i<nrPartitions;i++)
				{
					if((docId>count) && (docId<(count+splitSize)))
					{
						//debug ("Determined key to fall in partition: " + i);
						return (i);
					}
					
					count+=splitSize;
				}
			}
		}			
				
		return (0);
	}
	/**
	 * Return a partition based on the size of the document. This could
	 * be useful for certain types of storage requirements. Keep in mind
	 * that you will need a distribution of sizes and heuristics on the
	 * best partitioning to make this work
	 * 000,000-001,024 bytes - Partition 1 (1K)
	 * 001,025-004,048 bytes - Partition 2 (4K)
	 * 004,049-010,000 bytes - Partition 3 (10K)
	 * 010,001-050,000 bytes - Partition 4 (50k)
	 * 050,001-100,000 bytes - Partition 5 (Larger than 50k)
	 */
	private int getPartitionDocSize(Text key, Text value, int numPartitions) 
	{	
		//debug ("getPartitionDocID ()");
				
		long splitSize=Math.round(docMax/nrPartitions);
		
		//debug ("Split size: " + splitSize);
		
		String raw=key.toString();
		
		if (raw.indexOf(':')!=-1)
		{		
			String [] temp=raw.split(":");
					
			if (temp.length>1)
			{
				long docId=Integer.parseInt(temp [1]);
				
				long count=0;
				
				for (int i=0;i<nrPartitions;i++)
				{
					if((docId>count) && (docId<(count+splitSize)))
					{
						//debug ("Determined key to fall in partition: " + i);
						return (i);
					}
					
					count+=splitSize;
				}
			}
		}			
				
		return (0);
	}	
	/**
	 * Return a partition based on the first character in the key, with
	 * the following distribution:
	 * a b c d e - Partition 1 
	 * f g h i j - Partition 2 
	 * k l m n o - Partition 3
	 * p q r s t - Partition 4 
	 * u v w x y - Partition 5 
	 * z (other) - Partition 1
	 */		
	private int getPartitionAlphabetical(Text key, Text value, int numPartitions) 
	{	
		String keyString=key.toString().toLowerCase();
		
		int partition=0;
		
		// Let's partition by the first letter of the key.
		
		if (keyString.length()==0)
		{
			//debug ("Error: key found of 0 length!");
			return (0);
		}
		
		if (keyString.substring(0,1).matches("[a-e]")==true)
		{
			//debug (keyString+":1 out of: " +numPartitions);
			partition=0;
		}
		
		if (keyString.substring(0,1).matches("[f-j]")==true)
		{
			//debug (keyString+":2 out of: " +numPartitions);			
			partition=1;
		}
		
		if (keyString.substring(0,1).matches("[k-o]")==true)
		{
			//debug (keyString+":3 out of: " +numPartitions);			
			partition=2;
		}		
		
		if (keyString.substring(0,1).matches("[p-t]")==true)
		{
			//debug (keyString+":4 out of: " +numPartitions);			
			partition=3;
		}		
		
		if (keyString.substring(0,1).matches("[u-y]")==true)
		{
			//debug (keyString+":5 out of: " +numPartitions);			
			partition=4;
		}		
		
		if (partition>(numPartitions-1))
			return 0;

		return (partition);		
	}	
	/**
	 * The main entry point for Hadoop. It is called at the appropriate
	 * time by Hadoop to figure out which partition (set of nodes) the
	 * data should go to. Please keep in mind that currently more
	 * classes are in development to use the partitioning better and
	 * to make the Reducers more aware of which partition they're
	 * part of. For more information see this example:
	 * http://www.riccomini.name/Topics/DistributedComputing/Hadoop/SortByValue/
	 */	
	@Override
	public int getPartition(Text key, Text value, int numPartitions) 
	{
		if (getPartitionType().equals("docid")==true)
			return (getPartitionDocID (key,value,numPartitions));
		
		if (getPartitionType().equals("alhpabetical")==true)
			return (getPartitionAlphabetical (key,value,numPartitions));
		
		if (getPartitionType().equals("docsize")==true)
			return (getPartitionDocSize (key,value,numPartitions));		
		
		return (0);
	}   
}
