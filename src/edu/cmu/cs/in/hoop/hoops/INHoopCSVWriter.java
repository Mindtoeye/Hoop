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

package edu.cmu.cs.in.hoop.hoops;

import java.util.ArrayList;

import edu.cmu.cs.in.base.io.INFileManager;
import edu.cmu.cs.in.base.INKV;
import edu.cmu.cs.in.base.INKVInteger;
import edu.cmu.cs.in.hoop.base.INHoopBase;
import edu.cmu.cs.in.hoop.base.INHoopInterface;
import edu.cmu.cs.in.hoop.base.INHoopSaveBase;

/**
* 
*/
public class INHoopCSVWriter extends INHoopSaveBase implements INHoopInterface
{    			
	private String mode="TAB"; // TAB,COMMA,DASH
	private String outURI="./HoopOut.csv";
	private INFileManager fManager=null;
	
	/**
	 *
	 */
    public INHoopCSVWriter () 
    {
		setClassName ("INHoopCSVWriter");
		debug ("INHoopCSVWriter ()");
				
		setHoopDescription ("Save KVs in CSV format");
		
		fManager=new INFileManager ();
    }  
	/**
	 *
	 */
	public Boolean runHoop (INHoopBase inHoop)
	{		
		debug ("runHoop ()");
		
		ArrayList <INKV> inData=inHoop.getData();
					
		if (inData!=null)
		{
			StringBuffer formatted=new StringBuffer ();
			
			for (int t=0;t<inData.size();t++)
			{
				INKVInteger aKV=(INKVInteger) inData.get(t);
								
				formatted.append(aKV.getKeyString ());
				//formatted.append(",");
				
				ArrayList<Object> vals=aKV.getValuesRaw();
				
				for (int i=0;i<vals.size();i++)
				{
					formatted.append(",");
					formatted.append(vals.get(i));
				}
				
				formatted.append("\n");
			}
			
			fManager.saveContents (outURI,formatted.toString());
		}	
						
		return (true);
	}	
	/**
	 * 
	 */
	public INHoopBase copy ()
	{
		return (new INHoopCSVWriter ());
	}	
}
