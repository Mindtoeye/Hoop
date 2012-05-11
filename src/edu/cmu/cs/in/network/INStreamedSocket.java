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

public class INStreamedSocket extends INBase implements Runnable
{	
	private String data="";
	private PrintWriter outWriter=null;
	private Socket clientSocket=null;
	private BufferedReader in = null;
	private int socketTimeout=2000; // Just two seconds, we want to keep it tight
	private INMessageReceiver receiver;
	private Boolean threadRunning=false;
	
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
		
		InputStream is=null;
		
		if (clientSocket==null)
		{
			outWriter=null; // Reset
			in=null; // Reset
					
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
		
		debug ("Sent: " + aMessage);
		
		data=convertStreamToString (is);
				
		return data;
	}
	/**
	 * 
	 */
	public Boolean sendAndKeepOpen (String aHost,
									int aPort,
									String aMessage,
									INMessageReceiver aReceiver)
	{
		debug ("sendAndKeepOpen ("+aHost+","+aPort+")");
						
		receiver=aReceiver;
		
		if (clientSocket==null)
		{
			debug ("Creating connection ...");
			
			close (); // Or a reset. Same thing
			
			try
			{
				debug ("Creating socket ...");
								
				SocketAddress sockaddr = new InetSocketAddress(aHost,aPort);
				clientSocket=new Socket();
				clientSocket.connect (sockaddr,socketTimeout);
								
				debug ("Created socket, creating printwriter ...");
				
				outWriter=new PrintWriter (clientSocket.getOutputStream(),true);
								
				debug ("Created socket, connecting input stream ...");
				
				in=new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));				
			} 
			catch (UnknownHostException e) 
			{
				debug ("Unknown host: " + aHost);
				
				close ();
				
				if (receiver!=null)
					receiver.handleConnectionClosed();	
				
				return (false);
			} 
			catch (SecurityException e)
			{
				debug ("A security exception occurred while connecting to the remote host");
				
				close ();
				
				if (receiver!=null)
					receiver.handleConnectionClosed();	
				
				return (false);				
			}
			catch  (IOException e) 
			{
				debug ("No I/O or connection timeout");
				
				close ();
				
				if (receiver!=null)
					receiver.handleConnectionClosed();	
				
				return (false);
			}
								
			// Create the thread supplying it with the runnable object
			Thread thread=new Thread(this);

			threadRunning=true;
			
			// Start the thread
			thread.start();			
		}

		debug ("Connection open, sending ...");
		
		if (outWriter!=null)
		{
			outWriter.print (aMessage+"\0");
			outWriter.flush();
			debug ("Sent " +(aMessage.length()+1)+" characters");
		}
		else
		{
			debug ("Internal error: outwriter is null");
			return (false);
		}
							
		return (true);
	}	
	/**
	 * 
	 */
	/*
	public String receiveFromOpenSocket ()
	{
		if (is!=null)
		{
			return (convertStreamToString (is));
		}
		
		return (null);
	}
	*/
	/**
	 * 
	 */
	public void close ()
	{
		debug ("close ()");
		
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
		}
		
		if (in!=null)
		{
			try 
			{
				in.close();
			} 
			catch (IOException e) 
			{
				debug ("Error closing input stream");
				e.printStackTrace();
			}
		}
		
		outWriter=null;
		in=null;
		clientSocket=null;	
		threadRunning=false;		
	}	
	/**Read the characters from a Reader into a String. Reads until receives the
	 * given end-of-message character, which it consumes without returning.
	 * 
	 * @param rdr
	 *            Reader to read; should be BufferedReader or equivalent for
	 *            efficiency
	 * @param eom
	 *            end-of-message character; not returned with String result
	 * @return String with all characters from Reader
	 * @exception IOException
	 */
	private String readToEom(Reader rdr, int eom) throws IOException 
	{
		debug ("readToEom (Reader rdr, int eom)");
		
		StringWriter result = new StringWriter(4096);
		int c;
		int count = 0;
		
		while (0<=(c=rdr.read()) && (c!=eom)) 
		{
			count++;
			
			if (c == '\r')
			{
				debug ("CR return is found at offset " + count);
			}
			
			//debug ("C: " + c);
			
			result.write(c);
		}
		
		if (count==0)
			return (null);
			
		return result.toString();
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
		debug ("readToEom (DataInputStream rdr, int eom)");
		
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
		
		if (count==0)
			return (null);
				
		return result.toString();
	}
	/**
	 * 
	 */
	@Override
	public void run() 
	{
		debug ("run ()");
						
		while (threadRunning==true)
		{
			try 
			{
				//in.ready();
				data=readToEom (in,0);
			} 
			catch (IOException e) 
			{
				//e.printStackTrace();

				debug ("Error reading data, most likely connection reset or closed");
				
				close ();
				
				if (receiver!=null)
					receiver.handleConnectionClosed();			

				threadRunning=false;	
				
				return;				
			}
			
			if (data!=null)
			{
				if (receiver!=null)
				{
					if (data.isEmpty()==true)
					{
						debug ("Internal error: empty string received!");
						close ();
					}
					else
						receiver.handleIncomingData(data);
				}					
			}			
			else
			{
				debug ("Received null data, probably closed the socket");
																	
				if (receiver!=null)
					receiver.handleConnectionClosed();
				
				close ();
								
				return;
			}
		}		
	}		
}
