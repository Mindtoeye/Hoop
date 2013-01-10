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
import edu.cmu.cs.in.hoop.properties.HoopStoredProperties;
import edu.cmu.cs.in.hoop.properties.types.HoopSerializable;

/**
*
*/
public class HoopBaseTyped extends HoopVisual implements HoopPropertyContainer
{    			
	private ArrayList <HoopDataType> types=null;
	protected HoopStoredProperties props=null;
	
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
		
		props=new HoopStoredProperties ();
    }
    /**
     * 
     */
    public void copyTypes (HoopBaseTyped aTargetHoop)
    {
    	//debug ("copyTypes ()");
    	
    	for (int i=0;i<types.size();i++)
    	{
    		HoopDataType aType=types.get(i);
    		
    		//debug ("Copying type ("+i+"): " + aType.getType() + " -> " + aType.getTypeValue());
    		
    		aTargetHoop.setKVType (i,aType.getDataType(),aType.getTypeValue());
    	}    	
    }
    /**
     * 
     */
    public void listTypes ()
    {
    	debug ("listTypes ()");
    	
    	for (int i=0;i<types.size();i++)
    	{
    		HoopDataType aType=types.get(i);
    		
    		debug ("Type ("+i+"): " + aType.typeToString () + " -> " + aType.getTypeValue());    		
    	}    	
    }    
    /**
     * 
     */
    public int getTypesSize ()
    {
    	return (types.size());
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
    	
		target.setDataType (aType);
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
    	
		target.setDataType(aType);
		target.setTypeValue (aValue);
    }
    /**
     * 
     */
    public void setKVLabel (int anIndex,String aValue)
    {
    	debug ("setKVLabel ()");
    	
    	HoopDataType target=types.get(anIndex);
    	
    	if (target==null)
    	{
    		target=new HoopDataType ();
    		types.set(anIndex, target);
    	}
    	
		target.setTypeValue (aValue);    	
    }
    /**
     * 
     */
    public String getKVTypeName (int anIndex)
    {
    	if (anIndex>=types.size())
    		return ("");
    	
    	HoopDataType target=types.get(anIndex);
    	
    	return (target.getTypeValue());
    }
    /**
     * 
     */
    public Object getKVTypeValue (int anIndex)
    {
    	if (anIndex>=types.size())
    		return (null);
    	
    	HoopDataType target=types.get(anIndex);
    	
    	return (target.getTypeValue());
    }
    /**
     * 
     */
    /*
	@Override
	public HoopBaseTyped getPropParent() 
	{	
		return this;
	}
	*/
	/**
	 * 
	 */
	@Override
	public void propertyChanged() 
	{
		// TODO Auto-generated method stub
		
	}
	@Override
	public ArrayList<HoopSerializable> getProperties() 
	{
		return props.getProperties();
	}    
    /**
     * 
     */
    public HoopSerializable getProperty (String anInstance)
    {
    	return (props.getProperty(anInstance));
    }
    /**
     * 
     */
    public int getPropertyIndex (String anInstance)
    {
    	return (props.getPropertyIndex(anInstance));
    }
    /**
     * 
     */
    public void removeProperty (String aProperty)
    {
    	props.removeProperty(aProperty);
    }    
    /**
     * 
     */
    public void enableProperty (String aProperty,Boolean aVal)
    {
    	props.enableProperty(aProperty, aVal);
    }    
    /**
     * 
     */
    public void enableProperty (HoopSerializable aProperty,Boolean aVal)
    {
    	props.enableProperty(aProperty, aVal);
    }    	
}
