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

import edu.cmu.cs.in.base.HoopDataType;
import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.base.io.HoopBerkeleyDB;
import edu.cmu.cs.in.base.io.HoopBerkeleyDBInstance;
import edu.cmu.cs.in.base.kv.HoopKV;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.hoop.hoops.base.HoopSaveBase;
import edu.cmu.cs.in.hoop.properties.types.HoopEnumSerializable;
import edu.cmu.cs.in.hoop.properties.types.HoopStringSerializable;

/**
* 
*/
public class HoopBerkeleyDBWriter extends HoopSaveBase
{    			
	private static final long serialVersionUID = -7691949614893802486L;

	private HoopBerkeleyDB driver=null;
	
	private HoopEnumSerializable dbType=null; // DOCS, USERS, CUSTOM	
	private HoopStringSerializable dbName=null; // Only used with custom db type
	
	/**
	 *
	 */
    public HoopBerkeleyDBWriter () 
    {
		setClassName ("HoopBerkeleyDBWriter");
		debug ("HoopBerkeleyDBWriter ()");
												
		setHoopDescription ("Write to a BerkeleyDB");
		
		dbType=new HoopEnumSerializable (this,"dbType","DOCS,USERS,CUSTOM");
		dbName=new HoopStringSerializable (this,"dbName","Database Name");
		
		driver=new HoopBerkeleyDB ();		
    }
    /**
     * 
     */
    /**
     * 
     */
    protected Boolean typesToDB (HoopBase inHoop)
    {
    	debug ("typesToDB ()");
    	    	
		ArrayList <HoopDataType> types=inHoop.getTypes();
						
		for (int n=0;n<types.size();n++)
		{			
			HoopDataType aType=types.get(n);
			
			HoopBerkeleyDBInstance dbInstance=new HoopBerkeleyDBInstance ();
			dbInstance.setInstanceName(aType.getTypeValue ());
			driver.accessDB(dbInstance);
									
			/*
			Element aTypeElement=new Element ("type");
			
			aTypeElement.setAttribute("type",aType.getTypeValue ());
			aTypeElement.setAttribute("value",aType.typeToString ());
						
			typeElement.addContent(aTypeElement);
			*/
		}		
		
		return (true);
    }
	/**
	 *
	 */
	public Boolean runHoop (HoopBase inHoop)
	{		
		debug ("runHoop ()");
		
		ArrayList <HoopKV> inData=inHoop.getData();
		
		if (inData==null)
		{
			this.setErrorString ("Error: no input data to work with");
			return (false);
		}		
		
		debug ("Mapping project path ("+getProjectPath ()+") to db dir ...");
		
		if (dbType.getValue().equals("DOCS"))		
			driver.setDbDir (getProjectPath ()+"/system/documents");
		else
		{
			if (dbType.getValue().equals("USERS"))
				driver.setDbDir (getProjectPath ()+"/system/users");
			else
			{
				if (dbName.getValue().isEmpty()==true)
				{
					this.setErrorString("Error: please provide the name of a custom database");
					return (false);
				}
				
				driver.setDbDir (getProjectPath ()+"/system/custom/"+dbName.getValue());
			}
		}
		
		if (HoopLink.fManager.createDirectory (driver.getDbDir())==false)
		{
			this.setErrorString("Error creating database directory: "+driver.getDbDir());
			return (false);
		}

		driver.startDBService (dbType.getValue());
		
		typesToDB (inHoop);	
		
		for (int t=0;t<inData.size();t++)
		{
			HoopKV aKV=inData.get(t);
															
			ArrayList<Object> vals=aKV.getValuesRaw();
				
			for (int i=0;i<vals.size();i++)
			{
				HoopBerkeleyDBInstance mapped=(HoopBerkeleyDBInstance) driver.getDB(i);
				
				mapped.writeKV((String) vals.get(i),"Serialized Content");
			}			
		}			
				
		return (true);
	}	
	/**
	 * 
	 */
	public HoopBase copy ()
	{
		return (new HoopBerkeleyDBWriter ());
	}		
}
