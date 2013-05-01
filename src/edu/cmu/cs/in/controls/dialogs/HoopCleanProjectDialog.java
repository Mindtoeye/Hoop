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

import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import edu.cmu.cs.in.controls.base.HoopJDialog;

/**
 * 
 */
public class HoopCleanProjectDialog extends HoopJDialog implements ActionListener 
{
	private static final long serialVersionUID = -623676537557209085L;
	
	private JCheckBox cleanDocuments=null;
	private JCheckBox cleanBuildOutput=null;
	private JCheckBox cleanTempFiles=null;
	
	/**
     * 
     */
    public HoopCleanProjectDialog (JFrame frame, boolean modal) 
	{
		super (frame, modal,"Clean Project");
		
		setClassName ("HoopCleanProjectDialog");
		debug ("HoopCleanProjectDialog ()");				
    }  

    /**
     * 
     */
    protected void addContent ()
    {
		JPanel contentFrame=this.getFrame();
				
		cleanDocuments=new JCheckBox ();
		cleanDocuments.setOpaque(false);
		cleanDocuments.setText("Remove and reset document database");
		cleanDocuments.setFont(new Font("Dialog", 1, 10));
		contentFrame.add (cleanDocuments);
		
		cleanBuildOutput=new JCheckBox ();
		cleanBuildOutput.setOpaque(false);
		cleanBuildOutput.setText("Clean build output");
		cleanBuildOutput.setFont(new Font("Dialog", 1, 10));
		contentFrame.add (cleanBuildOutput);
		
		cleanTempFiles=new JCheckBox ();
		cleanTempFiles.setOpaque(false);
		cleanTempFiles.setText("Remove temp files");
		cleanTempFiles.setFont(new Font("Dialog", 1, 10));
		contentFrame.add (cleanTempFiles);		
    }
    /**
     * 
     */
    public Boolean getCleanDocuments ()
    {
    	return (cleanDocuments.isSelected());
    }
    /**
     * 
     */
    public Boolean getCleanBuildOutput ()
    {
    	return (cleanBuildOutput.isSelected());
    }
    /**
     * 
     */
    public Boolean getCleanTempFiles ()
    {
    	return (cleanTempFiles.isSelected());
    }    
}
