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

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;

//import org.w3c.dom.Document;

//import com.mxgraph.io.mxCodec;
import com.mxgraph.model.mxICell;
import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.swing.mxGraphComponent;
//import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraph;

//import edu.cmu.cs.in.hoop.INHoopGraphEditor;
import edu.cmu.cs.in.base.INBase;
import edu.cmu.cs.in.hoop.editor.INHoopNodeRenderer;

/**
 * 
 */
public class INHoopVisualGraphComponent extends mxGraphComponent
{
	private static final long serialVersionUID = -1L;

	/**
	 * 
	 * @param graph
	 */
	public INHoopVisualGraphComponent (mxGraph graph)
	{		
		super(graph);
		
		debug ("INHoopVisualGraphComponent ()");

		setConnectable(true);
		
		getGraph().setCellsResizable(false);
		getGraph ().setAllowDanglingEdges(false);
		getGraphHandler().setCloneEnabled(false);
		getGraphHandler().setImagePreview(false);		
		
		setGridVisible(false);
		setToolTips(true);
		getConnectionHandler().setCreateTarget(true);

		// Loads the defualt stylesheet from an external file
		/*
		mxCodec codec = new mxCodec();
		Document doc = mxUtils.loadDocument(INHoopGraphEditor.class.getResource("/assets/resources/default-style.xml").toString());
		codec.decode(doc.getDocumentElement(), graph.getStylesheet());
		*/

		getViewport().setOpaque(true);
		getViewport().setBackground(new Color (47,46,47));
	}
	/**
	 * 
	 */
	protected void debug (String aMessage)
	{
		INBase.debug ("INHoopVisualGraphComponent",aMessage);
	}
	/**
	 * Overrides drop behaviour to set the cell style if the target
	 * is not a valid drop target and the cells are of the same
	 * type (eg. both vertices or both edges). 
	 */
	public Object[] importCells (Object[] cells, 
								 double dx, 
								 double dy,
								 Object target,
								 Point location)
	{
		debug ("importCells ()");
		
		if (target == null && cells.length == 1 && location != null)
		{
			target = getCellAt(location.x, location.y);

			if (target instanceof mxICell && cells[0] instanceof mxICell)
			{
				mxICell targetCell = (mxICell) target;
				mxICell dropCell = (mxICell) cells[0];

				if (targetCell.isVertex() == dropCell.isVertex() || targetCell.isEdge() == dropCell.isEdge())
				{
					mxIGraphModel model = graph.getModel();
					model.setStyle(target, model.getStyle(cells[0]));
					graph.setSelectionCell(target);

					return (null);
				}
			}
		}

		return super.importCells(cells, dx, dy, target, location);
	}
	/**
	 * 
	 */
	public Component[] createComponents(mxCellState state)
	{
		debug ("createComponents ()");
		
		if (getGraph().getModel().isVertex(state.getCell()))
		{
			return new Component[] 
			{ 
				new INHoopNodeRenderer(state.getCell(), this) 
			};
		}

		return (null);
	}	
}
