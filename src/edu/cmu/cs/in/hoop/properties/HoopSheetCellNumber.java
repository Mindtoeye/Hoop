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

import javax.swing.text.Document;

import edu.cmu.cs.in.controls.base.HoopJTextField;
import edu.cmu.cs.in.hoop.properties.HoopNumberFieldFilter;

public class HoopSheetCellNumber extends HoopJTextField
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected HoopNumberFieldFilter numberFieldFilter;
    
    /**
	 * 
	 */	    
    public HoopSheetCellNumber() 
    {
        this(10,HoopNumberFieldFilter.DECIMAL);
        
		setClassName ("HoopSheetCellNumber");
		debug ("HoopSheetCellNumber ()");
        
        numberFieldFilter = new HoopNumberFieldFilter();        
    }
    /**
	 * 
	 */	
    public HoopSheetCellNumber(int iMaxLen)
    {    	
        this(iMaxLen,HoopNumberFieldFilter.NUMERIC);
        
		setClassName ("HoopSheetCellNumber");
		debug ("HoopSheetCellNumber (int)");        
        
        numberFieldFilter = new HoopNumberFieldFilter();        
    }
    /**
	 * 
	 */	
    public HoopSheetCellNumber(int iMaxLen, int iFormat)
    {
    	if (numberFieldFilter==null)
            numberFieldFilter = new HoopNumberFieldFilter();
    	
        setMaxLength(iMaxLen);
        setFormat(iFormat);

        super.setDocument (numberFieldFilter);
        
		setClassName ("HoopSheetCellNumber");
		debug ("HoopSheetCellNumber (int,int)");  
    }
    /**
	 * 
	 */	
    public void setMaxLength(int maxLen)
    {
    	debug ("setMaxLength ()");
    	
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
    	debug ("setEnabled ()");
    	
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
    	debug ("setEditable ()");
    	
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
    	debug ("setPrecision ()");
    	
        if( numberFieldFilter.format == HoopNumberFieldFilter.NUMERIC )
            return;

        if (iPrecision >= 0)
        	numberFieldFilter.precision = iPrecision;
        else
        	numberFieldFilter.precision = HoopNumberFieldFilter.DEF_PRECISION;
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
    	debug ("getNumber ()");
    	
        Number number = null;
        
        if( numberFieldFilter.format == HoopNumberFieldFilter.NUMERIC )
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
        	case HoopNumberFieldFilter.DECIMAL:
        										numberFieldFilter.format = HoopNumberFieldFilter.DECIMAL;
        										numberFieldFilter.precision = HoopNumberFieldFilter.DEF_PRECISION;
        										numberFieldFilter.allowedChars = HoopNumberFieldFilter.FM_DECIMAL;
        										break;
            
            case HoopNumberFieldFilter.NUMERIC:
            									numberFieldFilter.format = HoopNumberFieldFilter.NUMERIC;
            									numberFieldFilter.precision = 0;
            									numberFieldFilter.allowedChars = HoopNumberFieldFilter.FM_NUMERIC;
            									break;
            default:
            									numberFieldFilter.format = HoopNumberFieldFilter.NUMERIC;
            									numberFieldFilter.precision = 0;
            									numberFieldFilter.allowedChars = HoopNumberFieldFilter.FM_NUMERIC;
            									break;
        }
    }
    /**
	 * 
	 */	
    public void setAllowNegative(boolean b)
    {
    	numberFieldFilter.allowNegative = b;

        if (b)
        	numberFieldFilter.negativeChars = ""+HoopNumberFieldFilter.NEGATIVE;
        else
        	numberFieldFilter.negativeChars = HoopNumberFieldFilter.BLANK;
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
