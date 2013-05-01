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

package edu.cmu.cs.in;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import edu.cmu.cs.in.base.HoopRoot;

/**
 * http://www.rbgrn.net/content/43-java-single-application-instance
 */
public class HoopApplicationManager extends HoopRoot
{
    private static HoopApplicationInstanceListener subListener;

    /** Randomly chosen, but static, high socket number */
    public static final int SINGLE_INSTANCE_NETWORK_SOCKET = 44331;

    /** Must end with newline */
    public static final String SINGLE_INSTANCE_SHARED_KEY = "$$NewInstance$$\n";

	/**
	 *
	 */
	public HoopApplicationManager () 
	{
		setClassName ("HoopApplicationManager");
		debug ("HoopApplicationManager ()");		
	}    
    /**
     * Registers this instance of the application.
     *
     * @return true if first instance, false if not.
     */
    public static boolean registerInstance() 
    {
        // returnValueOnError should be true if lenient (allows app to run on network error) or false if strict.
        boolean returnValueOnError = true;
        
        // try to open network socket
        // if success, listen to socket for new instance message, return true
        // if unable to open, connect to existing and send new instance message, return false
        
        try 
        {
            final ServerSocket socket = new ServerSocket(SINGLE_INSTANCE_NETWORK_SOCKET, 10, InetAddress.getLocalHost());
            
            HoopRoot.debug("HoopApplicationManager","Listening for application instances on socket " + SINGLE_INSTANCE_NETWORK_SOCKET);
            
            Thread instanceListenerThread = new Thread(new Runnable() 
            {
                public void run() 
                {
                    boolean socketClosed = false;
                    
                    while (!socketClosed) 
                    {
                        if (socket.isClosed()) 
                        {
                            socketClosed = true;
                        } 
                        else 
                        {                           
                        	try 
                            {
                                Socket client = socket.accept();
                                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                                String message = in.readLine();
                                
                                if (SINGLE_INSTANCE_SHARED_KEY.trim().equals(message.trim())) 
                                {
                                	HoopRoot.debug("HoopApplicationManager","Shared key matched - new application instance found");
                                    //fireNewInstance();
                                }
                                in.close();
                                client.close();
                            } catch (IOException e) 
                            {
                                socketClosed = true;
                            }
                        }
                    }
                }
            });
            instanceListenerThread.start();
            // listen
        } catch (UnknownHostException e) 
        {
            //log.error(e.getMessage(), e);
            return returnValueOnError;
        } 
        catch (IOException e) 
        {
        	HoopRoot.debug("HoopApplicationManager","Port is already taken.  Notifying first instance.");
            
            try 
            {
                Socket clientSocket = new Socket(InetAddress.getLocalHost(), SINGLE_INSTANCE_NETWORK_SOCKET);
                OutputStream out = clientSocket.getOutputStream();
                out.write(SINGLE_INSTANCE_SHARED_KEY.getBytes());
                out.close();
                clientSocket.close();
                HoopRoot.debug("HoopApplicationManager","Successfully notified first instance.");
                return false;
            } 
            catch (UnknownHostException e1) 
            {
                //log.error(e.getMessage(), e);
                return returnValueOnError;
            } 
            catch (IOException e1) 
            {
                //log.error("Error connecting to local port for single instance notification");
                //log.error(e1.getMessage(), e1);
                return returnValueOnError;
            }

        }
        return true;
    }

    public static void setApplicationInstanceListener(HoopApplicationInstanceListener listener) 
    {
        subListener = listener;
    }

    private static void fireNewInstance() 
    {
      if (subListener != null) 
      {
        subListener.newInstanceCreated();
      }
  	}
}
