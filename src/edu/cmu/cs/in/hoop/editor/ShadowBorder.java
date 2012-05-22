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

package edu.cmu.cs.in.hoop.editor;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.io.Serializable;

import javax.swing.border.Border;

/**
 * Border with a drop shadow.
 */
public class ShadowBorder implements Border, Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6854989457150641240L;

	private Insets insets;

	public static ShadowBorder sharedInstance = new ShadowBorder();

	private ShadowBorder()
	{
		insets = new Insets(0, 0, 2, 2);
	}

	public Insets getBorderInsets(Component c)
	{
		return insets;
	}

	public boolean isBorderOpaque()
	{
		return false;
	}

	public void paintBorder(Component c, Graphics g, int x, int y, int w, int h)
	{
		// choose which colors we want to use
		Color bg = c.getBackground();

		if (c.getParent() != null)
		{
			bg = c.getParent().getBackground();
		}

		if (bg != null)
		{
			Color mid = bg.darker();
			Color edge = average(mid, bg);

			g.setColor(bg);
			g.drawLine(0, h - 2, w, h - 2);
			g.drawLine(0, h - 1, w, h - 1);
			g.drawLine(w - 2, 0, w - 2, h);
			g.drawLine(w - 1, 0, w - 1, h);

			// draw the drop-shadow
			g.setColor(mid);
			g.drawLine(1, h - 2, w - 2, h - 2);
			g.drawLine(w - 2, 1, w - 2, h - 2);

			g.setColor(edge);
			g.drawLine(2, h - 1, w - 2, h - 1);
			g.drawLine(w - 1, 2, w - 1, h - 2);
		}
	}

	private static Color average(Color c1, Color c2)
	{
		int red = c1.getRed() + (c2.getRed() - c1.getRed()) / 2;
		int green = c1.getGreen() + (c2.getGreen() - c1.getGreen()) / 2;
		int blue = c1.getBlue() + (c2.getBlue() - c1.getBlue()) / 2;
		return new Color(red, green, blue);
	}

	public static ShadowBorder getSharedInstance()
	{
		return sharedInstance;
	}
}