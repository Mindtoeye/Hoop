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
 * Models a java.awt Component child.
 */
public class HoopMultiSplitLeaf extends HoopMultiSplitNode 
{
	private String name = "";

	/**
	 * Create a Leaf node.  The default value of name is "". 
	 */
	public HoopMultiSplitLeaf () 
	{ 
    	setClassName ("HoopMultiSplitLeaf");
    	debug ("HoopMultiSplitLeaf ()");			
	}

	/**
	 * Create a Leaf node with the specified name.  Name can not
	 * be null.
	 * 
	 * @param name value of the Leaf's name property
	 * @throws IllegalArgumentException if name is null
	 */
	public HoopMultiSplitLeaf(String name) 
	{
	    if (name == null) 
	    {
	    	throw new IllegalArgumentException("name is null");
	    }
	    this.name = name;
	}

	/**
	 * Return the Leaf's name.
	 * 
	 * @return the value of the name property.
	 * @see #setName
	 */
	public String getName() 
	{ 
		return name; 
	}
	/**
	 * Set the value of the name property.  Name may not be null.
	 * 
	 * @param name value of the name property
	 * @throws IllegalArgumentException if name is null
	 */
	public void setName(String name) 
	{
	    if (name == null) 
	    {
	    	throw new IllegalArgumentException("name is null");
	    }
	    this.name = name;
	}
    /**
     * 
     */
	public String toString() 
	{
	    StringBuffer sb = new StringBuffer("HoopMultiSplitLeaf");
	    sb.append(" \"");
	    sb.append(getName());
	    sb.append("\""); 
	    sb.append(" weight=");
	    sb.append(getWeight());
	    sb.append(" ");
	    sb.append(getBounds());
	    return sb.toString();
	}
}
    