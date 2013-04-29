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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import edu.cmu.cs.in.base.HoopRoot;

/**
 * 
 */
public class HoopVFSLJar extends HoopRoot implements HoopVFSLInterface
{
	/**
	 * 
	 */
	public HoopVFSLJar ()
	{
		setClassName ("HoopVFSLJar");
		debug ("HoopVFSLJar ()");		
	}

	@Override
	public StringBuilder getContents() 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getURI() 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setURI(String uRI) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isStreamOpen() 
	{
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * 
	 */
	@Override
	public Writer getOutputStream() 
	{
		debug ("getOutputStream ()");
				
		return null;
	}
	
	@Override
	public boolean openStream(String aFileURI) 
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void closeStream() 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void writeToStream(String aContents) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public String loadContents(String aFileURI) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean saveContents(String aFileURI, String aContents) 
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean appendContents(String aFileURI, String aContents) 
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean createDirectory(String aDirURI) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeDirectory(String aDirURI) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean doesFileExist(String aFileURI) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ArrayList<String> listDirectoryEntries(String aDirURI) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> listFiles(String aDirURI) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long getFileTime(String aPath) 
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	
	/**
	 * 
	 */
	public Boolean extractJarToDisk (String aJarFile,String aDest)
	{
		debug ("extractJarToDisk ("+aJarFile+","+aDest+")");
		
		JarFile jar=null;
		
		try 
		{
			jar = new JarFile(aJarFile);
		} 
		catch (IOException e1) 
		{
			e1.printStackTrace();
			return (false);
		}
		
		Enumeration<JarEntry> e = jar.entries();
				
		while (e.hasMoreElements()) 
		{
			JarEntry file = (JarEntry) e.nextElement();
			File f = new File(aDest + File.separator + file.getName());
			
			if (file.isDirectory()) 
			{ 
				// if its a directory, create it
				f.mkdir();
				continue;
			}
			
			InputStream is=null; // get the input stream
			
			try 
			{
				is = jar.getInputStream(file);
			} 
			catch (IOException e1) 
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
			
			FileOutputStream fos=null;
			
			try 
			{
				fos = new FileOutputStream(f);
			} 
			catch (FileNotFoundException e1) 
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			int avail=-1;
			
			try 
			{
				avail = is.available();
			} 
			catch (IOException e2) 
			{		
				e2.printStackTrace();
				
				try {jar.close ();} catch (IOException e3) {e3.printStackTrace();}
				
				return (false);
			}
			
			while (avail > 0) 
			{  
				// write contents of 'is' to 'fos'
				try 
				{
					fos.write(is.read());
				} 
				catch (IOException e1) 
				{
					e1.printStackTrace();
					
					try {jar.close ();} catch (IOException e2) {e2.printStackTrace();}
					
					return (false);
				}
				
				try 
				{
					avail=is.available();
				} 
				catch (IOException e1) 
				{				
					e1.printStackTrace();
					
					try {jar.close ();} catch (IOException e2) {e2.printStackTrace();}
					
					return (false);
				}
			}
			
			try 
			{
				fos.close();
			} 
			catch (IOException e1) 
			{
				debug ("Error, unable to close file output stream");
				e1.printStackTrace();
								
				try {jar.close ();} catch (IOException e2) {e2.printStackTrace();}
				
				return (false);
			}
			
			try 
			{
				is.close();
			} 
			catch (IOException e1) 
			{
				debug ("Error, unable to close file input stream");
				e1.printStackTrace();
				
				try {jar.close ();} catch (IOException e2) {e2.printStackTrace();}
				
				return (false);
			}
		}
		
		try {jar.close ();} catch (IOException e1) {e1.printStackTrace();}
		
		return (true);
	}		
	/**
	 * 
	 */
	public String extractJarEntry (String aJarFile,String aFile)
	{
		debug ("extractJarEntry ("+aJarFile+","+aFile+")");
		
		JarFile jar=null;
		
		try 
		{
			jar = new JarFile(aJarFile);
		} 
		catch (IOException e1) 
		{
			e1.printStackTrace();
			return (null);
		}
		
		Enumeration<JarEntry> e = jar.entries();
				
		while (e.hasMoreElements()) 
		{
			JarEntry file = (JarEntry) e.nextElement();
						
			if ((file.isDirectory()==false) && (file.getName().equals(aFile)==true)) 
			{
				InputStream is=null; // get the input stream
				
				try 
				{
					is = jar.getInputStream(file);
				} 
				catch (IOException e1) 
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
					return (null);
				} 
				
				HoopFileTools fTools=new HoopFileTools ();
									
				String entryContents=fTools.convertStreamToString (is);
								
				try 
				{
					is.close();
				} 
				catch (IOException e1) 
				{
					debug ("Error, unable to close file input stream");
					e1.printStackTrace();
					return (null);
				}
				
				try 
				{
					jar.close();
				} 
				catch (IOException e1) 
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			
				return (entryContents);
			}	
		}
		
		try 
		{
			jar.close();
		} 
		catch (IOException e1) 
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return (null);
	}
	/**
	 * 
	 */
	public String cleanPath (String aPath)
	{
		while (aPath.indexOf("//")!=-1)
		{
			aPath=aPath.replaceAll("//","/");
		}
		
		String clean2=aPath.replaceAll("\\\\\\\\","\\\\");
		
		return (clean2);
	}
	/**
	 * 
	 */
	private String getJarName (String aFileURI)
	{
		int splitIndex=aFileURI.indexOf("?");
		
		if (splitIndex==-1)
			return (aFileURI);
		
		return (aFileURI.substring(0, splitIndex));
	}
	/**
	 * 
	 */
	private String getJarFileName (String aFileURI)
	{
		int splitIndex=aFileURI.indexOf("?");
		
		if (splitIndex==-1)
		{
			return (aFileURI);
		}
		
		String splitString=aFileURI.substring(splitIndex+1);
		
		return (cleanPath (splitString));		
	}	
	/**
	 * We do not close the Jar here. Need to figure out a way
	 * to take care of that in the context of a VFSL
	 */	
	public InputStream getJarInputStream (String aFileURI)
	{
		debug ("getJarInputStream ("+aFileURI+")");
		
		JarFile jarFile=null;
		
		String jarName=getJarName(aFileURI);
				
		if (jarName==null)
		{
			debug ("Error: jarName is null");
			return (null);
		}
		else
			debug ("Using jar: " + jarName);
		
		try 
		{
			jarFile = new JarFile(jarName);
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return (null);
		}
		
		String jarFileName=getJarFileName (aFileURI);
		
		if (jarFileName==null)
		{
			debug ("Error: jarFileName is null");
			
			try {jarFile.close();} catch (IOException e1) {e1.printStackTrace();}
			
			return (null);
		}
		else
			debug ("Using jar: " + jarFileName);
		
		JarEntry entry = jarFile.getJarEntry (jarFileName);
		
		if (entry==null)
		{
			debug ("Error: entry is null");
			
			try {jarFile.close();} catch (IOException e1) {e1.printStackTrace();}
			
			return (null);
		}
		
		InputStream input=null;
		
		try 
		{
			input=jarFile.getInputStream (entry);
		}
		catch (IOException e) 
		{
			debug ("Unable to obtain inputstream for entry in jar");
			
			e.printStackTrace();
			
			try {jarFile.close();} catch (IOException e1) {e1.printStackTrace();}
			
			return (null);
		}				
		
		//try {jarFile.close();} catch (IOException e1) {e1.printStackTrace();}
		
		debug ("We should now have an open input stream set on our jar file entry");
		
		return (input);
	}
	/**
	 * 
	 */
	public String getJarContents (String aFileURI)
	{    
		debug ("getJarContents ("+aFileURI+")");
		
		JarFile jarFile=null;
		
		try 
		{
			jarFile = new JarFile(getJarName(aFileURI));
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ("");
		}
		
		JarEntry entry = jarFile.getJarEntry(getJarFileName (aFileURI));
		
		InputStream input=null;
		
		try 
		{
			input=jarFile.getInputStream(entry);
		}
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			try {jarFile.close();} catch (IOException e1) {e1.printStackTrace();}
			
			return ("");
		}		
		
