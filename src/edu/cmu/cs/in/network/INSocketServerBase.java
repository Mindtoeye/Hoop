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

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

//import org.w3c.dom.Document;
import org.w3c.dom.Element;

import edu.cmu.cs.in.base.INLink;
import edu.cmu.cs.in.base.INXMLBase;
import edu.cmu.cs.in.hadoop.INHadoopReporter;

/**
*
*/
public class INSocketServerBase extends INHadoopReporter  implements Runnable
{			
	private INServerThread   clients []=new INServerThread [250]; // MAKE THIS DYNAMIC!!
	private ServerSocket     server = null;
	private Thread           thread = null;
	private int              clientCount = 0;
	private int              localPort=8080;
	private INMessageHandler messageHandler=null;

	/**
	*
	*/
	public INSocketServerBase ()
	{
    	setClassName ("INSocketServerBase");
    	debug ("INSocketServerBase ()");    	
	}
	/**
	*
	*/
	public void setMessageHandler(INMessageHandler aHandler) 
	{
		messageHandler=aHandler;
	}	
	/**
	*
	*/	
	public INMessageHandler getMessageHandler() 
	{
		return messageHandler;
	}
	/**
	*
	*/	
	public void setLocalPort (int localPort) 
	{
		this.localPort = localPort;
	}
	/**
	*
	*/	
	public int getLocalPort() 
	{
		return localPort;
	}
	/**
	*
	*/
	public void runServer ()
	{
		debug ("runServer ()");
		
		//setLocalPort (getLocalPort()); // ??? what the hell is this?

		try	     
		{  
			debug ("Binding to port " + getLocalPort() + ", please wait  ...");
			
	        server = new ServerSocket (getLocalPort());
	        	        	       
	        start ();
	        
	        debug ("Server started: " + server);
		}
		catch(IOException ioe)
		{  
			debug ("Can not bind to port " + getLocalPort() + ": " + ioe.getMessage()); 
		}		
	}
	/**
	*
	*/	
	public void run()
	{  
		debug ("run ()");
		
		while (thread != null)
		{  
			try
			{  
				debug ("Waiting for a client ...");
				
				addThread (server.accept()); 
			}
			catch(IOException ioe)
			{  
				debug ("Server accept error: " + ioe); 
				stop(); 
			}
		}
	}
	/**
	*
	*/	   
	public void start()
	{ 
		debug ("start ()");
		
		if (thread == null)
		{  
			thread = new Thread(this); 
			thread.start();
		} 
	}
	/**
	*
	*/	   
	public void stop()   
	{ 
		debug ("stop ()");
		
		if (thread != null)
		{  
			INServerThread stopper=(INServerThread) thread;
			stopper.stopThread (); 
			thread = null;
		}
	}
	/**
	*
	*/	   
	private int findClient(int ID)
	{  
		debug ("findClient ()");
		
		for (int i = 0; i < clientCount; i++)
		{
			if (clients[i].getID()==ID)
				return i;
		}
		   
		return -1;
	}
	/**
	*
	*/
	public void sendAllSockets (String message)
	{
		debug ("sendAllSockets ()");
		
		for (int i = 0; i < clientCount; i++)
		{
			clients [i].send("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+message);		
		}						
	}
	/**
	*
	*/	
	public void sendClient (int ID,String message)
	{
		debug ("sendClient ("+ID+")");
		
		if (findClient(ID)==-1)
		{
			debug ("Error: unable to find socket thread with id: " + ID);
			return;
		}
				
		clients [findClient(ID)].send("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+message);
	}
	/**
	 * 
	 */
	public void sendAllMonitors (String aMessage)
	{
		// to be implemented in child class, called by INServiceChecker
	}
	/**
	*
	*/	   
	public synchronized void handle(int ID, String input)
	{  
		debug ("handle () <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		
		if (input.isEmpty()==true)
		{
			debug ("Received empty string, removing client ...");
			remove (ID);
			return;
		}
		
		if (input.equals("")==true)
		{
			debug ("Received empty string, removing client ...");
			remove (ID);
			return;
		}
		
		if (input.trim().length()==0)
		{
			debug ("Received whitespace string, removing client ...");
			remove (ID);
			return;			
		}
		
		if (input.indexOf("<policy-file-request/>")!=-1)
		{
			sendClient (ID,INLink.crossDomainPolicy);
			return;
		}
		
		 if (messageHandler!=null)
		 {
			 debug ("Calling installed message handler ...");
			 messageHandler.handleIncomingData (ID,input);
		 }
		
		INXMLBase helper=new INXMLBase ();
				
		Element root=helper.fromXMLString (input);
		  
		if (root!=null)
			fromXML (ID,root);
		else
			debug ("Error, unable to parse incoming data as XML");
	}
	/**
	 * 
	 */
	 public Boolean fromXML (int ID,Element root)
	 {
		 debug ("fromXML ()");
	  	  
		 if (messageHandler!=null)
		 {
			 debug ("Calling installed message handler ...");
			 messageHandler.handleIncomingXML (ID,root);
		 }
		 
		 // Or override this
		 
		 return (true);
	 }
	 /**
	  *
	  */	   
	 public void shutdownService (int ID)
	 {  
		 debug ("shutdownService ("+ID+")");
		 
		 // Implement in child class!!
	 }
	 /**
	  *
	  */	   
	 public synchronized void remove(int ID)
	 {  
		 debug ("remove ("+ID+")");
		
		 shutdownService (ID);
		
		 int pos = findClient(ID);
		
		 if (pos >= 0)
		 {  
			 INServerThread toTerminate = clients [pos];
			 debug ("Removing client thread " + ID + " at " + pos);
			
			 if (pos < clientCount-1)
			 {
				 for (int i = pos+1; i < clientCount; i++)
					 clients[i-1] = clients[i];
			 }
			 
			 clientCount--;
	         
			 try
			 {  
				 toTerminate.close(); 
			 }
			 catch(IOException ioe)
			 {  
				 debug ("Error closing thread: " + ioe); 
			 }
	         
			 toTerminate.stopThread(); 
		 }
	 }
	 /**
	  *
	  */	   	
	 private void addThread(Socket socket)
	 {  
		 debug ("addThread ()");
		
		 if (clientCount < clients.length)
		 {  
			debug ("Client accepted: " + socket);
			
			clients[clientCount] = new INServerThread (this, socket);
			
			try
			{  
				clients[clientCount].open(); 
	            clients[clientCount].start();  
	            clientCount++; 
			}
			catch(IOException ioe)
			{  
				debug ("Error opening thread: " + ioe); 
			} 
		}
		else
	         debug ("Client refused: maximum " + clients.length + " reached.");
	}	
}
