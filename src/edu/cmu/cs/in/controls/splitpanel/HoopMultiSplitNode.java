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

package edu.cmu.cs.in.controls.splitpanel;

import java.awt.Rectangle;
import java.util.List;

import edu.cmu.cs.in.base.HoopRoot;

/** 
 * Base class for the nodes that model a MultiSplitLayout.
 */
public class HoopMultiSplitNode extends HoopRoot
{	         	
	private HoopMultiSplitSplitter parent = null;  
    private Rectangle bounds = new Rectangle();
    private double weight = 0.0;

    public HoopMultiSplitNode ()
    {
    	setClassName ("HoopMultiSplitNode");
    	debug ("HoopMultiSplitNode ()");
    }    
    /** 
	 * Returns the Split parent of this HoopMultiSplitNode, or null.
	 *
	 * @return the value of the parent property.
	 * @see #setParent
	 */
	public HoopMultiSplitSplitter getParent() 
	{ 
		return parent; 
	}
	/**
	 * Set the value of this HoopMultiSplitNode's parent property.  The default
	 * value of this property is null.
	 * 
	 * @param parent a Split or null
	 * @see #getParent
	 */
	public void setParent(HoopMultiSplitSplitter parent) 
	{
	    this.parent = parent;
	}	
	/**
	 * Returns the bounding Rectangle for this HoopMultiSplitNode.
	 * 
	 * @return the value of the bounds property.
	 * @see #setBounds
	 */
	public Rectangle getBounds() 
	{ 
	    return new Rectangle(this.bounds);
	}
	/**
	 * Set the bounding Rectangle for this node.  The value of 
	 * bounds may not be null.  The default value of bounds
	 * is equal to <code>new Rectangle(0,0,0,0)</code>.
	 * 
	 * @param bounds the new value of the bounds property
	 * @throws IllegalArgumentException if bounds is null
	 * @see #getBounds
	 */
	public void setBounds(Rectangle bounds) 
	{
	    if (bounds == null) 
	    {
	    	throw new IllegalArgumentException("null bounds");
	    }
	    
	    this.bounds = new Rectangle(bounds);
	}
	/** 
	 * Value between 0.0 and 1.0 used to compute how much space
	 * to add to this sibling when the layout grows or how
	 * much to reduce when the layout shrinks.
	 * 
	 * @return the value of the weight property
	 * @see #setWeight
	 */
	public double getWeight() 
	{ 
		return weight; 
	}
	/** 
	 * The weight property is a between 0.0 and 1.0 used to
	 * compute how much space to add to this sibling when the
	 * layout grows or how much to reduce when the layout shrinks.
	 * If rowLayout is true then this node's width grows
	 * or shrinks by (extraSpace * weight).  If rowLayout is false,
	 * then the node's height is changed.  The default value
	 * of weight is 0.0.
	 * 
	 * @param weight a double between 0.0 and 1.0
	 * @see #getWeight
	 * @see MultiSplitLayout#layoutContainer
	 * @throws IllegalArgumentException if weight is not between 0.0 and 1.0
	 */
	public void setWeight(double weight) 
	{
		this.weight=0.0;
		
		/*
	    if ((weight < 0.0)|| (weight > 1.0)) 
	    {
	    	throw new IllegalArgumentException("invalid weight");
	    }
	    
	    this.weight = weight;
	    */
	}
	/**
	 * 
	 */
	public HoopMultiSplitNode siblingAtOffset(int offset) 
	{
		HoopMultiSplitSplitter parent = getParent();
		
	    if (parent == null) 
	    {
	    	return null; 
	    }
	    
	    List<HoopMultiSplitNode> siblings = parent.getChildren();
	    int index = siblings.indexOf(this);
	    
	    if (index == -1) 
	    {
	    	return null; 
	    }
	    index += offset;
	    
	    return ((index > -1) && (index < siblings.size())) ? siblings.get(index) : null;
	}	   
	/** 
	 * Return the HoopMultiSplitNode that comes after this one in the parent's
	 * list of children, or null.  If this node's parent is null,
	 * or if it's the last child, then return null.
	 * 
	 * @return the HoopMultiSplitNode that comes after this one in the parent's list of children.
	 * @see #previousSibling
	 * @see #getParent
	 */
	public HoopMultiSplitNode nextSibling() 
	{
	    return siblingAtOffset(+1);
	}
	/** 
	 * Return the HoopMultiSplitNode that comes before this one in the parent's
	 * list of children, or null.  If this node's parent is null,
	 * or if it's the last child, then return null.
	 * 
	 * @return the HoopMultiSplitNode that comes before this one in the parent's list of children.
	 * @see #nextSibling
	 * @see #getParent
	 */
	public HoopMultiSplitNode previousSibling() 
	{
	    return siblingAtOffset(-1);
	}
}
