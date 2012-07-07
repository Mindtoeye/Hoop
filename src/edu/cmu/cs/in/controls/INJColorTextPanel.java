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

import java.awt.Font;
import java.awt.Graphics2D;
import javax.swing.*;
import java.awt.*;
import java.text.*;
import java.util.ArrayList;
import java.awt.font.*;

import edu.cmu.cs.in.base.INBase;
import edu.cmu.cs.in.controls.INVisualFeature;
import edu.cmu.cs.in.controls.INVisualSentence;

/**
 *
 */
public class INJColorTextPanel extends JPanel implements ListCellRenderer
{
	private static final long serialVersionUID = 1L;
	public INVisualSentence featureSet=null;
	private int padding=6;
	private int lineSpacing=1;
	private int yOffset=20;
		
	/**
	 *
	 */
	private void debug (String aMessage)
	{
		INBase.debug ("INFeatureMatrixPane",aMessage);	
	}	
	/**
	 *
	 */	
	public INJColorTextPanel ()
	{
		super();
		
		debug ("INFeatureMatrixPane ()");		
		
		setBackground(INJColorTools.parse("#ffffff"));
	}
	/** 
     * Return the renderers fixed size here.  
     */
    public Dimension getPreferredSize() 
    {    	
    	return new Dimension (this.getWidth(),yOffset);
    }	
	/**
	 * Set the attributes of the class and return a reference
	 */	
	public Component getListCellRendererComponent (JList list,Object value,int index,boolean iss,boolean chf)
	{		
		featureSet=(INVisualSentence) value;
	
		if (iss) 
		{
            setBorder(BorderFactory.createLineBorder (Color.blue,1));
		} 
		else 
		{
            setBorder(BorderFactory.createLineBorder (list.getBackground(),1));
		}

		return this;
	}	
	/**
	 *
	 */	
	public void setPadding(int padding) 
	{
		this.padding = padding;
	}
	/**
	 *
	 */	
	public int getPadding() 
	{
		return padding;
	}	
	/**
	 *
	 */	
	public void paint (Graphics g) 
	{
		super.paint (g);
		//debug ("paint");
		
		if (featureSet==null)
		{
			debug ("No feature set available, aborting ...");
			return;
		}	
						
		Graphics2D g2d=(Graphics2D) g;
		
		// Show a small class indicator ...
		
		INVisualClass aClass=featureSet.getClassification();
		if (aClass!=null)
		{
			Color classColor=aClass.getColor();
			g2d.setColor(classColor);
			g2d.fillRect(0,0,4,this.getHeight());
		}
		
		// Now let's draw the features ...

		Font font=new Font("Verdana",Font.PLAIN,12);
		
		int counter=padding;
		yOffset=0;
		
		FontMetrics fm=g2d.getFontMetrics (font);
		
		ArrayList<INVisualFeature> featureList=featureSet.getFeatures ();
		
		int textHeight=10;
		
		for (int i=0;i<featureList.size();i++)
		{			
			INVisualFeature feature=(INVisualFeature) featureList.get(i);
			
			AttributedString attributedString=new AttributedString (feature.text);
			
			attributedString.addAttribute (TextAttribute.FONT,font);
									
			//debug ("Counter: " + counter + " at feature: " + feature.text);
			
			attributedString.addAttribute  (TextAttribute.FOREGROUND,
											feature.featureToCoverage(),
											0,
											feature.text.length());
			
			java.awt.geom.Rectangle2D rect=fm.getStringBounds (feature.text,g);
			
			textHeight=(int)(rect.getHeight());
			int textWidth =(int)(rect.getWidth());
						
			g2d.drawString (attributedString.getIterator(),counter,yOffset+textHeight);
			
			counter+=textWidth;
			counter+=2; // add our estimation of whitespace
			
			if (counter>this.getWidth())
			{
				counter=0;
				yOffset+=(textHeight+lineSpacing);
			}
		}	
		
		yOffset+=(textHeight+lineSpacing);
		
		setPreferredSize (new Dimension (getWidth (),yOffset));
	}
	/**
	 *
	 */	
/*	
	public void paint (Graphics g) 
	{
		super.paint (g);
		//debug ("paint");
		
		if (featureSet==null)
		{
			debug ("No feature set available, aborting ...");
			return;
		}	
				
		Graphics2D g2d=(Graphics2D) g;

		Font font=new Font("Verdana",Font.PLAIN,12);
		
		String formatted=featureSetToString (featureSet.getFeatures ());
		
		//debug ("Reconstructed: " + formatted);
		
		AttributedString attributedString=new AttributedString (formatted);
		
		attributedString.addAttribute (TextAttribute.FONT, font);

		featureSetToIntensity (attributedString,featureSet.getFeatures ());
		
		FontMetrics fm   = g2d.getFontMetrics (font);
		java.awt.geom.Rectangle2D rect=fm.getStringBounds (formatted,g);

		int textHeight = (int)(rect.getHeight()); 
		int textWidth  = (int)(rect.getWidth());
		//int panelHeight= this.getHeight();
		//int panelWidth = this.getWidth();
		    
		g2d.drawString (attributedString.getIterator(),padding,textHeight+padding);				
	}
*/		
	/**
	 *
	 */
	@SuppressWarnings("unused")
	private String featureSetToString (ArrayList<INVisualFeature> featureList)
	{
		//debug ("featureSetToString ()");
		
		StringBuffer formatter=new StringBuffer ();
		
		for (int i=0;i<featureList.size();i++)
		{
			INVisualFeature feature=(INVisualFeature) featureList.get(i);
			
			//debug ("Adding: " + feature.text);
			
			formatter.append (feature.text);
			formatter.append (" ");
		}
		
		return (formatter.toString());
	}
	/**
	 *
	 */
	@SuppressWarnings("unused")
	private void featureSetToIntensity (AttributedString visualString,ArrayList<INVisualFeature> featureList)
	{
		//debug ("featureSetToIntensity ()");
				
		int counter=0;
		
		for (int i=0;i<featureList.size();i++)
		{			
			INVisualFeature feature=(INVisualFeature) featureList.get(i);
			
			//debug ("Counter: " + counter + " at feature: " + feature.text);
			
			visualString.addAttribute (TextAttribute.FOREGROUND,feature.featureToCoverage(),counter,counter+feature.text.length());
			counter+=feature.text.length();
			counter++; // compensate for space
		}		
	}		
}
