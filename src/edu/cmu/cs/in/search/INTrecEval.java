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
