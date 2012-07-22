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

import java.util.ArrayList;
import java.util.List;

import edu.cmu.cs.in.base.kv.HoopKV;
import edu.cmu.cs.in.base.kv.HoopKVInteger;
import edu.cmu.cs.in.base.HoopSimpleFeatureMaker;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.hoop.hoops.base.HoopInterface;
import edu.cmu.cs.in.hoop.hoops.base.HoopTransformBase;

/**
* 
*/
public class HoopSentence2Tokens extends HoopTransformBase implements HoopInterface
{    					
	/**
	 *
	 */
    public HoopSentence2Tokens () 
    {
		setClassName ("HoopSentence2Tokens");
		debug ("HoopSentence2Tokens ()");
										
		setHoopDescription ("Parse Sentences into Tokens");
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
			HoopSimpleFeatureMaker featureMaker=new HoopSimpleFeatureMaker ();
			
			for (int i=0;i<inData.size();i++)
			{
				HoopKVInteger aKV=(HoopKVInteger) inData.get(i);
				
				List<String> tokens=featureMaker.unigramTokenizeBasic (aKV.getValue());
								
				for (int j=0;j<tokens.size();j++)
				{				
					String aToken=tokens.get(j);
					addKV (new HoopKVInteger (j,aToken));
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
	public HoopBase copy ()
	{
		return (new HoopSentence2Tokens ());
	}		
}
