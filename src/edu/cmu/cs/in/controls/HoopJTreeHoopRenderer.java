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

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.border.Border;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;

import edu.cmu.cs.in.base.HoopRoot;
import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;

/**
 *
 */
public class HoopJTreeHoopRenderer extends JLabel implements TreeCellRenderer
{
	private static final long serialVersionUID = -1L;
	private JTree tree=null;
	private Border border=null;
	
	/**
	 * 
	 */
	public HoopJTreeHoopRenderer ()
	{	
		border = BorderFactory.createEmptyBorder (1,1,1,2);
		
		this.setFont(new Font("Dialog", 1, 10));
		this.setOpaque(true);
		this.setBorder (border);
	}
	/**
	 * 
	 */
	protected void debug (String aMessage)
	{
		HoopBase.debug("HoopJTreeHoopRenderer",aMessage);
	}	
	/**
	 * 
	 */
	public void setTree (JTree aTree)
	{
		tree=aTree;
	}
	/**
	 * 
	 */
	public JTree getTree ()
	{
		return (tree);
	}	
	/**
	 * 
	 */
    public Component getTreeCellRendererComponent (JTree tree,
    											   Object value,
    											   boolean isSelected,
    											   boolean isExpanded, 
    											   boolean isLeaf, 
    											   int row, 
    											   boolean hasFocus) 
    {
    	DefaultMutableTreeNode node=(DefaultMutableTreeNode) value;
    	    	
    	Object userObject=node.getUserObject();
    	    	
    	if (userObject!=null)
    	{
    		if (userObject instanceof String)
    		{
    			setText ((String) userObject);    			
    			setIcon (HoopLink.getImageByName("gtk-open.png"));
    		}
    		else
    		{
    			HoopBase hoop=(HoopBase) userObject;
    			setText (hoop.getHoopDescription());
    			setIcon (HoopLink.getImageByName("hoop.png"));
    		}
    	}
    	else
    		setText (value.toString());
    	
    	if (isSelected==true)
    		this.setBackground(new Color (220,220,220));
    	else
    		this.setBackground(new Color (255,255,255));
    	
    	setEnabled(tree.isEnabled());
    	
    	return this;
    }   
}