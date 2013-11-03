/*
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
 */

package edu.cmu.cs.in.hoop.hoops.task.HoopCalendarControls;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.controls.base.HoopCustomJPanel;
import edu.cmu.cs.in.controls.base.HoopCustomRenderer;

import java.util.*;
import java.awt.print.*;

/**
 *  Description of the Class
 */
public class HoopDateLookPanel extends HoopCustomJPanel implements Printable 
{
	private static final long serialVersionUID = 3340184454444054029L;
	
	private ArrayList<EventRenderer> visible_event_renderer_list = new ArrayList<EventRenderer>(); // contains only events visible in calendar
	
  private CalendarRenderer my_calendar = new CalendarRenderer(this);
  private ZoomPointer zoom_pointer = new ZoomPointer(this);
  private ShiftPointer shift_pointer = new ShiftPointer(this);
  private Cursor default_cursor = Toolkit.getDefaultToolkit().createCustomCursor(HoopLink.getImageByName("default.gif").getImage(), new Point(0, 0), "default");
  private Cursor default_day_cursor = Toolkit.getDefaultToolkit().createCustomCursor(HoopLink.getImageByName("default_day.gif").getImage(), new Point(0, 0), "default_day");
  private Cursor move_cursor = Toolkit.getDefaultToolkit().createCustomCursor(HoopLink.getImageByName("move.gif").getImage(),new Point(16, 16), "move");
  private Cursor move_day_cursor = Toolkit.getDefaultToolkit().createCustomCursor(HoopLink.getImageByName("move_day.gif").getImage(),new Point(16, 16), "move_day");
  private Cursor move_day_copy_cursor = Toolkit.getDefaultToolkit().createCustomCursor(HoopLink.getImageByName("move_day_copy.gif").getImage(),new Point(16, 16), "move_day_copy");
  private Cursor move_copy_cursor = Toolkit.getDefaultToolkit().createCustomCursor(HoopLink.getImageByName("move_copy.gif").getImage(),new Point(16, 16), "move_copy");
  private EventMemory event_memory;
  private Settings settings = new Settings();
  //private HoopCalendarTaskDialog main_frame;
  private GoToFrame go_to_frame = null;
  private ColourFrame colour_frame = null;

  private JPopupMenu jPopupMenu_main = new JPopupMenu();
  private JMenuItem jMenuItem_exit = new JMenuItem();
  private JMenuItem jMenuItem_revert = new JMenuItem();
  private JMenuItem jMenuItem_mode = new JMenuItem();
  private JMenuItem jMenuItem_help = new JMenuItem();
  private JMenuItem jMenuItem_dates_table = new JMenuItem();
  private JMenuItem jMenuItem_go_to = new JMenuItem();
  private JMenuItem jMenuItem_print = new JMenuItem();
  private JMenuItem jMenuItem_settings = new JMenuItem();
  private JMenuItem jMenuItem_colour = new JMenuItem();

  private int last_x;  // position of mouse pointer when pressed
  private int last_y;
  private int mouse_x;  // coordinates of mouse pointer
  private int mouse_y;

  private long first_rendered_hour_UTC_ms;

  private final long first_rendered_hour_UTC_ms_min = (new GregorianCalendar(1, 0, 1, 0, 0)).getTime().getTime(); // 01.01.01 00:00
  private final long last_rendered_UTC_ms_max = (new GregorianCalendar(2501, 0, 1, 0, 0)).getTime().getTime(); // 01.01.2501 00:00
  private final static long rendered_hours_min = 24;
  private final static long rendered_hours_max = 365 * 24;
  private boolean rebuilt_visible_event_renderer_list = true; // indicates that visible events have changed

  private long first_rendered_hour_UTC_ms_before_shift;
  private long number_of_rendered_hours;
  private long number_of_rendered_hours_before_zoom;
  private boolean extended_view = false;
  private boolean start_ext_view = false;

  private EventRenderer mouse_over_event_renderer;  // "event_renderer" where the mouse is over
  private HoopCalendarEvent dragging_event;  // event that is dragging
  private HoopCalendarEvent dragging_event_before_dragging;  // stores the original event when dragging start
  private boolean dragging_event_resize = false;  // true if the dragging event is a new one not copied
  private long last_begin_UTC_ms;  // values of dragging event when dragging starts
  private long last_end_UTC_ms;
  private long last_alarm_UTC_ms;

  /**
   *  Height of a line for year/date/day of week.<br>
   *  very important! controls all sizes of other windows and fonts
   */
  protected static int slot_height = 0;

  /**
   *  Description of the Field
   */
  protected static int frame_decor_height = 0;
  /**
   *  Description of the Field
   */
  protected static int frame_decor_width = 0;

