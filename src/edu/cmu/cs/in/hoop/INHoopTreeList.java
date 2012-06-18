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

import java.awt.Cursor;
import java.awt.Font;
//import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
//import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
//import java.awt.dnd.DnDConstants;
//import java.awt.dnd.DragGestureEvent;
//import java.awt.dnd.DragGestureListener;
//import java.awt.dnd.DragSource;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.util.mxGraphTransferable;
//import com.mxgraph.swing.util.mxSwingConstants;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource;
import com.mxgraph.util.mxRectangle;

import edu.cmu.cs.in.base.INHoopLink;
import edu.cmu.cs.in.base.INStringTools;
import edu.cmu.cs.in.controls.INHoopShadowBorder;
import edu.cmu.cs.in.controls.INJTreeHoopRenderer;
import edu.cmu.cs.in.controls.base.INEmbeddedJPanel;
import edu.cmu.cs.in.hoop.base.INHoopBase;

/**
 * 
 */
public class INHoopTreeList extends INEmbeddedJPanel
{
	private static final long serialVersionUID = 1L;		
	private JTree tree=null;
	protected JLabel selectedEntry = null;	
	protected mxEventSource eventSource = new mxEventSource(this);
	
	private INJTreeHoopRenderer treeHoopRenderer=null;
	private mxGraphTransferable transferable=null;
	
