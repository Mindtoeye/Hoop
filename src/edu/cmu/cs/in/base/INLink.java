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

package edu.cmu.cs.in.base;

//import java.util.regex.Matcher;
//import java.util.regex.Pattern;

import java.util.ArrayList;

import javax.swing.ImageIcon;

import edu.cmu.cs.in.INDataSet;
import edu.cmu.cs.in.base.INBase;
import edu.cmu.cs.in.controls.base.INJInternalFrame;
import edu.cmu.cs.in.hoop.INHoopConsoleInterface;
import edu.cmu.cs.in.search.INTextSearch;
import edu.cmu.cs.in.stats.INPerformanceMetrics;

/**
*
*/
public class INLink extends INBase
{    		    	
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
	
	public static String monitorHost="extlogin.opencloud";
	//public static String monitorHost="augustus.pslc.cs.cmu.edu";
	public static int monitorPort=8082;
	
	public static ImageIcon icon=null; // This should ideally be an array of icons, available as standard resources	
	public static ImageIcon linkIcon=null; // This should ideally be an array of icons, available as standard resources
	public static ImageIcon unlinkIcon=null; // This should ideally be an array of icons, available as standard resources
	
	public static String [] stops={"a","an","and","are","as","at","for","i","if","in","is","it","of","on","so","that","the","to"};
	public static String [] garbage={"*","?","!",".","'","#","-","_",":",",","|","\"","(",")","^",";"};
	
	public static String vocabularyPath="./";
	public static ArrayList <String> posFiles=null;	
	public static ArrayList <INPerformanceMetrics> metrics=null;
	public static ArrayList <INJInternalFrame> windows=null;
	public static ArrayList <INTextSearch> searchHistory=null;
	public static INFileManager fManager=null;	
	public static INDataSet dataSet=null;
	public static ArrayList <String> queries=null;
	public static int experimentNr=0;
	
	public static String whitespace_chars   =  ""       /* dummy empty string for homogeneity */
											+ "\\u0009" // CHARACTER TABULATION
											+ "\\u000A" // LINE FEED (LF)
											+ "\\u000B" // LINE TABULATION
											+ "\\u000C" // FORM FEED (FF)
											+ "\\u000D" // CARRIAGE RETURN (CR)
											+ "\\u0020" // SPACE
											+ "\\u0085" // NEXT LINE (NEL) 
											+ "\\u00A0" // NO-BREAK SPACE
											+ "\\u1680" // OGHAM SPACE MARK
											+ "\\u180E" // MONGOLIAN VOWEL SEPARATOR
											+ "\\u2000" // EN QUAD 
											+ "\\u2001" // EM QUAD 
											+ "\\u2002" // EN SPACE
											+ "\\u2003" // EM SPACE
											+ "\\u2004" // THREE-PER-EM SPACE
											+ "\\u2005" // FOUR-PER-EM SPACE
											+ "\\u2006" // SIX-PER-EM SPACE
											+ "\\u2007" // FIGURE SPACE
											+ "\\u2008" // PUNCTUATION SPACE
											+ "\\u2009" // THIN SPACE
											+ "\\u200A" // HAIR SPACE
											+ "\\u2028" // LINE SEPARATOR
											+ "\\u2029" // PARAGRAPH SEPARATOR
											+ "\\u202F" // NARROW NO-BREAK SPACE
											+ "\\u205F" // MEDIUM MATHEMATICAL SPACE
											+ "\\u3000" // IDEOGRAPHIC SPACE
											;
	
	//public static Pattern whitespace=null;
	
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
				
		metrics=new ArrayList<INPerformanceMetrics> ();
		windows=new ArrayList<INJInternalFrame>();
		searchHistory=new ArrayList<INTextSearch>();
		
		fManager=new INFileManager ();
		
		listStops ();
    }  
	/**
	 *
	 */
    public static void addWindow (INJInternalFrame aWindow)
    {
    	if (windows!=null)
    		windows.add(aWindow);
    }
	/**
	 *
	 */
    public static void removeWindow (INJInternalFrame aWindow)
    {
    	if (windows!=null)
    		windows.remove(aWindow);
    }
	/**
	 *
	 */
    public static INJInternalFrame getWindow (String aTitle)
    {
    	for (int i=0;i<windows.size();i++)
    	{
    		INJInternalFrame aWindow=windows.get(i);
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
    public static void updateWindows (String aTitle)
    {
   		for (int i=0;i<windows.size();i++)
   		{
   			INJInternalFrame aWindow=windows.get(i);
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
 		for (int i=0;i<windows.size();i++)
 		{
 			INJInternalFrame aWindow=windows.get(i);
 			aWindow.updateContents ();
 		}   
    }   
	/**
	 *
	 */
    public void listStops ()
    {
    	debug ("listStops ()");
    	
    	for (int i=0;i<stops.length;i++)
    	{
    		debug ("Using stopword: " + stops [i]);
    	}
    }
	/**
	 *
	 */
    public void listGarbage ()
    {
    	debug ("listGarbage ()");
   	
    	for (int i=0;i<garbage.length;i++)
    	{
    		debug ("Using garbage term: " + garbage [i]);
    	}
    }    
}
