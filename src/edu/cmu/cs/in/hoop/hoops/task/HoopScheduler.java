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

import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;

import edu.cmu.cs.in.base.HoopRoot;
import edu.cmu.cs.in.controls.HoopCircleCounter;
import edu.cmu.cs.in.controls.HoopPieChart;
import edu.cmu.cs.in.hoop.editor.HoopNodeRenderer;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.hoop.hoops.base.HoopControlBase;
import edu.cmu.cs.in.hoop.hoops.base.HoopInterface;

/**
* 
*/
public class HoopScheduler extends HoopControlBase implements HoopInterface
{    					
	private static final long serialVersionUID = 5343404003031040810L;
	
	private long cycles=-1; // forever
	private long cycleCount=0;
	
	private long duration=5000;
    private long resolution=500;
    
    private Boolean replaced=false;
	
    private HoopCircleCounter countdown=null;
    
	private class HoopTimerTask extends TimerTask 
	{
	    //private JLabel visualizer=null;
	    
	    private long tracking=0;
	    private long trackingDuration=0;
	    private long trackingResolution=0;
	    
	    /**
	     * 
	     */
	    public HoopTimerTask (long aDuration,long aResolution)
	    {
	    	//visualizer=aViz;
	    	
	    	trackingDuration=aDuration;
	    	trackingResolution=aResolution;
	    	
	    	updateVisuals ();
	    }
	 	
	    /**
	     * 
	     * @param aMessage
	     */
	    private void debug (String aMessage)
	    {
	    	HoopRoot.debug("HoopTimerTask",aMessage);
	    }
	    
	    /**
	     * 
	     */
	    public void run() 
	    {	        
	        if (tracking <= trackingDuration) 
	        {	            
	            updateVisuals ();
	        } 
	        else 
	        {
	            debug ("Stopping timer ...");

	            this.cancel();
	        }
	        
	    	tracking+=trackingResolution;
	    }
	    /**
	     * 
	     */
	    private void updateVisuals ()
	    {
	    	debug ("updateVisuals ()");
	    	
            if (countdown!=null)
            {
            	//Long formatter=(trackingDuration-tracking);
            	
            	countdown.setValues((int) trackingDuration,(int) (trackingDuration-tracking));
            	//visualizer.setText(formatter.toString());
            }
	    }
	}
	
	/**
	 *
	 */
    public HoopScheduler () 
    {
		setClassName ("HoopScheduler");
		debug ("HoopScheduler ()");
										
		setHoopDescription ("Schedules or times Hoops downstream");
    }
	/**
	 *
	 */
	public Boolean runHoop (HoopBase inHoop)
	{		
		debug ("runHoop ()");
		
		if (replaced==false)
		{
			if ((HoopNodeRenderer) this.getVisualizer()!=null)
			{
				HoopNodeRenderer renderer=(HoopNodeRenderer) this.getVisualizer();

				countdown=new HoopCircleCounter ();
			
				renderer.replaceContentArea(countdown);
			}
			
			replaced=true;
		}
		
        //1- Creating an instance of Timer class.
        Timer timer = new Timer ("Printer");
                
    	//JLabel aPanel=this.getVisualizer().getContentPanel ();
        
        //2- Creating an instance of class contains your repeated method.
        HoopTimerTask task = new HoopTimerTask(duration,resolution);
 
 
        // HoopTimerTask is a class implements Runnable interface so
        // You have to override run method with your certain code black
 
        // Second Parameter is the specified the Starting Time for your timer in
        // MilliSeconds or Date
 
        // Third Parameter is the specified the Period between consecutive
        // calling for the method.
 
        timer.schedule(task,0,resolution);		
		
		try 
		{
			Thread.sleep(duration);
		} 
		catch (InterruptedException e) 
		{
		
			e.printStackTrace();
			return (true);
		}

		timer.cancel();
		
		debug ("All done, returning control to scheduler ...");
		
		if (cycles==-1)
		{
			debug ("The Never Ending Story ...");
			
			this.setDone(false);
			return (true);
		}
		
		cycles++;
		
		if (cycles>=cycleCount)
		{
			this.setDone(true);
			return (true);
		}
		
		this.setDone(false);
		return (true);
	}	
	/**
	 * 
	 */
	public HoopBase copy ()
	{
		return (new HoopScheduler ());
	}	
}
