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

import edu.cmu.cs.in.base.INBase;
import edu.cmu.cs.in.base.INFileManager;
import edu.cmu.cs.in.base.INLink;

/** 
 * @author vvelsen
 *
 * There's not much to this class, it holds a list of documents
 * (INDocument) so that we can easily retrieve them
 *
 */
public class INDataSet extends INBase
{
	private ArrayList<INDocument> documents=null;
	
	/**
	 *
	 */
    public INDataSet () 
    {
		setClassName ("INDataSet");
		debug ("INDataSet ()");		
				
		// For fast and easy access we re-use the file manager from
		// the registry. Makes a huge difference when you're doing
		// loops since it's a costly object to create
		
		if (INLink.fManager==null)
			INLink.fManager=new INFileManager ();
		
		setDocuments(new ArrayList<INDocument> ());
    }
	/**
	 *
	 */    
	public ArrayList<INDocument> getDocuments() 
	{
		return documents;
	}
	/**
	 *
	 */	
	public void setDocuments(ArrayList<INDocument> documents) 
	{
		this.documents = documents;
	}  
	/**
	 *
	 */
	public void addDocument (INDocument anInstance)
	{
		documents.add(anInstance);
	}
	/**
	 *
	 */
    public Boolean loadDocuments (String aPath)
    {
    	debug ("loadDocuments ("+aPath+")");
    	
    	ArrayList<String> files=INLink.fManager.listFiles (aPath);
    	
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
    			INDocument loader=new INDocument ();
    			loader.setInstanceName(entry);
    			
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
    		INDocument test=documents.get(i);
    		debug (test.getInstanceName()+" : " + test.getTokens().size());
    	}
    }
}
