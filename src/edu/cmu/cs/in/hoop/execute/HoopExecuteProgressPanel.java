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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.controls.base.HoopEmbeddedJPanel;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.stats.HoopPerformanceMetrics;

/** 
 *
 */
public class HoopExecuteProgressPanel extends HoopEmbeddedJPanel implements HoopExecutionMonitor, ActionListener
{	
	private static final long serialVersionUID = -9132114294178560223L;
	private JList executionTrace=null;	
	private DefaultListModel model=null;
	
    private JRadioButton showLinear = null;   
    private JRadioButton showStaggered = null;
    
    private JRadioButton showAverage = null;   
    private JRadioButton showLatest = null;    
    
    private JLabel timeIndicator=null;    
    private Timer displayTimer=null;
    private Long timeCounter=(long) 0;
	
	/**
	 * 
	 */	
	public HoopExecuteProgressPanel()
	{
		setClassName ("HoopExecuteProgressPanel");
		debug ("HoopExecuteProgressPanel ()");
		
		Box mainBox = new Box (BoxLayout.Y_AXIS);
		
		// Create controls first
		
		Box controlBox = new Box (BoxLayout.X_AXIS);			
		controlBox.setMinimumSize(new Dimension (100,25));
		controlBox.setPreferredSize(new Dimension (100,25));
		
	    showLinear = new JRadioButton();
	    showLinear.setText("Show Linear");
	    //showLinear.setIcon(HoopLink.getImageByName("data.gif"));
	    showLinear.setSelected(true);
	    showLinear.setFont(new Font("Dialog", 1, 10));
	    showLinear.addActionListener(this);
	    
	    showStaggered = new JRadioButton();
	    showStaggered.setText("Show Staggered");
	    //showStaggered.setIcon(HoopLink.getImageByName("delete.png"));
	    showStaggered.setFont(new Font("Dialog", 1, 10));
	    showStaggered.addActionListener(this);
	    
	    //Group the radio buttons.
	    ButtonGroup group=new ButtonGroup();
	    group.add (showLinear);
	    group.add (showStaggered);
	    
	    controlBox.add(showLinear);
	    controlBox.add(showStaggered);	
	    
	    controlBox.add(new JSeparator(SwingConstants.VERTICAL));
	    
	    showAverage = new JRadioButton();
	    showAverage.setText("Show Linear");
	    //showAverage.setIcon(HoopLink.getImageByName("data.gif"));
	    showAverage.setSelected(true);
	    showAverage.setFont(new Font("Dialog", 1, 10));
	    showAverage.addActionListener(this);
	    
	    showLatest = new JRadioButton();
	    showLatest.setText("Show Staggered");
	    //showLatest.setIcon(HoopLink.getImageByName("delete.png"));
	    showLatest.setFont(new Font("Dialog", 1, 10));
	    showLatest.addActionListener(this);
	    
	    //Group the radio buttons.
	    ButtonGroup group2=new ButtonGroup();
	    group2.add (showAverage);
	    group2.add (showLatest);	    
		
	    controlBox.add(showAverage);
	    controlBox.add(showLatest);	
	    
	    controlBox.add(new JSeparator(SwingConstants.VERTICAL));
	    
		timeIndicator=new JLabel ();
		timeIndicator.setText("00:00:00");
		timeIndicator.setFont(new Font("Dialog", 1, 10));
		timeIndicator.setMinimumSize(new Dimension (75,23));
		timeIndicator.setPreferredSize(new Dimension (75,23));
		timeIndicator.setMaximumSize(new Dimension (75,23));
	    
		controlBox.add(timeIndicator);
		
		// Add the actual progress controls ...
								
		ListCellRenderer renderer = new HoopExecutionListRenderer ();
		
		model = new DefaultListModel();
		
		executionTrace=new JList (model);
		executionTrace.setOpaque(true);
		executionTrace.setBackground(new Color (220,220,220));
		executionTrace.setCellRenderer(renderer);
		
		JScrollPane traceContainer=new JScrollPane (executionTrace);
		traceContainer.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		// Wrap it all up ...
		
		mainBox.add(controlBox);
		mainBox.add(traceContainer);
		
		this.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
		
		setContentPane (mainBox);
		
		HoopLink.executionMonitor=this;
		
		displayTimer = new Timer(1000,this);
		//displayTimer.setinitialDelay(0);
	}
	/**
	 * 
	 */
	@Override
	public void reset() 
	{
		debug ("reset ()");
	
		model = new DefaultListModel();
		
		executionTrace.setModel(model);
				
		displayTimer.stop();
		timeCounter=(long) 0;
		timeIndicator.setText("00:00:00");
	}
	/**
	 * 
	 */
	@Override
	public void start() 
	{
		debug ("start ()");
		
		displayTimer.start();
	}
	/**
	 * 
	 */
	@Override
	public void stop() 
	{
		debug ("stop ()");
		
		displayTimer.stop();
	}	
	/**
	 * 
	 */
	public void handleCloseEvent ()
	{
		debug ("handleCloseEvent ()");
		
		HoopLink.executionMonitor=null;
	}
	/**
	 * 
	 */
	@Override
	public void updateHoopStartExecution (HoopBase aHoop) 
	{
		debug ("updateHoopStartExecution ()");
		
		int elementIndex=model.indexOf(aHoop);
		
		if (elementIndex==-1)
		{
			int pos = executionTrace.getModel().getSize();
			model.add (pos,aHoop);
		}
		else
		{
			executionTrace.repaint(executionTrace.getCellBounds(elementIndex, elementIndex));
		}
		
		updateDependencyProgress ();
	}
	/**
	 * 
	 */
	@Override
	public void updateHoopEndExecution (HoopBase aHoop) 
	{
		debug ("updateHoopEndExecution ()");
		
		calcVisualStats ();
		
		int elementIndex=model.indexOf(aHoop);
		
		if (elementIndex==-1)
		{
			int pos = executionTrace.getModel().getSize();
			model.add (pos,aHoop);
		}
		else
		{
			executionTrace.repaint(executionTrace.getCellBounds(elementIndex, elementIndex));
		}
		
		updateDependencyProgress ();
	}
	/**
	 * 
	 */
	private void calcVisualStats ()
	{
		debug ("calcVisualStats ()");
		
		if (model==null)
			return;
		
		HoopExecutionListRenderer.maxMs=(long) 1;
		
		Long totalMeasure=(long) 0;
		
		HoopExecutionListRenderer.totalCount=model.size();
		
		for (int t=0;t<model.size();t++)
		{
			HoopBase aHoop=(HoopBase) model.get(t);
			
			HoopPerformanceMetrics metrics=aHoop.getPerformanceMetrics();
			
			Long aMeasure=metrics.getYValue();
			
			if (aMeasure==0)
				aMeasure=(long) 1;
			
			aHoop.duration=aMeasure;
			
			totalMeasure+=aMeasure;
			
			if (aMeasure>HoopExecutionListRenderer.maxMs)
				HoopExecutionListRenderer.maxMs=aMeasure;				
		}
		
		debug ("Max time: " + HoopExecutionListRenderer.maxMs + "ms, total: " + totalMeasure + ", for pixel count: " + HoopExecutionListRenderer.totalWidth + ", with " + HoopExecutionListRenderer.totalCount + " hoops");
		
		// Calculate transforms ...
		
		float divver=totalMeasure/HoopExecutionListRenderer.totalWidth;
		
		debug ("divver: " + divver);
		
		// Update all the visual settings ...
		
		int offset=0;
		
		for (int i=0;i<model.size();i++)
		{
			HoopBase aHoop=(HoopBase) model.get(i);
			
			float mult=totalMeasure/aHoop.duration;
			
			debug ("mult: " + mult);
			
			aHoop.durationOffset=offset;
			aHoop.durationWidth=(int) (divver*mult);
			
			debug ("Offset: " + offset + ", width: " + aHoop.durationWidth);
			
			offset+=aHoop.durationWidth;
		}		
	}	
	/**
	 * 
	 */
	private void updateDependencyProgress ()
	{
		debug ("updateDependencyProgress ()");	
	}
	/**
	 * 
	 */
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		debug ("actionPerformed ()");
	
		if (e.getSource()==displayTimer)
		{
			timeCounter++;
						
			timeIndicator.setText(String.format("%d:%d:%d", 
												TimeUnit.MILLISECONDS.toHours(timeCounter),
												TimeUnit.MILLISECONDS.toMinutes(timeCounter),
												TimeUnit.MILLISECONDS.toSeconds(timeCounter)));
			
			calcVisualStats ();
		}
	}
}
