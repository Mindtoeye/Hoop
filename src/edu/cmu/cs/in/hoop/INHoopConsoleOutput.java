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

import edu.cmu.cs.in.controls.base.INEmbeddedJPanel;

/** 
 * @author vvelsen
 *
 */
public class INHoopConsoleOutput extends INEmbeddedJPanel
{	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Creates a new JPanel with a double buffer and a flow layout.
	 */	
	public INHoopConsoleOutput()
	{
		setClassName ("INHoopConsoleOutput");
		debug ("INHoopConsoleOutput ()");
							
	}	
}
