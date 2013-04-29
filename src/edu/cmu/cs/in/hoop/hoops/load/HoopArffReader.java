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

public class HoopArffReader extends HoopTransformBase implements HoopInterface
{
	private static final long serialVersionUID = 8420767904994324626L;
	
	private String mode="COMMA"; // TAB,COMMA,DASH
	private Boolean skipHeader=true;
	private String relation="undefined";
	private int attrCounter=0;
	
	private int completed=0; 
	
	/**
	 *
	 */ 
	public HoopArffReader () 
	{		
		setClassName ("HoopArffReader");
		debug ("HoopArffReader ()");
				
		setHoopDescription ("Convert from a Weka Arff stream");		
	}
	/**
	 * 
	 */
	public String getRelation() 
	{
		return relation;
	}
	/**
	 * 
	 */	
	public void setRelation(String relation) 
	{
		this.relation = relation;
	}	
	/**
	 *
	 */	
	public void setMode(String mode) 
	{
		this.mode = mode;
	}
	/**
	 *
	 */	
	public String getMode() 
	{
		return mode;
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
		return (new HoopArffReader ());
	}	
	/** 
	 * @param aLine
	 */
	private Boolean processRelation (String aLine)
	{
		debug ("ProcessRelation ()");
		
		return (true);
	}	
	/** 
	 * @param aLine
	 */
	private Boolean processAttribute (String aLine)
	{
		debug ("ProcessAttribute ()");
		
		String entries []=aLine.split("\\s+");
		
		if (entries.length==0)
		{
			this.setErrorString("Error: unable to parse attribute line: " + aLine);
			return (false);
		}

		this.setKVType(attrCounter,HoopDataType.STRING,entries [1]);
						
		return (true);
	}
	/** 
	 * @param aLine
	 */
	private Boolean processDataLine (String aLine)
	{
		debug ("ProcessDataLine ()");
		
		String entries []=aLine.split("(?<!\\\\),");
		
		if (entries.length==0)
		{
			this.setErrorString("Error: unable to parse data line: " + aLine);
			return (false);
		}
		
		HoopKVString kv=new HoopKVString ();
		
		for (int i=0;i<entries.length;i++)
		{			
			if (i==0)
			{
				kv.setKey(entries [i]);
			}
			else
			{
				kv.setValue(entries [i],i-1);
			}
		}
		
		this.addKV(kv);
		
		return (true);
	}	
	/**
	 * We're expecting a KV object that has a key of type String representing the file URI,
	 * where the content value (also a String) represents the flat text of the file.
	 */
	public Boolean runHoop (HoopBase inHoop)
	{		
		debug ("runHoop ()");
		
		attrCounter=0;
				
		//this.setMaxValues(1);
		
		ArrayList <HoopKV> inData=inHoop.getData();
		
		if (inData!=null)
		{									
			debug ("Loading " + inData.size()+" entries ...");
									
			for (int t=0;t<inData.size();t++)
			{
				HoopKVString aKV=(HoopKVString) inData.get(t);
				
				String split[]=aKV.getValueAsString().split("\\n");
				
				debug ("Arff data contains " + split.length + " rows");
				
				boolean markData=false;
						
				for (int i=0;i<split.length;i++)
				{			
					if (markData==false)
					{
						if (split [i].indexOf("@relation")!=-1)
						{
							if (processRelation (split [i])==false)
								return (false);
						}						
						
						if (split [i].indexOf("@attribute")!=-1)
						{
							if (processAttribute (split [i])==false)
								return (false);
						}
					
						if (split [i].indexOf("@data")!=-1)
						{
							markData=true;
							completed++;
						}
					}
					else
					{
						if (processDataLine (split [i])==false)
							return (false);
					}
				}
			}
		}	
		else
			debug ("Error: no data input hoop");
		
		debug ("Max tokens per line: " + this.getMaxValues());
		
		if (completed==0)
		{
			this.setErrorString("Parsing error, no data loaded");
			return (false);
		}
		
		return (true);
	}	
}
