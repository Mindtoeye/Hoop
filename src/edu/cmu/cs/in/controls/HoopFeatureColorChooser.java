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

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
//import javax.swing.JFrame;
import javax.swing.JPanel;

public class HoopFeatureColorChooser extends AbstractAction 
{
	private static final long serialVersionUID = 1L;
	
	JColorChooser chooser;
    JDialog dialog;
    
	/**------------------------------------------------------------------------------------
	 *
	 */
    HoopFeatureColorChooser (JPanel frame, JColorChooser chooser) 
    {
        super ("Color Chooser...");
        this.chooser = chooser;

        // Choose whether dialog is modal or modeless
        boolean modal = true;

        // Create the dialog that contains the chooser
        dialog = JColorChooser.createDialog (frame, "Hoop Feature Color", modal,chooser,null,null);
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