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
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//import java.awt.event.MouseEvent;
//import java.awt.event.MouseListener;
//import java.awt.event.MouseMotionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
//import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

import com.mxgraph.swing.mxGraphComponent;
//import com.mxgraph.swing.handler.mxCellHandler;
import com.mxgraph.view.mxGraph;

import edu.cmu.cs.in.base.INHoopLink;
import edu.cmu.cs.in.base.INHoopProperties;
import edu.cmu.cs.in.controls.INHoopShadowBorder;
import edu.cmu.cs.in.controls.base.INJComponent;
import edu.cmu.cs.in.hoop.base.INHoopBase;

/**
 *  
 * Implements an event redirector for the specified handle index, where 0
 * is the top right, and 1-7 are the top center, rop right, middle left,
 * middle right, bottom left, bottom center and bottom right, respectively.
 * Default index is 7 (bottom right). 
 * 
 */
public class INHoopNodeRenderer extends INJComponent implements /*MouseListener, MouseMotionListener,*/ ActionListener
{
	private static final long serialVersionUID = -1L;

	protected Object cell;
	
	protected mxGraphComponent graphContainer=null;
	protected mxGraph graph=null;
	
	//protected JPanel contentArea=null;	
	protected JTextArea contentArea=null;
	protected JPanel titleBar=null;
	protected JLabel icon=null;
	protected JLabel label=null;
	protected JLabel titleLabel=null;
	protected JPanel toolBar=null;
	protected JPanel bottomPanel=null;
	protected JButton kvExamineButton=null;
	protected JButton showHelpButton=null;
		
	Box leftPortBox = null;
	Box rightPortBox = null;	
	
	protected int index=7;
	
	protected INHoopBase hoop=null;
	private   INHoopBase hoopTemplate=null;
		
	/**
	 * 
	 */
	public INHoopNodeRenderer (Object cell,mxGraphComponent graphContainer)
	{
		setClassName ("INHoopNodeRenderer");
		debug ("INHoopNodeRenderer ()");
		
		//Border blackborder=BorderFactory.createLineBorder(Color.black);
		//Border redborder=BorderFactory.createLineBorder(Color.red);		
		
		this.cell = cell;
		this.graphContainer = graphContainer;
		this.graph = graphContainer.getGraph();
		
		setBackground(INHoopProperties.graphPanelColor);
		setOpaque(true);
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createCompoundBorder(INHoopShadowBorder.getSharedInstance(), BorderFactory.createBevelBorder(BevelBorder.RAISED)));

		titleBar = new JPanel();
		titleBar.setBackground(INHoopProperties.graphPanelColor);
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
		toolBar.setBackground(INHoopProperties.graphPanelColor);
		toolBar.setOpaque(true);
				
		kvExamineButton=new JButton ();
		kvExamineButton.setIcon(INHoopLink.getImageByName("zoom.png"));
		kvExamineButton.setPreferredSize(new Dimension(16, 16));
		kvExamineButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		kvExamineButton.setToolTipText("Collapse/Expand");
		kvExamineButton.setBackground(INHoopProperties.graphPanelColor);
		kvExamineButton.setOpaque(true);
		kvExamineButton.setBorder(null);
		kvExamineButton.setMargin(new Insets (0,0,0,0));
		kvExamineButton.addActionListener(this);
		toolBar.add(kvExamineButton);
		
		showHelpButton=new JButton ();
		showHelpButton.setIcon(INHoopLink.getImageByName("help_icon.png"));
		showHelpButton.setPreferredSize(new Dimension(16, 16));
		showHelpButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		showHelpButton.setToolTipText("Collapse/Expand");
		showHelpButton.setBackground(INHoopProperties.graphPanelColor);
		showHelpButton.setOpaque(true);
		showHelpButton.setBorder(null);
		showHelpButton.setMargin(new Insets (0,0,0,0));
		showHelpButton.addActionListener(this);
		toolBar.add(showHelpButton);		

		titleBar.add(toolBar, BorderLayout.EAST);
		add (titleBar, BorderLayout.NORTH);
		
