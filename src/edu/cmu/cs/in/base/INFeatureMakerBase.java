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

package edu.cmu.cs.in.base;

//import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
*
*/
public class INFeatureMakerBase extends INBase implements INFeatureMaker
{	
	private String [] splitter=null;
	
	/**
	 *
	 */
	public INFeatureMakerBase () 
	{
		setClassName ("INFeatureMakerBase");
		//debug ("INFeatureMakerBase ()");
	}
	/**
	 *
	 */	
	public List<String> unigramTokenize (String aSource)
	{				
		//debug ("unigramTokenize (String)");
		
		splitter=aSource.split("\\s+");
				
		return Arrays.asList(splitter);		
	}	
	/**
	 *
	 */	
	public List<String> unigramTokenize (String aSource, int n)
	{
		//debug ("unigramTokenize (String,int)");
		
		splitter=aSource.split("\\s+");
		
		return Arrays.asList(splitter);		
	}	
}

