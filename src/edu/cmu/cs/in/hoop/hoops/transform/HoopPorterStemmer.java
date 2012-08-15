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

import edu.cmu.cs.in.base.HoopPorterStemmerOriginal;
import edu.cmu.cs.in.base.kv.HoopKV;
import edu.cmu.cs.in.base.kv.HoopKVInteger;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.hoop.hoops.base.HoopInterface;
import edu.cmu.cs.in.hoop.hoops.base.HoopTransformBase;
import edu.cmu.cs.in.hoop.properties.types.HoopStringSerializable;

/**
* 
*/
public class HoopPorterStemmer extends HoopTransformBase implements HoopInterface
{    	
	
	public	HoopStringSerializable minChars = null;
	private HoopPorterStemmerOriginal stemmer=null;
	
	/**
	 *
	 */
    public HoopPorterStemmer () 
    {
		setClassName ("HoopPorterStemmer");
		debug ("HoopPorterStemmer ()");
				
		setHoopDescription ("Stems input KVs using the Porter Stemmer");
		
		stemmer=new HoopPorterStemmerOriginal ();
		minChars=new HoopStringSerializable (this,"minChars","5");		
    }
	/**
	 *
	 */
	public Boolean runHoop (HoopBase inHoop)
	{		
		debug ("runHoop ()");
		
		Integer minCheck=Integer.parseInt(minChars.getValue());
				
		ArrayList <HoopKV> inData=inHoop.getData();
		if (inData!=null)
		{					
			for (int i=0;i<inData.size();i++)
			{
				HoopKVInteger aKV=(HoopKVInteger) inData.get(i);
				HoopKVInteger newKV=new HoopKVInteger ();
				
				if (aKV.getValue().length()>=minCheck)
				{
					newKV.setKey(i);
					newKV.setValue(stemmer.stem(aKV.getValue()));
					// Keep a copy of the original in the first extra value slot
					newKV.setValue(aKV.getValue(),1); 
					addKV (newKV);
				}
				else
				{
					addKV (new HoopKVInteger (i,aKV.getValue()));
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
		return (new HoopPorterStemmer ());
	}	
}
