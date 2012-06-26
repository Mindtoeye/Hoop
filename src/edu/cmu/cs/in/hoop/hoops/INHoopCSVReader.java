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

import edu.cmu.cs.in.base.INKV;
import edu.cmu.cs.in.hoop.base.INHoopBase;
import edu.cmu.cs.in.hoop.base.INHoopInterface;
import edu.cmu.cs.in.hoop.base.INHoopTransformBase;

public class INHoopCSVReader extends INHoopTransformBase implements INHoopInterface
{
	private String mode="COMMA"; // TAB,COMMA,DASH
	private Boolean skipHeader=true;
	
	/**
	 *
	 */ 
	public INHoopCSVReader () 
	{		
		setClassName ("INCSVReader");
		debug ("INCSVReader ()");
				
		setHoopDescription ("Convert from a CSV stream");		
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
	public INHoopBase copy ()
	{
		return (new INHoopCSVReader ());
	}	
	/**
	 *
	 */
	public Boolean runHoop (INHoopBase inHoop)
	{		
		debug ("runHoop ()");
				
		this.setMaxValues(1);
		
		ArrayList <INKV> inData=inHoop.getData();
		
		if (inData!=null)
		{									
			debug ("Loading " + inData.size()+" entries ...");
			
			for (int t=0;t<inData.size();t++)
			{
				INKV aKV=inData.get(t);
				
				String split[]=aKV.getValue().split("\\n");
				
				//debug ("CSV data contains " + split.length + " rows");
				
				INKV currentKV=null;
		
				for (int i=0;i<split.length;i++)
				{			
					int skipper=-1;
					
					if (skipHeader==true)
						skipper=0;
					
					if (i>skipper)
					{
						String entries[]=null;
								
						if (mode.equals ("TAB")==true)
							entries=split [i].split("\\t");
						else
						{
							if (mode.equals ("COMMA")==true)
								entries=split [i].split("(?<!\\\\),");
						}
		 
						currentKV=new INKV ();
						currentKV.setKey (i);
				
						//debug ("Storing " + entries.length + " tokens ...");	
						
						for (int j=0;j<entries.length;j++)
						{
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
