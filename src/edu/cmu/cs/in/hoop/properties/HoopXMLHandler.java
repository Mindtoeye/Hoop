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

package edu.cmu.cs.in.hoop.properties;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import edu.cmu.cs.in.base.HoopRoot;

/**
 * 
 */
public class HoopXMLHandler extends DefaultHandler 
{

	private JTree xmlJTree;
	//DefaultTreeModel treeModel;
	int lineCounter;
	DefaultMutableTreeNode base = new DefaultMutableTreeNode("XML Viewer");
	//static HoopXMLViewer treeViewer = null;
	//JTextField txtFile = null;

    /**
	 * 
	 */
	public void setTree (JTree aTree)
	{
		xmlJTree=aTree;
	}
    /**
	 * 
	 */
    private void debug (String aMessage)
    {
    	HoopRoot.debug ("HoopXMLHandler",aMessage);
    }		
	/**
	 * 
	 */			
	@Override
	public void startElement(String uri, String localName, String tagName, Attributes attr) throws SAXException 
	{
		debug ("startElement ()");
		
		DefaultMutableTreeNode current = new DefaultMutableTreeNode(tagName);

		base.add(current);
		base = current;

		for (int i = 0; i < attr.getLength(); i++) 
		{
			DefaultMutableTreeNode currentAtt = new DefaultMutableTreeNode(attr.getLocalName(i) + " = "
					+ attr.getValue(i));
			base.add(currentAtt);
		}
	}
	/**
	 * 
	 */		
	public void skippedEntity(String name) throws SAXException 
	{
		debug ("Skipped Entity: '" + name + "'");
	}
	/**
	 * 
	 */		
	@Override
	public void startDocument() throws SAXException 
	{
		debug ("startDocument ()");
		
		super.startDocument();
		base=new DefaultMutableTreeNode("XML Viewer");
		DefaultTreeModel model=(DefaultTreeModel) xmlJTree.getModel();
		if (model!=null)
		 model.setRoot (base);
	}
	/**
	 * 
	 */		
	public void characters(char[] ch, int start, int length) throws SAXException 
	{
		String s = new String(ch, start, length).trim();
		if (!s.equals("")) 
		{
			DefaultMutableTreeNode current = new DefaultMutableTreeNode("Descrioption : " + s);
			base.add(current);

		}
	}
	/**
	 * 
	 */		
	public void endElement(String namespaceURI, String localName, String qName) throws SAXException 
	{

		base = (DefaultMutableTreeNode) base.getParent();
	}
	/**
	 * 
	 */		
	/*
	public static void main(String[] args) 
	{
		treeViewer = new HoopXMLViewer();
		// treeViewer.xmlSetUp();
		treeViewer.createUI();

	}
	*/
	/**
	 * 
	 */		
	@Override
	public void endDocument() throws SAXException 
	{
		debug ("endDocument ()");
		
		// Refresh JTree
		((DefaultTreeModel) xmlJTree.getModel()).reload();
		//expandAll(xmlJTree);
	}

	public void xmlSetUp(String aStream) 
	{
		debug ("xmlSetUp ()");
		
		if (aStream==null)
		{
			debug ("Error: input is null");
			return;
		}
		
		SAXParserFactory fact=SAXParserFactory.newInstance();
		SAXParser parser=null;
				
		try 
		{
			parser = fact.newSAXParser();
		} catch (ParserConfigurationException e1) 
		{
			debug ("Error: ParserConfigurationException");
			e1.printStackTrace();
		} catch (SAXException e1) 
		{
			debug ("Error: SAXException");
			e1.printStackTrace();
		}
		
		InputStream is=new ByteArrayInputStream (aStream.getBytes());
		
		/*
		if (is==null)
		{
			debug ("Error: input stream is null");
			return;
		}
		*/
		
		if (parser==null)
		{
			debug ("No parser available, aborting");
			return;
		}
		
		try 
		{
			parser.parse (is,this);			
		} 
		catch (Exception e) 
		{
			debug ("Error parsing XML stream:" + e);
			e.printStackTrace();
		}
	}	
/*
	public void createUI() {

		treeModel = new DefaultTreeModel(base);
		xmlJTree = new JTree(treeModel);
		JScrollPane scrollPane = new JScrollPane(xmlJTree);

		JFrame windows = new JFrame();

		windows.setTitle("XML Tree Viewer using SAX Parser in JAVA");

		JPanel pnl = new JPanel();
		pnl.setLayout(null);
		JLabel lbl = new JLabel("File :");
		txtFile = new JTextField("Selected File Name Here");
		{ JUndo.JTextUndoPacket jtup = JUndo.makeTextUndoable(txtFile); }
		JButton btn = new JButton("Select File");

		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				JFileChooser fileopen = new JFileChooser();
				//FileFilter filter = new FileNameExtensionFilter("xml files", "xml");
				//fileopen.addChoosableFileFilter(filter);
				fileopen.addChoosableFileFilter(null);

				int ret = fileopen.showDialog(null, "Open file");

				if (ret == JFileChooser.APPROVE_OPTION) {
					File file = fileopen.getSelectedFile();
					txtFile.setText(file.getPath() + File.separator + file.getName());
					xmlSetUp(file);
				}

			}
		});
		lbl.setBounds(0, 0, 100, 30);
		txtFile.setBounds(110, 0, 250, 30);
		btn.setBounds(360, 0, 100, 30);
		scrollPane.setBounds(0, 50, 500, 600);

		pnl.add(lbl);

		pnl.add(txtFile);
		pnl.add(btn);

		pnl.add(scrollPane);

		windows.add(pnl);
		windows.setSize(500, 700);
		windows.setVisible(true);
		windows.setDefaultCloseOperation( windows.EXIT_ON_CLOSE);
	}
*/
}
