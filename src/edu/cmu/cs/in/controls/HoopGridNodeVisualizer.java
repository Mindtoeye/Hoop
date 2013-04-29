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
import java.awt.Font;
import java.awt.Graphics2D;
//import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
//import javax.swing.border.Border;

import edu.cmu.cs.in.base.HoopRoot;
import edu.cmu.cs.in.base.HoopLink;

/**
 * 
 */
public class HoopGridNodeVisualizer extends JPanel
{	
	private static final long serialVersionUID = -4339919593982358362L;
	
	private int nrColumns=6;
	private int nrRows=6;
	
	private int nodeWidth=124;
	private int nodeHeight=70;
	
	private int paddingX=4;
    private int paddingY=14;
	
	private ArrayList<HoopGridNode> nodes=null;
		
	/**
	 *
	 */
	public HoopGridNodeVisualizer () 
	{
		debug ("HoopGridNodeVisualizer ()");
				
		this.setBorder(BorderFactory.createLoweredBevelBorder());
						
		nodes=new ArrayList<HoopGridNode> ();
	}
	/**
	 *
	 */
	private void debug (String aMessage)
	{
		HoopRoot.debug ("HoopGridNodeVisualizer",aMessage);
	}
	/**
	 *
	 */
	public void setNrColumns(int nrColumns) 
	{
		this.nrColumns = nrColumns;
	}
	/**
	 *
	 */	
	public int getNrColumns() 
	{
		return nrColumns;
	}
	/**
	 *
	 */	
	public void setNrRows(int nrRows) 
	{
		this.nrRows = nrRows;
	}
	/**
	 *
	 */	
	public int getNrRows() 
	{
		return nrRows;
	}		
	/**
	 *
	 */
	private HoopGridNode findNode (String aMachine)
	{
		for (int i=0;i<nodes.size();i++)
		{
			HoopGridNode test=nodes.get(i);
			if (test.getNodeName().equals(aMachine)==true)
			{
				return (test);
			}
		}
		
		return (null);
	}
	/**
	 *
	 */
	public HoopGridNode addNode (String aMachine)
	{
		HoopGridNode test=findNode (aMachine);
		if (test==null)
		{
			test=new HoopGridNode ();
			test.setNodeName(aMachine);
			nodes.add(test);
		}
		
		return (test);
	}
	/**
	 *
	 */
	public void incNodeMapper (String aMachine)
	{
		HoopGridNode test=findNode (aMachine);
		if (test==null)
		{
			test=new HoopGridNode ();
			test.setNodeName(aMachine);
			nodes.add(test);
		}
		
		test.incMapperCount();
		
		this.repaint ();
	}
	/**
	 *
	 */
	public void incNodeReducer (String aMachine)
	{
		HoopGridNode test=findNode (aMachine);
		if (test==null)
		{
			test=new HoopGridNode ();
			test.setNodeName(aMachine);
			nodes.add(test);
		}
		
		test.incReducerCount();
		
		this.repaint ();
	}
	/**
	 *
	 */
	public void decNodeMapper (String aMachine)
	{
		HoopGridNode test=findNode (aMachine);
		if (test==null)
		{
			test=new HoopGridNode ();
			test.setNodeName(aMachine);
			nodes.add(test);
		}
		
		test.decMapperCount();
		
		this.repaint ();
	}
	/**
	 *
	 */
	public void decNodeReducer (String aMachine)
	{
		HoopGridNode test=findNode (aMachine);
		if (test==null)
		{
			test=new HoopGridNode ();
			test.setNodeName(aMachine);
			nodes.add(test);
		}
		
		test.decReducerCount();
		
		this.repaint ();
	}	
	/**
	 *
	 */	
    public void paint(Graphics g) 
    {   
    	super.paint(g); // This renders the border
    	
		Graphics2D g2=(Graphics2D)g;
		
    	g.clearRect(2,2,this.getWidth()-4,this.getHeight()-4);
		g.setColor (new Color (220,220,220));
		g.fillRect(2,2,this.getWidth()-4,this.getHeight()-4);
		
		FontRenderContext frc=g2.getFontRenderContext();
		Font f = new Font("Dialog",1,10);
					    	
        int xPos=paddingX;
        int yPos=paddingY;                
        //int colCount=0;
        
        for (int i=0;i<nodes.size();i++)
        {        	
        	HoopGridNode node=nodes.get(i);

        	//Color bg = getBackground();
        	g.setColor (new Color (255,255,255));
        	
    		g.fill3DRect (xPos,yPos,nodeWidth,nodeHeight,true);
    		
    		if (HoopLink.icon!=null)
    			HoopLink.icon.paintIcon (this,g,xPos+2,yPos+2);

    		g2.setColor (Color.black);
    		
    		TextLayout t1=new TextLayout ("Mr:"+node.getMapperCount(),f,frc);	
    		t1.draw (g2,xPos+70,yPos+12);
    		
    		TextLayout t1A=new TextLayout ("Mt:"+node.getMapperCountTotal(),f,frc);	
    		t1A.draw (g2,xPos+70,yPos+24);    		
    		
    		TextLayout t2=new TextLayout ("Rr:"+node.getReducerCount(),f,frc);	
    		t2.draw (g2,xPos+70,yPos+36);
    		
    		TextLayout t2A=new TextLayout ("Rt:"+node.getReducerCountTotal(),f,frc);	
    		t2A.draw (g2,xPos+70,yPos+48);    		
    		
    		StringBuffer nameFormat=new StringBuffer ();
    		
    		if (node.getNodeName().length()>20)
    		{
    			nameFormat.append(node.getNodeName().substring(0,20));
    			nameFormat.append("...");
    		}
    		else
    			nameFormat.append(node.getNodeName());
    		
    		TextLayout t3=new TextLayout (nameFormat.toString(),f,frc);	
    		t3.draw (g2,xPos,yPos+nodeHeight+12);		
    		
    		xPos+=(nodeWidth+paddingX);
    		
    		//colCount++;
    		
    		if ((xPos+(nodeWidth+paddingX)>this.getWidth()))
    		{    	
    			//colCount=0;
            	xPos=paddingX;    		
            	yPos+=(nodeHeight+paddingY+2);
    		}	
        }               
    }
}

