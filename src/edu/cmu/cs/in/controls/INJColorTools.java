/** 
 * Author: Martin van Velsen <vvelsen@cs.cmu.edu>
 * 
 * This source set uses code written for other various graduate 
 * courses and is part of a larger research effort in the field of
 * interactive narrative (IN). Courses that have contribute
 * to this source base are:
 * 
 * 	05-834 Applied Machine Learning
 *  11-719 Computational Models of Discourse Analysis
 *  11-741 Information Retrieval
 * 
 */

package edu.cmu.cs.in.controls;

import java.awt.*;

import edu.cmu.cs.in.base.INFeatureMatrixBase;

public class INJColorTools extends INFeatureMatrixBase
{
	/**------------------------------------------------------------------------------------ 
	 * Parses the specified Color as a string. 
	 * @param representation of the color as a 24-bit integer, the format of the string can be either htmlcolor #xxxxxx or xxxxxx.
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
	//-------------------------------------------------------------------------------------
}
