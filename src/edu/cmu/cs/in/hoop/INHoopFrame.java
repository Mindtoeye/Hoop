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
import java.io.File;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.Border;
//import javax.swing.plaf.metal.MetalTabbedPaneUI;

import org.apache.hadoop.util.VersionInfo;

import edu.cmu.cs.in.INHoopMessageHandler;
import edu.cmu.cs.in.base.INFileManager;
import edu.cmu.cs.in.base.INLink;
//import edu.cmu.cs.in.controls.INEclipseTabbedPaneUI;
//import edu.cmu.cs.in.controls.INGridNodeVisualizer;
import edu.cmu.cs.in.controls.INJFeatureList;
//import edu.cmu.cs.in.controls.INTabbedPaneUI;
import edu.cmu.cs.in.controls.base.INJFrame;
import edu.cmu.cs.in.controls.INScatterPlot;
import edu.cmu.cs.in.network.INSocketServerBase;
import edu.cmu.cs.in.stats.INStatistics;

/** 
 * @author vvelsen
 *
 */
public class INHoopFrame extends INJFrame implements ActionListener
{
	private static final long serialVersionUID = 2788834485599638868L;
	
	JMenuBar menuBar;
    JDesktopPane desktop;
    JInternalFrame toolPalette;
    JCheckBoxMenuItem showToolPaletteMenuItem;

    static final Integer DOCLAYER = new Integer(5);
    static final Integer TOOLLAYER = new Integer(6);
    static final Integer HELPLAYER = new Integer(7);

    static final String ABOUTMSG = "Hoop is an interactive text exploration tool written with the express\n purpose of understanding narrative structures in written form.";
	
    private JFileChooser fc=null;
    
	private INSocketServerBase server=null;
	private INHoopMessageHandler handler=null;
	private INStatistics stats=null;
	private INScatterPlot plotter=null;
	private INJFeatureList jobList=null;     
	private INJFeatureList stopList=null;
	private INJFeatureList positionList=null;
	private	INHoopStatusBar statusbar=null;
		
