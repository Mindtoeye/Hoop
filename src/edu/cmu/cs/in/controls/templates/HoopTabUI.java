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

package edu.cmu.cs.in.controls.templates;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JTabbedPane;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

import edu.cmu.cs.in.base.HoopRoot;

/** 
 * 
 */
public class HoopTabUI extends BasicTabbedPaneUI 
{
    private static final Insets NO_HoopSETS = new Insets(0, 0, 0, 0);
    //private boolean contentTopBorderDrawn = true;
    //private Color lineColor = null;
    //private Color dividerColor = null;
    private Insets contentInsets = null;
    private ColorSet selectedColorSet=null;
    private ColorSet defaultColorSet=null;
    private ColorSet hoverColorSet=null;    
    private int lastRollOverTab = -1;
    
    private JTabbedPane sizeReference=null;

    /** 
     * 
     */    
    /*
    public static ComponentUI createUI(JComponent c) 
    {
        return new HoopTabUI();
    }
    */
    /**
     * 
     */
    private void debug (String aMessage)
    {
    	HoopRoot.debug("HoopTabUI",aMessage);
    }
    /** 
     * 
     */
    public HoopTabUI(JTabbedPane aContainer) 
    {
    	debug ("HoopTabUI (JTabbedPane)");

    	sizeReference=aContainer;
    	
        selectedColorSet = new ColorSet();

        selectedColorSet.topGradColor1 = new Color(198, 198, 197);
        selectedColorSet.topGradColor2 = Color.WHITE;

        selectedColorSet.bottomGradColor1 = Color.WHITE;
        selectedColorSet.bottomGradColor2 = Color.WHITE;

        defaultColorSet = new ColorSet();

        defaultColorSet.topGradColor1 = Color.WHITE;
        defaultColorSet.topGradColor2 = Color.WHITE;

        defaultColorSet.bottomGradColor1 = Color.WHITE;
        defaultColorSet.bottomGradColor2 = new Color(198, 198, 197);

        hoverColorSet = new ColorSet();
        hoverColorSet.topGradColor1 = new Color(244, 244, 244);
        hoverColorSet.topGradColor2 = new Color(223, 223, 223);

        hoverColorSet.bottomGradColor1 = new Color(211, 211, 211);
        hoverColorSet.bottomGradColor2 = new Color(235, 235, 235);

        //lineColor = new Color(158, 158, 158);
        //dividerColor = new Color(175,175,175);

        contentInsets = new Insets(0,0,0,0); // The area of the actual content area not the top button
        
        //maxTabHeight = 20;

        setContentInsets (contentInsets);
    }
    /** 
     * 
     */
    //public void setContentTopBorderDrawn(boolean b) 
    //{
    //    contentTopBorderDrawn = b;
    //}
    /** 
     * 
     */
    public void setContentInsets(Insets i) 
    {
        contentInsets = i;
    }
    /** 
     * 
     */
    public void setContentInsets(int i) 
    {
        contentInsets = new Insets(i, i, i, i);
    }
    /** 
     * 
     */
    public int getTabRunCount(JTabbedPane pane) 
    {
        return 1;
    }
    /** 
     * 
     */
    @Override
    protected void installDefaults() 
    {
        super.installDefaults();

        RollOverListener l = new RollOverListener();
        tabPane.addMouseListener((MouseListener) l);
        tabPane.addMouseMotionListener((MouseMotionListener) l);

        tabAreaInsets = NO_HoopSETS;
        tabInsets = new Insets(2,0,0,0);
    }
    /** 
     * 
     */
    protected boolean scrollableTabLayoutEnabled() 
    {
        return false;
    }
    /** 
     * 
     */
    @Override
    protected Insets getContentBorderInsets(int tabPlacement) 
    {
        return contentInsets;
    }
    /** 
     * 
     */
    @Override
    protected int calculateTabHeight(int tabPlacement, int tabIndex, int fontHeight) 
    {
        return super.calculateTabHeight(tabPlacement, tabIndex, fontHeight);
        //  return 21;
    }
    /** 
     * 
     */
    @Override
    protected int calculateTabWidth(int tabPlacement, int tabIndex, FontMetrics metrics) 
    {
    	debug ("calculateTabWidth ()");
    	
    	debug ("Total width " + sizeReference.getWidth() + " for " + sizeReference.getTabCount() + " tabs");
    	
    	int tabWidth=Math.round(sizeReference.getWidth()/sizeReference.getTabCount())-4;
    	
		return tabWidth; // the width of the tab    	
    }
    /** 
     * 
     */
    @Override
    protected int calculateMaxTabHeight(int tabPlacement) 
    {
        return super.calculateMaxTabHeight(tabPlacement);
        //return 21;
    }
    /** 
     * 
     */
    @Override
    protected void paintTabArea(Graphics g, int tabPlacement, int selectedIndex) 
    {
    	/**
        g.setColor (Color.WHITE);
        g.fillRoundRect (0, 0, tabPane.getWidth(), tabPane.getHeight(), 10, 10);
        */
    	
		Graphics2D g2 = (Graphics2D) g;

		g2.setColor(new Color (200,200,200));		
		g2.fillRect (1,1,tabPane.getWidth()-1, tabPane.getHeight()-1);
		
		g2.setColor(new Color (75,75,75));		
		g2.drawRect (1,1,tabPane.getWidth()-2, tabPane.getHeight()-2);    	
        
        super.paintTabArea (g, tabPlacement, selectedIndex);
    }
    /** 
     * 
     */
    @Override
    protected void paintTabBackground (Graphics g,
    								   int tabPlacement,
    								   int tabIndex, 
    								   int x, 
    								   int y, 
    								   int w, 
    								   int h, 
    								   boolean isSelected) 
    {
    	//super.paintTabBackground(g,tabPlacement,tabIndex,x,y,w,h,isSelected);
    	
    	Rectangle rect = rects[tabIndex];  

        int xpos = rect.x;
        int ypos = rect.y;
        int width = rect.width;
        int height = rect.width;
    	
		Graphics2D g2 = (Graphics2D) g;
		
        if (isSelected) 
        {
    		g2.setColor(new Color (0,220,220));		
    		g2.fillRect (xpos,ypos,width, height);
        } 
        else if (getRolloverTab() == tabIndex) 
        {
    		g2.setColor(new Color (0,220,220));		
    		g2.fillRect (xpos,ypos,width, height);
        } 
        else 
        {
    		g2.setColor(new Color (200,200,200));		
    		g2.fillRect (xpos,ypos,width, height);
        }		
		    	
    	/*
        Graphics2D g2d = (Graphics2D) g;
        ColorSet colorSet;

        Rectangle rect = rects[tabIndex];     

        if (isSelected) 
        {
            colorSet = selectedColorSet;
        } 
        else if (getRolloverTab() == tabIndex) 
        {
            //colorSet = hoverColorSet;
            colorSet = selectedColorSet;
        } 
        else 
        {
            colorSet = defaultColorSet;
        }

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASHoopG,RenderingHints.VALUE_ANTIALIAS_ON);

        int width = rect.width;
        int xpos = rect.x;
        
        if (tabIndex > 0) 
        {
            width--;
            xpos++;
        }

        g2d.setPaint(new GradientPaint(xpos, 0, colorSet.topGradColor1, xpos, 10, colorSet.topGradColor2));
        g2d.fillRoundRect(xpos, 0, width, 10, 10, 10);
        g2d.setPaint(new GradientPaint(0, 10, colorSet.bottomGradColor1, 0, 21, colorSet.bottomGradColor2));
        g2d.fillRoundRect(xpos, 10, width, 11, 10, 10);

        if (contentTopBorderDrawn) 
        {
            g2d.setColor(lineColor);
            g2d.drawLine(rect.x, 20, rect.x + rect.width - 1, 20);
        }
        */
    }
    /** 
     * 
     */
    @Override
    protected void paintTabBorder (Graphics g,
    							   int tabPlacement, 
    							   int tabIndex,
    							   int x, 
    							   int y, 
    							   int w, 
    							   int h, 
    							   boolean isSelected) 
    {
    	/*
        g.setColor (dividerColor);
        g.drawRoundRect(x, y, w, tabPane.getHeight(), 10, 10);
        */
    	
    	Graphics2D g2 = (Graphics2D) g;
    	
		g2.setColor(Color.BLACK);
		g2.drawLine(x,y+2,x,h-2);		
		g2.drawLine(x,y+2,w,y+2);
		g2.drawLine(x+w,y+2,x+w,h);
    }
    /** 
     * 
     */
    @Override
    protected void paintContentBorderTopEdge(Graphics g, int tabPlacement, int selectedIndex, int x, int y, int w, int h) 
    {
    	
    }
    /** 
     * 
     */
    @Override
    protected void paintContentBorderRightEdge(Graphics g, int tabPlacement, int selectedIndex, int x, int y, int w, int h) 
    {
        // Do nothing
    }
    /** 
     * 
     */
    @Override
    protected void paintContentBorderLeftEdge (Graphics g, 
    										   int tabPlacement,
    										   int selectedIndex, 
    										   int x, 
    										   int y, 
    										   int w, 
    										   int h) 
    {
        // Do nothing
    }
    /** 
     * 
     */
    @Override
    protected void paintContentBorderBottomEdge (Graphics g, 
    											 int tabPlacement,
    											 int selectedIndex, 
    											 int x, 
    											 int y, 
    											 int w, 
    											 int h) 
    {
        // Do nothing
    }
    /** 
     * 
     */
    @Override
    protected void paintFocusIndicator (Graphics g, 
    									int tabPlacement,
    									Rectangle[] rects, 
    									int tabIndex, 
    									Rectangle iconRect,
    									Rectangle textRect, 
    									boolean isSelected) 
    {
        // Do nothing
    }
    /** 
     * 
     */
    @Override
    protected int getTabLabelShiftY (int tabPlacement,
    								 int tabIndex,
    								 boolean isSelected) 
    {
        return 0;
    }
    
    
    /** 
     * 
     */
    private class ColorSet 
    {
        @SuppressWarnings("unused")
		public Color topGradColor1=new Color (125,125,125);
        
