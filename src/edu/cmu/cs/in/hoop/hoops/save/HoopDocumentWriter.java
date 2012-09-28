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

package edu.cmu.cs.in.hoop.hoops.save;

import java.util.ArrayList;

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.base.kv.HoopKV;
import edu.cmu.cs.in.base.kv.HoopKVDocument;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.hoop.hoops.base.HoopSaveBase;
import edu.cmu.cs.in.search.HoopDataSet;

/**
* 
*/
public class HoopDocumentWriter extends HoopSaveBase
{    				
	private static final long serialVersionUID = -1691608095189030052L;
			
	/**
	 *
	 */
    public HoopDocumentWriter () 
    {
		setClassName ("HoopDocumentWriter");
		debug ("HoopDocumentWriter ()");
												
		setHoopDescription ("Write Documents to Document DB");					
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
			this.setErrorString ("Error: no input data to work with");
			return (false);
		}		
		
		debug ("Mapping project path ("+getProjectPath ()+") to db dir ...");
				
		if (HoopLink.dataSet==null)
		{
			HoopLink.dataSet=new HoopDataSet ();
			HoopLink.dataSet.checkDB ();
		}
		else
			HoopLink.dataSet.checkDB ();
		
		for (int t=0;t<inData.size();t++)
		{			
			HoopKV aKV=inData.get(t);
									
			if (aKV instanceof HoopKVDocument)
			{			
				HoopKVDocument newDocument=(HoopKVDocument) aKV;
				
				// This call will associate a timestamp with a document, but
				// through an alternative table also use a unique ID to link
				// to a document. But only if the documentID field is not blank
				// and contains a long value.
			
				HoopLink.dataSet.writeKV (newDocument.getKey(),newDocument);
			}			
		}			
				
		return (true);
	}		
	/**
	 * 
	 */
	public HoopBase copy ()
	{
		return (new HoopDocumentWriter ());
	}		
}
