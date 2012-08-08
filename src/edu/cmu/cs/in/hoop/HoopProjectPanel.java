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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.controls.HoopJTreeHoopRenderer;
import edu.cmu.cs.in.controls.base.HoopEmbeddedJPanel;
import edu.cmu.cs.in.hoop.project.HoopFile;
import edu.cmu.cs.in.hoop.project.HoopProject;
import edu.cmu.cs.in.hoop.project.HoopProjectFile;
import edu.cmu.cs.in.hoop.project.HoopWrapperFile;

/**
 * 
 */
public class HoopProjectPanel extends HoopEmbeddedJPanel implements MouseListener, ActionListener
{
	private static final long serialVersionUID =- 1L;		
	private JTree tree=null;
	private HoopJTreeHoopRenderer treeHoopRenderer=null;
	
    private JButton expandButton=null;
    private JButton foldButton=null;
    private JButton inverseButton=null;
    private JButton selectedButton=null;		
	
    private JButton refreshButton=null;
    
	/**
	 * 
	 */
	public HoopProjectPanel ()  
    {    	
		setClassName ("HoopProjectPanel");
		debug ("HoopProjectPanel ()");    	
		
		this.setSingleInstance(true);
		
		Box mainBox = new Box (BoxLayout.Y_AXIS);
		
	    Box buttonBox = new Box (BoxLayout.X_AXIS);
	    
	    expandButton=new JButton ();
	    expandButton.setFont(new Font("Dialog", 1, 8));
	    expandButton.setPreferredSize(new Dimension (20,20));
	    expandButton.setMaximumSize(new Dimension (20,20));
	    //expandButton.setText("All");
	    expandButton.setIcon(HoopLink.getImageByName("tree-expand-icon.png"));
	    expandButton.addActionListener(this);

	    buttonBox.add (expandButton);
	    buttonBox.add (Box.createRigidArea(new Dimension(2,0)));
	    
	    foldButton=new JButton ();
	    foldButton.setFont(new Font("Dialog", 1, 8));
	    foldButton.setPreferredSize(new Dimension (20,20));
	    foldButton.setMaximumSize(new Dimension (20,20));
	    foldButton.setIcon(HoopLink.getImageByName("tree-fold-icon.png"));
	    //foldButton.setText("None");
	    foldButton.addActionListener(this);
	    
	    buttonBox.add (foldButton);
	    buttonBox.add (Box.createRigidArea(new Dimension(2,0)));
	    
	    inverseButton=new JButton ();
	    inverseButton.setFont(new Font("Dialog", 1, 8));
	    inverseButton.setPreferredSize(new Dimension (75,20));
	    //inverseButton.setMaximumSize(new Dimension (2000,20));
	    inverseButton.setText("Inverse");
	    inverseButton.addActionListener(this);
	    inverseButton.setEnabled(false);
	    
	    buttonBox.add (inverseButton);
	    buttonBox.add (Box.createRigidArea(new Dimension(2,0)));
	    
	    selectedButton=new JButton ();
	    selectedButton.setFont(new Font("Dialog", 1, 8));
	    selectedButton.setPreferredSize(new Dimension (75,20));
	    //selectedButton.setMaximumSize(new Dimension (2000,20));
	    selectedButton.setText("Selected");
	    selectedButton.addActionListener(this);
	    selectedButton.setEnabled(false);
	    
	    buttonBox.add (selectedButton);
	    buttonBox.add (Box.createRigidArea(new Dimension(2,0)));
	    
	    refreshButton=new JButton ();
	    refreshButton.setFont(new Font("Dialog", 1, 8));
	    refreshButton.setPreferredSize(new Dimension (20,20));
	    refreshButton.setMaximumSize(new Dimension (20,20));
	    refreshButton.setIcon(HoopLink.getImageByName("gtk-refresh.png"));
	    refreshButton.addActionListener(this);
	    	    
	    buttonBox.add (refreshButton);
	    buttonBox.add (Box.createRigidArea(new Dimension(2,0)));	    
	    
	    buttonBox.add(Box.createHorizontalGlue());
				
		treeHoopRenderer=new HoopJTreeHoopRenderer ();
					    				
		tree=new JTree();
		tree.setFont(new Font("Dialog", 1, 10)); // overwritten by cellrenderer?
		tree.getSelectionModel().setSelectionMode (TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.setRootVisible(true);
		tree.setBorder(BorderFactory.createEmptyBorder(4,4,4,4));
		tree.setCellRenderer (treeHoopRenderer);
		tree.setDragEnabled(false);
		tree.addMouseListener (this);
		
		JScrollPane scrollPane=new JScrollPane(tree);
		scrollPane.setBorder(BorderFactory.createEmptyBorder(4,4,4,4));
				
		TreeNode root = (TreeNode) tree.getModel().getRoot();
		expandAll (tree, new TreePath(root));				
				
		tree.addMouseListener(this);
		
		mainBox.add(buttonBox);
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
		
		HoopProject proj=HoopLink.project;
		
		if (proj==null)
		{
			debug ("Error: no project available yet");
			return;
		}	
		
    	DefaultMutableTreeNode root = new DefaultMutableTreeNode(proj.getInstanceName());
    	root.setUserObject(proj);
		  			
    	ArrayList <HoopProjectFile> files=proj.getFiles();
    	
    	for (int i=0;i<files.size();i++)
    	{
    		HoopFile testFile=files.get(i);
    		
    		if (testFile instanceof HoopProjectFile)
    		{
    			HoopProjectFile aFile=(HoopProjectFile) testFile;
    		
    			DefaultMutableTreeNode catNode = new DefaultMutableTreeNode(aFile.getInstanceName());
    			catNode.setUserObject(aFile);
			
    			root.add(catNode);
    		}
    		else
    		{
    			if (testFile.getIsDir()==true)
    			{
    				
    			}
    			else
    			{
    				
    			}
    		}
    	}		    	
    	
    	DefaultTreeModel model = new DefaultTreeModel(root);
    	    	
    	tree.setModel(model);
    	
    	//treeHoopRenderer.setTree(tree);
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
	
		if (e.getClickCount()==2)
		{				
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

			if (node!=null)
			{
				if (node.getUserObject()!=null)
				{
					if (node.getUserObject() instanceof HoopWrapperFile)
					{
						HoopWrapperFile anExternalFile= (HoopWrapperFile) node.getUserObject();
						
						alert ("Opening linked file ["+anExternalFile.getInstanceName()+"] with external viewer");
						
						HoopTextViewer test=(HoopTextViewer) HoopLink.getWindow("Text Viewer");
						
						if (test==null)
						{
							HoopLink.addView ("Text Viewer",new HoopTextViewer(),HoopLink.center);
							test=(HoopTextViewer) HoopLink.getWindow("Text Viewer");
						}	
						
						test.showFile(anExternalFile);
						
						HoopLink.popWindow ("Text Viewer");						
					}
				}
			}
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
	public void componentShown(ComponentEvent event) 
	{
		debug ("componentShown ()");
		
		super.componentShown (event);
		
		debug ("Enabling certain file menu options");
		
	}
	@Override
	public void actionPerformed(ActionEvent event) 
	{
		debug ("actionPerformed ()");

		//String act=event.getActionCommand();
		JButton button = (JButton)event.getSource();
		
		if (button==expandButton)
		{
			TreeNode root = (TreeNode) tree.getModel().getRoot();
			expandAll (tree, new TreePath(root));
		}		
		
		if (button==foldButton)
		{
			TreeNode root = (TreeNode) tree.getModel().getRoot();
			collapseAll (tree, new TreePath(root));
		}						
		
		if (button==refreshButton)
		{
			HoopProject proj=HoopLink.project;
			
			if (proj==null)
			{
				debug ("Error: no project available yet");
				return;
			}	
			
			if (proj.refresh ()==true)
			{
				updateContents();
			}
		}
	}		
}
