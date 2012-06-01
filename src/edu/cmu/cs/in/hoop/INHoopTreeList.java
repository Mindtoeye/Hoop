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
 * 	http://www.apl.jhu.edu/~hall/java/Swing-Tutorial/Swing-Tutorial-JTree.html
 * 
 */

package edu.cmu.cs.in.hoop;

import java.awt.Font;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import edu.cmu.cs.in.base.INHoopLink;
import edu.cmu.cs.in.controls.base.INEmbeddedJPanel;
import edu.cmu.cs.in.hoop.base.INHoopBase;

/**
 * 
 */
public class INHoopTreeList extends INEmbeddedJPanel
{
	private static final long serialVersionUID = 1L;
		
	/**
	 * 
	 */
	public INHoopTreeList ()  
    {    	
		setClassName ("INHoopTreeList");
		debug ("INHoopTreeList ()");    	
		
		this.setSingleInstance(true);
		
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
		
		if (INHoopLink.hoopTemplates!=null)
		{    	
			for (int i=0;i<INHoopLink.hoopTemplates.size();i++)
			{
				INHoopBase hoopTemplate=INHoopLink.hoopTemplates.get(i);
				DefaultMutableTreeNode child = new DefaultMutableTreeNode(hoopTemplate.getClassName());
				root.add(child);
			}	
		}	
		
		JTree tree = new JTree(root);
		tree.setFont(new Font("Dialog", 1, 10));
		
		setContentPane (new JScrollPane(tree));		
    }
	/**
	 *
	 */	
	public void updateContents() 
	{
		debug ("updateContents ()");

	}		
}