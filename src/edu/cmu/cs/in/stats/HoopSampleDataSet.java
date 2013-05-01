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
 * 
 */
public class HoopSampleDataSet extends HoopRoot
{   
	private ArrayList <HoopSampleMeasure> dataSet=null;
	
	private double mean  =0;	
	private double stddev=0;
	private double stderr=0;
	
	private double min=0;
	private double max=0;
	
	private double tTest=0;
	
	private long hypothesis=0;
		
	/**
	 * 
	 */
	public HoopSampleDataSet ()
	{	
		setClassName ("HoopSampleDataSet");
		debug ("HoopSampleDataSet ()");
		
		dataSet=new ArrayList<HoopSampleMeasure> ();
	}	
	/**
	 * 
	 */
	public HoopSampleDataSet (String aLabel)
	{	
		setClassName ("HoopSampleDataSet");
		debug ("HoopSampleDataSet ()");
		
		dataSet=new ArrayList<HoopSampleMeasure> ();
		
		setLabel (aLabel);
	}		
	/**
	 * 
	 */
	public ArrayList <HoopSampleMeasure> getDataSet ()
	{
		return (dataSet);
	}
	
	public void setDataSet (ArrayList<HoopSampleMeasure> p_data)
	{
		dataSet = p_data;
	}
	
	/**
	 * 
	 */
	public void setLabel (String aLabel)
	{
		this.setInstanceName(aLabel);
	}
	/**
	 * 
	 */
	public String getLabel ()
	{
		return (this.getInstanceName());
	}
	/**
	 * 
	 */
	public double getMean() 
	{
		return mean;
	}
	/**
	 * 
	 */	
	public void setMean(double mean) 
	{
		this.mean = mean;
	}
	/**
	 * 
	 */	
	public double getStddev() 
	{
		return stddev;
	}
	/**
	 * 
	 */	
	public void setStddev(double stddev) 
	{
		this.stddev = stddev;
	}
	/**
	 * 
	 */	
	public double getStderr() 
	{
		return stderr;
	}
	/**
	 * 
	 */	
	public void setStderr(double stderr) 
	{
		this.stderr = stderr;
	}
	/**
	 * 
	 */	
	public double getMin() 
	{
		return min;
	}
	/**
	 * 
	 */	
	public void setMin(double min) 
	{
		this.min = min;
	}
	/**
	 * 
	 */	
	public double getMax() 
	{
		return max;
	}
	/**
	 * 
	 */	
	public void setMax(double max) 
	{
		this.max = max;
	}
	/**
	 * 
	 */
	public int getSampleSize ()
	{
		return (dataSet.size());
	}
	/**
	 * 
	 */
	public int getN ()
	{
		return (dataSet.size());
	}
	/**
	 * 
	 */
	public double gettTest() 
	{
		return tTest;
	}
	/**
	 * 
	 */
	public void settTest(double tTest) 
	{
		this.tTest = tTest;
	}
	/**
	 * 
	 * @return
	 */
	public long getHypothesis() 
	{
		return hypothesis;
	}
	/**
	 * 
	 * @param hypothesis
	 */
	public void setHypothesis(long hypothesis) 
	{
		this.hypothesis = hypothesis;
	}	
}
