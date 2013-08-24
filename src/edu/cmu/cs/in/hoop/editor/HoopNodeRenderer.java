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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.base.HoopProperties;
import edu.cmu.cs.in.controls.HoopProgressPainter;
import edu.cmu.cs.in.controls.HoopShadowBorder;
import edu.cmu.cs.in.controls.base.HoopJPanel;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;

/**
 *  
 * Implements an event redirector for the specified handle index, where 0
 * is the top right, and 1-7 are the top center, rop right, middle left,
 * middle right, bottom left, bottom center and bottom right, respectively.
 * Default index is 7 (bottom right). 
 * 
 */
public class HoopNodeRenderer extends HoopJPanel implements /*MouseListener, MouseMotionListener,*/ ActionListener, MouseListener
{
	private static final long serialVersionUID = -1L;

	protected Object cell;
	
	protected mxGraphComponent graphContainer=null;
	protected mxGraph graph=null;
	
	//protected JPanel contentArea=null;	
	protected JLabel contentArea=null;
	protected JPanel titleBar=null;
	protected JLabel icon=null;
	protected JLabel label=null;
	protected JLabel titleLabel=null;
	protected JLabel statusLabel=null;
	protected JLabel statusPanel=null;
	protected JPanel toolBar=null;
	protected JPanel bottomPanel=null;
	protected HoopProgressPainter progressPanel=null;
	protected JButton kvExamineButton=null;
	protected JButton hoopOptionButton=null;
	protected JButton showHelpButton=null;
	
	protected JPopupMenu popup=null;
	protected JCheckBoxMenuItem beforeCheck=null;
	protected JCheckBoxMenuItem afterCheck=null;
	
	Box leftPortBox = null;
	Box rightPortBox = null;	
	
	protected int index=7;
	
	protected HoopBase hoop=null;
	private   HoopBase hoopTemplate=null;
	
	protected Color backgrColor=HoopProperties.graphPanelColor;
	
	protected Border blackborder=null;
	protected Border redborder=null;	
	protected Border raisedRed=null;
	
	protected Box contentBox=null;
	
