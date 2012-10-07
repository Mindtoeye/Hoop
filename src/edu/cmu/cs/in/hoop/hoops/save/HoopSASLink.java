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

import edu.cmu.cs.in.base.kv.HoopKV;
import edu.cmu.cs.in.base.HoopDataType;
import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.hoop.hoops.base.HoopFileSaveBase;
import edu.cmu.cs.in.hoop.properties.types.HoopEnumSerializable;

/**
* http://stackoverflow.com/questions/769621/dealing-with-commas-in-a-csv-file
*/
public class HoopSASLink extends HoopFileSaveBase
{    				
	private static final long serialVersionUID = -5605997153926163226L;
	
	private HoopEnumSerializable writeMode=null; // APPEND, OVERWRITE	
	private HoopEnumSerializable mode=null; // TAB,COMMA,DASH
	
	/**
	 *
	 */
    public HoopSASLink () 
    {
		setClassName ("HoopSASLink");
		debug ("HoopSASLink ()");
				
		setHoopDescription ("Save KVs in CSV format");
		setFileExtension ("csv");
				
		writeMode=new HoopEnumSerializable (this,"writeMode","OVERWRITE,APPEND");
		mode=new HoopEnumSerializable (this,"mode","COMMA,TAB,DASH");
    }  
    /**
     * 
     */
    public String getSeparatorChar ()
    {
    	if (mode.getValue().equals("TAB"))
    		return ("\t");
    	
    	if (mode.getValue().equals("COMMA"))
    		return (",");
    	
    	if (mode.getValue().equals("DASH"))
    		return ("-");
    	
    	return (",");
    }
	/**
	 *
	 */
	public Boolean runHoop (HoopBase inHoop)
	{		
		debug ("runHoop ()");
		
		String sepChar=getSeparatorChar ();
		
		StringBuffer formatted=new StringBuffer ();		
		
		if (this.getExecutionCount()==0)
		{		
			ArrayList <HoopDataType> types=inHoop.getTypes();
						
			for (int n=0;n<types.size();n++)
			{
				if (n>0)
				{
					formatted.append(sepChar);
				}	
			
				HoopDataType aType=types.get(n);			
				formatted.append (aType.getTypeValue());
				formatted.append("(");
				formatted.append(aType.typeToString());
				formatted.append(")");
			}
		
			formatted.append("\n");
		}	
		
		ArrayList <HoopKV> inData=inHoop.getData();
							
		if (inData!=null)
		{			
			for (int t=0;t<inData.size();t++)
			{
				HoopKV aKV=inData.get(t);
								
				formatted.append(aKV.getKeyString ());
				
				ArrayList<Object> vals=aKV.getValuesRaw();
				
				for (int i=0;i<vals.size();i++)
				{
					formatted.append(sepChar);
					
					// IMPORTANT: Transform to String and check if it is empty or blank, if so replace
					// with a . as per SAS specs
					
					formatted.append(vals.get(i));
				}
				
				formatted.append("\n");
			}
			
			if (writeMode.getValue().equals("OVERWRITE")==true)
			{
				HoopLink.fManager.saveContents (this.projectToFullPath(URI.getValue()),formatted.toString());
			}
			else
			{
				HoopLink.fManager.appendContents (this.projectToFullPath(URI.getValue()),formatted.toString());
			}
		}	
						
		return (true);
	}	
	/**
	 * 
	 */
	public HoopBase copy ()
	{
		return (new HoopSASLink ());
	}	
}
