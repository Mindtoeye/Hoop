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

import edu.cmu.cs.in.hoop.hoops.analyze.HoopNaiveBayes;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.hoop.hoops.base.HoopDisplayBase;
import edu.cmu.cs.in.hoop.hoops.base.HoopFileLoadBase;
import edu.cmu.cs.in.hoop.hoops.base.HoopFileSaveBase;
import edu.cmu.cs.in.hoop.hoops.load.HoopArffReader;
import edu.cmu.cs.in.hoop.hoops.load.HoopBerkeleyDBReader;
import edu.cmu.cs.in.hoop.hoops.load.HoopCSVReader;
import edu.cmu.cs.in.hoop.hoops.load.HoopMySQLReader;
import edu.cmu.cs.in.hoop.hoops.load.HoopStdin;
import edu.cmu.cs.in.hoop.hoops.load.HoopURLReader;
import edu.cmu.cs.in.hoop.hoops.save.HoopArffWriter;
import edu.cmu.cs.in.hoop.hoops.save.HoopBerkeleyDBWriter;
import edu.cmu.cs.in.hoop.hoops.save.HoopCSVWriter;
import edu.cmu.cs.in.hoop.hoops.save.HoopStdout;
import edu.cmu.cs.in.hoop.hoops.save.HoopXMLWriter;
import edu.cmu.cs.in.hoop.hoops.task.HoopPathChooser;
import edu.cmu.cs.in.hoop.hoops.task.HoopScheduler;
import edu.cmu.cs.in.hoop.hoops.transform.HoopCleanTokens;
import edu.cmu.cs.in.hoop.hoops.transform.HoopFilterGarbage;
import edu.cmu.cs.in.hoop.hoops.transform.HoopFilterStopWords;
import edu.cmu.cs.in.hoop.hoops.transform.HoopKV2TXT;
import edu.cmu.cs.in.hoop.hoops.transform.HoopPorterStemmer;
import edu.cmu.cs.in.hoop.hoops.transform.HoopSentence2Tokens;
import edu.cmu.cs.in.hoop.hoops.transform.HoopText2Sentence;
import edu.cmu.cs.in.hoop.hoops.transform.HoopTokenCaseChange;
import edu.cmu.cs.in.hoop.hoops.transform.HoopUniqueTerms;
import edu.cmu.cs.in.hoop.hoops.transform.HoopXML2Features;
import edu.cmu.cs.in.hoop.hoops.HoopHDFSMultiFileInput;
import edu.cmu.cs.in.hoop.hoops.HoopHDFSMultiFileOutput;
import edu.cmu.cs.in.hoop.hoops.HoopHDFSSingleFileInput;
import edu.cmu.cs.in.hoop.hoops.HoopHDFSSingleFileOutput;

/** 
 * @author Martin van Velsen
 */
public class HoopManager extends HoopBase
{
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
    	addTemplate (new HoopHDFSSingleFileOutput ());
    	addTemplate (new HoopHDFSSingleFileInput ());
    	addTemplate (new HoopHDFSMultiFileOutput ());
    	addTemplate (new HoopHDFSMultiFileInput ());
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
    }
}
