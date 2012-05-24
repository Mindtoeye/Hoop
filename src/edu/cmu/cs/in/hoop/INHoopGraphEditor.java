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

package edu.cmu.cs.in.hoop;

import java.awt.Color;
import java.awt.Point;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.List;

//import javax.swing.ImageIcon;
//import javax.swing.UIManager;

import org.w3c.dom.Document;

import com.mxgraph.io.mxCodec;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxICell;
import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.util.mxGraphTransferable;
//import com.mxgraph.swing.util.mxSwingConstants;
//import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.util.mxPoint;
import com.mxgraph.util.mxResources;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraph;

import edu.cmu.cs.in.base.INHoopLink;
//import edu.cmu.cs.in.hoop.editor.EditorMenuBar;
import edu.cmu.cs.in.hoop.editor.INHoopBasicGraphEditor;
import edu.cmu.cs.in.hoop.editor.INHoopEditorPalette;

public class INHoopGraphEditor extends INHoopBasicGraphEditor
{
	private static final long serialVersionUID = -4601740824088314699L;
	public static final NumberFormat numberFormat = NumberFormat.getInstance();
	public static URL url = null;

	//GraphEditor.class.getResource("/assets/images/connector.gif");

	public INHoopGraphEditor()
	{
		this ("Hoop Graph Editor", new CustomGraphComponent(new CustomGraph()));
		
		setClassName ("INHoopGraphEditor");
		debug ("INHoopGraphEditor ()");
		
		this.setSingleInstance(true);
	}

