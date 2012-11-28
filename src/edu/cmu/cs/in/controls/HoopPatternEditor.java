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

import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import edu.cmu.cs.in.controls.base.HoopJDialog;

/**
 * 
 */
public class HoopPatternEditor extends HoopJDialog implements ActionListener 
{		
	private static final long serialVersionUID = -2275264716559312322L;
	
	/**
     * 
     */
    public HoopPatternEditor (int aMode,JFrame frame, boolean modal) 
	{
		super (frame, modal,"Hoop Pattern Editor");
		
		setClassName ("HoopPatternEditor");
		debug ("HoopPatternEditor ()");
				
		JPanel contentFrame=this.getFrame();
		
		Box contentBox = new Box (BoxLayout.Y_AXIS);
		

		contentFrame.add (contentBox);			
    }	
}
