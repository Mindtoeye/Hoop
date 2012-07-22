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

package edu.cmu.cs.in.hoop.properties;

import java.awt.*;

/**
 * This clas extends the color class and add features like valueOf to 
 * the color class to convert HTML color format to a Color object.
 */
public class HoopColorUtil  
{
	/** 
	 * Parses the specified Color as a string. 
	 * @param representation of the color as a 24-bit integer, the format of the string can be either htmlcolor #xxxxxx or xxxxxx.
	 * @return the new color.
	 * @exception NumberFormatException if the format of the string does not comply with rules or illegal charater for the 24-bit integer as a string.
	 */
	public static Color parse(String nm ) throws NumberFormatException 
	{
		if (nm.equals("")==true)
			return (new Color (255,255,255));
	  
		if ( nm.startsWith("#") ) 
		{
			nm = nm.substring(1);
		}
    
		if ( nm.startsWith("0x") || nm.startsWith("0X")) 
		{
			nm = nm.substring(2);
		} 
       
		nm = nm.toLowerCase();
		
		/*
		if (nm.length() > 6) 
		{
			throw new NumberFormatException("nm is not a 24 bit representation of the color, string too long"); 
		}
		 */
		
		System.out.println("nm=" + nm );
		Color color=new Color(Integer.parseInt(nm,16));
		return color;
	} 
	/**
	 *
	 */   	
	public static String toHex (Color aColor)
	{
		return (Integer.toHexString(aColor.getRGB() & 0x00ffffff));
	}
}


