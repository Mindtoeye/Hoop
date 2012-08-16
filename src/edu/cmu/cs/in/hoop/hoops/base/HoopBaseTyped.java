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

import edu.cmu.cs.in.base.HoopDataType;
import edu.cmu.cs.in.hoop.properties.types.HoopSerializable;

/**
*
*/
public class HoopBaseTyped extends HoopVisual implements HoopPropertyContainer
{    			
	private ArrayList <HoopDataType> types=null;
	private ArrayList <HoopSerializable> properties=null;
	
	/**
	 *
	 */
    public HoopBaseTyped () 
    {
		setClassName ("HoopBaseTyped");
		debug ("HoopBaseTyped ()");
				
		types=new ArrayList <HoopDataType> ();
		types.add(new HoopDataType (HoopDataType.STRING,"Key"));
		types.add(new HoopDataType (HoopDataType.STRING,"Value"));
		
		properties=new ArrayList<HoopSerializable> ();		
    }
    /**
     *  
     */
    public ArrayList <HoopDataType> getTypes ()
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
    	
    		//debug ("Comparing " + prop.getInstanceName() + " to: " + anInstance);
    		
    		if (prop.getInstanceName().equals(anInstance)==true)
    			return (prop);
    	}
    	
    	return (null);
    }
    /**
     * 
     */
    public int getPropertyIndex (String anInstance)
    {
    	debug ("getProperty ("+anInstance+")");
    	
    	for (int i=0;i<properties.size();i++)
    	{
    		HoopSerializable prop=properties.get(i);
    		
    		if (prop.getInstanceName().equals(anInstance.toLowerCase())==true)
    			return (i);
    	}
    	
    	return (-1);
    }    
    /**
     * 
     */
    public void setKVType (int anIndex,int aType)
    {
    	if (anIndex>(types.size()-1))
    	{
    		debug ("Attempting to access non existing index ("+anIndex+"->"+aType+"), creating ...");
    		types.add(new HoopDataType (aType,"Value"));    		
    	}    	
    	
    	HoopDataType target=types.get(anIndex);
    	
    	if (target==null)
    	{
    		target=new HoopDataType ();
    		types.set(anIndex, target);
    	}
    	
		target.setType (aType);
    }
    /**
     * 
     */
    public void setKVType (int anIndex,int aType,String aValue)
    {
    	if (anIndex>(types.size()-1))
    	{
    		debug ("Attempting to access non existing index ("+anIndex+"->"+aType+"), creating ...");
    		types.add(new HoopDataType (aType,"Value"));    		
    	}
    	
    	HoopDataType target=types.get(anIndex);
    	
    	if (target==null)
    	{
    		target=new HoopDataType ();
    		types.set(anIndex, target);
    	}
    	
		target.setType(aType);
		target.setTypeValue (aValue);
    }
    /**
     * 
     */
    public String getKVTypeName (int anIndex)
    {
    	HoopDataType target=types.get(anIndex);
    	
    	return (target.getTypeValue());
    }
    /**
     * 
     */
    public Object getKVTypeValue (int anIndex)
    {
    	HoopDataType target=types.get(anIndex);
    	
    	return (target.getTypeValue());
    }
    /**
     * 
     */
	@Override
	public HoopBaseTyped getPropParent() 
	{	
		return this;
	}
	/**
	 * 
	 */
	@Override
	public void propertyChanged() 
	{
		// TODO Auto-generated method stub
		
	}    
}
