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

package edu.cmu.cs.in.hoop.hoops.task.HoopCalendarControls;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.controls.base.HoopCustomJButton;
import edu.cmu.cs.in.controls.base.HoopCustomJComponent;
import edu.cmu.cs.in.controls.base.HoopCustomJPanel;
import edu.cmu.cs.in.controls.base.HoopJFrame;

import java.util.*;

/**
 *  Description of the Class
 */
public class GoToFrame extends HoopJFrame 
{
	private static final long serialVersionUID = -674204257908033898L;
	
	JPanel contentPane;
	private GoToPanel go_to_panel;
	private DateLookPanel date_look_panel;


  /**
   *  Construct the frame
   *
   * @param  dp  Description of the Parameter
   */
  public GoToFrame(DateLookPanel dp) 
  {
		setClassName ("GoToFrame");
		debug ("GoToFrame ()");	  
	  
		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		try 
		{
			date_look_panel = dp;
			go_to_panel = new GoToPanel(dp, this);
			contentPane = (JPanel) this.getContentPane();
			setIconImage(HoopLink.getImageByName("dl.png").getImage());
			this.setSize(8 * DateLookPanel.slot_height + DateLookPanel.frame_decor_width, 3 * DateLookPanel.slot_height + DateLookPanel.frame_decor_height);
			this.setTitle("DateLook GoTo");
			contentPane.add(go_to_panel);
			this.addKeyListener(go_to_panel);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
  }


  /**
   *  Description of the Method
   *
   * @param  e  Description of the Parameter
   */
  protected void processWindowEvent(WindowEvent e) {
    super.processWindowEvent(e);
    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
      date_look_panel.set_go_to_frame(null);
    }
  }


  /**
   *  Description of the Class
   */
  public static class GoToPanel extends HoopCustomJPanel
  {
	private static final long serialVersionUID = -8779971939199366249L;
	
	private DateLookPanel date_look_panel;
    private HoopCustomJComponent day_field;
    private HoopCustomJComponent month_field;
    private HoopCustomJComponent year_field;
    private HoopCustomJButton go_button;
    private HoopCustomJButton cancel_button;


    /**
     *  Constructor for the GoToPanel object
     *
     * @param  dp  Description of the Parameter
     * @param  pw  parent window
     */
    public GoToPanel(DateLookPanel dp, Window pw) {
      super(true);
      date_look_panel = dp;

      long ms = (new GregorianCalendar()).getTime().getTime();
      int rel_font = 85;

      // day of month
      day_field = new HoopCustomJComponent(this, bg_color, Color.orange, Converter.ms2day(ms),
          31, 1, 1, null, ".", 52, 128, 150, rel_font);

      // month
      month_field = new HoopCustomJComponent(this, bg_color, Color.orange, Converter.ms2month(ms),
          11, 0, 1, Converter.longMonthNames, "", 205, 128, 490, rel_font);

      // year
      year_field = new HoopCustomJComponent(this, bg_color, Color.orange, Converter.ms2year(ms),
          2500, 1, 1, null, "", 695, 128, 255, rel_font);
      go_button = new HoopCustomJButton(this, new Color(0, 50, 100), Color.orange, Color.red, "go to", 52, 575, 320, rel_font);
      cancel_button = new HoopCustomJButton(this, new Color(0, 50, 100), Color.orange, Color.red, "cancel", 410, 575, 320, rel_font);
    }


    /**
     *  Paint component
     *
     * @param  g  Graphics object
     */
    public void paintComponent(Graphics g) {
      super.paintComponent(g);
      day_field.draw(g2);
      month_field.draw(g2);
      year_field.draw(g2);
      go_button.draw(g2);
      cancel_button.draw(g2);
    }


    /**
     *  Check for pressed key and handles it.<br>
     *  F1 - opens HelpFrame<br>
     *  ctrl-Q - closes the frame<br>
     *  ctrl-S - closes the frame<br>
     *  ctrl-G - shifts the visible space of time in the main window so that the
     *  event will be visible
     *
     * @param  e  key event
     */
    public void keyPressed(KeyEvent e) 
    {
        if (e.getModifiers() == InputEvent.CTRL_MASK) 
        {
          if (e.getKeyCode() == KeyEvent.VK_Q) {
            date_look_panel.set_go_to_frame(null);
            parent_window.dispose();
            return;
          }
          if (e.getKeyCode() == KeyEvent.VK_C) {
            date_look_panel.set_go_to_frame(null);
            parent_window.dispose();
            return;
          }
          if (e.getKeyCode() == KeyEvent.VK_G) {
            go_to();
            return;
          }
        }
    }


    /**
     *  Description of the Method
     *
     * @param  e  Description of the Parameter
     */
    public void mouseClicked(MouseEvent e) {
      day_field.mouse_clicked(e);
      month_field.mouse_clicked(e);
      year_field.mouse_clicked(e);
      if (go_button.mouse_clicked(e)) {
        date_look_panel.set_first_rendered_hour_UTC_ms(
            Converter.hmdmy2ms(0, 0, day_field.get_value(), month_field.get_value(), year_field.get_value()));
        date_look_panel.repaint();
        date_look_panel.set_go_to_frame(null);
        parent_window.dispose();
      }
      else if (cancel_button.mouse_clicked(e)) {
        date_look_panel.set_go_to_frame(null);
        parent_window.dispose();
      }
      this.repaint();
    }


    /**
     *  Description of the Method
     *
     * @param  e  Description of the Parameter
     */
    public void mousePressed(MouseEvent e) {
      go_button.mouse_pressed(e);
      cancel_button.mouse_pressed(e);
      this.repaint();
    }


    /**
     *  Description of the Method
     *
     * @param  e  Description of the Parameter
     */
    public void mouseReleased(MouseEvent e) {
      go_button.mouse_released(e);
      cancel_button.mouse_released(e);
      this.repaint();
    }


    /**
     *  Description of the Method
     *
     * @param  e  Description of the Parameter
     */
    public void mouseMoved(MouseEvent e) {
      day_field.mouse_over(e);
      month_field.mouse_over(e);
      year_field.mouse_over(e);
      go_button.mouse_over(e);
      cancel_button.mouse_over(e);
      this.repaint();
    }


    /**
     *  Description of the Method
     *
     * @param  e  Description of the Parameter
     */
    public void mouseDragged(MouseEvent e) {
    }


    /**
     *  Description of the Method
     *
     * @param  e  mouse wheel event
     */
    public void mouseWheelMoved(MouseWheelEvent e) {
      day_field.mouse_wheel_rotate(e);
      month_field.mouse_wheel_rotate(e);
      year_field.mouse_wheel_rotate(e);
      this.repaint();
    }


    /**
     *  Description of the Method
     */
    private void go_to() {
      date_look_panel.set_first_rendered_hour_UTC_ms(
          Converter.hmdmy2ms(0, 0, day_field.get_value(), month_field.get_value(), year_field.get_value()));
      date_look_panel.repaint();
      date_look_panel.set_go_to_frame(null);
      parent_window.dispose();
    }
  }
}