		Box contentBox = new Box (BoxLayout.X_AXIS);	
		//contentBox.setBorder(redborder);
		contentBox.setMinimumSize(new Dimension(50,50));
		contentBox.setPreferredSize(new Dimension(100,100));
		contentBox.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		
		leftPortBox = new Box (BoxLayout.Y_AXIS);
		//leftPortBox.setAlignmentX(LEFT_ALIGNMENT);
		leftPortBox.setBackground(INHoopProperties.graphPanelColor);
		leftPortBox.setOpaque(true);
		leftPortBox.setMinimumSize(new Dimension(50,20));
		leftPortBox.setPreferredSize(new Dimension(50,100));
		
		rightPortBox = new Box (BoxLayout.Y_AXIS);		
		//rightPortBox.setAlignmentX(RIGHT_ALIGNMENT);
		rightPortBox.setBackground(INHoopProperties.graphPanelColor);
		rightPortBox.setOpaque(true);
		rightPortBox.setMinimumSize(new Dimension(50,20));		
		rightPortBox.setPreferredSize(new Dimension(50,100));		
		
		contentArea=new JTextArea ();
		contentArea.setFont(new Font("Dialog", 1, 10));
		//contentArea.setForeground(Color.WHITE);
		contentArea.setLineWrap(true);
		contentArea.setWrapStyleWord(true);
		contentArea.setEditable(false);
		contentArea.setBackground(INHoopProperties.graphPanelContent);
		//contentArea.setBorder(BorderFactory.createLoweredBevelBorder());
				
		contentBox.add(leftPortBox);
		//contentBox.add(contentArea);
		contentBox.add(Box.createHorizontalGlue());
		contentBox.add(rightPortBox);
		
		add (contentBox, BorderLayout.CENTER);
		
		label = new JLabel(INHoopLink.getImageByName("resize.png"));
		label.setCursor(new Cursor(Cursor.NW_RESIZE_CURSOR));

		bottomPanel=new JPanel();
		bottomPanel.setLayout(new BorderLayout());
		bottomPanel.setBackground(INHoopProperties.graphPanelColor);
		bottomPanel.setOpaque(true);
		bottomPanel.add(label, BorderLayout.EAST);

		add(bottomPanel, BorderLayout.SOUTH);
		
		//label.addMouseListener(this);
		//label.addMouseMotionListener(this);

		setMinimumSize(new Dimension(20, 30));
		
