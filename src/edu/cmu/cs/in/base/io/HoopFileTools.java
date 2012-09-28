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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
//import java.util.Enumeration;
//import java.util.jar.JarEntry;
//import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.cmu.cs.in.base.HoopLink;
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
	 * @param text
	 * @return
	 */
	public static boolean isValidName (String text)
	{
	    Pattern pattern = Pattern.compile
	    (
	        "# Match a valid Windows filename (unspecified file system).          \n" +
	        "^                                # Anchor to start of string.        \n" +
	        "(?!                              # Assert filename is not: CON, PRN, \n" +
	        "  (?:                            # AUX, NUL, COM1, COM2, COM3, COM4, \n" +
	        "    CON|PRN|AUX|NUL|             # COM5, COM6, COM7, COM8, COM9,     \n" +
	        "    COM[1-9]|LPT[1-9]            # LPT1, LPT2, LPT3, LPT4, LPT5,     \n" +
	        "  )                              # LPT6, LPT7, LPT8, and LPT9...     \n" +
	        "  (?:\\.[^.]*)?                  # followed by optional extension    \n" +
	        "  $                              # and end of string                 \n" +
	        ")                                # End negative lookahead assertion. \n" +
	        "[^<>:\"/\\\\|?*\\x00-\\x1F]*     # Zero or more valid filename chars.\n" +
	        "[^<>:\"/\\\\|?*\\x00-\\x1F\\ .]  # Last char is not a space or dot.  \n" +
	        "$                                # Anchor to end of string.            ", 
	        Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.COMMENTS
	    );
	    
	    Matcher matcher = pattern.matcher(text);
	    boolean isMatch = matcher.matches();
	    
	    return isMatch;
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
	public boolean removeDirectory (String aDirURI)
	{
		debug ("removeDirectory ("+aDirURI+")");

		File dir=new File (aDirURI);
		
	    if (dir.isDirectory()) 
	    {
	        String[] children = dir.list();
	        
	        for (int i=0; i<children.length; i++) 
	        {
	            boolean success = removeDirectory (dir + "/"+ children[i]);
	            
	            if (!success) 
	            {
	                return false;
	            }
	        }
	    }

	    // The directory is now empty so delete it
	    return dir.delete();
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
		
		if (basePath==null)
			return (true);
		
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
        	//debug (f.getName());
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
	/**
	 * 
	 */
	public Long getFileTime (String aURI)
	{
		File checker=new File (aURI);
		
		return (checker.lastModified ());
	}
	/**
	 * 
	 */
	public String getFileTimeString (Long aTimeStamp)
	{
        Date dateNow = new Date (aTimeStamp);
        
        SimpleDateFormat dateformatYYYYMMDD = new SimpleDateFormat(HoopLink.dateFormat);
        
        return (dateformatYYYYMMDD.format(dateNow));       
	}
	/**
	 * 
	 */
	public Long dateStringToLong (String aDate,String aFormat)
	{
		debug ("dateStringToLong ("+aDate+")");
		
    	DateFormat formatter = new SimpleDateFormat(aFormat);
    	
      	Date date=null;
      	
		try 
		{
			date = (Date) formatter.parse(aDate);
		} catch (ParseException e) 
		{
			e.printStackTrace();
			return (long) (0);
		}
      	
		return date.getTime();
	}
	/**
	 * 
	 */
	public Long dateStringToLong (String aDate)
	{
		debug ("dateStringToLong ("+aDate+")");
		
    	DateFormat formatter = new SimpleDateFormat(HoopLink.dateFormat);
    	
      	Date date=null;
      	
		try 
		{
			date = (Date) formatter.parse(aDate);
		} catch (ParseException e) 
		{
			e.printStackTrace();
			return (long) (0);
		}
      	
		return date.getTime();
	}	
} 
