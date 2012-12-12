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
import java.awt.Component;
//import java.awt.event.ComponentEvent;
//import java.awt.event.ComponentListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.jdesktop.jxlayer.JXLayer;
import org.jdesktop.jxlayer.plaf.effect.BufferedImageOpEffect;
import org.jdesktop.jxlayer.plaf.ext.LockableUI;

import com.jhlabs.image.BlurFilter;

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.base.HoopProperties;

/** 
 * @author vvelsen
 *
 */
public class HoopEmbeddedJPanel extends HoopJPanel
{	
	private static final long serialVersionUID = 1L;
	
	private Component contentPane=null;
	
	/// Most of them are single instances, we'll let selected panels override this
	private Boolean singleInstance=true;
	
	private JTabbedPane host=null;
	
	private JPanel view=null;
	
	private LockableUI blurUI = null;
	private JXLayer<JComponent> layer=null;
	
	private ImageIcon icon=null;
	
	/**
	 * 
	 */	
	public HoopEmbeddedJPanel()
	{
		setClassName ("HoopEmbeddedJPanel");
		debug ("HoopEmbeddedJPanel ()");
		
		icon=HoopLink.imageIcons [5];
		
		this.setBorder(BorderFactory.createEmptyBorder(HoopProperties.tabPadding,HoopProperties.tabPadding,HoopProperties.tabPadding,HoopProperties.tabPadding));		
		this.setLayout(new BorderLayout(2,2));
		//this.addComponentListener(this);

		blurUI = new LockableUI(new BufferedImageOpEffect(new BlurFilter()));
		
		view=new JPanel ();
		view.setLayout(new BorderLayout(0,0));
		
		layer=new JXLayer<JComponent>(view);
		layer.setUI (blurUI);
		
		this.add(layer);
		
		//contentPane=view;
		
		//setContentPane (layer);
	}	
	/**
	 * 
	 */	
	public HoopEmbeddedJPanel(ImageIcon anIcon)
	{
		setClassName ("HoopEmbeddedJPanel");
		debug ("HoopEmbeddedJPanel (ImageIcon)");
		
		icon=anIcon;
		
		this.setBorder(BorderFactory.createEmptyBorder(HoopProperties.tabPadding,HoopProperties.tabPadding,HoopProperties.tabPadding,HoopProperties.tabPadding));		
		this.setLayout(new BorderLayout(2,2));
		//this.addComponentListener(this);

		blurUI = new LockableUI(new BufferedImageOpEffect(new BlurFilter()));
		
		view=new JPanel ();
		view.setLayout(new BorderLayout(0,0));
		
		layer=new JXLayer<JComponent>(view);
		layer.setUI (blurUI);
		
		this.add(layer);
		
		//contentPane=view;
		
		//setContentPane (layer);
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
	/**
	 * 
	 */
	public void setLocked (Boolean aVal)
	{
		debug ("setLocked ()");
		
		if (blurUI!=null)
			blurUI.setLocked (aVal);
	}
}
