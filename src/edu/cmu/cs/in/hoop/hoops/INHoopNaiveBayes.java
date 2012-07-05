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

import edu.cmu.cs.in.base.kv.INKV;
import edu.cmu.cs.in.base.kv.INKVInteger;
import edu.cmu.cs.in.hoop.base.INHoopAnalyze;
import edu.cmu.cs.in.hoop.base.INHoopBase;
import edu.cmu.cs.in.hoop.base.INHoopInterface;

/**
* 
*/
public class INHoopNaiveBayes extends INHoopAnalyze implements INHoopInterface
{    				
	/**
	 *
	 */
    public INHoopNaiveBayes () 
    {
		setClassName ("INHoopNaiveBayes");
		debug ("INHoopNaiveBayes ()");
				
		removeOutPort ("KV");
		
		setHoopDescription ("Classify Using Naive Bayes");
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
			//StringBuffer formatted=new StringBuffer ();
			
			for (int t=0;t<inData.size();t++)
			{
				INKVInteger aKV=(INKVInteger) inData.get(t);
								
				//formatted.append(aKV.getKeyString ());
				
				ArrayList<Object> vals=aKV.getValuesRaw();
				
				for (int i=0;i<vals.size();i++)
				{
					//formatted.append(",");
					//formatted.append(vals.get(i));
				}
				
				//formatted.append("\n");
			}			
		}	
						
		return (true);				
	}	 
	/**
	 * 
	 */
	public INHoopBase copy ()
	{
		return (new INHoopNaiveBayes ());
	}	
}
