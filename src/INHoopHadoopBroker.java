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

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import edu.cmu.cs.in.base.INBase;
import edu.cmu.cs.in.base.INLink;
import edu.cmu.cs.in.network.INSocketServerBase;
import edu.cmu.cs.in.network.INHoopMonitorConnection;
import edu.cmu.cs.in.hadoop.INHadoopReporter;

public class INHoopHadoopBroker extends INSocketServerBase
{	
	public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";
	//public static FileSystem hdfs=null;
       
	private ArrayList <INHoopMonitorConnection> monitorList=null;
	
	/**
	 *
	 */
    public INHoopHadoopBroker () 
    {
		setClassName ("INHoopHadoopBroker");
		debug ("INHoopHadoopBroker ()");
		INBase.debug ("INHoop","Running on: "+INHadoopReporter.getMachineName ());
		
		monitorList=new ArrayList<INHoopMonitorConnection> ();
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
    	INBase.debug ("INHoopHadoopBroker",aMessage);
    }
    /**
	 *
	 */
    private static Boolean parseArgs (String args [])
    {
    	dbg ("parseArgs ()");
    
    	/*
    	if (args.length<3)
    	{    		
    		return (false);
    	}
    	*/
    	
        for (int i = 0; i < args.length; i++)
        {        	
        	if (args [i].compareTo ("-casefold")==0)
        	{
        		if (args [i+1].equals("yes")==true)
        			INLink.casefold=true;
        		else
        			INLink.casefold=false;
        	}
        	
     	
        }    	
        
        return (true);
    }
	/**
	 * 
	 */
    private INHoopMonitorConnection findMonitor (int ID)
    {
    	debug ("findMonitor ("+ID+")");

    	for (int i=0;i<monitorList.size();i++)
    	{
    		INHoopMonitorConnection test=monitorList.get(i);
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
    	
    	INHoopMonitorConnection test=findMonitor (ID);
    	if (test==null)
    	{
    		test=new INHoopMonitorConnection ();
    		test.setID(ID);
    		monitorList.add(test);
    	}
    }
	/**
	 * 
	 */
    private void removeMonitor (int ID)
    {
    	debug ("removeMonitor ()");
    	
    	INHoopMonitorConnection test=findMonitor (ID);
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
    		INHoopMonitorConnection test=monitorList.get(i);
    		sendClient (test.getID(),rawXML);
    	}    	
    }
	/**
	 * 
	 */
	 public Boolean fromXML (int ID,Element root,String rawXML)
	 {
		 debug ("fromXML ()");
	  	  
		 //"<register class" or <unregister class
		 
		 if ((root.getNodeName().equals("register")==true) || (root.getNodeName().equals("unregister")==true))
		 {
			 debug ("Processing register or unregister message ...");
			 NamedNodeMap attrs = root.getAttributes();
	        
			 for(int i = 0 ; i<attrs.getLength() ; i++) 
			 {
				 Attr attribute = (Attr)attrs.item(i);
	          	          
				 if (attribute.getName().toLowerCase().equals("class")==true)
				 {
					 // send on, don't even look at it
					 debug ("Detected a register or unregister message from a hadoop reporter, sending on to monitor ...");
	        	  					 					 
					 sendAllMonitors (rawXML);
					 
					 return (true); 
				 }
				 
				 if (attribute.getName().toLowerCase().equals("type")==true)
				 {
					 if (attribute.getValue().toLowerCase().equals("monitor")==true)
					 {
						 if (root.getNodeName().equals("register")==true)							 
							 addMonitor (ID);
						 
						 if (root.getNodeName().equals("unregister")==true)							 
							 removeMonitor (ID);						 
					 }	 
				 }
			 }
		 }  
		 
		 return (true);
	 }    
    /**
	 *
	 */
    public static void main(String args[]) throws Exception
	{
    	// run the INLink constructor; We need this to have a global settings registry    	
    	@SuppressWarnings("unused")
		INLink link = new INLink(); 
    	
    	dbg ("main ()");
    	    	    	    	
    	if (parseArgs (args)==false)
    	{
    		usage ();
    		return;
    	}
    	        
        dbg ("Starting system ...");
        
        INHoopHadoopBroker broker=new INHoopHadoopBroker ();
        broker.runServer ();
	}
}
