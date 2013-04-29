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

package edu.cmu.cs.in.hoop.hoops.base;

import java.util.ArrayList;

import edu.cmu.cs.in.base.kv.HoopKV;
import edu.cmu.cs.in.controls.HoopProgressPainter;
import edu.cmu.cs.in.hoop.editor.HoopVisualRepresentation;

/**
* 
*/
public class HoopCopy extends HoopBatch implements HoopInterface
{    					
	private static final long serialVersionUID = 4922606095942183740L;
	
	/**
	 *
	 */
    public HoopCopy () 
    {
		setClassName ("HoopCopy");
		debug ("HoopCopy ()");
				
		setHoopDescription ("Abstract Hoop KV copier");		
    }   
	/** 
	 * @param aKV
	 * @return
	 */
	protected Boolean processKVBatch (ArrayList <HoopKV> inData,int currentIndex,int batchSize)
	{	
		debug ("processKVBatch ()");
		
		HoopVisualRepresentation viz=this.getVisualizer();
		
		HoopProgressPainter progress=viz.getProgressPainter();
		
		progress.setLevels(0,inData.size());
		
		for (int i=0;i<currentIndex;i++)
		{
			HoopKV aKV=inData.get(i);
						
			updateProgressStatus (i,inData.size());
			
			addKV (aKV);
		}
				
		return (true);
	}
	/**
	 * 
	 */
	public HoopBase copy ()
	{
		return (new HoopCopy ());
	}	
}