        @SuppressWarnings("unused")
        public Color topGradColor2=new Color (125,125,125);
        
        @SuppressWarnings("unused")
        public Color bottomGradColor1=new Color (125,125,125);
        
        @SuppressWarnings("unused")
        public Color bottomGradColor2=new Color (125,125,125);
    }

    /** 
     * 
     */
    private class RollOverListener implements MouseMotionListener, MouseListener 
    {
    	/** 
    	 * 
    	 */
        public void mouseDragged(MouseEvent e) 
        {
        	
        }
        /** 
         * 
         */
        public void mouseMoved(MouseEvent e) 
        {
            checkRollOver();
        }
        /** 
         * 
         */
        public void mouseClicked(MouseEvent e) 
        {
        	
        }
        /** 
         * 
         */
        public void mousePressed(MouseEvent e) 
        {
        	
        }

        public void mouseReleased(MouseEvent e) 
        {
        	
        }
        /** 
         * 
         */
        public void mouseEntered(MouseEvent e) 
        {
            checkRollOver();
        }
        /** 
         * 
         */
        public void mouseExited(MouseEvent e) 
        {
            tabPane.repaint();
        }
        /** 
         * 
         */
        private void checkRollOver() 
        {
            int currentRollOver = getRolloverTab();
            
            if (currentRollOver != lastRollOverTab) 
            {
                lastRollOverTab = currentRollOver;

                Rectangle tabsRect = new Rectangle(0, 0, tabPane.getWidth(), tabPane.getHeight());
                tabPane.repaint(tabsRect);
            }
        }
    }
}
