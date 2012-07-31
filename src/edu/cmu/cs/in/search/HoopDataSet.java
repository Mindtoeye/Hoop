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

package edu.cmu.cs.in.search;

import java.util.ArrayList;

import edu.cmu.cs.in.base.HoopRoot;
import edu.cmu.cs.in.base.io.HoopFileManager;
import edu.cmu.cs.in.base.HoopLink;

/** 
 * @author vvelsen
 *
 * There's not much to this class, it holds a list of documents
 * (HoopDocument) so that we can easily retrieve them
 *
 */
public class HoopDataSet extends HoopRoot
{
	private ArrayList<HoopDocument> documents=null;
	
	/**
	 *
	 */
    public HoopDataSet () 
    {
		setClassName ("HoopDataSet");
		debug ("HoopDataSet ()");		
				
		// For fast and easy access we re-use the file manager from
		// the registry. Makes a huge difference when you're doing
		// loops since it's a costly object to create
		
		if (HoopLink.fManager==null)
			HoopLink.fManager=new HoopFileManager ();
		
		setDocuments(new ArrayList<HoopDocument> ());
    }
	/**
	 *
	 */    
	public ArrayList<HoopDocument> getDocuments() 
	{
		return documents;
	}
	/**
	 *
	 */	
	public void setDocuments(ArrayList<HoopDocument> documents) 
	{
		this.documents = documents;
	}  
	/**
	 *
	 */
	public void addDocument (HoopDocument anInstance)
	{
		documents.add(anInstance);
	}
	/**
	 *
	 */
    public Boolean loadDocuments (String aPath)
    {
    	debug ("loadDocuments ("+aPath+")");
    	
    	ArrayList<String> files=HoopLink.fManager.listFiles (aPath);
    	
    	if (files.size()==0)
    	{
    		debug ("Error: no files found");
    		return (false);
    	}
    	
    	for (int i=0;i<files.size();i++)
    	{
    		String entry=(String) files.get(i);
    		
    		if ((entry.equals(".")==true) || (entry.equals(".")==true))
    		{
    			// Skipping the usual suspects
    		}
    		else
    		{
    			HoopDocument loader=new HoopDocument ();
    			//loader.setInstanceName(entry);
    			loader.setKey(entry);
    			
    			// Keep in mind here that we load an entire document all at
    			// once. This is a remnant from how the hadoop code worked,
    			// however the position lists are retrieved much faster
    			
    			loader.loadDocument (aPath+"/"+entry);
    			documents.add(loader);
    		}
    	}
    	
    	return (true);
    }
	/**
	 * Nice to have debug method to see what was loaded
	 */
    public void printStats ()
    {
    	debug ("printStats ()");
    	
    	for (int i=0;i<documents.size();i++)
    	{
    		HoopDocument test=documents.get(i);
    		debug (test.getKey()+" : " + test.getTokens().size());
    	}
    }
}