	/**
	 * 
	 */
	public INHoopGraphEditor (String appTitle, mxGraphComponent component)
	{
		super (appTitle, component);
		
		final mxGraph graph = graphComponent.getGraph();

		// Creates the shapes palette
		INHoopEditorPalette shapesPalette = insertPalette(mxResources.get("shapes"));
		INHoopEditorPalette imagesPalette = insertPalette(mxResources.get("images"));
		INHoopEditorPalette symbolsPalette = insertPalette(mxResources.get("symbols"));

		// Sets the edge template to be used for creating new edges if an edge
		// is clicked in the shape palette
		shapesPalette.addListener(mxEvent.SELECT, new mxIEventListener()
		{
			public void invoke(Object sender, mxEventObject evt)
			{
				Object tmp = evt.getProperty("transferable");

				if (tmp instanceof mxGraphTransferable)
				{
					mxGraphTransferable t = (mxGraphTransferable) tmp;
					Object cell = t.getCells()[0];

					if (graph.getModel().isEdge(cell))
					{
						((CustomGraph) graph).setEdgeTemplate(cell);
					}
				}
			}

		});

		shapesPalette.addTemplate("Container",INHoopLink.getImageByName("swimlane.png"),"swimlane", 280, 280, "Container");
		shapesPalette.addTemplate("Icon",INHoopLink.getImageByName("rounded.png"),"icon;image=/assets/images/wrench.png",70, 70, "Icon");
		shapesPalette.addTemplate("Label",INHoopLink.getImageByName("rounded.png"),"label;image=/assets/images/gear.png",130, 50, "Label");
		shapesPalette.addTemplate("Rectangle",INHoopLink.getImageByName("rectangle.png"),	null, 160, 120, "");
		shapesPalette.addTemplate("Rounded Rectangle",INHoopLink.getImageByName("rounded.png"),"rounded=1", 160, 120, "");
		shapesPalette.addTemplate("Ellipse",INHoopLink.getImageByName("ellipse.png"),"ellipse", 160, 160, "");
		shapesPalette.addTemplate("Double Ellipse",INHoopLink.getImageByName("doubleellipse.png"),"ellipse;shape=doubleEllipse", 160, 160, "");
		shapesPalette.addTemplate("Triangle",INHoopLink.getImageByName("triangle.png"),"triangle", 120, 160, "");
		shapesPalette.addTemplate("Rhombus",INHoopLink.getImageByName("rhombus.png"),"rhombus", 160, 160, "");
		shapesPalette.addTemplate("Horizontal Line",INHoopLink.getImageByName("hline.png"),"line", 160, 10, "");
		shapesPalette.addTemplate("Hexagon",INHoopLink.getImageByName("hexagon.png"),"shape=hexagon", 160, 120, "");
		shapesPalette.addTemplate("Cylinder",INHoopLink.getImageByName("cylinder.png"),"shape=cylinder", 120, 160, "");
		shapesPalette.addTemplate("Actor",INHoopLink.getImageByName("actor.png"),"shape=actor", 120, 160, "");
		shapesPalette.addTemplate("Cloud",INHoopLink.getImageByName("cloud.png"),"ellipse;shape=cloud", 160, 120, "");
		shapesPalette.addEdgeTemplate("Straight",INHoopLink.getImageByName("straight.png"),"straight", 120, 120, "");
		shapesPalette.addEdgeTemplate("Horizontal Connector",INHoopLink.getImageByName("connect.png"),null, 100, 100, "");
		shapesPalette.addEdgeTemplate("Vertical Connector",INHoopLink.getImageByName("vertical.png"),"vertical", 100, 100, "");
		shapesPalette.addEdgeTemplate("Entity Relation",INHoopLink.getImageByName("entity.png"),"entity", 100, 100, "");
		shapesPalette.addEdgeTemplate("Arrow",INHoopLink.getImageByName("arrow.png"),"arrow", 120, 120, "");
		imagesPalette.addTemplate("Bell",INHoopLink.getImageByName("bell.png"),"image;image=/assets/images/bell.png",50, 50, "Bell");
		imagesPalette.addTemplate("Box",INHoopLink.getImageByName("box.png"),"image;image=/assets/images/box.png",50, 50, "Box");
		imagesPalette.addTemplate("Cube",INHoopLink.getImageByName("cube_green.png"),"image;image=/assets/images/cube_green.png",50, 50, "Cube");
		imagesPalette.addTemplate("User",INHoopLink.getImageByName("dude3.png"),"roundImage;image=/assets/images/dude3.png", 50, 50, "User");
		imagesPalette.addTemplate("Earth",INHoopLink.getImageByName("earth.png"),"roundImage;image=/assets/images/earth.png",50, 50, "Earth");
		imagesPalette.addTemplate("Gear",INHoopLink.getImageByName("gear.png"),"roundImage;image=/assets/images/gear.png",50, 50, "Gear");
		imagesPalette.addTemplate("Home",INHoopLink.getImageByName("house.png"),"image;image=/assets/images/house.png",50, 50, "Home");
		imagesPalette.addTemplate("Package",INHoopLink.getImageByName("package.png"),"image;image=/assets/images/package.png",50, 50, "Package");
		imagesPalette.addTemplate("Printer",INHoopLink.getImageByName("printer.png"),"image;image=/assets/images/printer.png", 50, 50, "Printer");
		imagesPalette.addTemplate("Server",INHoopLink.getImageByName("server.png"),"image;image=/assets/images/server.png",	50, 50, "Server");
		imagesPalette.addTemplate("Workplace",INHoopLink.getImageByName("workplace.png"),"image;image=/assets/images/workplace.png", 50, 50, "Workplace");
		imagesPalette.addTemplate("Wrench",INHoopLink.getImageByName("wrench.png"),"roundImage;image=/assets/images/wrench.png",50, 50, "Wrench");
		symbolsPalette.addTemplate("Cancel",INHoopLink.getImageByName("cancel_end.png"),"roundImage;image=/assets/images/cancel_end.png", 80, 80, "Cancel");
		symbolsPalette.addTemplate("Error",INHoopLink.getImageByName("error.png"),"roundImage;image=/assets/images/error.png", 80, 80, "Error");
		symbolsPalette.addTemplate("Event",INHoopLink.getImageByName("event.png"),"roundImage;image=/assets/images/event.png", 80, 80, "Event");
		symbolsPalette.addTemplate("Fork",INHoopLink.getImageByName("fork.png"),"rhombusImage;image=/assets/images/fork.png", 80, 80, "Fork");
		symbolsPalette.addTemplate("Inclusive",INHoopLink.getImageByName("inclusive.png"),"rhombusImage;image=/assets/images/inclusive.png", 80, 80, "Inclusive");
		symbolsPalette.addTemplate("Link",INHoopLink.getImageByName("link.png"),"roundImage;image=/assets/images/link.png",	80, 80, "Link");
		symbolsPalette.addTemplate("Merge",INHoopLink.getImageByName("merge.png"), "rhombusImage;image=/assets/images/merge.png", 80, 80, "Merge");
		symbolsPalette.addTemplate("Message",INHoopLink.getImageByName("message.png"),"roundImage;image=/assets/images/message.png", 80, 80, "Message");
		symbolsPalette.addTemplate("Multiple",INHoopLink.getImageByName("multiple.png"),"roundImage;image=/assets/images/multiple.png", 80, 80, "Multiple");
		symbolsPalette.addTemplate("Rule",INHoopLink.getImageByName("rule.png"),"roundImage;image=/assets/images/rule.png", 80, 80, "Rule");
		symbolsPalette.addTemplate("Terminate",INHoopLink.getImageByName("terminate.png"), "roundImage;image=/assets/images/terminate.png", 80, 80, "Terminate");
		symbolsPalette.addTemplate("Timer",INHoopLink.getImageByName("timer.png"), "roundImage;image=/assets/images/timer.png",	80, 80, "Timer");
	}

