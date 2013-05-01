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

import edu.cmu.cs.in.base.HoopRoot;

public class HoopPositionList extends HoopRoot
{    		    					
	private String stemmTerm=""; 
	private String collTerm="";
	private Long freq=(long) 1; 
	private Long N=(long) 1;
	private ArrayList<HoopPositionEntry> posEntries=null;
	
	/**
	 *
	 */
    public HoopPositionList () 
    {
		setClassName ("HoopPositionList");
		//debug ("HoopPositionList ()");		
		posEntries=new ArrayList<HoopPositionEntry> ();
    }
	/**
	 *
	 */
    public void addDocument (HoopPositionEntry aDoc)
    {
    	posEntries.add(aDoc);
    }    
	/**
	 *
	 */
    public void addDocument (String aDocID)
    {
    	HoopPositionEntry newEntry=new HoopPositionEntry ();
    	newEntry.setDocID(Long.parseLong(aDocID));
    	posEntries.add(newEntry);
    }
	/**
	 *
	 */
    public ArrayList<HoopPositionEntry> getPosEntries ()
    {
    	return (posEntries);
    }
	/**
	 *
	 */
	public void setStemmTerm(String stemmTerm) 
	{
		this.stemmTerm = stemmTerm;
	}
	/**
	 *
	 */
	public String getStemmTerm() 
	{
		return stemmTerm;
	}
	/**
	 *
	 */
	public void setCollTerm(String collTerm) 
	{
		this.collTerm = collTerm;
	}
	/**
	 *
	 */
	public String getCollTerm() 
	{
		return collTerm;
	}
	/**
	 *
	 */
	public void setFreq(Long freq) 
	{
		this.freq = freq;
	}
	/**
	 *
	 */
	public Long getFreq() 
	{
		return freq;
	}
	/**
	 *
	 */
	public void setN(Long n) 
	{
		N = n;
	}
	/**
	 *
	 */
	public Long getN() 
	{
		return N;
	}    
}
