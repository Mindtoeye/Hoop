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

import com.mxgraph.swing.mxGraphComponent;

import edu.cmu.cs.in.base.INHoopLink;
import edu.cmu.cs.in.hoop.base.INHoopBase;
import edu.cmu.cs.in.hoop.base.INHoopFileLoadBase;
import edu.cmu.cs.in.hoop.properties.INHoopInspectablePanel;
import edu.cmu.cs.in.hoop.properties.INHoopPropertyPanel;

/**   
 * 
 */
public class INHoopNodePanel extends INHoopNodeRenderer
{
	private static final long serialVersionUID = -1L;
	private INHoopBase hoop=null;	
	private INHoopPropertyPanel propPanel=null;
	
	/**
	 * 
	 */
	public INHoopNodePanel (final Object cell, final mxGraphComponent graphContainer)
	{
		super (cell,graphContainer);
		
		setClassName ("INHoopNodeRenderer");
		debug ("INHoopNodeRenderer ()");
		
		propPanel=(INHoopPropertyPanel) INHoopLink.getWindow("Properties");
		
		setHoop (new INHoopFileLoadBase ()); // Just for testing
	}
	/**
	 * 
	 */	
	public INHoopBase getHoop() 
	{
		return hoop;
	}
	/**
	 * 
	 */	
	public void setHoop(INHoopBase aHoop) 
	{
		this.hoop=aHoop;
		
		this.setTitle(hoop.getHoopDescription());
		
		if (propPanel!=null)
		{
			propPanel.addPropertyPanel (new INHoopInspectablePanel (hoop.getHoopDescription()));
		}
	}	
}
