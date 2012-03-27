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

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Iterator;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import edu.cmu.cs.in.base.INDataCollection;
import edu.cmu.cs.in.base.INBase;
import edu.cmu.cs.in.base.INFileManager;
//import edu.cmu.cs.in.quickbayes.INQuickBayesAttribute;
import edu.cmu.cs.in.ml.quickbayes.INQuickBayesData;
//import edu.cmu.cs.in.quickbayes.INQuickBayesFeature;
import edu.cmu.cs.in.ml.quickbayes.INSentinetReader;

@SuppressWarnings("serial")

public class INSentinetPanel extends JPanel implements ActionListener
{		
	private INSentinetReader reader=null;
	private INQuickBayesData dataGrid=null;
	
	private INDataCollection INData=null;	
	private INFileManager fManager=null;
	
	public INJFeatureList sentList=null;
    private JFileChooser fc=null;
	
    private JButton saveButton=null;
    private JButton saveAsButton=null;
    private JButton loadButton=null;
    private JButton importButton=null;    
    
    private StringBuffer loader=null;
    
    int matched=0;
    
    File permanence=null;
    
	/**------------------------------------------------------------------------------------
	 *
	 */
	private void debug (String aMessage)
	{
		INBase.debug ("INSentinetPanel",aMessage);	
	}
	/**------------------------------------------------------------------------------------
	 *
	 */	
	public INSentinetPanel ()
	{
		super();
		
		fc = new JFileChooser();
		FileNameExtensionFilter filter=new FileNameExtensionFilter (".txt and .csv files", "txt", "csv");
		fc.setFileFilter(filter);
		fManager=new INFileManager();

		reader=new INSentinetReader (INData);
		dataGrid=new INQuickBayesData ();
		
		debug ("INSentinetPanel ()");
		
		this.setBackground(INJColorTools.parse("#cccccc"));
						
		this.setLayout(new BoxLayout (this,BoxLayout.Y_AXIS));
				
		sentList=new INJFeatureList ();
	    	    	    	    	    	    	   	    	    				
	    Box buttonBox = new Box (BoxLayout.X_AXIS);

	    saveButton=new JButton ();
	    saveButton.setFont(new Font("Dialog", 1, 10));
	    saveButton.setMaximumSize(new Dimension (2000,20));
	    saveButton.setText("Save");
	    saveButton.setEnabled(false);
	    buttonBox.add (saveButton);

	    saveAsButton=new JButton ();
	    saveAsButton.setFont(new Font("Dialog", 1, 10));
	    saveAsButton.setMaximumSize(new Dimension (2000,20));
	    saveAsButton.setText("SaveAs");
	    //saveAsButton.setEnabled(false);
	    buttonBox.add (saveAsButton);
	    
	    loadButton=new JButton ();
	    loadButton.setFont(new Font("Dialog", 1, 10));
	    loadButton.setMaximumSize(new Dimension (2000,20));
	    loadButton.setText("Load");	
	    buttonBox.add (loadButton);
	    
	    importButton=new JButton ();
	    importButton.setFont(new Font("Dialog", 1, 10));
	    importButton.setMaximumSize(new Dimension (2000,20));
	    importButton.setText("Import");	
	    buttonBox.add (importButton);	    
		
	    this.add (sentList);		
	    this.add (buttonBox);
	    
	    saveButton.addActionListener (this);
	    saveAsButton.addActionListener (this);
	    loadButton.addActionListener (this);	    	    	    
	    importButton.addActionListener (this);
	}
	/**------------------------------------------------------------------------------------
	 *
	 */
	public void assignData (INDataCollection aData)
	{
		debug ("assignData ()");
		INData=aData;
		
		reader.assignData (aData);
	}
	/**------------------------------------------------------------------------------------
	 *
	 */		
    public void actionPerformed(ActionEvent event) 
    {
    	debug ("actionPerformed ()");
    	
		JButton button = (JButton)event.getSource();
		    	    	    	    	
		if (button.getText()=="Import") 
			importFile ();
		
		if (button.getText()=="Load") 
			load ();		
        
		if (button.getText()=="Save") 
			save ();
       
		if (button.getText()=="SaveAs") 
			saveAs ();        
    }
	/**------------------------------------------------------------------------------------
	 *
	 */    
    private void importFile ()
    {
    	debug ("importFile ()");
    	
        int returnVal=fc.showOpenDialog (this);
        
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

        		debug ("Loading: " + file.getName());
            
        		reader.processInputTab (fManager.loadContents(file.getAbsolutePath()),dataGrid);
        		
        		match ();
        	}	
        } 
        else 
        {
            debug ("Open command cancelled by user.");
        }    	
    }
	/**------------------------------------------------------------------------------------
	 *
	 */    
    private void load ()
    {
    	debug ("load ()");
    }
	/**------------------------------------------------------------------------------------
	 *
	 */    
    private void save ()
    {
    	debug ("save ()");
    	
    	if (permanence==null)
    	{
    		saveAs ();
    	}
    	else
    	{
            debug ("Saving: " + permanence.getName());
            
            toXML ();
                        
            fManager.saveContents(permanence.getAbsolutePath(),loader.toString());    		
    	}
    }
	/**------------------------------------------------------------------------------------
	 *
	 */    
    private void saveAs ()
    {
    	debug ("saveAs ()");
    	
        int returnVal=fc.showSaveDialog (this);
        
        if (returnVal==JFileChooser.APPROVE_OPTION) 
        {
        	permanence=fc.getSelectedFile();

            debug ("Saving: " + permanence.getName());
            
            toXML ();
            
            //debug ("XML: " + loader.toString());
            
            fManager.saveContents(permanence.getAbsolutePath(),loader.toString());
            
            saveButton.setEnabled (true);
        } 
        else 
        {
        	debug ("Save command cancelled by user");
        }
    }
	/**------------------------------------------------------------------------------------
	 *
	 */
    private void toXML ()
    {
    	debug ("toXML ()");
    	
    	loader=new StringBuffer ();
    	loader.append ("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
    	loader.append ("<data name=\"");
    	//loader.append (INData.sents.getPerspectiveName());
    	loader.append ("\" >\n");
    	
    	if (INData==null)
    	{
    		debug ("Error: no document set available");
    		return;
    	}
    	
		if (INData.sents!=null)
		{
			if (sentList!=null)
			{					
				for (int i=0;i<INData.sents.size();i++)
				{
					INVisualSentinetFeature feat=INData.sents.get(i);
					loader.append ("\t");
					loader.append (feat.toXML());
					loader.append ("\n");
				}								
			}
			else
				debug ("No sentinet panel yet");
		}
		else
			debug ("Error: no sentiment list available");		
        
    	loader.append ("</data>\n");
    }
	/**------------------------------------------------------------------------------------
	 *
	 */		
	public void addDependend (INVisualFeatureVisualizer aDep)
	{
	    sentList.addDependend (aDep);	    		
	}
	/**------------------------------------------------------------------------------------
	 *
	 */	
	private void match ()
	{
		debug ("matchSentinetFeatures ()");
				
		// Fill the panel ...
		
		if (INData.sents!=null)
		{
			if (sentList!=null)
			{			
				DefaultListModel sentModel=new DefaultListModel();
								
				for (int i=0;i<INData.sents.size();i++)
				{
					INVisualSentinetFeature feat=INData.sents.get(i);
					feat.setSelected(true); // We only do this during import
					sentModel.addElement (feat);				
				}
				
				sentList.setModel (sentModel);				
			}
			else
				debug ("No sentinet panel yet");
		}
		else
			debug ("Error: no sentiment list available");		
		
		// Match the documents to sentiment terms ...
		
		matched=0;
						
		ArrayList<INVisualSentence> sentences=INData.texts;
		if (sentences!=null)
		{
			for (int j=0;j<sentences.size();j++)
			{
				INVisualSentence sentence=sentences.get(j);
				matched+=sentence.buildSentimentFeatureList ("Sentinet",INData.sents);
			}
		}
		
		// All done, show what we have ...
		
		sentList.setLabel ("Number terms " + INData.sents.size() + ", nr hits: " + matched);
	}
	//-------------------------------------------------------------------------------------	
}
