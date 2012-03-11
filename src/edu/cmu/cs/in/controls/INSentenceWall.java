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

//import java.awt.Dimension;
import java.awt.Dimension;
import java.awt.Font;
//import java.awt.Graphics;
//import java.awt.LayoutManager;
import java.util.ArrayList;

//import javax.swing.Box;
//import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import edu.cmu.cs.in.base.INBase;

@SuppressWarnings("serial")

public class INSentenceWall extends JPanel implements INVisualFeatureVisualizer
{	
	public ArrayList <INVisualSentence> texts=null;	
	private JList sentenceList=null;
		
	/**------------------------------------------------------------------------------------
	 *
	 */
	private void debug (String aMessage)
	{
		INBase.debug ("INSentenceWall",aMessage);	
	}
	/**------------------------------------------------------------------------------------
	 *
	 */	
	public INSentenceWall ()
	{
		super();
		
		debug ("INSentenceWall ()");
		
		this.setLayout(new BoxLayout (this,BoxLayout.Y_AXIS));
			
	    sentenceList=new JList ();
	    JScrollPane sentenceScrollList = new JScrollPane (sentenceList);
	    sentenceScrollList.setMinimumSize (new Dimension(100, 80));

	    this.add (sentenceScrollList);
	    JLabel featuresLabel=new JLabel ("Feature Wall");
	    featuresLabel.setFont(new Font("Dialog", 1, 10));	    	    
	    this.add (featuresLabel);		
	}
	/**------------------------------------------------------------------------------------
	 *
	 */	
	public void updateVisualFeatures () 
	{		
		debug ("updateVisualFeatures ()");
		
	}
	/**------------------------------------------------------------------------------------
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
	//-------------------------------------------------------------------------------------	
}
