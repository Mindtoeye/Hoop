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

//import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.handler.mxPanningHandler;

import edu.cmu.cs.in.base.HoopRoot;

/**
 * 
 */
public class HoopPanningHandler extends mxPanningHandler
{
	/**
	 * 
	 * @param graphComponent
	 */
	public HoopPanningHandler (mxGraphComponent aComponent)
	{
		super (aComponent);
		
		if (graphComponent==null)
			debug ("Error graph component is null");
	}

	/**
	 * 
	 */
	public void debug (String aMessage)
	{
		HoopRoot.debug("HoopPanningHandler",aMessage);
	}	
	
	/**
	 * 
	 */
	public void mousePressed(MouseEvent e)
	{
		if (graphComponent==null)
		{
			debug ("Internal error: graphComponent is null");
			return;
		}
		
		debug ("mousePressed ("+isEnabled()+","+e.isConsumed()+","+graphComponent.isPanningEvent(e)+","+e.isPopupTrigger()+")");
		
		//if (isEnabled() && !e.isConsumed() && graphComponent.isPanningEvent(e)	&& !e.isPopupTrigger())
		if (isEnabled() && !e.isConsumed() && !e.isPopupTrigger())
		{
			start = e.getPoint();
		}
	}

	/**
	 * 
	 */
	public void mouseDragged(MouseEvent e)
	{
		//debug ("mouseDragged ("+e.isConsumed()+")");
		
		if (start==null)
		{
			debug ("Info: no start point set, not panning");
			return;
		}
				
		if (!e.isConsumed() && (start!=null))
		{
			//debug ("Panning ...");
			
			int dx = e.getX() - start.x;
			int dy = e.getY() - start.y;

			Rectangle r = graphComponent.getViewport().getViewRect();

			int right = r.x + ((dx > 0) ? 0 : r.width) - dx;
			int bottom = r.y + ((dy > 0) ? 0 : r.height) - dy;

			graphComponent.getGraphControl().scrollRectToVisible(new Rectangle(right, bottom, 0, 0));

			e.consume();
		}
	}

	/**
	 * 
	 */
	public void mouseReleased(MouseEvent e)
	{
		if (!e.isConsumed() && start != null)
		{
			int dx = Math.abs(start.x - e.getX());
			int dy = Math.abs(start.y - e.getY());

			if (graphComponent.isSignificant(dx, dy))
			{
				e.consume();
			}
		}

		start = null;
	}
}
