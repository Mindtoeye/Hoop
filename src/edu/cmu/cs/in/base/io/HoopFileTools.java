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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import edu.cmu.cs.in.base.HoopRoot;

/**
 *
 */
public class HoopFileTools extends HoopRoot
{	
	/**
	 *
	 */
	public HoopFileTools () 
	{
		setClassName ("HoopFileTools");
		debug ("HoopFileTools ()");
	}
	/**
	 *
	 */	
	public boolean doesFileExist (String aFileURI)
	{
		debug ("getContents ("+aFileURI+")");
		
		File file=new File (aFileURI);
		
	    boolean exists = file.exists();
	    if (!exists) 
	    {
	     return (false);   
	    }
	    
	    return (true);
	}  
	/**
	 *
	 */
	public boolean createDirectory (String aDirURI)
	{
		debug ("createDirectory ("+aDirURI+")");
		
		if (doesFileExist (aDirURI)==true)
		{
			debug ("Directory already exists");
		}
		else
		{
		    boolean success = (new File(aDirURI)).mkdirs();
		    if (!success) 
		    {
		    	debug ("Unable to create directory");
		    	return (false);
		    }  			
		}
		
		return (true);
	}
	/**
	 * 
	 */
	public boolean checkCreate (String aFileURI)
	{
		debug ("checkCreate ()");
		
		// Check to see if the directory exists yet ...
		
		File checker=new File (aFileURI);
		
		String basePath=checker.getParent();
		
		if (createDirectory (basePath)==false)
			return (false);

		/*
		if (!aFile.exists()) 
		{
			debug ("File does not exist: " + aFile);
			return (false);
		}
						
		if (!aFile.isFile()) 
		{
			debug ("Should not be a directory: " + aFile);
			return (false);
		}
				
		if (!aFile.canWrite()) 
		{
			debug ("File cannot be written: " + aFile);
			return (false);
		}
		*/

		
		return (true);
	}
	/**
	 * 
	 */
	public String stripExtension (String aPath)
	{
		return (aPath.substring(0,aPath.lastIndexOf('.')));
	}
	/**
	 * 
	 */
	public String getExtension (String aPath)
	{
		int pos = aPath.lastIndexOf('.');
		String ext = aPath.substring(pos+1);
		
		return (ext);
	}
	/**
	 * 
	 */
	public ArrayList<String> listDirectoryEntries (String aPath)
	{
		debug ("listDirectoryEntries ("+aPath+")");
		
		ArrayList<String> files=new ArrayList<String> ();
		
		File actual = new File(aPath);
		
        for( File f : actual.listFiles())
        {
        	debug (f.getName());
       		files.add(f.getName());
        }		
        
        return files;
	}	
	/**
	 * 
	 */
	public ArrayList<String> listFiles (String aPath)
	{
		debug ("listFiles ("+aPath+")");
		
		ArrayList<String> files=new ArrayList<String> ();
		
		File actual = new File(aPath);
		
        for( File f : actual.listFiles())
        {        
        	if (f.isDirectory()==false)
        		files.add(f.getName());
        }		
        
        return files;
	}
	/** 
	 * @param is
	 * @return
	 */
	public String convertStreamToString(InputStream is) 
	{
	    try 
	    {
	        return new java.util.Scanner(is).useDelimiter("\\A").next();
	    } 
	    catch (java.util.NoSuchElementException e) 
	    {
	        return "";
	    }
	}
	/**
	 * 
	 */
	public Boolean extractJarToDisk (String aJarFile,String aDest)
	{
		debug ("extractJarToDisk ("+aJarFile+","+aDest+")");
		
		JarFile jar;
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
					return (false);
				}
				
				try 
				{
					avail=is.available();
				} 
				catch (IOException e1) 
				{				
					e1.printStackTrace();
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
				return (false);
			}
		}
		
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
									
				String entryContents=convertStreamToString (is);
						
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
			
				return (entryContents);
			}	
		}
		
		return (null);
	}
	/** 
	 * @param srFile
	 * @param dtFile
	 */
	public Boolean copyFile(String srFile, String dtFile)
	{
		debug ("copyFile ("+srFile+","+dtFile+")");
		
		try
		{
			File f1 = new File(srFile);
			File f2 = new File(dtFile);
			InputStream in = new FileInputStream(f1);		 
			OutputStream out = new FileOutputStream(f2);

			byte[] buf = new byte[1024];
			int len;
			
			while ((len = in.read(buf)) > 0)
			{
				out.write(buf, 0, len);
			}
			
			in.close();
			out.close();
			
			debug ("File copied.");
		  }
		
		  catch(FileNotFoundException ex)
		  {
			  debug (ex.getMessage() + " in the specified directory.");
			  return (false);
		  }
		  catch(IOException e)
		  {
			  debug (e.getMessage());  
		  }
		  
		  return (true);
	}	
	/** 
	 * @param srFile
	 * @param dtFile
	 */
	public Boolean copyFileAppend (String srFile, String dtFile)
	{
		debug ("copyFileAppend ("+srFile+","+dtFile+")");
		
		try
		{
			File f1 = new File(srFile);
			File f2 = new File(dtFile);
			InputStream in = new FileInputStream(f1);		  
			OutputStream out = new FileOutputStream(f2,true);

			byte[] buf = new byte[1024];
			int len;
			
			while ((len = in.read(buf)) > 0)
			{
				out.write(buf, 0, len);
			}
			
			in.close();
			out.close();
			
			debug ("File copied.");
		  }
		
		  catch(FileNotFoundException ex)
		  {
			  debug (ex.getMessage() + " in the specified directory.");
			  return (false);
		  }
		  catch(IOException e)
		  {
			  debug (e.getMessage());  
		  }
		  
		  return (true);
	}		
	/**
	 * WARNING! This method only works with fully specified paths. You
	 * can not use it with path that contains <PROJECTPATH>
	 */
	public String createSequenceFilename (String aURI,int anIndex)
	{
		debug ("createSequenceFilename ("+aURI+","+anIndex+")");
		
		StringBuffer formatter=new StringBuffer ();
		
		File transformer=new File (aURI);
		
		formatter.append(transformer.getParent()+"/"+stripExtension (transformer.getName())+"-"+anIndex+"."+getExtension (transformer.getName()));
		
		return (formatter.toString());
	}
} 
