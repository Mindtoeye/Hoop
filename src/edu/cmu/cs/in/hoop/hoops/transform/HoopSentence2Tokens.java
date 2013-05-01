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
import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.base.HoopSimpleFeatureMaker;
import edu.cmu.cs.in.hoop.HoopStatisticsPanel;
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
	private HoopStringSerializable splitCharacter=null;
	private HoopEnumSerializable targetTokenizer=null;
	private HoopEnumSerializable generateMode=null;
	
	/**
	 *
	 */
    public HoopSentence2Tokens () 
    {
		setClassName ("HoopSentence2Tokens");
		debug ("HoopSentence2Tokens ()");
										
		setHoopDescription ("Split Text into Tokens");

		targetTokenizer=new HoopEnumSerializable (this,"targetTokenizer","RegEx,Stanford,SplitOnCharacter");		
		removePunctuation=new HoopBooleanSerializable (this,"removePunctuation",true);
		splitRegEx=new HoopStringSerializable (this,"splitRegEx","\\W");
		splitCharacter=new HoopStringSerializable (this,"splitCharacter","|");
		generateMode=new HoopEnumSerializable (this,"generateMode","Add,New");
    }
	/**
	 *
	 */
	public Boolean runHoop (HoopBase inHoop)
	{		
		String result = "";
		debug ("runHoop ()");
		
		TokenizerFactory<Word> factory = PTBTokenizerFactory.newTokenizerFactory();
				
		ArrayList <HoopKV> inData=inHoop.getData();
		
		if (inData!=null)
		{		
			HoopSimpleFeatureMaker featureMaker=new HoopSimpleFeatureMaker ();
			
			result = "Number of sentences in input :: "+inData.size();
			for (int i=0;i<inData.size();i++)
			{
				//HoopKVInteger aKV=(HoopKVInteger) inData.get(i);
				HoopKV aKV=inData.get(i);
				
				HoopKV newKV=createKV (aKV);
				
				//debug ("Processing item: " + i + " with value: " + aKV.getValueAsString());
				
				//>------------------------------------------------------------------------
				
				if (targetTokenizer.getValue().equalsIgnoreCase("SplitOnCharacter")==true)
				{									
					//debug ("Using builtin tokenizer ...");
					
					List<String> tokens=featureMaker.unigramTokenizeOnCharacter (aKV.getValueAsString(),splitCharacter.getPropValue());
											
					//debug ("Extracted " + tokens.size());
					
					if (generateMode.getPropValue().equalsIgnoreCase("Add")==true)
					{
						//debug ("Generate mode is Add");
						
						//HoopKVInteger newToken=new HoopKVInteger ();
						
						for (int j=0;j<tokens.size();j++)
						{							
							String aToken=tokens.get(j);
										
							String strippedInput=aToken;
							
							//debug ("final input for new token: " + strippedInput);
						
							if (removePunctuation.getPropValue ()==true)
								strippedInput = aToken.replaceAll(splitRegEx.getValue(), "");
						
							//newToken.setKey (i);
							//newToken.setValue (strippedInput, j);
							Integer keyFormatter=i;
							newKV.setKeyString(keyFormatter.toString());
							newKV.setValue(strippedInput, j);
						}	
						
						//addKV (newToken);
						addKV (newKV);
					}
					else
					{
						//debug ("Generate mode is New");
						
						for (int j=0;j<tokens.size();j++)
						{				
							String aToken=tokens.get(j);
										
							String strippedInput=aToken;
						
							if (removePunctuation.getPropValue ()==true)											
								strippedInput = aToken.replaceAll(splitRegEx.getValue(), "");
							
							//debug ("final input for new token: " + strippedInput);
						
							if (this.reKey.getPropValue()==false)				
							{
								Integer keyFormatter=j;
								newKV.setKeyString(keyFormatter.toString());
								newKV.setValue(strippedInput);
								addKV (newKV);
								//addKV (new HoopKVInteger (j,strippedInput));
							}
							else
							{
								Integer keyFormatter=i;
								newKV.setKeyString(keyFormatter.toString());
								newKV.setValue(strippedInput);
								addKV (newKV);								
								//addKV (new HoopKVInteger (i,strippedInput));
							}
						}							
					}
				}					
				
				//>------------------------------------------------------------------------
				
				if (targetTokenizer.getValue().equalsIgnoreCase("RegEx")==true)
				{									
					//debug ("Using builtin tokenizer ...");
					
					List<String> tokens=featureMaker.unigramTokenizeBasic (aKV.getValueAsString());
											
					//debug ("Extracted " + tokens.size());
					
					if (generateMode.getPropValue().equalsIgnoreCase("Add")==true)
					{
						//debug ("Generate mode is Add");
						
						//HoopKVInteger newToken=new HoopKVInteger ();
						
						for (int j=0;j<tokens.size();j++)
						{							
							String aToken=tokens.get(j);
										
							String strippedInput=aToken;
							
							//debug ("final input for new token: " + strippedInput);
						
							if (removePunctuation.getPropValue ()==true)
								strippedInput = aToken.replaceAll(splitRegEx.getValue(), "");
						
							Integer keyFormatter=i;
							newKV.setKeyString(keyFormatter.toString());
							newKV.setValue(strippedInput, j);							
						}	
						
						//addKV (newToken);
						addKV (newKV);
					}
					else
					{
						//debug ("Generate mode is New");
						
						for (int j=0;j<tokens.size();j++)
						{				
							String aToken=tokens.get(j);
										
							String strippedInput=aToken;
						
							if (removePunctuation.getPropValue ()==true)											
								strippedInput = aToken.replaceAll(splitRegEx.getValue(), "");
							
							//debug ("final input for new token: " + strippedInput);
						
							if (this.reKey.getPropValue()==false)				
							{
								Integer keyFormatter=j;
								newKV.setKeyString(keyFormatter.toString());
								newKV.setValue(strippedInput);
								addKV (newKV);
								//addKV (new HoopKVInteger (j,strippedInput));
							}
							else
							{
								Integer keyFormatter=i;
								newKV.setKeyString(keyFormatter.toString());
								newKV.setValue(strippedInput);
								addKV (newKV);								
								//addKV (new HoopKVInteger (i,strippedInput));
							}						
						}							
					}
				}	
					
				//>------------------------------------------------------------------------
					
				if (targetTokenizer.getValue().equalsIgnoreCase("Stanford")==true)
				{					    
					//debug ("Using stanford tokenizer ...");
					
				    Tokenizer<Word> tokenizer = factory.getTokenizer(new StringReader(aKV.getValueAsString()));
					    
				    List<Word> sTokens=tokenizer.tokenize();
				    
				    //debug ("Extracted " + sTokens.size());
					    
				    for (int t=0;t<sTokens.size();t++)
				    {
				    	Word aTerm=sTokens.get(t);
					    	
						if (this.reKey.getPropValue()==false)				
						{
							Integer keyFormatter=t;
							newKV.setKeyString(keyFormatter.toString());
							newKV.setValue(aTerm.toString());
							addKV (newKV);
							//addKV (new HoopKVInteger (j,strippedInput));
						}
						else
						{
							Integer keyFormatter=i;
							newKV.setKeyString(keyFormatter.toString());
							newKV.setValue(aTerm.toString());
							addKV (newKV);								
							//addKV (new HoopKVInteger (i,strippedInput));
						}
				    }					    						
				}
					
				//>------------------------------------------------------------------------				
				
				updateProgressStatus (i,inData.size());
			}						
		}
		else
			return (false);
			
		HoopStatisticsPanel statsPanel;
		if(HoopLink.getWindow("Statistics")!=null){
			statsPanel=(HoopStatisticsPanel) HoopLink.getWindow("Statistics");
		}else{
			statsPanel=new HoopStatisticsPanel ();
		}
		HoopLink.addView ("Statistics",statsPanel,HoopLink.bottom);
    	statsPanel.appendString("\n"+result);
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
