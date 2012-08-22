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
import java.util.Iterator;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JScrollPane;

import com.sleepycat.collections.StoredMap;

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.base.kv.HoopKVDocument;
import edu.cmu.cs.in.controls.base.HoopEmbeddedJPanel;
import edu.cmu.cs.in.search.HoopDataSet;

/** 
 * @author vvelsen
 *
 */
public class HoopDocumentList extends HoopEmbeddedJPanel implements ActionListener
{	
	private static final long serialVersionUID = 2319368351656283482L;
	private JList docList=null;
	
    private JButton expandButton=null;
    private JButton foldButton=null;
    private JButton inverseButton=null;
    private JButton selectedButton=null;	
    
    private JButton refreshButton=null;
    private JButton configureButton=null;
	
    private int maxShown=20;
    
	/**
	 * Constructs a new frame that is initially invisible.
	 */	
	public HoopDocumentList()
	{		
		setClassName ("HoopDocumentList");
		debug ("HoopDocumentList ()");
		
	   	Box holder = new Box (BoxLayout.Y_AXIS);
	   		   	
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
	    
	    refreshButton=new JButton ();
	    refreshButton.setFont(new Font("Dialog", 1, 8));
	    refreshButton.setPreferredSize(new Dimension (20,20));
	    refreshButton.setMaximumSize(new Dimension (20,20));
	    refreshButton.setIcon(HoopLink.getImageByName("gtk-refresh.png"));
	    refreshButton.addActionListener(this);
	    	    
	    buttonBox.add (refreshButton);
	    buttonBox.add (Box.createRigidArea(new Dimension(2,0)));	 
	    
	    configureButton=new JButton ();
	    configureButton.setFont(new Font("Dialog", 1, 8));
	    configureButton.setPreferredSize(new Dimension (20,20));
	    configureButton.setMaximumSize(new Dimension (20,20));
	    configureButton.setIcon(HoopLink.getImageByName("gtk-preferences.png"));
	    configureButton.addActionListener(this);
	    	    
	    buttonBox.add (configureButton);
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
	    	    
	    buttonBox.add(Box.createHorizontalGlue());	   	
	   		   	
	   	docList=new JList ();	   	
		docList.setCellRenderer (new HoopDocumentListRenderer ());
		
	    JScrollPane docScrollList = new JScrollPane (docList);	   

	    holder.add(buttonBox);
	    holder.add (docScrollList);
	   	
		setContentPane (holder);
   			
		updateContents(); // Just in case we already have something
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
		
		if (HoopLink.dataSet.getData()==null)
		{
			return;
		}
		
		StoredMap<String,HoopKVDocument> docs=HoopLink.dataSet.getData();
		
		DefaultListModel mdl=new DefaultListModel ();
		
		StoredMap<String,HoopKVDocument> map=HoopLink.dataSet.getData();
		
		Iterator<HoopKVDocument> iterator = map.values().iterator();

		int index=0;
		int count=maxShown;
		
		if (docs.size()<maxShown)
			count=docs.size ();
										
		if (map!=null)
		{
			while ((iterator.hasNext()) && (index<count)) 
			{
				HoopKVDocument aDoc=(HoopKVDocument) iterator.next();
				
				mdl.addElement(aDoc);
				
				index++;
			}	
		}
				
		docList.setModel (mdl);		
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
	}			
}
