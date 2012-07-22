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

package edu.cmu.cs.in.controls;

import edu.cmu.cs.in.base.HoopRoot;

public class HoopGridNode extends HoopRoot
{
	private int mapperCount=0;
	private int reducerCount=0;
	private int mapperCountTotal=0;
	private int reducerCountTotal=0;	
	private String nodeName="";
	private Boolean active=false;
	
	/**
	 *
	 */
	public HoopGridNode () 
	{
		setClassName("HoopGridNode");
		debug ("HoopGridNode ()");
	}
	/**
	 *
	 */
	public void incMapperCount ()
	{
		this.mapperCount++;
		this.mapperCountTotal++;
	}
	/**
	 *
	 */
	public void decMapperCount ()
	{
		this.mapperCount--;
	}	
	/**
	 *
	 */
	public void incReducerCount ()
	{
		this.reducerCount++;
		this.reducerCountTotal++;
	}
	/**
	 *
	 */
	public void decReducerCount ()
	{
		this.reducerCount--;
	}	
	/**
	 *
	 */
	public void setMapperCount(int mapperCount) 
	{
		this.mapperCount = mapperCount;
	}
	/**
	 *
	 */
	public int getMapperCount() 
	{
		return mapperCount;
	}
	/**
	 *
	 */
	public void setReducerCount(int reducerCount) 
	{
		this.reducerCount = reducerCount;
	}
	/**
	 *
	 */
	public int getReducerCount() 
	{
		return reducerCount;
	}
	/**
	 *
	 */
	public void setNodeName(String nodeName) 
	{
		this.nodeName = nodeName;
	}
	/**
	 *
	 */
	public String getNodeName() 
	{
		return nodeName;
	}
	/**
	 *
	 */	
	public void setActive(Boolean active) 
	{
		this.active = active;
	}
	/**
	 *
	 */	
	public Boolean getActive() 
	{
		return active;
	}
	/**
	 *
	 */	
	public int getMapperCountTotal() 
	{
		return mapperCountTotal;
	}
	/**
	 *
	 */
	public int getReducerCountTotal() 
	{
		return reducerCountTotal;
	}
}