	/**
	* 
	*/
	public static class CustomGraphComponent extends mxGraphComponent
	{
		private static final long serialVersionUID = -6833603133512882012L;

		/**
		 * 
		 * @param graph
		 */
		public CustomGraphComponent(mxGraph graph)
		{
			super(graph);

			// Sets switches typically used in an editor
			setPageVisible(true);
			setGridVisible(true);
			setToolTips(true);
			getConnectionHandler().setCreateTarget(true);

			// Loads the defalt stylesheet from an external file
			mxCodec codec = new mxCodec();
			Document doc = mxUtils.loadDocument(INHoopGraphEditor.class.getResource("/assets/resources/default-style.xml").toString());
			codec.decode(doc.getDocumentElement(), graph.getStylesheet());

			// Sets the background to white
			getViewport().setOpaque(true);
			getViewport().setBackground(Color.WHITE);
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
			if (target == null && cells.length == 1 && location != null)
			{
				target = getCellAt(location.x, location.y);

				if (target instanceof mxICell && cells[0] instanceof mxICell)
				{
					mxICell targetCell = (mxICell) target;
					mxICell dropCell = (mxICell) cells[0];

					if (targetCell.isVertex() == dropCell.isVertex()
							|| targetCell.isEdge() == dropCell.isEdge())
					{
						mxIGraphModel model = graph.getModel();
						model.setStyle(target, model.getStyle(cells[0]));
						graph.setSelectionCell(target);

						return null;
					}
				}
			}

			return super.importCells(cells, dx, dy, target, location);
		}
	}

	/**
	 * A graph that creates new edges from a given template edge.
	 */
	public static class CustomGraph extends mxGraph
	{
		/**
		 * Holds the edge to be used as a template for inserting new edges.
		 */
		protected Object edgeTemplate;

		/**
		 * Custom graph that defines the alternate edge style to be used when
		 * the middle control point of edges is double clicked (flipped).
		 */
		public CustomGraph()
		{
			setAlternateEdgeStyle("edgeStyle=mxEdgeStyle.ElbowConnector;elbow=vertical");
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
			if (edgeTemplate != null)
			{
				mxCell edge = (mxCell) cloneCells(new Object[] { edgeTemplate })[0];
				edge.setId(id);

				return edge;
			}

			return super.createEdge(parent, id, value, source, target, style);
		}

	}

	/**
	 * 
	 * @param args
	 */
	/*
	public static void main(String[] args)
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e1)
		{
			e1.printStackTrace();
		}

		mxSwingConstants.SHADOW_COLOR = Color.LIGHT_GRAY;
		mxConstants.W3C_SHADOWCOLOR = "#D3D3D3";
		
		GraphEditor editor = new GraphEditor();
		editor.createFrame(new EditorMenuBar(editor)).setVisible(true);
	}
	*/
}
