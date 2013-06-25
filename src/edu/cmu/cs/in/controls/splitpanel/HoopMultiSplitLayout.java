/*
 * $Id: HoopMultiSplitLayout.java,v 1.15 2005/10/26 14:29:54 hansmuller Exp $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

package edu.cmu.cs.in.controls.splitpanel;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import javax.swing.UIManager;

import edu.cmu.cs.in.base.HoopRoot;

/**
 * The HoopMultiSplitLayout layout manager recursively arranges its
 * components in row and column groups called "Splits".  Elements of
 * the layout are separated by gaps called "Dividers".  The overall
 * layout is defined with a simple tree model whose nodes are 
 * instances of HoopMultiSplitLayout.Split, HoopMultiSplitLayout.Divider, 
 * and HoopMultiSplitLayout.Leaf. Named Leaf nodes represent the space 
 * allocated to a component that was added with a constraint that
 * matches the Leaf's name.  Extra space is distributed
 * among row/column siblings according to their 0.0 to 1.0 weight.
 * If no weights are specified then the last sibling always gets
 * all of the extra space, or space reduction.
 * 
 * <p>
 * Although HoopMultiSplitLayout can be used with any Container, it's
 * the default layout manager for MultiSplitPane.  MultiSplitPane
 * supports interactively dragging the Dividers, accessibility, 
 * and other features associated with split panes.
 * 
 * <p>
 * All properties in this class are bound: when a properties value
 * is changed, all PropertyChangeListeners are fired.
 * 
 * @author Hans Muller
 * @see MultiSplitPane
 */

public class HoopMultiSplitLayout extends HoopRoot implements LayoutManager 
{
    private final Map<String, Component> childMap = new HashMap<String, Component>();
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private HoopMultiSplitNode model;
    private int dividerSize;
    private boolean floatingDividers = true;

    /**
     * Create a HoopMultiSplitLayout with a default model with a single
     * Leaf node named "default".  
     * 
     * #see setModel
     */
    public HoopMultiSplitLayout() 
    { 
    	this(new HoopMultiSplitLeaf("default"));
    	
		setClassName ("HoopMultiSplitLayout");
		debug ("HoopMultiSplitLayout ()");	    	
    }
    
