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
import edu.cmu.cs.in.controls.base.HoopCustomJButton;
import edu.cmu.cs.in.controls.base.HoopCustomJComponent;
import edu.cmu.cs.in.controls.base.HoopCustomJPanel;
import edu.cmu.cs.in.controls.base.HoopJFrame;

import java.awt.geom.*;
import java.awt.datatransfer.*;

/**
 *  Description of the Class
 */
public class DescriptionEditorFrame extends HoopJFrame 
{
	private static final long serialVersionUID = 1606123189339267412L;
	
	private JPanel contentPane;
	private JScrollPane scrollPane;
	private JSplitPane split_pane;
	private JEditorPane editor_pane;
	private ButtonPanel button_panel;

	private StringBuffer description;
	private EditorFrame my_editor_frame;


  /**
   *  Construct the frame
   *
   * @param  ef  EditorFrame from where DescriptionEditor has been opened
   * @param  sb  description of the event
   * @param  s   summary of the event
   * @param  c   colour of the event
   */
  public DescriptionEditorFrame(EditorFrame ef, StringBuffer sb, String s, Color c) 
  {
    super();

	setClassName ("DescriptionEditorFrame");
	debug ("DescriptionEditorFrame ()");    

    my_editor_frame = ef;
    description = sb;
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try {
      contentPane = (JPanel) this.getContentPane();
      setIconImage(HoopLink.getImageByName("dl.png").getImage());
      this.setSize(new Dimension(28 * DateLookPanel.slot_height + DateLookPanel.frame_decor_width,
          15 * DateLookPanel.slot_height + DateLookPanel.frame_decor_height));
      this.setTitle("DateLook DescriptionEditor");
      int h = DateLookPanel.slot_height;

      editor_pane = new JEditorPane("text/plain", description.toString());
      editor_pane.setFont(new Font("SansSerif", Font.PLAIN, h * 2 / 3));
      button_panel = new ButtonPanel(this, ef, editor_pane, s, c);
      editor_pane.addKeyListener(button_panel);

      scrollPane = new JScrollPane(editor_pane);
      split_pane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, button_panel, scrollPane);
      split_pane.setResizeWeight(0);
      split_pane.setDividerSize(4);
      split_pane.setDividerLocation((int) DateLookPanel.slot_height * 3 / 2);
      contentPane.add(split_pane);
    }
    catch (Exception e) {
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
      description.delete(0, description.length());
      description.append(editor_pane.getText());
      my_editor_frame.delete_description_editor_frame();
    }
  }


  /**
   *  Description of the Method
   */
  public void dispose() {
    description.delete(0, description.length());
    description.append(editor_pane.getText());
    super.dispose();
  }


  /**
   *  Description of the Class
   */
  public static class ButtonPanel extends HoopCustomJPanel
  {
	private static final long serialVersionUID = -6399173091848021168L;
	
	private EditorFrame my_editor_frame;
    private JEditorPane editor_pane;

    private HoopCustomJButton copy_button;
    private HoopCustomJButton cut_button;
    private HoopCustomJButton paste_button;
    private HoopCustomJButton close_button;
    private HoopCustomJComponent summary_text;
    private Clipboard clipboard;


    /**
     *  Constructor for the ButtonPanel object
     *
     * @param  pw  parent window
     * @param  ef  EditorFrame from where DescriptionEditor has been opened
     * @param  ep  Description of the Parameter
     * @param  s   summary of the event
     * @param  c   colour of the event
     */
    public ButtonPanel(Window pw, EditorFrame ef, JEditorPane ep, String s, Color c) {
      super(false);
      my_editor_frame = ef;
      editor_pane = ep;
      clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
      String[] st = {s};
      summary_text = new HoopCustomJComponent(this, c, Color.orange, 0, 0, 0, 1, st, "", 15, 200, 510, 24);
      copy_button = new HoopCustomJButton(this, new Color(0, 50, 100), Color.orange, Color.red, "copy", 570, 200, 91, 24);
      cut_button = new HoopCustomJButton(this, new Color(0, 50, 100), Color.orange, Color.red, "cut", 670, 200, 91, 24);
      paste_button = new HoopCustomJButton(this, new Color(0, 50, 100), Color.orange, Color.red, "paste", 770, 200, 91, 24);
      close_button = new HoopCustomJButton(this, new Color(0, 50, 100), Color.orange, Color.red, "close", 900, 200, 91, 24);
    }


    /**
     *  Paint component
     *
     * @param  g  Graphics object
     */
    public void paintComponent(Graphics g) 
    {
      super.paintComponent(g);

      Font font = new Font("SansSerif", Font.PLAIN, 24 * this.getWidth() / 1000);
      Rectangle2D bounds = font.getStringBounds("0", g2.getFontRenderContext());
      g2.setColor(Color.black);
      g2.setFont(font);
      g2.setFont(font);
      copy_button.draw(g2);
      cut_button.draw(g2);
      paste_button.draw(g2);
      close_button.draw(g2);
      summary_text.draw(g2);
    }


    /**
     *  Check for pressed key and handles it.<br>
     *  F1 - opens HelpFrame<br>
     *  ctrl-Q - closes the frame
     *
     * @param  e  key event
     */
    public void keyPressed(KeyEvent e) 
    {
        if (e.getModifiers() == InputEvent.CTRL_MASK) 
        {
        	if (e.getKeyCode() == KeyEvent.VK_Q) 
        	{
        		parent_window.dispose();
        		my_editor_frame.delete_description_editor_frame();
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
      if (copy_button.mouse_clicked(e)) {
        clipboard.setContents(new StringSelection(editor_pane.getSelectedText()), null);
      }
      if (cut_button.mouse_clicked(e)) {
        clipboard.setContents(new StringSelection(editor_pane.getSelectedText()), null);
        editor_pane.replaceSelection("");
      }
      if (paste_button.mouse_clicked(e)) {
        Transferable contents = clipboard.getContents(this);
        if (contents != null) {
          try {
            editor_pane.replaceSelection((String) (contents.getTransferData(DataFlavor.stringFlavor)));
          }
          catch (Exception ex) {
          }
        }
      }
      if (close_button.mouse_clicked(e)) {
        parent_window.dispose();
        my_editor_frame.delete_description_editor_frame();
      }
    }


    /**
     *  Description of the Method
     *
     * @param  e  Description of the Parameter
     */
    public void mousePressed(MouseEvent e) {
      copy_button.mouse_pressed(e);
      cut_button.mouse_pressed(e);
      paste_button.mouse_pressed(e);
      close_button.mouse_pressed(e);
      this.repaint();
    }


    /**
     *  Description of the Method
     *
     * @param  e  Description of the Parameter
     */
    public void mouseReleased(MouseEvent e) {
      copy_button.mouse_released(e);
      cut_button.mouse_released(e);
      paste_button.mouse_released(e);
      close_button.mouse_released(e);
      this.repaint();
    }


    /**
     *  Description of the Method
     *
     * @param  e  Description of the Parameter
     */
    public void mouseMoved(MouseEvent e) {
      copy_button.mouse_over(e);
      cut_button.mouse_over(e);
      paste_button.mouse_over(e);
      close_button.mouse_over(e);
      this.repaint();
    }
  }
}

