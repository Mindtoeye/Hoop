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

public class HoopPieChart extends HoopJComponent
{
	private static final long serialVersionUID = 155658985885978298L;
	
	HoopSlice[] slices = 
	{ 
		new HoopSlice(5, Color.black), 
		new HoopSlice(33, Color.green),
		new HoopSlice(20, Color.yellow), 
		new HoopSlice(15, Color.red) 
	};
   
	/**
    *
    */
	public HoopPieChart() 
	{
		setClassName ("HoopPieChart");
		debug ("HoopPieChart ()");
		
		this.setMinimumSize(new Dimension (20,20));
		this.setPreferredSize(new Dimension (20,20));
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
		
		drawPie((Graphics2D) g,rect, slices);
	}
	/**
    *
    * @param g
    * @param area
    * @param slices
    */
	private void drawPie(Graphics2D g, Rectangle area, HoopSlice[] slices) 
	{
		//debug ("drawPie ("+area.x+","+area.y+","+area.width+","+area.height+"->"+slices.length+")");
		
		double total = 0.0D;
		
		int effectiveDiam=area.height;
		
		if (area.height<area.width)
		{
			effectiveDiam=area.height;
		}
		else
			effectiveDiam=area.width;
      
		for (int i = 0; i < slices.length; i++) 
		{
			total += slices[i].value;
		}
      
		double curValue = 0.0D;
		int startAngle = 0;
		
		for (int i = 0; i < slices.length; i++) 
		{
			startAngle = (int) (curValue * 360 / total);
			int arcAngle = (int) (slices[i].value * 360 / total);

			g.setColor(slices[i].color);
			g.fillArc(area.x, area.y, effectiveDiam, effectiveDiam, startAngle, arcAngle);
			
			curValue += slices[i].value;
		}
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
	    content.add(new HoopPieChart ());
	    f.setVisible(true);
	}	
}
