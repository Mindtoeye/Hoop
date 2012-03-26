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

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.cmu.cs.in.base.INDataCollection;
import edu.cmu.cs.in.base.INBase;
import edu.cmu.cs.in.base.INFileManager;

@SuppressWarnings("serial")

public class INGrammarPanel extends JPanel implements ActionListener
{			
	private INDataCollection INData=null;		
    private JFileChooser fc=null;
	private INFileManager fManager=null;

    private StringBuffer loader=null;	
	
    private JButton addButton=null;
    private JButton removeButton=null;    
    
    private JButton saveButton=null;
    private JButton saveAsButton=null;
    private JButton loadButton=null;	
    
    //private JList<INVisualRuleFeature> paraphraseList=null;    
    private JList paraphraseList=null;
    private INJFeatureList ruleList=null;    
    private ArrayList <INVisualRuleFeature> ruleTemplates=null;
    
    private DefaultListModel ruleModel=null;
	
	/**
	 *
	 */
	private void debug (String aMessage)
	{
		INBase.debug ("INGrammarPanel",aMessage);	
	}
	/**
	 *
	 */	
	public INGrammarPanel ()
	{
		super();
		
		debug ("INGrammarPanel ()");

		this.setLayout(new BoxLayout (this,BoxLayout.Y_AXIS));
				
		fc = new JFileChooser();
		FileNameExtensionFilter filter=new FileNameExtensionFilter (".xml rule files", "xml");
		fc.setFileFilter(filter);
		fManager=new INFileManager();
		
	    ruleModel=new DefaultListModel ();
		
		Box leftLayout=new Box (BoxLayout.Y_AXIS);
	    
	    paraphraseList=new JList ();
	    JScrollPane paraphraseScrollList = new JScrollPane (paraphraseList);
	    
	    ruleList=new INJFeatureList (false);
	    
	    //>-----------------------------------------------
	    
	    Box controlLayout=new Box (BoxLayout.X_AXIS);
	    
	    addButton=new JButton ();
	    addButton.setText ("Add");
	    addButton.setMaximumSize(new Dimension (2000,20));
	    addButton.setFont(new Font("Dialog", 1, 10));
	    
	    removeButton=new JButton ();
	    removeButton.setText ("Remove");
	    removeButton.setMaximumSize(new Dimension (2000,20));
	    removeButton.setFont(new Font("Dialog", 1, 10));
	    
	    controlLayout.add (addButton);
	    controlLayout.add (removeButton);
	    
	    //>-----------------------------------------------	    
	    	    	    
	    leftLayout.add (paraphraseScrollList);
	    leftLayout.add (controlLayout);	    	    	    
	    //leftLayout.add (ruleScrollList);
	    leftLayout.add (ruleList);
	    
	    JPanel configPanel=new JPanel ();
	    configPanel.setMinimumSize(new Dimension (200,50));
	    configPanel.setMaximumSize(new Dimension (200,2000));
	    
	    Box rightLayout=new Box (BoxLayout.X_AXIS);
	    rightLayout.add(configPanel);
	    	    
		JSplitPane mainPanel=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,leftLayout,rightLayout);
	    		
	    Box aLayout=new Box (BoxLayout.X_AXIS);
	    aLayout.add(mainPanel);
	    
	    //>-----------------------------------------------		
		
	    Box buttonBox = new Box (BoxLayout.X_AXIS);
	    saveButton=new JButton ();
	    saveButton.setFont(new Font("Dialog", 1, 10));
	    saveButton.setMaximumSize(new Dimension (2000,20));
	    saveButton.setText("Save");
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
	    
	    //>-----------------------------------------------	    
		
	    this.add (aLayout);		
	    this.add (buttonBox);
	    
	    addButton.addActionListener (this);
	    removeButton.addActionListener (this);	    
	    
	    saveButton.addActionListener (this);
	    saveAsButton.addActionListener (this);
	    loadButton.addActionListener (this);
	    
		generateRules ();
		
