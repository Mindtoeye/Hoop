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
import java.io.File;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import edu.cmu.cs.in.base.INHoopLink;
import edu.cmu.cs.in.controls.INScatterPlot;
//import edu.cmu.cs.in.hoop.editor.INHoopEditorToolBar;
import edu.cmu.cs.in.hoop.project.INHoopProject;
import edu.cmu.cs.in.hoop.properties.INHoopPropertyPanel;
import edu.cmu.cs.in.network.INMessageReceiver;

/** 
 * @author vvelsen
 *
 */
public class INHoopMainFrame extends INHoopMultiViewFrame implements ActionListener, INMessageReceiver
{
	private static final long serialVersionUID = -1L;

    private JFileChooser fc=null;
	
    static final private String PREVIOUS = "previous";
	    
    private INScatterPlot plotter=null;
	private INHoopConsole console=null;
	private INHoopPropertyPanel propPanel=null;
	
	private Component compReference=null;
			
	/**
	 *
	 */	
    public INHoopMainFrame() 
    {                
    	setClassName ("INHoopMainFrame");
    	debug ("INHoopMainFrame ()");
    	
    	compReference=this;
    	
    	ArrayList <String> profileLocations=INHoopLink.environment.getProfileLocation();
    	
    	if (profileLocations.size()==0)
    	{
    		debug ("Error: unable to figure out where the profile location should be");
    	}
    	else
    		debug ("Determined profile directory to be: " + profileLocations.get(0));
    	
		fc = new JFileChooser();
		FileNameExtensionFilter filter=new FileNameExtensionFilter (".xml rule files", "xml");
		fc.setFileFilter(filter);    	
    	    
        buildMenus();       
        
        addButtons (this.getToolBar());
                        
        startEditor ();
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
        JMenu project = buildProjectMenu();
        JMenu run = buildRunMenu();
        JMenu tools = buildToolsMenu();
        JMenu help = buildHelpMenu();
        
        mBar.add(file);
        mBar.add(edit);
        mBar.add(tools);
        mBar.add(project);
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
    	
    	//>------------------------------------------------------
    	
    	JMenuItem newFile = new JMenuItem("New");
    	JMenuItem open = new JMenuItem("Open File");
    	
    	newFile.addActionListener(new ActionListener() 
    	{
    		public void actionPerformed(ActionEvent e) 
    		{
    			if (INHoopLink.project!=null)
    			{
    				debug ("We already have an open project!");
    				
    				Object[] options = {"Yes","No","Cancel"};
    	           	int n = JOptionPane.showOptionDialog (compReference,
    	           										  "You already have a project open, save and close this project first?",
    	           										  "Hoop Info Panel",
    	           										  JOptionPane.YES_NO_CANCEL_OPTION,
    	           										  JOptionPane.QUESTION_MESSAGE,
    	           										  null,
    	           										  options,
    	           										  options[2]);
    	           	
    	           	if (n==0)
    	           	{          	
    	           		debug ("Saving project ...");
    	           		
    	           		INHoopLink.project.save();
    	           	}
    	           	
    	           	if (n==2)
    	           	{
    	           		debug ("Aborting creating new project");
    	           		return;
    	           	}
    			}
    			
    			FileNameExtensionFilter filter=new FileNameExtensionFilter ("Target Directories", "Directories");
    			fc.setFileFilter(filter);    			
    			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    			
    			int returnVal=fc.showOpenDialog (compReference);

    			if (returnVal==JFileChooser.APPROVE_OPTION) 
    			{
    				Object[] options = {"Yes","No","Cancel"};
    	           	int n = JOptionPane.showOptionDialog (compReference,
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

    	           		debug ("Creating in directory: " + file.getAbsolutePath() + " ...");
    	                   	           		
    	           		INHoopLink.project=new INHoopProject ();
    	           		if (INHoopLink.project.newProject (file.getAbsolutePath()))
    	           		{
    	           			debug ("Error creating project!");
    	           			
    	           		}
    	           	}	
    			} 
    			else 
    			{
    				debug ("Open command cancelled by user.");
    			}
    		}
    	});

    	open.addActionListener(new ActionListener() 
    	{
    		public void actionPerformed(ActionEvent e) 
    		{
    			if (INHoopLink.project!=null)
    			{
    				debug ("We already have an open project!");
    				
    				Object[] options = {"Yes","No","Cancel"};
    	           	int n = JOptionPane.showOptionDialog (compReference,
    	           										  "You already have a project open, save and close this project first?",
    	           										  "Hoop Info Panel",
    	           										  JOptionPane.YES_NO_CANCEL_OPTION,
    	           										  JOptionPane.QUESTION_MESSAGE,
    	           										  null,
    	           										  options,
    	           										  options[2]);
    	           	
    	           	if (n==0)
    	           	{          	
    	           		debug ("Saving project ...");
    	           		
    	           		INHoopLink.project.save();
    	           	}
    	           	
    	           	if (n==2)
    	           	{
    	           		debug ("Aborting creating new project");
    	           		return;
    	           	}
    			}    			
    			
    			FileNameExtensionFilter filter=new FileNameExtensionFilter (".hprj project files", "hprj");
    			fc.setFileFilter(filter); 
    			fc.setFileSelectionMode (JFileChooser.FILES_ONLY);
    			
    			int returnVal=fc.showOpenDialog (compReference);

    			if (returnVal==JFileChooser.APPROVE_OPTION) 
    			{
    				Object[] options = {"Yes","No","Cancel"};
    	           	int n = JOptionPane.showOptionDialog (compReference,
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

    	           		debug ("Loading: " + file.getAbsolutePath() + " ...");
    	               
    	           		
    	           	}	
    			} 
    			else 
    			{
    				debug ("Open command cancelled by user.");
    			}
    			
    		}
    	});    	
    	
    	//>------------------------------------------------------
    	
    	JMenuItem save = new JMenuItem("Save",INHoopLink.getImageByName("save.gif"));
    	JMenuItem saveas = new JMenuItem("Save As ...",INHoopLink.getImageByName("saveas.gif"));
    	JMenuItem saveall = new JMenuItem("Save All",INHoopLink.getImageByName("save.gif"));
    	JMenuItem revert = new JMenuItem("Revert",INHoopLink.getImageByName("undo.gif"));
    	
    	save.addActionListener(new ActionListener() 
    	{
    		public void actionPerformed(ActionEvent e) 
    		{
    			debug ("Save ...");
    		
    			INHoopProject proj=INHoopLink.project;
    			
    			if (proj==null)
    			{
    				debug ("Internal error: no project available");
    				return;
    			}
    			
    			if (proj.getVirginFile()==true)
    			{
        			FileNameExtensionFilter filter=new FileNameExtensionFilter ("Target Directories", "Directories");
        			fc.setFileFilter(filter);    			
        			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        			
        			int returnVal=fc.showOpenDialog (compReference);

        			if (returnVal==JFileChooser.APPROVE_OPTION) 
        			{
        	           	File file = fc.getSelectedFile();

        	           	debug ("Creating in directory: " + file.getAbsolutePath() + " ...");
        	                   	           		
        	           	INHoopLink.project.setFileURI(file.getAbsolutePath()+"/.hprj");
        	           		
        	           	proj.save();	
        			} 
        			else 
        			{
        				debug ("Open command cancelled by user.");
        				return;
        			}    				
    			}
    			else
    			{
    				proj.save();
    			}	
    		}
    	});
    	
    	saveas.addActionListener(new ActionListener() 
    	{
    		public void actionPerformed(ActionEvent e) 
    		{
    			debug ("SaveAs ...");

    		}
    	});
    	
    	saveall.addActionListener(new ActionListener() 
    	{
    		public void actionPerformed(ActionEvent e) 
    		{
    			debug ("SaveAll ...");

    		}
    	});
    	
    	revert.addActionListener(new ActionListener() 
    	{
    		public void actionPerformed(ActionEvent e) 
    		{
    			debug ("Revert ...");

    		}
    	});    	
    	
    	//>------------------------------------------------------    	
    	
    	JMenuItem imp = new JMenuItem("Import ...");
    	
    	imp.addActionListener(new ActionListener() 
    	{
    		public void actionPerformed(ActionEvent e) 
    		{
    			INHoopProject proj=INHoopLink.project;
    			
    			if (proj==null)
    			{
    				debug ("Internal error: no project available");
    				return;
    			}
    			
    			if (proj.getVirginFile()==true)
    			{
    				alert ("Please save your project first");
    				return;
    			}
    		}
    	});    	
    	
    	JMenuItem exp = new JMenuItem("Export ...");
    	exp.setEnabled(false);
    	
    	exp.addActionListener(new ActionListener() 
    	{
    		public void actionPerformed(ActionEvent e) 
    		{
    		
    		}
    	});    	
    	
    	//>------------------------------------------------------    	
    	
    	JMenuItem props = new JMenuItem("Properties");
    	
    	//>------------------------------------------------------
    	
    	JMenuItem quit = new JMenuItem("Exit");
    	    	
    	quit.addActionListener(new ActionListener() 
    	{
    		public void actionPerformed(ActionEvent e) 
    		{
    			quit();
    		}
    	});
    	
    	//>------------------------------------------------------

    	file.add(newFile);
    	file.add(open);
    	file.addSeparator();
    	file.add(save);
    	file.add(saveas);
    	file.add(saveall);
    	file.add(revert);
    	file.addSeparator();
    	file.add(imp);
    	file.add(exp);
    	file.addSeparator();
    	file.add(props);   
    	file.addSeparator();    	
    	file.add(quit);
    	
    	return (file);
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
    	
    	return (edit);
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
    	    	
    	    	addView ("Console",console,INHoopLink.bottom);    	    	
    		}
    	});
    	
    	JMenuItem errorItem=new JMenuItem("Error Output");    	
    	
    	errorItem.addActionListener(new ActionListener() 
    	{
    		public void actionPerformed(ActionEvent e) 
    		{    	    
    	    	addView ("Errors",new INHoopErrorPanel(),"bottom");
    		}
    	});    	
    	
    	JMenuItem plotterItem=new JMenuItem("Main Data Plotter");    	
    	
    	plotterItem.addActionListener(new ActionListener() 
    	{
    		public void actionPerformed(ActionEvent e) 
    		{
    	    	plotter=new INScatterPlot ();
    	    	
    	    	addView ("Plotter",plotter,INHoopLink.bottom);    	    	
    		}
    	});        	
    	
    	JMenuItem propertiesItem=new JMenuItem("Properties");    	
    	
    	propertiesItem.addActionListener(new ActionListener() 
    	{
    		public void actionPerformed(ActionEvent e) 
    		{
    	    	propPanel=new INHoopPropertyPanel();
    	    	
    	    	addView ("Properties",propPanel,INHoopLink.right);    	    	
    		}
    	});
    	
    	JMenuItem statsItem=new JMenuItem("Statistics");    	
    	
    	statsItem.addActionListener(new ActionListener() 
    	{
    		public void actionPerformed(ActionEvent e) 
    		{
    	    	INHoopStatistics statsPanel=new INHoopStatistics ();
    	    	
    	    	addView ("Statistics",statsPanel,INHoopLink.bottom);    	    	
    		}
    	});
    	    	
    	views.add (documentItem);
    	views.add (documentListItem);
    	views.add (consoleItem);
    	views.add (errorItem);
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
    	//JMenuItem stopWordItem = new JMenuItem("Stopword Editor");
    	JMenuItem vocabularyItem = new JMenuItem("Vocabulary Editor");
    	JMenuItem opSpaceItem = new JMenuItem("Narrative Opportunity Space Visualizer");
    	//JMenuItem hexMapItem = new JMenuItem("Hexagon Map");

    	searchItem.addActionListener(new ActionListener() 
    	{
    		public void actionPerformed(ActionEvent e) 
    		{
    			addView ("Search",new INHoopSearch (),INHoopLink.center);
    		}
    	});
   	
    	clusterItem.addActionListener(new ActionListener() 
    	{
    		public void actionPerformed(ActionEvent e) 
    		{
    			addView ("Cluster",new INHoopCluster (),INHoopLink.center);
    		}
    	});
   	
    	experimentItem.addActionListener(new ActionListener() 
    	{
    		public void actionPerformed(ActionEvent e) 
    		{
    			addView ("Experimenter",new INHoopExperimenter (),INHoopLink.center);
    		}
    	});
    	
    	reporterItem.addActionListener(new ActionListener() 
    	{
    		public void actionPerformed(ActionEvent e) 
    		{
    			addView ("Reporter",new INHoopReporter (),INHoopLink.bottom);
    		}
    	});    	
    	
    	hoopEditorItem.addActionListener(new ActionListener() 
    	{
    		public void actionPerformed(ActionEvent e) 
    		{    			    			
    			startEditor ();
    		}
    	});     	
    	
    	jobListItem.addActionListener(new ActionListener() 
    	{
    		public void actionPerformed(ActionEvent e) 
    		{
    			addView ("Hadoop Jobs",new INHoopJobList (),INHoopLink.right);
    		}
    	});    	
    	
    	/*
    	stopWordItem.addActionListener(new ActionListener() 
    	{
    		public void actionPerformed(ActionEvent e) 
    		{
    			addView ("Stop Words",new INHoopStopWordEditor (),INHoopLink.left);
    		}
    	});
    	*/     	    	

    	vocabularyItem.addActionListener(new ActionListener() 
    	{
    		public void actionPerformed(ActionEvent e) 
    		{
    			addView ("Vocabulary",new INHoopVocabularyEditor (),INHoopLink.left);
    		}
    	});      	
    	
    	/*
    	opSpaceItem.addActionListener(new ActionListener() 
    	{
    		public void actionPerformed(ActionEvent e) 
    		{
    			addView ("Opportunity Space",new INHoopOpportunitySpace (),INHoopLink.center);
    		}
    	});
    	*/
    	
    	/*
    	hexMapItem.addActionListener(new ActionListener() 
    	{
    		public void actionPerformed(ActionEvent e) 
    		{
    			addView ("Hexagon Map",new INHoopJava3DJPanel (),INHoopLink.center);
    		}
    	});
    	*/
    	    	    	
    	tools.add (searchItem);
    	tools.add (clusterItem);
    	tools.add (experimentItem);
    	tools.add (reporterItem);
    	tools.add (hoopEditorItem);
    	tools.add (jobListItem);
    	//tools.add (stopWordItem);
    	tools.add (vocabularyItem);
    	tools.add (opSpaceItem);
    	//tools.add (hexMapItem);
    	
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
    protected JMenu buildProjectMenu() 
    {
    	JMenu project = new JMenu("Project");
    	JMenuItem buildItem = new JMenuItem("Build");
    	JMenuItem cleanItem = new JMenuItem("Clean");
    	JMenuItem propertiesItem = new JMenuItem("Properties");

    	buildItem.addActionListener(new ActionListener() 
    	{
    		public void actionPerformed(ActionEvent e) 
    		{
    			// Fill in later
    		}
    	});

    	cleanItem.addActionListener(new ActionListener() 
    	{
    		public void actionPerformed(ActionEvent e) 
    		{
    			// Fill in later
    		}
    	});

    	propertiesItem.addActionListener(new ActionListener() 
    	{
    		public void actionPerformed(ActionEvent e) 
    		{
    			// Fill in later
    		}
    	});

    	project.add(buildItem);
    	project.add(cleanItem);
    	project.addSeparator();
    	project.add(propertiesItem);
   	
    	return (project);
    }    
	/**
	 *
	 */	
    protected JMenu buildRunMenu() 
    {
    	JMenu runMenu = new JMenu("Run");
    	JMenuItem runOnceItem = new JMenuItem("Run Once");
    	runOnceItem.setIcon(INHoopLink.getImageByName("run-once.png"));
    	
    	JMenuItem runNTimesItem = new JMenuItem("Run N Times");
    	runNTimesItem.setIcon(INHoopLink.getImageByName("run-n.png"));
    	
    	JMenuItem runForeverItem = new JMenuItem("Run Until Stopped");
    	runForeverItem.setIcon(INHoopLink.getImageByName("run-forever.png"));
    	
    	JMenuItem debugItem = new JMenuItem("Debug");

    	runOnceItem.addActionListener(new ActionListener() 
    	{
    		public void actionPerformed(ActionEvent e) 
    		{
    			debug ("Run Once ...");
    			    			
    			INHoopExecuteInEditor runtime=new INHoopExecuteInEditor ();
    			runtime.setRoot(INHoopLink.hoopGraphManager.getRoot());
    			runtime.setLoopCount(1);
    			
    			new Thread(runtime).start();    			
    		}
    	});
    	
    	runNTimesItem.addActionListener(new ActionListener() 
    	{
    		public void actionPerformed(ActionEvent e) 
    		{
    			debug ("Run N Times ...");
    			    			
    			INHoopExecuteInEditor runtime=new INHoopExecuteInEditor ();
    			runtime.setRoot(INHoopLink.hoopGraphManager.getRoot());
    			runtime.setLoopCount(10);
    			
    			new Thread(runtime).start();    			
    		}
    	});
    	
    	runForeverItem.addActionListener(new ActionListener() 
    	{
    		public void actionPerformed(ActionEvent e) 
    		{
    			debug ("Run Forever ...");
    			    			
    			INHoopExecuteInEditor runtime=new INHoopExecuteInEditor ();
    			runtime.setRoot(INHoopLink.hoopGraphManager.getRoot());
    			runtime.setLoopCount(-1);
    			
    			new Thread(runtime).start();    			
    		}
    	});    	

    	debugItem.addActionListener(new ActionListener() 
    	{
    		public void actionPerformed(ActionEvent e) 
    		{
    			debug ("Debug ...");
    		}
    	});

       runMenu.add(runOnceItem);
       runMenu.add(runNTimesItem);
       runMenu.add(runForeverItem);
       runMenu.add (new JSeparator());
       runMenu.add(debugItem);

       return runMenu;
    }    
	/**
	 *
	 */    
    protected void addButtons(JToolBar toolBar) 
    {
        JButton runButton = makeNavigationButton("RUN", 
        							  			 PREVIOUS,
        							  			 "Run Once",
        							  			 "Run Once",
        							  			 INHoopLink.getImageByName("run-once.png"));                
        toolBar.add(runButton);
        
        JButton runNButton = makeNavigationButton("RUN N", 
	  			 								  PREVIOUS,
	  			 								  "Run N Times",
	  			 								  "Run N Times",
	  			 								  INHoopLink.getImageByName("run-n.png"));                
        toolBar.add(runNButton);

        JButton runForeverButton = makeNavigationButton("RUN FOREVER", 
        												PREVIOUS,
        												"Run Forever",
        												"Run Forever",
        												INHoopLink.getImageByName("run-forever.png"));                
        toolBar.add(runForeverButton);
        
        
        JSeparator sep=new JSeparator(SwingConstants.VERTICAL);
        sep.setMinimumSize(new Dimension (5,5));
        sep.setMaximumSize(new Dimension (5,50));
        
        toolBar.add(sep);                
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
	/**
	 * 
	 */
	private void startEditor ()
	{
		debug ("startEditor ()");
		
    	console=new INHoopConsole();    	
    	addView ("Console",console,INHoopLink.bottom);  		
		
    	INHoopProjectPanel projectPanel=new INHoopProjectPanel ();
    	addView ("Project",projectPanel,INHoopLink.left);
    	
		INHoopTreeList hoopList=new INHoopTreeList ();
		addView ("Hoop List",hoopList,INHoopLink.left);
		
    	propPanel=new INHoopPropertyPanel();
    	
    	addView ("Properties",propPanel,INHoopLink.right);
		
		INHoopGraphEditor editor=new INHoopGraphEditor ();
		addView ("Hoop Editor",editor,INHoopLink.center);
		
		INHoopLink.project=new INHoopProject ();
		INHoopLink.project.newProject ();
		
		/*
		INHoopLink.menuBar.create(editor);
		
		INHoopLink.toolEditorBar=new INHoopEditorToolBar ();
		INHoopLink.toolBoxContainer.add (INHoopLink.toolEditorBar,1);    		
		INHoopLink.toolEditorBar.create(editor,JToolBar.HORIZONTAL);
		*/
		
		projectPanel.updateContents();
	}
}
