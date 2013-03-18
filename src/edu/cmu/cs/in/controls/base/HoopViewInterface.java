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

/** 
 *
 */
public interface HoopViewInterface
{	
	/**
	 * 
	 */
	public HoopEmbeddedJPanel getPanel ();
	
	/*
	 * 
	 */
	public void setDescription (String aDescription);
	
	/**
	 * 
	 */
	public String getDescription ();
	
	/**
	 * 
	 */
	public ImageIcon getIcon();
	
	/**
	 * 
	 */
	public void setIcon(ImageIcon icon); 
	
	/**
	 * 
	 */	
	public JTabbedPane getHost();

	/**
	 * 
	 */	
	public void setHost(JTabbedPane host);
	
	/**
	 * 
	 */
	public Component getContentPane ();

	/**
	 * 
	 */
	public void handleCloseEvent ();

	/**
	 *
	 */	
	public void updateContents();

	/**
	 * 
	 */
	public int getViewType();

	/**
	 * 
	 */
	public void setViewType(int viewType);
	
}
