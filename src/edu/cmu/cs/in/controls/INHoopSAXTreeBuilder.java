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

import javax.swing.tree.DefaultMutableTreeNode;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 */
class INHoopSAXTreeBuilder extends DefaultHandler
{      
	private DefaultMutableTreeNode currentNode = null;
	private DefaultMutableTreeNode previousNode = null;
	private DefaultMutableTreeNode rootNode = null;

	/**
	 *
	 */
	public INHoopSAXTreeBuilder (DefaultMutableTreeNode root)
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
