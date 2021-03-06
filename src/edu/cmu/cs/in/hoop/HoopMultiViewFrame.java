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

import edu.cmu.cs.in.base.io.HoopVFSL;
import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.controls.base.HoopEmbeddedJPanel;
import edu.cmu.cs.in.controls.dialogs.HoopAboutDialog;
import edu.cmu.cs.in.controls.splitpanel.HoopMultiSplitLayout;
import edu.cmu.cs.in.controls.splitpanel.HoopMultiSplitNode;
import edu.cmu.cs.in.controls.splitpanel.HoopMultiSplitPane;
import edu.cmu.cs.in.hoop.editor.HoopEditorMenuBar;

/** 
 * @author vvelsen
 *
 */
public class HoopMultiViewFrame extends HoopPreferencesJFrame
{
	private static final long serialVersionUID = -1;
			    				
	/**
	 *
	 */	
    public HoopMultiViewFrame() 
    {
    	setClassName ("HoopMultiViewFrame");
    	debug ("HoopMultiViewFrame ()");
    	
		this.setTitle ("Hoop");
                	    
		// loadImageIcons ();
		
		HoopLink.fManager=new HoopVFSL ();
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
       //JOptionPane.showMessageDialog(this, ABOUTMSG+"\n\nHoop version "+HoopVersion.version+"\nCompiled on: " + HoopVersion.compiledDate + "\n(compiled on Hadoop "+VersionInfo.getVersion()+", running on port "+HoopLink.monitorPort+")");
    	HoopAboutDialog aboutDialog=new HoopAboutDialog (HoopLink.mainFrame,true);
    	aboutDialog.setVisible(true);
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
    private void constructUI ()
    {
    	Container cp = this.getContentPane();
    	
		//Border padding = BorderFactory.createEmptyBorder(2,0,0,0);
		Border blackborder=BorderFactory.createLineBorder(Color.black);
		//Border redborder=BorderFactory.createLineBorder(Color.red);		
						
		cp.setLayout(new BoxLayout (getContentPane(),BoxLayout.Y_AXIS));
																								
		HoopLink.menuBar = new HoopEditorMenuBar ();
        setJMenuBar(HoopLink.menuBar);
                												
		HoopLink.statusBar=new HoopStatusBar ();
		HoopLink.statusBar.setBorder(blackborder);
		HoopLink.statusBar.setMinimumSize(new Dimension (50,22));
		HoopLink.statusBar.setPreferredSize(new Dimension (100,22));
		//HoopLink.statusBar.setMaximumSize(new Dimension (1000,22));		
				
		HoopLink.toolBar=new JToolBar ("Still draggable");
		HoopLink.toolBar.setMinimumSize(new Dimension (50,24));
		HoopLink.toolBar.setPreferredSize(new Dimension (150,24));
		
		HoopLink.toolBoxContainer=new JPanel ();
		HoopLink.toolBoxContainer.setLayout(new BoxLayout (HoopLink.toolBoxContainer,BoxLayout.X_AXIS));
		HoopLink.toolBoxContainer.add(HoopLink.toolBar);
		HoopLink.toolBoxContainer.add (Box.createHorizontalGlue());
				
        JSeparator sep1=new JSeparator(SwingConstants.HORIZONTAL);
        //sep1.setBackground(new Color (150,150,150));		
        sep1.setMinimumSize(new Dimension (50,5));
        sep1.setMaximumSize(new Dimension (100,5));
                        
        //cp.add(sep1);
        cp.add (Box.createRigidArea(new Dimension(0,2)));
        
        cp.add (HoopLink.toolBoxContainer);
        
        JSeparator sep2=new JSeparator(SwingConstants.HORIZONTAL);
        sep2.setMinimumSize(new Dimension (50,5));
        sep2.setMaximumSize(new Dimension (500,5));

        //cp.add(sep2);
        cp.add (Box.createRigidArea(new Dimension(0,2)));
		
		HoopLink.left=new HoopTabDraggable ();		
		HoopLink.left.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		HoopLink.right=new HoopTabDraggable ();		
		HoopLink.right.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		HoopLink.center=new HoopTabDraggable ();		
		HoopLink.center.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		HoopLink.bottom=new HoopTabDraggable ();
		HoopLink.bottom.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		
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
        HoopMultiSplitNode modelRoot=HoopMultiSplitLayout.parseModel(layoutDef);

        HoopMultiSplitPane multiSplitPane = new HoopMultiSplitPane();
        multiSplitPane.setAutoscrolls(false);
        //multiSplitPane.setContinuousLayout(false);

        multiSplitPane.setBackground(new Color (180,180,180));		
        multiSplitPane.setDividerSize(5);
        multiSplitPane.getMultiSplitLayout().setModel(modelRoot);
       	multiSplitPane.add (HoopLink.left, "left");
       	multiSplitPane.add (HoopLink.right, "right");
        multiSplitPane.add (HoopLink.center, "middle");
        multiSplitPane.add (HoopLink.bottom, "bottom");
       	       	
       	cp.add (multiSplitPane, BorderLayout.CENTER);       	
       	cp.add (HoopLink.statusBar, BorderLayout.CENTER);       	
    }
    /**
     * 
     */
    public void processSplitDimensions ()
    {
    	debug ("processSplitDimensions ("+this.getWidth()+","+this.getHeight()+")");
    	
    	int horizontalStrip=this.getWidth()/4;
    	int horizontalCenter=this.getWidth()/2;
    	
    	int verticalStrip=this.getHeight()/4;
    	
    	debug ("Split (calculated) horizontal strip: " + horizontalStrip +", horizontal center: " + horizontalCenter +", verticalStrip: " + verticalStrip);
    	
    	horizontalStrip=HoopLink.preferences.getInt("horizontalStrip",horizontalStrip);
    	horizontalCenter=HoopLink.preferences.getInt("horizontalCenter",horizontalCenter);
    	verticalStrip=HoopLink.preferences.getInt("verticalStrip",verticalStrip);
    	
    	debug ("Split (stored) horizontal strip: " + horizontalStrip +", horizontal center: " + horizontalCenter +", verticalStrip: " + verticalStrip);
    	    	
       	HoopLink.left.setPreferredSize(new Dimension (horizontalStrip,this.getHeight()));
       	HoopLink.right.setPreferredSize(new Dimension (horizontalStrip,this.getHeight()));
       	HoopLink.center.setPreferredSize(new Dimension (horizontalCenter,this.getHeight()-verticalStrip));
       	HoopLink.bottom.setPreferredSize(new Dimension (horizontalCenter,verticalStrip));
       	
       	HoopLink.preferences.putInt("horizontalStrip", horizontalStrip);
       	HoopLink.preferences.putInt("horizontalCenter", horizontalCenter);
       	HoopLink.preferences.putInt("verticalStrip", verticalStrip);
    }
	/**
	 *
	 */
    /*
	@Override
	public void actionPerformed (ActionEvent event) 
	{
		debug("actionPerformed ()");
		
		
	}
	*/
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
