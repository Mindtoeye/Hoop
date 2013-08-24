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

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.JFrame;

import edu.cmu.cs.in.controls.base.HoopJComponent;

public class HoopCircleCounter extends HoopJComponent
{
	private static final long serialVersionUID = 2996883829374520467L;
	
	private int total=1;
	private int slice=1;
	
	/**
    *
    */
	public HoopCircleCounter() 
	{
		setClassName ("HoopCircleCounter");
		debug ("HoopCircleCounter ()");
		
		this.setMinimumSize(new Dimension (20,20));
		this.setPreferredSize(new Dimension (20,20));
	}
	/**
	 * 
	 */
	public void setValues (int aTotal,int aSlice)
	{
		total=aTotal;
		slice=aSlice;
		
		this.repaint();
	}
	/**
    * 
    */
	public void paint(Graphics g) 
	{
		//debug ("paint ()");
		
		super.paint(g);
				
		Rectangle rect=new Rectangle ();
		
		rect.x=0;
		rect.y=0;
		rect.width=this.getWidth();
		rect.height=this.getHeight();
		
		drawPie((Graphics2D) g,rect);
	}
	/**
    *
    * @param g
    * @param area
    * @param slices
    */
	private void drawPie(Graphics2D g, Rectangle area) 
	{
		//debug ("drawPie ("+area.x+","+area.y+","+area.width+","+area.height+")");
				
		int xOffset=0;
		int yOffset=0;
		int effectiveDiam=area.height;
		
		if (area.height<area.width)
		{
			effectiveDiam=area.height;
			
			xOffset=(int) ((area.width-area.height)/2);
		}
		else
		{
			effectiveDiam=area.width;
			
			yOffset=(int) ((area.height-area.width)/2);
		}
            
		double curValue = 0.0D;
		int startAngle = 0;
		
		startAngle = (int) (curValue * 360 / total);
		int arcAngle = (int) (slice * 360 / total);
		
		g.setColor (new Color (220,220,220));
		g.fillArc (xOffset+area.x,
				   yOffset+area.y,
				   effectiveDiam,
				   effectiveDiam,
				   startAngle,
				   arcAngle);
		
		g.setColor (new Color (0,0,0));
		g.drawArc (xOffset+area.x, 
				   yOffset+area.y,
				   effectiveDiam,
				   effectiveDiam,
				   startAngle,
				   arcAngle);		
	}
	/**
	 * 
	 */
	public void setText (String aMessage)
	{
		// Nothing for now, will be shown as a label below the pie chart
	}
	/**
	 *
	 * @param args
	 */
	public static void main(String[] args) 
	{
	    JFrame f = new JFrame("Hoop Pie Chart Test");
	    f.setSize(400, 150);
	    Container content = f.getContentPane();
	    content.setBackground(Color.white);
	    content.setLayout(new FlowLayout()); 
	    content.add(new HoopCircleCounter ());
	    f.setVisible(true);
	}	
}
