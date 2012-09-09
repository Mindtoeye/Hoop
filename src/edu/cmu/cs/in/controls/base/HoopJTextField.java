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

import javax.swing.JTextField;
import javax.swing.text.Document;

import edu.cmu.cs.in.base.HoopRoot;

/** 
 *
 */
public class HoopJTextField extends JTextField
{	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4378616722684789803L;
	private String className="HoopBase";	
	private String instanceName="Undefined";
	
	/**
	 * Constructs a new TextField.
	 */
	public HoopJTextField()
	{
		setClassName ("HoopJTextField");
		debug ("HoopJTextField ()");
	}	
	/**
	 * Constructs a new JTextField that uses the given text storage model and the given number of columns.
	 */	
	public HoopJTextField(Document doc, String text, int columns)
	{
		super (doc,text,columns);
		
		setClassName ("HoopJTextField");
		debug ("HoopJTextField ()");
	}
	/**
	 * Constructs a new empty TextField with the specified number of columns.
	 */	
	public HoopJTextField(int columns)
	{
		super (columns);
		
		setClassName ("HoopJTextField");
		debug ("HoopJTextField ()");
	}
	/**
	 * Constructs a new TextField initialized with the specified text.
	 */	
	public HoopJTextField(String text)
	{
		super (text);
		
		setClassName ("HoopJTextField");
		debug ("HoopJTextField ()");
	}
	/**
	 * Constructs a new TextField initialized with the specified text and columns.
	 */	
	public HoopJTextField(String text, int columns)
	{
		super (text,columns);
		
		setClassName ("HoopJTextField");
		debug ("HoopJTextField ()");
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
}
