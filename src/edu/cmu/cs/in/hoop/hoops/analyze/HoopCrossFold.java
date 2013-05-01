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

package edu.cmu.cs.in.hoop.hoops.analyze;

import java.util.ArrayList;

import edu.cmu.cs.in.base.kv.HoopKV;
import edu.cmu.cs.in.base.kv.HoopKVInteger;
import edu.cmu.cs.in.hoop.hoops.base.HoopAnalyze;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.hoop.hoops.base.HoopInterface;

/**
* 
*/
public class HoopCrossFold extends HoopAnalyze implements HoopInterface
{    				
	private static final long serialVersionUID = -5912525306830079787L;
	
	/**
	 *
	 */
    public HoopCrossFold () 
    {
		setClassName ("HoopCrossFold");
		debug ("HoopCrossFold ()");
				
		removeOutPort ("KV");
		
		setHoopDescription ("Crossfold separation");
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
			//StringBuffer formatted=new StringBuffer ();
			
			for (int t=0;t<inData.size();t++)
			{
				HoopKVInteger aKV=(HoopKVInteger) inData.get(t);
								
				//formatted.append(aKV.getKeyString ());
				
				ArrayList<Object> vals=aKV.getValuesRaw();
				
				for (int i=0;i<vals.size();i++)
				{
					//formatted.append(",");
					//formatted.append(vals.get(i));
				}
				
				//formatted.append("\n");
				
				updateProgressStatus (t,inData.size());
			}			
		}	
						
		return (true);				
	}	 
	/**
	 * 
	 */
	public HoopBase copy ()
	{
		return (new HoopCrossFold ());
	}	
}
