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

package edu.cmu.cs.in.controls.wizard;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/**
 * 
 */
public class HoopWizardPageListRenderer extends JLabel implements ListCellRenderer 
{
	private static final long serialVersionUID = -3192181979337623468L;

	/**
	 * 
	 */
	public HoopWizardPageListRenderer() 
    {
		setFont(new Font("Dialog", 1, 10));
		setHorizontalAlignment (SwingConstants.CENTER); 
        setOpaque (true);
        setBorder (new EmptyBorder(3,3,3,3));
    }
	/**
	 * 
	 */
    public Component getListCellRendererComponent (JList list, 
    											   Object value, 
    											   int index, 
    											   boolean isSelected, 
    											   boolean cellHasFocus) 
    {
        setText(value.toString());
     
        if (cellHasFocus==true)
        {
        	setBackground(new Color (136,207,255));
        }
        else
        {
        	if (isSelected==true)
        	{
        		setBackground (new Color (136,207,255));
        	}
        	else
        	{
        		if (index % 2 == 0) 
        			setBackground(new Color (250,250,250));
        		else 
        			setBackground(new Color (238,238,238));
        	}	
        }	

        return this;
    }
}
