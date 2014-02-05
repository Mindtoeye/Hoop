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
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
//import javax.swing.JSeparator;
import javax.swing.JTextArea;
//import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import edu.cmu.cs.in.base.HoopFixedSizeQueue;
import edu.cmu.cs.in.base.HoopRoot;

/** 
 *
 */
public class HoopWizardFinishPage extends JPanel
{
	private static final long serialVersionUID = -4193982654107790562L;
	
	private JProgressBar progress=null;
	
	private JTextArea console=null;
	private JScrollPane consoleContainer=null;
	private int consoleSize=200; // Only show 200 lines
	private HoopFixedSizeQueue <String>consoleData=null;	
	
	/**
	 *
	 */
    public HoopWizardFinishPage () 
    {
    	debug ("HoopWizardFinishPage ()");
    	
		//>---------------------------------------------------------------------------    	
    	
		this.setLayout (new BoxLayout (this,BoxLayout.Y_AXIS));
		this.setBorder(new EmptyBorder(5,5,5,5));
		this.setAlignmentX(Component.LEFT_ALIGNMENT);    	
    	
    	JLabel explanationMessage=new JLabel ();
    	explanationMessage.setText ("Please click Finish to complete the wizard.");
    	explanationMessage.setAlignmentX(Component.LEFT_ALIGNMENT);
    	
    	progress=new JProgressBar ();
    	progress.setAlignmentX(Component.LEFT_ALIGNMENT);
    	
    	HoopWizardBase.progress=this.progress;
    			
		console=new JTextArea ();
	    console.setFont(new Font("Courier",1,12));
		console.setMinimumSize(new Dimension (50,150));
		console.setPreferredSize(new Dimension (50,150));
		
		consoleContainer = new JScrollPane (console);
		consoleContainer.setMinimumSize(new Dimension (50,150));
		//consoleContainer.setPreferredSize(new Dimension (50,150));
		consoleContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		//>---------------------------------------------------------------------------		
		
    	this.add(explanationMessage);
		    	    	
		this.add(progress);
		
		//this.add(Box.createVerticalGlue());
		this.add(Box.createRigidArea(new Dimension(0,10)));
				
		this.add(consoleContainer);
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
    /**
     * 
     */
    public void reset ()
    {
		consoleData=new HoopFixedSizeQueue<String>(consoleSize);
		
		console.setText(""); // Reset
    }
	/**
	 * 
	 */
	public void showFinishOutput (String aMessage)
	{
		debug (aMessage);
		
		consoleData.add (aMessage);
		
		if (console!=null)
		{	
			console.setText(consoleData.toConsoleString ());
			
			// Scroll to bottom			
			JScrollBar vertical = consoleContainer.getVerticalScrollBar();
			vertical.setValue( vertical.getMaximum() );
		}	
	}
}
