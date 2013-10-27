/**  
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
 *  Original Header:
 *  
 *  Title:        DateLook
 *  Copyright:    Copyright (c) 2005
 *  Author:       Rene Ewald
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License as
 *  published by the Free Software Foundation; either version 2 of
 *  the License, or (at your option) any later version.
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *  General Public License for more details. You should have
 *  received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 * 
 */

package edu.cmu.cs.in.controls.base;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.geom.*;

import edu.cmu.cs.in.hoop.hoops.base.HoopBase;

/**
 *  Rene's Button on a RPanel
 */
public class HoopCustomJButton extends HoopCustomRenderer 
{
	private int x_relative;  // position/width normiert auf 1000
	private int y_relative;
	private int witdh_relative;
	private int font_size_relative;  // heigth is calculated from font height
	private String label = "";
	private Color bg_colour;
	private Color high_light_colour;
	private Color text_colour;  // colour of text
	//private Color boundary_colour;  // colour of boundary
	private Color button_pressed_colour;  // the background colour if button is pressed

	private int x_pos;
	private int y_pos;
	private int last_panel_width = 0;
	private int last_panel_height = 0;
	private int height;
	private int ascent;
	private int label_width;
	private int width;
	private Color tmp_colour;  // temporary colour of background
	private Font font;

	/**
	 *  Constructor for the RButton object
	 *
	 * @param  p   parent JPanel
	 * @param  bg  ground colour
	 * @param  hl  highlight colour
	 * @param  bp  button pressed colour
	 * @param  a   button text
	 * @param  x   relative x position on panel (0 is left border, 1000 is right border)
	 * @param  y   relative y position on panel (0 is top border, 1000 is bottom border)
	 * @param  w   relative width (0 is 0, 1000 is the whole width of panel)
	 * @param  fs  relative font size of button text (* panel_width / 1000 gives the real font size)
	 */
	public HoopCustomJButton (JPanel p,
							  Color bg,
							  Color hl,
							  Color bp,
							  String a,
							  int x,
							  int y,
							  int w,
							  int fs) 
	{
		super(p);
    
		debug ("HoopCustomJButton ()");
    
		x_relative = x;
		y_relative = y;
		witdh_relative = w;
		font_size_relative = fs;
		label = a;
		bg_colour = bg;
		high_light_colour = hl;
		tmp_colour = bg;
		button_pressed_colour = bp;

		// determine colour of text
		if (tmp_colour.getGreen() + tmp_colour.getRed() + tmp_colour.getBlue() < 300) 
		{
			text_colour = Color.white;
		}
		else 
		{
			text_colour = Color.black;
		}
		
		//boundary_colour = Color.black;
	}
  
	/**
	 * 
	 */
	private void debug (String aMessage)
	{
		HoopBase.debug ("HoopCustomJButton",aMessage);
	}

	/**
	 *  Draw the button on panel
	 *
	 * @param  g2  Graphics2D object
	 */
	public void draw(Graphics2D g2) 
	{
		if (super.panel.getWidth() != last_panel_width || super.panel.getHeight() != last_panel_height) 
		{
			// determine position, size and font because parent panel has been resized
			int panel_width = super.panel.getWidth();
			int panel_height = super.panel.getHeight();
			
			x_pos = x_relative * panel_width / 1000;
			y_pos = y_relative * panel_height / 1000;
			width = witdh_relative * panel_width / 1000;
			font = new Font("SansSerif", Font.PLAIN, font_size_relative * panel_width / 1000);
			
			Rectangle2D bounds = font.getStringBounds(label, g2.getFontRenderContext());
			height = (int) bounds.getHeight() + 2;
			ascent = (int) -bounds.getY();
			label_width = (int) bounds.getWidth();
			
			last_panel_width = panel_width;
			last_panel_height = panel_height;
		}
    
		g2.setColor(tmp_colour);
		//g2.fillRoundRect(x_pos, y_pos - 1, width, height, height, height);
		//g2.fillRect(x_pos, y_pos - 1, width, height);
		g2.fill3DRect(x_pos, y_pos - 1, width, height,true);
		//g2.setColor(boundary_colour);
		//g2.drawRoundRect(x_pos, y_pos - 1, width, height, height, height);
		g2.setColor(text_colour);
		g2.setFont(font);
		g2.drawString(label, x_pos + (width - label_width) / 2, y_pos + ascent + 1);
	}

