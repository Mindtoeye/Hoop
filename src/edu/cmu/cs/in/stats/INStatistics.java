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

package edu.cmu.cs.in.stats;

import java.util.ArrayList;

import edu.cmu.cs.in.base.INBase;
import edu.cmu.cs.in.base.INLink;
import edu.cmu.cs.in.stats.INPerformanceMetrics;

public class INStatistics extends INBase
{    			
	private ArrayList <INStatisticsMeasure> measures=null;
	
	/**
	 *
	 */
    public INStatistics () 
    {
		setClassName ("INStatistics");
		debug ("INStatistics ()");		
		
		measures=new ArrayList<INStatisticsMeasure> ();
    }  
    /**
     * 
     */
    private INStatisticsMeasure findMeasure (String label)
    {
    	for (int i=0;i<measures.size();i++)
    	{
    		INStatisticsMeasure test=measures.get(i);
    		if (test.getLabel().equals(label)==true)
    			return (test);
    	}
    	
    	return (null);
    }
    /**
     * 
     */
    private void incMeasure (String label,float aValue)
    {
    	INStatisticsMeasure incer=findMeasure (label);
    	if (incer==null)
    	{
    		incer=new INStatisticsMeasure ();
    		incer.setLabel(label);
    		measures.add(incer);
    	}
    	
    	incer.increment(aValue);
    }
	/**
	 *
	 */
    public void calcStatistics ()
    {
    	debug ("calcStatistics ();");
   
    	if (INLink.metrics==null)
    	{
    		debug ("Error: no metrics data available");
    		return;
    	}
    	
    	for (int i=0;i<INLink.metrics.size();i++)
    	{    		
    		INPerformanceMetrics entry=INLink.metrics.get(i);
    		
    		if (entry.isOpen()==false)
    		{
    			debug ("Adding measure: " + entry.getLabel() + " with measure: " + entry.getMeasure());
    		
    			incMeasure (entry.getLabel(),entry.getMeasure()/1000); // to keep things sane
    		}	
    	}
    }
	/**
	 *
	 */
    public String printStatistics ()
    {
    	debug ("printStatistics ();");
    	    	
    	StringBuffer results=new StringBuffer ();
    	
    	for (int i=0;i<measures.size();i++)
    	{
    		INStatisticsMeasure measure=measures.get(i);
    		results.append("Measure: "+measure.getLabel()+" N:"+measure.getN ()+" Avg: " + measure.getAvg()+" Total value: "+measure.getValue()+"\n");
    	}
    	
    	return (results.toString());
    }
}
