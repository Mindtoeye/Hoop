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

package edu.cmu.cs.in.search;

import java.util.ArrayList;

import edu.cmu.cs.in.search.INDataSet;
import edu.cmu.cs.in.search.INDocument;
import edu.cmu.cs.in.base.INBase;
import edu.cmu.cs.in.base.INLink;
import edu.cmu.cs.in.search.INTextSearch;
import edu.cmu.cs.in.stats.INStatsBase;

/** 
 * @author vvelsen
 *
 * A simply class that takes a set of documents as found in the registry object
 * INLink.dataset and dumps out a file that can be uploaded to trec_eval
 */
public class INTrecEval extends INStatsBase
{    	
	private String outputPath=".";
	private String report="";
	private Boolean collate=true;
	
	/**
	 *
	 */
    public INTrecEval (Boolean aCollate) 
    {
		setClassName ("INTrecEval");
		debug ("INTrecEval ()");		
		
		collate=aCollate;
    }    
    /**
     * 
     */
	public void setOutputPath(String outputPath) 
	{
		this.outputPath = outputPath;
	}
    /**
     * 
     */
	public String getOutputPath() 
	{
		return outputPath;
	}
	/**
	 * 
	 */
	public String flushData ()
	{
		if (collate==true)
		{
			return (flushDataCollated ());
		}
	
		
		return (flushDataRegular ());
	}
	/**
	 * 
	 */
	public static double Round(double Rval, int Rpl) 
	{
		double p = (double)Math.pow(10,Rpl);
		Rval = Rval * p;
		double tmp = Math.round(Rval);
		return (double)tmp/p;
	}	
	/**
	 * 
	 */
	public String flushDataRegular ()
	{
		debug ("flushDataRegular ()");
	
		if (INLink.searchHistory==null)
		{
			debug ("Error: no searches completed yet");
			return (report);
		}
		
		if (INLink.fManager==null)
		{
			debug ("Error: no file manager available");
			return (report);
		}
		
		if (outputPath.isEmpty()==true)
		{
			debug ("Error: no output path specified");
			return (report);
		}
				
		StringBuffer overview=new StringBuffer ();
		
		for (int i=0;i<INLink.searchHistory.size();i++)
		{
			INTextSearch aSearch=INLink.searchHistory.get(i);			
			//INQueryOperator query=aSearch.getRootQueryOperator();
			INDataSet docs=INLink.dataSet;
											
			if (docs!=null)
			{
				ArrayList<INDocument> docList=docs.getDocuments();
				
				overview.append("Report ("+i+") generated for " + docList.size() + " documents\n");
				
				if (docList!=null)
				{
					StringBuffer formatted=new StringBuffer ();
					
					formatted.append("QueryID");
					formatted.append("\t");
					formatted.append("Q0");
					formatted.append("\t");
					formatted.append("DocID");
					formatted.append("\t");
					formatted.append("Rank");
					formatted.append("\t");
					formatted.append("Score");
					formatted.append("\t");
					formatted.append("RunID");
					formatted.append("\n");
					
					for (int j=0;j<docList.size();j++)
					{
						INDocument aDoc=docList.get(j);
						
						formatted.append(aSearch.getInstanceName());
						formatted.append("\t");
						formatted.append("Q0");
						formatted.append("\t");
						formatted.append(aDoc.getDocID());
						formatted.append("\t");
						formatted.append(aDoc.getRank()+1); // trec_eval starts at 1
						formatted.append("\t");
						formatted.append(String.format("%.2f",aDoc.getScore()));
						formatted.append("\t");
						formatted.append("run-"+j);
						formatted.append("\n");
					}
					
					INLink.fManager.saveContents(outputPath+"/"+"TrecEval-Log-Exp"+INLink.experimentNr+"-Query"+i+"-"+INBase.generateFileTimestamp ()+".txt", formatted.toString());
				}	
			}
			else
				debug ("Internal error: query object is null");
		}
				
		return (overview.toString());
	}
	/**
	 * 
	 */
	public String flushDataCollated ()
	{
		debug ("flushDataCollated ()");
	
		if (INLink.searchHistory==null)
		{
			debug ("Error: no searches completed yet");
			return (report);
		}
		
		if (INLink.fManager==null)
		{
			debug ("Error: no file manager available");
			return (report);
		}
		
		if (outputPath.isEmpty()==true)
		{
			debug ("Error: no output path specified");
			return (report);
		}
				
		StringBuffer overview=new StringBuffer ();		
		StringBuffer formatted=new StringBuffer ();
		
		formatted.append("QueryID");
		formatted.append("\t");
		formatted.append("Q0");
		formatted.append("\t");
		formatted.append("DocID");
		formatted.append("\t");
		formatted.append("Rank");
		formatted.append("\t");
		formatted.append("Score");
		formatted.append("\t");
		formatted.append("RunID");
		formatted.append("\n");
		
		for (int i=0;i<INLink.searchHistory.size();i++)
		{
			INTextSearch aSearch=INLink.searchHistory.get(i);			
			//INQueryOperator query=aSearch.getRootQueryOperator();
			INDataSet docs=INLink.dataSet;
											
			if (docs!=null)
			{
				ArrayList<INDocument> docList=docs.getDocuments();
				
				overview.append("Report ("+i+") generated for " + docList.size() + " documents\n");
				
				if (docList!=null)
				{										
					for (int j=0;j<docList.size();j++)
					{
						INDocument aDoc=docList.get(j);
						
						formatted.append(aSearch.getInstanceName());
						formatted.append("\t");
						formatted.append("Q0");
						formatted.append("\t");
						formatted.append(aDoc.getDocID());
						formatted.append("\t");
						formatted.append(aDoc.getRank()+1); // trec_eval starts at 1
						formatted.append("\t");
						formatted.append(String.format("%.2f",aDoc.getScore()));
						formatted.append("\t");
						formatted.append("run-"+i);
						formatted.append("\n");
					}										
				}	
			}
			else
				debug ("Internal error: query object is null");
		}
		
		INLink.fManager.saveContents(outputPath+"/"+"TrecEval-Log-Exp"+INLink.experimentNr+"-"+INBase.generateFileTimestamp ()+".txt", formatted.toString());
				
		return (overview.toString());
	}	
}
