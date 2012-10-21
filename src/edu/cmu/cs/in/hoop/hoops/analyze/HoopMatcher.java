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
//import edu.cmu.cs.in.base.kv.HoopKVInteger;
import edu.cmu.cs.in.base.kv.HoopKVString;
import edu.cmu.cs.in.hoop.hoops.base.HoopAnalyze;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.hoop.hoops.base.HoopInterface;
import edu.cmu.cs.in.hoop.properties.types.HoopBooleanSerializable;
import edu.cmu.cs.in.hoop.properties.types.HoopIntegerSerializable;
import edu.cmu.cs.in.hoop.properties.types.HoopURISerializable;
import edu.cmu.cs.in.ling.HoopPatternMatch;
import edu.cmu.cs.in.ling.HoopPatternMatcher;

/**
* 
*/
public class HoopMatcher extends HoopAnalyze implements HoopInterface
{    					
	private static final long serialVersionUID = -6162931325565067936L;
	
	public  HoopURISerializable patternFile=null;
	public  HoopBooleanSerializable allowOverlap=null;		
	private Boolean patternsLoaded=false;		
	private HoopPatternMatcher matcher=null;
	protected HoopIntegerSerializable batchSize=null;
		
	/**
	 *
	 */
    public HoopMatcher () 
    {
		setClassName ("HoopMatcher");
		debug ("HoopMatcher ()");
				
		//removeOutPort ("KV");
		
		matcher=new HoopPatternMatcher ();
		
		setHoopDescription ("Basic pattern matcher");		
		
		patternFile=new HoopURISerializable (this,"patternFile","");
		batchSize=new HoopIntegerSerializable (this,"batchSize",10);
		allowOverlap=new HoopBooleanSerializable (this,"allowOverlap",false);
    }
    /**
     * 
     */
    private Boolean loadPatterns ()
    {
    	debug ("loadPatterns ()");
    	
    	String aFile=patternFile.getPropValue();
    	
    	debug ("Loading pattern file: " + aFile);
    	
    	String text=HoopLink.fManager.loadContents(HoopLink.relativeToAbsolute(aFile));
    	
    	if (text==null)
    	{
    		this.setErrorString("Error! Unable to load pattern file: " + aFile);
    		return (false);
    	}
    	
    	//debug (text);
    	
    	if (matcher.loadFromText(text)==false)
    	{
    		this.setErrorString("Error! Can't load patterns from text file");
    		return (false);
    	}
    	
    	patternsLoaded=true;
    	
    	return (true);
    }    
	/**
	 * We assume here a stream of tokens or terms with a sliding window that
	 * gets reset when an end of sentence marker is found. End of sentence
	 * markers are either newlines, periods, question marks or exclamation
	 * marks. Discovered patterns are stored per sentence and then further
	 * refined to meet the semantic criteria encoded in this hoop or any
	 * derived hoops.
	 * 
	 * Based on:
	 * 
	 * Philip Gianfortoni, David Adamson, and Carolyn P. Ros�. 2011. Modeling 
	 * of stylistic variation in social media with stretchy patterns. In 
	 * Proceedings of the First Workshop on Algorithms and Resources for 
	 * Modelling of Dialects and Language Varieties (DIALECTS '11). Association 
	 * for Computational Linguistics, Stroudsburg, PA, USA, 49-59. 
	 * 
	 * Jacob Eisenstein and Regina Barzilay. 2008. Bayesian unsupervised topic 
	 * segmentation. In Proceedings of the Conference on Empirical Methods in 
	 * Natural Language Processing (EMNLP '08). Association for Computational 
	 * Linguistics, Stroudsburg, PA, USA, 334-343.
	 * 
	 */
	public Boolean runHoop (HoopBase inHoop)
	{		
		debug ("runHoop ()");
			
		if (patternsLoaded==false)
		{
			if (loadPatterns ()==false)
				return (false);
		}
		
		int bSize=batchSize.getPropValue();
		int counter=0;
		
		ArrayList <HoopKV> inData=inHoop.getData();
		
		if (inData!=null)
		{			
			for (int t=0;t<inData.size();t++)
			{
				HoopKV aKV=inData.get(t);
								
				updateProgressStatus (t+1,inData.size());
				
				if (processToken (aKV)==false)
					return (false);
			}			
		}
						
		return (true);				
	}	 
	/** 
	 * @param aKV
	 * @return
	 */
	private Boolean processToken (HoopKV aKV)
	{		
		ArrayList<Object> vals=aKV.getValuesRaw();

		for (int i=0;i<vals.size();i++)
		{
			ArrayList <HoopPatternMatch> matchList=matcher.matchPattern(vals, i);					
			
			if (matchList.size()>0)
			{					
				for (int j=0;j<matchList.size();j++)
				{
					HoopPatternMatch matched=matchList.get(j);
					
					HoopKVString newKV=new HoopKVString ();
					newKV.setKeyString(aKV.getKeyString());
				
					Double converter=matched.score;						
					newKV.setValue(converter.toString(),0);
				
					newKV.setValue(matched.matchedPattern,1);
										
					debug ("Adding new match KV: " + matched.matchedPattern);
			
					addKV (newKV);
				}	
			
				if (allowOverlap.getPropValue()==false)
				{
					// Skip over entire length of top pattern found

					HoopPatternMatch matched=matchList.get(0);
					
					if (matched!=null)
					{
						debug ("Skipping over pattern with size " + matched.size);
						
					}
				}
			}	
		}		
		
		return (true);
	}
	/**
	 * 
	 */
	protected void updateProgressStatus (int anIndex,int aTotal)
	{
		
		StringBuffer aStatus=new StringBuffer ();
		
		aStatus.append (" R: ");
		aStatus.append (anIndex);
		aStatus.append (" out of ");
		aStatus.append (aTotal);
		
		getVisualizer ().setExecutionInfo (aStatus.toString ());
	}
	/**
	 * 
	 */
	public HoopBase copy ()
	{
		return (new HoopMatcher ());
	}	
}
