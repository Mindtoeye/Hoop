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
import java.util.Random;

import edu.cmu.cs.in.INDataSet;
import edu.cmu.cs.in.INDocument;
import edu.cmu.cs.in.base.INBase;
import edu.cmu.cs.in.base.INLink;
import edu.cmu.cs.in.search.INQueryOperator;
import edu.cmu.cs.in.hadoop.INPositionEntry;
import edu.cmu.cs.in.hadoop.INPositionList;
import edu.cmu.cs.in.stats.INPerformanceMetrics;

/**
* For your reading and puzzlement pleasure you will find a term query
* full text search class. In essence this class manages the various
* structures needed to perform a retrieval and ranking set of steps
* on a number of input terms and an archive of pre-processed documents.
*/
public class INTextSearch extends INBase
{    				
    private INQueryOperator operation=null;
    private int topDocs=20;
    private INDataSet localDataSet=null;
    
	/**
	 *
	 */
    public INTextSearch () 
    {
		setClassName ("INTextSearch");
		debug ("INTextSearch ()");
		localDataSet=new INDataSet ();
    }  
    /**
     * 
     */    
    public INDataSet getDataSet ()
    {
    	return (localDataSet);
    }
    /**
     * 
     */
    public INQueryOperator getRootQueryOperator ()
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
	 *
	 */
	public long search (String aQuery,int aTopDocs)
	{
		debug ("search ()");
		
		setTopDocs (aTopDocs);
		
		INPerformanceMetrics metrics=new INPerformanceMetrics ();
		metrics.setMarker ("Query ");
		INLink.metrics.add(metrics);
		
		Long memBefore=Runtime.getRuntime().freeMemory();
		
		buildQuery (aQuery);				
		searchQuery (operation);
		INPositionList result=merge (operation);
				
		if (result!=null)
		{
			rankDocumentList (result);
		}
		
		Long memAfter=Runtime.getRuntime().freeMemory();
				
		long timeTaken=metrics.getMarkerRaw ();
		
		operation.setTimeTaken(timeTaken);
		operation.setMemUsed(memAfter-memBefore);
						
		return (timeTaken);
	}
	/**
	 * 
	 */
	private INDataSet rankDocumentList (INPositionList aList)
	{
		debug ("rankDocumentList ("+aList.getPosEntries().size()+")");
		
		INLink.dataSet=new INDataSet ();
		
		ArrayList<INPositionEntry> docIDs=aList.getPosEntries();
		
		int count=topDocs;
		
		if (docIDs.size ()<topDocs)
			count=docIDs.size ();
		
		for (int i=0;i<count;i++)
		{
			INPositionEntry entry=docIDs.get(i);
			
			INDocument newDocument=new INDocument ();
			newDocument.setDocID(String.format("%d",entry.getDocID()));
			newDocument.setRank(i);
			
			Random testScoreGenerator = new Random();			
			newDocument.setScore(testScoreGenerator.nextFloat());
			
			localDataSet.addDocument (newDocument);						
		}
		
		INLink.dataSet=localDataSet;
		
		return (localDataSet);
	}
	/**
	 * Implementation of the merge pseudo code as listed in:
	 * Manning, C. (2008). Introduction to information retrieval. 
	 * New York: Cambridge University Press. 
	 */	
	private INPositionList mergeLists (String operator,
									   INPositionList aFirst,
									   INPositionList aSecond)
	{
		debug ("mergeLists ("+operator+")");
		
		INPositionList merged=new INPositionList ();
		
		ArrayList<INPositionEntry> posEntriesA=aFirst.getPosEntries();
		ArrayList<INPositionEntry> posEntriesB=aSecond.getPosEntries();
		
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
				INPositionEntry anEntry=posEntriesB.get(i);
				merged.addDocument (anEntry);
			}
			
