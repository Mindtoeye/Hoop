package com.mxgraph.examples.swing;

import javax.swing.JFrame;

import com.mxgraph.canvas.mxICanvas;
import com.mxgraph.canvas.mxImageCanvas;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.handler.mxRubberband;
import com.mxgraph.swing.view.mxInteractiveCanvas;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraph;

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.hoop.hoops.base.HoopLoadBase;
import edu.cmu.cs.in.hoop.hoops.transform.HoopSentence2Tokens;

/**
 * 
 */
public class HoopCustomCanvas extends JFrame
{
	private static final long serialVersionUID = -844106998814982739L;
		
	private mxGraphComponent graphComponent=null;
	private HoopJGraphPanel graphPanel=null;

	/**
	 * 
	 */
	public HoopCustomCanvas()
	{
		super("Custom Canvas");

		mxGraph graph = new mxGraph()
		{
			/**
			 *  Removes the folding icon and disables any folding
			 */			
			public boolean isCellFoldable(Object cell, boolean collapse)
			{
				return false;
			}
			
			/**
			 * 
			 */
			public void drawState(mxICanvas canvas, mxCellState state,boolean drawLabel)
			{
				String label = (drawLabel) ? state.getLabel() : "";

				// Indirection for wrapped swing canvas inside image canvas (used for creating
				// the preview image when cells are dragged)
				if (getModel().isVertex(state.getCell()) && 
					canvas instanceof mxImageCanvas	&& 
					((mxImageCanvas) canvas).getGraphicsCanvas() instanceof HoopJGraphPanel)
				{
					((HoopJGraphPanel) ((mxImageCanvas) canvas).getGraphicsCanvas()).drawHoop(state, label);
				}
				// Redirection of drawing vertices in SwingCanvas
				else if (getModel().isVertex(state.getCell()) && canvas instanceof HoopJGraphPanel)
				{
					((HoopJGraphPanel) canvas).drawHoop(state, label);
				}
				else
				{
					super.drawState(canvas, state, drawLabel);
				}
			}
		};
		
		graphComponent = new mxGraphComponent(graph)
		{
			private static final long serialVersionUID = 4683716829748931448L;

			public mxInteractiveCanvas createCanvas()
			{
				graphPanel=new HoopJGraphPanel(this);
				
				return graphPanel;
			}
		};		
		
		graph.setAllowDanglingEdges(false);
		graph.setAllowLoops(false);
		
		graph.getModel().beginUpdate();
		
		graphPanel.createHoopRenderer (new HoopLoadBase (),10,10);
		
		graphPanel.createHoopRenderer (new HoopSentence2Tokens (),250,150);

		graph.getModel().endUpdate();

		getContentPane().add(graphComponent);

		new mxRubberband(graphComponent);
	}
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
		/**
		 * We want to make sure all images we might need are loaded because for example
		 * some Hoops created below in the HoopManager might use visual resources. Of
		 * course this will have to completely change for cluster usage.
		 */
    	HoopLink.loadImageIcons ();	
    	
    	HoopCustomCanvas frame = new HoopCustomCanvas();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(550, 450);
		frame.setVisible(true);
	}
}
