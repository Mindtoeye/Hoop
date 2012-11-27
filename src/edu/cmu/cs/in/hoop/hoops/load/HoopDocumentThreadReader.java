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

package edu.cmu.cs.in.hoop.hoops.load;

import java.util.ArrayList;

import com.sleepycat.collections.StoredMap;

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.base.kv.HoopKV;
import edu.cmu.cs.in.base.kv.HoopKVDocument;
import edu.cmu.cs.in.base.kv.HoopKVLong;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.hoop.hoops.base.HoopLoadBase;

public class HoopDocumentThreadReader extends HoopLoadBase
{
	private static final long serialVersionUID = 3069547626921137451L;
		
	/**
	 *
	 */ 
	public HoopDocumentThreadReader () 
	{		
		setClassName ("HoopDocumentThreadReader");
		debug ("HoopDocumentThreadReader ()");
		
		setHoopDescription ("Load Documents for a specific thread ID");
				
		//removeInPort ("KV");
		enableProperty ("URI",false);		
	}
	/**
	 *
	 */
	public Boolean runHoop (HoopBase inHoop)
	{		
		debug ("runHoop ()");
			
		StoredMap<Long, HoopKVLong> threadMap=HoopLink.dataSet.getThreads();
		
		if (threadMap==null)
		{
			this.setErrorString("Error: no thread dataset available for documents");
			return (false);
		}
		
		ArrayList<HoopKV> data=inHoop.getData();
		
		for (int i=0;i<data.size();i++)
		{
			HoopKVDocument aDocument=(HoopKVDocument) data.get(i);
			
			debug ("Locating thread: " + aDocument.threadID.getValue());
						
			Long targetThread=Long.parseLong(aDocument.threadID.getValue());
			
			HoopKVLong aThread=threadMap.get(targetThread);
			
			if (aThread!=null)
			{
				debug ("Retrieving document IDs ...");
			
				ArrayList <Object> aDocIDList=aThread.getValuesRaw ();
				
				for (int j=0;j<aDocIDList.size();j++)
				{
					debug ("Document ID: " + aDocIDList.get(j));
					
				}			
			}
		}
						
		return (true);
	}
	/**
	 * 
	 */
	public HoopBase copy ()
	{
		return (new HoopDocumentThreadReader ());
	}
}
