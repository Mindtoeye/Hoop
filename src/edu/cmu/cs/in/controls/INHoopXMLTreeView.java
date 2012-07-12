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
 * 	Adapted from:
 *   Java, XML, and Web Services Bible
 *   Mike Jasnowski
 *   ISBN: 0-7645-4847-6
 * 
 */

package edu.cmu.cs.in.controls;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.StringReader;
//import java.util.ArrayList;

import javax.swing.*;
import javax.swing.tree.*;

import org.xml.sax.*;
import org.xml.sax.helpers.*;

import org.apache.xerces.parsers.*;

import edu.cmu.cs.in.base.INHoopLink;
//import edu.cmu.cs.in.base.INHoopProperties;
import edu.cmu.cs.in.controls.base.INJPanel;

/**
 *
 */
public class INHoopXMLTreeView extends INJPanel implements ActionListener
{
	private static final long serialVersionUID = -1L;
    private JFileChooser fc=null;
	private SAXTreeBuilder saxTree = null;
	private JTree tree=null;
	private JButton loadExample=null;
    private JButton expandButton=null;
    private JButton foldButton=null;	
    private DefaultTreeModel model=null;
	
	private String testXML="<?xml version=\"1.0\" encoding=\"UTF-8\"?><games><game genre=\"rpg\">XML Invaders</game><game genre=\"rpg\">A Node in the XPath</game><game genre=\"rpg\">XPath Racers</game></games>";
   
	/**
	 * 
	 */
	public INHoopXMLTreeView ()
	{ 
		setClassName ("INHoopXMLTreeView");
		debug ("INHoopXMLTreeView ()");

		this.setLayout(new BoxLayout (this,BoxLayout.Y_AXIS));
		
	    Box buttonBox = new Box (BoxLayout.X_AXIS);
	    buttonBox.setBorder(BorderFactory.createEmptyBorder(1,1,1,2));
	    
	    loadExample=new JButton ();
	    loadExample.setFont(new Font("Dialog", 1, 8));
	    loadExample.setPreferredSize(new Dimension (20,20));
	    loadExample.setMaximumSize(new Dimension (20,20));
	    loadExample.setIcon(INHoopLink.getImageByName("gtk-floppy.png"));
	    loadExample.addActionListener(this);
	    buttonBox.add (loadExample);	    
	    
	    expandButton=new JButton ();
	    expandButton.setFont(new Font("Dialog", 1, 8));
	    expandButton.setPreferredSize(new Dimension (20,20));
	    expandButton.setMaximumSize(new Dimension (20,20));
	    //expandButton.setText("All");
	    expandButton.setIcon(INHoopLink.getImageByName("tree-expand-icon.png"));
	    expandButton.addActionListener(this);
	    buttonBox.add (expandButton);
	    
	    foldButton=new JButton ();
	    foldButton.setFont(new Font("Dialog", 1, 8));
	    foldButton.setPreferredSize(new Dimension (20,20));
	    foldButton.setMaximumSize(new Dimension (20,20));
	    foldButton.setIcon(INHoopLink.getImageByName("tree-fold-icon.png"));	    	    
	    buttonBox.add(foldButton);
	    
	    buttonBox.add(Box.createHorizontalGlue());
		
		DefaultMutableTreeNode top = new DefaultMutableTreeNode("XML Document"); 
              
		saxTree = new SAXTreeBuilder (top); 
              
		try 
		{
			SAXParser saxParser = new SAXParser();
			saxParser.setContentHandler(saxTree);
			saxParser.parse(new InputSource(new StringReader(testXML)));
		}
		catch(Exception ex)
		{
			top.add(new DefaultMutableTreeNode(ex.getMessage()));
		}
              
		tree = new JTree(saxTree.getTree()); 
		tree.setFont(new Font("Dialog", 1, 10));
		JScrollPane scrollPane = new JScrollPane(tree);
		
		model=(DefaultTreeModel) tree.getModel();
              
		/*
		this.setBorder(BorderFactory.createEmptyBorder(INHoopProperties.tabPadding,INHoopProperties.tabPadding,INHoopProperties.tabPadding,INHoopProperties.tabPadding));		
		this.setLayout(new BorderLayout(2,2));
		this.add(scrollPane);
		*/
				
		this.add(buttonBox);
		this.add(scrollPane);
	}

