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

import java.text.SimpleDateFormat;
import java.util.Date;

public class INHoopRoot
{
	public static int debugLine=0;
	
	private SimpleDateFormat df;	
	private String instanceName="Undefined";
	private String className="HoopBase";
	private String classType="Unknown"; // Used for serialization and should be a base type
	private String errorString="";
			
	/**
	 *
	 */
	public INHoopRoot () 
	{
		setClassName ("INHoopRoot");
		//debug ("INHoopRoot ()");
		
		df=new SimpleDateFormat ("HH:mm:ss.SSS");
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
	public static String generateFileTimestamp ()
	{
		SimpleDateFormat dfStatic=new SimpleDateFormat ("HH-mm-ss-SSS");
		return (dfStatic.format(new Date()));
	}
	/**
	 *
	 */		
	public static void debug (String aClass,String s) 
	{		
		StringBuffer buffer=new StringBuffer ();
					
		buffer.append(String.format ("[%s] [%d] <"+aClass+"> %s\n", generateFileTimestamp (), ++INHoopRoot.debugLine, s));

		System.out.print (buffer.toString());			
	}    
	/**
	 *
	 */		
	public void debug(String s) 
	{		
		StringBuffer buffer=new StringBuffer ();

		buffer.append (String.format ("[%s] [%d] <"+className+"> %s\n", generateFileTimestamp (), ++INHoopRoot.debugLine, s));

		System.out.print (buffer.toString());		
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
}
