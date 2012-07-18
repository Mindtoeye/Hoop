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

/**
 *
 */
public interface INHoopVisualRepresentation
{
	/**
	 * 
	 * @param aState
	 */
	public void setState (String aState);
	
	/**
	 * When a graph is saved it will go through each hoop and call this
	 * method. This will in turn in the panel belonging to that hoop
	 * call a method that sets all the visual properties back into the
	 * hoop.
	 */
	public void propagateVisualProperties ();
}
