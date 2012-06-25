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
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;

import edu.cmu.cs.in.network.INStreamedSocket;
import edu.cmu.cs.in.search.INDataSet;
import edu.cmu.cs.in.base.io.INFileManager;
import edu.cmu.cs.in.controls.base.INEmbeddedJPanel;
import edu.cmu.cs.in.hoop.INHoopConsoleInterface;
import edu.cmu.cs.in.hoop.INHoopGraphManager;
import edu.cmu.cs.in.hoop.INHoopManager;
import edu.cmu.cs.in.hoop.INHoopStatusBar;
import edu.cmu.cs.in.hoop.INHoopTabDraggable;
import edu.cmu.cs.in.hoop.INHoopTabPane;
import edu.cmu.cs.in.hoop.editor.INHoopEditorMenuBar;
import edu.cmu.cs.in.hoop.editor.INHoopEditorToolBar;
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
		 							 "arrow-down.png", // 84		 
		 							 "actor.png", // 84		 
		 							 "alignbottom.gif", // 84		 
		 							 "aligncenter.gif", // 84
		 							 "alignleft.gif", // 84
		 							 "alignmiddle.gif", // 84
		 							 "alignright.gif", // 84
		 							 "aligntop.gif", // 84
		 							 "arrow.gif", // 84
		 							 "arrow.png", // 84
		 							 "bell.png", // 84
		 							 "block_end.gif", // 84
		 							 "block_start.gif", // 84
		 							 "bold.gif", // 84
		 							 "bottom.gif", // 84
		 							 "box.png", // 84
		 							 "cancel_end.png", // 84
		 							 "cancel_intermediate.png", // 84g
		 							 "center.gif", // 84
		 							 "classic_end.gif", // 84
		 							 "classic_start.gif", // 84
		 							 "cloud.png", // 84
		 							 "collapse.gif", // 84
		 							 "connect.gif", // 84
		 							 "connect.png", // 84
		 							 "connector.gif", // 84
		 							 "copy.gif", // 84
		 							 "cube_green.png", // 84
		 							 "cut.gif", // 84
		 							 "cylinder.png", // 84
		 							 "delete.gif", // 84
		 							 "diagram.gif", // 84
		 							 "diamond_end.gif", // 84
		 							 "diamond_start.gif", // 84
		 							 "doubleellipse.png", // 84
		 							 "down.gif", // 84
		 							 "dude3.png", // 84
		 							 "earth.png", // 84
		 							 "ellipse.png", // 84
		 							 "entity.gif", // 84
		 							 "entity.png", // 84
		 							 "error.png", // 84
		 							 "event.png", // 84
		 							 "event_end.png", // 84
		 							 "event_intermediate.png", // 84
		 							 "expand.gif", // 84
		 							 "fillcolor.gif", // 84
		 							 "fit.gif", // 84
		 							 "font.gif", // 84
		 							 "fontcolor.gif", // 84
		 							 "fork.png", // 84
		 							 "gear.png", // 84
		 							 "group.gif", // 84
		 							 "hexagon.png", // 84
		 							 "hline.png", // 84
		 							 "house.gif", // 84
		 							 "house.png", // 84
		 							 "image.gif", // 84
		 							 "inclusive.png", // 84
		 							 "italic.gif", // 84
		 							 "left.gif", // 84
		 							 "linecolor.gif", // 84
		 							 "link.png", // 84
		 							 "maximize.gif", // 84
		 							 "merge.png", // 84
		 							 "message.png", // 84
		 							 "message_end.png", // 84
		 							 "message_intermediate.png", // 84
		 							 "middle.gif", // 84
		 							 "minimize.gif", // 84
		 							 "multiple.png", // 84
		 							 "new.gif", // 84
		 							 "open.gif", // 84
		 							 "open_end.gif", // 84
		 							 "open_start.gif", // 84
		 							 "outline.gif", // 84
		 							 "oval_end.gif", // 84
		 							 "oval_start.gif", // 84
		 							 "package.png", // 84
		 							 "pagesetup.gif", // 84
		 							 "pan.gif", // 84
		 							 "paste.gif", // 84
		 							 "plain.gif", // 84
		 							 "preferences.gif", // 84
		 							 "preview.gif", // 84
		 							 "print.gif", // 84
		 							 "printer.png", // 84
		 							 "rectangle.png", // 84
		 							 "redo.gif", // 84
		 							 "resize.gif", // 84
		 							 "rhombus.png", // 84
		 							 "right.gif", // 84
		 							 "rounded.png", // 84
		 							 "rule.png", // 84
		 							 "save.gif", // 84
		 							 "saveas.gif", // 84
		 							 "select.gif", // 84
		 							 "server.png", // 84
		 							 "straight.gif", // 84
		 							 "straight.png", // 84
		 							 "swimlane.png", // 84
		 							 "terminate.png", // 84
		 							 "timer.png", // 84
		 							 "toback.gif", // 84
		 							 "tofront.gif", // 84
		 							 "top.gif", // 84
		 							 "tree.gif", // 84
		 							 "triangle.png", // 84
		 							 "undo.gif", // 84
		 							 "ungroup.gif", // 84
		 							 "up.gif", // 84
		 							 "vertical.gif", // 84
		 							 "vertical.png", // 84
		 							 "workplace.png", // 84
		 							 "wrench.png", // 84
		 							 "zoom.gif", // 84
		 							 "zoomactual.gif", // 84
		 							 "zoomin.gif", // 84
		 							 "zoomout.gif",// 84
		 							 "led-green.png",// 84
		 							 "led-yellow.png",// 84
		 							 "led-red.png",// 84 							 
		 							 "resize.png",// 84		 							 
		 							 "zoom.png", // 84
		 							 "port.png",
		 							 "tree-expand-icon.png",
		 							 "tree-fold-icon.png", // 84
		 							 "hoop.png"// 84
		 							 };
	
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
   	}      
}
