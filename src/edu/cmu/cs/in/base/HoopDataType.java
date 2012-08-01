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

/**
 * IMPORTANT: this class is not derived from HoopRoot because it needs to be small
 * and compact. We use this class as the base class for objects that are used in
 * arrays, lists and tables and we can't waste memory.
 */
public class HoopDataType
{
	public static final int CLASS=0; // This is the default if the class isn't used for a specific type wrapper
	public static final int INT=1;
	public static final int STRING=2;
	public static final int FLOAT=3;
	public static final int BOOLEAN=4;
	public static final int ENUM=5;
	public static final int TABLE=6;	
	public static final int DOCUMENT=7;
	
	public static final int COLOR=8; // Vague type that is assumed to be captured in a string
	public static final int FONT=9; // Vague type that is assumed to be captured in a string
	public static final int URI=10; // Vague type that is assumed to be captured in a string
	public static final int URL=11; // Vague type that is assumed to be captured in a string
	
	protected int type=CLASS;
	protected String typeValue="String";
	
	/**
	 *
	 */
    public HoopDataType () 
    {

    }    
	/**
	 *
	 */
    public HoopDataType (int aType,String aValue) 
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
			case CLASS:
							return ("Class");		
			case INT:
							return ("Integer");
			case STRING:
							return ("String");
			case FLOAT:
							return ("Float");
			case BOOLEAN:
							return ("Boolean");
			case ENUM:
							return ("ENUM");							
			case TABLE:
							return ("Table");
			case FONT:
							return ("Font");
			case COLOR:
							return ("Color");
			case URI:
							return ("URI");
			case URL:
							return ("URL");				
		}
		
		return ("Class");
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
