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

import java.awt.Color;
import javax.swing.*;
import javax.swing.text.*;

import edu.cmu.cs.in.hoop.properties.INHoopNumberFieldFilter;

public class INHoopSheetCellNumber extends JTextField
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected INHoopNumberFieldFilter numberFieldFilter;
    
    /**
	 * 
	 */	    
    public INHoopSheetCellNumber() 
    {
        this(10,INHoopNumberFieldFilter.DECIMAL);
        numberFieldFilter = new INHoopNumberFieldFilter();        
    }
    /**
	 * 
	 */	
    public INHoopSheetCellNumber(int iMaxLen)
    {    	
        this(iMaxLen,INHoopNumberFieldFilter.NUMERIC);
        numberFieldFilter = new INHoopNumberFieldFilter();        
    }
    /**
	 * 
	 */	
    public INHoopSheetCellNumber(int iMaxLen, int iFormat)
    {
    	if (numberFieldFilter==null)
            numberFieldFilter = new INHoopNumberFieldFilter();
    	
        setMaxLength(iMaxLen);
        setFormat(iFormat);

        super.setDocument (numberFieldFilter);
    }
    /**
	 * 
	 */	
    public void setMaxLength(int maxLen)
    {
        if (maxLen > 0)
        	numberFieldFilter.maxLength = maxLen;
        else
        	numberFieldFilter.maxLength = 0;
    }
    /**
	 * 
	 */	
    public int getMaxLength()
    {
      return numberFieldFilter.maxLength;
    }
    /**
	 * 
	 */	
    public void setEnabled(boolean enable)
    {
        super.setEnabled(enable);

        if( enable )
        {
            setBackground(Color.white);
            setForeground(Color.black);
        }
        else
        {
            setBackground(Color.lightGray);
            setForeground(Color.darkGray);
        }
    }
    /**
	 * 
	 */	
    public void setEditable(boolean enable)
    {
        super.setEditable(enable);

        if( enable )
        {
            setBackground(Color.white);
            setForeground(Color.black);
        }
        else
        {
            setBackground(Color.lightGray);
            setForeground(Color.darkGray);
        }
    }
    /**
	 * 
	 */	
    public void setPrecision(int iPrecision)
    {
        if( numberFieldFilter.format == INHoopNumberFieldFilter.NUMERIC )
            return;

        if (iPrecision >= 0)
        	numberFieldFilter.precision = iPrecision;
        else
        	numberFieldFilter.precision = INHoopNumberFieldFilter.DEF_PRECISION;
    }
    /**
	 * 
	 */	
    public int getPrecision()
    {
      return numberFieldFilter.precision;
    }
    /**
	 * 
	 */	
    public Number getNumber()
    {
        Number number = null;
        if( numberFieldFilter.format == INHoopNumberFieldFilter.NUMERIC )
            number = new Integer(getText());
        else
            number = new Double(getText());

        return number;
    }
    /**
	 * 
	 */	
    public void setNumber(Number value)
    {
        setText(String.valueOf(value));
    }
    /**
	 * 
	 */	
    public int getInt()
    {
        return Integer.parseInt(getText());
    }
    /**
	 * 
	 */	
    public void setInt(int value)
    {
        setText(String.valueOf(value));
    }
    /**
	 * 
	 */	
    public float getFloat()
    {
        return (new Float(getText())).floatValue();
    }
    /**
	 * 
	 */	
    public void setFloat(float value)
    {
        setText(String.valueOf(value));
    }
    /**
	 * 
	 */	
    public double getDouble()
    {
        return (new Double(getText())).doubleValue();
    }
    /**
	 * 
	 */	
    public void setDouble(double value)
    {
        setText(String.valueOf(value));
    }
    /**
	 * 
	 */	
    public int getFormat()
    {
        return numberFieldFilter.format;
    }
    /**
	 * 
	 */	
    public void setFormat(int iFormat)
    {
        switch(iFormat)
        {
            case INHoopNumberFieldFilter.NUMERIC:
            default:
            	numberFieldFilter.format = INHoopNumberFieldFilter.NUMERIC;
            	numberFieldFilter.precision = 0;
            	numberFieldFilter.allowedChars = INHoopNumberFieldFilter.FM_NUMERIC;
                break;

            case INHoopNumberFieldFilter.DECIMAL:
            	numberFieldFilter.format = INHoopNumberFieldFilter.DECIMAL;
            	numberFieldFilter.precision = INHoopNumberFieldFilter.DEF_PRECISION;
            	numberFieldFilter.allowedChars = INHoopNumberFieldFilter.FM_DECIMAL;
                break;
        }
    }
    /**
	 * 
	 */	
    public void setAllowNegative(boolean b)
    {
    	numberFieldFilter.allowNegative = b;

        if( b )
        	numberFieldFilter.negativeChars = ""+INHoopNumberFieldFilter.NEGATIVE;
        else
        	numberFieldFilter.negativeChars = INHoopNumberFieldFilter.BLANK;
    }
    /**
	 * 
	 */	
    public boolean isAllowNegative()
    {
        return numberFieldFilter.allowNegative;
    }
    /**
	 * 
	 */	
    public void setDocument(Document document)
    {
    	
    }
}				