	/**
	 * 
	 */
	public INHoopTreeList ()  
    {    	
		setClassName ("INHoopTreeList");
		debug ("INHoopTreeList ()");    	
		
		this.setSingleInstance(true);
		
		treeHoopRenderer=new INJTreeHoopRenderer ();
				
		tree = new JTree(toTreeModel ());
		tree.setFont(new Font("Dialog", 1, 10));
		tree.setCellRenderer (treeHoopRenderer);
		tree.getSelectionModel().setSelectionMode (TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.setRootVisible(false);
		tree.setBorder(BorderFactory.createEmptyBorder(4,4,4,4));
		tree.putClientProperty("JTree.lineStyle", "Horizontal");
		tree.setDragEnabled(true);
		
		treeHoopRenderer.setTree(tree);
		
		JScrollPane scrollPane=new JScrollPane(tree);
		scrollPane.setBorder(BorderFactory.createEmptyBorder(4,4,4,4));
		
		setContentPane (scrollPane);
		
		TreeNode root = (TreeNode) tree.getModel().getRoot();
		expandAll (tree, new TreePath(root));				
		
		//initTreeDnD ();
		
		tree.setTransferHandler(new TransferHandler()
		{
			private static final long serialVersionUID = -1L;

			/**
			 * 
			 */
			public boolean canImport(JComponent comp, DataFlavor[] flavors)
			{				
				//debug ("canImport ()");
				
				return false; // This tree does not allow items to be dragged into it
			}
			/**
			 * Only support copy actions. We do not remove hoops from our tree once
			 * we drag them onto the graph.
			 */
			public int getSourceActions(JComponent comp) 
			{
				debug ("getSourceActions ()");
				
				return COPY;
			}			
			/**
			 * 
			 */
			protected Transferable createTransferable (JComponent c) 
			{
				debug ("createTransferable ()");
				
	            JTree tree = (JTree) c;
	            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();

	            if (node.isLeaf()) 
	            {
	            	debug ("Creating transferable instance of leaf node ...");
	            	
	            	INHoopBase hoopTemplate=(INHoopBase) node.getUserObject();
	            	
	            	debug ("Assigning hoop class: " + hoopTemplate.getClassName() + " to transferable");
	            	
	        		mxCell cell = new mxCell (hoopTemplate.getClassName(),new mxGeometry (0,0,100,100),"label;image=/assets/images/gear.png");
	        		cell.setVertex (true);
	        		
	        		mxRectangle bounds = (mxGeometry) cell.getGeometry().clone();
	        		
	        		transferable = new mxGraphTransferable (new Object[] { cell }, bounds);	            	
	            		            		            	
	            	return (transferable);
	            }
	            else
	            	debug ("Tree node is not a leaf, not dragging");
	               
	            return (null);
	        }			
		});
		
		tree.addMouseListener(new MouseListener()
		{
			public void mousePressed(MouseEvent e)
			{
				debug ("mousePressed ()");
				
				int x = (int) e.getPoint().getX();
		        int y = (int) e.getPoint().getY();
		        
		        TreePath path = tree.getPathForLocation(x, y);
		        if (path == null) 
		        {
		            tree.setCursor(Cursor.getDefaultCursor());
		        } 
		        else 
		        {
		            tree.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		            
		            setSelectionEntry(treeHoopRenderer, transferable);		            
		        }												
			}
			public void mouseClicked(MouseEvent e)
			{
				debug ("mouseClicked ()");				
			}
			public void mouseEntered(MouseEvent e)
			{
				debug ("mouseEntered ()");				
			}
			public void mouseExited(MouseEvent e)
			{
				debug ("mouseExited ()");				
			}
			public void mouseReleased(MouseEvent e)
			{
				debug ("mouseReleased ()");				
			}
		});				
    }
	/**
	 * 
	 */
	/*
	private void initTreeDnD ()
	{
		debug ("initTreeDnD ()");
		
		mxCell cell = new mxCell (treeHoopRenderer,new mxGeometry (0,0,100,100),"label;image=/assets/images/gear.png");
		cell.setVertex (true);
		
		mxRectangle bounds = (mxGeometry) cell.getGeometry().clone();
		
		transferable = new mxGraphTransferable (new Object[] { cell }, bounds);
				
		// Install the handler for dragging nodes into a graph

		DragGestureListener dragGestureListener = new DragGestureListener()
		{
			public void dragGestureRecognized(DragGestureEvent e)
			{
				debug ("dragGestureRecognized");
				
				e.startDrag (null,mxSwingConstants.EMPTY_IMAGE,new Point(),transferable, null);
			}
		};
		
		DragSource dragSource = DragSource.getDefaultDragSource();
		//dragSource.createDefaultDragGestureRecognizer (tree,DnDConstants.ACTION_COPY, dragGestureListener);
	}
	*/
	/**
	 * When this method is called we should assume that we have to re-evaluate all existing hoop templates
	 */	
	public void updateContents() 
	{
		debug ("updateContents ()");
	}	
	/**
	 *
	 */	
	public void collapseAll(JTree tree, TreePath parent) 
	{
	    TreeNode node = (TreeNode) parent.getLastPathComponent();
	    if (node.getChildCount() >= 0) 
	    {
	    	for (Enumeration e = node.children(); e.hasMoreElements();) 
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
	    	for (Enumeration e = node.children(); e.hasMoreElements();) 
	    	{
	    		TreeNode n = (TreeNode) e.nextElement();
	    		TreePath path = parent.pathByAddingChild(n);
	    		expandAll(tree, path);
	    	}
	    }
	    
	    tree.expandPath(parent);	    
	 }
	/**
	 * 
	 */
	public void setSelectionEntry (JLabel entry,mxGraphTransferable transferable)
	{
		debug ("setSelectionEntry ()");
		
		//DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
		
		//treeHoopRenderer.setHoopTemplate (node.getUserObject());
		
		JLabel previous = selectedEntry;
		selectedEntry = entry;

		if (previous != null)
		{
			previous.setBorder(null);
			previous.setOpaque(false);
		}
		else
			debug ("previous==null");		

		if (selectedEntry != null)
		{
			selectedEntry.setBorder(INHoopShadowBorder.getSharedInstance());
			selectedEntry.setOpaque(true);
		}
		else
			debug ("selectedEntry==null");		

		debug ("fireEvent ...");
		
		eventSource.fireEvent(new mxEventObject (mxEvent.SELECT,
												 "entry",
												 selectedEntry,
												 "transferable",
												 transferable,
												 "previous",
												 previous));
	}	
    /**
     * 
     */
    public DefaultMutableTreeNode toTreeModel ()
    {
    	ArrayList<String> hoopCategories=INHoopLink.hoopManager.getHoopCategories();	
    	ArrayList<INHoopBase> hoopTemplates=INHoopLink.hoopManager.getHoopTemplates();    	
    	
    	DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
		
    	if ((hoopTemplates!=null) && (hoopCategories!=null))
    	{    	
    		for (int j=0;j<hoopCategories.size();j++)
    		{
    			String aCat=hoopCategories.get(j);
    			
				DefaultMutableTreeNode catNode = new DefaultMutableTreeNode(aCat);
				catNode.setUserObject(new String (INStringTools.getNamespaceLast(aCat)));
				root.add(catNode);
				    			
    			for (int i=0;i<hoopTemplates.size();i++)
    			{
    				INHoopBase hoopTemplate=hoopTemplates.get(i);
    				
    				if (hoopTemplate.getHoopCategory().toLowerCase().equals(aCat.toLowerCase())==true)
    				{
    					DefaultMutableTreeNode templateNode = new DefaultMutableTreeNode(hoopTemplate.getClassName());
    					templateNode.setUserObject(hoopTemplate);
    					catNode.add(templateNode);    							
    				}	
    			}
    		}	
    	}
    	
    	return (root);
    }
}
