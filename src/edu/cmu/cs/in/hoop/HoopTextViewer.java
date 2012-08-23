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

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Element;

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.controls.base.HoopEmbeddedJPanel;
import edu.cmu.cs.in.hoop.project.HoopWrapperFile;

/**
 * 
 */
public class HoopTextViewer extends HoopEmbeddedJPanel implements ActionListener
{	
	private static final long serialVersionUID = -1L;
	private JEditorPane textViewer=null;
	private JTextArea lines=null;
	
	private JButton inButton=null;
	private JButton outButton=null;	
	
	private int fontSize=10;
	
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
		
		controlBox.add(inButton);
		controlBox.add (Box.createRigidArea(new Dimension(2,0)));
		controlBox.add(outButton);
		controlBox.add (Box.createRigidArea(new Dimension(2,0)));
			
		controlBox.add(Box.createHorizontalGlue());		
									    
		textViewer = new JEditorPane();
		textViewer.setEditable(false);
		textViewer.setFont(new Font("Dialog", 1, fontSize));

		lines = new JTextArea("1");		 
		lines.setBackground(Color.LIGHT_GRAY);
		lines.setFont(new Font("Dialog", 1, fontSize));
		lines.setEditable(false);
		
		JScrollPane scroller=new JScrollPane ();
		scroller.getViewport().add(textViewer);
		scroller.setRowHeaderView (lines);		
		scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
				    	
    	mainBox.add(controlBox);
    	mainBox.add(scroller);
		
		setContentPane (mainBox);
		
		textViewer.getDocument().addDocumentListener(new DocumentListener()
		{
			public String getText()
			{
				int caretPosition = textViewer.getDocument().getLength();
				Element root = textViewer.getDocument().getDefaultRootElement();
				String text = "1" + System.getProperty ("line.separator");
				
				for(int i = 2; i < root.getElementIndex( caretPosition ) + 2; i++)
				{
					text += i + System.getProperty("line.separator");
				}
				
				return text;
			}
			
			@Override
			public void changedUpdate(DocumentEvent de) 
			{
				lines.setText(getText());
			}
 
			@Override
			public void insertUpdate(DocumentEvent de) 
			{
				lines.setText(getText());
			}
 
			@Override
			public void removeUpdate(DocumentEvent de) 
			{
				lines.setText(getText());
			}
 
		});
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
		
		//String act=event.getActionCommand();
		JButton button = (JButton) event.getSource();	
		
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
}
