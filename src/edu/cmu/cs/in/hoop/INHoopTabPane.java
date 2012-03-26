/** 
 * Author: Martin van Velsen <vvelsen@cs.cmu.edu>
 * 
 * This source set uses code written for other various graduate 
 * courses and is part of a larger research effort in the field of
 * interactive narrative (IN). Courses that have contribute
 * to this source base are:
 * 
 * 	05-834 Applied Machine Learning
 *  11-719 Computational Models of Discourse Analysis
 *  11-741 Information Retrieval
 *  
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
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */ 

package edu.cmu.cs.in.hoop;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;

import edu.cmu.cs.in.base.INLink;
import edu.cmu.cs.in.controls.base.INEmbeddedJPanel;
import edu.cmu.cs.in.controls.base.INJPanel;

import java.awt.*;
import java.awt.event.*;

/**
 * Component to be used as tabComponent. Contains a JLabel to show the text and 
 * a JButton to close the tab it belongs to. 
 */ 
public class INHoopTabPane extends INJPanel 
{
	private static final long serialVersionUID = 1L;
	private final JTabbedPane pane;
	private JButton icon = null;
	private JButton button = null;

    /** 
     * @param pane
     */
    public INHoopTabPane (final JTabbedPane pane) 
    {		
        //unset default FlowLayout' gaps
        super (new FlowLayout(FlowLayout.LEFT, 0, 0));
        
		setClassName ("INHoopTabPane");
		debug ("INHoopTabPane ()");
        
        if (pane == null) 
        {
            throw new NullPointerException("TabbedPane is null");
        }
        else
        {        
        	this.pane=pane;
        
        	setOpaque (false);
        
        	icon=new JButton ();
        	icon.setOpaque (false);
        	icon.setBorder (null);

        	add (icon);
        	
        	//	make JLabel read titles from JTabbedPane
        	JLabel label = new JLabel() 
        	{
        		private static final long serialVersionUID = 1L;

        		/**
        		 * 
        		 */			
        		public String getText() 
        		{
        			//debug ("getText ()");
        			
        			int i = pane.indexOfTabComponent (INHoopTabPane.this);
        			if (i != -1) 
        			{
        				//debug ("Title at index: " + i + " is: " + pane.getTitleAt(i));
        				return pane.getTitleAt(i);
        			}
        			//else
        				//debug ("Unable to find tab index");
                
        			return null;
        		}
        	};
                
        	add(label);
        	//add more space between the label, the icon and the button
        	label.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        	//tab button
        	button=new TabButton();
                
        	add(button);
        	//add more space to the top of the component
        	setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
        }	
    }
    /** 
     * 
     */
    public void update ()
    {
    	debug ("update ()");
    	
    	if (icon!=null)
    	{
    		int j=pane.indexOfTabComponent (INHoopTabPane.this);
    		if (j!=-1)
    		{
    			debug ("Setting icon for tab at index: "+ j);
    			Icon tabIcon=pane.getIconAt(j);
    			if (tabIcon!=null)
    			{
    				//button.setHorizontalTextPosition(SwingConstants.RIGHT);
    				//icon.setHorizontalTextPosition(JButton.RIGHT); 
    				icon.setIcon(tabIcon);    				
    			}
    			else
    				debug ("Error tab does not have an icon");
    		}        
    		else
    			debug ("Unable to find tab index");
    	}	    	
    }
    /** 
     * 
     */
    private class TabButton extends JButton implements ActionListener 
    {
		private static final long serialVersionUID = 1L;
	
        /**
		 * 
		 */		
		public TabButton() 
        {
            int size = 17;
            setPreferredSize(new Dimension(size, size));
            setToolTipText("Close this tab");
            //Make the button looks the same for all Laf's
            setUI(new BasicButtonUI());
            //Make it transparent
            setContentAreaFilled(false);
            //No need to be focusable
            setFocusable(false);
            setBorder(BorderFactory.createEtchedBorder());
            setBorderPainted(false);
            //Making nice rollover effect
            //we use the same listener for all buttons
            addMouseListener(buttonMouseListener);
            setRolloverEnabled(true);
            //Close the proper tab by clicking the button
            addActionListener(this);
        }
        /** 
         * 
         */
        public void actionPerformed(ActionEvent e) 
        {
            int i = pane.indexOfTabComponent (INHoopTabPane.this);
            
            if (i != -1) 
            {
            	INEmbeddedJPanel aContent=(INEmbeddedJPanel) pane.getComponentAt(i);
            	if (aContent!=null)
            	{
            		INLink.removeWindow(aContent);
            	}
                pane.remove(i);
            }
        }
        /** 
         * we don't want to update UI for this button
         */
        public void updateUI() 
        {
        	
        }
        /** 
         * paint the cross
         */
        protected void paintComponent(Graphics g) 
        {
            super.paintComponent(g);
            
            Graphics2D g2 = (Graphics2D) g.create();
            //shift the image for pressed buttons
            if (getModel().isPressed()) 
            {
                g2.translate(1, 1);
            }
            g2.setStroke(new BasicStroke(2));
            g2.setColor(Color.BLACK);
            
            if (getModel().isRollover()) 
            {
                g2.setColor(Color.MAGENTA);
            }
            
            int delta = 6;
            g2.drawLine(delta, delta, getWidth() - delta - 1, getHeight() - delta - 1);
            g2.drawLine(getWidth() - delta - 1, delta, delta, getHeight() - delta - 1);
            g2.dispose();
        }
    }
    
    /**
	 * 
	 */
    private final static MouseListener buttonMouseListener = new MouseAdapter() 
    {
        /**
		 * 
		 */    	
        public void mouseEntered(MouseEvent e) 
        {
            Component component = e.getComponent();
            if (component instanceof AbstractButton) 
            {
                AbstractButton button = (AbstractButton) component;
                button.setBorderPainted(true);
            }
        }
        /**
		 * 
		 */
        public void mouseExited(MouseEvent e) 
        {
            Component component = e.getComponent();
            
            if (component instanceof AbstractButton) 
            {
                AbstractButton button = (AbstractButton) component;
                button.setBorderPainted(false);
            }
        }
    };
}