  // variables to control the descriptons renderer
  private int[] free_x = new int[16];  // array to remember x_coordinate of free space in row before
  private int free_space_y;  // temporarily store calculated y-coordinate for a description
  private int space_between_date_descriptions;
  private int descriptions_slot_height;
  private int required_description_renderer_height = 0;  // to show all dates descriptions in extended view
  private int y_description_slot0;  // y-coordinate of first row in descriptons renderer
  private int main_frame_height_before_ext_view;  // used to switch back to simple view

  
  /**
   *  Constructor for the HoopDateLookPanel object
   *
   * @param  mf  Description of the Parameter
   */
  public HoopDateLookPanel() 
  {
    super(false);
    
	setClassName ("HoopDateLookPanel");
	debug ("HoopDateLookPanel ()");        
    
    this.setBackground(Color.white);
    this.setCursor(default_cursor);

    //initialize first_rendered_hour_UTC_ms with 00:00:00 of today
    GregorianCalendar gc = new GregorianCalendar();
    gc.set(GregorianCalendar.HOUR_OF_DAY, 0);
    gc.set(GregorianCalendar.MINUTE, 0);
    gc.set(GregorianCalendar.SECOND, 0);
    gc.set(GregorianCalendar.MILLISECOND, 0);
    first_rendered_hour_UTC_ms = gc.getTime().getTime();

    number_of_rendered_hours = settings.get_number_of_rendered_hours();
    if (settings.get_ext_view()) {
      start_ext_view = true;
    }

    jMenuItem_exit.setText("Exit");
    jMenuItem_revert.setText("Revert & Exit");
    jMenuItem_mode.setText("Extended View");
    jMenuItem_help.setText("Help");
    jMenuItem_dates_table.setText("EventManager");
    jMenuItem_go_to.setText("Goto");
    jMenuItem_print.setText("Print");
    jMenuItem_settings.setText("Save Pos & Zoom");
    jMenuItem_colour.setText("Predef Colours");

    jPopupMenu_main.add(jMenuItem_mode);
    jPopupMenu_main.add(jMenuItem_go_to);
    jPopupMenu_main.add(jMenuItem_dates_table);
    jPopupMenu_main.add(jMenuItem_print);
    jPopupMenu_main.add(jMenuItem_colour);
    jPopupMenu_main.add(jMenuItem_settings);
    jPopupMenu_main.add(jMenuItem_revert);
    jPopupMenu_main.add(jMenuItem_exit);

    jMenuItem_exit.addActionListener(
      new java.awt.event.ActionListener() {
        public void actionPerformed(ActionEvent e) {
          jPopupMenu_main.setVisible(false);
          event_memory.save();
          System.exit(0);
        }
      });
    jMenuItem_revert.addActionListener(
      new java.awt.event.ActionListener() {
        public void actionPerformed(ActionEvent e) {
          jPopupMenu_main.setVisible(false);
          event_memory.revert();
          System.exit(0);
        }
      });
    jMenuItem_mode.addActionListener(
      new java.awt.event.ActionListener() {
        public void actionPerformed(ActionEvent e) {
          jPopupMenu_main.setVisible(false);
          toggle_view_mode();
        }
      });
    jMenuItem_dates_table.addActionListener(
      new java.awt.event.ActionListener() {
        public void actionPerformed(ActionEvent e) {
          open_event_table();
          jPopupMenu_main.setVisible(false);
        }
      });
    jMenuItem_go_to.addActionListener(
      new java.awt.event.ActionListener() {
        public void actionPerformed(ActionEvent e) {
          open_go_to_frame();
          jPopupMenu_main.setVisible(false);
        }
      });
    jMenuItem_print.addActionListener(
      new java.awt.event.ActionListener() {
        public void actionPerformed(ActionEvent e) {
          open_print_frame();
          jPopupMenu_main.setVisible(false);
        }
      });
    jMenuItem_settings.addActionListener(
      new java.awt.event.ActionListener() 
      {
        public void actionPerformed(ActionEvent e) 
        {
          settings.save(new Rectangle(HoopLink.mainFrame.getX(), HoopLink.mainFrame.getY(),  HoopLink.mainFrame.getWidth(), slot_height * 10 + frame_decor_height), number_of_rendered_hours, extended_view);
          jPopupMenu_main.setVisible(false);
        }
      });
    jMenuItem_colour.addActionListener(
      new java.awt.event.ActionListener() {
        public void actionPerformed(ActionEvent e) {
          open_colour_frame();
          jPopupMenu_main.setVisible(false);
        }
      });
  }


  /**
   *  Paint component
   *
   * @param  g  Graphics object
   */
  public void paintComponent(Graphics g) 
  {
    frame_decor_height = HoopLink.mainFrame.getHeight() - this.getHeight();
    frame_decor_width = HoopLink.mainFrame.getWidth() - this.getWidth();

    required_description_renderer_height = 0;
    if (!extended_view) {
      slot_height = this.getHeight() / 10;  // in extended mode slot_height is frozen
      descriptions_slot_height = (slot_height * 20) / 15;
    }
    super.paintComponent(g);

    if (dragging_event == null) {
      if (rebuilt_visible_event_renderer_list) {
        // rebuilt visible_event_renderer_list here
        rebuilt_visible_event_renderer_list = false;
        visible_event_renderer_list.clear();
        for (int i = event_memory.get_size() - 1; i > -1; i--) {
          EventRenderer tmp_renderer = event_memory.get_event(i).get_event_renderer();
          if (tmp_renderer == null) {
            // create a renderer for this event
            tmp_renderer = new EventRenderer(event_memory.get_event(i), this);
            event_memory.get_event(i).set_event_renderer(tmp_renderer);
          }
          if (tmp_renderer.draw(g2, true, false, false)) {
            // event is visible in calendar
            visible_event_renderer_list.add(tmp_renderer); // add to visible_event_renderer_list
          }
        }
      }
      else {
        // reuse visible_event_renderer_list
        for (int i = 0; i < visible_event_renderer_list.size(); i++) {
          ((EventRenderer) visible_event_renderer_list.get(i)).draw(g2, true, false, false);
        }
      }
    }
    else {
      // if dragging is in progress the "old" list of visible events can be used -> faster rendering!
      if (!visible_event_renderer_list.contains(dragging_event.get_event_renderer())) {
        rebuilt_visible_event_renderer_list = true;
        visible_event_renderer_list.add(dragging_event.get_event_renderer());
      }
      else {
        // dragging event to foreground if not already there
        if (visible_event_renderer_list.get(visible_event_renderer_list.size() - 1) != dragging_event.get_event_renderer()) {
          visible_event_renderer_list.add(visible_event_renderer_list.remove(
          // dragging event to foreground
              visible_event_renderer_list.indexOf(dragging_event.get_event_renderer())));
        }
      }
      if (dragging_event_before_dragging != null && !visible_event_renderer_list.contains(dragging_event_before_dragging.get_event_renderer())) {
        visible_event_renderer_list.add(dragging_event_before_dragging.get_event_renderer());
      }
      for (int i = 0; i < visible_event_renderer_list.size(); i++) {
        ((EventRenderer) visible_event_renderer_list.get(i)).draw(g2, true, false, false);
      }
    }

    if (extended_view) {
      // draw connection lines over rectangles
      reset_space_map();    // reset occupied space for date description on page
      for (int i = 0; i < visible_event_renderer_list.size(); i++) {
        ((EventRenderer) visible_event_renderer_list.get(i)).draw(g2, false, true, false);
      }
      // draw descriptions over the connection lines
      reset_space_map();    // reset occupied space for date description on page
      for (int i = 0; i < visible_event_renderer_list.size(); i++) {
        ((EventRenderer) visible_event_renderer_list.get(i)).draw(g2, false, false, true);
      }
    }

    my_calendar.draw(g2);
    zoom_pointer.draw(g2);
    shift_pointer.draw(g2);

    if (mouse_over_event_renderer != null) {
      mouse_over_event_renderer.draw_mouse_over_description(g2, mouse_x, mouse_y);
    }

    if (extended_view && required_description_renderer_height > this.getHeight() - 10 * slot_height) {
      // enlarge main_frame if extended view and there are descriptions invisible

      EventQueue.invokeLater(
        new Runnable() {
          // invoke later because it isn't a good idea to start new paint within a paint
          public void run() {
        	  HoopLink.mainFrame.setSize(HoopLink.mainFrame.getWidth(), required_description_renderer_height + main_frame_height_before_ext_view);
        	  HoopLink.mainFrame.paintAll(HoopLink.mainFrame.getGraphics());
          }
        });
    }
    if (start_ext_view) {
      // or DateLook should start with extended view
      start_ext_view = false;
      EventQueue.invokeLater(
        new Runnable() {
          // invoke later because it isn't a good idea to start new paint within a paint
          public void run() {
            toggle_view_mode();
          }
        });
    }
  }


