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

//import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JOptionPane;

import org.jdom.Element;

import edu.cmu.cs.in.base.INFilterBase;
import edu.cmu.cs.in.base.io.INFileManager;

public class INBase 
{
	private SimpleDateFormat df;	
	private String instanceName="Undefined";
	private String className="INBase";
	private String classType="Unknown"; // Used for serialization and should be a base type
	private String errorString="";
	
	private ArrayList<INFilterBase> filters=null;
	
	/** The output stream for {@link #debug(String)}. Default value {@link System#out}. */
	//public static PrintStream outStream = System.out;

	/** The error output stream. Default value {@link System#err}. */
	//public static PrintStream errStream = System.err;	
	
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
   public void setClassType (String aName)
   {
   	classType=aName;
   }
	/**
	 *
	 */
   	public String getClassType ()
   	{
   		return (classType);
   	} 	
	/**
	 * 
	 */	
	public void setErrorString(String errorString) 
	{
		this.errorString = errorString;
	}
	/**
	 * 
	 */	
	public String getErrorString() 
	{
		return errorString;
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
		if (INHoopLink.nodebug==true)
			return;
		
		StringBuffer buffer=new StringBuffer ();
					
		buffer.append(String.format ("[%s] [%d] <"+aClass+"> %s\n", generateFileTimestamp (), ++INHoopLink.debugLine, s));

		System.out.print (buffer.toString());
		
		if (INHoopLink.console!=null)
		{
			INHoopLink.console.appendString (buffer.toString ());		
		}
	}    
	/**
	 *
	 */		
	public void debug(String s) 
	{
		if (INHoopLink.nodebug==true)
			return;		
		
		StringBuffer buffer=new StringBuffer ();

		buffer.append (String.format ("[%s] [%d] <"+className+"> %s\n", generateFileTimestamp (), ++INHoopLink.debugLine, s));

		System.out.print (buffer.toString());
		
		if (INHoopLink.console!=null)
		{
			INHoopLink.console.appendString (buffer.toString ());
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
	public String getName() 
	{
		return instanceName;
	}
	/**
	 *
	 */	
	public void setName(String instanceName) 
	{
		this.instanceName = instanceName;
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
	/**
	 * 
	 */
	protected Element getClassElement ()
	{
		Element classElement=new Element (getClassName ());
				
		return (classElement);
	}
	/**
	 * 
	 */
	protected Element getClassElement (String aName)
	{
		Element classElement=new Element (getClassName ());
		
		classElement.setAttribute("name",aName);
		
		return (classElement);
	}	
	/**
	*	
	*/		
	protected String getClassOpen ()
	{
		return ("<"+getClassType ()+">");
	}
	/**
	*	
	*/		
	protected String getClassOpen (String aName)
	{
		return ("<"+getClassType ()+" name=\""+aName+"\">");
	}	
	/**
	*	
	*/		
	protected String getClassClose ()
	{
		return ("</"+getClassType ()+">");
	}
	/**
	 * 
	 */
	public static void alert (String aMessage)
	{
		JOptionPane.showMessageDialog(INHoopLink.mainFrame,aMessage);
	}
}

