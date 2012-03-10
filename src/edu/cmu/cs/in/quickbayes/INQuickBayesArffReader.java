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

import java.io.IOException;
import java.util.ArrayList;

import edu.cmu.cs.in.quickbayes.INQuickBayesFileBase;

public class INQuickBayesArffReader extends INQuickBayesFileBase
{
	INQuickBayesData dataGrid=null;
	
	/**
	*
	*/	
	public INQuickBayesArffReader () 
	{
		super ();
		
		setClassName ("INQuickBayesArffReader");
		debug ("INQuickBayesArffReader ()");		
	}
	//---------------------------------------------------------------------------------
	public void addFeature (String a_feature)
	{
		//debug ("Adding feature: " + a_feature);
		INQuickBayesFeature feature=new INQuickBayesFeature ();
		feature.featureName=a_feature.toLowerCase ();
		dataGrid.features.add (feature);
	}
	//---------------------------------------------------------------------------------
	public void addFeatureRaw (String a_feature)
	{
		String parsed[]=a_feature.split(" ");
		if (parsed.length>1)
			addFeature (parsed [1]);
	}
	//---------------------------------------------------------------------------------
	public void addInstanceString (String a_row,int rowCounter)
	{
		//debug ("Adding instance string: " + a_row);
		String instances[]=a_row.split(",");
		
		ArrayList<String> row=new ArrayList <String> ();
		
		for (int i=0;i<instances.length;i++)
		{
			//debug ("Adding instance: " + instances [i]);
			row.add(instances [i]);
		}
		
		dataGrid.data.add(row);
	}
	//---------------------------------------------------------------------------------
	public boolean processInput (String a_file,INQuickBayesData a_grid)
	{
		dataGrid=a_grid;
		
		StringBuffer raw=null;
		
		try 
		{
			raw=load (a_file);
		} catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return (false);
		}
		
		String split[]=raw.toString ().split("\\n");
		
		boolean markData=false;
		dataGrid.nrRows=0;
		
		for (int i=0;i<split.length;i++)
		{
			if (markData==false)
			{
				if (split [i].indexOf("@attribute")!=-1)
					addFeatureRaw (split [i]);
			
				if (split [i].indexOf("@data")!=-1)
				{
					markData=true;
					dataGrid.nrFeatures=dataGrid.features.size();
				}
			}
			else
			{
				addInstanceString (split [i],dataGrid.nrRows);
				dataGrid.nrRows++;
			}
		}
		
		return (true);
	}
	//---------------------------------------------------------------------------------	
}


