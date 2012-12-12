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

package edu.cmu.cs.in.hoop;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import com.sleepycat.collections.StoredMap;

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.base.kv.HoopKVDocument;
import edu.cmu.cs.in.base.kv.HoopKVLong;
import edu.cmu.cs.in.controls.HoopButtonBox;
import edu.cmu.cs.in.controls.base.HoopEmbeddedJPanel;
import edu.cmu.cs.in.search.HoopDataSet;

/** 
 *
 */
public class HoopDocumentList extends HoopEmbeddedJPanel implements ActionListener, MouseListener
{	
	private static final long serialVersionUID = 2319368351656283482L;
	private JList<HoopKVDocument> docList=null;
	
    private JButton expandButton=null;
    private JButton foldButton=null;
	    
    private JButton refreshButton=null;
    private JButton configureButton=null;
    
    private JButton prevButton=null;
    private JTextField startIndex=null;
    private JTextField windowSize=null;
    private JButton nextButton=null;
    
    private JButton searchButton=null;
    private JButton sortButton=null;
    
    private JLabel dbInfo=null;
    
    private HoopButtonBox buttonBox=null;
    private HoopButtonBox searchBox=null;
	
    private Integer start=0;
    private Integer maxShown=20;
    
    private JTree threadTree=null;
    
    private JButton treeExpandButton=null;
    private JButton treeFoldButton=null;
    
    private HoopDocumentListRenderer rendererObject=null;
        
