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
	private String mode="TAB"; // TAB,COMMA,DASH
	private Boolean skipHeader=false;
	
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
				
		ArrayList <INKV> inData=inHoop.getData();
		
		if (inData!=null)
		{										
			for (int t=0;t<inData.size();t++)
			{
				INKV aKV=inData.get(t);
				
				String split[]=aKV.getValue().split("\\n");
				
				INKV currentKV=null;
		
				for (int i=0;i<split.length;i++)
				{			
					int skipper=-1;
					
					if (skipHeader==true)
						skipper=0;
					
					if (i>skipper)
					{
						String entries[]=split [i].split("\\t");
		 
						currentKV=new INKV ();
						currentKV.setKey (i);
				
						for (int j=0;j<entries.length;j++)
						{
							currentKV.setValue(entries [j],j);							
						}
						
						this.addKV(currentKV);
					}	
				}
			}
		}	
		
		return (true);
	}	
}