		showRules ();
	}
	/**
	 *
	 */
	public void assignData (INDataCollection aData)
	{
		debug ("assignData ()");
		INData=aData;
		
		//reader.assignData (aData);
		
		//generateRules ();
	}	
	@SuppressWarnings("unused")
	/**
	 *
	 */
	private void addRule (String aDescription)
	{
		INVisualRuleFeature rule=new INVisualRuleFeature ();
		rule.setDescription(aDescription);
		ruleTemplates.add(rule);
	}
	@SuppressWarnings("unused")
	/**
	 *
	 */
	private void addRule (String aDescription,float aFreq)
	{
		INVisualRuleFeature rule=new INVisualRuleFeature ();
		rule.setDescription(aDescription);
		rule.setFrequency(aFreq);
		ruleTemplates.add(rule);
	}
	@SuppressWarnings("unused")
	/**
	 *
	 */
	private void addRule (String aDescription,String anExample)
	{
		INVisualRuleFeature rule=new INVisualRuleFeature ();
		rule.setDescription(aDescription);
		rule.setExample(anExample);
		ruleTemplates.add(rule);
	}	
	/**
	 *
	 */
	@SuppressWarnings("unused")
	private void addRule (String aDescription,String anExample,float aFreq)
	{
		INVisualRuleFeature rule=new INVisualRuleFeature ();
		rule.setDescription(aDescription);
		rule.setExample(anExample);
		rule.setFrequency(aFreq);
		ruleTemplates.add(rule);
	}		
	/**
	 *
	 */
	//@SuppressWarnings("unused")
	private void addRule (String aDescription,String anExample,float aFreq,String aBreak)
	{
		INVisualRuleFeature rule=new INVisualRuleFeature ();
		rule.setDescription(aDescription);
		rule.setExample(anExample);
		rule.setFrequency(aFreq);
		rule.setBreakTerm(aBreak);
		ruleTemplates.add(rule);
	}	
	/**
	 *
	 */
	private void generateRules ()
	{
		debug ("generateRules ()");
		
		ruleTemplates=new ArrayList<INVisualRuleFeature> ();
				
		//addRule ("C1 [,|;|.] C2","He loves her; she loves him.",(float) 10.45);
		addRule ("C1 and C2","They respect him and he respects them.",(float) 27.03,"and");
		addRule ("C1 and C2 back","She does love him and he loves her back.",(float) 0.73,"and");
		addRule ("C1 and C2 for that","He destroyed her life and she hates him for that.",(float) 0.055,"and");
		addRule ("C1 and C2, too","I chase him and he chaces me, too.",(float) 0.21,"and");
		addRule ("C1 when C2","He ignores her when she scolds him.",(float) 8.82,"when");
		addRule ("C1 whenever C2","He was there for her when she needed him.",(float) 1.71,"whenever");
		addRule ("C1 because C2","She married him becaus he was good to her",(float) 11.45,"because");
		addRule ("C1 as much as C2","They hit him as much as he hit them.",(float) 16.55,"as much as");
		addRule ("C1 for C2 (vb-ing)","They thanked him for helping them.",(float) 7.77,"for"); // They thanked him for helping. ? 
		addRule ("C1 but C2","He tried to talk to her but she ignores him.",(float) 0.10,"but");
		addRule ("C1 for what C2","They will punish him for what he did to them.",(float) 2.48,"for what");
		addRule ("C1 and thus C2","She rejected him and thus he killed her.",(float) 0.046,"and thus");
		//addRule ("when C1, C2","When she started to hit them, they arrested him.",(float) 10.91,"when");
		addRule ("C1 as long as C2","She is staying with him as long as he is kind to her.",(float) 1.69,"as long as");		
	}
	/**
	 *
	 */
	private void showRules ()
	{
		debug ("showRules ()");
		
		DefaultListModel listModel=new DefaultListModel();
				
		for (int i=0;i<ruleTemplates.size();i++)
		{
			INVisualRuleFeature rule=ruleTemplates.get(i);
			    		
			listModel.addElement (rule);		
                		      		
		}
		
		paraphraseList.setModel (listModel);				
	}
	/**
	 *
	 */
	private void showSelectedRules ()
	{
		debug ("showSelectedRules ()");
		
		DefaultListModel listModel=new DefaultListModel();
				
		for (int i=0;i<INData.rules.size();i++)
		{
			INVisualRuleFeature rule=INData.rules.get(i);
			    		
			listModel.addElement (rule);		
               		      		
		}
		
		ruleList.setModel (listModel);				
	}	
	/**
	 *
	 */		
	public void actionPerformed(ActionEvent e) 
	{
	   debug ("actionPerformed ()");
   		   
	   //>------------------------------------------------------
	   	
       if (e.getSource ()==addButton) 
       {   	       	   
		   Object[] ruleArray=paraphraseList.getSelectedValues();
		   
		   for (int i=0;i<ruleArray.length;i++)
		   {
			   INVisualRuleFeature rule=(INVisualRuleFeature) ruleArray [i];
			   if (rule!=null)
			   {
				   INVisualRuleFeature clone=rule.clone();
				   INData.rules.add(clone);
				   ruleModel.addElement(clone);				  
			   }
			   else
				   debug ("Error obtaining rule from templates");
		   }   
		   
		   ruleList.setModel(ruleModel);
       }
       
	   //>------------------------------------------------------
	   	
       if (e.getSource ()==removeButton) 
       {
    	   int index=ruleList.getSelectedIndex();
    	   if (index==-1)
    	   {
    		   
    	   }
    	   else
    	   {
    		   INVisualRuleFeature rule=INData.rules.get(index);
    		   DefaultListModel listModel=(DefaultListModel) paraphraseList.getModel();				  				    		
    		   listModel.removeElement(rule);
    		   INData.rules.remove(rule);
    	   }
       }       
       
	   //>------------------------------------------------------
   	
       if (e.getSource ()==loadButton) 
       {
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
               
           		fromXML (fManager.loadContentsXML(file.getAbsolutePath()));
           		
           		match ();
           	}	
           } 
           else 
           {
               debug ("Open command cancelled by user.");
           }
       }
       
       //>------------------------------------------------------        
       
       if (e.getSource() == saveButton) 
       {
           int returnVal = fc.showSaveDialog (this);
           
           if (returnVal == JFileChooser.APPROVE_OPTION) 
           {
               File file=fc.getSelectedFile();

               debug ("Saving: " + file.getName());
               
               toXML ();
               
               //debug ("XML: " + loader.toString());
               
               fManager.saveContents(file.getAbsolutePath(),loader.toString());
           } 
           else 
           {
           	debug ("Save command cancelled by user");
           }
       }
       
       //>------------------------------------------------------        
       
       if (e.getSource() == saveAsButton) 
       {
           int returnVal = fc.showSaveDialog (this);
           
           if (returnVal == JFileChooser.APPROVE_OPTION) 
           {
               File file=fc.getSelectedFile();

               debug ("Saving: " + file.getName());
               
               toXML ();
               
               //debug ("XML: " + loader.toString());
               
               fManager.saveContents(file.getAbsolutePath(),loader.toString());
           } 
           else 
           {
           	debug ("Save command cancelled by user");
           }
       }        
       
       //>------------------------------------------------------        
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
   	
		INData.rules=new ArrayList<INVisualRuleFeature> ();
   	      
		if (children.getLength()>0)
		{    
			for (int i=0;i<children.getLength();i++) 
			{
				Node node=children.item (i);
   	     
				if (node.getNodeName ().equals ("rule")==true)
				{    	    	 
					INVisualRuleFeature rule=new INVisualRuleFeature ();
					rule.fromXML(node);
					INData.rules.add(rule);
				}
			}
		}
		
		showSelectedRules ();
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
   	
		if (INData==null)
		{
			debug ("Error: no document set available");
			return;
		}
		
		if (INData.classes==null)
		{
			debug ("Error: no class set available");
			return;
		}    	
   	
		for (int j=0;j<INData.rules.size();j++)
		{   
			INVisualRuleFeature rule=INData.rules.get(j);
			loader.append("\t"+rule.toXML()+"\n");
       }	    	
       
		loader.append ("</data>\n");
	}	
	/**
	 *
	 */
	private void match ()
	{
		debug ("matchRules ()");
		
		for (int i=0;i<INData.texts.size();i++)
		{
			INVisualSentence sentence=INData.texts.get(i);
			sentence.buildRuleFeatureList ("Rules",INData.rules);
		}
	}	
}
