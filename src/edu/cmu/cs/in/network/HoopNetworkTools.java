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

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import edu.cmu.cs.in.base.HoopRoot;

/**
 * 
 */
public class HoopNetworkTools extends HoopRoot
{		
	/**
	*
	*/	
	public HoopNetworkTools ()
	{  
    	setClassName ("HoopNetworkTools");
    	//debug ("CTATNetworkTools ()");

	}
	/**
	*
	*/	
	public void showAddresses ()
	{
		debug ("showAddresses ()");
		
		Enumeration <NetworkInterface>netInterfaces=null;
		
		try 
		{
			debug ("Obtaining network interfaces ...");
			netInterfaces = NetworkInterface.getNetworkInterfaces();
		} 
		catch (SocketException e1) 
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		InetAddress ip =null;
		
		debug ("Finding proper host info in network interfaces ...");
		
		Boolean found=false;
		
		while ((netInterfaces.hasMoreElements()) && (found==false))
		{
			NetworkInterface ni=(NetworkInterface) netInterfaces.nextElement();
			
			try 
			{
				if ((ni.isVirtual()==false) && (ni.isUp()==true))
				{
					debug ("Examining interface: "+ni.getName());
				
					Enumeration <InetAddress>ips=ni.getInetAddresses();
				
					while ((ips.hasMoreElements()) && (found==false))
					{
						ip=(InetAddress) ips.nextElement();
					
						debug ("isSiteLocalAddress: " + ip.isSiteLocalAddress()+", isLoopbackAddress: "+ip.isLoopbackAddress()+","+ip.getHostAddress());				
					}
				}
			} 
			catch (SocketException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}			
	}
}
