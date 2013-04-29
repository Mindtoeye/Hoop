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

import javax.swing.ImageIcon;
import javax.swing.JComponent;

import edu.cmu.cs.in.base.HoopRoot;

/** 
 * @author vvelsen
 *
 */
public class HoopJComponent extends JComponent 
{
	private static final long serialVersionUID = -6522707440476438254L;
	private String className="HoopJComponent";	
	private String instanceName="Undefined";
	
    protected ImageIcon defaultIcon=null;	
	
	/**
	 * Creates a new HoopJComponent
	 */	
	public HoopJComponent()
	{
		setClassName ("HoopJComponent");
		debug ("HoopJComponent ()");
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
	public String getInstanceName() 
	{
		return instanceName;
	}
	/**
	 *
	 */	
	public void setInstanceName(String instanceName) 
	{
		this.instanceName = instanceName;
	}		
	/**
	 * 
	 */
	protected void debug (String aMessage)
	{
		HoopRoot.debug(getClassName(),aMessage);
	}	
	/**
	 * 
	 */
	protected void alert (String aMessage)
	{
		HoopRoot.alert(aMessage);
	}	
}
