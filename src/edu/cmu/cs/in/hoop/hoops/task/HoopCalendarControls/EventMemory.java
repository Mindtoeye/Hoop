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
import java.io.*;
import javax.swing.*;

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;

import java.text.*;

/**
 *  Description of the Class
 */
public class EventMemory extends HoopBase 
{		
	private static final long serialVersionUID = -465870522557350602L;

	/**
	 *  encoding of the used operating system e.g. "ISO-8859-15"
	 */
	public final String default_encoding = new OutputStreamWriter(new ByteArrayOutputStream()).getEncoding();  // get os default charset;

	private HoopDateLookPanel date_look_panel;
	
	private ArrayList<HoopCalendarEvent> event_list = new ArrayList<HoopCalendarEvent>();
	private ArrayList<HoopCalendarEvent> deleted_event_list = new ArrayList<HoopCalendarEvent>();  // stores the deleted events for sync

	private String data_file_name="";  // file to store dates in home directory of user (vCalendar 1.0)
	private String backup_file_name="";  // file to backup dates in home directory of user (vCalendar 1.0)
	private EventTableFrame event_table_frame;

	/**
	 *  Constructor for the EventMemory object.
	 *
	 * @param  dp  Description of the Parameter
	 */
	public EventMemory(HoopDateLookPanel dp) 
	{
		setClassName ("EventMemory");
		debug ("EventMemory ()");
		
		date_look_panel = dp;
		//data_file_name = HoopLink.project.getBasePath() + File.separatorChar + ".datelook" + File.separatorChar + "dates.vcs";
		//backup_file_name = HoopLink.project.getBasePath() + File.separatorChar + ".datelook" + File.separatorChar + "dates.bak";
  	}
	
	/**
	 * 
	 */
	private void generateFileNames ()
	{
		data_file_name = HoopLink.project.getBasePath() + File.separatorChar + ".datelook" + File.separatorChar + "dates.vcs";
		backup_file_name = HoopLink.project.getBasePath() + File.separatorChar + ".datelook" + File.separatorChar + "dates.bak";	
	}

