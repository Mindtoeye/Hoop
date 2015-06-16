package com.mxgraph.examples.swing;

import java.util.ArrayList;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.view.mxInteractiveCanvas;
import com.mxgraph.util.mxPoint;
import com.mxgraph.view.mxCellState;

import edu.cmu.cs.in.base.HoopProperties;
import edu.cmu.cs.in.base.HoopRoot;
import edu.cmu.cs.in.hoop.editor.HoopNodePanelRenderer;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;

/**
 * 
 */
public class HoopJGraphPanel extends mxInteractiveCanvas
{
	protected mxGraphComponent graphComponent;
	protected HoopNodePanelRenderer panelRenderer=null;
	
	public static int PORT_DIAMETER = 12;
	public static int PORT_RADIUS = PORT_DIAMETER / 2;

	/**
	 * 
	 * @param graphComponent
	 */
	public HoopJGraphPanel(mxGraphComponent graphComponent)
	{
		HoopRoot.debug ("HoopJGraphPanel","HoopJGraphPanel ()");
		
		this.graphComponent = graphComponent;
						
		panelRenderer=new HoopNodePanelRenderer (graphComponent);
	}
	/**
	 * 
	 * @return
	 */
	public mxCell createHoopRenderer (HoopBase aHoop,int anX,int anY)
	{
		HoopRoot.debug ("HoopJGraphPanel","createHoopRenderer ()");
		
		Object parent = graphComponent.getGraph().getDefaultParent();
		
		mxCell cell = (mxCell) graphComponent.getGraph().insertVertex(parent, null, "", anX, anY, HoopProperties.hoopDefaultWidth, HoopProperties.hoopDefaultHeight, "");
		cell.setConnectable(false);

		ArrayList <String> inPorts=aHoop.getInPorts();
		ArrayList <String> outPorts=aHoop.getOutPorts();
		
		for (int i=0;i<inPorts.size();i++)
		{
			mxGeometry geo = new mxGeometry(0.1, 0.5, PORT_DIAMETER, PORT_DIAMETER);
			// Because the origin is at upper left corner, need to translate to
			// position the center of port correctly
			geo.setOffset(new mxPoint(-PORT_RADIUS, -PORT_RADIUS));
			geo.setRelative(true);

			mxCell inPort = new mxCell(null, geo,"shape=ellipse;perimter=ellipsePerimeter");
			inPort.setVertex(true);
			
			graphComponent.getGraph().addCell (inPort,cell);
		}
		
		for (int j=0;j<outPorts.size();j++)
		{
			mxGeometry geo = new mxGeometry(0.9, 0.5, PORT_DIAMETER, PORT_DIAMETER);
			// Because the origin is at upper left corner, need to translate to
			// position the center of port correctly
			geo.setOffset(new mxPoint(-PORT_RADIUS, -PORT_RADIUS));
			geo.setRelative(true);

			mxCell inPort = new mxCell(null, geo,"shape=ellipse;perimter=ellipsePerimeter");
			inPort.setVertex(true);
			
			graphComponent.getGraph().addCell (inPort,cell);
		}		
		
		/*
		//>-------------------------------------------------------------------------
		
		mxGeometry geo1 = new mxGeometry(0.1, 0.5, PORT_DIAMETER,	PORT_DIAMETER);
		// Because the origin is at upper left corner, need to translate to
		// position the center of port correctly
		geo1.setOffset(new mxPoint(-PORT_RADIUS, -PORT_RADIUS));
		geo1.setRelative(true);

		mxCell port1 = new mxCell(null, geo1,"shape=ellipse;perimter=ellipsePerimeter");
		port1.setVertex(true);

		//>-------------------------------------------------------------------------
		
		mxGeometry geo2 = new mxGeometry(0.9, 0.3, PORT_DIAMETER,PORT_DIAMETER);
		geo2.setOffset(new mxPoint(-PORT_RADIUS, -PORT_RADIUS));
		geo2.setRelative(true);

		mxCell port2 = new mxCell(null, geo2,"shape=ellipse;perimter=ellipsePerimeter");
		port2.setVertex(true);
		
		//>-------------------------------------------------------------------------
		
		mxGeometry geo3 = new mxGeometry(0.9, 0.6, PORT_DIAMETER,PORT_DIAMETER);
		geo3.setOffset(new mxPoint(-PORT_RADIUS, -PORT_RADIUS));
		geo3.setRelative(true);

		mxCell port3 = new mxCell(null, geo3,"shape=ellipse;perimter=ellipsePerimeter");
		port3.setVertex(true);			

		graphComponent.getGraph().addCell (port1,cell);
		graphComponent.getGraph().addCell (port2,cell);
		graphComponent.getGraph().addCell (port3,cell);
		*/
		
		return (cell);
	}
	/**
	 * 
	 * @param state
	 * @param label
	 */
	public void drawHoop (mxCellState state, String label)
	{
		panelRenderer.drawHoop(g, state);
				
		/*
		rendererPane.paintComponent(g, 
									vertexRenderer,
									graphComponent,
									(int) state.getX() + translate.x, 
									(int) state.getY() + translate.y,
									(int) state.getWidth(),
									(int) state.getHeight(), 
									true);
		 */		
	}
}
