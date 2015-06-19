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

import edu.cmu.cs.in.base.kv.HoopKV;
//import edu.cmu.cs.in.base.kv.HoopKVInteger;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.hoop.hoops.base.HoopInterface;
import edu.cmu.cs.in.hoop.hoops.base.HoopTransformBase;
import edu.cmu.cs.in.hoop.properties.types.HoopStringSerializable;

/**
* 
*/
public class HoopText2Sentence extends HoopTransformBase implements HoopInterface
{    	
	private static final long serialVersionUID = 6939927189106023532L;
	private HoopStringSerializable splitRegEx=null;
	
	private HoopStringSerializable skipFirstLines=null;
	private HoopStringSerializable ignoreLastLines=null;
	
	/**
	 *
	 */
    public HoopText2Sentence () 
    {
		setClassName ("HoopText2Sentence");
		debug ("HoopText2Sentence ()");
										
		setHoopDescription ("Parse File into Sentences");
		
		splitRegEx=new HoopStringSerializable (this,"splitRegEx","[\\r\\n]+");
		skipFirstLines=new HoopStringSerializable (this,"skipFirstLines","0");
		ignoreLastLines=new HoopStringSerializable (this,"ignoreLastLines","0");
    }
	/**
	 *
	 */
	public Boolean runHoop (HoopBase inHoop)
	{		
		debug ("runHoop ()");
		
		ArrayList <HoopKV> inData=inHoop.getData();
		
		Integer skipLines=Integer.parseInt(this.skipFirstLines.getPropValue());
		Integer ignoreLines=Integer.parseInt(this.ignoreLastLines.getPropValue());
		
		debug ("Skipping the first " + skipLines + " lines");
		
		if (inData!=null)
		{			
			if (inData.size()==0)
			{
				this.setErrorString("Error: data size is 0");
				return (false);
			}
			
			for (int t=0;t<inData.size();t++)
			{
				HoopKV aKV=inData.get(t);

				String aFullText=aKV.getValueAsString();
				
				if (aFullText!=null)
				{
					String lines[] =aFullText.split(splitRegEx.getValue());
				
					for (int i=0;i<lines.length;i++)
					{
						if ((i>=skipLines) && (i<(lines.length-ignoreLines)))
						{													
							HoopKV sentenceKV=createKV (aKV);
						
							if (this.reKey.getPropValue()==true)
							{
								Integer createdKey=(t+1)*(i+1);
							
								sentenceKV.setKeyString (createdKey.toString());
							}
						
							String newSentence=lines [i];
						
							sentenceKV.setValue(newSentence);
							sentenceKV.begin=0;
							sentenceKV.end=newSentence.length();
				
							addKV (sentenceKV);
						}
					}	
				}					
				
				updateProgressStatus (t,inData.size());
			}		
		}		
		else
		{
			this.setErrorString("Error: no data found in incoming hoop");
			return (false);
		}	
						
		return (true);
	}	
	/**
	 * 
	 */
	public HoopBase copy ()
	{
		return (new HoopText2Sentence ());
	}		
}

