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

import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

import edu.cmu.cs.in.base.HoopRoot;

/**
 * 
 */
public class HoopComponentSnapshot extends HoopRoot 
{
	/**
	 * 
	 */
	public HoopComponentSnapshot ()
	{
		setClassName ("HoopRoot");
		debug ("HoopComponentSnapshot ()");
	}	
	/**
	 * 
	 */	
	public static BufferedImage getScreenShot (Component component) 
	{
		HoopRoot.debug("HoopComponentSnapshot","getScreenShot (Component component)");
		
		BufferedImage image = new BufferedImage(component.getWidth(),component.getHeight(),BufferedImage.TYPE_INT_RGB);
		// call the Component's paint method, using
		// the Graphics object of the image.
		component.paint (image.getGraphics());
    
		return image;
	}
	/**
	 * 
	 * @param component
	 * @param aFile
	 * @return
	 */
	public static BufferedImage saveScreenShot (Component component,String aFile) 
	{
		HoopRoot.debug("HoopComponentSnapshot","saveScreenShot (Component component,String aFile) ");
		
		BufferedImage image = new BufferedImage(component.getWidth(),component.getHeight(),BufferedImage.TYPE_INT_RGB);
	
		// call the Component's paint method, using the Graphics object of the image.
		component.paint (image.getGraphics());
    
		try
		{
			HoopRoot.debug("HoopComponentSnapshot","Saving "+ aFile +" ...");
			
			// write the image as a PNG
			ImageIO.write (image,"png", new File(aFile));
		} 
		catch(Exception e) 
		{
			e.printStackTrace();
		}
    
		return image;
	}  
} 
