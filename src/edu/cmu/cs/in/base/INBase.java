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

package edu.cmu.cs.in.base;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import edu.cmu.cs.in.base.INFilterBase;

public class INBase 
{
	private SimpleDateFormat df;	
	private String instanceName="Undefined";
	private String className="INBase";
	
	private ArrayList<INFilterBase> filters=null;
	
	/**
	 *
	 */
	public INBase () 
	{
		df=new SimpleDateFormat ("HH:mm:ss.SSS");
		filters=new ArrayList<INFilterBase> ();
	}
	/**
	 *
	 */
	public void setClassName (String aName)
	{
		className=aName;
	}
	/**
	 *
	 */
	public String getClassName ()
	{
		return (className);
	}
	/**
	 *
	 */    
	public String getCurrentDate ()
	{
		return (df.format(new Date()));
	}
	/**
	 *
	 */
	public ArrayList<INFilterBase> getFilters ()
	{
		return (filters);
	}
	/**
	 *
	 */
	public void addFilter (INFilterBase aFilter)
	{
		filters.add(aFilter);
	}
	/**
	 *
	 */
	public static String generateFileTimestamp ()
	{
		SimpleDateFormat dfStatic=new SimpleDateFormat ("HH-mm-ss-SSS");
		return (dfStatic.format(new Date()));
	}
	/**
	 *
	 */		
	public static void debug(String aClass,String s) 
	{
		
		StringBuffer buffer=new StringBuffer ();
					
		buffer.append(String.format ("[%s] [%d] <"+aClass+"> %s\n", generateFileTimestamp (), ++INFeatureMatrixLink.debugLine, s));

		System.out.print (buffer.toString());
		
		if (INLink.console!=null)
		{
			INLink.console.appendString (buffer.toString ());		
		}
	}    
	/**
	 *
	 */		
	public void debug(String s) 
	{
		StringBuffer buffer=new StringBuffer ();

		buffer.append (String.format ("[%s] [%d] <"+className+"> %s\n", generateFileTimestamp (), ++INFeatureMatrixLink.debugLine, s));

		System.out.print (buffer.toString());
		
		if (INLink.console!=null)
		{
			INLink.console.appendString (buffer.toString ());
		}
	}  
	/**
	 *
	 */		
	public void dumpDebugData(String s) 
	{
		debug ("dumpDebugData ()");
		
		INFileManager fManager=new INFileManager ();
		fManager.saveContents("C://temp/debug.txt",s);
	}
	/**
	 *
	 */	
	public String getInstanceName() 
	{
		return instanceName;
	}
	/**
	 *
	 */	
	public void setInstanceName(String instanceName) 
	{
		this.instanceName = instanceName;
	}
}

