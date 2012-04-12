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
public class INHoopVocabularyEditor extends INEmbeddedJPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;
	
	private INJFeatureList positionList=null;
    private JFileChooser fc=null;
	
	/**
	 * 
	 */
	public INHoopVocabularyEditor ()  
    {
    	//super("Vocabulary Editor", true, true, true, true);
    	
		positionList=new INJFeatureList ();
		positionList.setLabel("Vocabulary");
		positionList.setMinimumSize(new Dimension (50,5000));
		positionList.setMaximumSize(new Dimension (5000,5000));
		
		JButton loadVocabulary=new JButton ();
		loadVocabulary.setText("Load Vocabulary");
		loadVocabulary.setFont(new Font("Dialog", 1, 10));
		loadVocabulary.setMinimumSize(new Dimension (100,25));
		loadVocabulary.setPreferredSize(new Dimension (5000,25));
		loadVocabulary.setMaximumSize(new Dimension (5000,25));
		loadVocabulary.addActionListener(this);				
		
		Box vocabularyBox = new Box (BoxLayout.Y_AXIS);
		vocabularyBox.setMinimumSize(new Dimension (50,50));
		vocabularyBox.setPreferredSize(new Dimension (150,5000));
		vocabularyBox.setMaximumSize(new Dimension (150,5000));
		
		vocabularyBox.add(loadVocabulary);
		vocabularyBox.add(positionList);
		
		setContentPane (vocabularyBox);
		//setSize (425,300);
		//setLocation (75,75);		    	
    }
	/**
	 * 
	 */
	@Override
	public void actionPerformed(ActionEvent event) 
	{
		int returnVal=0;
		
		//String act=event.getActionCommand();
		JButton button = (JButton)event.getSource();
		
		//>---------------------------------------------------------

		if (button.getText().equals("Load Vocabulary"))
		{
			debug ("Loading/Replacing term dictionary ...");
			
			fc = new JFileChooser();
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
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
	        		
	        		INHoopLink.vocabularyPath=file.getPath ();
	        		
	        		ArrayList<String> tempFiles=INHoopLink.fManager.listFiles (file.getPath());
	        	   	
	        		if (tempFiles==null)
	        		{
	        			debug ("Error no position list files found!");
	        			return;
	        		}
	        		else
	        			debug ("Found " + tempFiles.size() + " candidates");
	           	
	        		for (int i=0;i<tempFiles.size();i++)
	        		{
	        			String test=tempFiles.get(i);
	        			if (test.indexOf(".inv")!=-1)
	        			{	
	        				String [] splitter=test.split("\\.");
	        				INHoopLink.posFiles.add(splitter [0]);
	        			}
	        		}
	        		
	        		positionList.modelFromArrayList (INHoopLink.posFiles);   	
	        		positionList.selectAll();
	           	
	        		debug ("Found " + INHoopLink.posFiles.size() + " position list files");
	        	}
	        }						
		}		
	}  
}