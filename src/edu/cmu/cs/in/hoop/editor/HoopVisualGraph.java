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

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Map;

import javax.swing.JOptionPane;

import com.mxgraph.canvas.mxICanvas;
import com.mxgraph.canvas.mxImageCanvas;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxICell;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraph;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventSource;

import edu.cmu.cs.in.base.HoopRoot;
import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.hoop.properties.HoopPropertyPanel;

/** 
 *
 */
public class HoopVisualGraph extends mxGraph implements mxEventSource.mxIEventListener, Serializable
{	
	private static final long serialVersionUID = -6644679794406853442L;

	public static final NumberFormat numberFormat = NumberFormat.getInstance();
	
	/// Holds the edge to be used as a template for inserting new edges.
	protected Object edgeTemplate;

	private Boolean hardReset=false;
	
	/**
	 * Custom graph that defines the alternate edge style to be used when
	 * the middle control point of edges is double clicked (flipped).
	 */
	public HoopVisualGraph()
	{
		setAllowDanglingEdges(false);
		setCellsCloneable(false);		
		setAllowLoops (false);
		setDisconnectOnMove (false);
				
		// Sets the default edge style
		Map<String, Object> style = this.getStylesheet().getDefaultEdgeStyle();
		style.put (mxConstants.STYLE_STROKECOLOR,"yellow");
		style.put (mxConstants.STYLE_STROKEWIDTH,"2");
		//style.put (mxConstants.STYLE_EDGE, mxConstants.EDGESTYLE_ELBOW);
		//style.put (mxConstants.STYLE_EDGE, mxConstants.EDGESTYLE_LOOP);
		//style.put (mxConstants.STYLE_EDGE, mxConstants.EDGESTYLE_ENTITY_RELATION);
		//style.put (mxConstants.STYLE_EDGE, mxConstants.EDGESTYLE_ORTHOGONAL);
		//style.put (mxConstants.STYLE_EDGE, mxConstants.ENTITY_SEGMENT);
		this.addListener (mxEvent.CELLS_REMOVED,(mxIEventListener) this);				
	}
	/**
	 * 
	 */
	private void debug (String aMessage)
	{
		HoopRoot.debug("HoopVisualGraph",aMessage);
	}
	/**
	 * 
	 */
	public static void alert (String aMessage)
	{
		JOptionPane.showMessageDialog(HoopLink.mainFrame,aMessage);
	}
	/**
	 * 
	 */
	public Boolean getHardReset() 
	{
		return hardReset;
	}
	/**
	 * 
	 * @param hardReset
	 */
	public void setHardReset(Boolean hardReset) 
	{
		this.hardReset = hardReset;
	}	
	/**
	 * Sets the edge template to be used to inserting edges.
	 */
	public void setEdgeTemplate(Object template)
	{
		edgeTemplate = template;
	}
	/**
	 * For now we prevent users from editing a node/hoop. Ideally we do this check
	 * in a mouse handler so that we can fire other editing events, however at
	 * this point it is not clear where that mouse event handler would be set.
	 */
	public boolean isCellEditable(Object cell)
	{
		debug ("isCellEditable ()");
		
		HoopBase testHoop=cellToHoop (cell);
		
		if (testHoop!=null)
		{
			debug ("No");
			
			HoopPropertyPanel panel=(HoopPropertyPanel) HoopLink.getWindow("Properties");
			
			panel.popPanel (testHoop);
			
			return (false);
		}
		
		debug ("Yes");
		
		return (true);
	}
	/**
	 * In order to avoid complete serialization of all the visual objects,
	 * the hoop objects and anything else attached to a cell, we now go
	 * through a unique id that identifies a hoop
	 */
	public HoopBase cellToHoop (Object aCell)
	{
		debug ("cellToHoop ()");
		
		if (aCell instanceof mxCell)
		{
			mxCell cell=(mxCell) aCell;
			
			if (cell.getValue() instanceof HoopBase)
			{
				debug ("ERROR: this cell still contains a Hoop not a String!");
			}
			else
			{
				if (cell.getValue() instanceof String)
				{
					String hoopID=(String) cell.getValue();
											
					HoopBase testHoop=HoopLink.hoopGraphManager.findHoopByID (hoopID);

					return (testHoop);
				}
				else
					debug ("Internal error: cell contains unidentified object type: " + cell.getValue());
			}
		}		
		
		return (null);
	}
	/**
	 * 
	 */
	public void hoopToCell (HoopBase aHoop,Object aCell)
	{
		debug ("hoopToCell ()");
		
		if (aCell instanceof mxCell)
		{
			mxCell cell=(mxCell) aCell;
			
			cell.setValue(aHoop.getInstanceName());
		}				
	}
	/**
	 * Prints out some useful information about the cell in the tooltip.
	 */
	public String getToolTipForCell(Object cellTest)
	{
		/*
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
		*/
		
		HoopBase testHoop=cellToHoop (cellTest);
		if (testHoop!=null)
		{
			return ("Hoop: " + testHoop.getClassName());
		}
		
		
		return ("Unknown object");
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
		
		/*
		Object result=null;
		
		if ((source!=null) && (target!=null))
		{
			HoopBase aSource=(HoopBase) source;
			HoopBase aTarget=(HoopBase) target;
		
			result=super.createEdge(parent,aSource.getClassName()+"->"+aTarget.getClassName(),value,source,target,style);
		}
		else
		{
			result=super.createEdge(parent,"Edge",value,source,target,style);
		}
		*/
		
		Object result=super.createEdge(parent,id,value,source,target,style);
				
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
            					String style)
	{
		//debug ("createVertex ()");
		
		Object result=super.createVertex(parent,id,value,x,y,width,height,style);
		
		//debug (result.toString());
		
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
		//debug ("createVertex ()");
		
		Object result=super.createVertex(parent,id,value,x,y,width,height,style,something);
		
		//debug (result.toString());
		
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
		if (getModel().isVertex(state.getCell()) && canvas instanceof mxImageCanvas && ((mxImageCanvas) canvas).getGraphicsCanvas() instanceof HoopVisualGraphCanvas)
		{
			((HoopVisualGraphCanvas) ((mxImageCanvas) canvas).getGraphicsCanvas()).drawVertex(state, label);
		}
		// Redirection of drawing vertices in SwingCanvas
		else if (getModel().isVertex(state.getCell()) && canvas instanceof HoopVisualGraphCanvas)
		{
			((HoopVisualGraphCanvas) canvas).drawVertex(state, label);
		}
		else
		{
			super.drawState(canvas, state, drawLabel);
		}
	}	
	/**
	 * 
	 */
	/*
	public String validateEdge(Object edge,Object source,Object target)
	{
		debug ("validateEdge ()");
		
		HoopBase sourceHoop=null;
		HoopBase targetHoop=null;
		
		if (source instanceof mxCell)
		{
			//debug ("We've got a source cell here");
	
			mxCell sourceCell=(mxCell) source;
			
			Object userSourceObject=sourceCell.getValue();
			
			if (userSourceObject!=null)
			{
				if (userSourceObject instanceof HoopBase)
				{
					sourceHoop=(HoopBase) userSourceObject;
				}
				else
					debug ("Edge source is: " + userSourceObject);
			}
			else
				debug ("Source is null");
		}	
				
		if (target instanceof mxCell)
		{
			//debug ("We've got a target cell here");
	
			mxCell targetCell=(mxCell) target;
			
			Object userTargetObject=targetCell.getValue();
			
			if (userTargetObject!=null)
			{
				if (userTargetObject instanceof HoopBase)
				{
					targetHoop=(HoopBase) userTargetObject;
				}
				else
					debug ("Edge target is: " + userTargetObject);
			}
			else
				debug ("Target is null");
		}			
		
		if ((sourceHoop!=null) && (targetHoop!=null))
		{
			debug ("We have a valid source and target hoop, linking them together ...");
			
			if (HoopLink.hoopGraphManager.connectHoops(sourceHoop, targetHoop)==false)
			{				
				debug ("Error creating edge");
				return ("Error creating edge");
			}
		}			
		else
			debug ("Info: no complete edge yet, waiting ...");
		
		return (null);
	}
	*/
	/**
	 * 
	 */
	public boolean containsStartHoop (Object [] cells)
	{
		debug ("containsStartHoop ()");
		
		if (this.getHardReset()==true)
			return (false);
		
		if (cells==null)
		{
			return (false);
		}
		
		for (int i=0;i<cells.length;i++)
		{
			Object cellTest=cells [i];
			
			HoopBase testHoop=cellToHoop (cellTest);
					
			if (testHoop!=null)
			{
				if (testHoop.getClassName().equals("HoopStart")==true)
				{
					debug ("One of the hoops is a start node");
						
					return (true);
				}
			}	
		}
		
		return (false);
	}
	/**
	 * 
	 */
	public Object[] removeCells ()
	{
		debug ("removeCells ()");
		
		if (containsStartHoop (getSelectionCells())==true)
		{
			alert ("Can't delete start node");
			
			return (null);
		}
		
		Object[] result=super.removeCells();
		
		return (result);
	}
	/**
	 * 
	 */
	public Object[] removeCells (Object[] cells)
	{
		debug ("removeCells (Object[] cells)");
		
		if (containsStartHoop (cells)==true)
		{
			alert ("Can't delete start node");
			
			return (null);
		}		
		
		Object[] result=super.removeCells(cells);
		
		return (result);
	}
	/**
	 * 
	 */
	public Object[] removeCells (Object[] cells,boolean includeEdges)
	{
		debug ("removeCells (Object[] cells,boolean includeEdges)");

		if (containsStartHoop (cells)==true)
		{
			alert ("Can't delete start node");
			
			return (null);
		}
		
		Object[] result=super.removeCells(cells,includeEdges);
		
		return (result);
	}	
	/**
	 * From: mxEventSource.mxIEventListener
	 */
	@Override
	public void invoke(Object sender, mxEventObject evt) 
	{
		debug ("invoke ()");
		
		if (evt.getName().equals(mxEvent.CELLS_REMOVED)==true)
		{		
			debug ("CELLS_REMOVED");
			
			Object[] removedCells = (Object[]) evt.getProperty("cells");
		 
			debug ("Processing invoke on " + removedCells.length + " objects ...");
			
			int i=0;
			
			//>-------------------------------------------------------
			
			debug ("Checking edges first ...");
			
			for (i=0;i<removedCells.length;i++)
			{
				Object test=removedCells [i];
				
				if (test instanceof mxCell)
				{
					mxCell cell=(mxCell) test;
			 
					if(cell.isEdge()) 
					{
						debug ("Removing edge ...");
						
						mxICell source=cell.getSource();
						
						// Translate source cell to hoop
						
						HoopBase sourceHoop=HoopLink.hoopGraphManager.findHoopByReference (source);
						
						if (sourceHoop==null)
						{
							debug ("Error: unable to find source hoop in graph!");
							return;
						}
												
						mxICell target=cell.getTarget();
						
						// Translate target cell to hoop
						
						HoopBase targetHoop=HoopLink.hoopGraphManager.findHoopByReference (target);
						
						if (targetHoop==null)
						{
							debug ("Error: unable to find target hoop in graph!");
							//return;
						}
						else						
							HoopLink.hoopGraphManager.disconnectHoops(sourceHoop,targetHoop);	
						
						//sourceHoop.propagateVisualProperties ();
						//targetHoop.propagateVisualProperties ();						
					}					
				}	
			}
			
			//>-------------------------------------------------------
			
			debug ("Checking nodes next ...");
			
			for (i=0;i<removedCells.length;i++)
			{
				Object test=removedCells [i];
				
				if (test instanceof mxCell)
				{
					mxCell cell=(mxCell) test;
			 					
					if (cell.isVertex())
					{
						debug ("Removing node ...");
						
						mxCell properCell=cell;
						
						//HoopBase aHoop=(HoopBase) properCell.getValue();
												
						//HoopLink.hoopGraphManager.removeHoop((HoopBase) properCell.getValue());
						
						HoopLink.hoopGraphManager.removeHoop(cellToHoop (properCell));						
					}
				}	
			}			
			
			//>-------------------------------------------------------
		}
		
		HoopLink.hoopGraphManager.listConnections();
	}
}
