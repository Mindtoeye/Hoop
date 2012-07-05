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

package edu.cmu.cs.in.hoop.base;

import javax.swing.JPanel;

import org.w3c.dom.Element;

/**
* Here we have the basis for all the hoops. It manages incoming and
* outgoing links to other hoops. Please keep in mind that even
* though the API allows more than one incoming hoop, we currently
* restrict the functionality to only one.
*/
public interface INHoopInterface
{    			
	/**
	 *
	 */
	public Boolean runHoop (INHoopBase inHoop);

	/**
	 * 
	 */
	public INHoopBase copy ();
	
	/**
	 * 
	 */
	public JPanel getPropertiesPanel ();
	
	/**
	 * 
	 */
	public void fromXML (String aStream);
	
	/**
	 * 
	 */
	public void fromXMLElement (Element anElement);
	
	/**
	 * 
	 */
	public String toXML ();
}
