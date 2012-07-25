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

import java.util.ArrayList;

import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.util.mxGraphTransferable;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.view.mxGraph;

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.hoop.editor.HoopBasicGraphEditor;
import edu.cmu.cs.in.hoop.editor.HoopEditorPalettePanel;
import edu.cmu.cs.in.hoop.editor.HoopVisualGraph;
import edu.cmu.cs.in.hoop.editor.HoopVisualGraphComponent;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.hoop.hoops.base.HoopConnection;
import edu.cmu.cs.in.hoop.project.HoopGraphFile;

public class HoopGraphEditor extends HoopBasicGraphEditor implements mxIEventListener
{
	private static final long serialVersionUID = -1L;
	private mxGraph graph=null;

	public HoopGraphEditor()
	{
		this ("Hoop Graph Editor", new HoopVisualGraphComponent (new HoopVisualGraph()));
		
		setClassName ("HoopGraphEditor");
		debug ("HoopGraphEditor ()");
		
		this.setSingleInstance(true);
	}
	/**
	 * 
	 */
	public void reset ()
	{
		graph.removeCells(graph.getChildVertices(graph.getDefaultParent()));		
	}
	/**
	 * 
	 */
	public mxGraph getGraph ()
	{
		return (graphComponent.getGraph());
	}
	/**
	 * 
	 */
	public HoopGraphEditor (String appTitle, mxGraphComponent component)
	{
		super (appTitle, component);
		
		setClassName ("HoopGraphEditor");
		debug ("HoopGraphEditor ()");		
		
		graph=graphComponent.getGraph();
	
		/*
		HoopEditorPalette shapesPalette = insertPalette("shapes");
	
		// Sets the edge template to be used for creating new edges if an edge
		// is clicked in the shape palette
		shapesPalette.addListener(mxEvent.SELECT,this);
		
		shapesPalette.addTemplate("Container",HoopLink.getImageByName("swimlane.png"),"swimlane", 280, 280, "Container");
		shapesPalette.addTemplate("Icon",HoopLink.getImageByName("rounded.png"),"icon;image=/assets/images/wrench.png",70, 70, "Icon");
		shapesPalette.addTemplate("Label",HoopLink.getImageByName("rounded.png"),"label;image=/assets/images/gear.png",130, 50, "Label");
		shapesPalette.addTemplate("Rectangle",HoopLink.getImageByName("rectangle.png"),	null, 160, 120, "");
		shapesPalette.addTemplate("Rounded Rectangle",HoopLink.getImageByName("rounded.png"),"rounded=1", 160, 120, "");
		shapesPalette.addTemplate("Ellipse",HoopLink.getImageByName("ellipse.png"),"ellipse", 160, 160, "");
		shapesPalette.addTemplate("Double Ellipse",HoopLink.getImageByName("doubleellipse.png"),"ellipse;shape=doubleEllipse", 160, 160, "");
		shapesPalette.addTemplate("Triangle",HoopLink.getImageByName("triangle.png"),"triangle", 120, 160, "");
		shapesPalette.addTemplate("Rhombus",HoopLink.getImageByName("rhombus.png"),"rhombus", 160, 160, "");
		shapesPalette.addTemplate("Horizontal Line",HoopLink.getImageByName("hline.png"),"line", 160, 10, "");
		shapesPalette.addTemplate("Hexagon",HoopLink.getImageByName("hexagon.png"),"shape=hexagon", 160, 120, "");
		shapesPalette.addTemplate("Cylinder",HoopLink.getImageByName("cylinder.png"),"shape=cylinder", 120, 160, "");
		shapesPalette.addTemplate("Actor",HoopLink.getImageByName("actor.png"),"shape=actor", 120, 160, "");
		shapesPalette.addTemplate("Cloud",HoopLink.getImageByName("cloud.png"),"ellipse;shape=cloud", 160, 120, "");
		shapesPalette.addEdgeTemplate("Straight",HoopLink.getImageByName("straight.png"),"straight", 120, 120, "");
		shapesPalette.addEdgeTemplate("Horizontal Connector",HoopLink.getImageByName("connect.png"),null, 100, 100, "");
		shapesPalette.addEdgeTemplate("Vertical Connector",HoopLink.getImageByName("vertical.png"),"vertical", 100, 100, "");
		shapesPalette.addEdgeTemplate("Entity Relation",HoopLink.getImageByName("entity.png"),"entity", 100, 100, "");
		shapesPalette.addEdgeTemplate("Arrow",HoopLink.getImageByName("arrow.png"),"arrow", 120, 120, "");
		*/
	}
	/**
	 * 
	 */
	public void handleCloseEvent ()
	{
		debug ("handleCloseEvent ()");
		
		HoopEditorPalettePanel pallette=(HoopEditorPalettePanel) HoopLink.getWindow("Hoop Palette");
		
		if (pallette!=null)
			HoopLink.removeWindow(pallette);
		else
			debug ("Error: can't remove Hoop Palette, object is null");
		
		HoopLink.toolBoxContainer.remove(HoopLink.toolEditorBar);
		HoopLink.toolEditorBar=null;
	}
	/**
	 * 
	 */
	public void instantiateFromFile (HoopGraphFile aFile)
	{
		debug ("instantiateFromFile ()");
		
		ArrayList <HoopBase> hoopList=aFile.getHoops();
		ArrayList <HoopConnection> hoopConnections=aFile.getHoopConnections();
		
		Object parent=graph.getDefaultParent();
		
		graph.getModel().beginUpdate();
				
		try
		{
			// First the nodes ...
			
			for (int i=0;i<hoopList.size();i++)
			{	
				HoopBase aHoop=hoopList.get(i);
			
				mxCell graphObject=(mxCell) graph.insertVertex (parent, 
																aHoop.getClassName(),
																aHoop,
																aHoop.getX(),
																aHoop.getY(),
																aHoop.getWidth(),
																aHoop.getHeight());
				
				graphObject.setValue(aHoop);				
			}			
		}
		finally
		{
			graph.getModel().endUpdate();
		}		
		
		graph.getModel().beginUpdate();
		
		try
		{
			// Then the connections ...
			
			for (int j=0;j<hoopConnections.size();j++)
			{
				HoopConnection aConnection=hoopConnections.get(j);
				
				HoopBase fromHoop=aFile.getByID(aConnection.getFromHoopID());
				HoopBase toHoop=aFile.getByID(aConnection.getToHoopID());
								
				if (fromHoop!=null)
				{			
					if (toHoop!=null)
					{
						if (fromHoop.getGraphCellReference()==null)
						{
							debug ("Error: no mxCell object available in 'from' Hoop");
						}
						else				
						{
							if (toHoop.getGraphCellReference()==null)
							{
								debug ("Error: no mxCell object available in 'to' Hoop");
							}
							else
							{
								debug ("Inserting new edge ...");
								
								graph.insertEdge (parent,
												  null,
												  "Edge",
												  fromHoop.getGraphCellReference(),
												  toHoop.getGraphCellReference());								
							}
						}						
					}
					else
						debug ("Error, can't find target hoop in graph from id: " + aConnection.getToHoopID());
				}
				else
					debug ("Error, can't find source hoop in graph from id: " + aConnection.getFromHoopID());
			}			
		}
		finally
		{
			graph.getModel().endUpdate();
		}		
	}
	/**
	 * 
	 */
	public void invoke(Object sender, mxEventObject evt)
	{
		Object tmp = evt.getProperty("transferable");

		if (tmp instanceof mxGraphTransferable)
		{
			mxGraphTransferable t = (mxGraphTransferable) tmp;
			Object cell = t.getCells()[0];

			if (graph.getModel().isEdge(cell))
			{
				((HoopVisualGraph) graph).setEdgeTemplate(cell);
			}
		}
	}	
}
