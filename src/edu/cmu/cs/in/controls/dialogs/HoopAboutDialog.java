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

package edu.cmu.cs.in.controls.dialogs;

import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.controls.HoopImageJPanel;
import edu.cmu.cs.in.controls.base.HoopJDialog;
import edu.cmu.cs.in.hoop.HoopVersion;

import org.apache.hadoop.util.VersionInfo;

/**
 * 
 */
public class HoopAboutDialog extends HoopJDialog implements ActionListener 
{	
	private static final long serialVersionUID = 7966149364705666547L;
	
	static final String ABOUTMSG = "Hoop is an interactive text exploration tool written with the express\n purpose of understanding linguistic structures in written form.";

	/**
     * 
     */
    public HoopAboutDialog (JFrame frame, boolean modal) 
	{
		super (HoopJDialog.OK,frame, modal,"About");
		
		setClassName ("HoopAboutDialog");
		debug ("HoopAboutDialog ()");				

		JPanel contentFrame=getFrame ();
		
		HoopImageJPanel linkButton=new HoopImageJPanel (HoopLink.getImageByName("Hoop-logo.png"));
		linkButton.setMinimumSize(new Dimension (250,167));
		//linkButton.setMaximumSize(new Dimension (250,167));
		linkButton.setPreferredSize(new Dimension (250,167));
		
		JTextArea infoField=new JTextArea ();
		infoField.setText(ABOUTMSG+"\n\nHoop version "+HoopVersion.version+"\nCompiled on: " + HoopVersion.compiledDate + "\n(compiled on Hadoop "+VersionInfo.getVersion()+", running on port "+HoopLink.monitorPort);
						
		contentFrame.add(linkButton);
		contentFrame.add(infoField);
		
		this.setSize(new Dimension (260,350));
    } 
}    