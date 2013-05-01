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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.TexturePaint;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JViewport;

import edu.cmu.cs.in.base.HoopProperties;

/**
 *
 */
public class HoopJViewport extends JViewport  
{  
	private static final long serialVersionUID = -1L;
	private Paint texture=null;  
   
	/**
	 * 
	 */
    public HoopJViewport()  
    {  
    	setBackground(HoopProperties.graphBackgroundColor);
    	
    	//setOpaque (true);
    	
        BufferedImage image = loadImage("/assets/images/checkbox.jpg");  
        int w = image.getWidth()/2;  
        int h = image.getHeight()/2;
        Rectangle2D r = new Rectangle2D.Double(0, 0, w, h);  
        texture = new TexturePaint(image, r);  
    }  
    /**
     * 
     */
    /*
    public boolean isOpaque ()
    {
    	return (true);
    }
    */
    /**
     * 
     */
    public void paint(Graphics g)  
    {      	
    	//super.paint (g);
    	
        Graphics2D g2 = (Graphics2D)g;  
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);  
        int w = getWidth();  
        int h = getHeight();  
        g2.setPaint (texture);  
        g2.fillRect(0, 0, w, h);        
    }      
    /**
    * 
    */
    private BufferedImage loadImage(String aPath)  
    {  
        BufferedImage image = null;  
        //String fileName = "images/cougar.jpg";  
        try  
        {  
            URL url = getClass().getResource(aPath);  
            image = ImageIO.read(url);  
        }  
        catch(MalformedURLException mue)  
        {  
            System.out.println("url: " + mue.getMessage());  
        }  
        catch(IOException ioe)  
        {  
            System.out.println("read: " + ioe.getMessage());  
        }  
        return image;  
    }  
    /** 
     * @param icon
     * @param targetComponent
     * @return
     */
    public BufferedImage iconToImage (ImageIcon icon, Component targetComponent)
    {
    	int w = icon.getIconWidth();
    	int h = icon.getIconHeight();
    	GraphicsConfiguration gc = targetComponent.getGraphicsConfiguration();
    	BufferedImage image = gc.createCompatibleImage(w,h);
    	Graphics2D g = image.createGraphics();
    	icon.paintIcon(targetComponent,g,0,0);
    	g.dispose();
    	return image;
    }    
}  
