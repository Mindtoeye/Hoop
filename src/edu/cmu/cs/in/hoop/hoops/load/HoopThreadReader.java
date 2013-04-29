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

import java.util.Iterator;

import com.sleepycat.collections.StoredMap;

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.base.kv.HoopKVLong;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.hoop.hoops.base.HoopLoadBase;
import edu.cmu.cs.in.search.HoopDataSet;

public class HoopThreadReader extends HoopLoadBase
{	
	private static final long serialVersionUID = 7898298666315596078L;
			
	private StoredMap<Long,HoopKVLong> threadData=null;
	private Iterator<HoopKVLong> dbIterator = null;
	
	/**
	 *
	 */ 
	public HoopThreadReader () 
	{		
		setClassName ("HoopThreadReader");
		debug ("HoopThreadReader ()");
		
		setHoopDescription ("Load threads from Document DB");
				
		removeInPort ("KV");
		enableProperty ("URI",false);		
	}
	/**
	 * 
	 */
	public void reset ()
	{
		dbIterator=null;
		threadData=null;
		
		debug ("Mapping project path ("+getProjectPath ()+") to db dir ...");
		
		if (HoopLink.dataSet==null)
		{
			HoopLink.dataSet=new HoopDataSet ();
			HoopLink.dataSet.checkDB ();
		}
		else
			HoopLink.dataSet.checkDB ();
		
		if (HoopLink.dataSet!=null)
		{
			threadData=HoopLink.dataSet.getThreads();
			
			dbIterator = threadData.values().iterator();
		}				
	}
	/**
	 *
	 */
	public Boolean runHoop (HoopBase inHoop)
	{		
		debug ("runHoop ()");
		
		if (threadData==null)
		{
			this.setErrorString("Error: thread data is null");
			return (false);
		}
										
		int actualSize=threadData.size();
		
		calculateIndexingSizes (actualSize);
						
		if (mode.getValue().equals("LINEAR")==true)
			processLinear ();
		else
			processSample ();
		
		getVisualizer ().setExecutionInfo (" R: " + bCount + " = ("+loadIndex+") out of " + loadMax);
						
		return (true);
	}
	/**
	 * 
	 */
	private void processLinear ()
	{
		debug ("processLinear ()");
				
		bCount=0;
		
		while (checkLoopDone ()==false)		
		{			
			if (dbIterator.hasNext()==true)
			{			
				if (loadDataObject (loadIndex)==false)
					return;
			}	
			
			loadIndex++; // Update total index
			bCount++; // Update batch count
		}		
		
		if (checkDone ()==false)
			this.setDone(false);
	}
	/**
	 * 
	 */
	private void processSample ()
	{
		debug ("processSample ()");
		
		bCount=0;
		
		while (checkLoopDone ()==false)		
		{			
			if (loadDataObject (getSample (originalSize))==false)
				return;
			
			loadIndex++; // Update total index
			bCount++; // Update batch count
		}		
		
		if (checkDone ()==false)
			this.setDone(false);
	}	
	/**
	 * 
	 */
	protected boolean loadDataObject (int anIndex)
	{
		debug ("loadDataObject ("+anIndex+")");
		
		HoopKVLong aDoc=(HoopKVLong) dbIterator.next();
				
		processThread (aDoc);		
		
		return (true);
	}	
	/**
	 * 
	 */
	private void processThread (HoopKVLong aThread)
	{
		debug ("processDocument ()");
			
		this.addKV(aThread);
	}
	/**
	 * 
	 */
	public HoopBase copy ()
	{
		return (new HoopThreadReader ());
	}
}
