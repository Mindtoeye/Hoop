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
import java.util.Collections;

import edu.cmu.cs.in.search.HoopDataSet;
import edu.cmu.cs.in.search.HoopDocument;
import edu.cmu.cs.in.base.HoopRoot;
import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.search.HoopQueryOperator;
import edu.cmu.cs.in.search.HoopPositionEntry;
import edu.cmu.cs.in.search.HoopPositionList;
import edu.cmu.cs.in.stats.HoopPerformanceMetrics;

/**
* Here you will find a term query full text search class. In 
* essence this class manages the various structures needed to 
* perform a retrieval and ranking set of steps on a number
* of input terms and an archive of pre-processed documents.
*/
public class HoopTextSearch extends HoopRoot
{    				
    private HoopQueryOperator operation=null;

    // As per homework request the amount of documents to be retrieved
    private int topDocs=100;
    
    // This is where the resulting dataset/document set will end up
    // When this is all filled up it will be assigned to HoopLink.dataSet
    // so that other objects can use the set
    private HoopDataSet localDataSet=null;
    
	/**
	 *
	 */
    public HoopTextSearch () 
    {
		setClassName ("HoopTextSearch");
		debug ("HoopTextSearch ()");
		localDataSet=new HoopDataSet ();
    }  
    /**
     * 
     */    
    public HoopDataSet getDataSet ()
    {
    	return (localDataSet);
    }
    /**
     * 
     */
    public HoopQueryOperator getRootQueryOperator ()
    {
    	return (operation);
    }
	/**
	 *
	 */    
	public void setTopDocs(int topDocs) 
	{
		this.topDocs = topDocs;
	}
	/**
	 *
	 */	
	public int getTopDocs() 
	{
		return topDocs;
	}	    
	/**
	 * The entry point for our search. It only takes a string and the
	 * desired number of retrieved documents	 
	 */
	public long search (String aQuery,int aTopDocs)
	{
		debug ("search ()");
		
		setTopDocs (aTopDocs);
		
		// First set a marker so that we can see how long it all took
		
		HoopPerformanceMetrics metrics=new HoopPerformanceMetrics ();
		metrics.setMarker ("Query ");
		HoopLink.metrics.add(metrics);
		
		// Also measure memory, might be useful
		
		Long memBefore=Runtime.getRuntime().freeMemory();

		// Now first we build an internal representation of the query
		// including any hierarchy and preprocessing.
		
		buildQuery (aQuery);				
		
		// Next is when we traverse the internal structuer depth-first
		// with some minor optimizations
		
		searchQuery (operation);
		
		// Finally we merge everything and activate/run any operators
		
		HoopPositionList result=merge (operation);
						
		// We then rank and sort the final list of documents and
		// prepare them to be sent to HoopTrecEval
		
		if (result!=null)
		{
			rankDocumentList (result);
		}
		
		// Now let's see how we did by retrieving our markers
		
		Long memAfter=Runtime.getRuntime().freeMemory();
				
		long timeTaken=metrics.getMarkerRaw ();
		
		operation.setTimeTaken(timeTaken);
		operation.setMemUsed(memAfter-memBefore);
						
		return (timeTaken);
	}
	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	private HoopDataSet rankDocumentList (HoopPositionList aList)
	{
		debug ("rankDocumentList ("+aList.getPosEntries().size()+")");
		
		HoopLink.dataSet=new HoopDataSet ();
		
		ArrayList<HoopPositionEntry> docIDs=aList.getPosEntries();
		
		// First we use a sort to re-order the positions
		// list into a ranked list ...
				
		Collections.sort(docIDs);
				
		// Then from that sorted list we take the top Y documents
		// as indicated by the user or default settings. For the
		// homework this was set to the top 100 documents
		
		int count=topDocs;
		
		if (docIDs.size ()<topDocs)
			count=docIDs.size ();
		
		for (int i=0;i<count;i++)
		{
			HoopPositionEntry entry=docIDs.get(i);
			
			HoopDocument newDocument=new HoopDocument ();
			//newDocument.setDocID(String.format("%d",entry.getDocID()));
			newDocument.setKey(entry.getDocID());
			newDocument.setRank(i);				
			newDocument.setScore(entry.getEvaluation());
			
			//localDataSet.addDocument (newDocument);
			localDataSet.writeKV (entry.getDocID(),newDocument);
		}
		
		HoopLink.dataSet=localDataSet;
		
		return (localDataSet);
	}
	/**
	 * Implementation of the merge pseudo code as listed in:
	 * Manning, C. (2008). Introduction to information retrieval. 
	 * New York: Cambridge University Press. 
	 */	
	private HoopPositionList mergeLists (String operator,
									   HoopPositionList aFirst,
									   HoopPositionList aSecond)
	{
		debug ("mergeLists ("+operator+")");
		
		HoopPositionList merged=new HoopPositionList ();
		
		ArrayList<HoopPositionEntry> posEntriesA=aFirst.getPosEntries();
		ArrayList<HoopPositionEntry> posEntriesB=aSecond.getPosEntries();
		
		debug ("Checking ... "+posEntriesA.size()+","+posEntriesB.size());
		
		if ((posEntriesA==null) || (posEntriesB==null))
		{
			debug ("Internal error: one of the positions lists is null");
			return (null);
		}
		
		if ((posEntriesA.size()==0) && (posEntriesB.size ()==0))
		{
			debug ("Internal error: both lists have length 0");
			return(null);
		}
		
		if ((posEntriesA.size()==0) && (posEntriesB.size()>0))
		{
			debug ("Copying positions list B into Merged list");

			for (int i=0;i<posEntriesB.size();i++)
			{
				HoopPositionEntry anEntry=posEntriesB.get(i);
				merged.addDocument (anEntry);
			}
			
			return (merged);
		}
		
		if (posEntriesB.size()==0)
		{
			debug ("Internal error: positions list for " + aFirst.getInstanceName() + " (B) is 0 length");
			return (null);
		}		
		
		HoopPositionEntry first=posEntriesA.get(0);
		HoopPositionEntry second=posEntriesB.get(0);
		
		debug ("Merging ...");
		
		int indexA=0;
		int indexB=0;
		
		while ((first!=null) && (second!=null))
		{			
			long firstIndex=first.getDocID();
			long secondIndex=second.getDocID();
			
			//debug ("Comparing "+firstIndex+" at: "+indexA+" to " + secondIndex + " at: " + indexB);
			
			if (firstIndex==secondIndex)
			{
				//debug ("Found duplicate: " +first.getDocID());
				merged.addDocument (first); // doesn't matter which one we use
				indexA++;
				indexB++;
			}
			else
			{
				if (firstIndex<secondIndex)
				{
					//debug ("incrementing first index");
					indexA++;
				}
				else
				{
					//debug ("incrementing second index");
					indexB++;
				}
			}
									
			if (indexA>(posEntriesA.size()-1))
			{
				first=null;
			}
			else
			{
				first=posEntriesA.get(indexA);
				//debug ("Obtained next (A)->"+indexA+" entry: " + first.getDocID());
			}
			
			if (indexB>(posEntriesB.size()-1))
			{
				second=null;
			}
			else			
			{
				second=posEntriesB.get(indexB);
				//debug ("Obtained next (B)->"+indexB+" entry: " + second.getDocID());
			}
		}
		
		return (merged);
	}
	/**
	 *
	 */
	private HoopPositionList merge (HoopQueryOperator aRoot)
	{
		debug ("merge ("+aRoot.getInstanceName()+")");
		
		ArrayList<HoopQueryOperator> operators=aRoot.getOperators();
		
		debug ("Merging ("+ aRoot.getOperator()+") over: " + operators.size() + " position lists");
		
		HoopPositionList last=aRoot.getPositions(); // This is where we want stuff to end up in
		
		for (int i=0;i<operators.size();i++)
		{			
			HoopQueryOperator op=(HoopQueryOperator) operators.get(i);
					
			if (op.getPositions().getPosEntries().size()!=0)
			{			
				last=mergeLists (op.getInstanceName(),
								 last,
								 op.getPositions());
			}
			else
			{
				debug ("Positions list has no entries, skipping for merge ...");
				last=op.getPositions();
			}
			
			if (last==null)
			{
				debug ("Internal error: merged list is null");
				return (null);
			}
			
			debug ("List now: " + last.getPosEntries().size());
		}
		
		return (last);
	}
	/**
	 *
	 */
	private void searchQuery (HoopQueryOperator aRoot)
	{
		debug ("searchQuery ()");
		
		ArrayList<HoopQueryOperator> operators=aRoot.getOperators();
		
		debug ("Loading and merging: " + operators.size() + " search operators/terms");
		
		for (int i=0;i<operators.size();i++)
		{			
			HoopQueryOperator op=(HoopQueryOperator) operators.get(i);
			
			if (op.getIsTerm()==true)
			{
				// Each query operator already has a bound term, now it will 
				// obtain a positions list and bind it as well
				
				if (op.loadPositionList ()==false)
				{
					debug ("Query aborted, no position list for: " + op.getInstanceName());
					//return;
				}
				else
				{
					debug ("Position list loaded for: " + op.getInstanceName());
					
					/*
					HoopPositionList tList=op.getPositions();
					ArrayList <HoopPositionEntry>lList=tList.getPosEntries();
					for (int w=0;w<lList.size();w++)
					{
						HoopPositionEntry pEntry=lList.get(w);
						debug ("Entry: " + pEntry.getDocID());
					}
					*/
				}
			}
			else
			{
				debug ("Processing operator: " + op.getInstanceName());
				searchQuery (op);
				merge (op);
			}			
		}
	}
	/**
	 * 
	 */
	public String[] removeElements(String[] input, String deleteMe) 
	{
	    ArrayList <String> result=new ArrayList<String> ();
	    
	    for (int i=0;i<input.length;i++)
	    {
	        if(deleteMe.equals(input [i])==false)
	        {
	            result.add(new String (input [i]));
	        }
	    }    

	    String [] stringed=new String [result.size()];
	    
	    for (int t=0;t<result.size();t++)
	    {
	    	stringed [t]=result.get(t);
	    }
	    
	    return (stringed);
	}
	/**
	 * Here we have a choice of how to deal with things
	 * like stop words. Currently any term indicated to
	 * be a stop word is simply removed but we could
	 * also tag a term as a stop word so we can track
	 * at a different level if it should be removed or
	 * not.
	 */
	private String [] removeStops (String [] aList)
	{
		debug ("removeStops ()");
		
		if (HoopLink.stops==null)
			return (aList);
		
		String [] changer=aList;
		
		for (int i=0;i<HoopLink.stops.length;i++)
		{			
			String stopWord=HoopLink.stops [i];
			changer=removeElements (changer,stopWord);
		}
		
		return changer;
	}
	/**
	 * Example: #OR (#AND (viva la vida) coldplay)  
	 */
	private int buildSubQuery (HoopQueryOperator aRoot,ArrayList <String> list,int anIndex)
	{
		debug ("buildSubQuery ("+aRoot.getInstanceName()+")");
				
		int i=anIndex;
		
		HoopQueryOperator newRoot=aRoot;
		
		for (i=anIndex;i<list.size ();i++)
		{
			String token=list.get (i);
			
			debug ("Token: " + token);
			
			if (token.equals("(")==true)
			{
				// Further down the rabbit hole
				i=buildSubQuery (newRoot,list,i+1);
			}
			else
			{
				if (token.equals(")")==true)
				{
					// Further up the rabbit hole
					debug ("pop");
					return (i);
				}
				else
				{					
					newRoot=aRoot.addOperator (token);
				}
			}	
		}		
		
		return (i);
	}	
	/**
	 * We probably get most of the parsing speed out of this
	 * small routine. It takes a string and expands parenthesis
	 * by adding whitespaces around them. That way the tokenizer
	 * doesn't need any exceptions and can work linearly.
	 */
	private String preProcess (String aQuery)
	{
		debug ("preProcess ()");
		
		String formatted=aQuery.toLowerCase();
		
		formatted=formatted.replaceAll("\\("," ( ");
		
		formatted=formatted.replaceAll("\\)"," ) ");
				
		return (formatted);
	}
	/**
	 * In order to do queries on hyphenated strings we
	 * transform the compound term into its own #near/1
	 * sub search. This code should also be able to
	 * handle multiple hypenations within one compound
	 * term
	 */
	private ArrayList <String> queryExpand (String [] input)
	{
		ArrayList <String> expanded=new ArrayList<String> ();
		
		for (int i=0;i<input.length;i++)
		{
			String token=input [i];
			
			if (token.indexOf('-')!=-1)
			{
				String [] splitter=token.split("\\-");
				expanded.add ("#near/1");
				
				expanded.add("(");
				
				for (int j=0;j<splitter.length;j++)
				{
					expanded.add(splitter [j]);
				}
				
				expanded.add (")");
			}
			else
			{
				// Add as is ...
				
				expanded.add(token);
			}
			
		}	
		
		return (expanded);
	}
	/**
	 * Example: #OR (#AND (viva la vida) coldplay)  
	 */
	private HoopQueryOperator buildQuery (String aQuery)
	{
		debug ("buildQuery ("+aQuery+")");

		String processed=preProcess (aQuery);
		
		debug ("Reformatted: " + processed);
		
		String [] raw=processed.split("\\s+");
		
		String [] stopped=removeStops (raw);

		ArrayList <String> split=queryExpand (stopped);
		
		for (int t=0;t<split.size ();t++)
		{
			debug ("Query item: " + split.get (t));
		}
		
		operation=new HoopQueryOperator ();
		operation.setInstanceName("Query");
		
		HoopQueryOperator newRoot=operation;
		
		for (int i=0;i<split.size ();i++)
		{
			String token=split.get(i);
			
			debug ("Token: " + token);
			
			if (token.equals("(")==true)
			{
				// Further down the rabbit hole
				i=buildSubQuery (newRoot,split,i+1);
			}
			else
			{
				if (token.equals(")")==true)
				{
					// Further up the rabbit hole
					debug ("pop");
				}
				else
				{					
					newRoot=operation.addOperator (token);
				}
			}	
		}
				
		return (newRoot);
	}
}

