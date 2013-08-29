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

package edu.cmu.cs.in.hoop.execute;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.hoop.HoopDocumentList;
import edu.cmu.cs.in.hoop.HoopErrorPanel;
import edu.cmu.cs.in.hoop.HoopProjectPanel;
import edu.cmu.cs.in.hoop.HoopStatisticsPanel;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.stats.HoopSampleDataSet;

/** 
 * 
 */
public class HoopExecuteInEditor extends HoopExecute implements ActionListener
{		
	public static int SPEED_SLOW=0;
	public static int SPEED_NORMAL=1;
	public static int SPEED_FAST=2;
	
	private int executeSpeed=SPEED_SLOW;
	
	/**
	 *
	 */
	public HoopExecuteInEditor () 
	{
		setClassName ("HoopExecuteInEditor");
		debug ("HoopExecuteInEditor ()");		
		
		this.setInEditor(true);
	}	
	/**
	 * 
	 */
	protected void showError (String aClass,String anError)
	{
		debug ("showError ()");
		
		debug ("Hoop: " + aClass + ", Error: " + anError);
		
		if (HoopLink.executionMonitor!=null)
		{	
			HoopLink.executionMonitor.stop ();
		}
		
		HoopErrorPanel errorPanel=(HoopErrorPanel) HoopLink.getWindow("Errors");
		
		if (errorPanel==null)
		{
			debug ("No error panel yet, creating ...");
			
			HoopLink.addView ("Errors",new HoopErrorPanel(),"bottom");
			errorPanel=(HoopErrorPanel) HoopLink.getWindow("Errors");
		}	
				
		HoopLink.popWindow("Errors");
		
		errorPanel.addError (aClass,anError);		
	}	
	/**
	 * 
	 */
	protected void updateDependencies ()
	{
		debug ("updateDependencies ()");
		
		HoopProjectPanel projectPane=(HoopProjectPanel) HoopLink.getWindow("Project");
       	if (projectPane!=null)
       	{
       		//projectPane.refresh();
       	}		
       	
		HoopDocumentList docList=(HoopDocumentList) HoopLink.getWindow("Document List");
		
		if (docList!=null)
			docList.updateContents();
	}	
	/**
	 * 
	 */
	protected void startExecution ()
	{
		debug ("startExecution ()");
		
		HoopStatisticsPanel statsPanel=(HoopStatisticsPanel) HoopLink.getWindow("Statistics");
		
		if (statsPanel!=null)
		{
			statsPanel.clear();
		}
				
		/*
		HoopStatisticsPanel statsPanel;
		if(HoopLink.getWindow("Statistics")!=null){
			statsPanel=(HoopStatisticsPanel) HoopLink.getWindow("Statistics");
		}else{
			statsPanel=new HoopStatisticsPanel ();
		}
		statsPanel.clear();
		HoopLink.addView ("Statistics",statsPanel,HoopLink.bottom);
    					
		HoopExecuteProgressPanel executionMonitor=(HoopExecuteProgressPanel) HoopLink.getWindow("Execution Monitor");
		if (executionMonitor==null)
		{
			executionMonitor=new HoopExecuteProgressPanel ();
			HoopLink.addView ("Execution Monitor",executionMonitor,HoopLink.bottom);
		}
		*/		
		
		HoopLink.runner=this;
	}	
	/**
	 * 
	 */
	protected void endExecution ()
	{
		debug ("endExecution ()");
		
		HoopExecuteProgressPanel executionMonitor=(HoopExecuteProgressPanel) HoopLink.getWindow("Execution Monitor");
		if (executionMonitor!=null)
		{
			executionMonitor.stop ();
		}

		HoopStatisticsPanel statsPanel;
		if(HoopLink.getWindow("Statistics")!=null){
			statsPanel=(HoopStatisticsPanel) HoopLink.getWindow("Statistics");
		}else{
			statsPanel=new HoopStatisticsPanel ();
		}
		HoopLink.addView ("Statistics",statsPanel,HoopLink.bottom);
    	statsPanel.appendString("\n"+"-------------------------------------------------------------------------------------------------------------------");
    	
    	HoopSampleDataSet sds = new HoopSampleDataSet("HOOPS");
    	sds.setDataSet(HoopLink.timeTakenByHoops);
    	
    	statsPanel.setData(sds);    			
	}		
	/**
	 * 
	 */
	@Override
	public void actionPerformed(ActionEvent event) 
	{		
		debug ("actionPerformed ("+event.getActionCommand()+")");
		
		String act=event.getActionCommand();
		
		if (act.equalsIgnoreCase("stop")==true)
		{
			JButton button = (JButton)event.getSource();
			stopExecution ();
			button.setEnabled(false);
		}
		
		if (act.equalsIgnoreCase("run")==true)
		{
			setRoot(HoopLink.hoopGraphManager.getRoot());
			setLoopCount(1);
			
			Thread runner=new Thread (this);
			runner.setUncaughtExceptionHandler(new HoopExecuteExceptionHandler ());    			
			runner.start();
		}
		
		/*
		if (act.equalsIgnoreCase("runN")==true)
		{
			setRoot(HoopLink.hoopGraphManager.getRoot());
			setLoopCount(10);
			
			Thread runner=new Thread (this);
			runner.setUncaughtExceptionHandler(new HoopExecuteExceptionHandler ());    			
			runner.start();
		}
		
		if (act.equalsIgnoreCase("runForever")==true)
		{
			setRoot(HoopLink.hoopGraphManager.getRoot());
			setLoopCount(-1);
			
			Thread runner=new Thread (this);
			runner.setUncaughtExceptionHandler(new HoopExecuteExceptionHandler ());    			
			runner.start();
		}
		*/				
	}	
	/**
	 * 
	 */
	protected void startHoopExecution (HoopBase aHoop)
	{
		debug ("startHoopExecution ()");
		
		if (HoopLink.executionMonitor!=null)
		{
			HoopLink.executionMonitor.updateHoopStartExecution(aHoop);
		}
	}	
	/**
	 * 
	 */
	protected void endHoopExecution (HoopBase aHoop)
	{
		debug ("endHoopExecution ()");
		
		if (HoopLink.executionMonitor!=null)
		{
			HoopLink.executionMonitor.updateHoopEndExecution(aHoop);
		}
	}
	/**
	 * 
	 * @return
	 */
	public int getExecuteSpeed() 
	{
		return executeSpeed;
	}
	/**
	 * 
	 * @param executeSpeed
	 */
	public void setExecuteSpeed(int executeSpeed) 
	{
		debug ("setExecuteSped ("+executeSpeed+")");
		
		this.executeSpeed = executeSpeed;
	}		
}