    /**
     * Create a HoopMultiSplitLayout with the specified model.
     * 
     * #see setModel
     */
    public HoopMultiSplitLayout(HoopMultiSplitNode model) 
    {
		setClassName ("HoopMultiSplitLayout");
		debug ("HoopMultiSplitLayout ()");	
    	
    	this.model = model;
        this.dividerSize = UIManager.getInt("SplitPane.dividerSize");
        
        if (this.dividerSize == 0) 
        	
        {
            this.dividerSize = 7;
        }
    }
    /**
     * 
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) 
    {
        if (listener != null) 
        {
        	pcs.addPropertyChangeListener(listener);
        }
    }
    /**
     * 
     */    
    public void removePropertyChangeListener(PropertyChangeListener listener) 
    {
        if (listener != null) 
        {
        	pcs.removePropertyChangeListener(listener);
        }
    }
    /**
     * 
     */    
    public PropertyChangeListener[] getPropertyChangeListeners() 
    {
        return pcs.getPropertyChangeListeners();
    }
    /**
     * 
     */
    private void firePCS(String propertyName, Object oldValue, Object newValue) 
    {
    	if (!(oldValue != null && newValue != null && oldValue.equals(newValue))) 
    	{
    		pcs.firePropertyChange(propertyName, oldValue, newValue);
        }
    }
    /**
     * Return the root of the tree of Split, Leaf, and Divider nodes
     * that define this layout.  
     * 
     * @return the value of the model property
     * @see #setModel
     */
    public HoopMultiSplitNode getModel() 
    { 
    	return model; 
    }
    /**
     * Set the root of the tree of Split, Leaf, and Divider nodes
     * that define this layout.  The model can be a Split node
     * (the typical case) or a Leaf.  The default value of this
     * property is a Leaf named "default".
     * 
     * @param model the root of the tree of Split, Leaf, and Divider node
     * @throws IllegalArgumentException if model is a Divider or null
     * @see #getModel
     */
    public void setModel(HoopMultiSplitNode model) 
    {
    	if ((model == null) || (model instanceof HoopMultiSplitDivider)) 
    	{
    		throw new IllegalArgumentException("invalid model");
    	}
    	
    	HoopMultiSplitNode oldModel = model;
    	this.model = model;
    	firePCS("model", oldModel, model);
    }
    /**
     * Returns the width of Dividers in Split rows, and the height of 
     * Dividers in Split columns.
     *
     * @return the value of the dividerSize property
     * @see #setDividerSize
     */
    public int getDividerSize()
    { 
    	return dividerSize; 
    }
    /**
     * Sets the width of Dividers in Split rows, and the height of 
     * Dividers in Split columns.  The default value of this property
     * is the same as for JSplitPane Dividers.
     *
     * @param dividerSize the size of dividers (pixels)
     * @throws IllegalArgumentException if dividerSize < 0
     * @see #getDividerSize
     */
    public void setDividerSize(int dividerSize) 
    {
    	if (dividerSize < 0) 
    	{
    		throw new IllegalArgumentException("invalid dividerSize");
    	}
    	
    	int oldDividerSize = this.dividerSize;
    	this.dividerSize = dividerSize;
    	firePCS("dividerSize", oldDividerSize, dividerSize);
    }
    /**
     * @return the value of the floatingDividers property
     * @see #setFloatingDividers
     */
    public boolean getFloatingDividers()
    { 
    	return floatingDividers; 
    }
    /**
     * If true, Leaf node bounds match the corresponding component's 
     * preferred size and Splits/Dividers are resized accordingly.  
     * If false then the Dividers define the bounds of the adjacent
     * Split and Leaf nodes.  Typically this property is set to false
     * after the (MultiSplitPane) user has dragged a Divider.
     * 
     * @see #getFloatingDividers
     */
    public void setFloatingDividers(boolean floatingDividers) 
    {
    	boolean oldFloatingDividers = this.floatingDividers;
    	this.floatingDividers = floatingDividers;
    	firePCS("floatingDividers", oldFloatingDividers, floatingDividers);
    }
    /** 
     * Add a component to this HoopMultiSplitLayout.  The
     * <code>name</code> should match the name property of the Leaf
     * node that represents the bounds of <code>child</code>.  After
     * layoutContainer() recomputes the bounds of all of the nodes in
     * the model, it will set this child's bounds to the bounds of the
     * Leaf node with <code>name</code>.  Note: if a component was already
     * added with the same name, this method does not remove it from 
     * its parent.  
     * 
     * @param name identifies the Leaf node that defines the child's bounds
     * @param child the component to be added
     * @see #removeLayoutComponent
     */
    public void addLayoutComponent(String name, Component child) 
    {
    	if (name == null) 
    	{
    		throw new IllegalArgumentException("name not specified");
    	}
    	
    	childMap.put(name, child);
    }
    /**
     * Removes the specified component from the layout.
     * 
     * @param child the component to be removed
     * @see #addLayoutComponent
     */
    public void removeLayoutComponent(Component child) 
    {
    	String name = child.getName();
    	
    	if (name != null) 
    	{
    		childMap.remove(name);
    	}
    }
    /**
     * 
     */
    private Component childForHoopMultiSplitNode(HoopMultiSplitNode node) 
    {
    	if (node instanceof HoopMultiSplitLeaf) 
    	{
    		HoopMultiSplitLeaf leaf = (HoopMultiSplitLeaf)node;
    		String name = leaf.getName();
    		return (name != null) ? childMap.get(name) : null;
    	}
    	
    	return null;
    }
    /**
     * 
     */    
    private Dimension preferredComponentSize(HoopMultiSplitNode node) 
    {
    	Component child = childForHoopMultiSplitNode(node);
    	return (child != null) ? child.getPreferredSize() : new Dimension(0, 0);	
    }
    /**
     * 
     */
    private Dimension minimumComponentSize(HoopMultiSplitNode node) 
    {
    	Component child = childForHoopMultiSplitNode(node);
    	return (child != null) ? child.getMinimumSize() : new Dimension(0, 0);
    }
    /**
     * 
     */
    private Dimension preferredHoopMultiSplitNodeSize(HoopMultiSplitNode root) 
    {
    	if (root instanceof HoopMultiSplitLeaf) 
    	{
    		return preferredComponentSize(root);
    	}
    	else if (root instanceof HoopMultiSplitDivider) 
    	{
    		int dividerSize = getDividerSize();
    		return new Dimension(dividerSize, dividerSize);
    	}
    	else 
    	{
    		HoopMultiSplitSplitter split = (HoopMultiSplitSplitter)root;
    		List<HoopMultiSplitNode> splitChildren = split.getChildren();
    		int width = 0;
    		int height = 0;
    		
    		if (split.isRowLayout()) 
    		{ 
    			for(HoopMultiSplitNode splitChild : splitChildren) 
    			{
    				Dimension size = preferredHoopMultiSplitNodeSize(splitChild);
    				width += size.width;
    				height = Math.max(height, size.height);
    			}
    		}
    		else 
    		{
    			for(HoopMultiSplitNode splitChild : splitChildren) 
    			{
    				Dimension size = preferredHoopMultiSplitNodeSize(splitChild);
    				width = Math.max(width, size.width);
    				height += size.height;
    			}
    		}
    		
    		return new Dimension(width, height);
    	}
    }
    /**
     * 
     */
    private Dimension minimumHoopMultiSplitNodeSize(HoopMultiSplitNode root) 
    {
    	if (root instanceof HoopMultiSplitLeaf) 
    	{
    		Component child = childForHoopMultiSplitNode(root);
    		return (child != null) ? child.getMinimumSize() : new Dimension(0, 0);
    	}
    	else if (root instanceof HoopMultiSplitDivider) 
    	{
    		int dividerSize = getDividerSize();
    		return new Dimension(dividerSize, dividerSize);
    	}
    	else 
    	{
    		HoopMultiSplitSplitter split = (HoopMultiSplitSplitter)root;
    		List<HoopMultiSplitNode> splitChildren = split.getChildren();
    		int width = 0;
    		int height = 0;
    		if (split.isRowLayout()) 
    		{ 
    			for(HoopMultiSplitNode splitChild : splitChildren) 
    			{
    				Dimension size = minimumHoopMultiSplitNodeSize(splitChild);
    				width += size.width;
    				height = Math.max(height, size.height);
    			}
    		}
    		else 
    		{
    			for(HoopMultiSplitNode splitChild : splitChildren) 
    			{
    				Dimension size = minimumHoopMultiSplitNodeSize(splitChild);
    				width = Math.max(width, size.width);
    				height += size.height;
    			}
    		}
    		
    		return new Dimension(width, height);
    	}
    }
    /**
     * 
     */
    private Dimension sizeWithInsets(Container parent, Dimension size) 
    {
    	Insets insets = parent.getInsets();
    	int width = size.width + insets.left + insets.right;
    	int height = size.height + insets.top + insets.bottom;
    	return new Dimension(width, height);
    }
    /**
     * 
     */
    public Dimension preferredLayoutSize(Container parent) 
    {
    	Dimension size = preferredHoopMultiSplitNodeSize(getModel());
    	return sizeWithInsets(parent, size);
    }
    /**
     * 
     */
    public Dimension minimumLayoutSize(Container parent) 
    {
    	Dimension size = minimumHoopMultiSplitNodeSize(getModel());
    	return sizeWithInsets(parent, size);
    }
    /**
     * 
     */
    private Rectangle boundsWithYandHeight(Rectangle bounds, double y, double height) 
    {
    	Rectangle r = new Rectangle();
    	r.setBounds((int)(bounds.getX()), (int)y, (int)(bounds.getWidth()), (int)height);
    	return r;
    }
    /**
     * 
     */
    private Rectangle boundsWithXandWidth(Rectangle bounds, double x, double width) 
    {
    	Rectangle r = new Rectangle();
    	r.setBounds((int)x, (int)(bounds.getY()), (int)width, (int)(bounds.getHeight()));
    	return r;
    }
    /**
     * 
     */
    private void minimizeSplitBounds(HoopMultiSplitSplitter split, Rectangle bounds) 
    {
    	Rectangle splitBounds = new Rectangle(bounds.x, bounds.y, 0, 0);
    	List<HoopMultiSplitNode> splitChildren = split.getChildren();
    	HoopMultiSplitNode lastChild = splitChildren.get(splitChildren.size() - 1);
    	Rectangle lastChildBounds = lastChild.getBounds();
    	
    	if (split.isRowLayout()) 
    	{
    		int lastChildMaxX = lastChildBounds.x + lastChildBounds.width;
    		splitBounds.add(lastChildMaxX, bounds.y + bounds.height);
    	}
    	else 
    	{
    		int lastChildMaxY = lastChildBounds.y + lastChildBounds.height;
    		splitBounds.add(bounds.x + bounds.width, lastChildMaxY);
    	}
    	
    	split.setBounds(splitBounds);
    }
    /**
     * 
     */
    private void layoutShrink(HoopMultiSplitSplitter split, Rectangle bounds) 
    {
    	Rectangle splitBounds = split.getBounds();
    	ListIterator<HoopMultiSplitNode> splitChildren = split.getChildren().listIterator();
    	HoopMultiSplitNode lastWeightedChild = split.lastWeightedChild();

    	if (split.isRowLayout()) 
    	{
    		int totalWidth = 0;          // sum of the children's widths
    		int minWeightedWidth = 0;    // sum of the weighted childrens' min widths
    		int totalWeightedWidth = 0;  // sum of the weighted childrens' widths
    		
    		for(HoopMultiSplitNode splitChild : split.getChildren()) 
    		{
    			int nodeWidth = splitChild.getBounds().width;
    			int nodeMinWidth = Math.min(nodeWidth, minimumHoopMultiSplitNodeSize(splitChild).width);
    			totalWidth += nodeWidth;
    			if (splitChild.getWeight() > 0.0) 
    			{
    				minWeightedWidth += nodeMinWidth;
    				totalWeightedWidth += nodeWidth;
    			}
    		}

	    double x = bounds.getX();
	    double extraWidth = splitBounds.getWidth() - bounds.getWidth();
	    double availableWidth = extraWidth;
	    boolean onlyShrinkWeightedComponents = 
		(totalWeightedWidth - minWeightedWidth) > extraWidth;

	    while(splitChildren.hasNext()) {
		HoopMultiSplitNode splitChild = splitChildren.next();
		Rectangle splitChildBounds = splitChild.getBounds();
		double minSplitChildWidth = minimumHoopMultiSplitNodeSize(splitChild).getWidth();
		double splitChildWeight = (onlyShrinkWeightedComponents)
		    ? splitChild.getWeight()
		    : (splitChildBounds.getWidth() / (double)totalWidth);

		if (!splitChildren.hasNext()) {
		    double newWidth =  Math.max(minSplitChildWidth, bounds.getMaxX() - x); 
		    Rectangle newSplitChildBounds = boundsWithXandWidth(bounds, x, newWidth);
		    layout2(splitChild, newSplitChildBounds);
		}
		else if ((availableWidth > 0.0) && (splitChildWeight > 0.0)) {
		    double allocatedWidth = Math.rint(splitChildWeight * extraWidth);
		    double oldWidth = splitChildBounds.getWidth();
		    double newWidth = Math.max(minSplitChildWidth, oldWidth - allocatedWidth);
		    Rectangle newSplitChildBounds = boundsWithXandWidth(bounds, x, newWidth);
		    layout2(splitChild, newSplitChildBounds);
		    availableWidth -= (oldWidth - splitChild.getBounds().getWidth());
		}
		else {
		    double existingWidth = splitChildBounds.getWidth();
		    Rectangle newSplitChildBounds = boundsWithXandWidth(bounds, x, existingWidth);
		    layout2(splitChild, newSplitChildBounds);
		}
		x = splitChild.getBounds().getMaxX();
	    }
	}

	else {
	    int totalHeight = 0;          // sum of the children's heights
	    int minWeightedHeight = 0;    // sum of the weighted childrens' min heights
	    int totalWeightedHeight = 0;  // sum of the weighted childrens' heights
	    for(HoopMultiSplitNode splitChild : split.getChildren()) {
		int nodeHeight = splitChild.getBounds().height;
		int nodeMinHeight = Math.min(nodeHeight, minimumHoopMultiSplitNodeSize(splitChild).height);
		totalHeight += nodeHeight;
		if (splitChild.getWeight() > 0.0) {
		    minWeightedHeight += nodeMinHeight;
		    totalWeightedHeight += nodeHeight;
		}
	    }

	    double y = bounds.getY();
	    double extraHeight = splitBounds.getHeight() - bounds.getHeight();
	    double availableHeight = extraHeight;
	    boolean onlyShrinkWeightedComponents = 
		(totalWeightedHeight - minWeightedHeight) > extraHeight;

	    while(splitChildren.hasNext()) {
		HoopMultiSplitNode splitChild = splitChildren.next();
		Rectangle splitChildBounds = splitChild.getBounds();
		double minSplitChildHeight = minimumHoopMultiSplitNodeSize(splitChild).getHeight();
		double splitChildWeight = (onlyShrinkWeightedComponents)
		    ? splitChild.getWeight()
		    : (splitChildBounds.getHeight() / (double)totalHeight);

		if (!splitChildren.hasNext()) {
		    double oldHeight = splitChildBounds.getHeight();
		    double newHeight =  Math.max(minSplitChildHeight, bounds.getMaxY() - y); 
		    Rectangle newSplitChildBounds = boundsWithYandHeight(bounds, y, newHeight);
		    layout2(splitChild, newSplitChildBounds);
		    availableHeight -= (oldHeight - splitChild.getBounds().getHeight());
		}
		else if ((availableHeight > 0.0) && (splitChildWeight > 0.0)) {
		    double allocatedHeight = Math.rint(splitChildWeight * extraHeight);
		    double oldHeight = splitChildBounds.getHeight();
		    double newHeight = Math.max(minSplitChildHeight, oldHeight - allocatedHeight);
		    Rectangle newSplitChildBounds = boundsWithYandHeight(bounds, y, newHeight);
		    layout2(splitChild, newSplitChildBounds);
		    availableHeight -= (oldHeight - splitChild.getBounds().getHeight());
		}
		else {
		    double existingHeight = splitChildBounds.getHeight();
		    Rectangle newSplitChildBounds = boundsWithYandHeight(bounds, y, existingHeight);
		    layout2(splitChild, newSplitChildBounds);
		}
		y = splitChild.getBounds().getMaxY();
	    }
	}

	/* The bounds of the Split node root are set to be 
	 * big enough to contain all of its children. Since 
	 * Leaf children can't be reduced below their 
	 * (corresponding java.awt.Component) minimum sizes, 
	 * the size of the Split's bounds maybe be larger than
	 * the bounds we were asked to fit within.
	 */
	minimizeSplitBounds(split, bounds);
    }