			return (merged);
		}
		
		if (posEntriesB.size()==0)
		{
			debug ("Internal error: positions list for " + aFirst.getInstanceName() + " (B) is 0 length");
			return (null);
		}		
		
		INPositionEntry first=posEntriesA.get(0);
		INPositionEntry second=posEntriesB.get(0);
		
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
	private INPositionList merge (INQueryOperator aRoot)
	{
		debug ("merge ("+aRoot.getInstanceName()+")");
		
		ArrayList<INQueryOperator> operators=aRoot.getOperators();
		
		debug ("Merging ("+ aRoot.getOperator()+") over: " + operators.size() + " position lists");
		
		INPositionList last=aRoot.getPositions(); // This is where we want stuff to end up in
		
		for (int i=0;i<operators.size();i++)
		{			
			INQueryOperator op=(INQueryOperator) operators.get(i);
		
			last=mergeLists (op.getInstanceName(),last,op.getPositions());
			
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
	private void searchQuery (INQueryOperator aRoot)
	{
		debug ("searchQuery ()");
		
		ArrayList<INQueryOperator> operators=aRoot.getOperators();
		
		debug ("Loading and merging: " + operators.size() + " search operators/terms");
		
		for (int i=0;i<operators.size();i++)
		{			
			INQueryOperator op=(INQueryOperator) operators.get(i);
			
			if (op.getIsTerm()==true)
			{
				// Each query operator already has a bound term, now it will 
				// obtain a positions list and bind it as well
				
				if (op.loadPositionList ()==false)
				{
					debug ("Query aborted, no position list for: " + op.getInstanceName());
					return;
				}
				else
				{
					debug ("Position list loaded for: " + op.getInstanceName());
					
					/*
					INPositionList tList=op.getPositions();
					ArrayList <INPositionEntry>lList=tList.getPosEntries();
					for (int w=0;w<lList.size();w++)
					{
						INPositionEntry pEntry=lList.get(w);
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
	 * 
	 */
	private String [] removeStops (String [] aList)
	{
		debug ("removeStops ()");
		
		if (INLink.stops==null)
			return (aList);
		
		String [] changer=aList;
		
		for (int i=0;i<INLink.stops.length;i++)
		{
			//debug ("Checking: " + INLink.stops [i]);
			
			String stopWord=INLink.stops [i];
			changer=removeElements (changer,stopWord);
		}
		
		return changer;
	}
	/**
	 * Example: #OR (#AND (viva la vida) coldplay)  
	 */
	private int buildSubQuery (INQueryOperator aRoot,String [] list,int anIndex)
	{
		debug ("buildSubQuery ("+aRoot.getInstanceName()+")");
				
		int i=anIndex;
		
		INQueryOperator newRoot=aRoot;
		
		for (i=anIndex;i<list.length;i++)
		{
			String token=list [i];
			
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
	 * 
	 */
	private String preProcess (String aQuery)
	{
		debug ("preProcess ()");
		
		String formatted=aQuery.toLowerCase();
		
		// Allow for easier tokenization
		formatted=formatted.replaceAll("\\("," ( ");
		
		// Allow for easier tokenization
		formatted=formatted.replaceAll("\\)"," ) ");
		
		// We will treat hypenated terms as phrases with a
		// nearness of 1 and then let the rest of the
		// algorithm take care of it
		formatted=formatted.replaceAll("\\-","/1 ");
		
		return (formatted);
	}
	/**
	 * Example: #OR (#AND (viva la vida) coldplay)  
	 */
	private INQueryOperator buildQuery (String aQuery)
	{
		debug ("buildQuery ("+aQuery+")");

		String processed=preProcess (aQuery);
		
		debug ("Reformatted: " + processed);
		
		String [] raw=processed.split("\\s+");
		
		String [] split=removeStops (raw);

		for (int t=0;t<split.length;t++)
		{
			debug ("Query item: " + split [t]);
		}
		
		operation=new INQueryOperator ();
		operation.setInstanceName("Query");
		
		INQueryOperator newRoot=operation;
		
		for (int i=0;i<split.length;i++)
		{
			String token=split [i];
			
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

