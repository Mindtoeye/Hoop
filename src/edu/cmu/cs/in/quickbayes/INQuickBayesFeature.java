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

import java.util.ArrayList;

import edu.cmu.cs.in.base.INFeatureMatrixBase;

public class INQuickBayesFeature extends INFeatureMatrixBase
{
	public boolean majorityAttribute		=false;
	public String featureName				="undefined";
	public ArrayList <INQuickBayesAttribute> instances=null;
	
	//--------------------------------------------------------------------------------- 
	public INQuickBayesFeature () 
	{
		setClassName ("INQuickBayesFeature");
		debug ("INQuickBayesFeature ()");
		
		instances=new ArrayList<INQuickBayesAttribute> ();
	}
	//---------------------------------------------------------------------------------
	public INQuickBayesAttribute findAttribute (String an_instance)
	{
		for (int i=0;i<instances.size();i++)
		{
		 INQuickBayesAttribute tester=instances.get (i);
		 
		 //debug ("Comparing inst: " + tester.instanceName + " with: " + an_instance.toLowerCase());
		 
		 if (tester.instanceName.equals(an_instance.toLowerCase ())==true)
		 {			
			 return (tester);
		 }
		}
		
		//debug ("Inst: " + an_instance + " not found");
		return (null);
	}	
	//---------------------------------------------------------------------------------
	public boolean addAttribute (String an_instance)
	{
		INQuickBayesAttribute tester=findAttribute (an_instance);

		if (tester!=null)
		{
			//tester.countClassifier++;
			tester.instanceCount++;
			return (false);
		}
		
		tester=new INQuickBayesAttribute ();
		tester.instanceName=an_instance.toLowerCase ();
		//tester.countClassifier=1;
		tester.instanceCount=1;
		instances.add(tester);
		
		return (true);
	}
	//---------------------------------------------------------------------------------	
}
