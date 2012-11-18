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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.concurrent.TimeUnit;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JSeparator;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;

import edu.cmu.cs.in.controls.HoopProgressPainter;
import edu.cmu.cs.in.controls.base.HoopJPanel;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.stats.HoopPerformanceMetrics;

/**
 * 
 */
public class HoopExecutionListRenderer extends HoopJPanel implements ListCellRenderer 
{
	private static final long serialVersionUID = -2950497524627822787L;
	
	public static int MODEDEFAULT=0;
	public static int MODESTAGGERED=1;
	
	public static int mode=MODEDEFAULT;
	
	public static int TIMEDEFAULT=0;
	public static int TIMEAVERAGE=1;
	
	public static int modeTime=TIMEAVERAGE;
		  
	private JLabel textInfo=null;
	private JLabel stateInfo=null;
	private JLabel cycleIndicator=null;
	private JLabel timeIndicator=null;
	private HoopProgressPainter progressIndicator=null;
		
	private int maxHeight=18;
	
	public static Long maxMs=(long) 1;
	public static Long totalMs=(long) 1;
	public static int totalWidth=-1;
	public static int totalCount=1;
		
	/**
	 * 
	 */	
	public HoopExecutionListRenderer()
	{
		setClassName ("HoopExecutionListRenderer");
		debug ("HoopExecutionListRenderer ()");
			  
		this.setBorder(BorderFactory.createRaisedBevelBorder());
		this.setLayout(new BoxLayout (this,BoxLayout.X_AXIS));
		
		textInfo=new JLabel ();
		textInfo.setFont(new Font("Dialog", 1, 10));
		textInfo.setMinimumSize(new Dimension (150,maxHeight));
		textInfo.setPreferredSize(new Dimension (150,maxHeight));
		textInfo.setMaximumSize(new Dimension (150,maxHeight));
		this.add(textInfo);
			
		JSeparator sep1=new JSeparator(SwingConstants.VERTICAL);
		sep1.setMinimumSize(new Dimension (5,maxHeight));
		sep1.setPreferredSize(new Dimension (5,maxHeight));
		sep1.setMaximumSize(new Dimension (5,maxHeight));
		this.add(sep1);
			
		stateInfo=new JLabel ();
		stateInfo.setFont(new Font("Dialog", 1, 10));
		stateInfo.setMinimumSize(new Dimension (50,maxHeight));
		stateInfo.setPreferredSize(new Dimension (50,maxHeight));
		stateInfo.setMaximumSize(new Dimension (50,maxHeight));
		this.add(stateInfo);
			
		JSeparator sep2=new JSeparator(SwingConstants.VERTICAL);
		sep2.setMinimumSize(new Dimension (5,maxHeight));
		sep2.setPreferredSize(new Dimension (5,maxHeight));
		sep2.setMaximumSize(new Dimension (5,maxHeight));
		this.add(sep2);
			
		cycleIndicator=new JLabel ();
		cycleIndicator.setFont(new Font("Dialog", 1, 10));
		cycleIndicator.setMinimumSize(new Dimension (40,maxHeight));
		cycleIndicator.setPreferredSize(new Dimension (40,maxHeight));
		cycleIndicator.setMaximumSize(new Dimension (40,maxHeight));
		this.add (cycleIndicator);
		
		JSeparator sep3=new JSeparator(SwingConstants.VERTICAL);
		sep3.setMinimumSize(new Dimension (5,maxHeight));
		sep3.setPreferredSize(new Dimension (5,maxHeight));
		sep3.setMaximumSize(new Dimension (5,maxHeight));
		this.add(sep3);
		
		timeIndicator=new JLabel ();
		timeIndicator.setFont(new Font("Dialog", 1, 10));
		timeIndicator.setMinimumSize(new Dimension (75,maxHeight));
		timeIndicator.setPreferredSize(new Dimension (75,maxHeight));
		timeIndicator.setMaximumSize(new Dimension (75,maxHeight));
		this.add (timeIndicator);		
			
		JSeparator sep4=new JSeparator(SwingConstants.VERTICAL);
		sep4.setMinimumSize(new Dimension (5,maxHeight));
		sep4.setPreferredSize(new Dimension (5,maxHeight));
		sep4.setMaximumSize(new Dimension (5,maxHeight));
		this.add(sep4);
			
		progressIndicator=new HoopProgressPainter ();			
		progressIndicator.setFont(new Font("Dialog", 1, 10));
		this.add (progressIndicator);			
	}
	/**
	 * 
	 */
	public Component getListCellRendererComponent (JList list, 
		  										   Object value,
		  										   int index,
		  										   boolean isSelected, 
		  										   boolean cellHasFocus) 
	{			  
		//>---------------------------------------------------------
		
		HoopExecutionListRenderer.totalWidth=progressIndicator.getWidth();
			  
		if (value instanceof HoopBase)
		{
			HoopBase aHoop=(HoopBase) value;
				  				  
			textInfo.setText(aHoop.getClassName());
				
			stateInfo.setText(aHoop.getExecutionState());
								  
			HoopPerformanceMetrics metrics=aHoop.getPerformanceMetrics();
				  
			if (metrics!=null)
			{				
				cycleIndicator.setText("Ex: "+aHoop.getExecutionCount());
			
				if (HoopExecutionListRenderer.modeTime==HoopExecutionListRenderer.TIMEAVERAGE)
				{
					timeIndicator.setText("~"+metrics.getAverage());
				}
				else
				{
					Long result=metrics.getYValue();
					timeIndicator.setText(formatDuration (result));
				}	
				
				//progressIndicator.setLevels(metrics.getYValue(),HoopExecutionListRenderer.maxMs);
				progressIndicator.setLevels(aHoop.durationOffset,aHoop.durationWidth);
			}
		}
			  
		//>---------------------------------------------------------			  
			  
		if (value instanceof String)
		{
			String aLabel=(String) value;
			textInfo.setText(aLabel);
		}
			  
		//>---------------------------------------------------------			  
		  
		return (this);
	}
	/**
	 * 
	 */
	private String formatDuration (Long aDuration)
	{
		if (aDuration>(1000*60))
		{
			return (String.format("%d min, %d sec", 
					TimeUnit.MILLISECONDS.toMinutes(aDuration),
					TimeUnit.MILLISECONDS.toSeconds(aDuration) - 
					TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(aDuration))));
		}		
		
		if (aDuration>1000)
		{
			return (String.format("%d sec",TimeUnit.MILLISECONDS.toSeconds(aDuration)));
		}
		
		return (aDuration+" ms");
	}
}
