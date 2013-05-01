
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

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

//import org.pushingpixels.flamingo.api.bcb.JBreadcrumbBar;
import org.pushingpixels.flamingo.api.bcb.core.BreadcrumbFileSelector;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxCompactTreeLayout;
import com.mxgraph.layout.mxEdgeLabelLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.layout.mxOrganicLayout;
import com.mxgraph.layout.mxParallelEdgeLayout;
import com.mxgraph.layout.mxPartitionLayout;
import com.mxgraph.layout.mxStackLayout;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.handler.mxKeyboardHandler;
import com.mxgraph.swing.handler.mxPanningHandler;
import com.mxgraph.swing.handler.mxRubberband;
import com.mxgraph.swing.util.mxMorphing;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.util.mxResources;
import com.mxgraph.util.mxUndoManager;
import com.mxgraph.util.mxUndoableEdit;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.util.mxUndoableEdit.mxUndoableChange;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxGraphSelectionModel;

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.controls.HoopBreadCrumbBar;
import edu.cmu.cs.in.controls.HoopButtonBox;
import edu.cmu.cs.in.controls.base.HoopEmbeddedJPanel;
import edu.cmu.cs.in.controls.base.HoopLockableJPanel;
import edu.cmu.cs.in.hoop.execute.HoopExecuteInEditor;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.hoop.properties.HoopPropertyPanel;

public class HoopBasicGraphEditor extends HoopEmbeddedJPanel implements MouseWheelListener, KeyListener, MouseMotionListener, ActionListener
{
	private static final long serialVersionUID = -1L;
	protected mxGraphComponent graphComponent;
	protected mxGraph graph=null;
	protected mxUndoManager undoManager;
	
	protected String appTitle;
	protected File currentFile;
	
	protected JToggleButton annotateToggle=null;
	protected JButton clearBreakPoints=null;
	protected JButton zoomIn=null;
	protected JButton zoomOut=null;
	protected JButton speedButton=null;
	protected HoopLockableJPanel graphContainer=null;
	
	protected HoopBreadCrumbBar phaseBreadCrumbBar=null;

	/// Flag indicating whether the current graph has been modified 
	protected boolean modified = false;
	protected mxPanningHandler panning=null;
	protected mxRubberband rubberband=null;
	protected mxKeyboardHandler keyboardHandler=null;
	
	public boolean shiftDown=false;
	
