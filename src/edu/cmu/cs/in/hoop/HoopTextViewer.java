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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
//import java.util.Enumeration;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.JTree;
//import javax.swing.JEditorPane;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.border.EtchedBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.MouseInputListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import com.sleepycat.collections.StoredMap;

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.base.io.HoopFileTools;
import edu.cmu.cs.in.base.kv.HoopKVDocument;
import edu.cmu.cs.in.base.kv.HoopKVLong;
import edu.cmu.cs.in.controls.base.HoopEmbeddedJPanel;
import edu.cmu.cs.in.hoop.project.HoopWrapperFile;

/**
 * http://docs.oracle.com/javase/tutorial/uiswing/components/text.html
 */
public class HoopTextViewer extends HoopEmbeddedJPanel implements ActionListener, MouseInputListener, DocumentListener, FocusListener, ItemListener
{	
	private static final long serialVersionUID = -1L;

	private JTextPane textViewer=null;
	private JTextArea lines=null;
	
	private JButton inButton=null;
	private JButton outButton=null;
	
	private JPanel controlPanel=null;
	
	private JCheckBox wordWrap=null;
	private JCheckBox showThread=null;
	private JComboBox<String> filterText=null;
	private JComboBox<String> renderType=null;
	private JComboBox<String> contentType=null;
		
	private JTree threadTree=null;
	
	private int fontSize=10;
	
	String[] renderContent = { "Main Text", "Abstract"};
	String[] renderTypes = { "TEXT", "HTML", "XML", "RTF", "Tokens", "Language Model"};
	
	private HoopKVDocument internalDocument=null;
	
	public class MyHighlighter extends DefaultHighlighter.DefaultHighlightPainter 
	{
		public MyHighlighter(Color c) 
		{
			super(c);
		}
	}	
	
	private Highlighter highlighter=null;
	
	/**
	 * 
	 */
	public HoopTextViewer ()  
    {    	
		setClassName ("HoopTextViewer");
		debug ("HoopTextViewer ()");    	
		
		this.setSingleInstance(false);
		
		Box mainBox = new Box (BoxLayout.Y_AXIS);
								
		Box controlBox = new Box (BoxLayout.X_AXIS);
		
		inButton=new JButton ();
		inButton.setIcon(HoopLink.imageIcons [72]);
		inButton.setMargin(new Insets(1, 1, 1, 1));
		inButton.setFont(new Font("Dialog",1,10));
		inButton.setPreferredSize(new Dimension (20,20));
		inButton.addActionListener(this);
		
		outButton=new JButton ();
		outButton.setIcon(HoopLink.imageIcons [73]);
		outButton.setMargin(new Insets(1, 1, 1, 1));
		outButton.setFont(new Font("Dialog",1,10));
		outButton.setPreferredSize(new Dimension (20,20));
		outButton.addActionListener(this);		
		
		wordWrap=new JCheckBox ();
		wordWrap.setText("Word Wrap");
		wordWrap.setFont(new Font("Dialog",1,10));
		wordWrap.setPreferredSize(new Dimension (100,20));
		wordWrap.addItemListener(this);
		
		showThread=new JCheckBox ();
		showThread.setText("Show Document Thread");
		showThread.setFont(new Font("Dialog",1,10));
		showThread.setPreferredSize(new Dimension (150,20));
		showThread.addItemListener(this);		
		
		filterText=new JComboBox<String>();
		filterText.setFont(new Font("Dialog",1,10));
		filterText.setPreferredSize(new Dimension (150,20));
		filterText.setMaximumSize(new Dimension (150,20));
		//filterText.addActionListener(this);
		filterText.addItemListener (this);
		
		renderType=new JComboBox<String>(renderTypes);
		renderType.setFont(new Font("Dialog",1,10));
		renderType.setPreferredSize(new Dimension (150,20));
		renderType.setMaximumSize(new Dimension (150,20));
		//renderType.addActionListener(this);
		renderType.addItemListener (this);
				
		contentType=new JComboBox<String>(renderContent);
		contentType.setFont(new Font("Dialog",1,10));
		contentType.setPreferredSize(new Dimension (150,20));
		contentType.setMaximumSize(new Dimension (150,20));
		//contentType.addActionListener(this);
		contentType.addItemListener (this);
		
		controlBox.add (inButton);
		controlBox.add (Box.createRigidArea(new Dimension(2,0)));
		controlBox.add (outButton);
		controlBox.add (Box.createRigidArea(new Dimension(2,0)));
		controlBox.add (wordWrap);
		controlBox.add (Box.createRigidArea(new Dimension(2,0)));
		controlBox.add (showThread);
		controlBox.add (Box.createRigidArea(new Dimension(2,0)));						
		controlBox.add (filterText);
		controlBox.add (Box.createRigidArea(new Dimension(2,0)));
		controlBox.add (renderType);
		controlBox.add (Box.createRigidArea(new Dimension(2,0)));
		controlBox.add (contentType);
					
		controlBox.add(Box.createHorizontalGlue());		
		
		controlPanel=new JPanel ();
		controlPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		controlPanel.setMinimumSize(new Dimension (100,120));
		controlPanel.setPreferredSize(new Dimension (100,120));
		//controlPanel.setMaximumSize(new Dimension (500,120));
									    
		//textViewer = new JTextArea();
		textViewer = new JTextPane();
		textViewer.setEditable(false);
		textViewer.setFont(new Font("Dialog", 1, fontSize));
		textViewer.getDocument().addDocumentListener (this);
		textViewer.setMargin(new Insets(4,4,4,4)); 
		
		highlighter = textViewer.getHighlighter();

		lines = new JTextArea("1");		 
		lines.setBackground(Color.LIGHT_GRAY);
		lines.setFont(new Font("Dialog", 1, fontSize));
		lines.setEditable(false);
		lines.addMouseListener(this);
		lines.addFocusListener(this);
		lines.setMargin(new Insets(4,4,4,4)); 
		
		JScrollPane scroller=new JScrollPane ();
		scroller.getViewport().add(textViewer);
		scroller.setRowHeaderView (lines);		
		scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
				    	
		threadTree=new JTree();
		threadTree.setFont(new Font("Dialog", 1, 10)); // overwritten by cellrenderer?
		threadTree.getSelectionModel().setSelectionMode (TreeSelectionModel.SINGLE_TREE_SELECTION);
		threadTree.setRootVisible(true);
		threadTree.setBorder(BorderFactory.createEmptyBorder(4,4,4,4));
		//threadTree.setCellRenderer (treeHoopRenderer);
		threadTree.setDragEnabled(false);
		threadTree.addMouseListener (this);		
		
		JScrollPane treeScroller=new JScrollPane (threadTree);		
		treeScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,scroller, treeScroller);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(250);
		
