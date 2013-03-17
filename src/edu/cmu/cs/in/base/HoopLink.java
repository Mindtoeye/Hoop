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

package edu.cmu.cs.in.base;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.prefs.Preferences;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;

import edu.cmu.cs.in.search.HoopDataSet;
import edu.cmu.cs.in.base.io.HoopVFSL;
import edu.cmu.cs.in.base.io.HoopStreamedSocket;
import edu.cmu.cs.in.controls.base.HoopEmbeddedJPanel;
import edu.cmu.cs.in.controls.base.HoopViewInterface;
import edu.cmu.cs.in.hoop.HoopConsoleInterface;
import edu.cmu.cs.in.hoop.HoopGraphManager;
import edu.cmu.cs.in.hoop.HoopHelp;
import edu.cmu.cs.in.hoop.HoopManager;
import edu.cmu.cs.in.hoop.HoopStatusBar;
import edu.cmu.cs.in.hoop.HoopTabDraggable;
import edu.cmu.cs.in.hoop.HoopTabPane;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.hoop.hoops.base.HoopConnection;
import edu.cmu.cs.in.hoop.editor.HoopEditorMenuBar;
import edu.cmu.cs.in.hoop.editor.HoopEditorToolBar;
import edu.cmu.cs.in.hoop.execute.HoopExecute;
import edu.cmu.cs.in.hoop.execute.HoopExecutionMonitor;
import edu.cmu.cs.in.hoop.project.HoopProject;
import edu.cmu.cs.in.hoop.properties.HoopStoredProperties;
import edu.cmu.cs.in.hoop.properties.types.HoopBooleanSerializable;
import edu.cmu.cs.in.search.HoopTextSearch;
import edu.cmu.cs.in.stats.HoopPerformanceMeasure;
import edu.cmu.cs.in.stats.HoopStatistics;

/**
*
*/
public class HoopLink extends HoopProperties
{    		    			
	public static Preferences preferences=null;
	
	public static HoopConsoleInterface console=null;	
	public static HoopStatistics stats=null;
	
	public static ImageIcon icon=null; // This should ideally be an array of icons, available as standard resources	
	public static ImageIcon linkIcon=null; // This should ideally be an array of icons, available as standard resources
	public static ImageIcon unlinkIcon=null; // This should ideally be an array of icons, available as standard resources
	