		InputStreamReader isr = new InputStreamReader(input);
		BufferedReader reader = new BufferedReader(isr);
		String line;
		
		StringBuffer formatter=new StringBuffer ();
		
		try 
		{
			while ((line = reader.readLine()) != null)
			{
			     //System.out.println(line);
				formatter.append(line);
				formatter.append("\n");
			}
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ("");
		}
		
		try {jarFile.close();} catch (IOException e1) {e1.printStackTrace();}
		
		return (formatter.toString());
	}	
	/**
	 * 
	 */
	public Boolean jarFileExist (String aJarURI)
	{
		debug ("jarFileExist ("+aJarURI+")");
		
		String aJar=getJarName (aJarURI);
		String filePath=getJarFileName (aJarURI);
		
		debug ("Looking for " + filePath + " in " + aJar);
		
		JarFile jarFile=null;
		
		try 
		{
			jarFile=new JarFile(aJar);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
						
			return (false);
		}		
		
		Enumeration<JarEntry> enm = jarFile.entries();
		
		while (enm.hasMoreElements())
		{	         
	         JarEntry entry = (JarEntry) enm.nextElement();
	         
	         String name = entry.getName();
	         
	         if (name.equalsIgnoreCase(filePath)==true)
	         {
	        	 debug ("File found!");
	        	 
	        	 try {jarFile.close();} catch (IOException e1) {e1.printStackTrace();}
	        	 
	        	 return(true);
	         }	         
		}   
				
		debug ("File not found in jar!");
		
		try {jarFile.close();} catch (IOException e1) {e1.printStackTrace();}
		
		return (false);
	}
	/**
	 * 
	 */
	public void jarListContents (String aJarURI)
	{
		debug ("jarListContents ("+aJarURI+")");
		
		String aJar=getJarName (aJarURI);
		
		JarFile jarFile=null;
		
		try 
		{
			jarFile=new JarFile(aJar);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
						
			return;
		}
		
		Enumeration<JarEntry> enm = jarFile.entries();
		
		while (enm.hasMoreElements())
		{	         
	         JarEntry entry = (JarEntry) enm.nextElement();
	         String name = entry.getName();
	         long size = entry.getSize();
	         long compressedSize = entry.getCompressedSize();
	         //System.out.println(name + "\t" + size + "\t" + compressedSize);	         
	         debug (name + "\t" + size + "\t" + compressedSize);
		}   
		
		try 
		{
			jarFile.close();
		} 
		catch (IOException e1) 
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
	}
	/**
	 * 
	 */
	@Override
	public OutputStream getOutputStreamBinary() 
	{
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * 
	 */
	@Override
	public boolean openStreamBinary(String aFileURI) 
	{
		// TODO Auto-generated method stub
		return false;
	}
	/**
	 * 
	 */
	@Override
	public InputStream openInputStream(String aFileURI) 
	{
		// TODO Auto-generated method stub
		return null;
	}	
}
