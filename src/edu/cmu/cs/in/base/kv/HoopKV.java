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
import java.util.ArrayList;

import edu.cmu.cs.in.base.HoopDataType;
//import edu.cmu.cs.in.base.HoopRoot;

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
public class HoopKV extends HoopDataType implements HoopKVInterface, Serializable
{    			
	private static final long serialVersionUID = -120610909596827250L;
	protected ArrayList <Object> values=null;
		
	private HoopKV parentKV=null;
	
	/**
	 *
	 */
    public HoopKV ()
    {
    	values=new ArrayList <Object>();
    }
	/** 
	 * @return
	 */
	public HoopKV getParentKV() 
	{
		return parentKV;
	}
	/** 
	 * @param parentKV
	 */
	public void setParentKV(HoopKV parentKV) 
	{
		this.parentKV = parentKV;
	}    
    /**
     * 
     */
    public void setKeyString (String aKey)
    {
    	// Implement in child!
    }
    /**
     * 
     */
    public String getKeyString ()
    {
    	return ("abstract");
    }
	/**
	 *
	 */
    public int getValueSize ()
    {
    	return (values.size());
    }
    /**
     * 
     */
    public void setValuesRaw (ArrayList<Object> newValues)
    {
    	values=newValues;
    }
	/**
	 *
	 */
    public ArrayList <Object> getValuesRaw ()
    {
    	return (values);
    }
    /**
     * 
     */
    public void add (Object newValue)
    {
    	values.add(newValue);
    }
    /**
     * 
     */
	@Override
	public Object getValue() 
	{
		return new String ("abstract");
	}
	/**
	 * 
	 */
	@Override
	public Object getValue(int anIndex) 
	{
		return (values.get(anIndex));
	}
	/**
	 * 
	 */
	@Override
	public String getValueAsString() 
	{
		return (String) (values.get(0));
	}
	/**
	 * 
	 */
	@Override
	public String getValueAsString(int anIndex) 
	{
		//HoopRoot.debug ("HoopKV","getValueAsString ("+anIndex+","+values.size()+")");
		
		if (anIndex<0)
			return ("NaS");
		
		if (anIndex>(values.size()-1))
			return ("0");
					
		Object transformer=values.get(anIndex);
			
		return (transformer.toString());	
	}
	/**
	 * Insert a new value and move all the other existing values down
	 */
	public void bump(Object value,String aLabel) 
	{						
		// PROCESS NEW LABEL AND MOVE THE OLD ONE UP!
		
		values.add (0,value);
	}	
	/**
	 * Insert a new value and move all the other existing values down
	 */
	public void bump(Object value) 
	{						
		// PROCESS NEW LABEL AND MOVE THE OLD ONE UP!
		
		values.add (0,value);
	}	
	/**
	 * 
	 */
	public void toss (int valueIndex)
	{
		values.remove(valueIndex);
	}
	/**
	 *
	 */
	public void setValue(Object value) 
	{
		setValue (value,0);
	}			
	/**
	 *
	 */
	public void setValue(Object value, int anIndex) 
	{
		if (anIndex>(values.size()-1))
		{
			// fill with bogus data up to the requested element
			
			for (int i=(values.size ()-1);i<anIndex;i++)
			{
				values.add("0");
			}
		}
		
		values.set(anIndex,value);
	}		
	/**
	 * 
	 */
	public void addValueAsUniqueString (String aString)
	{
		Boolean found=false;
		
		for (int i=0;i<values.size();i++)
		{
			String tester=(String) values.get(i);
			
			if (tester.equalsIgnoreCase(aString)==true)
			{
				found=true;
			}
		}
		
		if (found==false)
		{
			values.add(aString);
		}
	}
}