	public static ImageIcon [] imageIcons=null;
	public static String [] imgURLs={
									 "broken.jpg",// 01
									 "close.png",// 02
									 "gtk-about.png",// 03
		 							 "gtk-add.png",// 04
		 							 "gtk-apply.png",// 05
		 							 "gtk-bold.png",// 06
									 "gtk-cancel.png",// 07
		 							 "gtk-cdrom.png",// 08
		 							 "gtk-clear.png",// 09
		 							 "gtk-close.png",// 10
									 "gtk-convert.png",// 11
		 							 "gtk-copy.png",// 12
		 							 "gtk-cut.png",// 13
		 							 "gtk-delete.png",// 14
		 							 "gtk-edit.png",// 15
		 							 "gtk-execute.png",// 16
		 							 "gtk-file.png",// 17
		 							 "gtk-find-and-replace.png",// 18
		 							 "gtk-find.png",// 19
		 							 "gtk-floppy.png",// 20
		 							 "gtk-go-back-ltr.png",// 21
		 							 "gtk-go-down.png",// 22
		 							 "gtk-go-forward-ltr.png",// 23
		 							 "gtk-go-up.png",// 24
		 							 "gtk-goto-bottom.png",// 25
		 							 "gtk-goto-first-ltr.png",// 26
		 							 "gtk-goto-last-ltr.png",// 27
		 							 "gtk-goto-top.png",// 28
		 							 "gtk-harddisk.png",// 29
		 							 "gtk-help.png",// 30
		 							 "gtk-home.png",// 31
		 							 "gtk-indent-ltr.png",// 32
		 							 "gtk-index.png",// 33
		 							 "gtk-info.png",// 34
		 							 "gtk-italic.png",// 35
		 							 "gtk-jump-to-ltr.png",// 36
		 							 "gtk-justify-center.png",// 37
		 							 "gtk-justify-fill.png",// 38
		 							 "gtk-justify-left.png",// 39
		 							 "gtk-justify-right.png",// 40
		 							 "gtk-network.png",// 41
		 							 "gtk-new.png",// 42
		 							 "gtk-no.png",// 43
		 							 "gtk-ok.png",// 44
		 							 "gtk-open.png",// 45
		 							 "gtk-paste.png",// 46
		 							 "gtk-preferences.png",// 47
		 							 "gtk-print-preview.png",// 48
		 							 "gtk-print.png",// 49
		 							 "gtk-properties.png",// 50
		 							 "gtk-quit.png",// 51
		 							 "gtk-redo-ltr.png",// 52
		 							 "gtk-refresh.png",// 53
		 							 "gtk-remove.png",// 54
		 							 "gtk-revert-to-saved-ltr.png",// 55
		 							 "gtk-save-as.png",// 56
		 							 "gtk-save.png",// 57
		 							 "gtk-select-all.png",// 58
		 							 "gtk-select-color.png",// 59
		 							 "gtk-select-font.png",// 60
		 							 "gtk-sort-ascending.png",// 61
		 							 "gtk-sort-descending.png",// 62
		 							 "gtk-spell-check.png",// 63
		 							 "gtk-stop.png",// 64
		 							 "gtk-strikethrough.png",// 65
									 "gtk-undelete-ltr.png",// 66
		 							 "gtk-underline.png",// 67
		 							 "gtk-undo-ltr.png",// 68
		 							 "gtk-unindent-ltr.png",// 69
		 							 "gtk-yes.png",// 70
		 							 "gtk-zoom-100.png",// 71
		 							 "gtk-zoom-fit.png",// 72
		 							 "gtk-zoom-in.png",// 73
		 							 "gtk-zoom-out.png",// 74
		 							 "link.jpg",// 75
		 							 "machine.png",// 76
		 							 "wiki-links.jpg",// 77
		 							 "hoop-load.png",// 78
		 							 "hoop-save.png",// 79
		 							 "hoop-to-kv.png",// 80
		 							 "hoop-to-list.png",// 81
		 							 "hoop-to-table.png",// 82
		 							 "arrow-up.png",// 83
		 							 "arrow-down.png", 		 
		 							 "actor.png", 		 
		 							 "alignbottom.gif", 		 
		 							 "aligncenter.gif", 
		 							 "alignleft.gif", 
		 							 "alignmiddle.gif", 
		 							 "alignright.gif", 
		 							 "aligntop.gif", 
		 							 "arrow.gif", 
		 							 "arrow.png", 
		 							 "bell.png", 
		 							 "block_end.gif", 
		 							 "block_start.gif", 
		 							 "bold.gif", 
		 							 "bottom.gif", 
		 							 "box.png", 
		 							 "cancel_end.png", 
		 							 "cancel_intermediate.png", 
		 							 "center.gif", 
		 							 "classic_end.gif", 
		 							 "classic_start.gif", 
		 							 "cloud.png", 
		 							 "collapse.gif", 
		 							 "connect.gif", 
		 							 "connect.png", 
		 							 "connector.gif", 
		 							 "copy.gif", 
		 							 "cube_green.png", 
		 							 "cut.gif", 
		 							 "cylinder.png", 
		 							 "delete.gif", 
		 							 "diagram.gif", 
		 							 "diamond_end.gif", 
		 							 "diamond_start.gif", 
		 							 "doubleellipse.png", 
		 							 "down.gif", 
		 							 "dude3.png", 
		 							 "earth.png", 
		 							 "ellipse.png", 
		 							 "entity.gif", 
		 							 "entity.png", 
		 							 "error.png", 
		 							 "event.png", 
		 							 "event_end.png", 
		 							 "event_intermediate.png", 
		 							 "expand.gif", 
		 							 "fillcolor.gif", 
		 							 "fit.gif", 
		 							 "font.gif", 
		 							 "fontcolor.gif", 
		 							 "fork.png", 
		 							 "gear.png", 
		 							 "group.gif", 
		 							 "hexagon.png", 
		 							 "hline.png", 
		 							 "house.gif", 
		 							 "house.png", 
		 							 "image.gif", 
		 							 "inclusive.png", 
		 							 "italic.gif", 
		 							 "left.gif", 
		 							 "linecolor.gif", 
		 							 "link.png", 
		 							 "maximize.gif", 
		 							 "merge.png", 
		 							 "message.png", 
		 							 "message_end.png", 
		 							 "message_intermediate.png", 
		 							 "middle.gif", 
		 							 "minimize.gif", 
		 							 "multiple.png", 
		 							 "new.gif", 
		 							 "open.gif", 
		 							 "open_end.gif", 
		 							 "open_start.gif", 
		 							 "outline.gif", 
		 							 "oval_end.gif", 
		 							 "oval_start.gif", 
		 							 "package.png", 
		 							 "pagesetup.gif", 
		 							 "pan.gif", 
		 							 "paste.gif", 
		 							 "plain.gif", 
		 							 "preferences.gif", 
		 							 "preview.gif", 
		 							 "print.gif", 
		 							 "printer.png", 
		 							 "rectangle.png", 
		 							 "redo.gif", 
		 							 "resize.gif", 
		 							 "rhombus.png", 
		 							 "right.gif", 
		 							 "rounded.png", 
		 							 "rule.png", 
		 							 "save.gif", 
		 							 "saveas.gif", 
		 							 "select.gif", 
		 							 "server.png", 
		 							 "straight.gif", 
		 							 "straight.png", 
		 							 "swimlane.png", 
		 							 "terminate.png", 
		 							 "timer.png", 
		 							 "toback.gif", 
		 							 "tofront.gif", 
		 							 "top.gif", 
		 							 "tree.gif", 
		 							 "triangle.png", 
		 							 "undo.gif", 
		 							 "ungroup.gif", 
		 							 "up.gif", 
		 							 "vertical.gif", 
		 							 "vertical.png", 
		 							 "workplace.png", 
		 							 "wrench.png", 
		 							 "zoom.gif", 
		 							 "zoomactual.gif", 
		 							 "zoomin.gif", 
		 							 "zoomout.gif",
		 							 "led-green.png",
		 							 "led-yellow.png",
		 							 "led-red.png", 							 
		 							 "resize.png",		 							 
		 							 "zoom.png", 
		 							 "port.png",
		 							 "tree-expand-icon.png",
		 							 "tree-fold-icon.png", 
		 							 "hoop.png",
		 							 "run-once.png",
		 							 "run-n.png", 
		 							 "run-forever.png", 		 							 
		 							 "checkbox.jpg", 
		 							 "player-pause.png", 
		 							 "player-play.png", 		 							 
		 							 "player-stop.png",
		 							 "help_icon.png",
		 							 "mime_xml.png",
		 							 "text_icon.png",
		 							 "unknown_216_16.png",
		 							 "link-views.png",
		 							 "folder.png",
		 							 "system-folder.png",
		 							 "wait_animated.gif",
		 							 "run-stopped.png",
		 							 "run-running.png",
		 							 "csv.png",
		 							 "weka.png",
		 							 "alphab_sort_icon.png",
		 							 "run.png",
		 							 "debug.png",
		 							 "delete.png",
		 							 "data.gif",
		 							 "Hoop-logo.png",
		 							 "graph-icon2.gif",
		 							 "console.png",
		 							 "hoop-graph.png",
		 							 "data-inspector.png",
		 							 "project.png",
		 							 "splash_icon_word_balloon.gif",
		 							 "run-cluster.png",
		 							 "annotation.gif"
		 							 };
	
