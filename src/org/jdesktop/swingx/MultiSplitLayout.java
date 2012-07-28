/*
 * $Id: MultiSplitLayout.java,v 1.15 2005/10/26 14:29:54 hansmuller Exp $
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

package org.jdesktop.swingx;

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
 * The MultiSplitLayout layout manager recursively arranges its
 * components in row and column groups called "Splits".  Elements of
 * the layout are separated by gaps called "Dividers".  The overall
 * layout is defined with a simple tree model whose nodes are 
 * instances of MultiSplitLayout.Split, MultiSplitLayout.Divider, 
 * and MultiSplitLayout.Leaf. Named Leaf nodes represent the space 
 * allocated to a component that was added with a constraint that
 * matches the Leaf's name.  Extra space is distributed
 * among row/column siblings according to their 0.0 to 1.0 weight.
 * If no weights are specified then the last sibling always gets
 * all of the extra space, or space reduction.
 * 
 * <p>
 * Although MultiSplitLayout can be used with any Container, it's
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

public class MultiSplitLayout extends HoopRoot implements LayoutManager 
{
    private final Map<String, Component> childMap = new HashMap<String, Component>();
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private MultiSplitNode model;
    private int dividerSize;
    private boolean floatingDividers = true;

    /**
     * Create a MultiSplitLayout with a default model with a single
     * Leaf node named "default".  
     * 
     * #see setModel
     */
    public MultiSplitLayout() 
    { 
    	this(new MultiSplitLeaf("default"));
    	
		setClassName ("MultiSplitLayout");
		debug ("MultiSplitLayout ()");	    	
    }
    
    /**
     * Create a MultiSplitLayout with the specified model.
     * 
     * #see setModel
     */
    public MultiSplitLayout(MultiSplitNode model) 
    {
		setClassName ("MultiSplitLayout");
		debug ("MultiSplitLayout ()");	
    	
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
    public MultiSplitNode getModel() 
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
    public void setModel(MultiSplitNode model) 
    {
    	if ((model == null) || (model instanceof MultiSplitDivider)) 
    	{
    		throw new IllegalArgumentException("invalid model");
    	}
    	
    	MultiSplitNode oldModel = model;
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
     * Add a component to this MultiSplitLayout.  The
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
    private Component childForMultiSplitNode(MultiSplitNode node) 
    {
    	if (node instanceof MultiSplitLeaf) 
    	{
    		MultiSplitLeaf leaf = (MultiSplitLeaf)node;
    		String name = leaf.getName();
    		return (name != null) ? childMap.get(name) : null;
    	}
    	
    	return null;
    }
    /**
     * 
     */    
    private Dimension preferredComponentSize(MultiSplitNode node) 
    {
    	Component child = childForMultiSplitNode(node);
    	return (child != null) ? child.getPreferredSize() : new Dimension(0, 0);	
    }
    /**
     * 
     */
    private Dimension minimumComponentSize(MultiSplitNode node) 
    {
    	Component child = childForMultiSplitNode(node);
    	return (child != null) ? child.getMinimumSize() : new Dimension(0, 0);
    }
    /**
     * 
     */
    private Dimension preferredMultiSplitNodeSize(MultiSplitNode root) 
    {
    	if (root instanceof MultiSplitLeaf) 
    	{
    		return preferredComponentSize(root);
    	}
    	else if (root instanceof MultiSplitDivider) 
    	{
    		int dividerSize = getDividerSize();
    		return new Dimension(dividerSize, dividerSize);
    	}
    	else 
    	{
    		MultiSplitSplitter split = (MultiSplitSplitter)root;
    		List<MultiSplitNode> splitChildren = split.getChildren();
    		int width = 0;
    		int height = 0;
    		
    		if (split.isRowLayout()) 
    		{ 
    			for(MultiSplitNode splitChild : splitChildren) 
    			{
    				Dimension size = preferredMultiSplitNodeSize(splitChild);
    				width += size.width;
    				height = Math.max(height, size.height);
    			}
    		}
    		else 
    		{
    			for(MultiSplitNode splitChild : splitChildren) 
    			{
    				Dimension size = preferredMultiSplitNodeSize(splitChild);
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
    private Dimension minimumMultiSplitNodeSize(MultiSplitNode root) 
    {
    	if (root instanceof MultiSplitLeaf) 
    	{
    		Component child = childForMultiSplitNode(root);
    		return (child != null) ? child.getMinimumSize() : new Dimension(0, 0);
    	}
    	else if (root instanceof MultiSplitDivider) 
    	{
    		int dividerSize = getDividerSize();
    		return new Dimension(dividerSize, dividerSize);
    	}
    	else 
    	{
    		MultiSplitSplitter split = (MultiSplitSplitter)root;
    		List<MultiSplitNode> splitChildren = split.getChildren();
    		int width = 0;
    		int height = 0;
    		if (split.isRowLayout()) 
    		{ 
    			for(MultiSplitNode splitChild : splitChildren) 
    			{
    				Dimension size = minimumMultiSplitNodeSize(splitChild);
    				width += size.width;
    				height = Math.max(height, size.height);
    			}
    		}
    		else 
    		{
    			for(MultiSplitNode splitChild : splitChildren) 
    			{
    				Dimension size = minimumMultiSplitNodeSize(splitChild);
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
    	Dimension size = preferredMultiSplitNodeSize(getModel());
    	return sizeWithInsets(parent, size);
    }
    /**
     * 
     */
    public Dimension minimumLayoutSize(Container parent) 
    {
    	Dimension size = minimumMultiSplitNodeSize(getModel());
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
    private void minimizeSplitBounds(MultiSplitSplitter split, Rectangle bounds) 
    {
    	Rectangle splitBounds = new Rectangle(bounds.x, bounds.y, 0, 0);
    	List<MultiSplitNode> splitChildren = split.getChildren();
    	MultiSplitNode lastChild = splitChildren.get(splitChildren.size() - 1);
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
    private void layoutShrink(MultiSplitSplitter split, Rectangle bounds) 
    {
    	Rectangle splitBounds = split.getBounds();
    	ListIterator<MultiSplitNode> splitChildren = split.getChildren().listIterator();
    	MultiSplitNode lastWeightedChild = split.lastWeightedChild();

    	if (split.isRowLayout()) 
    	{
    		int totalWidth = 0;          // sum of the children's widths
    		int minWeightedWidth = 0;    // sum of the weighted childrens' min widths
    		int totalWeightedWidth = 0;  // sum of the weighted childrens' widths
    		
    		for(MultiSplitNode splitChild : split.getChildren()) 
    		{
    			int nodeWidth = splitChild.getBounds().width;
    			int nodeMinWidth = Math.min(nodeWidth, minimumMultiSplitNodeSize(splitChild).width);
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
		MultiSplitNode splitChild = splitChildren.next();
		Rectangle splitChildBounds = splitChild.getBounds();
		double minSplitChildWidth = minimumMultiSplitNodeSize(splitChild).getWidth();
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
	    for(MultiSplitNode splitChild : split.getChildren()) {
		int nodeHeight = splitChild.getBounds().height;
		int nodeMinHeight = Math.min(nodeHeight, minimumMultiSplitNodeSize(splitChild).height);
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
		MultiSplitNode splitChild = splitChildren.next();
		Rectangle splitChildBounds = splitChild.getBounds();
		double minSplitChildHeight = minimumMultiSplitNodeSize(splitChild).getHeight();
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


    private void layoutGrow(MultiSplitSplitter split, Rectangle bounds) {
	Rectangle splitBounds = split.getBounds();
	ListIterator<MultiSplitNode> splitChildren = split.getChildren().listIterator();
	MultiSplitNode lastWeightedChild = split.lastWeightedChild();

	/* Layout the Split's child MultiSplitNodes' along the X axis.  The bounds 
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
		MultiSplitNode splitChild = splitChildren.next();
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

	/* Layout the Split's child MultiSplitNodes' along the Y axis.  The bounds 
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
		MultiSplitNode splitChild = splitChildren.next();
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
   private void layout2(MultiSplitNode root, Rectangle bounds) {
	if (root instanceof MultiSplitLeaf) {
	    Component child = childForMultiSplitNode(root);
	    if (child != null) {
		child.setBounds(bounds);
	    }
	    root.setBounds(bounds);
	}
	else if (root instanceof MultiSplitDivider) {
	    root.setBounds(bounds);
	}
	else if (root instanceof MultiSplitSplitter) {
		MultiSplitSplitter split = (MultiSplitSplitter)root;
	    boolean grow = split.isRowLayout() 
		? (split.getBounds().width <= bounds.width)
		: (split.getBounds().height <= bounds.height);
	    if (grow) {
		layoutGrow(split, bounds);
		root.setBounds(bounds);
	    }
	    else {
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
     * This pass sets the bounds of each MultiSplitNode in the layout model.  It
     * does not resize any of the parent Container's
     * (java.awt.Component) children.  That's done in the second pass,
     * see layoutGrow() and layoutShrink().
     */
    private void layout1(MultiSplitNode root, Rectangle bounds) {
	if (root instanceof MultiSplitLeaf) {
	    root.setBounds(bounds);
	}
	else if (root instanceof MultiSplitSplitter) {
		MultiSplitSplitter split = (MultiSplitSplitter)root;
	    Iterator<MultiSplitNode> splitChildren = split.getChildren().iterator();
	    Rectangle childBounds = null;
	    int dividerSize = getDividerSize();
	    
	    /* Layout the Split's child MultiSplitNodes' along the X axis.  The bounds 
	     * of each child will have the same y coordinate and height as the 
	     * layout1() bounds argument.  
	     * 
	     * Note: the column layout code - that's the "else" clause below
	     * this if, is identical to the X axis (rowLayout) code below.
	     */
	    if (split.isRowLayout()) {
		double x = bounds.getX();
		while(splitChildren.hasNext()) {
		    MultiSplitNode splitChild = splitChildren.next();
		    MultiSplitDivider dividerChild = 
			(splitChildren.hasNext()) ? (MultiSplitDivider)(splitChildren.next()) : null;

		    double childWidth = 0.0;
		    if (getFloatingDividers()) {
			childWidth = preferredMultiSplitNodeSize(splitChild).getWidth();
		    }
		    else {
			if (dividerChild != null) {
			    childWidth = dividerChild.getBounds().getX() - x;
			}
			else {
			    childWidth = split.getBounds().getMaxX() - x;
			}
		    }
		    childBounds = boundsWithXandWidth(bounds, x, childWidth);
		    layout1(splitChild, childBounds);

		    if (getFloatingDividers() && (dividerChild != null)) {
			double dividerX = childBounds.getMaxX();
			Rectangle dividerBounds = boundsWithXandWidth(bounds, dividerX, dividerSize);
			dividerChild.setBounds(dividerBounds);
		    }
		    if (dividerChild != null) {
			x = dividerChild.getBounds().getMaxX();
		    }
		}
	    }

	    /* Layout the Split's child MultiSplitNodes' along the Y axis.  The bounds 
	     * of each child will have the same x coordinate and width as the 
	     * layout1() bounds argument.  The algorithm is identical to what's
	     * explained above, for the X axis case.
	     */
	    else {
		double y = bounds.getY();
		while(splitChildren.hasNext()) {
		    MultiSplitNode splitChild = splitChildren.next();
		    MultiSplitDivider dividerChild = 
			(splitChildren.hasNext()) ? (MultiSplitDivider)(splitChildren.next()) : null;

		    double childHeight = 0.0;
		    if (getFloatingDividers()) {
			childHeight = preferredMultiSplitNodeSize(splitChild).getHeight();
		    }
		    else {
			if (dividerChild != null) {
			    childHeight = dividerChild.getBounds().getY() - y;
			}
			else {
			    childHeight = split.getBounds().getMaxY() - y;
			}
		    }
		    childBounds = boundsWithYandHeight(bounds, y, childHeight);
		    layout1(splitChild, childBounds);

		    if (getFloatingDividers() && (dividerChild != null)) {
			double dividerY = childBounds.getMaxY();
			Rectangle dividerBounds = boundsWithYandHeight(bounds, dividerY, dividerSize);
			dividerChild.setBounds(dividerBounds);
		    }
		    if (dividerChild != null) {
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
     * The specified MultiSplitNode is either the wrong type or was configured
     * incorrectly.
     */
    public static class InvalidLayoutException extends RuntimeException 
    {
    	private final MultiSplitNode node;
    	
    	public InvalidLayoutException (String msg, MultiSplitNode node) 
    	{
    		super(msg);
    		this.node = node;
    	}
    	/** 
    	 * @return the invalid MultiSplitNode.
    	 */
    	public MultiSplitNode getMultiSplitNode() 
    	{ 
    		return node; 
    	}
    }

    private void throwInvalidLayout(String msg, MultiSplitNode node) 
    {
    	throw new InvalidLayoutException(msg, node);
    }

    private void checkLayout(MultiSplitNode root) 
    {
    	if (root instanceof MultiSplitSplitter) 
    	{
    		MultiSplitSplitter split = (MultiSplitSplitter)root;
    		if (split.getChildren().size() <= 2) 
    		{
    			throwInvalidLayout("Split must have > 2 children", root);
    		}
	    Iterator<MultiSplitNode> splitChildren = split.getChildren().iterator();
	    double weight = 0.0;
	    while(splitChildren.hasNext()) {
		MultiSplitNode splitChild = splitChildren.next();
		if (splitChild instanceof MultiSplitDivider) {
		    throwInvalidLayout("expected a Split or Leaf MultiSplitNode", splitChild);
		}
		if (splitChildren.hasNext()) {
		    MultiSplitNode dividerChild = splitChildren.next();
		    if (!(dividerChild instanceof MultiSplitDivider)) {
			throwInvalidLayout("expected a Divider MultiSplitNode", dividerChild);
		    }
		}
		weight += splitChild.getWeight();
		checkLayout(splitChild);
	    }
	    if (weight > 1.0) {
		throwInvalidLayout("Split children's total weight > 1.0", root);
	    }
	}
    }

    /** 
     * Compute the bounds of all of the Split/Divider/Leaf MultiSplitNodes in 
     * the layout model, and then set the bounds of each child component
     * with a matching Leaf MultiSplitNode.
     */
    public void layoutContainer(Container parent) {
	checkLayout(getModel());
	Insets insets = parent.getInsets();
	Dimension size = parent.getSize();
	int width = size.width - (insets.left + insets.right);
	int height = size.height - (insets.top + insets.bottom);
	Rectangle bounds = new Rectangle(insets.left, insets.top, width, height);
	layout1(getModel(), bounds); 
	layout2(getModel(), bounds); 
    }


    private MultiSplitDivider dividerAt(MultiSplitNode root, int x, int y) {
	if (root instanceof MultiSplitDivider) {
		MultiSplitDivider divider = (MultiSplitDivider)root;
	    return (divider.getBounds().contains(x, y)) ? divider : null;
	}
	else if (root instanceof MultiSplitSplitter) {
		MultiSplitSplitter split = (MultiSplitSplitter)root;
	    for(MultiSplitNode child : split.getChildren()) {
		if (child.getBounds().contains(x, y)) {
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
    public MultiSplitDivider dividerAt(int x, int y) {
	return dividerAt(getModel(), x, y);
    }

    private boolean nodeOverlapsRectangle(MultiSplitNode node, Rectangle r2) {
	Rectangle r1 = node.getBounds();
	return 
	    (r1.x <= (r2.x + r2.width)) && ((r1.x + r1.width) >= r2.x) &&
	    (r1.y <= (r2.y + r2.height)) && ((r1.y + r1.height) >= r2.y);
    }

    private List<MultiSplitDivider> dividersThatOverlap(MultiSplitNode root, Rectangle r) {
	if (nodeOverlapsRectangle(root, r) && (root instanceof MultiSplitSplitter)) {
	    List<MultiSplitDivider> dividers = new ArrayList();
	    for(MultiSplitNode child : ((MultiSplitSplitter)root).getChildren()) {
		if (child instanceof MultiSplitDivider) {
		    if (nodeOverlapsRectangle(child, r)) {
			dividers.add((MultiSplitDivider)child);
		    }
		}
		else if (child instanceof MultiSplitSplitter) {
		    dividers.addAll(dividersThatOverlap(child, r));
		}
	    }
            return dividers;
	}
	else {
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
    public List<MultiSplitDivider> dividersThatOverlap(Rectangle r) {
	if (r == null) {
	    throw new IllegalArgumentException("null Rectangle");
	}
	return dividersThatOverlap(getModel(), r);
    }

    
    

      
    /**
     * 
     */
    private static void throwParseException(StreamTokenizer st, String msg) throws Exception {
	throw new Exception("MultiSplitLayout.parseModel Error: " + msg);
    }
    /**
     * 
     */
    private static void parseAttribute(String name, StreamTokenizer st, MultiSplitNode node) throws Exception {
	if ((st.nextToken() != '=')) {
	    throwParseException(st, "expected '=' after " + name);
	}
	if (name.equalsIgnoreCase("WEIGHT")) {
	    if (st.nextToken() == StreamTokenizer.TT_NUMBER) {
		node.setWeight(st.nval);
	    }
	    else { 
		throwParseException(st, "invalid weight");
	    }
	}
	else if (name.equalsIgnoreCase("NAME")) {
	    if (st.nextToken() == StreamTokenizer.TT_WORD) {
		if (node instanceof MultiSplitLeaf) {
		    ((MultiSplitLeaf)node).setName(st.sval);
		}
		else {
		    throwParseException(st, "can't specify name for " + node);
		}
	    }
	    else {
		throwParseException(st, "invalid name");
	    }
	}
	else {
	    throwParseException(st, "unrecognized attribute \"" + name + "\"");
	}
    }
    /**
     * 
     */
    private static void addSplitChild(MultiSplitSplitter parent, MultiSplitNode child) 
    {
    	List<MultiSplitNode> children = new ArrayList(parent.getChildren());
    	
    	if (children.size() == 0) 
    	{
    		children.add(child);
    	}
    	else 
    	{
    		children.add(new MultiSplitDivider());
    		children.add(child);
    	}
    	
    	parent.setChildren(children);
    }
    /**
     * 
     */
    private static void parseLeaf(StreamTokenizer st, MultiSplitSplitter parent) throws Exception 
    {
    	MultiSplitLeaf leaf = new MultiSplitLeaf();
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
    private static void parseSplit(StreamTokenizer st, MultiSplitSplitter parent) throws Exception 
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
    				addSplitChild(parent, new MultiSplitLeaf(st.sval));
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
    				MultiSplitSplitter split = new MultiSplitSplitter();
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
    private static MultiSplitNode parseModel (Reader r) 
    {
    	StreamTokenizer st = new StreamTokenizer(r);
    	
    	try 
    	{
    		MultiSplitSplitter root = new MultiSplitSplitter();
    		parseSplit(st, root);
    		return root.getChildren().get(0);
    	}
    	catch (Exception e) 
    	{
    		HoopRoot.debug ("MultiSplitLayout",e.toString());
    	}
    	finally 
    	{
    		try { r.close(); } catch (IOException ignore) {}
    	}
    	return null;
    }

    /**
     * A convenience method that converts a string to a 
     * MultiSplitLayout model (a tree of MultiSplitNodes) using a 
     * a simple syntax.  MultiSplitNodes are represented by 
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
     * @return the MultiSplitNode root of a tree based on s.
     */
    public static MultiSplitNode parseModel(String s) 
    {
    	return parseModel(new StringReader(s));
    }
    /**
     * 
     */
    private static void printModel(String indent, MultiSplitNode root) 
    {
    	if (root instanceof MultiSplitSplitter) 
    	{
    		MultiSplitSplitter split = (MultiSplitSplitter)root;
    		
    		HoopRoot.debug("MultiSplitLayout",indent + split);
    		
    		for(MultiSplitNode child : split.getChildren()) 
    		{
    			printModel(indent + "  ", child);
    		}
    	}
    	else 
    	{
    		HoopRoot.debug("MultiSplitLayout",indent + root);
    	}
    }
    /** 
     * Print the tree with enough detail for simple debugging.
     */
    public static void printModel(MultiSplitNode root) 
    {
    	printModel("", root);
    }
}
