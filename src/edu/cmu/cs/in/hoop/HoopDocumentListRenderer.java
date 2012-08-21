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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.base.kv.HoopKVDocument;

import edu.cmu.cs.in.controls.base.HoopJPanel;
  
public class HoopDocumentListRenderer extends HoopJPanel implements ListCellRenderer, ActionListener
{
	private static final long serialVersionUID = 1L;
	private HoopKVDocument aDoc=null;
	private JLabel docLabel=null;
	private JButton linker=null;
	private Border border=null;
	private Border selectedBorder=null;
	
	/**
	 * 
	 */	
	public HoopDocumentListRenderer () 
	{		
		setClassName ("HoopDocumentListRenderer");
		debug ("HoopDocumentListRenderer ()");
		
		this.setOpaque(true);
		
		this.setBackground(new Color (0xF5F5E3));
		
		border=BorderFactory.createLineBorder(Color.black);
		selectedBorder=BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		//this.setBorder(BorderFactory.createRaisedBevelBorder());
		//this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		
		this.setLayout(new BoxLayout (this,BoxLayout.X_AXIS));
		this.setMinimumSize(new Dimension (100,60));
		this.setPreferredSize(new Dimension (100,60));
		this.setMaximumSize(new Dimension (5000,60));
		
		Box controlBox = new Box (BoxLayout.Y_AXIS);
		controlBox.setMinimumSize(new Dimension (16,50));
		controlBox.setPreferredSize(new Dimension (16,5000));
		controlBox.setMaximumSize(new Dimension (16,5000));
		
		docLabel=new JLabel ();
		docLabel.setFont(new Font("Dialog", 1, 10));
		docLabel.setMinimumSize(new Dimension (20,60));
		docLabel.setPreferredSize(new Dimension (100,60));
		docLabel.setMaximumSize(new Dimension (5000,60));
		
		linker=new JButton ();
		linker.setIcon (HoopLink.linkIcon);
		linker.setFont(new Font("Dialog", 1, 10));
		linker.setMinimumSize (new Dimension (17,17));
		linker.setPreferredSize (new Dimension (17,17));
		linker.setMaximumSize (new Dimension (17,17));
		linker.addActionListener(this);
		
		JButton loader=new JButton ();
		loader.setText("L");
		loader.setFont(new Font("Dialog", 1, 10));
		loader.setMinimumSize (new Dimension (17,17));
		loader.setPreferredSize (new Dimension (17,17));
		loader.setMaximumSize (new Dimension (17,17));		
		
		JButton teaser=new JButton ();
		teaser.setText("T");
		teaser.setFont(new Font("Dialog", 1, 10));
		teaser.setMinimumSize (new Dimension (17,17));
		teaser.setPreferredSize (new Dimension (17,17));
		teaser.setMaximumSize (new Dimension (17,17));		
		
		controlBox.add(linker);
		controlBox.add(loader);
		controlBox.add(teaser);
		
		this.add(docLabel);
		this.add(controlBox);		
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
		
		setEnabled(listBox.isEnabled());

		aDoc=(HoopKVDocument) obj;
		
		docLabel.setText("Rank: "+(aDoc.getRank()+1)+" Evaluation: " + aDoc.getScore());
		
		/*
		if(hasFocus)
			this.setBorder(BorderFactory.createLoweredBevelBorder());
        else 
        	this.setBorder(BorderFactory.createRaisedBevelBorder());
        */
		
		fixBorder (hasFocus);

		return this;
	}
	/**
	 * 
	 */
	private void fixBorder (Boolean hasFocus)
	{
		if (aDoc==null)
		{
			this.setBorder (BorderFactory.createTitledBorder(selectedBorder,"Document"));
			return;
		}
		
		if (hasFocus)
		{
			this.setBorder (BorderFactory.createTitledBorder(border,aDoc.getDocID()));
		}
		else
		{
			this.setBorder (BorderFactory.createTitledBorder(selectedBorder,aDoc.getDocID()));			
		}		
	}
	/**
	 * 
	 */	
	@Override
	public void actionPerformed(ActionEvent event) 
	{
		debug ("actionPerformed ()");
		
		JButton button = (JButton) event.getSource();
		
		if (button==linker)
		{
			debug ("Loading document ...");
			
			//HoopLemurDocument loader=new HoopLemurDocument ();
			//loader.setDocID(aDoc.getDocID());
			//loader.loadDocument();
		}
	}	
}