  /**
   *  Description of the Method
   *
   * @param  g                     Description of the Parameter
   * @param  pf                    Description of the Parameter
   * @param  p                     Description of the Parameter
   * @return                       Description of the Return Value
   * @exception  PrinterException  Description of the Exception
   */
  public int print(Graphics g, PageFormat pf, int p) throws PrinterException {
    if (p != 0) {
      return Printable.NO_SUCH_PAGE;
    }
    Graphics2D g2 = (Graphics2D) g;

    // set coordinates and scale: x_range = 0 to panel-width, y_range = 0 to 30 * slot_height
    g2.translate(pf.getImageableX(), pf.getImageableY());
    g2.scale(pf.getImageableWidth() / (double) this.getWidth(),
        pf.getImageableHeight() / (double) (30 * slot_height));

    // print rectangles first
    reset_space_map();
    // reset occupied space for date description on page
    for (int i = 0; i < visible_event_renderer_list.size(); i++) {
      ((EventRenderer) visible_event_renderer_list.get(i)).draw(g2, true, false, false);
    }
    
    // print connection lines over rectangles
    reset_space_map();    // reset occupied space for date description on page
    for (int i = 0; i < visible_event_renderer_list.size(); i++) {
      ((EventRenderer) visible_event_renderer_list.get(i)).draw(g2, false, true, false);
    }
    
    // print descriptions over the connection lines
    reset_space_map();    // reset occupied space for date description on page
    for (int i = 0; i < visible_event_renderer_list.size(); i++) {
      ((EventRenderer) visible_event_renderer_list.get(i)).draw(g2, false, false, true);
    }
    my_calendar.draw(g2);
    g2.setColor(Color.white);
    g2.fillRect(-100, 0, 100, HoopDateLookPanel.slot_height * 30);   // prevent faulty print left from x=0
    g2.setColor(Color.black);
    g2.drawRect(0, 0, this.getWidth(), 10 * slot_height + 15 * descriptions_slot_height);   // print a frame
    return Printable.PAGE_EXISTS;
  }


  /**
   *  Check for pressed key and handles it.<br>
   *  F1 - opens HelpFrame<br>
   *  ctrl-Q - saves database to file and closes DateLook application<br>
   *  ctrl-D - opens the EventTableFrame<br>
   *  ctrl-V - toggles the view mode (extended - simlpe view)<br>
   *  ctrl-G - opens the GoToFrame<br>
   *  ctrl-P - opens the PrintDialog<br>
   *  ctrl-O - opens the PredefColourEditor<br>
   *  ctrl-M - opens the menu<br>
   *  left - shifts visible space of time to left<br>
   *  ctrl-left - zooms in visible space of time<br>
   *  right - shifts visible space of time to right<br>
   *  ctrl-right - zooms out visible space of time<br>
   *
   *
   * @param  e  key event
   */
  public void keyPressed(KeyEvent e) {
    int divider = 32;    // used for left/right arrow keys to determine the step to be done

      if (e.getModifiers() == InputEvent.CTRL_MASK) 
      {
        if (e.getKeyCode() == KeyEvent.VK_Q) {
          event_memory.save();
          System.exit(0);
          return;
        }
        if (e.getKeyCode() == KeyEvent.VK_E) {
          open_event_table();
          return;
        }
        if (e.getKeyCode() == KeyEvent.VK_V) {
          toggle_view_mode();
          return;
        }
        if (e.getKeyCode() == KeyEvent.VK_G) {
          open_go_to_frame();
          return;
        }
        if (e.getKeyCode() == KeyEvent.VK_P) {
          open_print_frame();
          return;
        }
        if (e.getKeyCode() == KeyEvent.VK_O) {
          open_colour_frame();
          return;
        }
        if (e.getKeyCode() == KeyEvent.VK_M) {
          Font temp_font = my_calendar.get_font();
          jMenuItem_exit.setFont(temp_font);
          jMenuItem_revert.setFont(temp_font);
          jMenuItem_mode.setFont(temp_font);
          jMenuItem_help.setFont(temp_font);
          jMenuItem_dates_table.setFont(temp_font);
          jMenuItem_go_to.setFont(temp_font);
          jMenuItem_print.setFont(temp_font);
          jMenuItem_settings.setFont(temp_font);
          jMenuItem_colour.setFont(temp_font);
          jPopupMenu_main.show(this, 20, 20);
          return;
        }
        divider = 1;
        // if no control key is pressed jump one screen width
      }
      if (dragging_event == null) {
        // if dragging takes place keys must be locked to prevent
        // that the mouse pointer loses its event
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
          if (e.getModifiers() == InputEvent.SHIFT_MASK) {
            set_number_of_rendered_hours((int) get_number_of_rendered_hours() * 105 / 100);
          }
          else {
            GregorianCalendar gc = Converter.ms2gc(first_rendered_hour_UTC_ms);
            gc.add(GregorianCalendar.HOUR_OF_DAY, Math.max((int) number_of_rendered_hours / divider, 1));
            set_first_rendered_hour_UTC_ms(gc.getTime().getTime());
          }
          mouse_over_event_renderer = null;
          this.repaint();
          return;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
          if (e.getModifiers() == InputEvent.SHIFT_MASK) {
            set_number_of_rendered_hours((int) get_number_of_rendered_hours() * 95 / 100);
          }
          else {
            GregorianCalendar gc = Converter.ms2gc(first_rendered_hour_UTC_ms);
            gc.add(GregorianCalendar.HOUR_OF_DAY, Math.min((int) -number_of_rendered_hours / divider, -1));
            set_first_rendered_hour_UTC_ms(gc.getTime().getTime());
          }
          mouse_over_event_renderer = null;
          this.repaint();
          return;
        }
      }

