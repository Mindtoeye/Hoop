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

import java.util.ArrayList;

import edu.cmu.cs.in.base.kv.HoopKV;
import edu.cmu.cs.in.base.kv.HoopKVString;
import edu.cmu.cs.in.base.HoopDataType;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.hoop.hoops.base.HoopInterface;
import edu.cmu.cs.in.hoop.hoops.base.HoopTransformBase;
import edu.cmu.cs.in.hoop.properties.types.HoopStringSerializable;

/**
 * http://stackoverflow.com/questions/769621/dealing-with-commas-in-a-csv-file
 */
public class HoopCSVReader extends HoopTransformBase implements HoopInterface
{
	//private String mode="COMMA"; // TAB,COMMA,DASH
	private Boolean skipHeader=true;
	
	public	HoopStringSerializable mode = null;
	
	/**
	 *
	 */ 
	public HoopCSVReader () 
	{		
		setClassName ("HoopCSVReader");
		debug ("HoopCSVReader ()");
				
		setHoopDescription ("Convert from a CSV stream");
		
		mode=new HoopStringSerializable (this,"mode","COMMA");
	}
	/**
	 *
	 */	
	public void setMode(String aMode) 
	{
		mode.setValue(aMode);
	}
	/**
	 *
	 */	
	public String getMode() 
	{
		return mode.getValue();
	}
	/**
	 *
	 */	
	public void setSkipHeader(Boolean skipHeader) 
	{
		this.skipHeader = skipHeader;
	}
	/**
	 *
	 */	
	public Boolean getSkipHeader() 
	{
		return skipHeader;
	}	
	/**
	 * 
	 */
	public HoopBase copy ()
	{
		return (new HoopCSVReader ());
	}	
	/**
	 * We're expecting a KV object that has a key of type String representing the file URI,
	 * where the content value (also a String) represents the flat text of the file.
	 */
	public Boolean runHoop (HoopBase inHoop)
	{		
		debug ("runHoop ()");
				
		this.setMaxValues(1);
		
		ArrayList <HoopKV> inData=inHoop.getData();
		
		if (inData!=null)
		{									
			debug ("Loading " + inData.size()+" entries ...");
			
			for (int t=0;t<inData.size();t++)
			{
				HoopKVString aKV=(HoopKVString) inData.get(t);
				
				String split[]=aKV.getValueAsString().split("\\n");
				
				//debug ("CSV data contains " + split.length + " rows");
				
				HoopKVString currentKV=null;
		
				for (int i=0;i<split.length;i++)
				{			
					String entries[]=null;
								
					if (mode.equals ("TAB")==true)
						entries=split [i].split("\\t");
					else
					{
						if (mode.equals ("COMMA")==true)
							entries=split [i].split("(?<!\\\\),");
					}

					if ((skipHeader==true) && (i==0))
					{
						for (int j=0;j<entries.length;j++)
						{
							setKVType (j,HoopDataType.STRING,entries [j]);				
						}						
					}
					else
					{					
						currentKV=new HoopKVString ();
				
						//debug ("Storing " + entries.length + " tokens ...");	
						
						for (int j=0;j<entries.length;j++)
						{
							if (j==0)
								currentKV.setKey(entries [j]);
							else
								currentKV.setValue(entries [j],j);				
						}
						
						if (entries.length>this.getMaxValues())
							this.setMaxValues(entries.length);
						
						this.addKV(currentKV);
					}	
				}
			}
		}	
		else
			debug ("Error: no data input hoop");
		
		debug ("Max tokens per line: " + this.getMaxValues());
		
		return (true);
	}	
}
