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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import edu.cmu.cs.in.controls.INHoopShadowBorder;
import edu.cmu.cs.in.controls.base.INJPanel;

/** 
 * @author Martin van Velsen
 */
public class INHoopVisualNode extends INJPanel
{
	private static final long serialVersionUID = -1L;
	private String title="Undefined";
	private JLabel titleLabel=null;
	private JPanel contentArea=null;
	
	/**
	 * Creates a new JPanel with a double buffer and a flow layout.
	 */	
	public INHoopVisualNode()
	{
		setClassName ("INHoopVisualNode");
		debug ("INHoopVisualNode ()");
		
		//this.setBorder(INHoopShadowBorder.getSharedInstance());
		
		//setLayout(new BorderLayout());
		setLayout(new BoxLayout (this,BoxLayout.Y_AXIS));
		setBorder(BorderFactory.createCompoundBorder(INHoopShadowBorder.getSharedInstance(), BorderFactory.createBevelBorder(BevelBorder.RAISED)));
				
		this.setBackground(new Color (50,50,50));
		this.setOpaque(true);
		
		titleLabel=new JLabel ();
		titleLabel.setFont(new Font("Dialog", 1, 10));
		titleLabel.setMinimumSize(new Dimension (50,24));
		titleLabel.setPreferredSize(new Dimension (50,24));
		titleLabel.setForeground(Color.white);
		titleLabel.setOpaque(false);
		this.add(titleLabel);
		
		contentArea=new JPanel ();
		contentArea.setBackground(new Color (29,29,29));
		contentArea.setBorder(BorderFactory.createLoweredBevelBorder());
		this.add (contentArea);
	}
	/** 
	 * @param title
	 */
	public void setTitle(String title) 
	{
		this.title = title;
		titleLabel.setText(title);
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
		titleLabel.setText(title);
	}
	/** 
	 * 
	 */
	public String getText() 
	{
		return title;
	}		
}
	