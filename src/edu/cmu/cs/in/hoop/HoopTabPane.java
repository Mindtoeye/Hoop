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

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;

import edu.cmu.cs.in.base.HoopLink;
//import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.controls.base.HoopEmbeddedJPanel;
import edu.cmu.cs.in.controls.base.HoopJPanel;

import java.awt.*;
import java.awt.event.*;

/**
 * Component to be used as tabComponent. Contains a JLabel to show the text and 
 * a JButton to close the tab it belongs to. 
 */ 
public class HoopTabPane extends HoopJPanel 
{
	private static final long serialVersionUID = 1L;
	private final JTabbedPane pane;
	private JButton icon = null;
	private JButton button = null;

    /** 
     * @param pane
     */
    public HoopTabPane (final JTabbedPane pane) 
    {		
        //unset default FlowLayout' gaps
        super (new FlowLayout(FlowLayout.LEFT, 0, 0));
        
		setClassName ("HoopTabPane");
		debug ("HoopTabPane ()");
			
		//this.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		//this.setMargin(new Insets(0,0,0,0));
		
        if (pane == null) 
        {
            throw new NullPointerException("TabbedPane is null");
        }
        else
        {        
        	this.pane=pane;
        
        	setOpaque (true);
        	                	
        	icon=new JButton ();
        	icon.setOpaque (false);
        	icon.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        	

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
        			int i = pane.indexOfTabComponent (HoopTabPane.this);
        			if (i != -1) 
        			{        		
        				return pane.getTitleAt(i);
        			}
                
        			return null;
        		}
        	};
                
        	label.setFont(new Font("Dialog", 1, 10));
        	add (label);
        	
        	label.setBorder(BorderFactory.createEmptyBorder(0, 1, 0, 1));

        	button=new HoopTabButton();
        	button.setBorder(BorderFactory.createEmptyBorder(0, 1, 0, 1));
                
        	add (button);        	
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
    		int j=pane.indexOfTabComponent (HoopTabPane.this);
    		
    		if (j!=-1)
    		{
    			debug ("Setting icon for tab at index: "+ j);
    			Icon tabIcon=pane.getIconAt(j);
    			
    			if (tabIcon!=null)
    			{
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
    private class HoopTabButton extends JButton implements ActionListener 
    {
		private static final long serialVersionUID = 1L;
	
        /**
		 * 
		 */		
		public HoopTabButton() 
        {
            int size = 25;
            
            this.setPreferredSize(new Dimension(size, size));
            this.setToolTipText("Close this tab");
            this.setMargin(new Insets(0,0,0,0));
            this.setBorder(BorderFactory.createEmptyBorder(0,0, 0, 0));

            this.setUI (new BasicButtonUI());
            
            this.setContentAreaFilled(false);
            
            this.setFocusable(false);
            this.setBorderPainted(false);            
            this.addMouseListener(buttonMouseListener);
            this.setRolloverEnabled(true);
            this.addActionListener(this);
        }
        /** 
         * 
         */
        public void actionPerformed(ActionEvent e) 
        {
            int i = pane.indexOfTabComponent (HoopTabPane.this);
            
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
    }
    /** 
     * paint the cross
     */
    protected void paintComponent(Graphics g) 
    {
    	super.paintComponent(g);
    	
    	g.setColor(Color.RED);
    	g.drawRect(0,0,this.getWidth(),this.getHeight());
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


