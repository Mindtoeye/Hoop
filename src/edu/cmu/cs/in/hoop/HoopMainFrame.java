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

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.mxgraph.model.mxCell;
import com.mxgraph.view.mxGraph;

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.controls.HoopSentenceWall;
//import edu.cmu.cs.in.controls.HoopSentinetPanel;
//import edu.cmu.cs.in.controls.base.HoopEmbeddedJPanel;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.hoop.hoops.task.HoopStart;
import edu.cmu.cs.in.hoop.project.HoopGraphFile;
import edu.cmu.cs.in.hoop.project.HoopProject;
import edu.cmu.cs.in.hoop.project.HoopWrapperFile;
//import edu.cmu.cs.in.hoop.project.HoopProjectFile;
import edu.cmu.cs.in.hoop.properties.HoopPropertyPanel;
import edu.cmu.cs.in.hoop.visualizers.HoopCluster;
import edu.cmu.cs.in.hoop.visualizers.HoopScatterPlot;
import edu.cmu.cs.in.network.HoopMessageReceiver;

/** 
 * @author vvelsen
 *
 */
public class HoopMainFrame extends HoopMultiViewFrame implements ActionListener, HoopMessageReceiver
{
	private static final long serialVersionUID = -1L;

    //private JFileChooser fc=null;
	
    static final private String PREVIOUS = "previous";
	    
    private HoopScatterPlot plotter=null;
	private HoopConsole console=null;
	private HoopPropertyPanel propPanel=null;
	
	private Component compReference=null;
			
