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

package edu.cmu.cs.in.base;

import java.util.ArrayList;
import java.util.HashMap;
//import java.util.List;

//import edu.cmu.side.uima.UIMAToolkit.DocumentList;
//import edu.cmu.side.uima.type.INSegment;

import edu.cmu.cs.in.controls.INVisualClass;
import edu.cmu.cs.in.controls.INVisualFeature;
import edu.cmu.cs.in.controls.INVisualRuleFeature;
import edu.cmu.cs.in.controls.INVisualSentence;
import edu.cmu.cs.in.controls.INVisualSentinetFeature;

public class INDataCollection extends INFeatureMatrixBase 
{	
	//public DocumentList context=null;
	//public List<INSegment> segments=null;
	
	public String [] annotations=null;	
	public ArrayList<INVisualSentence> texts=null;
	public HashMap<String, INVisualFeature> unigrams=null;	
	public ArrayList<INVisualClass> classes=null;	
	public ArrayList<INVisualSentinetFeature> sents=null;
	public ArrayList<INVisualRuleFeature> rules=null;
	
	// Language support tools
	
	public static ArrayList <String> pronouns=null;
	
	/**------------------------------------------------------------------------------------
	 *
	 */
	public INDataCollection () 
	{
		setClassName ("INDataCollection");
		debug ("INDataCollection ()");
		
		texts=new ArrayList<INVisualSentence> ();
		classes=new ArrayList<INVisualClass> ();	
		sents=new ArrayList<INVisualSentinetFeature> ();
		rules=new ArrayList<INVisualRuleFeature> ();
		
		if (pronouns==null)
		{
			setPronouns(new ArrayList<String>());
			
			pronouns.add("i");
			pronouns.add("we");
			
			pronouns.add("me");
			pronouns.add("us");
			
			pronouns.add("my");
			pronouns.add("our");
			
			pronouns.add("myself");
			pronouns.add("ourselves");			
			
			pronouns.add("mine");
			pronouns.add("ours");
			
			pronouns.add("he");
			pronouns.add("she");

			pronouns.add("him");
			pronouns.add("her");
			
			pronouns.add("they");
			pronouns.add("them");
		}	
	}
	/**------------------------------------------------------------------------------------
	 *
	 */	
	public void setPronouns(ArrayList <String> aPronouns) 
	{
		//this.pronouns = pronouns;
		INDataCollection.pronouns=aPronouns;
	}
	/**------------------------------------------------------------------------------------
	 *
	 */	
	public ArrayList <String> getPronouns() 
	{
		//return pronouns;
		return INDataCollection.pronouns;
	}	
	//-------------------------------------------------------------------------------------	
}
