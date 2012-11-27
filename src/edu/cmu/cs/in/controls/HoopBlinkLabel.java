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

package edu.cmu.cs.in.controls;

import edu.cmu.cs.in.controls.base.HoopJPanel;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * 
 */
public class HoopBlinkLabel extends HoopJPanel
{
	private static final long serialVersionUID = 808440805553414966L;
	
	private int top = 0;

	/**
	 * 
	 */
	public void start()
	{
		new Timer(500, new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				top++;
				repaint();
			}
		}).start();
	}
	/**
	 * 
	 */
	public void paintComponent( Graphics g )
	{
		super.paintComponent( g ); // JMK. call superclass's paintComponent
		
		// 'Flash'
		if (top % 2 == 0) 
		{
			return;
		}

		// JMK. declare the coordinate points for the shape on the sign

		// JMK. over, up, down diag, down back diag, up, over
		int xPoints[] = {200, 200, 220, 280, 260, 260, 200};
		int yPoints[] = {210, 230, 190, 220, 250, 230, 230};

		Graphics2D g2d = ( Graphics2D ) g;

		// JMK. make a lblack background rectangle
		g.setColor(new Color( 20,0,0));

		// JMK. draw a rectangle that will fit inside my jframe
		g.fill3DRect( 10, 0, 750, 550, true);

		// JMK. create the Sign object of type GeneralPath
		GeneralPath Sign = new GeneralPath();

		// JMK. set the initial coordinates of the Sign
		Sign.moveTo( xPoints[ 0 ], yPoints[ 0 ] );

		// JMK. create the Sign. this does not draw the Sign
		for ( int count = 1; count < xPoints.length; count++ )
			Sign.lineTo( xPoints[ count ], yPoints[ count ] );

		// JMK. close the Sign (the last up stroke )
		Sign.closePath();


		//JMK. set a color for the symbol
		g2d.setColor( new Color( 200,100,000));

		g2d.fill( Sign ); // JMK. Finally we draw the filled
		// randomly colored Sign

	}
}



