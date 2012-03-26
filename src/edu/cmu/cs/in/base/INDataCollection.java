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

public class INDataCollection extends INBase 
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