    private void layoutGrow(HoopMultiSplitSplitter split, Rectangle bounds) {
	Rectangle splitBounds = split.getBounds();
	ListIterator<HoopMultiSplitNode> splitChildren = split.getChildren().listIterator();
	HoopMultiSplitNode lastWeightedChild = split.lastWeightedChild();

	/* Layout the Split's child HoopMultiSplitNodes' along the X axis.  The bounds 
	 * of each child will have the same y coordinate and height as the 
	 * layoutGrow() bounds argument.  Extra width is allocated to the 
	 * to each child with a non-zero weight:
	 *     newWidth = currentWidth + (extraWidth * splitChild.getWeight())
	 * Any extraWidth "left over" (that's availableWidth in the loop
	 * below) is given to the last child.  Note that Dividers always
	 * have a weight of zero, and they're never the last child.
	 */
	if (split.isRowLayout()) {
	    double x = bounds.getX();
	    double extraWidth = bounds.getWidth() - splitBounds.getWidth();
	    double availableWidth = extraWidth;

	    while(splitChildren.hasNext()) {
		HoopMultiSplitNode splitChild = splitChildren.next();
		Rectangle splitChildBounds = splitChild.getBounds();
		double splitChildWeight = splitChild.getWeight();

		if (!splitChildren.hasNext()) {  
		    double newWidth = bounds.getMaxX() - x; 
		    Rectangle newSplitChildBounds = boundsWithXandWidth(bounds, x, newWidth);
		    layout2(splitChild, newSplitChildBounds);
		}
		else if ((availableWidth > 0.0) && (splitChildWeight > 0.0)) {
		    double allocatedWidth = (splitChild.equals(lastWeightedChild)) 
			? availableWidth
			: Math.rint(splitChildWeight * extraWidth);
		    double newWidth = splitChildBounds.getWidth() + allocatedWidth;
		    Rectangle newSplitChildBounds = boundsWithXandWidth(bounds, x, newWidth);
		    layout2(splitChild, newSplitChildBounds);
		    availableWidth -= allocatedWidth;
		}
		else {
		    double existingWidth = splitChildBounds.getWidth();
		    Rectangle newSplitChildBounds = boundsWithXandWidth(bounds, x, existingWidth);
		    layout2(splitChild, newSplitChildBounds);
		}
		x = splitChild.getBounds().getMaxX();
	    }
	}

	/* Layout the Split's child HoopMultiSplitNodes' along the Y axis.  The bounds 
	 * of each child will have the same x coordinate and width as the 
	 * layoutGrow() bounds argument.  Extra height is allocated to the 
	 * to each child with a non-zero weight:
	 *     newHeight = currentHeight + (extraHeight * splitChild.getWeight())
	 * Any extraHeight "left over" (that's availableHeight in the loop
	 * below) is given to the last child.  Note that Dividers always
	 * have a weight of zero, and they're never the last child.
	 */
	else {
	    double y = bounds.getY();
	    double extraHeight = bounds.getMaxY() - splitBounds.getHeight();
	    double availableHeight = extraHeight;

	    while(splitChildren.hasNext()) {
		HoopMultiSplitNode splitChild = splitChildren.next();
		Rectangle splitChildBounds = splitChild.getBounds();
		double splitChildWeight = splitChild.getWeight();
		
		if (!splitChildren.hasNext()) {
		    double newHeight = bounds.getMaxY() - y; 
		    Rectangle newSplitChildBounds = boundsWithYandHeight(bounds, y, newHeight);
		    layout2(splitChild, newSplitChildBounds);
		}
		else if ((availableHeight > 0.0) && (splitChildWeight > 0.0)) {
		    double allocatedHeight = (splitChild.equals(lastWeightedChild)) 
			? availableHeight
			: Math.rint(splitChildWeight * extraHeight);
		    double newHeight = splitChildBounds.getHeight() + allocatedHeight;
		    Rectangle newSplitChildBounds = boundsWithYandHeight(bounds, y, newHeight);
		    layout2(splitChild, newSplitChildBounds);
		    availableHeight -= allocatedHeight;
		}
		else {
		    double existingHeight = splitChildBounds.getHeight();
		    Rectangle newSplitChildBounds = boundsWithYandHeight(bounds, y, existingHeight);
		    layout2(splitChild, newSplitChildBounds);
		}
		y = splitChild.getBounds().getMaxY();
	    }
	}
    }


