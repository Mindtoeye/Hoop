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

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.awt.Container;

import javax.swing.*;
import javax.swing.border.Border;

import org.jdesktop.swingx.MultiSplitLayout;
import org.jdesktop.swingx.MultiSplitNode;
import org.jdesktop.swingx.MultiSplitPane;
import org.apache.hadoop.util.VersionInfo;

import edu.cmu.cs.in.base.io.HoopFileManager;
import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.controls.base.HoopEmbeddedJPanel;
import edu.cmu.cs.in.hoop.editor.HoopEditorMenuBar;

/** 
 * @author vvelsen
 *
 */
public class HoopMultiViewFrame extends HoopPreferencesJFrame implements ActionListener
{
	private static final long serialVersionUID = -1;
			
    static final String ABOUTMSG = "Hoop is an interactive text exploration tool written with the express\n purpose of understanding narrative structures in written form.";
				
	/**
	 *
	 */	
    public HoopMultiViewFrame() 
    {
    	setClassName ("HoopMultiViewFrame");
    	debug ("HoopMultiViewFrame ()");
    	
		this.setTitle ("Hoop");
                	    
	    loadImageIcons ();
		
		HoopLink.fManager=new HoopFileManager ();
		HoopLink.posFiles=new ArrayList<String> ();
                        
        this.addWindowListener(new WindowAdapter() 
        {
        	public void windowClosing(WindowEvent e) 
        	{
        		quit();
        	}
        });      
        
        constructUI ();
    }    
	/**
	 *
	 */	
    public void showAboutBox() 
    {
       JOptionPane.showMessageDialog(this, ABOUTMSG+"\n\nHoop version "+HoopVersion.version+"\nCompiled on: " + HoopVersion.compiledDate + "\n(compiled on Hadoop "+VersionInfo.getVersion()+", running on port "+HoopLink.monitorPort+")");
    }
    /**
     * 
     */
    protected JMenuBar getMainMenuBar ()
    {
    	return (HoopLink.menuBar);
    }
	/**
	 *
	 */
    protected JToolBar getToolBar ()
    {
    	return (HoopLink.toolBar);
    }
    /**
     * 
     */
    protected HoopStatusBar getStatusBar ()
    {
    	return (HoopLink.statusBar);
    }
	/**
	 *
	 */    
    private void loadImageIcons ()
    {
    	debug ("loadImageIcons ()");
    	
    	java.net.URL imgURL=null;
    	String loadPath="";
      		        	
    	HoopLink.imageIcons=new ImageIcon [HoopLink.imgURLs.length];
    	
    	for (int i=0;i<HoopLink.imgURLs.length;i++)
    	{
    		loadPath="/assets/images/"+HoopLink.imgURLs [i];
    	    imgURL=getClass().getResource(loadPath);

    	    if (imgURL!=null) 
    	    {
    	    	HoopLink.imageIcons [i]=new ImageIcon(imgURL,HoopLink.imgURLs [i]);
    	    	//debug ("Loaded: " + loadPath);
    	    } 
    	    else 
    	    	debug ("Unable to load image ("+loadPath+") icon from jar");    		
    	}
    	
    	HoopLink.icon=HoopLink.getImageByName("machine.jpg");
    	HoopLink.linkIcon=HoopLink.getImageByName("link.jpg");
    	HoopLink.unlinkIcon=HoopLink.getImageByName("broken.jpg");
    }  
    /**
     * 
     */
    private void constructUI ()
    {
    	Container cp = this.getContentPane();
    	
		//Border padding = BorderFactory.createEmptyBorder(2,0,0,0);
		Border blackborder=BorderFactory.createLineBorder(Color.black);
		//Border redborder=BorderFactory.createLineBorder(Color.red);		
						
		getContentPane().setLayout(new BoxLayout (getContentPane(),BoxLayout.Y_AXIS));
																								
		HoopLink.menuBar = new HoopEditorMenuBar ();
        setJMenuBar(HoopLink.menuBar);
                												
		HoopLink.statusBar=new HoopStatusBar ();
		HoopLink.statusBar.setBorder(blackborder);
		HoopLink.statusBar.setMinimumSize(new Dimension (50,22));
		HoopLink.statusBar.setPreferredSize(new Dimension (100,22));
		//HoopLink.statusBar.setMaximumSize(new Dimension (1000,22));		
				
		HoopLink.toolBar=new JToolBar ("Still draggable");
		HoopLink.toolBar.setMinimumSize(new Dimension (50,24));
		HoopLink.toolBar.setPreferredSize(new Dimension (200,24));
		
		HoopLink.toolBoxContainer=new JPanel ();
		HoopLink.toolBoxContainer.setLayout(new BoxLayout (HoopLink.toolBoxContainer,BoxLayout.X_AXIS));
		HoopLink.toolBoxContainer.add(HoopLink.toolBar);
		HoopLink.toolBoxContainer.add (Box.createHorizontalGlue());
				
        JSeparator sep1=new JSeparator(SwingConstants.HORIZONTAL);
        //sep1.setBackground(new Color (150,150,150));		
        sep1.setMinimumSize(new Dimension (50,5));
        sep1.setMaximumSize(new Dimension (100,5));
                        
        cp.add(sep1);        
        cp.add (HoopLink.toolBoxContainer);
        
        JSeparator sep2=new JSeparator(SwingConstants.HORIZONTAL);
        sep2.setMinimumSize(new Dimension (50,5));
        sep2.setMaximumSize(new Dimension (500,5));

        cp.add(sep2);
		
		HoopLink.left=new HoopTabDraggable ();		
		HoopLink.right=new HoopTabDraggable ();		
		HoopLink.center=new HoopTabDraggable ();		
		HoopLink.bottom=new HoopTabDraggable ();
		
		/*
 			(ROW weight=1.0 
 				(LEAF name=left weight=0.2) 
 				(COLUMN weight=0.6 
 					(LEAF name=middle weight=0.9) 
 					(LEAF name=bottom weight=0.1)
 				) 
 				(LEAF name=right weight=0.2)
 			)
		 */
		
        String layoutDef = "(ROW weight=1.0 (LEAF name=left weight=0.2) (COLUMN weight=0.6 (LEAF name=middle weight=0.9) (LEAF name=bottom weight=0.1)) (LEAF name=right weight=0.2))";
        MultiSplitNode modelRoot=MultiSplitLayout.parseModel(layoutDef);

        MultiSplitPane multiSplitPane = new MultiSplitPane();
        multiSplitPane.setBackground(new Color (180,180,180));		
        multiSplitPane.setDividerSize(5);
        multiSplitPane.getMultiSplitLayout().setModel(modelRoot);
       	multiSplitPane.add(HoopLink.left, "left");
       	multiSplitPane.add(HoopLink.right, "right");
        multiSplitPane.add(HoopLink.center, "middle");
        multiSplitPane.add(HoopLink.bottom, "bottom");
       	       	
       	cp.add(multiSplitPane, BorderLayout.CENTER);
       	
       	cp.add (HoopLink.statusBar, BorderLayout.CENTER);
       									
		debug ("Ready for input");        		 	
    }
	/**
	 *
	 */    
    protected JButton makeNavigationButton(String imageName,
    									   String actionCommand,
    									   String toolTipText,
    									   String altText,
    									   ImageIcon icon) 
    {
    	//Create and initialize the button.
    	JButton button = new JButton();
    	button.setBorder (null);
    	button.setMinimumSize(new Dimension (26,22));
    	button.setPreferredSize(new Dimension (50,22));
    	button.setActionCommand(actionCommand);
    	button.setToolTipText(toolTipText);
    	button.addActionListener(this);
    	button.setIcon(icon);
    	button.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
    	//button.setText(altText);

    	return button;
    }         
	/**
	 *
	 */	
	@Override
	public void actionPerformed (ActionEvent event) 
	{
		debug("actionPerformed ()");
	}
	/** 
	 * @param aTitle
	 * @param aContent
	 * @param aPane
	 */
	protected void addView (String aTitle,
							HoopEmbeddedJPanel aContent,
							JTabbedPane aPane)
	{
		debug ("addView ("+aTitle+",HoopEmbeddedJPanel,JTabbedPane)");

		HoopLink.addView (aTitle,aContent,aPane);	
	}
	/**
	 *
	 */
	protected void addView (String aTitle,
							HoopEmbeddedJPanel aContent,
							String aPane)
	{
		debug ("addView ()");
		
		HoopLink.addView (aTitle,aContent,aPane);
	}
}