	/**
	 * 
	 */
	protected mxIEventListener undoHandler = new mxIEventListener()
	{
		public void invoke(Object source, mxEventObject evt)
		{
			undoManager.undoableEditHappened((mxUndoableEdit) evt.getProperty("edit"));
		}
	};
	/**
	 * 
	 */
	protected mxIEventListener changeTracker = new mxIEventListener()
	{
		public void invoke(Object source, mxEventObject evt)
		{
			setModified(true);
		}
	};
	/**
	 * 
	 */
	public HoopBasicGraphEditor (String appTitle, 
								 mxGraphComponent component)
	{
		super (HoopLink.getImageByName("hoop-graph.png"));
		
		setClassName ("HoopBasicGraphEditor");
		debug ("HoopBasicGraphEditor ()");		
		
		// Stores a reference to the graph and creates the command history
		graphComponent = component;
		graph = graphComponent.getGraph();
		undoManager = createUndoManager();

		// Do not change the scale and translation after files have been loaded
		graph.setResetViewOnRootChange(true);

		// Updates the modified flag if the graph model changes
		graph.getModel().addListener(mxEvent.CHANGE, changeTracker);

		// Adds the command history to the model and view
		graph.getModel().addListener(mxEvent.UNDO, undoHandler);
		graph.getView().addListener(mxEvent.UNDO, undoHandler);

		// Keeps the selection in sync with the command history
		mxIEventListener undoHandler = new mxIEventListener()
		{
			public void invoke(Object source, mxEventObject evt)
			{
				List<mxUndoableChange> changes = ((mxUndoableEdit) evt.getProperty("edit")).getChanges();
				graph.setSelectionCells (graph.getSelectionCellsForChanges(changes));
			}
		};

		undoManager.addListener(mxEvent.UNDO, undoHandler);
		undoManager.addListener(mxEvent.REDO, undoHandler);

		// Display some useful information about repaint events
		installRepaintListener();
						
		createEditorGUI ();

		installHandlers();
		installListeners();				
		
		if (rubberband!=null)
			rubberband.setEnabled(false);
		
		if (graphComponent!=null)
		{
			graphComponent.getSelectionCellsHandler().setEnabled(false);
			graphComponent.getSelectionCellsHandler().setVisible(false);			
		}	
	}
	/**
	 * 
	 */
	private void createEditorGUI ()
	{
		debug ("createEditorGUI ()");
		
		JPanel container=(JPanel) this.getContentPane();
		
		graphContainer=new HoopLockableJPanel ();
		
		graphContainer.add(graphComponent);
		
		container.setLayout(new GridBagLayout());
		
        GridBagConstraints c = new GridBagConstraints();
        				
		HoopButtonBox graphControls=new HoopButtonBox ();
		graphControls.setMinimumSize(new Dimension (100,24));
		graphControls.setPreferredSize(new Dimension (100,24));
		
		c.gridx=0;
		c.gridy=0;
		c.weightx=1.0;
		c.weighty=0.0;
		c.fill = GridBagConstraints.HORIZONTAL;
		
		container.add(graphControls,c);
		//container.add(Box.createRigidArea(new Dimension(0,2)));
		
		c.gridx=0;
		c.gridy=1;
		c.weightx=1.0;
		c.weighty=1.0;		
		c.fill = GridBagConstraints.BOTH;
		
		container.add(graphContainer,c);
		
		// setup buttons and breadcrumb menu ...
		
		annotateToggle=new JToggleButton ();
		annotateToggle.setFont(new Font("Dialog", 1, 8));
		annotateToggle.setPreferredSize(new Dimension (20,20));
		annotateToggle.setMaximumSize(new Dimension (20,20));
		annotateToggle.setIcon(HoopLink.getImageByName("annotation.gif"));
		annotateToggle.addActionListener(this);		
	    
	    graphControls.addComponent(annotateToggle);
	    
	    speedButton=new JButton ();
	    speedButton.setFont(new Font("Dialog", 1, 8));
	    speedButton.setPreferredSize(new Dimension (36,20));
	    speedButton.setMaximumSize(new Dimension (36,20));
	    speedButton.setIcon(HoopLink.getImageByName("speed-1.png"));
	    speedButton.addActionListener(this);
	    
		graphControls.addComponent(speedButton);
			    	    
	    clearBreakPoints=new JButton ();
	    clearBreakPoints.setFont(new Font("Dialog", 1, 8));
	    clearBreakPoints.setPreferredSize(new Dimension (20,20));
	    clearBreakPoints.setMaximumSize(new Dimension (20,20));
	    clearBreakPoints.setIcon(HoopLink.getImageByName("gtk-cancel.png"));
	    clearBreakPoints.addActionListener(this);		
	    
	    graphControls.addComponent(clearBreakPoints);

	    zoomIn=new JButton ();
	    zoomIn.setFont(new Font("Dialog", 1, 8));
	    zoomIn.setPreferredSize(new Dimension (20,20));
	    zoomIn.setMaximumSize(new Dimension (20,20));
	    zoomIn.setIcon(HoopLink.getImageByName("zoomin.gif"));
	    zoomIn.addActionListener(this);		
	    
	    graphControls.addComponent(zoomIn);
	    
	    zoomOut=new JButton ();
	    zoomOut.setFont(new Font("Dialog", 1, 8));
	    zoomOut.setPreferredSize(new Dimension (20,20));
	    zoomOut.setMaximumSize(new Dimension (20,20));
	    zoomOut.setIcon(HoopLink.getImageByName("zoomout.gif"));
	    zoomOut.addActionListener(this);		
	    
	    graphControls.addComponent(zoomOut);
	    
	    
	    
	    /*
	    phaseBreadCrumbBar=new HoopBreadCrumbBar (null);
	    phaseBreadCrumbBar.setMinimumSize(new Dimension (100,24));
	    
	    graphControls.addComponent(phaseBreadCrumbBar);
	    
	    createBreadCrumbs ();
	    */
	    
	    BreadcrumbFileSelector bar=new BreadcrumbFileSelector ();
	    graphControls.addComponent(bar);
	}
	/**
	 * 
	 */
	private void createBreadCrumbs ()
	{
		debug ("createBreadCrumbs ()");
		
		ArrayList<String> demoCrumbs=new ArrayList<String> ();
		
		demoCrumbs.add("First");
		demoCrumbs.add("Second");
		demoCrumbs.add("Third");
		
		phaseBreadCrumbBar.setPath(demoCrumbs);
	}
	/** 
	 * @return JTabbedPane
	 */
	public JTabbedPane getLibraryPane ()
	{
		debug ("getLibraryPane ()");
		
		HoopEditorPalettePanel pal=(HoopEditorPalettePanel) HoopLink.getWindow("Hoop Palette");
		
		if (pal!=null)
			return (pal.getLibraryPane ());
			
		return (null);
	}
	/**
	 * 
	 */
	protected mxUndoManager createUndoManager()
	{
		debug ("createUndoManager ()");
		
		return new mxUndoManager();
	}
	/**
	 * 
	 */
	protected void installHandlers()
	{
		debug ("installHandlers ()");
		
		// Installs rubberband selection and handling for some special
		// keystrokes such as F2, Control-C, -V, X, A etc.
		rubberband = new mxRubberband(graphComponent);
				
		panning=new HoopPanningHandler (graphComponent);
		
		//mxGraphHandler handler=new mxGraphHandler (graphComponent);
				
		keyboardHandler = new HoopEditorKeyboardHandler(graphComponent);
		
	    graph.getSelectionModel().addListener(mxEvent.CHANGE, new mxIEventListener() 
	    {
	        @Override
	        public void invoke(Object sender, mxEventObject evt) 
	        {
	            debug ("evt.toString() = " + evt.toString());
	            debug ("Selection in graph component");
	            
	            if (sender instanceof mxGraphSelectionModel) 
	            {
	                for (Object cell : ((mxGraphSelectionModel)sender).getCells()) 
	                {
	                    debug ("cell=" + graph.getLabel(cell));
	                    	                    
						if (cell instanceof mxCell)
						{
							debug ("We've got a vertex cell here, configuring ...");
				
							mxCell aCell=(mxCell) cell;
												
							Object userObject=aCell.getValue();
											
							if (userObject instanceof String)
							{										
								debug ("This selection is a template, not an id");														
							}
							else
							{
								HoopVisualGraph vizGraph=(HoopVisualGraph) graph;
								
								HoopBase aHoop=vizGraph.cellToHoop(cell);
								
								HoopPropertyPanel propPanel=(HoopPropertyPanel) HoopLink.getWindow("Properties");
								if (propPanel!=null)
								{
									propPanel.highlightHoop (aHoop);
								}
							}
						}
	                }
	            }
	        }
	    });
	}
	/**
	 * 
	 */
	protected void installRepaintListener()
	{
		debug ("installRepaintListener ()");
		
		graphComponent.getGraph().addListener(mxEvent.REPAINT,new mxIEventListener()
		{
			public void invoke(Object source, mxEventObject evt)
			{
				String buffer = (graphComponent.getTripleBuffer() != null) ? "" : " (unbuffered)";
				mxRectangle dirty = (mxRectangle) evt.getProperty("region");

				if (dirty == null)
				{
					status("Repaint all" + buffer);
				}
				else
				{
					status("Repaint: x=" + (int) (dirty.getX()) + " y="	+ (int) (dirty.getY()) + " w=" + (int) (dirty.getWidth()) + " h=" + (int) (dirty.getHeight()) + buffer);
				}
			}
		});
	}
	/**
	 * 
	 */
	/*
	public HoopEditorPalette insertPalette(String title)
	{
		debug ("insertPalette ("+title+")");
		
		JTabbedPane libraryPane=getLibraryPane ();
		
		if (libraryPane==null)
			return (null);
		
		final HoopEditorPalette palette = new HoopEditorPalette();
		final JScrollPane scrollPane = new JScrollPane(palette);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		libraryPane.addTab (title, HoopLink.getImageByName("gtk-file.png"),scrollPane,title);
		//palette.setPreferredWidth(150);

		// Updates the widths of the palettes if the container size changes
		libraryPane.addComponentListener(new ComponentAdapter()
		{
			public void componentResized(ComponentEvent e)
			{
				int w = scrollPane.getWidth()-scrollPane.getVerticalScrollBar().getWidth();
				palette.setPreferredWidth(w);
			}

		});

		return palette;
	}
	*/
	/**
	 * 
	 */
	protected void processScale (double aScale)
	{
		debug ("processScale ("+aScale+")");
		
		// Implement in child class
	}
	/**
	 * 
	 */
	protected void showGraphPopupMenu (MouseEvent e)
	{
		debug ("showGraphPopupMenu ()");
		
		Point pt = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(),graphComponent);
		HoopEditorPopupMenu menu = new HoopEditorPopupMenu(HoopBasicGraphEditor.this);
		menu.show(graphComponent, pt.x, pt.y);

