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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.hoop.execute.HoopExecute;
import edu.cmu.cs.in.hoop.execute.HoopExecuteExceptionHandler;
import edu.cmu.cs.in.hoop.execute.HoopExecuteProgressPanel;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.hoop.visualizers.HoopCluster;

public class HoopActionListener extends HoopBase implements ActionListener
{
	private static final long serialVersionUID = 8950962537196303523L;
	
	/**
	 * 
	 */
	public HoopActionListener (String aCommand)
	{
		this.setInstanceName(aCommand);
	}
	/**
	 * 
	 */
	@Override
	public void actionPerformed (ActionEvent action) 
	{
		debug ("actionPerformed ()");
		
		//>--------------------------------------------------------------------
		
		if (this.getInstanceName().equalsIgnoreCase("run")==true)
		{
			HoopExecuteProgressPanel executionMonitor=(HoopExecuteProgressPanel) HoopLink.getWindow("Execution Monitor");
		
			if (executionMonitor==null)
			{
				executionMonitor=new HoopExecuteProgressPanel ();
				HoopLink.addView ("Execution Monitor",executionMonitor,HoopLink.bottom);
			}
		
			HoopLink.popWindow("Execution Monitor");
		
			HoopLink.runner.setRoot(HoopLink.hoopGraphManager.getRoot());
			HoopLink.runner.setLoopCount(1);
			HoopLink.runner.setLocation (HoopExecute.LOCAL);
		    			
			Thread runner=new Thread (HoopLink.runner);
			runner.setUncaughtExceptionHandler(new HoopExecuteExceptionHandler ());    			
			runner.start();
		}
		
		//>--------------------------------------------------------------------
		
		if (this.getInstanceName().equalsIgnoreCase("runcluster")==true)
		{
			HoopExecuteProgressPanel executionMonitor=(HoopExecuteProgressPanel) HoopLink.getWindow("Execution Monitor");
			if (executionMonitor==null)
			{
				executionMonitor=new HoopExecuteProgressPanel ();
				HoopLink.addView ("Execution Monitor",executionMonitor,HoopLink.bottom);
			}
			
			HoopLink.popWindow("Execution Monitor");
			
			HoopCluster clusterMonitor=(HoopCluster) HoopLink.getWindow("Cluster Monitor");
			if (clusterMonitor==null)
			{
				clusterMonitor=new HoopCluster ();
				HoopLink.addView ("Cluster Monitor",clusterMonitor,HoopLink.center);
			}
			
			HoopLink.popWindow("Cluster Monitor");
			
			HoopLink.runner.setRoot(HoopLink.hoopGraphManager.getRoot());
			HoopLink.runner.setLoopCount(1);
			HoopLink.runner.setLocation (HoopExecute.CLUSTER);
			    			
			Thread runner=new Thread (HoopLink.runner);
			runner.setUncaughtExceptionHandler(new HoopExecuteExceptionHandler ());    			
			runner.start();			
		}
		
		//>--------------------------------------------------------------------
		
		if (this.getInstanceName().equalsIgnoreCase("debug")==true)
		{
			
		}
		
		//>--------------------------------------------------------------------
		
		if (this.getInstanceName().equalsIgnoreCase("stop")==true)
		{
			HoopLink.runner.kill();
		}
	}
}