	/**
	 *  Read the database from predefined file and make a backup of that file (.bak).
	 */
	public synchronized void read_data_file() 
	{
		generateFileNames ();		
		
		try 
		{
			import_vCalendar(new File(data_file_name), null);
      
			// write a copy to backup file
			File backup_file = new File(backup_file_name);
			int[] i = new int[event_list.size()];
			for (int k = 0; k < i.length; k++) 
			{
				i[k] = k; // array for all events built
			}
			
			export_vCalendar(backup_file, i, null, false);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		// reset imported now for all
		for (int i = 0; i < this.get_size(); i++) 
		{
			this.get_event(i).set_now_imported(false);
		}
	}

	/**
	 *  Save all events to predefined file.
	 */
	public synchronized void save() 
	{
		generateFileNames ();
		
		try 
		{
			String tmp_file_name = HoopLink.project.getBasePath() + File.separatorChar + ".datelook" + File.separatorChar + "datelook" + (new Date()).getTime() + ".tmp";
			File tmp_file = new File(tmp_file_name);
			int[] i = new int[event_list.size()];
      
			for (int k = 0; k < i.length; k++) 
			{
				i[k] = k;  // array for all events built
			}
      
			export_vCalendar(tmp_file, i, null, false);

			if (tmp_file.canWrite()) 
			{
				File data_file = new File(data_file_name);
				data_file.delete();
				tmp_file.renameTo(data_file);
			}
		}
		catch (Exception b) 
		{
			b.printStackTrace();
		}
	}

	/**
	 *  Revert to the backup database (*.bak) and rename file *.bak to *.vcs and reads it.
	 */
	public synchronized void revert() 
	{
		File backup_file = new File(backup_file_name);
    
		if (backup_file.canWrite()) 
		{
			File data_file = new File(data_file_name);
			data_file.delete();
			backup_file.renameTo(data_file);
			read_data_file();   // read old data base
		}
	}

	/**
	 *  Import events from an input stream reader.
	 *
	 * @param  isr            input stream reader
	 * @param  fl             length of data in byte
	 * @param  pb             progress bar
	 * @exception  Exception  -
	 */
	//@SuppressWarnings("unchecked")
	public synchronized void import_vCalendar(InputStreamReader isr, long fl, JProgressBar pb) throws Exception 
	{
		boolean begin_found = false;
		boolean vcalender_version_ok = false;
		final long file_length = fl;
		final JProgressBar progress_bar = pb;
	  
		if (progress_bar != null) 
		{
			progress_bar.setMaximum(1000);
			progress_bar.setValue(0);
		}

		BufferedReader b_file_reader = new BufferedReader(isr) 
		{
			// readLine() is overidden with new readLine() that unfolds lines
			// according to two different methods: remove CRLF + LWSP or
			// if it is a property value coded with quoted-printable: remove '=' + CRLF
			long read_bytes = 0;

			public String readLine() throws IOException 
			{
				String s = super.readLine();
				
				if (s == null) 
				{
					return null;
				}
				
				boolean line_unfolding_quoted_printable = false;
				
				if (s.toUpperCase().indexOf("QUOTED-PRINTABLE") < s.indexOf(":") & s.toUpperCase().indexOf("QUOTED-PRINTABLE") > 0) 
				{
					line_unfolding_quoted_printable = true;
				}

				mark(100);
				
				while (true) 
				{
					String s2 = super.readLine();
					
					if (s2 == null || s2.length() == 0) 
					{
						return s;
					}
					
					if (s2.charAt(0) == ' ' & !line_unfolding_quoted_printable | s.endsWith("=") & line_unfolding_quoted_printable) 
					{
						mark(100);
						
						if (line_unfolding_quoted_printable) 
						{
							s = s.substring(0, s.length() - 1) + s2;
						}
						else 
						{
							s = s + s2.substring(1);
						}
						
						read_bytes = read_bytes + 3;
					}
					else 
					{
						reset();
						break;
					}
				}
				
				if (progress_bar != null) 
				{
					read_bytes = read_bytes + s.length() + 2;
					progress_bar.setValue((int) ((read_bytes * 1000L) / file_length));
				}
				
				return s;
			}
      };

      // read line by line.
      while (true) 
      {
    	  String line = b_file_reader.readLine();
    	  
    	  if (line == null) 
    	  {
    		  break;
    	  }
    	  else if (!begin_found && line.toUpperCase().compareTo("BEGIN:VCALENDAR") == 0) 
    	  {
    		  begin_found = true;
    	  }
    	  else if (begin_found && !vcalender_version_ok && line.toUpperCase().startsWith("VERSION:")) 
    	  {
    		  if (line.substring(8).trim().compareTo("1.0") != 0) 
    		  {
    			  break;
    		  }
    		  else 
    		  {
    			  vcalender_version_ok = true;
    		  }
    	  }
    	  else if (vcalender_version_ok &  line.trim().toUpperCase().compareTo("BEGIN:VEVENT") == 0 | line.trim().toUpperCase().compareTo("BEGIN:VTODO") == 0) 
    	  {
    		  // handle VEVENT
    		  HoopCalendarEvent t = new HoopCalendarEvent(this);
    		  t.set_period(HoopCalendarEvent.None);
    		  t.set_vcal_class(HoopCalendarEvent.Public);
    		  t.set_summary("");
    		  t.set_description("");
    		  boolean summary_found = false;
    		  boolean dtstart_found = false;
    		  boolean dtend_found = false;
    		  boolean x_alarmcounter_found = false;
    		  boolean last_modified_found = false;
    		  while (line.trim().toUpperCase().compareTo("END:VEVENT") != 0 & line.trim().toUpperCase().compareTo("END:VTODO") != 0) 
    		  {
    			  line = b_file_reader.readLine();
    			  
    			  if (line == null) 
    			  {
    				  break;
    			  }
    			  else if (line.toUpperCase().startsWith("DTSTART")) 
    			  {
    				  Long l = Converter.dtstart2UTC(line.substring(line.indexOf(":") + 1));
    				  
    				  if (l != null) 
    				  {
    					  t.set_begin_UTC_ms(l.longValue());
    					  t.set_alarm_UTC_ms(l.longValue());
    					  dtstart_found = true;
    				  }
    			  }
    			  else if (line.toUpperCase().startsWith("DTEND")) 
    			  {
    				  Long l = Converter.dtstart2UTC(line.substring(line.indexOf(":") + 1));
    				  
    				  if (l != null) 
    				  {
    					  t.set_end_UTC_ms(l.longValue());
    					  dtend_found = true;
    				  }
    			  }
    			  else if (line.toUpperCase().startsWith("DALARM")) 
    			  {
    				  Long l = Converter.dtstart2UTC(line.substring(line.indexOf(":") + 1));
    				  
    				  if (l != null) 
    				  {
    					  t.set_alarm_UTC_ms(l.longValue());
    					  t.set_alarm_active(true);
    				  }
    			  }
    			  else if (line.toUpperCase().startsWith("RRULE")) 
    			  {
    				  int idp = line.indexOf(":");
    				  
    				  if (line.indexOf("YM") > idp & idp > 0) 
    				  {
    					  t.set_period(HoopCalendarEvent.Yearly);
    					  t.set_period_multiplier(Converter.stringNumberBehindPos2int(line, line.indexOf("YM") + 2));
    				  }
    				  else if (line.indexOf("MD") > idp & idp > 0) 
    				  {
    					  t.set_period(HoopCalendarEvent.Monthly);
    					  t.set_period_multiplier(Converter.stringNumberBehindPos2int(line, line.indexOf("MD") + 2));
    				  }
    				  else if (line.indexOf("W") > idp & idp > 0) 
    				  {
    					  t.set_period(HoopCalendarEvent.Weekly);
    					  t.set_period_multiplier(Converter.stringNumberBehindPos2int(line, line.indexOf("W") + 1));
    				  }
    				  else if (line.indexOf("D") > idp & idp > 0) 
    				  {
    					  t.set_period(HoopCalendarEvent.Daily);
    					  t.set_period_multiplier(Converter.stringNumberBehindPos2int(line, line.indexOf("D") + 1));
    				  }
    				  
    				  if (t.get_period() != HoopCalendarEvent.None) 
    				  {
    					  if (line.indexOf(" #") > line.indexOf(":")) 
    					  {
    						  int period = Integer.parseInt(line.substring(line.indexOf("#") + 1).trim());
    						  
    						  if (period == 0) 
    						  {
    							  period = 999; // 999 is used internally for unlimited
    						  }
    						  
    						  t.set_number_of_periods(period);
    					  }
    					  else 
    					  {
    						  t.set_number_of_periods(2);
    					  }
    				  }
    			  }
    			  else if (line.toUpperCase().startsWith("UID")) 
    			  {
    				  t.set_UID(line.substring(line.indexOf(":") + 1).trim());
    			  }
    			  else if (line.toUpperCase().startsWith("LAST-MODIFIED")) 
    			  {
    				  Long l = Converter.dtstart2UTC(line.substring(line.indexOf(":") + 1));
    				  
    				  if (l != null) 
    				  {
    					  t.set_last_mod_UTC_ms(l.longValue());
    				  }
    				  
    				  last_modified_found = true;
    			  }
    			  else if (line.toUpperCase().startsWith("CLASS")) 
    			  {
    				  if (line.substring(line.indexOf(":") + 1).toUpperCase().trim().equals("PUBLIC")) 
    				  {
    					  t.set_vcal_class(HoopCalendarEvent.Public);
    				  }
    				  else 
    				  {
    					  t.set_vcal_class(HoopCalendarEvent.Private);
    				  }
    			  }
    			  else if (line.toUpperCase().startsWith("SUMMARY")) 
    			  {
    				  // determine CHARSET
    				  t.set_summary_encoding(Converter.getEncodingfromLine(line));
    				  // ckeck for quoted-printable
    				  boolean qt = false;
    				  
    				  if (line.toUpperCase().indexOf("QUOTED-PRINTABLE") < line.indexOf(":") & line.toUpperCase().indexOf("QUOTED-PRINTABLE") > 0) 
    				  {
    					  qt = true;
    				  }
    				  t.set_summary(Converter.byte2unicode(line.substring(line.indexOf(":") + 1), t.get_summary_encoding(), qt));
    				  summary_found = true;
    			  }
    			  else if (line.toUpperCase().startsWith("DESCRIPTION")) 
    			  {
    				  // determine CHARSET
    				  t.set_description_encoding(Converter.getEncodingfromLine(line));
    				  // ckeck for quoted-printable
    				  boolean qt = false;
    				  
    				  if (line.toUpperCase().indexOf("QUOTED-PRINTABLE") < line.indexOf(":") & line.toUpperCase().indexOf("QUOTED-PRINTABLE") > 0) 
    				  {
    					  qt = true;
    				  }
    				  
    				  t.set_description(Converter.byte2unicode(line.substring(line.indexOf(":") + 1), t.get_description_encoding(), qt));
    			  }
    			  else if (line.toUpperCase().startsWith("X-DTLK-COLOUR:") & line.length() > 22) 
    			  {
    				  t.set_renderer_color(new Color(Integer.parseInt(line.substring(14, 17)),
    						  Integer.parseInt(line.substring(17, 20)),
    						  Integer.parseInt(line.substring(20, 23))));
    			  }
    			  else if (line.toUpperCase().startsWith("X-DTLK-GROUP:")) 
    			  {
    				  t.set_renderer_group(Integer.parseInt(line.substring(13).trim()));
    			  }
    			  else if (line.toUpperCase().startsWith("X-DTLK-ALARMCOUNTER:")) 
    			  {
    				  t.set_alarm_counter(Integer.parseInt(line.substring(20).trim()));
    				  x_alarmcounter_found = true;
    			  }
    		  }

    		  // add new date list
    		  if (summary_found & dtstart_found & dtend_found) 
    		  {
    			  // if an alarmcounter found in event then use this, otherwise
    			  // calculate a value by own so that all alarms in past are marked as performed
    			  if (!x_alarmcounter_found) 
    			  {
    				  t.set_alarm_counter_to_next_after_now();
    			  }

    			  // test for already existing UID and compare Last Modification Time
    			  boolean store_imported_event = true;
    			  boolean stop_searching = false;
    			  for (int i = 0; i < event_list.size(); i++) 
    			  {
    				  if (stop_searching) 
    				  {
    					  break;
    				  }
    				  
    				  if (((HoopCalendarEvent) event_list.get(i)).get_UID().compareTo(t.get_UID()) == 0) 
    				  {
    					  stop_searching = true;
    					  if (((HoopCalendarEvent) event_list.get(i)).get_last_mod_UTC_ms() < t.get_last_mod_UTC_ms()
    							  & last_modified_found) 
    					  {
    						  // event is already in memory and older than the imported one
    						  // delete stored event
    						  ((HoopCalendarEvent) event_list.get(i)).delete();
    					  }
    					  else 
    					  {
    						  // event is already in memory and younger than the imported one
    						  store_imported_event = false;
    					  }
    				  }
    			  }
    			  
    			  // test whether this event is to be deleted
    			  for (int i = 0; i < deleted_event_list.size(); i++) 
    			  {
    				  if (stop_searching) 
    				  {
    					  break;
    				  }
    				  
    				  if (((HoopCalendarEvent) deleted_event_list.get(i)).get_UID().compareTo(t.get_UID()) == 0) 
    				  {
    					  stop_searching = true;
    					  
    					  if (!(((HoopCalendarEvent) deleted_event_list.get(i)).get_last_mod_UTC_ms() < t.get_last_mod_UTC_ms())) 
    					  {
    						  // event is deleted later than modified -> don't store
    						  store_imported_event = false;
    					  }
    				  }
    			  }
    			  
    			  if (store_imported_event) 
    			  {
    				  event_list.add(t);
    				  t.set_now_imported(true);
    			  }
    		  }
    	  }
      }
    
      b_file_reader.close();
      Collections.sort(event_list);
      date_look_panel.changed();
	}

	/**
	 *  Import events from a file in vCalendar-format
	 *
	 * @param  file           file (object)
	 * @param  pb             progress bar
	 * @exception  Exception  -
	 */
	public synchronized void import_vCalendar(File file, JProgressBar pb) throws Exception 
	{
		if (file.canRead()) 
		{
			this.import_vCalendar(new MyInputStreamReader(new FileInputStream(file)), file.length(), pb);
		}
	}

	/**
	 *  Export events to a OutputStreamWriter
	 *
	 * @param  osw            OutputStreamWriter object
	 * @param  i              array with indexes of the events to be exported
	 * @param  pb             progress bar that shows the progress
	 * @param  public_only    true - export only events of class public<br>
	 *                        false - export all selected events
	 * @return                number of exported events
	 * @exception  Exception  -
	 */
	public synchronized int export_vCalendar(OutputStreamWriter osw, int[] i, JProgressBar pb, boolean public_only) throws Exception 
	{
		int number_of_exported_events = 0;
		JProgressBar progress_bar = pb;
		BufferedWriter file_writer = new BufferedWriter(osw);
		file_writer.write("BEGIN:VCALENDAR\r\n");
		file_writer.write("PRODID:-//Rene Ewald//DateLook 1.9.9//EN\r\n");
		file_writer.write("VERSION:1.0\r\n");
	  
		if (progress_bar != null) 
		{
			progress_bar.setValue(0);
			progress_bar.setMaximum(i.length);
		}
	  
		for (int n = 0; n < i.length; n++) 
		{
			if (progress_bar != null) 
			{
				progress_bar.setValue(n);
			}
		  
			if (!(public_only & ((HoopCalendarEvent) event_list.get(i[n])).get_vcal_class() == HoopCalendarEvent.Private)) 
			{
				write_vevent(file_writer, (HoopCalendarEvent) event_list.get(i[n]));
			}
		  
			number_of_exported_events++;
		}
	  
		file_writer.write("END:VCALENDAR\r\n");
		file_writer.close();
		return number_of_exported_events;
	}

  	/**
  	 *  Export events to file
  	 *
  	 * @param  file           database file
  	 * @param  i              array with indexes of events to be exported
  	 * @param  pb             progress bar that shows the progress
  	 * @param  public_only    true - only events of class public will be exported,<br>
  	 *                        false - all selected events will be exported
  	 * @return                number of exported events
  	 * @exception  Exception  
  	 */
  	public synchronized int export_vCalendar(File file, int[] i, JProgressBar pb, boolean public_only) throws Exception 
  	{
  		// charset to export is always US-ASCII, that is sure because there are no other characters inside the file
  		return this.export_vCalendar(new OutputStreamWriter(new FileOutputStream(file), "US-ASCII"), i, pb, public_only);
  	}

  	/**
  	 *  Delete event from database
  	 *
  	 * @param  t  event object
  	 */
  	public synchronized void delete_event(HoopCalendarEvent t) 
  	{
  		deleted_event_list.add(t);
  		event_list.remove(t);
  		this.changed();
  	}

  	/**
  	 *  Add event to database
  	 *
  	 * @param  t  event object
  	 */
  	public synchronized void add_event(HoopCalendarEvent t) 
  	{
  		deleted_event_list.remove(t);    // if an event only temporary deleted during drag
  		event_list.add(t);
  		this.changed();
  	}

  	/**
  	 *  Gets the size attribute of the EventMemory object
  	 *
  	 * @return    The size value
  	 */
  	public synchronized int get_size() 
  	{
  		return event_list.size();
  	}

  	/**
  	 *  Get event
  	 *
  	 * @param  i  index of the event
  	 * @return    event object
  	 */
  	public synchronized HoopCalendarEvent get_event(int i) 
  	{
  		return (HoopCalendarEvent) event_list.get(i);
  	}

  	/**
  	 *  Set the event_table_frame attribute of the EventMemory object
  	 *
  	 * @param  ttf  The new event_table_frame value
  	 */
  	public synchronized void set_event_table_frame(EventTableFrame ttf) 
  	{
  		event_table_frame = ttf;
  	}

  	/**
  	 *  Get the event_table_frame attribute of the EventMemory object
  	 *
  	 * @return    The event_table_frame value
  	 */
  	public synchronized EventTableFrame get_event_table_frame() 
  	{
  		return event_table_frame;
  	}

  	/**
  	 *  Must be called if values of at least one event has been changed.<br>
  	 *  The internal list of all events will be sorted.
  	 */
  	@SuppressWarnings("unchecked")
  	public void changed() 
  	{
  		Collections.sort(event_list);
  		
  		if (event_table_frame != null) 
  		{
  			event_table_frame.changed();
  		}
  	}

  	/**
  	 *  Write a event (vEvent) to a BufferedWriter object.
  	 *
  	 * @param  bw               BufferedWriter object
  	 * @param  t                event
  	 * @exception  IOException  -
  	 */
  	public void write_vevent(BufferedWriter bw, HoopCalendarEvent t) throws IOException 
  	{
  		NumberFormat formatter = NumberFormat.getNumberInstance();
  		formatter.setMinimumIntegerDigits(2);
  		formatter.setGroupingUsed(false);
  		NumberFormat formatter4 = NumberFormat.getNumberInstance();
  		formatter4.setMinimumIntegerDigits(4);
  		formatter4.setGroupingUsed(false);

  		Date d = new Date();
  		GregorianCalendar gc = new GregorianCalendar();
  		gc.setTimeZone(new SimpleTimeZone(0, "UTC"));
  		bw.write("BEGIN:VEVENT\r\n");

  		// write DTSTART
  		d.setTime(t.get_begin_UTC_ms());
  		gc.setTime(d);
  		bw.write("DTSTART:" + formatter4.format(gc.get(GregorianCalendar.YEAR)) +
  				formatter.format(gc.get(GregorianCalendar.MONTH) + 1) +
  				formatter.format(gc.get(GregorianCalendar.DAY_OF_MONTH)) + "T" +
  				formatter.format(gc.get(GregorianCalendar.HOUR_OF_DAY)) +
  				formatter.format(gc.get(GregorianCalendar.MINUTE)) +
  				"00Z\r\n");

  		// write DTEND
  		d.setTime(t.get_end_UTC_ms());
  		gc.setTime(d);
  		bw.write("DTEND:" + formatter4.format(gc.get(GregorianCalendar.YEAR)) +
  				formatter.format(gc.get(GregorianCalendar.MONTH) + 1) +
  				formatter.format(gc.get(GregorianCalendar.DAY_OF_MONTH)) + "T" +
  				formatter.format(gc.get(GregorianCalendar.HOUR_OF_DAY)) +
  				formatter.format(gc.get(GregorianCalendar.MINUTE)) +
  				"00Z\r\n");

  		// write DALARM
  		if (t.get_alarm_active()) 
  		{
  			d.setTime(t.get_alarm_UTC_ms());
  			gc.setTime(d);
  			bw.write("DALARM:" + formatter4.format(gc.get(GregorianCalendar.YEAR)) +
  					formatter.format(gc.get(GregorianCalendar.MONTH) + 1) +
  					formatter.format(gc.get(GregorianCalendar.DAY_OF_MONTH)) + "T" +
  					formatter.format(gc.get(GregorianCalendar.HOUR_OF_DAY)) +
  					formatter.format(gc.get(GregorianCalendar.MINUTE)) +
  					"00Z\r\n");
  		}

  		// write RRULE
  		d.setTime(t.get_begin_UTC_ms());
  		gc.setTime(d);
  		int period = t.get_period();
  		if (period != HoopCalendarEvent.None) 
  		{
  			String s = "";
  			if (period == HoopCalendarEvent.Yearly) 
  			{
  				s = "YM" + Integer.toString(t.get_period_multiplier()) + " " + Integer.toString(gc.get(GregorianCalendar.MONTH) + 1);
  			}
  			else if (period == HoopCalendarEvent.Monthly) 
  			{
  				s = "MD" + Integer.toString(t.get_period_multiplier()) + " " + Integer.toString(gc.get(GregorianCalendar.DAY_OF_MONTH) + 1);
  			}
  			else if (period == HoopCalendarEvent.Weekly) 
  			{
  				s = "W" + Integer.toString(t.get_period_multiplier());
  			}
  			else if (period == HoopCalendarEvent.Daily) 
  			{
  				s = "D" + Integer.toString(t.get_period_multiplier());
  			}
  			
  			int num = t.get_number_of_periods();
  			if (num == 999) 
  			{
  				num = 0; // 999 is internally used for unlimited
  			}
  			s = s + " #" + Integer.toString(num);
  			bw.write("RRULE:" + s + "\r\n");
  		}

  		// write UID
  		String uid = "UID:" + t.get_UID();
  		int i = 65;
  		// fold line
  		while (i < uid.length()) 
  		{
  			uid = uid.substring(0, i) + "\r\n " + uid.substring(i);
  			i = i + 65;
  		}
  		
  		bw.write(uid + "\r\n");

  		// write LAST-MODIFIED
  		d.setTime(t.get_last_mod_UTC_ms());
  		gc.setTime(d);
  		bw.write("LAST-MODIFIED:" + formatter4.format(gc.get(GregorianCalendar.YEAR)) +
  				formatter.format(gc.get(GregorianCalendar.MONTH) + 1) +
  				formatter.format(gc.get(GregorianCalendar.DAY_OF_MONTH)) + "T" +
  				formatter.format(gc.get(GregorianCalendar.HOUR_OF_DAY)) +
  				formatter.format(gc.get(GregorianCalendar.MINUTE)) +
  				formatter.format(gc.get(GregorianCalendar.SECOND)) + "Z\r\n");

  		// write CLASS
  		String class_string = "PUBLIC";
  		if (t.get_vcal_class() == HoopCalendarEvent.Private) 
  		{
  			class_string = "PRIVATE";
  		}
  		
  		bw.write("CLASS:" + class_string + "\r\n");

  		// write SUMMARY
  		bw.write("SUMMARY;CHARSET="
  				+ t.get_summary_encoding()
  				+ ";ENCODING=QUOTED-PRINTABLE:"
  				+ Converter.unicode2quodedPrintable(t.get_summary(), t.get_summary_encoding())
  				+ "\r\n");

  		// write DESCRIPTION
  		String tmp = t.get_description();
  		if (tmp.length() > 0) 
  		{
  			bw.write("DESCRIPTION;CHARSET="
  					+ t.get_description_encoding()
  					+ ";ENCODING=QUOTED-PRINTABLE:"
  					+ Converter.unicode2quodedPrintable(tmp, t.get_description_encoding())
  					+ "\r\n");
  		}

  		// write X-DTLK-COLOUR, X-DTLK-GPOUP and X-DTLK-ALARMCOUNTER
  		formatter.setMinimumIntegerDigits(3);
  		bw.write("X-DTLK-COLOUR:" + formatter.format(t.get_renderer_color().getRed())
  				+ formatter.format(t.get_renderer_color().getGreen())
  				+ formatter.format(t.get_renderer_color().getBlue())
  				+ "\r\n");
  		bw.write("X-DTLK-GROUP:" + Integer.toString(t.get_renderer_group()) + "\r\n");
  		bw.write("X-DTLK-ALARMCOUNTER:" + Integer.toString(t.get_alarm_counter()) + "\r\n");
  		bw.write("END:VEVENT" + "\r\n");
  	}

  	/**
  	 *  Read bytes from FileInputStream and extends each byte to a character imply by adding 0x00 at MSB.
  	 */
  	public static class MyInputStreamReader extends InputStreamReader 
  	{
  		InputStream my_input_stream;

  		/**
  		 *  Constructor for the MyInputStreamReader object
  		 *
  		 * @param  in  Description of the Parameter
  		 */
  		public MyInputStreamReader(InputStream in) 
  		{
  			super(in);
  			my_input_stream = in;
  		}

  		/**
  		 *  Read a character from predefined input stream.
  		 *
  		 * @return    character
  		 */
  		public int read() 
  		{
  			try 
  			{
  				return (char) my_input_stream.read();
  			}
  			catch (Exception e) 
  			{
  				e.printStackTrace();
  				return -1;
  			}
  		}

  		/**
  		 *  Read characters from my_input_stream and write the character to a character buffer<br>
  		 *  to a given offset.
  		 *
  		 * @param  cbuf    character buffer
  		 * @param  offset  offset in cbuf where read characters is written to
  		 * @param  length  number of characters to be read
  		 * @return         number of read characters
  		 */
  		public int read(char[] cbuf, int offset, int length) 
  		{
  			byte[] my_byte_array = new byte[length];
  			int retVal;

  			try 
  			{
  				retVal = my_input_stream.read(my_byte_array, 0, length);
  			}
  			catch (Exception e) 
  			{
  				e.printStackTrace();
  				return -1;
  			}

  			for (int k = 0; k < retVal; k++) 
  			{
  				cbuf[k + offset] = (char) my_byte_array[k];
  			}

  			return retVal;
  		}
  	}
}

