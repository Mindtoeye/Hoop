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

import edu.cmu.cs.in.base.INBase;

public class INPositionList extends INBase
{    		    					
	private String stemmTerm=""; 
	private String collTerm="";
	private Long freq=(long) 1; 
	private Long N=(long) 1;
	private ArrayList<INPositionEntry> posEntries=null;
	
	/**
	 *
	 */
    public INPositionList () 
    {
		setClassName ("INPositionList");
		//debug ("INPositionList ()");		
		posEntries=new ArrayList<INPositionEntry> ();
    }
	/**
	 *
	 */
    public void addDocument (INPositionEntry aDoc)
    {
    	posEntries.add(aDoc);
    }    
	/**
	 *
	 */
    public void addDocument (String aDocID)
    {
    	INPositionEntry newEntry=new INPositionEntry ();
    	newEntry.setDocID(Long.parseLong(aDocID));
    	posEntries.add(newEntry);
    }
	/**
	 *
	 */
    public ArrayList<INPositionEntry> getPosEntries ()
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