	public static JFrame mainFrame=null;
	
	public static String vocabularyPath="./";
	public static ArrayList <String> posFiles=null;	
	public static ArrayList <HoopPerformanceMeasure> metrics=null;
	public static ArrayList <HoopEmbeddedJPanel> windows=null;
	public static ArrayList <HoopViewInterface> windowsPlugins=null;
	public static ArrayList <HoopTextSearch> searchHistory=null;
	public static ArrayList <String>jobs=null;
	public static HoopVFSL fManager=null;	
	public static HoopDataSet dataSet=null;
	public static ArrayList <String> queries=null;
	public static int experimentNr=0;
	
	// Globally accessible UI elements
	
	public static HoopEditorMenuBar menuBar=null;
	public static JToolBar toolBar=null;
	public static HoopEditorToolBar toolEditorBar=null;
	public static JPanel toolBoxContainer=null;
	public static HoopStatusBar statusBar=null;
	
	public static HoopExecutionMonitor executionMonitor=null;
	
	public static HoopTabDraggable left=null;
	public static HoopTabDraggable right=null;
	public static HoopTabDraggable center=null;
	public static HoopTabDraggable bottom=null;	
		
	// Networking access
	
	public static HoopStreamedSocket brokerConnection=null;
	
	// Core hoop access
	
