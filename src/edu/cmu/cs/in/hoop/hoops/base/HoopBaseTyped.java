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

package edu.cmu.cs.in.hoop.hoops.base;

import java.util.ArrayList;

import edu.cmu.cs.in.base.kv.HoopKVType;
import edu.cmu.cs.in.hoop.properties.types.HoopSerializable;

/**
*
*/
public class HoopBaseTyped extends HoopVisual implements HoopPropertyContainer
{    			
	private ArrayList <HoopKVType> types=null;
	private ArrayList <HoopSerializable> properties=null;
	
	/**
	 *
	 */
    public HoopBaseTyped () 
    {
		setClassName ("HoopBaseTyped");
		debug ("HoopBaseTyped ()");
				
		types=new ArrayList <HoopKVType> ();
		types.add(new HoopKVType (HoopKVType.STRING,"Key"));
		types.add(new HoopKVType (HoopKVType.STRING,"Value"));
		
		properties=new ArrayList<HoopSerializable> ();		
    }
    /**
     *  
     */
    public ArrayList <HoopKVType> getTypes ()
    {
    	return (types);
    }
    /**
     * 
     */
    public ArrayList <HoopSerializable> getProperties ()
    {
    	return (properties);
    }
    /**
     * 
     */
    public HoopSerializable getProperty (String anInstance)
    {
    	debug ("getProperty ("+anInstance+")");
    	
    	for (int i=0;i<properties.size();i++)
    	{
    		HoopSerializable prop=properties.get(i);
    		
    		if (prop.getInstanceName().equals(anInstance)==true)
    			return (prop);
    	}
    	
    	return (null);
    }
    /**
     * 
     */
    public void setKVType (int anIndex,int aType)
    {
    	HoopKVType target=types.get(anIndex);
    	
    	if (target==null)
    	{
    		target=new HoopKVType ();
    		types.set(anIndex, target);
    	}
    	
		target.setType (aType);
    }
    /**
     * 
     */
    public void setKVType (int anIndex,int aType,String aValue)
    {
    	HoopKVType target=types.get(anIndex);
    	
    	if (target==null)
    	{
    		target=new HoopKVType ();
    		types.set(anIndex, target);
    	}
    	
		target.setType(aType);
		target.setTypeValue (aValue);
    }
    /**
     * 
     */
	@Override
	public HoopBaseTyped getPropParent() 
	{	
		return this;
	}    
}