		e.consume();
	}
	/**
	 * 
	 */
	/*
	protected void mouseLocationChanged(MouseEvent e)
	{
		status(e.getX() + ", " + e.getY());
	}
	*/
	/**
	 * 
	 */
	protected void installListeners()
	{
		debug ("installListeners ()");
		
		graphComponent.addKeyListener(this);
		
		// Handles mouse wheel events in the outline and graph component
		/*
		graphOutline.addMouseWheelListener(wheelTracker);
		graphComponent.addMouseWheelListener(wheelTracker);

		// Installs the popup menu in the outline
		graphOutline.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				// Handles context menu on the Mac where the trigger is on mousepressed
				mouseReleased(e);
			}

			public void mouseReleased(MouseEvent e)
			{
				if (e.isPopupTrigger())
				{
					showOutlinePopupMenu(e);
				}
			}

		});
		*/

		graphComponent.addMouseWheelListener(this);
		
		// Installs the popup menu in the graph component		
		/*
		graphComponent.getGraphControl().addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				// Handles context menu on the Mac where the trigger is on mousepressed
				mouseReleased(e);
			}

			public void mouseReleased(MouseEvent e)
			{

				if (e.isPopupTrigger())
				{
					showGraphPopupMenu(e);
				}
			}
		});
		*/

		// Installs a mouse motion listener to display the mouse location
		graphComponent.getGraphControl().addMouseMotionListener (this);
		
		/*
		graphComponent.getGraphControl().addMouseMotionListener (new MouseMotionListener()
		{
			public void mouseDragged (MouseEvent e)
			{
				status(e.getX() + ", " + e.getY());
			}
			public void mouseMoved (MouseEvent e)
			{
				if (this.shiftDown==true)
				{
					status("Drag: " + e.getX() + ", " + e.getY());
				}
				else
					status(e.getX() + ", " + e.getY());
			}
		});
		*/
	}
	/**
	 * 
	 */
	public void setCurrentFile(File file)
	{
		File oldValue = currentFile;
		currentFile = file;

		firePropertyChange("currentFile", oldValue, file);
	}
	/**
	 * 
	 */
	public File getCurrentFile()
	{
		return currentFile;
	}
	/**
	 * 
	 * @param modified
	 */
	public void setModified(boolean modified)
	{
		boolean oldValue = this.modified;
		this.modified = modified;

		firePropertyChange("modified", oldValue, modified);
	}
	/**
	 * 
	 * @return whether or not the current graph has been modified
	 */
	public boolean isModified()
	{
		return modified;
	}
	/**
	 * 
	 */
	public mxGraphComponent getGraphComponent()
	{
		return graphComponent;
	}
	/**
	 * 
	 */
	public mxUndoManager getUndoManager()
	{
		return undoManager;
	}
	/**
	 * 
	 * @param name
	 * @param action
	 * @return a new Action bound to the specified string name
	 */
	public Action bind(String name, final Action action)
	{
		return bind(name, action, null);
	}
	/**
	 * 
	 * @param name
	 * @param action
	 * @return a new Action bound to the specified string name and icon
	 */
	@SuppressWarnings("serial")
	public Action bind(String name, final Action action, String iconUrl)
	{
		return new AbstractAction(name, (iconUrl != null) ? new ImageIcon(HoopBasicGraphEditor.class.getResource(iconUrl)) : null)
		{
			public void actionPerformed(ActionEvent e)
			{
				action.actionPerformed(new ActionEvent(getGraphComponent(), e.getID(), e.getActionCommand()));
			}
		};
	}
	/**
	 * 
	 * @param msg
	 */
	public void status(String msg)
	{
		HoopLink.statusBar.setStatus(msg);
	}
	/**
	 * 
	 */
	public void setLookAndFeel(String clazz)
	{
		JFrame frame = (JFrame) SwingUtilities.windowForComponent(this);

		if (frame != null)
		{
			try
			{
				UIManager.setLookAndFeel(clazz);
				SwingUtilities.updateComponentTreeUI(frame);

				// Needs to assign the key bindings again
				keyboardHandler = new HoopEditorKeyboardHandler(graphComponent);
			}
			catch (Exception e1)
			{
				e1.printStackTrace();
			}
		}
	}
	/**
	 * Creates an action that executes the specified layout.
	 * 
	 * @param key Key to be used for getting the label from mxResources and also
	 * to create the layout instance for the commercial graph editor example.
	 * @return an action that executes the specified layout
	 */
	@SuppressWarnings("serial")
	public Action graphLayout(final String key, boolean animate)
	{
		debug ("graphLayout ()");
		
		final mxIGraphLayout layout = createLayout(key, animate);

		if (layout != null)
		{
			return new AbstractAction(mxResources.get(key))
			{
				public void actionPerformed(ActionEvent e)
				{
					final mxGraph graph = graphComponent.getGraph();
					Object cell = graph.getSelectionCell();

					if (cell == null || graph.getModel().getChildCount(cell) == 0)
					{
						cell = graph.getDefaultParent();
					}

					graph.getModel().beginUpdate();
					try
					{
						long t0 = System.currentTimeMillis();
						layout.execute(cell);
						status("Layout: " + (System.currentTimeMillis() - t0) + " ms");
					}
					finally
					{
						mxMorphing morph = new mxMorphing(graphComponent,20,1.2,20);

						morph.addListener(mxEvent.DONE, new mxIEventListener()
						{
							public void invoke(Object sender, mxEventObject evt)
							{
								graph.getModel().endUpdate();
							}

						});

						morph.startAnimation();
					}
				}
			};
		}
		else
		{
			return new AbstractAction(mxResources.get(key))
			{
				public void actionPerformed(ActionEvent e)
				{
					JOptionPane.showMessageDialog(graphComponent,mxResources.get("noLayout"));
				}
			};
		}
	}
	/**
	 * Creates a layout instance for the given identifier.
	 */
	protected mxIGraphLayout createLayout(String ident, boolean animate)
	{
		debug ("createLayout ()");
		
		mxIGraphLayout layout = null;

		if (ident != null)
		{
			mxGraph graph = graphComponent.getGraph();

			if (ident.equals("verticalHierarchical"))
			{
				layout = new mxHierarchicalLayout(graph);
			}
			else if (ident.equals("horizontalHierarchical"))
			{
				layout = new mxHierarchicalLayout(graph, JLabel.WEST);
			}
			else if (ident.equals("verticalTree"))
			{
				layout = new mxCompactTreeLayout(graph, false);
			}
			else if (ident.equals("horizontalTree"))
			{
				layout = new mxCompactTreeLayout(graph, true);
			}
			else if (ident.equals("parallelEdges"))
			{
				layout = new mxParallelEdgeLayout(graph);
			}
			else if (ident.equals("placeEdgeLabels"))
			{
				layout = new mxEdgeLabelLayout(graph);
			}
			else if (ident.equals("organicLayout"))
			{
				layout = new mxOrganicLayout(graph);
			}
			if (ident.equals("verticalPartition"))
			{
				layout = new mxPartitionLayout(graph, false)
				{
					/**
					 * Overrides the empty implementation to return the size of the
					 * graph control.
					 */
					public mxRectangle getContainerSize()
					{
						return graphComponent.getLayoutAreaSize();
					}
				};
			}
			else if (ident.equals("horizontalPartition"))
			{
				layout = new mxPartitionLayout(graph, true)
				{
					/**
					 * Overrides the empty implementation to return the size of the
					 * graph control.
					 */
					public mxRectangle getContainerSize()
					{
						return graphComponent.getLayoutAreaSize();
					}
				};
			}
			else if (ident.equals("verticalStack"))
			{
				layout = new mxStackLayout(graph, false)
				{
					/**
					 * Overrides the empty implementation to return the size of the
					 * graph control.
					 */
					public mxRectangle getContainerSize()
					{
						return graphComponent.getLayoutAreaSize();
					}
				};
			}
			else if (ident.equals("horizontalStack"))
			{
				layout = new mxStackLayout(graph, true)
				{
					/**
					 * Overrides the empty implementation to return the size of the
					 * graph control.
					 */
					public mxRectangle getContainerSize()
					{
						return graphComponent.getLayoutAreaSize();
					}
				};
			}
			else if (ident.equals("circleLayout"))
			{
				layout = new mxCircleLayout(graph);
			}
		}

		return layout;
	}
	/**
	 * 
	 * @param aKey
	 */
	@Override
	public void keyPressed(KeyEvent aKey) 
	{
		//debug ("keyPressed ("+aKey.isShiftDown()+")");
		
		shiftDown=aKey.isShiftDown();
		
		
		if (shiftDown==true)
		{
			rubberband.setEnabled(true);
			graphComponent.getSelectionCellsHandler().setEnabled(true);
			graphComponent.getSelectionCellsHandler().setVisible(true);
		}
		else
			rubberband.setEnabled(false);
	}
	/**
	 * 
	 * @param aKey
	 */
	@Override
	public void keyReleased(KeyEvent aKey) 
	{
		//debug ("keyReleased ("+aKey.isShiftDown()+")");
		
		shiftDown=aKey.isShiftDown();
		
		rubberband.setEnabled(false);
		
		graphComponent.getSelectionCellsHandler().setEnabled(false);
		graphComponent.getSelectionCellsHandler().setVisible(false);		
	}
	/**
	 * 
	 * @param aKey
	 */
	@Override
	public void keyTyped(KeyEvent aKey) 
	{
		//debug ("keyTyped ()");
		
	}
	/**
	 * 
	 */
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		debug ("mouseWheelMoved ()");
		
		if (e.getWheelRotation()<0)
		{
			graphComponent.zoomIn();
		}
		else
		{
			graphComponent.zoomOut();
		}

		status ("Scale: " + (int) (100*graphComponent.getGraph ().getView ().getScale ()) + "%");
		
		processScale (graphComponent.getGraph ().getView ().getScale ());
	}	
	/**
	 * 
	 */
	@Override
	public void mouseDragged(MouseEvent e) 
	{		
		if (this.shiftDown==true)
		{			
			status("S: "+ graphComponent.getGraph ().getView ().getScale () + " Drag (D): " + e.getX() + ", " + e.getY());
		}
		else
			status(e.getX() + ", " + e.getY());		
	}
	/**
	 * 
	 */
	@Override
	public void mouseMoved(MouseEvent e) 
	{
		if (this.shiftDown==true)
		{
			status("S: "+ graphComponent.getGraph ().getView ().getScale () + " Drag (M): " + e.getX() + ", " + e.getY());
		}
		else
			status("S: "+ graphComponent.getGraph ().getView ().getScale () + ", "+ e.getX() + ", " + e.getY());		
	}
	/**
	 * 
	 */
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		debug ("actionPerformed ()");
		
		if (e.getSource()==annotateToggle)
		{
			debug ("Toggling annotation view ...");
			
			if (annotateToggle.isSelected()==true)
			{				
				graphContainer.setLocked(true);
			}
			else
			{
				graphContainer.setLocked(false);
			}
		}
		
		if (e.getSource ()==speedButton)
		{
			HoopExecuteInEditor execution=(HoopExecuteInEditor) HoopLink.runner;
			
			if (execution.getExecuteSpeed ()==HoopExecuteInEditor.SPEED_SLOW)
			{
				execution.setExecuteSpeed(HoopExecuteInEditor.SPEED_NORMAL);
				speedButton.setIcon(HoopLink.getImageByName("speed-2.png"));
				return;
			}
			
			if (execution.getExecuteSpeed ()==HoopExecuteInEditor.SPEED_NORMAL)
			{
				execution.setExecuteSpeed(HoopExecuteInEditor.SPEED_FAST);
				speedButton.setIcon(HoopLink.getImageByName("speed-3.png"));
				return;
			}
			
			if (execution.getExecuteSpeed ()==HoopExecuteInEditor.SPEED_FAST)
			{
				execution.setExecuteSpeed(HoopExecuteInEditor.SPEED_SLOW);
				speedButton.setIcon(HoopLink.getImageByName("speed-1.png"));
				return;
			}			
		}
		
		if (e.getSource()==clearBreakPoints)
		{
			debug ("Clearing breakpoints ...");
			
			Object[] options = {"Ok","Cancel"};
			int n = JOptionPane.showOptionDialog (this,
												  "Are you sure you want to clear all breakpoints?",
													"Hoop Info Panel",
													JOptionPane.OK_CANCEL_OPTION,
													JOptionPane.QUESTION_MESSAGE,
													null,
													options,
													options[1]);
			if (n==0)
			{
				// Clear all breakpoints ...
				
			}
			
		}
		
		if (e.getSource()==zoomIn)
		{
			
			graphComponent.zoomIn();
			
			
		}
		if (e.getSource()==zoomOut)
		{
			graphComponent.zoomOut();
			
		}
	}
}

