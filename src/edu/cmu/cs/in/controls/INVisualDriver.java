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

package edu.cmu.cs.in.controls;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

//import edu.cmu.cs.in.INFeatureMatrixPane;
import edu.cmu.cs.in.base.INBase;

public class INVisualDriver extends ComponentAdapter 
{
	//@SuppressWarnings("unused")
	//private INFeatureMatrixPane responsibility=null;
	
	/**------------------------------------------------------------------------------------
	 *
	 */
	private void debug (String aMessage)
	{
		INBase.debug ("INVisualDriver",aMessage);	
	}	
	/**------------------------------------------------------------------------------------
	 *
	 */
	/*
	public INVisualDriver (INFeatureMatrixPane aResponsibility)
	{
		debug ("INVisualDriver ()");
		
		responsibility=aResponsibility;
	}
	*/
	/**------------------------------------------------------------------------------------
	 *
	 */	
	public void	componentHidden (ComponentEvent e)
	{
		//debug ("componentHidden ()");
		
	}	 
	/**------------------------------------------------------------------------------------
	 *
	 */
	public void	componentMoved (ComponentEvent e)
	{
		//debug ("componentMoved ()");
		
	}
	/**------------------------------------------------------------------------------------
	 *
	 */
	public void	componentResized (ComponentEvent e)
	{
		//debug ("componentResized ()");
		
	}
	/**------------------------------------------------------------------------------------
	 *
	 */
	public void	componentShown (ComponentEvent e)
	{
		debug ("componentShown ()");
		
		/*
		if (responsibility!=null)
			responsibility.process ();
		else
			debug ("Error: no component provided to propagate event");
		 */			
	}
	//-------------------------------------------------------------------------------------	
}
