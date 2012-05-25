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
import org.jdesktop.swingx.MultiSplitPane;
import org.apache.hadoop.util.VersionInfo;

import edu.cmu.cs.in.base.INFileManager;
import edu.cmu.cs.in.base.INHoopLink;
import edu.cmu.cs.in.controls.base.INEmbeddedJPanel;
import edu.cmu.cs.in.controls.base.INJFrame;
import edu.cmu.cs.in.hoop.editor.INHoopEditorMenuBar;

/** 
 * @author vvelsen
 *
 */
public class INHoopMultiViewFrame extends INJFrame implements ActionListener
{
	private static final long serialVersionUID = -1;
		
	protected INHoopTabDraggable left=null;
	protected INHoopTabDraggable right=null;
	protected INHoopTabDraggable center=null;
	protected INHoopTabDraggable bottom=null;
	
    static final String ABOUTMSG = "Hoop is an interactive text exploration tool written with the express\n purpose of understanding narrative structures in written form.";
	
    //private JMenuBar menuBar=null;
	//private JToolBar toolBar=null;
	private INHoopStatusBar statusBar=null;
			
	/**
	 *
	 */	
    public INHoopMultiViewFrame() 
    {
        super ("Hoop");
        
		this.setTitle ("Hoop");
                
        final int inset=50;
        Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
        setBounds (inset,inset,screenSize.width-inset*2, screenSize.height-inset*2);
	    
	    loadImageIcons ();
		
		INHoopLink.fManager=new INFileManager ();
		INHoopLink.posFiles=new ArrayList<String> ();
                        
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
       JOptionPane.showMessageDialog(this, ABOUTMSG+"\n\nHoop version "+INHoopVersion.version+" (compiled on Hadoop "+VersionInfo.getVersion()+", running on port "+INHoopLink.monitorPort+")");
    }
    /**
     * 
     */
    protected JMenuBar getMainMenuBar ()
    {
    	return (INHoopLink.menuBar);
    }
	/**
	 *
	 */
    protected JToolBar getToolBar ()
    {
    	return (INHoopLink.toolBar);
    }
    /**
     * 
     */
    protected INHoopStatusBar getStatusBar ()
    {
    	return (statusBar);
    }
	/**
	 *
	 */    
    private void loadImageIcons ()
    {
    	debug ("loadImageIcons ()");
    	
    	java.net.URL imgURL=null;
    	String loadPath="";
      		        	
    	INHoopLink.imageIcons=new ImageIcon [INHoopLink.imgURLs.length];
    	
    	for (int i=0;i<INHoopLink.imgURLs.length;i++)
    	{
    		loadPath="/assets/images/"+INHoopLink.imgURLs [i];
    	    imgURL=getClass().getResource(loadPath);

    	    if (imgURL!=null) 
    	    {
    	    	INHoopLink.imageIcons [i]=new ImageIcon(imgURL,INHoopLink.imgURLs [i]);
    	    	//debug ("Loaded: " + loadPath);
    	    } 
    	    else 
    	    	debug ("Unable to load image ("+loadPath+") icon from jar");    		
    	}
    	
    	INHoopLink.icon=INHoopLink.getImageByName("machine.jpg");
    	INHoopLink.linkIcon=INHoopLink.getImageByName("link.jpg");
    	INHoopLink.unlinkIcon=INHoopLink.getImageByName("broken.jpg");
    }  
    /**
     * 
     */
    private void constructUI ()
    {
		//Border padding = BorderFactory.createEmptyBorder(2,0,0,0);
		Border blackborder=BorderFactory.createLineBorder(Color.black);
		//Border redborder=BorderFactory.createLineBorder(Color.red);		
						
		getContentPane().setLayout(new BoxLayout (getContentPane(),BoxLayout.Y_AXIS));
																								
		INHoopLink.menuBar = new INHoopEditorMenuBar ();
        setJMenuBar(INHoopLink.menuBar);
                												
		statusBar=new INHoopStatusBar ();
		statusBar.setBorder(blackborder);
		statusBar.setMinimumSize(new Dimension (50,22));
		statusBar.setPreferredSize(new Dimension (100,22));
		//statusBar.setMaximumSize(new Dimension (5000,22));		
				
		INHoopLink.toolBar=new JToolBar ("Still draggable");
		INHoopLink.toolBar.setMinimumSize(new Dimension (50,24));
		INHoopLink.toolBar.setPreferredSize(new Dimension (200,24));
		
		INHoopLink.toolBoxContainer=new JPanel ();
		INHoopLink.toolBoxContainer.setLayout(new BoxLayout (INHoopLink.toolBoxContainer,BoxLayout.X_AXIS));
		INHoopLink.toolBoxContainer.add(INHoopLink.toolBar);
		INHoopLink.toolBoxContainer.add (Box.createHorizontalGlue());
				
        JSeparator sep1=new JSeparator(SwingConstants.HORIZONTAL);
        //sep1.setBackground(new Color (150,150,150));		
        sep1.setMinimumSize(new Dimension (50,5));
        sep1.setMaximumSize(new Dimension (100,5));
                        
        getContentPane ().add(sep1);        
        getContentPane ().add (INHoopLink.toolBoxContainer);
        
        JSeparator sep2=new JSeparator(SwingConstants.HORIZONTAL);
        sep2.setMinimumSize(new Dimension (50,5));
        sep2.setMaximumSize(new Dimension (5000,5));

        getContentPane ().add(sep2);
		
		left=new INHoopTabDraggable ();		
		right=new INHoopTabDraggable ();		
		center=new INHoopTabDraggable ();		
		bottom=new INHoopTabDraggable ();
		
        String layoutDef = "(ROW weight=1.0 (LEAF name=left weight=0.2) (COLUMN weight=0.6 (LEAF name=middle weight=0.8) (LEAF name=bottom weight=0.2)) (LEAF name=right weight=0.2))";
        MultiSplitLayout.Node modelRoot = MultiSplitLayout.parseModel(layoutDef);

        MultiSplitPane multiSplitPane = new MultiSplitPane();
        multiSplitPane.setBackground(new Color (180,180,180));		
        multiSplitPane.setDividerSize(5);
        multiSplitPane.getMultiSplitLayout().setModel(modelRoot);
       	multiSplitPane.add(left, "left");
       	multiSplitPane.add(right, "right");
        multiSplitPane.add(center, "middle");
        multiSplitPane.add(bottom, "bottom");

       	Container cp = this.getContentPane();
       	cp.add(multiSplitPane, BorderLayout.CENTER);
       	
       	getContentPane ().add (statusBar);
       									
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
    protected void addView (String aTitle,
    						INEmbeddedJPanel aContent,
    						JTabbedPane aPane)
    {
    	debug ("addView ("+aTitle+",INEmbeddedJPanel,JTabbedPane)");
    	
    	if (INHoopLink.getWindow(aTitle)!=null)
    	{
    		debug ("We already have such a window, aborting");
    		return;
    	}
    	
    	aContent.setInstanceName(aTitle);
    	INHoopLink.addWindow(aContent);
    	
    	aPane.addTab(aTitle,INHoopLink.imageIcons [5],aContent,"New Panel");
    	int index=aPane.indexOfComponent (aContent);
    	INHoopTabPane pane=new INHoopTabPane (aPane);
    	aPane.setTabComponentAt(index,pane);
    	pane.update();
    }
	/**
	 *
	 */
    protected void addView (String aTitle,
    						INEmbeddedJPanel aContent,
    						String aPane)
    {
    	debug ("addView ("+aTitle+",INEmbeddedJPanel,String)");
    	
    	INHoopTabDraggable target=null;
   	
    	if (aPane.equals("center")==true)
    		target=center;
    	
    	if (aPane.equals("left")==true)
    		target=left;
    	
    	if (aPane.equals("right")==true)
    		target=right;
    	
    	if (aPane.equals("bottom")==true)
    		target=bottom;
    	
    	addView (aTitle,aContent,target);
    }    
	/**
	 *
	 */	
    public void quit() 
    {
        System.exit(0);
    }      
	/**
	 *
	 */	
	@Override
	public void actionPerformed (ActionEvent event) 
	{
		debug("actionPerformed ()");
	}
}
