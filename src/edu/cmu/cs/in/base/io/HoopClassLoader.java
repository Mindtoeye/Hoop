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
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarFile;
import java.util.jar.JarEntry;

import edu.cmu.cs.in.base.HoopRoot;

/**
 * 
 */
public class HoopClassLoader extends ClassLoader
{
	private ArrayList<HoopPluginClassProxy> classes; // map from class-name to class

	/**
	 * 
	 */
	public HoopClassLoader()
	{
		classes = new ArrayList<HoopPluginClassProxy>();
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
	public ArrayList<HoopPluginClassProxy> getClasses ()
	{
		debug ("getClasses ()");
		
		return (classes);
	}
	/**
	 * 
	 */
	public ArrayList<HoopPluginClassProxy> loadHoopInterfaces (String aJarFile,boolean resolve)
	{
		debug ("loadHoopInterfaces ("+aJarFile+","+resolve+")");
						
		classes = new ArrayList<HoopPluginClassProxy>();
		
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
										
						Class[] interfaces = c.getInterfaces();

						debug ("Running namespace check on "+interfaces.length+" declared interfaces ...");
					
						for (Class thisInterface : interfaces)
						{
							debug ("Checking: " + thisInterface.getName());
							
							if (thisInterface.getName().equals("HoopInterface") || thisInterface.getName().endsWith(".HoopInterface"))
							{
								debug ("Found interface (HoopInterface): " + thisInterface.getName());
							
								if(resolve)
								{
									resolveClass(c);
								}
							
								HoopPluginClassProxy newProxy=new HoopPluginClassProxy ();
								newProxy.classType="HoopInterface";
								newProxy.reference=c;
								
								classes.add(newProxy);					
							}
							
							if (thisInterface.getName().equals("HoopViewInterface") || thisInterface.getName().endsWith(".HoopViewInterface"))
							{
								debug ("Found interface (HoopViewInterface): " + thisInterface.getName());
							
								if(resolve)
								{
									resolveClass(c);
								}
							
								HoopPluginClassProxy newProxy=new HoopPluginClassProxy ();
								newProxy.classType="HoopViewInterface";
								newProxy.reference=c;
								
								classes.add(newProxy);					
							}
						}
					}
					catch(ClassFormatError cfe)
					{
						debug ("Ignoring ClassFormatError ..., moving on to the next class in the jar file");
						continue;
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
	/**
	 * Based on: http://tutorials.jenkov.com/java-reflection/dynamic-class-loading-reloading.html
	 */
    public Class<?> loadClass(String name,String ifaceClass) throws ClassNotFoundException 
    {
    	debug ("loadClass ("+name+","+ifaceClass+")");
    	    	
        String url = name;
        
        if (name.indexOf("file:")==-1)
        	url = "file:" + name;

        debug ("Loading: " + url);
                
        try 
        {
            URL myUrl = new URL(url);
            
            URLConnection connection = myUrl.openConnection();
            
            InputStream input = connection.getInputStream();
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int data = input.read();

            while(data != -1)
            {
                buffer.write(data);
                data = input.read();
            }

            input.close();

            byte[] classData = buffer.toByteArray();

            return defineClass(ifaceClass, classData, 0, classData.length);

        } 
        catch (MalformedURLException e) 
        {
            e.printStackTrace();
        } 
        catch (IOException e) 
        {
            e.printStackTrace(); 
        }

        return null;
    }
	
}
