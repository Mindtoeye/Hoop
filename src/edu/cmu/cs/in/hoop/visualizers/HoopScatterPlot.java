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

package edu.cmu.cs.in.hoop.visualizers;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.util.ArrayList;
import javax.swing.BorderFactory;

import edu.cmu.cs.in.base.HoopLink;
//import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.controls.base.HoopJPanel;
import edu.cmu.cs.in.stats.HoopSampleDataSet;
import edu.cmu.cs.in.stats.HoopSampleMeasure;

public class HoopScatterPlot extends HoopJPanel
{		
	private static final long serialVersionUID = 316985892467849872L;
	
	//private ArrayList <HoopSampleMeasure>data=null;
	private HoopSampleDataSet dataSet=null;
	
	private float min=5000000;
	private float max=-5000000;
	
	private Boolean dataPrep=false;
	
	private int windowLeft=30;
	private int windowRight=2;
	private int windowTop=12;
	private int windowBottom=12;
		
	private Boolean busy=false;
	
	private int visualN=0;
	
	/**
	 *
	 */
	public HoopScatterPlot () 
	{
		setClassName ("HoopScatterPlot");
		debug ("HoopScatterPlot ()");
		
		this.setBorder(BorderFactory.createLoweredBevelBorder());
	}
	/**
	 *
	 */
	public void setData (HoopSampleDataSet aData)
	{
		dataSet=aData;
		dataPrep=false;
		
		this.repaint ();
	}
	/**
	 *
	 */
	private void prepData ()
	{
		debug ("prepData ()");
	
    	if (dataSet==null)
    		return;		
		
		if (dataPrep==true)
			return;
				
	   	min=5000000;
    	max=-5000000;
    	
    	visualN=0;
    	
    	ArrayList <HoopSampleMeasure> data=dataSet.getDataSet();
    	
		debug ("Analyzing " + data.size() + " entries ...");
    	    	
    	for (int j=0;j<data.size();j++) // OPTIMIZE THIS!
    	{    		
    		HoopSampleMeasure test=data.get(j);
    		    		
    		//if ((test.isOpen()==false) && (test.getLabel().equals("Main")==false))
    		if (test.isOpen()==false)
    		{    		
    			if (test.getMeasure()<min)
    				min=test.getMeasure();
    		
    			if (test.getMeasure()>max)
    				max=test.getMeasure();
    			
    			visualN++;
    		}	
    		
    		debug ("data ("+j+"): " +test.getMeasure()+" min: " + min + " max: " + max);    		
    	}			
		
		dataPrep=true;
		
		debug ("prepData () Done");
	}
	/**
	 *
	 */	
	public void paintLabels (Graphics g) 
	{	
		Graphics2D g2=(Graphics2D)g;
		
		FontRenderContext frc=g2.getFontRenderContext();
		Font f = new Font("Courier",1,10);
		
		// Min
		
		String formatterA=String.format("%.2f",min/1000) ;

		TextLayout t1=new TextLayout (formatterA,f,frc);		
		g2.setColor (Color.black);
		t1.draw (g2,0,this.getHeight()-2);		
		
		TextLayout t=new TextLayout ("Time",f,frc);		
		g2.setColor (Color.black);
		t.draw (g2,0,this.getHeight()/2);		
		
		
		// Min
		
		String formatterB=String.format("%.2f",max/1000);
		TextLayout t2=new TextLayout (formatterB,f,frc);		
		g2.setColor (Color.black);
		t2.draw (g2,0,10);
	}
	/**
	 *
	 */		
	private void drawDecorations (Graphics g)
	{
    	int width=this.getWidth();
    	int height=this.getHeight();    
    	
		g.drawLine(windowLeft,windowTop,windowLeft,height-windowBottom);
		g.drawLine(width-windowRight,windowTop,width-windowRight,height-windowBottom);
		g.drawLine(windowLeft,windowTop,width-windowRight,windowTop);
		g.drawLine(windowLeft,height-windowBottom,width-windowRight,height-windowBottom);
	}
	/**
	 *
	 */	
    public void paint(Graphics g) 
    {   
    	debug ("paint ()");
    	
    	super.paint(g);
    	
    	Graphics2D g2=(Graphics2D)g;
    	g.clearRect(2, 2,this.getWidth()-4,this.getHeight()-4);
    	
    	//debug ("paint ("+data.size()+")");
    	    	
    	int width=this.getWidth();
    	int height=this.getHeight();    	
    	
    	if (dataSet==null){
    		debug("found data set nulll!!!!!!!!!!!!!!!!!!");
    		return;
    	}
    	    	    
    	if (busy==true)
    		return;
    	
    	busy=true;
    	
    	ArrayList <HoopSampleMeasure> data=dataSet.getDataSet();
    	
    	if (data.size()==0)
    		return;
    	
    	prepData ();
    	    	
    	float winWidth=width-windowLeft-windowRight;
    	float xDensity=(winWidth/((float) visualN));
    	float xProgress=0;
    	float yPlot=0;
    	
    	int oldXPlot=-1;
    	int oldYPlot=-1;
    	    	
    	float divver=(max-min);

    	if (divver==0)
    		divver=1;
    	
    	float yDensity=((height-windowBottom-windowTop)/divver);
    	
    	int closedCounter=0;
    	
    	//debug ("xDensity: " + xDensity + " divver:" + divver + " max: " + max +" min: " + min + " width: " +winWidth);   	
    	
    	for (int i=0;i<data.size();i++)
    	{    		
    		HoopSampleMeasure measure=data.get(i);
    		
    		FontRenderContext frc=g2.getFontRenderContext();
    		Font f = new Font("Courier",1,10);
    		
    		// Min
    		
    		String dataSize = HoopLink.dataSizeForHoop.get(i);
    		    	
    		TextLayout t1=new TextLayout (dataSize,f,frc);		
    		g2.setColor (Color.black);
    			    		
    		//if ((measure.isOpen()==false) && (measure.getLabel().equals("Main")==false))
    		if (measure.isOpen()==false)
    		{
    			yPlot=height-((measure.getMeasure()-min)*yDensity)-windowBottom;
    			
    			//debug ("Measure: "+ measure.getMeasure()+" yPlot: " + yPlot + " yDensity: " + yDensity + " at: " + xProgress);
    	    		
    			g.setColor (new Color (200,200,200));
 	    			    			
    			if ((oldXPlot!=-1) && (oldYPlot!=-1))
    			{
   					g.setColor (new Color (200,200,200));
    				
    				g.drawLine(oldXPlot,oldYPlot,(int) xProgress+windowLeft,(int) yPlot);
    				g.setColor(Color.black);
    				t1.draw(g2, (int) xProgress+windowLeft,(int) yPlot-10);
    				g.setColor (new Color (200,200,200));
    			}
    			    			    			
    			if (visualN<this.getWidth())
    				g.draw3DRect((int) xProgress+windowLeft-1,(int) yPlot-1,3,3,true);
    			else
    				g.draw3DRect((int) xProgress+windowLeft,(int) yPlot,1,1,true);
    			
    			oldXPlot=((int) xProgress+windowLeft);
    			oldYPlot=(int) yPlot;

    			xProgress+=xDensity;
    			
    			closedCounter++;
    		}	
    	}	
    	
    	//debug ("Plotted " + closedCounter + " measures");
    	
    	drawDecorations (g);
    	
    	if (closedCounter>0)
    		paintLabels (g);
    	    	    	
    	busy=false;
    }
	/**
	 *
	 */	
	public void updateContents() 
	{
		debug ("updateContents ()");

		//this.setData(HoopLink.metrics);
	}	    
}
