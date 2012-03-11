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

import edu.cmu.cs.in.quickbayes.INQuickBayesFileBase;

public class INQuickBayesCSVWriter extends INQuickBayesFileBase 
{
	//--------------------------------------------------------------------------------- 
	public INQuickBayesCSVWriter () 
	{
		super ();
		
		setClassName ("INQuickBayesCSVWriter");
		debug ("INQuickBayesCSVWriter ()");		
	}
	//---------------------------------------------------------------------------------
	public void processOutput (String a_file,INQuickBayesData data)
	{
	 StringBuffer formatted=new StringBuffer ();
	 
	 for (int i=0;i<data.features.size();i++)
	 {
		 if (i!=0)
			 formatted.append ("\t"); 
		 		 
		 formatted.append(data.features.get(i).featureName);
	 }
	 
	 formatted.append("\n");
	 
	 for (int j=0;j<data.data.size();j++)
	 {
		 if (j!=0)
			 formatted.append("\n");
		 
		 ArrayList<String> temp=data.data.get (j);
		 
		 for (int k=0;k<temp.size();k++)
		 {
			 if (k!=0)
				 formatted.append("\t");
			 
			 formatted.append(temp.get (k));
		 }		 
	 }	 
	 
	 saveAs (a_file,formatted);
	}
	//---------------------------------------------------------------------------------
}
