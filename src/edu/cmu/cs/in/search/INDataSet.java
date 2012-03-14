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
