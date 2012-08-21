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

/**
 * You can think of the KV Class as a KV object where member variables
 * are stored in the 'values' variable of the HoopKV class. We use the
 * same mechanism as found in the serializable properties classes where
 * we maintain an internal list of HoopKV objects inside the KV class.
 * 
 * Derived classes should be careful when accessing member variables
 * and parent variables since the meaning can change depending on how
 * KV Class objects are used.
 */
public class HoopKVClass extends HoopKV implements HoopKVInterface, Serializable
{    					
	private static final long serialVersionUID = -562572734161851194L;
	private String key="undefined";
	private ArrayList<HoopKV> variables=null;
	
	/**
	 *
	 */
    public HoopKVClass ()
    {
    	setType (HoopDataType.CLASS);
    	
    	variables=new ArrayList<HoopKV> ();
    }    
    /**
     * 
     */
    public void addVariable (HoopKV aVar)
    {
    	variables.add(aVar);
    }
    /*
     * 
     */
    public ArrayList<HoopKV> getVariables ()
    {
    	return (variables);
    }
	/**
	 *
	 */
	public String getKey() 
	{
		return key;
	}
	/**
	 *
	 */
	public void setKey(String key) 
	{
		this.key = key;
	}    
	/**
	 *
	 */
	@Override
	public String getKeyString() 
	{
		return key;
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
