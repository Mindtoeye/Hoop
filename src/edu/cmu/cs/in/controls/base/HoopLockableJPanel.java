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

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.jdesktop.jxlayer.JXLayer;
import org.jdesktop.jxlayer.plaf.effect.BufferedImageOpEffect;
import org.jdesktop.jxlayer.plaf.ext.LockableUI;

import com.jhlabs.image.BlurFilter;

import edu.cmu.cs.in.base.HoopProperties;

/** 
 *
 */
public class HoopLockableJPanel extends HoopJPanel
{			
	private static final long serialVersionUID = 3473472183797325350L;

	protected JPanel view=null;
	
	protected Component contentPane=null;
	
	protected LockableUI blurUI = null;
	protected JXLayer<JComponent> layer=null;
		
	/**
	 * 
	 */	
	public HoopLockableJPanel()
	{
		setClassName ("HoopLockableJPanel");
		debug ("HoopLockableJPanel ()");
		
		this.setBorder(BorderFactory.createEmptyBorder(HoopProperties.tabPadding,HoopProperties.tabPadding,HoopProperties.tabPadding,HoopProperties.tabPadding));		
		this.setLayout(new BorderLayout(2,2));		
		
		blurUI = new LockableUI(new BufferedImageOpEffect(new BlurFilter()));
		
		view=createView ();
		view.setLayout(new BorderLayout(0,0));
		
		layer=new JXLayer<JComponent>(view);
		layer.setUI (blurUI);
		
		this.add(layer);		
	}	
	/**
	 * 
	 */	
	public HoopLockableJPanel(ImageIcon anIcon)
	{
		setClassName ("HoopLockableJPanel");
		debug ("HoopLockableJPanel (ImageIcon)");
		
		this.setBorder(BorderFactory.createEmptyBorder(HoopProperties.tabPadding,HoopProperties.tabPadding,HoopProperties.tabPadding,HoopProperties.tabPadding));		
		this.setLayout(new BorderLayout(2,2));		
		
		blurUI = new LockableUI(new BufferedImageOpEffect(new BlurFilter()));
		
		view=createView ();
		view.setLayout(new BorderLayout(0,0));
		
		layer=new JXLayer<JComponent>(view);
		layer.setUI (blurUI);
		
		this.add(layer);		
	}
	/**
	 * 
	 */
	protected JPanel createView ()
	{
		debug ("createView ()");
		
		return (new JPanel ());
	}	
	/**
	 * 
	 */
	public void setLocked (Boolean aVal)
	{
		debug ("setLocked ()");
		
		if (blurUI!=null)
		{
			blurUI.setLocked (aVal);
		}
	}
}