	public static HoopManager hoopManager=null;
	public static HoopGraphManager hoopGraphManager=null;
	
	// Execution access
	
	/*
	 * This static reference should ideally be a parent class of HoopExecute or an
	 * interface. Java allows cyclic imports but it's better not to do this.
	 */
	public static HoopExecute runner=null;
	
	// Le data
	
	public static HoopProject project=null;
	
	// Hoop support variables
	
	public static Integer hoopInstanceIndex=1;
				
	/**
	 *
	 */
    public HoopLink () 
    {
		setClassName ("HoopLink");
		debug ("HoopLink ()");
		
		props=new HoopStoredProperties ();
		
		setupProperties ();
		
		//whitespace=Pattern.compile("["  + whitespace_chars + "]");
		//Matcher matcher = whitespace.matcher ("test");
				
		if (stats==null)
			stats=new HoopStatistics ();
		
		if (metrics==null)
			metrics=new ArrayList<HoopPerformanceMeasure> ();
		
		if (windows==null)
			windows=new ArrayList<HoopEmbeddedJPanel>();
		
		if (windowsPlugins==null)
			windowsPlugins=new ArrayList<HoopViewInterface>();
		
		if (searchHistory==null)
			searchHistory=new ArrayList<HoopTextSearch>();
		
		if (jobs==null)
			jobs=new ArrayList<String> ();
		
		if (hoopManager==null)
			hoopManager=new HoopManager ();
		
		if (hoopGraphManager==null)
			hoopGraphManager=new HoopGraphManager ();		
    }  
    /**
     * 
     */
    private void setupProperties ()
    {
    	HoopBooleanSerializable tmp=new HoopBooleanSerializable (props,"logtodisk",true);
    }
    /**
     * 
     */
    public static ImageIcon getImageByName (String aName)
    {
    	for (int i=0;i<imgURLs.length;i++)
    	{
    		String test=imgURLs [i];
    		
    		if (test.toLowerCase().equals(aName.toLowerCase())==true)
    		{
    			ImageIcon target=imageIcons [i];
    			
    			return (target);
    		}
    	}
    	
    	HoopRoot.debug ("HoopLink","Error: image " + aName + " not found");
    	
    	return (null);
    }
    /**
     * 
     */
    public static ImageIcon fileToIcon (String aName)
    {
    	if (aName.toLowerCase().indexOf(".xml")!=-1)
    	{
    		return (HoopLink.getImageByName("mime_xml.png"));    		    	
    	}
    	
    	if (aName.toLowerCase().indexOf(".txt")!=-1)
    	{
    		return (HoopLink.getImageByName("text_icon.png"));    		
    	}
    	
    	if (aName.toLowerCase().indexOf(".arrf")!=-1)
    	{
    		return (HoopLink.getImageByName("weka.png"));    		
    	}
    	
    	if (aName.toLowerCase().indexOf(".csv")!=-1)
    	{
    		return (HoopLink.getImageByName("csv.png"));    		
    	}
    	    	    		    	    	
    	//return (HoopLink.getImageByName("unknown_216_16.png"));
    	
    	return (null);
    }
	/**
	 *
	 */
    public static void addWindow (HoopEmbeddedJPanel aWindow)
    {
    	HoopRoot.debug ("HoopLink","addWindow ()");
    	
    	if (windows!=null)
    		windows.add(aWindow);
    }
	/**
	 *
	 */
    public static void removeWindowInternal (HoopEmbeddedJPanel aWindow)
    {
	   HoopRoot.debug ("HoopLink","removeWindowInternal ()");
	   
   		if (windows!=null)
   		{
   			windows.remove(aWindow);
   		}
    }    
	/**
	 *
	 */
    public static void removeWindow (HoopEmbeddedJPanel aWindow)
    {
    	HoopRoot.debug ("HoopLink","removeWindow ()");
    	
    	if (windows!=null)
    	{
    		aWindow.close();
    		windows.remove(aWindow);
    	}
    }
	/**
	 *
	 */
    public static HoopEmbeddedJPanel getWindow (String aTitle)
    {
    	HoopRoot.debug ("HoopLink","getWindow ()");
    	
    	for (int i=0;i<windows.size();i++)
    	{
    		HoopEmbeddedJPanel aWindow=windows.get(i);
    		if (aWindow.getInstanceName().toLowerCase().equals(aTitle.toLowerCase())==true)
    		{
    			return (aWindow);
    		}
    	}
    	
    	return (null);
    }
	/**
	 *
	 */
    public static void updateWindow (String aTitle)
    {
    	HoopRoot.debug ("HoopLink","updateWindow ()");
    	
   		for (int i=0;i<windows.size();i++)
   		{
   			HoopEmbeddedJPanel aWindow=windows.get(i);
   			if (aWindow.getInstanceName().toLowerCase().equals(aTitle.toLowerCase())==true)
   			{
   				aWindow.updateContents ();
   			}
   		}   
    }
	/**
	 *
	 */
   public static void popWindow (String aTitle)
   {
   		HoopRoot.debug ("HoopLink","popWindow ()");
   	
  		for (int i=0;i<windows.size();i++)
  		{
  			HoopEmbeddedJPanel aWindow=windows.get(i);
  			
  			if (aWindow.getInstanceName().toLowerCase().equals(aTitle.toLowerCase())==true)
  			{
  				aWindow.updateContents ();
  				
  				JTabbedPane pane=aWindow.getHost();
  				
  				if (pane!=null)
  				{
  					pane.setSelectedComponent (aWindow);
  				}
  				else
  					HoopRoot.debug ("HoopLink","Error unable to find window with title: " + aTitle);
  			}
  		}   
   }    
	/**
	 *
	 */
    public static void updateAllWindows ()
    {
    	HoopRoot.debug ("HoopLink","updateAllWindows ()");
    	
 		for (int i=0;i<windows.size();i++)
 		{
 			HoopEmbeddedJPanel aWindow=windows.get(i);
 			aWindow.updateContents ();
 		}   
    }       
    /**
     * 
     */
    public void setStatus (String aStatus)
    {    	
    	if (statusBar!=null)
    		statusBar.setStatus(aStatus);
    }
	/**
	 *
	 */
  	public static void addView (String aTitle,
  								HoopEmbeddedJPanel aContent,
  								String aPane)
  	{
  		HoopRoot.debug ("HoopLink","addView ()");
  	
  		HoopTabDraggable target=null;
 	
  		if (aPane.equals("center")==true)
  			target=HoopLink.center;
  	
  		if (aPane.equals("left")==true)
  			target=HoopLink.left;
  	
  		if (aPane.equals("right")==true)
  			target=HoopLink.right;
  	
  		if (aPane.equals("bottom")==true)
  			target=HoopLink.bottom;
  	
  		HoopLink.addView (aTitle,aContent,target);
  	}      
	/**
	 *
	 */
  	public static void addView (String aTitle,
  								HoopEmbeddedJPanel aContent,
		   						JTabbedPane aPane)
  	{
  		HoopRoot.debug ("HoopLink","addView ()");
	   
  		aContent.setInstanceName(aTitle);
   		HoopLink.addWindow(aContent);
   	
   		aPane.addTab(aTitle,
   					 aContent.getIcon(),
   					 aContent,
   					 "New Panel");
   		int index=aPane.indexOfComponent (aContent);
   		HoopTabPane pane=new HoopTabPane (aPane);
   		aPane.setTabComponentAt(index,pane);
   	
   		if (aPane==HoopLink.left)
   		{   			
   			HoopRoot.debug ("HoopLink","Setting appropriate dimensions for left panel ...");
   			HoopLink.left.setPreferredSize(new Dimension (150,200));
   			aPane.setPreferredSize(new Dimension (150,200));   			
   		}
   		
   		if (aPane==HoopLink.right)
   		{   			
   			HoopRoot.debug ("HoopLink","Setting appropriate dimensions for right panel ...");   			
   			HoopLink.right.setPreferredSize(new Dimension (150,200));
   			aPane.setPreferredSize(new Dimension (150,200));   			
   		}
   		
   		if (aPane==HoopLink.bottom)
   		{   			
   			HoopRoot.debug ("HoopLink","Setting appropriate dimensions for bottom panel ...");   			
   			HoopLink.bottom.setPreferredSize(new Dimension (300,150));
   			aPane.setPreferredSize(new Dimension (300,150));  			
   		}   		
   	
   		pane.update();
   		
   		aContent.setHost(aPane);

   		aPane.setSelectedComponent (aContent);   		
   	}
  	/**
  	 * 
  	 */
  	public static void showHelpTopic (String aTopic)
  	{
  		HoopRoot.debug ("HoopLink","showHelpTopic ()");
  		  		
  		HoopHelp helpWindow=(HoopHelp) HoopLink.getWindow("Help");
  		
  		if (helpWindow==null)
  		{
  			addView ("Help",new HoopHelp (),HoopLink.left);      
  			helpWindow=(HoopHelp) HoopLink.getWindow("Help");
  		}
  		
  		HoopLink.popWindow("Help");
  		
  		helpWindow.navigateToTopic (aTopic);
  	}
	/** 
	 * @return HoopBase
	 */
	public static HoopBase getGraphRoot() 
	{
		HoopRoot.debug ("HoopLink","getGraphRoot ()");
		
		if (HoopLink.project==null)
			return (null);
		
		return (HoopLink.project.getGraphRoot());
	}
	/** 
	 * @return HoopBase
	 */
	public static ArrayList <HoopConnection> getGraphConnections() 
	{
		HoopRoot.debug ("HoopLink","getGraphConnections ()");
		
		if (HoopLink.project==null)
			return (null);
		
		return (HoopLink.project.getGraphConnections());
	}	
	/**
	 * 
	 */
	public static String relativeToAbsolute (String aPath)
	{		
		if (HoopLink.project==null)
			return (aPath); // Nothing to do

		if (HoopLink.project.getVirginFile()==true)
			return (aPath);
		
		if (aPath.indexOf("<PROJECTPATH>")==-1)
			return (aPath); // Nothing to do
				
		StringBuffer formatted=new StringBuffer ();
		
		String lastPart=aPath.substring(13); // index of <PROJECTPATH> which is 13 long
		
		String projectPath=HoopLink.project.getBasePath();
		
		formatted.append (projectPath);
		formatted.append("//");
		formatted.append(lastPart);
		
		return (formatted.toString());
	}
	/**
	 * 
	 */
	public static String absoluteToRelative (String aPath)
	{				
		if (HoopLink.project==null)
			return (aPath);
		
		if (HoopLink.project.getVirginFile()==true)
			return (aPath);
		
		String projectPath=HoopLink.project.getBasePath();
		
		if (aPath.indexOf(projectPath)==-1)
			return (aPath);
		
		HoopRoot.debug ("HoopLink","Subtracting ["+HoopLink.project.getBasePath()+"] from path: " + aPath);
		
		String remainder=aPath.substring(HoopLink.project.getBasePath().length());
		
		StringBuffer formatted=new StringBuffer ();
		
		formatted.append("<PROJECTPATH>");
		//formatted.append("/");
		formatted.append(remainder);
		
		return (formatted.toString());
	}	
}
