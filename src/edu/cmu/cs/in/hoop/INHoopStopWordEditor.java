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

import edu.cmu.cs.in.base.INHoopLink;
import edu.cmu.cs.in.controls.INJFeatureList;
import edu.cmu.cs.in.controls.base.INEmbeddedJPanel;
//import edu.cmu.cs.in.controls.base.INJInternalFrame;

/**
 * 
 */
public class INHoopStopWordEditor extends INEmbeddedJPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;
	
    private JFileChooser fc=null;
	private INJFeatureList stopList=null;
	
	/**
	 * 
	 */
	public INHoopStopWordEditor ()  
    {
    	//super("StopWord Editor", true, true, true, true);
    	
		setClassName ("INHoopStopWordEditor");
		debug ("INHoopStopWordEditor ()");    	
    
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
		//setSize (425,300);
		//setLocation (75,75);
		
		loadData ();
    }
	/**
	 * 
	 */
	public void loadData ()
	{
		debug ("loadData ()");
		
		stopList.modelFromArray (INHoopLink.stops);   	
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
	        		
	        		ArrayList <String> newStops=INHoopLink.fManager.loadLines(file.getPath());
	        		
	        		stopList.modelFromArrayList (newStops);
	        		
	        		INHoopLink.stops=new String [newStops.size()];
	        		
	        		for (int t=0;t<newStops.size();t++)
	        		{
	        			INHoopLink.stops [t]=newStops.get(t);
	        		}
	        	}
	        }			
		}			
		
		//>---------------------------------------------------------		
	}  
}