    /* Second pass of the layout algorithm: branch to layoutGrow/Shrink
     * as needed.
     */
   private void layout2(HoopMultiSplitNode root, Rectangle bounds) 
   {
	   //debug ("layout2 ()");
	   
	   if (root instanceof HoopMultiSplitLeaf) 
	   {
		   Component child = childForHoopMultiSplitNode(root);
		   if (child != null) 
		   {
			   child.setBounds(bounds);
		   }
		   
		   root.setBounds(bounds);
	   }
	   else if (root instanceof HoopMultiSplitDivider) 
	   {
		   root.setBounds(bounds);
	   }
	   else if (root instanceof HoopMultiSplitSplitter) 
	   {
		   HoopMultiSplitSplitter split = (HoopMultiSplitSplitter)root;
		   boolean grow = split.isRowLayout() ? (split.getBounds().width <= bounds.width) : (split.getBounds().height <= bounds.height);
		   
		   if (grow) 
		   {
			   layoutGrow(split, bounds);
			   root.setBounds(bounds);
		   }
		   else 
		   {
			   layoutShrink(split, bounds);
                // split.setBounds() called in layoutShrink()
		   }
	   }
    }
    /* First pass of the layout algorithm.
     * 
     * If the Dividers are "floating" then set the bounds of each
     * node to accomodate the preferred size of all of the 
     * Leaf's java.awt.Components.  Otherwise, just set the bounds
     * of each Leaf/Split node so that it's to the left of (for
     * Split.isRowLayout() Split children) or directly above
     * the Divider that follows.
     * 
     * This pass sets the bounds of each HoopMultiSplitNode in the layout model.  It
     * does not resize any of the parent Container's
     * (java.awt.Component) children.  That's done in the second pass,
     * see layoutGrow() and layoutShrink().
     */
    private void layout1(HoopMultiSplitNode root, Rectangle bounds) 
    {
    	//debug ("layout1 ()");
    	
    	if (root instanceof HoopMultiSplitLeaf) 
    	{
    		root.setBounds(bounds);
    	}
    	else if (root instanceof HoopMultiSplitSplitter) 
    	{
    		HoopMultiSplitSplitter split = (HoopMultiSplitSplitter)root;
    		Iterator<HoopMultiSplitNode> splitChildren = split.getChildren().iterator();
    		Rectangle childBounds = null;
    		int dividerSize = getDividerSize();
	    
    		/* Layout the Split's child HoopMultiSplitNodes' along the X axis.  The bounds 
    		 * of each child will have the same y coordinate and height as the 
    		 * layout1() bounds argument.  
    		 * 
    		 * Note: the column layout code - that's the "else" clause below
    		 * this if, is identical to the X axis (rowLayout) code below.
    		 */
    		if (split.isRowLayout()) 
    		{
    			double x = bounds.getX();
    			
    			while(splitChildren.hasNext()) 
    			{
    				HoopMultiSplitNode splitChild = splitChildren.next();
    				HoopMultiSplitDivider dividerChild =(splitChildren.hasNext()) ? (HoopMultiSplitDivider)(splitChildren.next()) : null;

    				double childWidth = 0.0;
    				if (getFloatingDividers()) 
    				{
    					childWidth = preferredHoopMultiSplitNodeSize(splitChild).getWidth();
    				}
    				else 
    				{
    					if (dividerChild != null) 
    					{
    						childWidth = dividerChild.getBounds().getX() - x;
    					}
    					else 
    					{
    						childWidth = split.getBounds().getMaxX() - x;
    					}
    				}
    				
    				childBounds = boundsWithXandWidth(bounds, x, childWidth);
    				layout1(splitChild, childBounds);

    				if (getFloatingDividers() && (dividerChild != null)) 
    				{
    					double dividerX = childBounds.getMaxX();
    					Rectangle dividerBounds = boundsWithXandWidth(bounds, dividerX, dividerSize);
    					dividerChild.setBounds(dividerBounds);
    				}
		    
    				if (dividerChild != null) 
    				{
    					x = dividerChild.getBounds().getMaxX();
    				}
    			}
    		}

    		/* Layout the Split's child HoopMultiSplitNodes' along the Y axis.  The bounds 
    		 * of each child will have the same x coordinate and width as the 
    		 * layout1() bounds argument.  The algorithm is identical to what's
    		 * explained above, for the X axis case.
    		 */
    		else 
    		{
    			double y = bounds.getY();
    			
    			while(splitChildren.hasNext()) 
    			{
    				HoopMultiSplitNode splitChild = splitChildren.next();
    				HoopMultiSplitDivider dividerChild = (splitChildren.hasNext()) ? (HoopMultiSplitDivider)(splitChildren.next()) : null;

    				double childHeight = 0.0;
    				
    				if (getFloatingDividers()) 
    				{
    					childHeight = preferredHoopMultiSplitNodeSize(splitChild).getHeight();
    				}
    				else 
    				{
    					if (dividerChild != null) 
    					{
    						childHeight = dividerChild.getBounds().getY() - y;
    					}
    					else 
    					{
    						childHeight = split.getBounds().getMaxY() - y;
    					}
    				}
    				
    				childBounds = boundsWithYandHeight(bounds, y, childHeight);
    				layout1(splitChild, childBounds);

    				if (getFloatingDividers() && (dividerChild != null)) 
    				{
    					double dividerY = childBounds.getMaxY();
    					Rectangle dividerBounds = boundsWithYandHeight(bounds, dividerY, dividerSize);
    					dividerChild.setBounds(dividerBounds);
    				}
    				if (dividerChild != null) 
    				{
    					y = dividerChild.getBounds().getMaxY();
    				}
    			}
    		}
    		
    		/* The bounds of the Split node root are set to be just
    		 * big enough to contain all of its children, but only
    		 * along the axis it's allocating space on.  That's 
    		 * X for rows, Y for columns.  The second pass of the 
    		 * layout algorithm - see layoutShrink()/layoutGrow() 
    		 * allocates extra space.
    		 */
    		
    		minimizeSplitBounds(split, bounds);
    	}
    }

