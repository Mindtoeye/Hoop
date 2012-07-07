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

package edu.cmu.cs.in.controls;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import edu.cmu.cs.in.base.INFeatureMakerBase;
import edu.cmu.cs.in.base.INBase;

/** 
 * 
 */
public class INVisualSentence extends INBase
{    		    
	public String text="";
	private INVisualClass classification=null;	
	private ArrayList <INFeaturePerspective> perspectives=null;
	private int searchWindow=5;
	
	/**
	 *
	 */
    public INVisualSentence () 
    {
		setClassName ("INVisualSentence");
		//debug ("INVisualSentence ()");
		
		setPerspectives(new ArrayList<INFeaturePerspective> ());
		addPerspective (new INFeaturePerspective ("Unigrams"));
		addPerspective (new INFeaturePerspective ("Sentinet"));
		addPerspective (new INFeaturePerspective ("Rules"));
    }
	/**
	 *
	 */
    public void addPerspective (INFeaturePerspective aPersp)
    {
    	perspectives.add(aPersp);
    }
	/**
	 *
	 */
    public void addPerspective (String aType)
    {
    	INFeaturePerspective newPerspective=new INFeaturePerspective (aType);
    	perspectives.add(newPerspective);
    }    
	/**
	 *
	 */        
	public void setPerspectives(ArrayList <INFeaturePerspective> perspectives) 
	{
		this.perspectives = perspectives;
	}
	/**
	 *
	 */	
	public ArrayList <INFeaturePerspective> getPerspectives() 
	{
		return perspectives;
	}    
	/**
	 *
	 */
    public ArrayList<INVisualFeature> getFeatures ()
    {
    	if (perspectives==null)
    		return (null);
    	    	
    	return (perspectives.get(0).getFeatures ());
    }
	/**
	 *
	 */
    public INFeaturePerspective getPerspective (String aType)
    {
    	if (perspectives==null)
    		return (null);
  	    	
    	for (int i=0;i<perspectives.size();i++)
    	{
    		INFeaturePerspective persp=perspectives.get(i);
    		if (persp.getPerspectiveName().equals(aType)==true)
    			return (persp);
    	}
    	
    	return (null);
    }    
	/**
	 *
	 */
    public void assignClass (INVisualClass aClass)
    {
    	classification=aClass;
    }
	/**
	 *
	 */
    public INVisualClass getClassification ()
    {
    	return classification;
    }    
	/**
	 *
	 */
    public ArrayList<INVisualFeature> getFeatures (String aType)
    {
    	for (int i=0;i<perspectives.size();i++)
    	{
    		INFeaturePerspective persp=perspectives.get(i);
    		if (persp.getPerspectiveName().equals(aType)==true)
    			return (persp.getFeatures());
    	}
    	    		
    	return (null);
    }    
	/**
	 *
	 */
    public void buildUnigramFeatureList (String aSentence,String aPerspective) 
    {    	
    	//debug ("buildUnigramFeatureList ()");
    	
    	text=aSentence;
    	
    	ArrayList <INVisualFeature> features=getFeatures (aPerspective);
    	if (features==null)
    	{
    		// not found yet, let's create it
    		addPerspective (new INFeaturePerspective (aPerspective));
    		features=getFeatures (aPerspective); // Not really proper code but it works
    	}
    	    	    	
    	INFeatureMakerBase maker=new INFeatureMakerBase ();
    	List<String> feats=(List<String>) maker.unigramTokenize (aSentence);
    	
    	for (int i=0;i<feats.size();i++)
    	{
    		INVisualFeature newFeature=new INVisualFeature ();
    		newFeature.text=(String) feats.get(i);
    		features.add(newFeature);
    	}
    }
	/**
	 *
	 */
    public int buildSentimentFeatureList (String aPerspective,ArrayList <INVisualSentinetFeature> sents) 
    {    	
	   //debug ("buildSentimentFeatureList ()");
	   
	   int hitcount=0;
	   
	   if (text==null)
	   {
		   debug ("Error: this feature builder assumes a text has already been assigned and tokenized");
		   return 0;
	   }
   	   	
	   ArrayList <INVisualFeature> features=getFeatures (aPerspective);
	   if (features==null)
	   {
		   // not found yet, let's create it
		   addPerspective (new INFeaturePerspective (aPerspective));
		   features=getFeatures (aPerspective); // Not really proper code but it works
	   }
   	    	    		   
	   for (int i=0;i<sents.size();i++)
	   {
		   INVisualSentinetFeature sentimentTerm=sents.get(i);
		   if (text.contains(sentimentTerm.text)==true)
		   {
			   //debug ("Found sentiment feature "+sentimentTerm.text+" in document");
			   features.add(sentimentTerm);
			   hitcount++;
		   }
	   }
	   
	   return (hitcount);
    }    
	/**
	 *
	 */
    public int buildRuleFeatureList (String aPerspective,ArrayList <INVisualRuleFeature> rules) 
    {    	
    	//debug ("buildRuleFeatureList ()");
	   
    	int hitcount=0;
	   
    	if (text==null)
    	{
    		debug ("Error: this feature builder assumes a text has already been assigned and tokenized");
    		return 0;
    	}
  	   	
    	ArrayList <INVisualFeature> features=getFeatures (aPerspective);
    	if (features==null)
    	{
    		// not found yet, let's create it
    		addPerspective (new INFeaturePerspective (aPerspective));
    		features=getFeatures (aPerspective); // Not really proper code but it works
    	}
  	    	    		   
    	for (int i=0;i<rules.size();i++)
    	{
    		INVisualRuleFeature rule=rules.get(i);
    		Boolean result=matchRule (rule);
    		if (result==true)
			   features.add(rule);
		   
    	}
	   
    	return (hitcount);
    }        
	/**
	 *
	 */
    private Boolean matchRule (INVisualRuleFeature aRule)
    {
    	debug ("matchRule ("+aRule.getDescription()+")");
    	    	
    	ArrayList<String> breaker=aRule.getBreakFeatures();
    	ArrayList<INVisualFeature> breakMatch=null;
    	
    	if (breaker==null)
    	{
    		debug ("Internal error: no breaking features available for rule: " + aRule.getDescription());
    		return (false);
    	}
    	
    	if (breaker.isEmpty()==true)
    	{
    		debug ("Internal error: no breaking features available for rule: " + aRule.getDescription());
    		return (false);
    	}    	
    	
    	ArrayList<INVisualFeature> unis=getFeatures ("Unigrams");
    	if (unis==null)
    	{
    		debug ("Internal error: unable to find unigram perspective in visual sentence/document");
    		return (false);
    	}
    	    	
    	// First locate and align the break term(s) with the unigram list. The
    	// following method returns a list of sequential visual features in the
    	// list of unigrams for this sentence.
    	
    	breakMatch=findBreak (unis,breaker);
    	
    	if (breakMatch==null)
    	{
    		debug ("Bounce: no break match found");
    		return (false);
    	}
    	
    	// Then find the pronouns for the C1 clause around it using a window of 5
    	
    	INVisualFeature firstFeature=breakMatch.get(0);
    	int backIndex=unis.indexOf(firstFeature);
    	
    	for (int i=backIndex;i<(backIndex-searchWindow);i--)
    	{
    		INVisualFeature tester=unis.get(i);
    		if (tester!=null)
    		{
    			if (tester.isPronoun()==true)
    			{
    				
    			}
    		}	
    	}
    	
    	// Then find the pronouns for the C2 clause around it using a window of 5
    	
    	INVisualFeature lastFeature=breakMatch.get(breakMatch.size()-1);
    	
    	int forwardIndex=unis.indexOf(lastFeature);
    	
    	for (int j=forwardIndex;j<(forwardIndex+searchWindow);j++)
    	{
    		INVisualFeature tester=unis.get(j);
    		if (tester!=null)
    		{
    			if (tester.isPronoun()==true)
    			{
    				
    			}
    		}	    		
    	}    	
    	
    	// Optionally ensure there are appropriate verbs in the clauses
    	
    	// If nothing went wrong along the way then we should have a match!
    	
    	return (true);
    }
	/**
	 *
	 */
    private ArrayList<INVisualFeature> findBreak (ArrayList<INVisualFeature> aUnis,
    												ArrayList<String> aBreak)
    {
    	debug ("findBreak ()");
    	
    	ListIterator<INVisualFeature> unigramIterator = aUnis.listIterator();
    	//ListIterator<String> breakIterator = aBreak.listIterator();
    	ArrayList<INVisualFeature> breakMatch=new ArrayList<INVisualFeature> ();
    	
    	int breakIndex=0;
    	
    	String first=aBreak.get(breakIndex);
    	
    	// First find a starting point, or point of attachment
    	
    	INVisualFeature uniTest=null;
    	
    	while (unigramIterator.hasNext())
    	{
    		uniTest=(INVisualFeature) unigramIterator.next();
    		if (uniTest.text.equals(first))
    		{
    			breakMatch.add(uniTest);
    		}
    	}
    	
    	if (breakMatch.isEmpty()==true)
    	{
    		debug ("Can't get a break");
    		return (null);
    	}
    	
    	// Quick optimization ...
    	
    	if (aBreak.size()==1)
    		return (breakMatch);
    	
    	// Then try to follow if we can ...
    	
    	int uniIndex=aUnis.indexOf(uniTest);
    	   	
    	uniIndex++;
    	breakIndex++;
    	
    	first=aBreak.get(breakIndex);
    	uniTest=aUnis.get(uniIndex);
    	
    	if (uniTest==null)
    	{
    		debug ("We're probably seeking beyond the text, aborting ...");
    		return null;
    	}
    	
    	while (uniTest.text.equals(first) && (breakIndex<aBreak.size()))
    	{
    		breakMatch.add(uniTest);
    		
        	uniIndex++;
        	breakIndex++;
        	
        	first=aBreak.get(breakIndex);
        	uniTest=aUnis.get(uniIndex);
        	
        	if (uniTest==null)
        	{
        		debug ("We're probably seeking beyond the text, aborting ...");
        		return null;
        	}        	
    	}
    	
    	// We should now have a match with at least one entry
    	
    	return (breakMatch);
    }	
}
