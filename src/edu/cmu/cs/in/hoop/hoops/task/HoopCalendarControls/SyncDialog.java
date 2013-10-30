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

import javax.swing.*;

import edu.cmu.cs.in.controls.base.HoopCustomJButton;
import edu.cmu.cs.in.controls.base.HoopCustomJComponent;
import edu.cmu.cs.in.controls.base.HoopCustomJPanel;
import edu.cmu.cs.in.controls.base.HoopJDialog;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

/**
 *  Dialog window for synchronization of local and remote database via FTP or a
 *  net file system
 */
public class SyncDialog extends HoopJDialog 
{
	private static final long serialVersionUID = -7200755961085439126L;
	private JPanel contentPane;
	
	/**
	 *  The panel
	 */
	public SyncPanel sync_panel;

  /**
   *  Constructor for the SyncDialog object
   *
   * @param  tm  event memory, storing the local database
   * @param  p   parent frame
   */
  public SyncDialog(EventMemory tm, JFrame p) 
  {
    super(p, true,"SyncDialog");
    
	setClassName ("SyncDialog");
	debug ("SyncDialog ()");    
    
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    
    try {
      contentPane = (JPanel) this.getContentPane();
      this.setSize(14 * DateLookPanel.slot_height + DateLookPanel.frame_decor_width,
          (7 * DateLookPanel.slot_height) + DateLookPanel.frame_decor_height);
      this.setTitle("DateLook Synchroniser");
      sync_panel = new SyncPanel(tm, this);
      contentPane.add(sync_panel);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }


  /**
   *  Process window event
   *
   * @param  e  window event
   */
  protected void processWindowEvent(WindowEvent e) {
    // catch the window close button when sync is in progress
    if (sync_panel.progress_bar.getValue() == 0) {
      super.processWindowEvent(e);
    }
  }


  /**
   *  Panel that contains components to control the synchronization<br>
   *  of local and remote database.
   */
  public static class SyncPanel extends HoopCustomJPanel 
  {
	private static final long serialVersionUID = -1130282343869980698L;
	
	private EventMemory event_memory;
    private HoopCustomJButton sync_button;
    private HoopCustomJButton close_button;
    private HoopCustomJComponent protocol_entry;
    private HoopCustomJComponent UID_text;
    private HoopCustomJComponent PW_text;
    private HoopCustomJComponent HP_text;
    private JTextField jTextField_user_name = new JTextField();
    private JPasswordField jPasswordField_password;
    private JTextField jTextField_ftp_host_path = new JTextField();    // only one is visibale
    private JTextField jTextField_path = new JTextField();    // depending on the protocol
    private JProgressBar progress_bar = new JProgressBar();
    private final static int rel_font_size = 48;


    /**
     *  Constructor for the SyncPanel object
     *
     * @param  tm  event memory, storing the local database
     * @param  pw  parent window
     */
    public SyncPanel(EventMemory tm, 
    				 SyncDialog pw) 
    {
      super (true);
            
      event_memory = tm;

      SyncSettings sync_settings = new SyncSettings();
      String[] sp = {"No server", "FTP-server", "file-server"};
      protocol_entry = new HoopCustomJComponent(this, bg_color, Color.orange, sync_settings.get_protocol(), 2, 1, 1, sp, "", 20, 50, 280, rel_font_size);
      String[] sa = {"UID:"};
      UID_text = new HoopCustomJComponent(this, bg_color, bg_color, 0, 0, 0, 1, sa, "", 20, 230, 180, rel_font_size);
      String[] sb = {"PW:"};
      PW_text = new HoopCustomJComponent(this, bg_color, bg_color, 0, 0, 0, 1, sb, "", 520, 230, 180, rel_font_size);
      String[] sc = {"H/P:"};
      HP_text = new HoopCustomJComponent(this, bg_color, bg_color, 0, 0, 0, 1, sc, "", 20, 430, 180, rel_font_size);

      jTextField_user_name.setText(sync_settings.get_user_name());
      jTextField_user_name.setToolTipText("enter user name");
      jTextField_user_name.addKeyListener(this);
      jTextField_ftp_host_path.setText(sync_settings.get_ftp_host_path_name());
      jTextField_ftp_host_path.setToolTipText("enter hostname'/'path");
      jTextField_ftp_host_path.addKeyListener(this);
      jTextField_path.setText(sync_settings.get_path_name());
      jTextField_path.setToolTipText("enter path");
      jTextField_path.addKeyListener(this);
      jPasswordField_password = new JPasswordField(sync_settings.get_password());
      jPasswordField_password.setToolTipText("enter password");
      jPasswordField_password.addKeyListener(this);
      if (protocol_entry.get_value() != SyncSettings.protFTP) {
        jTextField_user_name.setVisible(false);
        jPasswordField_password.setVisible(false);
        jTextField_ftp_host_path.setVisible(false);
      }
      else {
        jTextField_path.setVisible(false);
      }
      progress_bar.setBackground(Color.white);
      progress_bar.setForeground(new Color(0, 0, 90));
      this.add(jTextField_user_name);
      this.add(jPasswordField_password);
      this.add(jTextField_ftp_host_path);
      this.add(jTextField_path);
      this.add(progress_bar);
      sync_button = new HoopCustomJButton(this, new Color(0, 50, 100), Color.orange, Color.red, "sync", 18, 830, 180, rel_font_size);
      close_button = new HoopCustomJButton(this, new Color(0, 50, 100), Color.orange, Color.red, "close", 802, 830, 180, rel_font_size);
    }


    /**
     *  Paint component
     *
     * @param  g  Graphics object
     */
    public void paintComponent(Graphics g) {
      super.paintComponent(g);
      sync_button.draw(g2);
      close_button.draw(g2);
      protocol_entry.draw(g2);
      UID_text.draw(g2);
      PW_text.draw(g2);
      HP_text.draw(g2);
    }


    /**
     *  Process component event.<br>
     *  If component resized it resizes all components, <br>
     *  if then components are overlapping it resizes the frame
     *
     * @param  e  component event
     */
    public void processComponentEvent(ComponentEvent e) {
      super.processComponentEvent(e);
      if (e.getID() == ComponentEvent.COMPONENT_RESIZED) {
        // rezise text field
        Graphics g = this.getGraphics();
        Graphics2D g2 = (Graphics2D) g;
        Font font = new Font("SansSerif", Font.PLAIN, rel_font_size * this.getWidth() / 1000);
        Rectangle2D bounds = font.getStringBounds("0", g2.getFontRenderContext());

        jTextField_user_name.setFont(font);
        jPasswordField_password.setFont(font);
        jTextField_ftp_host_path.setFont(font);
        jTextField_path.setFont(font);
        progress_bar.setFont(font);

        int pw = this.getWidth();
        int ph = this.getHeight();
        jTextField_user_name.setBounds(pw * 170 / 1000, ph * 220 / 1000, pw * 330 / 1000, (int) bounds.getHeight() * 3 / 2);
        jPasswordField_password.setBounds(pw * 650 / 1000, ph * 220 / 1000, pw * 330 / 1000, (int) bounds.getHeight() * 3 / 2);
        jTextField_ftp_host_path.setBounds(pw * 170 / 1000, ph * 420 / 1000, pw * 810 / 1000, (int) bounds.getHeight() * 3 / 2);
        jTextField_path.setBounds(jTextField_ftp_host_path.getBounds());
        progress_bar.setBounds(pw * 20 / 1000, ph * 620 / 1000, pw * 960 / 1000, (int) bounds.getHeight() * 3 / 2);
      }
    }


    /**
     *  Unock all buttons and text fields for input.
     */
    public void unlock_input() {
      // unlock close-button of EventManagerFrame (parent)
      ((EventTableFrame) (parent_window.getOwner())).set_close_button_locked(false);

      // unlock input
      jTextField_user_name.setEditable(true);
      jPasswordField_password.setEditable(true);
      jTextField_ftp_host_path.setEditable(true);
      jTextField_path.setEditable(true);
      this.addMouseListener(this);
      this.addMouseWheelListener(this);
      this.addMouseMotionListener(this);
      this.addKeyListener(this);
      jTextField_user_name.addKeyListener(this);
      jTextField_ftp_host_path.addKeyListener(this);
      jTextField_path.addKeyListener(this);
      jPasswordField_password.addKeyListener(this);
    }


    /**
     *  Lock all buttons and text fields for input.
     */
    private void lock_input() {
      // lock close-button of EventManagerFrame (parent)
      ((EventTableFrame) (parent_window.getOwner())).set_close_button_locked(true);

      // lock input
      jTextField_user_name.setEditable(false);
      jPasswordField_password.setEditable(false);
      jTextField_ftp_host_path.setEditable(false);
      jTextField_path.setEditable(false);
      this.removeMouseListener(this);
      this.removeMouseWheelListener(this);
      this.removeMouseMotionListener(this);
      this.removeKeyListener(this);
      jTextField_user_name.removeKeyListener(this);
      jTextField_ftp_host_path.removeKeyListener(this);
      jTextField_path.removeKeyListener(this);
      jPasswordField_password.removeKeyListener(this);
    }


    /**
     *  Check for pressed key and handles it.<br>
     *  F1 - opens HelpFrame<br>
     *  ctrl-Q - saves the sync-settings and closes the dialog<br>
     *  ctrl-C - closes dialog<br>
     *  ctrl-S - starts syncronization
     *
     * @param  e  key event
     */
    public void keyPressed(KeyEvent e) 
    {
        if (e.getModifiers() == InputEvent.CTRL_MASK) 
        {
          if (e.getKeyCode() == KeyEvent.VK_Q) {
            SyncSettings sync_settings = new SyncSettings();
            sync_settings.save(protocol_entry.get_value(), jTextField_user_name.getText(),
                new String(jPasswordField_password.getPassword()), jTextField_ftp_host_path.getText(),
                jTextField_path.getText());
            parent_window.dispose();
            return;
          }
          if (e.getKeyCode() == KeyEvent.VK_C) {
            parent_window.dispose();
            return;
          }
          if (e.getKeyCode() == KeyEvent.VK_S) {
            start_sync();
            return;
          }
        }
    }


    /**
     *  Handle mouse click.<br>
     *  Check whether buttons/entries are hit and if true handle the action.<br>
     *  close button - save sync settings and close dialog<br>
     *  sync button - start synchronization<br>
     *  protocol entry - toggle between FTP- or net file system
     *
     * @param  e  mouse event
     */
    public void mouseClicked(MouseEvent e) {
      if (sync_button.mouse_clicked(e)) {
        start_sync();
      }
      else if (close_button.mouse_clicked(e)) {
        SyncSettings sync_settings = new SyncSettings();
        sync_settings.save(protocol_entry.get_value(), jTextField_user_name.getText(),
            new String(jPasswordField_password.getPassword()), jTextField_ftp_host_path.getText(),
            jTextField_path.getText());
        parent_window.dispose();
      }
      else if (protocol_entry.mouse_clicked(e)) {
        if (protocol_entry.get_value() == SyncSettings.protFTP) {
          jTextField_user_name.setVisible(true);
          jPasswordField_password.setVisible(true);
          jTextField_ftp_host_path.setVisible(true);
          jTextField_path.setVisible(false);
        }
        else {
          jTextField_user_name.setVisible(false);
          jPasswordField_password.setVisible(false);
          jTextField_ftp_host_path.setVisible(false);
          jTextField_path.setVisible(true);
        }
      }
    }


    /**
     *  Handle mouse press.
     *
     * @param  e  mouse event
     */
    public void mousePressed(MouseEvent e) {
      sync_button.mouse_pressed(e);
      close_button.mouse_pressed(e);
      protocol_entry.mouse_pressed(e);
      this.repaint();
    }


    /**
     *  Handle mouse release.
     *
     * @param  e  mouse event
     */
    public void mouseReleased(MouseEvent e) {
      sync_button.mouse_released(e);
      close_button.mouse_released(e);
      protocol_entry.mouse_released(e);
      this.repaint();
    }


    /**
     *  Handle mouse move.
     *
     * @param  e  mouse event
     */
    public void mouseMoved(MouseEvent e) {
      sync_button.mouse_over(e);
      close_button.mouse_over(e);
      protocol_entry.mouse_over(e);
      this.repaint();
    }


    /**
     *  Handle mouse wheel move.
     *
     * @param  e  mouse wheel event
     */
    public void mouseWheelMoved(MouseWheelEvent e) {
      if (protocol_entry.mouse_wheel_rotate(e)) {
        if (protocol_entry.get_value() == SyncSettings.protFTP) {
          jTextField_user_name.setVisible(true);
          jPasswordField_password.setVisible(true);
          jTextField_ftp_host_path.setVisible(true);
          jTextField_path.setVisible(false);
        }
        else {
          jTextField_user_name.setVisible(false);
          jPasswordField_password.setVisible(false);
          jTextField_ftp_host_path.setVisible(false);
          jTextField_path.setVisible(true);
        }
      }
      this.repaint();
      this.requestFocus();
    }


    /**
     *  Start synchronization.
     */
    private void start_sync() {
      // save values first
      SyncSettings sync_settings = new SyncSettings();
      sync_settings.save(protocol_entry.get_value(), jTextField_user_name.getText(),
          new String(jPasswordField_password.getPassword()), jTextField_ftp_host_path.getText(),
          jTextField_path.getText());

      String host_path;
      if (protocol_entry.get_value() == SyncSettings.protFTP) {
        host_path = jTextField_ftp_host_path.getText();
      }
      else {
        host_path = jTextField_path.getText();
      }
      Synchronizer synchronizer = new Synchronizer(event_memory, (SyncDialog) parent_window,
          protocol_entry.get_value(), jTextField_user_name.getText(),
          new String(jPasswordField_password.getPassword()),
          host_path, progress_bar);
      this.lock_input();

      // start synchronisation
      synchronizer.start();
    }
  }
}

