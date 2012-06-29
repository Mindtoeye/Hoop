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

package edu.cmu.cs.in.hoop.base;

import java.util.ArrayList;

import edu.cmu.cs.in.base.INKVType;
import edu.cmu.cs.in.hoop.properties.INHoopInspectable;

/**
*
*/
public class INHoopBaseTyped extends INHoopInspectable
{    			
	private ArrayList <INKVType> types=null;
	
	/**
	 *
	 */
    public INHoopBaseTyped () 
    {
		setClassName ("INHoopBaseTyped");
		debug ("INHoopBaseTyped ()");
				
		types=new ArrayList <INKVType> ();
		types.add(new INKVType (INKVType.STRING,"Key"));
		types.add(new INKVType (INKVType.STRING,"Value"));						
    }
    /**
     *  
     */
    public ArrayList <INKVType> getTypes ()
    {
    	return (types);
    }
    /**
     * 
     */
    public void setKVType (int anIndex,int aType)
    {
    	INKVType target=types.get(anIndex);
    	
    	if (target==null)
    	{
    		target=new INKVType ();
    		types.set(anIndex, target);
    	}
    	
		target.setType (aType);
    }
    /**
     * 
     */
    public void setKVType (int anIndex,int aType,String aValue)
    {
    	INKVType target=types.get(anIndex);
    	
    	if (target==null)
    	{
    		target=new INKVType ();
    		types.set(anIndex, target);
    	}
    	
		target.setType(aType);
		target.setTypeValue (aValue);
    }    
}
