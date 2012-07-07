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
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;

import edu.cmu.cs.in.controls.base.INEmbeddedJPanel;

/**
 * 
 */
public class INSentenceWall extends INEmbeddedJPanel implements INVisualFeatureVisualizer
{	
	private static final long serialVersionUID = -1L;
	
	public ArrayList <INVisualSentence> texts=null;	
	private JList sentenceList=null;
		
	/**
	 *
	 */	
	public INSentenceWall ()
	{
		setClassName ("INSentenceWall");
		debug ("INSentenceWall ()");
				
		Box mainBox = new Box (BoxLayout.Y_AXIS);
			
	    sentenceList=new JList ();
	    JScrollPane sentenceScrollList = new JScrollPane (sentenceList);
	    sentenceScrollList.setMinimumSize (new Dimension(100, 80));
	    
	    JLabel featuresLabel=new JLabel ("Feature Wall");
	    featuresLabel.setFont(new Font("Dialog", 1, 10));	    	    
	    
	    mainBox.add (sentenceScrollList);
	    mainBox.add(featuresLabel);
	    
	    setContentPane (mainBox);
	}
	/**
	 *
	 */	
	public void updateVisualFeatures () 
	{		
		debug ("updateVisualFeatures ()");
		
	}
	/**
	 *
	 */	
	public void assignData (ArrayList <INVisualSentence> aData)
	{
		debug ("assignData ()");
		
		texts=aData;
		
        DefaultListModel model=new DefaultListModel();
        
        for (int i=0;i<texts.size();i++)
        {
        	INVisualSentence sentence=texts.get(i);
        	model.addElement (sentence);
        }
        
        sentenceList.setCellRenderer (new INJColorTextPanel ());                
        sentenceList.setModel (model);             
	}	
}
