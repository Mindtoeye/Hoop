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

import java.util.Random;

import edu.cmu.cs.in.hoop.properties.types.HoopEnumSerializable;
import edu.cmu.cs.in.hoop.properties.types.HoopStringSerializable;

/**
* 
*/
public class HoopLoadBase extends HoopIOBase implements HoopInterface
{
	private static final long serialVersionUID = 4117447861954274834L;
	private String content=null;
	
    public HoopStringSerializable batchSize=null;    
    public HoopStringSerializable queryMax=null;
	public HoopEnumSerializable mode=null; // LINEAR,SAMPLE
	
    protected Integer bSize=100;
    protected Integer bCount=0;
    protected Integer loadMax=100;
    protected Integer loadIndex=0;
    protected Integer originalSize=0;
	
	/**
	 *
	 */
    public HoopLoadBase () 
    {
		setClassName ("HoopLoadBase");
		debug ("HoopLoadBase ()");
		
		setHoopCategory ("Load");								
		setHoopDescription ("Abstract Hoop Loader");
		
		removeInPort ("KV");
		
    	batchSize=new HoopStringSerializable (this,"batchSize","100");    	
    	queryMax=new HoopStringSerializable (this,"queryMax","");
		mode=new HoopEnumSerializable (this,"mode","LINEAR,SAMPLE");
    }
	/**
	 * 
	 */
	public void reset ()
	{
		debug ("reset ()");
		
		super.reset ();
		
		bSize=-1;
		bCount=0;		
	    loadMax=100;
	    loadIndex=0;			
	}    
	/**
	 *
	 */
	public void setContent(String content) 
	{
		this.content = content;
	}
	/**
	 *
	 */
	public String getContent() 
	{
		return content;
	}
	/**
	 *
	 */
	public Boolean runHoop (HoopBase inHoop)
	{		
		debug ("runHoop ()");
		
				
		return (true);
	}	
	/**
	 * 
	 */
	protected void calculateIndexingSizes (int actualSize)
	{
		debug ("calculateIndexingSizes ("+actualSize+")");
		
		originalSize=actualSize;
		
		if (bSize==-1)
		{
			debug ("Prepping indexing variables ...");
			
			bSize=Integer.parseInt(batchSize.getPropValue());
			loadMax=Integer.parseInt(queryMax.getPropValue());
				
			if (actualSize<loadMax)
				loadMax=actualSize;
			
			if (actualSize<bSize)
				bSize=actualSize;
					
			if (bSize>loadMax)
				loadMax=bSize;
		}	
		else
			debug ("We're already in a run, no need to prep indexing variables");		
	}	
	/**
	 * 
	 */
	protected boolean checkLoopDone ()
	{
		if (bCount<bSize)
			return (false);
			
		return (true);
	}
	/**
	 * 
	 */
	protected boolean checkDone ()
	{
		if (loadIndex<loadMax)
			return (false);
		
		return (true);
	}
	/**
	 * 
	 */
	protected int getSample (int sampleN)
	{
		debug ("getSample ("+sampleN+")");
		
	    Random randomGenerator = new Random();
	    int randomInt = randomGenerator.nextInt(sampleN);

	    debug ("New sample " + randomInt + " out of " + sampleN);
	    
	    return (randomInt);
	} 	
	/**
	 * 
	 */
	public HoopBase copy ()
	{
		return (new HoopLoadBase ());
	}	
}
