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

package edu.cmu.cs.in.hoop;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import edu.cmu.cs.in.base.INLink;
import edu.cmu.cs.in.controls.INJFeatureList;
import edu.cmu.cs.in.controls.base.INEmbeddedJPanel;
//import edu.cmu.cs.in.controls.base.INJInternalFrame;

/**
 * 
 */
public class INHoopVocabularyEditor extends INEmbeddedJPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;
	
	private INJFeatureList positionList=null;
    private JFileChooser fc=null;
	
	/**
	 * 
	 */
	public INHoopVocabularyEditor ()  
    {
    	//super("Vocabulary Editor", true, true, true, true);
    	
		positionList=new INJFeatureList ();
		positionList.setLabel("Vocabulary");
		positionList.setMinimumSize(new Dimension (50,5000));
		positionList.setMaximumSize(new Dimension (5000,5000));
		
		JButton loadVocabulary=new JButton ();
		loadVocabulary.setText("Load Vocabulary");
		loadVocabulary.setFont(new Font("Dialog", 1, 10));
		loadVocabulary.setMinimumSize(new Dimension (100,25));
		loadVocabulary.setPreferredSize(new Dimension (5000,25));
		loadVocabulary.setMaximumSize(new Dimension (5000,25));
		loadVocabulary.addActionListener(this);				
		
		Box vocabularyBox = new Box (BoxLayout.Y_AXIS);
		vocabularyBox.setMinimumSize(new Dimension (50,50));
		vocabularyBox.setPreferredSize(new Dimension (150,5000));
		vocabularyBox.setMaximumSize(new Dimension (150,5000));
		
		vocabularyBox.add(loadVocabulary);
		vocabularyBox.add(positionList);
		
		setContentPane (vocabularyBox);
		//setSize (425,300);
		//setLocation (75,75);		    	
    }
	/**
	 * 
	 */
	@Override
	public void actionPerformed(ActionEvent event) 
	{
		int returnVal=0;
		
		//String act=event.getActionCommand();
		JButton button = (JButton)event.getSource();
		
		//>---------------------------------------------------------

		if (button.getText().equals("Load Vocabulary"))
		{
			debug ("Loading/Replacing term dictionary ...");
			
			fc = new JFileChooser();
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	        returnVal=fc.showOpenDialog (this);

	        if (returnVal==JFileChooser.APPROVE_OPTION) 
	        {
	        	Object[] options = {"Yes",
	        	                    "No",
	        	                    "Cancel"};
	        	int n = JOptionPane.showOptionDialog(this,
	        	    "Loading a saved set will override any existing selections, do you want to continue?",
	        	    "IN Info Panel",
	        	    JOptionPane.YES_NO_CANCEL_OPTION,
	        	    JOptionPane.QUESTION_MESSAGE,
	        	    null,
	        	    options,
	        	    options[2]);
	        	
	        	if (n==0)
	        	{          	
	        		File file = fc.getSelectedFile();
	        		
	        		INLink.vocabularyPath=file.getPath ();
	        		
	        		ArrayList<String> tempFiles=INLink.fManager.listFiles (file.getPath());
	        	   	
	        		if (tempFiles==null)
	        		{
	        			debug ("Error no position list files found!");
	        			return;
	        		}
	        		else
	        			debug ("Found " + tempFiles.size() + " candidates");
	           	
	        		for (int i=0;i<tempFiles.size();i++)
	        		{
	        			String test=tempFiles.get(i);
	        			if (test.indexOf(".inv")!=-1)
	        			{	
	        				String [] splitter=test.split("\\.");
	        				INLink.posFiles.add(splitter [0]);
	        			}
	        		}
	        		
	        		positionList.modelFromArrayList (INLink.posFiles);   	
	        		positionList.selectAll();
	           	
	        		debug ("Found " + INLink.posFiles.size() + " position list files");
	        	}
	        }						
		}		
	}  
}