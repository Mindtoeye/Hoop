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

//import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
//import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
///import javax.swing.JList;
import javax.swing.JScrollPane;
//import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.util.mxGraphTransferable;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource;
import com.mxgraph.util.mxRectangle;

import edu.cmu.cs.in.base.HoopLink;
//import edu.cmu.cs.in.base.HoopProperties;
import edu.cmu.cs.in.base.HoopStringTools;
import edu.cmu.cs.in.controls.HoopButtonBox;
//import edu.cmu.cs.in.controls.HoopShadowBorder;
import edu.cmu.cs.in.controls.HoopJTreeHoopRenderer;
import edu.cmu.cs.in.controls.HoopThreadView;
import edu.cmu.cs.in.controls.base.HoopEmbeddedJPanel;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;

/**
 * 
 */
public class HoopTreeList extends HoopEmbeddedJPanel implements MouseListener, ActionListener
{
	private static final long serialVersionUID = 1L;		
	private JTree tree=null;
	protected JLabel selectedEntry = null;	
	protected mxEventSource eventSource = new mxEventSource(this);
	
	private HoopJTreeHoopRenderer treeHoopRenderer=null;
	private mxGraphTransferable transferable=null;
		
    private JButton expandButton=null;
    private JButton foldButton=null;
    private JButton inverseButton=null;
    private JButton selectedButton=null;		
    
    private HoopThreadView threadView=null;
	
