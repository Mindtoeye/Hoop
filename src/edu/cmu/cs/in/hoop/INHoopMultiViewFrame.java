/** 
 * Author: Martin van Velsen <vvelsen@cs.cmu.edu>
 * 
 * This source set uses code written for other various graduate 
 * courses and is part of a larger research effort in the field of
 * interactive narrative (IN). Courses that have contribute
 * to this source base are:
 * 
 * 	05-834 Applied Machine Learning
 *  11-719 Computational Models of Discourse Analysis
 *  11-741 Information Retrieval
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

//import edu.cmu.cs.in.INHoopMessageHandler;
import edu.cmu.cs.in.base.INFileManager;
import edu.cmu.cs.in.base.INLink;
import edu.cmu.cs.in.controls.base.INEmbeddedJPanel;
import edu.cmu.cs.in.controls.base.INJFrame;
//import edu.cmu.cs.in.controls.INScatterPlot;
//import edu.cmu.cs.in.network.INSocketServerBase;
//import edu.cmu.cs.in.stats.INStatistics;

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
	
    private JMenuBar menuBar=null;
	private JToolBar toolBar=null;
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
		
		INLink.fManager=new INFileManager ();
		INLink.posFiles=new ArrayList<String> ();
                        
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
       JOptionPane.showMessageDialog(this, ABOUTMSG+"\n\nHoop version "+INHoopVersion.version+" (compiled on Hadoop "+VersionInfo.getVersion()+", running on port "+INLink.monitorPort+")");
    }
    /**
     * 
     */
    protected JMenuBar getMainMenuBar ()
    {
    	return (menuBar);
    }
	/**
	 *
	 */
    protected JToolBar getToolBar ()
    {
    	return (toolBar);
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
    
	    imgURL=getClass().getResource("/assets/images/machine.png");
	    if (imgURL!=null) 
	    {
	        INLink.icon=new ImageIcon(imgURL,"Machine");
	    } 
	    else 
	    	debug ("Unable to load image ("+imgURL+") icon from jar");
	    
	    java.net.URL linkURL=getClass().getResource("/assets/images/link.jpg");
	    if (linkURL!=null) 
	    {
	        INLink.linkIcon=new ImageIcon(linkURL,"Link");
	    } 
	    else 
	    	debug ("Unable to load image ("+linkURL+") icon from jar");
	    
	    java.net.URL unlinkURL=getClass().getResource("/assets/images/broken.jpg");
	    if (unlinkURL!=null) 
	    {
	        INLink.unlinkIcon=new ImageIcon(unlinkURL,"Unlink");
	    } 
	    else 
	    	debug ("Unable to load image ("+unlinkURL+") icon from jar");	    	
    	
    	INLink.imageIcons=new ImageIcon [INLink.imgURLs.length];
    	
    	for (int i=0;i<INLink.imgURLs.length;i++)
    	{
    		loadPath="/assets/images/"+INLink.imgURLs [i];
    	    imgURL=getClass().getResource(loadPath);

    	    if (imgURL!=null) 
    	    {
    	    	INLink.imageIcons [i]=new ImageIcon(imgURL,INLink.imgURLs [i]);
    	    	debug ("Loaded: " + loadPath);
    	    } 
    	    else 
    	    	debug ("Unable to load image ("+loadPath+") icon from jar");    		
    	}
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
																								
        menuBar = new JMenuBar ();
        setJMenuBar(menuBar);
		
		// Add a plotter ...

		/*
		plotter=new INScatterPlot ();
		plotter.setMinimumSize(new Dimension (200,150));
		plotter.setPreferredSize(new Dimension (5000,150));
		plotter.setMaximumSize(new Dimension (5000,150));		
		
		Box statsBox = new Box (BoxLayout.X_AXIS);
		//statsBox.setBorder(redborder);					
		statsBox.setMinimumSize(new Dimension (20,110));
		statsBox.setPreferredSize(new Dimension (5000,110));
		statsBox.setMaximumSize(new Dimension (5000,110));	
				
		statsBox.add (plotter);
		*/						
				
		/*
		JTextArea rawStats=new JTextArea ();
		rawStats.setBorder(blackborder);
		rawStats.setEditable (false);
		rawStats.setFont(new Font("Dialog", 1, 10));
		rawStats.setMinimumSize(new Dimension (50,60));
		rawStats.setPreferredSize(new Dimension (5000,60));
		rawStats.setMaximumSize(new Dimension (5000,60));
		*/
			
		/*
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.setFont(new Font("Dialog", 1, 10));
		
		statsBox.setBorder(padding);
		tabbedPane.addTab("Plots & Graphs",null,statsBox,"Visual representations of statistics");
				
		rawStats.setBorder(padding);
		tabbedPane.addTab("Raw Statistics",null,rawStats,"Raw statistical data");
		*/
				
		// Finally add your vanilla default standard status bar ...
						
		statusBar=new INHoopStatusBar ();
		statusBar.setBorder(blackborder);
		statusBar.setMinimumSize(new Dimension (50,22));
		statusBar.setPreferredSize(new Dimension (5000,22));
		statusBar.setMaximumSize(new Dimension (5000,22));		
		
		//getContentPane ().add (centerBox);
		//getContentPane ().add (tabbedPane);
		
		toolBar = new JToolBar("Still draggable");
        //addButtons(toolBar);
        toolBar.setMinimumSize(new Dimension (50,24));
        toolBar.setPreferredSize(new Dimension (5000,24));
        toolBar.setMaximumSize(new Dimension (5000,24));	
        
        JSeparator sep1=new JSeparator(SwingConstants.HORIZONTAL);
        sep1.setMinimumSize(new Dimension (50,5));
        sep1.setMaximumSize(new Dimension (5000,5));
        
        getContentPane ().add(sep1);
        
        getContentPane ().add (toolBar);
        
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
        multiSplitPane.setDividerSize(5);
        multiSplitPane.getMultiSplitLayout().setModel(modelRoot);
       	multiSplitPane.add(left, "left");
       	multiSplitPane.add(right, "right");
        multiSplitPane.add(center, "middle");
        multiSplitPane.add(bottom, "bottom");

       	Container cp = this.getContentPane();
       	cp.add(multiSplitPane, BorderLayout.CENTER);
       	
       	getContentPane ().add (statusBar);
				
       	/*
		handler=new INHoopMessageHandler (stats,plotter,rawStats,null);
		
		server=new INSocketServerBase ();
		server.setLocalPort (INLink.monitorPort);
		server.setMessageHandler (handler);
				
		server.runServer ();
		*/
		
		debug ("Ready for input");        
		
		/*
		addView ("Package Explorer",new JPanel (),left);
		addView ("Navigator",new JPanel (),left);
		
		addView ("Search",new JPanel (),bottom);
		
		addView ("Problems",new JPanel (),right);		
		addView ("Console",new JPanel (),right);
		*/    	
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
    	
    	if (INLink.getWindow(aTitle)!=null)
    	{
    		debug ("We already have such a window, aborting");
    		return;
    	}
    	
    	aContent.setInstanceName(aTitle);
    	INLink.addWindow(aContent);
    	
    	aPane.addTab(aTitle,INLink.imageIcons [5],aContent,"New Panel");
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
