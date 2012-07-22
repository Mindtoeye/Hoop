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

package edu.cmu.cs.in.stats;

import java.util.ArrayList;

import edu.cmu.cs.in.base.HoopRoot;
import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.stats.HoopPerformanceMetrics;

public class HoopStatistics extends HoopRoot
{    			
	private ArrayList <HoopStatisticsMeasure> measures=null;
	
	/**
	 *
	 */
    public HoopStatistics () 
    {
		setClassName ("HoopStatistics");
		debug ("HoopStatistics ()");		
		
		measures=new ArrayList<HoopStatisticsMeasure> ();
    }  
    /**
     * 
     */
    private HoopStatisticsMeasure findMeasure (String label)
    {
    	for (int i=0;i<measures.size();i++)
    	{
    		HoopStatisticsMeasure test=measures.get(i);
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
    	HoopStatisticsMeasure incer=findMeasure (label);
    	if (incer==null)
    	{
    		incer=new HoopStatisticsMeasure ();
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
   
    	if (HoopLink.metrics==null)
    	{
    		debug ("Error: no metrics data available");
    		return;
    	}
    	
    	for (int i=0;i<HoopLink.metrics.size();i++)
    	{    		
    		HoopPerformanceMetrics entry=HoopLink.metrics.get(i);
    		
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
    		HoopStatisticsMeasure measure=measures.get(i);
    		results.append("Measure: "+measure.getLabel()+" N:"+measure.getN ()+" Avg: " + measure.getAvg()+" Total value: "+measure.getValue()+"\n");
    	}
    	
    	return (results.toString());
    }
}
