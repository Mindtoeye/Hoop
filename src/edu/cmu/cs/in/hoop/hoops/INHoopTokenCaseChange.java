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

/**
* 
*/
public class INHoopTokenCaseChange extends INHoopTransformBase implements INHoopInterface
{    					
	private Boolean toLower=true;
	
	/**
	 *
	 */
    public INHoopTokenCaseChange () 
    {
		setClassName ("INHoopTokenCaseChange");
		debug ("INHoopTokenCaseChange ()");
										
		setHoopDescription ("Change tokens to upper or lower case");
    }
    /*
     * 
     */
	public void setToLower(Boolean toLower) 
	{
		this.toLower = toLower;
	}
	/*
	 * 
	 */
	public Boolean getToLower() 
	{
		return toLower;
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
			for (int i=0;i<inData.size();i++)
			{
				INKV aKV=inData.get(i);
				
				if (toLower==true)
					addKV (new INKV (i,aKV.getValue().toLowerCase()));
				else
					addKV (new INKV (i,aKV.getValue().toUpperCase()));
			}						
		}
		else
			return (false);
				
		return (true);
	}
	/**
	 * 
	 */
	public INHoopBase copy ()
	{
		return (new INHoopTokenCaseChange ());
	}	
}
