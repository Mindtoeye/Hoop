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

package edu.cmu.cs.in.hoop.hoops.transform;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.objectbank.TokenizerFactory;
import edu.stanford.nlp.process.PTBTokenizer.PTBTokenizerFactory;
import edu.stanford.nlp.process.Tokenizer;

import edu.cmu.cs.in.base.kv.HoopKV;
import edu.cmu.cs.in.base.kv.HoopKVInteger;
import edu.cmu.cs.in.base.HoopSimpleFeatureMaker;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.hoop.hoops.base.HoopInterface;
import edu.cmu.cs.in.hoop.hoops.base.HoopTransformBase;
import edu.cmu.cs.in.hoop.properties.types.HoopBooleanSerializable;
import edu.cmu.cs.in.hoop.properties.types.HoopEnumSerializable;
import edu.cmu.cs.in.hoop.properties.types.HoopStringSerializable;

/**
* 
*/
public class HoopSentence2Tokens extends HoopTransformBase implements HoopInterface
{    						
	private static final long serialVersionUID = -8312790693714962219L;
	private HoopBooleanSerializable removePunctuation=null; // TRUE,FALSE	
	private HoopStringSerializable splitRegEx=null;
	private HoopEnumSerializable targetTokenizer=null;
	private HoopEnumSerializable generateMode=null;
	
	/**
	 *
	 */
    public HoopSentence2Tokens () 
    {
		setClassName ("HoopSentence2Tokens");
		debug ("HoopSentence2Tokens ()");
										
		setHoopDescription ("Parse Sentences into Tokens");
		
		removePunctuation=new HoopBooleanSerializable (this,"removePunctuation",true);
		splitRegEx=new HoopStringSerializable (this,"splitRegEx","\\W");
		targetTokenizer=new HoopEnumSerializable (this,"targetTokenizer","Builtin,Stanford");
		generateMode=new HoopEnumSerializable (this,"generateMode","Add,New");
    }
	/**
	 *
	 */
	public Boolean runHoop (HoopBase inHoop)
	{		
		debug ("runHoop ()");
		
		TokenizerFactory<Word> factory = PTBTokenizerFactory.newTokenizerFactory();
				
		ArrayList <HoopKV> inData=inHoop.getData();
		if (inData!=null)
		{		
			HoopSimpleFeatureMaker featureMaker=new HoopSimpleFeatureMaker ();
			
			for (int i=0;i<inData.size();i++)
			{
				HoopKVInteger aKV=(HoopKVInteger) inData.get(i);
				
				//>------------------------------------------------------------------------
				
				if (targetTokenizer.getValue().equalsIgnoreCase("Builtin")==true)
				{									
					List<String> tokens=featureMaker.unigramTokenizeBasic (aKV.getValue());
											
					if (generateMode.getPropValue().equalsIgnoreCase("Add")==true)
					{
						HoopKVInteger newToken=new HoopKVInteger ();
						
						addKV (newToken);
						
						for (int j=0;j<tokens.size();j++)
						{				
							String aToken=tokens.get(j);
										
							String strippedInput=aToken;
						
							if (removePunctuation.getPropValue ()==true)											
								strippedInput = aToken.replaceAll(splitRegEx.getValue(), "");
						
							newToken.setKey (i);
							newToken.setValue (strippedInput, j);
						}	
					}
					else
					{
						for (int j=0;j<tokens.size();j++)
						{				
							String aToken=tokens.get(j);
										
							String strippedInput=aToken;
						
							if (removePunctuation.getPropValue ()==true)											
								strippedInput = aToken.replaceAll(splitRegEx.getValue(), "");
						
							if (this.reKey.getPropValue()==false)						
								addKV (new HoopKVInteger (j,strippedInput));
							else
								addKV (new HoopKVInteger (i,strippedInput));							
						}							
					}
				}	
					
				//>------------------------------------------------------------------------
					
				if (targetTokenizer.getValue().equalsIgnoreCase("Stanford")==true)
				{					    
				    Tokenizer<Word> tokenizer = factory.getTokenizer(new StringReader(aKV.getValue()));
					    
				    List<Word> sTokens=tokenizer.tokenize();
					    
				    for (int t=0;t<sTokens.size();t++)
				    {
				    	Word aTerm=sTokens.get(t);
					    	
				    	if (this.reKey.getPropValue()==false)
				    		addKV (new HoopKVInteger (t,aTerm.toString()));
				    	else
				    		addKV (new HoopKVInteger (i,aTerm.toString()));
				    }
					    
				    //debug (tokenizer.tokenize());						
				}
					
				//>------------------------------------------------------------------------								
			}						
		}
		else
			return (false);
				
		return (true);
	}	
	/**
	 * 
	 */
	public HoopBase copy ()
	{
		return (new HoopSentence2Tokens ());
	}		
}
