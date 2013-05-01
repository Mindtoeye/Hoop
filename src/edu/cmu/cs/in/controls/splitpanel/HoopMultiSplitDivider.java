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
    
/** 
 * Models a single vertical/horiztonal divider.
 */
public class HoopMultiSplitDivider extends HoopMultiSplitNode 
{
	/**
	 * 
	 */
	public HoopMultiSplitDivider ()
	{
    	setClassName ("HoopMultiSplitDivider");
    	debug ("HoopMultiSplitDivider ()");		
	}	
	/**
	 * Convenience method, returns true if the Divider's parent
	 * is a Split row (a Split with isRowLayout() true), false
	 * otherwise. In other words if this Divider's major axis
	 * is vertical, return true.
	 * 
	 * @return true if this Divider is part of a Split row.
	 */
	public final boolean isVertical() 
	{
		HoopMultiSplitSplitter parent = getParent();
	    return (parent != null) ? parent.isRowLayout() : false;
	}
	/** 
	 * Dividers can't have a weight, they don't grow or shrink.
	 * @throws UnsupportedOperationException
	 */
	public void setWeight(double weight) 
	{
	    throw new UnsupportedOperationException();
	}
    /**
     * 
     */
	public String toString() 
	{
	    return "HoopMultiSplitDivider " + getBounds().toString();
	}
}
