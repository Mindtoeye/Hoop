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

import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraph;

import edu.cmu.cs.in.base.INBase;
import edu.cmu.cs.in.base.INHoopLink;
import edu.cmu.cs.in.base.INHoopProperties;
import edu.cmu.cs.in.hoop.base.INHoopBase;

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

		// Loads the default stylesheet from an external file
		/*
		mxCodec codec = new mxCodec();
		Document doc = mxUtils.loadDocument(INHoopGraphEditor.class.getResource("/assets/resources/default-style.xml").toString());
		codec.decode(doc.getDocumentElement(), graph.getStylesheet());
		*/

		getViewport().setOpaque(true);
		getViewport().setBackground(INHoopProperties.graphBackgroundColor);
	}
	/**
	 * 
	 */
	protected void debug (String aMessage)
	{
		INBase.debug ("INHoopVisualGraphComponent",aMessage);
	}
	/**
	 * 
	 */
	public Component[] createComponents(mxCellState state)
	{
		debug ("createComponents ("+state+")");
		
		debug ("Cell: " + state.getCell());
		
		if (getGraph().getModel().isVertex(state.getCell()))
		{
			debug ("Returning valid INHoopNodePanel for vertex cell...");
			
			Component [] createdPanels=new Component [1];
			
			Object aCell=state.getCell();
			
			if (aCell!=null)
			{
				if (aCell instanceof mxCell)
				{
					debug ("We've got a cell here");
			
					mxCell cell=(mxCell) aCell;
					
					Object userObject=cell.getValue();
										
					if (userObject instanceof String)
					{										
						String templateName=(String) userObject;
						
						debug ("User object: " + templateName);
											
						INHoopBase hoopTemplate=INHoopLink.hoopManager.instantiate (templateName);
						
						if (INHoopLink.hoopGraphManager.getRoot()==null)
							INHoopLink.hoopGraphManager.setRoot(hoopTemplate);
																	
						createdPanels [0]=new INHoopNodePanel (hoopTemplate,cell, this);
			
						return (createdPanels);
					}
					else
						debug ("Transferable object is not a hoop template string");
				}
				else
					debug ("Cell object is not of type mxCell");
			}
			else
				debug ("Error: cell state doesn't contain a cell object");			
		}
		
		return (null);
	}	
}
