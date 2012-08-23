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

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import edu.cmu.cs.in.base.kv.HoopKVDocument;
import edu.cmu.cs.in.controls.base.HoopJPanel;
  
public class HoopDocumentListRenderer extends HoopJPanel implements ListCellRenderer
{
	private static final long serialVersionUID = 1L;
	
	private HoopKVDocument aDoc=null;
	
	private JTextArea text=null;
	private JLabel docLabel=null;
	//private Border border=null;
	private Border selectedBorder=null;
	private Border defaultBorder=null;
	
	private int prefHeight=100;
	
	/**
	 * 
	 */	
	public HoopDocumentListRenderer () 
	{		
		setClassName ("HoopDocumentListRenderer");
		debug ("HoopDocumentListRenderer ()");
		
		this.setOpaque(true);		
		this.setBackground(new Color (0xF5F5E3));
		
		//border=BorderFactory.createLineBorder(Color.black);		
		selectedBorder=BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		defaultBorder=BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
		
		this.setLayout(new BoxLayout (this,BoxLayout.Y_AXIS));
		this.setMinimumSize(new Dimension (100,prefHeight));
		this.setPreferredSize(new Dimension (100,prefHeight));
		
		Box buttonBox = new Box (BoxLayout.X_AXIS);
		
		docLabel=new JLabel ();
		//docLabel.setBorder(border);
		docLabel.setFont(new Font("Dialog", 1, 10));
		
		buttonBox.add(docLabel);
		buttonBox.add(Box.createHorizontalGlue());
				
		text=new JTextArea ();
		text.setLineWrap(true);
		text.setWrapStyleWord(true);		
		text.setFont(new Font("Dialog", 1, 10));
		text.setEditable(false);
		
		JScrollPane scrollPane = new JScrollPane(text);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		
		this.add (buttonBox);
		this.add (scrollPane);
		this.add (Box.createRigidArea(new Dimension(0,4)));
	}
	/**
	 * 
	 */
	public Component getListCellRendererComponent  (JList listBox, 
													Object obj, 
													int currentindex, 
													boolean isChecked, 
													boolean hasFocus) 
	{
		if (obj==null)
		{
			debug ("Internal error: object is null");
			return this;
		}	
	
		aDoc=(HoopKVDocument) obj;
								
		docLabel.setText("Rank: "+(aDoc.getRank()+1)+" Evaluation: " + aDoc.getScore());
		
		text.setText(aDoc.abstr.getValue());
		
		showSelection (hasFocus);

		return this;
	}
	/**
	 * 
	 */
	private void showSelection (Boolean hasFocus)
	{		
		if (aDoc==null)
		{
			this.setBorder (BorderFactory.createTitledBorder (defaultBorder,
															  "Document",
															  TitledBorder.LEFT, 
															  TitledBorder.TOP,
															  new Font("Dialog", 1, 10)));
			return;
		}
	
		String fixedTitle=aDoc.title.getValue();

		if (fixedTitle.length()>20)
			fixedTitle=aDoc.title.getValue().substring(0,20)+"...";
				
		if (hasFocus)
		{
			text.setBackground(new Color (255,255,220));
			this.setBorder (BorderFactory.createTitledBorder (defaultBorder,
															  aDoc.getDocID() +" : " + fixedTitle,	
															  TitledBorder.LEFT, 
															  TitledBorder.TOP,
															  new Font("Dialog", 1, 10))); 
		}
		else
		{
			text.setBackground(new Color (255,255,255));
			this.setBorder (BorderFactory.createTitledBorder (selectedBorder,
															  aDoc.getDocID() +" : " + fixedTitle,	
															  TitledBorder.LEFT, 
															  TitledBorder.TOP,
															  new Font("Dialog", 1, 10)));
		}		
		
		text.setCaretPosition(0);
	}
}