	/**
	 * 
	 */
	public HoopTreeList ()  
    {    	
		setClassName ("HoopTreeList");
		debug ("HoopTreeList ()");    	
		
		this.setSingleInstance(true);
				
		Box mainBox = new Box (BoxLayout.Y_AXIS);
					    
	    //Box buttonBox = new Box (BoxLayout.X_AXIS);
		
		HoopButtonBox buttonBox=new HoopButtonBox ();
	   	buttonBox.setMinimumSize(new Dimension (100,24));
	   	buttonBox.setPreferredSize(new Dimension (200,24));
	   	buttonBox.setMaximumSize(new Dimension (2000,24));		
	    
	    expandButton=new JButton ();
	    expandButton.setFont(new Font("Dialog", 1, 8));
	    expandButton.setPreferredSize(new Dimension (20,20));
	    expandButton.setMaximumSize(new Dimension (20,20));
	    //expandButton.setText("All");
	    expandButton.setIcon(HoopLink.getImageByName("tree-expand-icon.png"));
	    expandButton.addActionListener(this);

	    buttonBox.addComponent (expandButton);
	    //buttonBox.add (Box.createRigidArea(new Dimension(2,0)));
	    
	    foldButton=new JButton ();
	    foldButton.setFont(new Font("Dialog", 1, 8));
	    foldButton.setPreferredSize(new Dimension (20,20));
	    foldButton.setMaximumSize(new Dimension (20,20));
	    foldButton.setIcon(HoopLink.getImageByName("tree-fold-icon.png"));
	    //foldButton.setText("None");
	    foldButton.addActionListener(this);
	    
	    buttonBox.addComponent (foldButton);
	    //buttonBox.add (Box.createRigidArea(new Dimension(2,0)));
	    
	    inverseButton=new JButton ();
	    inverseButton.setFont(new Font("Dialog", 1, 8));
	    inverseButton.setPreferredSize(new Dimension (75,20));
	    //inverseButton.setMaximumSize(new Dimension (2000,20));
	    inverseButton.setText("Inverse");
	    inverseButton.addActionListener(this);
	    
	    buttonBox.addComponent (inverseButton);
	    //buttonBox.add (Box.createRigidArea(new Dimension(2,0)));
	    
	    selectedButton=new JButton ();
	    selectedButton.setFont(new Font("Dialog", 1, 8));
	    selectedButton.setPreferredSize(new Dimension (75,20));
	    //selectedButton.setMaximumSize(new Dimension (2000,20));
	    selectedButton.setText("Selected");
	    selectedButton.addActionListener(this);
	    selectedButton.setEnabled(false);
	    
	    buttonBox.addComponent (selectedButton);
	    //buttonBox.add (Box.createRigidArea(new Dimension(2,0)));
	    
	    //buttonBox.add(Box.createHorizontalGlue());
		
		treeHoopRenderer=new HoopJTreeHoopRenderer ();
				
		tree=new JTree(toTreeModel ());
		tree.setFont(new Font("Dialog", 1, 10)); // overwritten by cellrenderer?
		//tree.setBackground(HoopProperties.graphBackgroundColor);
		tree.setCellRenderer (treeHoopRenderer);
		tree.getSelectionModel().setSelectionMode (TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.setRootVisible(false);
		tree.setBorder(BorderFactory.createEmptyBorder(4,4,4,4));
		tree.putClientProperty("JTree.lineStyle", "Horizontal");
		tree.setDragEnabled(true);
		
		treeHoopRenderer.setTree(tree);
		
		JScrollPane scrollPane=new JScrollPane(tree);
		scrollPane.setBorder(BorderFactory.createEmptyBorder(4,4,4,4));
				
		TreeNode root = (TreeNode) tree.getModel().getRoot();
		expandAll (tree, new TreePath(root));				
		
		//initTreeDnD ();
		
		tree.addMouseListener(this);
		
		tree.setTransferHandler (new TransferHandler()
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
	            	
	            	HoopBase hoopTemplate=(HoopBase) node.getUserObject();
	            	
	            	debug ("Assigning hoop class: " + hoopTemplate.getClassName() + " to transferable");
	            	
	        		mxCell cell = new mxCell (hoopTemplate.getClassName(),new mxGeometry (0,0,150,100),"label;image=/assets/images/gear.png");
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
		
		//>----------------------------------------------------------------------------
		
		threadView=new HoopThreadView ();
		threadView.setMinimumSize(new Dimension (50,100));
				
		//>----------------------------------------------------------------------------		
		
		mainBox.add(buttonBox);
		mainBox.add(scrollPane);			
		mainBox.add (threadView);
		
		setContentPane (mainBox);			
    }
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
	public void collapseAll(JTree tree, TreePath parent,Boolean isRoot) 
	{
	    TreeNode node = (TreeNode) parent.getLastPathComponent();
	    if (node.getChildCount() >= 0) 
	    {
	    	for (Enumeration<?> e = node.children(); e.hasMoreElements();) 
	    	{
	    		TreeNode n = (TreeNode) e.nextElement();
	    		TreePath path = parent.pathByAddingChild(n);
	    		collapseAll(tree, path,false);
	    	}
	    }
	    
	    if (isRoot==false)
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
	/**
	 * 
	 */
	public void setSelectionEntry (JLabel entry,mxGraphTransferable transferable)
	{
		debug ("setSelectionEntry ()");
				
		JLabel previous = selectedEntry;
		selectedEntry = entry;

		/*
		if (previous != null)
		{
			previous.setBorder(null);
			previous.setOpaque(false);
		}
		else
			debug ("previous==null");
		*/			

		/*
		if (selectedEntry != null)
		{
			selectedEntry.setBorder(HoopShadowBorder.getSharedInstance());
			selectedEntry.setOpaque(true);
		}
		else
			debug ("selectedEntry==null");
		*/			

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
    	ArrayList<String> hoopCategories=HoopLink.hoopManager.getHoopCategories();	
    	ArrayList<HoopBase> hoopTemplates=HoopLink.hoopManager.getHoopTemplates();    	
    	
    	DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
		
    	if ((hoopTemplates!=null) && (hoopCategories!=null))
    	{    	
    		for (int j=0;j<hoopCategories.size();j++)
    		{
    			String aCat=hoopCategories.get(j);
    			
				DefaultMutableTreeNode catNode = new DefaultMutableTreeNode(aCat);
				catNode.setUserObject(new String (HoopStringTools.getNamespaceLast(aCat)));
				root.add(catNode);
				    			
    			for (int i=0;i<hoopTemplates.size();i++)
    			{
    				HoopBase hoopTemplate=hoopTemplates.get(i);
    				
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
            tree.setCursor(Cursor.getDefaultCursor());
        } 
        else 
        {
            tree.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            
            setSelectionEntry(treeHoopRenderer, transferable);		            
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
	public void actionPerformed(ActionEvent event) 
	{		
		debug ("actionPerformed ("+event.getActionCommand()+")");
		
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
			collapseAll (tree, new TreePath(root),true);
		}				
	}    
}
