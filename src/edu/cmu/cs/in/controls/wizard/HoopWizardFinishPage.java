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

package edu.cmu.cs.in.controls.wizard;

import java.awt.Component;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import edu.cmu.cs.in.base.HoopRoot;

/** 
 * @author vvelsen
 *
 */
public class HoopWizardFinishPage extends JPanel
{
	private static final long serialVersionUID = -4193982654107790562L;
	
	JProgressBar progress=null;
	
	/**
	 *
	 */
    public HoopWizardFinishPage () 
    {
    	debug ("HoopWizardFinishPage ()");
    	
		this.setLayout (new BoxLayout (this,BoxLayout.Y_AXIS));
		this.setBorder(new EmptyBorder(5,5,5,5));
		this.setAlignmentX(Component.LEFT_ALIGNMENT);    	
    	
    	JLabel explanationMessage=new JLabel ();
    	explanationMessage.setText ("<html>Please click Finish to complete the wizard.<br><br><br></html>");
    	
    	progress=new JProgressBar ();
    	
    	HoopWizardBase.progress=this.progress;
    	
    	this.add(explanationMessage);
		this.add(new JSeparator(SwingConstants.HORIZONTAL));
		this.add(progress);
    }
    /**
     * 
     * @return
     */
    public JProgressBar getProgressBar ()
    {
    	return (progress);
    }
    /**
     * 
     */
    private void debug (String aMessage)
    {
    	HoopRoot.debug("HoopWizardFinishPage",aMessage);
    }
}
