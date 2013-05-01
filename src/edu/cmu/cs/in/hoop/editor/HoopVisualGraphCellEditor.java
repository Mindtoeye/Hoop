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

import java.util.EventObject;

import com.mxgraph.swing.view.mxICellEditor;

import edu.cmu.cs.in.base.HoopRoot;

/** 
 *
 */
public class HoopVisualGraphCellEditor extends HoopRoot implements mxICellEditor
{
	/**
	 * 
	 */
	public HoopVisualGraphCellEditor ()
	{
		setClassName ("HoopVisualGraphCellEditor");
		debug ("HoopVisualGraphCellEditor ()");		
	}
	/**
	 * 
	 */	
	@Override
	public Object getEditingCell() 
	{
		debug ("getEditingCell ()");
		
		return null;
	}
	/**
	 * 
	 */
	@Override
	public void startEditing(Object arg0, EventObject arg1) 
	{
		debug ("startEditing ()");
		
	}
	/**
	 * 
	 */
	@Override
	public void stopEditing(boolean arg0) 
	{
		debug ("stopEditing ()");
		
	}	
}
