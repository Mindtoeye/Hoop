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
import java.util.Random;

//import edu.cmu.cs.in.base.HoopBase;

public class HoopPositionEntry /*extends HoopBase*/ implements Comparable
{
	private long docID=-1; 
	private long tf=1; 
	private long docLen=1; 
	private float evaluation=(float) 0.0;
	private ArrayList<Long> posList=null;
	
	/**
	 *
	 */
    public HoopPositionEntry () 
    {
		//setClassName ("HoopPositionEntry");
		//debug ("HoopPositionEntry ()");		
		
		posList=new ArrayList<Long> ();
		
		Random testScoreGenerator = new Random();	
		setEvaluation((float) (testScoreGenerator.nextFloat()-0.010441));		
    }
	/**
	 *
	 */
	public void setDocID(long docID) 
	{
		this.docID = docID;
	}
	/**
	 *
	 */
	public long getDocID() 
	{
		return docID;
	}
	/**
	 *
	 */
	public void setTf(long tf) 
	{
		this.tf = tf;
	}
	/**
	 *
	 */
	public long getTf() 
	{
		return tf;
	}
	/**
	 *
	 */
	public void setDocLen(long docLen) 
	{
		this.docLen = docLen;
	}
	/**
	 *
	 */
	public long getDocLen() 
	{
		return docLen;
	}
	/**
	 *
	 */
	public void setPosList(ArrayList<Long> posList) 
	{
		this.posList = posList;
	}
	/**
	 *
	 */
	public ArrayList<Long> getPosList() 
	{
		return posList;
	}
	/**
	 *
	 */	
	public float getEvaluation() 
	{
		return evaluation;
	}
	/**
	 *
	 */	
	public void setEvaluation(float evaluation) 
	{
		this.evaluation = evaluation;
	}
	/**
	 *
	 */	
	@Override
	public int compareTo(Object obj) 
	{
		//System.out.println ("compareTo ()");
		
		HoopPositionEntry target=(HoopPositionEntry) obj;
		if (target.evaluation>this.evaluation)
			return (1);
		
		return 0;
	}    
}
