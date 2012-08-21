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

import java.io.Serializable;

import edu.cmu.cs.in.base.HoopDataType;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;

/**
* Any data created by hoops (HoopBase) will use the KV object in some
* form. HoopKV objects represent Key/Value pairs and form the most basic
* data structure unit within the Hoop system. If your hoop can represent
* language in a list, table or other structure containing KV values, then
* any other hoop should be able to inspect and process your data.
* 
* Additionally the KV class allows native support for multi-value 
* representations. This allows you to create tables for example where
* the key represents the index and the list of values represents the
* columns. Keep in mind that each KV instance can have any number of
* values stored and that means you might have to add additional
* housekeeping code to keep a table symmetrical. Support classes are
* provided to make this task easier by representing KV tables natively,
* which enforces each KV row to have an equal number of values. Empty
* values are added in case padding is needed for KVs (rows) that have
* less than the maximum number of values (rows). Please see the Naive
* Bayes classes for a good example of how this is used.
* 
* We should deliberately not derive from HoopBase since that comes with a
* large memory footprint. Currently the key and value types are somewhat
* pre-determined and immutable. That means that for a long while any
* code you write will have to check first the type of key and the type
* of value before using the contents of a KV object.
*/
public class HoopKVInteger extends HoopKV implements HoopKVInterface, Serializable
{    			
	/**
	 * 
	 */
	private static final long serialVersionUID = 8108471626856740809L;
	private Integer key=0;
		
	/**
	 *
	 */
    public HoopKVInteger ()
    {
    	setType (HoopDataType.INT);
    	
    	// Make sure we have at least one entry for quick access
    	values.add(new String ("0"));
    }
	/**
	 *
	 */
    public HoopKVInteger (Integer aKey,String aValue) 
    {	   
    	setType (HoopDataType.INT);
    	
    	// Make sure we have at least one entry for quick access
    	values.add(new String ("0"));
    	
    	setKey (aKey);
    	setValue (aValue);
    }  
	/**
	 *
	 */
	public Integer getKey() 
	{
		return key;
	}
	/**
	 *
	 */
	public void setKey(Integer key) 
	{
		this.key = key;
	}
	/**
	 * 
	 */
	public void incKey ()
	{
		//HoopBase.debug("HoopKVInteger","Inc " + this.key + " for: " +this.getValue());
		
		this.key++;
	}
	/**
	 *
	 */
	public String getValue() 
	{
		return (String) (values.get(0));
	}
	/**
	 *
	 */
	public String getValue(int anIndex) 
	{
		if (anIndex>values.size())
			return ("0");
		
		return (String) (values.get(anIndex));
	}
	/**
	 *
	 */
	public String getValueAsString() 
	{
		return (String) (values.get(0));
	}
	/**
	 *
	 */
	public String getValueAsString(int anIndex) 
	{
		if (anIndex>values.size())
			return ("0");
		
		return (String) (values.get(anIndex));
	}		
	/**
	 *
	 */
	public void setValue(String value) 
	{
		values.set(0,value);
	}
	/**
	 *
	 */
	public void setValue(String value, int anIndex) 
	{
		if (anIndex>(values.size()-1))
		{
			// fill with bogus data up to the requested element
			
			for (int i=(values.size ()-1);i<anIndex;i++)
			{
				values.add(new String ("0"));
			}
		}
		
		values.set(anIndex,value);
	}	
	/**
	 *
	 */	
	public String getKeyString() 
	{		
		return key.toString();
	}
	/**
	 *
	 */	
	public void setKeyString(String keyString) 
	{
		key=Integer.parseInt(keyString);
	}  
}
