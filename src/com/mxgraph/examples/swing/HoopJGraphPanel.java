package com.mxgraph.examples.swing;

import javax.swing.CellRendererPane;
import javax.swing.border.Border;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.view.mxInteractiveCanvas;
import com.mxgraph.view.mxCellState;

import edu.cmu.cs.in.hoop.editor.HoopNodeRenderer;

/**
 * 
 */
public class HoopJGraphPanel extends mxInteractiveCanvas
{
	protected CellRendererPane rendererPane = new CellRendererPane();
	//protected JLabel vertexRenderer = null;
	protected HoopNodeRenderer vertexRenderer = null;
	protected mxGraphComponent graphComponent;
	
	protected Border blackborder=null;
	protected Border redborder=null;	
	protected Border raisedRed=null;	

	/**
	 * 
	 * @param graphComponent
	 */
	public HoopJGraphPanel(mxGraphComponent graphComponent)
	{
		this.graphComponent = graphComponent;
		
		vertexRenderer=new HoopNodeRenderer (null,null);

		/*
		vertexRenderer = new JLabel();

		blackborder=BorderFactory.createLineBorder(Color.black);
		redborder=BorderFactory.createLineBorder(Color.red);
		raisedRed=BorderFactory.createBevelBorder(BevelBorder.RAISED,Color.red,Color.red);

		vertexRenderer.setLayout(new BorderLayout());
		vertexRenderer.setBorder(BorderFactory.createCompoundBorder(HoopShadowBorder.getSharedInstance(), BorderFactory.createBevelBorder(BevelBorder.RAISED)));		
		
		vertexRenderer.setHorizontalAlignment(JLabel.CENTER);
		vertexRenderer.setBackground(graphComponent.getBackground().darker());
		vertexRenderer.setOpaque(true);
		*/
	}
	/**
	 * 
	 * @param state
	 * @param label
	 */
	public void drawVertex(mxCellState state, String label)
	{		
		// TODO: Configure other properties...

		rendererPane.paintComponent(g, 
									vertexRenderer,
									graphComponent,
									(int) state.getX() + translate.x, 
									(int) state.getY() + translate.y,
									(int) state.getWidth(),
									(int) state.getHeight(), 
									true);
	}
}
