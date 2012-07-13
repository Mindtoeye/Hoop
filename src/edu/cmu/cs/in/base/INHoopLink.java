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

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;

import edu.cmu.cs.in.network.INStreamedSocket;
import edu.cmu.cs.in.search.INDataSet;
import edu.cmu.cs.in.base.io.INFileManager;
import edu.cmu.cs.in.controls.base.INEmbeddedJPanel;
import edu.cmu.cs.in.hoop.INHoopConsoleInterface;
import edu.cmu.cs.in.hoop.INHoopGraphManager;
import edu.cmu.cs.in.hoop.INHoopHelp;
import edu.cmu.cs.in.hoop.INHoopManager;
import edu.cmu.cs.in.hoop.INHoopStatusBar;
import edu.cmu.cs.in.hoop.INHoopTabDraggable;
import edu.cmu.cs.in.hoop.INHoopTabPane;
import edu.cmu.cs.in.hoop.base.INHoopBase;
import edu.cmu.cs.in.hoop.editor.INHoopEditorMenuBar;
import edu.cmu.cs.in.hoop.editor.INHoopEditorToolBar;
import edu.cmu.cs.in.hoop.project.INHoopProject;
import edu.cmu.cs.in.search.INTextSearch;
import edu.cmu.cs.in.stats.INPerformanceMetrics;
import edu.cmu.cs.in.stats.INStatistics;

