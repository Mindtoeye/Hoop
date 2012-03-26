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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import edu.cmu.cs.in.controls.INJFeatureList;
import edu.cmu.cs.in.controls.base.INEmbeddedJPanel;
//import edu.cmu.cs.in.controls.base.INJInternalFrame;

/**
 * 
 */
public class INHoopJobList extends INEmbeddedJPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;
	
	private INJFeatureList jobList=null;
	
	/**
	 * 
	 */
	public INHoopJobList ()  
    {
    	//super("Hadoop Job List", true, true, true, true);
    	
		setClassName ("INHoopJobList");
		debug ("INHoopJobList ()");    	
    	
		jobList=new INJFeatureList ();
		jobList.setLabel("Selected Jobs");
		jobList.setMinimumSize(new Dimension (150,60));
		jobList.setPreferredSize(new Dimension (150,150));
		jobList.setMaximumSize(new Dimension (5000,150));		
		
		setContentPane (jobList);
		setSize (425,300);
		setLocation (75,75);		
    }
	/**
	 * 
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) 
	{
		
	}  
}