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

import java.net.*;
import java.io.*;

import edu.cmu.cs.in.base.HoopRoot;
import edu.cmu.cs.in.base.io.HoopStreamedSocket;

/**
*
*/
public class HoopServerThread extends Thread
{  
	private HoopSocketServerBase	server	 =null;
	private HoopStreamedSocket		helper	=null;
	private Socket           		socket	 =null;
	private int              		ID		 =-1;
	private DataInputStream  		streamIn =null;
	private PrintWriter				outWriter=null;
	private Boolean					threadStopped=false;
	
	/**
	 *
	 */	
	public HoopServerThread (HoopSocketServerBase _server, Socket _socket)
	{  
		super();
		
		debug ("HoopServerThread ()");
		
		server = _server;
		socket = _socket;
		ID     = socket.getPort();
		
		helper=new HoopStreamedSocket ();
	}
	/**
	 *
	 */
	private void debug (String aMessage)
	{
		HoopRoot.debug ("HoopServerThread",aMessage);
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
