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

package edu.cmu.cs.in.hoop.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.handler.mxCellHandler;
import com.mxgraph.view.mxGraph;

import edu.cmu.cs.in.base.INHoopLink;
import edu.cmu.cs.in.controls.INHoopShadowBorder;

/**
 *  
 * Implements an event redirector for the specified handle index, where 0
 * is the top right, and 1-7 are the top center, rop right, middle left,
 * middle right, bottom left, bottom center and bottom right, respectively.
 * Default index is 7 (bottom right). 
 * 
 */
public class INHoopNodeRenderer extends JComponent implements MouseListener, MouseMotionListener
{
	private static final long serialVersionUID = -1L;

	protected Object cell;
	
	protected mxGraphComponent graphContainer=null;
	protected mxGraph graph=null;
	
	private JPanel contentArea=null;	
	private JPanel titleBar=null;
	private JLabel icon=null;
	private JLabel label=null;
	private JLabel titleLabel=null;
	private JPanel toolBar=null;
	private JPanel bottomPanel=null;
	
	protected int index=7;

	/**
	 * 
	 */
	@SuppressWarnings("serial")
	public INHoopNodeRenderer (final Object cell, final mxGraphComponent graphContainer)
	{
		this.cell = cell;
		this.graphContainer = graphContainer;
		this.graph = graphContainer.getGraph();
		
		setBackground(new Color (50,50,50));
		setOpaque(true);
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createCompoundBorder(INHoopShadowBorder.getSharedInstance(), BorderFactory.createBevelBorder(BevelBorder.RAISED)));

		titleBar = new JPanel();
		titleBar.setBackground(new Color(50,50,50));
		titleBar.setOpaque(true);
		titleBar.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 1));
		titleBar.setLayout(new BorderLayout());

		icon = new JLabel(INHoopLink.getImageByName("led-yellow.png"));
		icon.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 1));
		titleBar.add(icon, BorderLayout.WEST);

		titleLabel = new JLabel(String.valueOf(graph.getLabel(cell)));
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setFont(new Font("Dialog", 1, 10));
		titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 1, 0, 2));
		titleBar.add(titleLabel, BorderLayout.CENTER);

		toolBar = new JPanel();
		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT, 1, 2));
		toolBar.setBackground(new Color(50,50,50));
		toolBar.setOpaque(true);
		
		JButton button = new JButton(new AbstractAction("", INHoopLink.getImageByName("minimize.gif"))
		{
			public void actionPerformed(ActionEvent e)
			{
				graph.foldCells(graph.isCellCollapsed(cell), false,	new Object[] { cell });
				
				if (graph.isCellCollapsed(cell))
					((JButton) e.getSource()).setIcon(INHoopLink.getImageByName("maximize.gif"));
				else
					((JButton) e.getSource()).setIcon(INHoopLink.getImageByName("minimize.gif"));
			}
		});
		
		button.setPreferredSize(new Dimension(16, 16));
		button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		button.setToolTipText("Collapse/Expand");
		button.setOpaque(false);
		toolBar.add(button);

		titleBar.add(toolBar, BorderLayout.EAST);
		add (titleBar, BorderLayout.NORTH);
		
		contentArea=new JPanel ();
		contentArea.setBackground(new Color (29,29,29));
		contentArea.setBorder(BorderFactory.createLoweredBevelBorder());
		add (contentArea, BorderLayout.CENTER);
		
		label = new JLabel(INHoopLink.getImageByName("resize.png"));
		label.setCursor(new Cursor(Cursor.NW_RESIZE_CURSOR));

		bottomPanel=new JPanel();
		bottomPanel.setLayout(new BorderLayout());
		bottomPanel.setBackground(new Color(50,50,50));
		bottomPanel.setOpaque(true);
		bottomPanel.add(label, BorderLayout.EAST);

		add(bottomPanel, BorderLayout.SOUTH);
		
		label.addMouseListener(this);
		label.addMouseMotionListener(this);

		setMinimumSize(new Dimension(20, 30));
		
		setTitle ("Hoop Node");
	}
	/**
	 * 
	 */
	public static INHoopNodeRenderer getVertex(Component component)
	{
		while (component != null)
		{
			if (component instanceof INHoopNodeRenderer)
			{
				return (INHoopNodeRenderer) component;
			}
			
			component = component.getParent();
		}

		return null;
	}
	/**
	 * 
	 */	
	public void setTitle (String aTitle)
	{
		titleLabel.setText(aTitle);
	}
	/**
	 * 
	 */	
	@Override
	public void mouseDragged(MouseEvent e) 
	{
		graphContainer.getGraphControl().dispatchEvent(SwingUtilities.convertMouseEvent((Component) e.getSource(),e, graphContainer.getGraphControl()));
	}
	/**
	 * 
	 */	
	@Override
	public void mouseMoved(MouseEvent arg0) 
	{
		// Not implemented yet
	}
	/**
	 * 
	 */	
	@Override
	public void mouseClicked(MouseEvent arg0) 
	{
		// Not implemented yet
	}
	/**
	 * 
	 */	
	@Override
	public void mouseEntered(MouseEvent arg0) 
	{
		// Not implemented yet		
	}
	/**
	 * 
	 */	
	@Override
	public void mouseExited(MouseEvent arg0) 
	{
		// Not implemented yet		
	}
	/**
	 * 
	 */	
	@Override
	public void mousePressed(MouseEvent e) 
	{
		// Selects to create a handler for resizing
		if (!graph.isCellSelected(cell))
		{
			graphContainer.selectCellForEvent(cell, e);
		}

		// Initiates a resize event in the handler
		mxCellHandler handler = graphContainer.getSelectionCellsHandler().getHandler(cell);

		if (handler != null)
		{
			// Starts the resize at index 7 (bottom right)
			handler.start(SwingUtilities.convertMouseEvent((Component) e.getSource(), e, graphContainer.getGraphControl()),	index);
			e.consume();
		}
	}
	/**
	 * 
	 */	
	@Override
	public void mouseReleased(MouseEvent e) 
	{
		graphContainer.getGraphControl().dispatchEvent(SwingUtilities.convertMouseEvent((Component) e.getSource(),e, graphContainer.getGraphControl()));
	}
}