	/**
	 * 
	 */
	public HoopNodeRenderer (Object cell,mxGraphComponent graphContainer)
	{
		setClassName ("HoopNodeRenderer");
		debug ("HoopNodeRenderer ()");
		
		blackborder=BorderFactory.createLineBorder(Color.black);
		redborder=BorderFactory.createLineBorder(Color.red);
		raisedRed=BorderFactory.createBevelBorder(BevelBorder.RAISED,Color.red,Color.red);
		
		setupContextPopup ();
		
		this.cell = cell;
		this.graphContainer = graphContainer;
		this.graph = graphContainer.getGraph();
				
		setOpaque(true);
		setBackground(backgrColor);
		//setBorder (blackborder);

		setLayout(new BorderLayout());
		setBorder(BorderFactory.createCompoundBorder(HoopShadowBorder.getSharedInstance(), BorderFactory.createBevelBorder(BevelBorder.RAISED)));

		titleBar = new JPanel();

		titleBar.setBackground(backgrColor);

		titleBar.setOpaque(true);
		titleBar.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 1));
		titleBar.setLayout(new BorderLayout());

		icon = new JLabel(HoopLink.getImageByName("led-yellow.png"));
		icon.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 1));
		titleBar.add(icon, BorderLayout.WEST);

		titleLabel = new JLabel(String.valueOf(graph.getLabel(cell)));

		//titleLabel.setForeground(Color.WHITE);
		titleLabel.setForeground(Color.BLACK);
		titleLabel.setFont(new Font("Dialog", 1, 10));
		titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 1, 0, 2));
		titleBar.add(titleLabel, BorderLayout.CENTER);

		toolBar = new JPanel();
		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT, 1, 2));
		toolBar.setBackground(backgrColor);
		toolBar.setOpaque(true);

		/*
		nodeSelectCheckbox=new JCheckBox();
		nodeSelectCheckbox.setToolTipText("Select node");
		nodeSelectCheckbox.setBackground(HoopProperties.graphPanelColor);
		nodeSelectCheckbox.setOpaque(true);
		nodeSelectCheckbox.setSelected(false); 
		nodeSelectCheckbox.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		nodeSelectCheckbox.addActionListener(this);
		toolBar.add(nodeSelectCheckbox);
		*/		
		
		kvExamineButton=new JButton ();
		kvExamineButton.setIcon(HoopLink.getImageByName("zoom.png"));
		kvExamineButton.setPreferredSize(new Dimension(16, 16));
		kvExamineButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		kvExamineButton.setToolTipText("Collapse/Expand");
		kvExamineButton.setBackground(backgrColor);
		kvExamineButton.setOpaque(true);
		kvExamineButton.setBorder(null);
		kvExamineButton.setMargin(new Insets (0,0,0,0));
		kvExamineButton.addActionListener(this);
		toolBar.add(kvExamineButton);
		
		hoopOptionButton=new JButton ();
		hoopOptionButton.setIcon(HoopLink.getImageByName("gtk-execute.png"));
		hoopOptionButton.setPreferredSize(new Dimension(16, 16));
		hoopOptionButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		hoopOptionButton.setToolTipText("Hoop Runtime Configuration");
		hoopOptionButton.setBackground(backgrColor);
		hoopOptionButton.setOpaque(true);
		hoopOptionButton.setBorder(null);
		hoopOptionButton.setMargin(new Insets (0,0,0,0));
		//hoopOptionButton.addActionListener(this);
		hoopOptionButton.addMouseListener(this);
		toolBar.add(hoopOptionButton);		
		
		showHelpButton=new JButton ();
		showHelpButton.setIcon(HoopLink.getImageByName("help_icon.png"));
		showHelpButton.setPreferredSize(new Dimension(16, 16));
		showHelpButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		showHelpButton.setToolTipText("Collapse/Expand");
		showHelpButton.setBackground(backgrColor);
		showHelpButton.setOpaque(true);
		showHelpButton.setBorder(null);
		showHelpButton.setMargin(new Insets (0,0,0,0));
		showHelpButton.addActionListener(this);
		toolBar.add(showHelpButton);		

		titleBar.add(toolBar, BorderLayout.EAST);
		add (titleBar, BorderLayout.NORTH);
		
		Box centerFrame = new Box (BoxLayout.Y_AXIS);
		
		contentBox = new Box (BoxLayout.X_AXIS);

		//contentBox.setBorder(redborder);
		contentBox.setMinimumSize(new Dimension(50,50));
		contentBox.setPreferredSize(new Dimension(100,100));
		contentBox.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
		
		leftPortBox = new Box (BoxLayout.Y_AXIS);
		//leftPortBox.setAlignmentX(LEFT_ALIGNMENT);
		leftPortBox.setBackground(backgrColor);

		leftPortBox.setOpaque(true);
		leftPortBox.setMinimumSize(new Dimension(50,20));
		leftPortBox.setPreferredSize(new Dimension(50,100));
		
		rightPortBox = new Box (BoxLayout.Y_AXIS);		
		//rightPortBox.setAlignmentX(RIGHT_ALIGNMENT);
		rightPortBox.setBackground(backgrColor);

		rightPortBox.setOpaque(true);
		rightPortBox.setMinimumSize(new Dimension(50,20));		
		rightPortBox.setPreferredSize(new Dimension(50,100));		
		
		contentArea=new JLabel ();
		contentArea.setOpaque(false);
				
		leftPortBox.setBorder(redborder);
		contentArea.setBorder(redborder);
		rightPortBox.setBorder(redborder);
		
		contentBox.add(leftPortBox);
		contentBox.add(Box.createHorizontalGlue());		
		contentBox.add(contentArea);
		contentBox.add(Box.createHorizontalGlue());
		contentBox.add(rightPortBox);
		
		JSeparator topSeparator=new JSeparator (SwingConstants.HORIZONTAL);
		topSeparator.setBackground(new Color (0,0,0));
		
		JSeparator bottomSeparator=new JSeparator (SwingConstants.HORIZONTAL);
		bottomSeparator.setBackground(new Color(0,0,0));
		
		centerFrame.add (topSeparator);
		centerFrame.add (contentBox);
		centerFrame.add (bottomSeparator);
		
		add (centerFrame, BorderLayout.CENTER);
		
		Box progressBox = new Box (BoxLayout.X_AXIS);	
		progressBox.setMinimumSize(new Dimension(50,5));
		progressBox.setPreferredSize(new Dimension(100,5));
		progressBox.setBorder(BorderFactory.createEmptyBorder(2,2,0,2));
		
		progressPanel=new HoopProgressPainter ();
		progressPanel.setProgressVisualization(HoopProgressPainter.VIZ_FLAT);
		progressPanel.setProgressBarType (HoopProgressPainter.BAR_AUTO);
		progressPanel.setBackground(HoopProperties.graphPanelColorLight);
		progressPanel.setOpaque(true);		
		
		progressBox.add(progressPanel);
				
		statusLabel = new JLabel();
		statusLabel.setText("Ex: ");
		statusLabel.setFont(new Font("Dialog", 1, 10));
		//statusLabel.setForeground(Color.WHITE);
		statusLabel.setForeground(Color.BLACK);
		
		statusPanel = new JLabel();
		statusPanel.setText(" R: ");
		statusPanel.setFont(new Font("Dialog", 1, 10));
		//statusPanel.setForeground(Color.WHITE);
		statusPanel.setForeground(Color.BLACK);
		
		label = new JLabel(HoopLink.getImageByName("resize.png"));
		label.setCursor(new Cursor(Cursor.NW_RESIZE_CURSOR));

		bottomPanel=new JPanel();
		bottomPanel.setLayout(new BorderLayout());
		bottomPanel.setBackground(backgrColor);
		bottomPanel.setOpaque(true);
		
		bottomPanel.add(progressBox,BorderLayout.NORTH);
		bottomPanel.add(statusLabel,BorderLayout.WEST);
		bottomPanel.add(statusPanel,BorderLayout.CENTER);
		bottomPanel.add(label, BorderLayout.EAST);

		add(bottomPanel, BorderLayout.SOUTH);
		
		setMinimumSize(new Dimension(20, 30));
		
		setTitle ("Hoop Node");
		
		initPanel ();
	}
	/**
	 * 
	 */
	public JLabel getContentPanel ()
	{
		return (contentArea);
	}
	/**
	 * 
	 */
	public void replaceContentArea (JComponent newPanel)
	{
		debug ("replaceContentArea ()");
		
		if (newPanel==null)
		{
			debug ("Error: can't replace with a null object");
			return;
		}
		
		contentBox.remove(2);
		
		contentBox.add(newPanel,2);
	}
	/**
	 * 
	 */
	private void initPanel ()
	{
		debug ("initPanel ()");
		
		progressPanel.setLevels (50,100);
	}
	/**
	 * 
	 */
	public HoopProgressPainter getProgressPainter ()
	{
		return (progressPanel);
	}
	/**
	 * 
	 */
	private void setupContextPopup ()
	{
		debug ("setupContextPopup ()");
		
        popup = new JPopupMenu();
        
        
        
        beforeCheck=new JCheckBoxMenuItem(new AbstractAction("Break Before") 
        {
			private static final long serialVersionUID = 6156155928600672289L;

			public void actionPerformed(ActionEvent e) 
            {
				setBreakBefore ();
            }
        });
        
        popup.add(beforeCheck);
        
        afterCheck=new JCheckBoxMenuItem(new AbstractAction("Break After") 
        {
			private static final long serialVersionUID = -2964750728898706499L;

			public void actionPerformed(ActionEvent e) 
            {            	
            	setBreakAfter ();
            }
        });
        
        popup.add(afterCheck);
        
        popup.add(new JSeparator());
        
        popup.add(new JMenuItem(new AbstractAction("Add Stats Probe") 
        {
			private static final long serialVersionUID = -3742710177419283015L;

			public void actionPerformed(ActionEvent e) 
            {            	
            	addStatsProbe ();
            }
        }));
	}
	/**
	 * 
	 */	
	public HoopBase getHoopTemplate() 
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
	protected void setStatus (String aStatus)
	{
		statusLabel.setText(aStatus);
	}	
	/**
	 * 
	 */
	public void setExecutionInfo (String aStatus)
	{
		//debug ("setExecutionInfo ("+aStatus+")");
		statusPanel.setText(aStatus);
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
	
		JLabel portIcon = new JLabel(HoopLink.getImageByName("port.png"));
		portIcon.setMinimumSize(new Dimension (16,16));
		portIcon.setMaximumSize(new Dimension (16,16));		
		
		JLabel aPortLabel=new JLabel ();
		aPortLabel.setText(aPort);
		//aPortLabel.setBorder(bevel);
		aPortLabel.setHorizontalAlignment (JLabel.RIGHT);
		aPortLabel.setFont(new Font("Dialog", 1, 10));	
		//aPortLabel.setForeground(Color.WHITE);
		aPortLabel.setForeground(Color.BLACK);
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
		
		JLabel portIcon = new JLabel(HoopLink.getImageByName("port.png"));
		portIcon.setMinimumSize(new Dimension (16,16));
		portIcon.setMaximumSize(new Dimension (16,16));
		
		JLabel aPortLabel=new JLabel ();
		aPortLabel.setText(aPort);
		//aPortLabel.setBorder(bevel);
		aPortLabel.setHorizontalAlignment (JLabel.LEFT);
		aPortLabel.setFont(new Font("Dialog", 1, 10));
		//aPortLabel.setForeground(Color.WHITE);
		aPortLabel.setForeground(Color.BLACK);
		aPortLabel.setMinimumSize(new Dimension (50,20));
		//aPortLabel.setPreferredSize(new Dimension (50,20));		

		portBox.add(aPortLabel);		
		portBox.add(portIcon);
		
		rightPortBox.add(portBox);		
	}	
	/**
	 * 
	 */	
	public void setHoopTemplate(HoopBase hoopTemplate) 
	{
		this.hoopTemplate = hoopTemplate;
	}	
	/**
	 * 
	 */
	public static HoopNodeRenderer getVertex(Component component)
	{
		while (component != null)
		{
			if (component instanceof HoopNodeRenderer)
			{
				return (HoopNodeRenderer) component;
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
	/*
	public void setDescription (String aDescription)
	{
		if (contentArea!=null)
			contentArea.setText(aDescription);
	}
	*/	
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
		}	
		
		/*
		if (button==hoopOptionButton)
		{
			popup.show(e.getComponent(), event.getX(), event.getY());
		}
		*/
		
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
	/**
	 * 
	 */
	protected void setWaiting (Boolean aVal)
	{
		/*
		if (aVal==true)
			contentArea.setVisible(false);
		else
			contentArea.setVisible(true);
		*/	
	}
	@Override
	public void mouseClicked(MouseEvent arg0) 
	{
		
	}
	@Override
	public void mouseEntered(MouseEvent arg0) 
	{
		
	}
	@Override
	public void mouseExited(MouseEvent arg0) 
	{
		
	}
	@Override
	public void mousePressed(MouseEvent event) 
	{
		debug ("Showing context menu ...");
		
		popup.show(event.getComponent(), event.getX(), event.getY());	
	}
	@Override
	public void mouseReleased(MouseEvent arg0) 
	{
		
	}
	/**
	 *  
	 */
	private void setBreakBefore ()
	{
		debug ("setBreakBefore ()");
		
		if (hoop!=null)
		{
			if (hoop.getBreakBefore()==false)
			{
				beforeCheck.setSelected(true);
				hoop.setBreakBefore(true);
			}
			else
			{
				beforeCheck.setSelected(false);
				hoop.setBreakBefore(false);
			}
		}		
	}
	/**
	 *  
	 */
	private void setBreakAfter ()
	{
		debug ("setBreakAfter ()");
		
		if (hoop!=null)
		{
			if (hoop.getBreakAfter()==false)
			{
				afterCheck.setSelected(true);
				hoop.setBreakAfter(true);
			}
			else
			{
				afterCheck.setSelected(true);
				hoop.setBreakAfter(false);
			}	
		}
	}	
	/**
	 *  
	 */
	private void addStatsProbe ()
	{
		debug ("addStatsProbe ()");
	}	
}
