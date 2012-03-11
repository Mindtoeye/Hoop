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

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
//import javax.swing.JFrame;
import javax.swing.JPanel;

public class INFeatureColorChooser extends AbstractAction 
{
	private static final long serialVersionUID = 1L;
	
	JColorChooser chooser;
    JDialog dialog;
    
	/**------------------------------------------------------------------------------------
	 *
	 */
    INFeatureColorChooser (JPanel frame, JColorChooser chooser) 
    {
        super ("Color Chooser...");
        this.chooser = chooser;

        // Choose whether dialog is modal or modeless
        boolean modal = true;

        // Create the dialog that contains the chooser
        dialog = JColorChooser.createDialog (frame, "IN Feature Color", modal,chooser,null,null);
    }
	/**------------------------------------------------------------------------------------
	 *
	 */
    public void actionPerformed(ActionEvent evt) 
    {
        // Show dialog
        dialog.setVisible(true);

        // Disable the action; to enable the action when the dialog is closed, see
        // Listening for OK and Cancel Events in a JColorChooser Dialog
        setEnabled(false);
    }
	//-------------------------------------------------------------------------------------    
};