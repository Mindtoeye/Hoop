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

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.border.BevelBorder;

import edu.cmu.cs.in.controls.base.INJPanel;

/** 
 * @author Martin van Velsen
 */
public class INHoopVisualNode extends INJPanel
{
	private static final long serialVersionUID = -1L;
	private String title="Undefined";
	
	/**
	 * Creates a new JPanel with a double buffer and a flow layout.
	 */	
	public INHoopVisualNode()
	{
		setClassName ("INHoopVisualNode");
		debug ("INHoopVisualNode ()");
		
		this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		//this.setHorizontalAlignment(JLabel.CENTER);
		//this.setBackground(graphComponent.getBackground().darker());
		this.setOpaque(true);		
	}
	/** 
	 * @param title
	 */
	public void setTitle(String title) 
	{
		this.title = title;
	}
	/** 
	 * 
	 */
	public String getTitle() 
	{
		return title;
	}	
	/** 
	 * @param title
	 */	
	public void setText(String title) 
	{
		this.title = title;
	}
	/** 
	 * 
	 */
	public String getText() 
	{
		return title;
	}		
}
	