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

package edu.cmu.cs.in.hoop.properties;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;

import edu.cmu.cs.in.controls.base.INEmbeddedJPanel;

/** 
 * @author vvelsen
 *
 */
public class INHoopPropertyPanel extends INEmbeddedJPanel
{	
	private static final long serialVersionUID = 1L;
		
    private Box hoopPropertyBox=null;
    
	/**
	 * Creates a new JPanel with a double buffer and a flow layout.
	 */	
	public INHoopPropertyPanel()
	{
		setClassName ("INHoopPropertyPanel");
		debug ("INHoopPropertyPanel ()");
						
		hoopPropertyBox=new Box (BoxLayout.Y_AXIS);
		hoopPropertyBox.setMinimumSize(new Dimension (20,20));
		hoopPropertyBox.setMaximumSize(new Dimension (5000,5000));
        
        this.add(hoopPropertyBox);
        
        hoopPropertyBox.add(new INHoopInspectablePanel ());
	}
	/**
	 * 
	 */
	public void handleCloseEvent ()
	{
		debug ("handleCloseEvent ()");
		
	}
	/**
	 *
	 */	
	public void updateContents() 
	{
		debug ("updateContents ()");

	}	
}
