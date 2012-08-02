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

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JViewport;

import com.mxgraph.model.mxCell;
//import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.handler.mxConnectionHandler;
//import com.mxgraph.swing.handler.mxRubberband;
import com.mxgraph.swing.view.mxInteractiveCanvas;
//import com.mxgraph.util.mxPoint;
//import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraph;

import edu.cmu.cs.in.base.HoopRoot;
import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.base.HoopProperties;
import edu.cmu.cs.in.controls.HoopJViewport;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.hoop.hoops.base.HoopConnection;

/**
 * 
 */
public class HoopVisualGraphComponent extends mxGraphComponent
{
	private static final long serialVersionUID = -1L;
	
	final int PORT_DIAMETER = 20;
	final int PORT_RADIUS = PORT_DIAMETER / 2;		

	private HoopJViewport viewport=new HoopJViewport ();
	private Boolean useCustomViewport=false;
	
	/**
	 * 
	 * @param graph
	 */
	public HoopVisualGraphComponent (mxGraph graph)
	{		
		super(graph);
		
		debug ("HoopVisualGraphComponent ()");

		setConnectable(true);
		
		getGraphHandler().setCloneEnabled(false);
		getGraphHandler().setImagePreview(false);		
		
		setGridVisible(false);
		setToolTips(true);
		//getConnectionHandler().setCreateTarget(true);

		// Loads the default stylesheet from an external file
		/*
		mxCodec codec = new mxCodec();
		Document doc = mxUtils.loadDocument(HoopGraphEditor.class.getResource("/assets/resources/default-style.xml").toString());
		codec.decode(doc.getDocumentElement(), graph.getStylesheet());
		*/
		
		if (useCustomViewport==true)
		{
			viewport.setOpaque(true);
			this.setViewport(viewport);
			//this.setViewportView(viewport);
		}
		else
		{
			JViewport canvas=this.getViewport();		
			canvas.setBackground(HoopProperties.graphBackgroundColor);
		}	
	}
	/**
	 * 
	 */
	/*
	public JViewport getViewport ()
	{
		if (useCustomViewport==true)
		{
			return (viewport);
		}

		return (super.getViewport());
		
		return (viewport);
	}
	*/
	/** 
	 * @return
	 */
	public Boolean getUseCustomViewport() 
	{
		return useCustomViewport;
	}
	/**
	 * 
	 * @param useCustomViewport
	 */
	public void setUseCustomViewport(Boolean useCustomViewport) 
	{
		this.useCustomViewport = useCustomViewport;
	}	
	/**
	 * 
	 */
	protected void debug (String aMessage)
	{
		HoopRoot.debug ("HoopVisualGraphComponent",aMessage);
	}
	/**
	 * 
	 */
	protected mxConnectionHandler createConnectionHandler()
	{
		debug ("createConnectionHandler()");
		
		return (new HoopVisualGraphConnectionHandler (this));
	}
	/**
	 * 
	 */
	public mxInteractiveCanvas createCanvas()
	{
		debug ("createCanvas ()");
		
		return new HoopVisualGraphCanvas (this);
	}	
	/**
	 * 
	 */
	public Component[] createComponents (mxCellState state)
	{
		debug ("createComponents ()");		
		
		if (state.getCell()!=null)
		{
			//debug ("Processing createComponents for cell: " + state.getCell().toString());
			
			// >---------------------------------------------------------------
			
			if (getGraph().getModel().isVertex(state.getCell()))
			{			
				debug ("Cell represents a vertex");
				
				Component [] createdPanels=new Component [1];
			
				Object aCell=state.getCell();
			
				if (aCell!=null)
				{				
					if (aCell instanceof mxCell)
					{
						debug ("We've got a vertex cell here, configuring ...");
			
						mxCell cell=(mxCell) aCell;
					
						//cell.setConnectable(false);
					
						Object userObject=cell.getValue();
										
						if (userObject instanceof String)
						{										
							String templateName=(String) userObject;
						
							debug ("User object: " + templateName);
											
							HoopBase hoopTemplate=HoopLink.hoopManager.instantiate (templateName);
						
							cell.setValue(hoopTemplate);
						
							hoopTemplate.setGraphCellReference(cell);
												
							HoopLink.hoopGraphManager.addHoop (hoopTemplate);
						
							HoopNodePanel aPanel=new HoopNodePanel (hoopTemplate,cell, this);					
						
							hoopTemplate.setVisualizer (aPanel);
																	
							createdPanels [0]=aPanel;
																		
							/*
							graph.getModel().beginUpdate();
						
							try
							{
								mxGeometry geo = graph.getModel().getGeometry(cell);
								// The size of the rectangle when the minus sign is clicked
								geo.setAlternateBounds(new mxRectangle(20, 20, 100, 50));

								mxGeometry geo1 = new mxGeometry(0, 0.5, PORT_DIAMETER,	PORT_DIAMETER);
								// Because the origin is at upper left corner, need to translate to
								// position the center of port correctly
								geo1.setOffset(new mxPoint(-PORT_RADIUS, -PORT_RADIUS));
								geo1.setRelative(true);

								mxCell port1 = new mxCell(null, geo1, "shape=ellipse;perimter=ellipsePerimeter");
								port1.setVertex(true);
								port1.setConnectable(false);

								mxGeometry geo2 = new mxGeometry(1.0, 0.5, PORT_DIAMETER,	PORT_DIAMETER);
								geo2.setOffset(new mxPoint(-PORT_RADIUS, -PORT_RADIUS));
								geo2.setRelative(true);
							
								mxCell port2 = new mxCell(null, geo2,"shape=ellipse;perimter=ellipsePerimeter");
								port2.setVertex(true);
								port2.setConnectable(false);

								graph.addCell(port1,cell);
								graph.addCell(port2,cell);
							}
							finally
							{
								graph.getModel().endUpdate();
							}
							 */							
		    				        											
							return (createdPanels);
						}
						else
						{
							if (userObject instanceof HoopBase)
							{
								debug ("Assigning pre-configured Hoop to new vertex and panel ...");
							
								HoopBase aHoop=(HoopBase) userObject;
							
								cell.setValue(aHoop);
							
								aHoop.setGraphCellReference(cell);
																				
								HoopNodePanel aPanel=new HoopNodePanel (aHoop,cell, this);
								aPanel.setLocation(aHoop.getX(),aHoop.getY());
								aPanel.setPreferredSize(new Dimension (aHoop.getWidth(),aHoop.getHeight()));
							
								aHoop.setVisualizer (aPanel);
																		
								createdPanels [0]=aPanel;		
							
								return (createdPanels);
							}
							else
								debug ("Transferable object is not a hoop template string nor a hoop template");
						}
					}									
				}
				else
					debug ("Error: cell state doesn't contain a cell object");
			}

			// >---------------------------------------------------------------
			
			if (getGraph().getModel().isEdge(state.getCell()))
			{
				debug ("Cell represents an edge");
				
				Object aCell=state.getCell();
				
				if (aCell!=null)
				{				
					if (aCell instanceof mxCell)
					{
						debug ("We've got an edge cell here, configuring ...");
						
						mxCell cell=(mxCell) aCell;
												
						if ((cell.getSource()!=null) && (cell.getTarget()!=null))
						{						
							Object aSource=cell.getSource().getValue();
							
							Object aTarget=cell.getTarget().getValue();
							
							//debug ("We have an edge cell with id: " + cell.getId() + " ("+aSource.toString()+" -> " + aTarget.toString()+")");
							
							HoopBase sourceHoop=(HoopBase) aSource;
							HoopBase targetHoop=(HoopBase) aTarget;
							
							if ((sourceHoop!=null) && (targetHoop!=null))
							{
								debug ("We have a valid source and target hoop, linking them together ...");

								HoopConnection newConnection=HoopLink.hoopGraphManager.connectHoops(sourceHoop, targetHoop);
								
								if (newConnection==null)
								{				
									debug ("Error creating edge");
								}
								else
								{
									debug ("Edge created");
									
									newConnection.setGraphCellReference(cell);
									
									sourceHoop.propagateVisualProperties ();
									targetHoop.propagateVisualProperties ();
									
									//cell.setValue (newConnection);
								}
							}			
							else
								debug ("Info: no complete edge yet, waiting ...");									
						}
					}
				}	
			}
			
			// >---------------------------------------------------------------
		}
		else
			debug ("Cell object is null");
		
		return (null);
	}	
}
