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

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
//import java.awt.LayoutManager;
import java.util.ArrayList;

//import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import edu.cmu.cs.in.base.INBase;

@SuppressWarnings("serial")

public class INFeatureWall extends JPanel implements INVisualFeatureVisualizer
{	
	public ArrayList <INVisualSentence> texts=null;
	
	private int xPadding=5;
	private int yPadding=5;
	
	private JPanel featureWall=null;
	private JScrollPane featureWallScrollPane =null;
	
	/**
	 *
	 */
	private void debug (String aMessage)
	{
		INBase.debug ("INFeatureWall",aMessage);	
	}
	/**
	 *
	 */	
	public INFeatureWall ()
	{
		super();
		
		debug ("INFeatureWall ()");
		
		this.setLayout(new BoxLayout (this,BoxLayout.Y_AXIS));
		
		featureWall=new JPanel ();
		
		featureWallScrollPane=new JScrollPane (featureWall);
		featureWallScrollPane.setMinimumSize (new Dimension(100,80));
		featureWallScrollPane.setMaximumSize (new Dimension(100,8000));
				
		this.add (featureWallScrollPane);
		JLabel featureWallLabel=new JLabel ("Feature Wall");
		featureWallLabel.setFont(new Font("Dialog", 1, 10));
		this.add (featureWallLabel);		
	}
	/**
	 *
	 */
	public void assignData (ArrayList <INVisualSentence> aData)
	{
		texts=aData;
		
		this.setSize(100,aData.size());
		
		//this.revalidate();
		updateVisualFeatures ();
	}
	/**
	 *
	 */	
	public void updateVisualFeatures () 
	{		
		debug ("updateVisualFeatures ()");
		
		if (texts==null)
		{
			debug ("No feature set available, aborting ...");
			return;
		}	
		
		int index=yPadding;
		int longest=0;
										
		Graphics panelGraphics=featureWall.getGraphics();
		
		if (panelGraphics==null)
		{
			debug ("Error obtaining graphics object for panel drawing");
			return;
		}
		
		for (index=0;index<texts.size();index++)
		{
			INVisualSentence sentence=texts.get(index);
			
			ArrayList<INVisualFeature> features=sentence.getFeatures();
			
			if (features==null)
			{
				debug ("Internal error, null features object found in visual sentence");
				return;
			}
			
			int counter=xPadding;
			
			for (int j=0;j<features.size();j++)
			{			
				INVisualFeature feature=(INVisualFeature) features.get(j);
				
				panelGraphics.setColor (feature.featureToCoverage ()); 

				panelGraphics.drawLine (counter,index,counter+feature.text.length(),index);
				
				if ((counter+feature.text.length())>longest)
				{
					longest=counter+feature.text.length();
				}	
				
				counter+=feature.text.length();
								
				//counter++; // we don't show whitespace
			}	
		}		
		
		featureWall.setPreferredSize (new Dimension (longest,texts.size()));
		featureWallScrollPane.revalidate();
	}	
}
