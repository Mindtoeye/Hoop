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

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import edu.cmu.cs.in.base.INLink;
import edu.cmu.cs.in.controls.base.INEmbeddedJPanel;
//import edu.cmu.cs.in.controls.base.INJInternalFrame;

/** 
 * @author vvelsen
 *
 */
public class INHoopEditor extends INEmbeddedJPanel implements ActionListener
{	
	private static final long serialVersionUID = -1;
	
	private mxGraph graph=null;
	
	private JButton addLoadHoop=null;
	private JButton addSaveHoop=null;
	private JButton addToKVHoop=null;
	private JButton addToListHoop=null;
	private JButton addToTableHoop=null;	
		
	/**
	 * 
	 */	
	public INHoopEditor()
	{
		//super("Hoop Editor", true, true, true, true);
		
		setClassName ("INHoopEditor");
		debug ("INHoopEditor ()");
								
		addLoadHoop=new JButton ();
		addLoadHoop.setIcon(INLink.imageIcons [77]);
		addLoadHoop.setMargin(new Insets(1, 1, 1, 1));		
		addLoadHoop.setFont(new Font("Courier",1,8));
		addLoadHoop.setMinimumSize(new Dimension (16,16));
		addLoadHoop.setPreferredSize(new Dimension (16,16));
		addLoadHoop.addActionListener(this);
		
		addSaveHoop=new JButton ();
		addSaveHoop.setIcon(INLink.imageIcons [78]);
		addSaveHoop.setMargin(new Insets(1, 1, 1, 1));		
		addSaveHoop.setFont(new Font("Courier",1,8));
		addSaveHoop.setMinimumSize(new Dimension (16,16));
		addSaveHoop.setPreferredSize(new Dimension (16,16));
		addSaveHoop.addActionListener(this);
		
		addToKVHoop=new JButton ();
		addToKVHoop.setIcon(INLink.imageIcons [79]);
		addToKVHoop.setMargin(new Insets(1, 1, 1, 1));		
		addToKVHoop.setFont(new Font("Courier",1,8));
		addToKVHoop.setMinimumSize(new Dimension (16,16));
		addToKVHoop.setPreferredSize(new Dimension (16,16));
		addToKVHoop.addActionListener(this);
		
		addToListHoop=new JButton ();
		addToListHoop.setIcon(INLink.imageIcons [80]);
		addToListHoop.setMargin(new Insets(1, 1, 1, 1));		
		addToListHoop.setFont(new Font("Courier",1,8));
		addToListHoop.setMinimumSize(new Dimension (16,16));
		addToListHoop.setPreferredSize(new Dimension (16,16));
		addToListHoop.addActionListener(this);
		
		addToTableHoop=new JButton ();
		addToTableHoop.setIcon(INLink.imageIcons [81]);
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
		mxGraphComponent graphComponent = new mxGraphComponent(graph);
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
			Object parent=graph.getDefaultParent();
					
			graph.insertVertex (parent, null,"Load",20,20,80,30);			
		}
		
		if (button==addSaveHoop)
		{
			Object parent=graph.getDefaultParent();
			
			graph.insertVertex (parent, null,"Save",20,20,80,30);			
		}
		
		if (button==addToKVHoop)
		{
			Object parent=graph.getDefaultParent();
			
			graph.insertVertex (parent, null,"To KV",20,20,80,30);			
		}
		
		if (button==addToListHoop)
		{
			Object parent=graph.getDefaultParent();
			
			graph.insertVertex (parent, null,"To List",20,20,80,30);			
		}
		
		if (button==addToTableHoop)
		{
			Object parent=graph.getDefaultParent();
			
			graph.insertVertex (parent, null,"To Table",20,20,80,30);			
		}														
	}
}
