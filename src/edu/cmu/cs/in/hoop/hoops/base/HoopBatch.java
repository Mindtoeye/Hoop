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
import edu.cmu.cs.in.hoop.properties.types.HoopIntegerSerializable;

/**
* 
*/
public class HoopBatch extends HoopBase implements HoopInterface
{    				
	private static final long serialVersionUID = -4859643880252836376L;

	protected HoopIntegerSerializable batchSize=null;
	private Boolean breakout=false;
	private	int currentIndex=0;
	
	/**
	 *
	 */
    public HoopBatch () 
    {
		setClassName ("HoopBatch");
		debug ("HoopBatch ()");
		
		setHoopCategory ("Test");		
		setHoopDescription ("Abstract Hoop Batch Manager");
		
		batchSize=new HoopIntegerSerializable (this,"batchSize",1);
    }
	/**
	 * 
	 */
    public void reset ()
    {
    	debug ("reset ()");
    	
    	super.reset();
    	
    	breakout=false;
    	currentIndex=0;
    }   
	/**
	 *
	 */
	public Boolean runHoop (HoopBase inHoop)
	{		
		debug ("runHoop ()");

		ArrayList <HoopKV> inData=inHoop.getData();
		
		if (inData==null)
		{
			this.setErrorString("Error: no data available");
			return (false);
		}
		
		int bSize=batchSize.getPropValue();
		
		if (bSize==0)
			bSize=1;
						
		if (inData.size()<bSize)
			bSize=inData.size ();
									
		this.resetData();
			
		if (processKVBatch (inData,currentIndex,bSize)==false)
			return (false);
				
		updateProgressStatus (currentIndex+bSize,inData.size());
						
		currentIndex+=bSize;
			
		if ((currentIndex+bSize)>inData.size ())
		{
			breakout=true;
		}
		
		this.setDone(breakout);
													
		return (true);
	}
	/** 
	 * @param aKV
	 * @return
	 */
	protected Boolean processKVBatch (ArrayList <HoopKV> inData,int currentIndex,int batchSize)
	{	
		debug ("processKVBatch ()");
		
		// Override and use in child class
		
		debug ("Processing batch with size " + batchSize + " starting at: " + currentIndex);
		
		return (true);
	}
	/**
	 * 
	 */
	public HoopBase copy ()
	{
		return (new HoopBatch ());
	}	
}
