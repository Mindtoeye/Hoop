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
import edu.cmu.cs.in.controls.base.INJInternalFrame;

/**
 * 
 */
public class INHoopStopWordEditor extends INJInternalFrame implements ActionListener
{
	private static final long serialVersionUID = 1L;
	
    private JFileChooser fc=null;
	private INJFeatureList stopList=null;
	
	/**
	 * 
	 */
	public INHoopStopWordEditor ()  
    {
    	super("StopWord Editor", true, true, true, true);
    	
		setClassName ("INHoopEditor");
		debug ("INHoopEditor ()");    	
    
		stopList=new INJFeatureList ();
		stopList.setLabel("Selected Stop Words");
		stopList.setMinimumSize(new Dimension (50,5000));
		stopList.setMaximumSize(new Dimension (5000,5000));
		
		JButton loadStops=new JButton ();
		loadStops.setText("Load Stopwords");
		loadStops.setFont(new Font("Dialog", 1, 10));
		loadStops.setMinimumSize(new Dimension (100,25));
		loadStops.setPreferredSize(new Dimension (5000,25));
		loadStops.setMaximumSize(new Dimension (5000,25));
		loadStops.addActionListener(this);				
		
		Box stopBox = new Box (BoxLayout.Y_AXIS);
		stopBox.setMinimumSize(new Dimension (50,50));
		stopBox.setPreferredSize(new Dimension (150,5000));
		stopBox.setMaximumSize(new Dimension (150,5000));
		
		stopBox.add(loadStops);
		stopBox.add(stopList);		
		
		setContentPane (stopBox);
		setSize (425,300);
		setLocation (75,75);
		
		loadData ();
    }
	/**
	 * 
	 */
	public void loadData ()
	{
		debug ("loadData ()");
		
		stopList.modelFromArray (INLink.stops);   	
		stopList.selectAll();		
	}
	/**
	 * 
	 */
	@Override
	public void actionPerformed(ActionEvent event) 
	{
		debug ("actionPerformed ("+event.getActionCommand()+")");
		
		int returnVal=0;
		
		String act=event.getActionCommand();
		JButton button = (JButton)event.getSource();
				
		//>---------------------------------------------------------
				
		if (button.getText().equals("Load Stopwords"))
		{
			debug ("Command " + act + " on loadStops");

			//fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			fc = new JFileChooser();
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
	        		
	        		debug ("Loading: " + file.getPath() + " ...");
	        		
	        		ArrayList <String> newStops=INLink.fManager.loadLines(file.getPath());
	        		
	        		stopList.modelFromArrayList (newStops);
	        		
	        		INLink.stops=new String [newStops.size()];
	        		
	        		for (int t=0;t<newStops.size();t++)
	        		{
	        			INLink.stops [t]=newStops.get(t);
	        		}
	        	}
	        }			
		}			
		
		//>---------------------------------------------------------		
	}  
}