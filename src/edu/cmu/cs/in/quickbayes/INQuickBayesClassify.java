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

package edu.cmu.cs.in.quickbayes;

import edu.cmu.cs.in.base.INFeatureMatrixBase;

public class INQuickBayesClassify extends INFeatureMatrixBase
{
	public String featureName="undefined";
	public String attributeName="undefined";
	
	public float value=0;
	public String valueString="-1/-1";
	
	//--------------------------------------------------------------------------------- 
	public INQuickBayesClassify () 
	{
		setClassName ("INQuickBayesClassify");
		debug ("INQuickBayesClassify ()");   
	}
	//---------------------------------------------------------------------------------	
}
