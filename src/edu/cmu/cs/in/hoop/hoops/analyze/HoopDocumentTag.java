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
import edu.cmu.cs.in.hoop.hoops.base.HoopAnalyze;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.hoop.hoops.base.HoopInterface;
import edu.cmu.cs.in.hoop.properties.types.HoopBooleanSerializable;
import edu.cmu.cs.in.hoop.properties.types.HoopEnumSerializable;
import edu.cmu.cs.in.hoop.properties.types.HoopStringSerializable;

/**
* 
*/
public class HoopDocumentTag extends HoopAnalyze implements HoopInterface
{    					
	private static final long serialVersionUID = 8296031944301161003L;
	
	protected HoopEnumSerializable tagType=null;
	public	HoopStringSerializable tagLabels=null;
	public 	HoopBooleanSerializable tagThreadsSeparately=null;	
	
	/**
	 *
	 */
    public HoopDocumentTag () 
    {
		setClassName ("HoopDocumentTag");
		debug ("HoopDocumentTag ()");
						
		setHoopDescription ("Tag document based on certain criteria");
		
		tagType=new HoopEnumSerializable (this,"tagType","split,all,code");
		tagLabels=new HoopStringSerializable (this,"tagLabels","");
		tagThreadsSeparately=new HoopBooleanSerializable (this,"tagThreadsSeparately",false);
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
					HoopKVDocument tagDocument=(HoopKVDocument) aKV;
					
					/**
					 * Split the data set by effectively create data by tag. So if there
					 * are 4 tags provided you will end up with the data tagged with one
					 * tag each, effectively splitting the data in 4.
					 */
					
					if (tagType.getValue().equalsIgnoreCase("split")==true)
					{
						if (tagThreadsSeparately.getPropValue()==true)
						{
							tagDocument.addTag(splitter [splitCounter]);
							
							splitCounter++;

							if (splitCounter>=splitMax)
							{
								splitCounter=0;
							}
						}
						else
						{
							if (tagDocument.threadID.getValue().equals(tagDocument.threadStarter.getValue()))
							{							
								debug ("Tagging thread starter: " + tagDocument.threadID.getValue() + " with: " + splitter [splitCounter]);
								
								tagDocument.addTag(splitter [splitCounter]);
							}
							else
							{
								debug ("Not tagging thread sub document!");
							}
						}							
					}

					/**
					 * Simply assign all the tags to all the documents without applying
					 * any criteria. For now we're not looking at thread information
					 */
										
					if (tagType.getValue().equalsIgnoreCase("all")==true)
					{
						for (int j=0;j<splitter.length;j++)
						{
							tagDocument.addTag (splitter [j]);
						}
					}
					
					/**
					 * In preparation for automatic coding of documents we provide an
					 * empty code block as a place-holder. The idea is that the coding
					 * criteria and codes will be read from a configuration file and 
					 * applied here.
					 */
					
					if (tagType.getValue().equalsIgnoreCase("code")==true)
					{
						
					}					
					
					//>---------------------------------------------------------------
					
					this.addKV(aKV);
				}
				else
					debug ("Not tagging non document object: " + aKV.toString());
				
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
		return (new HoopDocumentTag ());
	}	
}
