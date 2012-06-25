
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
 * 	Notes:
 * 
 *	You can put a Canvas3D inside a JPanel, but you will want to organize 
 *	your layout up-front to make sure the Canvas3D doesn't overlap with 
 *	any other Swing component, because the Canvas3D (heavyweight) will 
 *	eclipse the lightweight component.  I have used a Canvas3D inside a 
 *	TabPane with no problem, as long as I detach any live BranchGroups 
 *	when switching tabs (however this causes a delay depending on the size 
 *	of your BranchGroup).
 *	You can also use two Canvas3Ds, perhaps side-by-side in different 
 *	JPanels.  Your performance will degrade, however, because you'll be 
 *	splitting CPU between the two Canvas3D's.
 */

package edu.cmu.cs.in.controls.map;

import java.awt.BorderLayout;
//import java.awt.Color;
//import java.awt.Component;

import javax.media.j3d.Canvas3D;
import javax.swing.JPopupMenu;

import com.sun.j3d.utils.universe.SimpleUniverse;
//import javax.swing.BorderFactory;
//import javax.swing.BoxLayout;
//import javax.swing.border.Border;

//import edu.cmu.cs.in.base.INHoopLink;
import edu.cmu.cs.in.controls.base.INEmbeddedJPanel;

/** 
 * @author vvelsen
 *
 */
public class INHoopJava3DJPanel extends INEmbeddedJPanel
{	
	private static final long serialVersionUID = 1L;
	
	private INHoopJava3D driver=null;
	private Canvas3D canvas=null;
		
	/**
	 * Creates a new JPanel with a double buffer and a flow layout.
	 */	
	public INHoopJava3DJPanel()
	{
		setClassName ("INHoopJava3DJPanel");
		debug ("INHoopJava3DJPanel ()");
		
		//JPopupMenu.setDefaultLightWeightPopupEnabled (false);
		
		this.setLayout(new BorderLayout(2,2));
		
		driver=new INHoopJava3D ();
		
		canvas=new Canvas3D (SimpleUniverse.getPreferredConfiguration());

        this.add(canvas,BorderLayout.CENTER);		
        
        driver.create();
	}
}
