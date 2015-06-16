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
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.ImageObserver;

import javax.swing.BorderFactory;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxCellState;

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.base.HoopProperties;
import edu.cmu.cs.in.base.HoopRoot;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;

/**
 * 
 */
public class HoopNodePanelRenderer extends HoopRoot implements ImageObserver
{
	//protected CellRendererPane rendererPane = new CellRendererPane();
	
	protected Border blackborder=null;
	protected Border redborder=null;	
	protected Border raisedRed=null;	
	
	private Border borderPainter=null;	
	private mxGraphComponent graphComponent=null;
	private Paint gradient=null;
	
	/**
	 * 
	 */
	public HoopNodePanelRenderer (mxGraphComponent aComponent)
	{
		setClassName ("HoopNodePanelRenderer");
		debug ("HoopNodePanelRenderer ()");
		
	    //GradientPaint redtowhite = new GradientPaint(5, 5, Color.red, 200, 5, Color.blue);
	    gradient = new GradientPaint(0, 0, 
	    							new Color(0xf3e2c7),
	    							0, 50, 
	    							new Color(0xc19e67),
	    							false);
				
		graphComponent=aComponent;
		
		borderPainter=BorderFactory.createBevelBorder(BevelBorder.RAISED);		
	}
	/**
	* Ports are not used as terminals for edges, they are
	* only used to compute the graphical connection point
	*/
	public boolean isPort(Object cell)
	{
		mxGeometry geo =graphComponent.getGraph().getCellGeometry(cell);
		
		return (geo != null) ? geo.isRelative() : false;
	}	
	/**
	 * 
	 * @param state
	 * @param label
	 */
	public void drawHoop (Graphics2D g, mxCellState state)
	{		 				
		mxCell cell=(mxCell) state.getCell();
		
		if (isPort (cell))
		{
		    g.setColor(new Color (217, 217, 217));
		    g.fillOval((int) state.getX(),
					   (int) state.getY(),
					   (int) state.getWidth(),
					   (int) state.getHeight());
		    
		    g.setColor(new Color (100, 100, 100));
		    g.drawOval((int) state.getX(),
					   (int) state.getY(),
					   (int) state.getWidth(),
					   (int) state.getHeight()); 
		}
		else
		{			
			//HoopBase hoopTest=(HoopBase) cell.getValue();			
			// debug ("Check: " + hoopTest.getClass().getName());
			
			/*
			g.setColor(HoopProperties.getHoopColor (null));
			g.fillRect((int) state.getX(),
					 (int) state.getY(),
					 (int) state.getWidth(),
					 (int) state.getHeight());
			*/
			
		    gradient = new GradientPaint(0, 0, 
										 new Color(0xf3e2c7),
										 0, (int) state.getHeight(), 
										 new Color(0xc19e67),
										 false);
			
		    g.setPaint(gradient);
		    g.fill(new RoundRectangle2D.Double((int) state.getX(),
					 							(int) state.getY(),
					 							(int) state.getWidth(),
					 							(int) state.getHeight(), 10, 10));			
			
			
			if (borderPainter!=null)
			{
				borderPainter.paintBorder(graphComponent, 
										  g,
										  (int) state.getX(),
										  (int) state.getY(),
										  (int) state.getWidth(),
										  (int) state.getHeight());
			}
			else
			{
				g.draw3DRect((int) state.getX(),
							 (int) state.getY(),
							 (int) state.getWidth(),
							 (int) state.getHeight(),
							 true);
			}			 
			
			// Title area
			
			g.drawImage(HoopLink.getImageByName("led-yellow.png").getImage(),
						(int) state.getX()+4,
						(int) state.getY()+4,
						this);
			
			g.setColor(Color.black);
			/*
			g.drawString(hoopTest.getClassName(),
						 (int) state.getX()+HoopLink.getImageByName("led-yellow.png").getIconWidth()+6,
						 (int) state.getY()+16);
			*/
			
			g.drawString("HoopBase",
					 	 (int) state.getX()+HoopLink.getImageByName("led-yellow.png").getIconWidth()+6,
					 	 (int) state.getY()+16);			
			
			g.drawImage(HoopLink.getImageByName("zoom.png").getImage(),
						(int) state.getX()+(int) state.getWidth()-(16*3)-4,
						(int) state.getY()+4,
						this);
			
			g.drawImage(HoopLink.getImageByName("gtk-execute.png").getImage(),
						(int) state.getX()+(int) state.getWidth()-(16*2)-4,
						(int) state.getY()+4,
						this);
			
			g.drawImage(HoopLink.getImageByName("help_icon.png").getImage(),
						(int) state.getX()+(int) state.getWidth()-(16*1)-4,
						(int) state.getY()+4,
						this);			
			
			// Line markers
			
			g.setColor(Color.black);
			g.drawLine((int) state.getX()+4,
					   (int) state.getY()+24,
					   (int) state.getX()+(int) state.getWidth()-4,
					   (int) state.getY()+24);
			
			g.setColor(Color.black);
			g.drawLine((int) state.getX()+4,
					   (int) state.getY()+(int) state.getHeight()-24,
					   (int) state.getX()+(int) state.getWidth()-4,
					   (int) state.getY()+(int) state.getHeight()-24);
						
			// Footer area
			
			g.setColor(Color.black);
			g.drawString("Ex: 0, E: 1",
						 (int) state.getX()+3,
						 (int) state.getY()+(int) state.getHeight()-4);
			
			// Gripper

			g.drawImage(HoopLink.getImageByName("resize.png").getImage(),
						(int) state.getX()+(int) state.getWidth()-HoopLink.getImageByName("resize.png").getIconWidth()-2,
						(int) state.getY()+(int) state.getHeight()-HoopLink.getImageByName("resize.png").getIconHeight()-2,
						this);			
		}
	}
	/**
	 * No idea why we need this but the g.drawImage needs a pointer to some such interface
	 */
	@Override
	public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) 
	{	
		return false;
	}	
}
