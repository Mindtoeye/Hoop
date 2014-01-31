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


import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
//import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import edu.cmu.cs.in.base.HoopRoot;

/** 
 * @author vvelsen
 *
 */
public class HoopWizardBase extends HoopRoot implements ActionListener
{
	// Settings

	private Boolean quitOnFinish=true;
	private int pageIndex=0;
	private int stepListWidth=125;

	// Panels, Frames and Controls ...

	protected JFrame frame; //will hold the mainPane, and any message dialogs
	protected JPanel mainPane=null; 
	
	private JLabel titleLabel=null;
	
	private JButton previousButton=null;
	private JButton nextButton=null;
	
	private JButton quitButton=null;
	private JButton cancelButton=null;
	private JButton restoreButton=null;
	
	private Box rightBox=null;
	
	private JPanel substitute=null;
	
	// Data models and tracking variables
	
	private JList pageList=null;
	private ArrayList<HoopWizardPanelDescription> pages=new ArrayList<HoopWizardPanelDescription> ();
	
	private DefaultListModel<String> listModel = new DefaultListModel<String>();	
	public static JProgressBar progress=null;
	
	/**
	 *
	 */
    public HoopWizardBase () 
    {
    	setClassName ("HoopWizardBase");
    	debug ("HoopWizardBase ()");    	
    }
    /**
     * 
     */    
	public Boolean getQuitOnFinish() 
	{
		return quitOnFinish;
	}
    /**
     * 
     */	
	public void setQuitOnFinish(Boolean quitOnFinish) 
	{
		this.quitOnFinish = quitOnFinish;
	}    
    /**
     * 
     */
    public void init (String aName)
    {
    	debug ("init ()");
    	
	    mainPane = new JPanel();
    	mainPane.setLayout(new BoxLayout (mainPane,BoxLayout.Y_AXIS));
    	mainPane.setBorder(new EmptyBorder(5,5,5,5));
    	//mainPane.getLayout().setHorizontalAlignment (SwingConstants.LEFT);
    	
		Box centerBox = new Box (BoxLayout.X_AXIS);
		centerBox.setBorder(new EmptyBorder(5,5,5,5));
		
		mainPane.add(centerBox);
		
		pageList=new JList ();
		pageList.setBorder(new EmptyBorder(5,5,5,5));
		pageList.setCellRenderer(new HoopWizardPageListRenderer());
		pageList.setEnabled(false);
		pageList.setMinimumSize (new Dimension (stepListWidth,50));
		pageList.setMaximumSize (new Dimension (stepListWidth,700));
		
		JScrollPane listScroller = new JScrollPane(pageList);
		
		listScroller.setMinimumSize (new Dimension (stepListWidth,50));
		listScroller.setPreferredSize (new Dimension (stepListWidth,300));
		listScroller.setMaximumSize (new Dimension (stepListWidth,700));
		listScroller.setBorder(BorderFactory.createLineBorder(new Color (100,100,100)));
		    	
    	centerBox.add(listScroller);
		
		rightBox = new Box (BoxLayout.Y_AXIS);
		rightBox.setBorder(new EmptyBorder(5,5,5,5));
	            
        titleLabel=new JLabel (aName);
        titleLabel.setHorizontalAlignment (SwingConstants.LEFT);
        titleLabel.setFont(new Font("Dialog", 1, 12));
        //titleLabel.setBorder(BorderFactory.createLineBorder(new Color (0,0,0)));
        titleLabel.setMinimumSize(new Dimension (50,22));
        //titleLabel.setMaximumSize(new Dimension (500,22));
        
        rightBox.add(titleLabel);
        rightBox.add(new JSeparator(SwingConstants.HORIZONTAL));

        centerBox.add(rightBox);
        
        substitute=createWizardJPanel ();
           			
		rightBox.add(substitute);
		rightBox.add(Box.createVerticalGlue()); 
		
		mainPane.add (centerBox);
		
		Box buttonBox = new Box (BoxLayout.X_AXIS);
		buttonBox.setBorder(new EmptyBorder(5,5,5,0));
		
		restoreButton = new JButton("Restore default settings");
		restoreButton.setEnabled(false);		
		
		buttonBox.add(restoreButton);
		
		buttonBox.add(Box.createHorizontalGlue());
		
		previousButton = new JButton("< Prev");
		previousButton.addActionListener(this);
		previousButton.setEnabled(false);
		
		buttonBox.add(previousButton);
		
		buttonBox.add(Box.createRigidArea(new Dimension(5,0)));
		
		nextButton = new JButton("Next >");
		nextButton.addActionListener(this);
		
		buttonBox.add(nextButton);		
		buttonBox.add(Box.createRigidArea(new Dimension(5,0)));

		quitButton = new JButton("Finish");
		quitButton.setEnabled(false);
		quitButton.addActionListener(this);
		
		buttonBox.add(quitButton);
		buttonBox.add(Box.createRigidArea(new Dimension(5,0)));
		
		cancelButton = new JButton("Cancel");
		cancelButton.setEnabled (false);
		cancelButton.addActionListener(this);
		
		buttonBox.add(cancelButton);
		
		mainPane.add(new JSeparator(SwingConstants.HORIZONTAL));
		mainPane.add(buttonBox);
		
		frame = new JFrame(aName);
		frame.setSize(582,347);
		frame.setContentPane(mainPane);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLocationRelativeTo(null); //umm should this be relative to something else??
		//frame.pack();
		frame.setResizable(false);
    }
    /**
     * 
     */
    public JButton getPreviousButton ()
    {
    	return (previousButton);
    }    
    /**
     * 
     */
    public JButton getNextButton ()
    {
    	return (nextButton);
    }    
    /**
     * 
     */
    public JButton getFinishButton ()
    {
    	return (quitButton);
    }    
    /**
     * 
     */
    public JButton getOkButton ()
    {
    	return (cancelButton);
    }
    /**
     * 
     */
    public void show ()
    {
		frame.setVisible(true);
    }
    /**
     * 
     */    
	public int getStepListWidth() 
	{
		return stepListWidth;
	}
    /**
     * 
     */	
	public void setStepListWidth(int stepListWidth) 
	{
		this.stepListWidth = stepListWidth;
	}        
    /**
     * 
     */
    protected void populateWizard ()
    {
    	debug ("populateWizard ()");

    	// Implement in child class
    }
    /**
     * 
     */
    public JPanel createWizardJPanel ()
    {
    	debug ("createWizardJPanel ()");
    	
		JPanel wizardPane=new JPanel ();
		BoxLayout layout=new BoxLayout (wizardPane,BoxLayout.Y_AXIS);
		wizardPane.setLayout (layout);
		wizardPane.setBorder(new EmptyBorder(5,5,5,5));
		wizardPane.setAlignmentX (Component.LEFT_ALIGNMENT);
		
		return (wizardPane);
    }    
    /**
     * 
     */
    protected void addPageLabel (String aLabel)
    {
    	debug ("addPageLabel ()");
    	    	
    	listModel.addElement (aLabel);
    	
    	pageList.setModel(listModel);
    }
    /**
     * 
     */
    public HoopWizardPanelDescription addPage (String aLabel,JPanel aPanel)
    {    	    	
    	debug ("addPage ()");
    	
    	HoopWizardPanelDescription newPage=new HoopWizardPanelDescription ();
    	newPage.setPanelLabel(aLabel);
    	newPage.setPanelContent(aPanel);
    	
    	pages.add(newPage);
    	
    	addPageLabel (aLabel);
    	
    	Component test=rightBox.getComponent(2);
    	
    	debug ("Test: " + test);
    	
    	if (test==substitute)
    	{
    		debug ("We have a start page, substituting ...");
    		rightBox.remove(substitute);
    		rightBox.add(aPanel,2);
    	}
    	else
    		debug ("Info: component at index 2 is not our substitute page");
    	
    	updateState ();
    	
    	return (newPage);
    }
    /**
     * 
     */
    private void updateState ()
    {
    	debug ("updateState ()");
    	
    	Component test=rightBox.getComponent(2);
    	
    	if (test!=null)
    	{
    		for (int i=0;i<pages.size();i++)
    		{    	    			
    			HoopWizardPanelDescription testPanel=pages.get(i);
    			
    			if (testPanel.getPanelContent()==test)
    			{
    				pageList.setSelectedIndex(i);
    				
    				titleLabel.setText(testPanel.getPanelLabel()+" - Page ( "+(i+1)+" of " + pages.size() + ")");
    				
    				return;
    			}
    		}
    	}
    	else
    		titleLabel.setText("No wizard pages defined");
    	
    	debug ("updateState () Done");
    }
    /**
     * 
     */
    protected void generateStartPage (JPanel aContainer)
    {
    	debug ("generateStartPage ()");
    	
    	// Implement in child class
    }
    /**
     * 
     */
    protected Boolean checkReadyToFinish ()
    {
    	debug ("checkReadyToFinish ()");
    	
    	// Implement in child class
    	
    	return (false);
    }
    /**
     * 
     */
    protected Boolean processFinish ()
    {
    	debug ("processFinish ()");
    	
    	// Implement in child class
    	
    	return (false);
    }
    /**
     * Remove any temp files, etc
     */
    protected Boolean cleanup ()
    {
    	debug ("cleanup ()");
    	
    	// Implement in child class
    	
    	return (false);
    }    
	/**
	 * 
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) 
	{
		debug ("actionPerformed ()");
		
		mainPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		
		Object comp=arg0.getSource();
		
		if (comp==cancelButton)
		{
			frame.setVisible(false);
			frame.dispose();
		}
		
		if (comp==restoreButton)
		{
			
		}
		
		if (comp==quitButton)
		{
			if (processFinish ()==true)
			{
				if (getQuitOnFinish()==true)
				{
					cleanup ();
				
					frame.setVisible(false);
					frame.dispose();
				}	
			}	
		}
		
		if (comp==previousButton)
		{
			pageIndex--;
			
			previousButton.setEnabled(true);
			nextButton.setEnabled(true);
			quitButton.setEnabled(false);
			
			if (pageIndex<=0)
			{
				pageIndex=0;
				
				previousButton.setEnabled(false);
			}
									
			updatePageContent ();
			
			updateState ();			
		}
		
		if (comp==nextButton)
		{
			pageIndex++;
			
			previousButton.setEnabled(true);
			nextButton.setEnabled(true);
			
			if (pageIndex>(pages.size()-1))
			{
				pageIndex=(pages.size()-1);
			}
			
			if (pageIndex==(pages.size ()-1))
			{
				nextButton.setEnabled(false);
								
				if (checkReadyToFinish ()==true)
				{
					/**
					 * Let the user do this instead
					 */
					
