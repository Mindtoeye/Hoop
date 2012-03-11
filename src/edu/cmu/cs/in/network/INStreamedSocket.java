/** 
 * Author: Martin van Velsen <vvelsen@cs.cmu.edu>
 * 
 * This source set uses code written for other various graduate 
 * courses and is part of a larger research effort in the field of
 * interactive narrative (IN). Courses that have contribute
 * to this source base are:
 * 
 * 	05-834 Applied Machine Learning
 *  11-719 Computational Models of Discourse Analysis
 *  11-741 Information Retrieval
 * 
 */

package edu.cmu.cs.in.network;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

import edu.cmu.cs.in.base.INBase;

/**
 * 
 */
public class INStreamedSocket extends INBase
{
	private String data="";
	private PrintWriter outWriter=null;
	private Socket clientSocket=null;
	private InputStream is=null;
	private int socketTimeout=2000; // Just two seconds, we want to keep it tight
	
	/**
	*
	*/	
	public INStreamedSocket ()
	{  
    	setClassName ("INStreamedSocket");
    	debug ("INStreamedSocket ()");

	}
	/**
	 * To convert the InputStream to String we use the
	 * Reader.read(char[] buffer) method. We iterate until the
	 * Reader return -1 which means there's no more data to
	 * read. We use the StringWriter class to produce the string.
	*/	
    public String convertStreamToString (InputStream is)
    {
    	debug ("convertStreamToString ()");
    	
        if (is != null) 
        {
            Writer writer = new StringWriter();

            char[] buffer = new char[1024];
            
            try 
            {
                Reader reader=null;
                
				try 
				{
					reader = new BufferedReader (new InputStreamReader(is, "UTF-8"));
				}
				catch (UnsupportedEncodingException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
					debug ("UnsupportedEncodingException");
				}
                int n;
                
                try 
                {
					while ((n = reader.read(buffer)) != -1) 
					{
					    writer.write (buffer, 0, n);
					}
				} 
                catch (IOException e) 
                {
					// TODO Auto-generated catch block
					e.printStackTrace();
					debug ("IOException");
				}
            } 
            finally 
            {
                try 
                {
					is.close();
				} 
                catch (IOException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
					debug ("IOException");
				}
            }
            
            return writer.toString();
        } 
        else 
        {        
            return "";
        }
    }	
	/**
	 * 
	 */
	public String sendAndReceiveXML (String aHost,int aPort,String aMessage)
	{
		debug ("sendAndReceive ("+aHost+","+aPort+")");
			
		return (sendAndReceive (aHost,aPort,"<?xml version=\"1.0\" encoding=\"utf-8\"?>"+aMessage));		
	}	        
	/**
	 * 
	 */
	public String sendAndReceive (String aHost,int aPort,String aMessage)
	{
		debug ("sendAndReceive ("+aHost+","+aPort+")");
						
		if (clientSocket==null)
		{
			outWriter=null; // Reset
			is=null; // Reset
					
			try
			{				
				SocketAddress sockaddr = new InetSocketAddress(aHost,aPort);
				clientSocket=new Socket();
				clientSocket.connect (sockaddr,socketTimeout);				
				
				outWriter = new PrintWriter (clientSocket.getOutputStream(),true);
				is=clientSocket.getInputStream();
				
				if (is==null)
				{
					return ("");
				}
			} 
			catch (UnknownHostException e) 
			{
				debug ("Unknown host: " + aHost);
				return ("");
			} 
			catch  (IOException e) 
			{
				debug ("No I/O or connection timeout");
				return ("");
			}
		}	
	
		outWriter.println (aMessage+"\0");
		
		data=convertStreamToString (is);
		
		//debug ("Data " + data);
		
		return data;
	}
	/**
	 * 
	 */
	public Boolean sendAndKeepOpen (String aHost,int aPort,String aMessage)
	{
		debug ("sendAndKeepOpen ("+aHost+","+aPort+")");
						
		if (clientSocket==null)
		{
			outWriter=null; // Reset
			is=null; // Reset
			
			try
			{
				debug ("Creating socket ...");
								
				SocketAddress sockaddr = new InetSocketAddress(aHost,aPort);
				clientSocket=new Socket();
				clientSocket.connect (sockaddr,socketTimeout);	
								
				debug ("Created socket, creating printwriter ...");
				
				outWriter = new PrintWriter (clientSocket.getOutputStream(),true);
				
				debug ("Created socket, connecting input stream ...");
				
				is=clientSocket.getInputStream();
				
				if (is==null)
				{
					outWriter=null;
					is=null;
					clientSocket=null;					
					return (false);
				}				
			} 
			catch (UnknownHostException e) 
			{
				debug ("Unknown host: " + aHost);
				outWriter=null;
				is=null;
				clientSocket=null;				
				return (false);
			} 
			catch (SecurityException e)
			{
				debug ("A security exception occurred while connecting to the remote host");
				outWriter=null;
				is=null;
				clientSocket=null;				
				return (false);				
			}
			catch  (IOException e) 
			{
				debug ("No I/O or connection timeout");
				outWriter=null;
				is=null;
				clientSocket=null;				
				return (false);
			}
		}	
	
		outWriter.println (aMessage+"\0");
				
		return (true);
	}	
	/**
	 * 
	 */
	public void close ()
	{
		if (clientSocket!=null)
		{
			try 
			{
				clientSocket.close();
			} 
			catch (IOException e) 
			{				
				debug ("Error closing socket");
				e.printStackTrace();
			}
			
			outWriter=null;
			is=null;
			clientSocket=null;
		}
	}	
	/**
	 * 
	 * Read the characters from a Reader into a String. Reads until receives the
	 * given end-of-message character, which it consumes without returning.
	 * 
	 * @param rdr
	 *            Reader to read; should be DataInputStream or equivalent for
	 *            efficiency
	 * @param eom
	 *            end-of-message character; not returned with String result
	 * @return String with all characters from Reader
	 * @exception IOException
	 */
	public String readToEom (DataInputStream rdr, int eom) throws IOException 
	{
		debug ("readToEom ()");
		
		StringWriter result=new StringWriter (4096);
		
		int c;
		int count = 0;
		
		while (0 <= (c = rdr.read()) && c != eom) 
		{			
			count++;
			
			if (c == '\r')
			{
				debug ("CR return is found at offset " + count);
			}	
			
			result.write(c);			
		}
		
		debug ("Received: " + result.toString ());
				
		return result.toString();
	}		
}
