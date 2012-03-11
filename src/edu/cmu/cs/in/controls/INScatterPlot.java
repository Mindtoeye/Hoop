/** 
 * Author: Martin van Velsen <vvelsen@cs.cmu.edu>
 * 
 * This source set uses code written for other various graduate 
 * courses and is part of a larger research effort in the field of
 * interactive narrative (IN). Courses that have contribute
 * to this source base are:
 * 
 * 	05-834 Applied Machine Learning
 *  11-719 Computational Models of Discourse Analysis
 *  11-741 Information Retrieval
 * 
 */

package edu.cmu.cs.in.controls;

import java.awt.Color;
//import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
//import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
//import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import edu.cmu.cs.in.base.INBase;
import edu.cmu.cs.in.stats.INPerformanceMetrics;

public class INScatterPlot extends JPanel
{		
	private static final long serialVersionUID = 316985892467849872L;
	
	private ArrayList <INPerformanceMetrics>data=null;
	
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
	public INScatterPlot () 
	{
		debug ("INScatterPlot ()");
		
		Border border=BorderFactory.createLineBorder(Color.red);
		this.setBorder(border);		
	}
	/**
	 *
	 */
	private void debug (String aMessage)
	{
		INBase.debug ("INScatterPlot",aMessage);	
	}
	/**
	 *
	 */
	public void setData (ArrayList<INPerformanceMetrics> aData)
	{
		data=aData;
		dataPrep=false;
		this.repaint ();
	}
	/**
	 *
	 */
	private void prepData ()
	{
		//debug ("prepData ()");
	
    	if (data==null)
    		return;		
		
		if (dataPrep==true)
			return;
		
	   	min=5000000;
    	max=-5000000;
    	
    	visualN=0;
    	    	
    	for (int j=0;j<data.size();j++) // OPTIMIZE THIS!
    	{    		
    		INPerformanceMetrics test=data.get(j);
    		    		
    		if ((test.isOpen()==false) && (test.getLabel().equals("Main")==false))
    		{    		
    			if (test.getMeasure()<min)
    				min=test.getMeasure();
    		
    			if (test.getMeasure()>max)
    				max=test.getMeasure();
    			
    			visualN++;
    		}	
    		
    		//debug ("data: " +test.getMeasure()+" min: " + min + " max: " + max);    		
    	}			
		
		dataPrep=true;
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
    	if (data==null)
    		return;
    	    	    
    	if (busy==true)
    		return;
    	
    	busy=true;
    	
    	if (data.size()==0)
    		return;
    	
    	prepData ();
    	
    	g.clearRect(0, 0,this.getWidth(),this.getHeight());
    	    	
    	//debug ("paint ("+data.size()+")");
    	    	
    	int width=this.getWidth();
    	int height=this.getHeight();    	
    	
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
    		INPerformanceMetrics measure=data.get(i);
    		
    		if ((measure.isOpen()==false) && (measure.getLabel().equals("Main")==false))
    		{
    			yPlot=height-((measure.getMeasure()-min)*yDensity)-windowBottom;
    			
    			//debug ("Measure: "+ measure.getMeasure()+" yPlot: " + yPlot + " yDensity: " + yDensity + " at: " + xProgress);
    	    		
    			g.setColor (new Color (200,200,200));
    			
    			if ((oldXPlot!=-1) && (oldYPlot!=-1))
    				g.drawLine(oldXPlot,oldYPlot,(int) xProgress+windowLeft,(int) yPlot);
    			    			    			
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
}

