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

/**
 * SS in ANOVA is the sum of squares of all observations. The term "sum of squares" means, 
 * in this case, the "sum of squares of the differences from the mean", and the corresponding formula is
 * SS = Σ (x - m(x))²
 * 
 * In order to compute it, you have to
 * - compute the mean m(x) of all observations
 * - for each x, compute the difference x - m(x) and square it
 * - sum all the squares
 * 
 * There is an alternative computation, that can be easier to perform, and is based on the equivalent formula
 * SS = Σ x² - S²/N
 * where S is the sum of all observations and N is the number of observatios.
 * In this case, you have to
 * - sum the squares of all observations, thus getting Σ x²
 * - sum all observations, thus getting S
 * - square S and divide by number of observations N
 * - subtract S²/n from Σ x² 
 */
public class HoopStatistics extends HoopRoot
{    				
	/**
	 *
	 */
    public HoopStatistics () 
    {
		setClassName ("HoopStatistics");
		debug ("HoopStatistics ()");		
				
    }  
	/**
	 *
	 */
    public void calcStatistics (HoopSampleDataSet aSet) 
    {
    	debug ("calcStatistics ();");
   
    	long min=5000000;
    	long max=-5000000;
    	long total=0;
    	
    	ArrayList <HoopSampleMeasure> list=aSet.getDataSet();
    	
    	if (list.size()==0)
    		return;
    	
    	for (int i=0;i<list.size();i++)
    	{
    		HoopSampleMeasure aMeasure=list.get(i);
    		
    		total+=aMeasure.getMeasure();
    		
    		if (aMeasure.getMeasure()<min)
    			min=aMeasure.getMeasure();
    		
    		if (aMeasure.getMeasure()>max)
    			max=aMeasure.getMeasure();
    	}
    	
    	aSet.setMin(min);
    	aSet.setMax(max);
    	aSet.setMean(total/list.size());
    	
    	double ss=0;
    	
    	for (int j=0;j<list.size();j++)
    	{
    		HoopSampleMeasure aMeasure=list.get(j);
    		
    		double raw=aMeasure.getMeasure()-aSet.getMean();
    		
    		ss+=(raw*raw);
    		
    	}
    	
    	// Calculate basic stats ...
    	
    	aSet.setStddev(Math.sqrt (ss/(list.size()-1)));
    	aSet.setStderr(aSet.getStddev()/Math.sqrt (list.size ()));    	
    	
    	// Generate one sample t-test from hypothesis ...
    	
    	aSet.settTest((aSet.getMean()-aSet.getHypothesis())/(aSet.getStddev()/Math.sqrt((int) list.size())));
    }
	/**
	 *
	 */
    public String printStatistics (HoopSampleDataSet aSet)
    {
    	debug ("printStatistics ()");

    	if (aSet==null)
    		return ("Error: provided data set is null!");
    	
    	StringBuffer results=new StringBuffer ();
    	
    	results.append("Measure: "+aSet.getLabel()+"\n");
    	results.append("N:"+aSet.getN ()+"\n");
    	results.append("Mean: " + aSet.getMean() + "\n");
    	results.append("Standard Deviation: " + aSet.getStddev() + "\n");
    	results.append("Standard Error: " + aSet.getStderr() + "\n");
    	results.append("Hypothesis: " + aSet.getHypothesis() + "\n");
    	results.append("T-Test (One Sample): " + aSet.gettTest() + "\n");
    	    	
    	return (results.toString());
    }
}

/*
$mean  =0;
$stddev=0;
$stderr=0;

function get_mean ()
{
 global $mean;
 return round ($mean,2);
}

function get_stddev ()
{
 global $stddev;
 return round ($stddev,2);
}

function get_stderr ()
{
 global $stderr;
 return round ($stderr,2);
}

function get_stats ($data)
{
 global $mean,$stddev,$stderr;
 
 $tempdata=array ();
 $results="";
 $total=0;
 $ss   =0;
 $count=0;
 
 $min  =500000;
 $max  =0;
 
 foreach ($data as $entry)
 {
  $tempdata [$count]=array ();
  $tempdata [$count][0]=$entry;
  $tempdata [$count][1]=$entry;
  
  $total+=$entry;
  $count++;
  
  if ($entry>$max)
   $max=$entry;
  
  if ($entry<$min)
   $min=$entry;
 }
 
 $mean=$total/$count;
 
 $count=0;
 
 foreach ($tempdata as $entry)
 {
  $raw=$tempdata [$count][1]-$mean;
  $tempdata [$count][1]=($raw*$raw);
  $ss+=$tempdata [$count][1]; 
  $count++;
 } 
 
 $stddev=sqrt ($ss/($count-1));
 $stderr=$stddev/sqrt ($count);
 
 $results="Moments:<br> <ul><li>Minimum: ".number_format ($min,2)."</li><li>Mean: ".number_format ($mean,2)."</li><li>Std Dev: ".number_format ($stddev,2)."</li><li>Std Err Mean: ".number_format ($stderr,2)."</li><li>Max: ".number_format ($max,2)."</li><li>N: ".$count."</li></ul>";
 
 return $results;
}
*/
