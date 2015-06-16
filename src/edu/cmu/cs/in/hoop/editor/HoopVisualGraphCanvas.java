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
import java.awt.Image;
import java.awt.image.ImageObserver;

import javax.swing.BorderFactory;
import javax.swing.CellRendererPane;
import javax.swing.JLabel;
import javax.swing.border.BevelBorder;

import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.view.mxInteractiveCanvas;
import com.mxgraph.view.mxCellState;

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.base.HoopProperties;
import edu.cmu.cs.in.base.HoopRoot;

/** 
 * This class needs a bit of explanation as does the HoopVisualGraphComponent
 * class and HoopVisualGraph. These three classes manage both the visual
 * appearance as well as the graph management in JGraph. What might be confusing
 * is the fact that we have graph node rendering in both HoopVisualGraphCanvas
 * as well as HoopVisualGraphComponent.
 */
public class HoopVisualGraphCanvas extends mxInteractiveCanvas implements ImageObserver
{
	protected CellRendererPane rendererPane = new CellRendererPane();
	protected JLabel vertexRenderer = new JLabel();
	protected mxGraphComponent graphComponent;

	/** 
	 * @param graphComponent
	 */
	public HoopVisualGraphCanvas (mxGraphComponent graphComponent)
	{
		debug ("HoopVisualGraphCanvas ()");
		
		this.graphComponent = graphComponent;

		vertexRenderer.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		vertexRenderer.setHorizontalAlignment(JLabel.CENTER);
		vertexRenderer.setBackground(graphComponent.getBackground().darker());
		vertexRenderer.setOpaque(true);
	}
	/**
	 * 
	 */
	private void debug (String aMessage)
	{
		HoopRoot.debug ("HoopVisualGraphCanvas",aMessage);
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
	 * No idea why we need this but the g.drawImage needs a pointer to some such interface
	 */
	@Override
	public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) 
	{	
		return false;
	}	
	/** 
	 * @param state
	 * @param label
	 */
	public void drawVertex (mxCellState state, String label)
	{
		drawCustomVertex (state,label);

		/*
		vertexRenderer.setText(label);
	
		rendererPane.paintComponent (g, 
									 vertexRenderer, 
									 graphComponent,
									 (int) state.getX() + translate.x, 
									 (int) state.getY()	+ translate.y, 
									 (int) state.getWidth(),
									 (int) state.getHeight(), 
									 true);
		*/
	}	
	/**
	 * 
	 * @param state
	 * @param label
	 */
	public void drawCustomVertex (mxCellState state, String label)
	{
		boolean raised = true;
		 				
		if (isPort (state.getCell()))
		{
		    g.setColor(new Color (255, 255, 204));
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
			g.setColor(HoopProperties.getHoopColor (null));
			g.fillRect((int) state.getX(),
					 (int) state.getY(),
					 (int) state.getWidth(),
					 (int) state.getHeight());
					 
			g.draw3DRect((int) state.getX(),
						 (int) state.getY(),
						 (int) state.getWidth(),
						 (int) state.getHeight(),
						 raised);
			
			// Title area
			
			g.drawImage(HoopLink.getImageByName("led-yellow.png").getImage(),
						(int) state.getX()+4,
						(int) state.getY()+4,
						this);
			
			g.setColor(Color.black);
			g.drawString("HoopBase",
						 (int) state.getX()+HoopLink.getImageByName("led-yellow.png").getIconWidth()+6,
						 (int) state.getY()+16);
			
			// Gripper

			g.drawImage(HoopLink.getImageByName("resize.png").getImage(),
						(int) state.getX()+(int) state.getWidth()-HoopLink.getImageByName("resize.png").getIconWidth(),
						(int) state.getY()+(int) state.getHeight()-HoopLink.getImageByName("resize.png").getIconHeight(),
						this);
		}
	}
}
