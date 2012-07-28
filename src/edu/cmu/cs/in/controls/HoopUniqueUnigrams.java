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
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
//import org.w3c.dom.Entity;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.cmu.cs.in.base.HoopDataCollection;
import edu.cmu.cs.in.base.HoopRoot;
import edu.cmu.cs.in.base.io.HoopFileManager;

@SuppressWarnings("serial")

/**
 * 
 */
public class HoopUniqueUnigrams extends JPanel implements ActionListener
{		
	private HoopDataCollection HoopData=null;	
	private HoopFileManager fManager=null;
	
	public HoopJFeatureList posList=null;
    public HoopJFeatureList negList=null;
    private JFileChooser fc=null;
	
    private JButton saveButton=null;
    private JButton saveAsButton=null;
    private JButton loadButton=null;
    
    private StringBuffer loader=null;
    
    File permanence=null;
    
	/**
	 *
	 */
	private void debug (String aMessage)
	{
		HoopRoot.debug ("HoopUniqueUnigrams",aMessage);	
	}
	/**
	 *
	 */	
	public HoopUniqueUnigrams ()
	{
		super();
		
		fc = new JFileChooser();
		FileNameExtensionFilter filter=new FileNameExtensionFilter (".xml unigram files", "xml");
		fc.setFileFilter(filter);
		fManager=new HoopFileManager();

        //Uncomment one of the following lines to try a different
        //file selection mode.  The first allows just directories
        //to be selected (and, at least in the Java look and feel,
        //shown).  The second allows both files and directories
        //to be selected.  If you leave these lines commented out,
        //then the default mode (FILES_ONLY) will be used.
        //
        //fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        //fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		
		debug ("HoopUniqueUnigrams ()");
		
		//Border border=BorderFactory.createLineBorder(Color.black);
		this.setBackground(HoopJColorTools.parse("#cccccc"));
						
		this.setLayout(new BoxLayout (this,BoxLayout.Y_AXIS));
				
		posList=new HoopJFeatureList ();
		posList.setMinimumSize (new Dimension (80,80));
	    posList.setMaximumSize (new Dimension (2000,2000));
	    	    
	    negList=new HoopJFeatureList ();
		negList.setMinimumSize (new Dimension (80,80));
	    negList.setMaximumSize (new Dimension (2000,2000));
	    	    
		JSplitPane horizontalPanel=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,posList,negList);
		//horizontalPanel.setBorder(border);
		horizontalPanel.setMinimumSize (new Dimension (80,80));
	    horizontalPanel.setMaximumSize (new Dimension (2000,2000));
	    
	    Box horSplitBox = new Box (BoxLayout.X_AXIS);
	    horSplitBox.add (horizontalPanel);		
	    	   	    	    				
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
	    buttonBox.add (saveAsButton);
	    
	    loadButton=new JButton ();
	    loadButton.setFont(new Font("Dialog", 1, 10));
	    loadButton.setMaximumSize(new Dimension (2000,20));
	    loadButton.setText("Load");	
	    buttonBox.add (loadButton);
		
	    this.add (horSplitBox);		
	    this.add (buttonBox);
	    
	    saveButton.addActionListener (this);
	    saveAsButton.addActionListener (this);
	    loadButton.addActionListener (this);	    	    	    
	}
	/**
	 *
	 */
	public void assignData (HoopDataCollection aData)
	{
		debug ("assignData ()");
		HoopData=aData;
	}
	/**
	 *
	 */		
    public void actionPerformed(ActionEvent event) 
    {
    	debug ("actionPerformed ()");
    	
		JButton button = (JButton)event.getSource();
		    	
    	//>------------------------------------------------------
    	    	    	
		if (button.getText()=="Load") 
			load ();
                
		if (button.getText()=="Save") 
			save ();
                
		if (button.getText()=="SaveAs") 
			saveAs ();
    }	
	/**
	 *
	 */    
    private void load ()
    {
        int returnVal=fc.showOpenDialog (this);

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

        		debug ("Loading: " + file.getName());
            
        		fromXML (fManager.loadContentsXML(file.getAbsolutePath()));
        	}
        }
    }
	/**
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
	/**
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
	/**
	 *
	 */
    private void fromXML (Element anElement)
    {
    	debug ("fromXML ("+anElement.getNodeName ()+")");
    	    	    	
    	NodeList children=anElement.getChildNodes ();
    	      
    	if (children==null)
    	{
    		debug ("Internal error: children list is null");
    		return;
    	}
    	
    	int matched=0;
    	      
    	if (children.getLength()>0)
    	{    
    		for (int i=0;i<children.getLength();i++) 
    	    {
    			Node node=children.item (i);
    	     
    			if (node.getNodeName ().equals ("class")==true)
    			{    	    	 
    				NamedNodeMap attributes = (NamedNodeMap)node.getAttributes();
    				for (int g = 0; g < attributes.getLength(); g++) 
    				{
    					Attr attribute = (Attr)attributes.item(g);
    					
    					if (attribute.getName ().equals ("name")==true)
    					{
    						debug ("Processing class: " + attribute.getValue ());

    						HoopVisualFeature testFeature=new HoopVisualFeature ();
                    	 
    						NodeList featList=node.getChildNodes ();
    						
    						for (int j=0;j<featList.getLength();j++)
    						{
    							Node featNode=featList.item (j);                    		 

    							testFeature.fromXML (featNode);

    							for (int t=0;t<HoopData.classes.size();t++)
    							{        
    								HoopVisualClass cl=(HoopVisualClass) HoopData.classes.get(t);
                    	        	                    	        	        		
    								HashMap<String,HoopVisualFeature>feats=cl.getFeatures();
  
    								HoopVisualFeature checkFeature=feats.get(testFeature.text);
    								if (checkFeature!=null)
    								{
    									checkFeature.setSelected(true);
    									matched++;
    								} 
    							}                    		                     		                     		 
    						}
    					}
    				}    	    	 
    			}
    	    }
    	}
    	
    	debug ("We've been able to match " + matched + " unigrams");
    }
	/**
	 *
	 */
    private void toXML ()
    {
    	debug ("toXML ()");
    	
    	loader=new StringBuffer ();
    	loader.append ("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
    	loader.append ("<data>\n");
    	
    	if (HoopData==null)
    	{
    		debug ("Error: no document set available");
    		return;
    	}
    	
    	if (HoopData.classes==null)
    	{
    		debug ("Error: no class set available");
    		return;
    	}    	
    	
        for (int j=0;j<HoopData.classes.size();j++)
        {        
        	HoopVisualClass cl=(HoopVisualClass) HoopData.classes.get(j);
        	
        	loader.append("\t<class name=\""+cl.getName()+"\">\n");
        	        		
        	HashMap<String,HoopVisualFeature>feats=cl.getFeatures();
        	Collection <HoopVisualFeature>col=feats.values();
        	Iterator<HoopVisualFeature>itr = col.iterator();
        		        		
       		while(itr.hasNext())
       		{        
       			HoopVisualFeature checker=itr.next();
       			//debug ("Appending: " + checker.text);
       			loader.append("\t\t"+checker.toXML()+"\n");
       		}		                
       		
       		loader.append("\t</class>\n");
        }	    	
        
    	loader.append ("</data>\n");
    }
	/**
	 *
	 */		
	public void addDependend (HoopVisualFeatureVisualizer aDep)
	{
	    posList.addDependend (aDep);	    
	    negList.addDependend (aDep);		
	}	
}
