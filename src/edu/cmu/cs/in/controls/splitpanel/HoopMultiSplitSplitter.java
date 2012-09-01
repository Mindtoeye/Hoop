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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** 
 * Defines a vertical or horizontal subdivision into two or more
 * tiles.
 */
public class HoopMultiSplitSplitter extends HoopMultiSplitNode 
{
	private List<HoopMultiSplitNode> children = Collections.emptyList();
	private boolean rowLayout = true;

	/**
	 * 
	 */
	public HoopMultiSplitSplitter ()
	{
    	setClassName ("HoopMultiSplitSplitter");
    	debug ("HoopMultiSplitSplitter ()");		
	}	
	/**
	 * Returns true if the this Split's children are to be 
	 * laid out in a row: all the same height, left edge
	 * equal to the previous MultiSplitNode's right edge.  If false,
	 * children are laid on in a column.
	 * 
	 * @return the value of the rowLayout property.
	 * @see #setRowLayout
	 */
	public boolean isRowLayout() 
	{ 
		return rowLayout; 
	}
	/**
	 * Set the rowLayout property.  If true, all of this Split's
	 * children are to be laid out in a row: all the same height,
	 * each node's left edge equal to the previous MultiSplitNode's right
	 * edge.  If false, children are laid on in a column.  Default
	 * value is true.
	 * 
	 * @param rowLayout true for horizontal row layout, false for column
	 * @see #isRowLayout
	 */
	public void setRowLayout(boolean rowLayout) 
	{
	    this.rowLayout = rowLayout;
	}
	/** 
	 * Returns this Split node's children.  The returned value
	 * is not a reference to the Split's internal list of children
	 * 
	 * @return the value of the children property.
	 * @see #setChildren
	 */
	public List<HoopMultiSplitNode> getChildren() 
	{ 
	    return new ArrayList<HoopMultiSplitNode>(children);
	}
	/**
	 * Set's the children property of this Split node.  The parent
	 * of each new child is set to this Split node, and the parent
	 * of each old child (if any) is set to null.  This method
	 * defensively copies the incoming List.  Default value is
	 * an empty List.
	 * 
	 * @param children List of children
	 * @see #getChildren
	 * @throws IllegalArgumentException if children is null
	 */
	public void setChildren(List<HoopMultiSplitNode> children) 
	{
	    if (children == null) 
	    {
	    	throw new IllegalArgumentException("children must be a non-null List");
	    }
	    
	    for(HoopMultiSplitNode child : this.children) 
	    {
	    	child.setParent(null);
	    }
	    
	    this.children = new ArrayList<HoopMultiSplitNode>(children);
	    
	    for(HoopMultiSplitNode child : this.children) 
	    {
	    	child.setParent(this);
	    }
	}
	/**
	 * Convenience method that returns the last child whose weight
	 * is > 0.0.
	 * 
	 * @return the last child whose weight is > 0.0.
	 * @see #getChildren
	 * @see MultiSplitNode#getWeight
	 */
	public final HoopMultiSplitNode lastWeightedChild() 
	{
	    List<HoopMultiSplitNode> children = getChildren();
	    HoopMultiSplitNode weightedChild = null;
	    for(HoopMultiSplitNode child : children) {
		if (child.getWeight() > 0.0) {
		    weightedChild = child;
		}
	    }
	    return weightedChild;
	}
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() 
	{
	    int nChildren = getChildren().size();
	    StringBuffer sb = new StringBuffer("MultiSplitSplitter");
	    sb.append(isRowLayout() ? " ROW [" : " COLUMN [");
	    sb.append(nChildren + ((nChildren == 1) ? " child" : " children"));
	    sb.append("] ");
	    sb.append(getBounds());
	    return sb.toString();
	}
}
   