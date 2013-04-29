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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.cmu.cs.in.base.kv.HoopKV;
import edu.cmu.cs.in.base.kv.HoopKVString;
import edu.cmu.cs.in.hoop.hoops.base.HoopAnalyze;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.hoop.hoops.base.HoopInterface;

/**
* 
*/
public class HoopRoomAnnotator extends HoopAnalyze implements HoopInterface
{    								
	private static final long serialVersionUID = 8754991875223570365L;
	
	private Pattern mYorktownPattern = Pattern.compile("\\b[0-4]\\d-[0-2]\\d\\d\\b");
	private Pattern mHawthornePattern = Pattern.compile("\\b[G1-4][NS]-[A-Z]\\d\\d\\b");	
	
	/**
	 *
	 */
    public HoopRoomAnnotator () 
    {
		setClassName ("HoopRoomAnnotator");
		debug ("HoopRoomAnnotator ()");
								
		setHoopDescription ("Port of UIMA Room Annotator");				
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
			this.setErrorString("No data found in previous hoop");
			return (false);
		}
		
		for (int t=0;t<inData.size();t++)
		{
			HoopKV aKV=inData.get(t);		
				
			ArrayList<Object> vals=aKV.getValuesRaw();

			String value=(String) vals.get(0);
				
		    Matcher matcher = mYorktownPattern.matcher(value);
			    
		    while (matcher.find()) 
		    {
		    	// found one - create annotation
		    	/*
		      	RoomNumber annotation = new RoomNumber(aJCas);
		      	annotation.setBegin(matcher.start());
		      	annotation.setEnd(matcher.end());
		      	annotation.setBuilding("Yorktown");
		      	annotation.addToIndexes();
		      	*/
		    	
		    	Integer start=matcher.start();
		    	Integer end=matcher.end();
		    	
		    	HoopKVString newKV=new HoopKVString ();
		    	newKV.setKeyString(aKV.getKeyString());
		    	newKV.setValue("Yorktown",0);
		    	newKV.setValue(value.substring(matcher.start(), matcher.end()),1);
		    	newKV.setValue(start.toString(),2);
		    	newKV.setValue(end.toString(),3);
		    	
		    	this.addKV(newKV);
		    }
		    
		    // search for Hawthorne room numbers
		    matcher = mHawthornePattern.matcher(value);
			    
		    while (matcher.find()) 
		    {
		    	// found one - create annotation
		    	/*
		    	RoomNumber annotation = new RoomNumber(aJCas);
		    	annotation.setBegin(matcher.start());
		    	annotation.setEnd(matcher.end());
		    	annotation.setBuilding("Hawthorne");
		    	annotation.addToIndexes();
		    	*/
		    	
		    	Integer start=matcher.start();
		    	Integer end=matcher.end();		    	
		    	
		    	HoopKVString newKV=new HoopKVString ();
		    	newKV.setKeyString(aKV.getKeyString());
		    	newKV.setValue("Hawthorne",0);
		    	newKV.setValue(value.substring(matcher.start(), matcher.end()),1);
		    	
		    	newKV.setValue(start.toString(),2);
		    	newKV.setValue(end.toString(),3);		    	
		    	
		    	this.addKV(newKV);		    	
		    }
		    
		    updateProgressStatus (t,inData.size());
		}	
		
		return (true);
	}	
	/**
	 * 
	 */
	public HoopBase copy ()
	{
		return (new HoopRoomAnnotator ());
	}	
}