    /** 
     * The specified HoopMultiSplitNode is either the wrong type or was configured
     * incorrectly.
     */
    public static class InvalidLayoutException extends RuntimeException 
    {
    	private final HoopMultiSplitNode node;
    	
    	public InvalidLayoutException (String msg, HoopMultiSplitNode node) 
    	{
    		super(msg);
    		this.node = node;
    	}
    	/** 
    	 * @return the invalid HoopMultiSplitNode.
    	 */
    	public HoopMultiSplitNode getHoopMultiSplitNode() 
    	{ 
    		return node; 
    	}
    }

    private void throwInvalidLayout(String msg, HoopMultiSplitNode node) 
    {
    	throw new InvalidLayoutException(msg, node);
    }

    /**
     * This method checks to see if any of the weights in the current split
     * add up to 1.0 or more.
     * 
     * @param root
     */
    private void checkLayout(HoopMultiSplitNode root) 
    {
    	//debug ("checkLayout ()");
    	
    	if (root instanceof HoopMultiSplitSplitter) 
    	{
    		HoopMultiSplitSplitter split = (HoopMultiSplitSplitter)root;
    		if (split.getChildren().size() <= 2) 
    		{
    			throwInvalidLayout("Split must have > 2 children", root);
    		}
    		
    		Iterator<HoopMultiSplitNode> splitChildren = split.getChildren().iterator();
    		double weight = 0.0;
    		
    		while(splitChildren.hasNext()) 
    		{
    			HoopMultiSplitNode splitChild = splitChildren.next();
    			
    			if (splitChild instanceof HoopMultiSplitDivider) 
    			{
    				throwInvalidLayout("expected a Split or Leaf HoopMultiSplitNode", splitChild);
    			}
    			
    			if (splitChildren.hasNext()) 
    			{
    				HoopMultiSplitNode dividerChild = splitChildren.next();
    				
    				if (!(dividerChild instanceof HoopMultiSplitDivider)) 
    				{
    					throwInvalidLayout("expected a Divider HoopMultiSplitNode", dividerChild);
    				}
    			}
    			
    			weight += splitChild.getWeight();
    			checkLayout(splitChild);
    		}
    		
    		if (weight > 1.0) 
    		{
    			throwInvalidLayout("Split children's total weight > 1.0", root);
    		}
    	}
    }

