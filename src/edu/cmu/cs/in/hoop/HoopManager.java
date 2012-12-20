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

import java.util.ArrayList;

import edu.cmu.cs.in.hoop.hoops.analyze.HoopCrossFold;
import edu.cmu.cs.in.hoop.hoops.analyze.HoopCustomExperimentTest;
import edu.cmu.cs.in.hoop.hoops.analyze.HoopDocumentTag;
import edu.cmu.cs.in.hoop.hoops.analyze.HoopEvaluate;
import edu.cmu.cs.in.hoop.hoops.analyze.HoopKVStats;
import edu.cmu.cs.in.hoop.hoops.analyze.HoopMatcher;
import edu.cmu.cs.in.hoop.hoops.analyze.HoopNaiveBayes;
import edu.cmu.cs.in.hoop.hoops.analyze.HoopSemanticPatterns;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.hoop.hoops.base.HoopBatch;
import edu.cmu.cs.in.hoop.hoops.base.HoopCopy;
import edu.cmu.cs.in.hoop.hoops.base.HoopDisplayBase;
import edu.cmu.cs.in.hoop.hoops.base.HoopKVTest;
import edu.cmu.cs.in.hoop.hoops.base.HoopTest;
import edu.cmu.cs.in.hoop.hoops.load.HoopArffReader;
import edu.cmu.cs.in.hoop.hoops.load.HoopBerkeleyDBReader;
import edu.cmu.cs.in.hoop.hoops.load.HoopCSVReader;
import edu.cmu.cs.in.hoop.hoops.load.HoopDocumentReader;
import edu.cmu.cs.in.hoop.hoops.load.HoopDocumentThreadReader;
import edu.cmu.cs.in.hoop.hoops.load.HoopFTPReader;
import edu.cmu.cs.in.hoop.hoops.load.HoopFileLoadBase;
import edu.cmu.cs.in.hoop.hoops.load.HoopMySQLReader;
import edu.cmu.cs.in.hoop.hoops.load.HoopStdin;
import edu.cmu.cs.in.hoop.hoops.load.HoopThreadReader;
import edu.cmu.cs.in.hoop.hoops.load.HoopURLReader;
import edu.cmu.cs.in.hoop.hoops.load.HoopWikipediaAccess;
import edu.cmu.cs.in.hoop.hoops.save.HoopArffWriter;
import edu.cmu.cs.in.hoop.hoops.save.HoopBerkeleyDBWriter;
import edu.cmu.cs.in.hoop.hoops.save.HoopCSVWriter;
import edu.cmu.cs.in.hoop.hoops.save.HoopDocumentUpdater;
import edu.cmu.cs.in.hoop.hoops.save.HoopDocumentWriter;
import edu.cmu.cs.in.hoop.hoops.save.HoopFileSaveBase;
import edu.cmu.cs.in.hoop.hoops.save.HoopIndexDocuments;
import edu.cmu.cs.in.hoop.hoops.save.HoopSASLink;
import edu.cmu.cs.in.hoop.hoops.save.HoopStdout;
import edu.cmu.cs.in.hoop.hoops.save.HoopTextFileSave;
import edu.cmu.cs.in.hoop.hoops.save.HoopXMLDocumentWriter;
import edu.cmu.cs.in.hoop.hoops.save.HoopXMLWriter;
import edu.cmu.cs.in.hoop.hoops.task.HoopPathChooser;
import edu.cmu.cs.in.hoop.hoops.task.HoopScheduler;
import edu.cmu.cs.in.hoop.hoops.task.HoopStart;
import edu.cmu.cs.in.hoop.hoops.transform.HoopCleanTokens;
import edu.cmu.cs.in.hoop.hoops.transform.HoopColumnRenamer;
import edu.cmu.cs.in.hoop.hoops.transform.HoopDocumentCreator;
import edu.cmu.cs.in.hoop.hoops.transform.HoopFilterGarbage;
import edu.cmu.cs.in.hoop.hoops.transform.HoopFilterStopWords;
import edu.cmu.cs.in.hoop.hoops.transform.HoopKV2TXT;
import edu.cmu.cs.in.hoop.hoops.transform.HoopKeySort;
import edu.cmu.cs.in.hoop.hoops.transform.HoopLabelKV;
import edu.cmu.cs.in.hoop.hoops.transform.HoopMap2Document;
import edu.cmu.cs.in.hoop.hoops.transform.HoopPorterStemmer;
import edu.cmu.cs.in.hoop.hoops.transform.HoopReKey;
import edu.cmu.cs.in.hoop.hoops.transform.HoopSentence2Tokens;
import edu.cmu.cs.in.hoop.hoops.transform.HoopText2Sentence;
import edu.cmu.cs.in.hoop.hoops.transform.HoopTokenCaseChange;
import edu.cmu.cs.in.hoop.hoops.transform.HoopUniqueTerms;
import edu.cmu.cs.in.hoop.hoops.transform.HoopXML2Features;
import edu.cmu.cs.in.hoop.hoops.visualize.HoopParseTree;

/** 
 * @author Martin van Velsen
 */
public class HoopManager extends HoopBase
{
	private static final long serialVersionUID = -3676325977389900928L;
	
