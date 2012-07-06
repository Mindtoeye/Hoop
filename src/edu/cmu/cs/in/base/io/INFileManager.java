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

import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.ArrayList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import edu.cmu.cs.in.base.INBase;

public class INFileManager extends INBase
{
	private ArrayList<String> files=null;	
	private String URI="";
	private StringBuilder contents=null;
	private String [] lineList=null;
	private ArrayList<String> lines=null;
	private BufferedReader permInput=null;
	
	/**
	 *
	 */
	public INFileManager () 
	{
		setClassName ("INFileManager");
		debug ("INFileManager ()");
	}
	/**
	 *
	 */	
	public StringBuilder getContents() 
	{
		return contents;
	}	
	/**
	 *
	 */	
	public String getURI() 
	{
		return URI;
	}
	/**
	 *
	 */	
	public void setURI(String uRI) 
	{
		URI = uRI;
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
		    boolean success = (new File(aDirURI)).mkdir();
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
	public String readALine (String aFileURI)
	{    
		//debug ("loadContents ("+aFileURI+")");
		
		if (permInput!=null)
		{
			try 
			{
				String result=permInput.readLine();
				if (result==null)
				{
					permInput.close();
					permInput=null;
				}
				
				return (result);
			}
			catch (IOException e)
			{
				permInput=null;
				return (null);
			}			
		}
		
		setURI (aFileURI);
		
		File aFile=new File(aFileURI);
   
		try 
		{						
			permInput=new BufferedReader (new FileReader(aFile));
						
			try 
			{
				String result=permInput.readLine();
				if (result==null)
				{
					permInput.close();
					permInput=null;
				}
				
				return (result);
			}
			catch (IOException e)
			{
	
			}
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
			permInput=null;
			return (null);
		}
		
		permInput=null;	
		return (null);
	}	
	/**
	 *
	 */	
	public String loadContents (String aFileURI)
	{    
		debug ("loadContents ("+aFileURI+")");
		
		setURI (aFileURI);

		if (doesFileExist (aFileURI)==false)
		{
			this.setErrorString ("Error, file does not exist: " + aFileURI);
			return (null);
		}
		
		File aFile=new File(aFileURI);

		contents=new StringBuilder();
    
		try 
		{						
			BufferedReader input =  new BufferedReader (new FileReader(aFile));
						
			try 
			{
				String line = null; //not declared within while loop
				
				/* readLine is a bit quirky : it returns the content of a line 
				 * MINUS the newline. it returns null only for the END of the 
				 * stream. it returns an empty String if two newlines appear 
				 * in a row.
				 */
				while (( line = input.readLine()) != null)
				{
					contents.append(line);
					contents.append(System.getProperty("line.separator"));
				}
			}
			catch (IOException e)
			{
				return (null);				
			}
			finally 
			{
				input.close();
			}
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
			return (null);
		}
    
		return (contents.toString());
	}
	/**
	 *
	 */	
	public ArrayList<String> dataToLines (String aData)
	{    
		debug ("dataToLines ()");
		
		lines=new ArrayList<String> ();		
	
		lineList=aData.split(System.getProperty("line.separator"));
		
		for (int i=0;i<lineList.length;i++)
		{
			lines.add(new String (lineList [i]));
		}
		
		return (lines);
	}
	/**
	 *
	 */	
	public ArrayList<String> loadLines (String aFileURI)
	{    
		debug ("loadLines ("+aFileURI+")");
		
		setURI (aFileURI);
				
		File aFile=new File(aFileURI);

		//contents=new StringBuilder();
   
		lines=new ArrayList<String> ();	
		
		try 
		{						
			BufferedReader input =  new BufferedReader (new FileReader(aFile));
						
			try 
			{
				String line = null; //not declared within while loop
				
				/* readLine is a bit quirky : it returns the content of a line 
				 * MINUS the newline. it returns null only for the END of the 
				 * stream. it returns an empty String if two newlines appear 
				 * in a row.
				 */
				while (( line = input.readLine()) != null)
				{
					//lines.add(line.trim());
					//contents.append(line);					
					//contents.append(System.getProperty("line.separator"));
					lines.add(line.trim ());
				}
			}
			catch (IOException e)
			{
				return (null);				
			}
			finally 
			{
				input.close();
			}
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
			return (null);
		}
   		
		return (lines);
	}	
	/**
	 *
	 */ 	
	public Element loadContentsXML (String aFileURI)
	{
		debug ("getContentsXML ("+aFileURI+")");
		
		setURI (aFileURI);
		
		Document               document=null;
		DocumentBuilderFactory dbf     =null;
		DocumentBuilder        builder =null;  
		  
		try 
		{
			dbf    =DocumentBuilderFactory.newInstance();
			builder=dbf.newDocumentBuilder ();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			return (null);
		} 
		  
		try
		{
			document=builder.parse (new File (aFileURI));
		}
		catch (IOException e) 
		{
			e.printStackTrace();
			return (null);   
		}    
		catch (SAXException e) 
		{
			debug ("Error while parsing file");
			return (null);   
		}   
		  
		Element root=document.getDocumentElement();
		
		return (root);
	}
	/**
	 *
	 */  
	public int countLines (String aFileURI) 
	{
		debug ("countLines ("+aFileURI+")");
		
		setURI (aFileURI);
		
		int results=0;
		
		File aFile = new File (aFileURI);

		try 
		{						
			BufferedReader input =  new BufferedReader (new FileReader(aFile));
						
			try 
			{
				@SuppressWarnings("unused")
				String line=null; //not declared within while loop
				
				/* readLine is a bit quirky : it returns the content of a line 
				 * MINUS the newline. it returns null only for the END of the 
				 * stream. it returns an empty String if two newlines appear 
				 * in a row.
				 */
				while (( line = input.readLine()) != null)
				{
					results++;
				}
			}
			catch (IOException e)
			{
				return (-1);				
			}
			finally 
			{
				input.close();
			}
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
			return (-1);
		}		
				
		return (results);
	}
	/**
	 *
	 */  
	public boolean saveContents (String aFileURI,String aContents) 
	{
		debug ("saveContents ("+aFileURI+")");
		
		setURI (aFileURI);
		
		File aFile = new File (aFileURI);
		Writer output =null; 
		
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

		//use buffering
		try 
		{			
			output=new BufferedWriter (new FileWriter(aFile));
		}
		catch (IOException e) 
		{
			debug ("Exception: IOException while opening output file");
			return (false);			
		}			
		
		try 
		{
			// FileWriter always assumes default encoding is OK!
			output.write (aContents);			
		}
		catch (IOException e) 
		{
			debug ("Exception: IOException while writing contents to disk");
			
			try
			{
				output.close();
			}
			catch (IOException closeException)
			{
				debug ("Exception: closeException while closing file writer");
				return (false);
			}
			
			return (false);
		}
		
		try
		{
			output.flush();
		}
		catch (IOException e) 
		{
			debug ("Exception: IOException while flushing contents to disk");
		}
		
		try
		{
			output.close();
		}
		catch (IOException e) 
		{
			debug ("Exception: IOException closing output file");
		}
		
		return (true);
	}
	/**
	 * 
	 */
	public void listLines (ArrayList<String> aLines)
	{
		debug ("listLines ()");
		
		/*
		for (int i=0;i<aLines.size();i++)
		{
			debug ("* "+aLines.get(i)+" *");
		}
		*/
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
	public ArrayList<String> listFiles (String aPath)
	{
		debug ("listFiles ("+aPath+")");
		
		files=new ArrayList<String> ();
		
        //File actual = new File(".");
		File actual = new File(aPath);
		
        for( File f : actual.listFiles())
        {
            //System.out.println( f.getName() );
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
		
		Enumeration e = jar.entries();
				
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
		
		Enumeration e = jar.entries();
				
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
} 
