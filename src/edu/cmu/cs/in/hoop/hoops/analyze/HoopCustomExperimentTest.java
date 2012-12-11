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

package edu.cmu.cs.in.hoop.hoops.analyze;

import java.util.ArrayList;

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.base.kv.HoopKV;
import edu.cmu.cs.in.base.kv.HoopKVDocument;
import edu.cmu.cs.in.base.kv.HoopKVInteger;
import edu.cmu.cs.in.base.kv.HoopKVString;
import edu.cmu.cs.in.hoop.hoops.base.HoopAnalyze;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.hoop.hoops.base.HoopInterface;
import edu.cmu.cs.in.hoop.properties.types.HoopEnumSerializable;
import edu.cmu.cs.in.ling.HoopEngPronounModel;
import edu.cmu.cs.in.ling.HoopLingTerm;

/**
* 
*/
public class HoopCustomExperimentTest extends HoopAnalyze implements HoopInterface
{    								
	private static final long serialVersionUID = 8887457217876512280L;
	
	private String threadTracker="";
	private String typeCache="";
	
	private HoopEngPronounModel model=null;
	
	private HoopKVString analysisResult=null;
	
	private HoopEnumSerializable patternGeneration=null;
	
	/**
	 *
	 */
    public HoopCustomExperimentTest () 
    {
		setClassName ("HoopCustomExperimentTest");
		debug ("HoopCustomExperimentTest ()");
				
		setHoopDescription ("Custom Hoop to experiment with patterns");
		
		patternGeneration=new HoopEnumSerializable (this,"patternGeneration","additive,delta");
    }
    /**
     * 
     */
    public void reset ()
    {
    	debug ("reset ()");
    	
    	super.reset();
    	
    	threadTracker="";
    	typeCache="";
    	analysisResult=null;
    	
    	if (model==null)
    		model=HoopLink.pronounModel;
    }
	/**
	 *
	 */
	public Boolean runHoop (HoopBase inHoop)
	{		
		debug ("runHoop ()");
			
		ArrayList <HoopKV> inData=inHoop.getData();
		
		if (inData!=null)
		{
			//StringBuffer formatted=new StringBuffer ();
			
			debug ("Processing " + inData.size() + " documents ...");
			
			for (int t=0;t<inData.size();t++)
			{
				HoopKV aKV=inData.get(t);
				
				if (aKV instanceof HoopKVDocument)
				{
					HoopKVDocument aDocument=(HoopKVDocument) aKV;
					
					if (aDocument.threadID.getValue().equals(threadTracker)==true)
					{
						if (processDocument (aDocument)==false)
							return (false);	
					}
					else
					{
						threadTracker=aDocument.threadID.getValue();
						analysisResult=new HoopKVString (threadTracker,"*");
						this.addKV(analysisResult);
						
						debug ("Adding document " + aDocument.getKeyString() + " with thread id: " + aDocument.threadID.getValue());
						
						if (aDocument.threadID.getValue()!=null)
						{
							if (processDocument (aDocument)==false)
							{
								return (false);
							}
						}
						else
							debug ("Error: document.threadID is null");						
					}
				}
			}			
		}	
						
		return (true);				
	}	     
	/**
	 * 
	 */
	/*
	protected Boolean processKVBatch (ArrayList <HoopKV> inData,int currentIndex,int batchSize)
	{
		debug ("processKVBatch ()");
		
		model=HoopLink.pronounModel;
		
		if (model==null)
		{
			this.setErrorString("Error: no English pronoun model available");
			return (false);
		}
				
		for (int t=0;t<batchSize;t++)
		{
			HoopKV aKV=inData.get(t);
			
			if (aKV instanceof HoopKVDocument)
			{
				HoopKVDocument aDocument=(HoopKVDocument) aKV;
				
				if (aDocument.threadID.getValue().equals(threadTracker)==true)
				{
					if (processDocument (aDocument,aDocument.threadID.getValue())==false)
						return (false);
				}
				else
				{
					threadTracker=aDocument.threadID.getValue();
					analysisResult=new HoopKVString (threadTracker,"*");
					this.addKV(analysisResult);
					
					if (processDocument (aDocument,aDocument.threadID.getValue())==false)
						return (false);
				}
			}
		}	
		
		return (true);
	}
	*/
	/**
	 * 
	 */
	private Boolean processDocument (HoopKVDocument aDocument)
	{
		debug ("processDocument ("+aDocument.threadID.getValue()+")");
		
		/*
		if (aDocument==null)
		{
			this.setErrorString("Error: a document provided null");
			return (false);
		}
		*/
		
		if (analysisResult==null)
		{
			this.setErrorString("Error: analysis result object is null");
			return (false);
		}
		
		if (model.getTerms().size()==0)
		{
			this.setErrorString("Error: pronoun model doesn't contain any entries");
			return (false);
		}
				
		//String aKey=aDocument.threadID.getValue();
		
		HoopKVString basicTokenView=aDocument.getView ("Simple Tokens");
		
		if (basicTokenView==null)
		{
			this.setErrorString("Error: a document provided does not have any tokens");
			return (false);
		}
		
		ArrayList<Object> tokens=basicTokenView.getValuesRaw();
		
		if (tokens==null)
		{
			this.setErrorString("Error: token list is null");
			return (false);
		}
		
		debug ("Examining " + tokens.size() + " tokens");
		
		for (int i=0;i<tokens.size();i++)
		{
			String aToken=(String) tokens.get(i);
			
			HoopLingTerm proFound=model.getTerm (aToken);
			
			if (proFound!=null)
			{
				String typeFound=proFound.getValue(5);
				
				//debug ("Pronoun " + proFound.getKeyString() + " found, for: " + typeFound);
												
				if ((typeCache.equals("in")==true) && (typeFound.equals ("out")))
				{
					analysisResult.add("1");
				}
				
				if ((typeCache.equals("out")==true) && (typeFound.equals ("in")))
				{
					analysisResult.add("0");
				}
				
				if (patternGeneration.getPropValue().equalsIgnoreCase("additive")==true)
				{				
					if ((typeCache.equals("in")==true) && (typeFound.equals ("in")))
					{
						analysisResult.add("0");
					}
				
					if ((typeCache.equals("out")==true) && (typeFound.equals ("out")))
					{
						analysisResult.add("1");
					}
				}	
				
				typeCache=typeFound;
			}
		}
		
		return (true);
	}
	/**
	 * 
	 */
	public HoopBase copy ()
	{
		return (new HoopCustomExperimentTest ());
	}	
}