    /** 
     * Compute the bounds of all of the Split/Divider/Leaf HoopMultiSplitNodes in 
     * the layout model, and then set the bounds of each child component
     * with a matching Leaf HoopMultiSplitNode.
     */
    public void layoutContainer(Container parent) 
    {
    	//debug ("layoutContainer ()");
    	
    	checkLayout(getModel());
    	
    	Insets insets = parent.getInsets();
    	Dimension size = parent.getSize();
    	int width = size.width - (insets.left + insets.right);
    	int height = size.height - (insets.top + insets.bottom);
    	
    	//debug ("Size: " + width + "," + height);
    	
    	Rectangle bounds = new Rectangle(insets.left, insets.top, width, height);
    	
    	layout1(getModel(), bounds);
    	
    	layout2(getModel(), bounds);    	
    }
    /**
     * 
     * @param root
     * @param x
     * @param y
     * @return
     */
    private HoopMultiSplitDivider dividerAt(HoopMultiSplitNode root, int x, int y) 
    {
    	if (root instanceof HoopMultiSplitDivider) 
    	{
    		HoopMultiSplitDivider divider = (HoopMultiSplitDivider)root;
    		return (divider.getBounds().contains(x, y)) ? divider : null;
    	}
    	else if (root instanceof HoopMultiSplitSplitter) 
    	{
    		HoopMultiSplitSplitter split = (HoopMultiSplitSplitter)root;
    		for(HoopMultiSplitNode child : split.getChildren()) 
    		{
    			if (child.getBounds().contains(x, y)) 
    			{
    				return dividerAt(child, x, y);
    			}
    		}
    	}
    	
    	return null;
    }