	/**
	 *
	 */	
    public HoopMainFrame() 
    {                
    	setClassName ("HoopMainFrame");
    	debug ("HoopMainFrame ()");
    	
    	compReference=this;
    	    	
		//fc = new JFileChooser();
		//FileNameExtensionFilter filter=new FileNameExtensionFilter (".xml rule files", "xml");
		//fc.setFileFilter(filter);    	
    	    
        buildMenus();       
        
        addButtons (this.getToolBar());
                        
        startEditor ();
    }
    /**
     * 
     */
    private void updateProjectViews ()
    {
    	debug ("updateProjectViews ()");
    	
    	HoopProjectPanel projWindow=(HoopProjectPanel) HoopLink.getWindow("Project");
    	if (projWindow!=null)
    	{
    		projWindow.updateContents();
    	}
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
    	
    	JMenuItem newFile = new JMenuItem("New Project");
    	JMenuItem open = new JMenuItem("Open Project");
    	
    	newFile.addActionListener(new ActionListener() 
    	{
    		public void actionPerformed(ActionEvent e) 
    		{
    			newProject ();
    		}
    	});

    	open.addActionListener(new ActionListener() 
    	{
    		public void actionPerformed(ActionEvent e) 
    		{
    			openProject ();    			
    		}
    	});    	
    	
    	//>------------------------------------------------------
    	
    	JMenuItem save = new JMenuItem("Save",HoopLink.getImageByName("save.gif"));
    	
    	JMenuItem saveas = new JMenuItem("Save As ...",HoopLink.getImageByName("saveas.gif"));
    	
    	JMenuItem saveall = new JMenuItem("Save All",HoopLink.getImageByName("save.gif"));
    	saveall.setEnabled(false);
    	
    	JMenuItem revert = new JMenuItem("Revert",HoopLink.getImageByName("undo.gif"));
    	revert.setEnabled(false);
    	
    	save.addActionListener(new ActionListener() 
    	{
    		public void actionPerformed(ActionEvent e) 
    		{
    			debug ("Save ...");
    		
    			projectSave ();
    		}
    	});
    	
    	saveas.addActionListener(new ActionListener() 
    	{
    		public void actionPerformed(ActionEvent e) 
    		{
    			debug ("SaveAs ...");
    		        		
    			projectSaveAs ();
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
    	//imp.setEnabled(false);
    	
    	imp.addActionListener(new ActionListener() 
    	{
    		public void actionPerformed(ActionEvent e) 
    		{
    			importFiles ();
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
    	props.setEnabled(false);
    	
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
    			addView ("Document",new HoopDocumentViewer(),"right");
    		}
    	});
    	
    	JMenuItem documentListItem=new JMenuItem("Document Set Viewer");    	
    	
    	documentListItem.addActionListener(new ActionListener() 
    	{
    		public void actionPerformed(ActionEvent e) 
    		{
    			addView ("Document List",new HoopDocumentList(),"right");
    		}
    	});    	
    	
    	JMenuItem consoleItem=new JMenuItem("Console Output");    	
    	
    	consoleItem.addActionListener(new ActionListener() 
    	{
    		public void actionPerformed(ActionEvent e) 
    		{
    	    	console=new HoopConsole();
    	    	
    	    	addView ("Console",console,HoopLink.bottom);    	    	
    		}
    	});
    	
    	JMenuItem errorItem=new JMenuItem("Error Output");    	
    	
    	errorItem.addActionListener(new ActionListener() 
    	{
    		public void actionPerformed(ActionEvent e) 
    		{    	    
    	    	addView ("Errors",new HoopErrorPanel(),"bottom");
    		}
    	});    	
    	
    	JMenuItem plotterItem=new JMenuItem("Main Data Plotter");    	
    	
    	plotterItem.addActionListener(new ActionListener() 
    	{
    		public void actionPerformed(ActionEvent e) 
    		{
    	    	plotter=new HoopScatterPlot ();
    	    	
    	    	addView ("Plotter",plotter,HoopLink.bottom);    	    	
    		}
    	});        	
    	
    	JMenuItem propertiesItem=new JMenuItem("Properties");    	
    	
    	propertiesItem.addActionListener(new ActionListener() 
    	{
    		public void actionPerformed(ActionEvent e) 
    		{
    	    	propPanel=new HoopPropertyPanel();
    	    	
    	    	addView ("Properties",propPanel,HoopLink.right);    	    	
    		}
    	});
    	
    	JMenuItem statsItem=new JMenuItem("Statistics");    	
    	
    	statsItem.addActionListener(new ActionListener() 
    	{
    		public void actionPerformed(ActionEvent e) 
    		{
    	    	HoopStatistics statsPanel=new HoopStatistics ();
    	    	
    	    	addView ("Statistics",statsPanel,HoopLink.bottom);    	    	
    		}
    	});
    	
    	JMenuItem sWallItem=new JMenuItem("Sentence Wall");    	
    	
    	sWallItem.addActionListener(new ActionListener() 
    	{
    		public void actionPerformed(ActionEvent e) 
    		{
    			HoopSentenceWall sWallPanel=new HoopSentenceWall ();
    	    	
    	    	addView ("Sentence Wall",sWallPanel,HoopLink.right);    	    	
    		}
    	});
    	
    	JMenuItem sTextViewItem=new JMenuItem("Text Viewer");    	
    	
    	sTextViewItem.addActionListener(new ActionListener() 
    	{
    		public void actionPerformed(ActionEvent e) 
    		{
    			HoopTextViewer sTextViewPanel=new HoopTextViewer ();
    	    	
    	    	addView ("Text Viewer",sTextViewPanel,HoopLink.center);    	    	
    		}
    	});    	
    	    	    	
    	views.add (sTextViewItem);
    	views.add (documentItem);
    	views.add (documentListItem);
    	views.add (consoleItem);
    	views.add (errorItem);
    	views.add (statsItem);
    	views.add (plotterItem);
    	views.add (new JSeparator());
    	views.add (propertiesItem);
    	views.add (sWallItem);
    	
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
    	JMenuItem sentItem = new JMenuItem("Sentinet Panel");

    	searchItem.addActionListener(new ActionListener() 
    	{
    		public void actionPerformed(ActionEvent e) 
    		{
    			addView ("Search",new HoopSearch (),HoopLink.center);
    		}
    	});
   	
    	clusterItem.addActionListener(new ActionListener() 
    	{
    		public void actionPerformed(ActionEvent e) 
    		{
    			addView ("Cluster",new HoopCluster (),HoopLink.center);
    		}
    	});
   	
    	experimentItem.addActionListener(new ActionListener() 
    	{
    		public void actionPerformed(ActionEvent e) 
    		{
    			addView ("Experimenter",new HoopExperimenter (),HoopLink.center);
    		}
    	});
    	
    	reporterItem.addActionListener(new ActionListener() 
    	{
    		public void actionPerformed(ActionEvent e) 
    		{
    			addView ("Reporter",new HoopReporter (),HoopLink.bottom);
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
    			addView ("Hadoop Jobs",new HoopJobList (),HoopLink.right);
    		}
    	});    	
    	
    	/*
    	stopWordItem.addActionListener(new ActionListener() 
    	{
    		public void actionPerformed(ActionEvent e) 
    		{
    			addView ("Stop Words",new HoopStopWordEditor (),HoopLink.left);
    		}
    	});
    	*/     	    	

    	vocabularyItem.addActionListener(new ActionListener() 
    	{
    		public void actionPerformed(ActionEvent e) 
    		{
    			addView ("Vocabulary",new HoopVocabularyEditor (),HoopLink.left);
    		}
    	});      	
    	
    	sentItem.addActionListener (new ActionListener ()
    	{    		    		
    		public void actionPerformed(ActionEvent e) 
    		{
    			//addView ("Sentinet",new HoopSentinetPanel (),HoopLink.center);
    		}    		
    	});
    	
    	/*
    	opSpaceItem.addActionListener(new ActionListener() 
    	{
    		public void actionPerformed(ActionEvent e) 
    		{
    			addView ("Opportunity Space",new HoopOpportunitySpace (),HoopLink.center);
    		}
    	});
    	*/
    	
    	/*
    	hexMapItem.addActionListener(new ActionListener() 
    	{
    		public void actionPerformed(ActionEvent e) 
    		{
    			addView ("Hexagon Map",new HoopJava3DJPanel (),HoopLink.center);
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
    	tools.add (sentItem);
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
        		addView ("Help",new HoopHelp (),HoopLink.left);        		
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
    	runOnceItem.setIcon(HoopLink.getImageByName("run-once.png"));
    	
    	JMenuItem runNTimesItem = new JMenuItem("Run N Times");
    	runNTimesItem.setIcon(HoopLink.getImageByName("run-n.png"));
    	
    	JMenuItem runForeverItem = new JMenuItem("Run Until Stopped");
    	runForeverItem.setIcon(HoopLink.getImageByName("run-forever.png"));
    	
    	JMenuItem debugItem = new JMenuItem("Debug");

    	runOnceItem.addActionListener(new ActionListener() 
    	{
    		public void actionPerformed(ActionEvent e) 
    		{
    			debug ("Run Once ...");
    			
    			//showErrorWindow ();
    			    			
    			HoopExecuteInEditor runtime=new HoopExecuteInEditor ();
    			runtime.setRoot(HoopLink.hoopGraphManager.getRoot());
    			runtime.setLoopCount(1);
    			
    			new Thread(runtime).start();    			
    		}
    	});
    	
    	runNTimesItem.addActionListener(new ActionListener() 
    	{
    		public void actionPerformed(ActionEvent e) 
    		{
    			debug ("Run N Times ...");
    			
    			//showErrorWindow ();
    			    			    			    		
    			HoopExecuteInEditor runtime=new HoopExecuteInEditor ();
    			runtime.setRoot(HoopLink.hoopGraphManager.getRoot());
    			runtime.setLoopCount(10);
    			
    			new Thread(runtime).start();    			
    		}
    	});
    	
    	runForeverItem.addActionListener(new ActionListener() 
    	{
    		public void actionPerformed(ActionEvent e) 
    		{
    			debug ("Run Forever ...");
    			    			
    			//showErrorWindow ();
    			
    			HoopExecuteInEditor runtime=new HoopExecuteInEditor ();
    			runtime.setRoot(HoopLink.hoopGraphManager.getRoot());
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
        							  			 HoopLink.getImageByName("run-once.png"));                
        toolBar.add(runButton);
        
        JButton runNButton = makeNavigationButton("RUN N", 
	  			 								  PREVIOUS,
	  			 								  "Run N Times",
	  			 								  "Run N Times",
	  			 								  HoopLink.getImageByName("run-n.png"));                
        toolBar.add(runNButton);

        JButton runForeverButton = makeNavigationButton("RUN FOREVER", 
        												PREVIOUS,
        												"Run Forever",
        												"Run Forever",
        												HoopLink.getImageByName("run-forever.png"));                
        toolBar.add(runForeverButton);
        
        
        JSeparator sep=new JSeparator(SwingConstants.VERTICAL);
        sep.setMinimumSize(new Dimension (5,5));
        sep.setMaximumSize(new Dimension (5,50));
        
        toolBar.add(sep);                
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
		
    	console=new HoopConsole();    	
    	addView ("Console",console,HoopLink.bottom);  		
		
    	HoopProjectPanel projectPanel=new HoopProjectPanel ();
    	addView ("Project",projectPanel,HoopLink.left);
    	
		HoopTreeList hoopList=new HoopTreeList ();
		addView ("Hoop List",hoopList,HoopLink.left);
		
    	propPanel=new HoopPropertyPanel();
    	
    	addView ("Properties",propPanel,HoopLink.right);
		
		HoopGraphEditor editor=new HoopGraphEditor ();
		addView ("Hoop Editor",editor,HoopLink.center);
		
		newProjectInternal (null);
		
		/*
		HoopLink.menuBar.create(editor);
		
		HoopLink.toolEditorBar=new HoopEditorToolBar ();
		HoopLink.toolBoxContainer.add (HoopLink.toolEditorBar,1);    		
		HoopLink.toolEditorBar.create(editor,JToolBar.HORIZONTAL);
		*/
		
		projectPanel.updateContents();
	}
	/**
	 * 
	 */
	private void newProjectInternal (String aURI)
	{
		debug ("newProjectInternal ()");
		
		HoopLink.project=new HoopProject ();
		HoopLink.project.newProject (aURI);
		
		HoopGraphEditor editor=(HoopGraphEditor) HoopLink.getWindow("Hoop Editor");
		if (editor==null)
		{
			alert ("Error no graph editor available to create new start node");
			return;
		}
		
		mxGraph graph=editor.getGraph ();
		
		Object parent=graph.getDefaultParent();
		
		graph.getModel().beginUpdate();
		
		try
		{
			HoopStart startNode=new HoopStart ();
			
			mxCell graphObject=(mxCell) graph.insertVertex (parent, 
															startNode.getClassName(),
															startNode,
															20,
															20,
															startNode.getWidth(),
															startNode.getHeight());
			
			graphObject.setValue(startNode);			
		}
		finally
		{
			graph.getModel().endUpdate();
		}												
	}
	/**
	 * 
	 */
	private void showErrorWindow ()
	{
		debug ("showErrorWindow ()");
		
		HoopErrorPanel test=(HoopErrorPanel) HoopLink.getWindow("Errors");
		
		if (test==null)
		{
			addView ("Errors",new HoopErrorPanel(),"bottom");
			test=(HoopErrorPanel) HoopLink.getWindow("Errors");
		}	
		
		HoopLink.popWindow ("Errors");
	}
	/**
	 * http://www.catalysoft.com/articles/busyCursor.html
	 */
	private Boolean newProject ()
	{
		debug ("newProject ()");
		
		if (HoopLink.project!=null)
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
           		
           		HoopLink.project.save();
           	}
           	
           	if (n==2)
           	{
           		debug ("Aborting creating new project");
           		return (false);
           	}
		}
		
		JFileChooser fc = new JFileChooser();
		
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
                   	           		
           		/*
           		HoopLink.project=new HoopProject ();
           		if (HoopLink.project.newProject (file.getAbsolutePath()))
           		{
           			debug ("Error creating project!");
           			return (false);
           		}
           		*/
           		
           		newProjectInternal (file.getAbsolutePath());
           	}	
		} 
		else 
		{
			debug ("Open command cancelled by user.");
			return (false);
		}		
		
