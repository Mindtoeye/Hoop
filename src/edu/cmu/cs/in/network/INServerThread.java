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

import java.net.*;
import java.io.*;

import edu.cmu.cs.in.base.INFeatureMatrixBase;

/**
*
*/
public class INServerThread extends Thread
{  
	private INSocketServerBase	server	 =null;
	private INStreamedSocket		helper	=null;
	private Socket           		socket	 =null;
	private int              		ID		 =-1;
	private DataInputStream  		streamIn =null;
	private PrintWriter				outWriter=null;
	private Boolean					threadStopped=false;
	
	/**
	 *
	 */	
	public INServerThread (INSocketServerBase _server, Socket _socket)
	{  
		super();
		
		debug ("INServerThread ()");
		
		server = _server;
		socket = _socket;
		ID     = socket.getPort();
		
		helper=new INStreamedSocket ();
	}
	/**
	 *
	 */
	private void debug (String aMessage)
	{
		INFeatureMatrixBase.debug ("INServerThread",aMessage);
	}
	/**
	 *
	 */	
	public void send(String msg)
	{   
		debug ("send () >>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ");
		debug (msg);
						
		if (outWriter==null)
		{
			debug ("Error: no open output stream available");
			return;
		}
			
		outWriter.print (msg);
		
		if (outWriter.checkError()==true)
		{
			debug ("Error printing message to socket");
			return;
		}
		
		outWriter.write('\0');
		
		if (outWriter.checkError()==true)
		{
			debug ("Error writing message to socket");
			return;
		}		
		
		outWriter.flush();
		
		if (outWriter.checkError()==true)
		{
			debug ("Error flushing socket");
			return;
		}		
	}
	/**
	 *
	 */
	public void stopThread ()
	{
		debug ("stopThread ()");
		threadStopped=true;
	}
	/**
	 *
	 */	
	public int getID()
	{  
		return ID;
	}
	/**
	 *
	 */	
	public void run()
	{  
		debug ("Server Thread " + ID + " running.");
		
		while (true && (threadStopped==false))
		{  
			try
			{  				
				server.handle(ID,helper.readToEom (streamIn,0));
			}
			catch(IOException ioe)
			{  
				debug (ID + " ERROR reading: " + ioe.getMessage());
	            stopThread ();				
	            server.remove(ID);
			}
		}
	}
	/**
	 *
	 */	
	public void open() throws IOException
	{  
		debug ("open ()");
				
		streamIn = new DataInputStream (new BufferedInputStream(socket.getInputStream()));
		
		outWriter=new PrintWriter (new OutputStreamWriter (socket.getOutputStream(),"UTF-8"));
	}
	/**
	 *
	 */	
	public void close() throws IOException
	{  
		debug ("close ()");
				
		if (socket != null) 
			socket.close();
		
		if (streamIn != null)  
			streamIn.close();

		if (outWriter!=null)
			outWriter.close();
	}
}
