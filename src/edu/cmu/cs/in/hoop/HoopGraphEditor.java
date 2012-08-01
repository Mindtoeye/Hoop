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
												  aConnection.getInstanceName(),
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
