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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.controls.base.HoopEmbeddedJPanel;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.hoop.hoops.base.HoopLoadBase;
//import edu.cmu.cs.in.controls.base.HoopJInternalFrame;
import edu.cmu.cs.in.hoop.hoops.base.HoopTransformBase;
import edu.cmu.cs.in.hoop.hoops.save.HoopFileSaveBase;

/** 
 * @author vvelsen
 *
 */
public class HoopEditor extends HoopEmbeddedJPanel implements ActionListener, MouseListener
{	
	private static final long serialVersionUID = -1;
	
	private mxGraph graph=null;
	
	private JButton addLoadHoop=null;
	private JButton addSaveHoop=null;
	private JButton addToKVHoop=null;
	private JButton addToListHoop=null;
	private JButton addToTableHoop=null;	
	
	private ArrayList <HoopBase>hoops=null;
	
	private mxGraphComponent graphComponent=null;
		
	/**
	 * 
	 */	
	public HoopEditor()
	{
		super (HoopLink.getImageByName("hoop-graph.png"));
		
		setClassName ("HoopEditor");
		debug ("HoopEditor ()");
		
		hoops=new ArrayList<HoopBase> ();
								
		addLoadHoop=new JButton ();
		addLoadHoop.setIcon(HoopLink.imageIcons [77]);
		addLoadHoop.setMargin(new Insets(1, 1, 1, 1));		
		addLoadHoop.setFont(new Font("Courier",1,8));
		addLoadHoop.setMinimumSize(new Dimension (16,16));
		addLoadHoop.setPreferredSize(new Dimension (16,16));
		addLoadHoop.addActionListener(this);
		
		addSaveHoop=new JButton ();
		addSaveHoop.setIcon(HoopLink.imageIcons [78]);
		addSaveHoop.setMargin(new Insets(1, 1, 1, 1));		
		addSaveHoop.setFont(new Font("Courier",1,8));
		addSaveHoop.setMinimumSize(new Dimension (16,16));
		addSaveHoop.setPreferredSize(new Dimension (16,16));
		addSaveHoop.addActionListener(this);
		
		addToKVHoop=new JButton ();
		addToKVHoop.setIcon(HoopLink.imageIcons [79]);
		addToKVHoop.setMargin(new Insets(1, 1, 1, 1));		
		addToKVHoop.setFont(new Font("Courier",1,8));
		addToKVHoop.setMinimumSize(new Dimension (16,16));
		addToKVHoop.setPreferredSize(new Dimension (16,16));
		addToKVHoop.addActionListener(this);
		
		addToListHoop=new JButton ();
		addToListHoop.setIcon(HoopLink.imageIcons [80]);
		addToListHoop.setMargin(new Insets(1, 1, 1, 1));		
		addToListHoop.setFont(new Font("Courier",1,8));
		addToListHoop.setMinimumSize(new Dimension (16,16));
		addToListHoop.setPreferredSize(new Dimension (16,16));
		addToListHoop.addActionListener(this);
		
		addToTableHoop=new JButton ();
		addToTableHoop.setIcon(HoopLink.imageIcons [81]);
		addToTableHoop.setMargin(new Insets(1, 1, 1, 1));		
		addToTableHoop.setFont(new Font("Courier",1,8));
		addToTableHoop.setMinimumSize(new Dimension (16,16));
		addToTableHoop.setPreferredSize(new Dimension (16,16));
		addToTableHoop.addActionListener(this);		
		
		Border blackborder=BorderFactory.createLineBorder(Color.black);
		
		JPanel filler=new JPanel ();
		filler.setBorder (blackborder);
		filler.setMinimumSize (new Dimension (16,16));
		filler.setMaximumSize (new Dimension (16,5000));
		
		Box controlBox = new Box (BoxLayout.Y_AXIS);
		controlBox.add(addLoadHoop);
		controlBox.add(addSaveHoop);
		controlBox.add(addToKVHoop);
		controlBox.add(addToListHoop);
		controlBox.add(addToTableHoop);			
		//controlBox.add(filler);
		controlBox.setMinimumSize(new Dimension (24,150));
		controlBox.setPreferredSize(new Dimension (24,150));
		
		graph=new mxGraph();		
		graphComponent = new mxGraphComponent(graph);
		//graphComponent.setEnabled(false);
						
		JScrollPane consoleContainer = new JScrollPane (graphComponent);
		consoleContainer.setMinimumSize(new Dimension (50,50));
		//consoleContainer.setPreferredSize(new Dimension (5000,5000));
			
		Box mainBox = new Box (BoxLayout.X_AXIS);
		
		mainBox.add(controlBox);
		mainBox.add(consoleContainer);
				
		setContentPane (mainBox);
		//setSize (425,300);
		//setLocation (75,75);			
		
		graphComponent.getGraphControl().addMouseListener(this);				
	}
	/**
	 * 
	 */
	private void addHoop (HoopBase aHoop)
	{
		// Add to internal structure
		
		hoops.add(aHoop);
		
		// Add to visual graph
		
		Object parent=graph.getDefaultParent();		
		graph.insertVertex (parent,null,aHoop.getHoopCategory(),20,20,80,30);		
	}
	/**
	 * 
	 */	
	@Override
	public void actionPerformed(ActionEvent event) 
	{
		debug ("actionPerformed ()");
		
		//String act=event.getActionCommand();
		JButton button = (JButton)event.getSource();		

		if (button==addLoadHoop)
		{
			addHoop (new HoopLoadBase ());			
		}
		
		if (button==addSaveHoop)
		{
			addHoop (new HoopFileSaveBase ());			
		}
		
		if (button==addToKVHoop)
		{
			addHoop (new HoopTransformBase ());			
		}
		
		if (button==addToListHoop)
		{
			//addHoop (new HoopLoadBase ());			
		}
		
		if (button==addToTableHoop)
		{
			//addHoop (new HoopLoadBase ());			
		}														
	}
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent arg0) 
	{
		
	}
	@Override	
	public void mouseReleased(MouseEvent e)
	{
		Object cell = graphComponent.getCellAt(e.getX(), e.getY());
		
		if (cell != null)
		{
			debug ("cell="+graph.getLabel(cell));
		}
	}	
}