    /** 
     * Return the Divider whose bounds contain the specified
     * point, or null if there isn't one.
     * 
     * @param x x coordinate
     * @param y y coordinate
     * @return the Divider at x,y
     */
    public HoopMultiSplitDivider dividerAt(int x, int y) 
    {
    	return dividerAt(getModel(), x, y);
    }
    /**
     * 
     * @param node
     * @param r2
     * @return
     */
    private boolean nodeOverlapsRectangle(HoopMultiSplitNode node, Rectangle r2) 
    {
    	Rectangle r1 = node.getBounds();
    	
    	return 
	    (r1.x <= (r2.x + r2.width)) && ((r1.x + r1.width) >= r2.x) &&
	    (r1.y <= (r2.y + r2.height)) && ((r1.y + r1.height) >= r2.y);
    }
    /**
     * 
     * @param root
     * @param r
     * @return
     */
    private List<HoopMultiSplitDivider> dividersThatOverlap(HoopMultiSplitNode root, Rectangle r) 
    {
    	if (nodeOverlapsRectangle(root, r) && (root instanceof HoopMultiSplitSplitter)) 
    	{
    		List<HoopMultiSplitDivider> dividers = new ArrayList();
    		
    		for(HoopMultiSplitNode child : ((HoopMultiSplitSplitter)root).getChildren()) 
    		{
    			if (child instanceof HoopMultiSplitDivider) 
    			{
    				if (nodeOverlapsRectangle(child, r)) 
    				{
    					dividers.add((HoopMultiSplitDivider)child);
    				}
    			}
    			else if (child instanceof HoopMultiSplitSplitter) 
    			{
    				dividers.addAll(dividersThatOverlap(child, r));
    			}
    		}
            return dividers;
    	}
    	else 
    	{
    		return Collections.emptyList();
    	}
    }

    /**
     * Return the Dividers whose bounds overlap the specified
     * Rectangle.
     * 
     * @param r target Rectangle
     * @return the Dividers that overlap r
     * @throws IllegalArgumentException if the Rectangle is null
     */
    public List<HoopMultiSplitDivider> dividersThatOverlap(Rectangle r) 
    {
    	if (r == null) 
    	{
    		throw new IllegalArgumentException("null Rectangle");
    	}
    	
    	return dividersThatOverlap(getModel(), r);
    }

