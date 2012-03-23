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
 */

package edu.cmu.cs.in.hoop;

import java.awt.Color;
//import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
//import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

/** 
 * 
 */
public class INHoopTabUI extends BasicTabbedPaneUI 
{
    private static final Insets NO_INSETS = new Insets(0, 0, 0, 0);
    private ColorSet selectedColorSet;
    private ColorSet defaultColorSet;
    private ColorSet hoverColorSet;
    private boolean contentTopBorderDrawn = true;
    private Color lineColor = new Color(158, 158, 158);
    private Color dividerColor = new Color(175,175,175);
    private Insets contentInsets = new Insets(10, 10, 10, 10);
    private int lastRollOverTab = -1;

    /** 
     * 
     */    
    public static ComponentUI createUI(JComponent c) 
    {
        return new INHoopTabUI();
    }
    /** 
     * 
     */
    public INHoopTabUI() 
    {
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

        maxTabHeight = 20;

        setContentInsets(0);
    }
    /** 
     * 
     */
    public void setContentTopBorderDrawn(boolean b) 
    {
        contentTopBorderDrawn = b;
    }
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

        tabAreaInsets = NO_INSETS;
        tabInsets = new Insets(0, 0, 0, 0);
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
        int w = super.calculateTabWidth(tabPlacement, tabIndex, metrics);
        int wid = metrics.charWidth('M');
        w += wid * 2;
        return w;
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
        g.setColor(Color.WHITE);

        g.fillRoundRect(0, 0, tabPane.getWidth(), tabPane.getHeight(), 10, 10);
        super.paintTabArea(g, tabPlacement, selectedIndex);
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

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

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
        g.setColor(dividerColor);
        g.drawRoundRect(x, y, w, tabPane.getHeight(), 10, 10);
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
        Color topGradColor1;
        Color topGradColor2;
        Color bottomGradColor1;
        Color bottomGradColor2;
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
