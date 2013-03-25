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

package edu.cmu.cs.in.base;

import java.awt.Color;

import edu.cmu.cs.in.hoop.properties.HoopStoredProperties;

/**
*
*/
public class HoopProperties extends HoopLangLink
{    		    	
	// Multi view properties
	
	public static int tabPadding=4;
	
	// Graph settings
	
	public static Color graphBackgroundColor=new Color (140,140,140);
	public static Color graphPanelContent=new Color (132,132,132);
	public static Color graphPanelColor=new Color (120,120,120);
	public static Color graphPanelColorLight=new Color (160,160,160);
	
	// Dynamic structure that maintains global properties. This
	// structure should be storable on disk
	
	public static HoopStoredProperties props=null;
}
