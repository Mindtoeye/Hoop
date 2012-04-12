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

/*
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.tree.*;
*/

package edu.cmu.cs.in.hoop;

import edu.cmu.cs.in.controls.INGridNodeVisualizer;
import edu.cmu.cs.in.controls.base.INEmbeddedJPanel;

/**
 * 
 */
public class INHoopCluster extends INEmbeddedJPanel 
{  
	private static final long serialVersionUID = 8387762921834350566L;

	private INGridNodeVisualizer driver=null;
	
	/**
	 * 
	 */
	public INHoopCluster() 
    {
		driver=new INGridNodeVisualizer ();
		setContentPane (driver);
    }
	/**
	 *
	 */	
	public void updateContents() 
	{
		debug ("updateContents ()");
		
		if (driver!=null)
		{			
			// This should result in a paint operation
			driver.updateUI();
		}	
	}		
}
