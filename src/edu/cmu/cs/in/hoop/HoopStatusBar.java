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

package edu.cmu.cs.in.hoop;

import java.awt.Dimension;
import java.awt.Font;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JTextField;

import edu.cmu.cs.in.controls.base.HoopJPanel;

/** 
 * @author vvelsen
 *
 */
public class HoopStatusBar extends HoopJPanel
{		
	class UpdateTimeTask extends TimerTask 
	{
		public void run() 
		{
			//System.out.println ("tick");
			update ();
		}
	}	
	
	private static final long serialVersionUID = -1L;
	
	private JTextField memory=null;
	private JTextField keys=null;
	private JTextField memo=null;
	
	/**
	 * 
	 */	
	public HoopStatusBar()
	{
		setClassName ("HoopStatusBar");
		debug ("HoopStatusBar ()");
		
		BoxLayout bLayout=new BoxLayout (this,BoxLayout.X_AXIS);
	
		setLayout(bLayout);
				
		memory=new JTextField ();
		memory.setEditable(false);
		memory.setFont(new Font("Dialog", 1, 10));
		memory.setMinimumSize(new Dimension (250,23));
		memory.setPreferredSize(new Dimension (250,23));
		memory.setMaximumSize(new Dimension (250,23));
		
		keys=new JTextField ();
		keys.setEditable(false);
		keys.setFont(new Font("Dialog", 1, 10));
		keys.setMinimumSize(new Dimension (100,23));
		keys.setPreferredSize(new Dimension (100,23));
		keys.setMaximumSize(new Dimension (100,23));
				
		memo=new JTextField ();
		memo.setEditable(false);
		memo.setFont(new Font("Dialog", 1, 10));
		memo.setMinimumSize(new Dimension (50,23));
		memo.setPreferredSize(new Dimension (100,23));
		memo.setMaximumSize(new Dimension (100,23));
		
		this.add(memory);
		this.add(keys);		
		this.add(Box.createHorizontalGlue());
		this.add(memo);
				
		update ();
		
		Timer timer=new Timer();
		timer.schedule(new UpdateTimeTask (),0,1000L);
	}
	/**
	 * 
	 */	
	public void update ()
	{
		StringBuffer formatted=new StringBuffer ();
		formatted.append ("Total: ");
		formatted.append (Long.toString(Runtime.getRuntime().totalMemory()/1024));
		formatted.append ("K Max: ");
		formatted.append (Long.toString(Runtime.getRuntime().maxMemory()/1024));
		formatted.append ("K Free: ");
		formatted.append (Long.toString(Runtime.getRuntime().freeMemory()/1024));
		formatted.append ("K");
		
		memory.setText(formatted.toString());
		
		keys.setText("N | C | S - I/O");		
	}
	/**
	 * 
	 */
	public void setStatus (String aMemo)
	{
		if (memo!=null)
			memo.setText(aMemo);
	}
}
