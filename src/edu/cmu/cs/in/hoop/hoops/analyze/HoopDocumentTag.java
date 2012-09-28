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
import edu.cmu.cs.in.base.kv.HoopKVDocument;
//import edu.cmu.cs.in.base.kv.HoopKVInteger;
import edu.cmu.cs.in.hoop.hoops.base.HoopAnalyze;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.hoop.hoops.base.HoopInterface;
import edu.cmu.cs.in.hoop.properties.types.HoopEnumSerializable;
import edu.cmu.cs.in.hoop.properties.types.HoopStringSerializable;

/**
* 
*/
public class HoopDocumentTag extends HoopAnalyze implements HoopInterface
{    					
	private static final long serialVersionUID = 8296031944301161003L;
	
	private HoopEnumSerializable tagType=null;
	public	HoopStringSerializable tagLabels=null;
	
	/**
	 *
	 */
    public HoopDocumentTag () 
    {
		setClassName ("HoopDocumentTag");
		debug ("HoopDocumentTag ()");
				
		//removeOutPort ("KV");
		
		setHoopDescription ("Tag document based on certain criteria");
		
		tagType=new HoopEnumSerializable (this,"tagType","split,keyword,code");
		tagLabels=new HoopStringSerializable (this,"tagLabels","");
    }
	/**
	 *
	 */
	public Boolean runHoop (HoopBase inHoop)
	{		
		debug ("runHoop ()");
		
		String aValue=tagLabels.getValue();
		
		if (aValue.isEmpty()==true)
		{
			this.setErrorString("Please provide one or more tag labels");
			return (false);
		}
		
		String [] splitter=aValue.split(",");
		
		Integer splitMax=splitter.length;
		Integer splitCounter=0;
		
		debug ("Applying a total of " + splitMax + " labels");
		
		ArrayList <HoopKV> inData=inHoop.getData();
		
		if (inData!=null)
		{			
			for (int t=0;t<inData.size();t++)
			{
				HoopKV aKV=(HoopKV) inData.get(t);
				
				if (aKV instanceof HoopKVDocument)
				{
					//>---------------------------------------------------------------
					
					if (tagType.getValue().equalsIgnoreCase("split")==true)
					{
						((HoopKVDocument) aKV).addTag(splitter [splitCounter]);

						splitCounter++;
					
						if (splitCounter>=splitMax)
						{
							splitCounter=0;
						}
					}	
					
					//>---------------------------------------------------------------
										
					if (tagType.getValue().equalsIgnoreCase("keyword")==true)
					{
						
					}
					
					//>---------------------------------------------------------------
					
					if (tagType.getValue().equalsIgnoreCase("code")==true)
					{
						
					}					
					
					//>---------------------------------------------------------------
				}
				else
					debug ("Not tagging non document object: " + aKV.toString());
			}			
		}	
						
		return (true);				
	}	 
	/**
	 * 
	 */
	public HoopBase copy ()
	{
		return (new HoopDocumentTag ());
	}	
}
