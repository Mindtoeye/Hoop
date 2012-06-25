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

import java.awt.*;

import edu.cmu.cs.in.base.INBase;

public class INJColorTools extends INBase
{
	/** 
	 * Parses the specified Color as a string. 
	 * @param nm of the color as a 24-bit integer, the format of the string can be either htmlcolor #xxxxxx or xxxxxx.
	 * @return the new color.
	 * @exception NumberFormatException if the format of the string does not comply with rules or illegal charater for the 24-bit integer as a string.
	 */
	public static Color parse (String nm ) throws NumberFormatException 
	{
		if (nm.startsWith("#")) 
		{
			nm = nm.substring(1);
		}
		
		nm = nm.toLowerCase();
    
		if (nm.length() > 6) 
		{
			throw new NumberFormatException ("nm is not a 24 bit representation of the color, string too long"); 
		}
		
		//System.out.println("nm=" + nm );
		Color color = new Color( Integer.parseInt( nm , 16 ) );
		return color;
	}
}
