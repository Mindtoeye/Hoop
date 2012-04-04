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

package edu.cmu.cs.in.controls.base;

import java.awt.Component;
import java.awt.Dimension;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;

import org.w3c.dom.Element;

//import edu.cmu.pact.Utilities.trace;
import edu.cmu.cs.in.base.INBase;
import edu.cmu.cs.in.base.INXMLBase;

public class INHoopJDialog extends JDialog
{	
	private static final long serialVersionUID = 1L;
	
	/** The output stream for {@link #debug(String)}. Default value {@link System#out}. */
	protected static PrintStream outStream = System.out;

	/** The error output stream. Default value {@link System#err}. */
	protected static PrintStream errStream = System.err;

	private SimpleDateFormat df;
	private String className=getClass().getSimpleName();
	
	private String name="";
	
    protected ImageIcon defaultIcon=null;	
	
	/**
	 *
	 */
    public INHoopJDialog () 
    {
    	df=new SimpleDateFormat ("HH:mm:ss.SSS");
    }
	/**
	 *
	 */
    public INHoopJDialog (JFrame parent, String title, Boolean modal) 
    {
    	super(parent, title, true);
   		df=new SimpleDateFormat ("HH:mm:ss.SSS");
    }    
	/**
	 *
	 */
    public void setName (String aName)
    {
    	name=aName;
    }
	/**
	 *
	 */
    public String getName ()
    {
    	return (name);
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
	protected String getClassOpen ()
	{
		return ("<"+getClassName ()+">");
	}
	/**
	*	
	*/		
	protected String getClassOpen (String aName)
	{
		return ("<"+getClassName ()+" name=\""+aName+"\">");
	}	
	/**
	*	
	*/		
	protected String getClassClose ()
	{
		return ("</"+getClassName ()+">");
	}	
	/**
	*	
	*/		
	public String toString()
	{
		StringBuffer buffer=new StringBuffer();
		buffer.append(getClassOpen ()+"<name>"+getName ()+"</name>"+getClassClose ());
		return (buffer.toString ());
	}    
	/**
	 *
	 */
    public void fromString (String xml)
    {
    	debug ("fromString ()");
    	
    	/*
        SAXBuilder builder = new SAXBuilder();
        Document doc=null;
		try {
			doc = builder.build(new StringReader(xml));
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
		  
		Element root=doc.getRootElement();
		*/
    	
    	INXMLBase driver=new INXMLBase ();
    	Element elm=driver.fromXMLString(xml);
		
		fromXML (elm);
    }
	/**
	 *
	 */
    public void fromXML (Element root)
    {
    	debug ("fromXML ()");	
    }	
	/**
	 *
	 */		
	public static void debug(String aClass,String s) 
	{
		INBase.debug(aClass, s);
	}    
	/**
	 *
	 */		
	public void debug(String s) 
	{
		INBase.debug("INHoopDialog", s);
		
		/*
		if (trace.getDebugCode("br"))
		{
			if (outStream == null)
				return;
			outStream.printf ("[%s] [%d] <"+className+"> %s\n",df.format(new Date()), ++INHoopSSELink.debugLine, s);
			outStream.flush();
		}
		*/	
	}	
	/**
	 * Write a string and a stack backtrace.
	 * @param s
	 * @param e
	 */
	public void debugStack(String s, Throwable e) {
		if (outStream == null)
			return;
		debug(s);
		e.printStackTrace(outStream);
		outStream.flush();
	}
	/**
	 * @return the outStream
	 */
	public static PrintStream getOutStream() {
		return outStream;
	}

	/**
	 * @return the errStream
	 */
	public static PrintStream getErrStream() {
		return errStream;
	}

	/**
	 * @param outStream the outStream to set
	 */
	/*
	public static void setOutStream(PrintStream outStream) 
	{
		INBase.outStream = outStream;
	}
	*/
	/**
	 * @param errStream the errStream to set
	 */
	/*
	public static void setErrStream(PrintStream errStream) 
	{
		INBase.errStream = errStream;
	}
	*/
	/**
	 * Convenience for creating vector from string
	 * @param s
	 * @return Vector with s as first element; null if s is null 
	 */
	protected Vector<String> s2v(String s) 
	{
		if (s == null)
			return null;
		Vector<String> result = new Vector<String>();
		result.add(s);
		return result;
	}		
	/** 
	 * Returns an ImageIcon, or null if the path was invalid. 
	 */
	protected ImageIcon createImageIcon (String aFile,String description) 
	{				
		debug ("createImageIcon ("+aFile+")");
		
		ClassLoader loader=getClass ().getClassLoader();
		if (loader==null)
		{
			debug ("Error: no class loader object available");
			return (defaultIcon);
		}
		
		URL resource=loader.getResource("pact/CommWidgets/"+aFile);
		if (resource==null)
		{
			debug ("Error: unable to find image resource in jar file");
			return (defaultIcon);
		}			
		
		return (new ImageIcon (resource,description));
	}
	/**
	 * 
	 */	
	public Box addInHorizontalLayout (Component comp,int maxX,int maxY) 
	{
		Box dynamicBox=new Box (BoxLayout.X_AXIS);
		dynamicBox.setMinimumSize(new Dimension (20,20));
		dynamicBox.setMaximumSize(new Dimension (maxX,maxY));
		dynamicBox.add(comp);
		return (dynamicBox);
	}
	/**
	 * 
	 */	
	public Box addInVerticalLayout (Component comp) 
	{
		Box dynamicBox=new Box (BoxLayout.Y_AXIS);
		dynamicBox.add(comp);
		return (dynamicBox);
	}	
}
