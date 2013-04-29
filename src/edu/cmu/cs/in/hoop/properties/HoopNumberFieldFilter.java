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

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

class HoopNumberFieldFilter extends PlainDocument
{    	        
	//private static final long serialVersionUID = 1L;
	public static final char DOT = '.';
	public static final char NEGATIVE = '-';
	public static final String BLANK = "";
	public static final int DEF_PRECISION = 2;

	public static final int NUMERIC = 2;
	public static final int DECIMAL = 3;

	public static final String FM_NUMERIC = "0123456789";
	public static final String FM_DECIMAL = FM_NUMERIC + DOT;

	public int maxLength = 0;
	public int format = NUMERIC;
	public String negativeChars = BLANK;
	public String allowedChars = null;
	public boolean allowNegative = false;
	public int precision = 0;
        
	private static final long serialVersionUID = 1L;

	public HoopNumberFieldFilter()
	{
		super();
	}

	public void insertString(int offset, String  str, AttributeSet attr) throws BadLocationException
	{
		String text = getText(0,offset) + str + getText(offset,(getLength() - offset));

		if( str == null || text == null)
			return;

		for(int i=0; i<str.length(); i++)
		{
			if((allowedChars+negativeChars).indexOf(str.charAt(i)) == -1)
				return;
		}

		int precisionLength = 0, dotLength = 0, minusLength = 0;
		int textLength = text.length();

		try
		{
			if( format == NUMERIC )
			{
				new Long(text);
			}
			else if( format == DECIMAL )
			{
				new Double(text);

				int dotIndex = text.indexOf(DOT);
				if( dotIndex != -1 )
				{
					dotLength = 1;
					precisionLength = textLength - dotIndex - dotLength;

					if( precisionLength > precision )
						return;
				}
			}
		}
		catch(Exception ex)
		{
			return;
		}

		if(text.startsWith(""+NEGATIVE) )
		{
			if( !allowNegative )
				return;
			else
				minusLength = 1;
		}

		if( maxLength < (textLength - dotLength - precisionLength - minusLength) )
			return;

		super.insertString(offset, str, attr);
	}
}
