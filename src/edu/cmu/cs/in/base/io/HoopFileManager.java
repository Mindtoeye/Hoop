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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;

/**
 * Notes:
 * - Turn this into one layer of a VFSL
 * - When combining jar as a FS with HDFS, use fully qualified
 * URIs to detect how file systems should be mounted. For
 * example, we could have:
 * 
 * 		file://textfile.txt
 * 		file://archive.jar?textfile.txt
 * 		hdfs://archive.jar?textfile.txt
 * 
 * The last one would result in a set of layers that would
 * be: hdfs -> jar -> file
 */
public class HoopFileManager extends HoopFileTools implements HoopVFSLInterface
{
	private String 				URI="";
	private StringBuilder 		contents=null;
	private BufferedReader 		permInput=null;
	private Writer 				streamOut=null;
	private FileOutputStream	streamOutBinary=null;
	
	private InputStream			streamInBinary=null;
	
	/**
	 *
	 */
	public HoopFileManager () 
	{
		setClassName ("HoopFileManager");
		debug ("HoopFileManager ()");
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
	public Writer getOutputStream ()
	{
		return (streamOut);
	}
	/**
	 * 
	 */
	public OutputStream getOutputStreamBinary ()
	{
		return (streamOutBinary);
	}	
	/**
	 *
	 */  
	public boolean isStreamOpen ()
	{
		if ((streamOut!=null) || (streamOutBinary!=null))
			return (true);
		
		return (false);
	}
	/**
	 *
	 */  
	public boolean openStream (String aFileURI) 
	{
		debug ("openStream ("+aFileURI+")");
		
		if (streamOut!=null)
		{
			debug ("Stream already open");
			return (true);
		}
		
		File aFile=new File (aFileURI);
		
		try 
		{
			aFile.createNewFile();
		} 
		catch (IOException e1) 
		{		
			e1.printStackTrace();
			return (false);
		} 
						
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

		//use buffering
		try 
		{			
			streamOut=new BufferedWriter (new FileWriter(aFile));
		}
		catch (IOException e) 
		{
			return (false);			
		}			
		
		return (true);
	}	
	/**
	 *
	 */  
	public boolean openStreamBinary (String aFileURI) 
	{
		debug ("openStreamBinary ("+aFileURI+")");
		
		if (streamOut!=null)
		{
			debug ("Stream already open");
			return (true);
		}
		
		File aFile=new File (aFileURI);
		
		try 
		{
			aFile.createNewFile();
		} 
		catch (IOException e1) 
		{		
			e1.printStackTrace();
			return (false);
		} 
						
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

		//use buffering
		try 
		{			
			streamOutBinary=new FileOutputStream (aFile);
		}
		catch (IOException e) 
		{
			return (false);			
		}			
		
		return (true);
	}		
	/**
	 *
	 */  
	public void closeStream ()
	{
		debug ("closeStream ()");
		
		if (streamOut!=null)
		{
			try 
			{
				streamOut.close();
				streamOut=null;
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
				debug ("Error closing output stream");
			}
		}
	}
	/**
	 *
	 */ 
	public void writeToStream (String aContents)
	{
		if (streamOut!=null)
		{
			try 
			{
				streamOut.write (aContents);
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try 
			{
				streamOut.flush();
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
	}	
	/**
	 *
	 */	
	public String readALine (String aFileURI)
	{    		
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
		//debug ("loadContents ("+aFileURI+")");
		
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
				 * MHoopUS the newline. it returns null only for the END of the 
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
		
		//debug ("Loaded " + contents.length() + " characters");
    
		return (contents.toString());
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
		
		if (checkCreate (aFileURI)==false)
			return (false);
		
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
	public boolean appendContents (String aFileURI,String aContents) 
	{
		debug ("appendContents ("+aFileURI+")");
		
		setURI (aFileURI);
		
		File aFile = new File (aFileURI);
		Writer output =null; 
		
		if (checkCreate (aFileURI)==false)
			return (false);

		//use buffering
		try 
		{			
			output=new BufferedWriter (new FileWriter(aFile,true));
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
	public InputStream openInputStream (String aFileURI)
	{
		debug ("openInputStream ("+aFileURI+")");
		
		File f = new File(aFileURI);
		
		try 
		{
			streamInBinary=new FileInputStream(f);
		} 
		catch (FileNotFoundException e) 
		{		
			e.printStackTrace();
			return (null);
		}
		
		return (streamInBinary);
	}	
} 
