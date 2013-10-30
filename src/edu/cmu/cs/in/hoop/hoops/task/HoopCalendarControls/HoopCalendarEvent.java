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
import java.util.*;

import edu.cmu.cs.in.hoop.hoops.base.HoopBase;

/**
 *  Describes an event.
 */
public class HoopCalendarEvent extends HoopBase implements Comparable<Object> 
{
	private static final long serialVersionUID = 677036993550551376L;
	
	private String UID;  // Unique Identifier (vCalendar 1.0)
	private long begin_UTC_ms;
	private long end_UTC_ms;
	private long alarm_UTC_ms;
	private long last_mod_UTC_ms;
	private int period = HoopCalendarEvent.None;
  
	/**
	 *  Events cycle None
	 */
	public final static int None = 0;
  
	/**
	 *  Events cycle Daily
	 */
	public final static int Daily = 1;
  
	/**
	 *  Events cycle Weekly
	 */
	public final static int Weekly = 2;
  
	/**
	 *  Events cycle Monthly
	 */
	public final static int Monthly = 3;
  
	/**
	 *  Events cycle Yearly
	 */
	public final static int Yearly = 4;

	private int period_multiplier = 1;  // to provide the possiblility for biweekly or evry other day..

	/**
	 *  Events class Public
	 */
	public final static int Public = 0;
  
	/**
	 *  Events class Private
	 */
	public final static int Private = 1;

	private int number_of_periods = 2;
	private int alarm_counter = 0;
	private boolean alarm_active = false;

	private int renderer_group;  // line in panel where it is rendered
	private Color renderer_color;
	private String summary = "new event";
	private String summary_encoding;  // encoding of summary e.g. "ISO-8859-15"
	private String description = "";
	private String description_encoding;
	private EventRenderer my_renderer;
	private EventMemory event_memory;
	private EditorFrame my_editor_frame = null;
	private boolean now_imported = false;  // to remeber that the event is imported now
  
	/**
	 *  Events class Public or Private.<br>
	 *  Public - will be exchanged with remote database if synchronizing.<br>
	 *  Private - will NOT be exchanged with remote database if synchronizing.
	 */
	private int vcal_class = HoopCalendarEvent.Public;

