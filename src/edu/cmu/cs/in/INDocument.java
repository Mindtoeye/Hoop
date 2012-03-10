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

package edu.cmu.cs.in;

import java.util.ArrayList;
import java.util.List;

import edu.cmu.cs.in.base.INFileManager;
import edu.cmu.cs.in.base.INFeatureMatrixBase;
import edu.cmu.cs.in.base.INLink;
import edu.cmu.cs.in.base.INPorterStemmer;
import edu.cmu.cs.in.base.INSimpleFeatureMaker;
import edu.cmu.cs.in.base.INWikipediaFilter;

/**
*
*/
public class INDocument extends INFeatureMatrixBase
{	
	private String docID="-1";
	private INPorterStemmer stemmer=null;
	private INFileManager fManager=null;	
	private List<String> tokens=null;
	private Boolean includePositions=false;
	
	/**
	 *
	 */
    public INDocument () 
    {
		setClassName ("INDocument");
		debug ("INDocument ()");
		
		setStemmer(new INPorterStemmer ());
		fManager=new INFileManager ();
		
		tokens=new ArrayList<String>();
    }
	/**
	 *
	 */    
	public String getDocID() 
	{
		return docID;
	}
	/**
	 *
	 */	
	public void setDocID(String docID) 
	{
		this.docID = docID;
	}    
	/**
	 *
	 */    
	public Boolean getIncludePositions() 
	{
		return includePositions;
	}
	/**
	 *
	 */	
	public void setIncludePositions(Boolean includePositions) 
	{
		this.includePositions = includePositions;
	}    
	/**
	 *
	 */    
	public INPorterStemmer getStemmer() 
	{
		return stemmer;
	}
	/**
	 *
	 */	
	public void setStemmer(INPorterStemmer stemmer) 
	{
		this.stemmer = stemmer;
	}    
	/**
	 *
	 */    
	public List<String> getTokens() 
	{
		return tokens;
	}
	/**
	 *
	 */	
	public void setTokens(List<String> tokens) 
	{
		this.tokens = tokens;
	}    
	/**
	 *
	 */
    public Boolean isStopWord (String test)
    {
		for (int j=0;j<INLink.stops.length;j++)
		{
			// We should already be in lowercase for this to work!!
			
			if (test.equals(INLink.stops [j])==true)
			{
				return (true);
			}
		}    	
		
		return (false);
    }
	/**
	 *
	 */
    public Boolean isGarbage (String test)
    {
    	for (int j=0;j<INLink.garbage.length;j++)
		{
			// We should already be in lowercase for this to work!!
			
			if (test.equals(INLink.garbage [j])==true)
			{
				return (true);
			}
			
			// PUT THIS IN THE WIKI FILTER!!!
			
			if ((test.indexOf("http://")!=-1) || (test.indexOf("http/")!=-1) || (test.indexOf("ftp:")!=-1))
			{
				return (true);
			}
		}    	
		
		return (false);
    }   
	/**
	 *
	 */
    public Boolean loadDocumentFromData (String aData)
    {
    	debug ("loadDocumentFromData ()");
    	
    	INFileManager manager=new INFileManager ();
    	    	
    	ArrayList<String> lines=manager.dataToLines (aData);
	   
    	if (lines==null)
    	{
    		debug ("Error: something went wrong loading the file");
    		return (false);
    	}
    	    	
    	return (parseLines (lines));
    }
	/**
	 *
	 */
    public Boolean loadDocument (String aPath)
    {
    	debug ("loadDocument ("+aPath+")");
    	
    	//content=fManager.loadContents(aPath);
    	ArrayList<String> lines=fManager.loadLines (aPath);
    	
    	if (lines==null)
    	{
    		debug ("Error: something went wrong loading the file");
    		return (false);
    	}
    	
    	return (parseLines (lines));
    }
	/**
	 *
	 */    
    private Boolean parseLines (ArrayList<String> lines)
    {
    	debug ("parseLines ()");
    	
    	List<String> rawTokens=new ArrayList<String>();
    	//List<String> stemmedTokens=new ArrayList<String>();
    	tokens=new ArrayList<String>();
    	
    	INSimpleFeatureMaker tokenizer=new INSimpleFeatureMaker ();
    	tokenizer.addFilter (new INWikipediaFilter ());
    	
    	for (int j=0;j<lines.size();j++)
    	{
    		String aLine=lines.get(j);
    		rawTokens=tokenizer.unigramTokenize (rawTokens,aLine);
    		
    		if (rawTokens==null)
    		{
    			debug ("Internal error, returned null token list!");
    			return (false);
    		}
    		
    	}	
    	    	    	    	
    	debug ("Found " + rawTokens.size() + " tokens");
    	    	
    	int removed=0;
    	int position=0;    	    		
    	
   		for (int i=0;i<rawTokens.size();i++)
   		{
   			String token=rawTokens.get(i);

   	    	if (INLink.stopwords==true)
   	    	{
   	    		if ((isStopWord (token)==true) || (isGarbage (token)==true))
   	    			removed++;
   	    		else
   	    		{
   	    			if (INLink.stemming==true)
   	    			{
   	    				if (token.length()>INLink.minstemsize)
   	    				{
   	    					if (includePositions==true)
   	    						tokens.add (stemmer.stem(token)+":"+position);
   	    					else
   	    						tokens.add (stemmer.stem(token));
   	    				}
   	    				else
   	    				{
   	    					if (includePositions==true)
   	    						tokens.add(token+":"+position);
   	    					else
   	    						tokens.add(token);
   	    				}
   	    			}
   	    			else
   	    				tokens.add(token);
   	    		}	
   	    	}	
   	    	
   	    	position++;
   		}
    	    	
    	debug ("Removed " + removed + " tokens from document");
    	
    	outputDocument ();
    	
    	return (true);    	
    }
	/**
	 *
	 */
    public void outputDocument ()
    {
    	debug ("outputDocument ()");
    	
    	StringBuffer formatted=new StringBuffer ();
    	
		for (int i=0;i<tokens.size();i++)
		{
			String token=tokens.get(i);
			
			if (i>0)
			{
				formatted.append(" ");
			}
			
			formatted.append(token);			
		}    	
		
		//debug ("Document: " + formatted.toString());
		
		if (INLink.cleanoutput==true)
		{
			fManager.saveContents (fManager.stripExtension (fManager.getURI())+"-cleaned.txt",formatted.toString());
		}
    }
}