		Box splitBox = new Box (BoxLayout.X_AXIS);
		
		splitBox.add(splitPane);
		
    	mainBox.add(controlBox);
    	mainBox.add(Box.createRigidArea(new Dimension(0,2)));
    	mainBox.add(controlPanel);
    	mainBox.add(Box.createRigidArea(new Dimension(0,2)));
    	mainBox.add(splitBox);
		
		setContentPane (mainBox);		
    }
	/**
	 * 
	 */
	public void showDocument (HoopKVDocument aDocument)
	{
		debug ("showDocument ()");
		
		internalDocument=aDocument;
						
		String aChoice=(String)contentType.getSelectedItem();
		
		debug ("Showing: " + aChoice);
		
		if (aChoice.equalsIgnoreCase("MAIN TEXT")==true)
			showText (internalDocument.toText ());
		else
			showText (internalDocument.toAbstract ());
	
		fillTextFilterCombo ();
		
		showDocumentThread (aDocument);
	}
	/**
	 * 
	 */
	public void showText (String aText)
	{
		debug ("showText ()");
		
		textViewer.getDocument().putProperty(DefaultEditorKit.EndOfLineStringProperty, "\n");		
		textViewer.setText(aText);
		textViewer.setCaretPosition(0);
	}	
	/**
	 * 
	 */
	public void showFile (HoopWrapperFile aFile)
	{
		debug ("showFile (HoopWrapperFile)");
		
		internalDocument=null;
		
		String text=HoopLink.fManager.loadContents(aFile.getFileURI());
		
		textViewer.getDocument().putProperty(DefaultEditorKit.EndOfLineStringProperty, "\n");
		textViewer.setText(text);
		textViewer.setCaretPosition(0);
	}
	/**
	 * 
	 */
	public void showFile (String aFile)
	{
		debug ("showFile (String)");
		
		String text=HoopLink.fManager.loadContents(aFile);
		
		textViewer.setText(text);
		textViewer.setCaretPosition(0);
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
	@Override	
	public void actionPerformed(ActionEvent event)
	{
		debug ("actionPerformed ()");
		
		Object controlTest=event.getSource();
		
		if (controlTest instanceof JButton)
		{
			JButton button = (JButton) controlTest;	
		
			if (button==inButton)
			{
				fontSize++;
			
				if (fontSize>23)
					fontSize=23;
			
				textViewer.setFont(new Font("Dialog", 1, fontSize));
				lines.setFont(new Font("Dialog", 1, fontSize));
			}
		
			if (button==outButton)
			{
				fontSize--;
			
				if (fontSize<1)
					fontSize=1;
			
				textViewer.setFont(new Font("Dialog", 1, fontSize));
				lines.setFont(new Font("Dialog", 1, fontSize));
			}
		}
		
		if (controlTest instanceof JComboBox)
		{
		     JComboBox cb = (JComboBox) controlTest;
		     
		     if (cb==filterText)
		     {
		    	 String aChoice=(String)filterText.getSelectedItem();
		     
		    	 if (aChoice!=null)
		    	 {
		    		 if (aChoice.isEmpty()==false)
		    		 {
		    			 Integer newView=Integer.parseInt(aChoice);
		    	 
		    			 if (internalDocument!=null)
		    			 {
		    				 showText (internalDocument.toText (newView));
		    			 }
		    		 }
		    		 else
		    			 showText (internalDocument.toText (0));
		    	 }
		    	 else
	    			 showText (internalDocument.toText (0));
		    	 
		    	 return;
		     }
		     
		     if (cb==renderType)
		     {
		    	 String aChoice=(String)renderType.getSelectedItem();
		    	 
		    	 if (aChoice.equalsIgnoreCase("txt")==true)
		    		 textViewer.setContentType("text/text");
		    	 
		    	 if (aChoice.equalsIgnoreCase("html")==true)
		    		 textViewer.setContentType("text/html");
		    	 
		    	 if (aChoice.equalsIgnoreCase("rtf")==true)
		    		 textViewer.setContentType("text/rtf");
		    	 
		    	 if (aChoice.equalsIgnoreCase("xml")==true)
		    	 {
		    		 
		    	 }
		    	 
		    	 if (aChoice.equalsIgnoreCase("tokens")==true)
		    	 {
		    		 
		    	 }		
		    	 
		    	 return;		    	 
		     }
		     
		     if (cb==contentType)
		     {
		    	 String aChoice=(String)contentType.getSelectedItem();
		    	 
		    	 if (aChoice.equalsIgnoreCase("MAIN TEXT")==true)
		    	 {
		    		 filterText.setEnabled(true);
		    		 renderType.setEnabled(true);
		    		 
		    		 showDocument (internalDocument);
		    	 }
		    	 
		    	 if (aChoice.equalsIgnoreCase("ABSTRACT")==true)
		    	 {
		    		 renderType.setEnabled(false);
		    		 filterText.setEnabled(false);
		    		 
		    		 showDocument (internalDocument);
		    	 }
		    	 
		    	 return;		    	 
		     }
		}
	}
	/**
	 * 
	 */
	public String getText()
	{
		int caretPosition = textViewer.getDocument().getLength();
		
		Element root = textViewer.getDocument().getDefaultRootElement();
		
		String text = "1" + System.getProperty ("line.separator");
		
		for (int i = 2; i < root.getElementIndex (caretPosition) + 2; i++)
		{
			text += i + System.getProperty("line.separator");
		}
		
		return text;
	}
	/**
	 * 
	 */	
	@Override
	public void changedUpdate(DocumentEvent de) 
	{
		lines.setText(getText());
	}
	/**
	 * 
	 */
	@Override
	public void insertUpdate(DocumentEvent de) 
	{
		lines.setText(getText());
	}
	/**
	 * 
	 */
	@Override
	public void removeUpdate(DocumentEvent de) 
	{
		lines.setText(getText());
	}
	/**
	 * 
	 */
	@Override
	public void mouseClicked(MouseEvent me) 
	{
		debug ("mouseClicked ()");
		
		if(me.getClickCount() == 2)
		{
			try 
			{
				int caretPos = lines.getCaretPosition();
				int lineOffset = lines.getLineOfOffset(caretPos);
				
				if(lines.getText().charAt(caretPos-1) == '\n')
					lineOffset--;
				
				/*
				highlighter.addHighlight(textViewer.getLineStartOffset(lineOffset),
										 textViewer.getLineEndOffset(lineOffset), 
										 new MyHighlighter(Color.cyan));
				*/
				
				highlighter.addHighlight(getLineStartOffset(textViewer,lineOffset),
						 				getLineEndOffset(textViewer,lineOffset), 
						 				new MyHighlighter(Color.cyan));				
			} 
			catch (BadLocationException e) 
			{
				e.printStackTrace();
			}
		}
	}
	@Override
	public void mouseEntered(MouseEvent arg0) {}
	@Override
	public void mouseExited(MouseEvent arg0) {}
	@Override
	public void mousePressed(MouseEvent arg0) {}
	@Override
	public void mouseReleased(MouseEvent arg0) {}
	@Override
	public void mouseDragged(MouseEvent arg0) {}
	@Override
	public void mouseMoved(MouseEvent arg0) {}
	@Override
	public void focusGained(FocusEvent e) {}
	@Override
	public void focusLost(FocusEvent e) 
	{
		highlighter.removeAllHighlights();		
	}
	/**
	 * 
	 */	
	private void fillTextFilterCombo ()
	{
		debug ("fillTextFilterCombo ()");
		
		filterText.removeAllItems();
		
		for (int i=0;i<internalDocument.getValueSize ();i++)
		{
			Integer fauxLabel=i;
			
			filterText.addItem(fauxLabel.toString());
		}	
	}
	/**
	 * 
	 */
	@Override
	public void itemStateChanged(ItemEvent e) 
	{
		debug ("itemStateChanged ()");
		
	    Object source = e.getItemSelectable();

	    if (source == wordWrap) 
	    {
	    	if (wordWrap.isSelected()==true)
	    	{
	    		/*
	    		textViewer.setLineWrap (true);
	    		textViewer.setWrapStyleWord(true);
	    		*/
	    	}
	    	else
	    	{
	    		/*
	    		textViewer.setLineWrap (false);
	    		*/
	    	}
	    }
	}
	/**
	 * 
	 * @param comp
	 * @param offset
	 * @return
	 * @throws BadLocationException
	 */
	static int getLineEndOffset(JTextComponent comp, int offset) throws BadLocationException 
	{
	    Document doc = comp.getDocument();
	    if (offset < 0) 
	    {
	        throw new BadLocationException("Can't translate offset to line", -1);
	    } 
	    else if (offset > doc.getLength()) 
	    {
	        throw new BadLocationException("Can't translate offset to line", doc.getLength() + 1);
	    } 
	    else 
	    {
	        Element map = doc.getDefaultRootElement();
	        return map.getElementIndex(offset);
	    }
	}
	/**
	 * 
	 * @param comp
	 * @param line
	 * @return
	 * @throws BadLocationException
	 */
	static int getLineStartOffset(JTextComponent comp, int line) throws BadLocationException 
	{
	    Element map = comp.getDocument().getDefaultRootElement();
	    
	    if (line < 0) 
	    {
	        throw new BadLocationException("Negative line", -1);
	    } 
	    else if (line >= map.getElementCount()) 
	    {
	        throw new BadLocationException("No such line", comp.getDocument().getLength() + 1);
	    } 
	    else 
	    {
	        Element lineElem = map.getElement(line);
	        return lineElem.getStartOffset();
	    }
	}
	/**
	 * 
	 * @param e
	 */
	public void caretUpdate(CaretEvent e) 
	{
	    int dot = e.getDot();
	    int line=0;
	    @SuppressWarnings("unused")
		int positionInLine=0;
	    
		try 
		{
			line = getLineEndOffset (textViewer, dot);
		} 
		catch (BadLocationException e1) 
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	    try 
	    {
			positionInLine = dot - getLineStartOffset(textViewer, line);
		}
	    catch (BadLocationException e1) 
	    {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	/**
	 * 
	 */
	private void showDocumentThread (HoopKVDocument aDocument)
	{
		debug ("showDocumentThread ()");

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
		
		if (aDocument.threadID.getValue().isEmpty()==true)
		{
			debug ("Error: document does not contain a thread ID");
			return;
		}

		String threadLabel=(aDocument.threadID.getValue() + " : " + aDocument.title.getValue());
		
    	DefaultMutableTreeNode root=new DefaultMutableTreeNode (threadLabel);
    	//root.setUserObject (aDocument.documentID.getValue());
    	
    	Long newThreadID=Long.parseLong(aDocument.threadID.getValue());
    	
    	HoopKVLong threadRoot=threadData.get(newThreadID);
    	    	    	
    	for (int i=0;i<threadRoot.getValueSize();i++)
    	{    		
    		String tmpDateString=(String) threadRoot.getValuesRaw().get(i);
    		
    		debug ("Adding ID: " + tmpDateString);
    		  
    		HoopFileTools fTools=new HoopFileTools ();
    		
    		//HoopKVDocument threadDocument=HoopLink.dataSet.getDocumentFromDate (aDocument.dateStringToLong (tmpDateString));
    		HoopKVDocument threadDocument=HoopLink.dataSet.getDocumentFromDate (fTools.dateStringToLong (tmpDateString,aDocument.dateFormat.getValue()));
    		
    		if (threadDocument!=null)
    		{
        		DefaultMutableTreeNode aNode=new DefaultMutableTreeNode (threadDocument.author.getValue());
        		//aNode.setUserObject (threadDocument);
        		root.add(aNode);        		
    		}
    	}
    	
    	DefaultTreeModel model = new DefaultTreeModel(root);

    	threadTree.setModel(model);
	}
}