    this.cursor_control(e);
  }


  /**
   *  Description of the Method
   *
   * @param  e  Description of the Parameter
   */
  public void keyReleased(KeyEvent e) {
    this.cursor_control(e);
  }


  /**
   *  Description of the Method
   *
   * @param  e  Description of the Parameter
   */
  public void mousePressed(MouseEvent e) {
    jPopupMenu_main.setVisible(false);
    this.set_font_antialiasing (false);
    if (zoom_pointer.get_visible() || shift_pointer.get_visible() || dragging_event != null) {
      return;   // zoom/shift or dragging in progress -> do nothing
    }
    
    last_x = e.getX();    // remember position for dragging or zoom/shift
    last_y = e.getY();

    if (last_y < slot_height * 5) {
      // start zoom or shift
      number_of_rendered_hours_before_zoom = number_of_rendered_hours;
      first_rendered_hour_UTC_ms_before_shift = first_rendered_hour_UTC_ms;
      if ((e.getModifiers() & InputEvent.BUTTON3_MASK) != 0) {
        // start zoom
        zoom_pointer.modify(0, last_x, last_y, true);
      }
      else if ((e.getModifiers() & InputEvent.BUTTON1_MASK) != 0) {
        // start shift
        shift_pointer.modify(0, last_x, last_y, true);
      }
      this.repaint();
    }
    else if (last_y < slot_height * 10) {
      // mouse points into dates renderer
      if ((e.getModifiers() & InputEvent.BUTTON1_MASK) != 0 && (e.getModifiers() & InputEvent.BUTTON3_MASK) == 0) {
        // start dragging of an event
        for (int i = visible_event_renderer_list.size() - 1; i > -1; i--) {
          if (((EventRenderer) visible_event_renderer_list.get(i)).clicked(last_x, last_y)) {
            // existing event matched
        	  HoopCalendarEvent t = ((EventRenderer) visible_event_renderer_list.get(i)).get_event();
            if (t.get_my_editor_frame() == null) {
              dragging_event_before_dragging = t.clone2();  // remark: new UID!
              dragging_event = t;
              event_memory.add_event(dragging_event_before_dragging);
              dragging_event.get_event_renderer().set_focus(true);
              event_memory.delete_event(dragging_event_before_dragging);
              // should be invisible in Event Manager
              EventRenderer tmp_renderer = dragging_event_before_dragging.get_event_renderer();
              if (tmp_renderer == null) {
                // create a renderer for this event
                tmp_renderer = new EventRenderer(dragging_event_before_dragging, this);
                dragging_event_before_dragging.set_event_renderer(tmp_renderer);
              }
              dragging_event_before_dragging.get_event_renderer().set_visible(false);
              mouse_over_event_renderer = dragging_event.get_event_renderer();
              last_begin_UTC_ms = dragging_event.get_begin_UTC_ms();
              last_end_UTC_ms = dragging_event.get_end_UTC_ms();
              last_alarm_UTC_ms = dragging_event.get_alarm_UTC_ms();
              dragging_event_resize = false;
            }
            else {  // can't move event, editor is open
              Toolkit.getDefaultToolkit().beep();
            }
            
            cursor_control(e);
            return;
          }
        }
        // no event matched therefore create a new one
        int group = (last_y - slot_height * 5) / slot_height;
        long begin_UTC_ms = ((long) last_x * number_of_rendered_hours * 60L * 60L * 1000L) /
            ((long) this.getWidth()) + first_rendered_hour_UTC_ms;
        dragging_event = new HoopCalendarEvent(begin_UTC_ms, group, event_memory);
        dragging_event.set_end_UTC_ms(begin_UTC_ms);
        dragging_event_resize = true;
        last_begin_UTC_ms = begin_UTC_ms;
        last_end_UTC_ms = begin_UTC_ms;
        last_alarm_UTC_ms = begin_UTC_ms;
        event_memory.add_event(dragging_event);
        EventRenderer tmp_renderer = dragging_event.get_event_renderer();
        if (tmp_renderer == null) {
          // create a renderer for this event
          tmp_renderer = new EventRenderer(dragging_event, this);
          dragging_event.set_event_renderer(tmp_renderer);
        }
        tmp_renderer.set_focus(true);

        cursor_control(e);
      }
    }
  }


  /**
   *  Description of the Method
   *
   * @param  e  Description of the Parameter
   */
  public void mouseMoved(MouseEvent e) {
    mouse_x = e.getX();
    mouse_y = e.getY();
    if (mouse_y > slot_height * 5) {  // 5 * slot_height
      for (int i = visible_event_renderer_list.size() - 1; i > -1; i--) {
        if (((EventRenderer) visible_event_renderer_list.get(i)).clicked(mouse_x, mouse_y) && mouse_y < slot_height * 10) {
          mouse_over_event_renderer = (EventRenderer) visible_event_renderer_list.get(i);
          this.setCursor(default_cursor);
          this.repaint();
          return;
        }
      }
      this.setCursor(default_cursor);
    }
    else {
      this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
    mouse_over_event_renderer = null;
    this.repaint();
  }


  /**
   *  Description of the Method
   *
   * @param  e  Description of the Parameter
   */
  public void mouseDragged(MouseEvent e) {
    mouse_x = e.getX();
    mouse_y = e.getY();
    if (zoom_pointer.get_visible()) { // zoom
      set_number_of_rendered_hours((int) (last_x * number_of_rendered_hours_before_zoom / Math.max(e.getX(), 1)));
      zoom_pointer.modify(0, e.getX(), e.getY(), true);
      this.repaint();
    }
    else if (shift_pointer.get_visible()) {  // shift
      int delta_x = last_x - e.getX();
      long delta_first_hour = (number_of_rendered_hours_before_zoom * (long) delta_x /
          (long) this.getWidth());
      GregorianCalendar gc = Converter.ms2gc(first_rendered_hour_UTC_ms_before_shift);
      gc.add(GregorianCalendar.HOUR_OF_DAY, (int) delta_first_hour);
      set_first_rendered_hour_UTC_ms(gc.getTime().getTime());
      shift_pointer.modify(0, e.getX(), e.getY(), true);
      this.repaint();
    }
    else if (dragging_event != null) {  // drag an event
      dragging_event.set_renderer_group(
          Math.max(Math.min((e.getY() - slot_height * 5) / slot_height, 4), 0));

      long delta = (long) (e.getX() - last_x) * (long) number_of_rendered_hours *
          60L * 60L * 1000L / (long) this.getWidth();
      long begin_delta;
      long end_delta;
      long alarm_delta;
      boolean shift_pressed = false;
      if ((e.getModifiers() & InputEvent.SHIFT_MASK) == 1) {
        shift_pressed = true;
      }
      if (!shift_pressed || (dragging_event_resize == true)) {
        // shift in five-minutes-steps
        delta = (delta / (5L * 60L * 1000L)) * (5L * 60L * 1000L);  // set to 5 min steps
        begin_delta = delta;
        end_delta = delta;
        alarm_delta = delta;
      }
      else {
        // shift in one-day-steps
        // set delta to full days and consider DST-switches (23h or 25h-days).
        // because of alarm or/and begin can be shifted over a DST-switch and end not
        // all delta times must considered separately
        int shifted_days = (int) (delta / (24L * 60L * 60L * 1000L));
        begin_delta = Converter.UTCplusPeriod2UTC(last_begin_UTC_ms, HoopCalendarEvent.Daily, shifted_days, 1) - last_begin_UTC_ms;
        end_delta = Converter.UTCplusPeriod2UTC(last_end_UTC_ms, HoopCalendarEvent.Daily, shifted_days, 1) - last_end_UTC_ms;
        alarm_delta = Converter.UTCplusPeriod2UTC(last_alarm_UTC_ms, HoopCalendarEvent.Daily, shifted_days, 1) - last_alarm_UTC_ms;
      }

      long new_alarm_UTC_ms = last_alarm_UTC_ms + alarm_delta;
      long new_begin_UTC_ms = last_begin_UTC_ms + begin_delta;
      long new_end_UTC_ms = last_end_UTC_ms + end_delta;
      if (new_alarm_UTC_ms > first_rendered_hour_UTC_ms_min & new_end_UTC_ms < last_rendered_UTC_ms_max) {
        // do not shift over absolute time limits!
        if (dragging_event_resize == false) {
          // move event
          dragging_event.set_begin_UTC_ms(new_begin_UTC_ms);
          dragging_event.set_alarm_UTC_ms(new_alarm_UTC_ms);
          dragging_event.set_end_UTC_ms(new_end_UTC_ms);
          dragging_event.set_alarm_counter_to_next_after_now();
        }
        else {
          // new event has been created
          if (shift_pressed) {
            // if shift is pressed round begin/end/alarm-time to day-boundary
            if (end_delta > 0) {
              // shift end time
              dragging_event.set_end_UTC_ms(Converter.ms2msdayboundary(new_end_UTC_ms));
              dragging_event.set_begin_UTC_ms(Converter.ms2msdayboundary(last_begin_UTC_ms));
              dragging_event.set_alarm_UTC_ms(Converter.ms2msdayboundary(last_alarm_UTC_ms));
            }
            else {
              // shift begin time and alarm time
              dragging_event.set_begin_UTC_ms(Converter.ms2msdayboundary(new_begin_UTC_ms));
              dragging_event.set_alarm_UTC_ms(Converter.ms2msdayboundary(new_alarm_UTC_ms));
              dragging_event.set_end_UTC_ms(Converter.ms2msdayboundary(last_end_UTC_ms));
            }
          }
          else {
            if (end_delta > 0) {
              // shift end time
              dragging_event.set_end_UTC_ms(new_end_UTC_ms);
              dragging_event.set_begin_UTC_ms(last_begin_UTC_ms);
              dragging_event.set_alarm_UTC_ms(last_alarm_UTC_ms);
            }
            else {
              // shift begin time and alarm time
              dragging_event.set_begin_UTC_ms(new_begin_UTC_ms);
              dragging_event.set_alarm_UTC_ms(new_alarm_UTC_ms);
              dragging_event.set_end_UTC_ms(last_end_UTC_ms);
            }
          }
        }
        dragging_event.changed();
      }
      cursor_control(e);
    }
  }


  /**
   *  Description of the Method
   *
   * @param  e  Description of the Parameter
   */
  public void mouseReleased(MouseEvent e) {
    this.set_font_antialiasing (true);
    if (mouse_y > slot_height * 5) {
      this.setCursor(default_cursor);
    }
    else {
      this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    int x = e.getX();
    int y = e.getY();

    if (zoom_pointer.get_visible() | shift_pointer.get_visible()) {
      // zoom or shift took place
      zoom_pointer.modify(false);
      shift_pointer.modify(false);
      this.repaint();
      return;
    }

    if (dragging_event != null) {
      // dragging took place
      if (dragging_event_resize) {
        // dragging of new event
        EditorFrame ed = new EditorFrame(dragging_event, null, true, false);
        dragging_event.set_my_editor_frame(ed);
        ed.setLocation((int) this.getLocationOnScreen().getX() + x + 10,
            (int) this.getLocationOnScreen().getY() + y + 10);
        ed.setVisible(true);
        dragging_event_resize = false;
      }
      else if ((e.getModifiers() & InputEvent.CTRL_MASK) != 0) {
        // copy of event requested
        // Change UIDs of original and copy
        String UID = dragging_event_before_dragging.get_UID();
        dragging_event_before_dragging.set_UID(dragging_event.get_UID());
        dragging_event.set_UID(UID);
        if (!dragging_event_before_dragging.get_event_renderer().get_visible()) {
          event_memory.add_event(dragging_event_before_dragging);
        }
        dragging_event_before_dragging.get_event_renderer().set_visible(true);
        // make visible in Event Manager
        EditorFrame ed = new EditorFrame(dragging_event, null, true, false);
        dragging_event.set_my_editor_frame(ed);
        ed.setLocation((int) this.getLocationOnScreen().getX() + x + 10,
            (int) this.getLocationOnScreen().getY() + y + 10);
        ed.setVisible(true);

      }
      else {
        // only shift of old event
        EditorFrame ed = new EditorFrame(dragging_event, dragging_event_before_dragging, false, false);
        dragging_event.set_my_editor_frame(ed);
        ed.setLocation((int) this.getLocationOnScreen().getX() + x + 10,
            (int) this.getLocationOnScreen().getY() + y + 10);
        ed.setVisible(true);

        dragging_event_before_dragging.delete();
      }
      dragging_event.changed();
      rebuilt_visible_event_renderer_list = true;
      dragging_event_before_dragging = null;
      dragging_event = null;
      return;
    }

    //no dragging, no zoom, no shift took place
    if ((e.getModifiers() & InputEvent.BUTTON3_MASK) != 0) {
      for (int i = visible_event_renderer_list.size() - 1; i > -1; i--) {
        if (((EventRenderer) visible_event_renderer_list.get(i)).clicked(x, y)) {
          // meet an event
          if (((EventRenderer) visible_event_renderer_list.get(i)).get_event().get_my_editor_frame() != null) {
            return;
            // meet event is still edited
          }
          mouse_over_event_renderer = null;
          this.repaint();
          EditorFrame ed = new EditorFrame(((EventRenderer) visible_event_renderer_list.get(i)).get_event(), null, false, false);
          ((EventRenderer) visible_event_renderer_list.get(i)).get_event().set_my_editor_frame(ed);
          ed.setLocation((int) this.getLocationOnScreen().getX() + x - 10, (int) this.getLocationOnScreen().getY() + y - 10);
          ed.setVisible(true);
          return;
        }
      }
      Font temp_font = my_calendar.get_font();
      jMenuItem_exit.setFont(temp_font);
      jMenuItem_revert.setFont(temp_font);
      jMenuItem_mode.setFont(temp_font);
      jMenuItem_help.setFont(temp_font);
      jMenuItem_dates_table.setFont(temp_font);
      jMenuItem_go_to.setFont(temp_font);
      jMenuItem_print.setFont(temp_font);
      jMenuItem_settings.setFont(temp_font);
      jMenuItem_colour.setFont(temp_font);
      jPopupMenu_main.show(this, x, y);
    }
  }


  /**
   *  Check whether the mouse wheel rotates and handle the actions to be done:
   *  <br>
   *  mouse wheel rotates - shift visible space of time<br>
   *  mouse wheel rotates + shift - zoom.
   *
   * @param  e  mouse wheel event
   */
  public void mouseWheelMoved(MouseWheelEvent e) {
    if (!zoom_pointer.get_visible() & !shift_pointer.get_visible() & dragging_event == null) {
      GregorianCalendar gc = Converter.ms2gc(first_rendered_hour_UTC_ms);

      if ((e.getModifiers() & InputEvent.SHIFT_MASK) == 0) {
        // shift visible space of time

        int delta = Math.max((int) ((long) Math.abs(e.getWheelRotation()) * number_of_rendered_hours / 25), 1);
        int y = e.getY();
        // srcoll speed depends on where the mouse is year, month or day area of the calendar
        if (y < slot_height) {
          delta = delta * 365;
        }
        else if (y < 2 * slot_height) {
          delta = delta * 30;
        }
        if (e.getWheelRotation() < 0) {
          delta = -delta;
        }
        gc.add(GregorianCalendar.HOUR_OF_DAY, delta);
        set_first_rendered_hour_UTC_ms(gc.getTime().getTime());
      }
      else {
        // change zoom state

        int factor = 50;
        int y = e.getY();
        // zoom speed depends on where the mouse is year, month or day area of the calendar
        if (y < slot_height) {
          factor = 10;
        }
        else if (y < 2 * slot_height) {
          factor = 20;
        }
        set_number_of_rendered_hours((int) (get_number_of_rendered_hours() +
            (get_number_of_rendered_hours() / factor) * e.getWheelRotation()));
      }
      mouse_over_event_renderer = null;
      this.repaint();
    }
  }


  /**
   *  Method is called if an event is dragging or if a key is pressed or released.<br>
   *  It controls the cursor and the rendering of the original event
   *
   * @param  e  input event
   */
  private void cursor_control(InputEvent e) {
    if (dragging_event == null) {
      return;
    }
    if (dragging_event_resize) {
      // new event is being created
      if ((e.getModifiers() & InputEvent.SHIFT_MASK) != 0) {
        this.setCursor(default_day_cursor);
      }
      else {
        this.setCursor(default_cursor);
      }
    }
    // dragging an old or copied event
    else if ((e.getModifiers() & InputEvent.CTRL_MASK) != 0) {
      // dragging the copy
      if ((e.getModifiers() & InputEvent.SHIFT_MASK) != 0) {
        this.setCursor(move_day_copy_cursor);
      }
      else {
        this.setCursor(move_copy_cursor);
      }
      if (!dragging_event_before_dragging.get_event_renderer().get_visible()) {
        event_memory.add_event(dragging_event_before_dragging);
      }
      dragging_event_before_dragging.get_event_renderer().set_visible(true);
      dragging_event.changed();
    }
    else {
      // dragging the original
      if ((e.getModifiers() & InputEvent.SHIFT_MASK) != 0) {
        this.setCursor(move_day_cursor);
      }
      else {
        this.setCursor(move_cursor);
      }
      if (dragging_event_before_dragging.get_event_renderer().get_visible()) {
        event_memory.delete_event(dragging_event_before_dragging);
      }
      dragging_event_before_dragging.get_event_renderer().set_visible(false);
      dragging_event.changed();
      // not dragging_event_before_dragging.changed()
      // because last_mod of it would be changed and this would be wrong
    }
  }


  /**
   *  Description of the Method
   */
  public void rebuilt_visible_event_renderer_list() {
    rebuilt_visible_event_renderer_list = true;
  }


  /**
   *  Gets the first_rendered_hour_UTC_ms attribute of the HoopDateLookPanel object
   *
   * @return    The first_rendered_hour_UTC_ms value
   */
  public long get_first_rendered_hour_UTC_ms() {
    return first_rendered_hour_UTC_ms;
  }


  /**
   *  Description of the Method
   *
   * @param  d  Description of the Parameter
   */
  public void set_first_rendered_hour_UTC_ms(long d) {
    rebuilt_visible_event_renderer_list = true;
    
    // prevent that time is visible which is out of range 01.01.01 00:00 to 01.01.2500 00:00
    first_rendered_hour_UTC_ms =
        Math.min(Math.max(first_rendered_hour_UTC_ms_min, d),
        last_rendered_UTC_ms_max - number_of_rendered_hours * 60L * 60L * 1000L);
  }


  /**
   *  Gets the number_of_rendered_hours attribute of the HoopDateLookPanel object
   *
   * @return    The number_of_rendered_hours value
   */
  public long get_number_of_rendered_hours() {
    return number_of_rendered_hours;
  }


  /**
   *  Set number of renderer hours.
   *
   * @param  i  number of renderer hours
   */
  public void set_number_of_rendered_hours(int i) {
    rebuilt_visible_event_renderer_list = true;
    
    // prevent that time is visible which is out of range 01.01.01 00:00 to 01.01.2500 00:00
    number_of_rendered_hours = Math.min(Math.min(Math.max(rendered_hours_min, i), rendered_hours_max),
        (last_rendered_UTC_ms_max - first_rendered_hour_UTC_ms) / (60L * 60L * 1000L));
  }


  /**
   *  Sets the event_memory attribute of the HoopDateLookPanel object
   *
   * @param  tm  The new event_memory value
   */
  public void set_event_memory(EventMemory tm) {
    event_memory = tm;
  }


  /**
   *  Sets the go_to_frame attribute of the HoopDateLookPanel object
   *
   * @param  gt  The new go_to_frame value
   */
  public void set_go_to_frame(GoToFrame gt) {
    go_to_frame = gt;
  }


  /**
   *  Sets the colour_frame attribute of the HoopDateLookPanel object
   *
   * @param  cf  The new colour_frame value
   */
  public void set_colour_frame(ColourFrame cf) {
    colour_frame = cf;
  }


  /**
   *  Description of the Method
   */
  private void open_event_table() {
    if (event_memory.get_event_table_frame() == null) {
      // open table with all dates
      EventTableFrame event_table_frame = new EventTableFrame(event_memory);
      event_table_frame.setLocation((int) this.getLocationOnScreen().getX() + 20,
          (int) this.getLocationOnScreen().getY() + 20);
      event_memory.set_event_table_frame(event_table_frame);
      event_table_frame.setVisible(true);
    }
  }


  /**
   *  Description of the Method
   */
  private void open_go_to_frame() {
    if (go_to_frame == null) {
      go_to_frame = new GoToFrame(this);
      go_to_frame.setLocation(this.getLocationOnScreen().x + mouse_x - 10, this.getLocationOnScreen().y + mouse_y - 10);
      go_to_frame.setVisible(true);
    }
  }


  /**
   *  Description of the Method
   */
  private void open_print_frame() {
    PrinterJob print_job = PrinterJob.getPrinterJob();
    PageFormat page_format = print_job.defaultPage();
    page_format.setOrientation(PageFormat.LANDSCAPE);
    print_job.setPrintable(this, page_format);
    if (print_job.printDialog()) {
      try {
        print_job.print();
      }
      catch (Exception e) {
        e.printStackTrace();
      }
    }
  }


  /**
   *  Description of the Method
   */
  private void open_colour_frame() {
    if (colour_frame == null) {
      colour_frame = new ColourFrame(this, null, null);
      colour_frame.setLocation((int) this.getLocationOnScreen().getX() + mouse_x - 10, (int) this.getLocationOnScreen().getY() + mouse_y - 10);
      colour_frame.setVisible(true);
    }
  }


  /**
   *  Toggle view mode between simple and extended
   */
  public void toggle_view_mode() {
    if (extended_view) {
      jMenuItem_mode.setText("Extended View");
      HoopLink.mainFrame.setSize(HoopLink.mainFrame.getWidth(), main_frame_height_before_ext_view);
    }
    else {
      jMenuItem_mode.setText("Simple View");
      main_frame_height_before_ext_view = HoopLink.mainFrame.getHeight();
      HoopLink.mainFrame.setSize(HoopLink.mainFrame.getWidth(), main_frame_height_before_ext_view + 8 * descriptions_slot_height);
    }
    extended_view = !extended_view;
    HoopLink.mainFrame.paintAll(HoopLink.mainFrame.getGraphics());
  }


  /**
   *  Indicate to the HoopDateLookPanel that at least one event has been changed.<br>
   *  The panel will be repainted and the visible_event_renderer_list will be rebuilt.
   */
  public void changed() {
    // called if an event has been changed
    if (dragging_event == null) {
      rebuilt_visible_event_renderer_list = true;
    }
    this.repaint();
  }


  /**
   *  Gets the main_frame attribute of the HoopDateLookPanel object
   *
   * @return    The main_frame value
   */
  public JFrame get_main_frame() 
  {
    return HoopLink.mainFrame;
  }


  /**
   *  Give coordinates of free space to render the event's description on descriptions renderer
   *
   * @param  x_rectangle  Description of the Parameter
   * @param  width        Description of the Parameter
   * @return              Description of the Return Value
   */
  public int get_free_space_X(int x_rectangle, int width) {

    int description_slot = 0;
    int panel_width = this.getWidth();
    while (description_slot < 15 &
        free_x[description_slot] < x_rectangle + width + space_between_date_descriptions &
        free_x[description_slot] < panel_width) {
      description_slot++;
    }
    required_description_renderer_height = Math.max(required_description_renderer_height,
        Math.min(15, (description_slot + 1)) * descriptions_slot_height);
    if (description_slot == 15) {
      // search for slot with the most free space
      int best_description_slot = 0;
      for (int i = 1; i < 15; i++) {
        if (free_x[best_description_slot] < free_x[i]) {
          best_description_slot = i;
        }
      }
      free_space_y = y_description_slot0 + best_description_slot * descriptions_slot_height;
      x_rectangle = free_x[best_description_slot] - width - space_between_date_descriptions;
      free_x[best_description_slot] = x_rectangle;
      return x_rectangle;
    }
    else {
      if (x_rectangle + width + space_between_date_descriptions > panel_width) {
        x_rectangle = panel_width - width - space_between_date_descriptions;
      }
      x_rectangle = Math.max(x_rectangle, space_between_date_descriptions);
      free_x[description_slot] = x_rectangle - space_between_date_descriptions;
      free_space_y = y_description_slot0 + description_slot * descriptions_slot_height;
      return x_rectangle;
    }
  }


  /**
   *  Gets the free_space_Y attribute of the HoopDateLookPanel object
   *
   * @return    The free_space_Y value
   */
  public int get_free_space_Y() {
    // must be called immediately after get_free_space_X()
    return free_space_y;
  }


  /**
   *  Set all space of descriptions renderer to empty
   */
  private void reset_space_map() {
    int panel_width = this.getWidth();
    for (int i = 0; i < 15; i++) {
      free_x[i] = panel_width;
    }
    space_between_date_descriptions = this.getWidth() / 100;
    y_description_slot0 = (slot_height * 10) + descriptions_slot_height / 5;
  }


  /**
   *  Description of the Class
   */
  public static class ZoomPointer extends HoopCustomRenderer
  {
    private int x_1;
    private int x_2;
    private int y;
    private boolean visible;


    /**
     *  Constructor for the ZoomPointer object
     *
     * @param  p  Description of the Parameter
     */
    public ZoomPointer(HoopDateLookPanel p) {
      super(p);
    }


    /**
     *  Description of the Method
     *
     * @param  g2  Description of the Parameter
     */
    public void draw(Graphics2D g2) {
      if (visible) {
        g2.setColor(Color.red);
        g2.fill3DRect(x_1, y, x_2 - x_1, 2, true);
        g2.fill3DRect(x_1, 0, 2, panel.getHeight(), true);
        g2.fill3DRect(x_2, 0, 2, panel.getHeight(), true);
        Polygon arrow = new Polygon();
        arrow.addPoint(x_1, y);
        arrow.addPoint(x_1 + 10, y - 7);
        arrow.addPoint(x_1 + 10, y + 7);
        g2.fillPolygon(arrow);
        arrow = new Polygon();
        arrow.addPoint(x_2, y);
        arrow.addPoint(x_2 - 10, y - 7);
        arrow.addPoint(x_2 - 10, y + 7);
        g2.fillPolygon(arrow);
      }
    }


    /**
     *  Description of the Method
     *
     * @param  x1  Description of the Parameter
     * @param  x2  Description of the Parameter
     * @param  y1  Description of the Parameter
     * @param  v   Description of the Parameter
     */
    public void modify(int x1, int x2, int y1, boolean v) {
      x_1 = x1;
      x_2 = x2;
      y = y1;
      visible = v;
    }


    /**
     *  Description of the Method
     *
     * @param  v  Description of the Parameter
     */
    public void modify(boolean v) {
      visible = v;
    }


    /**
     *  Gets the visible attribute of the ZoomPointer object
     *
     * @return    The visible value
     */
    public boolean get_visible() {
      return visible;
    }
  }


  /**
   *  Description of the Class
   */
  public static class ShiftPointer extends HoopCustomRenderer
  {
    private int x_2;
    private int y;
    boolean visible;


    /**
     *  Constructor for the ShiftPointer object
     *
     * @param  p  Description of the Parameter
     */
    public ShiftPointer(HoopDateLookPanel p) 
    {
      super(p);
    }


    /**
     *  Description of the Method
     *
     * @param  g2  Description of the Parameter
     */
    public void draw(Graphics2D g2) {
      if (visible) {
        g2.setColor(Color.orange);
        g2.fill3DRect(x_2 - 17, y, 34, 2, true);
        g2.fill3DRect(x_2, 0, 2, panel.getHeight(), true);
        Polygon arrow = new Polygon();
        arrow.addPoint(x_2 - 20, y);
        arrow.addPoint(x_2 - 10, y - 7);
        arrow.addPoint(x_2 - 10, y + 7);
        g2.fillPolygon(arrow);
        arrow = new Polygon();
        arrow.addPoint(x_2 + 20, y);
        arrow.addPoint(x_2 + 10, y - 7);
        arrow.addPoint(x_2 + 10, y + 7);
        g2.fillPolygon(arrow);
      }
    }


    /**
     *  Description of the Method
     *
     * @param  x1  Description of the Parameter
     * @param  x2  Description of the Parameter
     * @param  y1  Description of the Parameter
     * @param  v   Description of the Parameter
     */
    public void modify(int x1, int x2, int y1, boolean v) {
      x_2 = x2;
      y = y1;
      visible = v;
    }


    /**
     *  Description of the Method
     *
     * @param  v  Description of the Parameter
     */
    public void modify(boolean v) {
      visible = v;
    }


    /**
     *  Gets the visible attribute of the ShiftPointer object
     *
     * @return    The visible value
     */
    public boolean get_visible() {
      return visible;
    }
  }
}

