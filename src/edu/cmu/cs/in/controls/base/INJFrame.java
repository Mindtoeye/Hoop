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

package edu.cmu.cs.in.controls.base;

import java.awt.GraphicsConfiguration;
import javax.swing.JFrame;

import edu.cmu.cs.in.base.INBase;

/** 
 * @author vvelsen
 *
 */
public class INJFrame extends JFrame 
{

	private static final long serialVersionUID = 7391090164244209176L;
	private String className="INBase";	
	
	/**
	 * Constructs a new frame that is initially invisible.
	 */	
	public INJFrame()
	{
		setClassName ("INJFrame");
		debug ("INJFrame ()");
	}
	/**
	 * Creates a Frame in the specified GraphicsConfiguration of a screen device and a blank title.
	 */	
	public INJFrame(GraphicsConfiguration gc)
	{
		super (gc);
		setClassName ("INJFrame");
		debug ("INJFrame ()");
	}
	/**
	 * Creates a new, initially invisible Frame with the specified title.
	 */	
	public INJFrame(String title)
	{
		super (title);
		setClassName ("INJFrame");
		debug ("INJFrame ()");
	}
	/**
	 * Creates a JFrame with the specified title and the specified GraphicsConfiguration of a screen device.
	 */	
	public INJFrame(String title, GraphicsConfiguration gc)
	{
		super (title,gc);
		setClassName ("INJFrame");
		debug ("INJFrame ()");
	}  
	/**
	 *
	 */
	public void setClassName (String aName)
	{
		className=aName;
	}
	/**
	 *
	 */
	public String getClassName ()
	{
		return (className);
	}	
	/**
	 * 
	 */
	protected void debug (String aMessage)
	{
		INBase.debug(getClassName(),aMessage);
	}
}