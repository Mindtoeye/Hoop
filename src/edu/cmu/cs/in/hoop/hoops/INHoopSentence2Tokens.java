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

package edu.cmu.cs.in.hoop.hoops;

import java.util.ArrayList;
import java.util.List;

import edu.cmu.cs.in.base.kv.INKV;
import edu.cmu.cs.in.base.kv.INKVInteger;
import edu.cmu.cs.in.base.INSimpleFeatureMaker;
import edu.cmu.cs.in.hoop.base.INHoopBase;
import edu.cmu.cs.in.hoop.base.INHoopInterface;
import edu.cmu.cs.in.hoop.base.INHoopTransformBase;

/**
* 
*/
public class INHoopSentence2Tokens extends INHoopTransformBase implements INHoopInterface
{    					
	/**
	 *
	 */
    public INHoopSentence2Tokens () 
    {
		setClassName ("INHoopSentence2Tokens");
		debug ("INHoopSentence2Tokens ()");
										
		setHoopDescription ("Parse Sentences into Tokens");
    }
	/**
	 *
	 */
	public Boolean runHoop (INHoopBase inHoop)
	{		
		debug ("runHoop ()");
				
		ArrayList <INKV> inData=inHoop.getData();
		if (inData!=null)
		{		
			INSimpleFeatureMaker featureMaker=new INSimpleFeatureMaker ();
			
			for (int i=0;i<inData.size();i++)
			{
				INKVInteger aKV=(INKVInteger) inData.get(i);
				
				List<String> tokens=featureMaker.unigramTokenizeBasic (aKV.getValue());
								
				for (int j=0;j<tokens.size();j++)
				{				
					String aToken=tokens.get(j);
					addKV (new INKVInteger (j,aToken));
				}									
			}						
		}
		else
			return (false);
				
		return (true);
	}	
	/**
	 * 
	 */
	public INHoopBase copy ()
	{
		return (new INHoopSentence2Tokens ());
	}		
}
