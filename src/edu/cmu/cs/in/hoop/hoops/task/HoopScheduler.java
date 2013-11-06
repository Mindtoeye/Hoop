/** 
 * Author: Martin van Velsen <vvelsen@cs.cmu.edu>
 * 
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
 */

package edu.cmu.cs.in.hoop.hoops.task;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.GregorianCalendar;
import java.util.Timer;

import javax.swing.JButton;
import javax.swing.JPanel;

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.hoop.hoops.base.HoopControlBase;
import edu.cmu.cs.in.hoop.hoops.base.HoopInterface;
import edu.cmu.cs.in.hoop.hoops.task.HoopCalendarControls.HoopAlarmHandler;
import edu.cmu.cs.in.hoop.hoops.task.HoopCalendarControls.HoopDateLookPanel;
import edu.cmu.cs.in.hoop.hoops.task.HoopCalendarControls.EventMemory;
import edu.cmu.cs.in.hoop.properties.HoopVisualProperties;

/**
* 
*/
public class HoopScheduler extends HoopControlBase implements HoopInterface, ActionListener
{    					
	private static final long serialVersionUID = -6588901457817911066L;
	
	private HoopDateLookPanel date_look_panel = null;	
	private EventMemory event_memory =null;
	private HoopAlarmHandler alarm_handler =null;
	private Timer alarm_checker = null;	
	
	private   JPanel editorPanel=null;
	private   JButton editorButton=null;	
		
	/**
	 *
	 */
    public HoopScheduler () 
    {
		setClassName ("HoopScheduler");
		debug ("HoopScheduler ()");

		long timer_rate = 5 * 1000;   // check every 5 second
		
		date_look_panel = new HoopDateLookPanel();	
		event_memory = new EventMemory(date_look_panel);
		
		alarm_handler = new HoopAlarmHandler(event_memory);

		alarm_checker = new Timer();
		alarm_checker.scheduleAtFixedRate (alarm_handler,
										   (long) (timer_rate - Math.IEEEremainder((new GregorianCalendar()).getTime().getTime(),timer_rate)),
										   timer_rate);	   		
										
		setHoopDescription ("Schedules or Hoops via a calendar");	
		
    	editorPanel=new JPanel ();    	
    	editorPanel.setLayout (new BorderLayout(2,2));
    	
		editorPanel.setPreferredSize(new Dimension (150,25)); 
		
    	editorButton=new JButton ();
    	editorButton.setText("Edit Patterns");
    	editorButton.addActionListener(this);
    	
    	editorPanel.add (editorButton,BorderLayout.CENTER);
    	
    	HoopVisualProperties vizProps=this.getVisualProperties();
    	
    	vizProps.preferredPanelHeight=50;		
    }
    /**
     * 
     */
    public void reset ()
    {
    	
    }
	/**
	 *
	 */
	public Boolean runHoop (HoopBase inHoop)
	{		
		debug ("runHoop ()");
		
		super.runHoop(inHoop); // This will do the right thing with the data

		if (HoopLink.project.getVirginFile()==true)
		{
			alert ("Error: please save your project first before running the scheduler");
			return (true);
		}		
		
		event_memory.read_data_file();
		
		this.setDone(false);
		
		return (true);
	}
	/**
	 * 
	 */
	@Override
	public JPanel getPropertiesPanel() 
	{
		debug ("getPropertiesPanel ()");
		
		return (editorPanel);		
	}			
	/**
	 * 
	 */
	/*
	@Override
	public JPanel getPropertiesPanel() 
	{
		if (HoopLink.project.getVirginFile()==true)
		{
			alert ("Error: please save your project first before opening the scheduler");
			return (null);
		}
				
		event_memory.read_data_file();
		date_look_panel.set_event_memory(event_memory);		
		
		return (date_look_panel);
	}*/	
	/**
	 * 
	 */
	public HoopBase copy ()
	{
		return (new HoopScheduler ());
	}
	/**
	 * 
	 */
	@Override
	public void actionPerformed(ActionEvent e)
	{
		debug ("actionPerformed ()");
		
		if (HoopLink.project.getVirginFile()==true)
		{
			alert ("Error: please save your project first before opening the scheduler");
			return;
		}		
		
		/*
		if (editor==null)
		{
			editor=new HoopPatternEditor (HoopLink.mainFrame,true);
			editor.resizeAndCenter(450,400);		
			editor.setVisible(true);
			
			debug ("Cleaning up editor ...");
			
			editor=null;
		}
		*/
	}	
}
