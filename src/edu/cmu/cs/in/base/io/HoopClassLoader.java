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

package edu.cmu.cs.in.base.io;

import java.io.*;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.jar.JarFile;
import java.util.jar.JarEntry;

import edu.cmu.cs.in.base.HoopRoot;

/**
 * 
 */
public class HoopClassLoader extends ClassLoader
{
	private Hashtable<String, Class> classes; // map from class-name to class

	/**
	 * 
	 */
	public HoopClassLoader()
	{
		classes = new Hashtable<String, Class>();
	}
	/**
	 * 
	 */
	protected void debug (String aMessage)
	{
		HoopRoot.debug("HoopClassLoader",aMessage);
	}	
	/**
	 * 
	 */
	public Hashtable<String, Class> getClasses ()
	{
		debug ("getClasses ()");
		
		return (classes);
	}
	/**
	 * 
	 */
	/*
	public Class loadClass(String name) throws ClassNotFoundException
	{
		debug ("loadClass ("+name+")");
		
		return loadClass(name, true);
	}
	*/
	/**
	 * 
	 */
	/*
	public Class loadClass(String name, boolean resolve) throws ClassNotFoundException
	{
		debug ("loadClass ("+name+","+resolve+")");
		
		// first check local cache of classes
		Class cached = classes.get(name);
		if(cached != null)
		{
			debug ("Found class in cache");
			return cached;
		}

		// special case if we're looking for a "ThingDoer"
		if(name.equals("HoopInterface"))
		{
			debug ("Class is a HoopInterface, resolving ...");
			return loadHoopInterface(resolve);
		}

		debug ("Checking system classes ...");
		
		// check with the system class loader
		Class c = super.findSystemClass(name);
		classes.put(name, c);
		return c;
	}
	*/
	/**
	 * 
	 */
	private Class loadHoopInterface(boolean resolve) throws ClassNotFoundException
	{
		debug ("loadHoopInterface ("+resolve+")");
		
		JarFile jf;
		try
		{
			jf = new JarFile("lib.jar");
		}
		catch(IOException ioe)
		{
			throw new ClassNotFoundException();
		}

		for(Enumeration<JarEntry> entries = jf.entries(); entries.hasMoreElements(); )
		{
			JarEntry entry = entries.nextElement();
			
			byte[] bytes = jarEntryToBytes (jf,entry);

			/* determine whether this byte array is a class file, and if it is, whether it implements ThingDoer interface */
			try
			{
				/* Check if it implements the interface by defining the class */
				try
				{
					Class c = defineClass(null, bytes, 0, bytes.length);
					if(c == null)
					{
						continue;
					}
					Class[] interfaces = c.getInterfaces();
					boolean isThingDoer = false;
					for(Class thisInterface : interfaces)
					{
						if(thisInterface.getName().equals("ThingDoer") || thisInterface.getName().endsWith(".ThingDoer"))
						{
							isThingDoer = true;
							break;
						}
					}
					if(isThingDoer)
					{
						if(resolve)
						{
							resolveClass(c);
						}
						classes.put ("HoopInterface", c);
						return c;
					}
				}
				catch(ClassFormatError cfe)
				{
					continue; // move on to the next class in the jar file
				}
			}
			catch(IndexOutOfBoundsException ioobe)
			{
				continue; // this file is not long enough to be a class file; move on to the next one
			}
		}

		throw new ClassNotFoundException();
	}
	/**
	 * 
	 */
	public Hashtable<String, Class> loadHoopInterfaces (String aJarFile,boolean resolve)
	{
		debug ("loadHoopInterfaces ("+aJarFile+","+resolve+")");
						
		classes = new Hashtable<String, Class>();
		
		if (aJarFile.indexOf("Hoop")==-1)
		{
			debug ("Just for now the Hoop specific plugins need to have the name Hoop in them.");
			return (classes);
		}
		
		JarFile jf=null;
		
		try
		{
			jf = new JarFile(aJarFile);
		}
		catch(IOException ioe)
		{
			debug ("Error: jar file not found");
			return (null);
		}

		for (Enumeration<JarEntry> entries = jf.entries(); entries.hasMoreElements(); )
		{
			JarEntry entry = entries.nextElement();
			
			debug ("Examining jar entry: " + entry.getName());
			
			if (entry.getName().toLowerCase().indexOf(".class")!=-1)
			{						
				byte[] bytes = jarEntryToBytes (jf,entry);

				debug ("Determine whether this byte array is a class file, and if it is, whether it implements HoopInterface interface ...");
			
				try
				{
					debug ("Check if it implements the interface by defining the class ...");
				
					try
					{
						debug ("Trying to define class ...");
					
						Class c = defineClass (null, bytes, 0, bytes.length);
						if(c == null)
						{
							continue;
						}
					
						debug ("Running namespace check ...");
					
						Class[] interfaces = c.getInterfaces();
						boolean isThingDoer = false;
					
						for (Class thisInterface : interfaces)
						{
							if (thisInterface.getName().equals("HoopInterface") || thisInterface.getName().endsWith(".HoopInterface"))
							{
								debug ("Found interface: " + thisInterface.getName());
							
								isThingDoer = true;
								break;
							}
						}
					
						debug ("Adding to internal list of classes if resolved ...");
					
						if(isThingDoer)
						{					 
							if(resolve)
							{
								resolveClass(c);
							}
						
							classes.put("HoopInterface", c);
						}
					}
					catch(ClassFormatError cfe)
					{
						continue; // move on to the next class in the jar file
					}
				}
				catch(IndexOutOfBoundsException ioobe)
				{
					continue; // this file is not long enough to be a class file; move on to the next one
				}
			}	
		}
		
		return (classes);
	}	
	/**
	 * 
	 */
	private byte[] jarEntryToBytes (JarFile aJar,JarEntry anEntry)
	{
		debug ("jarEntryToBytes ()");
		
		InputStream is=null;
		try
		{
			is = aJar.getInputStream (anEntry);
		}
		catch(IOException ioe)
		{
			debug ("Error: unable to open jar entry");
			return (null);
		}
	
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int b;
	
		try
		{
			while((b = is.read()) != -1)
			{
				baos.write(b);
			}
		}
		catch(IOException e)
		{
			debug ("Error IOExceptions");
			return (null);
		}
	
		byte[] bytes = baos.toByteArray();		
		
		return (bytes);
	}
}