/**
*
*/
public class INHoopLink extends INHoopProperties
{    		    		
	public static INHoopConsoleInterface console=null;	
	public static INStatistics stats=null;
	
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
		 							 "player-stop.jpg",
		 							 "help_icon.png"
		 							 };
	
	public static JFrame mainFrame=null;
	
	public static String vocabularyPath="./";
	public static ArrayList <String> posFiles=null;	
	public static ArrayList <INPerformanceMetrics> metrics=null;
	public static ArrayList <INEmbeddedJPanel> windows=null;
	public static ArrayList <INTextSearch> searchHistory=null;
	public static ArrayList <String>jobs=null;
	public static INFileManager fManager=null;	
	public static INDataSet dataSet=null;
	public static ArrayList <String> queries=null;
	public static int experimentNr=0;
	
	// Globally accessible UI elements
	
	public static INHoopEditorMenuBar menuBar=null;
	public static JToolBar toolBar=null;
	public static INHoopEditorToolBar toolEditorBar=null;
	public static JPanel toolBoxContainer=null;
	public static INHoopStatusBar statusBar=null;
	
	public static INHoopTabDraggable left=null;
	public static INHoopTabDraggable right=null;
	public static INHoopTabDraggable center=null;
	public static INHoopTabDraggable bottom=null;	
	
	// Networking access
	
	public static INStreamedSocket brokerConnection=null;
	
	// Core hoop access
	
	public static INHoopManager hoopManager=null;
	public static INHoopGraphManager hoopGraphManager=null;
	
	// Le data
	
	public static INHoopProject project=null;
				
	/**
	 *
	 */
    public INHoopLink () 
    {
		setClassName ("INHoopLink");
		debug ("INHoopLink ()");
		
		//whitespace=Pattern.compile("["  + whitespace_chars + "]");
		//Matcher matcher = whitespace.matcher ("test");
				
		if (stats==null)
			stats=new INStatistics ();
		
		if (metrics==null)
			metrics=new ArrayList<INPerformanceMetrics> ();
		
		if (windows==null)
			windows=new ArrayList<INEmbeddedJPanel>();
		
		if (searchHistory==null)
			searchHistory=new ArrayList<INTextSearch>();
		
		if (jobs==null)
			jobs=new ArrayList<String> ();
		
		if (hoopManager==null)
			hoopManager=new INHoopManager ();
		
		if (hoopGraphManager==null)
			hoopGraphManager=new INHoopGraphManager ();		
    }  
    /**
     * 
     */
    public static ImageIcon getImageByName (String aName)
    {
    	for (int i=0;i<imgURLs.length;i++)
    	{
    		String test=imgURLs [i];
    		if (test.toLowerCase().equals(aName)==true)
    			return (imageIcons [i]);
    	}
    	
    	return (null);
    }
	/**
	 *
	 */
    public static void addWindow (INEmbeddedJPanel aWindow)
    {
    	INBase.debug ("INHoopLink","addWindow ()");
    	
    	if (windows!=null)
    		windows.add(aWindow);
    }
	/**
	 *
	 */
    public static void removeWindowInternal (INEmbeddedJPanel aWindow)
    {
	   INBase.debug ("INHoopLink","removeWindowInternal ()");
	   
   		if (windows!=null)
   		{
   			windows.remove(aWindow);
   		}
    }    
	/**
	 *
	 */
    public static void removeWindow (INEmbeddedJPanel aWindow)
    {
    	INBase.debug ("INHoopLink","removeWindow ()");
    	
    	if (windows!=null)
    	{
    		aWindow.close();
    		windows.remove(aWindow);
    	}
    }
	/**
	 *
	 */
    public static INEmbeddedJPanel getWindow (String aTitle)
    {
    	INBase.debug ("INHoopLink","getWindow ()");
    	
    	for (int i=0;i<windows.size();i++)
    	{
    		INEmbeddedJPanel aWindow=windows.get(i);
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
    	INBase.debug ("INHoopLink","updateWindow ()");
    	
   		for (int i=0;i<windows.size();i++)
   		{
   			INEmbeddedJPanel aWindow=windows.get(i);
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
   		INBase.debug ("INHoopLink","popWindow ()");
   	
  		for (int i=0;i<windows.size();i++)
  		{
  			INEmbeddedJPanel aWindow=windows.get(i);
  			if (aWindow.getInstanceName().toLowerCase().equals(aTitle.toLowerCase())==true)
  			{
  				aWindow.updateContents ();
  				
  				JTabbedPane pane=aWindow.getHost();
  				
  				if (pane!=null)
  					pane.setSelectedComponent (aWindow);
  			}
  		}   
   }    
	/**
	 *
	 */
    public static void updateAllWindows ()
    {
    	INBase.debug ("INHoopLink","updateAllWindows ()");
    	
 		for (int i=0;i<windows.size();i++)
 		{
 			INEmbeddedJPanel aWindow=windows.get(i);
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
  								INEmbeddedJPanel aContent,
  								String aPane)
  	{
  		INBase.debug ("INHoopLink","addView ()");
  	
  		INHoopTabDraggable target=null;
 	
  		if (aPane.equals("center")==true)
  			target=INHoopLink.center;
  	
  		if (aPane.equals("left")==true)
  			target=INHoopLink.left;
  	
  		if (aPane.equals("right")==true)
  			target=INHoopLink.right;
  	
  		if (aPane.equals("bottom")==true)
  			target=INHoopLink.bottom;
  	
  		INHoopLink.addView (aTitle,aContent,target);
  	}      
	/**
	 *
	 */
  	public static void addView (String aTitle,
  								INEmbeddedJPanel aContent,
		   						JTabbedPane aPane)
  	{
  		INBase.debug ("INHoopLink","addView ()");
	   
  		aContent.setInstanceName(aTitle);
   		INHoopLink.addWindow(aContent);
   	
   		aPane.addTab(aTitle,INHoopLink.imageIcons [5],aContent,"New Panel");
   		int index=aPane.indexOfComponent (aContent);
   		INHoopTabPane pane=new INHoopTabPane (aPane);
   		aPane.setTabComponentAt(index,pane);
   	
   		if (aPane==INHoopLink.left)
   		{   			
   			INBase.debug ("INHoopLink","Setting appropriate dimensions for left panel ...");
   			INHoopLink.left.setPreferredSize(new Dimension (150,200));
   			aPane.setPreferredSize(new Dimension (150,200));   			
   		}
   		
   		if (aPane==INHoopLink.right)
   		{   			
   			INBase.debug ("INHoopLink","Setting appropriate dimensions for right panel ...");   			
   			INHoopLink.right.setPreferredSize(new Dimension (150,200));
   			aPane.setPreferredSize(new Dimension (150,200));   			
   		}
   		
   		if (aPane==INHoopLink.bottom)
   		{   			
   			INBase.debug ("INHoopLink","Setting appropriate dimensions for bottom panel ...");   			
   			INHoopLink.bottom.setPreferredSize(new Dimension (300,150));
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
  		INBase.debug ("INHoopLink","showHelpTopic ()");
  		  		
  		INHoopHelp helpWindow=(INHoopHelp) INHoopLink.getWindow("Help");
  		
  		if (helpWindow==null)
  		{
  			addView ("Help",new INHoopHelp (),INHoopLink.left);      
  			helpWindow=(INHoopHelp) INHoopLink.getWindow("Help");
  		}
  		
  		INHoopLink.popWindow("Help");
  		
  		helpWindow.navigateToTopic (aTopic);
  	}
	/** 
	 * @return INHoopBase
	 */
	public static INHoopBase getGraphRoot() 
	{
		INBase.debug ("INHoopLink","getGraphRoot ()");
		
		if (INHoopLink.project==null)
			return (null);
		
		return (INHoopLink.project.getGraphRoot());
	}
	/**
	 * 
	 */
	public static String relativeToAbsolute (String aPath)
	{		
		if (aPath.indexOf("<PROJECTPATH>")==-1)
		{
			return (aPath); // Nothing to do
		}
		
		String lastPart=aPath.substring(11); // index of <PROJECTPATH> which is 12 long
		
		String projectPath=INHoopLink.project.getBasePath();
		
		return (projectPath+"\\"+lastPart);
	}
}
