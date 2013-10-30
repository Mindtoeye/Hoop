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

import java.net.*;
import java.io.*;
import javax.swing.*;

/**
 *  Description of the Class
 */
public class Synchronizer extends Thread {

  private EventMemory event_memory;
  private SyncDialog sync_dialog;
  private JProgressBar progress_bar;
  private int protocol = SyncSettings.protNone;
  private String user_name = "";
  private String password = "";
  private String sync_host_path_name = "";


  /**
   *  Constructor for the Synchronizer object
   *
   * @param  tm  Description of the Parameter
   * @param  sd  Description of the Parameter
   * @param  p   Description of the Parameter
   * @param  u   Description of the Parameter
   * @param  pw  Description of the Parameter
   * @param  hp  Description of the Parameter
   * @param  pb  Description of the Parameter
   */
  public Synchronizer(EventMemory tm, SyncDialog sd, int p, String u, String pw, String hp, JProgressBar pb) {
    event_memory = tm;
    sync_dialog = sd;
    protocol = p;
    user_name = u;
    password = pw;
    sync_host_path_name = hp;
    progress_bar = pb;
  }


  /**
   *  Main processing method for the Synchronizer object
   */
  public void run() {
    InputStreamReader in = null;
    OutputStreamWriter out = null;
    URL url = null;
    File net_file = null;
    long in_length = 0;

    // reset progress bar
    progress_bar.setValue(0);
    progress_bar.setString("connecting!");
    progress_bar.setStringPainted(true);

    if (sync_host_path_name.length() > 0) {
      try {
        try {
          // read and synchronise database
          if (protocol == SyncSettings.protFTP) {
            url = new URL("ftp://" + user_name + ":" + password + "@" + sync_host_path_name);
            URLConnection connection = url.openConnection();
            in = new InputStreamReader(connection.getInputStream());
            in_length = connection.getContentLength();
          }
          else {
            //protocol == SyncSettings.protFile
            net_file = new File(sync_host_path_name);
            in = new InputStreamReader(new FileInputStream(net_file), "US-ASCII");
            in_length = net_file.length();
          }
          progress_bar.setString("synchronising!");
          event_memory.import_vCalendar(in, Math.max(in_length, 1), progress_bar);
          in.close();
        }
        catch (Exception x) {
          // error during read/synchronise
          progress_bar.setString(x.getMessage());
        }

        // write updated database back
        progress_bar.setValue(0);
        progress_bar.setString("connecting!");
        if (protocol == SyncSettings.protFTP) {
          URLConnection connection = url.openConnection();
          connection.setDoOutput(true);
          out = new OutputStreamWriter(connection.getOutputStream(), "US-ASCII");
        }
        else if (protocol == SyncSettings.protFile) {
          out = new OutputStreamWriter(new FileOutputStream(net_file), "US-ASCII");
        }
        progress_bar.setString("writing!");
        int[] i = new int[event_memory.get_size()];
        for (int k = 0; k < i.length; k++) {
          i[k] = k;
        }
        // array for all events built
        progress_bar.setStringPainted(true);
        event_memory.export_vCalendar(out, i, progress_bar, true);
        out.close();
        sync_dialog.sync_panel.unlock_input();
        sync_dialog.dispose();
      }
      catch (Exception e) {
        progress_bar.setString(e.getMessage());
        sync_dialog.sync_panel.unlock_input();
      }
    }
    else {
      progress_bar.setString("enter valid URL!");
      sync_dialog.sync_panel.unlock_input();
    }
  }
}

