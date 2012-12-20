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

import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.net.ftp.FTPConnectionClosedException;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.io.CopyStreamException;

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.base.io.HoopHTTPReader;
import edu.cmu.cs.in.base.kv.HoopKVString;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.hoop.hoops.base.HoopLoadBase;
import edu.cmu.cs.in.hoop.properties.types.HoopStringSerializable;

/**
 * 
 */
public class HoopFTPReader extends HoopLoadBase
{	
	private static final long serialVersionUID = -1130808397513723791L;
	
	public	HoopStringSerializable URL=null;
	//private HoopHTTPReader reader=null;
	
	/**
	 *
	 */ 
	public HoopFTPReader () 
	{		
		setClassName ("HoopFTPReader");
		debug ("HoopFTPReader ()");
				
		setHoopDescription ("Read text data from a URL");	
	
		URL=new HoopStringSerializable (this,"URL","ftp://localhost/robots.txt");
		
		//reader=new HoopHTTPReader ();
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
		
		String localFileName="<PROJECTPATH>/tmp/download/"+urlObject.getFile();
		String serverPath=urlObject.getPath();
	 			
		OutputStream fileStream=null;
		
		if (HoopLink.fManager.openStream (this.projectToFullPath(urlObject.getFile()))==false)
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
		
		FTPClient ftp = new FTPClient();
     
		try 
		{
			int reply;
	      
			ftp.connect(urlObject.getHost());
	      
			System.out.println ("Connected to " + urlObject.getHost() + ".");
			System.out.print (ftp.getReplyString());

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
				ftp.login ("anonymous","narratoria-dev@gmail.com");
	    	  
				reply=ftp.getReplyCode();

				if (!FTPReply.isPositiveCompletion (reply)) 
				{
					ftp.disconnect();
					debug ("Unable to login to FTP server");
					return (null);
				}			    
		      
				debug ("Logged in");

				boolean rep=true;
		      
				rep=ftp.changeWorkingDirectory (serverPath);
				if (rep==false)
				{
					debug ("Unable to change working directory to: " + serverPath);
					return (null);
				}
				else
				{
					debug ("Current working directory: " + serverPath);
					debug ("Retrieving file ...");
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
	  			try 
	  			{
	  				ftp.disconnect();
	  			} 
	  			catch(IOException ioe) 
	  			{
	  				// do nothing
	  			}
	  		}
	  	}	
	  		
		String result=HoopLink.fManager.loadContents(localFileName);
		
		return (result);
	}	
	/**
	 * We're expecting a KV object that has a key of type String representing the file URI,
	 * where the content value (also a String) represents the flat text of the file.
	 */
	public Boolean runHoop (HoopBase inHoop)
	{		
		debug ("runHoop ()");
				
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
		
		newData.setValue (aResult);
		
		this.addKV (newData);
		
		return (true);
	}	
}
