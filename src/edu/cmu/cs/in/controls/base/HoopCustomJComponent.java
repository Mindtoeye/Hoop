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
import java.text.*;
import java.awt.geom.*;

import edu.cmu.cs.in.hoop.hoops.base.HoopBase;

/**
 *  Rene's Component. Input-item painted on a RPanel.<br>
 *  Its value can be modified by mouse events.
 */
public class HoopCustomJComponent extends HoopCustomRenderer 
{
  private int x_relative;  // position/width normiert auf 1000
  private int y_relative;
  private int witdh_relative;
  private int font_size_relative; // height ia calculated from font height
  private int value; // value to be shown and modified
  private int max_value;
  private int min_value;
  private int step;
  private boolean selected = false;
  private String additional_string = "";
  private String[] string_aray;
  private Color bg_color;
  private Color selected_bg_color = Color.red; // used as background if selected
  private Color high_light_color;

  private int last_panel_width = 0; // set in draw() to remember last dimension of parent panel
  private int last_panel_height = 0;
  private int last_value; // value while last call of draw() to detect changes
  private Font font;
  private String draw_string; // string to be drawn on the component
  private int x_pos;
  private int y_pos;
  private int height;
  private int ascent;
  private int width;
  private Color tmp_color;


  /**
   *  Constructor for the RComponent object
   *
   * @param  p    parent panel
   * @param  bg   back ground colour
   * @param  hl   high light colour
   * @param  i    value
   * @param  max  max value
   * @param  min  min value
   * @param  s    step value (value is in/decreased by this step on mouse event)
   * @param  sa   string area, contains a string for each possible value, this string is shown if the value is true
   * @param  a    additional shown fixed string
   * @param  x    relative position on panel x, range 0 - 1000, 0 - left, 1000 right border
   * @param  y    relative position on panel y, range 0 - 1000, 0 - top, 1000 bottom border
   * @param  w    relative width, range 0 - 1000, 0 - 0, 1000 - 100% of panel
   * @param  fs   relative font size
   */
  public HoopCustomJComponent (JPanel p,
		  			Color bg,
		  			Color hl,
		  			int i, 
		  			int max,
		  			int min,
		  			int s,
		  			String[] sa,
		  			String a,
		  			int x,
		  			int y,
		  			int w,
		  			int fs) 
  {
    super(p);
    
    debug ("HoopCustomJComponent ()");
    
    x_relative = x;
    y_relative = y;
    witdh_relative = w;
    font_size_relative = fs;
    string_aray = sa;
    additional_string = a;
    bg_color = bg;
    high_light_color = hl;
    tmp_color = bg;
    value = i;
    max_value = max;
    min_value = min;
    step = s;
  }
  
  /**
   * 
   * @param aMessage
   */
  private void debug (String aMessage)
  {
	  HoopBase.debug ("HoopCustomJComponent",aMessage);
  }


  /**
   *  Draw the RComponent on panel
   *
   * @param  g2  Graphics2D object
   */
  public void draw(Graphics2D g2) {
    if (last_panel_width != super.panel.getWidth() || last_panel_height != super.panel.getHeight() || last_value != value) {
      // position, size and font must be determined because of the parent panel has been resized or the value has been changed
      int panel_width = super.panel.getWidth();
      int panel_height = super.panel.getHeight();
    
      x_pos = x_relative * panel_width / 1000;
      y_pos = y_relative * panel_height / 1000;
      width = witdh_relative * panel_width / 1000;
      font = new Font("SansSerif", Font.PLAIN, font_size_relative * panel_width / 1000);
      
      if (string_aray == null) {
        NumberFormat formatter = NumberFormat.getNumberInstance();
        formatter.setMinimumIntegerDigits(Integer.toString(max_value).length());
        formatter.setGroupingUsed(false);
        draw_string = formatter.format((long) value);
      }
      else {
        draw_string = string_aray[value];
      }
      Rectangle2D bounds = font.getStringBounds(draw_string, g2.getFontRenderContext());
      ascent = (int) -bounds.getY();
      height = (int) bounds.getHeight();
  
      // check whether string fits into colored rectangle
      // if not cut and put "..." to its end
      if (bounds.getWidth() + height / 5 > width) {
        draw_string = draw_string.substring(0, draw_string.length() - 3) + "...";
        bounds = font.getStringBounds(draw_string, g2.getFontRenderContext());
      }
      while (bounds.getWidth() + height / 5 > width) {
        draw_string = draw_string.substring(0, draw_string.length() - 4) + "...";
        bounds = font.getStringBounds(draw_string, g2.getFontRenderContext());
      }
      last_panel_width = panel_width; // remember last dimension of parent panel
      last_panel_height = panel_height;
      last_value = value;
    }
    
    g2.setColor(tmp_color);
    g2.fillRoundRect(x_pos, y_pos - 1, width, height + 2, height + 2, height + 2);
    if (tmp_color.getGreen() + tmp_color.getRed() + tmp_color.getBlue() < 300) {
      g2.setColor(Color.white);
    }
    else {
      g2.setColor(Color.black);
    }
    g2.setFont(font);
    g2.drawString(draw_string + additional_string, x_pos + height / 5, y_pos + ascent);
  }


