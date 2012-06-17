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

import edu.cmu.cs.in.hoop.base.INHoopBase;
import edu.cmu.cs.in.hoop.base.INHoopDisplayBase;
import edu.cmu.cs.in.hoop.base.INHoopFileLoadBase;
import edu.cmu.cs.in.hoop.base.INHoopFileSaveBase;
import edu.cmu.cs.in.hoop.hoops.INHoopFile2Sentence;
import edu.cmu.cs.in.hoop.hoops.INHoopFilterStopWords;
import edu.cmu.cs.in.hoop.hoops.INHoopHDFSMultiFileInput;
import edu.cmu.cs.in.hoop.hoops.INHoopHDFSMultiFileOutput;
import edu.cmu.cs.in.hoop.hoops.INHoopHDFSSingleFileInput;
import edu.cmu.cs.in.hoop.hoops.INHoopHDFSSingleFileOutput;
import edu.cmu.cs.in.hoop.hoops.INHoopScheduler;
import edu.cmu.cs.in.hoop.hoops.INHoopSentence2Tokens;
import edu.cmu.cs.in.hoop.hoops.INHoopStdin;
import edu.cmu.cs.in.hoop.hoops.INHoopStoud;
import edu.cmu.cs.in.hoop.hoops.INHoopUniqueTerms;

public class INHoopManager extends INHoopBase
{
	private ArrayList<String> hoopCategories=null;	
	private ArrayList<INHoopBase> hoopTemplates=null;
	
	public INHoopManager()
	{		
		setClassName ("INHoopManager");
		debug ("INHoopManager ()");
		
		hoopTemplates=new ArrayList<INHoopBase> ();
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
	public ArrayList<INHoopBase> getHoopTemplates ()
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
	public void addTemplate (INHoopBase aTemplate)
	{
		debug ("addTemplate ()");
		
		addCategory (aTemplate.getHoopCategory());
		
		hoopTemplates.add(aTemplate);		
	}
    /**
     * 
     */    
	public INHoopBase instantiate (String aTemplate)
	{
		debug ("instantiate ("+aTemplate+")");
		
		for (int i=0;i<hoopTemplates.size();i++)
		{
			INHoopBase hoopTemplate=hoopTemplates.get(i);
			
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
    	
    	addTemplate(new INHoopDisplayBase ());
    	addTemplate(new INHoopFileLoadBase ());
    	addTemplate(new INHoopFileSaveBase ());
    	//addTemplate(new INHoopLoadBase ());
    	//addTemplate(new INHoopSaveBase ());
    	//addTemplate(new INHoopTransformBase ());    	
    	addTemplate(new INHoopFile2Sentence ());
    	addTemplate(new INHoopSentence2Tokens ());
    	addTemplate(new INHoopScheduler ());
    	addTemplate(new INHoopHDFSSingleFileOutput ());
    	addTemplate(new INHoopHDFSSingleFileInput ());
    	addTemplate(new INHoopHDFSMultiFileOutput ());
    	addTemplate(new INHoopHDFSMultiFileInput ());
    	addTemplate (new INHoopStoud ());
    	addTemplate (new INHoopStdin ());
    	addTemplate (new INHoopFilterStopWords ());
    	addTemplate (new INHoopUniqueTerms ());
    }
}
