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
public class HoopEmbeddedJPanel extends HoopLockableJPanel implements HoopViewInterface
{	
	private static final long serialVersionUID = 1L;
	
	public static int VIEW_GENERIC=0;
	public static int VIEW_TOOL=1;
	public static int VIEW_EDITOR=2;
	public static int VIEW_VISUALIZER=3;
		
	/// Most of them are single instances, we'll let selected panels override this
	private Boolean singleInstance=true;
	
	private JTabbedPane host=null;
			
	private ImageIcon icon=null;
	
	private int viewType=VIEW_GENERIC;
	
	private String windowDescription="Generic Window";
	
	protected HoopLink privateLink=null;
	
	/**
	 * 
	 */	
	public HoopEmbeddedJPanel()
	{
		setClassName ("HoopEmbeddedJPanel");
		debug ("HoopEmbeddedJPanel ()");
				
		if (privateLink!=null)
		{
			icon=privateLink.imageIcons [5];
		}	
	}	
	/**
	 * 
	 */	
	public HoopEmbeddedJPanel(HoopLink aLink)
	{
		setClassName ("HoopEmbeddedJPanel");
		debug ("HoopEmbeddedJPanel ()");
		
		privateLink=aLink;
				
		icon=privateLink.imageIcons [5];		
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
	public void setLink (HoopLink aLink)
	{
		privateLink=aLink;
	}
	/*
	 * 
	 */
	public void setDescription (String aDescription)
	{
		windowDescription=aDescription;
	}
	/**
	 * 
	 */
	public String getDescription ()
	{
		return (windowDescription);
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
		
		if (privateLink!=null)
		{			
			privateLink.removeWindowInternal (this);
		}	
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
	/**
	 * 
	 */
	public int getViewType() 
	{
		return viewType;
	}
	/**
	 * 
	 */
	public void setViewType(int viewType) 
	{
		this.viewType = viewType;
	}
	@Override
	public HoopEmbeddedJPanel getPanel() 
	{
		return (this);
	}	
}