	/**
	 *
	 */	
    public INHoopFrame() 
    {
        super ("Hoop");
        
		this.setTitle ("Hoop");
                
        final int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds (inset,inset,screenSize.width-inset*2, screenSize.height-inset*2);

	    java.net.URL imgURL=getClass().getResource("assets/images/machine.png");
	    if (imgURL!=null) 
	    {
	        INLink.icon=new ImageIcon(imgURL,"Machine");
	    } 
	    else 
	    	debug ("Unable to load image ("+imgURL+") icon from jar");
	    
	    java.net.URL linkURL=getClass().getResource("assets/images/link.jpg");
	    if (linkURL!=null) 
	    {
	        INLink.linkIcon=new ImageIcon(linkURL,"Link");
	    } 
	    else 
	    	debug ("Unable to load image ("+linkURL+") icon from jar");
	    
	    java.net.URL unlinkURL=getClass().getResource("assets/images/broken.jpg");
	    if (unlinkURL!=null) 
	    {
	        INLink.unlinkIcon=new ImageIcon(unlinkURL,"Unlink");
	    } 
	    else 
	    	debug ("Unable to load image ("+unlinkURL+") icon from jar");	
		
		INLink.fManager=new INFileManager ();
		fc = new JFileChooser();
		INLink.posFiles=new ArrayList<String> ();        
        
        buildContent();
        buildMenus();
        
        this.addWindowListener(new WindowAdapter() 
        {
        	public void windowClosing(WindowEvent e) 
        	{
        		quit();
        	}
        });                             
    }
	/**
	 *
	 */	
    protected void buildMenus() 
    {
        menuBar = new JMenuBar();
        menuBar.setOpaque(true);
        
        JMenu file = buildFileMenu();
        JMenu edit = buildEditMenu();
        JMenu views = buildViewsMenu();
        JMenu tools = buildToolsMenu();
        JMenu speed = buildSpeedMenu();
        JMenu help = buildHelpMenu();
        
        menuBar.add(file);
        menuBar.add(edit);
        menuBar.add(tools);
        menuBar.add(views);
        menuBar.add(speed);
        menuBar.add(help);
        
        setJMenuBar(menuBar);	
    }
	/**
	 *
	 */	
    protected JMenu buildFileMenu() 
    {
    	JMenu file = new JMenu("File");
    	JMenuItem newWin = new JMenuItem("New");
    	JMenuItem open = new JMenuItem("Open");
    	JMenuItem quit = new JMenuItem("Quit");

    	newWin.addActionListener(new ActionListener() 
    	{
    		public void actionPerformed(ActionEvent e) 
    		{
    			// Fill in later
    		}
    	});

    	open.addActionListener(new ActionListener() 
    	{
    		public void actionPerformed(ActionEvent e) 
    		{
    			// Fill in later
    		}
    	});

    	quit.addActionListener(new ActionListener() 
    	{
    		public void actionPerformed(ActionEvent e) 
    		{
    			quit();
    		}
    	});

    	file.add(newWin);
    	file.add(open);
    	file.addSeparator();
    	file.add(quit);
    	return file;
    }
	/**
	 *
	 */	
    protected JMenu buildEditMenu() 
    {
    	JMenu edit = new JMenu("Edit");
    	JMenuItem undo = new JMenuItem("Undo");
    	JMenuItem copy = new JMenuItem("Copy");
    	JMenuItem cut = new JMenuItem("Cut");
    	JMenuItem paste = new JMenuItem("Paste");
    	JMenuItem prefs = new JMenuItem("Preferences...");

    	undo.setEnabled(false);
    	copy.setEnabled(false);
    	cut.setEnabled(false);
    	paste.setEnabled(false);

    	prefs.addActionListener(new ActionListener() 
    	{
    		public void actionPerformed(ActionEvent e) 
    		{
    			openPrefsWindow();
    		}
    	});

    	edit.add(undo);
    	edit.addSeparator();
    	edit.add(cut);
    	edit.add(copy);
    	edit.add(paste);
    	edit.addSeparator();
    	edit.add(prefs);
    	return edit;
    }
	/**
	 *
	 */	
    protected JMenu buildViewsMenu() 
    {
    	JMenu views = new JMenu("Views");

    	JMenuItem documentItem = new JMenuItem("Document Viewer");

    	documentItem.addActionListener(new ActionListener() 
    	{
    		public void actionPerformed(ActionEvent e) 
    		{
    	    	JInternalFrame viewer = new INHoopDocumentViewer();
    	    	desktop.add (viewer, HELPLAYER);
    	    	try 
    	    	{ 
    	    		viewer.setVisible(true);
    	    		viewer.setSelected(true); 
    	    	} 
    	    	catch (java.beans.PropertyVetoException e2) 
    	    	{
    	    		
    	    	}
    		}
    	});
    	
    	JMenuItem documentListItem=new JMenuItem("Document Set Viewer");    	
    	
    	documentListItem.addActionListener(new ActionListener() 
    	{
    		public void actionPerformed(ActionEvent e) 
    		{
    	    	JInternalFrame viewer=new INHoopDocumentList();
    	    	desktop.add (viewer, HELPLAYER);
    	    	try 
    	    	{ 
    	    		viewer.setVisible(true);
    	    		viewer.setSelected(true); 
    	    	} 
    	    	catch (java.beans.PropertyVetoException e2) 
    	    	{
    	    		
    	    	}
    		}
    	});    	
    	
    	views.add (documentItem);
    	views.add (documentListItem);
    	
    	return (views);
    }
	/**
	 *
	 */	
    protected JMenu buildToolsMenu() 
    {
    	JMenu tools = new JMenu("Tools");

    	JMenuItem searchItem = new JMenuItem("Search");
    	JMenuItem clusterItem = new JMenuItem("Cluster Manager");
    	JMenuItem experimentItem = new JMenuItem("Experimenter");

    	searchItem.addActionListener(new ActionListener() 
    	{
    		public void actionPerformed(ActionEvent e) 
    		{
    			showSearchWindow();
    		}
    	});
   	
    	clusterItem.addActionListener(new ActionListener() 
    	{
    		public void actionPerformed(ActionEvent e) 
    		{
    			showClusterWindow();
    		}
    	});
   	
    	experimentItem.addActionListener(new ActionListener() 
    	{
    		public void actionPerformed(ActionEvent e) 
    		{
    			showExperimentWindow();
    		}
    	});    	

    	tools.add (searchItem);
    	tools.add (clusterItem);
    	tools.add (experimentItem);
    	
    	return (tools);
    }    
	/**
	 *
	 */	
    protected JMenu buildSpeedMenu() 
    {
        JMenu speed = new JMenu("Drag");

        JRadioButtonMenuItem live = new JRadioButtonMenuItem("Live");
        JRadioButtonMenuItem outline = new JRadioButtonMenuItem("Outline");

        JRadioButtonMenuItem slow = new JRadioButtonMenuItem("Old and Slow");

        ButtonGroup group = new ButtonGroup();

        group.add(live);
        group.add(outline);
        group.add(slow);

        live.setSelected(true);

        slow.addActionListener(new ActionListener()
        {
        	public void actionPerformed(ActionEvent e)
        	{
        		// for right now I'm saying if you set the mode
        		// to something other than a specified mode
        		// it will revert to the old way
        		// This is mostly for comparison's sake
        		desktop.setDragMode(-1);}});

        		live.addActionListener(new ActionListener()
        		{
        			public void actionPerformed(ActionEvent e)
        			{
        				desktop.setDragMode(JDesktopPane.LIVE_DRAG_MODE);
        			}
        		});
      
        		outline.addActionListener(new ActionListener()
        		{
        			public void actionPerformed(ActionEvent e)
        			{
                         desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
        			}
        		});
      
        speed.add(live);
        speed.add(outline);
        speed.add(slow);
        return speed;
    }
	/**
	 *
	 */	
    protected JMenu buildHelpMenu() 
    {
    	JMenu help = new JMenu("Help");
        JMenuItem about = new JMenuItem("About Hoop ...");
        JMenuItem openHelp = new JMenuItem("Open Help Window");

        about.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent e) 
        	{
        		showAboutBox();
        	}
        });

        openHelp.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent e) 
        	{
        		openHelpWindow();
        	}
        });

        help.add(about);
        help.add(openHelp);

        return help;
    }
	/**
	 *
	 */	
    protected void buildContent() 
    {    	                
		Border padding = BorderFactory.createEmptyBorder(2,0,0,0);
		Border blackborder=BorderFactory.createLineBorder(Color.black);
		//Border redborder=BorderFactory.createLineBorder(Color.red);		
						
		getContentPane().setLayout(new BoxLayout (getContentPane(),BoxLayout.Y_AXIS));
				
		Box centerBox = new Box (BoxLayout.X_AXIS);
		centerBox.setMinimumSize(new Dimension (50,25));
		centerBox.setPreferredSize(new Dimension (5000,5000));
		centerBox.setMaximumSize(new Dimension (5000,5000));		

		// Stop words ...
		
		stopList=new INJFeatureList ();
		stopList.setLabel("Selected Stop Words");
		stopList.setMinimumSize(new Dimension (50,5000));
		stopList.setMaximumSize(new Dimension (5000,5000));
		
		JButton loadStops=new JButton ();
		loadStops.setText("Load Stopwords");
		loadStops.setFont(new Font("Dialog", 1, 10));
		loadStops.setMinimumSize(new Dimension (100,25));
		loadStops.setPreferredSize(new Dimension (5000,25));
		loadStops.setMaximumSize(new Dimension (5000,25));
		loadStops.addActionListener(this);				
		
		Box stopBox = new Box (BoxLayout.Y_AXIS);
		stopBox.setMinimumSize(new Dimension (50,50));
		stopBox.setPreferredSize(new Dimension (150,5000));
		stopBox.setMaximumSize(new Dimension (150,5000));
		
		stopBox.add(loadStops);
		stopBox.add(stopList);
		
		// Job list ...
		
		jobList=new INJFeatureList ();
		jobList.setLabel("Selected Jobs");
		jobList.setMinimumSize(new Dimension (150,60));
		jobList.setPreferredSize(new Dimension (150,150));
		jobList.setMaximumSize(new Dimension (5000,150));		
		
		// Vocabulary/Dictionary
		
		// Stop words ...
		
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
		
		// Add it all together ...
		
		JTabbedPane leftTabs = new JTabbedPane();
		leftTabs.setFont(new Font("Dialog", 1, 10));

		stopBox.setBorder(padding);
		leftTabs.addTab("Stop Words",null,stopBox,"List of stop words");
		
		vocabularyBox.setBorder(padding);
		leftTabs.addTab("Vocabulary",null,vocabularyBox,"Global vocabulary or term dictionary");		

		jobList.setBorder(padding);
		leftTabs.addTab("Hadoop Jobs",null,jobList,"List of currently running jobs");
										
		JTabbedPane centerTabs = new JTabbedPane();
		centerTabs.setFont(new Font("Dialog", 1, 10));		

		desktop=new JDesktopPane();
		desktop.setBackground(new Color (120,120,120));
		
		centerTabs.addTab("Desktop",null,desktop,"Temp desktop");
				
		JSplitPane centerPane=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,leftTabs,centerTabs);
		centerPane.setResizeWeight(0.5);
		centerPane.setDividerLocation(250);
		centerPane.setOneTouchExpandable(true);
		centerPane.setContinuousLayout(true);
		
		centerBox.add(centerPane);
				
		// Add a plotter ...
		
		plotter=new INScatterPlot ();
		plotter.setMinimumSize(new Dimension (200,150));
		plotter.setPreferredSize(new Dimension (5000,150));
		plotter.setMaximumSize(new Dimension (5000,150));		
		
		Box statsBox = new Box (BoxLayout.X_AXIS);
		//statsBox.setBorder(redborder);					
		statsBox.setMinimumSize(new Dimension (20,110));
		statsBox.setPreferredSize(new Dimension (5000,110));
		statsBox.setMaximumSize(new Dimension (5000,110));	
		
		Box controlBox = new Box (BoxLayout.X_AXIS);
		//controlBox.setBorder(redborder);					
		controlBox.setMinimumSize(new Dimension (20,110));
		controlBox.setPreferredSize(new Dimension (5000,110));
		controlBox.setMaximumSize(new Dimension (5000,110));	
		
		statsBox.add (plotter);						
		controlBox.add (new INHoopConsole ());
		
		JTextArea rawStats=new JTextArea ();
		rawStats.setBorder(blackborder);
		rawStats.setEditable (false);
		rawStats.setFont(new Font("Dialog", 1, 10));
		rawStats.setMinimumSize(new Dimension (50,60));
		rawStats.setPreferredSize(new Dimension (5000,60));
		rawStats.setMaximumSize(new Dimension (5000,60));		
		
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.setFont(new Font("Dialog", 1, 10));
		statsBox.setBorder(padding);
		tabbedPane.addTab("Plots & Graphs",null,statsBox,"Visual representations of statistics");
		controlBox.setBorder(padding);
		tabbedPane.addTab("Console",null,controlBox,"Raw data and console output");
		rawStats.setBorder(padding);
		tabbedPane.addTab("Raw Statistics",null,rawStats,"Raw statistical data");
		
		// Add test controls to console tab bar
		
		//tabbedPane.setUI(new INEclipseTabbedPaneUI ());
		
		// Finally add your vanilla default standard status bar ...
						
		statusbar=new INHoopStatusBar ();
		statusbar.setBorder(blackborder);
		statusbar.setMinimumSize(new Dimension (50,25));
		statusbar.setPreferredSize(new Dimension (5000,25));
		statusbar.setMaximumSize(new Dimension (5000,25));		
		
		getContentPane ().add (centerBox);
		getContentPane ().add (tabbedPane);
		getContentPane ().add (statusbar);
		
		prepData ();
		
		handler=new INHoopMessageHandler (stats,plotter,rawStats,null,jobList);
		
		server=new INSocketServerBase ();
		server.setLocalPort (INLink.monitorPort);
		server.setMessageHandler (handler);
				
		server.runServer ();
		
		debug ("Ready for input");        
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
    public void openHelpWindow() 
    {
    	JInternalFrame help = new INHoopHelp();
    	desktop.add(help, HELPLAYER);
    	try 
    	{ 
    		help.setVisible(true);
    		help.setSelected(true); 
    	} 
    	catch (java.beans.PropertyVetoException e2) 
    	{
    		
    	}
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
    public void openPrefsWindow() 
    {
        //MetalworksPrefs dialog = new MetalworksPrefs(this);
        //dialog.show();
    }
	/**
	 *
	 */	
    public void showSearchWindow() 
    {
    	debug ("showSearchWindow ()");
    	
    	INHoopSearch doc=new INHoopSearch();
    	desktop.add (doc,DOCLAYER);
    
    	try 
    	{ 
    		doc.setVisible(true);
    		doc.setSelected(true); 
    	} 
    	catch (java.beans.PropertyVetoException e2) 
    	{
    		
    	}
    }
	/**
	 *
	 */	
    public void showClusterWindow() 
    {
    	debug ("showClusterWindow ()");
   	
    	INHoopCluster doc=new INHoopCluster();
    	desktop.add (doc,DOCLAYER);
    	   	
    	try 
    	{ 
    		doc.setVisible(true);
    		doc.setSelected(true); 
    	} 
    	catch (java.beans.PropertyVetoException e2) 
    	{
    		
    	}
    }
	/**
	 *
	 */	
    public void showExperimentWindow () 
    {
    	debug ("showExperimentWindow ()");
  	
    	INHoopExperimenter doc=new INHoopExperimenter();
    	desktop.add (doc,DOCLAYER);
    	  	
    	try 
    	{ 
    		doc.setVisible(true);
    		doc.setSelected(true); 
    	} 
    	catch (java.beans.PropertyVetoException e2) 
    	{
   		
    	}
    }    
    /**
	 *
	 */    
    private void prepData ()
    {
    	debug ("prepData");
   	
    	// Load the hardcoded minimal list of stop words
		stopList.modelFromArray (INLink.stops);   	
		stopList.selectAll();
    }    
	/**
	 *
	 */	
	@Override
	public void actionPerformed (ActionEvent event) 
	{
		debug ("actionPerformed ("+event.getActionCommand()+")");
		
		int returnVal=0;
		
		String act=event.getActionCommand();
		JButton button = (JButton)event.getSource();
		
		//>---------------------------------------------------------

		if (button.getText().equals("Load Vocabulary"))
		{
			debug ("Loading/Replacing term dictionary ...");
			
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
	        		
	        		ArrayList<String> tempFiles=INLink.fManager.listFiles (file.getPath());
	        	   	
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
	        				INLink.posFiles.add(test);
	        			}
	        		}
	        		
	        		positionList.modelFromArrayList (INLink.posFiles);   	
	        		positionList.selectAll();
	           	
	        		debug ("Found " + INLink.posFiles.size() + " position list files");
	        	}
	        }						
		}
		
		//>---------------------------------------------------------
				
		if (button.getText().equals("Load Stopwords"))
		{
			debug ("Command " + act + " on loadStops");

			//fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
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
	        		
	        		debug ("Loading: " + file.getPath() + " ...");
	        		
	        		ArrayList <String> newStops=INLink.fManager.loadLines(file.getPath());
	        		
	        		stopList.modelFromArrayList (newStops);
	        		
	        		INLink.stops=new String [newStops.size()];
	        		
	        		for (int t=0;t<newStops.size();t++)
	        		{
	        			INLink.stops [t]=newStops.get(t);
	        		}
	        	}
	        }			
		}			
		
		//>---------------------------------------------------------
	}  
}
