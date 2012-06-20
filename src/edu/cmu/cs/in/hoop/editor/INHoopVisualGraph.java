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

import java.text.NumberFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mxgraph.canvas.mxICanvas;
import com.mxgraph.canvas.mxImageCanvas;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxPoint;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxEdgeStyle;
import com.mxgraph.view.mxGraph;
import com.mxgraph.model.mxCell;

import edu.cmu.cs.in.base.INBase;
import edu.cmu.cs.in.base.INHoopLink;
import edu.cmu.cs.in.hoop.base.INHoopBase;

/** 
 * @author vvelsen
 * A graph that creates new edges from a given template edge.
 */
public class INHoopVisualGraph extends mxGraph
{	
	public static final NumberFormat numberFormat = NumberFormat.getInstance();
	
	/// Holds the edge to be used as a template for inserting new edges.
	protected Object edgeTemplate;

	/**
	 * Custom graph that defines the alternate edge style to be used when
	 * the middle control point of edges is double clicked (flipped).
	 */
	public INHoopVisualGraph()
	{
		setAllowDanglingEdges(false);
		setCellsCloneable(false);		
		setAllowLoops (false);
		setDisconnectOnMove (false);
		
		//setAlternateEdgeStyle("edgeStyle=mxEdgeStyle.ElbowConnector;elbow=vertical");
		
		// Sets the default edge style
		Map<String, Object> style = this.getStylesheet().getDefaultEdgeStyle();
		style.put(mxConstants.STYLE_EDGE, mxEdgeStyle.ElbowConnector);		
	}
	/**
	 * 
	 */
	private void debug (String aMessage)
	{
		INBase.debug("INHoopVisualGraph",aMessage);
	}
	/**
	 * Sets the edge template to be used to inserting edges.
	 */
	public void setEdgeTemplate(Object template)
	{
		edgeTemplate = template;
	}
	/**
	 * Prints out some useful information about the cell in the tooltip.
	 */
	public String getToolTipForCell(Object cell)
	{
		String tip = "<html>";
		mxGeometry geo = getModel().getGeometry(cell);
		mxCellState state = getView().getState(cell);

		if (getModel().isEdge(cell))
		{
			tip += "points={";

			if (geo != null)
			{
				List<mxPoint> points = geo.getPoints();

				if (points != null)
				{
					Iterator<mxPoint> it = points.iterator();

					while (it.hasNext())
					{
						mxPoint point = it.next();
						tip += "[x=" + numberFormat.format(point.getX())
								+ ",y=" + numberFormat.format(point.getY())
								+ "],";
					}

					tip = tip.substring(0, tip.length() - 1);
				}
			}

			tip += "}<br>";
			tip += "absPoints={";

			if (state != null)
			{
				for (int i = 0; i < state.getAbsolutePointCount(); i++)
				{
					mxPoint point = state.getAbsolutePoint(i);
					tip += "[x=" + numberFormat.format(point.getX())
							+ ",y=" + numberFormat.format(point.getY())
							+ "],";
				}

				tip = tip.substring(0, tip.length() - 1);
			}

			tip += "}";
		}
		else
		{
			tip += "geo=[";

			if (geo != null)
			{
				tip += "x=" + numberFormat.format(geo.getX()) + ",y="
						+ numberFormat.format(geo.getY()) + ",width="
						+ numberFormat.format(geo.getWidth()) + ",height="
						+ numberFormat.format(geo.getHeight());
			}

			tip += "]<br>";
			tip += "state=[";

			if (state != null)
			{
				tip += "x=" + numberFormat.format(state.getX()) + ",y="
						+ numberFormat.format(state.getY()) + ",width="
						+ numberFormat.format(state.getWidth())
						+ ",height="
						+ numberFormat.format(state.getHeight());
			}

			tip += "]";
		}

		mxPoint trans = getView().getTranslate();

		tip += "<br>scale=" + numberFormat.format(getView().getScale())
				+ ", translate=[x=" + numberFormat.format(trans.getX())
				+ ",y=" + numberFormat.format(trans.getY()) + "]";
		tip += "</html>";

		return tip;
	}
	/**
	 * Overrides the method to use the currently selected edge template for
	 * new edges.
	 * 
	 * @param graph
	 * @param parent
	 * @param id
	 * @param value
	 * @param source
	 * @param target
	 * @param style
	 * @return
	 */
	public Object createEdge(Object parent, 
							 String id, 
							 Object value,
							 Object source, 
							 Object target, 
							 String style)
	{
		debug ("createEdge ()");
		
		INHoopBase sourceHoop=null;
		INHoopBase targetHoop=null;
		
		if (source instanceof mxCell)
		{
			debug ("We've got a source cell here");
	
			mxCell sourceCell=(mxCell) source;
			
			Object userSourceObject=sourceCell.getValue();
			
			if (userSourceObject instanceof INHoopBase)
			{
				sourceHoop=(INHoopBase) userSourceObject;
			}
		}	
				
		if (target instanceof mxCell)
		{
			debug ("We've got a target cell here");
	
			mxCell targetCell=(mxCell) target;
			
			Object userTargetObject=targetCell.getValue();
			
			if (userTargetObject instanceof INHoopBase)
			{
				targetHoop=(INHoopBase) userTargetObject;
			}
		}			
		
		if (INHoopLink.hoopGraphManager.connectHoops(sourceHoop, targetHoop)==false)
		{
			
			return (null);
		}
		
		mxCell edge =null;
		
		if (edgeTemplate != null)
		{
			edge = (mxCell) cloneCells(new Object[] { edgeTemplate })[0];
			edge.setId(id);		
		}
		else
		{
			debug ("Calling super class ...");
			return (super.createEdge (parent, id, value, source, target, style));
		}	
		
		return edge;
	}
	/**
	 * 
	 */
	public Object createVertex (Object parent,
            					String id,
            					Object value,
            					double x,
            					double y,
            					double width,
            					double height,
            					String style)
	{
		debug ("createVertex ()");
		
		Object result=super.createVertex(parent,id,value,x,y,width,height,style);
		
		debug (result.toString());
		
		return (result);
	}
	/**
	 * 
	 */
	public Object createVertex (Object parent,
            					String id,
            					Object value,
            					double x,
            					double y,
            					double width,
            					double height,
            					String style,
            					boolean something)
	{
		debug ("createVertex ()");
		
		Object result=super.createVertex(parent,id,value,x,y,width,height,style,something);
		
		debug (result.toString());
		
		return (result);
	}	
	/**
	 *  Ports are not used as terminals for edges, they are
	 *  only used to compute the graphical connection point
	 */
	public boolean isPort(Object cell)
	{
		debug ("isPort ()");
		
		mxGeometry geo=getCellGeometry(cell);
		
		return (geo!=null) ? geo.isRelative() : false;
	}
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
	public void drawState (mxICanvas canvas, 
						   mxCellState state,
						   boolean drawLabel)
	{
		String label = (drawLabel) ? state.getLabel() : "";

		// Indirection for wrapped swing canvas inside image canvas (used for creating
		// the preview image when cells are dragged)
		if (getModel().isVertex(state.getCell()) && canvas instanceof mxImageCanvas && ((mxImageCanvas) canvas).getGraphicsCanvas() instanceof INHoopVisualGraphCanvas)
		{
			((INHoopVisualGraphCanvas) ((mxImageCanvas) canvas).getGraphicsCanvas()).drawVertex(state, label);
		}
		// Redirection of drawing vertices in SwingCanvas
		else if (getModel().isVertex(state.getCell()) && canvas instanceof INHoopVisualGraphCanvas)
		{
			((INHoopVisualGraphCanvas) canvas).drawVertex(state, label);
		}
		else
		{
			super.drawState(canvas, state, drawLabel);
		}
	}	
}
