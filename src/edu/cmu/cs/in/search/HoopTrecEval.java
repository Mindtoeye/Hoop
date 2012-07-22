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

import edu.cmu.cs.in.search.HoopDataSet;
import edu.cmu.cs.in.search.HoopDocument;
import edu.cmu.cs.in.base.HoopRoot;
import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.search.HoopTextSearch;
import edu.cmu.cs.in.stats.HoopStatsBase;

/** 
 * @author vvelsen
 *
 * A simply class that takes a set of documents as found in the registry object
 * HoopLink.dataset and dumps out a file that can be uploaded to trec_eval
 */
public class HoopTrecEval extends HoopStatsBase
{    	
	private String outputPath=".";
	private String report="";
	private Boolean collate=true;
	
	/**
	 *
	 */
    public HoopTrecEval (Boolean aCollate) 
    {
		setClassName ("HoopTrecEval");
		debug ("HoopTrecEval ()");		
		
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
	
		if (HoopLink.searchHistory==null)
		{
			debug ("Error: no searches completed yet");
			return (report);
		}
		
		if (HoopLink.fManager==null)
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
		
		for (int i=0;i<HoopLink.searchHistory.size();i++)
		{
			HoopTextSearch aSearch=HoopLink.searchHistory.get(i);			
			//HoopQueryOperator query=aSearch.getRootQueryOperator();
			HoopDataSet docs=HoopLink.dataSet;
											
			if (docs!=null)
			{
				ArrayList<HoopDocument> docList=docs.getDocuments();
				
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
						HoopDocument aDoc=docList.get(j);
						
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
					
					HoopLink.fManager.saveContents(outputPath+"/"+"TrecEval-Log-Exp"+HoopLink.experimentNr+"-Query"+i+"-"+HoopRoot.generateFileTimestamp ()+".txt", formatted.toString());
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
	
		if (HoopLink.searchHistory==null)
		{
			debug ("Error: no searches completed yet");
			return (report);
		}
		
		if (HoopLink.fManager==null)
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
		
		for (int i=0;i<HoopLink.searchHistory.size();i++)
		{
			HoopTextSearch aSearch=HoopLink.searchHistory.get(i);			
			//HoopQueryOperator query=aSearch.getRootQueryOperator();
			HoopDataSet docs=HoopLink.dataSet;
											
			if (docs!=null)
			{
				ArrayList<HoopDocument> docList=docs.getDocuments();
				
				overview.append("Report ("+i+") generated for " + docList.size() + " documents\n");
				
				if (docList!=null)
				{										
					for (int j=0;j<docList.size();j++)
					{
						HoopDocument aDoc=docList.get(j);
						
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
		
		HoopLink.fManager.saveContents(outputPath+"/"+"TrecEval-Log-Exp"+HoopLink.experimentNr+"-"+HoopRoot.generateFileTimestamp ()+".txt", formatted.toString());
				
		return (overview.toString());
	}	
}