		setTitle ("Hoop Node");
	}
	/**
	 * 
	 */	
	public INHoopBase getHoopTemplate() 
	{
		return hoopTemplate;
	}
	/**
	 * 
	 */
	protected void removeAllInPorts ()
	{
		debug ("removeAllInPorts ()");
		
		leftPortBox.removeAll();
	}	
	/**
	 * 
	 */
	protected void removeAllOutPorts ()
	{
		debug ("removeAllOutPorts ()");
		
		rightPortBox.removeAll();
	}
	/**
	 * 
	 */
	protected void addInPort (String aPort)
	{
		debug ("addInPort ("+aPort+")");
		
		//Border border=BorderFactory.createLineBorder(Color.white);	
		
		Box portBox = new Box (BoxLayout.X_AXIS);
		portBox.setAlignmentX(LEFT_ALIGNMENT);
		portBox.setMinimumSize(new Dimension (50,20));
		portBox.setPreferredSize(new Dimension (50,20));
		//portBox.setBorder(border);
	
		JLabel portIcon = new JLabel(INHoopLink.getImageByName("port.png"));
		portIcon.setMinimumSize(new Dimension (16,16));
		portIcon.setMaximumSize(new Dimension (16,16));		
		
		JLabel aPortLabel=new JLabel ();
		aPortLabel.setText(aPort);
		//aPortLabel.setBorder(bevel);
		aPortLabel.setHorizontalAlignment (JLabel.RIGHT);
		aPortLabel.setFont(new Font("Dialog", 1, 10));	
		aPortLabel.setForeground(Color.WHITE);
		aPortLabel.setMinimumSize(new Dimension (50,20));
		//aPortLabel.setPreferredSize(new Dimension (50,20));
		
		portBox.add(portIcon);
		portBox.add(aPortLabel);
		
		leftPortBox.add(portBox);
	}
	/**
	 * 
	 */
	protected void addOutPort (String aPort)
	{
		debug ("addOutPort ("+aPort+")");
		
		//Border border=BorderFactory.createLineBorder(Color.white);	
		
		Box portBox = new Box (BoxLayout.X_AXIS);
		portBox.setAlignmentX(RIGHT_ALIGNMENT);
		portBox.setMinimumSize(new Dimension (50,20));
		portBox.setPreferredSize(new Dimension (50,20));
		//portBox.setBorder(border);
		
		JLabel portIcon = new JLabel(INHoopLink.getImageByName("port.png"));
		portIcon.setMinimumSize(new Dimension (16,16));
		portIcon.setMaximumSize(new Dimension (16,16));
		
		JLabel aPortLabel=new JLabel ();
		aPortLabel.setText(aPort);
		//aPortLabel.setBorder(bevel);
		aPortLabel.setHorizontalAlignment (JLabel.LEFT);
		aPortLabel.setFont(new Font("Dialog", 1, 10));
		aPortLabel.setForeground(Color.WHITE);
		aPortLabel.setMinimumSize(new Dimension (50,20));
		//aPortLabel.setPreferredSize(new Dimension (50,20));		

		portBox.add(aPortLabel);		
		portBox.add(portIcon);
		
		rightPortBox.add(portBox);		
	}	
	/**
	 * 
	 */	
	public void setHoopTemplate(INHoopBase hoopTemplate) 
	{
		this.hoopTemplate = hoopTemplate;
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
		if (titleLabel!=null)
			titleLabel.setText(aTitle);
	}
	/**
	 * 
	 */	
	public void setDescription (String aDescription)
	{
		if (contentArea!=null)
			contentArea.setText(aDescription);
	}	
	/**
	 * 
	 */	
	/*
	@Override
	public void mouseDragged(MouseEvent e) 
	{
		graphContainer.getGraphControl().dispatchEvent(SwingUtilities.convertMouseEvent((Component) e.getSource(),e, graphContainer.getGraphControl()));
	}
	*/
	/**
	 * 
	 */
	/*
	@Override
	public void mouseMoved(MouseEvent arg0) 
	{
		// Not implemented yet
	}
	*/
	/**
	 * 
	 */
	/*
	@Override
	public void mouseClicked(MouseEvent arg0) 
	{
		// Not implemented yet
	}
	*/
	/**
	 * 
	 */
	/*
	@Override
	public void mouseEntered(MouseEvent arg0) 
	{
		// Not implemented yet		
	}
	*/
	/**
	 * 
	 */
	/*
	@Override
	public void mouseExited(MouseEvent arg0) 
	{
		// Not implemented yet		
	}
	*/
	/**
	 * 
	 */
	/*
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
	*/
	/**
	 * 
	 */	
	/*	
	@Override
	public void mouseReleased(MouseEvent e) 
	{
		graphContainer.getGraphControl().dispatchEvent(SwingUtilities.convertMouseEvent((Component) e.getSource(),e, graphContainer.getGraphControl()));
	}
	*/
	/**
	 * 
	 */
	@Override
	public void actionPerformed(ActionEvent event) 
	{
		debug ("actionPerformed ()");
		
		JButton button = (JButton)event.getSource();
    	    	    	    	
		if (button==kvExamineButton) 
		{		
			examineData ();
							
			/*
			if (graph!=null)
			{
				graph.foldCells (graph.isCellCollapsed(cell), false,	new Object[] { cell });
		
				if (graph.isCellCollapsed(cell))
					((JButton) e.getSource()).setIcon(INHoopLink.getImageByName("maximize.gif"));
				else
					((JButton) e.getSource()).setIcon(INHoopLink.getImageByName("minimize.gif"));
			}
			*/
		}	
		
		if (button==showHelpButton)
		{
			if (hoop!=null)
				hoop.showHelp();
		}
	}
	/**
	 * 
	 */
	protected void examineData ()
	{
		debug ("examineData ()");
		
		// Implement in child class				
	}
}
