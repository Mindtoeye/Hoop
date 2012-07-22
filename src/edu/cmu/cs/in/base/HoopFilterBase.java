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

package edu.cmu.cs.in.base;

import edu.cmu.cs.in.base.HoopRoot;

public class HoopFilterBase extends HoopRoot
{    		    		
	private Boolean noMore=false;
	
	/**
	 *
	 */
    public HoopFilterBase () 
    {
		setClassName ("HoopFilterBase");
		debug ("HoopFilterBase ()");						
    }  
	/**
	 *
	 */
    public Boolean evaluate (String aText)   
    {
    	debug ("evaluate ("+aText+")");
    	
    	if (noMore==true)
    		return (false);
    	
    	return (true);
    }
	/**
	 *
	 */
    public String clean (String aText)   
    {
    	debug ("clean ("+aText+")");
   	   	
    	return (aText);
    }
	/**
	 *
	 */
    public Boolean check (String aText)   
    {
    	debug ("check ("+aText+")");
  	   	
    	return (true);
    }    
	/**
	 *
	 */
    public void reset ()
    {
    	noMore=false;
    }
	/**
	 *
	 */    
	public Boolean getNoMore() 
	{
		return noMore;
	}
	/**
	 *
	 */	
	public void setNoMore(Boolean noMore) 
	{
		this.noMore = noMore;
	}  
}
