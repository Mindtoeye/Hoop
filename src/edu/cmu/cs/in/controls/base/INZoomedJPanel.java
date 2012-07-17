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
 
/**
 *
 */
public class INZoomedJPanel extends INJPanel implements MouseListener, MouseWheelListener
{ 
	private static final long serialVersionUID = -1L;

	private double zoomFactor = 0.5;   //<-- Change zoom factor to see effect
	private AffineTransform scaleXform,inverseXform;
 
	/**
	 * 
	 */
	public INZoomedJPanel() 
	{		
		setClassName ("INZoomedJPanel");
		debug ("INZoomedJPanel ()");		
		 
		this.setBackground(Color.white);
		this.setPreferredSize(new Dimension(200,300));
		this.addMouseListener (this);
		this.addMouseWheelListener(this);
		
		//Add a mouse Listener
		/*
		this.addMouseListener(new java.awt.event.MouseAdapter() 
		{
			public void mousePressed(java.awt.event.MouseEvent evt) 
			{
				debug ("mousePressed ()");
				
				try 
				{
					panelMousePressed(evt);
				} 
				catch (Exception e) 
				{	
					e.getMessage(); 
				}
			}//end
		});
		*/
	}
	/**
	 * 
	 */
	private void panelMousePressed (MouseEvent evt) //throws Exception 
	{
		debug ("panelMousePressed ()");
		
		Point press = evt.getPoint();
 
		debug ("X: " + press.x +" - Y: " + press.y);
 
		// Inverse the Point
		inverseXform.transform(press, press);
 
		/*Create a bound Rectangle to enclose this JLabel*/
		Rectangle enRect = new Rectangle(press.x,press.y,40,30);
 
		JLabel label = new JLabel("Label");
		label.setBounds(enRect);
 
		//Add it to the JPanel
		this.add(label);
 
		repaint();
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
	public static void main(String[] args) 
	{
        JFrame window = new JFrame ();
        
        INZoomedJPanel zoomer=new INZoomedJPanel ();
        window.setContentPane (zoomer);
        window.setPreferredSize(new Dimension (300,200));
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setTitle("JPanel Zoom Demo");
        window.setLocationRelativeTo(null);  // Center window.
        window.pack();
        
        window.setPreferredSize(new Dimension (300,200));
        
        window.setVisible(true);
    }
	/**
	 * 
	 */
	@Override
	public void mouseClicked(MouseEvent arg0) 
	{
		// TODO Auto-generated method stub
		
	}
	/**
	 * 
	 */
	@Override
	public void mouseEntered(MouseEvent arg0) 
	{
		// TODO Auto-generated method stub
		
	}
	/**
	 * 
	 */
	@Override
	public void mouseExited(MouseEvent arg0) 
	{
		// TODO Auto-generated method stub
		
	}
	/**
	 * 
	 */
	@Override
	public void mousePressed(MouseEvent evt) 
	{
		debug ("mousePressed ()");
		
		try 
		{
			panelMousePressed(evt);
		} 
		catch (Exception e) 
		{	
			e.getMessage(); 
		}
	}
	@Override
	public void mouseReleased(MouseEvent arg0) 
	{
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseWheelMoved(MouseWheelEvent evt) 
	{
		int notches = evt.getWheelRotation();
		
		changeZoom (zoomFactor+=(notches*0.01));				
	}	
}