					//quitButton.setEnabled(true);									
				}
			}
			else
				quitButton.setEnabled(false);
			
			updatePageContent ();
			
			updateState ();
		}
		
		mainPane.setCursor(Cursor.getDefaultCursor());
	}	
	/**
	 * 
	 */
	private void updatePageContent ()
	{
		debug ("updatePageContent ()");
		
    	Component test=rightBox.getComponent(2);
    	
    	if (test!=null)
    	{
    		HoopWizardPanelDescription targetPage=pages.get(pageIndex);
    		if (targetPage!=null)
    		{	    		
    			rightBox.remove(test);
    			rightBox.add(targetPage.getPanelContent(),2);
    		}	
    	}			
    	
    	rightBox.repaint();
	}
	/**
	 * 
	 */
	public JPanel getCurrentPanel ()
	{
		return (pages.get(pageIndex).getPanelContent());
	}
	/**
	 * 
	 */
	public void showFinishOutput (String aString)
	{
		JPanel fPanel=getCurrentPanel ();
		
		if (fPanel instanceof HoopWizardFinishPage)
		{
			HoopWizardFinishPage finishPage=(HoopWizardFinishPage) fPanel;
			
			finishPage.showFinishOutput(aString);
		}
	}
	/**
	 * 
	 */
	public void resetFinish ()
	{
		JPanel fPanel=getCurrentPanel ();
		
		if (fPanel instanceof HoopWizardFinishPage)
		{
			HoopWizardFinishPage finishPage=(HoopWizardFinishPage) fPanel;
			
			finishPage.reset ();
		}		
	}
    /**
     * 
     * @param args
     */
	public static void main(String[] args)
	{
		@SuppressWarnings("unused")
		HoopWizardBase t = new HoopWizardBase();
	}
}
