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
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.BorderFactory;
import javax.swing.JTabbedPane;

/*
import org.jdesktop.jxlayer.JXLayer;
import org.jdesktop.jxlayer.plaf.effect.BufferedImageOpEffect;
import org.jdesktop.jxlayer.plaf.ext.LockableUI;
*/

//import com.jhlabs.image.BlurFilter;

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.base.HoopProperties;

/** 
 * @author vvelsen
 *
 */
public class HoopEmbeddedJPanel extends HoopJPanel
{	
	private static final long serialVersionUID = 1L;
	
	/// Most of them are single instances, we'll let selected panels override this
	private Boolean singleInstance=true;
	
	private JTabbedPane host=null;
	
	/*
	private LockableUI blurUI = new LockableUI(new BufferedImageOpEffect(new BlurFilter()));
	private JXLayer<JComponent> layer=null;
	*/
	
	/**
	 * Creates a new JPanel with a double buffer and a flow layout.
	 */	
	public HoopEmbeddedJPanel()
	{
		setClassName ("HoopEmbeddedJPanel");
		debug ("HoopEmbeddedJPanel ()");
		
		this.setBorder(BorderFactory.createEmptyBorder(HoopProperties.tabPadding,HoopProperties.tabPadding,HoopProperties.tabPadding,HoopProperties.tabPadding));		
		this.setLayout(new BorderLayout(2,2));
		//this.addComponentListener(this);

		//layer=new JXLayer<JComponent>(this);
		//layer.setUI (blurUI);	        								
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
		debug ("updateContents ()");
		
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
		
		/*
		if (blurUI!=null)
			blurUI.setLocked (aVal);
		*/
	}
}
