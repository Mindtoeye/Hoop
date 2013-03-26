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

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JScrollPane;

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
	
	private Box buttonBox=null;
	private JScrollPane scroller=null;
	
	/**
	 * 
	 */
	public HoopButtonBox ()
	{
		setClassName ("HoopButtonBox");
		debug ("HoopButtonBox ()");
		
		//this.setBorder (BorderFactory.createLineBorder(Color.red));		
		this.setLayout(new BoxLayout (this,BoxLayout.X_AXIS));
		
		buttonBox=new Box (BoxLayout.X_AXIS);
		buttonBox.add (Box.createHorizontalGlue());
		
		scroller=new JScrollPane (buttonBox);
		//scroller.setBorder (BorderFactory.createLineBorder(Color.black));
		scroller.setBorder(BorderFactory.createEmptyBorder());
		scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		
		this.add(scroller);
	}
	/**
	 * 
	 */
	public HoopButtonBox (int aDirection)
	{
		setClassName ("HoopButtonBox");
		debug ("HoopButtonBox ()");
		
		if (aDirection==HoopButtonBox.HORIZONTAL)
		{
			this.setLayout(new BoxLayout (this,BoxLayout.X_AXIS));
			
			buttonBox = new Box (BoxLayout.X_AXIS);
			
			buttonBox.add(Box.createHorizontalGlue());
			
			scroller=new JScrollPane (buttonBox);
			
			this.add(scroller);
		}
		else
		{
			this.setLayout(new BoxLayout (this,BoxLayout.X_AXIS));
			
			buttonBox = new Box (BoxLayout.Y_AXIS);
			
			buttonBox.add(Box.createVerticalGlue());
			
			scroller=new JScrollPane (buttonBox);
			
			this.add(scroller);
		}
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
		
		buttonBox.add(aComponent,buttonBox.getComponents().length-1);
		
		if (direction==HORIZONTAL)
			buttonBox.add (Box.createRigidArea(new Dimension(2,0)),buttonBox.getComponents().length-1);
		else
			buttonBox.add (Box.createRigidArea(new Dimension(0,2)),buttonBox.getComponents().length-1);		
	}
}
