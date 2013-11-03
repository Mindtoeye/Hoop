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

import java.io.*;

import javax.swing.event.*;

import java.util.*;
import java.awt.geom.*;

import javax.swing.table.*;

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.controls.base.HoopCustomJButton;
import edu.cmu.cs.in.controls.base.HoopCustomJPanel;
import edu.cmu.cs.in.controls.base.HoopJFrame;

/**
 *  Frame with a table of all events and some buttons for searching, import, export, ...
 */
public class EventTableFrame extends HoopJFrame 
{
	private static final long serialVersionUID = 7391072159242581558L;
	
	private JPanel contentPane;
	private JScrollPane scrollPane;
	private JSplitPane split_pane;
	private SearchPanel search_panel;

	private EventTableModel event_table_model;
	private JTable event_table;
	private EventMemory event_memory;
	private EventTableFrame event_table_frame;

	private int last_selected_row = -1;

	private boolean close_button_locked = false;

  /**
   *  Construct the frame
   *
   * @param  tm  event memory, storing the local database
   */
  public EventTableFrame(EventMemory tm) 
  {
    super();
    
	setClassName ("EventTableFrame");
	debug ("EventTableFrame ()");    
    
    event_table_frame = this;
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    
    try 
    {
      event_memory = tm;
      event_table_model = new EventTableModel(tm);
      event_table = new JTable(event_table_model);
      contentPane = (JPanel) this.getContentPane();
      setIconImage(HoopLink.getImageByName("dl.png").getImage());
      this.setSize(new Dimension(32 * HoopDateLookPanel.slot_height + HoopDateLookPanel.frame_decor_width,
          15 * HoopDateLookPanel.slot_height + HoopDateLookPanel.frame_decor_height));
      this.setTitle("EventManager");
      int h = HoopDateLookPanel.slot_height;
      event_table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
      event_table.getColumnModel().getColumn(0).setPreferredWidth(h * 5);
      event_table.getColumnModel().getColumn(1).setPreferredWidth(h * 3);
      event_table.getColumnModel().getColumn(2).setPreferredWidth(h * 3);
      event_table.getColumnModel().getColumn(3).setPreferredWidth(h * 7 / 2);
      event_table.getColumnModel().getColumn(4).setPreferredWidth(h * 2);
      event_table.getColumnModel().getColumn(0).setMaxWidth(h * 8);
      event_table.getColumnModel().getColumn(1).setMaxWidth(h * 8);
      event_table.getColumnModel().getColumn(2).setMaxWidth(h * 8);
      event_table.getColumnModel().getColumn(3).setMaxWidth(h * 8);
      event_table.getColumnModel().getColumn(4).setMaxWidth(h * 8);
      event_table.setRowHeight(h);
      event_table.setIntercellSpacing(new Dimension(h / 4, h / 4));
      event_table.setFont(new Font("SansSerif", Font.PLAIN, h * 2 / 3));
      event_table.setGridColor(Color.gray);
      event_table.setColumnSelectionAllowed(false);
      event_table.setRowSelectionAllowed(true);
      event_table.getSelectionModel().setSelectionMode(DefaultListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
      event_table.getSelectionModel().addListSelectionListener(
        new ListSelectionListener() {
          public void valueChanged(ListSelectionEvent e) {
            search_panel.reset();
          }
        });
      MouseListener listener[] = event_table.getMouseListeners();
      for (int i = 0; i < listener.length; i++) {
        event_table.removeMouseListener(listener[i]);
      }

      event_table.addMouseListener(
        new MouseAdapter() {
          public void mousePressed(MouseEvent e) {
            event_table_frame.table_clicked(e);
          }
        });
      search_panel = new SearchPanel(this, event_table, event_memory, event_table_model);

      event_table.addKeyListener(search_panel);

      scrollPane = new JScrollPane(event_table);
      split_pane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, search_panel, scrollPane);
      split_pane.setResizeWeight(0);
      split_pane.setDividerSize(4);
      split_pane.setDividerLocation((int) HoopDateLookPanel.slot_height * 3 / 2);
      contentPane.add(split_pane);
      this.addKeyListener(search_panel);
      //srcoll to bottom
      event_table.scrollRectToVisible(event_table.getCellRect(event_table.getRowCount() - 1, 3, true));
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }


  /**
   *  Process window event.<br>
   *  In case of a window closing-event and if the close button is not locked the window will be closed.
   *
   * @param  e  window event
   */
  protected void processWindowEvent(WindowEvent e) {
    if (!close_button_locked) {
      super.processWindowEvent(e);
      if (e.getID() == WindowEvent.WINDOW_CLOSING) {
        event_memory.set_event_table_frame(null);
      }
    }
  }


  /**
   *  Set "close button locked"-flag
   *
   * @param  b  true - "close button" is locked<br>
   *            false - "close button" is unlocked
   */
  public void set_close_button_locked(boolean b) {
    close_button_locked = b;
  }


  /**
   *  Changed, indicates the at least one event has been changed.
   */
  public void changed() {
    event_table.tableChanged(new TableModelEvent(event_table_model));
    search_panel.reset();
    super.repaint();
  }


  /**
   *  Indicates that the table has been clicked.<br>
   *  Handles the selection/deselection of table rows.
   *
   * @param  e  mouse event
   */
  public void table_clicked(MouseEvent e) {
    int row = event_table.rowAtPoint(new Point(e.getX(), e.getY()));
    if (row > -1) {
      // existing row matched
      if ((e.getModifiers() & InputEvent.BUTTON3_MASK) != 0) {
        // right mouse button
        int[] sel_rows;
        if (!event_table.isRowSelected(row)) {
          sel_rows = new int[]{row};
        }
        else {
          sel_rows = event_table.getSelectedRows();
        }
        for (int i = 0; i < sel_rows.length; i++) {
        	HoopCalendarEvent t = event_memory.get_event(sel_rows[i]);
          if (t.get_my_editor_frame() == null) {
            EditorFrame ed = new EditorFrame(t, null, false, true);
            t.set_my_editor_frame(ed);
            ed.setLocation((int) event_table.getLocationOnScreen().getX() + e.getX() + 10 * i,
                (int) event_table.getLocationOnScreen().getY() + e.getY() + 10 * i);
            ed.setVisible(true);
          }
        }
      }
      else if ((e.getModifiers() & InputEvent.BUTTON1_MASK) != 0) {
        // left mouse button , this handling is needed only for MAC OS X
        if ((e.getModifiers() & InputEvent.CTRL_MASK) != 0) {
          // control button pressed simultaniously
          if (event_table.isRowSelected(row)) {
            event_table.removeRowSelectionInterval(row, row);
            if (row == last_selected_row) {
              last_selected_row = -1;
            }
          }
          else {
            event_table.addRowSelectionInterval(row, row);
            last_selected_row = row;
          }
        }
        else if ((e.getModifiers() & InputEvent.SHIFT_MASK) != 0) {
          // shift button pressed simultaniously
          if (last_selected_row != -1) {
            event_table.addRowSelectionInterval(row, last_selected_row);
          }
          else {
            event_table.addRowSelectionInterval(row, row);
            last_selected_row = row;
          }
        }
        else {
          // no additional modifier button pressed
          event_table.clearSelection();
          event_table.addRowSelectionInterval(row, row);
          last_selected_row = row;
        }
      }
    }
  }


  /**
   *  Sets the last_selected_row attribute of the EventTableFrame object
   *
   * @param  i  The new last_selected_row value
   */
  public void set_last_selected_row(int i) {
    last_selected_row = i;
  }


  /**
   *  The panel contains some fields and buttons to control searching, import/export and sync.
   */
  public static class SearchPanel extends HoopCustomJPanel
  {
	private static final long serialVersionUID = -5019384284120855637L;
	
	private JTable event_table;
    private EventMemory event_memory;
    private EventTableModel event_table_model;
    private SearchPanel me;

    private HoopCustomJButton search_button;
    private HoopCustomJButton export_button;
    private HoopCustomJButton import_button;
    private HoopCustomJButton sync_button;
    private HoopCustomJButton delete_button;
    private HoopCustomJButton sure_button;
    private HoopCustomJButton close_button;
    private JTextField search_text_field;
    private String result_text = "";
    private javax.swing.Timer button_timer;

    private boolean show_sure_button = false;


    /**
     *  Constructor for the SearchPanel object
     *
     * @param  pw   parent window
     * @param  tt   table of all events
     * @param  tm   memory that stores the events
     * @param  ttm  table model of tt
     */
    public SearchPanel(Window pw, JTable tt, EventMemory tm, EventTableModel ttm) {
      super(false);
      me = this;
      event_table = tt;
      event_memory = tm;
      event_table_model = ttm;
      search_button = new HoopCustomJButton(this, new Color(0, 50, 100), Color.orange, Color.red, "search", 190, 200, 80, 21);
      export_button = new HoopCustomJButton(this, new Color(0, 50, 100), Color.orange, Color.red, "export", 280, 200, 80, 21);
      import_button = new HoopCustomJButton(this, new Color(0, 50, 100), Color.orange, Color.red, "import", 370, 200, 80, 21);
      sync_button = new HoopCustomJButton(this, new Color(0, 50, 100), Color.orange, Color.red, "sync", 460, 200, 80, 21);
      delete_button = new HoopCustomJButton(this, new Color(0, 50, 100), Color.orange, Color.red, "delete", 550, 200, 80, 21);
      sure_button = new HoopCustomJButton(this, Color.blue, Color.red, Color.red, "sure?", 550, 200, 80, 21);
      close_button = new HoopCustomJButton(this, new Color(0, 50, 100), Color.orange, Color.red, "close", 910, 200, 80, 21);
      search_text_field = new JTextField();
      search_text_field.setBackground(Color.white);
      search_text_field.setEditable(true);
      search_text_field.setToolTipText("Enter one or more search strings separated by blanks or encapsulated by quotation.");
      search_text_field.addKeyListener(this);
      search_text_field.addKeyListener(
        new KeyAdapter() 
        {
          public void KeyPressed(KeyEvent e) 
          {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) 
            {
              search();
            }
          }
        });
      this.add(search_text_field);
      button_timer = new javax.swing.Timer(3000,
        new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            show_sure_button = false;
            result_text = "";
            me.repaint();
            button_timer.stop();
          }
        });
    }


    /**
     *  Process component event.<br>
     *  If component resized it resizes all components.
     *
     * @param  e  component event
     */
    public void processComponentEvent(ComponentEvent e) {
      super.processComponentEvent(e);
      if (e.getID() == ComponentEvent.COMPONENT_RESIZED) {
        Graphics g = this.getGraphics();
        Graphics2D g2 = (Graphics2D) g;
        Font font = new Font("SansSerif", Font.PLAIN, 21 * this.getWidth() / 1000);
        Rectangle2D bounds = font.getStringBounds("0", g2.getFontRenderContext());
        search_text_field.setFont(font);
        search_text_field.setBounds(this.getWidth() * 10 / 1000, this.getHeight() / 5 - 2,
            this.getWidth() / 6, (int) bounds.getHeight() + 4);
      }
    }


    /**
     *  Paint component
     *
     * @param  g  Graphics object
     */
    public void paintComponent(Graphics g) {
      super.paintComponent(g);

      Font font = new Font("SansSerif", Font.PLAIN, 21 * this.getWidth() / 1000);
      Rectangle2D bounds = font.getStringBounds("0", g2.getFontRenderContext());
      g2.setColor(Color.black);
      g2.setFont(font);
      g2.drawString(result_text, this.getWidth() * 65 / 100,
          this.getHeight() / 5 - 2 + (int) bounds.getHeight());
      search_button.draw(g2);
      export_button.draw(g2);
      import_button.draw(g2);
      sync_button.draw(g2);
      if (show_sure_button) {
        sure_button.draw(g2);
      }
      else {
        delete_button.draw(g2);
      }
      close_button.draw(g2);
    }


    /**
     *  Check for pressed key and handles it.<br>
     *  F1 - opens HelpFrame<br>
     *  ctrl-Q - closes the frame<br>
     *  ctrl-C - closes the frame<br>
     *  ctrl-S - opens SyncDialog<br>
     *  ctrl-E - opens FileChooser for export<br>
     *  ctrl-I - opens FileChooser for import
     *
     * @param  e  key event
     */
    public void keyPressed(KeyEvent e) 
    {
      if (e.getKeyCode() == KeyEvent.VK_ENTER) 
      {
        if (search_text_field.hasFocus()) {
          search();
          return;
        }
      }
      else {
        if ((e.getModifiers() & InputEvent.CTRL_MASK) != 0) {
          if (e.getKeyCode() == KeyEvent.VK_Q) {
            event_memory.set_event_table_frame(null);
            parent_window.dispose();
            return;
          }
          if (e.getKeyCode() == KeyEvent.VK_C) {
            event_memory.set_event_table_frame(null);
            parent_window.dispose();
            return;
          }
          if (e.getKeyCode() == KeyEvent.VK_S) {
            sync();
            return;
          }
          if (e.getKeyCode() == KeyEvent.VK_E) {
            export();
            return;
          }
          if (e.getKeyCode() == KeyEvent.VK_I) {
            import_();
            return;
          }
        }
      }
      show_sure_button = false;
      button_timer.stop();
      result_text = "";
      me.repaint();
    }


    /**
     *  Handle mouse click.<br>
     *  Check whether buttons are hit and if true handle the action.<br>
     *  close button - close EventTableFrame<br>
     *  search button - start searching<br>
     *  export button - open FileChooserDialog<br>
     *  import button - open FileChooserDialog<br>
     *  sync button - open SyncDialog<br>
     *  delete/sure button - delete highlighted events
     *
     * @param  e  mouse event
     */
    public void mouseClicked(MouseEvent e) {
      if (close_button.mouse_clicked(e)) {
        event_memory.set_event_table_frame(null);
        parent_window.dispose();
      }
      else if (search_button.mouse_clicked(e)) {
        search();
        return;
      }
      else if (export_button.mouse_clicked(e)) {
        export();
        return;
      }
      else if (import_button.mouse_clicked(e)) {
        import_();
        return;
      }
      else if (sync_button.mouse_clicked(e)) {
        sync();
        return;
      }
      else if (!show_sure_button && delete_button.mouse_clicked(e)) {
        if (event_table.getSelectedRows().length > 0) {
          button_timer.start();
          show_sure_button = true;
          result_text = "delete " + Integer.toString(event_table.getSelectedRows().length) + " item(s)?";
        }
        else {
          result_text = "select events first!";
        }
        this.repaint();
        return;
      }
      else if (show_sure_button && sure_button.mouse_clicked(e)) 
      {
        button_timer.stop();
        show_sure_button = false;
        int[] sel_rows = event_table.getSelectedRows();
        
        // array of events to delete is nessesary because of
        // the row indexes are wrong after first delete because table is rebuild.
        
        HoopCalendarEvent[] events_to_delete = new HoopCalendarEvent[sel_rows.length];
        
        for (int i = 0; i < sel_rows.length; i++) {
          events_to_delete[i] = event_memory.get_event(sel_rows[i]);
        }
        for (int i = 0; i < events_to_delete.length; i++) {
          events_to_delete[i].delete();
        }
        result_text = Integer.toString(sel_rows.length) + " item(s) deleted!";
        event_memory.save();
      }
    }


    /**
     *  Handle mouse press.
     *
     * @param  e  mouse event
     */
    public void mousePressed(MouseEvent e) {
      search_button.mouse_pressed(e);
      import_button.mouse_pressed(e);
      export_button.mouse_pressed(e);
      sync_button.mouse_pressed(e);
      sure_button.mouse_pressed(e);
      delete_button.mouse_pressed(e);
      close_button.mouse_pressed(e);
      this.repaint();
    }


    /**
     *  Handle mouse release.
     *
     * @param  e  mouse event
     */
    public void mouseReleased(MouseEvent e) {
      search_button.mouse_released(e);
      import_button.mouse_released(e);
      export_button.mouse_released(e);
      sync_button.mouse_released(e);
      sure_button.mouse_released(e);
      delete_button.mouse_released(e);
      close_button.mouse_released(e);
      this.repaint();
    }


    /**
     *  Handle mouse move.
     *
     * @param  e  mouse event
     */
    public void mouseMoved(MouseEvent e) {
      search_button.mouse_over(e);
      import_button.mouse_over(e);
      export_button.mouse_over(e);
      sync_button.mouse_over(e);
      sure_button.mouse_over(e);
      delete_button.mouse_over(e);
      close_button.mouse_over(e);
      this.repaint();
    }


    /**
     *  Reset result text, stop pending delete action (sure button set to invisible), decollapse all rows.
     */
    public void reset() {
      // but don't reset the selection
      show_sure_button = false;
      button_timer.stop();
      result_text = "";
      event_table.setRowHeight(HoopDateLookPanel.slot_height);
      this.repaint();
    }


    /**
     *  Export selected events
     */
    private void export() {
      show_sure_button = false;
      button_timer.stop();
      result_text = "";
      JFileChooser file_chooser = new JFileChooser();
      file_chooser.setFileFilter(
        new javax.swing.filechooser.FileFilter() {
          public boolean accept(File f) {
            if (f.isDirectory()) {
              return true;
            }
            if (f.toString().endsWith(".vcs")) {
              return true;
            }
            return false;
          }


          public String getDescription() {
            return "vCalendar *.vcs";
          }
        });
      file_chooser.setFileHidingEnabled(true);
      int retVal = file_chooser.showDialog(this, "Export");
      if (retVal == JFileChooser.APPROVE_OPTION) {
        try {
          result_text = Integer.toString(event_memory.export_vCalendar(
              file_chooser.getSelectedFile(), event_table.getSelectedRows(), null, false)) +
              "item(s) exported.";
        }
        catch (Exception b) {
          result_text = b.getMessage();
        }
      }
      this.repaint();
    }


    /**
     *  Import events from chosen file
     */
    private void import_() {
      this.reset();
      event_table.clearSelection();
      ((EventTableFrame) parent_window).set_last_selected_row(-1);
      JFileChooser file_chooser = new JFileChooser();
      file_chooser.setFileFilter(
        new javax.swing.filechooser.FileFilter() {
          public boolean accept(File f) {
            if (f.isDirectory()) {
              return true;
            }
            if (f.toString().endsWith(".vcs")) {
              return true;
            }
            return false;
          }


          public String getDescription() {
            return "vCalendar *.vcs";
          }
        });
      file_chooser.setFileHidingEnabled(true);
      int retVal = file_chooser.showDialog(this, "Import");
      if (retVal == JFileChooser.APPROVE_OPTION) {
        try {
          event_memory.import_vCalendar(file_chooser.getSelectedFile(), null);
        }
        catch (Exception x) {
          result_text = x.getMessage();
        }
      }
      event_table.clearSelection();
      ((EventTableFrame) parent_window).set_last_selected_row(-1);
      int k = 0;   //counter of imported event
      for (int i = 0; i < event_memory.get_size(); i++) {
        if (event_memory.get_event(i).get_now_imported()) {
          event_memory.get_event(i).set_now_imported(false);
          event_table.addRowSelectionInterval(i, i);
          k++;
        }
      }
      if (k != 0) {
        // row height of unselected rows must set in a separate loop because addRowSelection causes call of reset()
        for (int i = 0; i < event_memory.get_size(); i++) {
          if (event_table.isRowSelected(i)) {
            event_table.scrollRectToVisible(event_table.getCellRect(i, 3, true));
          }
          else {
            event_table.setRowHeight(i, 2);
          }
        }
      }
      if (result_text.length() == 0) {
        result_text = Integer.toString(k) + " item(s) imported!";
      }
      this.repaint();
      event_memory.save();
    }


    /**
     *  Open sync dialog
     */
    private void sync() {
      this.reset();
      event_table.clearSelection();
      ((EventTableFrame) parent_window).set_last_selected_row(-1);
      SyncDialog sync_dialog = new SyncDialog(event_memory, (JFrame) parent_window);
      sync_dialog.setLocation((int) this.getLocationOnScreen().getX() + 20,
          (int) this.getLocationOnScreen().getY() + 20);
      sync_dialog.setVisible(true);
      event_table.clearSelection();
      int k = 0;  // counter of imported events
      for (int i = 0; i < event_memory.get_size(); i++) {
        if (event_memory.get_event(i).get_now_imported()) {
          event_memory.get_event(i).set_now_imported(false);
          event_table.addRowSelectionInterval(i, i);
          k++;
        }
      }
      if (k != 0) {
        // row height of unselected rows must set in a separate loop because addRowSelection causes call of reset()
        for (int i = 0; i < event_memory.get_size(); i++) {
          if (event_table.isRowSelected(i)) {
            event_table.scrollRectToVisible(event_table.getCellRect(i, 3, true));
          }
          else {
            event_table.setRowHeight(i, 2);
          }
        }
      }
      result_text = Integer.toString(k) + " item(s) imported!";
      this.repaint();
      event_memory.save();
    }


    /**
     *  Start searching.
     */
    private void search() {
      show_sure_button = false;
      button_timer.stop();
      event_table.clearSelection();
      ((EventTableFrame) parent_window).set_last_selected_row(-1);
      int found_rows = 0;
      // parse for search strings
      ArrayList search_strings = new ArrayList();  // to store all search strings
      char[] chars = search_text_field.getText().toCharArray();
      for (int i = 0; i < chars.length; i++) {
        String tmp = "";
        char separator = ' ';
        while (i < chars.length && chars[i] != separator) {
          if (chars[i] == '"') {
            separator = '"';
          }
          else {
            tmp = tmp + chars[i];
          }
          i++;
        }
        if (tmp.length() > 0) {
          search_strings.add(tmp);
        }
      }
      String tmp = "";
      for (int i = 0; i < search_strings.size(); i++) {
        tmp = tmp + "\"" + (String) search_strings.get(i) + "\" ";
      }
      search_text_field.setText(tmp.trim());
      // start seaching in table here
      for (int i = event_table_model.getRowCount() - 1; i >= 0; i--) {
        int match_counter = 0;
        for (int k = 0; k < search_strings.size(); k++) {
          if (((String) event_table_model.getValueAt(i, 0)).toUpperCase().indexOf(((String) search_strings.get(k)).toUpperCase()) != -1 ||
              ((String) event_table_model.getValueAt(i, 1)).toUpperCase().indexOf(((String) search_strings.get(k)).toUpperCase()) != -1 ||
              ((String) event_table_model.getValueAt(i, 2)).toUpperCase().indexOf(((String) search_strings.get(k)).toUpperCase()) != -1 ||
              ((String) event_table_model.getValueAt(i, 3)).toUpperCase().indexOf(((String) search_strings.get(k)).toUpperCase()) != -1 ||
              ((String) event_table_model.getValueAt(i, 5)).toUpperCase().indexOf(((String) search_strings.get(k)).toUpperCase()) != -1) {
            match_counter++;
          }
        }
        if (match_counter == search_strings.size()) {
          // all search strings found in date
          event_table.addRowSelectionInterval(i, i);
          found_rows++;
        }
      }
      if (found_rows > 0) {
        for (int i = 0; i < event_table_model.getRowCount(); i++) {
          if (!event_table.isRowSelected(i)) {
            event_table.setRowHeight(i, 2);
          }
          else {
            event_table.scrollRectToVisible(event_table.getCellRect(i, 3, true));
          }
        }
      }
      result_text = Integer.toString(found_rows) + " item(s) found.";
      this.repaint();
    }
  }


  /**
   *  Event table model
   */
  public static class EventTableModel extends AbstractTableModel {

    private EventMemory event_memory;


    /**
     *  Constructor for the EventTableModel object
     *
     * @param  tm  Description of the Parameter
     */
    public EventTableModel(EventMemory tm) {
      event_memory = tm;
    }


    /**
     *  Gets the columnCount attribute of the EventTableModel object
     *
     * @return    The columnCount value
     */
    public int getColumnCount() {
      return 6;
    }


    /**
     *  Gets the rowCount attribute of the EventTableModel object
     *
     * @return    The rowCount value
     */
    public int getRowCount() {
      return event_memory.get_size();
    }


    /**
     *  Gets the valueAt attribute of the EventTableModel object
     *
     * @param  i    Description of the Parameter
     * @param  col  Description of the Parameter
     * @return      The valueAt value
     */
    public Object getValueAt(int i, int col) {
    	HoopCalendarEvent t = event_memory.get_event(i);
      switch (col) {
          case 0:
          {
            // Begin Date
            return Converter.ms2dmy(t.get_begin_UTC_ms());
          }
          case 1:
          {
            // Begin Time
            return Converter.ms2hm(t.get_begin_UTC_ms());
          }
          case 2:
          {
            // Class
            if (t.get_vcal_class() == HoopCalendarEvent.Private) {
              return "private";
            }
            return "public";
          }
          case 3:
          {
            // Period
            String pm = Integer.toString(t.get_period_multiplier()) + "-";
            if (t.get_period_multiplier() == 1) {
              pm = "";
            }
            switch (t.get_period()) {
                case (HoopCalendarEvent.None):
                  return "none";
                case (HoopCalendarEvent.Daily):
                  return pm + "daily";
                case (HoopCalendarEvent.Weekly):
                  return pm + "weekly";
                case (HoopCalendarEvent.Monthly):
                  return pm + "monthly";
                case (HoopCalendarEvent.Yearly):
                  return pm + "yearly";
            }
          }
          case 4:
          {
            // Number of periods
            if (t.get_period() != HoopCalendarEvent.None) {
              return Integer.toString(t.get_number_of_periods());
            }
            return "---";
          }
          case 5:
          {
            // Text
            return t.get_summary();
          }
          default:
            return "";
      }
    }


    /**
     *  Sets the valueAt attribute of the EventTableModel object
     *
     * @param  content  The new valueAt value
     * @param  row      The new valueAt value
     * @param  col      The new valueAt value
     */
    public void setValueAt(Object content, int row, int col) {
    }


    /**
     *  Gets the cellEditable attribute of the EventTableModel object
     *
     * @param  row  Description of the Parameter
     * @param  col  Description of the Parameter
     * @return      The cellEditable value
     */
    public boolean isCellEditable(int row, int col) {
      return false;
    }


    /**
     *  Gets the columnName attribute of the EventTableModel object
     *
     * @param  col  Description of the Parameter
     * @return      The columnName value
     */
    public String getColumnName(int col) {
      switch (col) {
          case 0:
            return "(first) Date";
          case 1:
            return "Time";
          case 2:
            return "Class";
          case 3:
            return "Cycle";
          case 4:
            return "times";
          case 5:
            return "Summary";
          default:
            return "";
      }
    }
  }
}

