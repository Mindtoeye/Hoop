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
 *  http://docs.oracle.com/javase/tutorial/uiswing/dnd/intro.html
 * 
 */

package edu.cmu.cs.in.hoop;

import java.util.ArrayList;
import java.util.Enumeration;
import java.awt.Font;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import edu.cmu.cs.in.base.INHoopLink;
import edu.cmu.cs.in.controls.base.INEmbeddedJPanel;
import edu.cmu.cs.in.hoop.project.INHoopProject;
import edu.cmu.cs.in.hoop.project.INHoopProjectFile;

/**
 * 
 */
public class INHoopProjectPanel extends INEmbeddedJPanel implements MouseListener
{
	private static final long serialVersionUID =- 1L;		
	private JTree tree=null;
	
	/**
	 * 
	 */
	public INHoopProjectPanel ()  
    {    	
		setClassName ("INHoopProjectPanel");
		debug ("INHoopProjectPanel ()");    	
		
		this.setSingleInstance(true);
		
		Box mainBox = new Box (BoxLayout.Y_AXIS);
					    				
		//tree=new JTree(toTreeModel ());
		tree=new JTree();
		tree.setFont(new Font("Dialog", 1, 10)); // overwritten by cellrenderer?
		tree.getSelectionModel().setSelectionMode (TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.setRootVisible(true);
		tree.setBorder(BorderFactory.createEmptyBorder(4,4,4,4));
		//tree.putClientProperty("JTree.lineStyle", "Horizontal");
		tree.setDragEnabled(false);
				
		JScrollPane scrollPane=new JScrollPane(tree);
		scrollPane.setBorder(BorderFactory.createEmptyBorder(4,4,4,4));
				
		TreeNode root = (TreeNode) tree.getModel().getRoot();
		expandAll (tree, new TreePath(root));				
				
		tree.addMouseListener(this);
		
		mainBox.add(scrollPane);
		
		setContentPane (mainBox);			
		
		updateContents();
    }
	/**
	 * 
	 */	
	public void updateContents() 
	{
		debug ("updateContents ()");
		
		INHoopProject proj=INHoopLink.project;
		
		if (proj==null)
		{
			debug ("Error: no project available yet");
			return;
		}	
		
    	DefaultMutableTreeNode root = new DefaultMutableTreeNode(proj.getInstanceName());
		  			
    	ArrayList <INHoopProjectFile> files=proj.getFiles();
    	
    	for (int i=0;i<files.size();i++)
    	{
    		INHoopProjectFile aFile=files.get(i);
    		
			DefaultMutableTreeNode catNode = new DefaultMutableTreeNode(aFile.getInstanceName());
			//catNode.setUserObject(aFile);
			root.add(catNode);
    	}		    	
    	
    	DefaultTreeModel model = new DefaultTreeModel(root);
    	
    	tree.setModel(model);
	}	
	/**
	 *
	 */	
	public void collapseAll(JTree tree, TreePath parent) 
	{
	    TreeNode node = (TreeNode) parent.getLastPathComponent();
	    if (node.getChildCount() >= 0) 
	    {
	    	for (Enumeration<?> e = node.children(); e.hasMoreElements();) 
	    	{
	    		TreeNode n = (TreeNode) e.nextElement();
	    		TreePath path = parent.pathByAddingChild(n);
	    		collapseAll(tree, path);
	    	}
	    }
	    	   
	    tree.collapsePath(parent);
	  }		
	/**
	 *
	 */	
	public void expandAll(JTree tree, TreePath parent) 
	{
	    TreeNode node = (TreeNode) parent.getLastPathComponent();
	    if (node.getChildCount() >= 0) 
	    {
	    	for (Enumeration<?> e = node.children(); e.hasMoreElements();) 
	    	{
	    		TreeNode n = (TreeNode) e.nextElement();
	    		TreePath path = parent.pathByAddingChild(n);
	    		expandAll(tree, path);
	    	}
	    }
	    
	    tree.expandPath(parent);	    
	}
	@Override
	public void mouseClicked(MouseEvent arg0) 
	{
		
	}
	/**
	 * 
	 */
	@Override
	public void mouseEntered(MouseEvent arg0) 
	{
		
	}
	/**
	 * 
	 */
	@Override
	public void mouseExited(MouseEvent arg0) 
	{
		
	}
	/**
	 * 
	 */
	@Override
	public void mousePressed(MouseEvent e) 
	{
		debug ("mousePressed ()");
		
		int x = (int) e.getPoint().getX();
        int y = (int) e.getPoint().getY();
        
        TreePath path = tree.getPathForLocation(x, y);
        if (path == null) 
        {
            //tree.setCursor(Cursor.getDefaultCursor());
        } 
        else 
        {
            //tree.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));            		            
        }								
	}
	/**
	 * 
	 */
	@Override
	public void mouseReleased(MouseEvent arg0) 
	{
		
	}	
	/**
	 *
	 */	
	@Override
	public void componentHidden(ComponentEvent arg0) 
	{
		debug ("componentHidden ()");
		
		debug ("Disabling certain file menu options");
		
	}
	/**
	 *
	 */	
	@Override
	public void componentShown(ComponentEvent arg0) 
	{
		debug ("componentShown ()");
		
		super.componentShown (arg0);
		
		debug ("Enabling certain file menu options");
		
	}		
}
