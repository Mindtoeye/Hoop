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

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.controls.base.HoopEmbeddedJPanel;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.stats.HoopPerformanceMetrics;

/** 
 *
 */
public class HoopExecuteProgressPanel extends HoopEmbeddedJPanel implements HoopExecutionMonitor
{	
	private static final long serialVersionUID = -9132114294178560223L;	
	JList executionTrace=null;	
	DefaultListModel model=null;
	
	/**
	 * 
	 */	
	public HoopExecuteProgressPanel()
	{
		setClassName ("HoopExecuteProgressPanel");
		debug ("HoopExecuteProgressPanel ()");
		
		Box mainBox = new Box (BoxLayout.Y_AXIS);
								
		ListCellRenderer renderer = new HoopExecutionListRenderer ();
		
		model = new DefaultListModel();
		
		executionTrace=new JList (model);
		executionTrace.setOpaque(true);
		executionTrace.setBackground(new Color (220,220,220));
		executionTrace.setCellRenderer(renderer);
		
		JScrollPane traceContainer=new JScrollPane (executionTrace);
		traceContainer.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		mainBox.add(traceContainer);
		
		this.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
		
		setContentPane (mainBox);
		
		HoopLink.executionMonitor=this;
	}
	@Override
	public void reset() 
	{
		debug ("reset ()");
	
		model = new DefaultListModel();
		
		executionTrace.setModel(model);
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
		debug ("updateDependencyProgress ()");
		
		if (model==null)
			return;
		
		HoopExecutionListRenderer.maxMs=1;
		
		for (int i=0;i<model.size();i++)
		{
			HoopBase aHoop=(HoopBase) model.get(i);
			
			HoopPerformanceMetrics metrics=aHoop.getPerformanceMetrics();
			
			if (metrics.getMarkerRaw()>HoopExecutionListRenderer.maxMs)
			{
				HoopExecutionListRenderer.maxMs=metrics.getMarkerRaw();
			}
		}
		
		debug ("Max time: " + HoopExecutionListRenderer.maxMs + "ms");		
	}	
	/**
	 * 
	 */
	private void updateDependencyProgress ()
	{
		debug ("updateDependencyProgress ()");	
	}
}