		updateProjectViews ();
		
		return (true);
	}
	/**
	 * http://www.catalysoft.com/articles/busyCursor.html
	 */
	private Boolean openProject ()
	{
		debug ("openProject");
		
		Container cp = this.getContentPane();
		
		if (HoopLink.project!=null)
		{
			HoopGraphFile graphFile=(HoopGraphFile) HoopLink.project.getFileByClass ("HoopGraphFile");
			
			if (graphFile!=null)
			{
				if (graphFile.getHoops().size()>0)
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
           		
						if (HoopLink.project.getVirginFile()==true)
	    				{
							if (projectSaveAs ()==false)
								return (false);
	    				}
						else
						{
				           	try
				           	{
				           		cp.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				           		
				           		HoopLink.project.save();
				           	}
				           	finally
				           	{
				           		cp.setCursor(Cursor.getDefaultCursor());
				           	}
						}
					}
           	
					if (n==2)
					{
						debug ("Aborting creating new project");
						return (false);
					}    					
				}	
			}	
		}    			
		
		/*
		FileNameExtensionFilter filter=new FileNameExtensionFilter (".hprj project files", "hprj");
		fc.setFileFilter(filter); 
		fc.setFileSelectionMode (JFileChooser.FILES_ONLY);
		*/
		
		JFileChooser fc = new JFileChooser();
		
		FileNameExtensionFilter filter=new FileNameExtensionFilter ("Target Directories", "Directories");
		fc.setFileFilter(filter);    			
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);    			
		
		int returnVal=fc.showOpenDialog (compReference);

		if (returnVal==JFileChooser.APPROVE_OPTION) 
		{   	
           	File file = fc.getSelectedFile();

           	debug ("Loading: " + file.getAbsolutePath() + " ...");
               
           	try
           	{
           		cp.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
           	
           		HoopLink.project=new HoopProject (); // Blatantly whipe it
           		HoopLink.project.load(file.getAbsolutePath());
           	}
           	finally
           	{
           		cp.setCursor(Cursor.getDefaultCursor());
           	}	
           		
           	// Do a ton of housekeeping here ...
           		
           	HoopGraphEditor win=(HoopGraphEditor) HoopLink.getWindow("Hoop Editor");
           		    	           		    	           		
           	if (win==null)
           	{
           		win=new HoopGraphEditor ();
           		addView ("Hoop Editor",win,HoopLink.center);
           	}
           		
           	win.reset();
           		
           	HoopGraphFile graphFile=(HoopGraphFile) HoopLink.project.getFileByClass ("HoopGraphFile");
           	if (graphFile!=null)
           	{
           		win.instantiateFromFile(graphFile);
           	}
           	else
           	{
           		alert ("Unable to find graph file in project");
           		return (false);
           	}
		} 
		else 
		{
			debug ("Open command cancelled by user.");
		}
		
		updateProjectViews ();
		
		return (true);
	}
	/**
	 * http://www.catalysoft.com/articles/busyCursor.html
	 */
	private Boolean projectSave ()
	{
		debug ("projectSave ()");
		
		Container cp = this.getContentPane();
		
		HoopProject proj=HoopLink.project;
		
		if (proj==null)
		{
			debug ("Internal error: no project available");
			return (false);
		}
		
		if (proj.getVirginFile()==true)
		{    
			try
			{
				cp.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			
				if (projectSaveAs ()==false)
				{
					cp.setCursor(Cursor.getDefaultCursor());
					return (false);
				}
			}
			finally
			{
				cp.setCursor(Cursor.getDefaultCursor());
			}
		}
		else
		{
			proj.save();
		}			
		
		return (true);
	}
	/**
	 * http://www.catalysoft.com/articles/busyCursor.html
	 */
	private Boolean projectSaveAs ()
	{
		debug ("projectSaveAs ()");
		
		Container cp = this.getContentPane();
		
		HoopProject proj=HoopLink.project;
		
		if (proj==null)
		{
			debug ("Internal error: no project available");
			return (false);
		}
		
		JFileChooser fc = new JFileChooser();
		
		FileNameExtensionFilter filter=new FileNameExtensionFilter ("Target Directories", "Directories");
		fc.setFileFilter(filter);    			
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			
		int returnVal=fc.showSaveDialog (compReference);

		if (returnVal==JFileChooser.APPROVE_OPTION) 
		{
	       	File file = fc.getSelectedFile();

	       	debug ("Creating in directory: " + file.getAbsolutePath() + " ...");
	                
	       	File testFile=new File (file.getAbsolutePath()+"/.hprj");
	       	if (testFile.exists()==true)
	       	{
	       		alert ("Error: a project already exists in that location");
	       		return (false);
	       	}
	       	else
	       	{        	       	
	       		HoopLink.project.setFileURI(file.getAbsolutePath()+"/.hprj");
	           		        	       	        	       	
	           	try
	           	{
	           		cp.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
	           		
	           		proj.save();
	           	}
	           	finally
	           	{
	           		cp.setCursor(Cursor.getDefaultCursor());
	           	}
	       	}	
		} 
		else 
		{
			debug ("Open command cancelled by user.");
			return (false);
		}	    			
		
		return (true);
	}
	/**
	 * 
	 */
	private void importFiles ()
	{
		debug ("importFiles ()");
		
		HoopProject proj=HoopLink.project;
			
		if (proj==null)
		{
			debug ("Internal error: no project available");
			return;
		}
		
		/*
		if (proj.getVirginFile()==true)
		{
			alert ("Please save your project first");
			return;
		}
		*/
		
		JFileChooser fc = new JFileChooser();
					
		int returnVal=fc.showOpenDialog (compReference);

		if (returnVal==JFileChooser.APPROVE_OPTION) 
		{
	       	File file = fc.getSelectedFile();
	       	
	       	String fromAbsolute=file.getAbsolutePath();
	       	String toRelative=proj.getBasePath()+"/"+file.getName();
	      
	       	HoopLink.fManager.copyFile(fromAbsolute, toRelative);
	       	
	       	HoopWrapperFile wrapper=new HoopWrapperFile ();
	       	wrapper.setFileURI("<PROJECTPATH>/"+file.getName ());
	       	
	       	proj.addFile(wrapper);
	       	proj.save();
		}
		
		updateProjectViews ();
	}
}