	/**
	 *  Constructor for the Event object
	 *
	 * @param  b   Begin time UTC in ms.
	 * @param  rg  Renderer group.<br>
	 *             Determines the row in main window where the events rectangle is drawn.
	 * @param  tm  Event memory, the object that stores this and other events.
	 */
	public HoopCalendarEvent(long b, int rg, EventMemory tm) 
	{
		setClassName ("HoopCalendarEvent");
		debug ("HoopCalendarEvent ()");
		
		generate_UID();
		last_mod_UTC_ms = new GregorianCalendar().getTime().getTime();  // is set to now
		event_memory = tm;
		b = b / (5L * 60L * 1000L) * (5L * 60L * 1000L);   //set in 5 min steps
		begin_UTC_ms = b;
		end_UTC_ms = b + 60L * 60L * 1000L;  // one hour duration for default
		alarm_UTC_ms = b;
		renderer_group = rg;
		renderer_color = new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255));
		summary_encoding = tm.default_encoding;
		description_encoding = tm.default_encoding;
	}

	/**
	 *  Constructor for the Event object
	 *
	 * @param  tm  Event memory, the object that stores this and other events.
	 */
	public HoopCalendarEvent(EventMemory tm) 
	{
		setClassName ("HoopCalendarEvent");
		debug ("HoopCalendarEvent ()");
		
		generate_UID();
		
		last_mod_UTC_ms = new GregorianCalendar().getTime().getTime();   // is set to now
		event_memory = tm;
		renderer_group = 0;
		renderer_color = new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255));
		summary_encoding = tm.default_encoding;
		description_encoding = tm.default_encoding;
	}

	/**
	 *  Compare to another event.<br>
	 *  The criteria is the events begin time only.
	 *
	 * @param  o  other event.
	 * @return    0 - if both begin time are equal<br>
	 *            1 - if own begin time is later than the other<br>
	 *           -1 - if own begin time is earlier than the other
	 */
	public int compareTo(Object o) 
	{
		if (this.begin_UTC_ms > ((HoopCalendarEvent) o).begin_UTC_ms) 
		{
			return 1;
		}
    
		if (this.begin_UTC_ms == ((HoopCalendarEvent) o).begin_UTC_ms) 
		{
			return 0;
		}
    
		return -1;
	}

	/**
	 *  Set summary
	 *
	 * @param  s  summary
	 */
	public synchronized void set_summary(String s) 
	{
		summary = s;
	}

	/**
	 *  Get summary
	 *
	 * @return    summary
	 */
	public synchronized String get_summary() 
	{
		return summary;
	}

	/**
	 *  Set summary encoding (e.g. "ISO-8859-15").
	 *
	 * @param  s  summary encoding
	 */
	public synchronized void set_summary_encoding(String s) 
	{
		if (s.length() == 0) 
		{  
			// if string is empty the operating system's default charset is used
			summary_encoding = event_memory.default_encoding;  // get os default charset
		}
		else 
		{
			summary_encoding = s;
		}
	}

	/**
	 *  Get summaries encoding (e.g. "ISO-8859-15").
	 *
	 * @return    summaries encoding
	 */
	public synchronized String get_summary_encoding() 
	{
		return summary_encoding;
	}

	/**
	 *  Set description
	 *
	 * @param  s  description.
	 */
	public synchronized void set_description(String s) 
	{
		description = s;
	}

	/**
	 *  Get description
	 *
	 * @return    description
	 */
	public synchronized String get_description() 
	{
		return description;
	}

	/**
	 *  Set description encoding.
	 *
	 * @param  s  description encoding (e.g. "ISO-8859-15")
	 */
	public synchronized void set_description_encoding(String s) 
	{
		if (s.length() == 0)
		{  
			// if string is empty the operating system's default charset is used
			description_encoding = event_memory.default_encoding;   // get os default charset
		}
		else 
		{
			description_encoding = s;
		}
	}

	/**
	 *  Get description encoding.
	 *
	 * @return    description encoding (e.g. "ISO-8859-15")
	 */
	public synchronized String get_description_encoding() 
	{
		return description_encoding;
	}

	/**
	 *  Set begin time in UTC (ms).
	 *
	 * @param  l  begin time in UTC (ms)
	 */
	public synchronized void set_begin_UTC_ms(long l) 
	{
		begin_UTC_ms = l / (5L * 60L * 1000L) * (5L * 60L * 1000L);   // set in 5 min steps
	}

	/**
	 *  Set UID.
	 *
	 * @param  s  UID
	 */
	public synchronized void set_UID(String s) 
	{
		UID = s;
	}

	/**
	 *  Get UID.
	 *
	 * @return    UID
	 */
	public synchronized String get_UID() 
	{
		return UID;
	}

	/**
	 *  Set last modified time in UTC (ms).
	 *
	 * @param  l  last modified time in UTC (ms)
	 */
	public synchronized void set_last_mod_UTC_ms(long l) 
	{
		last_mod_UTC_ms = l / 1000L * 1000L;   // in 1 sec steps
	}

	/**
	 *  Get last modified time in UTC (ms).
	 *
	 * @return    last modified time in UTC (ms)
	 */
	public synchronized long get_last_mod_UTC_ms()
	{
		return last_mod_UTC_ms;
	}

	/**
	 *  Get begin time in UTC (ms).
	 *
	 * @return    begin time in UTC (ms)
	 */
	public synchronized long get_begin_UTC_ms() 
	{
		return begin_UTC_ms;
	}

	/**
	 *  Set end time in UTC (ms).
	 *
	 * @param  l  Description of the Parameter
	 */
	public synchronized void set_end_UTC_ms(long l) 
	{
		end_UTC_ms = l / (5 * 60 * 1000) * (5 * 60 * 1000);   // set in 5 min steps
	}

	/**
	 *  Get end time in UTC (ms).
	 *
	 * @return    end time in UTC (ms)
	 */
	public synchronized long get_end_UTC_ms() 
	{
		return end_UTC_ms;
	}

	/**
	 *  Set alarm time in UTC (ms).
	 *
	 * @param  l  alarm time in UTC (ms)
	 */
	public synchronized void set_alarm_UTC_ms(long l) 
	{
		alarm_UTC_ms = l / (5L * 60L * 1000L) * (5L * 60L * 1000L);  // set in 5 min steps
	}

	/**
	 *  Get alarm time in UTC (ms).
	 *
	 * @return    alarm time in UTC (ms)
	 */
	public synchronized long get_alarm_UTC_ms() 
	{
		return alarm_UTC_ms;
	}

	/**
	 *  Set vcal class
	 *
	 * @param  i  Public or Private
	 */
	public synchronized void set_vcal_class(int i) 
	{
		vcal_class = Math.min(Math.max(i, Public), Private);
	}

	/**
	 *  Get vcal class
	 *
	 * @return    Public or Private
	 */
	public synchronized int get_vcal_class() 
	{
		return vcal_class;
	}

	/**
	 *  Set period.
   	*
   	* @param  i  period (range: None, Daily, Weekly, Monthly or Yearly)
   	*/
	public synchronized void set_period(int i) 
	{
		period = Math.min(Math.max(i, None), Yearly);
	}

	/**
	 *  Get period
	 *
	 * @return    period
	 */
	public synchronized int get_period() 
	{
		return period;
	}

	/**
	 *  Get period as string
	 *
	 * @return    period as string (e.g. weekly, daily,...)
	 */
	public synchronized String get_period_as_string() 
	{
		String pms = "";
		
		if (period_multiplier != 1) 
		{
			pms = Integer.toString(period_multiplier) + "-";
		}
		
		if (period == None) 
		{
			return "once";
		}
		else if (period == Daily) 
		{
			return pms + "daily";
		}
		else if (period == Weekly) 
		{
			return pms + "weekly";
		}
		else if (period == Monthly) 
		{
			return pms + "monthly";
		}
		
		return pms + "yearly";
	}

	/**
	 *  Set number of periods.
	 *
	 * @param  i  number of periods
	 */
	public synchronized void set_number_of_periods(int i) 
	{
		number_of_periods = Math.min(Math.max(i, 2), 999);
	}

	/**
	 *  Get number of periods.
	 *
	 * @return    number of periods
	 */
	public synchronized int get_number_of_periods() 
	{
		return number_of_periods;
	}

	/**
	 *  Set period multiplier.
	 *
	 * @param  i  period multiplier
	 */
	public synchronized void set_period_multiplier(int i) 
	{
		period_multiplier = Math.min(Math.max(i, 1), 9);
	}

	/**
	 *  Get period multiplier.
	 *
	 * @return    period multiplier
	 */
	public synchronized int get_period_multiplier() 
	{
		return period_multiplier;
	}

	/**
	 *  Set alarm counter.
	 *
	 * @param  i  alarm counter
	 */
	public synchronized void set_alarm_counter(int i) 
	{
		alarm_counter = Math.max(i, 0);
	}

	/**
	 *  Get alarm counter.
	 *
	 * @return    Description of the Return Value
	 */
	public synchronized int get_alarm_counter() 
	{
		return alarm_counter;
	}

	/**
	 *  Increase alarm counter.
	 */
	public synchronized void inc_alarm_counter() 
	{
		alarm_counter++;
	}

	/**
	 *  Set renderer group.<br>
	 *  Determines the rows where the event is drawn in the main window.
	 *
	 * @param  i  renderer group (range 0 - 4)
	 */
	public synchronized void set_renderer_group(int i) 
	{
		renderer_group = Math.min(Math.max(i, 0), 4);
	}

	/**
	 *  Get renderer group.<br>
	 *  Indicates the rows where the event is drawn in the main window.
	 *
	 * @return    renderer group (range 0 - 4)
	 */
	public synchronized int get_renderer_group() 
	{
		return renderer_group;
	}

	/**
	 *  Get renderer colour
	 *
	 * @return    Description of the Return Value
	 */
	public synchronized Color get_renderer_color() 
	{
		return renderer_color;
	}

	/**
	 *  Set renderer colour
	 *
	 * @param  c  colour
	 */
	public synchronized void set_renderer_color(Color c) 
	{
		renderer_color = c;
	}

	/**
	 *  Set "alarm active"-flag
	 *
	 * @param  b  true - set alarm active.<br>
	 *            false - set alarm inactive.
	 */
	public synchronized void set_alarm_active(boolean b) 
	{
		alarm_active = b;
	}

	/**
	 *  Get "alarm active"-flag
	 *
	 * @return    "alarm active"-flag
	 */
	public synchronized boolean get_alarm_active() 
	{
		return alarm_active;
	}

	/**
	 *  Get my editor frame.
	 *
	 * @return    my editor frame or null.
	 */
	public synchronized EditorFrame get_my_editor_frame() 
	{
		return my_editor_frame;
	}

	/**
	 *  Set my editor frame.<br>
	 *  Sets the focus to the renderer if ef != null or otherwise.<br>
	 *  removes the focus from the renderer.
	 *
	 * @param  ef  editor frame or null
	 */
	public synchronized void set_my_editor_frame(EditorFrame ef) 
	{
		my_editor_frame = ef;
		
		if (my_renderer != null) 
		{
			if (ef != null) 
			{
				my_renderer.set_focus(true);
			}
			else 
			{
				my_renderer.set_focus(false);
			}
		}
	}

	/**
	 *  Sets the event_renderer attribute of the Event object
	 *
	 * @param  tr  The new event_renderer value
	 */
	public synchronized void set_event_renderer(EventRenderer tr) 
	{
		my_renderer = tr;
	}

	/**
	 *  Get my event renderer.
	 *
	 * @return    my event renderer
	 */
	public synchronized EventRenderer get_event_renderer() 
	{
		return my_renderer;
	}

	/**
	 *  Set "now imorted"-flag.
	 *
	 * @param  i  true - is now imorted.<br>
	 *            false - is not now imorted.
	 */
	public synchronized void set_now_imported(boolean i) 
	{
		now_imported = i;
	}

	/**
	 *  Get "now imorted"-flag.
	 *
	 * @return    now imorted
	 */
	public synchronized boolean get_now_imported() 
	{
		return now_imported;
	}

	/**
	 *  Get event memory
	 *
	 * @return    event memory
	 */
	public synchronized EventMemory get_event_memory() 
	{
		return event_memory;
	}

	/**
	 *  Changed.<br>
	 *  Sets "last modified time" to now and informs its renderer and memory.<br>
	 *  Must be called always after changing data of the event.
	 */
	public synchronized void changed() 
	{
		set_last_mod_UTC_ms(new GregorianCalendar().getTime().getTime());
		
		if (my_renderer != null) 
		{
			my_renderer.changed();
		}
		
		event_memory.changed();
	}

	/**
	 *  Delete event itself, its renderer and delete it from memory.<br>
	 *  Dispose all attached windows (Editor...).
	 */
	public synchronized void delete() 
	{
		set_last_mod_UTC_ms(new GregorianCalendar().getTime().getTime());
		event_memory.delete_event(this);
		my_renderer.delete();
		
		if (my_editor_frame != null) 
		{
			my_editor_frame.dispose();
		}
	}

	/**
	 *  Clone event.<br>
	 *  But UID is a new one in the cloned event.
	 *
	 * @return    Description of the Return Value
	 */
	public synchronized HoopCalendarEvent clone2() 
	{
		HoopCalendarEvent t = new HoopCalendarEvent(event_memory);
		t.set_alarm_active(alarm_active);
		t.set_alarm_counter(alarm_counter);
		t.set_alarm_UTC_ms(alarm_UTC_ms);
		t.set_begin_UTC_ms(begin_UTC_ms);
		t.set_end_UTC_ms(end_UTC_ms);
		t.set_period(period);
		t.set_period_multiplier(period_multiplier);
		t.set_number_of_periods(number_of_periods);
		t.set_renderer_color(renderer_color);
		t.set_renderer_group(renderer_group);
		t.set_summary(summary);
		t.set_description(description);
		t.set_last_mod_UTC_ms(last_mod_UTC_ms);
		t.set_description_encoding(description_encoding);
		t.set_summary_encoding(summary_encoding);
		return t;
	}

	/**
	 *  Set alarm counter to next after now
	 */
	public synchronized void set_alarm_counter_to_next_after_now() 
	{
		long now_ms = new GregorianCalendar().getTime().getTime();
		
		alarm_counter = (int) Math.max(((now_ms - alarm_UTC_ms) / Converter.period2ms(period, period_multiplier)) - 1, 0);
		
		while (!(Converter.UTCplusPeriod2UTC(alarm_UTC_ms, period, alarm_counter, period_multiplier) > now_ms)) 
		{
			alarm_counter++;
		}
	}

	/**
	 *  Generate UID
	 */
	private void generate_UID() 
	{
		UID = "rr-e.de-" + Double.toString(Math.random()).substring(2) + "-" + Long.toString(new GregorianCalendar().getTime().getTime());  
         // "rr-e.de-" + UID is a random number + creating time UTC in ms
	}
}