  /**
   *  Check whether the mouse click hits the RComponent
   *
   * @param  e  mouse event
   * @return    true - if RComponent meet<br>
   *            false - if RComponent not meet
   */
  public boolean mouse_clicked(MouseEvent e) {
    int x = e.getX();
    int y = e.getY();
    if (y > y_pos && y < y_pos + height && x > x_pos && x < x_pos + width) {
      if ((e.getModifiers() & InputEvent.BUTTON1_MASK) != 0 && (e.getModifiers() & InputEvent.BUTTON3_MASK) == 0) {
        value = value + step;
        if (value > max_value) {
          value = min_value;
        }
      }
      else {
        value = value - step;
        if (value < min_value) {
          value = max_value;
        }
      }
      return true;
    }
    return false;
  }


  /**
   *  Check whether the mouse is over the RComponent and control the RComponents colour.<br>
   *  If mouse is over the RComponent it switches to the highlight colour.
   *
   * @param  e  mouse event
   * @return    true - if button meet<br>
   *            false - if button not meet
   */
  public boolean mouse_over(MouseEvent e) {
    int x = e.getX();
    int y = e.getY();

    if (y > y_pos && y < y_pos + height && x > x_pos && x < x_pos + width) {
      tmp_color = high_light_color;
      return true;
    }
    if (selected) {
      tmp_color = selected_bg_color;
    }
    else {
      tmp_color = bg_color;
    }
    return false;
  }


  /**
   *  Check whether the mouse wheel rotates over the RComponent.<br>
   *  If true the value is changed.
   *
   * @param  e  mouse wheel event
   * @return    true - if button meet<br>
   *            false - if button not meet
   */
  public boolean mouse_wheel_rotate(MouseWheelEvent e) {
    int x = e.getX();
    int y = e.getY();
    if (y > y_pos && y < y_pos + height && x > x_pos && x < x_pos + width) {
      value = value - step * e.getWheelRotation();
      if (value > max_value) {
        value = min_value;
      }
      else if (value < min_value) {
        value = max_value;
      }
      return true;
    }
    return false;
  }


  /**
   *  Gets the value attribute of the RComponent object
   *
   * @return    The value value
   */
  public int get_value() {
    return value;
  }


  /**
   *  Set value
   *
   * @param  i  value
   */
  public void set_value(int i) {
    value = i;
  }


  /**
   *  Set backgroung colour
   *
   * @param  c  background colour
   */
  public void set_bg_colour(Color c) {
    bg_color = c;
    tmp_color = c;
  }


  /**
   *  Set selected
   *
   * @param  b  true - selected<br>
   *            false - not selected
   */
  public void set_selected(boolean b) {
    selected = b;
    if (tmp_color.equals(high_light_color)) {
      return;
    }
    if (b) {
      tmp_color = selected_bg_color;
    }
    else {
      tmp_color = bg_color;
    }
  }


  /**
   *  Set additional string
   *
   * @param  s  additional string
   */
  public void set_additional_string(String s) {
    additional_string = s;
  }
  
  
  /**
   *  Set string aray, contains a string for each possible value,<br>
   *  this string is shown if the value is true
   *
   * @param  sa  string aray
   */
  public void set_string_aray(String[] sa) {
    string_aray = sa;
    draw_string = string_aray[value];
  }
}

