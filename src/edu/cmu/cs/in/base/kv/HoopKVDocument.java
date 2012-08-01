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

package edu.cmu.cs.in.base.kv;

import edu.cmu.cs.in.base.HoopDataType;

/**
 * Perhaps the most important data structure in the Hoop system is the
 * document class. Since it is derived from the Hoop KV Class we should
 * think about it as a KV object but with specialized methods and fixed
 * known member variables, such as rank and score. You can freely use 
 * this class as a KV Class and even as an entry in a KV List if so desired.
 * 
 * Our document representation class is directly derived from the Hoop KV abstract
 * class. For now this should suffice but we might have to derive from the KV Table
 * instead to allow for better data modeling.
 * 
 */
public class HoopKVDocument extends HoopKVClass implements HoopKVInterface
{    						
	private HoopKVInteger rank=null;
	private HoopKVFloat score=null;
	
	private HoopKVString description=null;
	private HoopKVString author=null;
	
	/**
	 *
	 */
    public HoopKVDocument ()
    {
    	setType (HoopDataType.CLASS);
    	
    	rank=new HoopKVInteger (1,"1");
    	score=new HoopKVFloat ((float) 1.0,"1.0");
    	
    	description=new HoopKVString ();
    	author=new HoopKVString ();
    }
	/**
	 *
	 */    
	public long getRank() 
	{
		return rank.getKey();
	}
	/**
	 *
	 */	
	public void setRank(int aRank) 
	{
		this.rank.setKey(aRank);
	}
	/**
	 *
	 */	
	public float getScore() 
	{
		return score.getKey();
	}
	/**
	 *
	 */	
	public void setScore(float aScore) 
	{
		this.score.setKey(aScore);
	}    
	/**
	 *
	 */
	public void setDocID(String aKey) 
	{
		this.setKey (aKey);
	}    
	/**
	 *
	 */
	public String getDocID() 
	{
		return this.getKey();
	}
	/**
	 *
	 */
	@Override
	public Object getValue() 
	{
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 *
	 */
	@Override
	public Object getValue(int anIndex) 
	{
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 *
	 */
	@Override
	public String getValueAsString() 
	{
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 *
	 */
	@Override
	public String getValueAsString(int anIndex) 
	{
		// TODO Auto-generated method stub
		return null;
	}
}
