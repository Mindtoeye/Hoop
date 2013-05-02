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

/*
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.*;
*/

import java.util.ArrayList;

/*
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
*/

import org.jdom.Element;

import edu.cmu.cs.in.base.HoopRoot;
import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.network.HoopNetworkTools;
import edu.cmu.cs.in.network.HoopSocketServerBase;
import edu.cmu.cs.in.network.HoopMonitorConnection;
import edu.cmu.cs.in.hadoop.HoopHadoopReporter;

/**
 * 
 */
public class HoopBroker extends HoopSocketServerBase
{	
	public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";
	private ArrayList <HoopMonitorConnection> monitorList=null;
	private Boolean showConfig=false;
	
	/**
	 *
	 */
    public HoopBroker () 
    {
		setClassName ("HoopBroker");
		debug ("HoopBroker ()");
		HoopRoot.debug ("Hoop","Running on: "+HoopHadoopReporter.getMachineName ());
		
		monitorList=new ArrayList<HoopMonitorConnection> ();
    }
    /**
	 *
	 */    
	public Boolean getShowConfig() 
	{
		return showConfig;
	}
    /**
	 *
	 */	
	public void setShowConfig(Boolean showConfig) 
	{
		this.showConfig = showConfig;
	}    
    /**
	 *
	 */
    private static void usage ()
    {
    	System.out.println ("Usage: choose either one of:");
    	

    }
    /**
	 *
	 */    
    private static void dbg (String aMessage)
    {
    	HoopRoot.debug ("HoopHadoopBroker",aMessage);
    }
    /**
	 *
	 */
    public Boolean parseArgs (String args [])
    {
    	dbg ("parseArgs ()");
        	
        for (int i = 0; i < args.length; i++)
        {        	
        	if (args [i].compareTo ("-port")==0)
        	{
        		this.setLocalPort(Integer.parseInt(args [i+1]));
        	}        	
        	
        	if (args [i].compareTo("-showconfig")==0)
        	{
        		this.setShowConfig(true);
        		
        		HoopNetworkTools nTools=new HoopNetworkTools ();
        		nTools.showAddresses();
        	}
        }    	
        
        return (true);
    }
	/**
	 * 
	 */
    private HoopMonitorConnection findMonitor (int ID)
    {
    	debug ("findMonitor ("+ID+")");

    	for (int i=0;i<monitorList.size();i++)
    	{
    		HoopMonitorConnection test=monitorList.get(i);
    		if (test.getID ()==ID)
    			return (test);
    	}
    	
    	return (null);
    }
	/**
	 * 
	 */
    private void addMonitor (int ID)
    {
    	debug ("addMonitor ()");
    	
    	HoopMonitorConnection test=findMonitor (ID);
    	if (test==null)
    	{
    		test=new HoopMonitorConnection ();
    		test.setID(ID);
    		monitorList.add(test);
    		sendClient (ID,"<ack type=\"monitor\" re=\"register\" />");
    	}
    }
	/**
	 * 
	 */
    private void removeMonitor (int ID)
    {
    	debug ("removeMonitor ()");
    	
    	HoopMonitorConnection test=findMonitor (ID);
    	if (test!=null)
    	{
    		monitorList.remove(test);
    	}
    }
	/**
	 * 
	 */
    private void sendAllMonitors (String rawXML)
    {
    	debug ("sendAllMonitors ()");
    	
    	for (int i=0;i<monitorList.size();i++)
    	{
    		HoopMonitorConnection test=monitorList.get(i);
    		sendClientRaw (test.getID(),rawXML);
    	}    	
    }
	/**
	 * <register class" or <unregister class
	 */
    @Override
    public Boolean fromStreamedXML (int ID,Element root,String rawXML)
    {
    	debug ("fromStreamedXML ("+root.getName()+")");
		 
    	if ((root.getName().equals("register")==true) || (root.getName().equals("unregister")==true))
    	{
    		debug ("Processing register or unregister message ...");

    		if (root.getAttributeValue("type").equalsIgnoreCase("class")==true)
    		{
				// send on, don't even look at it
				debug ("Detected a register or unregister message from a hadoop reporter, sending on to monitor ...");

				sendAllMonitors (rawXML);
				 
				return (true);     			
    		}
    		
    		if (root.getAttributeValue("type").equalsIgnoreCase("monitor")==true)
    		{
				if (root.getName().equals("register")==true)							 
					addMonitor (ID);
				 
				if (root.getName().equals("unregister")==true)							 
					removeMonitor (ID);	    			
    		}    		
    	}  
		 
    	return (true);
    }
    /**
	 *
	 */
    public static void main(String args[]) throws Exception
	{
    	// run the HoopLink constructor; We need this to have a global settings registry    	
    	@SuppressWarnings("unused")
		HoopLink link = new HoopLink(); 
    	    	    	    	    	    	        
        dbg ("Starting system ...");
        
        HoopBroker broker=new HoopBroker ();
        
    	if (broker.parseArgs (args)==false)
    	{
    		usage ();
    		return;
    	}        
        
        broker.runServer ();
	}
}