	@Override
	public void actionPerformed(ActionEvent event) 
	{
		debug ("actionPerformed ()");
		
		int returnVal=0;		
		String act=event.getActionCommand();
		JButton button = (JButton)event.getSource();
		
		if (button==loadExample)
		{
			debug ("Command " + act + " on loadStops");

			//fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			fc = new JFileChooser();
	        returnVal=fc.showOpenDialog (this);

	        if (returnVal==JFileChooser.APPROVE_OPTION) 
	        {
	        	Object[] options = {"Yes",
	        	                    "No",
	        	                    "Cancel"};
	        	int n = JOptionPane.showOptionDialog(this,
	        	    "Loading a saved set will override any existing selections, do you want to continue?",
	        	    "IN Info Panel",
	        	    JOptionPane.YES_NO_CANCEL_OPTION,
	        	    JOptionPane.QUESTION_MESSAGE,
	        	    null,
	        	    options,
	        	    options[2]);
	        	
	        	if (n==0)
	        	{          	
	        		File file = fc.getSelectedFile();
	        		
	        		debug ("Loading: " + file.getPath() + " ...");
	        		
	        		DefaultMutableTreeNode top = new DefaultMutableTreeNode(file.getName()); 
	                
	        		saxTree = new SAXTreeBuilder (top); 
	                      
	        		try 
	        		{
	        			SAXParser saxParser = new SAXParser();
	        			saxParser.setContentHandler(saxTree);
	        			saxParser.parse(file.getPath());
	        			
	        			model.setRoot(top);
	        			
	        			//tree.setModel(saxTree.getTree());
	        		}
	        		catch(Exception ex)
	        		{
	        			top.add(new DefaultMutableTreeNode(ex.getMessage()));
	        			
	        			model.setRoot(top);
	        			
	        			//tree.setModel(saxTree.getTree());
	        		}	        			      
	        	}
	        }						
		}
		
		if (button==foldButton)
		{
			
		}
		
		if (button==expandButton)
		{
			
		}		
	}              
}

/**
 *
 */
class SAXTreeBuilder extends DefaultHandler
{      
	private DefaultMutableTreeNode currentNode = null;
	private DefaultMutableTreeNode previousNode = null;
	private DefaultMutableTreeNode rootNode = null;

	/**
	 *
	 */
	public SAXTreeBuilder(DefaultMutableTreeNode root)
	{
		rootNode = root;
	}
	/**
	 *
	 */       
	public void startDocument()
	{
		currentNode = rootNode;
	}
	/**
	 *
	 */       
	public void endDocument()
	{
    	   
	}
	/**
	 *
	 */       
	public void characters(char[] data,int start,int end)
	{
		String str = new String(data,start,end);
		
		if (!str.equals("") && Character.isLetter(str.charAt(0)))
			currentNode.add(new DefaultMutableTreeNode(str));           
	}
	/**
	 *
	 */       
	public void startElement(String uri,String qName,String lName,Attributes atts)
	{
		previousNode = currentNode;
		currentNode = new DefaultMutableTreeNode(lName);
		
		// Add attributes as child nodes //
		attachAttributeList(currentNode,atts);
		previousNode.add(currentNode);              
	}
	/**
	 *
	 */       
	public void endElement(String uri,String qName,String lName)
	{
		if (currentNode.getUserObject().equals(lName))
		{
			currentNode = (DefaultMutableTreeNode)currentNode.getParent();
		}	
	}
	/**
	 *
	 */       
	public DefaultMutableTreeNode getTree()
	{
		return rootNode;
	}
	/**
	 *
	 */       
	private void attachAttributeList(DefaultMutableTreeNode node,Attributes atts)
	{
		for (int i=0;i<atts.getLength();i++)
		{
			String name = atts.getLocalName(i);
			String value = atts.getValue(name);
			node.add(new DefaultMutableTreeNode(name + " = " + value));
		}
	}       
}
