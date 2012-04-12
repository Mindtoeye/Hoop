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

//import javax.swing.ImageIcon;

import edu.cmu.cs.in.search.INDataSet;
//import edu.cmu.cs.in.base.INBase;
//import edu.cmu.cs.in.controls.base.INEmbeddedJPanel;
//import edu.cmu.cs.in.controls.base.INJInternalFrame;
//import edu.cmu.cs.in.hoop.INHoopConsoleInterface;
//import edu.cmu.cs.in.search.INTextSearch;
//import edu.cmu.cs.in.stats.INPerformanceMetrics;

/**
*
*/
public class INLangLink extends INLink
{    		    		
	public static String [] stops={
									"a",
									"an",
									"and",
									"are",
									"as",
									"at",
									"for",
									"i",
									"if",
									"in",
									"is",
									"it",
									"of",
									"on",
									"so",
									"that",
									"the",
									"to"
								  };
	public static String [] garbage={
									 "*",
									 "?",
									 "!",
									 ".",
									 "'",
									 "#",
									 "-",
									 "_",
									 ":",
									 ",",
									 "|",
									 "\"",
									 "(",
									 ")",
									 "^",
									 ";"};
	
	public static String vocabularyPath="./";
	public static ArrayList <String> posFiles=null;	
	public static ArrayList <String> queries=null;
	public static INDataSet dataSet=null;
	//public static ArrayList <INTextSearch> searchHistory=null;
	
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
	/**
	 *
	 */
    public INLangLink () 
    {
		setClassName ("INLangLink");
		debug ("INLangLink ()");		
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
