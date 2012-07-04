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

import java.awt.BorderLayout;
//import java.awt.Color;
import java.awt.Component;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.BorderFactory;
import javax.swing.JTabbedPane;
//import javax.swing.table.TableColumn;

//import javax.swing.BorderFactory;
//import javax.swing.BoxLayout;
//import javax.swing.border.Border;

import edu.cmu.cs.in.base.INHoopLink;
import edu.cmu.cs.in.base.INHoopProperties;

/** 
 * @author vvelsen
 *
 */
public class INEmbeddedJPanel extends INJPanel implements ComponentListener
{	
	private static final long serialVersionUID = 1L;
	
	/// Most of them are single instances, we'll let selected panels override this
	private Boolean singleInstance=true;
	
	private JTabbedPane host=null;
		
	/**
	 * Creates a new JPanel with a double buffer and a flow layout.
	 */	
	public INEmbeddedJPanel()
	{
		setClassName ("INEmbeddedJPanel");
		debug ("INEmbeddedJPanel ()");
		
		this.setBorder(BorderFactory.createEmptyBorder(INHoopProperties.tabPadding,INHoopProperties.tabPadding,INHoopProperties.tabPadding,INHoopProperties.tabPadding));		
		this.setLayout(new BorderLayout(2,2));
		this.addComponentListener(this);
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
		
		this.add(aChild);
	}
	/**
	 * 
	 */
	public void close ()
	{
		handleCloseEvent ();
		INHoopLink.removeWindowInternal (this);
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
		debug ("updateContents ()");
		
		// Implement in child class!!
	}
	/**
	 *
	 */	
	public void updateSize() 
	{
		debug ("updateSize ()");
		
		// Implement in child class !!
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
	@Override
	public void componentHidden(ComponentEvent arg0) 
	{
		debug ("componentHidden ()");
	}
	/**
	 *
	 */	
	@Override
	public void componentMoved(ComponentEvent arg0) 
	{
		debug ("componentMoved ()");		
	}
	/**
	 *
	 */	
	@Override
	public void componentResized(ComponentEvent arg0) 
	{
		debug ("componentResized ()");
		
		updateSize();		
	}
	/**
	 *
	 */	
	@Override
	public void componentShown(ComponentEvent arg0) 
	{
		debug ("componentShown ()");
		
		updateSize();
	}	
}
