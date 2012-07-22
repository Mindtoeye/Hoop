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

import edu.cmu.cs.in.base.HoopRoot;

@SuppressWarnings("serial")

public class HoopFeatureWall extends JPanel implements HoopVisualFeatureVisualizer
{	
	public ArrayList <HoopVisualSentence> texts=null;
	
	private int xPadding=5;
	private int yPadding=5;
	
	private JPanel featureWall=null;
	private JScrollPane featureWallScrollPane =null;
	
	/**
	 *
	 */
	private void debug (String aMessage)
	{
		HoopRoot.debug ("HoopFeatureWall",aMessage);	
	}
	/**
	 *
	 */	
	public HoopFeatureWall ()
	{
		super();
		
		debug ("HoopFeatureWall ()");
		
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
	public void assignData (ArrayList <HoopVisualSentence> aData)
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
			HoopVisualSentence sentence=texts.get(index);
			
			ArrayList<HoopVisualFeature> features=sentence.getFeatures();
			
			if (features==null)
			{
				debug ("Internal error, null features object found in visual sentence");
				return;
			}
			
			int counter=xPadding;
			
			for (int j=0;j<features.size();j++)
			{			
				HoopVisualFeature feature=(HoopVisualFeature) features.get(j);
				
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