    /**
     * 
     */
    private static void throwParseException(StreamTokenizer st, String msg) throws Exception 
    {
    	throw new Exception("HoopMultiSplitLayout.parseModel Error: " + msg);
    }
    /**
     * 
     */
    private static void parseAttribute(String name, StreamTokenizer st, HoopMultiSplitNode node) throws Exception 
    {
    	if ((st.nextToken() != '=')) 
    	{
    		throwParseException(st, "expected '=' after " + name);
    	}
    	
    	if (name.equalsIgnoreCase("WEIGHT")) 
    	{
    		if (st.nextToken() == StreamTokenizer.TT_NUMBER) 
    		{
    			node.setWeight(st.nval);
    		}
    		else 
    		{ 
    			throwParseException(st, "invalid weight");
    		}
    	}
    	else if (name.equalsIgnoreCase("NAME")) 
    	{
    		if (st.nextToken() == StreamTokenizer.TT_WORD) 
    		{
    			if (node instanceof HoopMultiSplitLeaf) 
    			{
    				((HoopMultiSplitLeaf)node).setName(st.sval);
    			}
    			else 
    			{
    				throwParseException(st, "can't specify name for " + node);
    			}
    		}
    		else 
    		{
    			throwParseException(st, "invalid name");
    		}
    	}
    	else 
    	{
    		throwParseException(st, "unrecognized attribute \"" + name + "\"");
    	}
    }
    /**
     * 
     */
    private static void addSplitChild(HoopMultiSplitSplitter parent, HoopMultiSplitNode child) 
    {
    	List<HoopMultiSplitNode> children = new ArrayList(parent.getChildren());
    	
    	if (children.size() == 0) 
    	{
    		children.add(child);
    	}
    	else 
    	{
    		children.add(new HoopMultiSplitDivider());
    		children.add(child);
    	}
    	
    	parent.setChildren(children);
    }
    /**
     * 
     */
    private static void parseLeaf(StreamTokenizer st, HoopMultiSplitSplitter parent) throws Exception 
    {
    	HoopMultiSplitLeaf leaf = new HoopMultiSplitLeaf();
    	int token;
    	
    	while ((token = st.nextToken()) != StreamTokenizer.TT_EOF) 
    	{
    		if (token == ')') 
    		{
    			break;
    		}
    		if (token == StreamTokenizer.TT_WORD) 
    		{
    			parseAttribute(st.sval, st, leaf);
    		}
    		else 
    		{
    			throwParseException(st, "Bad Leaf: " + leaf);
    		}
    	}
    	
    	addSplitChild(parent, leaf);
    }
    /**
     * 
     */
    private static void parseSplit(StreamTokenizer st, HoopMultiSplitSplitter parent) throws Exception 
    {
    	int token;
    	
    	while ((token = st.nextToken()) != StreamTokenizer.TT_EOF) 
    	{
    		if (token == ')') 
    		{
    			break;
    		}
    		else if (token == StreamTokenizer.TT_WORD) 
    		{
    			if (st.sval.equalsIgnoreCase("WEIGHT")) 
    			{
    				parseAttribute(st.sval, st, parent);
    			}
    			else 
    			{
    				addSplitChild(parent, new HoopMultiSplitLeaf(st.sval));
    			}
    		}
    		else if (token == '(') 
    		{
    			if ((token = st.nextToken()) != StreamTokenizer.TT_WORD) 
    			{
    				throwParseException(st, "invalid node type");
    			}
    			
    			String nodeType = st.sval.toUpperCase();
    			
    			if (nodeType.equals("LEAF")) 
    			{
    				parseLeaf(st, parent);
    			}
    			else if (nodeType.equals("ROW") || nodeType.equals("COLUMN")) 
    			{
    				HoopMultiSplitSplitter split = new HoopMultiSplitSplitter();
    				split.setRowLayout(nodeType.equals("ROW"));
    				addSplitChild(parent, split);
    				parseSplit(st, split);
    			}
    			else 
    			{
    				throwParseException(st, "unrecognized node type '" + nodeType + "'");
    			}
    		}
    	}
    }
    /**
     * 
     */
    private static HoopMultiSplitNode parseModel (Reader r) 
    {
    	StreamTokenizer st = new StreamTokenizer(r);
    	
    	try 
    	{
    		HoopMultiSplitSplitter root = new HoopMultiSplitSplitter();
    		
    		parseSplit(st, root);
    		
    		return root.getChildren().get(0);
    	}
    	catch (Exception e) 
    	{
    		HoopRoot.debug ("HoopMultiSplitLayout",e.toString());
    	}
    	finally 
    	{
    		try { r.close(); } catch (IOException ignore) {}
    	}
    	return null;
    }

    /**
     * A convenience method that converts a string to a 
     * HoopMultiSplitLayout model (a tree of HoopMultiSplitNodes) using a 
     * a simple syntax.  HoopMultiSplitNodes are represented by 
     * parenthetical expressions whose first token 
     * is one of ROW/COLUMN/LEAF.  ROW and COLUMN specify
     * horizontal and vertical Split nodes respectively, 
     * LEAF specifies a Leaf node.  A Leaf's name and 
     * weight can be specified with attributes, 
     * name=<i>myLeafName</i> weight=<i>myLeafWeight</i>.
     * Similarly, a Split's weight can be specified with
     * weight=<i>mySplitWeight</i>.
     * 
     * <p> For example, the following expression generates
     * a horizontal Split node with three children:
     * the Leafs named left and right, and a Divider in 
     * between:
     * <pre>
     * (ROW (LEAF name=left) (LEAF name=right weight=1.0))
     * </pre>
     * 
     * <p> Dividers should not be included in the string, 
     * they're added automatcially as needed.  Because 
     * Leaf nodes often only need to specify a name, one
     * can specify a Leaf by just providing the name.
     * The previous example can be written like this:
     * <pre>
     * (ROW left (LEAF name=right weight=1.0))
     * </pre>
     * 
     * <p>Here's a more complex example.  One row with
     * three elements, the first and last of which are columns
     * with two leaves each:
     * <pre>
     * (ROW (COLUMN weight=0.5 left.top left.bottom) 
     *      (LEAF name=middle)
     *      (COLUMN weight=0.5 right.top right.bottom))
     * </pre>
     * 
     * 
     * <p> This syntax is not intended for archiving or 
     * configuration files .  It's just a convenience for
     * examples and tests.
     * 
     * @return the HoopMultiSplitNode root of a tree based on s.
     */
    public static HoopMultiSplitNode parseModel(String s) 
    {
    	return parseModel(new StringReader(s));
    }
    /**
     * 
     */
    private static void printModel(String indent, HoopMultiSplitNode root) 
    {
    	if (root instanceof HoopMultiSplitSplitter) 
    	{
    		HoopMultiSplitSplitter split = (HoopMultiSplitSplitter)root;
    		
    		HoopRoot.debug("HoopMultiSplitLayout",indent + split);
    		
    		for(HoopMultiSplitNode child : split.getChildren()) 
    		{
    			printModel(indent + "  ", child);
    		}
    	}
    	else 
    	{
    		HoopRoot.debug("HoopMultiSplitLayout",indent + root);
    	}
    }
    /** 
     * Print the tree with enough detail for simple debugging.
     */
    public static void printModel(HoopMultiSplitNode root) 
    {
    	printModel("", root);
    }
}
