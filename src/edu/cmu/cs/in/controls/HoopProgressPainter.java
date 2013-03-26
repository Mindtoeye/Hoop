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

import edu.cmu.cs.in.base.HoopProperties;
import edu.cmu.cs.in.controls.base.HoopJPanel;

/**
 * 
 */
public class HoopProgressPainter extends HoopJPanel 
{
	private static final long serialVersionUID = 8372398146730004932L;
		
	public static int VIZ_FLAT=0;
	public static int VIZ_GRADIENT=1;
	
	public static int BAR_MANUAL=0;
	public static int BAR_AUTO=1;
	
	private int progressVisualization=VIZ_FLAT;
	private int progressBarType=BAR_MANUAL;
	
	private int currentLevel=1;
	private int maxLevel=1;	
	private int currentPixelPosition=0;
	private float divver=(float) 1.0;
	
	private Boolean hoopEnabled=false;
	
	private Boolean painting=false;
	
	private Graphics2D g2=null;
	
	/**
	 * 
	 */	
	public HoopProgressPainter()
	{
		setClassName ("HoopProgressPainter");
		debug ("HoopProgressPainter ()");			 			
	}
	/**
	 * 
	 */	
	public Boolean getHoopEnabled() 
	{
		return hoopEnabled;
	}
	/**
	 * 
	 */	
	public void setHoopEnabled(Boolean hoopEnabled) 
	{
		this.hoopEnabled = hoopEnabled;
	}	
	/**
	 * This is our only access method to configure the values used by
	 * the painter. Please make sure you drive this properly from 
	 * other objects such as cell renderers. A cell renderer is 
	 * essentially a singleton instance, which means that for
	 * example per list entry there isn't an associated renderer.
	 * There is only one per listbox. So if you have a progress
	 * painter you will have to make sure you know if you have
	 * multiple copies or just one.
	 */
	public void setLevels (int aCurrent,int aMax)
	{
		debug ("setLevels ("+aCurrent+","+aMax+","+this.getWidth()+")");
		
		currentLevel=aCurrent;
		maxLevel=aMax;
		
		if (maxLevel<1)
			maxLevel=1;
		
		if (currentLevel<1) // Otherwise divide by zero
			currentLevel=1;
		
		divver=(float) this.getWidth()/maxLevel;
		
		currentPixelPosition=(int) (divver*((float) currentLevel));
		
		this.repaint();
	}	
	/**
	 * 
	 */
	@Override
	protected void paintComponent(Graphics g)
	{		
		debug ("paintComponent ("+currentLevel+","+maxLevel+")");
		
		if (painting==true)
			return;
		
		painting=true;
		
		g2=(Graphics2D)g.create();
		
		if (progressBarType==BAR_MANUAL)
			drawBarManual (g);
								
		if (progressBarType==BAR_AUTO)
			drawBarAuto (g);
		
		g2.dispose ();
		
		painting=false;
	}
	/**
	 * 
	 * @param g
	 */
	protected void drawBarManual(Graphics g)
	{		
		debug ("drawBarManual ("+currentLevel+","+maxLevel+")");
						
		if (progressVisualization==HoopProgressPainter.VIZ_FLAT)
		{
			//Graphics2D g2 = (Graphics2D)g.create();
			
			int width=(maxLevel-4);
			
			g2.setColor(new Color (220,220,220));
			
			g2.fillRect (currentLevel,2,width,getHeight()-4);
			
			//g2.dispose ();
		}
		else
		{
			//Graphics2D g2 = (Graphics2D)g.create();

			Paint p = new GradientPaint (0,
										 4, 
										 new Color(180,180,180),
										 0,
										 getHeight ()-8,
										 new Color(220,220,220),
										 true);

			int width=(maxLevel-4);
							
			g2.setPaint (p);
			g2.fillRect (currentLevel,2,width,getHeight()-4);
		
			g2.setColor(new Color (0,0,0));
			g2.drawRect(currentLevel,2, width,this.getHeight()-4);
	
			//g2.dispose ();
		}	
	}
	/**
	 * 
	 * @param g
	 */
	protected void drawBarAuto(Graphics g)
	{		
		debug ("drawBarAuto ("+currentLevel+","+maxLevel+","+divver+")");
										
		if (progressVisualization==HoopProgressPainter.VIZ_FLAT)
		{						
			g2.setColor(new Color (220,220,220));
			
			g2.fillRect (0,2,currentPixelPosition,getHeight()-4);
			
			//g2.dispose ();
		}
		else
		{
			Paint p = new GradientPaint (0,
										 4, 
										 new Color(180,180,180),
										 0,
										 getHeight ()-8,
										 new Color(220,220,220),
										 true);							
			g2.setPaint (p);
			g2.fillRect (0,2,currentPixelPosition,getHeight()-4);
		
			g2.setColor(new Color (0,0,0));
			g2.drawRect(0,2, currentPixelPosition,this.getHeight()-4);
	
			//g2.dispose ();
		}	
	}	
	/**
	 * 
	 * @return
	 */
	public int getProgressVisualization() 
	{
		return progressVisualization;
	}
	/**
	 * 
	 * @param progressVisualization
	 */
	public void setProgressVisualization(int progressVisualization) 
	{
		this.progressVisualization = progressVisualization;
	}
	/**
	 * 
	 * @return
	 */
	public int getProgressBarType() 
	{
		return progressBarType;
	}
	/**
	 * 
	 * @param progressBarType
	 */
	public void setProgressBarType(int progressBarType) 
	{
		this.progressBarType = progressBarType;
	}	
}
