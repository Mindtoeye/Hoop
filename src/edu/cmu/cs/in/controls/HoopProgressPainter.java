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

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;

import edu.cmu.cs.in.controls.base.HoopJPanel;

/**
 * 
 */
public class HoopProgressPainter extends HoopJPanel 
{
	private static final long serialVersionUID = 8372398146730004932L;
	
	private long currentLevel=1;
	private long maxLevel=1;
	
	/**
	 * Creates a new JPanel with a double buffer and a flow layout.
	 */	
	public HoopProgressPainter()
	{
		setClassName ("HoopProgressPainter");
		debug ("HoopProgressPainter ()");			 			
	}
	/**
	 * 
	 */
	public void setLevels (long aCurrent,long aMax)
	{
		currentLevel=aCurrent;
		maxLevel=aMax;
		
		if (currentLevel==0) // Otherwise divide by zero
			currentLevel=1;	 
	}
	/**
	 * 
	 */
	@Override
	protected void paintComponent(Graphics g)
	{
		Graphics2D g2 = (Graphics2D)g.create();

		Paint p = new GradientPaint (0,
									 4, 
									 new Color(180,180,180),
									 0,
									 getHeight ()-8,
									 new Color(220,220,220),
									 true);

		int width=(getWidth ()-4);
				
		double divver=(maxLevel/currentLevel);
		
		double pWidth=(width/divver);
		
		int paintWidth=(int) (pWidth);
		
		//debug ("currentLevel: " + currentLevel + ", maxLevel: " + maxLevel + ", divver: " + divver + ", pWidth: " + pWidth + ", paintWidth: " + paintWidth);
		
		g2.setPaint (p);
		g2.fillRect (0,2,paintWidth,getHeight()-4);
		g2.dispose ();
	}
}
