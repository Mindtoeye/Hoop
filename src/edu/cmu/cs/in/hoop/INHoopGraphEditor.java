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

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.util.mxGraphTransferable;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.view.mxGraph;

import edu.cmu.cs.in.base.INHoopLink;
import edu.cmu.cs.in.hoop.editor.INHoopBasicGraphEditor;
import edu.cmu.cs.in.hoop.editor.INHoopEditorPalette;
import edu.cmu.cs.in.hoop.editor.INHoopEditorPalettePanel;
import edu.cmu.cs.in.hoop.editor.INHoopVisualGraph;
import edu.cmu.cs.in.hoop.editor.INHoopVisualGraphComponent;

public class INHoopGraphEditor extends INHoopBasicGraphEditor implements mxIEventListener
{
	private static final long serialVersionUID = -1L;
	//public static final NumberFormat numberFormat = NumberFormat.getInstance();
	//public static URL url = null;	
	private mxGraph graph=null;

	public INHoopGraphEditor()
	{
		this ("Hoop Graph Editor", new INHoopVisualGraphComponent (new INHoopVisualGraph()));
		
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
		
		setClassName ("INHoopGraphEditor");
		debug ("INHoopGraphEditor ()");		
		
		graph=graphComponent.getGraph();
	
		INHoopEditorPalette shapesPalette = insertPalette("shapes");
	
		// Sets the edge template to be used for creating new edges if an edge
		// is clicked in the shape palette
		shapesPalette.addListener(mxEvent.SELECT,this);
		
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
	}
	/**
	 * 
	 */
	public void handleCloseEvent ()
	{
		debug ("handleCloseEvent ()");
		
		INHoopEditorPalettePanel pallette=(INHoopEditorPalettePanel) INHoopLink.getWindow("Hoop Palette");
		
		if (pallette!=null)
			INHoopLink.removeWindow(pallette);
		else
			debug ("Error: can't remove Hoop Palette, object is null");
		
		INHoopLink.toolBoxContainer.remove(INHoopLink.toolEditorBar);
		INHoopLink.toolEditorBar=null;
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
				((INHoopVisualGraph) graph).setEdgeTemplate(cell);
			}
		}
	}	
}
