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
import java.util.List;

import edu.cmu.cs.in.base.io.HoopFileManager;
import edu.cmu.cs.in.base.kv.HoopKVDocument;
import edu.cmu.cs.in.base.HoopPorterStemmerOriginal;
import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.base.HoopRoot;
import edu.cmu.cs.in.base.HoopSimpleFeatureMaker;
import edu.cmu.cs.in.base.HoopWikipediaFilter;

/**
* Our document representation class is directly derived from the Hoop KV abstract
* class. For now this should suffice but we might have to derive from the KV Table
* instead to allow for better data modeling.
*/
public class HoopDocument extends HoopKVDocument
{		
	private HoopPorterStemmerOriginal stemmer=null;	
	private List<String> tokens=null;
	private Boolean includePositions=false;
	private String summary="empty";
	
	/**
	 *
	 */
    public HoopDocument () 
    {	
		debug ("HoopDocument ()");
		
		setStemmer(new HoopPorterStemmerOriginal ());
		
		tokens=new ArrayList<String>();
    }
    /**
     * 
     */
    protected void debug (String aMessage)
    {
    	HoopRoot.debug("HoopDocument",aMessage);
    }
    /**
	 *
	 */    
	public String getSummary() 
	{
		return summary;
	}
    /**
	 *
	 */	
	public void setSummary(String summary) 
	{
		this.summary = summary;
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
	public HoopPorterStemmerOriginal getStemmer() 
	{
		return stemmer;
	}
	/**
	 *
	 */	
	public void setStemmer(HoopPorterStemmerOriginal stemmer) 
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
		for (int j=0;j<HoopLink.stops.length;j++)
		{
			// We should already be in lowercase for this to work!!
			
			if (test.equals(HoopLink.stops [j])==true)
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
    	for (int j=0;j<HoopLink.garbage.length;j++)
		{
			// We should already be in lowercase for this to work!!
			
			if (test.equals(HoopLink.garbage [j])==true)
			{
				return (true);
			}
			
			// PUT THIS Hoop THE WIKI FILTER!!!
			
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
    	
    	HoopFileManager manager=new HoopFileManager ();
    	    	
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
    	
    	ArrayList<String> lines=HoopLink.fManager.loadLines (aPath);
    	
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
    	
    	HoopSimpleFeatureMaker tokenizer=new HoopSimpleFeatureMaker ();
    	tokenizer.addFilter (new HoopWikipediaFilter ());
    	
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

   	    	if (HoopLink.stopwords==true)
   	    	{
   	    		if ((isStopWord (token)==true) || (isGarbage (token)==true))
   	    			removed++;
   	    		else
   	    		{
   	    			if (HoopLink.stemming==true)
   	    			{
   	    				if (token.length()>HoopLink.minstemsize)
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
		
		if (HoopLink.cleanoutput==true)
		{
			HoopLink.fManager.saveContents (HoopLink.fManager.stripExtension (HoopLink.fManager.getURI())+"-cleaned.txt",formatted.toString());
		}
    }
}
