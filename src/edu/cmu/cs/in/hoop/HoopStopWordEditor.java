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

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.controls.HoopJFeatureList;
import edu.cmu.cs.in.controls.base.HoopEmbeddedJPanel;
//import edu.cmu.cs.in.controls.base.HoopJInternalFrame;

/**
 * 
 */
public class HoopStopWordEditor extends HoopEmbeddedJPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;
	
    private JFileChooser fc=null;
	private HoopJFeatureList stopList=null;
	private JButton loadStops=null;
	private JButton saveStops=null;
	
	/**
	 * 
	 */
	public HoopStopWordEditor ()  
    {
    	//super("StopWord Editor", true, true, true, true);
    	
		setClassName ("HoopStopWordEditor");
		debug ("HoopStopWordEditor ()");    	
    
	    Box buttonBox = new Box (BoxLayout.X_AXIS);
	    buttonBox.setBorder(BorderFactory.createEmptyBorder(1,1,1,2));
	    
	    loadStops=new JButton ();
	    loadStops.setFont(new Font("Dialog", 1, 8));
	    loadStops.setPreferredSize(new Dimension (20,20));
	    loadStops.setMaximumSize(new Dimension (20,20));
	    loadStops.setIcon(HoopLink.getImageByName("open.gif"));
	    loadStops.addActionListener(this);
	    buttonBox.add (loadStops);
	    
	    saveStops=new JButton ();
	    saveStops.setFont(new Font("Dialog", 1, 8));
	    saveStops.setPreferredSize(new Dimension (20,20));
	    saveStops.setMaximumSize(new Dimension (20,20));
	    saveStops.setIcon(HoopLink.getImageByName("save.gif"));
	    //foldButton.setText("None");
	    saveStops.addActionListener(this);
	    buttonBox.add (saveStops);
	    	    
	    buttonBox.add(Box.createHorizontalGlue());		
		
		stopList=new HoopJFeatureList ();
		stopList.setLabel("Selected Stop Words");
		stopList.setMinimumSize(new Dimension (50,5000));
		stopList.setMaximumSize(new Dimension (5000,5000));
							
		Box stopBox = new Box (BoxLayout.Y_AXIS);
		stopBox.setMinimumSize(new Dimension (50,50));
		stopBox.setPreferredSize(new Dimension (150,5000));
		stopBox.setMaximumSize(new Dimension (150,5000));
		
		stopBox.add(buttonBox);
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
		
		stopList.modelFromArray (HoopLink.stops);   	
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
				
		if (loadStops==button)
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
	        	    "Hoop Info Panel",
	        	    JOptionPane.YES_NO_CANCEL_OPTION,
	        	    JOptionPane.QUESTION_MESSAGE,
	        	    null,
	        	    options,
	        	    options[2]);
	        	
	        	if (n==0)
	        	{          	
	        		File file = fc.getSelectedFile();
	        		
	        		debug ("Loading: " + file.getPath() + " ...");
	        		
	        		ArrayList <String> newStops=HoopLink.fManager.loadLines(file.getPath());
	        		
	        		stopList.modelFromArrayList (newStops);
	        		
	        		HoopLink.stops=new String [newStops.size()];
	        		
	        		for (int t=0;t<newStops.size();t++)
	        		{
	        			HoopLink.stops [t]=newStops.get(t);
	        		}
	        	}
	        }			
		}			
		
		//>---------------------------------------------------------		
				
		if (saveStops==button)
		{
			
		}
		
		//>---------------------------------------------------------
		
	}  
}