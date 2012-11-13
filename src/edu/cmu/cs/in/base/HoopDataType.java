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
public class HoopDataType extends HoopAnnotation
{
	public static final int CLASS=0; // This is the default if the class isn't used for a specific type wrapper
	public static final int INT=1;
	public static final int LONG=2;
	public static final int STRING=3;
	public static final int FLOAT=4;
	public static final int BOOLEAN=5;
	public static final int ENUM=6;
	public static final int TABLE=7;	
	public static final int DOCUMENT=8;
	
	public static final int DATE=9; // Important higher level datatype, can be cast from and to an int
	public static final int COLOR=10; // Vague type that is assumed to be captured in a string
	public static final int FONT=11; // Vague type that is assumed to be captured in a string
	public static final int URI=12; // Vague type that is assumed to be captured in a string
	public static final int URL=13; // Vague type that is assumed to be captured in a string
	
	public static final int typeCount=14;
	
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
	public static String typesToString ()
	{
		StringBuffer formatter=new StringBuffer ();
		
		for (int i=0;i<HoopDataType.typeCount;i++)
		{
			if (i>0)
				formatter.append(",");
			
			formatter.append(HoopDataType.typeToString(i));
		}
		
		return (formatter.toString());
	}
	/**
	 * 
	 */
	public static String typeToString (int aType)
	{
		switch (aType)
		{
			case CLASS:
						return ("CLASS");
			case INT:						
						return ("INT");
			case LONG:			
						return ("LONG");
			case STRING:			
						return ("STRING");
			case FLOAT:			
						return ("FLOAT");
			case BOOLEAN:			
						return ("BOOLEAN");
			case ENUM:			
						return ("ENUM");
			case TABLE:			
						return ("TABLE");
			case DOCUMENT:			
						return ("DOCUMENT");
			case DATE:			
						return ("DATE");
			case COLOR:			
						return ("COLOR");
			case FONT:			
						return ("FONT");
			case URI:			
						return ("URI");
			case URL:			
						return ("URL");
		}
		
		return ("CLASS");		
	}
	/**
	 * 
	 */
	public String typeToString ()
	{
		switch (type)
		{
			case CLASS:
						return ("CLASS");
			case INT:						
						return ("INT");
			case LONG:			
						return ("LONG");
			case STRING:			
						return ("STRING");
			case FLOAT:			
						return ("FLOAT");
			case BOOLEAN:			
						return ("BOOLEAN");
			case ENUM:			
						return ("ENUM");
			case TABLE:			
						return ("TABLE");
			case DOCUMENT:			
						return ("DOCUMENT");
			case DATE:			
						return ("DATE");
			case COLOR:			
						return ("COLOR");
			case FONT:			
						return ("FONT");
			case URI:			
						return ("URI");
			case URL:			
						return ("URL");
		}
		
		return ("CLASS");
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
	/**
	 * 
	 */
	public static Boolean booleanStringValue (String aString)
	{
		if (
				(aString.equalsIgnoreCase("1")==true) ||
				(aString.equalsIgnoreCase("true")==true) ||
				(aString.equalsIgnoreCase("yes")==true)
			)
		{
			return (true);
		}
		
		return (false);
	}
}
