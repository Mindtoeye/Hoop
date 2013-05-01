/** 
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, HoopCLUDHoopG, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  Hoop NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, HoopDIRECT, HoopCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (HoopCLUDHoopG, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSHoopESS HoopTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER Hoop CONTRACT, STRICT LIABILITY, OR TORT (HoopCLUDHoopG
 * NEGLIGENCE OR OTHERWISE) ARISHoopG Hoop ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */ 

package edu.cmu.cs.in.hoop;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JTabbedPane;
import javax.swing.plaf.basic.BasicButtonUI;

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.controls.base.HoopEmbeddedJPanel;

/** 
 * 
 */
public class HoopTabButton extends JButton implements ActionListener 
{
	private static final long serialVersionUID = 1L;
	private JTabbedPane pane;
	private MouseListener buttonMouseListener;
	
	/**
	 * 
	 */		
	public HoopTabButton(JTabbedPane aPane,MouseListener aListener) 
	{
		pane=aPane;
		buttonMouseListener=aListener;
		
		int size = 25;
            
		this.setPreferredSize(new Dimension(size, size));
		this.setMaximumSize(new Dimension(size, size));
		this.setToolTipText("Close this tab");
		this.setIcon(HoopLink.getImageByName("gtk-close.png"));
		this.setMargin(new Insets(0,0,0,0));


		this.setBorderPainted(false); 
		this.setContentAreaFilled(false); 
		this.setFocusPainted(false); 
		this.setOpaque(false); 
		
		this.addMouseListener(buttonMouseListener);
		this.setRolloverEnabled(true);
		this.addActionListener(this);
	}
	/** 
	 * 
	 */
	public void actionPerformed(ActionEvent e) 
	{
		if (pane!=null)
		{
			int i = pane.indexOfTabComponent (pane);
            
			if (i != -1) 
			{
				HoopEmbeddedJPanel aContent=(HoopEmbeddedJPanel) pane.getComponentAt(i);
				if (aContent!=null)
				{
					HoopLink.removeWindow(aContent);
				}
				pane.remove(i);			
			}
		}	
	}
	/** 
	 * we don't want to update UI for this button
	 */
	/*
	public void updateUI() 
	{
        	
	}
	*/
	/** 
	 * paint the cross
	 */
	/**
	protected void paintComponent(Graphics g) 
	{
		super.paintComponent(g);
                        
		Graphics2D g2 = (Graphics2D) g.create();
            
		//shift the image for pressed buttons
		if (getModel().isPressed()) 
		{
			g2.translate(1, 1);
		}
            
		g2.setStroke (new BasicStroke(2));
		g2.setColor (Color.BLACK);
            
		if (getModel().isRollover()) 
		{
			g2.setColor (Color.WHITE);
		}
            
		int delta = 6;
		g2.drawLine(delta, delta, getWidth() - delta - 1, getHeight() - delta - 1);
		g2.drawLine(getWidth() - delta - 1, delta, delta, getHeight() - delta - 1);
		g2.dispose();
	}
	*/
}
