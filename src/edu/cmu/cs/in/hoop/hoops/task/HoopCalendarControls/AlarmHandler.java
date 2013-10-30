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

import java.util.*;

import edu.cmu.cs.in.hoop.hoops.base.HoopBase;

/**
 *  Checks for all events in the event memory whether an alarm is to be given now.
 */
public class AlarmHandler extends TimerTask 
{
	private EventMemory event_memory;

	/**
	 *  Constructor for the AlarmHandler object
	 *
	 * @param  tm  event memory, storing the local database
	 */
	public AlarmHandler(EventMemory tm) 
	{
		event_memory = tm;
	}

	/**
	 * 
	 */
	private void debug (String aMessage)
	{
		HoopBase.debug ("AlarmHandler",aMessage);
	}
	
	/**
	 *  Main processing method of the AlarmHandler object
	 */
	public void run() 
	{
		long now_ms = new GregorianCalendar().getTime().getTime();
    
		int open_alarm_frame_counter = 0;
    
		boolean saving_needed = false;
    
		for (int i = event_memory.get_size() - 1; i > -1; i--) 
		{
			HoopCalendarEvent t = event_memory.get_event(i);
      
			while (t.get_my_editor_frame() == null & t.get_alarm_active() &
					!(Converter.UTCplusPeriod2UTC(t.get_alarm_UTC_ms(), t.get_period(),
							t.get_alarm_counter(), t.get_period_multiplier()) > now_ms) &&
							t.get_number_of_periods() > t.get_alarm_counter()) 
			{
				debug ("New alarm going off!");
				
				AlarmFrame af = new AlarmFrame(t);
				af.setLocation(50 + open_alarm_frame_counter * 20, 50 + open_alarm_frame_counter * 20);
				af.setVisible(true);
				af.requestFocus();
				af.getToolkit().beep();
				t.inc_alarm_counter();
				saving_needed = true;
				open_alarm_frame_counter++;
			}
		}
    
		if (saving_needed) 
		{ 
			// needed to save increased alarm counters
			event_memory.save();
		}
  	}
}

