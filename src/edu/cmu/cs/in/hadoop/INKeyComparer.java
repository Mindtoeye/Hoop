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

import org.apache.hadoop.io.RawComparator;
import org.apache.hadoop.io.Text;

import edu.cmu.cs.in.base.INFeatureMatrixBase;

/**
* Future class to work with the partitioner
*/
public class INKeyComparer extends INFeatureMatrixBase implements RawComparator<Text> 
{
	/**
	 *
	 */
	public int compare (byte[] text1, int start1, int length1, byte[] text2, int start2, int length2) 
	{
		// look at first character of each text byte array
		return new Character((char)text1[0]).compareTo((char)text2[0]);
	}
	/**
	 *
	 */
	public int compare(Text o1, Text o2) 
	{
		return compare(o1.getBytes(), 0, o1.getLength(), o2.getBytes(), 0, o2.getLength());
	}
}