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

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
//import javax.swing.JEditorPane;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.MouseInputListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Element;
import javax.swing.text.Highlighter;

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.base.kv.HoopKVDocument;
import edu.cmu.cs.in.controls.base.HoopEmbeddedJPanel;
import edu.cmu.cs.in.hoop.project.HoopWrapperFile;

/**
 * 
 */
public class HoopTextViewer extends HoopEmbeddedJPanel implements ActionListener, MouseInputListener, DocumentListener, FocusListener, ItemListener
{	
	private static final long serialVersionUID = -1L;
	private JTextArea textViewer=null;
	private JTextArea lines=null;
	
	private JButton inButton=null;
	private JButton outButton=null;
	
	private JCheckBox wordWrap=null;
	private JComboBox filterText=null;
	
	private int fontSize=10;
	
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
		inButton.setFont(new Font("Courier",1,8));
		inButton.setPreferredSize(new Dimension (20,20));
		inButton.addActionListener(this);
		
		outButton=new JButton ();
		outButton.setIcon(HoopLink.imageIcons [73]);
		outButton.setMargin(new Insets(1, 1, 1, 1));
		outButton.setFont(new Font("Courier",1,8));
		outButton.setPreferredSize(new Dimension (20,20));
		outButton.addActionListener(this);		
		
		wordWrap=new JCheckBox ();
		wordWrap.setText("Word Wrap");
		wordWrap.setFont(new Font("Dialog",1,10));
		wordWrap.setPreferredSize(new Dimension (100,20));
		wordWrap.addItemListener(this);
		
		filterText=new JComboBox();
		filterText.setFont(new Font("Courier",1,9));
		filterText.setPreferredSize(new Dimension (150,20));
		filterText.setMaximumSize(new Dimension (150,20));
		filterText.addActionListener(this);
		
		controlBox.add (inButton);
		controlBox.add (Box.createRigidArea(new Dimension(2,0)));
		controlBox.add (outButton);
		controlBox.add (Box.createRigidArea(new Dimension(2,0)));
		controlBox.add (wordWrap);
		controlBox.add (Box.createRigidArea(new Dimension(2,0)));
		controlBox.add (filterText);
		controlBox.add (Box.createRigidArea(new Dimension(2,0)));		
					
		controlBox.add(Box.createHorizontalGlue());		
									    
		textViewer = new JTextArea();
		textViewer.setEditable(false);
		textViewer.setFont(new Font("Dialog", 1, fontSize));
		textViewer.getDocument().addDocumentListener (this);
		
		highlighter = textViewer.getHighlighter();

		lines = new JTextArea("1");		 
		lines.setBackground(Color.LIGHT_GRAY);
		lines.setFont(new Font("Dialog", 1, fontSize));
		lines.setEditable(false);
		lines.addMouseListener(this);
		lines.addFocusListener(this);		
		
		JScrollPane scroller=new JScrollPane ();
		scroller.getViewport().add(textViewer);
		scroller.setRowHeaderView (lines);		
		scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
				    	
    	mainBox.add(controlBox);
    	mainBox.add(scroller);
		
		setContentPane (mainBox);		
    }
	/**
	 * 
	 */
	public void showDocument (HoopKVDocument aDocument)
	{
		internalDocument=aDocument;
		
		showText (internalDocument.toText ());
	
		fillTextFilterCombo ();
	}
	/**
	 * 
	 */
	public void showText (String aText)
	{
		debug ("showText ()");
				
		textViewer.setText(aText);
		textViewer.setCaretPosition(0);
	}	
	/**
	 * 
	 */
	public void showFile (HoopWrapperFile aFile)
	{
		debug ("showFile (HoopWrapperFile)");
		
		String text=HoopLink.fManager.loadContents(aFile.getFileURI());
		
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
		     
		     String aChoice=(String)cb.getSelectedItem();
		     
		     Integer newView=Integer.parseInt(aChoice);
		     
		     
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
				
				highlighter.addHighlight(textViewer.getLineStartOffset(lineOffset),
										 textViewer.getLineEndOffset(lineOffset), 
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
		//filterText.addItem ("ALL");
		
		for (int i=0;i<internalDocument.getValueSize ();i++)
		{
			Integer fauxLabel=i;
			
			filterText.addItem(fauxLabel.toString());
		}
		
		/*
		Enumeration<String> names; 
		names = classTable.keys();
		
		while(names.hasMoreElements()) 
		{
			String str = (String) names.nextElement();
			//System.out.println(str + ": " +	names.get(str));
			filterClass.addItem (str);
		}
		*/
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
	    		textViewer.setLineWrap (true);
	    		textViewer.setWrapStyleWord(true);
	    	}
	    	else
	    	{
	    		textViewer.setLineWrap (false);
	    		//textViewer.setWrapStyleWord(false);
	    	}
	    }
	}	
}
