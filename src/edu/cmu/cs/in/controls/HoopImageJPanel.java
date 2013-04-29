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

import java.awt.Graphics;
import javax.swing.ImageIcon;

import edu.cmu.cs.in.controls.base.HoopJPanel;

public class HoopImageJPanel extends HoopJPanel
{
	private static final long serialVersionUID = 7264731538496886694L;
	private ImageIcon image=null;

    public HoopImageJPanel(ImageIcon anImage) 
    {
		setClassName ("HoopImageJPanel");
		debug ("HoopImageJPanel ()");
		
    	image=anImage;
    	
    	/*
       	try 
       	{                
          image = ImageIO.read(new File("image name and path"));
       	} 
       	catch (IOException ex) 
       	{
            // handle exception...
       	}
    	*/
    }

    @Override
    public void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        //g.drawImage(image, 0, 0, null); // see javadoc for more info on the parameters
        image.paintIcon(this,g,0,0);
    }

}