	private ArrayList<String> hoopCategories=null;	
	private ArrayList<HoopBase> hoopTemplates=null;
	
	/**
	 * 
	 */
	public HoopManager()
	{		
		setClassName ("HoopManager");
		debug ("HoopManager ()");
		
		hoopTemplates=new ArrayList<HoopBase> ();
		hoopCategories=new ArrayList<String> ();
		
		initHoopTemplates ();		
	}	
	/**
	 * 
	 */
	public ArrayList<String> getHoopCategories ()
	{
		return (hoopCategories);
	}
	/**
	 * 
	 */
	public ArrayList<HoopBase> getHoopTemplates ()
	{
		return (hoopTemplates);
	}
	/**
	 * 
	 */
	public Boolean getCategory (String aCategory)
	{
		for (int i=0;i<hoopCategories.size();i++)
		{
			String aCat=hoopCategories.get(i);
			
			if (aCat.toLowerCase().equals(aCategory.toLowerCase ())==true)
				return (true);
		}
		
		return (false);
	}
    /**
     * 
     */
	public void addCategory (String aCategory)
	{
		if (getCategory (aCategory)==false)
		{
			hoopCategories.add(aCategory);
		}	
	}
    /**
     * 
     */
	public void addTemplate (HoopBase aTemplate)
	{
		debug ("addTemplate ()");
		
		addCategory (aTemplate.getHoopCategory());
		
		hoopTemplates.add(aTemplate);		
	}
    /**
     * 
     */    
	public HoopBase instantiate (String aTemplate)
	{
		debug ("instantiate ("+aTemplate+")");
		
		if (aTemplate==null)
			return (null);
		
		if (aTemplate.equals("HoopStart")==true)
		{
			return (new HoopStart ());
		}
		
		for (int i=0;i<hoopTemplates.size();i++)
		{
			HoopBase hoopTemplate=hoopTemplates.get(i);
			
			if (hoopTemplate.getClassName().toLowerCase().equals(aTemplate.toLowerCase())==true)
				return (hoopTemplate.copy ());
			
		}	
		
		return (null);
	}
    /**
     * 
     */    
    private void initHoopTemplates ()
    {
    	debug ("initHoopTemplates ()");
    	
    	addTemplate (new HoopDisplayBase ());
    	addTemplate (new HoopFileLoadBase ());
    	addTemplate (new HoopFileSaveBase ());   	
    	addTemplate (new HoopText2Sentence ());
    	addTemplate (new HoopSentence2Tokens ());
    	addTemplate (new HoopStdout ());
    	addTemplate (new HoopStdin ());
    	addTemplate (new HoopFilterStopWords ());
    	addTemplate (new HoopUniqueTerms ());
    	addTemplate (new HoopTokenCaseChange ());
    	addTemplate (new HoopPorterStemmer ());
    	addTemplate (new HoopCleanTokens ());
    	addTemplate (new HoopFilterGarbage ());
    	addTemplate (new HoopKV2TXT ());
    	addTemplate (new HoopCSVWriter ());
    	addTemplate (new HoopCSVReader ());
    	addTemplate (new HoopNaiveBayes ());
    	addTemplate (new HoopArffReader ());
    	addTemplate (new HoopArffWriter ());
    	addTemplate (new HoopXML2Features ());
    	addTemplate (new HoopMySQLReader ());
    	addTemplate (new HoopBerkeleyDBReader ());
    	addTemplate (new HoopPathChooser ());
    	addTemplate (new HoopScheduler ());
    	addTemplate (new HoopBerkeleyDBWriter ());
    	addTemplate (new HoopXMLWriter ());
    	addTemplate (new HoopURLReader ());
    	addTemplate (new HoopXMLDocumentWriter ());
    	addTemplate (new HoopColumnRenamer ());
    	addTemplate (new HoopTextFileSave ());
    	addTemplate (new HoopKeySort ());
    	addTemplate (new HoopKVStats ());
    	addTemplate (new HoopMap2Document ());
    	addTemplate (new HoopDocumentReader ());
    	addTemplate (new HoopDocumentWriter ());
    	addTemplate (new HoopDocumentUpdater ());
    	addTemplate (new HoopLabelKV ());
    	addTemplate (new HoopCrossFold ());
    	addTemplate (new HoopSemanticPatterns ());
    	addTemplate (new HoopDocumentTag ());
    	addTemplate (new HoopDocumentCreator ());
    	addTemplate (new HoopSASLink ());
    	addTemplate (new HoopMatcher ());
    	addTemplate (new HoopReKey ());
    	addTemplate (new HoopBatch ());
    	addTemplate (new HoopKVTest ());
    	addTemplate (new HoopCopy ());
    	addTemplate (new HoopEvaluate ());
    	addTemplate (new HoopParseTree ());
    	addTemplate (new HoopTest ());
    	addTemplate (new HoopDocumentThreadReader ());
    	addTemplate (new HoopCustomExperimentTest ());
    	addTemplate (new HoopIndexDocuments ());
    	addTemplate (new HoopWikipediaAccess ());
    	addTemplate (new HoopThreadReader ());
    	addTemplate (new HoopFTPReader ());
    }
}
