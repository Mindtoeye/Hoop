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

import java.awt.Font;
import javax.swing.JTabbedPane;

import edu.cmu.cs.in.controls.base.HoopEmbeddedJPanel;

/** 
 * @author vvelsen
 *
 */
public class HoopEditorPalettePanel extends HoopEmbeddedJPanel
{	
	private static final long serialVersionUID = 1L;
	private JTabbedPane libraryPane=null;
		
	/**
	 * Creates a new JPanel with a double buffer and a flow layout.
	 */	
	public HoopEditorPalettePanel()
	{
		setClassName ("HoopEditorPalletePanel");
		debug ("HoopEditorPalletePanel ()");
		
		this.setSingleInstance(true);
		
		libraryPane=new JTabbedPane ();
		libraryPane.setFont(new Font("Dialog", 1, 10));
		
		//this.add(libraryPane);
		this.setContentPane(libraryPane);
	}
	/**
	 * 
	 */
	public void handleCloseEvent ()
	{
		debug ("handleCloseEvent ()");
		
		// Process this in child class!!
	}
	/**
	 *
	 */	
	public void updateContents() 
	{
		debug ("updateContents ()");
		// Implement in child class!!
	}
	/** 
	 * @return
	 */
	public JTabbedPane getLibraryPane() 
	{
		return libraryPane;
	}	
}
