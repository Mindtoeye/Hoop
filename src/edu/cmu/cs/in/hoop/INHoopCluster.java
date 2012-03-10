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

/*
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.tree.*;
*/

package edu.cmu.cs.in.hoop;

import edu.cmu.cs.in.controls.INGridNodeVisualizer;
import edu.cmu.cs.in.controls.base.INJInternalFrame;

/**
 * 
 */
public class INHoopCluster extends INJInternalFrame 
{  
	private static final long serialVersionUID = 8387762921834350566L;

	public INHoopCluster() 
    {
    	super("Cluster Manager", true, true, true, true);

		setContentPane (new INGridNodeVisualizer ());
		setSize (325,200);
		setLocation (75,75);
    }
}
