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

/**
 * 
 * @author vvelsen
 *
 */
public class INKVType
{
	public static final int INT=1;
	public static final int STRING=2;
	public static final int FLOAT=3;
	public static final int BOOLEAN=4;
	public static final int TABLE=5;
	
	private int type=INT;
	private String typeValue="String";
	
	/**
	 *
	 */
    public INKVType () 
    {

    }    
	/**
	 *
	 */
    public INKVType (int aType,String aValue) 
    {
    	type=aType;
    	setTypeValue(aValue);
    }
    /**
    * 
 	*/    
	public int getType() 
	{
		return type;
	}
    /**
    * 
 	*/	
	public void setType(int type) 
	{
		this.type = type;
	}    
	/**
	 * 
	 */
	public String typeToString ()
	{
		switch (type)
		{
			case INT:
							return ("Integer");
			case STRING:
							return ("String");
			case FLOAT:
							return ("Float");
			case BOOLEAN:
							return ("Boolean");
			case TABLE:
							return ("Table");
		}
		
		return ("Integer");
	}
	/*
	 * 
	 */
	public void setTypeValue(String typeValue) 
	{
		this.typeValue = typeValue;
	}
	/*
	 * 
	 */
	public String getTypeValue() 
	{
		return typeValue;
	}	
}
