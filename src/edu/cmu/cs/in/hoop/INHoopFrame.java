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
import javax.swing.*;

//import edu.cmu.cs.in.INHoopMessageHandler;
import edu.cmu.cs.in.base.INHoopLink;
import edu.cmu.cs.in.controls.INScatterPlot;
//import edu.cmu.cs.in.controls.INScatterPlot;
import edu.cmu.cs.in.hoop.properties.INHoopPropertyPanel;
//import edu.cmu.cs.in.network.INSocketServerBase;
//import edu.cmu.cs.in.stats.INStatistics;
import edu.cmu.cs.in.network.INMessageReceiver;
import edu.cmu.cs.in.network.INStreamedSocket;

/** 
 * @author vvelsen
 *
 */
public class INHoopFrame extends INHoopMultiViewFrame implements ActionListener, INMessageReceiver
{
	private static final long serialVersionUID = 2788834485599638868L;
	    
    static final private String PREVIOUS = "previous";
    static final private String UP = "up";
    static final private String NEXT = "next";
	    
    private INScatterPlot plotter=null;
	private INHoopConsole console=null;
	private INHoopPropertyPanel propPanel=null;
	//private INStreamedSocket brokerConnection=null;
			
	/**
	 *
	 */	
    public INHoopFrame() 
    {                
    	debug ("INHoopFrame ()");
    	    
        buildMenus();       
        addButtons (this.getToolBar());
        
       	//brokerConnection=new INStreamedSocket ();
       	//brokerConnection.sendAndKeepOpen("127.0.0.1",8080,"<register client=\"ui\" />",this);
    }
	/**
	 *
	 */	
    protected void buildMenus() 
    {
        JMenuBar mBar = getMainMenuBar();
        mBar.setOpaque(true);
        
        JMenu file = buildFileMenu();
        JMenu edit = buildEditMenu();
        JMenu views = buildViewsMenu();
        JMenu run = buildRunMenu();
        JMenu tools = buildToolsMenu();
        JMenu help = buildHelpMenu();
        
        mBar.add(file);
        mBar.add(edit);
        mBar.add(tools);
        mBar.add(run);
        mBar.add(views);
        mBar.add(help);        
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
    	JMenu views = new JMenu("Window");

    	JMenuItem documentItem = new JMenuItem("Document Viewer");

    	documentItem.addActionListener(new ActionListener() 
    	{
    		public void actionPerformed(ActionEvent e) 
    		{
    			addView ("Document",new INHoopDocumentViewer(),"right");
    		}
    	});
    	
    	JMenuItem documentListItem=new JMenuItem("Document Set Viewer");    	
    	
    	documentListItem.addActionListener(new ActionListener() 
    	{
    		public void actionPerformed(ActionEvent e) 
    		{
    			addView ("Document List",new INHoopDocumentList(),"right");
    		}
    	});    	
    	
    	JMenuItem consoleItem=new JMenuItem("Console Output");    	
    	
    	consoleItem.addActionListener(new ActionListener() 
    	{
    		public void actionPerformed(ActionEvent e) 
    		{
    	    	console=new INHoopConsole();
    	    	
    	    	addView ("Console",console,bottom);    	    	
    		}
    	});    
    	
    	JMenuItem plotterItem=new JMenuItem("Main Data Plotter");    	
    	
    	plotterItem.addActionListener(new ActionListener() 
    	{
    		public void actionPerformed(ActionEvent e) 
    		{
    	    	plotter=new INScatterPlot ();
    	    	
    	    	addView ("Plotter",plotter,bottom);    	    	
    		}
    	});        	
    	
    	JMenuItem propertiesItem=new JMenuItem("Properties");    	
    	
    	propertiesItem.addActionListener(new ActionListener() 
    	{
    		public void actionPerformed(ActionEvent e) 
    		{
    	    	propPanel=new INHoopPropertyPanel();
    	    	
    	    	addView ("Properties",propPanel,right);    	    	
    		}
    	});
    	
    	JMenuItem statsItem=new JMenuItem("Statistics");    	
    	
    	statsItem.addActionListener(new ActionListener() 
    	{
    		public void actionPerformed(ActionEvent e) 
    		{
    	    	INHoopStatistics statsPanel=new INHoopStatistics ();
    	    	
    	    	addView ("Statistics",statsPanel,bottom);    	    	
    		}
    	});
    	    	
    	views.add (documentItem);
    	views.add (documentListItem);
    	views.add (consoleItem);
    	views.add (statsItem);
    	views.add (plotterItem);
    	views.add (new JSeparator());
    	views.add (propertiesItem);
    	
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
    	JMenuItem reporterItem = new JMenuItem("Reporter");
    	JMenuItem hoopEditorItem = new JMenuItem("Hoop Editor");    	
    	JMenuItem jobListItem = new JMenuItem("Hadoop Jobs");
    	JMenuItem stopWordItem = new JMenuItem("Stopword Editor");
    	JMenuItem vocabularyItem = new JMenuItem("Vocabulary Editor");
    	JMenuItem opSpaceItem = new JMenuItem("Narrative Opportunity Space Visualizer");

    	searchItem.addActionListener(new ActionListener() 
    	{
    		public void actionPerformed(ActionEvent e) 
    		{
    			addView ("Search",new INHoopSearch (),center);
    		}
    	});
   	
    	clusterItem.addActionListener(new ActionListener() 
    	{
    		public void actionPerformed(ActionEvent e) 
    		{
    			addView ("Cluster",new INHoopCluster (),center);
    		}
    	});
   	
    	experimentItem.addActionListener(new ActionListener() 
    	{
    		public void actionPerformed(ActionEvent e) 
    		{
    			addView ("Experimenter",new INHoopExperimenter (),center);
    		}
    	});
    	
    	reporterItem.addActionListener(new ActionListener() 
    	{
    		public void actionPerformed(ActionEvent e) 
    		{
    			addView ("Reporter",new INHoopReporter (),bottom);
    		}
    	});    	
    	
    	hoopEditorItem.addActionListener(new ActionListener() 
    	{
    		public void actionPerformed(ActionEvent e) 
    		{
    			addView ("Hoop Editor",new INHoopEditor (),center);
    		}
    	});     	
    	
    	jobListItem.addActionListener(new ActionListener() 
    	{
    		public void actionPerformed(ActionEvent e) 
    		{
    			addView ("Hadoop Jobs",new INHoopJobList (),right);
    		}
    	});    	
    	
    	stopWordItem.addActionListener(new ActionListener() 
    	{
    		public void actionPerformed(ActionEvent e) 
    		{
    			addView ("Stop Words",new INHoopStopWordEditor (),left);
    		}
    	});     	    	

    	vocabularyItem.addActionListener(new ActionListener() 
    	{
    		public void actionPerformed(ActionEvent e) 
    		{
    			addView ("Vocabulary",new INHoopVocabularyEditor (),left);
    		}
    	});      	
    	
    	opSpaceItem.addActionListener(new ActionListener() 
    	{
    		public void actionPerformed(ActionEvent e) 
    		{
    			addView ("Opportunity Space",new INHoopOpportunitySpace (),center);
    		}
    	});    	
    	
    	tools.add (searchItem);
    	tools.add (clusterItem);
    	tools.add (experimentItem);
    	tools.add (reporterItem);
    	tools.add (hoopEditorItem);
    	tools.add (jobListItem);
    	tools.add (stopWordItem);
    	tools.add (vocabularyItem);
    	tools.add (opSpaceItem);
    	
    	return (tools);
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
    protected JMenu buildRunMenu() 
    {
    	JMenu runMenu = new JMenu("Run");
    	JMenuItem runItem = new JMenuItem("Run");
    	JMenuItem debugItem = new JMenuItem("Debug");

    	runItem.addActionListener(new ActionListener() 
    	{
    		public void actionPerformed(ActionEvent e) 
    		{
    			debug ("Run ...");
    		}
    	});

    	debugItem.addActionListener(new ActionListener() 
    	{
    		public void actionPerformed(ActionEvent e) 
    		{
    			debug ("Debug ...");
    		}
    	});

       runMenu.add(runItem);
       runMenu.add(debugItem);

       return runMenu;
    }    
	/**
	 *
	 */    
    protected void addButtons(JToolBar toolBar) 
    {
        JButton button = null;

        //first button
        button = makeNavigationButton("Back24", 
        							  PREVIOUS,
                                      "Back to previous something-or-other",
                                      "Previous",
                                      INHoopLink.imageIcons [44]);        
        
        toolBar.add(button);
        
        //first button
        button = makeNavigationButton("Back24", 
        							  PREVIOUS,
                                      "Back to previous something-or-other",
                                      "Previous",
                                      INHoopLink.imageIcons [16]);
        toolBar.add(button);

        //second button
        button = makeNavigationButton("Up24", 
        							  UP,
                                      "Up to something-or-other",
                                      "Up",
                                      INHoopLink.imageIcons [55]);
        toolBar.add(button);

        //third button
        button = makeNavigationButton("Forward24", 
        							  NEXT,
                                      "Forward to something-or-other",
                                      "Next",
                                      INHoopLink.imageIcons [56]);
        toolBar.add (button);
        
        JSeparator sep=new JSeparator(SwingConstants.VERTICAL);
        sep.setMinimumSize(new Dimension (5,5));
        sep.setMaximumSize(new Dimension (5,50));
        
        toolBar.add(sep);
        
        //third button
        button = makeNavigationButton("Forward24", 
        							  NEXT,
                                      "Forward to something-or-other",
                                      "Next",
                                      INHoopLink.imageIcons [2]);
        toolBar.add (button);
        
        //third button
        button = makeNavigationButton("Forward24", 
        							  NEXT,
                                      "Forward to something-or-other",
                                      "Next",
                                      INHoopLink.imageIcons [69]);
        toolBar.add (button);        
    }     
	/**
	 *
	 */	
    public void openHelpWindow() 
    {
    	/*
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
    	*/
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
	@Override
	public void handleIncomingData(String data) 
	{
		// TODO Auto-generated method stub
		
	}
	/**
	 *
	 */	
	@Override
	public void handleConnectionClosed() 
	{
		// TODO Auto-generated method stub
		
	}      
}
