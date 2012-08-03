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

package edu.cmu.cs.in.hoop.hoops.base;

import edu.cmu.cs.in.base.HoopRoot;

/**
*
*/
public class HoopVisual extends HoopRoot
{
	protected int x=0;
	protected int y=0;
	protected int width=150; 
	protected int height=100;
	
	/**
	 *
	 */
    public HoopVisual () 
    {
		setClassName ("HoopVisual");
		debug ("HoopVisual ()");
						
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
}
