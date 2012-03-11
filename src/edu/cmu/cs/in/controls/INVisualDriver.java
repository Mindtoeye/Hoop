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

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

//import edu.cmu.cs.in.INFeatureMatrixPane;
import edu.cmu.cs.in.base.INBase;

public class INVisualDriver extends ComponentAdapter 
{
	//@SuppressWarnings("unused")
	//private INFeatureMatrixPane responsibility=null;
	
	/**------------------------------------------------------------------------------------
	 *
	 */
	private void debug (String aMessage)
	{
		INBase.debug ("INVisualDriver",aMessage);	
	}	
	/**------------------------------------------------------------------------------------
	 *
	 */
	/*
	public INVisualDriver (INFeatureMatrixPane aResponsibility)
	{
		debug ("INVisualDriver ()");
		
		responsibility=aResponsibility;
	}
	*/
	/**------------------------------------------------------------------------------------
	 *
	 */	
	public void	componentHidden (ComponentEvent e)
	{
		//debug ("componentHidden ()");
		
	}	 
	/**------------------------------------------------------------------------------------
	 *
	 */
	public void	componentMoved (ComponentEvent e)
	{
		//debug ("componentMoved ()");
		
	}
	/**------------------------------------------------------------------------------------
	 *
	 */
	public void	componentResized (ComponentEvent e)
	{
		//debug ("componentResized ()");
		
	}
	/**------------------------------------------------------------------------------------
	 *
	 */
	public void	componentShown (ComponentEvent e)
	{
		debug ("componentShown ()");
		
		/*
		if (responsibility!=null)
			responsibility.process ();
		else
			debug ("Error: no component provided to propagate event");
		 */			
	}
	//-------------------------------------------------------------------------------------	
}
