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
//import javax.swing.plaf.basic.BasicButtonUI;

//import edu.cmu.cs.in.base.HoopLink;
//import edu.cmu.cs.in.base.HoopLink;
//import edu.cmu.cs.in.controls.base.HoopEmbeddedJPanel;
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
	
	private JTabbedPane pane=null;
	private JButton icon = null;
	private JButton button = null;
	private JLabel label=null;

    /** 
     * @param pane
     */
    public HoopTabPane (final JTabbedPane aPane) 
    {		
        //unset default FlowLayout' gaps
        //super (new FlowLayout(FlowLayout.LEFT, 0, 0));
    	
    	//super (new BorderLayout());
    	
    	this.setLayout(new BoxLayout (this,BoxLayout.X_AXIS));
        
		setClassName ("HoopTabPane");
		debug ("HoopTabPane ()");
			
		//this.setBorder (BorderFactory.createLineBorder(Color.green));		
		
        if (aPane == null) 
        {
            throw new NullPointerException("TabbedPane is null");
        }
        else
        {        
        	this.pane=aPane;
        
        	setOpaque (false);
        	                	
        	icon=new JButton ();
        	icon.setOpaque (false);
        	icon.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        	
        	this.add (icon);
        	
        	//	make JLabel read titles from JTabbedPane
        	label = new JLabel() 
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
        	label.setBorder(BorderFactory.createEmptyBorder(0, 1, 0, 1));
        	
        	this.add (label);
        	        	
        	this.add(Box.createHorizontalGlue());

        	button=new HoopTabButton(pane,buttonMouseListener);
        	button.setPreferredSize(new Dimension (20,20));
        	button.setBorder(BorderFactory.createEmptyBorder(0, 1, 0, 1));
                
        	this.add (button);        	
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
     * paint the cross
     */
    /*
    protected void paintComponent(Graphics g) 
    {    	
    	super.paintComponent(g);
    	
    	g.setColor(Color.RED);
    	g.drawRect(0,0,this.getWidth(),this.getHeight());    	
    }
    */
	/**
	 *
	 */	
	public void updateSize() 
	{
		debug ("updateSize ()");
		    
		super.updateSize();
			
    	debug ("Adjusting font size for width: " + this.getWidth());
    	
    	int fontSize=Math.round(this.getWidth()/20);
    	
    	if (fontSize<8)
    		fontSize=8;
    	
    	if (fontSize>11)
    		fontSize=11;
    	
    	label.setFont(new Font("Dialog", 1, fontSize));    	
	}	    
    /**
	 * 
	 */
    private static MouseListener buttonMouseListener = new MouseAdapter() 
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


