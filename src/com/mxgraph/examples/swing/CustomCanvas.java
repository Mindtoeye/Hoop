package com.mxgraph.examples.swing;

import javax.swing.JFrame;

import com.mxgraph.canvas.mxICanvas;
import com.mxgraph.canvas.mxImageCanvas;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.handler.mxRubberband;
import com.mxgraph.swing.view.mxInteractiveCanvas;
import com.mxgraph.util.mxPoint;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraph;

/**
 * 
 */
public class CustomCanvas extends JFrame
{
	private static final long serialVersionUID = -844106998814982739L;
	
	final int PORT_DIAMETER = 10;
	final int PORT_RADIUS = PORT_DIAMETER / 2;	

	/**
	 * 
	 */
	public CustomCanvas()
	{
		super("Custom Canvas");

		// Demonstrates the use of a Swing component for rendering vertices.
		// Note: Use the heavyweight feature to allow for event handling in
		// the Swing component that is used for rendering the vertex.

		mxGraph graph = new mxGraph()
		{
			public void drawState(mxICanvas canvas, mxCellState state,boolean drawLabel)
			{
				String label = (drawLabel) ? state.getLabel() : "";

				// Indirection for wrapped swing canvas inside image canvas (used for creating
				// the preview image when cells are dragged)
				if (getModel().isVertex(state.getCell())
						&& canvas instanceof mxImageCanvas
						&& ((mxImageCanvas) canvas).getGraphicsCanvas() instanceof HoopJGraphPanel)
				{
					((HoopJGraphPanel) ((mxImageCanvas) canvas).getGraphicsCanvas()).drawVertex(state, label);
				}
				// Redirection of drawing vertices in SwingCanvas
				else if (getModel().isVertex(state.getCell()) && canvas instanceof HoopJGraphPanel)
				{
					((HoopJGraphPanel) canvas).drawVertex(state, label);
				}
				else
				{
					super.drawState(canvas, state, drawLabel);
				}
			}
		};

		Object parent = graph.getDefaultParent();

		graph.getModel().beginUpdate();
		
		try
		{
			mxCell v1 = (mxCell) graph.insertVertex(parent, null, "Hello", 20,20, 100, 100, "");
			v1.setConnectable(false);
			mxGeometry geo = graph.getModel().getGeometry(v1);
			// The size of the rectangle when the minus sign is clicked
			geo.setAlternateBounds(new mxRectangle(20, 20, 100, 50));

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

			graph.addCell (port1,v1);
			graph.addCell (port2,v1);
			graph.addCell (port3,v1);

			//>-------------------------------------------------------------------------
			
			Object v2 = graph.insertVertex(parent, null, "World!", 240, 150, 80, 30);
			
			graph.insertEdge(parent, null, "Edge", port2, v2);
		}
		finally
		{
			graph.getModel().endUpdate();
		}

		mxGraphComponent graphComponent = new mxGraphComponent(graph)
		{
			private static final long serialVersionUID = 4683716829748931448L;

			public mxInteractiveCanvas createCanvas()
			{
				return new HoopJGraphPanel(this);
			}
		};

		getContentPane().add(graphComponent);

		// Adds rubberband selection
		new mxRubberband(graphComponent);
	}
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
		CustomCanvas frame = new CustomCanvas();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400, 320);
		frame.setVisible(true);
	}

}
