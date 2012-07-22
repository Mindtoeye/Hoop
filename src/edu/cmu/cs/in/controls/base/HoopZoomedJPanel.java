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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;

import javax.swing.JFrame;
import javax.swing.JLabel;

import edu.cmu.cs.in.controls.HoopJFeatureList;
 
/**
 *
 */
public class HoopZoomedJPanel extends HoopJPanel implements MouseWheelListener
{ 
	private static final long serialVersionUID = -1L;

	private double zoomFactor = 0.5;   //<-- Change zoom factor to see effect
	private AffineTransform scaleXform,inverseXform;
 
	/**
	 * 
	 */
	public HoopZoomedJPanel() 
	{		
		setClassName ("HoopZoomedJPanel");
		debug ("HoopZoomedJPanel ()");		
		 
		this.setBackground(Color.white);
		this.setPreferredSize(new Dimension(200,300));
		this.addMouseWheelListener(this);		
	}
	/**
	 * 
	 */
	public void paint(Graphics g) 
	{
		super.paintComponent(g); // clears background
		Graphics2D g2 = (Graphics2D) g;
 
		/*1) Backup current transform*/
		AffineTransform backup = g2.getTransform();
 
		/*2) Create a scale transform*/
		scaleXform = new AffineTransform(this.zoomFactor,
										 0.0, 0.0,
										 this.zoomFactor,
										 0.0, 0.0);
 
		/*3) Create the inverse of scale (used on mouse evt points)*/
		try 
		{
			inverseXform = new AffineTransform();
			inverseXform = scaleXform.createInverse();
		} 
		catch (Exception ex)
		{  
			
		}
 
		/*4) Apply transformation*/
		g2.transform(scaleXform);
   
		super.paint(g);
 
		/*After drawing do*/
		g2.setTransform(backup);
	}
	/**
	 * Change zoom factor
	 * @param newZoomFactor
	 */	
	public void changeZoom (double newZoomFactor) 
	{
		this.zoomFactor = newZoomFactor;
		
		revalidate();
		repaint();
	} 
	/**
	 * 
	 */
	public double getZoomFactor() 
	{
		return this.zoomFactor;
	}
	/**
	 * 
	 */
	public Dimension getPreferredSize() 
	{
		Dimension unzoomed= getLayout().preferredLayoutSize(this);
		Dimension zoomed= new Dimension((int) (unzoomed.width * zoomFactor),
										(int) (unzoomed.height * zoomFactor));
 
		return zoomed;
	}
	/**
	 * 
	 */
	@Override
	public void mouseWheelMoved(MouseWheelEvent evt) 
	{
		int notches = evt.getWheelRotation();
		
		changeZoom (zoomFactor+=(notches*0.01));				
	}		
	/**
	 * 
	 */
	public static void main(String[] args) 
	{
        JFrame window = new JFrame ();
        
        HoopZoomedJPanel zoomer=new HoopZoomedJPanel ();
        window.setContentPane (zoomer);
        window.setPreferredSize(new Dimension (300,200));
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setTitle("JPanel Zoom Demo");
        window.setLocationRelativeTo(null);  // Center window.
        window.pack();
        
        window.setPreferredSize(new Dimension (300,200));
        
        window.setVisible(true);
    }
}
