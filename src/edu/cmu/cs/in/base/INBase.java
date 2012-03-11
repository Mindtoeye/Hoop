/** 
 * Author: Martin van Velsen <vvelsen@cs.cmu.edu>
 * 
 * This source set uses code written for other various graduate 
 * courses and is part of a larger research effort in the field of
 * interactive narrative (IN). Courses that have contribute
 * to this source base are:
 * 
 * 	05-834 Applied Machine Learning
 *  11-719 Computational Models of Discourse Analysis
 *  11-741 Information Retrieval
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
	public static void debug(String aClass,String s) 
	{
		SimpleDateFormat dfStatic=new SimpleDateFormat ("HH:mm:ss.SSS");
		StringBuffer buffer=new StringBuffer ();
					
		buffer.append(String.format ("[%s] [%d] <"+aClass+"> %s\n", dfStatic.format(new Date()), ++INFeatureMatrixLink.debugLine, s));

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

		buffer.append (String.format ("[%s] [%d] <"+className+"> %s\n", df.format(new Date()), ++INFeatureMatrixLink.debugLine, s));

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

