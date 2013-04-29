/** 
 *  Author: Martin van Velsen <vvelsen@cs.cmu.edu>
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

package edu.cmu.cs.in.hoop.properties;

import edu.cmu.cs.in.base.HoopRoot;

/**
* To separate any non-hoop related objects from the core hoop classes we
* created a new class that stores any visual properties and attributes of
* a hoop. This way when we run a hoop graph on a cluster we don't create
* massive amounts of objects that are not relevant. Please see the
* class HoopVisual for the place where this class is created and managed.
*/
public class HoopVisualProperties extends HoopRoot
{
	protected int x=0;
	protected int y=0;
	
	protected int width=150; 
	protected int height=100;
	
	protected int originalWidth=150; 
	protected int originalHeight=100;	
	
	private Boolean highlighted=false;
		
	public Long duration=(long) 1;
	public int durationOffset=0;
	public int durationWidth=1;
	
	public int preferredPanelHeight=-1; // figure out dynamically
	
	/**
	 *
	 */
    public HoopVisualProperties () 
    {
		setClassName ("HoopVisualProperties");
		debug ("HoopVisualProperties ()");
						
    }
	/**
	 *
	 */
	public int getX() 
	{
		return x;
	}
	/**
	 *
	 */
	public void setX(int x) 
	{
		this.x = x;
	}
	/**
	 *
	 */
	public int getY() 
	{
		return y;
	}
	/**
	 *
	 */
	public void setY(int y) 
	{
		this.y = y;
	}
	
	
	
	/**
	 *
	 */
	public int getWidth() 
	{
		return width;
	}
	/**
	 *
	 */
	public void setWidth(int width) 
	{
		this.width = width;
	}
	/**
	 *
	 */
	public int getHeight() 
	{
		return height;
	}
	/**
	 *
	 */
	public void setHeight(int height) 
	{
		this.height = height;
	}
	

	/**
	 *
	 */
	public int getOriginalWidth() 
	{
		return originalWidth;
	}
	/**
	 *
	 */
	public void setOriginalWidth(int width) 
	{
		this.originalWidth = width;
	}
	/**
	 *
	 */
	public int getOriginalHeight() 
	{
		return originalHeight;
	}
	/**
	 *
	 */
	public void setOriginalHeight(int height) 
	{
		this.originalHeight = height;
	}	
	
	
	/**
	 * 
	 * @param highlighted
	 */
	public void setHighlighted(Boolean highlighted) 
	{
		this.highlighted = highlighted;
	}
	/**
	 * 
	 * @return
	 */
	public Boolean getHighlighted() 
	{
		return highlighted;
	}    
}
