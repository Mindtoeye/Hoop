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

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;

import edu.cmu.cs.in.base.HoopLink;

/** 
 *
 */
public class HoopEmbeddedJPanel extends HoopLockableJPanel
{	
	private static final long serialVersionUID = 1L;
		
	/// Most of them are single instances, we'll let selected panels override this
	private Boolean singleInstance=true;
	
	private JTabbedPane host=null;
			
	private ImageIcon icon=null;
	
	/**
	 * 
	 */	
	public HoopEmbeddedJPanel()
	{
		setClassName ("HoopEmbeddedJPanel");
		debug ("HoopEmbeddedJPanel ()");
		
		icon=HoopLink.imageIcons [5];		
	}	
	/**
	 * 
	 */	
	public HoopEmbeddedJPanel(ImageIcon anIcon)
	{
		setClassName ("HoopEmbeddedJPanel");
		debug ("HoopEmbeddedJPanel (ImageIcon)");
		
		icon=anIcon;
	}
	/**
	 * 
	 */
	public ImageIcon getIcon() 
	{
		return icon;
	}
	/**
	 * 
	 */
	public void setIcon(ImageIcon icon) 
	{
		this.icon = icon;
	}	
	/**
	 * 
	 */	
	public JTabbedPane getHost() 
	{
		return host;
	}
	/**
	 * 
	 */	
	public void setHost(JTabbedPane host) 
	{
		this.host = host;
	}	
	/**
	 * 
	 */
	protected void setContentPane (Component aChild)
	{
		debug ("setContentPane ()");
		
		contentPane=aChild;
		
		view.add(aChild);
	}
	/**
	 * 
	 */
	public Component getContentPane ()
	{
		return (view);
	}
	/**
	 * 
	 */
	public void close ()
	{
		handleCloseEvent ();
		HoopLink.removeWindowInternal (this);
	}
	/**
	 * 
	 */
	public void handleCloseEvent ()
	{
		debug ("handleCloseEvent ()");
		
		// Process this in child class!!
	}
	/**
	 *
	 */	
	public void updateContents() 
	{
		//debug ("updateContents ()");
		
		// Implement in child class!!
	}
	/**
	 *
	 */	
	public Boolean getSingleInstance() 
	{
		return singleInstance;
	}
	/**
	 *
	 */	
	public void setSingleInstance(Boolean singleInstance) 
	{
		this.singleInstance = singleInstance;
	}	
}
