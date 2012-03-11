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

import edu.cmu.cs.in.base.INBase;

public class INQuickBayesInstance extends INBase
{
	public String instanceName="undefined";
	public int countClassifier=0;	
	public int instanceCount  =0;
			
	//--------------------------------------------------------------------------------- 
	public INQuickBayesInstance () 
	{
		setClassName ("INQuickBayesInstance");
		debug ("INQuickBayesInstance ()");		
	}
	//---------------------------------------------------------------------------------
	public String getLikelihoodString ()
	{
		StringBuffer likelihood=new StringBuffer ();
		
		if (instanceCount==0)
			return ("0");
		
		likelihood.append(countClassifier);
		likelihood.append ("/");
		likelihood.append(instanceCount);
		
		return (likelihood.toString());
	}
	//---------------------------------------------------------------------------------
	public float getLikelihood ()
	{		
		if (instanceCount==0)
			return (0);
		
		return ((float) countClassifier / (float) instanceCount);
	}	
	//---------------------------------------------------------------------------------	
}