	/**
	 * Constructs a new frame that is initially invisible.
	 */	
	public HoopDocumentList()
	{				
		super (HoopLink.getImageByName("gtk-copy.png"));
		
		setClassName ("HoopDocumentList");
		debug ("HoopDocumentList ()");
		
	   	Box holder = new Box (BoxLayout.Y_AXIS);
	   		   	
	   	buttonBox=new HoopButtonBox ();
	   	buttonBox.setMinimumSize(new Dimension (100,24));
	   	buttonBox.setPreferredSize(new Dimension (200,24));
	   	buttonBox.setMaximumSize(new Dimension (2000,24));
	   		    
	    expandButton=new JButton ();
	    expandButton.setFont(new Font("Dialog", 1, 8));
	    expandButton.setPreferredSize(new Dimension (20,20));
	    expandButton.setMaximumSize(new Dimension (20,20));
	    expandButton.setIcon(HoopLink.getImageByName("tree-expand-icon.png"));
	    expandButton.addActionListener(this);

	    buttonBox.addComponent(expandButton);
	    
	    foldButton=new JButton ();
	    foldButton.setFont(new Font("Dialog", 1, 8));
	    foldButton.setPreferredSize(new Dimension (20,20));
	    foldButton.setMaximumSize(new Dimension (20,20));
	    foldButton.setIcon(HoopLink.getImageByName("tree-fold-icon.png"));
	    foldButton.addActionListener(this);
	    
	    buttonBox.addComponent(foldButton);
	    
	    refreshButton=new JButton ();
	    refreshButton.setFont(new Font("Dialog", 1, 8));
	    refreshButton.setPreferredSize(new Dimension (20,20));
	    refreshButton.setMaximumSize(new Dimension (20,20));
	    refreshButton.setIcon(HoopLink.getImageByName("gtk-refresh.png"));
	    refreshButton.addActionListener(this);
	    	    
	    buttonBox.addComponent(refreshButton);
	    
	    configureButton=new JButton ();
	    configureButton.setFont(new Font("Dialog", 1, 8));
	    configureButton.setPreferredSize(new Dimension (20,20));
	    configureButton.setMaximumSize(new Dimension (20,20));
	    configureButton.setIcon(HoopLink.getImageByName("gtk-preferences.png"));
	    configureButton.addActionListener(this);
	    	    
	    buttonBox.addComponent(configureButton);	    
	    	    
	    prevButton=new JButton ();
	    prevButton.setPreferredSize(new Dimension (20,20));
	    prevButton.setMaximumSize(new Dimension (20,20));
	    prevButton.setIcon(HoopLink.getImageByName("gtk-go-back-ltr.png"));
	    prevButton.addActionListener(this);
	    
	    buttonBox.addComponent(prevButton);
	    
	    JLabel startLabel=new JLabel ();
	    startLabel.setFont(new Font("Dialog", 1, 8));
	    startLabel.setText("From: ");
	    
	    buttonBox.addComponent(startLabel);
	    
	    startIndex=new JTextField ();
	    startIndex.setText(start.toString());
	    startIndex.setFont(new Font("Dialog", 1, 8));
	    startIndex.setPreferredSize(new Dimension (50,20));
	    startIndex.setMaximumSize(new Dimension (50,20));	    
	    
	    buttonBox.addComponent(startIndex);
	    
	    JLabel withLabel=new JLabel ();
	    withLabel.setFont(new Font("Dialog", 1, 8));
	    withLabel.setText("show: ");
	    
	    buttonBox.addComponent(withLabel);	    
	    
	    windowSize=new JTextField ();
	    windowSize.setText(maxShown.toString());
	    windowSize.setFont(new Font("Dialog", 1, 8));
	    windowSize.setPreferredSize(new Dimension (50,20));
	    windowSize.setMaximumSize(new Dimension (50,20));
	    
	    buttonBox.addComponent(windowSize);
	    
	    nextButton=new JButton ();
	    nextButton.setPreferredSize(new Dimension (20,20));
	    nextButton.setMaximumSize(new Dimension (20,20));
	    nextButton.setIcon(HoopLink.getImageByName("gtk-go-forward-ltr.png"));
	    nextButton.addActionListener(this);
	    
	    buttonBox.addComponent(nextButton);
	    
	    dbInfo=new JLabel ();
	    dbInfo.setFont(new Font("Dialog", 1, 8));
	    	
	    buttonBox.addComponent(dbInfo);
	    
	   	searchBox=new HoopButtonBox ();
	   	searchBox.setMinimumSize(new Dimension (100,24));
	   	searchBox.setPreferredSize(new Dimension (200,24));
	   	searchBox.setMaximumSize(new Dimension (2000,24));
	   	
	    searchButton=new JButton ();
	    searchButton.setPreferredSize(new Dimension (20,20));
	    searchButton.setMaximumSize(new Dimension (20,20));
	    searchButton.setIcon(HoopLink.getImageByName("gtk-find-and-replace.png"));
	    searchButton.addActionListener(this);
	    
	    searchBox.addComponent(searchButton);
	    
	    sortButton=new JButton ();
	    sortButton.setPreferredSize(new Dimension (20,20));
	    sortButton.setMaximumSize(new Dimension (20,20));
	    sortButton.setIcon(HoopLink.getImageByName("alphab_sort_icon.png"));
	    sortButton.addActionListener(this);	    
	    	   		   	
	    searchBox.addComponent(sortButton);
	    
	    rendererObject=new HoopDocumentListRenderer ();
	    
	   	docList=new JList<HoopKVDocument> ();	   	
		docList.setCellRenderer (rendererObject);
		docList.addMouseListener(this);
		
	    JScrollPane docScrollList = new JScrollPane (docList);
	    docScrollList.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

	    holder.add (buttonBox);	   
	    holder.add (searchBox);
	    holder.add (docScrollList);
	    
	    JTabbedPane tabbedPane = new JTabbedPane();
	    
	    tabbedPane.addTab ("Document View", 
	    				   HoopLink.getImageByName("text_icon.png"), 
	    				   holder,
	                       "Document View");
	    
	    //>-------------------------------------------------------
	    	    
	    threadTree=new JTree ();
		threadTree.setFont(new Font("Dialog", 1, 10)); // overwritten by cellrenderer?
		threadTree.getSelectionModel().setSelectionMode (TreeSelectionModel.SINGLE_TREE_SELECTION);
		threadTree.setRootVisible(false);
		threadTree.setBorder(BorderFactory.createEmptyBorder(4,4,4,4));
		threadTree.setDragEnabled(false);
		threadTree.addMouseListener (this);

	    JScrollPane treeScroller=new JScrollPane (threadTree);
	    
	    Box treeButtonBox = new Box (BoxLayout.X_AXIS);
	    
	    treeExpandButton=new JButton ();
	    treeExpandButton.setFont(new Font("Dialog", 1, 8));
	    treeExpandButton.setPreferredSize(new Dimension (20,20));
	    treeExpandButton.setMaximumSize(new Dimension (20,20));
	    //treeExpandButton.setText("All");
	    treeExpandButton.setIcon(HoopLink.getImageByName("tree-expand-icon.png"));
	    treeExpandButton.addActionListener(this);

	    treeButtonBox.add (treeExpandButton);
	    treeButtonBox.add (Box.createRigidArea(new Dimension(2,0)));
	    
	    treeFoldButton=new JButton ();
	    treeFoldButton.setFont(new Font("Dialog", 1, 8));
	    treeFoldButton.setPreferredSize(new Dimension (20,20));
	    treeFoldButton.setMaximumSize(new Dimension (20,20));
	    treeFoldButton.setIcon(HoopLink.getImageByName("tree-fold-icon.png"));
	    //treeFoldButton.setText("None");
	    treeFoldButton.addActionListener(this);
	    
	    treeButtonBox.add (treeFoldButton);
	    treeButtonBox.add (Box.createRigidArea(new Dimension(2,0)));	    
	    
	    treeButtonBox.add(Box.createHorizontalGlue());
	    
	    JPanel treePanel=new JPanel ();
	    treePanel.setLayout(new BoxLayout (treePanel,BoxLayout.Y_AXIS));
	    treePanel.add (treeButtonBox);
	    treePanel.add (treeScroller);
	    	   	    		
	    tabbedPane.addTab ("Thread View", 
				   		   HoopLink.getImageByName("tree.gif"), 
				   		   treePanel,
                		   "Thread View");	    
	    
	    //>-------------------------------------------------------
	    
		setContentPane (tabbedPane);
   			
		updateContents(); // Just in case we already have something
	}
	/**
	 * 
	 */
	public void reset ()
	{
		debug ("reset ()");
				
		DefaultListModel<HoopKVDocument> mdl=new DefaultListModel<HoopKVDocument> ();
		
		docList.setModel (mdl);
		
	   	DefaultMutableTreeNode root = new DefaultMutableTreeNode("Threads");	   	
    	root.setUserObject("Threads");
		
	   	DefaultTreeModel model = new DefaultTreeModel(root);		
		
		threadTree.setModel(model);
		
		rendererObject.reset ();
	}
	/**
	 *
	 */	
	public void updateContents() 
	{
		debug ("updateContents ()");

		if (HoopLink.dataSet==null)
			HoopLink.dataSet=new HoopDataSet ();

		HoopLink.dataSet.checkDB();
		
		debug ("Dataset checked, showing first entries in document list ...");

		StoredMap<Long,HoopKVDocument> map=HoopLink.dataSet.getData();
		
		if (map==null)
		{
			return;
		}
										
		DefaultListModel<HoopKVDocument> mdl=new DefaultListModel<HoopKVDocument> ();
		
		debug ("There are currently " + map.size() + " entries available");
				
		Integer sizeTransformer=map.size();
		
		dbInfo.setText(sizeTransformer.toString()+" Entries");
		
		Iterator<HoopKVDocument> iterator = map.values().iterator();

		int index=0;
		int count=maxShown;
		
		if (map.size()<maxShown)
			count=map.size ();
										
		if (map!=null)
		{
			while ((iterator.hasNext()) && (index<count)) 
			{
				HoopKVDocument aDoc=(HoopKVDocument) iterator.next();
			
				debug ("Adding document ...");
				
				mdl.addElement(aDoc);
				
				index++;
			}	
		}
				
		docList.setModel (mdl);			
		
		showDocumentThreads ();
	}
	/**
	 * 
	 */
	private void showDocumentThreads ()
	{
		debug ("showDocumentThreads ()");

		if (HoopLink.dataSet==null)
		{
			return;
		}
		
		StoredMap<Long,HoopKVLong> threadData=HoopLink.dataSet.getThreads();
		
		if (threadData==null)
		{
			debug ("Error: no thread data available");
			return;
		}		
		
		StoredMap<Long,HoopKVDocument> map=HoopLink.dataSet.getData();
		
		if (map==null)
		{
			debug ("Error: no document data available");
			return;
		}		
		   	
    	Integer totalShown=maxShown;
    	
    	int dSize=threadData.size();
    	
    	if (dSize<totalShown)
    		totalShown=dSize;
    	
    	debug ("Thread data size: " + dSize + " adjusted: " + totalShown);
    	    	
		Iterator<HoopKVLong> iterator = threadData.values().iterator();

		int index=0;
		int count=maxShown;
		
		if (threadData.size()<maxShown)
			count=threadData.size ();
		
	   	DefaultMutableTreeNode root = new DefaultMutableTreeNode("Threads");	   	
    	root.setUserObject("Threads");
		
	   	DefaultTreeModel model = new DefaultTreeModel(root);
										
	   	int totalDocumentSize=0;
	   	
		while ((iterator.hasNext()) && (index<count)) 
		{
			HoopKVLong aThread=(HoopKVLong) iterator.next();
				
   			DefaultMutableTreeNode aNode=new DefaultMutableTreeNode (aThread.getKeyString ());
   			root.add(aNode);   
				
       		ArrayList <Object> threadContents=aThread.getValuesRaw();
            	
       		debug ("Thread with id " +aThread.getKeyString() + " has " + aThread.getValuesRaw().size() + " entries");
        		
       		totalDocumentSize+=aThread.getValuesRaw().size();
       		
       		for (int j=0;j<threadContents.size();j++)
       		{
       			String docID=(String) threadContents.get(j);
        		
           		DefaultMutableTreeNode threadNode=new DefaultMutableTreeNode (docID);
           		aNode.add(threadNode); 
       		}     			
    			
			index++;
		}	
		
		debug ("There are a total of " + totalDocumentSize + " documents in the database");
		  		    
    	threadTree.setModel(model);
	}	
	/**
	 * 
	 */
	@Override
	public void actionPerformed(ActionEvent event) 
	{
		debug ("actionPerformed ()");

		//String act=event.getActionCommand();
		JButton button = (JButton)event.getSource();
		
		if (button==expandButton)
		{
			//TreeNode root = (TreeNode) tree.getModel().getRoot();
			//expandAll (tree, new TreePath(root));
		}		
		
		if (button==foldButton)
		{
			//TreeNode root = (TreeNode) tree.getModel().getRoot();
			//collapseAll (tree, new TreePath(root));
		}						
		
		if (button==refreshButton)
		{
			updateContents();
		}
		
		if (button==configureButton)
		{
			//updateContents();
		}		
		
		if (button==searchButton)
		{
			HoopSearch test=(HoopSearch) HoopLink.getWindow("Search");
			
			if (test==null)
			{
				HoopLink.addView ("Search",new HoopSearch(),HoopLink.center);
				test=(HoopSearch) HoopLink.getWindow("Search");
			}	
		    			
			//test.showDocument(aDoc);
		
			HoopLink.popWindow ("Search");
		}
		
		if (button==sortButton)
		{
			
		}
	}
	/**
	 * 
	 */
	@Override
	public void mouseClicked(MouseEvent event) 
	{
		debug ("mouseClicked ()");
		
		if (event.getSource()==threadTree)
		{
			debug ("Can't process thread tree double click yet");
		}
		else
		{
			if (event.getClickCount()==2)
			{
				int index = docList.locationToIndex(event.getPoint());
            
				debug ("Double clicked on Item " + index);
            
				HoopKVDocument aDoc=(HoopKVDocument) docList.getModel().getElementAt(index);
            
				if (aDoc!=null)
				{
					HoopTextViewer test=(HoopTextViewer) HoopLink.getWindow("Text Viewer");
    			
					if (test==null)
					{
						HoopLink.addView ("Text Viewer",new HoopTextViewer(),HoopLink.center);
						test=(HoopTextViewer) HoopLink.getWindow("Text Viewer");
					}	
    		    
					test.showDocument(aDoc);
    		
					HoopLink.popWindow ("Text Viewer");
				}
			}
		}	
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
	public void mousePressed(MouseEvent arg0) 
	{
		
	}
	/**
	 * 
	 */	
	@Override
	public void mouseReleased(MouseEvent arg0) 
	{
		
	}			
}
