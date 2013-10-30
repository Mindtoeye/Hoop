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

/*
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import javax.swing.filechooser.*;

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.controls.base.HoopJFrame;

import java.io.*;


public class HoopCalendarTaskDialog extends HoopJFrame 
{
	private static final long serialVersionUID = 1366987389308437971L;
	
	protected JPanel contentPane;
	private DateLookPanel date_look_panel;
	private EventMemory event_memory;
	private AlarmHandler alarm_handler;
	private java.util.Timer alarm_checker = new java.util.Timer();

	public HoopCalendarTaskDialog() 
	{
		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    
		try 
		{
			date_look_panel = new DateLookPanel(this);
			event_memory = new EventMemory(date_look_panel);
			event_memory.read_data_file();     // read dates from file and store in memory
			date_look_panel.set_event_memory(event_memory);

			setIconImage(HoopLink.getImageByName("dl.png").getImage());
			Rectangle rect = (new Settings().get_position_and_size());
			this.setLocation(rect.x, rect.y);
			this.setSize(rect.width, rect.height);
			this.setTitle("DateLook 1.9.9");

			contentPane = (JPanel) this.getContentPane();
			contentPane.add(date_look_panel);
			this.addKeyListener(date_look_panel);

			alarm_handler = new AlarmHandler(event_memory);

			long timer_rate = 5 * 1000;   // check every 5 second
			alarm_checker.scheduleAtFixedRate (alarm_handler,(long) (timer_rate - Math.IEEEremainder((new GregorianCalendar()).getTime().getTime(),timer_rate)), timer_rate);
		}
		catch (Exception e) 
		{
			debug ("Error!");
			e.printStackTrace();
		}
	}

	protected void processWindowEvent(WindowEvent e) 
	{
		debug ("processWindowEvent ()");
	  
		if (e.getID() == WindowEvent.WINDOW_CLOSING) 
		{
//    		  event_memory.save();
			super.processWindowEvent(e);
			System.exit(0);
		}
		else 
		{
			super.processWindowEvent(e);
		}
	}

	public static void main(String[] args) 
	{    
		new HoopCalendarTaskDialog().setVisible(true);
	}
}
*/
