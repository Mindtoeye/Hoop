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

import java.awt.Font;
import java.io.StringReader;

import javax.swing.BoxLayout;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.apache.xerces.parsers.SAXParser;
import org.xml.sax.InputSource;

import edu.cmu.cs.in.base.HoopLink;
//import edu.cmu.cs.in.controls.HoopHTMLPane;
import edu.cmu.cs.in.controls.HoopSAXTreeBuilder;
import edu.cmu.cs.in.controls.base.HoopEmbeddedJPanel;

/*
 * 
 */
public class HoopXMLFileViewer extends HoopEmbeddedJPanel
{  
	private static final long serialVersionUID = -1L;
	
	private JTree tree=null;
	private HoopSAXTreeBuilder saxTree = null;
    private SAXParser saxParser =null;    
    private DefaultMutableTreeNode top=null;
    private DefaultTreeModel model=null;
    
	private String testXML="<?xml version=\"1.0\" encoding=\"UTF-8\"?><games><game genre=\"rpg\">XML Invaders</game><game genre=\"rpg\">A Node in the XPath</game><game genre=\"rpg\">XPath Racers</game></games>";
	
	/**
	 * 
	 */
	public HoopXMLFileViewer () 
    {
		setClassName ("HoopXMLFileViewer");
		debug ("HoopXMLFileViewer ()");  
		
		this.setLayout(new BoxLayout (this,BoxLayout.Y_AXIS));
		
		top = new DefaultMutableTreeNode("XML Document"); 
        
		saxTree = new HoopSAXTreeBuilder (top);         
		saxParser = new SAXParser();
		saxParser.setContentHandler(saxTree);
		
		tree = new JTree(saxTree.getTree()); 
		tree.setFont(new Font("Dialog", 1, 10));
		JScrollPane scrollPane = new JScrollPane(tree);
		
		model=(DefaultTreeModel) tree.getModel();
		
		setContent (testXML);              		       	
       	setContentPane(scrollPane);       	
    }
	/**
	 * 
	 */
	public void setContent (String aContent)
	{
		debug ("setContent ()");
		
		top = new DefaultMutableTreeNode("XML Document"); 
        
		saxTree = new HoopSAXTreeBuilder (top);         
		saxParser = new SAXParser();
		saxParser.setContentHandler(saxTree);
		
		try 
		{
			saxParser.parse(new InputSource(new StringReader(aContent)));
		}
		catch(Exception ex)
		{
			top.add(new DefaultMutableTreeNode(ex.getMessage()));
			debug ("Error parsing xml!");
			debug (ex.toString());
		}
		
		model.setRoot(top);
	}
	/**
	 * 
	 */
	public void showFile (String aFile)
	{
		debug ("showFile (String)");
		
		String text=HoopLink.fManager.loadContents(aFile);
		
		setContent (text);
	}	
}
