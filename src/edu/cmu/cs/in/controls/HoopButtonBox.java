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

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;

import edu.cmu.cs.in.controls.base.HoopJPanel;

/**
 * 
 */
public class HoopButtonBox extends HoopJPanel
{	
	private static final long serialVersionUID = -1L;
	
	public static int HORIZONTAL=1;
	public static int VERTICAL=2;

	private int direction=HORIZONTAL;
	
	/**
	 * 
	 */
	public HoopButtonBox ()
	{
		setClassName ("HoopButtonBox");
		debug ("HoopButtonBox ()");
		
		this.setLayout(new BoxLayout (this,BoxLayout.X_AXIS));
	}
	/**
	 * 
	 */
	public HoopButtonBox (int aDirection)
	{
		setClassName ("HoopButtonBox");
		debug ("HoopButtonBox ()");
		
		if (aDirection==HoopButtonBox.HORIZONTAL)
			this.setLayout(new BoxLayout (this,BoxLayout.X_AXIS));
		else
			this.setLayout(new BoxLayout (this,BoxLayout.Y_AXIS));
	}		
	/**
	 * 
	 */
	public void setDirection(int direction) 
	{
		this.direction = direction;
	}
	/**
	 * 
	 */
	public int getDirection() 
	{
		return direction;
	}
	/**
	 * 
	 */
	public void addComponent (JComponent aComponent)
	{
		debug ("addComponent ()");
		
		this.add(aComponent);
		
		if (direction==HORIZONTAL)
			this.add (Box.createRigidArea(new Dimension(0,2)));
		else
			this.add (Box.createRigidArea(new Dimension(2,0)));		
	}
}
