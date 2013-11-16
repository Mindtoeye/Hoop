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

package edu.cmu.cs.in.hoop.hoops.load;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.net.ftp.FTPConnectionClosedException;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.io.CopyStreamException;

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.base.kv.HoopKVString;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.hoop.hoops.base.HoopLoadBase;
import edu.cmu.cs.in.hoop.properties.types.HoopEnumSerializable;
import edu.cmu.cs.in.hoop.properties.types.HoopStringSerializable;

/**
 * 
 */
public class HoopFTPReader extends HoopLoadBase
{	
	private static final long serialVersionUID = -1130808397513723791L;
	
	public HoopStringSerializable URL=null;
	public HoopEnumSerializable fileSource=null;
	
	/**
	 *
	 */ 
	public HoopFTPReader () 
	{		
		setClassName ("HoopFTPReader");
		debug ("HoopFTPReader ()");
				
		setHoopDescription ("Load file through FTP");	
	
		URL=new HoopStringSerializable (this,"URL","ftp://localhost/robots.txt");
		fileSource=new HoopEnumSerializable (this,"fileSource","As-Is,KV,CAS");
	}
	/**
	 * 
	 */
	public HoopBase copy ()
	{
		return (new HoopFTPReader ());
	}	
	/**
	 * 
	 */
	private String retrieveFTP (String aURL)
	{
		debug ("retrieveFTP ("+aURL+")");
		
		URL urlObject=null;

		try 
		{
			urlObject=new URL (aURL);
		} 
		catch (MalformedURLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		String downloadPath=this.projectToFullPath ("<PROJECTPATH>/tmp/download/");
		HoopLink.fManager.createDirectory(downloadPath);
		
		File translator=new File (urlObject.getFile());
		
		String localFileName="<PROJECTPATH>/tmp/download/"+translator.getName();
	 			
		OutputStream fileStream=null;
		
		if (HoopLink.fManager.openStreamBinary (this.projectToFullPath(localFileName))==false)
		{
			this.setErrorString("Error opening temporary output file");
			return (null);
		}
	 
		fileStream=HoopLink.fManager.getOutputStreamBinary();
		
		if (fileStream==null)
		{
			this.setErrorString("Error opening temporary output file");
			return (null);
		}			
		
		debug ("Starting FTP client ...");
		
		FTPClient ftp = new FTPClient();
     
		try 
		{
			int reply;
			
			debug ("Connecting ...");
	      
			ftp.connect(urlObject.getHost());
	      
			debug ("Connected to " + urlObject.getHost() + ".");
			debug (ftp.getReplyString());

			// After connection attempt, you should check the reply code to verify
			// success.
			reply=ftp.getReplyCode();

			if (!FTPReply.isPositiveCompletion (reply)) 
			{
				ftp.disconnect();
				debug ("FTP server refused connection.");
				return (null);
			}
			else
			{
				ftp.login ("anonymous","hoop-dev@gmail.com");
	    	  
				reply=ftp.getReplyCode();

				if (!FTPReply.isPositiveCompletion (reply)) 
				{
					ftp.disconnect();
					debug ("Unable to login to FTP server");
					return (null);
				}			    
		      
				debug ("Logged in");

				boolean rep=true;
								
				String pathFixed=translator.getParent().replace("\\", "/");
						      
				rep=ftp.changeWorkingDirectory (pathFixed);
				if (rep==false)
				{
					debug ("Unable to change working directory to: " + pathFixed);
					return (null);
				}
				else
				{
					debug ("Current working directory: " + pathFixed);
					
					debug ("Retrieving file " + urlObject.getFile() + " ...");
					
					try
					{
						rep=ftp.retrieveFile (urlObject.getFile(),fileStream);
					}
					catch (FTPConnectionClosedException connEx)
					{
						debug ("Caught: FTPConnectionClosedException");
						connEx.printStackTrace();
						return (null);
					}
					catch (CopyStreamException strEx)
					{
						debug ("Caught: CopyStreamException");
						strEx.printStackTrace();
						return (null);				    		  
					}
					catch (IOException ioEx)
					{
						debug ("Caught: IOException");
						ioEx.printStackTrace();
						return (null);				  				    		  
					}
					
					debug ("File retrieved");
				}
	      
				ftp.logout();
			}	  
	    } 
	  	catch(IOException e) 
	  	{
	  		debug ("Error retrieving FTP file");
	  		e.printStackTrace();
	  		return (null);
	  	} 
	  	finally 
	  	{
	  		if (ftp.isConnected()) 
	  		{
	  			debug ("Closing ftp connection ...");
	  			
	  			try 
	  			{
	  				ftp.disconnect();
	  			} 
	  			catch(IOException ioe) 
	  			{
	  				debug ("Exception closing ftp connection");
	  			}
	  		}
	  	}	
	  	
		debug ("Closing local file stream ...");
		
		try 
		{
			fileStream.close();
		} 
		catch (IOException e) 
		{	
			e.printStackTrace();
		}
		
		String result=HoopLink.fManager.loadContents(this.projectToFullPath(localFileName));
		
		return (result);
	}	
	/**
	 * We're expecting a KV object that has a key of type String representing the file URI,
	 * where the content value (also a String) represents the flat text of the file.
	 */
	public Boolean runHoop (HoopBase inHoop)
	{		
		debug ("runHoop ()");

		if (HoopLink.project.getVirginFile()==true)
		{
			alert ("Error: please save your project first before running the ftp hoop");
			return (false);
		}		
			
		//>------------------------------------------------------------------
		
		if (fileSource.getValue().equalsIgnoreCase("as-is")==true)
		{			
			if (URL.getValue().isEmpty()==true)
			{
				this.setErrorString("Please provide a url");
				return (false);
			}
			
			if (URL.getValue().toLowerCase().indexOf("ftp://")==-1)
			{
				this.setErrorString("Error: the url does not start with http://");
				return (false);
			}	
			
			HoopKVString newData=new HoopKVString ();
		
			newData.setKey(URL.getValue());
		
			String aResult="";
				
			aResult=retrieveFTP (URL.getValue());
		
			if (aResult==null)
			{
				this.setErrorString ("FTP routine returned null data");
				return (false);
			}
		
			newData.setValue (aResult);
		
			this.addKV (newData);
		}
		
		//>------------------------------------------------------------------
		
		if (fileSource.getValue().equalsIgnoreCase("kv")==true)
		{
			
		}
		
		//>------------------------------------------------------------------		
		
		return (true);
	}	
}
