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

public class INCSVReader extends INFeatureMatrixBase 
{
	public INQuickBayesData dataGrid=null;
	public String mode="TAB"; // TAB,COMMA,DASH
	
	/**
	 *
	 */ 
	public INCSVReader () 
	{
		super ();
		
		setClassName ("INCSVReader");
		debug ("INCSVReader ()"); 		
	}
	/**
	 *
	 */
	public void addFeature (String a_feature)
	{
		debug ("Adding feature: " + a_feature);
		INQuickBayesFeature feature=new INQuickBayesFeature ();
		feature.featureName=a_feature.toLowerCase ();
		dataGrid.features.add (feature);
	}
	/**
	 *
	 */
	public void addFeatureRaw (String a_feature)
	{
		String parsed[]=a_feature.split(" ");
		if (parsed.length>1)
			addFeature (parsed [1]);
	}
	/**
	 *
	 */
	public void addInstanceStringTab (String entries[],int rowCounter)
	{
		debug ("addInstanceStringTab ()");
				
		ArrayList<String> row=new ArrayList <String> ();
		
		for (int i=0;i<entries.length;i++)
		{
			debug ("Adding instance: " + entries [i]);
			row.add (entries [i]);
		}
		
		dataGrid.data.add (row);
	}	
	/**
	 *
	 */
	public void addInstanceString (String a_row,int rowCounter)
	{
		debug ("Adding instance string: " + a_row);
		String instances[]=a_row.split(",");
		
		ArrayList<String> row=new ArrayList <String> ();
		
		for (int i=0;i<instances.length;i++)
		{
			debug ("Adding (selected) instance: " + instances [i]);
			row.add(instances [i]);	
		}
		
		dataGrid.data.add(row);
	}	
	/**
	 *
	 */
	public boolean processInputTab (String a_file,INQuickBayesData a_grid)
	{
		debug ("processInput ()");
		
		dataGrid=a_grid;
								
		debug ("Processing data ...");
		
		String split[]=a_file.toString ().split("\\n");
				
		int index=0;
		
		for (int i=0;i<split.length;i++)
		{
			//debug ("Looking at line: " + i + ": "+ split [i]);
					
			String entries[]=split [i].split("\\t");
		 
			if (index==0)
			{
				for (int j=0;j<entries.length;j++)
				{
					addFeature (entries [j]);
				}
			}
			else
			{
				addInstanceStringTab (entries,index);
			}
					 
			index++;
		}
		
		return (true);
	}	
	/**
	 * This method is for now exclusively dedicated to loading a file of a specific format
	 * that has the features Date, Text and Class. Please use processInputTab for any other
	 * input loading and processing.
	 */
	public boolean processInput (String a_file,INQuickBayesData a_grid)
	{
		debug ("processInput ()");
		
		dataGrid=a_grid;
				
		addFeature ("Date");
		addFeature ("Text");
		addFeature ("Class");		
				
		debug ("Processing data ...");
		
		String split[]=a_file.toString ().split("\\n");
				
		int index=0;
		
		for (int i=0;i<split.length;i++)
		{
		 String entries[]=split [i].split("\\t");
		 
		 addInstanceString (entries [1]+",undefined,"+entries [8],index);
		 
		 index++;
		}
		
		return (true);
	}	
	//---------------------------------------------------------------------------------
}