	/**
	 *  Check whether the mouse click hits the button
	 *
	 * @param  e  mouse event
	 * @return    true - if button meet<br>
	 *           	false - if button not meet
	 */
	public boolean mouse_clicked(MouseEvent e) 
	{
		int x = e.getX();
		int y = e.getY();
		
		if (x > x_pos & x < x_pos + width & y > y_pos & y < y_pos + height) 
		{
			return true;
		}
		
		return false;
	}

	/**
	 *  Check whether the mouse is over the button and control the button colour.<br>
	 *  If mouse is over the button it switches to the highlight colour.
	 *
	 * @param  e  mouse event
	 * @return    true - if button meet<br>
	 *          	false - if button not meet
	 */
	public boolean mouse_over(MouseEvent e) 
	{
		// 	toggles the background colour and high_light_colour
		int x = e.getX();
		int y = e.getY();
		
		if (y > y_pos && y < y_pos + height && x > x_pos && x < x_pos + width) 
		{
			tmp_colour = high_light_colour;
			
			if (tmp_colour.getGreen() + tmp_colour.getRed() + tmp_colour.getBlue() < 300) 
			{
				text_colour = Color.white;
			}
			else 
			{
				text_colour = Color.black;
			}
			
			return true;
		}
		
		tmp_colour = bg_colour;
		
		if (tmp_colour.getGreen() + tmp_colour.getRed() + tmp_colour.getBlue() < 300) 
		{
			text_colour = Color.white;
		}
		else 
		{
			text_colour = Color.black;
		}
		
		return false;
	}

	/**
	 *  Check whether the mouse is over the button and control the button colour.<br>
	 *  If mouse is over the button it toggles the text colour and the boundary colour.
	 *
	 * @param  e  mouse event
	 * @return    true - if button meet<br>
	 *            false - if button not meet
	 */
	public boolean mouse_over2(MouseEvent e) 
	{
		// toggles the text colour and boundary colour
		int x = e.getX();
		int y = e.getY();
		
		if (y > y_pos && y < y_pos + height && x > x_pos && x < x_pos + width) 
		{
			if (tmp_colour.getGreen() + tmp_colour.getRed() + tmp_colour.getBlue() < 300) 
			{
				text_colour = Color.black;
			}
			else 
			{
				text_colour = Color.white;
			}
			
			//boundary_colour = Color.white;
			
			return true;
		}
		
		if (tmp_colour.getGreen() + tmp_colour.getRed() + tmp_colour.getBlue() < 300) 
		{
			text_colour = Color.white;
		}
		else 
		{
			text_colour = Color.black;
		}
		
		//boundary_colour = Color.black;
		
		return false;
	}

	/**
	 *  Check whether the pressed mouse hits the button.<br>
	 *  If mouse hits the button its colour changes to "button pressed colour".
	 *
	 * @param  e  mouse event
	 * @return    true - if button meet<br>
	 *           	false - if button not meet
	 */
	public boolean mouse_pressed(MouseEvent e) 
	{
		int x = e.getX();
		int y = e.getY();
		
		if (y > y_pos && y < y_pos + height && x > x_pos && x < x_pos + width) 
		{
			tmp_colour = button_pressed_colour;
			return true;
		}
		
		return false;
	}

	/**
	 *  Check whether the mouse release hits the button
	 *
	 * @param  e  mouse event
	 * @return    true - if button meet<br>
	 *            false - if button not meet
	 */
	public boolean mouse_released(MouseEvent e) 
	{
		return mouse_over(e);
	}

	/**
	 *  Set the background colour.
	 *
	 * @param  c  background colour
	 */
	public void set_bg_colour(Color c) 
	{
		bg_colour = c;
		tmp_colour = c;
		
		// determine new colour of text
		if (tmp_colour.getGreen() + tmp_colour.getRed() + tmp_colour.getBlue() < 300) 
		{
			text_colour = Color.white;
		}
		else 
		{
			text_colour = Color.black;
		}
	}

	/**
	 *  Set the highlight colour.
	 *
	 * @param  c  highlight colour
	 */
	public void set_high_light_colour(Color c) 
	{
		high_light_colour = c;
	}

	/**
	 *  Gets the bg_colour attribute of the RButton object
	 *
	 * @return    The bg_colour value
	 */
	public Color get_bg_colour() 
	{
		return bg_colour;
	}
}

