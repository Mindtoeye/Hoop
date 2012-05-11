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

//import java.util.regex.Matcher;
//import java.util.regex.Pattern;

import java.util.ArrayList;

import javax.swing.ImageIcon;

import edu.cmu.cs.in.search.INDataSet;
import edu.cmu.cs.in.base.INBase;
import edu.cmu.cs.in.controls.base.INEmbeddedJPanel;
//import edu.cmu.cs.in.controls.base.INJInternalFrame;
import edu.cmu.cs.in.hoop.INHoopConsoleInterface;
import edu.cmu.cs.in.search.INTextSearch;
import edu.cmu.cs.in.stats.INPerformanceMetrics;

/**
*
*/
public class INLink extends INBase
{    		    	
	public static int debugLine=0;
	
	public static boolean useHadoop=false;
	public static boolean casefold=true;
	public static boolean stopwords=true;	
	public static boolean stemming=true;
	public static boolean cleanoutput=false;
	public static boolean dbglocal=false;
	public static int minstemsize=4;
	
	public static INHoopConsoleInterface console=null;
	
	public static boolean postonly=false;
	
	public static String shardtype="alphabetical"; // One of: docid|alphabetical|docsize
	public static long splitsize=10;
	public static int nrshards=1;
	public static int shardcount=0;
	public static String shardcreate="hdfs";
	
	public static String task="none";
	
	public static String datapath=".";
	public static String outputpath=".";
	
	public static String monitorHost="10.0.0.245";
	//public static String monitorHost="augustus.pslc.cs.cmu.edu";
	public static int monitorPort=8080;
	
	/*
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
		 							 "arrow-down.png"// 84		 							 
		 							 };
	*/		 							 
	
	//public static String vocabularyPath="./";
	//public static ArrayList <String> posFiles=null;	
	//public static ArrayList <INPerformanceMetrics> metrics=null;
	//public static ArrayList <INEmbeddedJPanel> windows=null;
	//public static ArrayList <INTextSearch> searchHistory=null;
	public static INFileManager fManager=null;	
	//public static INDataSet dataSet=null;
	//public static ArrayList <String> queries=null;
	//public static int experimentNr=0;
		
	public static String crossDomainPolicy = "<?xml version=\"1.0\"?>\n" +
	"<!DOCTYPE cross-domain-policy SYSTEM \"http://www.macromedia.com/xml/dtds/cross-domain-policy.dtd\">\n" + 
	"<cross-domain-policy>\n" + 
	"<allow-access-from domain=\"*\" to-ports=\"*\" />\n" +
	"</cross-domain-policy>\0";	
	
	/**
	 *
	 */
    public INLink () 
    {
		setClassName ("INLink");
		debug ("INLink ()");
		
		//whitespace=Pattern.compile("["  + whitespace_chars + "]");
		//Matcher matcher = whitespace.matcher ("test");
				
		//metrics=new ArrayList<INPerformanceMetrics> ();
		//windows=new ArrayList<INEmbeddedJPanel>();
		//searchHistory=new ArrayList<INTextSearch>();
		
		fManager=new INFileManager ();		
    }      
}
