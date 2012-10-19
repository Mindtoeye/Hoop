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

//import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;

//import javax.swing.border.Border;

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.controls.base.HoopEmbeddedJPanel;
import edu.cmu.cs.in.controls.base.HoopJPanel;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.stats.HoopPerformanceMetrics;

/** 
 *
 */
public class HoopExecuteProgressPanel extends HoopEmbeddedJPanel implements HoopExecutionMonitor
{	
	/**
	 * 
	 */
	public class HoopExecutionListRenderer extends HoopJPanel implements ListCellRenderer 
	{
		private static final long serialVersionUID = -2950497524627822787L;
		protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();
		  
		private JLabel textInfo=null;
		private JLabel stateInfo=null;
		private JLabel timeIndicator=null;
		private JLabel progressIndicator=null;
		
		/**
		 * Creates a new JPanel with a double buffer and a flow layout.
		 */	
		public HoopExecutionListRenderer()
		{
			setClassName ("HoopExecutionListRenderer");
			debug ("HoopExecutionListRenderer ()");
			  
			this.setBorder(BorderFactory.createRaisedBevelBorder());
			this.setLayout(new BoxLayout (this,BoxLayout.X_AXIS));
			
			textInfo=new JLabel ();
			textInfo.setFont(new Font("Dialog", 1, 10));
			textInfo.setMaximumSize(new Dimension (150,20));
			this.add(textInfo);
			
			stateInfo=new JLabel ();
			stateInfo.setFont(new Font("Dialog", 1, 10));
			stateInfo.setMaximumSize(new Dimension (50,20));
			this.add(stateInfo);
			
			timeIndicator=new JLabel ();
			timeIndicator.setFont(new Font("Dialog", 1, 10));
			timeIndicator.setMaximumSize(new Dimension (150,20));
			this.add (timeIndicator);
			
			progressIndicator=new JLabel ();
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
			  
			if (value instanceof HoopBase)
			{
				HoopBase aHoop=(HoopBase) value;
				  				  
				textInfo.setText(aHoop.getClassName());
				
				stateInfo.setText(aHoop.getExecutionState());
								  
				HoopPerformanceMetrics metrics=aHoop.getPerformanceMetrics();
				  
				if (metrics!=null)
				{
					StringBuffer formatter=new StringBuffer ();

					Long result=metrics.getMarkerRaw ();
					  
					formatter.append(result.toString());
					formatter.append("ms");
					
					timeIndicator.setText(formatter.toString());
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
	}	
	
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
		
		//Border blackborder=BorderFactory.createLineBorder(Color.black);
						
		ListCellRenderer renderer = new HoopExecutionListRenderer ();
		
		model = new DefaultListModel();
		
		executionTrace=new JList (model);
		executionTrace.setCellRenderer(renderer);
		
		JScrollPane traceContainer=new JScrollPane (executionTrace);
		traceContainer.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		mainBox.add(traceContainer);
		
		this.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
		
		setContentPane (mainBox);
		
		HoopLink.executionMonitor=this;
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
	public void updateHoopExecution (HoopBase aHoop) 
	{
		debug ("updateHoopExecution ()");
		
		int pos = executionTrace.getModel().getSize();
		model.add (pos,aHoop);
	}	
}
