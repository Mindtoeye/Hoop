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

/*
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.tree.*;
*/

package edu.cmu.cs.in.hoop.visualizers;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.base.kv.HoopKV;
import edu.cmu.cs.in.controls.base.HoopEmbeddedJPanel;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;


import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.parser.lexparser.Options;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreePrint;
import edu.stanford.nlp.parser.ui.TreeJPanel;

/**
 * 
 */
public class HoopParseTreeViewer extends HoopEmbeddedJPanel implements ActionListener, MouseListener 
{  		
	private static final long serialVersionUID = 1L;
	
	private HoopBase inHoop=null;
	
	private JList sentenceList=null;
	
	private DefaultListModel model=null;
	
	private JTextArea entry=null;
	private JButton parseButton=null;
	
	private String theLexicalizedParserName = "englishPCFG.ser.gz";
	private LexicalizedParser theLexicalizedParser = null;
	
	private TreeJPanel treePanel=null;
	
	/**
	 * 
	 */
	public HoopParseTreeViewer() 
    {				
		//this.setLayout(new BoxLayout (this,BoxLayout.Y_AXIS));				
		
		Options theseOptions = new Options();
		theseOptions.setOptions(new String[] {});
		String theseExtraFlags[] = new String[] { "-maxLength", "256", "-retainTmpSubcategories" };
		
		//theLexicalizedParser = LexicalizedParser.loadModel(theLexicalizedParserName, theseOptions, theseExtraFlags);
		theLexicalizedParser = LexicalizedParser.loadModel();
		
		model = new DefaultListModel();
		
		sentenceList=new JList (model);
		sentenceList.setOpaque(true);
		sentenceList.setBackground(new Color (220,220,220));
		sentenceList.addMouseListener (this);
		
		JScrollPane scroller=new JScrollPane (sentenceList);
		scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		Box controlBox=Box.createHorizontalBox();
				
		entry=new JTextArea ();
		entry.setFont(new Font("Dialog", 1, 10));
		//entry.setBorder(blackborder);
		entry.setMinimumSize(new Dimension (100,25));
		entry.setPreferredSize(new Dimension (200,25));		
		entry.setMaximumSize(new Dimension (200,25));
				
		//entry.setText("The quick brown fox jumps over the lazy dog.");
		
		parseButton=new JButton ();
		//parseButton.setIcon(HoopLink.imageIcons [8]);
		parseButton.setMargin(new Insets(1, 1, 1, 1));
		parseButton.setText("Parse");
		parseButton.setFont(new Font("Courier",1,8));
		parseButton.setPreferredSize(new Dimension (20,16));
		parseButton.addActionListener(this);
		
		controlBox.add(entry);
		controlBox.add(parseButton);
		controlBox.add(Box.createHorizontalGlue());
		
		treePanel=new TreeJPanel ();
		
		JPanel content=(JPanel) getContentPane ();
		content.setLayout(new BoxLayout (content,BoxLayout.Y_AXIS));
		
		//this.add(controlBox);
		//this.add(scroller);		
		//this.add (treePanel);
		
		content.add(controlBox);
		content.add(scroller);		
		content.add (treePanel);
    }
	/**
	 *
	 */	
	public HoopBase getInHoop() 
	{
		return inHoop;
	}
	/**
	 *
	 */	
	public void setInHoop(HoopBase inHoop) 
	{
		debug ("setInHoop ()");
		
		this.inHoop = inHoop;
	}	
	/**
	 *
	 */	
	public void updateContents() 
	{
		debug ("updateContents ()");
			
		if (this.inHoop==null)
		{
			debug ("Error: no input Hoop available");
			return;
		}
				
		ArrayList <HoopKV> inData=inHoop.getData();
		
		if (inData==null)
		{
			debug ("Error: no input data available");
			return;
		}
		
		if (inData.size()==0)
		{
			debug ("Error: empty data!");
			return;
		}
		
		model = new DefaultListModel();
					
		for (int t=0;t<inData.size();t++)
		{
			HoopKV aKV=inData.get(t);

			model.addElement(aKV.getValue());
		}		
				
		sentenceList.setModel(model);		
	}
	/**
	 * 
	 */
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		processInput (entry.getText ());		
	}
	/**
	 * 
	 */
	private void processInput (String aSentence)
	{
		debug ("processInput ("+aSentence+")");
		
		Tree thisTree = theLexicalizedParser.apply(aSentence);
		
		debug ("We have a tree!");
		
		PrintWriter pw = new PrintWriter(System.out, true); 
		
		TreePrint posPrinter = new TreePrint("wordsAndTags");
		posPrinter.printTree(thisTree, pw);
		
		ArrayList ar=thisTree.taggedYield();
		debug (ar.toString());
		
		for (Tree subtree : thisTree) 
		{ 
			if (thisTree.isLeaf()==true)
			{
				debug ("Tree leaf: " + subtree.label().value());
			}
			else
			{
				debug ("Tree node: " + subtree.label().value());				
			}	
		}		
		
		treePanel.setTree(thisTree);
	}
	/**
	 * 
	 */
	@Override
	public void mouseClicked(MouseEvent e) 
	{
		debug ("mouseClicked ()");
		
        JList list = (JList)e.getSource();
        
        if (e.getClickCount() == 2) 
        {
            int index = sentenceList.locationToIndex(e.getPoint());
            
            String selectedSentence=(String) model.get(index);
            
            processInput (selectedSentence);
        }		
	}
	/**
	 * 
	 */
	@Override
	public void mousePressed(MouseEvent e) 
	{
		
	}
	/**
	 * 
	 */	
	@Override
	public void mouseReleased(MouseEvent e) 
	{

	}
	/**
	 * 
	 */	
	@Override
	public void mouseEntered(MouseEvent e) 
	{
		
	}
	/**
	 * 
	 */	
	@Override
	public void mouseExited(MouseEvent e) 
	{
		
	}
}
