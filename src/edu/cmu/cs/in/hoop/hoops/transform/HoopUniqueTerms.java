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
//import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import edu.cmu.cs.in.base.kv.HoopKV;
import edu.cmu.cs.in.base.kv.HoopKVInteger;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.hoop.hoops.base.HoopInterface;
import edu.cmu.cs.in.hoop.hoops.base.HoopTransformBase;

/**
* 
*/
public class HoopUniqueTerms extends HoopTransformBase implements HoopInterface
{    				
	/**
	 *
	 */
    public HoopUniqueTerms () 
    {
		setClassName ("HoopUniqueTerms");
		debug ("HoopUniqueTerms ()");
				
		setHoopDescription ("Sort into unique list of KVs");		
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
			Hashtable<String,HoopKVInteger> uniqueHash = new Hashtable<String,HoopKVInteger>();

			for (int i=0;i<inData.size();i++)
			{
				HoopKVInteger aKV=(HoopKVInteger) inData.get(i);

				/* Right now we're assuming that a previous hoop has turned
				 * all the entries to lowercase. We might have to make this
				 * an option for this hoop to first do a transform.				 
				 */
				
				if (uniqueHash.containsKey (aKV.getValue())==true)
				{
					HoopKVInteger temper=uniqueHash.get(aKV.getValue());
					temper.incKey();
					
					ArrayList<Object> unStemmed=aKV.getValuesRaw();
					
					for (int t=0;t<unStemmed.size();t++)
					{
						String original=(String) unStemmed.get(t);
						temper.addValueAsUniqueString(original);
					}					
				}
				else								
				{
					aKV.setKey(1);
					uniqueHash.put(aKV.getValue(),aKV);
				}
				
				updateProgressStatus (i,inData.size());
			}
			
			Iterator<Map.Entry<String,HoopKVInteger>> it = uniqueHash.entrySet().iterator();

			int index=0;
			
			while (it.hasNext()) 
			{
				Map.Entry<String, HoopKVInteger> entry = it.next();
								
				HoopKVInteger newKV=new HoopKVInteger (index,entry.getKey());
				Integer termCount=entry.getValue().getKey();
				newKV.setValue(entry.getValue().getValue());								
				newKV.setValue(termCount.toString(),1);
				
				ArrayList<Object> stemmers=entry.getValue().getValuesRaw();
				
				for (int j=1;j<stemmers.size();j++)
				{					
					String original=(String) stemmers.get(j);
										
					newKV.addValueAsUniqueString(original);
				}				
							  
				addKV (newKV);			  
			  
				index++;
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
		return (new HoopUniqueTerms ());
	}	
}
